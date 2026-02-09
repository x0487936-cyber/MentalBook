import java.util.*;
import java.time.LocalDateTime;

/**
 * Conversation Context Manager for VirtualXander
 * Manages conversation state, history, and context
 */
public class ConversationContext {
    
    private static final int MAX_HISTORY_SIZE = 50;
    private static final long CONTEXT_TIMEOUT_MINUTES = 30;
    
    private List<ConversationTurn> conversationHistory;
    private Map<String, Object> sessionData;
    private Map<String, String> userPreferences;
    private String currentTopic;
    private String currentState;
    private LocalDateTime lastInteractionTime;
    private LocalDateTime conversationStartTime;
    private int conversationTurnCount;
    
    public ConversationContext() {
        this.conversationHistory = new ArrayList<>();
        this.sessionData = new HashMap<>();
        this.userPreferences = new HashMap<>();
        this.currentTopic = "general";
        this.currentState = "idle";
        this.lastInteractionTime = LocalDateTime.now();
        this.conversationStartTime = LocalDateTime.now();
        this.conversationTurnCount = 0;
    }
    
    /**
     * Represents a single turn in the conversation
     */
    private static class ConversationTurn {
        String userInput;
        String recognizedIntent;
        String botResponse;
        String topic;
        LocalDateTime timestamp;
        Map<String, Object> metadata;
        
        public ConversationTurn(String userInput, String recognizedIntent, String botResponse, String topic) {
            this.userInput = userInput;
            this.recognizedIntent = recognizedIntent;
            this.botResponse = botResponse;
            this.topic = topic;
            this.timestamp = LocalDateTime.now();
            this.metadata = new HashMap<>();
        }
    }
    
    /**
     * Adds a conversation turn to the history
     */
    public void addTurn(String userInput, String recognizedIntent, String botResponse, String topic) {
        ConversationTurn turn = new ConversationTurn(userInput, recognizedIntent, botResponse, topic);
        conversationHistory.add(turn);
        
        // Update current topic
        if (topic != null && !topic.isEmpty()) {
            this.currentTopic = topic;
        }
        
        // Update state
        this.currentState = "processing";
        
        // Update counters and timestamps
        conversationTurnCount++;
        lastInteractionTime = LocalDateTime.now();
        
        // Maintain maximum history size
        if (conversationHistory.size() > MAX_HISTORY_SIZE) {
            conversationHistory.remove(0);
        }
    }
    
    /**
     * Gets the most recent user input
     */
    public String getLastUserInput() {
        if (conversationHistory.isEmpty()) {
            return null;
        }
        return conversationHistory.get(conversationHistory.size() - 1).userInput;
    }
    
    /**
     * Gets the last bot response
     */
    public String getLastBotResponse() {
        if (conversationHistory.isEmpty()) {
            return null;
        }
        return conversationHistory.get(conversationHistory.size() - 1).botResponse;
    }
    
    /**
     * Gets conversation history for a specific number of turns
     */
    public List<String> getRecentHistory(int turns) {
        List<String> recentTurns = new ArrayList<>();
        int start = Math.max(0, conversationHistory.size() - turns);
        
        for (int i = start; i < conversationHistory.size(); i++) {
            ConversationTurn turn = conversationHistory.get(i);
            recentTurns.add("User: " + turn.userInput);
            recentTurns.add("Xander: " + turn.botResponse);
        }
        
        return recentTurns;
    }
    
    /**
     * Gets the full conversation history
     */
    public List<ConversationTurn> getFullHistory() {
        return new ArrayList<>(conversationHistory);
    }
    
    /**
     * Searches conversation history for specific content
     */
    public List<ConversationTurn> searchHistory(String query) {
        List<ConversationTurn> matches = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (ConversationTurn turn : conversationHistory) {
            if (turn.userInput.toLowerCase().contains(lowerQuery) ||
                (turn.botResponse != null && turn.botResponse.toLowerCase().contains(lowerQuery))) {
                matches.add(turn);
            }
        }
        
        return matches;
    }
    
    /**
     * Gets conversation duration in minutes
     */
    public long getConversationDurationMinutes() {
        return java.time.Duration.between(conversationStartTime, LocalDateTime.now()).toMinutes();
    }
    
    /**
     * Gets number of conversation turns
     */
    public int getTurnCount() {
        return conversationTurnCount;
    }
    
    /**
     * Checks if conversation has been idle for too long
     */
    public boolean isContextExpired() {
        return java.time.Duration.between(lastInteractionTime, LocalDateTime.now()).toMinutes() > CONTEXT_TIMEOUT_MINUTES;
    }
    
    /**
     * Resets the conversation context
     */
    public void reset() {
        conversationHistory.clear();
        sessionData.clear();
        currentTopic = "general";
        currentState = "idle";
        conversationStartTime = LocalDateTime.now();
        conversationTurnCount = 0;
    }
    
    /**
     * Stores a value in session data
     */
    public void setSessionData(String key, Object value) {
        sessionData.put(key, value);
    }
    
    /**
     * Retrieves a value from session data
     */
    @SuppressWarnings("unchecked")
    public <T> T getSessionData(String key) {
        return (T) sessionData.get(key);
    }
    
    /**
     * Removes a value from session data
     */
    public void removeSessionData(String key) {
        sessionData.remove(key);
    }
    
    /**
     * Clears all session data
     */
    public void clearSessionData() {
        sessionData.clear();
    }
    
    /**
     * Sets a user preference
     */
    public void setPreference(String key, String value) {
        userPreferences.put(key, value);
    }
    
    /**
     * Gets a user preference
     */
    public String getPreference(String key) {
        return userPreferences.get(key);
    }
    
    /**
     * Gets all user preferences
     */
    public Map<String, String> getAllPreferences() {
        return new HashMap<>(userPreferences);
    }
    
    /**
     * Sets the current topic
     */
    public void setCurrentTopic(String topic) {
        this.currentTopic = topic;
    }
    
    /**
     * Gets the current topic
     */
    public String getCurrentTopic() {
        return currentTopic;
    }
    
    /**
     * Sets the current state
     */
    public void setCurrentState(String state) {
        this.currentState = state;
    }
    
    /**
     * Gets the current state
     */
    public String getCurrentState() {
        return currentState;
    }
    
    /**
     * Gets context summary for response generation
     */
    public ContextSummary getContextSummary() {
        return new ContextSummary(
            currentTopic,
            currentState,
            conversationTurnCount,
            getConversationDurationMinutes(),
            getLastUserInput(),
            getRecentHistory(3)
        );
    }
    
    /**
     * Context summary data class
     */
    public static class ContextSummary {
        public final String currentTopic;
        public final String currentState;
        public final int turnCount;
        public final long durationMinutes;
        public final String lastInput;
        public final List<String> recentHistory;
        
        public ContextSummary(String currentTopic, String currentState, int turnCount,
                             long durationMinutes, String lastInput, List<String> recentHistory) {
            this.currentTopic = currentTopic;
            this.currentState = currentState;
            this.turnCount = turnCount;
            this.durationMinutes = durationMinutes;
            this.lastInput = lastInput;
            this.recentHistory = recentHistory;
        }
    }
}

