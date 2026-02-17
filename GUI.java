import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

/**
 * MentalBook Desktop Chat Client with Creative UI
 * Features modern design with custom fonts and gradients
 */
class ChatClientGUI {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    // Modern color palette
    private static final Color PRIMARY_COLOR = new Color(91, 141, 239);
    private static final Color PRIMARY_LIGHT = new Color(123, 163, 245);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 255);
    private static final Color BOT_MESSAGE_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(45, 55, 72);
    private static final Color TEXT_SECONDARY = new Color(113, 128, 150);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);

    private JFrame frame;
    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;
    private JButton exitButton;
    private PrintWriter out;
    private Socket socket;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ChatClientGUI().start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void start() throws IOException {
        // Set up the GUI with modern styling
        frame = new JFrame("MentalBook - Your Mental Wellness Companion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setMinimumSize(new Dimension(500, 500));
        frame.setLayout(new BorderLayout(0, 0));
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Create custom font
        Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
        Font bodyFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        // Header Panel with Gradient
        JPanel headerPanel = createHeaderPanel(titleFont);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Text area to display the chat messages with custom styling
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(bodyFont);
        textArea.setForeground(TEXT_PRIMARY);
        textArea.setBackground(BOT_MESSAGE_COLOR);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.setBackground(BOT_MESSAGE_COLOR);
        scrollPane.getViewport().setBackground(BOT_MESSAGE_COLOR);

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel for input field and buttons
        JPanel inputPanel = createInputPanel(bodyFont, buttonFont);
        contentPanel.add(inputPanel, BorderLayout.SOUTH);

        frame.add(contentPanel, BorderLayout.CENTER);

        // Status Bar
        JPanel statusBar = createStatusBar(bodyFont);
        frame.add(statusBar, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Connect to the server
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);

            // Add welcome message
            textArea.append("MentalBook: Hello! I'm your mental wellness companion. \n\n");
            textArea.append("I can chat, support you, play games, and help with various tasks.\n");
            textArea.append("Feel free to share anything on your mind!\n\n");
            textArea.append("------------------------------------------\n\n");
            
            // Start listener thread
            new Thread(() -> listenForMessages()).start();
            
        } catch (IOException e) {
            textArea.append("Unable to connect to server. Please ensure the server is running.\n");
        }
    }

    private void listenForMessages() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response;
            while ((response = reader.readLine()) != null) {
                final String botMessage = response;
                SwingUtilities.invokeLater(() -> {
                    textArea.append("Xander: " + botMessage + "\n\n");
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                });
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> {
                textArea.append("Connection lost. Please restart the application.\n");
            });
        }
    }

    private JPanel createHeaderPanel(Font titleFont) {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    getWidth(), getHeight(), PRIMARY_LIGHT
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };

        headerPanel.setPreferredSize(new Dimension(frame.getWidth(), 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // App title
        JLabel titleLabel = new JLabel("MentalBook");
        titleLabel.setFont(titleFont.deriveFont(Font.BOLD, 22f));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Your Mental Wellness Companion");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(35, 110, 0, 0));
        headerPanel.add(subtitleLabel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createInputPanel(Font bodyFont, Font buttonFont) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Styled text field with placeholder
        textField = new JTextField("Type a message...");
        textField.setFont(bodyFont);
        textField.setForeground(TEXT_PRIMARY);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        textField.setPreferredSize(new Dimension(400, 45));
        
        // Handle focus for placeholder
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals("Type a message...")) {
                    textField.setText("");
                    textField.setForeground(TEXT_PRIMARY);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText("Type a message...");
                    textField.setForeground(TEXT_SECONDARY);
                }
            }
        });

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Send button with gradient effect
        sendButton = createStyledButton("Send", buttonFont, PRIMARY_COLOR, Color.WHITE);
        sendButton.addActionListener(e -> sendMessage());
        buttonPanel.add(sendButton);

        // Exit button
        exitButton = createStyledButton("Exit", buttonFont, Color.WHITE, TEXT_PRIMARY);
        exitButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        exitButton.addActionListener(e -> exitChat());
        buttonPanel.add(exitButton);

        panel.add(textField, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private JButton createStyledButton(String text, Font font, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, bgColor,
                    getWidth(), 0, bgColor.darker()
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(font);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 40));
        
        return button;
    }

    private JPanel createStatusBar(Font bodyFont) {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(240, 242, 245));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setFont(bodyFont.deriveFont(Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_SECONDARY);
        statusBar.add(statusLabel, BorderLayout.WEST);

        JLabel versionLabel = new JLabel("v0.2.0.2");
        versionLabel.setFont(bodyFont.deriveFont(Font.PLAIN, 12));
        versionLabel.setForeground(TEXT_SECONDARY);
        statusBar.add(versionLabel, BorderLayout.EAST);

        return statusBar;
    }

    private void sendMessage() {
        String message = textField.getText();
        if (message != null && !message.trim().isEmpty() && !message.equals("Type a message...")) {
            // Add user message to display
            textArea.append("You: " + message + "\n\n");
            out.println(message);
            textField.setText("Type a message...");
            textField.setForeground(TEXT_SECONDARY);
        }
    }

    private void exitChat() {
        if (out != null) {
            out.println("exit");
            out.close();
        }
        
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
        
        System.exit(0);
    }
}

