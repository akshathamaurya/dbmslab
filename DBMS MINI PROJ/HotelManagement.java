
package miniproject01;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class HotelManagement extends JFrame {

    private JTextField idField, nameField, phoneField, emailField;
    private JTable guestTable;
    private DefaultTableModel model;

    // Database connection
    private final String url =
            "jdbc:mysql://localhost:3306/hotel_management";
    private final String username = "root";
    private final String password = "Adhyacvgowda@1234";

    // GRAND PALACE theme colors
    private final Color navy = new Color(15, 25, 45);
    private final Color panelColor = new Color(24, 38, 62);
    private final Color gold = new Color(212, 175, 55);
    private final Color lightGold = new Color(245, 222, 150);
    private final Color white = Color.WHITE;
    private final Color tableBackground = new Color(248, 248, 245);

    public HotelManagement() {
        setTitle("GRAND PALACE - Guest Management");
        setSize(1000, 650);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(navy);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Heading
        JLabel heading = new JLabel(
                "GRAND PALACE", SwingConstants.CENTER
        );
        heading.setFont(new Font("Serif", Font.BOLD, 30));
        heading.setForeground(gold);

        JLabel subHeading = new JLabel(
                "GUEST MANAGEMENT", SwingConstants.CENTER
        );
        subHeading.setFont(new Font("Arial", Font.BOLD, 16));
        subHeading.setForeground(white);

        JPanel headingPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headingPanel.setBackground(navy);
        headingPanel.add(heading);
        headingPanel.add(subHeading);

        mainPanel.add(headingPanel, BorderLayout.NORTH);

        // Guest details form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 12, 12));
        formPanel.setBackground(panelColor);
        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(gold, 1),
                        "Guest Details",
                        0, 0,
                        new Font("Arial", Font.BOLD, 14),
                        gold
                )
        );

        idField = createTextField();
        nameField = createTextField();
        phoneField = createTextField();
        emailField = createTextField();

        formPanel.add(createLabel("Guest ID:"));
        formPanel.add(idField);

        formPanel.add(createLabel("Guest Name:"));
        formPanel.add(nameField);

        formPanel.add(createLabel("Phone:"));
        formPanel.add(phoneField);

        formPanel.add(createLabel("Email:"));
        formPanel.add(emailField);

        // Buttons
        JButton addButton = createButton("Add Guest");
        JButton updateButton = createButton("Update Guest");
        JButton deleteButton = createButton("Delete Guest");
        JButton clearButton = createButton("Clear Fields");
        JButton refreshButton = createButton("Refresh Table");

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );
        buttonPanel.setBackground(navy);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        JPanel formSection = new JPanel(new BorderLayout(10, 10));
        formSection.setBackground(navy);
        formSection.add(formPanel, BorderLayout.CENTER);
        formSection.add(buttonPanel, BorderLayout.SOUTH);

        // Guest table
        model = new DefaultTableModel(
                new String[]{"Guest ID", "Guest Name", "Phone", "Email"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        guestTable = new JTable(model);
        guestTable.setRowHeight(28);
        guestTable.setFont(new Font("Arial", Font.PLAIN, 13));
        guestTable.setBackground(tableBackground);
        guestTable.setForeground(navy);
        guestTable.setSelectionBackground(lightGold);
        guestTable.setSelectionForeground(navy);
        guestTable.setGridColor(new Color(220, 220, 210));
        guestTable.setAutoCreateRowSorter(true);
        guestTable.setFillsViewportHeight(true);

        JTableHeader tableHeader = guestTable.getTableHeader();
        tableHeader.setBackground(gold);
        tableHeader.setForeground(navy);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 13));
        tableHeader.setPreferredSize(new Dimension(0, 32));

        JScrollPane scrollPane = new JScrollPane(guestTable);
        scrollPane.getViewport().setBackground(tableBackground);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(gold),
                        "Guest Records",
                        0, 0,
                        new Font("Arial", Font.BOLD, 14),
                        gold
                )
        );

        JPanel contentPanel = new JPanel(new BorderLayout(10, 15));
        contentPanel.setBackground(navy);
        contentPanel.add(formSection, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Button actions
        addButton.addActionListener(e -> addGuest());
        updateButton.addActionListener(e -> updateGuest());
        deleteButton.addActionListener(e -> deleteGuest());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadGuests());

        // Fill form when a table row is selected
        guestTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && guestTable.getSelectedRow() != -1) {

                int selectedRow = guestTable.getSelectedRow();
                int row = guestTable.convertRowIndexToModel(selectedRow);

                idField.setText(
                        model.getValueAt(row, 0).toString()
                );

                nameField.setText(
                        model.getValueAt(row, 1).toString()
                );

                phoneField.setText(
                        model.getValueAt(row, 2) == null
                                ? "" : model.getValueAt(row, 2).toString()
                );

                emailField.setText(
                        model.getValueAt(row, 3) == null
                                ? "" : model.getValueAt(row, 3).toString()
                );
            }
        });

        loadGuests();
    }

    // Create styled labels
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(white);
        return label;
    }

    // Create styled text fields
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBackground(new Color(250, 250, 245));
        field.setForeground(navy);
        field.setCaretColor(navy);
        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(gold),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)
                )
        );
        return field;
    }

    // Create styled buttons
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(gold);
        button.setForeground(navy);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(lightGold),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                )
        );
        return button;
    }

    // Connect to MySQL
    private Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC driver was not found. "
                    + "Check your project libraries.", e
            );
        }

        return DriverManager.getConnection(url, username, password);
    }

    // Validate required fields
    private boolean validateGuestFields() {
        if (nameField.getText().trim().isEmpty()
                || phoneField.getText().trim().isEmpty()
                || emailField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter Guest Name, Phone and Email.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        if (!emailField.getText().trim().contains("@")
                || !emailField.getText().trim().contains(".")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        return true;
    }

    // Add guest
    private void addGuest() {
        if (!validateGuestFields()) {
            return;
        }

        String sql =
                "INSERT INTO guests (name, phone, email) VALUES (?, ?, ?)";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nameField.getText().trim());
            ps.setString(2, phoneField.getText().trim());
            ps.setString(3, emailField.getText().trim());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this, "Guest added successfully!"
            );

            clearFields();
            loadGuests();

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    // Update guest
    private void updateGuest() {
        int guestId = getValidGuestId();
        if (guestId == -1) {
            return;
        }

        if (!validateGuestFields()) {
            return;
        }

        String sql =
                "UPDATE guests SET name=?, phone=?, email=? WHERE guest_id=?";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nameField.getText().trim());
            ps.setString(2, phoneField.getText().trim());
            ps.setString(3, emailField.getText().trim());
            ps.setInt(4, guestId);

            int count = ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    count > 0
                            ? "Guest updated successfully!"
                            : "Guest ID not found."
            );

            if (count > 0) {
                clearFields();
                loadGuests();
            }

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    // Delete guest
    private void deleteGuest() {
        int guestId = getValidGuestId();
        if (guestId == -1) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this guest?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM guests WHERE guest_id=?";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, guestId);
            int count = ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    count > 0
                            ? "Guest deleted successfully!"
                            : "Guest ID not found."
            );

            if (count > 0) {
                clearFields();
                loadGuests();
            }

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    // Validate Guest ID
    private int getValidGuestId() {
        String id = idField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select a guest from the table or enter a Guest ID.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return -1;
        }

        try {
            int guestId = Integer.parseInt(id);

            if (guestId <= 0) {
                throw new NumberFormatException();
            }

            return guestId;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Guest ID must be a valid positive number.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return -1;
        }
    }

    // Load guest records from database
    private void loadGuests() {
        model.setRowCount(0);

        String sql =
                "SELECT guest_id, name, phone, email FROM guests";

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("guest_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email")
                });
            }

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    // Clear form
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        guestTable.clearSelection();
    }

    // Display errors
    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HotelManagement app = new HotelManagement();
            app.setVisible(true);
        });
    }
}
