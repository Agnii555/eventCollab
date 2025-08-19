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
 * Adapter for integrating legacy calendar systems with the campus event system.
 * This adapter bridges the incompatible interface of old calendar systems
 * with our modern Event interface.
 * <p>
 * Legacy Calendar Format:
 * - Uses different field names (e.g., "event_name" instead of "title")
 * - Uses different date formats
 * - Has different capacity representations
 * - May have additional fields we don't need
 */
public class LegacyCalendarAdapter implements ExternalEventAdapter {

    private final ExternalEventSource legacySource;
    private String lastSyncTime;
    private final LectureEventFactory lectureFactory;
    private final SeminarEventFactory seminarFactory;
    private final WorkshopEventFactory workshopFactory;

    public LegacyCalendarAdapter(ExternalEventSource legacySource) {
        this.legacySource = legacySource;
        this.lastSyncTime = "Never";
        this.lectureFactory = new LectureEventFactory();
        this.seminarFactory = new SeminarEventFactory();
        this.workshopFactory = new WorkshopEventFactory();
    }

    @Override
    public List<EventAPI> importEvents() {
        List<EventAPI> adaptedEvents = new ArrayList<>();

        try {
            if (!legacySource.isConnected()) {
                System.out.println("Legacy calendar source is not connected");
                return adaptedEvents;
            }

            List<Map<String, Object>> rawEvents = legacySource.getRawEventData();

            for (Map<String, Object> rawEvent : rawEvents) {
                EventAPI adaptedEvent = adaptLegacyEvent(rawEvent);
                if (adaptedEvent != null) {
                    adaptedEvents.add(adaptedEvent);
                }
            }

            this.lastSyncTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            System.out.println("Successfully imported " + adaptedEvents.size() + " events from legacy calendar");

        } catch (Exception e) {
            System.err.println("Error importing from legacy calendar: " + e.getMessage());
        }

        return adaptedEvents;
    }

    /**
     * Adapt legacy event format to our Event format
     */
    private EventAPI adaptLegacyEvent(Map<String, Object> rawEvent) {
        try {
            // Legacy calendar uses different field names - we need to adapt them
            String eventId = generateEventId(rawEvent);
            String title = adaptTitle(rawEvent);
            String description = adaptDescription(rawEvent);
            LocalDate date = adaptDate(rawEvent);
            String location = adaptLocation(rawEvent);
            int capacity = adaptCapacity(rawEvent);
            String eventType = adaptEventType(rawEvent);

            // Use appropriate factory based on event type
            switch (eventType.toLowerCase()) {
                case "lecture":
                case "class":
                case "course":
                    return lectureFactory.createEvent(eventId, title, description, date, location, capacity);

                case "seminar":
                case "presentation":
                case "talk":
                    return seminarFactory.createEvent(eventId, title, description, date, location, capacity);

                case "workshop":
                case "lab":
                case "training":
                    return workshopFactory.createEvent(eventId, title, description, date, location, capacity);

                default:
                    // Default to lecture for unknown types
                    return lectureFactory.createEvent(eventId, title, description, date, location, capacity);
            }

        } catch (Exception e) {
            System.err.println("Error adapting legacy event: " + e.getMessage());
            return null;
        }
    }

    private String generateEventId(Map<String, Object> rawEvent) {
        // Legacy system might use "id", "event_id", or "legacy_id"
        Object id = rawEvent.get("id");
        if (id == null) id = rawEvent.get("event_id");
        if (id == null) id = rawEvent.get("legacy_id");

        if (id != null) {
            return "LEGACY_" + id;
        }

        // Generate ID from title and date if no ID exists
        return "LEGACY_" + System.currentTimeMillis();
    }

    private String adaptTitle(Map<String, Object> rawEvent) {
        // Legacy system might use "event_name", "title", or "name"
        Object title = rawEvent.get("event_name");
        if (title == null) title = rawEvent.get("title");
        if (title == null) title = rawEvent.get("name");

        return title != null ? title.toString() : "Untitled Event";
    }

    private String adaptDescription(Map<String, Object> rawEvent) {
        // Legacy system might use "desc", "description", or "details"
        Object desc = rawEvent.get("desc");
        if (desc == null) desc = rawEvent.get("description");
        if (desc == null) desc = rawEvent.get("details");

        return desc != null ? desc.toString() : "No description available";
    }

    private LocalDate adaptDate(Map<String, Object> rawEvent) {
        // Legacy system might use different date formats
        Object dateObj = rawEvent.get("event_date");
        if (dateObj == null) dateObj = rawEvent.get("date");
        if (dateObj == null) dateObj = rawEvent.get("scheduled_date");

        if (dateObj == null) {
            return LocalDate.now().plusDays(1); // Default to tomorrow
        }

        String dateStr = dateObj.toString();

        // Try different legacy date formats
        try {
            // Format: "MM/dd/yyyy"
            if (dateStr.contains("/")) {
                String[] parts = dateStr.split("/");
                if (parts.length == 3) {
                    int month = Integer.parseInt(parts[0]);
                    int day = Integer.parseInt(parts[1]);
                    int year = Integer.parseInt(parts[2]);
                    return LocalDate.of(year, month, day);
                }
            }

            // Format: "yyyy-MM-dd"
            if (dateStr.contains("-") && dateStr.length() == 10) {
                return LocalDate.parse(dateStr);
            }

            // Format: "dd-MM-yyyy"
            if (dateStr.contains("-") && dateStr.length() == 10) {
                String[] parts = dateStr.split("-");
                if (parts.length == 3) {
                    int day = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int year = Integer.parseInt(parts[2]);
                    return LocalDate.of(year, month, day);
                }
            }

        } catch (Exception e) {
            System.err.println("Error parsing legacy date format: " + dateStr);
        }

        return LocalDate.now().plusDays(1); // Default fallback
    }

    private String adaptLocation(Map<String, Object> rawEvent) {
        // Legacy system might use "venue", "location", or "place"
        Object location = rawEvent.get("venue");
        if (location == null) location = rawEvent.get("location");
        if (location == null) location = rawEvent.get("place");
        if (location == null) location = rawEvent.get("room");

        return location != null ? location.toString() : "TBD";
    }

    private int adaptCapacity(Map<String, Object> rawEvent) {
        // Legacy system might use "max_attendees", "capacity", or "limit"
        Object capacity = rawEvent.get("max_attendees");
        if (capacity == null) capacity = rawEvent.get("capacity");
        if (capacity == null) capacity = rawEvent.get("limit");
        if (capacity == null) capacity = rawEvent.get("max_participants");

        if (capacity != null) {
            try {
                return Integer.parseInt(capacity.toString());
            } catch (NumberFormatException e) {
                System.err.println("Error parsing capacity: " + capacity);
            }
        }

        return 50; // Default capacity
    }

    private String adaptEventType(Map<String, Object> rawEvent) {
        // Legacy system might use "type", "category", or "kind"
        Object type = rawEvent.get("type");
        if (type == null) type = rawEvent.get("category");
        if (type == null) type = rawEvent.get("kind");
        if (type == null) type = rawEvent.get("event_type");

        return type != null ? type.toString() : "lecture";
    }

    @Override
    public String getSourceName() {
        return "Legacy Calendar System";
    }

    @Override
    public boolean isSourceAvailable() {
        return legacySource != null && legacySource.isConnected();
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
}