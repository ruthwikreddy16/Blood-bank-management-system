import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BloodBank extends Frame implements ActionListener {
    private Button donateButton, displayButton, removeButton;
    private Label nameLabel, bloodGroupLabel, userIdLabel, contactNumberLabel, ageLabel;
    private TextField nameTextField, bloodGroupTextField, userIdTextField, contactNumberTextField, ageTextField;

    public BloodBank() {
        // Initialize components
        donateButton = new Button("Donate Blood");
        displayButton = new Button("Display Donors");
        removeButton = new Button("Remove Donor");
        nameLabel = new Label("Name:");
        bloodGroupLabel = new Label("Blood Group:");
        userIdLabel = new Label("User ID:");
        contactNumberLabel = new Label("Contact Number:");
        ageLabel = new Label("Age:");
        nameTextField = new TextField();
        bloodGroupTextField = new TextField();
        userIdTextField = new TextField();
        contactNumberTextField = new TextField();
        ageTextField = new TextField();

        // Set layout manager to GridLayout
        setLayout(new GridLayout(8, 2));  // 7 rows, 2 columns

        // Add components to the frame
        add(nameLabel);
        add(nameTextField);
        add(bloodGroupLabel);
        add(bloodGroupTextField);
        add(contactNumberLabel);
        add(contactNumberTextField);
        add(ageLabel);
        add(ageTextField);

        // Add empty labels for spacing
        add(new Label(""));
        add(new Label(""));

        // Add buttons
        add(donateButton);
        add(displayButton);
        add(removeButton);

        // Register event listeners for the buttons
        donateButton.addActionListener(this);
        displayButton.addActionListener(this);
        removeButton.addActionListener(this);

        // Set frame properties (title, size, etc.)
        setTitle("Blood Bank Management System");
        setSize(500, 500);
        setLocationRelativeTo(null);  // Center the frame on the screen
        setResizable(false);  // Prevent resizing
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == donateButton) {
            String donorName = nameTextField.getText();
            String bloodGroup = bloodGroupTextField.getText();
            String contactNumber = contactNumberTextField.getText();
            int age = Integer.parseInt(ageTextField.getText());

            // Call the backend to insert donor information into the database and get the user ID
            int userId = BloodBankDatabase.insertDonor(donorName, bloodGroup, contactNumber, age);

            // Display the user ID
            userIdTextField.setText(String.valueOf(userId));

            // You might want to display a confirmation message or update the GUI accordingly
            System.out.println("Donation recorded for: " + donorName + " with User ID: " + userId);
        } else if (e.getSource() == displayButton) {
            // Call the backend to display all donors with their details
            displayAllDonors();
        } else if (e.getSource() == removeButton) {
            // Call the backend to remove a donor
            removeDonor();
        }
    }

    private void displayAllDonors() {
        try {
            ResultSet resultSet = BloodBankDatabase.getAllDonors();

            System.out.println("All Donors:");
            while (resultSet.next()) {
                int count = resultSet.getInt("name");
                String bloodGroup = resultSet.getString("blood_group");

                System.out.println("blood_group: " + bloodGroup + ", no.of donors " + count );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void removeDonor() {
        // For simplicity, let's assume you have a donor ID and use it to remove the donor

        // Create a simple dialog box for user input
        Frame inputFrame = new Frame("Enter User ID to Remove");
        Label label = new Label("User ID:");
        TextField textField = new TextField();
        Button okButton = new Button("OK");

        inputFrame.setLayout(new GridLayout(2, 2));
        inputFrame.add(label);
        inputFrame.add(textField);
        inputFrame.add(new Label(""));
        inputFrame.add(okButton);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userIdToRemove = textField.getText();
                inputFrame.dispose(); // Close the input frame

                if (!userIdToRemove.isEmpty()) {
                    try {
                        int userId = Integer.parseInt(userIdToRemove);
                        boolean removed = BloodBankDatabase.removeDonor(userId);

                        if (removed) {
                            System.out.println("Donor with User ID " + userId + " removed successfully.");
                        } else {
                            System.out.println("Donor with User ID " + userId + " not found or removal failed.");
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid input for User ID.");
                    }
                }
            }
        });

        inputFrame.setSize(300, 100);
        inputFrame.setLocationRelativeTo(this);
        inputFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new BloodBank();
    }
}
