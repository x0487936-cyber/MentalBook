import java.util.*;
import java.time.LocalDateTime;

/**
 * Conversation Context Manager for VirtualXander
 * Manages conversation state, history, and context
 * 
 * Integration with ContextEngine:
 * - Optionally syncs with ContextEngine for advanced entity tracking
 * - Synchronizes conversation turns with ContextEngine threads
 * - Tracks open questions in ContextEngine for follow-up
 * - Maintains backward compatibility with existing API
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
    
    // Topic clusters tracking
    private boolean topicClustersJustListed;
    
    // ContextEngine integration (optional)
    private ContextEngine contextEngine;
    private boolean syncWithContextEngine;
    
    public ConversationContext() {
        this.conversationHistory = new ArrayList<>();
        this.sessionData = new HashMap<>();
        this.userPreferences = new HashMap<>();
        this.currentTopic = "general";
        this.currentState = "idle";
        this.lastInteractionTime = LocalDateTime.now();
        this.conversationStartTime = LocalDateTime.now();
        this.conversationTurnCount = 0;
        this.contextEngine = null;
        this.syncWithContextEngine = false;
    }
    
    /**
     * Represents a single turn in the conversation
     */
    private static class ConversationTurn {
        String userInput;
        String botResponse;
        String topic;
        LocalDateTime timestamp;
        Map<String, Object> metadata;
        
        public ConversationTurn(String userInput, String botResponse, String topic) {
            this.userInput = userInput;
            this.botResponse = botResponse;
            this.topic = topic;
            this.timestamp = LocalDateTime.now();
            this.metadata = new HashMap<>();
        }
        
        public Map<String, Object> getMetadata() {
            return metadata;
        }
        
        public void setMetadata(String key, Object value) {
            this.metadata.put(key, value);
        }
        
        public String getTopic() {
            return topic;
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
    
    // ==================== CONTEXTENGINE INTEGRATION ====================
    
    /**
     * Set the ContextEngine instance for integration
     * When set, conversation turns, entities, and questions will be synced
     * @param contextEngine The ContextEngine to integrate with
     */
    public void setContextEngine(ContextEngine contextEngine) {
        this.contextEngine = contextEngine;
        this.syncWithContextEngine = (contextEngine != null);
    }
    
    /**
     * Get the current ContextEngine instance
     * @return The ContextEngine, or null if not set
     */
    public ContextEngine getContextEngine() {
        return contextEngine;
    }
    
    /**
     * Enable or disable syncing with ContextEngine
     * @param sync True to enable syncing, false to disable
     */
    public void setSyncWithContextEngine(boolean sync) {
        this.syncWithContextEngine = sync && contextEngine != null;
    }
    
    /**
     * Check if syncing with ContextEngine is enabled
     * @return True if syncing is enabled
     */
    public boolean isSyncWithContextEngine() {
        return syncWithContextEngine && contextEngine != null;
    }
    
    /**
     * Register an entity in both ConversationContext and ContextEngine
     * @param name Entity name
     * @param type Entity type (person, place, thing, etc.)
     * @param memoryTier Memory tier for ContextEngine
     * @return The registered entity from ContextEngine, or null if not integrated
     */
    public ContextEngine.ReferenceEntity registerEntity(String name, String type, ContextEngine.MemoryTier memoryTier) {
        if (contextEngine != null && syncWithContextEngine) {
            return contextEngine.addEntity(name, type, memoryTier);
        }
        return null;
    }
    
    /**
     * Find an entity by name in ContextEngine
     * @param name Entity name to search for
     * @return The entity if found, null otherwise
     */
    public ContextEngine.ReferenceEntity findEntity(String name) {
        if (contextEngine != null && syncWithContextEngine) {
            return contextEngine.findEntityByName(name);
        }
        return null;
    }
    
    /**
     * Get all tracked entities from ContextEngine
     * @return List of all entities, or empty list if not integrated
     */
    public List<ContextEngine.ReferenceEntity> getAllEntities() {
        if (contextEngine != null && syncWithContextEngine) {
            return contextEngine.getAllEntities();
        }
        return new ArrayList<>();
    }
    
    /**
     * Create an open question in ContextEngine for follow-up
     * @param questionText The question to track
     * @param askedBy Who asked the question
     * @param context Context around the question
     * @return The created question, or null if not integrated
     */
    public ContextEngine.OpenQuestion trackQuestion(String questionText, String askedBy, String context) {
        return trackQuestion(questionText, askedBy, context, ContextEngine.QuestionPriority.MEDIUM);
    }
    
    /**
     * Create an open question with priority in ContextEngine
     * @param questionText The question to track
     * @param askedBy Who asked the question
     * @param context Context around the question
     * @param priority Priority level for the question
     * @return The created question, or null if not integrated
     */
    public ContextEngine.OpenQuestion trackQuestion(String questionText, String askedBy, String context, 
                                                     ContextEngine.QuestionPriority priority) {
        if (contextEngine != null && syncWithContextEngine) {
            return contextEngine.createOpenQuestion(questionText, askedBy, context, priority);
        }
        return null;
    }
    
    /**
     * Get all open questions from ContextEngine
     * @return List of open questions sorted by priority, or empty list if not integrated
     */
    public List<ContextEngine.OpenQuestion> getOpenQuestions() {
        if (contextEngine != null && syncWithContextEngine) {
            return contextEngine.getOpenQuestions();
        }
        return new ArrayList<>();
    }
    
    /**
     * Mark a question as answered
     * @param questionId The ID of the question to mark as answered
     * @param answer The answer text
     */
    public void markQuestionAnswered(String questionId, String answer) {
        if (contextEngine != null && syncWithContextEngine) {
            contextEngine.markQuestionAnswered(questionId, answer);
        }
    }
    
    /**
     * Register a pronoun reference in ContextEngine
     * @param pronoun The pronoun (he, she, it, etc.)
     * @param entity The entity the pronoun refers to
     */
    public void registerReference(String pronoun, ContextEngine.ReferenceEntity entity) {
        if (contextEngine != null && syncWithContextEngine) {
            contextEngine.registerReference(pronoun, entity);
        }
    }
    
    /**
     * Resolve a pronoun reference in ContextEngine
     * @param pronoun The pronoun to resolve
     * @return The referenced entity, or null if not found or not integrated
     */
    public ContextEngine.ReferenceEntity resolveReference(String pronoun) {
        if (contextEngine != null && syncWithContextEngine) {
            return contextEngine.resolveReference(pronoun);
        }
        return null;
    }
    
    /**
     * Get the current conversation thread from ContextEngine
     * @return The current thread, or null if not integrated
     */
    public ContextEngine.ConversationThread getCurrentThread() {
        if (contextEngine != null && syncWithContextEngine) {
            return contextEngine.getCurrentThread();
        }
        return null;
    }
    
    /**
     * Create a new conversation thread in ContextEngine
     * @param topic The topic for the new thread
     * @return The created thread, or null if not integrated
     */
    public ContextEngine.ConversationThread createThread(String topic) {
        if (contextEngine != null && syncWithContextEngine) {
            return contextEngine.createThread(topic);
        }
        return null;
    }
    
    /**
     * Get context summary from both ConversationContext and ContextEngine
     * @return Enhanced context summary including ContextEngine data
     */
    public EnhancedContextSummary getEnhancedContextSummary() {
        Map<String, Object> engineSummary = null;
        if (contextEngine != null && syncWithContextEngine) {
            engineSummary = contextEngine.getContextSummary();
        }
        
        return new EnhancedContextSummary(
            currentTopic,
            currentState,
            conversationTurnCount,
            getConversationDurationMinutes(),
            getLastUserInput(),
            getRecentHistory(3),
            engineSummary
        );
    }
    
    /**
     * Enhanced context summary that includes ContextEngine data
     */
    public static class EnhancedContextSummary extends ContextSummary {
        public final Map<String, Object> contextEngineData;
        public final int entityCount;
        public final int openQuestionCount;
        public final double threadCoherence;
        
        public EnhancedContextSummary(String currentTopic, String currentState, int turnCount,
                                     long durationMinutes, String lastInput, List<String> recentHistory,
                                     Map<String, Object> contextEngineData) {
            super(currentTopic, currentState, turnCount, durationMinutes, lastInput, recentHistory);
            this.contextEngineData = contextEngineData;
            this.entityCount = contextEngineData != null ? 
                (Integer) contextEngineData.getOrDefault("totalEntities", 0) : 0;
            this.openQuestionCount = contextEngineData != null ? 
                (Integer) contextEngineData.getOrDefault("openQuestions", 0) : 0;
            this.threadCoherence = contextEngineData != null ? 
                (Double) contextEngineData.getOrDefault("currentCoherence", 0.0) : 0.0;
        }
    }
    
    // ==================== EXISTING METHODS (UNCHANGED) ====================
    
    /**
     * Gets metadata from the last conversation turn
     */
    public Map<String, Object> getLastTurnMetadata() {
        if (conversationHistory.isEmpty()) {
            return null;
        }
        return conversationHistory.get(conversationHistory.size() - 1).getMetadata();
    }
    
    /**
     * Sets metadata on the last conversation turn
     */
    public void setLastTurnMetadata(String key, Object value) {
        if (!conversationHistory.isEmpty()) {
            conversationHistory.get(conversationHistory.size() - 1).setMetadata(key, value);
        }
    }
    
    /**
     * Gets the topic of the last conversation turn
     */
    public String getLastTurnTopic() {
        if (conversationHistory.isEmpty()) {
            return null;
        }
        return conversationHistory.get(conversationHistory.size() - 1).getTopic();
    }
    
    /**
     * Gets the timestamp of the last conversation turn
     */
    public LocalDateTime getLastTurnTimestamp() {
        if (conversationHistory.isEmpty()) {
            return null;
        }
        return conversationHistory.get(conversationHistory.size() - 1).getTimestamp();
    }
    
    /**
     * Adds a conversation turn to the history
     * Also syncs with ContextEngine if integration is enabled
     */
    public void addTurn(String userInput, String botResponse, String topic) {
        ConversationTurn turn = new ConversationTurn(userInput, botResponse, topic);
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
        
        // Sync with ContextEngine if enabled
        if (contextEngine != null && syncWithContextEngine) {
            contextEngine.addMessageToCurrentThread("User", userInput);
            contextEngine.addMessageToCurrentThread("Xander", botResponse);
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
     * Also resets ContextEngine if integrated
     */
    public void reset() {
        conversationHistory.clear();
        sessionData.clear();
        currentTopic = "general";
        currentState = "idle";
        conversationStartTime = LocalDateTime.now();
        conversationTurnCount = 0;
        topicClustersJustListed = false;
        
        // Also reset ContextEngine if integrated
        if (contextEngine != null && syncWithContextEngine) {
            contextEngine.clearAll();
        }
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
     * Sets flag indicating Topic Clusters were just listed
     */
    public void setTopicClustersJustListed(boolean value) {
        this.topicClustersJustListed = value;
    }
    
    /**
     * Gets flag indicating Topic Clusters were just listed
     */
    public boolean isTopicClustersJustListed() {
        return topicClustersJustListed;
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
