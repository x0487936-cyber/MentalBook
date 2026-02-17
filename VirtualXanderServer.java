import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * VirtualXanderServer - Integrated Server for VirtualXander Chatbot
 * Connects VirtualXanderCore with Server/Client infrastructure
 * Part of Phase 5: Integration
 */
public class VirtualXanderServer {
    private static final int PORT = 12345;
    private static Set<ClientConnection> clients = new HashSet<>();
    private static ExecutorService threadPool = Executors.newFixedThreadPool(100);
    private static VirtualXanderCore virtualXander;
    private static boolean running = false;
    private static ServerSocket serverSocket;
    private static Logger logger;
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║            VirtualXander Server Starting...              ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        
        // Initialize logger
        logger = Logger.getInstance();
        logger.info("VirtualXanderServer", "Server starting...");
        
        // Initialize VirtualXander core
        try {
            virtualXander = new VirtualXanderCore();
            logger.info("VirtualXanderServer", "VirtualXanderCore initialized");
        } catch (Exception e) {
            logger.error("VirtualXanderServer", "Failed to initialize VirtualXanderCore", e);
            System.err.println("Failed to initialize VirtualXander: " + e.getMessage());
            return;
        }
        
        // Start server
        startServer();
    }
    
    /**
     * Starts the server
     */
    private static void startServer() {
        running = true;
        
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
            logger.info("VirtualXanderServer", "Server started on port " + PORT);
            System.out.println("Waiting for client connections...");
            
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());
                    logger.info("VirtualXanderServer", "New client: " + clientSocket.getRemoteSocketAddress());
                    
                    ClientConnection client = new ClientConnection(clientSocket);
                    clients.add(client);
                    threadPool.execute(client);
                    
                } catch (IOException e) {
                    if (running) {
                        logger.error("VirtualXanderServer", "Error accepting client", e);
                    }
                }
            }
            
        } catch (IOException e) {
            logger.error("VirtualXanderServer", "Failed to start server", e);
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
    
    /**
     * Stops the server
     */
    public static void stopServer() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              Server Shutting Down...                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        
        running = false;
        
        // Close all client connections
        for (ClientConnection client : new HashSet<>(clients)) {
            client.close();
        }
        
        // Shutdown thread pool
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        // Close server socket
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            logger.error("VirtualXanderServer", "Error closing server socket", e);
        }
        
        logger.info("VirtualXanderServer", "Server stopped");
        System.out.println("Server stopped.");
    }
    
    /**
     * Gets the number of connected clients
     */
    public static int getConnectedClients() {
        return clients.size();
    }
    
    /**
     * Gets the VirtualXanderCore instance
     */
    public static VirtualXanderCore getVirtualXander() {
        return virtualXander;
    }
    
    /**
     * Broadcasts a message to all connected clients
     */
    public static void broadcast(String message, String sender) {
        String formattedMessage = "[Server] " + sender + ": " + message;
        System.out.println(formattedMessage);
        
        for (ClientConnection client : new HashSet<>(clients)) {
            client.sendMessage(formattedMessage);
        }
    }
    
    /**
     * Client connection handler
     */
    private static class ClientConnection implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientId;
        private VirtualXanderCore clientCore;
        private boolean authenticated;
        
        public ClientConnection(Socket socket) {
            this.socket = socket;
            this.clientId = "Client-" + UUID.randomUUID().toString().substring(0, 8);
            this.clientCore = new VirtualXanderCore();
            this.authenticated = false;
        }
        
        @Override
        public void run() {
            try {
                // Initialize streams
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                // Send welcome message
                sendWelcomeMessage();
                
                String message;
                while (running && (message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit") || message.equalsIgnoreCase("quit")) {
                        sendMessage("Goodbye! Thanks for chatting with VirtualXander!");
                        break;
                    }
                    
                    if (message.equalsIgnoreCase("/status")) {
                        sendServerStatus();
                        continue;
                    }
                    
                    if (message.equalsIgnoreCase("/help")) {
                        sendHelpMessage();
                        continue;
                    }
                    
                    // Process message through VirtualXander
                    processMessage(message);
                }
                
            } catch (IOException e) {
                if (running) {
                    logger.error("VirtualXanderServer", "Client error: " + clientId, e);
                }
            } finally {
                close();
                clients.remove(this);
                System.out.println("Client disconnected: " + clientId);
                logger.info("VirtualXanderServer", "Client disconnected: " + clientId);
            }
        }
        
        private void sendWelcomeMessage() {
            String welcome = 
                "\n╔═══════════════════════════════════════════════════════════╗\n" +
                "║              Welcome to VirtualXander!                  ║\n" +
                "║         Your Personal Powered Chat Companion              ║\n" +
                "╠═══════════════════════════════════════════════════════════╣\n" +
                "║  Commands:                                             ║\n" +
                "║    /help  - Show available commands                     ║\n" +
                "║    /status - Show server status                        ║\n" +
                "║    exit    - Disconnect from server                    ║\n" +
                "╚═══════════════════════════════════════════════════════════╝\n\n" +
                "Xander: Hello! I'm VirtualXander, your friendly chat companion.\n" +
                "        How can I help you today?\n";
            
            sendMessage(welcome);
            authenticated = true;
        }
        
        private void sendHelpMessage() {
            String help = 
                "\n╔═══════════════════════════════════════════════════════════╗\n" +
                "║                  Available Commands                     ║\n" +
                "╠═══════════════════════════════════════════════════════════╣\n" +
                "║  /help     - Show this help message                    ║\n" +
                "║  /status   - Show server and connection status        ║\n" +
                "║  /stats    - Show conversation statistics              ║\n" +
                "║  /reset    - Reset conversation                         ║\n" +
                "║  /clear    - Clear screen                              ║\n" +
                "║  exit      - Disconnect from server                    ║\n" +
                "╠═══════════════════════════════════════════════════════════╣\n" +
                "║  I can help you with:                                 ║\n" +
                "║  • Greetings and casual chat                          ║\n" +
                "║  • Homework and academic questions                     ║\n" +
                "║  • Mental health support                              ║\n" +
                "║  • Gaming discussions                                  ║\n" +
                "║  • Creative writing assistance                        ║\n" +
                "║  • And much more!                                     ║\n" +
                "╚═══════════════════════════════════════════════════════════╝\n";
            
            sendMessage(help);
        }
        
        private void sendServerStatus() {
            String status = 
                "\n╔═══════════════════════════════════════════════════════════╗\n" +
                "║                  Server Status                           ║\n" +
                "╠═══════════════════════════════════════════════════════════╣\n" +
                "║  Your ID: " + String.format("%-42s", clientId) + "║\n" +
                "║  Connected Clients: " + String.format("%-35d", getConnectedClients()) + "║\n" +
                "║  Server Status: " + String.format("%-37s", running ? "Online" : "Offline") + "║\n" +
                "╚═══════════════════════════════════════════════════════════╝\n";
            
            sendMessage(status);
        }
        
        private void processMessage(String message) {
            // Log the message
            System.out.println("[" + clientId + "] " + message);
            logger.info("VirtualXanderServer", "Message from " + clientId + ": " + message);
            
            // Check for special commands
            if (message.startsWith("/")) {
                handleCommand(message);
                return;
            }
            
            // Process through VirtualXanderCore
            try {
                String response = clientCore.processMessage(message);
                sendMessage("Xander: " + response);
                
                // Log analytics
                logger.trackUserInteraction("message", clientCore.getIntentRecognizer().recognizeIntent(message));
                
            } catch (Exception e) {
                logger.error("VirtualXanderServer", "Error processing message", e);
                sendMessage("Xander: I'm sorry, something went wrong. Please try again.");
            }
        }
        
        private void handleCommand(String command) {
            String lowerCommand = command.toLowerCase();
            
            switch (lowerCommand) {
                case "/status":
                    sendServerStatus();
                    break;
                    
                case "/help":
                    sendHelpMessage();
                    break;
                    
                case "/stats":
                    sendConversationStats();
                    break;
                    
                case "/reset":
                    clientCore.getConversationContext().reset();
                    clientCore.getStateMachine().reset();
                    sendMessage("Xander: Conversation reset! Let's start fresh.");
                    break;
                    
                case "/clear":
                    sendMessage("\n".repeat(50));
                    sendWelcomeMessage();
                    break;
                    
                default:
                    sendMessage("Xander: Unknown command. Type /help for available commands.");
            }
        }
        
        private void sendConversationStats() {
            ConversationContext context = clientCore.getConversationContext();
            
            String stats = 
                "\n╔═══════════════════════════════════════════════════════════╗\n" +
                "║              Conversation Statistics                    ║\n" +
                "╠═══════════════════════════════════════════════════════════╣\n" +
                "║  Total Turns: " + String.format("%-39d", context.getTurnCount()) + "║\n" +
                "║  Duration: " + String.format("%-40d", context.getConversationDurationMinutes()) + " minutes║\n" +
                "║  Current Topic: " + String.format("%-36s", context.getCurrentTopic()) + "║\n" +
                "║  State: " + String.format("%-43s", clientCore.getStateMachine().getCurrentStateName()) + "║\n" +
                "╚═══════════════════════════════════════════════════════════╝\n";
            
            sendMessage(stats);
        }
        
        public void sendMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }
        
        public void close() {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                logger.error("VirtualXanderServer", "Error closing client connection", e);
            }
        }
    }
}

