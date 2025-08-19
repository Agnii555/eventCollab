package edu.neu.csye7374.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite command that executes multiple commands as a batch
 * Similar to professor's BatchCalculator pattern
 */
public class MacroCommand implements Command {
    
    private List<Command> commands;
    private String description;
    
    /**
     * Constructor for MacroCommand
     */
    public MacroCommand() {
        this.commands = new ArrayList<>();
        this.description = "Batch Command";
    }
    
    /**
     * Constructor with description
     * @param description Description of this batch operation
     */
    public MacroCommand(String description) {
        this.commands = new ArrayList<>();
        this.description = description;
    }
    
    /**
     * Add a command to the batch
     * @param command Command to add
     */
    public void addCommand(Command command) {
        commands.add(command);
    }
    
    /**
     * Execute all commands in the batch
     * @return true if all commands executed successfully, false if any failed
     */
    @Override
    public boolean execute() {
        System.out.println("Executing batch: " + description + " (" + commands.size() + " commands)");
        
        boolean allSuccessful = true;
        int successCount = 0;
        
        for (int i = 0; i < commands.size(); i++) {
            Command cmd = commands.get(i);
            boolean success = cmd.execute();
            
            if (success) {
                successCount++;
            } else {
                allSuccessful = false;
                System.out.println("  Command " + (i + 1) + " failed");
            }
        }
        
        System.out.println("Batch execution complete: " + successCount + "/" + commands.size() + " successful");
        return allSuccessful;
    }
    
    /**
     * Get the number of commands in this batch
     * @return Number of commands
     */
    public int getCommandCount() {
        return commands.size();
    }
    
    /**
     * Get the description of this batch
     * @return Description string
     */
    public String getDescription() {
        return description;
    }
}