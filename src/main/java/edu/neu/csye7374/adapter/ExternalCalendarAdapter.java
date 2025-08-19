package edu.neu.csye7374.adapter;

import edu.neu.csye7374.event.EventAPI;
import edu.neu.csye7374.factory.LectureEventFactory;
import edu.neu.csye7374.factory.SeminarEventFactory;
import edu.neu.csye7374.factory.WorkshopEventFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Adapter for integrating external calendar systems with the campus event system.
 * This adapter bridges external calendar API formats with our Event system.
 *
 * External Calendar Format:
 * - Uses JSON-like structure with specific field names
 * - Uses various timestamp formats (RFC3339, ISO, etc.)
 * - Has nested objects for location, attendees, etc.
 * - Includes external system-specific metadata we may not need
 */
public class ExternalCalendarAdapter implements ExternalEventAdapter {

    private ExternalEventSource externalCalendarSource;
    private String lastSyncTime;
    private String calendarId;
    private LectureEventFactory lectureFactory;
    private SeminarEventFactory seminarFactory;
    private WorkshopEventFactory workshopFactory;

    public ExternalCalendarAdapter(ExternalEventSource externalCalendarSource, String calendarId) {
        this.externalCalendarSource = externalCalendarSource;
        this.calendarId = calendarId;
        this.lastSyncTime = "Never";
        this.lectureFactory = new LectureEventFactory();
        this.seminarFactory = new SeminarEventFactory();
        this.workshopFactory = new WorkshopEventFactory();
    }

    @Override
    public List<EventAPI> importEvents() {
        List<EventAPI> adaptedEvents = new ArrayList<>();

        try {
            if (!externalCalendarSource.isConnected() || !externalCalendarSource.isAuthenticated()) {
                System.out.println("External calendar source is not connected or authenticated");
                return adaptedEvents;
            }

            List<Map<String, Object>> rawEvents = externalCalendarSource.getRawEventData();

            for (Map<String, Object> rawEvent : rawEvents) {
                EventAPI adaptedEvent = adaptExternalCalendarEvent(rawEvent);
                if (adaptedEvent != null) {
                    adaptedEvents.add(adaptedEvent);
                }
            }

            this.lastSyncTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            System.out.println("Successfully imported " + adaptedEvents.size() + " events from external calendar");

        } catch (Exception e) {
            System.err.println("Error importing from external calendar: " + e.getMessage());
        }

        return adaptedEvents;
    }

    // Adapt external calendar event format to our Event format
    @SuppressWarnings("unchecked")
    private EventAPI adaptExternalCalendarEvent(Map<String, Object> rawEvent) {
        try {
            // External calendar systems use various field names
            String eventId = adaptExternalEventId(rawEvent);
            String title = adaptExternalTitle(rawEvent);
            String description = adaptExternalDescription(rawEvent);
            LocalDate date = adaptExternalDate(rawEvent);
            String location = adaptExternalLocation(rawEvent);
            int capacity = adaptExternalCapacity(rawEvent);
            String eventType = determineEventTypeFromExternal(rawEvent);

            // Use appropriate factory based on event type
            switch (eventType.toLowerCase()) {
                case "lecture":
                case "class":
                case "course":
                    return lectureFactory.createEvent(eventId, title, description, date, location, capacity);

                case "seminar":
                case "presentation":
                case "meeting":
                    return seminarFactory.createEvent(eventId, title, description, date, location, capacity);

                case "workshop":
                case "training":
                case "hands-on":
                    return workshopFactory.createEvent(eventId, title, description, date, location, capacity);

                default:
                    // Default to seminar for external calendar events
                    return seminarFactory.createEvent(eventId, title, description, date, location, capacity);
            }

        } catch (Exception e) {
            System.err.println("Error adapting external calendar event: " + e.getMessage());
            return null;
        }
    }

    private String adaptExternalEventId(Map<String, Object> rawEvent) {
        Object id = rawEvent.get("id");
        return id != null ? "EXT_" + id.toString() : "EXT_" + System.currentTimeMillis();
    }

    private String adaptExternalTitle(Map<String, Object> rawEvent) {
        // Try common field names used by external systems
        Object summary = rawEvent.get("summary");
        if (summary == null) summary = rawEvent.get("title");
        if (summary == null) summary = rawEvent.get("name");
        return summary != null ? summary.toString() : "Untitled Event";
    }

    private String adaptExternalDescription(Map<String, Object> rawEvent) {
        Object description = rawEvent.get("description");
        if (description == null) description = rawEvent.get("details");
        if (description == null) description = rawEvent.get("notes");
        return description != null ? description.toString() : "No description available";
    }

