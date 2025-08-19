package edu.neu.csye7374.command;

/**
 * Command pattern interface following professor's style
 * Simple interface with just execute method that returns success status
 */
public interface Command {
    /**
     * Execute the command
     * @return true if execution was successful, false otherwise
     */
    boolean execute();
}