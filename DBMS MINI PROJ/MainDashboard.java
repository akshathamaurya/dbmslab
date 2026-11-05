
package miniproject01;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainDashboard extends JFrame {

    private final Color background = new Color(16, 24, 39);
    private final Color cardColor = new Color(28, 41, 59);
    private final Color gold = new Color(199, 166, 106);
    private final Color goldLight = new Color(235, 207, 151);
    private final Color white = new Color(245, 247, 250);
    private final Color muted = new Color(183, 194, 210);
    private final Color borderColor = new Color(55, 70, 91);

    public MainDashboard() {
        setTitle("Grand Palace - Hotel Management System");
        setSize(1050, 720);
        setMinimumSize(new Dimension(850, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createDashboard();
    }

    private void createDashboard() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(background);
        mainPanel.setBorder(new EmptyBorder(25, 35, 20, 35));

        // HEADER
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setBackground(background);
        header.setBorder(new EmptyBorder(0, 0, 22, 0));

        JPanel brandPanel = new JPanel(new BorderLayout(14, 0));
        brandPanel.setBackground(background);

        JLabel logo = new JLabel("\u2666");
        logo.setFont(new Font("Serif", Font.BOLD, 40));
        logo.setForeground(gold);
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setPreferredSize(new Dimension(55, 55));

        JPanel brandText = new JPanel();
        brandText.setBackground(background);
        brandText.setLayout(new BoxLayout(brandText, BoxLayout.Y_AXIS));

        JLabel hotelName = new JLabel("GRAND PALACE");
        hotelName.setFont(new Font("Serif", Font.BOLD, 25));
        hotelName.setForeground(goldLight);

        JLabel subtitle = new JLabel("HOTEL MANAGEMENT SYSTEM");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subtitle.setForeground(muted);

        brandText.add(hotelName);
        brandText.add(Box.createVerticalStrut(5));
        brandText.add(subtitle);

        brandPanel.add(logo, BorderLayout.WEST);
        brandPanel.add(brandText, BorderLayout.CENTER);

        JLabel adminLabel = new JLabel("  ADMIN PANEL  ");
        adminLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        adminLabel.setForeground(goldLight);
        adminLabel.setOpaque(true);
        adminLabel.setBackground(cardColor);
        adminLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(10, 8, 10, 8)
        ));

        header.add(brandPanel, BorderLayout.WEST);
        header.add(adminLabel, BorderLayout.EAST);

        // WELCOME SECTION
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(background);
        welcomePanel.setBorder(new EmptyBorder(15, 0, 24, 0));

        JLabel welcomeSmall = new JLabel("WELCOME TO YOUR WORKSPACE");
        welcomeSmall.setFont(new Font("SansSerif", Font.BOLD, 12));
        welcomeSmall.setForeground(gold);

        JLabel welcomeTitle = new JLabel("Manage your hotel.");
        welcomeTitle.setFont(new Font("Serif", Font.BOLD, 36));
        welcomeTitle.setForeground(white);

        JLabel welcomeDescription = new JLabel(
                "Everything you need to manage your hotel's daily operations."
        );
        welcomeDescription.setFont(new Font("SansSerif", Font.PLAIN, 14));
        welcomeDescription.setForeground(muted);

        welcomePanel.add(welcomeSmall);
        welcomePanel.add(Box.createVerticalStrut(10));
        welcomePanel.add(welcomeTitle);
        welcomePanel.add(Box.createVerticalStrut(8));
        welcomePanel.add(welcomeDescription);

        // MODULE CARDS
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 18, 18));
        cardsPanel.setBackground(background);
        cardsPanel.setBorder(new EmptyBorder(5, 0, 20, 0));

        cardsPanel.add(createModuleCard(
                "\u2659", "Guest Management",
                "Manage guest information, phone numbers and email addresses.",
                "OPEN GUEST MANAGEMENT",
                () -> new HotelManagement().setVisible(true)
        ));

        cardsPanel.add(createModuleCard(
                "\u265C", "Room Management",
                "Manage room numbers, room types, prices and availability.",
                "OPEN ROOM MANAGEMENT",
                () -> new RoomManagement().setVisible(true)
        ));

        cardsPanel.add(createModuleCard(
                "\u25C7", "Reservation Management",
                "Manage bookings, guest stays and reservation status.",
                "OPEN RESERVATIONS",
                () -> new ReservationManagement().setVisible(true)
        ));

        cardsPanel.add(createModuleCard(
                "\u20B9", "Payment Management",
                "Record payments, update payment status and view records.",
                "OPEN PAYMENTS",
                () -> new PaymentManagement().setVisible(true)
        ));

        // FOOTER
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(background);
        footer.setBorder(new EmptyBorder(15, 0, 0, 0));

        JLabel footerText = new JLabel(
                "GRAND PALACE  |  HOTEL MANAGEMENT SYSTEM"
        );
        footerText.setFont(new Font("SansSerif", Font.PLAIN, 10));
        footerText.setForeground(muted);

        JButton exitButton = new JButton("Exit Application");
        styleExitButton(exitButton);
        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Exit Application",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        footer.add(footerText, BorderLayout.WEST);
        footer.add(exitButton, BorderLayout.EAST);

        // COMBINE ALL SECTIONS
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(background);
        centerPanel.add(welcomePanel, BorderLayout.NORTH);
        centerPanel.add(cardsPanel, BorderLayout.CENTER);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createModuleCard(
            String symbol,
            String title,
            String description,
            String buttonText,
            Runnable action) {

        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                new EmptyBorder(18, 20, 18, 20)
        ));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(cardColor);

        JLabel iconLabel = new JLabel(symbol);
        iconLabel.setFont(new Font("Serif", Font.BOLD, 32));
        iconLabel.setForeground(gold);
        iconLabel.setPreferredSize(new Dimension(45, 42));

        JLabel arrow = new JLabel("\u2197");
        arrow.setFont(new Font("SansSerif", Font.BOLD, 20));
        arrow.setForeground(goldLight);

        top.add(iconLabel, BorderLayout.WEST);
        top.add(arrow, BorderLayout.EAST);

        JPanel textPanel = new JPanel();
        textPanel.setBackground(cardColor);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        titleLabel.setForeground(white);

        JTextArea descriptionLabel = new JTextArea(description);
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descriptionLabel.setForeground(muted);
        descriptionLabel.setBackground(cardColor);
        descriptionLabel.setEditable(false);
        descriptionLabel.setFocusable(false);
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setWrapStyleWord(true);
        descriptionLabel.setOpaque(false);
        descriptionLabel.setRows(2);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(descriptionLabel);

        JButton openButton = new JButton(buttonText);
        styleModuleButton(openButton);
        openButton.addActionListener(e -> action.run());

        card.add(top, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        card.add(openButton, BorderLayout.SOUTH);

        // Subtle hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(gold, 1),
                        new EmptyBorder(18, 20, 18, 20)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderColor, 1),
                        new EmptyBorder(18, 20, 18, 20)
                ));
            }
        });

        return card;
    }

    private void styleModuleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 10));
        button.setForeground(background);
        button.setBackground(gold);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 10, 12, 10));
        button.setOpaque(true);
        button.setBorderPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(goldLight);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(gold);
            }
        });
    }

    private void styleExitButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setForeground(white);
        button.setBackground(new Color(140, 55, 60));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 16, 10, 16));
        button.setOpaque(true);
        button.setBorderPainted(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName()
                );
            } catch (Exception e) {
                // Continue with the default Swing appearance.
            }

            new MainDashboard().setVisible(true);
        });
    }
}


