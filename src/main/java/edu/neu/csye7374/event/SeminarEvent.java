package edu.neu.csye7374.event;

import java.time.LocalDate;

public class SeminarEvent implements EventAPI {

    private final String id;
    private final String title;
    private final String description;
    private final LocalDate date;
    private final String location;
    private final int capacity;

    // Changed from public to protected - for Factory use
    public SeminarEvent(String id, String title, String description,
                        LocalDate date, String location, int capacity) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.capacity = capacity;
    }

    // Private constructor for Builder pattern
    private SeminarEvent(Builder builder) {
        this(builder.id, builder.title, builder.description,
             builder.date, builder.location, builder.capacity);
    }

    // Inner Builder class - Professor's style
    public static class Builder {
        private String id;
        private String title;
        private String description;
        private LocalDate date;
        private String location;
        private int capacity;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder setCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public SeminarEvent build() {
            return new SeminarEvent(this);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }
    
    @Override
    public String getDetails() {
        return "Seminar Event ID: " + id + "\n" +
                "Title: " + title + "\n" +
                "Description: " + description + "\n" +
                "Date: " + date.toString() + "\n" +
                "Location: " + location + "\n" +
                "Capacity: " + capacity + "\n" ;
    }
}