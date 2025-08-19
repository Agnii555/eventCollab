package edu.neu.csye7374.adapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mock implementation of a legacy calendar system for demonstration purposes.
 * This simulates an old calendar system with different field names and formats.
 */
public class MockLegacyCalendarSource implements ExternalEventSource {

    private boolean isConnected;
    private List<Map<String, Object>> mockLegacyEvents;

    public MockLegacyCalendarSource() {
        this.isConnected = true;
        initializeMockData();
    }

    private void initializeMockData() {
        this.mockLegacyEvents = new ArrayList<>();

        // Legacy Event 1 - Uses legacy field names
        Map<String, Object> event1 = new HashMap<>();
        event1.put("legacy_id", "LEG001");
        event1.put("event_name", "Introduction to Computer Science");
        event1.put("desc", "Basic concepts of programming and algorithms");
        event1.put("event_date", "12/15/2024");  // MM/dd/yyyy format
        event1.put("venue", "Room 101");
        event1.put("max_attendees", "80");
        event1.put("type", "lecture");

        // Legacy Event 2 - Different field variations
        Map<String, Object> event2 = new HashMap<>();
        event2.put("id", "LEG002");
        event2.put("name", "Data Structures Workshop");
        event2.put("details", "Hands-on practice with arrays, lists, and trees");
        event2.put("scheduled_date", "2024-12-20");  // yyyy-MM-dd format
        event2.put("place", "Computer Lab A");
        event2.put("capacity", "25");
        event2.put("category", "workshop");

        // Legacy Event 3 - Another variation
        Map<String, Object> event3 = new HashMap<>();
        event3.put("event_id", "LEG003");
        event3.put("title", "Software Engineering Seminar");
        event3.put("description", "Best practices in software development");
        event3.put("date", "20-12-2024");  // dd-MM-yyyy format
        event3.put("location", "Auditorium B");
        event3.put("limit", "150");
        event3.put("kind", "seminar");

        mockLegacyEvents.add(event1);
        mockLegacyEvents.add(event2);
        mockLegacyEvents.add(event3);
    }

    @Override
    public List<Map<String, Object>> getRawEventData() {
        System.out.println("MockLegacyCalendarSource: Fetching raw event data...");
        return new ArrayList<>(mockLegacyEvents);
    }

    @Override
    public List<Map<String, Object>> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        System.out.println("MockLegacyCalendarSource: Fetching events for date range " +
                startDate + " to " + endDate);
        // For mock purposes, return all events (in real implementation, would filter by date)
        return getRawEventData();
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    // Method to simulate connection issues for testing
    public void setConnected(boolean connected) {
        this.isConnected = connected;
    }

    @Override
    public boolean isAuthenticated() {
        return true; // Legacy systems might not have authentication
    }

    @Override
    public Map<String, String> getSourceMetadata() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("name", "Legacy Campus Calendar v1.0");
        metadata.put("version", "1.0.5");
        metadata.put("vendor", "Old University System");
        metadata.put("last_updated", "2020-03-15");
        metadata.put("capabilities", "read-only");
        return metadata;
    }
}