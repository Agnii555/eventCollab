package edu.neu.csye7374.adapter;

import java.time.LocalDate;
import java.util.*;

/**
 * Mock implementation of an external calendar system for demonstration purposes.
 * This simulates various external calendar API response formats that might be used
 * by other university systems, third-party calendars, or partner institutions.
 */
public class MockExternalCalendarSource implements ExternalEventSource {

    private boolean isConnected;
    private boolean isAuthenticated;
    private List<Map<String, Object>> mockExternalEvents;

    public MockExternalCalendarSource() {
        this.isConnected = true;
        this.isAuthenticated = true;
        initializeMockData();
    }

    @SuppressWarnings("unchecked")
    private void initializeMockData() {
        this.mockExternalEvents = new ArrayList<>();

        // External Calendar Event 1 - Academic style
        Map<String, Object> event1 = new HashMap<>();
        event1.put("id", "ext_event_1");
        event1.put("summary", "Advanced Database Systems");
        event1.put("description", "Graduate level course on distributed databases and NoSQL systems. Capacity: 45");
        event1.put("location", "Engineering Building Room 301");

        Map<String, Object> start1 = new HashMap<>();
        start1.put("dateTime", "2024-12-18T10:00:00-05:00");
        event1.put("start", start1);

        List<Map<String, Object>> attendees1 = new ArrayList<>();
        Map<String, Object> attendee1 = new HashMap<>();
        attendee1.put("email", "grad1@university.edu");
        attendees1.add(attendee1);
        event1.put("attendees", attendees1);

        // External Calendar Event 2 - Workshop style (all-day event)
        Map<String, Object> event2 = new HashMap<>();
        event2.put("id", "ext_event_2");
        event2.put("summary", "Research Methods Workshop - Data Analysis");
        event2.put("description", "Hands-on workshop for graduate students on statistical analysis methods");
        event2.put("location", "Statistics Lab Building");

        Map<String, Object> start2 = new HashMap<>();
        start2.put("date", "2024-12-22");  // All-day event format
        event2.put("start", start2);

        List<Map<String, Object>> attendees2 = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Map<String, Object> attendee = new HashMap<>();
            attendee.put("email", "researcher" + i + "@university.edu");
            attendees2.add(attendee);
        }
        event2.put("attendees", attendees2);

        // External Calendar Event 3 - Seminar/Conference style
        Map<String, Object> event3 = new HashMap<>();
        event3.put("id", "ext_event_3");
        event3.put("summary", "Guest Lecture: Sustainable Computing");
        event3.put("description", "Industry expert discusses green computing practices and environmental impact");
        event3.put("location", "Main Auditorium");
        event3.put("capacity", "200");  // Direct capacity field

        Map<String, Object> start3 = new HashMap<>();
        start3.put("dateTime", "2024-12-25T14:00:00-05:00");
        event3.put("start", start3);

        // External Calendar Event 4 - Different format variation
        Map<String, Object> event4 = new HashMap<>();
        event4.put("id", "ext_event_4");
        event4.put("title", "Career Fair - Tech Companies");  // Using 'title' instead of 'summary'
        event4.put("details", "Meet with representatives from major tech companies. Max: 500 students");  // Using 'details' and 'Max:'
        event4.put("venue", "Student Center");  // Using 'venue' instead of 'location'
        event4.put("date", "2024-12-28");  // Direct date field

        mockExternalEvents.add(event1);
        mockExternalEvents.add(event2);
        mockExternalEvents.add(event3);
        mockExternalEvents.add(event4);
    }

    @Override
    public List<Map<String, Object>> getRawEventData() {
        System.out.println("MockExternalCalendarSource: Fetching raw event data from external calendar API...");
        return new ArrayList<>(mockExternalEvents);
    }

    @Override
    public List<Map<String, Object>> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        System.out.println("MockExternalCalendarSource: Fetching external calendar events for date range " +
                         startDate + " to " + endDate);
        // For mock purposes, return all events (in real implementation, would filter by date)
        return getRawEventData();
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public Map<String, String> getSourceMetadata() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("name", "External Calendar API");
        metadata.put("version", "v2.1");
        metadata.put("vendor", "Partner University System");
        metadata.put("last_updated", "2024-12-01");
        metadata.put("capabilities", "read-only");
        metadata.put("api_quota", "10000 requests/day");
        metadata.put("supported_formats", "JSON, iCal");
        return metadata;
    }

    // Methods to simulate connection and authentication issues for testing
    public void setConnected(boolean connected) {
        this.isConnected = connected;
    }

    public void setAuthenticated(boolean authenticated) {
        this.isAuthenticated = authenticated;
    }
}