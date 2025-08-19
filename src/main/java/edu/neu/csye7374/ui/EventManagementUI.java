package edu.neu.csye7374.ui;

import edu.neu.csye7374.event.EventAPI;
import edu.neu.csye7374.manager.EventManager;
import edu.neu.csye7374.user.User;
import edu.neu.csye7374.user.Student;
import edu.neu.csye7374.user.Organizer;
import edu.neu.csye7374.facade.CampusEventSystemFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Swing GUI for Campus Event Management System
 * Adds: New User registration dialog + Register user for selected event dialog
 *
 * @author Group 5 (updated)
 */
public class EventManagementUI extends JFrame {

    private final EventManager eventManager;
    private JTable eventsTable;
    private JTable participantsTable;
    private JTextArea eventDetailsArea;
    private JLabel statsLabel;
    JComboBox<String> sortCombo;

    public EventManagementUI() {
        this.eventManager = EventManager.getInstance();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Campus Event Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 820);
        setLocationRelativeTo(null);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // Create header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create center panel with events and participants
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Create bottom panel with details and stats
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Load initial data
        refreshData();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 47, 61)); // deep blue
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel titleLabel = new JLabel("🎓 Campus Event Management System");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Right side controls (stats + actions)
        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightControls.setOpaque(false);

        statsLabel = new JLabel();
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        statsLabel.setForeground(Color.WHITE);

        JButton createEventBtn = new JButton("🎯 Create Event");
        stylePrimaryButton(createEventBtn);
        createEventBtn.addActionListener(e -> openCreateEventDialog());

        JButton newUserBtn = new JButton("＋ New User");
        stylePrimaryButton(newUserBtn);
        newUserBtn.addActionListener(e -> openNewUserDialog());

        JButton registerForEventBtn = new JButton("Register for Event");
        styleSecondaryButton(registerForEventBtn);
        registerForEventBtn.addActionListener(e -> openRegisterForEventDialog());

        rightControls.add(statsLabel);
        rightControls.add(createEventBtn);
        rightControls.add(registerForEventBtn);
        rightControls.add(newUserBtn);

        headerPanel.add(rightControls, BorderLayout.EAST);
        return headerPanel;
    }

    private void stylePrimaryButton(JButton b) {
        b.setBackground(new Color(40, 167, 69));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
    }

    private void styleSecondaryButton(JButton b) {
        b.setBackground(new Color(69, 123, 157));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        centerPanel.setBackground(new Color(245, 247, 250));

        // Left panel - Events table
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(220,220,220)), "📅 Events"));
        leftPanel.setBackground(Color.WHITE);

        // Create events table
        String[] eventColumns = {"Event ID", "Title", "Type", "Date", "Location", "Capacity", "Participants"};
        DefaultTableModel eventModel = new DefaultTableModel(eventColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        eventsTable = new JTable(eventModel);
        eventsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventsTable.getTableHeader().setReorderingAllowed(false);
        eventsTable.setRowHeight(28);

        JScrollPane eventScrollPane = new JScrollPane(eventsTable);
        leftPanel.add(eventScrollPane, BorderLayout.CENTER);

        // Right panel - Participants table
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(220,220,220)), "👥 Event Participants"));
        rightPanel.setBackground(Color.WHITE);

        String[] participantColumns = {"Name", "Email", "Type", "Details"};
        DefaultTableModel participantModel = new DefaultTableModel(participantColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        participantsTable = new JTable(participantModel);
        participantsTable.getTableHeader().setReorderingAllowed(false);
        participantsTable.setRowHeight(28);

        JScrollPane participantScrollPane = new JScrollPane(participantsTable);
        rightPanel.add(participantScrollPane, BorderLayout.CENTER);

        // Add notification button panel below the table
        JPanel notificationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        notificationPanel.setBackground(Color.WHITE);
        notificationPanel.setBorder(BorderFactory.createTitledBorder("📧 Send Notifications"));
        
        JButton sendNotificationBtn = new JButton("📧 Send Notification to Selected User");
        styleSecondaryButton(sendNotificationBtn);
        sendNotificationBtn.addActionListener(e -> openSendNotificationToSelectedUser());
        
        notificationPanel.add(sendNotificationBtn);
        rightPanel.add(notificationPanel, BorderLayout.SOUTH);

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        // Add selection listener to events table
        eventsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateParticipantsTable();
                updateEventDetails();
            }
        });

        // Double-click on event to open register dialog
        eventsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    openRegisterForEventDialog();
                }
            }
        });

        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        bottomPanel.setBackground(new Color(245, 247, 250));

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(220,220,220)), "📋 Event Details"));
        detailsPanel.setBackground(Color.WHITE);

        eventDetailsArea = new JTextArea();
        eventDetailsArea.setEditable(false);
        eventDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        eventDetailsArea.setRows(8);
        eventDetailsArea.setBackground(Color.WHITE);

        JScrollPane detailsScrollPane = new JScrollPane(eventDetailsArea);
        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        buttonsPanel.setBackground(new Color(245, 247, 250));

        JButton refreshButton = new JButton("🔄 Refresh Data");
        styleSecondaryButton(refreshButton);
        refreshButton.addActionListener(e -> refreshData());

        String[] sortOptions = {"Sort by Date", "Sort by Title"};
        sortCombo = new JComboBox<>(sortOptions);
        sortCombo.addActionListener(e -> refreshData());

        JButton exitButton = new JButton("❌ Exit");
        exitButton.addActionListener(e -> System.exit(0));
        exitButton.setBackground(new Color(220, 53, 69));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);

        buttonsPanel.add(refreshButton);
        buttonsPanel.add(sortCombo);
        buttonsPanel.add(exitButton);

        bottomPanel.add(detailsPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return bottomPanel;
    }

    private void refreshData() {
        loadEventsTable();
        updateStats();

        // Select first event if available
        if (eventsTable.getRowCount() > 0 && eventsTable.getSelectedRow() == -1) {
            eventsTable.setRowSelectionInterval(0, 0);
        } else {
            updateParticipantsTable();
            updateEventDetails();
        }
    }

    private void loadEventsTable() {
        DefaultTableModel model = (DefaultTableModel) eventsTable.getModel();
        model.setRowCount(0);

        List<EventAPI> events;
        String selectedSort = (String) sortCombo.getSelectedItem();

        if ("Sort by Date".equals(selectedSort)) {
            events = eventManager.eventsSortedByDate();
        } else if ("Sort by Title".equals(selectedSort)) {
            events = eventManager.eventsSortedByTitle();
        } else {
            events = eventManager.getAllEvents();
        }

        for (EventAPI event : events) {
            List<User> participants = eventManager.getEventParticipants(event.getId());
            String eventType = event.getClass().getSimpleName().replace("Event", "");
            model.addRow(new Object[]{
                    event.getId(),
                    event.getTitle(),
                    eventType,
                    event.getDate().toString(),
                    event.getLocation(),
                    event.getCapacity(),
                    participants.size()
            });
        }
    }

    private void updateParticipantsTable() {
        DefaultTableModel model = (DefaultTableModel) participantsTable.getModel();
        model.setRowCount(0);

        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow >= 0) {
            String eventId = (String) eventsTable.getValueAt(selectedRow, 0);
            List<User> participants = eventManager.getEventParticipants(eventId);

            for (User participant : participants) {
                String type = participant.getClass().getSimpleName();
                String details = "";

                if (participant instanceof Student) {
                    Student student = (Student) participant;
                    details = "Student ID: " + student.getStudentId() + ", Major: " + student.getMajor() + ", Year: " + student.getYear();
                } else if (participant instanceof Organizer) {
                    Organizer organizer = (Organizer) participant;
                    details = "Department: " + organizer.getDepartment() + ", Role: " + organizer.getRole();
                }

                model.addRow(new Object[]{
                        participant.getName(),
                        participant.getEmail(),
                        type,
                        details
                });
            }
        }
    }

    private void updateEventDetails() {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow >= 0) {
            String eventId = (String) eventsTable.getValueAt(selectedRow, 0);
            EventAPI event = eventManager.getEvent(eventId);
            List<User> participants = eventManager.getEventParticipants(eventId);

            StringBuilder details = new StringBuilder();
            details.append("Event Details:\n");
            details.append("==============\n");
            details.append("ID: ").append(event.getId()).append("\n");
            details.append("Title: ").append(event.getTitle()).append("\n");
            details.append("Description: ").append(event.getDescription()).append("\n");
            details.append("Date: ").append(event.getDate()).append("\n");
            details.append("Location: ").append(event.getLocation()).append("\n");
            details.append("Capacity: ").append(event.getCapacity()).append("\n");
            details.append("Registered Participants: ").append(participants.size()).append("\n\n");

            details.append("Participant List:\n");
            details.append("=================\n");

            if (participants.isEmpty()) {
                details.append("No participants registered yet.\n");
            } else {
                for (int i = 0; i < participants.size(); i++) {
                    User participant = participants.get(i);
                    details.append(i + 1).append(". ").append(participant.getName())
                            .append(" (").append(participant.getEmail()).append(")\n");
                }
            }

            eventDetailsArea.setText(details.toString());
        } else {
            eventDetailsArea.setText("Select an event to view details.");
        }
    }

    private void updateStats() {
        List<EventAPI> events = eventManager.getAllEvents();
        List<User> users = eventManager.getAllUsers();

        int totalParticipants = 0;
        for (EventAPI event : events) {
            totalParticipants += eventManager.getEventParticipants(event.getId()).size();
        }

        statsLabel.setText(String.format("Events: %d | Users: %d | Registrations: %d",
                events.size(), users.size(), totalParticipants));
    }

    // -------------------- New User dialog --------------------
    private void openNewUserDialog() {
        JDialog dialog = new JDialog(this, "New User Registration", true);
        dialog.setSize(520, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        header.setBackground(Color.WHITE);
        JLabel h = new JLabel("Create a new user");
        h.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(h, BorderLayout.WEST);
        dialog.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JPanel typeRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typeRow.setOpaque(false);
        typeRow.add(new JLabel("User Type:"));
        String[] userTypes = {"Student", "Organizer"};
        JComboBox<String> typeCombo = new JComboBox<>(userTypes);
        typeRow.add(typeCombo);
        form.add(typeRow);

        // Common fields
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();

        form.add(labeledRow("Full Name:", nameField));
        form.add(labeledRow("Email:", emailField));

        // Student-specific
        JTextField studentIdField = new JTextField();
        JTextField majorField = new JTextField();
        JComboBox<Integer> yearCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4});

        JPanel studentPanel = new JPanel();
        studentPanel.setOpaque(false);
        studentPanel.setLayout(new BoxLayout(studentPanel, BoxLayout.Y_AXIS));
        studentPanel.add(labeledRow("Student ID:", studentIdField));
        studentPanel.add(labeledRow("Major:", majorField));
        JPanel yearRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        yearRow.setOpaque(false);
        yearRow.add(new JLabel("Year:"));
        yearCombo.setPreferredSize(new Dimension(70, 24));
        yearRow.add(yearCombo);
        studentPanel.add(yearRow);

        // Organizer-specific
        JTextField deptField = new JTextField();
        JTextField roleField = new JTextField();
        JPanel organizerPanel = new JPanel();
        organizerPanel.setOpaque(false);
        organizerPanel.setLayout(new BoxLayout(organizerPanel, BoxLayout.Y_AXIS));
        organizerPanel.add(labeledRow("Department:", deptField));
        organizerPanel.add(labeledRow("Role:", roleField));

        // Card layout to switch panels
        JPanel cardHolder = new JPanel(new CardLayout());
        cardHolder.setOpaque(false);
        cardHolder.add(studentPanel, "Student");
        cardHolder.add(organizerPanel, "Organizer");
        form.add(cardHolder);

        typeCombo.addActionListener(ae -> {
            CardLayout cl = (CardLayout) cardHolder.getLayout();
            cl.show(cardHolder, (String) typeCombo.getSelectedItem());
        });

        dialog.add(form, BorderLayout.CENTER);

        // Footer with action buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.WHITE);
        JButton cancelBtn = new JButton("Cancel");
        JButton createBtn = new JButton("Create User");
        stylePrimaryButton(createBtn);
        cancelBtn.addActionListener(e -> dialog.dispose());

        createBtn.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name and Email are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success = false;
            if ("Student".equals(type)) {
                String studentId = studentIdField.getText().trim();
                if (studentId.isEmpty()) studentId = "SID" + System.currentTimeMillis() % 100000;
                String major = majorField.getText().trim();
                int year = (Integer) yearCombo.getSelectedItem();
                String uid = generateId("STU");
                Student s = new Student(uid, name, email, studentId, major, year);
                // register with EventManager
                success = eventManager.registerUser(s);
            } else {
                String dept = deptField.getText().trim();
                String role = roleField.getText().trim();
                String uid = generateId("ORG");
                Organizer o = new Organizer(uid, name, email, dept, role);
                success = eventManager.registerUser(o);
            }

            if (success) {
                JOptionPane.showMessageDialog(dialog, "User created and registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to register user. Check logs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        footer.add(cancelBtn);
        footer.add(createBtn);

        dialog.add(footer, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JPanel labeledRow(String label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(8, 4));
        row.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setPreferredSize(new Dimension(100, 24));
        row.add(l, BorderLayout.WEST);
        field.setPreferredSize(new Dimension(320, 24));
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private String generateId(String prefix) {
        return prefix + "_" + System.currentTimeMillis();
    }

    // -------------------- Create new event --------------------
    private void openCreateEventDialog() {
        JDialog dialog = new JDialog(this, "Create New Event", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Form fields
        JTextField titleField = new JTextField();
        JTextArea descriptionArea = new JTextArea(3, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        
        JTextField dateField = new JTextField("YYYY-MM-DD");
        JTextField locationField = new JTextField();
        JTextField capacityField = new JTextField();
        
        String[] eventTypes = {"lecture", "seminar", "workshop"};
        JComboBox<String> eventTypeCombo = new JComboBox<>(eventTypes);
        
        String[] organizerOptions = {"Select an organizer..."};
        List<User> organizers = eventManager.getAllUsers().stream()
                .filter(u -> u instanceof Organizer)
                .collect(java.util.stream.Collectors.toList());
        
        for (User org : organizers) {
            organizerOptions = java.util.Arrays.copyOf(organizerOptions, organizerOptions.length + 1);
            organizerOptions[organizerOptions.length - 1] = org.getName() + " (" + org.getId() + ")";
        }
        
        JComboBox<String> organizerCombo = new JComboBox<>(organizerOptions);
        JTextField streamLinkField = new JTextField();

        // Create form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        
        formPanel.add(labeledRow("Event Type:", eventTypeCombo));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(labeledRow("Title:", titleField));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(labeledRow("Description:", descriptionScroll));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(labeledRow("Date (YYYY-MM-DD):", dateField));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(labeledRow("Location:", locationField));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(labeledRow("Capacity:", capacityField));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(labeledRow("Organizer:", organizerCombo));
        formPanel.add(labeledRow("Stream Link (Optional):", streamLinkField));
        formPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton cancelBtn = new JButton("Cancel");
        JButton createBtn = new JButton("Create Event");
        stylePrimaryButton(createBtn);
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        createBtn.addActionListener(e -> {
            // Validation
            if (titleField.getText().trim().isEmpty() || 
                descriptionArea.getText().trim().isEmpty() ||
                dateField.getText().trim().isEmpty() ||
                locationField.getText().trim().isEmpty() ||
                capacityField.getText().trim().isEmpty() ||
                organizerCombo.getSelectedIndex() <= 0) {
                
                JOptionPane.showMessageDialog(dialog, 
                    "Please fill in all fields and select an organizer.", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Parse inputs
                String eventType = (String) eventTypeCombo.getSelectedItem();
                String title = titleField.getText().trim();
                String description = descriptionArea.getText().trim();
                LocalDate date = LocalDate.parse(dateField.getText().trim());
                String location = locationField.getText().trim();
                int capacity = Integer.parseInt(capacityField.getText().trim());
                String streamLink = streamLinkField.getText().trim();

                if (capacity <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Capacity must be positive.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Get organizer ID
                String organizerText = (String) organizerCombo.getSelectedItem();
                String organizerId = organizerText.substring(organizerText.lastIndexOf("(") + 1, organizerText.lastIndexOf(")"));
                
                // Create event using facade
                String eventId = CampusEventSystemFacade.getInstance().createEvent(
                    organizerId, eventType, title, description, date, location, capacity, streamLink);
                
                if (eventId != null) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Event created successfully! Event ID: " + eventId, 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    refreshData(); // Refresh the UI
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "Failed to create event. Please try again.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Error: " + ex.getMessage() + "\nPlease check your input format.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(createBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    // -------------------- Register existing user for selected event --------------------
    private void openRegisterForEventDialog() {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an event first.", "No Event Selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String eventId = (String) eventsTable.getValueAt(selectedRow, 0);
        EventAPI event = eventManager.getEvent(eventId);

        JDialog dialog = new JDialog(this, "Register User for: " + event.getTitle(), true);
        dialog.setSize(520, 220);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        p.setBackground(Color.WHITE);

        List<User> users = eventManager.getAllUsers();
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users available. Create a user first.", "No Users", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JComboBox<String> userCombo = new JComboBox<>();
        for (User u : users) {
            userCombo.addItem(u.getName() + " (" + u.getClass().getSimpleName() + ") - " + u.getEmail() + " [" + u.getId() + "]");
        }

        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        center.setOpaque(false);
        center.add(new JLabel("Choose user:"));
        userCombo.setPreferredSize(new Dimension(380, 28));
        center.add(userCombo);

        p.add(center, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        JButton cancel = new JButton("Cancel");
        JButton registerBtn = new JButton("Register");
        stylePrimaryButton(registerBtn);
        cancel.addActionListener(e -> dialog.dispose());

        registerBtn.addActionListener(e -> {
            int idx = userCombo.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(dialog, "Select a user.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            User chosen = users.get(idx);
            boolean ok = eventManager.registerUserForEvent(chosen.getId(), eventId);
            if (ok) {
                JOptionPane.showMessageDialog(dialog, chosen.getName() + " registered for " + event.getTitle(), "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Registration failed (maybe capacity reached or already registered).", "Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        footer.add(cancel);
        footer.add(registerBtn);

        dialog.add(p, BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }



    // ==================== Send Notification to Selected User ====================
    
    private void openSendNotificationToSelectedUser() {
        int selectedRow = participantsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select a user from the participants table first.", 
                "No User Selected", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String userName = (String) participantsTable.getValueAt(selectedRow, 0);
        String userEmail = (String) participantsTable.getValueAt(selectedRow, 1);
        
        System.out.println("🔍 [UI] User selected from table: " + userName + " (" + userEmail + ")");
        openSendNotificationDialog(userName, userEmail);
    }

    // ==================== Send Notification to Specific User ====================
    
    private void openSendNotificationDialog(String userName, String userEmail) {
        JDialog dialog = new JDialog(this, "Send Notification to " + userName, true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Show recipient info
        JLabel recipientLabel = new JLabel("Sending to: " + userName + " (" + userEmail + ")");
        recipientLabel.setFont(new Font("Arial", Font.BOLD, 14));
        recipientLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Add note about default values
        JLabel defaultNoteLabel = new JLabel("Note: Default values are pre-filled. You can modify them if needed.");
        defaultNoteLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        defaultNoteLabel.setForeground(new Color(100, 100, 100));
        defaultNoteLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Form fields with default values
        JTextField subjectField = new JTextField("Event coming up");
        JTextArea messageArea = new JTextArea("The event that you have registered for is coming up. Stay tuned!", 4, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScroll = new JScrollPane(messageArea);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(labeledRow("Subject:", subjectField));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(labeledRow("Message:", messageScroll));

        mainPanel.add(recipientLabel, BorderLayout.NORTH);
        mainPanel.add(defaultNoteLabel, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.SOUTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton cancelBtn = new JButton("Cancel");
        JButton sendBtn = new JButton("📧 Send Notification");
        stylePrimaryButton(sendBtn);
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        sendBtn.addActionListener(e -> {
            try {
                String subject = subjectField.getText().trim();
                String message = messageArea.getText().trim();
                
                System.out.println("🔍 [UI] Starting notification process...");
                System.out.println("🔍 [UI] Target user: " + userName + " (" + userEmail + ")");
                System.out.println("🔍 [UI] Subject: " + subject);
                System.out.println("🔍 [UI] Message: " + message);
                
                // Find the user by email and send notification
                List<User> allUsers = eventManager.getAllUsers();
                System.out.println("🔍 [UI] Total users in system: " + allUsers.size());
                
                User targetUser = allUsers.stream()
                    .filter(u -> u.getEmail().equals(userEmail))
                    .findFirst()
                    .orElse(null);
                
                if (targetUser == null) {
                    System.out.println("❌ [UI] User not found in system: " + userEmail);
                    JOptionPane.showMessageDialog(dialog, 
                        "User not found in system.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                System.out.println("✅ [UI] User found: " + targetUser.getName() + " (ID: " + targetUser.getId() + ")");
                System.out.println("🔍 [UI] User type: " + targetUser.getClass().getSimpleName());
                
                // Send notification using the facade
                System.out.println("📤 [UI] Calling CampusEventSystemFacade.sendNotificationToUser()...");
                CampusEventSystemFacade facade = CampusEventSystemFacade.getInstance();
                facade.sendNotificationToUser(targetUser.getId(), subject, message);
                
                System.out.println("✅ [UI] Notification process completed successfully!");
                JOptionPane.showMessageDialog(dialog, 
                    "Notification sent successfully to " + userName + "!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                
            } catch (Exception ex) {
                System.err.println("❌ [UI] Error in notification process: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, 
                    "Error sending notification: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(sendBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        // Set look and feel to system default
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) { }

        SwingUtilities.invokeLater(() -> {
            EventManagementUI ui = new EventManagementUI();
            ui.setVisible(true);
        });
    }
}
