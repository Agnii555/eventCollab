package edu.neu.csye7374.Demo;

import edu.neu.csye7374.adapter.ExternalCalendarAdapter;
import edu.neu.csye7374.adapter.ExternalEventAdapter;
import edu.neu.csye7374.adapter.LegacyCalendarAdapter;
import edu.neu.csye7374.adapter.MockExternalCalendarSource;
import edu.neu.csye7374.adapter.MockLegacyCalendarSource;
import edu.neu.csye7374.decorator.OnlineStreamingDecorator;
import edu.neu.csye7374.decorator.RecordingDecorator;
import edu.neu.csye7374.event.EventAPI;
import edu.neu.csye7374.facade.CampusEventSystemFacade;
import edu.neu.csye7374.factory.AbstractEventFactory;
import edu.neu.csye7374.factory.LectureEventFactory;
import edu.neu.csye7374.factory.SeminarEventFactory;
import edu.neu.csye7374.factory.WorkshopEventFactory;
import edu.neu.csye7374.manager.EventManager;
import edu.neu.csye7374.user.Student;
import edu.neu.csye7374.user.Organizer;
import edu.neu.csye7374.user.User;

import java.time.LocalDate;
import java.util.List;

/**
 * DemoSetup class for Campus Event Management System
 * Contains sample data setup for UI demonstration
 * 
 * @author Group 5
 */
public class Demo {
	
