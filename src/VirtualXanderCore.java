import java.util.*;

/**
 * VirtualXanderCore - Main orchestrator for the VirtualXander chatbot
 * Integrates Intent Recognition, Conversation Context, Response Generation, and State Machine
 * 
 * Phase 2 Features:
 * - Emotion Detection System
 * - Context-Aware Response Logic
 * - Topic Clustering System
 * - Plugin System for extensibility
 */
public class VirtualXanderCore {
    
    private IntentRecognizer intentRecognizer;
    private ConversationContext conversationContext;
    private ResponseGenerator responseGenerator;
    private ConversationStateMachine stateMachine;
    
    // Phase 2 Advanced Features
    private EmotionDetector emotionDetector;
    private ContextAwareResponseLogic contextAwareLogic;
    private TopicClusteringSystem topicClusteringSystem;
    private PluginSystem pluginSystem;
    
    private boolean isRunning;
    private Scanner scanner;
    
    public VirtualXanderCore() {
        this.intentRecognizer = new IntentRecognizer();
        this.conversationContext = new ConversationContext();
        this.responseGenerator = new ResponseGenerator();
        this.stateMachine = new ConversationStateMachine();
        
        // Initialize Phase 2 features
        this.emotionDetector = new EmotionDetector();
        this.contextAwareLogic = new ContextAwareResponseLogic();
        this.contextAwareLogic.setContext(conversationContext);
        this.contextAwareLogic.setEmotionDetector(emotionDetector);
        this.topicClusteringSystem = new TopicClusteringSystem();
        this.pluginSystem = new PluginSystem();
        
        this.isRunning = false;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Starts the conversation
     */
    public void start() {
        isRunning = true;
        printWelcome();
        
        while (isRunning) {
            try {
                processUserInput();
            } catch (Exception e) {
                handleError(e);
            }
        }
    }
    
    /**
     * Starts the conversation with custom scanner (for testing/integration)
     */
    public void start(Scanner customScanner) {
        this.scanner = customScanner;
        start();
    }
    
    private void printWelcome() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                    VirtualXander                          ║");
        System.out.println("║         Your Friendly Virtual Companion                   ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  I'm here to chat, help with homework, discuss games,    ║");
        System.out.println("║  provide mental health support, creative writing help,   ║");
        System.out.println("║  and much more! Now with enhanced emotion detection!    ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  Commands:                                                ║");
        System.out.println("║  - Type 'exit' or 'bye' to end the conversation         ║");
        System.out.println("║  - Type 'reset' to start a new conversation             ║");
        System.out.println("║  - Type 'status' to see current state                    ║");
        System.out.println("║  - Type 'emotion' to see detected emotion               ║");
        System.out.println("║  - Type 'topics' to see topic clusters                  ║");
        System.out.println("║  - Type 'plugins' to see loaded plugins                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Hello! Start chatting with me. Type 'exit' to end the chat.");
        System.out.println();
    }
    
    private void processUserInput() {
        System.out.print("You: ");
        String userInput = scanner.nextLine().trim();
        
        // Handle special commands
        if (handleSpecialCommands(userInput)) {
            return;
        }
        
        // Check for empty input
        if (userInput.isEmpty()) {
            System.out.println("Xander: I didn't catch that. Could you say something?");
            return;
        }
        
        // Phase 2: Detect emotions
        EmotionDetector.EmotionResult emotionResult = emotionDetector.detectEmotion(userInput);
        
        // Phase 2: Identify topics
        List<String> identifiedTopics = topicClusteringSystem.identifyTopics(userInput);
        if (!identifiedTopics.isEmpty()) {
            String dominantTopic = identifiedTopics.get(0);
            topicClusteringSystem.activateCluster(dominantTopic);
        }
        
        // Recognize intent
        String intent = intentRecognizer.recognizeIntent(userInput);
        
        // Check for farewell command
        if (intent.equals("farewell") || userInput.toLowerCase().contains("exit")) {
            farewell();
            return;
        }
        
        // Process through state machine
        ConversationStateMachine.ConversationState newState = stateMachine.processIntent(intent);
        
        // Generate response with Phase 2 enhancements
        String response = responseGenerator.generateResponse(intent, userInput, conversationContext);
        
        // Apply context-aware enhancements
        response = contextAwareLogic.generateContextualResponse(
            intent, userInput, emotionResult.getPrimaryEmotion(), conversationContext
        );
        
        // Add contextual follow-up for certain states
        response = addContextualFollowUp(intent, response, conversationContext);
        
        // Print response
        System.out.println("Xander: " + response);
        System.out.println();
        
        // Log state and emotion info (for debugging)
        logStateInfo(intent, newState, emotionResult);
    }
    
