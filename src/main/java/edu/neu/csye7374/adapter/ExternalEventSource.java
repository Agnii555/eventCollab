package edu.neu.csye7374.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Interface representing an external event source that needs to be adapted.
 * This interface defines the contract that external systems should implement
 * or that legacy systems expose in their original format.
 * <p>
 * The Adapter pattern will convert this interface to work with our Event system.
 */
public interface ExternalEventSource {

    /**
     * Get raw event data from external source
     *
     * @return List of raw event data (format varying by source)
     */
    List<Map<String, Object>> getRawEventData();

    /**
     * Get events for a specific date range from an external source
     *
     * @param startDate start date for the range
     * @param endDate   end date for the range
     * @return List of raw event data for the date range
     */
    List<Map<String, Object>> getEventsByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get connection status of an external source
     *
     * @return true if connected and accessible, false otherwise
     */
    boolean isConnected();

    /**
     * Get authentication status for an external source
     *
     * @return true if authenticated, false otherwise
     */
    boolean isAuthenticated();

    /**
     * Get metadata about the external source
     *
     * @return Map containing source metadata (name, version, capabilities, etc.)
     */
    Map<String, String> getSourceMetadata();
}