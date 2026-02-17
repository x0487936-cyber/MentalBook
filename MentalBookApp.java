import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

/**
 * MentalBookApp - The MentalBook Desktop Application
 * A standalone desktop companion app with modern GUI
 * Integrates VirtualXanderCore for AI-powered conversations
 */
public class MentalBookApp {
    
    private VirtualXanderCore core;
    private JFrame frame;
    private JTextPane chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton clearButton;
    private JLabel statusLabel;
    private JLabel moodLabel;
    private JList<String> topicList;
    private DefaultListModel<String> topicListModel;
    private JPanel typingIndicatorPanel;
    private Timer typingTimer;
    private int typingDotCount = 0;
    private boolean isShowingTyping = false;
    
    private Color primaryColor = new Color(70, 130, 180);
    private Color accentColor = new Color(100, 149, 237);
    private Color backgroundColor = new Color(245, 245, 250);
    private Color userMessageColor = new Color(70, 130, 180);
    private Color botMessageColor = new Color(255, 255, 255);
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                new MentalBookApp().start();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error starting MentalBook: " + e.getMessage(),
                    "Startup Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
    
    public void start() {
        // Initialize the VirtualXander core
        System.out.println("Initializing MentalBook Core...");
        core = new VirtualXanderCore();
        
        // Create and show the GUI
        createAndShowGUI();
    }
    