	/**
	 * Demo method that creates sample data for the Campus Event Management System
	 * This method sets up events, users, and registrations for UI demonstration
	 */
	public static void demo() {
		System.out.println("🚀 Setting up Campus Event Management System Demo...\n");
		
		// Get the singleton EventManager instance
		EventManager eventManager = EventManager.getInstance();
		
		// Create event factories
		AbstractEventFactory lectureFactory = new LectureEventFactory();
		AbstractEventFactory seminarFactory = new SeminarEventFactory();
		AbstractEventFactory workshopFactory = new WorkshopEventFactory();
		
		// ==================== CREATE ORGANIZERS ====================
		System.out.println("👥 Creating Organizers...");
		
		Organizer profSmith = new Organizer("ORG001", "Dr. Sarah Smith", "sarah.smith@neu.edu", "Computer Science", "Professor");
		Organizer profJohnson = new Organizer("ORG002", "Prof. Michael Johnson", "michael.johnson@neu.edu", "Engineering", "Associate Professor");
		Organizer profBrown = new Organizer("ORG003", "Dr. Emily Brown", "emily.brown@neu.edu", "Business", "Professor");
		Organizer profDavis = new Organizer("ORG004", "Prof. David Davis", "david.davis@neu.edu", "Arts", "Lecturer");
		
		// Register organizers
		eventManager.registerUser(profSmith);
		eventManager.registerUser(profJohnson);
		eventManager.registerUser(profBrown);
		eventManager.registerUser(profDavis);
		
		// ==================== CREATE STUDENTS ====================
		System.out.println("🎓 Creating Students...");
		
		Student alice = new Student("STU001", "Alice Johnson", "alice.johnson@neu.edu", "2024001", "Computer Science", 3);
		Student bob = new Student("STU002", "Bob Wilson", "bob.wilson@neu.edu", "2024002", "Engineering", 2);
		Student carol = new Student("STU003", "Carol Martinez", "carol.martinez@neu.edu", "2024003", "Business", 4);
		Student david = new Student("STU004", "David Thompson", "david.thompson@neu.edu", "2024004", "Computer Science", 1);
		Student emma = new Student("STU005", "Emma Garcia", "emma.garcia@neu.edu", "2024005", "Arts", 2);
		Student frank = new Student("STU006", "Frank Lee", "frank.lee@neu.edu", "2024006", "Engineering", 3);
		Student grace = new Student("STU007", "Grace Anderson", "grace.anderson@neu.edu", "2024007", "Business", 1);
		Student henry = new Student("STU008", "Henry Taylor", "henry.taylor@neu.edu", "2024008", "Computer Science", 4);
		Student isabella = new Student("STU009", "Isabella Rodriguez", "isabella.rodriguez@neu.edu", "2024009", "Arts", 2);
		Student jack = new Student("STU010", "Jack Miller", "jack.miller@neu.edu", "2024010", "Engineering", 3);
		
		// Register students
		eventManager.registerUser(alice);
		eventManager.registerUser(bob);
		eventManager.registerUser(carol);
		eventManager.registerUser(david);
		eventManager.registerUser(emma);
		eventManager.registerUser(frank);
		eventManager.registerUser(grace);
		eventManager.registerUser(henry);
		eventManager.registerUser(isabella);
		eventManager.registerUser(jack);
		
		// ==================== CREATE EVENTS ====================
		System.out.println("📅 Creating Events...");
		
		// Create Lecture Events
		EventAPI designPatternsLecture = lectureFactory.createEvent(
			"EVT001", 
			"Design Patterns in Software Engineering", 
			"Comprehensive overview of creational, structural, and behavioral design patterns with real-world examples.", 
			LocalDate.of(2024, 12, 15), 
			"Richards Hall 235", 
			50
		);
		
		EventAPI dataStructuresLecture = lectureFactory.createEvent(
			"EVT002", 
			"Advanced Data Structures", 
			"Deep dive into trees, graphs, and advanced algorithms for efficient data management.", 
			LocalDate.of(2024, 12, 18), 
			"Snell Library 90", 
			40
		);
		
		// Create Seminar Events
		EventAPI aiSeminar = seminarFactory.createEvent(
			"EVT003", 
			"AI and Machine Learning Seminar", 
			"Interactive seminar on current trends in artificial intelligence and machine learning applications.", 
			LocalDate.of(2024, 12, 20), 
			"West Village H 366", 
			30
		);
		
		EventAPI cybersecuritySeminar = seminarFactory.createEvent(
			"EVT004", 
			"Cybersecurity Best Practices", 
			"Learn about modern cybersecurity threats and best practices for protecting digital assets.", 
			LocalDate.of(2024, 12, 22), 
			"Shillman Hall 320", 
			25
		);
		
		// Create Workshop Events
		EventAPI webDevWorkshop = workshopFactory.createEvent(
			"EVT005", 
			"Full-Stack Web Development Workshop", 
			"Hands-on workshop covering frontend, backend, and database development with modern technologies.", 
			LocalDate.of(2024, 12, 25), 
			"ISEC 102", 
			20
		);
		
		EventAPI mobileAppWorkshop = workshopFactory.createEvent(
			"EVT006", 
			"Mobile App Development Workshop", 
			"Build mobile applications using React Native and modern development practices.", 
			LocalDate.of(2024, 12, 28), 
			"ISEC 201", 
			15
		);

		mobileAppWorkshop = new OnlineStreamingDecorator(mobileAppWorkshop,"Zoom", "https://zoom.us.com/12345789");
		mobileAppWorkshop = new RecordingDecorator(mobileAppWorkshop,true, "https://recording.zoom.us.com/12345789");

		// Add events to the system
		eventManager.addEvent(designPatternsLecture);
		eventManager.addEvent(dataStructuresLecture);
		eventManager.addEvent(aiSeminar);
		eventManager.addEvent(cybersecuritySeminar);
		eventManager.addEvent(webDevWorkshop);
		eventManager.addEvent(mobileAppWorkshop);
		
		// ==================== ADAPTER PATTERN DEMO - EXTERNAL CALENDAR INTEGRATION ====================
		System.out.println("\n Demonstrating External Calendar Integration (Adapter Pattern)...");
		
		// Create external calendar source and adapter
		MockExternalCalendarSource externalSource = new MockExternalCalendarSource();
		ExternalEventAdapter externalCalendarAdapter = new ExternalCalendarAdapter(externalSource, "partner-university-calendar");
		
		System.out.println(" External Calendar Source: " + externalCalendarAdapter.getSourceName());
		System.out.println(" Source Available: " + externalCalendarAdapter.isSourceAvailable());
		
		// Import events from external calendar
		List<EventAPI> externalEvents = externalCalendarAdapter.importEvents();
		System.out.println(" Imported " + externalEvents.size() + " events from external calendar");
		
		// Add external events to the system
		for (EventAPI event : externalEvents) {
			eventManager.addEvent(event);
			System.out.println("   ✓ Added: " + event.getTitle() + " (" + event.getClass().getSimpleName() + ")");
		}
		
		// ==================== ADAPTER PATTERN DEMO - LEGACY CALENDAR INTEGRATION ====================
		System.out.println("\n Demonstrating Legacy Calendar Integration (Adapter Pattern)...");
		
		// Create legacy calendar source and adapter
		MockLegacyCalendarSource legacySource = new MockLegacyCalendarSource();
		ExternalEventAdapter legacyCalendarAdapter = new LegacyCalendarAdapter(legacySource);
		
		System.out.println(" Legacy Calendar Source: " + legacyCalendarAdapter.getSourceName());
		System.out.println(" Source Available: " + legacyCalendarAdapter.isSourceAvailable());
		
		// Import events from legacy calendar
		List<EventAPI> legacyEvents = legacyCalendarAdapter.importEvents();
		System.out.println(" Imported " + legacyEvents.size() + " events from legacy calendar");
		
		// Add legacy events to the system
		for (EventAPI event : legacyEvents) {
			eventManager.addEvent(event);
			System.out.println("   ✓ Added: " + event.getTitle() + " (" + event.getClass().getSimpleName() + ")");
		}
		
		// ==================== REGISTER STUDENTS FOR EXTERNAL/LEGACY EVENTS ====================
		System.out.println("\n📝 Registering Students for External/Legacy Events...");
		
		// Register students for external events (using external event IDs)
		if (externalEvents.size() > 0) {
			String extEvent1Id = externalEvents.get(0).getId(); // Advanced Database Systems
			eventManager.registerUserForEvent("STU003", extEvent1Id); // Carol
			eventManager.registerUserForEvent("STU005", extEvent1Id); // Emma
			eventManager.registerUserForEvent("STU009", extEvent1Id); // Isabella
			System.out.println("   ✓ Registered 3 students for: " + externalEvents.get(0).getTitle());
		}
		
		if (externalEvents.size() > 1) {
			String extEvent2Id = externalEvents.get(1).getId(); // Research Methods Workshop
			eventManager.registerUserForEvent("STU002", extEvent2Id); // Bob
			eventManager.registerUserForEvent("STU004", extEvent2Id); // David
			eventManager.registerUserForEvent("STU008", extEvent2Id); // Henry
			System.out.println("   ✓ Registered 3 students for: " + externalEvents.get(1).getTitle());
		}
		
		if (externalEvents.size() > 2) {
			String extEvent3Id = externalEvents.get(2).getId(); // Guest Lecture: Sustainable Computing
			eventManager.registerUserForEvent("STU001", extEvent3Id); // Alice
			eventManager.registerUserForEvent("STU006", extEvent3Id); // Frank
			eventManager.registerUserForEvent("STU010", extEvent3Id); // Jack
			System.out.println("   ✓ Registered 3 students for: " + externalEvents.get(2).getTitle());
		}
		
		// Register students for legacy events
		if (legacyEvents.size() > 0) {
			String legEvent1Id = legacyEvents.get(0).getId(); // Introduction to Computer Science
			eventManager.registerUserForEvent("STU007", legEvent1Id); // Grace
			eventManager.registerUserForEvent("STU009", legEvent1Id); // Isabella
			System.out.println("   ✓ Registered 2 students for: " + legacyEvents.get(0).getTitle());
		}
		
		if (legacyEvents.size() > 1) {
			String legEvent2Id = legacyEvents.get(1).getId(); // Data Structures Workshop
			eventManager.registerUserForEvent("STU003", legEvent2Id); // Carol
			eventManager.registerUserForEvent("STU008", legEvent2Id); // Henry
			System.out.println("   ✓ Registered 2 students for: " + legacyEvents.get(1).getTitle());
		}
		
		System.out.println("\n Adapter Pattern Integration Summary:");
		System.out.println("• External Calendar Adapter: " + externalEvents.size() + " events imported");
		System.out.println("• Legacy Calendar Adapter: " + legacyEvents.size() + " events imported");
		System.out.println("• Total External Events: " + (externalEvents.size() + legacyEvents.size()));
		System.out.println("• Last External Sync: " + externalCalendarAdapter.getLastSyncTime());
		System.out.println("• Last Legacy Sync: " + legacyCalendarAdapter.getLastSyncTime());
		
		// ==================== FACADE PATTERN DEMO - UNIFIED INTERFACE ====================
		System.out.println("\n Demonstrating Facade Pattern - Unified System Interface...");
		
		// Get the facade instance
		CampusEventSystemFacade facade = CampusEventSystemFacade.getInstance();
		
		// Add the adapters to the facade for centralized management
		facade.addExternalAdapter(externalCalendarAdapter);
		facade.addExternalAdapter(legacyCalendarAdapter);
		
		// Demonstrate facade functionality
		System.out.println(" Facade Pattern Demonstrations:");
		
		// 1. Event Search via Facade
		System.out.println("  1. Searching events via facade:");
		List<EventAPI> foundEvents = facade.searchEventsByTitle("Design");
		System.out.println("     Found " + foundEvents.size() + " events containing 'Design'");
		
		// 2. Event Sorting via Facade
		System.out.println("  2. Getting sorted events via facade:");
		List<EventAPI> sortedEvents = facade.getSortedEvents("date");
		System.out.println("     Retrieved " + sortedEvents.size() + " events sorted by date");
		
		// 3. User Management via Facade
		System.out.println("  3. User management via facade:");
		boolean studentRegistered = facade.registerStudent("STU011", "John Facade", "john.facade@neu.edu", "2024011", "Computer Science", 2);
		System.out.println("     New student registration: " + (studentRegistered ? "Success" : "Failed"));
		
		// 4. Event Creation via Facade
		System.out.println("  4. Event creation via facade:");
		String newEventId = facade.createEvent("ORG001", "lecture", "Facade Pattern Workshop", 
		                                      "Learn about the Facade design pattern", 
		                                      LocalDate.of(2024, 12, 30), "Library 101", 25, "" );

		System.out.println("     New event created: " + (newEventId != null ? newEventId : "Failed"));
		
		// 5. External Event Import via Facade
		System.out.println("  5. External event import via facade:");
		int importedViaFacade = facade.importExternalEvents();
		System.out.println("     Additional events imported: " + importedViaFacade);
		
		// 6. System Statistics via Facade
		System.out.println("  6. System statistics via facade:");
		CampusEventSystemFacade.SystemStats stats = facade.getSystemStatistics();
		System.out.println("     " + stats.toString().replace("\n", "\n     "));
		
		System.out.println("\n Facade Pattern Benefits Demonstrated:");
		System.out.println("• Unified interface for all system operations");
		System.out.println("• Simplified client interactions with complex subsystems");
		System.out.println("• Centralized coordination of core components");
		System.out.println("• Integrated adapter management for external sources");
		
		// ==================== REGISTER STUDENTS FOR EVENTS ====================
		System.out.println(" Registering Students for Events...");
		
		// Design Patterns Lecture - High interest
		eventManager.registerUserForEvent("STU001", "EVT001"); // Alice
		eventManager.registerUserForEvent("STU002", "EVT001"); // Bob
		eventManager.registerUserForEvent("STU004", "EVT001"); // David
		eventManager.registerUserForEvent("STU008", "EVT001"); // Henry
		eventManager.registerUserForEvent("STU010", "EVT001"); // Jack
		
		// Data Structures Lecture
		eventManager.registerUserForEvent("STU001", "EVT002"); // Alice
		eventManager.registerUserForEvent("STU002", "EVT002"); // Bob
		eventManager.registerUserForEvent("STU006", "EVT002"); // Frank
		eventManager.registerUserForEvent("STU008", "EVT002"); // Henry
		
		// AI Seminar
		eventManager.registerUserForEvent("STU003", "EVT003"); // Carol
		eventManager.registerUserForEvent("STU005", "EVT003"); // Emma
		eventManager.registerUserForEvent("STU007", "EVT003"); // Grace
		eventManager.registerUserForEvent("STU009", "EVT003"); // Isabella
		
		// Cybersecurity Seminar
		eventManager.registerUserForEvent("STU001", "EVT004"); // Alice
		eventManager.registerUserForEvent("STU004", "EVT004"); // David
		eventManager.registerUserForEvent("STU006", "EVT004"); // Frank
		eventManager.registerUserForEvent("STU008", "EVT004"); // Henry
		eventManager.registerUserForEvent("STU010", "EVT004"); // Jack
		
		// Web Development Workshop
		eventManager.registerUserForEvent("STU002", "EVT005"); // Bob
		eventManager.registerUserForEvent("STU005", "EVT005"); // Emma
		eventManager.registerUserForEvent("STU007", "EVT005"); // Grace
		eventManager.registerUserForEvent("STU009", "EVT005"); // Isabella
		
		// Mobile App Workshop
		eventManager.registerUserForEvent("STU003", "EVT006"); // Carol
		eventManager.registerUserForEvent("STU005", "EVT006"); // Emma
		eventManager.registerUserForEvent("STU007", "EVT006"); // Grace
		eventManager.registerUserForEvent("STU009", "EVT006"); // Isabella
		eventManager.registerUserForEvent("STU010", "EVT006"); // Jack
		
		// ==================== DISPLAY DEMO SUMMARY ====================
		System.out.println("\n Demo Setup Complete!");
		System.out.println("==========================================");
		System.out.println(" System Statistics:");
		System.out.println(eventManager.getSystemStats());
		
		System.out.println("\n🎯 Sample Data Created:");
		System.out.println("• 4 Organizers (Professors from different departments)");
		System.out.println("• 10 Students (from various majors and years)");
		System.out.println("• 6 Internal Events (2 Lectures, 2 Seminars, 2 Workshops)");
		System.out.println("• 7 External/Legacy Events imported via Adapter Pattern");
		System.out.println("• Multiple event registrations with varying attendance");
		
		System.out.println("\n Event Details:");
		List<EventAPI> allEvents = eventManager.getAllEvents();
		for (EventAPI event : allEvents) {
			List<User> participants = eventManager.getEventParticipants(event.getId());
			System.out.println("• " + event.getTitle() + " - " + participants.size() + " participants");
		}
		
		System.out.println("\n Demo data is ready for UI implementation!");
	}
} 