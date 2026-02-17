import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * VirtualXanderClient - Integrated Client for VirtualXander Chatbot
 * Connects to VirtualXanderServer with GUI and command-line support
 * Part of Phase 5: Integration
 */
public class VirtualXanderClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 12345;
    public static final String VERSION = "2.0";
    
    public static void main(String[] args) {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        boolean guiMode = true;
        
        // Parse command line arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                case "--host":
                    if (i + 1 < args.length) {
                        host = args[++i];
                    }
                    break;
                case "-p":
                case "--port":
                    if (i + 1 < args.length) {
                        port = Integer.parseInt(args[++i]);
                    }
                    break;
                case "-c":
                case "--console":
                    guiMode = false;
                    break;
                case "-help":
                case "--help":
                    printHelp();
                    return;
            }
        }
        
        if (guiMode) {
            // Start GUI mode - make final copies for lambda
            final String finalHost = host;
            final int finalPort = port;
            SwingUtilities.invokeLater(() -> {
                try {
                    new VirtualXanderGUI(finalHost, finalPort).start();
                } catch (Exception e) {
                    System.err.println("Failed to connect: " + e.getMessage());
                    // Fall back to console mode
                    System.out.println("Falling back to console mode...");
                    startConsoleMode(finalHost, finalPort);
                }
            });
        } else {
            // Start console mode
            startConsoleMode(host, port);
        }
    }
    
    /**
     * Prints help message
     */
    private static void printHelp() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║            VirtualXander Client Help                  ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Usage: java VirtualXanderClient [OPTIONS]               ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Options:                                              ║");
        System.out.println("║   -h, --host HOST     Server hostname (default: localhost)║");
        System.out.println("║   -p, --port PORT     Server port (default: 12345)       ║");
        System.out.println("║   -c, --console       Run in console mode               ║");
        System.out.println("║   --help              Show this help message            ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Examples:                                             ║");
        System.out.println("║   java VirtualXanderClient                             ║");
        System.out.println("║   java VirtualXanderClient -h localhost -p 12345        ║");
        System.out.println("║   java VirtualXanderClient -c                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Starts console mode
     */
    private static void startConsoleMode(String host, int port) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║        VirtualXander Client - Console Mode             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println("Connecting to " + host + ":" + port + "...");
        
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {
            
            System.out.println("Connected! Type 'exit' to disconnect.\n");
            
            // Start listener thread
            Thread listenerThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    System.err.println("Connection lost: " + e.getMessage());
                }
            });
            listenerThread.setDaemon(true);
            listenerThread.start();
            
            // Main input loop
            while (true) {
                System.out.print("You: ");
                String input = scanner.nextLine();
                
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("\nDisconnecting...");
                    out.println("exit");
                    break;
                }
                
                out.println(input);
            }
            
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            System.err.println("Make sure VirtualXanderServer is running.");
        }
    }
}

/**
 * GUI Client for VirtualXander
 */
class VirtualXanderGUI {
    private String host;
    private int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean connected;
    
    // GUI components
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton connectButton;
    private JButton disconnectButton;
    private JLabel statusLabel;
    private JPanel connectionPanel;
    
    public VirtualXanderGUI(String host, int port) {
        this.host = host;
        this.port = port;
        this.connected = false;
        initializeGUI();
    }
    