    @SuppressWarnings("unchecked")
    private LocalDate adaptExternalDate(Map<String, Object> rawEvent) {
        try {
            // External calendars may have "start" object with "date" or "dateTime"
            Object startObj = rawEvent.get("start");
            if (startObj instanceof Map) {
                Map<String, Object> start = (Map<String, Object>) startObj;

                // Try "date" field first (all-day events)
                Object dateObj = start.get("date");
                if (dateObj != null) {
                    return LocalDate.parse(dateObj.toString());
                }

                // Try "dateTime" field (timed events)
                Object dateTimeObj = start.get("dateTime");
                if (dateTimeObj != null) {
                    String dateTimeStr = dateTimeObj.toString();
                    // Parse various timestamp formats
                    if (dateTimeStr.length() >= 10) {
                        return LocalDate.parse(dateTimeStr.substring(0, 10));
                    }
                }
            }

            // Try direct date fields
            Object directDate = rawEvent.get("date");
            if (directDate != null) {
                String dateStr = directDate.toString();
                if (dateStr.length() >= 10) {
                    return LocalDate.parse(dateStr.substring(0, 10));
                }
            }

        } catch (Exception e) {
            System.err.println("Error parsing external calendar date: " + e.getMessage());
        }

        return LocalDate.now().plusDays(1); // Default fallback
    }

    private String adaptExternalLocation(Map<String, Object> rawEvent) {
        Object location = rawEvent.get("location");
        if (location == null) location = rawEvent.get("venue");
        if (location == null) location = rawEvent.get("place");
        return location != null ? location.toString() : "Online";
    }

    @SuppressWarnings("unchecked")
    private int adaptExternalCapacity(Map<String, Object> rawEvent) {
        try {
            // External calendars may not have direct capacity field
            // Try to infer from attendees list or event description

            Object attendeesObj = rawEvent.get("attendees");
            if (attendeesObj instanceof List) {
                List<Map<String, Object>> attendees = (List<Map<String, Object>>) attendeesObj;
                // If there are attendees, use that as a base capacity
                return Math.max(attendees.size() * 2, 20); // Double the current attendees as capacity
            }

            // Try to extract capacity from description
            Object descObj = rawEvent.get("description");
            if (descObj != null) {
                String description = descObj.toString().toLowerCase();
                if (description.contains("capacity:")) {
                    String[] parts = description.split("capacity:");
                    if (parts.length > 1) {
                        String capacityPart = parts[1].trim().split("\\s+")[0];
                        try {
                            return Integer.parseInt(capacityPart);
                        } catch (NumberFormatException e) {
                            // Ignore and fall through to default
                        }
                    }
                }
                if (description.contains("max:")) {
                    String[] parts = description.split("max:");
                    if (parts.length > 1) {
                        String capacityPart = parts[1].trim().split("\\s+")[0];
                        try {
                            return Integer.parseInt(capacityPart);
                        } catch (NumberFormatException e) {
                            // Ignore and fall through to default
                        }
                    }
                }
            }

            // Try direct capacity field
            Object capacityObj = rawEvent.get("capacity");
            if (capacityObj != null) {
                try {
                    return Integer.parseInt(capacityObj.toString());
                } catch (NumberFormatException e) {
                    // Ignore and fall through to default
                }
            }

        } catch (Exception e) {
            System.err.println("Error determining capacity from external calendar event: " + e.getMessage());
        }

        return 30; // Default capacity for external calendar events
    }

    private String determineEventTypeFromExternal(Map<String, Object> rawEvent) {
        // Analyze title and description to determine event type
        String title = adaptExternalTitle(rawEvent).toLowerCase();
        String description = adaptExternalDescription(rawEvent).toLowerCase();
        String combined = title + " " + description;

        if (combined.contains("lecture") || combined.contains("class") || combined.contains("course")) {
            return "lecture";
        } else if (combined.contains("workshop") || combined.contains("training") ||
                   combined.contains("hands-on") || combined.contains("lab")) {
            return "workshop";
        } else if (combined.contains("seminar") || combined.contains("presentation") ||
                   combined.contains("meeting") || combined.contains("talk")) {
            return "seminar";
        }

        // Default to seminar for external calendar events
        return "seminar";
    }

    @Override
    public String getSourceName() {
        return "External Calendar System (" + calendarId + ")";
    }

    @Override
    public boolean isSourceAvailable() {
        return externalCalendarSource != null &&
               externalCalendarSource.isConnected() &&
               externalCalendarSource.isAuthenticated();
    }

    @Override
    public String getLastSyncTime() {
        return lastSyncTime;
    }

    @Override
    public int syncEvents() {
        List<EventAPI> events = importEvents();
        return events.size();
    }

    /**
     * Import events for a specific date range
     */
    public List<EventAPI> importEventsForDateRange(LocalDate startDate, LocalDate endDate) {
        List<EventAPI> adaptedEvents = new ArrayList<>();

        try {
            if (!isSourceAvailable()) {
                System.out.println("External calendar source is not available");
                return adaptedEvents;
            }

            List<Map<String, Object>> rawEvents = externalCalendarSource.getEventsByDateRange(startDate, endDate);

            for (Map<String, Object> rawEvent : rawEvents) {
                EventAPI adaptedEvent = adaptExternalCalendarEvent(rawEvent);
                if (adaptedEvent != null) {
                    adaptedEvents.add(adaptedEvent);
                }
            }

            System.out.println("Successfully imported " + adaptedEvents.size() +
                             " events from external calendar for date range " + startDate + " to " + endDate);

        } catch (Exception e) {
            System.err.println("Error importing external calendar events for date range: " + e.getMessage());
        }

        return adaptedEvents;
    }
}