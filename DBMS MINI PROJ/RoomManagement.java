
package miniproject01;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class RoomManagement extends JFrame {

    private JTextField roomIdField;
    private JTextField roomNumberField;
    private JTextField roomTypeField;
    private JTextField priceField;
    private JComboBox<String> statusBox;

    private JTable roomTable;
    private DefaultTableModel model;

    private final String url =
            "jdbc:mysql://localhost:3306/hotel_management";
    private final String username = "root";
    private final String password = "Adhyacvgowda@1234";

    // GRAND PALACE theme colors
    private final Color navy = new Color(13, 25, 45);
    private final Color panelColor = new Color(23, 39, 63);
    private final Color fieldColor = new Color(35, 51, 75);
    private final Color gold = new Color(212, 175, 55);
    private final Color lightGold = new Color(244, 218, 142);
    private final Color white = new Color(245, 246, 250);
    private final Color muted = new Color(180, 191, 207);

    public RoomManagement() {
        setTitle("GRAND PALACE | Room Management");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 580));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createInterface();
        loadRooms();
    }

    private Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC driver not found. Check Project Libraries.",
                    e
            );
        }

        return DriverManager.getConnection(url, username, password);
    }

    private void createInterface() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(navy);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(18, 22, 20, 22)
        );

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(navy);

        JLabel hotelName = new JLabel(
                "GRAND PALACE", SwingConstants.CENTER
        );
        hotelName.setFont(new Font("Serif", Font.BOLD, 30));
        hotelName.setForeground(gold);
        hotelName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel heading = new JLabel(
                "ROOM MANAGEMENT", SwingConstants.CENTER
        );
        heading.setFont(new Font("Arial", Font.BOLD, 17));
        heading.setForeground(white);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Manage room details, pricing and availability",
                SwingConstants.CENTER
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(muted);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(hotelName);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(heading);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitle);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Room form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 14, 12));
        formPanel.setBackground(panelColor);
        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(75, 88, 108), 1
                        ),
                        BorderFactory.createEmptyBorder(18, 20, 18, 20)
                )
        );

        roomIdField = new JTextField();
        roomNumberField = new JTextField();
        roomTypeField = new JTextField();
        priceField = new JTextField();

        statusBox = new JComboBox<>(
                new String[]{"Available", "Occupied", "Maintenance"}
        );
        statusBox.setSelectedIndex(0);

        addFormField(
                formPanel, "Room ID (for update/delete)", roomIdField
        );
        addFormField(formPanel, "Room Number", roomNumberField);
        addFormField(formPanel, "Room Type", roomTypeField);
        addFormField(formPanel, "Price per Night (₹)", priceField);
        addFormField(formPanel, "Room Status", statusBox);

        // Buttons
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 12)
        );
        buttonPanel.setBackground(navy);

        JButton addButton = createButton("Add Room", gold, navy);
        JButton updateButton = createButton(
                "Update Room", fieldColor, white
        );
        JButton deleteButton = createButton(
                "Delete Room", new Color(145, 53, 53), white
        );
        JButton clearButton = createButton(
                "Clear Fields", fieldColor, white
        );
        JButton refreshButton = createButton(
                "Refresh Table", fieldColor, white
        );

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setBackground(navy);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Room records table
        model = new DefaultTableModel(
                new String[]{
                    "Room ID",
                    "Room Number",
                    "Room Type",
                    "Price per Night (₹)",
                    "Room Status"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        roomTable = new JTable(model);
        roomTable.setRowHeight(27);
        roomTable.setFont(new Font("Arial", Font.PLAIN, 12));
        roomTable.setForeground(new Color(35, 43, 55));
        roomTable.setBackground(new Color(247, 248, 251));
        roomTable.setSelectionBackground(new Color(242, 224, 168));
        roomTable.setSelectionForeground(navy);
        roomTable.setGridColor(new Color(220, 224, 230));
        roomTable.setShowVerticalLines(false);
        roomTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        roomTable.setFillsViewportHeight(true);
        roomTable.setAutoCreateRowSorter(true);

        JTableHeader tableHeader = roomTable.getTableHeader();
        tableHeader.setBackground(navy);
        tableHeader.setForeground(gold);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setPreferredSize(new Dimension(0, 34));
        tableHeader.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.getViewport().setBackground(white);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                new Color(75, 88, 108)
                        ),
                        " ROOM RECORDS ",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 13),
                        gold
                )
        );
        scrollPane.setBackground(panelColor);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(panelColor);
        tablePanel.setBorder(
                BorderFactory.createEmptyBorder(5, 8, 8, 8)
        );
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, topPanel, tablePanel
        );
        splitPane.setResizeWeight(0.53);
        splitPane.setDividerSize(7);
        splitPane.setBorder(null);
        splitPane.setBackground(navy);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Button actions
        addButton.addActionListener(e -> addRoom());
        updateButton.addActionListener(e -> updateRoom());
        deleteButton.addActionListener(e -> deleteRoom());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadRooms());

        // Selecting a table row fills the form.
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedRoom();
            }
        });
    }

    private void addFormField(
            JPanel panel, String labelText, JComponent field) {

        JLabel label = new JLabel(labelText + ":");
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(lightGold);

        styleField(field);

        panel.add(label);
        panel.add(field);
    }

    private void styleField(JComponent field) {
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setForeground(white);
        field.setBackground(fieldColor);
        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(80, 96, 119)
                        ),
                        BorderFactory.createEmptyBorder(6, 8, 6, 8)
                )
        );

        if (field instanceof JComboBox) {
            JComboBox<?> combo = (JComboBox<?>) field;
            combo.setForeground(white);
            combo.setBackground(fieldColor);
        }
    }

    private JButton createButton(
            String text, Color background, Color foreground) {

        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(
                BorderFactory.createEmptyBorder(11, 15, 11, 15)
        );

        return button;
    }

    // Validate compulsory room details.
    private boolean validateRoomFields() {
        if (roomNumberField.getText().trim().isEmpty()
                || roomTypeField.getText().trim().isEmpty()
                || priceField.getText().trim().isEmpty()
                || statusBox.getSelectedItem() == null) {

            showValidationMessage(
                    "All room fields are compulsory.\n"
                    + "Enter Room Number, Room Type, Price and Status."
            );
            return false;
        }

        try {
            int roomNumber = Integer.parseInt(
                    roomNumberField.getText().trim()
            );

            if (roomNumber <= 0) {
                showValidationMessage(
                        "Room Number must be a positive whole number."
                );
                roomNumberField.requestFocus();
                return false;
            }

        } catch (NumberFormatException ex) {
            showValidationMessage(
                    "Room Number must be a valid whole number."
            );
            roomNumberField.requestFocus();
            return false;
        }

        try {
            double price = Double.parseDouble(
                    priceField.getText().trim()
            );

            if (!Double.isFinite(price) || price <= 0) {
                showValidationMessage(
                        "Price must be a positive number."
                );
                priceField.requestFocus();
                return false;
            }

        } catch (NumberFormatException ex) {
            showValidationMessage(
                    "Enter a valid price, such as 2500."
            );
            priceField.requestFocus();
            return false;
        }

        return true;
    }

    private void showValidationMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Room Validation",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void addRoom() {
        if (!validateRoomFields()) {
            return;
        }

        String sql = "INSERT INTO rooms "
                + "(room_number, room_type, price, status) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    Integer.parseInt(roomNumberField.getText().trim())
            );
            ps.setString(2, roomTypeField.getText().trim());
            ps.setDouble(
                    3,
                    Double.parseDouble(priceField.getText().trim())
            );
            ps.setString(4, statusBox.getSelectedItem().toString());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Room added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();
            loadRooms();

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void loadRooms() {
        model.setRowCount(0);

        String sql = "SELECT room_id, room_number, room_type, "
                + "price, status FROM rooms ORDER BY room_number";

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("room_id"),
                    rs.getInt("room_number"),
                    rs.getString("room_type"),
                    rs.getBigDecimal("price"),
                    rs.getString("status")
                });
            }

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void showSelectedRoom() {
        int selectedRow = roomTable.getSelectedRow();

        if (selectedRow < 0) {
            return;
        }

        int row = roomTable.convertRowIndexToModel(selectedRow);

        roomIdField.setText(model.getValueAt(row, 0).toString());
        roomNumberField.setText(model.getValueAt(row, 1).toString());
        roomTypeField.setText(model.getValueAt(row, 2).toString());
        priceField.setText(model.getValueAt(row, 3).toString());
        statusBox.setSelectedItem(model.getValueAt(row, 4).toString());
    }

    private void updateRoom() {
        if (roomIdField.getText().trim().isEmpty()) {
            showValidationMessage(
                    "Select a room from the table first."
            );
            return;
        }

        if (!validateRoomFields()) {
            return;
        }

        int roomId;

        try {
            roomId = Integer.parseInt(roomIdField.getText().trim());

            if (roomId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {
            showValidationMessage(
                    "Room ID must be a valid positive whole number."
            );
            roomIdField.requestFocus();
            return;
        }

        String sql = "UPDATE rooms SET room_number=?, room_type=?, "
                + "price=?, status=? WHERE room_id=?";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    Integer.parseInt(roomNumberField.getText().trim())
            );
            ps.setString(2, roomTypeField.getText().trim());
            ps.setDouble(
                    3,
                    Double.parseDouble(priceField.getText().trim())
            );
            ps.setString(4, statusBox.getSelectedItem().toString());
            ps.setInt(5, roomId);

            int count = ps.executeUpdate();

            if (count > 0) {
                JOptionPane.showMessageDialog(
                        this, "Room updated successfully!"
                );
                clearFields();
                loadRooms();
            } else {
                showValidationMessage("Room ID not found.");
            }

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void deleteRoom() {
        if (roomIdField.getText().trim().isEmpty()) {
            showValidationMessage(
                    "Select a room from the table first."
            );
            return;
        }

        int roomId;

        try {
            roomId = Integer.parseInt(roomIdField.getText().trim());

            if (roomId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {
            showValidationMessage(
                    "Room ID must be a valid positive whole number."
            );
            roomIdField.requestFocus();
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this room?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM rooms WHERE room_id=?";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            int count = ps.executeUpdate();

            if (count > 0) {
                JOptionPane.showMessageDialog(
                        this, "Room deleted successfully!"
                );
                clearFields();
                loadRooms();
            } else {
                showValidationMessage("Room ID not found.");
            }

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void clearFields() {
        roomIdField.setText("");
        roomNumberField.setText("");
        roomTypeField.setText("");
        priceField.setText("");
        statusBox.setSelectedIndex(0);
        roomTable.clearSelection();
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                "Error:\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RoomManagement().setVisible(true);
        });
    }
}
