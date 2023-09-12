import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactManagementSystem extends JFrame {
    private static final String CONTACTS_FILE = "contacts.txt";
    private List<Contact> contacts;
    private Set<String> phoneNumberSet;

    public ContactManagementSystem() {
        setTitle("Contact_Book");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));

        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(Color.BLACK);
        welcomePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel welcomeLabel = new JLabel("Welcome to Phonebook");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));

        welcomePanel.add(welcomeLabel);

        add(welcomePanel, BorderLayout.NORTH);

        JPanel functionalityPanel = new JPanel();
        functionalityPanel.setLayout(new FlowLayout());
        functionalityPanel.setBackground(Color.WHITE);
        functionalityPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(functionalityPanel, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Contact");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Enter the name:");
                String phonePattern = "\\d{10}";
                String phoneNumber = JOptionPane.showInputDialog("Enter the phone number (10 digits):");
                while (!phoneNumber.matches(phonePattern) || phoneNumberSet.contains(phoneNumber)) {
                    phoneNumber = JOptionPane.showInputDialog(
                            "Invalid phone number. Please enter 10 digits or a number that doesn't already exist:");
                }
                phoneNumberSet.add(phoneNumber);
                String emailPattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
                String email = JOptionPane.showInputDialog("Enter the email:");
                while (!email.matches(emailPattern)) {
                    email = JOptionPane.showInputDialog("Invalid email. Please enter a valid email:");
                }
                String address = JOptionPane.showInputDialog("Enter the address:");
                Contact contact = new Contact(name, phoneNumber, email, address);
                contacts.add(contact);
                saveContacts();
                JOptionPane.showMessageDialog(null, "Contact added successfully.");
            }
        });

        JButton updateButton = new JButton("Update Contact");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Enter the name of the contact to update:");
                List<Contact> searchResults = new ArrayList<>();
                for (Contact contact : contacts) {
                    if (contact.getName().equalsIgnoreCase(name)) {
                        searchResults.add(contact);
                    }
                }

                if (searchResults.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No contacts found with the provided name.");
                } else {
                    Contact contact = (Contact) JOptionPane.showInputDialog(null,
                            "Select a contact to update:", "Update Contact",
                            JOptionPane.QUESTION_MESSAGE, null, searchResults.toArray(), searchResults.get(0));

                    if (contact != null) {
                        String[] options = { "Name", "Phone Number", "Email" };
                        int choice = JOptionPane.showOptionDialog(null, "Choose the field to update:",
                                "Update Contact", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                                null, options, options[0]);

                        String updatedValue = JOptionPane.showInputDialog("Enter the updated value:");

                        if (choice == 0) {
                            contact.setName(updatedValue);
                        } else if (choice == 1) {
                            String phonePattern = "\\d{10}";
                            while (!updatedValue.matches(phonePattern) || phoneNumberSet.contains(updatedValue)) {
                                updatedValue = JOptionPane.showInputDialog(
                                        "Invalid phone number. Please enter 10 digits or a number that doesn't already exist:");
                            }
                            phoneNumberSet.remove(contact.getPhoneNumber());
                            contact.setPhoneNumber(updatedValue);
                            phoneNumberSet.add(updatedValue);
                        } else if (choice == 2) {
                            String emailPattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
                            while (!updatedValue.matches(emailPattern)) {
                                updatedValue = JOptionPane
                                        .showInputDialog("Invalid email. Please enter a valid email:");

                            }
                            contact.setEmail(updatedValue);
                        } else if (choice == 3) {
                            contact.setName(updatedValue);
                        }

                    }

                    saveContacts();
                    JOptionPane.showMessageDialog(null, "Contact updated successfully.");
                }
            }
        });

        JButton displayButton = new JButton("Display Contacts");
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contacts.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No contacts found.");
                } else {
                    StringBuilder contactInfo = new StringBuilder();
                    for (Contact contact : contacts) {
                        contactInfo.append("Name: ").append(contact.getName()).append("\n");
                        contactInfo.append("Phone Number: ").append(contact.getPhoneNumber()).append("\n");
                        contactInfo.append("Email: ").append(contact.getEmail()).append("\n");
                        contactInfo.append("Address: ").append(contact.getAddress()).append("\n");
                        contactInfo.append("\n");
                    }
                    JOptionPane.showMessageDialog(null, contactInfo.toString());
                }
            }
        });

        JButton deleteButton = new JButton("Delete Contact");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Enter the name of the contact to delete:");
                List<Contact> searchResults = new ArrayList<>();
                for (Contact contact : contacts) {
                    if (contact.getName().equalsIgnoreCase(name)) {
                        searchResults.add(contact);
                    }
                }

                if (searchResults.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No contacts found with the provided name.");
                } else {
                    int confirmation = JOptionPane.showConfirmDialog(null,
                            "are you sure you want to delete this contact");
                    // Contact contact = (Contact) JOptionPane.showInputDialog(null,
                    if (confirmation == JOptionPane.YES_OPTION) {
                        contacts.remove(searchResults.get(0));
                        saveContacts();
                        JOptionPane.showMessageDialog(null, "contact deleted succcesfully");
                    }

                }
            }
        });

        JButton searchButton = new JButton("Search Contacts");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Enter the name to search for:");
                List<Contact> searchResults = new ArrayList<>();
                for (Contact contact : contacts) {
                    if (contact.getName().equalsIgnoreCase(name)) {
                        searchResults.add(contact);
                    }
                }

                if (searchResults.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No contacts found with the provided name.");
                } else {
                    StringBuilder searchInfo = new StringBuilder();
                    for (Contact contact : searchResults) {
                        searchInfo.append("Name: ").append(contact.getName()).append("\n");
                        searchInfo.append("Phone Number: ").append(contact.getPhoneNumber()).append("\n");
                        searchInfo.append("Email: ").append(contact.getEmail()).append("\n");
                        searchInfo.append("Address: ").append(contact.getAddress()).append("\n");
                        searchInfo.append("\n");
                    }
                    JOptionPane.showMessageDialog(null, searchInfo.toString());
                }
            }
        });

        JButton exitButton = new JButton("exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        functionalityPanel.add(addButton);
        functionalityPanel.add(updateButton);
        functionalityPanel.add(displayButton);
        functionalityPanel.add(deleteButton);
        functionalityPanel.add(searchButton);

        loadContacts();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadContacts() {
        contacts = new ArrayList<>();
        phoneNumberSet = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CONTACTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] contactData = line.split(",");
                if (contactData.length == 4) {
                    String name = contactData[0];
                    String phoneNumber = contactData[1];
                    String email = contactData[2];
                    String address = contactData[3];
                    Contact contact = new Contact(name, phoneNumber, email, address);
                    contacts.add(contact);
                    phoneNumberSet.add(phoneNumber);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }
    }

    private void saveContacts() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONTACTS_FILE))) {
            for (Contact contact : contacts) {
                String line = contact.getName() + "," + contact.getPhoneNumber() + "," + contact.getEmail() + ","
                        + contact.getAddress();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ContactManagementSystem();
            }
        });
    }
}
