package edu.neu.csye7374.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Invoker class that executes commands and maintains history
 * Simplified version following professor's pattern
 */
public class CommandInvoker {
    
    private List<Command> commandHistory;
    private List<Command> executedCommands;
    
    /**
     * Constructor for CommandInvoker
     */
    public CommandInvoker() {
        this.commandHistory = new ArrayList<>();
        this.executedCommands = new ArrayList<>();
    }
    
    /**
     * Execute a single command
     * @param command Command to execute
     * @return true if execution was successful, false otherwise
     */
    public boolean executeCommand(Command command) {
        commandHistory.add(command);
        
        boolean success = command.execute();
        
        if (success) {
            executedCommands.add(command);
            System.out.println("Command executed successfully. Total executed: " + executedCommands.size());
        } else {
            System.out.println("Command execution failed");
        }
        
        return success;
    }
    
    /**
     * Execute multiple commands
     * @param commands List of commands to execute
     * @return List of boolean results for each command
     */
    public List<Boolean> executeAll(List<Command> commands) {
        List<Boolean> results = new ArrayList<>();
        
        System.out.println("Executing " + commands.size() + " commands...");
        
        for (Command cmd : commands) {
            boolean result = executeCommand(cmd);
            results.add(result);
        }
        
        return results;
    }
    
    /**
     * Get the complete command history
     * @return List of all commands that were attempted
     */
    public List<Command> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }
    
    /**
     * Get successfully executed commands
     * @return List of commands that executed successfully
     */
    public List<Command> getExecutedCommands() {
        return new ArrayList<>(executedCommands);
    }
    
    /**
     * Get the number of commands in history
     * @return Number of total commands attempted
     */
    public int getHistorySize() {
        return commandHistory.size();
    }
    
    /**
     * Get the number of successfully executed commands
     * @return Number of successful commands
     */
    public int getExecutedSize() {
        return executedCommands.size();
    }
    
    /**
     * Clear all command history
     */
    public void clearHistory() {
        commandHistory.clear();
        executedCommands.clear();
        System.out.println("Command history cleared");
    }
}