package edu.neu.csye7374.user;

import java.util.ArrayList;
import java.util.List;


public class Organizer extends User {
    private String department;
    private String role;
    private List<String> createdEvents;
    
    
    public Organizer(String id, String name, String email, String department, String role) {
        super(id, name, email);
        this.department = department;
        this.role = role;
        this.createdEvents = new ArrayList<>();
    }
    
    @Override
    public boolean register() {
        // Organizer registration logic
        System.out.println("Organizer " + getName() + " registered successfully.");
        return true;
    }
    
    @Override
    public boolean login() {
        // Organizer login logic
        System.out.println("Organizer " + getName() + " logged in successfully.");
        return true;
    }
    
    
    public String createEvent(String eventType, String title, String description, 
                            String date, String location, int capacity) {
        // Generate a unique event ID
        String eventId = "EVENT_" + System.currentTimeMillis();
        
        // Add the event to the organizer's created events list
        createdEvents.add(eventId);
        
        System.out.println("Organizer " + getName() + " created " + eventType + 
                          " event: " + title + " (ID: " + eventId + ")");
        
        return eventId;
    }
    
    
    public boolean updateEvent(String eventId, String title, String description, 
                             String date, String location, int capacity) {
        if (createdEvents.contains(eventId)) {
            System.out.println("Organizer " + getName() + " updated event: " + eventId);
            return true;
        }
        System.out.println("Organizer " + getName() + " cannot update event: " + eventId + " (not created by this organizer)");
        return false;
    }
    
    
    public boolean cancelEvent(String eventId) {
        if (createdEvents.contains(eventId)) {
            createdEvents.remove(eventId);
            System.out.println("Organizer " + getName() + " cancelled event: " + eventId);
            return true;
        }
        System.out.println("Organizer " + getName() + " cannot cancel event: " + eventId + " (not created by this organizer)");
        return false;
    }
    
    public List<String> getCreatedEvents() {
        return new ArrayList<>(createdEvents);
    }
    
    // Getters and setters
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    public String toString() {
        return "Organizer{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", department='" + department + '\'' +
                ", role='" + role + '\'' +
                ", createdEvents=" + createdEvents +
                '}';
    }
} 