    /**
     * Initializes the GUI
     */
    private void initializeGUI() {
        // Create frame
        frame = new JFrame("VirtualXander Chat - v" + VirtualXanderClient.VERSION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLayout(new BorderLayout());
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem connectItem = new JMenuItem("Connect");
        JMenuItem disconnectItem = new JMenuItem("Disconnect");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        connectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        disconnectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnectFromServer();
            }
        });
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnectFromServer();
                System.exit(0);
            }
        });
        
        fileMenu.add(connectItem);
        fileMenu.add(disconnectItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        
        frame.setJMenuBar(menuBar);
        
        // Connection panel
        connectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel hostLabel = new JLabel("Host:");
        JTextField hostField = new JTextField(host, 15);
        JLabel portLabel = new JLabel("Port:");
        JTextField portField = new JTextField(String.valueOf(port), 6);
        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");
        disconnectButton.setEnabled(false);
        
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    VirtualXanderGUI.this.host = hostField.getText();
                    VirtualXanderGUI.this.port = Integer.parseInt(portField.getText());
                    connectToServer();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid port number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnectFromServer();
            }
        });
        
        connectionPanel.add(hostLabel);
        connectionPanel.add(hostField);
        connectionPanel.add(portLabel);
        connectionPanel.add(portField);
        connectionPanel.add(connectButton);
        connectionPanel.add(disconnectButton);
        
        // Status label
        statusLabel = new JLabel("Status: Disconnected");
        
        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        
        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        inputField.setEnabled(false);
        
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        // Add panels to frame
        frame.add(connectionPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(statusLabel, BorderLayout.SOUTH);
        frame.add(inputPanel, BorderLayout.SOUTH);
        
        // Center frame on screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Add window close handler
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnectFromServer();
            }
        });
        
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                    "VirtualXander Chat Client v" + VirtualXanderClient.VERSION + "\n\n" +
                    "A personally created powered chat companion made by Xander Thompson, with support for:\n" +
                    "• Greetings and casual chat\n" +
                    "• Homework and academic questions\n" +
                    "• Mental health support\n" +
                    "• Gaming discussions\n" +
                    "• Creative writing assistance\n\n" +
                    "Connect to VirtualXanderServer to start chatting!",
                    "About VirtualXander",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    
    /**
     * Starts the client
     */
    public void start() {
        // Auto-connect
        connectToServer();
    }
    
    /**
     * Connects to the server
     */
    private void connectToServer() {
        if (connected) {
            return;
        }
        
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            connected = true;
            
            // Update GUI
            connectButton.setEnabled(false);
            disconnectButton.setEnabled(true);
            inputField.setEnabled(true);
            sendButton.setEnabled(true);
            statusLabel.setText("Status: Connected to " + host + ":" + port);
            
            appendToChat("═══════════════════════════════════════════════════════════\n");
            appendToChat("Connected to VirtualXander Server!\n");
            appendToChat("═══════════════════════════════════════════════════════════\n\n");
            
            // Start listener thread
            Thread listenerThread = new Thread(() -> listenForMessages());
            listenerThread.setDaemon(true);
            listenerThread.start();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,
                "Failed to connect to server: " + e.getMessage() + "\n\n" +
                "Make sure VirtualXanderServer is running on " + host + ":" + port,
                "Connection Failed",
                JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Status: Connection failed");
        }
    }
    
    /**
     * Disconnects from the server
     */
    private void disconnectFromServer() {
        if (!connected) {
            return;
        }
        
        try {
            if (out != null) {
                out.println("exit");
            }
            
            if (socket != null) {
                socket.close();
            }
            
            connected = false;
            
            // Update GUI
            connectButton.setEnabled(true);
            disconnectButton.setEnabled(false);
            inputField.setEnabled(false);
            sendButton.setEnabled(false);
            statusLabel.setText("Status: Disconnected");
            
            appendToChat("\n═══════════════════════════════════════════════════════════\n");
            appendToChat("Disconnected from server.\n");
            appendToChat("═══════════════════════════════════════════════════════════\n");
            
        } catch (IOException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
        }
    }
    
    /**
     * Listens for incoming messages
     */
    private void listenForMessages() {
        try {
            String message;
            while (connected && (message = in.readLine()) != null) {
                appendToChat(message + "\n");
            }
        } catch (IOException e) {
            if (connected) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(frame,
                        "Connection lost: " + e.getMessage(),
                        "Disconnected",
                        JOptionPane.WARNING_MESSAGE);
                    disconnectFromServer();
                });
            }
        }
    }
    
    /**
     * Sends a message
     */
    private void sendMessage() {
        if (!connected) {
            return;
        }
        
        String message = inputField.getText().trim();
        if (message.isEmpty()) {
            return;
        }
        
        // Display own message
        appendToChat("You: " + message + "\n");
        
        // Send to server
        out.println(message);
        inputField.setText("");
        inputField.requestFocus();
    }
    
    /**
     * Appends text to chat area (thread-safe)
     */
    private void appendToChat(String text) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(text);
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }
}