    private boolean handleSpecialCommands(String input) {
        String lowerInput = input.toLowerCase().trim();
        
        if (lowerInput.equals("exit") || lowerInput.equals("bye")) {
            farewell();
            return true;
        }
        
        if (lowerInput.equals("reset")) {
            resetConversation();
            return true;
        }
        
        if (lowerInput.equals("status")) {
            printStatus();
            return true;
        }
        
        if (lowerInput.equals("help")) {
            printHelp();
            return true;
        }
        
        if (lowerInput.equals("history")) {
            printConversationHistory();
            return true;
        }
        
        // Phase 2 special commands
        if (lowerInput.equals("emotion")) {
            printEmotionStatus();
            return true;
        }
        
        if (lowerInput.equals("topics") || lowerInput.equals("topic")) {
            printTopicClusters();
            return true;
        }
        
        if (lowerInput.equals("plugins")) {
            printPluginStatus();
            return true;
        }
        
        if (lowerInput.equals("analyze")) {
            analyzeConversation();
            return true;
        }
        
        return false;
    }
    
    private void farewell() {
        System.out.println("Xander: Goodbye! It was great chatting with you. Take care!");
        System.out.println();
        System.out.println("Conversation Statistics:");
        System.out.println("  - Total turns: " + conversationContext.getTurnCount());
        System.out.println("  - Duration: " + conversationContext.getConversationDurationMinutes() + " minutes");
        System.out.println("  - Topics explored: " + topicClusteringSystem.getActiveClusters().size());
        isRunning = false;
    }
    
    private void resetConversation() {
        conversationContext.reset();
        stateMachine.reset();
        topicClusteringSystem.resetClusters();
        System.out.println("Xander: Conversation reset! Let's start fresh. Hi there!");
        System.out.println();
    }
    