    private void createAndShowGUI() {
        // Create main frame
        frame = new JFrame("MentalBook - Your Companion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setLocationRelativeTo(null);
        
        // Set app icon
        setupFrameIcon();
        
        // Create menu bar
        frame.setJMenuBar(createMenuBar());
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(backgroundColor);
        
        // Create sidebar
        JPanel sidebarPanel = createSidebar();
        
        // Create chat area
        JPanel chatPanel = createChatArea();
        
        // Create input area
        JPanel inputPanel = createInputArea();
        
        // Create status bar
        JPanel statusBar = createStatusBar();
        
        // Add components to main panel
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(chatPanel, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(statusBar, BorderLayout.SOUTH);
        
        // Show the frame
        frame.setVisible(true);
        
        // Focus on input field
        inputField.requestFocusInWindow();
        
        // Welcome message
        appendBotMessage("Hello! Say hello to VirtualXander - your friendly companion.\n\n" +
                        "I'm here to chat, support you, and help with various tasks.\n" +
                        "Feel free to share anything on your mind!\n\n" +
                        "Type your message below and press Send or press Enter to chat.");
    }
    
    private void setupFrameIcon() {
        try {
            BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setColor(primaryColor);
            g.fillRect(0, 0, 16, 16);
            g.setColor(Color.WHITE);
            g.fillOval(4, 4, 8, 8);
            g.dispose();
            frame.setIconImage(img);
        } catch (Exception e) {
            // Ignore icon errors
        }
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
        JMenuItem newChatItem = new JMenuItem("New Chat", KeyEvent.VK_N);
        newChatItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        newChatItem.addActionListener(e -> newChat());
        
        JMenuItem exportItem = new JMenuItem("Export Chat", KeyEvent.VK_E);
        exportItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        exportItem.addActionListener(e -> exportChat());
        
        JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(newChatItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        
        JMenuItem copyItem = new JMenuItem("Copy", KeyEvent.VK_C);
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        copyItem.addActionListener(e -> chatArea.copy());
        
        JMenuItem selectAllItem = new JMenuItem("Select All", KeyEvent.VK_A);
        selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        selectAllItem.addActionListener(e -> chatArea.selectAll());
        
        editMenu.add(copyItem);
        editMenu.add(selectAllItem);
        
        // View menu
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        
        JMenuItem clearViewItem = new JMenuItem("Clear Chat", KeyEvent.VK_C);
        clearViewItem.addActionListener(e -> clearChat());
        
        JMenuItem refreshItem = new JMenuItem("Refresh", KeyEvent.VK_R);
        refreshItem.addActionListener(e -> refreshView());
        
        viewMenu.add(clearViewItem);
        viewMenu.add(refreshItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem aboutItem = new JMenuItem("About MentalBook", KeyEvent.VK_A);
        aboutItem.addActionListener(e -> showAboutDialog());
        
        JMenuItem helpItem = new JMenuItem("Help & Commands", KeyEvent.VK_H);
        helpItem.addActionListener(e -> showHelpDialog());
        
        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout(0, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(backgroundColor);
        
        // Header
        JLabel sidebarTitle = new JLabel("MentalBook");
        sidebarTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sidebarTitle.setForeground(primaryColor);
        sidebarTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Stats panel
        JPanel statsPanel = createStatsPanel();
        
        // Topics panel
        JPanel topicsPanel = createTopicsPanel();
        
        // Quick actions panel
        JPanel actionsPanel = createActionsPanel();
        
        // Add to sidebar
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(sidebarTitle, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.setBackground(backgroundColor);
        
        sidebar.add(topPanel, BorderLayout.NORTH);
        sidebar.add(topicsPanel, BorderLayout.CENTER);
        sidebar.add(actionsPanel, BorderLayout.SOUTH);
        
        return sidebar;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel title = new JLabel("Session Stats");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(primaryColor);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel turns = new JLabel("Turns: 0");
        turns.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        turns.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel topics = new JLabel("Topics: 0");
        topics.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        topics.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel mood = new JLabel("Mood: Neutral");
        mood.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mood.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(turns);
        panel.add(topics);
        panel.add(mood);
        
        return panel;
    }
    
    private JPanel createTopicsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JLabel title = new JLabel("Active Topics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(primaryColor);
        title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        topicListModel = new DefaultListModel<>();
        topicList = new JList<>(topicListModel);
        topicList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        topicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(topicList);
        scrollPane.setBorder(null);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        
        JButton newChatBtn = createSidebarButton("New Chat");
        newChatBtn.addActionListener(e -> newChat());
        
        JButton clearBtn = createSidebarButton("Clear");
        clearBtn.addActionListener(e -> clearChat());
        
        JButton statsBtn = createSidebarButton("Stats");
        statsBtn.addActionListener(e -> showStats());
        
        JButton helpBtn = createSidebarButton("Help");
        helpBtn.addActionListener(e -> showHelpDialog());
        
        panel.add(newChatBtn);
        panel.add(clearBtn);
        panel.add(statsBtn);
        panel.add(helpBtn);
        
        return panel;
    }
    
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        return button;
    }
    
    private JPanel createChatArea() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        
        // Chat area with rich text
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setBackground(new Color(250, 250, 252));
        chatArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(backgroundColor);
        
        JLabel chatTitle = new JLabel("Chat with VirtualXander");
        chatTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chatTitle.setForeground(primaryColor);
        chatTitle.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
        
        headerPanel.add(chatTitle, BorderLayout.WEST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createInputArea() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Input field
        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setPreferredSize(new Dimension(0, 40));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Send button
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(primaryColor);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        // Clear button
        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(e -> clearChat());
        
        // Add action listeners
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(sendButton);
        buttonPanel.add(clearButton);
        
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createStatusBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        
        moodLabel = new JLabel("Neutral");
        moodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        moodLabel.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(new Color(240, 240, 240));
        leftPanel.add(statusLabel);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(new Color(240, 240, 240));
        rightPanel.add(moodLabel);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void sendMessage() {
        String message = inputField.getText().trim();
        
        if (message.isEmpty()) {
            return;
        }
        
        // Clear input field
        inputField.setText("");
        
        // Add user message to chat
        appendUserMessage(message);
        
        // Update status
        statusLabel.setText("Thinking...");
        
        // Process message in background
        new Thread(() -> {
            try {
                showTypingIndicator();
                
                // Get response from core
                String response = core.processMessage(message);
                
                SwingUtilities.invokeLater(() -> {
                    removeTypingIndicator();
                    appendBotMessage(response);
                    updateStats();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    removeTypingIndicator();
                    appendBotMessage("I encountered an error. Please try again.");
                    statusLabel.setText("Error");
                });
                e.printStackTrace();
            }
        }).start();
    }
    
    private void appendUserMessage(String message) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String time = sdf.format(new Date());
            
            StyledDocument doc = chatArea.getStyledDocument();
            
            // Style for time
            Style timeStyle = chatArea.addStyle("userTime", null);
            StyleConstants.setForeground(timeStyle, new Color(150, 150, 150));
            StyleConstants.setFontSize(timeStyle, 11);
            
            // Style for label
            Style labelStyle = chatArea.addStyle("userLabelNew", null);
            StyleConstants.setForeground(labelStyle, new Color(100, 100, 150));
            StyleConstants.setBold(labelStyle, true);
            
            // Style for message
            Style msgStyle = chatArea.addStyle("userMsg", null);
            StyleConstants.setForeground(msgStyle, Color.WHITE);
            StyleConstants.setBackground(msgStyle, userMessageColor);
            
            // Insert time
            doc.insertString(doc.getLength(), "[" + time + "] ", timeStyle);
            doc.insertString(doc.getLength(), "You: ", labelStyle);
            doc.insertString(doc.getLength(), message + "\n\n", msgStyle);
            
            // Scroll to bottom
            chatArea.setCaretPosition(doc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void appendBotMessage(String message) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String time = sdf.format(new Date());
            
            StyledDocument doc = chatArea.getStyledDocument();
            
            // Style for time
            Style timeStyle = chatArea.addStyle("botTime", null);
            StyleConstants.setForeground(timeStyle, new Color(150, 150, 150));
            StyleConstants.setFontSize(timeStyle, 11);
            
            // Style for label
            Style labelStyle = chatArea.addStyle("botLabelNew", null);
            StyleConstants.setForeground(labelStyle, primaryColor);
            StyleConstants.setBold(labelStyle, true);
            
            // Style for message
            Style msgStyle = chatArea.addStyle("botMsg", null);
            StyleConstants.setForeground(msgStyle, new Color(50, 50, 50));
            StyleConstants.setBackground(msgStyle, botMessageColor);
            
            // Insert time
            doc.insertString(doc.getLength(), "[" + time + "] ", timeStyle);
            doc.insertString(doc.getLength(), "Xander: ", labelStyle);
            doc.insertString(doc.getLength(), message + "\n\n", msgStyle);
            
            // Scroll to bottom
            chatArea.setCaretPosition(doc.getLength());
            
            // Update status
            statusLabel.setText("Ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showTypingIndicator() {
        try {
            StyledDocument doc = chatArea.getStyledDocument();
            Style style = chatArea.addStyle("typing", null);
            StyleConstants.setItalic(style, true);
            StyleConstants.setForeground(style, new Color(150, 150, 150));
            
            doc.insertString(doc.getLength(), "Xander is typing...\n\n", style);
            chatArea.setCaretPosition(doc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void removeTypingIndicator() {
        try {
            String text = chatArea.getText();
            if (text.contains("Xander is typing...")) {
                int start = text.lastIndexOf("Xander is typing...");
                if (start >= 0) {
                    chatArea.setText(text.substring(0, start) + 
                        text.substring(start + "Xander is typing...\n\n".length()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateStats() {
        // Update conversation stats
        int turns = core.getConversationContext().getTurnCount();
        int topics = core.getTopicClusteringSystem().getActiveClusters().size();
        
        // Get last detected emotion
        String lastInput = core.getConversationContext().getLastUserInput();
        String mood = "Neutral";
        if (lastInput != null && !lastInput.isEmpty()) {
            try {
                EmotionDetector.EmotionResult result = core.getEmotionDetector().detectEmotion(lastInput);
                mood = result.getPrimaryEmotion().getName();
            } catch (Exception e) {
                // Ignore emotion detection errors
            }
        }
        
        // Update status bar
        statusLabel.setText("Turns: " + turns + " | Topics: " + topics);
        moodLabel.setText(mood);
        
        // Update topic list
        topicListModel.clear();
        for (TopicClusteringSystem.TopicCluster cluster : core.getTopicClusteringSystem().getActiveClusters()) {
            topicListModel.addElement(cluster.clusterName + " (" + cluster.visitCount + ")");
        }
    }
    
    private void newChat() {
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Start a new chat? This will clear the current conversation.", 
            "New Chat", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            core.getConversationContext().reset();
            core.getStateMachine().reset();
            core.getTopicClusteringSystem().resetClusters();
            
            chatArea.setText("");
            topicListModel.clear();
            
            appendBotMessage("Let's start fresh! Hi there!\n\nWhat would you like to talk about?");
        }
    }
    
    private void clearChat() {
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Clear all messages from the chat?", 
            "Clear Chat", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            chatArea.setText("");
            appendBotMessage("Chat cleared!\n\nWhat would you like to talk about?");
        }
    }
    
    private void refreshView() {
        updateStats();
        frame.repaint();
    }
    
    private void showStats() {
        String stats = "Session Statistics\n\n" +
            "Total Turns: " + core.getConversationContext().getTurnCount() + "\n" +
            "Duration: " + core.getConversationContext().getConversationDurationMinutes() + " minutes\n" +
            "Active Topics: " + core.getTopicClusteringSystem().getActiveClusters().size() + "\n" +
            "Plugins Loaded: " + core.getPluginSystem().getPluginCount();
        
        JOptionPane.showMessageDialog(frame, stats, "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportChat() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Chat");
        fileChooser.setSelectedFile(new File("VirtualXander_chat.txt"));
        
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                writer.println("VirtualXander Chat Export");
                writer.println("======================");
                writer.println();
                writer.println(chatArea.getText());
                
                JOptionPane.showMessageDialog(frame, 
                    "Chat exported successfully!", 
                    "Export Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, 
                    "Error exporting chat: " + e.getMessage(), 
                    "Export Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showAboutDialog() {
        String about = "VirtualXander - Your Companion\n" +
            "Version 0.2.0.0\n\n" +
            "A friendly virtual companion app powered by Xander's personaly feelings and reactions.\n\n" +
            "Features:\n" +
            "- Mental health support\n" +
            "- Emotion detection\n" +
            "- Topic tracking\n" +
            "- And much more!\n\n" +
            "Created by Xander Thompson";
        
        JOptionPane.showMessageDialog(frame, about, "About VirtualXander", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showHelpDialog() {
        String help = "VirtualXander Help\n\n" +
            "Commands you can use:\n\n" +
            "- exit / bye - End the conversation\n" +
            "- reset - Start a new conversation\n" +
            "- status - Show current status\n" +
            "- emotion - Show emotion analysis\n" +
            "- topics - Show topic clusters\n" +
            "- analyze - Analyze conversation\n" +
            "- mood - Show mood analysis\n" +
            "- enhance - Show mood enhancement\n" +
            "- history - Show conversation history\n\n" +
            "Tips:\n" +
            "- Type naturally and I'll understand!\n" +
            "- I can help with homework, mental health, gaming, and more.\n" +
            "- Use the sidebar to see active topics.\n" +
            "- Export your chat using File > Export Chat.";
        
        JOptionPane.showMessageDialog(frame, help, "Help & Commands", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}

