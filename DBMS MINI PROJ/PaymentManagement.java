



package miniproject01;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PaymentManagement extends JFrame {

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

    private JTextField paymentIdField;
    private JTextField reservationIdField;
    private JTextField amountField;
    private JTextField paymentDateField;

    private JComboBox<String> paymentStatusBox;
    private JTable paymentTable;
    private DefaultTableModel tableModel;

    public PaymentManagement() {
        setTitle("GRAND PALACE | Payment Management");
        setSize(1150, 720);
        setMinimumSize(new Dimension(950, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createInterface();
        loadPayments();
    }

    private Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC driver not found. Check Project Libraries.", e
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
                "PAYMENT MANAGEMENT", SwingConstants.CENTER
        );
        heading.setFont(new Font("Arial", Font.BOLD, 17));
        heading.setForeground(white);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Manage guest payments and transaction records",
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

        // Payment form
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

        paymentIdField = new JTextField();
        paymentIdField.setEditable(false);

        reservationIdField = new JTextField();
        amountField = new JTextField();
        paymentDateField = new JTextField();

        paymentStatusBox = new JComboBox<>(
                new String[]{"Pending", "Paid", "Failed", "Refunded"}
        );
        paymentStatusBox.setSelectedIndex(-1);

        addFormField(formPanel, "Payment ID", paymentIdField);
        addFormField(formPanel, "Reservation ID", reservationIdField);
        addFormField(formPanel, "Amount (₹)", amountField);
        addFormField(formPanel, "Payment Status", paymentStatusBox);
        addFormField(formPanel, "Payment Date (YYYY-MM-DD)", paymentDateField);

        // Buttons
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 12)
        );
        buttonPanel.setBackground(navy);

        JButton addButton = createButton("Record Payment", gold, navy);
        JButton updateButton = createButton("Update Payment", fieldColor, white);
        JButton deleteButton = createButton("Delete Payment",
                new Color(145, 53, 53), white);
        JButton clearButton = createButton("Clear Fields", fieldColor, white);
        JButton refreshButton = createButton("Refresh Table", fieldColor, white);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setBackground(navy);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Payment table
        tableModel = new DefaultTableModel(
                new String[]{
                    "Payment ID",
                    "Reservation ID",
                    "Guest Name",
                    "Room Number",
                    "Amount (₹)",
                    "Payment Status",
                    "Payment Date"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        paymentTable = new JTable(tableModel);
        paymentTable.setRowHeight(27);
        paymentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        paymentTable.setForeground(new Color(35, 43, 55));
        paymentTable.setBackground(new Color(247, 248, 251));
        paymentTable.setSelectionBackground(new Color(242, 224, 168));
        paymentTable.setSelectionForeground(navy);
        paymentTable.setGridColor(new Color(220, 224, 230));
        paymentTable.setShowVerticalLines(false);
        paymentTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        paymentTable.setFillsViewportHeight(true);
        paymentTable.setAutoCreateRowSorter(true);

        JTableHeader tableHeader = paymentTable.getTableHeader();
        tableHeader.setBackground(navy);
        tableHeader.setForeground(gold);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setPreferredSize(new Dimension(0, 34));
        tableHeader.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.getViewport().setBackground(white);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                new Color(75, 88, 108)
                        ),
                        " PAYMENT RECORDS ",
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
        addButton.addActionListener(e -> recordPayment());
        updateButton.addActionListener(e -> updatePayment());
        deleteButton.addActionListener(e -> deletePayment());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadPayments());

        paymentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedPayment();
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

    // Validate all required fields before INSERT or UPDATE.
    private boolean validateFields() {
        if (reservationIdField.getText().trim().isEmpty()) {
            showValidationMessage("Reservation ID is compulsory.");
            reservationIdField.requestFocus();
            return false;
        }

        if (amountField.getText().trim().isEmpty()) {
            showValidationMessage("Amount is compulsory.");
            amountField.requestFocus();
            return false;
        }

        if (paymentStatusBox.getSelectedItem() == null) {
            showValidationMessage("Please select a Payment Status.");
            paymentStatusBox.requestFocus();
            return false;
        }

        String dateText = paymentDateField.getText().trim();

        if (dateText.isEmpty()) {
            showValidationMessage("Payment Date is compulsory.");
            paymentDateField.requestFocus();
            return false;
        }

        int reservationId;

        try {
            reservationId = Integer.parseInt(
                    reservationIdField.getText().trim()
            );

            if (reservationId <= 0) {
                showValidationMessage(
                        "Reservation ID must be a positive whole number."
                );
                reservationIdField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showValidationMessage(
                    "Enter a valid whole number for Reservation ID."
            );
            reservationIdField.requestFocus();
            return false;
        }

        try {
            double amount = Double.parseDouble(amountField.getText().trim());

            if (!Double.isFinite(amount) || amount <= 0) {
                showValidationMessage(
                        "Amount must be a positive number."
                );
                amountField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showValidationMessage(
                    "Enter a valid amount, such as 2500."
            );
            amountField.requestFocus();
            return false;
        }

        try {
            LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            showValidationMessage(
                    "Enter a valid payment date in YYYY-MM-DD format."
            );
            paymentDateField.requestFocus();
            return false;
        }

        return true;
    }

    private void showValidationMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Payment Validation",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private boolean reservationExists(int reservationId)
            throws SQLException {

        String sql =
                "SELECT reservation_id FROM reservations " +
                "WHERE reservation_id = ?";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservationId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void recordPayment() {
        if (!validateFields()) {
            return;
        }

        int reservationId = Integer.parseInt(
                reservationIdField.getText().trim()
        );
        double amount = Double.parseDouble(amountField.getText().trim());
        String status = (String) paymentStatusBox.getSelectedItem();
        LocalDate paymentDate = LocalDate.parse(
                paymentDateField.getText().trim()
        );

        try {
            if (!reservationExists(reservationId)) {
                showValidationMessage(
                        "Reservation ID does not exist. Enter an existing Reservation ID."
                );
                return;
            }

            String sql =
                    "INSERT INTO payments " +
                    "(reservation_id, amount, payment_status, payment_date) " +
                    "VALUES (?, ?, ?, ?)";

            try (Connection con = connect();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, reservationId);
                ps.setDouble(2, amount);
                ps.setString(3, status);
                ps.setDate(4, Date.valueOf(paymentDate));
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Payment recorded successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();
            loadPayments();

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void updatePayment() {
        if (paymentIdField.getText().trim().isEmpty()) {
            showValidationMessage(
                    "Select a payment from the table first."
            );
            return;
        }

        if (!validateFields()) {
            return;
        }

        int paymentId;

        try {
            paymentId = Integer.parseInt(paymentIdField.getText().trim());

            if (paymentId <= 0) {
                showValidationMessage("Select a valid payment record.");
                return;
            }
        } catch (NumberFormatException e) {
            showValidationMessage(
                    "Invalid Payment ID. Select a payment from the table."
            );
            return;
        }

        int reservationId = Integer.parseInt(
                reservationIdField.getText().trim()
        );
        double amount = Double.parseDouble(amountField.getText().trim());
        String status = (String) paymentStatusBox.getSelectedItem();
        LocalDate paymentDate = LocalDate.parse(
                paymentDateField.getText().trim()
        );

        try {
            if (!reservationExists(reservationId)) {
                showValidationMessage(
                        "Reservation ID does not exist."
                );
                return;
            }

            String sql =
                    "UPDATE payments SET reservation_id = ?, amount = ?, " +
                    "payment_status = ?, payment_date = ? " +
                    "WHERE payment_id = ?";

            try (Connection con = connect();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, reservationId);
                ps.setDouble(2, amount);
                ps.setString(3, status);
                ps.setDate(4, Date.valueOf(paymentDate));
                ps.setInt(5, paymentId);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(
                            this, "Payment updated successfully!"
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this, "Payment record not found."
                    );
                }
            }

            clearFields();
            loadPayments();

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void deletePayment() {
        if (paymentIdField.getText().trim().isEmpty()) {
            showValidationMessage(
                    "Select a payment from the table first."
            );
            return;
        }

        int paymentId;

        try {
            paymentId = Integer.parseInt(paymentIdField.getText().trim());

            if (paymentId <= 0) {
                showValidationMessage("Select a valid payment record.");
                return;
            }
        } catch (NumberFormatException e) {
            showValidationMessage(
                    "Invalid Payment ID. Select a payment from the table."
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this payment record?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM payments WHERE payment_id = ?";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, paymentId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(
                        this, "Payment deleted successfully!"
                );
            } else {
                JOptionPane.showMessageDialog(
                        this, "Payment record not found."
                );
            }

            clearFields();
            loadPayments();

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void loadPayments() {
        tableModel.setRowCount(0);

        String sql =
                "SELECT p.payment_id, p.reservation_id, g.name, " +
                "rm.room_number, p.amount, p.payment_status, p.payment_date " +
                "FROM payments p " +
                "JOIN reservations r ON p.reservation_id = r.reservation_id " +
                "JOIN guests g ON r.guest_id = g.guest_id " +
                "JOIN rooms rm ON r.room_id = rm.room_id " +
                "ORDER BY p.payment_id";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("payment_id"),
                    rs.getInt("reservation_id"),
                    rs.getString("name"),
                    rs.getInt("room_number"),
                    rs.getBigDecimal("amount"),
                    rs.getString("payment_status"),
                    rs.getDate("payment_date")
                };

                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void showSelectedPayment() {
        int selectedRow = paymentTable.getSelectedRow();

        if (selectedRow < 0) {
            return;
        }

        // Convert displayed row index if the table has been sorted.
        int modelRow = paymentTable.convertRowIndexToModel(selectedRow);

        paymentIdField.setText(
                tableModel.getValueAt(modelRow, 0).toString()
        );
        reservationIdField.setText(
                tableModel.getValueAt(modelRow, 1).toString()
        );
        amountField.setText(
                tableModel.getValueAt(modelRow, 4).toString()
        );
        paymentStatusBox.setSelectedItem(
                tableModel.getValueAt(modelRow, 5).toString()
        );

        Object date = tableModel.getValueAt(modelRow, 6);
        paymentDateField.setText(date == null ? "" : date.toString());
    }

    private void clearFields() {
        paymentIdField.setText("");
        reservationIdField.setText("");
        amountField.setText("");
        paymentDateField.setText("");
        paymentStatusBox.setSelectedIndex(-1);
        paymentTable.clearSelection();
    }

    private void showDatabaseError(SQLException e) {
        JOptionPane.showMessageDialog(
                this,
                "Database error:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PaymentManagement().setVisible(true);
        });
    }
}
