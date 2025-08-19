package edu.neu.csye7374.user;

/**
 * Abstract base class for all users in the campus event management system.
 * Defines common attributes and methods for both students and organizers.
 */
public abstract class User {
    private String id;
    private String name;
    private String email;
    
    /**
     * Constructor for User class
     * @param id unique identifier for the user
     * @param name full name of the user
     * @param email email address of the user
     */
    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    /**
     * Abstract method for user registration
     * @return true if registration is successful, false otherwise
     */
    public abstract boolean register();
    
    /**
     * Abstract method for user login
     * @return true if login is successful, false otherwise
     */
    public abstract boolean login();
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
} 