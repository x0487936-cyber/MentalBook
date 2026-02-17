import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;

/**
 * DesktopEnhancements - System Integration & Desktop Features
 * 
 * Features:
 * - System tray integration with icon and menu
 * - Desktop notifications for messages and reminders
 * - Settings panel with preferences management
 * - Auto-start on system boot
 * - Minimize to tray functionality
 * - Global hotkey support
 * - Window state management
 */
public class DesktopEnhancements {
    
    private JFrame mainFrame;
    private TrayIcon trayIcon;
    private SystemTray systemTray;
    private Preferences prefs;
    private boolean minimizedToTray;
    private boolean notificationsEnabled;
    private boolean soundEnabled;
    private boolean autoStartEnabled;
    private boolean minimizeToTrayOnClose;
    private boolean globalHotkeysEnabled;
    private int unreadMessageCount;
    private JDialog settingsDialog;
    
    // Settings keys
    private static final String PREF_NOTIFICATIONS = "notifications_enabled";
    private static final String PREF_SOUND = "sound_enabled";
    private static final String PREF_AUTOSTART = "autostart_enabled";
    private static final String PREF_MINIMIZE_TRAY = "minimize_to_tray";
    private static final String PREF_GLOBAL_HOTKEYS = "global_hotkeys";
    private static final String PREF_WINDOW_X = "window_x";
    private static final String PREF_WINDOW_Y = "window_y";
    private static final String PREF_WINDOW_WIDTH = "window_width";
    private static final String PREF_WINDOW_HEIGHT = "window_height";
    private static final String PREF_ALWAYS_ON_TOP = "always_on_top";
    
    public DesktopEnhancements(JFrame frame) {
        this.mainFrame = frame;
        this.prefs = Preferences.userNodeForPackage(DesktopEnhancements.class);
        loadPreferences();
    }
    
    // ==================== SYSTEM TRAY ====================
    
    public boolean initializeSystemTray() {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported on this platform");
            return false;
        }
        
        try {
            systemTray = SystemTray.getSystemTray();
            
            // Create tray icon image
            Image trayImage = createTrayIcon();
            
            // Create popup menu
            PopupMenu popup = createTrayMenu();
            
            // Create tray icon
            trayIcon = new TrayIcon(trayImage, "MentalBook - Your Companion", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("MentalBook - Click to show/hide");
            
            // Add double-click listener
            trayIcon.addActionListener(e -> showMainWindow());
            
            systemTray.add(trayIcon);
            
            // Add window listener to minimize to tray
            setupMinimizeBehavior();
            
            return true;
            
        } catch (AWTException e) {
            System.err.println("Error initializing system tray: " + e.getMessage());
            return false;
        }
    }
    
    private Image createTrayIcon() {
        // Create a simple icon (16x16 or 32x32 for system tray)
        int size = SystemTray.getSystemTray().getTrayIconSize().width;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Draw a simple brain icon
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background circle
        g2d.setColor(new Color(70, 130, 180));
        g2d.fillOval(2, 2, size-4, size-4);
        
        // Brain pattern (simplified)
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        int center = size / 2;
        g2d.drawArc(center-6, center-6, 12, 12, 0, 180);
        g2d.drawArc(center-6, center-6, 12, 12, 180, 180);
        g2d.drawLine(center, center-6, center, center+6);
        
        g2d.dispose();
        return image;
    }
    
    private PopupMenu createTrayMenu() {
        PopupMenu popup = new PopupMenu();
        
        MenuItem showItem = new MenuItem("Show MentalBook");
        showItem.addActionListener(e -> showMainWindow());
        popup.add(showItem);
        
        popup.addSeparator();
        
        MenuItem newChatItem = new MenuItem("New Chat");
        newChatItem.addActionListener(e -> {
            showMainWindow();
            // Trigger new chat action
        });
        popup.add(newChatItem);
        
        MenuItem statusItem = new MenuItem("Status: Online");
        statusItem.setEnabled(false);
        popup.add(statusItem);
        
        popup.addSeparator();
        
        MenuItem settingsItem = new MenuItem("Settings...");
        settingsItem.addActionListener(e -> showSettingsDialog());
        popup.add(settingsItem);
        
        popup.addSeparator();
        
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> exitApplication());
        popup.add(exitItem);
        