    private void printStatus() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Current Status                              ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ State: " + String.format("%-40s", stateMachine.getCurrentStateName()) + "║");
        System.out.println("║ Turns: " + String.format("%-41s", conversationContext.getTurnCount()) + "║");
        System.out.println("║ Duration: " + String.format("%-37s", conversationContext.getConversationDurationMinutes() + " min") + "║");
        System.out.println("║ Topic: " + String.format("%-40s", conversationContext.getCurrentTopic()) + "║");
        System.out.println("║ Plugins: " + String.format("%-38s", pluginSystem.getPluginCount() + " loaded") + "║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void printEmotionStatus() {
        String lastInput = conversationContext.getLastUserInput();
        if (lastInput == null) {
            System.out.println("Xander: No conversation yet to analyze emotions.");
            return;
        }
        
        EmotionDetector.EmotionResult result = emotionDetector.detectEmotion(lastInput);
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Emotion Analysis                             ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Primary Emotion: " + String.format("%-35s", result.getPrimaryEmotion().getName()) + "║");
        System.out.println("║ Confidence: " + String.format("%-38s", String.format("%.2f", result.getConfidence())) + "║");
        System.out.println("║ Is Positive: " + String.format("%-37s", result.isPositive()) + "║");
        System.out.println("║ Is Negative: " + String.format("%-37s", result.isNegative()) + "║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void printTopicClusters() {
        List<TopicClusteringSystem.TopicCluster> activeClusters = topicClusteringSystem.getActiveClusters();
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Topic Clusters                              ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        
        if (activeClusters.isEmpty()) {
            System.out.println("║ No active topics yet. Start chatting to explore topics!  ║");
        } else {
            for (TopicClusteringSystem.TopicCluster cluster : activeClusters) {
                System.out.println("║ " + String.format("%-45s", cluster.clusterName + " (Visits: " + cluster.visitCount + ")") + "║");
            }
        }
        
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void printPluginStatus() {
        System.out.println(pluginSystem.getPluginSummary());
    }
    
    private void analyzeConversation() {
        ContextAwareResponseLogic.ConversationAnalysis analysis = 
            contextAwareLogic.analyzeConversation(conversationContext);
        
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Conversation Analysis                       ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Total Turns: " + String.format("%-37s", analysis.turnCount) + "║");
        System.out.println("║ Duration: " + String.format("%-38s", analysis.durationMinutes + " minutes") + "║");
        System.out.println("║ Current Topic: " + String.format("%-35s", analysis.currentTopic) + "║");
        System.out.println("║ Engagement Score: " + String.format("%-33s", String.format("%.2f", analysis.engagementScore)) + "║");
        System.out.println("║ Conversation Stage: " + String.format("%-33s", analysis.stage) + "║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        
        if (!analysis.recommendations.isEmpty()) {
            System.out.println("Recommendations:");
            for (String rec : analysis.recommendations) {
                System.out.println("  - " + rec);
            }
            System.out.println();
        }
    }
    
    private void printHelp() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                  Help & Commands                           ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  I can help you with:                                     ║");
        System.out.println("║  • Greetings and casual chat                              ║");
        System.out.println("║  • Homework and academic questions                        ║");
        System.out.println("║  • Mental health and emotional support                    ║");
        System.out.println("║  • Gaming discussions                                     ║");
        System.out.println("║  • Creative writing assistance                            ║");
        System.out.println("║  • Entertainment recommendations                          ║");
        System.out.println("║  • General advice and tips                                ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  Commands:                                                ║");
        System.out.println("║  • exit/bye - End conversation                            ║");
        System.out.println("║  • reset - Start new conversation                         ║");
        System.out.println("║  • status - Show current status                           ║");
        System.out.println("║  • emotion - Show emotion analysis                        ║");
        System.out.println("║  • topics - Show topic clusters                           ║");
        System.out.println("║  • plugins - Show loaded plugins                          ║");
        System.out.println("║  • analyze - Analyze conversation                         ║");
        System.out.println("║  • help - Show this help message                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void printConversationHistory() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              Conversation History                          ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        List<String> history = conversationContext.getRecentHistory(10);
        for (String turn : history) {
            String displayTurn = turn.length() > 45 ? turn.substring(0, 45) : turn;
            System.out.println("║ " + String.format("%-45s", displayTurn) + "║");
        }
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
    }
    
    private String addContextualFollowUp(String intent, String response, ConversationContext context) {
        // Add follow-up questions for certain intents to keep conversation flowing
        switch (intent) {
            case "greeting":
                if (!context.getCurrentTopic().equals("greeting")) {
                    return response + " Is there anything specific you'd like to talk about?";
                }
                break;
                
            case "homework_help":
                return response + " Just tell me what subject and topic you're working on!";
                
            case "mental_health_support":
                return response + " Remember, I'm here to listen without judgment.";
                
            case "creative_writing":
                return response + " I'd love to hear more about your creative ideas!";
                
            case "gaming":
                return response + " What games have you been enjoying lately?";
        }
        return response;
    }
    
    private void logStateInfo(String intent, ConversationStateMachine.ConversationState state, 
                              EmotionDetector.EmotionResult emotion) {
        // Uncomment for debugging
        // System.err.println("[DEBUG] Intent: " + intent + " -> State: " + state.getName() + " -> Emotion: " + emotion.getPrimaryEmotion().getName());
    }
    
    private void handleError(Exception e) {
        System.out.println("Xander: Oops! Something went wrong. Let's try again!");
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
    }
    
    /**
     * Processes a single message and returns the response (for API/integration use)
     */
    public String processMessage(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return "I didn't catch that. Could you say something?";
        }
        
        String normalizedInput = userInput.trim().toLowerCase();
        
        // Handle special commands
        if (normalizedInput.equals("exit") || normalizedInput.equals("bye")) {
            isRunning = false;
            return "Goodbye! It was great chatting with you.";
        }
        
        // Phase 2: Detect emotions
        EmotionDetector.EmotionResult emotionResult = emotionDetector.detectEmotion(userInput);
        
        // Phase 2: Identify topics
        List<String> identifiedTopics = topicClusteringSystem.identifyTopics(userInput);
        if (!identifiedTopics.isEmpty()) {
            topicClusteringSystem.activateCluster(identifiedTopics.get(0));
        }
        
        // Recognize intent
        String intent = intentRecognizer.recognizeIntent(userInput);
        
        // Process through state machine
        ConversationStateMachine.ConversationState state = stateMachine.processIntent(intent);
        
        // Generate response with Phase 2 enhancements
        String response = responseGenerator.generateResponse(intent, userInput, conversationContext);
        
        // Apply context-aware enhancements
        response = contextAwareLogic.generateContextualResponse(
            intent, userInput, emotionResult.getPrimaryEmotion(), conversationContext
        );
        
        return response;
    }
    
    // Phase 2: Getters for advanced features
    
    /**
     * Gets the emotion detector
     */
    public EmotionDetector getEmotionDetector() {
        return emotionDetector;
    }
    
    /**
     * Gets the context-aware response logic
     */
    public ContextAwareResponseLogic getContextAwareLogic() {
        return contextAwareLogic;
    }
    
    /**
     * Gets the topic clustering system
     */
    public TopicClusteringSystem getTopicClusteringSystem() {
        return topicClusteringSystem;
    }
    
    /**
     * Gets the plugin system
     */
    public PluginSystem getPluginSystem() {
        return pluginSystem;
    }
    
    /**
     * Gets the current state machine
     */
    public ConversationStateMachine getStateMachine() {
        return stateMachine;
    }
    
    /**
     * Gets the conversation context
     */
    public ConversationContext getConversationContext() {
        return conversationContext;
    }
    
    /**
     * Gets the intent recognizer
     */
    public IntentRecognizer getIntentRecognizer() {
        return intentRecognizer;
    }
    
    /**
     * Gets the response generator
     */
    public ResponseGenerator getResponseGenerator() {
        return responseGenerator;
    }
    
    /**
     * Checks if the conversation is running
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Stops the conversation
     */
    public void stop() {
        isRunning = false;
        if (scanner != null) {
            scanner.close();
        }
    }
}

