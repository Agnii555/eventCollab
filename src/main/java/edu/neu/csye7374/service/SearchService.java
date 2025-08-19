package edu.neu.csye7374.service;

import edu.neu.csye7374.event.EventAPI;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling all search and filtering operations for events.
 * Used by the facade to provide search functionality.
 */
public class SearchService {
    
    /**
     * Search events by title (case-insensitive partial match)
     */
    public List<EventAPI> searchByTitle(List<EventAPI> events, String title) {
        if (title == null || title.trim().isEmpty()) {
            return events;
        }
        
        String searchTitle = title.toLowerCase().trim();
        return events.stream()
                    .filter(event -> event.getTitle().toLowerCase().contains(searchTitle))
                    .collect(Collectors.toList());
    }
    
    /**
     * Search events by location (case-insensitive partial match)
     */
    public List<EventAPI> searchByLocation(List<EventAPI> events, String location) {
        if (location == null || location.trim().isEmpty()) {
            return events;
        }
        
        String searchLocation = location.toLowerCase().trim();
        return events.stream()
                    .filter(event -> event.getLocation().toLowerCase().contains(searchLocation))
                    .collect(Collectors.toList());
    }
    
    /**
     * Search events by date range
     */
    public List<EventAPI> searchByDateRange(List<EventAPI> events, LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            return events;
        }
        
        return events.stream()
                    .filter(event -> {
                        LocalDate eventDate = event.getDate();
                        if (startDate != null && eventDate.isBefore(startDate)) {
                            return false;
                        }
                        if (endDate != null && eventDate.isAfter(endDate)) {
                            return false;
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
    }
    
    /**
     * Search events by specific date
     */
    public List<EventAPI> searchByDate(List<EventAPI> events, LocalDate date) {
        if (date == null) {
            return events;
        }
        
        return events.stream()
                    .filter(event -> event.getDate().equals(date))
                    .collect(Collectors.toList());
    }
    
    /**
     * Search events by capacity range
     */
    public List<EventAPI> searchByCapacityRange(List<EventAPI> events, int minCapacity, int maxCapacity) {
        return events.stream()
                    .filter(event -> event.getCapacity() >= minCapacity && event.getCapacity() <= maxCapacity)
                    .collect(Collectors.toList());
    }
    
    /**
     * Search events by description keywords (case-insensitive)
     */
    public List<EventAPI> searchByDescription(List<EventAPI> events, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return events;
        }
        
        String searchKeyword = keyword.toLowerCase().trim();
        return events.stream()
                    .filter(event -> event.getDescription().toLowerCase().contains(searchKeyword))
                    .collect(Collectors.toList());
    }
    
    /**
     * Search events occurring today
     */
    public List<EventAPI> searchTodaysEvents(List<EventAPI> events) {
        LocalDate today = LocalDate.now();
        return searchByDate(events, today);
    }
    
    /**
     * Search events occurring this week
     */
    public List<EventAPI> searchThisWeekEvents(List<EventAPI> events) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        
        return searchByDateRange(events, startOfWeek, endOfWeek);
    }
    
    /**
     * Search events occurring this month
     */
    public List<EventAPI> searchThisMonthEvents(List<EventAPI> events) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        
        return searchByDateRange(events, startOfMonth, endOfMonth);
    }
    
    /**
     * Search upcoming events (from today onwards)
     */
    public List<EventAPI> searchUpcomingEvents(List<EventAPI> events) {
        LocalDate today = LocalDate.now();
        return events.stream()
                    .filter(event -> !event.getDate().isBefore(today))
                    .collect(Collectors.toList());
    }
    
    /**
     * Search past events
     */
    public List<EventAPI> searchPastEvents(List<EventAPI> events) {
        LocalDate today = LocalDate.now();
        return events.stream()
                    .filter(event -> event.getDate().isBefore(today))
                    .collect(Collectors.toList());
    }
    
    /**
     * Advanced search with multiple criteria
     */
    public List<EventAPI> advancedSearch(List<EventAPI> events, SearchCriteria criteria) {
        List<EventAPI> result = new ArrayList<>(events);
        
        if (criteria.getTitle() != null) {
            result = searchByTitle(result, criteria.getTitle());
        }
        
        if (criteria.getLocation() != null) {
            result = searchByLocation(result, criteria.getLocation());
        }
        
        if (criteria.getStartDate() != null || criteria.getEndDate() != null) {
            result = searchByDateRange(result, criteria.getStartDate(), criteria.getEndDate());
        }
        
        if (criteria.getMinCapacity() > 0 || criteria.getMaxCapacity() > 0) {
            int maxCap = criteria.getMaxCapacity() > 0 ? criteria.getMaxCapacity() : Integer.MAX_VALUE;
            result = searchByCapacityRange(result, criteria.getMinCapacity(), maxCap);
        }
        
        if (criteria.getDescription() != null) {
            result = searchByDescription(result, criteria.getDescription());
        }
        
        return result;
    }
    
    /**
     * Inner class for advanced search criteria
     */
    public static class SearchCriteria {
        private String title;
        private String location;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private int minCapacity;
        private int maxCapacity;
        
        public SearchCriteria() {}
        
        // Builder pattern methods
        public SearchCriteria withTitle(String title) {
            this.title = title;
            return this;
        }
        
        public SearchCriteria withLocation(String location) {
            this.location = location;
            return this;
        }
        
        public SearchCriteria withDescription(String description) {
            this.description = description;
            return this;
        }
        
        public SearchCriteria withDateRange(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
            return this;
        }
        
        public SearchCriteria withCapacityRange(int minCapacity, int maxCapacity) {
            this.minCapacity = minCapacity;
            this.maxCapacity = maxCapacity;
            return this;
        }
        
        // Getters
        public String getTitle() { return title; }
        public String getLocation() { return location; }
        public String getDescription() { return description; }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
        public int getMinCapacity() { return minCapacity; }
        public int getMaxCapacity() { return maxCapacity; }
    }
}