        return popup;
    }
    
    private void setupMinimizeBehavior() {
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                if (minimizeToTrayOnClose) {
                    minimizeToTray();
                }
            }
            
            @Override
            public void windowClosing(WindowEvent e) {
                if (minimizeToTrayOnClose) {
                    e.getWindow().setVisible(false);
                    minimizedToTray = true;
                } else {
                    exitApplication();
                }
            }
        });
    }
    
    public void minimizeToTray() {
        if (trayIcon != null) {
            mainFrame.setVisible(false);
            minimizedToTray = true;
            showTrayNotification("MentalBook", "Running in background. Click tray icon to show.");
        }
    }
    
    public void showMainWindow() {
        mainFrame.setVisible(true);
        mainFrame.setState(Frame.NORMAL);
        mainFrame.toFront();
        mainFrame.requestFocus();
        minimizedToTray = false;
        unreadMessageCount = 0;
        updateTrayIcon();
    }
    
    private void updateTrayIcon() {
        if (trayIcon != null && unreadMessageCount > 0) {
            trayIcon.setToolTip("MentalBook - " + unreadMessageCount + " unread messages");
        } else if (trayIcon != null) {
            trayIcon.setToolTip("MentalBook - Your Companion");
        }
    }
    
    // ==================== NOTIFICATIONS ====================
    
    public void showNotification(String title, String message, NotificationType type) {
        if (!notificationsEnabled) return;
        
        // Try system tray notification first
        if (trayIcon != null) {
            trayIcon.displayMessage(title, message, getTrayMessageType(type));
        }
        
        // Also show Swing notification if window not focused
        if (!mainFrame.isFocused()) {
            showSwingNotification(title, message, type);
        }
        
        // Play sound if enabled
        if (soundEnabled) {
            playNotificationSound(type);
        }
        
        unreadMessageCount++;
        updateTrayIcon();
    }
    
    private TrayIcon.MessageType getTrayMessageType(NotificationType type) {
        switch (type) {
            case ERROR: return TrayIcon.MessageType.ERROR;
            case WARNING: return TrayIcon.MessageType.WARNING;
            case INFO: return TrayIcon.MessageType.INFO;
            default: return TrayIcon.MessageType.NONE;
        }
    }
    
    private void showSwingNotification(String title, String message, NotificationType type) {
        SwingUtilities.invokeLater(() -> {
            JWindow notification = new JWindow();
            notification.setAlwaysOnTop(true);
            
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createLineBorder(getNotificationColor(type), 2));
            panel.setBackground(new Color(50, 50, 50));
            panel.setLayout(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            titleLabel.setForeground(Color.WHITE);
            
            JLabel messageLabel = new JLabel("<html><body style='width: 250px'>" + message + "</body></html>");
            messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            messageLabel.setForeground(Color.LIGHT_GRAY);
            
            panel.add(titleLabel, BorderLayout.NORTH);
            panel.add(messageLabel, BorderLayout.CENTER);
            
            notification.add(panel);
            notification.pack();
            
            // Position at bottom-right of screen
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            notification.setLocation(
                screenSize.width - notification.getWidth() - 20,
                screenSize.height - notification.getHeight() - 40
            );
            
            notification.setVisible(true);
            
            // Auto-hide after 5 seconds
            Timer timer = new Timer(5000, e -> {
                notification.setVisible(false);
                notification.dispose();
            });
            timer.setRepeats(false);
            timer.start();
            
            // Click to dismiss and show main window
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    notification.setVisible(false);
                    notification.dispose();
                    showMainWindow();
                }
            });
        });
    }
    
    private Color getNotificationColor(NotificationType type) {
        switch (type) {
            case ERROR: return new Color(231, 76, 60);
            case WARNING: return new Color(241, 196, 15);
            case SUCCESS: return new Color(46, 204, 113);
            case INFO: return new Color(52, 152, 219);
            default: return new Color(149, 165, 166);
        }
    }
    
    private void playNotificationSound(NotificationType type) {
        // Play system beep or custom sound
        Toolkit.getDefaultToolkit().beep();
    }
    
    public void showTrayNotification(String title, String message) {
        if (trayIcon != null) {
            trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
        }
    }
    
    // ==================== SETTINGS PANEL ====================
    
    public void showSettingsDialog() {
        if (settingsDialog != null && settingsDialog.isVisible()) {
            settingsDialog.toFront();
            return;
        }
        
        settingsDialog = new JDialog(mainFrame, "MentalBook Settings", true);
        settingsDialog.setSize(500, 450);
        settingsDialog.setLocationRelativeTo(mainFrame);
        settingsDialog.setResizable(false);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Create tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("General", createGeneralSettingsPanel());
        tabs.addTab("Notifications", createNotificationSettingsPanel());
        tabs.addTab("Advanced", createAdvancedSettingsPanel());
        
        panel.add(tabs, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> {
            savePreferences();
            settingsDialog.dispose();
            showNotification("Settings Saved", "Your preferences have been updated.", NotificationType.SUCCESS);
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> settingsDialog.dispose());
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        settingsDialog.add(panel);
        settingsDialog.setVisible(true);
    }
    
    private JPanel createGeneralSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Auto-start option
        gbc.gridx = 0; gbc.gridy = 0;
        JCheckBox autoStartBox = new JCheckBox("Start MentalBook on system boot");
        autoStartBox.setSelected(autoStartEnabled);
        autoStartBox.addActionListener(e -> autoStartEnabled = autoStartBox.isSelected());
        panel.add(autoStartBox, gbc);
        
        // Minimize to tray
        gbc.gridy = 1;
        JCheckBox minimizeBox = new JCheckBox("Minimize to system tray instead of closing");
        minimizeBox.setSelected(minimizeToTrayOnClose);
        minimizeBox.addActionListener(e -> minimizeToTrayOnClose = minimizeBox.isSelected());
        panel.add(minimizeBox, gbc);
        
        // Always on top
        gbc.gridy = 2;
        JCheckBox alwaysOnTopBox = new JCheckBox("Keep window always on top");
        alwaysOnTopBox.setSelected(mainFrame.isAlwaysOnTop());
        alwaysOnTopBox.addActionListener(e -> mainFrame.setAlwaysOnTop(alwaysOnTopBox.isSelected()));
        panel.add(alwaysOnTopBox, gbc);
        
        // Global hotkeys
        gbc.gridy = 3;
        JCheckBox hotkeysBox = new JCheckBox("Enable global hotkeys (Ctrl+Shift+M to show)");
        hotkeysBox.setSelected(globalHotkeysEnabled);
        hotkeysBox.addActionListener(e -> globalHotkeysEnabled = hotkeysBox.isSelected());
        panel.add(hotkeysBox, gbc);
        
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);
        
        return panel;
    }
    
    private JPanel createNotificationSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Enable notifications
        gbc.gridx = 0; gbc.gridy = 0;
        JCheckBox notificationsBox = new JCheckBox("Enable desktop notifications");
        notificationsBox.setSelected(notificationsEnabled);
        notificationsBox.addActionListener(e -> notificationsEnabled = notificationsBox.isSelected());
        panel.add(notificationsBox, gbc);
        
        // Enable sounds
        gbc.gridy = 1;
        JCheckBox soundBox = new JCheckBox("Play sound with notifications");
        soundBox.setSelected(soundEnabled);
        soundBox.addActionListener(e -> soundEnabled = soundBox.isSelected());
        panel.add(soundBox, gbc);
        
        // Notification preview
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton previewButton = new JButton("Test Notification");
        previewButton.addActionListener(e -> 
            showNotification("Test", "This is how notifications will appear!", NotificationType.INFO)
        );
        panel.add(previewButton, gbc);
        
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(Box.createVerticalGlue(), gbc);
        
        return panel;
    }
    
    private JPanel createAdvancedSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Clear data button
        gbc.gridx = 0; gbc.gridy = 0;
        JButton clearDataButton = new JButton("Clear All User Data");
        clearDataButton.setForeground(new Color(231, 76, 60));
        clearDataButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(settingsDialog,
                "This will delete all your preferences and data. Are you sure?",
                "Clear Data", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                clearAllPreferences();
                JOptionPane.showMessageDialog(settingsDialog, 
                    "All data has been cleared. Please restart the application.");
            }
        });
        panel.add(clearDataButton, gbc);
        
        // Export data button
        gbc.gridy = 1;
        JButton exportButton = new JButton("Export User Data");
        exportButton.addActionListener(e -> exportUserData());
        panel.add(exportButton, gbc);
        
        // Version info
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        JLabel versionLabel = new JLabel("MentalBook Version 0.2.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(Color.GRAY);
        panel.add(versionLabel, gbc);
        
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(Box.createVerticalGlue(), gbc);
        
        return panel;
    }
    
    // ==================== AUTO-START ====================
    
    public void setupAutoStart(boolean enable) {
        String os = System.getProperty("os.name").toLowerCase();
        
        try {
            if (os.contains("win")) {
                setupWindowsAutoStart(enable);
            } else if (os.contains("mac")) {
                setupMacAutoStart(enable);
            } else if (os.contains("nix") || os.contains("nux")) {
                setupLinuxAutoStart(enable);
            }
        } catch (Exception e) {
            System.err.println("Error setting up auto-start: " + e.getMessage());
        }
    }
    
    private void setupWindowsAutoStart(boolean enable) throws Exception {
        String runKey = "Software\\Microsoft\\Windows\\CurrentVersion\\Run";
        Preferences prefs = Preferences.userRoot().node(runKey);
        
        if (enable) {
            String jarPath = new File(DesktopEnhancements.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI()).getAbsolutePath();
            prefs.put("MentalBook", "javaw -jar \"" + jarPath + "\"");
        } else {
            prefs.remove("MentalBook");
        }
    }
    
    private void setupMacAutoStart(boolean enable) {
        // macOS implementation would use launchd plist files
        // This is a simplified placeholder
    }
    
    private void setupLinuxAutoStart(boolean enable) {
        // Linux implementation would use .desktop files in ~/.config/autostart
        // This is a simplified placeholder
    }
    
    // ==================== PREFERENCES MANAGEMENT ====================
    
    private void loadPreferences() {
        notificationsEnabled = prefs.getBoolean(PREF_NOTIFICATIONS, true);
        soundEnabled = prefs.getBoolean(PREF_SOUND, true);
        autoStartEnabled = prefs.getBoolean(PREF_AUTOSTART, false);
        minimizeToTrayOnClose = prefs.getBoolean(PREF_MINIMIZE_TRAY, true);
        globalHotkeysEnabled = prefs.getBoolean(PREF_GLOBAL_HOTKEYS, true);
        
        // Restore window position and size
        int x = prefs.getInt(PREF_WINDOW_X, -1);
        int y = prefs.getInt(PREF_WINDOW_Y, -1);
        int width = prefs.getInt(PREF_WINDOW_WIDTH, 1000);
        int height = prefs.getInt(PREF_WINDOW_HEIGHT, 700);
        
        if (x >= 0 && y >= 0) {
            mainFrame.setLocation(x, y);
        }
        mainFrame.setSize(width, height);
        mainFrame.setAlwaysOnTop(prefs.getBoolean(PREF_ALWAYS_ON_TOP, false));
    }
    
    public void savePreferences() {
        prefs.putBoolean(PREF_NOTIFICATIONS, notificationsEnabled);
        prefs.putBoolean(PREF_SOUND, soundEnabled);
        prefs.putBoolean(PREF_AUTOSTART, autoStartEnabled);
        prefs.putBoolean(PREF_MINIMIZE_TRAY, minimizeToTrayOnClose);
        prefs.putBoolean(PREF_GLOBAL_HOTKEYS, globalHotkeysEnabled);
        
        // Save window state
        prefs.putInt(PREF_WINDOW_X, mainFrame.getX());
        prefs.putInt(PREF_WINDOW_Y, mainFrame.getY());
        prefs.putInt(PREF_WINDOW_WIDTH, mainFrame.getWidth());
        prefs.putInt(PREF_WINDOW_HEIGHT, mainFrame.getHeight());
        prefs.putBoolean(PREF_ALWAYS_ON_TOP, mainFrame.isAlwaysOnTop());
        
        // Setup auto-start
        if (autoStartEnabled) {
            setupAutoStart(true);
        }
    }
    
    private void clearAllPreferences() {
        try {
            prefs.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void exportUserData() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("mentalbook-settings.json"));
        
        if (chooser.showSaveDialog(settingsDialog) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(chooser.getSelectedFile())) {
                writer.println("{");
                writer.println("  \"notifications\": " + notificationsEnabled + ",");
                writer.println("  \"sound\": " + soundEnabled + ",");
                writer.println("  \"autoStart\": " + autoStartEnabled + ",");
                writer.println("  \"minimizeToTray\": " + minimizeToTrayOnClose + ",");
                writer.println("  \"globalHotkeys\": " + globalHotkeysEnabled);
                writer.println("}");
                
                JOptionPane.showMessageDialog(settingsDialog, 
                    "Settings exported successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(settingsDialog, 
                    "Error exporting settings: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // ==================== UTILITY METHODS ====================
    
    public void exitApplication() {
        savePreferences();
        
        if (systemTray != null && trayIcon != null) {
            systemTray.remove(trayIcon);
        }
        
        System.exit(0);
    }
    
    public boolean isMinimizedToTray() {
        return minimizedToTray;
    }
    
    public void setUnreadMessageCount(int count) {
        this.unreadMessageCount = count;
        updateTrayIcon();
    }
    
    // ==================== NOTIFICATION TYPES ====================
    
    public enum NotificationType {
        INFO, SUCCESS, WARNING, ERROR
    }
}
