package edu.neu.csye7374.user;

import java.util.ArrayList;
import java.util.List;


public class Student extends User {
    private String studentId;
    private String major;
    private int year;
    private List<String> registeredEvents;
    
    
    public Student(String id, String name, String email, String studentId, String major, int year) {
        super(id, name, email);
        this.studentId = studentId;
        this.major = major;
        this.year = year;
        this.registeredEvents = new ArrayList<>();
    }
    
    @Override
    public boolean register() {
        System.out.println("Student " + getName() + " registered successfully.");
        return true;
    }
    
    @Override
    public boolean login() {
        // Student login logic
        System.out.println("Student " + getName() + " logged in successfully.");
        return true;
    }
    
   
    public boolean registerForEvent(String eventId) {
        if (!registeredEvents.contains(eventId)) {
            registeredEvents.add(eventId);
            System.out.println("Student " + getName() + " registered for event: " + eventId);
            return true;
        }
        System.out.println("Student " + getName() + " is already registered for event: " + eventId);
        return false;
    }
    
    
    public boolean cancelEventRegistration(String eventId) {
        if (registeredEvents.remove(eventId)) {
            System.out.println("Student " + getName() + " cancelled registration for event: " + eventId);
            return true;
        }
        System.out.println("Student " + getName() + " was not registered for event: " + eventId);
        return false;
    }
    
    
    public List<String> getRegisteredEvents() {
        return new ArrayList<>(registeredEvents);
    }
    
    // Getters and setters
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", studentId='" + studentId + '\'' +
                ", major='" + major + '\'' +
                ", year=" + year +
                ", registeredEvents=" + registeredEvents +
                '}';
    }
} 