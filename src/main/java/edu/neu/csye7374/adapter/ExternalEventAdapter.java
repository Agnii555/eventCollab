package edu.neu.csye7374.adapter;

import edu.neu.csye7374.event.EventAPI;

import java.util.List;

/**
 * Adapter interface for integrating external event sources with the campus event system.
 * This follows the Adapter pattern to bridge incompatible interfaces and enable
 * integration with external or legacy event feeds/calendars or APIs.
 * Benefits:
 * - Supports future external integrations without modifying core logic
 * - Follows the open/closed principle
 * - Provides a consistent interface for different external systems
 */

public interface ExternalEventAdapter {

    /**
     * Import events from the external source
     *
     * @return List of events imported from the external system
     */
    List<EventAPI> importEvents();

    /**
     * Get the name/identifier of the external source
     *
     * @return String identifier for the external source
     */
    String getSourceName();

    /**
     * Check if the external source is currently available/accessible
     *
     * @return true if the source is available, false otherwise
     */
    boolean isSourceAvailable();

    /**
     * Get the last sync timestamp
     *
     * @return String representation of the last sync time
     */
    String getLastSyncTime();

    /**
     * Sync events with the external source
     * This may include both importing new events and updating existing ones
     *
     * @return number of events synced
     */
    int syncEvents();
}