package edu.neu.csye7374;

import edu.neu.csye7374.Demo.Demo;
import edu.neu.csye7374.ui.EventManagementUI;
import javax.swing.SwingUtilities;

/**
 * 
 * @author Yash Zaveri
 * 
 */

public class Driver {
	public static void main(String[] args) {
		System.out.println("============Main Execution Start===================\n\n");

        // Run the demo to set up sample data
        Demo.demo();
        
        // Launch the UI
        SwingUtilities.invokeLater(() -> {
			EventManagementUI ui = new EventManagementUI();
			ui.setVisible(true);
		});
		System.out.println("🚀 Launching Swing GUI...");
        
        
		System.out.println("\n\n============Main Execution End===================");
	}
}
