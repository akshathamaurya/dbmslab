


package miniproject01;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class ReservationManagement extends JFrame {

    private JTextField reservationIdField;
    private JTextField guestIdField;
    private JTextField roomIdField;
    private JTextField checkInField;
    private JTextField checkOutField;

    private JComboBox<String> statusBox;
    private JTable reservationTable;
    private DefaultTableModel model;

    // Premium navy and gold theme
    private final Color background = new Color(16, 24, 39);
    private final Color panelColor = new Color(28, 41, 59);
    private final Color gold = new Color(199, 166, 106);
    private final Color goldLight = new Color(235, 207, 151);
    private final Color white = new Color(245, 247, 250);
    private final Color muted = new Color(183, 194, 210);
    private final Color borderColor = new Color(55, 70, 91);
    private final Color inputColor = new Color(245, 247, 250);

    private final String url =
            "jdbc:mysql://localhost:3306/hotel_management";
    private final String username = "root";

    // Replace locally with your existing MySQL password.
    private final String password = "Adhyacvgowda@1234";

    public ReservationManagement() {
        setTitle("Grand Palace - Reservation Management");
        setSize(1150, 760);
        setMinimumSize(new Dimension(900, 650));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createInterface();
        loadReservations();
    }

    private void createInterface() {

        JPanel mainPanel = new JPanel(new BorderLayout(0, 18));
        mainPanel.setBackground(background);
        mainPanel.setBorder(new EmptyBorder(22, 25, 20, 25));

        // ---------------- HEADER ----------------
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(background);
        headerPanel.setBorder(new EmptyBorder(0, 0, 5, 0));

        JPanel brandPanel = new JPanel(new BorderLayout(12, 0));
        brandPanel.setBackground(background);

        JLabel logoLabel = new JLabel("\u2666");
        logoLabel.setFont(new Font("Serif", Font.BOLD, 38));
        logoLabel.setForeground(gold);
        logoLabel.setPreferredSize(new Dimension(45, 45));

        JPanel brandText = new JPanel();
        brandText.setBackground(background);
        brandText.setLayout(new BoxLayout(brandText, BoxLayout.Y_AXIS));

        JLabel hotelName = new JLabel("GRAND PALACE");
        hotelName.setFont(new Font("Serif", Font.BOLD, 24));
        hotelName.setForeground(goldLight);

        JLabel pageSubtitle = new JLabel("RESERVATION MANAGEMENT");
        pageSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        pageSubtitle.setForeground(muted);

        brandText.add(hotelName);
        brandText.add(Box.createVerticalStrut(4));
        brandText.add(pageSubtitle);

        brandPanel.add(logoLabel, BorderLayout.WEST);
        brandPanel.add(brandText, BorderLayout.CENTER);

        JLabel adminBadge = new JLabel("  BOOKING DESK  ");
        adminBadge.setFont(new Font("SansSerif", Font.BOLD, 11));
        adminBadge.setForeground(goldLight);
        adminBadge.setOpaque(true);
        adminBadge.setBackground(panelColor);
        adminBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(9, 10, 9, 10)
        ));

        headerPanel.add(brandPanel, BorderLayout.WEST);
        headerPanel.add(adminBadge, BorderLayout.EAST);

        // ---------------- PAGE INTRODUCTION ----------------
        JPanel introPanel = new JPanel();
        introPanel.setBackground(background);
        introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.Y_AXIS));
        introPanel.setBorder(new EmptyBorder(12, 0, 8, 0));

        JLabel introLabel = new JLabel("MANAGE YOUR BOOKINGS");
        introLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        introLabel.setForeground(gold);

        JLabel heading = new JLabel("Reservations");
        heading.setFont(new Font("Serif", Font.BOLD, 32));
        heading.setForeground(white);

        JLabel description = new JLabel(
                "Create bookings, manage guest stays and track reservation status."
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 13));
        description.setForeground(muted);

        introPanel.add(introLabel);
        introPanel.add(Box.createVerticalStrut(5));
        introPanel.add(heading);
        introPanel.add(Box.createVerticalStrut(5));
        introPanel.add(description);

        JPanel titleSection = new JPanel(new BorderLayout());
        titleSection.setBackground(background);
        titleSection.add(headerPanel, BorderLayout.NORTH);
        titleSection.add(introPanel, BorderLayout.CENTER);

        // ---------------- FORM FIELDS ----------------
        reservationIdField = createTextField();
        guestIdField = createTextField();
        roomIdField = createTextField();
        checkInField = createTextField();
        checkOutField = createTextField();

        reservationIdField.setToolTipText(
                "Required when updating or cancelling a reservation"
        );
        guestIdField.setToolTipText("Enter an existing Guest ID");
        roomIdField.setToolTipText("Enter an existing Room ID");
        checkInField.setToolTipText("Format: YYYY-MM-DD");
        checkOutField.setToolTipText("Format: YYYY-MM-DD");

        statusBox = new JComboBox<>(new String[]{
            "Booked", "Checked-In", "Checked-Out", "Cancelled"
        });
        styleComboBox(statusBox);

        JPanel formPanel = new JPanel(new GridLayout(3, 4, 15, 13));
        formPanel.setBackground(panelColor);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(18, 18, 18, 18)
        ));

        addFormField(formPanel, "Reservation ID", reservationIdField);
        addFormField(formPanel, "Guest ID", guestIdField);
        addFormField(formPanel, "Room ID", roomIdField);
        addFormField(formPanel, "Check-in Date (YYYY-MM-DD)", checkInField);
        addFormField(formPanel, "Check-out Date (YYYY-MM-DD)", checkOutField);
        addFormField(formPanel, "Reservation Status", statusBox);

        // ---------------- BUTTONS ----------------
        JButton addButton = createButton("Book Room");
        JButton updateButton = createButton("Update Reservation");
        JButton cancelButton = createButton("Cancel Reservation");
        JButton clearButton = createButton("Clear Fields");
        JButton refreshButton = createButton("Refresh Table");

        cancelButton.setBackground(new Color(155, 65, 72));
        cancelButton.setForeground(white);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 12)
        );
        buttonPanel.setBackground(panelColor);
        buttonPanel.setBorder(new EmptyBorder(0, 8, 5, 8));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        JPanel formCard = new JPanel(new BorderLayout(0, 12));
        formCard.setBackground(panelColor);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(5, 5, 0, 5)
        ));

        JLabel formTitle = new JLabel("  RESERVATION DETAILS");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        formTitle.setForeground(goldLight);
        formTitle.setBorder(new EmptyBorder(8, 5, 0, 5));

        formCard.add(formTitle, BorderLayout.NORTH);
        formCard.add(formPanel, BorderLayout.CENTER);
        formCard.add(buttonPanel, BorderLayout.SOUTH);

        // ---------------- TABLE ----------------
        model = new DefaultTableModel(
                new String[]{
                    "Reservation ID",
                    "Guest ID",
                    "Guest Name",
                    "Room ID",
                    "Room Number",
                    "Check-in",
                    "Check-out",
                    "Status"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reservationTable = new JTable(model);
        reservationTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        reservationTable.setRowHeight(29);
        reservationTable.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );
        reservationTable.setBackground(inputColor);
        reservationTable.setForeground(new Color(30, 40, 55));
        reservationTable.setGridColor(new Color(215, 220, 228));
        reservationTable.setSelectionBackground(goldLight);
        reservationTable.setSelectionForeground(background);
        reservationTable.setShowVerticalLines(false);
        reservationTable.setFillsViewportHeight(true);
        reservationTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader tableHeader = reservationTable.getTableHeader();
        tableHeader.setBackground(panelColor);
        tableHeader.setForeground(goldLight);
        tableHeader.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );
        tableHeader.setPreferredSize(new Dimension(0, 35));
        tableHeader.setReorderingAllowed(false);

        // Set comfortable column widths.
        int[] columnWidths = {105, 75, 140, 75, 105, 105, 105, 110};

        for (int i = 0; i < columnWidths.length; i++) {
            reservationTable.getColumnModel()
                    .getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(reservationTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        scrollPane.getViewport().setBackground(inputColor);

        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(background);

        JPanel tableHeading = new JPanel(new BorderLayout());
        tableHeading.setBackground(background);

        JLabel tableTitle = new JLabel("Reservation Records");
        tableTitle.setFont(new Font("Serif", Font.BOLD, 22));
        tableTitle.setForeground(white);

        JLabel tableSubtitle = new JLabel(
                "Select a record to view or update its details."
        );
        tableSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tableSubtitle.setForeground(muted);

        JPanel tableTitleText = new JPanel();
        tableTitleText.setBackground(background);
        tableTitleText.setLayout(new BoxLayout(
                tableTitleText, BoxLayout.Y_AXIS
        ));
        tableTitleText.add(tableTitle);
        tableTitleText.add(Box.createVerticalStrut(4));
        tableTitleText.add(tableSubtitle);

        tableHeading.add(tableTitleText, BorderLayout.WEST);

        tablePanel.add(tableHeading, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // ---------------- FOOTER ----------------
        JLabel footer = new JLabel(
                "GRAND PALACE  |  RESERVATION MANAGEMENT SYSTEM"
        );
        footer.setFont(new Font("SansSerif", Font.PLAIN, 10));
        footer.setForeground(muted);
        footer.setBorder(new EmptyBorder(7, 0, 0, 0));

        JPanel contentPanel = new JPanel(new BorderLayout(0, 16));
        contentPanel.setBackground(background);
        contentPanel.add(formCard, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        mainPanel.add(titleSection, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // ---------------- BUTTON ACTIONS ----------------
        addButton.addActionListener(e -> addReservation());
        updateButton.addActionListener(e -> updateReservation());
        cancelButton.addActionListener(e -> cancelReservation());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadReservations());

        reservationTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && reservationTable.getSelectedRow() != -1) {

                int row = reservationTable.convertRowIndexToModel(
                        reservationTable.getSelectedRow()
                );

                reservationIdField.setText(
                        model.getValueAt(row, 0).toString()
                );
                guestIdField.setText(
                        model.getValueAt(row, 1).toString()
                );
                roomIdField.setText(
                        model.getValueAt(row, 3).toString()
                );
                checkInField.setText(
                        model.getValueAt(row, 5).toString()
                );
                checkOutField.setText(
                        model.getValueAt(row, 6).toString()
                );
                statusBox.setSelectedItem(
                        model.getValueAt(row, 7).toString()
                );
            }
        });
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setForeground(new Color(30, 40, 55));
        field.setBackground(inputColor);
        field.setCaretColor(new Color(30, 40, 55));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(7, 8, 7, 8)
        ));
        return field;
    }

    private void addFormField(
            JPanel panel, String labelText, JComponent input) {

        JPanel fieldPanel = new JPanel(new BorderLayout(0, 6));
        fieldPanel.setBackground(panelColor);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setForeground(muted);

        fieldPanel.add(label, BorderLayout.NORTH);
        fieldPanel.add(input, BorderLayout.CENTER);

        panel.add(fieldPanel);
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setBackground(inputColor);
        combo.setForeground(new Color(30, 40, 55));
        combo.setBorder(BorderFactory.createLineBorder(borderColor));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setForeground(background);
        button.setBackground(gold);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(11, 13, 11, 13));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(goldLight);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!"Cancel Reservation".equals(text)) {
                    button.setBackground(gold);
                } else {
                    button.setBackground(new Color(155, 65, 72));
                }
            }
        });

        return button;
    }

    // ---------------- DATABASE CONNECTION ----------------

    private Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC driver not found in project libraries.", e
            );
        }

        return DriverManager.getConnection(url, username, password);
    }

    // ---------------- VALIDATION ----------------

    private boolean validateReservationFields(
            boolean requireReservationId) {

        if ((requireReservationId
                    && reservationIdField.getText().trim().isEmpty())
                || guestIdField.getText().trim().isEmpty()
                || roomIdField.getText().trim().isEmpty()
                || checkInField.getText().trim().isEmpty()
                || checkOutField.getText().trim().isEmpty()
                || statusBox.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "All reservation fields are compulsory!\n"
                    + "Please fill in every required field.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        try {
            int guestId = Integer.parseInt(
                    guestIdField.getText().trim()
            );
            int roomId = Integer.parseInt(
                    roomIdField.getText().trim()
            );

            if (guestId <= 0 || roomId <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Guest ID and Room ID must be positive numbers.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return false;
            }

            if (requireReservationId) {
                int reservationId = Integer.parseInt(
                        reservationIdField.getText().trim()
                );

                if (reservationId <= 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Reservation ID must be a positive number.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return false;
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter valid numeric IDs.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        return validDates();
    }

    private boolean validDates() {
        try {
            LocalDate checkIn = LocalDate.parse(
                    checkInField.getText().trim()
            );
            LocalDate checkOut = LocalDate.parse(
                    checkOutField.getText().trim()
            );

            if (!checkOut.isAfter(checkIn)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Check-out must be after check-in.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return false;
            }

            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter valid dates in YYYY-MM-DD format, "
                    + "e.g. 2026-11-10.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    // ---------------- GUEST AND ROOM CHECKS ----------------

    private boolean guestAndRoomExist(int guestId, int roomId)
            throws SQLException {

        String guestSql =
                "SELECT guest_id FROM guests WHERE guest_id=?";
        String roomSql =
                "SELECT room_id FROM rooms WHERE room_id=?";

        try (Connection con = connect()) {

            try (PreparedStatement ps = con.prepareStatement(guestSql)) {
                ps.setInt(1, guestId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(
                                this, "Guest ID does not exist."
                        );
                        return false;
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(roomSql)) {
                ps.setInt(1, roomId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(
                                this, "Room ID does not exist."
                        );
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean roomAvailable(
            int roomId,
            LocalDate checkIn,
            LocalDate checkOut,
            int excludeReservationId) throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM reservations "
                + "WHERE room_id=? AND status IN ('Booked','Checked-In') "
                + "AND check_in < ? AND check_out > ? "
                + "AND reservation_id <> ?";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ps.setDate(2, Date.valueOf(checkOut));
            ps.setDate(3, Date.valueOf(checkIn));
            ps.setInt(4, excludeReservationId);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 0;
            }
        }
    }

    // ---------------- ADD RESERVATION ----------------

    private void addReservation() {

        if (!validateReservationFields(false)) {
            return;
        }

        try {
            int guestId = Integer.parseInt(
                    guestIdField.getText().trim()
            );
            int roomId = Integer.parseInt(
                    roomIdField.getText().trim()
            );

            LocalDate checkIn = LocalDate.parse(
                    checkInField.getText().trim()
            );
            LocalDate checkOut = LocalDate.parse(
                    checkOutField.getText().trim()
            );

            if (!guestAndRoomExist(guestId, roomId)) {
                return;
            }

            if (!roomAvailable(roomId, checkIn, checkOut, 0)) {
                JOptionPane.showMessageDialog(
                        this,
                        "This room is already booked for those dates."
                );
                return;
            }

            String sql =
                    "INSERT INTO reservations "
                    + "(guest_id, room_id, check_in, check_out, status) "
                    + "VALUES (?, ?, ?, ?, ?)";

            try (Connection con = connect();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, guestId);
                ps.setInt(2, roomId);
                ps.setDate(3, Date.valueOf(checkIn));
                ps.setDate(4, Date.valueOf(checkOut));
                ps.setString(5, statusBox.getSelectedItem().toString());

                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                    this, "Room reserved successfully!"
            );

            clearFields();
            loadReservations();

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    // ---------------- LOAD RESERVATIONS ----------------

    private void loadReservations() {
        model.setRowCount(0);

        String sql =
                "SELECT r.reservation_id, r.guest_id, g.name, "
                + "r.room_id, rm.room_number, r.check_in, r.check_out, r.status "
                + "FROM reservations r "
                + "JOIN guests g ON r.guest_id = g.guest_id "
                + "JOIN rooms rm ON r.room_id = rm.room_id "
                + "ORDER BY r.reservation_id";

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("reservation_id"),
                    rs.getInt("guest_id"),
                    rs.getString("name"),
                    rs.getInt("room_id"),
                    rs.getInt("room_number"),
                    rs.getDate("check_in"),
                    rs.getDate("check_out"),
                    rs.getString("status")
                });
            }

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    // ---------------- UPDATE RESERVATION ----------------

    private void updateReservation() {

        if (!validateReservationFields(true)) {
            return;
        }

        try {
            int reservationId = Integer.parseInt(
                    reservationIdField.getText().trim()
            );
            int guestId = Integer.parseInt(
                    guestIdField.getText().trim()
            );
            int roomId = Integer.parseInt(
                    roomIdField.getText().trim()
            );

            LocalDate checkIn = LocalDate.parse(
                    checkInField.getText().trim()
            );
            LocalDate checkOut = LocalDate.parse(
                    checkOutField.getText().trim()
            );

            if (!guestAndRoomExist(guestId, roomId)) {
                return;
            }

            String newStatus = statusBox.getSelectedItem().toString();

            if (newStatus.equals("Booked")
                    || newStatus.equals("Checked-In")) {

                if (!roomAvailable(
                        roomId, checkIn, checkOut, reservationId)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "This room is already booked for those dates."
                    );
                    return;
                }
            }

            String sql =
                    "UPDATE reservations SET guest_id=?, room_id=?, "
                    + "check_in=?, check_out=?, status=? "
                    + "WHERE reservation_id=?";

            try (Connection con = connect();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, guestId);
                ps.setInt(2, roomId);
                ps.setDate(3, Date.valueOf(checkIn));
                ps.setDate(4, Date.valueOf(checkOut));
                ps.setString(5, newStatus);
                ps.setInt(6, reservationId);

                int count = ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        count > 0
                        ? "Reservation updated successfully!"
                        : "Reservation ID not found."
                );

                if (count > 0) {
                    clearFields();
                }
            }

            loadReservations();

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    // ---------------- CANCEL RESERVATION ----------------

    private void cancelReservation() {
        if (reservationIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Select a reservation first."
            );
            return;
        }

        int reservationId;

        try {
            reservationId = Integer.parseInt(
                    reservationIdField.getText().trim()
            );

            if (reservationId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid positive Reservation ID.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Cancel this reservation?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "UPDATE reservations SET status='Cancelled' "
                + "WHERE reservation_id=?";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservationId);

            int count = ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    count > 0
                    ? "Reservation cancelled successfully!"
                    : "Reservation ID not found."
            );

            if (count > 0) {
                clearFields();
            }

            loadReservations();

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    // ---------------- CLEAR FIELDS ----------------

    private void clearFields() {
        reservationIdField.setText("");
        guestIdField.setText("");
        roomIdField.setText("");
        checkInField.setText("");
        checkOutField.setText("");
        statusBox.setSelectedIndex(0);
        reservationTable.clearSelection();
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ---------------- MAIN METHOD ----------------

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ReservationManagement().setVisible(true);
        });
    }
}
