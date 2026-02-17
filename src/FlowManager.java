import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

/**
 * FlowManager - Manages conversation flow and topic transitions for VirtualXander
 * 
 * Responsibilities:
 * - Topic Flow Control: Manages smooth transitions between conversation topics
 * - Conversation State: Tracks current topic, subtopics, and flow direction
 * - Flow Patterns: Implements various conversation flow strategies
 * - Priority Handling: Manages topic priorities and interruption handling
 * - Flow Metrics: Tracks conversation flow quality and engagement
 * 
 * This complements ContextEngine, NaturalProcessor, and ResponseRouter.
 */
public class FlowManager {
    
    // Current conversation flow state
    private FlowState currentFlowState;
    
    // Topic transition history
    private List<TopicTransition> transitionHistory;
    
    // Flow pattern strategies
    private Map<FlowPattern, FlowStrategy> flowStrategies;
    
    // Maximum history size
    private static final int MAX_HISTORY_SIZE = 50;
    
    // Flow quality thresholds
    private static final double MIN_ENGAGEMENT_THRESHOLD = 0.3;
    private static final int MAX_TOPIC_JUMPS = 3;
    
    // Current topic stack
    private Deque<String> topicStack;
    
    // Pending transitions
    private Queue<PendingTransition> pendingTransitions;
    
    // Flow metrics
    private FlowMetrics metrics;
    
    // Response length management
    private ResponseLengthConfig lengthConfig;
    
    // Conversation pacing controller
    private PacingController pacingController;
    
// Energy level matcher
    private EnergyLevelMatcher energyMatcher;
    
    // Smooth transition manager
    private SmoothTransitionManager smoothTransitionManager;
    
    // Question frequency calibrator
    private QuestionFrequencyCalibrator questionCalibrator;
    
    public FlowManager() {
        this.currentFlowState = new FlowState();
        this.transitionHistory = new ArrayList<>();
        this.topicStack = new ArrayDeque<>();
        this.pendingTransitions = new LinkedList<>();
        this.metrics = new FlowMetrics();
        this.lengthConfig = new ResponseLengthConfig();
        this.pacingController = new PacingController();
        this.energyMatcher = new EnergyLevelMatcher();
        this.smoothTransitionManager = new SmoothTransitionManager();
        this.questionCalibrator = new QuestionFrequencyCalibrator();
        initializeFlowStrategies();
    }
    
    // ==================== INNER CLASSES ====================
    
    /**
     * Represents the current state of conversation flow
     */
    public static class FlowState {
        private String currentTopic;
        private String previousTopic;
        private FlowDirection direction;
        private FlowPhase phase;
        private int topicStreak;
        private long lastTransitionTime;
        private boolean isInterrupted;
        private double engagementScore;
        
        public FlowState() {
            this.currentTopic = "greeting";
            this.previousTopic = null;
            this.direction = FlowDirection.FORWARD;
            this.phase = FlowPhase.INITIAL;
            this.topicStreak = 0;
            this.lastTransitionTime = System.currentTimeMillis();
            this.isInterrupted = false;
            this.engagementScore = 1.0;
        }
        
        public String getCurrentTopic() { return currentTopic; }
        public void setCurrentTopic(String t) { this.currentTopic = t; }
        
        public String getPreviousTopic() { return previousTopic; }
        public void setPreviousTopic(String t) { this.previousTopic = t; }
        
        public FlowDirection getDirection() { return direction; }
        public void setDirection(FlowDirection d) { this.direction = d; }
        
        public FlowPhase getPhase() { return phase; }
        public void setPhase(FlowPhase p) { this.phase = p; }
        
        public int getTopicStreak() { return topicStreak; }
        public void incrementStreak() { this.topicStreak++; }
        public void resetStreak() { this.topicStreak = 0; }
        
        public long getLastTransitionTime() { return lastTransitionTime; }
        public void setLastTransitionTime(long t) { this.lastTransitionTime = t; }
        
        public boolean isInterrupted() { return isInterrupted; }
        public void setInterrupted(boolean b) { this.isInterrupted = b; }
        
        public double getEngagementScore() { return engagementScore; }
        public void setEngagementScore(double d) { this.engagementScore = d; }
    }
    
    /**
     * Flow direction enum
     */
    public enum FlowDirection {
        FORWARD,     // Continuing on current topic
        BACKWARD,    // Returning to previous topic
        SIDETRACK,   // Exploring tangent
        INTERRUPT    // Emergency topic change
    }
    
    /**
     * Flow phase enum
     */
    public enum FlowPhase {
        INITIAL,      // Starting conversation
        EXPLORING,    // Deep discussion
        TRANSITION,   // Moving between topics
        WRAPPING_UP,  // Concluding topic
        COMPLETE      // End of conversation
    }
    
    /**
     * Flow pattern enum
     */
    public enum FlowPattern {
        LINEAR,       // Sequential topic progression
        BRANCHING,    // Topic branching based on user input
        CIRCULAR,     // Returning to themes
        ADAPTIVE,     // Dynamic flow based on context
        RESPONDENT    // Reacting to user cues
    }
    
    /**
     * Topic transition record
     */
    private static class TopicTransition {
        String fromTopic;
        String toTopic;
        TransitionType type;
        long timestamp;
        double smoothness;
        
        TopicTransition(String from, String to, TransitionType type, double smoothness) {
            this.fromTopic = from;
            this.toTopic = to;
            this.type = type;
            this.timestamp = System.currentTimeMillis();
            this.smoothness = smoothness;
        }
    }
    
    /**
     * Transition type enum
     */
    private enum TransitionType {
        USER_INITIATED,
        SYSTEM_INITIATED,
        CONTEXT_DRIVEN,
        INTERRUPTION,
        NATURAL
    }
    
    /**
     * Pending transition
     */
    private static class PendingTransition {
        String targetTopic;
        TransitionType type;
        int priority;
        long queuedTime;
        
        PendingTransition(String topic, TransitionType type, int priority) {
            this.targetTopic = topic;
            this.type = type;
            this.priority = priority;
            this.queuedTime = System.currentTimeMillis();
        }
    }
    
    /**
     * Flow strategy interface
     */
    private interface FlowStrategy {
        String determineNextTopic(FlowState state, String userInput, List<String> availableTopics);
        boolean shouldTransition(FlowState state, String userInput);
        double calculateTransitionSmoothness(String fromTopic, String toTopic);
    }
    
    /**
     * Flow metrics
     */
    private static class FlowMetrics {
        int totalTransitions;
        int abruptTransitions;
        int naturalTransitions;
        double averageEngagement;
        Map<String, Integer> topicFrequency;
        
        FlowMetrics() {
            this.totalTransitions = 0;
            this.abruptTransitions = 0;
            this.naturalTransitions = 0;
            this.averageEngagement = 1.0;
            this.topicFrequency = new HashMap<>();
        }
    }
    
    // ==================== INITIALIZATION ====================
    
    private void initializeFlowStrategies() {
        flowStrategies = new HashMap<>();
        
        flowStrategies.put(FlowPattern.LINEAR, new LinearFlowStrategy());
        flowStrategies.put(FlowPattern.BRANCHING, new BranchingFlowStrategy());
        flowStrategies.put(FlowPattern.CIRCULAR, new CircularFlowStrategy());
        flowStrategies.put(FlowPattern.ADAPTIVE, new AdaptiveFlowStrategy());
        flowStrategies.put(FlowPattern.RESPONDENT, new RespondentFlowStrategy());
    }
    
    // ==================== FLOW STRATEGIES ====================
    
    /**
     * Linear flow - sequential topic progression
     */
    private class LinearFlowStrategy implements FlowStrategy {
        private List<String> topicQueue;
        
        LinearFlowStrategy() {
            this.topicQueue = new ArrayList<>();
        }
        
        @Override
        public String determineNextTopic(FlowState state, String userInput, List<String> availableTopics) {
            if (topicQueue.isEmpty()) {
                // Generate linear sequence
                topicQueue = new ArrayList<>(availableTopics);
                Collections.shuffle(topicQueue);
            }
            
            if (!topicQueue.isEmpty()) {
                return topicQueue.remove(0);
            }
            
            return state.getCurrentTopic();
        }
        
        @Override
        public boolean shouldTransition(FlowState state, String userInput) {
            return state.getTopicStreak() >= 3 || 
                   userInput.contains("change") || 
                   userInput.contains("different");
        }
        
        @Override
        public double calculateTransitionSmoothness(String fromTopic, String toTopic) {
            return 0.8; // Linear is generally smooth
        }
    }
    
    /**
     * Branching flow - topic branching based on user input
     */
    private class BranchingFlowStrategy implements FlowStrategy {
        private Map<String, List<String>> topicBranches;
        
        BranchingFlowStrategy() {
            this.topicBranches = new HashMap<>();
            initializeBranches();
        }
        
        private void initializeBranches() {
            topicBranches.put("greeting", Arrays.asList("emotions", "activities", "help"));
            topicBranches.put("emotions", Arrays.asList("stress", "happiness", "relationships"));
            topicBranches.put("stress", Arrays.asList("coping", "work", "school"));
            topicBranches.put("happiness", Arrays.asList("achievements", "gratitude", "goals"));
            topicBranches.put("relationships", Arrays.asList("friends", "family", "romantic"));
        }
        
        @Override
        public String determineNextTopic(FlowState state, String userInput, List<String> availableTopics) {
            List<String> branches = topicBranches.get(state.getCurrentTopic());
            
            if (branches != null && !branches.isEmpty()) {
                // Choose branch based on keywords
                String detected = detectBranch(userInput, branches);
                if (detected != null) {
                    return detected;
                }
                return branches.get(0);
            }
            
            return availableTopics.get(new Random().nextInt(availableTopics.size()));
        }
        
        private String detectBranch(String input, List<String> branches) {
            input = input.toLowerCase();
            
            for (String branch : branches) {
                if (input.contains(branch)) {
                    return branch;
                }
            }
            
            return null;
        }
        
        @Override
        public boolean shouldTransition(FlowState state, String userInput) {
            return userInput.contains("what about") ||
                   userInput.contains("instead") ||
                   userInput.contains("related");
        }
        
        @Override
        public double calculateTransitionSmoothness(String fromTopic, String toTopic) {
            if (topicBranches.containsKey(fromTopic) && 
                topicBranches.get(fromTopic).contains(toTopic)) {
                return 0.95; // Very smooth for direct branches
            }
            return 0.5; // Less smooth for jumps
        }
    }
    
    /**
     * Circular flow - returning to themes
     */
    private class CircularFlowStrategy implements FlowStrategy {
        private static final int CIRCLE_SIZE = 3;
        
        @Override
        public String determineNextTopic(FlowState state, String userInput, List<String> availableTopics) {
            // Return to previous topic if we've been on current too long
            if (state.getTopicStreak() >= 4) {
                String previous = state.getPreviousTopic();
                if (previous != null) {
                    return previous;
                }
            }
            
            // Circular pattern: emotion -> activity -> achievement -> emotion
            return getCircularNext(state.getCurrentTopic(), availableTopics);
        }
        
        private String getCircularNext(String current, List<String> availableTopics) {
            String[] circle = {"emotions", "activities", "achievements"};
            
            int currentIndex = -1;
            for (int i = 0; i < circle.length; i++) {
                if (current.contains(circle[i])) {
                    currentIndex = i;
                    break;
                }
            }
            
            if (currentIndex >= 0) {
                int nextIndex = (currentIndex + 1) % circle.length;
                return circle[nextIndex];
            }
            
            return availableTopics.get(new Random().nextInt(availableTopics.size()));
        }
        
        @Override
        public boolean shouldTransition(FlowState state, String userInput) {
            return state.getTopicStreak() >= 4;
        }
        
        @Override
        public double calculateTransitionSmoothness(String fromTopic, String toTopic) {
            return 0.85; // Circular patterns are smooth
        }
    }
    
    /**
     * Adaptive flow - dynamic flow based on context
     */
    private class AdaptiveFlowStrategy implements FlowStrategy {
        @Override
        public String determineNextTopic(FlowState state, String userInput, List<String> availableTopics) {
            // Analyze user engagement
            double engagement = state.getEngagementScore();
            
            if (engagement < MIN_ENGAGEMENT_THRESHOLD) {
                // Low engagement - switch to more engaging topic
                return findEngagingTopic(availableTopics);
            }
            
            // Check for emotional cues
            if (detectEmotionalShift(userInput)) {
                return handleEmotionalTopic(userInput, availableTopics);
            }
            
            // Default to current flow
            return state.getCurrentTopic();
        }
        
        private String findEngagingTopic(List<String> topics) {
            String[] engagingTopics = {"hobbies", "fun", "achievements", "goals", "positive"};
            
            for (String engaging : engagingTopics) {
                for (String topic : topics) {
                    if (topic.toLowerCase().contains(engaging)) {
                        return topic;
                    }
                }
            }
            
            return topics.get(new Random().nextInt(topics.size()));
        }
        
        private boolean detectEmotionalShift(String input) {
            String[] positiveWords = {"happy", "great", "awesome", "excited", "love"};
            String[] negativeWords = {"sad", "upset", "angry", "anxious", "worried"};
            
            input = input.toLowerCase();
            
            for (String word : positiveWords) {
                if (input.contains(word)) return true;
            }
            
            for (String word : negativeWords) {
                if (input.contains(word)) return true;
            }
            
            return false;
        }
        
        private String handleEmotionalTopic(String input, List<String> availableTopics) {
            input = input.toLowerCase();
            
            if (containsAny(input, "happy", "great", "awesome", "excited", "love")) {
                return "celebration";
            }
            
            if (containsAny(input, "sad", "upset", "down", "blue")) {
                return "support";
            }
            
            if (containsAny(input, "anxious", "worried", "stressed", "overwhelmed")) {
                return "stress_management";
            }
            
            return availableTopics.get(new Random().nextInt(availableTopics.size()));
        }
        
        private boolean containsAny(String input, String... words) {
            for (String word : words) {
                if (input.contains(word)) return true;
            }
            return false;
        }
        
        @Override
        public boolean shouldTransition(FlowState state, String userInput) {
            return detectEmotionalShift(userInput) || 
                   state.getEngagementScore() < MIN_ENGAGEMENT_THRESHOLD;
        }
        
        @Override
        public double calculateTransitionSmoothness(String fromTopic, String toTopic) {
            return 0.7; // Adaptive can vary
        }
    }
    
    /**
     * Respondent flow - reacting to user cues
     */
    private class RespondentFlowStrategy implements FlowStrategy {
        @Override
        public String determineNextTopic(FlowState state, String userInput, List<String> availableTopics) {
            // React directly to user input
            String input = userInput.toLowerCase();
            
            // Detect explicit topic requests
            if (input.contains("talk about") || input.contains("discuss")) {
                return extractRequestedTopic(input, availableTopics);
            }
            
            // React to questions
            if (input.contains("?")) {
                return handleQuestion(state, availableTopics);
            }
            
            // React to statements
            return state.getCurrentTopic();
        }
        
        private String extractRequestedTopic(String input, List<String> availableTopics) {
            for (String topic : availableTopics) {
                if (input.contains(topic.toLowerCase())) {
                    return topic;
                }
            }
            return availableTopics.get(0);
        }
        
        private String handleQuestion(FlowState state, List<String> availableTopics) {
            // If user asks question, stay on topic to answer
            return state.getCurrentTopic();
        }
        
        @Override
        public boolean shouldTransition(FlowState state, String userInput) {
            return userInput.contains("change") ||
                   userInput.contains("different") ||
                   userInput.contains("something else");
        }
        
        @Override
        public double calculateTransitionSmoothness(String fromTopic, String toTopic) {
            return 0.9; // Respondent is usually smooth when user-led
        }
    }
    
    // ==================== PUBLIC METHODS ====================
    
    /**
     * Process a topic transition request
     * @param newTopic Target topic
     * @param type Transition type
     * @return Transition result
     */
    public TransitionResult transitionTo(String newTopic, TransitionType type) {
        String currentTopic = currentFlowState.getCurrentTopic();
        
        // Check if transition is allowed
        if (!isTransitionAllowed(currentTopic, newTopic, type)) {
            return new TransitionResult(false, currentTopic, "Transition blocked");
        }
        
        // Calculate smoothness
        FlowStrategy strategy = flowStrategies.get(FlowPattern.ADAPTIVE);
        double smoothness = strategy.calculateTransitionSmoothness(currentTopic, newTopic);
        
        // Create transition record
        TopicTransition transition = new TopicTransition(
            currentTopic, 
            newTopic, 
            type, 
            smoothness
        );
        
        // Update state
        currentFlowState.setPreviousTopic(currentTopic);
        currentFlowState.setCurrentTopic(newTopic);
        currentFlowState.setLastTransitionTime(System.currentTimeMillis());
        currentFlowState.setInterrupted(type == TransitionType.INTERRUPTION);
        
        // Update topic stack
        topicStack.push(currentTopic);
        if (topicStack.size() > MAX_HISTORY_SIZE) {
            topicStack.removeLast();
        }
        
        // Update history
        transitionHistory.add(transition);
        if (transitionHistory.size() > MAX_HISTORY_SIZE) {
            transitionHistory.remove(0);
        }
        
        // Update metrics
        updateMetrics(transition);
        
        // Update phase
        currentFlowState.setPhase(FlowPhase.TRANSITION);
        
        return new TransitionResult(true, newTopic, "Transition successful");
    }
    
    /**
     * Check if transition is allowed
     */
    private boolean isTransitionAllowed(String from, String to, TransitionType type) {
        // Interruptions are always allowed
        if (type == TransitionType.INTERRUPTION) {
            return true;
        }
        
        // Check for too many topic jumps
        int recentJumps = countRecentJumps();
        if (recentJumps >= MAX_TOPIC_JUMPS && type == TransitionType.USER_INITIATED) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Count recent topic jumps
     */
    private int countRecentJumps() {
        long now = System.currentTimeMillis();
        long window = 60000; // Last minute
        
        int jumps = 0;
        for (int i = transitionHistory.size() - 1; i >= 0; i--) {
            TopicTransition t = transitionHistory.get(i);
            if (now - t.timestamp > window) break;
            if (t.type == TransitionType.USER_INITIATED) jumps++;
        }
        
        return jumps;
    }
    
    /**
     * Update flow metrics
     */
    private void updateMetrics(TopicTransition transition) {
        metrics.totalTransitions++;
        
        if (transition.smoothness < 0.5) {
            metrics.abruptTransitions++;
        } else {
            metrics.naturalTransitions++;
        }
        
        String topic = transition.toTopic;
        metrics.topicFrequency.put(topic, metrics.topicFrequency.getOrDefault(topic, 0) + 1);
        
        // Update average engagement
        metrics.averageEngagement = (metrics.averageEngagement + currentFlowState.getEngagementScore()) / 2;
    }
    
    /**
     * Determine next topic based on current state
     * @param userInput User input
     * @param availableTopics Available topics
     * @return Recommended next topic
     */
    public String determineNextTopic(String userInput, List<String> availableTopics) {
        FlowPattern pattern = selectFlowPattern(userInput);
        FlowStrategy strategy = flowStrategies.get(pattern);
        
        String nextTopic = strategy.determineNextTopic(currentFlowState, userInput, availableTopics);
        
        // Update streak
        if (nextTopic.equals(currentFlowState.getCurrentTopic())) {
            currentFlowState.incrementStreak();
        } else {
            currentFlowState.resetStreak();
        }
        
        return nextTopic;
    }
    
    /**
     * Select appropriate flow pattern
     */
    private FlowPattern selectFlowPattern(String userInput) {
        userInput = userInput.toLowerCase();
        
        if (userInput.contains("what about") || userInput.contains("or")) {
            return FlowPattern.BRANCHING;
        }
        
        if (userInput.contains("before") || userInput.contains("back to")) {
            return FlowPattern.CIRCULAR;
        }
        
        if (userInput.contains("change") || userInput.contains("different")) {
            return FlowPattern.LINEAR;
        }
        
        if (userInput.contains("?") || userInput.contains("how") || userInput.contains("why")) {
            return FlowPattern.RESPONDENT;
        }
        
        return FlowPattern.ADAPTIVE;
    }
    
    /**
     * Queue a pending transition
     * @param topic Target topic
     * @param type Transition type
     * @param priority Priority (higher = more urgent)
     */
    public void queueTransition(String topic, TransitionType type, int priority) {
        pendingTransitions.add(new PendingTransition(topic, type, priority));
    }
    
    /**
     * Process pending transitions
     * @return Next topic to transition to, or null
     */
    public String processPendingTransitions() {
        if (pendingTransitions.isEmpty()) {
            return null;
        }
        
        // Sort by priority
        List<PendingTransition> sorted = new ArrayList<>(pendingTransitions);
        sorted.sort((a, b) -> Integer.compare(b.priority, a.priority));
        
        PendingTransition next = sorted.get(0);
        
        // Check if enough time has passed
        long elapsed = System.currentTimeMillis() - next.queuedTime;
        if (elapsed > 1000) { // Minimum 1 second between transitions
            pendingTransitions.remove(next);
            return next.targetTopic;
        }
        
        return null;
    }
    
    /**
     * Update engagement score
     * @param score New engagement score
     */
    public void updateEngagement(double score) {
        currentFlowState.setEngagementScore(score);
    }
    
    /**
     * Get flow quality score
     * @return Flow quality (0-1)
     */
    public double getFlowQuality() {
        if (metrics.totalTransitions == 0) {
            return 1.0;
        }
        
        double naturalRatio = (double) metrics.naturalTransitions / metrics.totalTransitions;
        double engagementFactor = metrics.averageEngagement;
        
        return (naturalRatio + engagementFactor) / 2;
    }
    
    /**
     * Get flow statistics
     * @return Flow statistics map
     */
    public Map<String, Object> getFlowStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalTransitions", metrics.totalTransitions);
        stats.put("abruptTransitions", metrics.abruptTransitions);
        stats.put("naturalTransitions", metrics.naturalTransitions);
        stats.put("flowQuality", getFlowQuality());
        stats.put("currentTopic", currentFlowState.getCurrentTopic());
        stats.put("engagementScore", currentFlowState.getEngagementScore());
        stats.put("topicStreak", currentFlowState.getTopicStreak());
        stats.put("topicFrequency", metrics.topicFrequency);
        
        return stats;
    }
    
    /**
     * Get transition history
     * @return List of recent transitions
     */
    public List<String> getTransitionHistory() {
        List<String> history = new ArrayList<>();
        
        for (TopicTransition t : transitionHistory) {
            history.add(String.format("%s -> %s (%s)", 
                t.fromTopic, t.toTopic, t.type));
        }
        
        return history;
    }
    
    /**
     * Reset flow manager
     */
    public void reset() {
        currentFlowState = new FlowState();
        transitionHistory.clear();
        topicStack.clear();
        pendingTransitions.clear();
        metrics = new FlowMetrics();
    }
    
    /**
     * Get current flow state
     * @return Current flow state
     */
    public FlowState getCurrentState() {
        return currentFlowState;
    }
    
    // ==================== RESULT CLASS ====================
    
    /**
     * Transition result
     */
    public static class TransitionResult {
        private boolean success;
        private String newTopic;
        private String message;
        
        public TransitionResult(boolean success, String newTopic, String message) {
            this.success = success;
            this.newTopic = newTopic;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public String getNewTopic() { return newTopic; }
        public String getMessage() { return message; }
    }
    
    // ==================== RESPONSE LENGTH MANAGEMENT ====================
    
    /**
     * Response length configuration and management
     */
    public static class ResponseLengthConfig {
        private ResponseLength currentLength;
        private int minWords;
        private int maxWords;
        private int targetWords;
        private double complexity;
        
        public enum ResponseLength {
            SHORT,      // 10-30 words
            MEDIUM,     // 30-60 words
            LONG,       // 60-100 words
            DETAILED    // 100+ words
        }
        
        public ResponseLengthConfig() {
            this.currentLength = ResponseLength.MEDIUM;
            this.minWords = 30;
            this.maxWords = 60;
            this.targetWords = 45;
            this.complexity = 0.5;
        }
        
        public ResponseLength getCurrentLength() { return currentLength; }
        
        public void setResponseLength(ResponseLength length) {
            this.currentLength = length;
            switch (length) {
                case SHORT:
                    this.minWords = 10;
                    this.maxWords = 30;
                    this.targetWords = 20;
                    break;
                case MEDIUM:
                    this.minWords = 30;
                    this.maxWords = 60;
                    this.targetWords = 45;
                    break;
                case LONG:
                    this.minWords = 60;
                    this.maxWords = 100;
                    this.targetWords = 80;
                    break;
                case DETAILED:
                    this.minWords = 100;
                    this.maxWords = 200;
                    this.targetWords = 150;
                    break;
            }
        }
        
        public int getMinWords() { return minWords; }
        public int getMaxWords() { return maxWords; }
        public int getTargetWords() { return targetWords; }
        public double getComplexity() { return complexity; }
        
        public void setComplexity(double c) { this.complexity = Math.max(0, Math.min(1, c)); }
        
        public ResponseLength suggestLength(String userInput, String topic) {
            String input = userInput.toLowerCase();
            int inputLength = userInput.split("\\s+").length;
            
            // Short responses for quick exchanges
            if (inputLength < 5 && (input.contains("?") || input.contains("ok") || input.contains("yes"))) {
                return ResponseLength.SHORT;
            }
            
            // Detailed for emotional topics
            if (containsAny(input, "sad", "depressed", "anxious", "worried", "stressed", "overwhelmed")) {
                return ResponseLength.LONG;
            }
            
            // Medium for most conversations
            return ResponseLength.MEDIUM;
        }
        
        private boolean containsAny(String input, String... words) {
            for (String word : words) {
                if (input.contains(word)) return true;
            }
            return false;
        }
    }
    
    /**
     * Get response length configuration
     * @return Response length config
     */
    public ResponseLengthConfig getResponseLengthConfig() {
        return lengthConfig;
    }
    
    /**
     * Determine appropriate response length
     * @param userInput User input
     * @param topic Current topic
     * @return Recommended response length
     */
    public ResponseLengthConfig.ResponseLength determineResponseLength(String userInput, String topic) {
        ResponseLengthConfig.ResponseLength suggested = lengthConfig.suggestLength(userInput, topic);
        lengthConfig.setResponseLength(suggested);
        return suggested;
    }
    
    // ==================== CONVERSATION PACING ====================
    
    /**
     * Conversation pacing controller
     */
    public static class PacingController {
        private PacingStyle currentPacing;
        private long lastResponseTime;
        private long averageResponseInterval;
        private List<Long> responseIntervals;
        private int exchangeCount;
        
        public enum PacingStyle {
            FAST,       // Quick responses, minimal pauses
            NORMAL,     // Natural conversation pace
            THOUGHTFUL, // Slightly slower, more deliberate
            DELIBERATE  // Longer pauses, considered responses
        }
        
        public PacingController() {
            this.currentPacing = PacingStyle.NORMAL;
            this.lastResponseTime = System.currentTimeMillis();
            this.averageResponseInterval = 2000; // 2 seconds default
            this.responseIntervals = new ArrayList<>();
            this.exchangeCount = 0;
        }
        
        public PacingStyle getCurrentPacing() { return currentPacing; }
        
        public void setPacing(PacingStyle pacing) {
            this.currentPacing = pacing;
        }
        
        public long getDelayBeforeResponse() {
            switch (currentPacing) {
                case FAST: return 200;
                case NORMAL: return 1500;
                case THOUGHTFUL: return 3000;
                case DELIBERATE: return 5000;
                default: return 1500;
            }
        }
        
        public void recordResponseTime() {
            long now = System.currentTimeMillis();
            long interval = now - lastResponseTime;
            
            responseIntervals.add(interval);
            if (responseIntervals.size() > 10) {
                responseIntervals.remove(0);
            }
            
            // Calculate average
            long sum = 0;
            for (Long i : responseIntervals) {
                sum += i;
            }
            averageResponseInterval = sum / responseIntervals.size();
            
            lastResponseTime = now;
            exchangeCount++;
        }
        
        public long getAverageResponseInterval() { return averageResponseInterval; }
        
        public PacingStyle detectPacingFromInput(String userInput) {
            String input = userInput.toLowerCase();
            int wordCount = userInput.split("\\s+").length;
            
            // Fast: short quick messages
            if (wordCount < 5 && (input.contains("!") || input.contains("??") || 
                input.contains("haha") || input.contains("lol"))) {
                return PacingStyle.FAST;
            }
            
            // Thoughtful: long, detailed questions
            if (wordCount > 20 && (input.contains("how") || input.contains("why") || 
                input.contains("explain") || input.contains("understand"))) {
                return PacingStyle.THOUGHTFUL;
            }
            
            // Deliberate: seeking advice or serious topics
            if (containsAny(input, "should i", "what do you think", "advice", "recommend")) {
                return PacingStyle.DELIBERATE;
            }
            
            return PacingStyle.NORMAL;
        }
        
        public void adaptPacing(String userInput) {
            PacingStyle detected = detectPacingFromInput(userInput);
            setPacing(detected);
        }
        
        public int getExchangeCount() { return exchangeCount; }
        
        private boolean containsAny(String input, String... phrases) {
            for (String phrase : phrases) {
                if (input.contains(phrase)) return true;
            }
            return false;
        }
    }
    
    /**
     * Get pacing controller
     * @return Pacing controller
     */
    public PacingController getPacingController() {
        return pacingController;
    }
    
    /**
     * Get recommended delay before response
     * @return Delay in milliseconds
     */
    public long getResponseDelay() {
        return pacingController.getDelayBeforeResponse();
    }
    
    // ==================== ENERGY LEVEL MATCHING ====================
    
    /**
     * Energy level matcher
     */
    public static class EnergyLevelMatcher {
        private EnergyLevel currentEnergy;
        private EnergyLevel targetEnergy;
        private Map<EnergyLevel, Double> energyTransitionMap;
        
        public enum EnergyLevel {
            LOW,        // Calm, gentle, subdued
            MODERATE,   // Balanced, neutral
            HIGH,       // Energetic, enthusiastic
            VARIABLE    // Fluctuating energy
        }
        
        public EnergyLevelMatcher() {
            this.currentEnergy = EnergyLevel.MODERATE;
            this.targetEnergy = EnergyLevel.MODERATE;
            this.energyTransitionMap = new HashMap<>();
            initializeTransitionMap();
        }
        
        private void initializeTransitionMap() {
            // Smoothness of transitioning between energy levels
            energyTransitionMap.put(EnergyLevel.LOW, 0.9);
            energyTransitionMap.put(EnergyLevel.MODERATE, 1.0);
            energyTransitionMap.put(EnergyLevel.HIGH, 0.85);
            energyTransitionMap.put(EnergyLevel.VARIABLE, 0.7);
        }
        
        public EnergyLevel getCurrentEnergy() { return currentEnergy; }
        public EnergyLevel getTargetEnergy() { return targetEnergy; }
        
        public void setEnergyLevel(EnergyLevel energy) {
            this.currentEnergy = energy;
            this.targetEnergy = energy;
        }
        
        public EnergyLevel detectUserEnergy(String userInput) {
            String input = userInput.toLowerCase();
            int wordCount = userInput.split("\\s+").length;
            
            // High energy indicators
            if (containsAny(input, "awesome", "amazing", "excited", "love it", "can't wait", 
                "haha", "lol", "lmao", "!!!") || userInput.contains("!")) {
                return EnergyLevel.HIGH;
            }
            
            // Low energy indicators
            if (containsAny(input, "tired", "exhausted", "drained", "sad", "depressed", 
                "lonely", "alone", "sigh", ":(", "meh") || wordCount < 3) {
                return EnergyLevel.LOW;
            }
            
            return EnergyLevel.MODERATE;
        }
        
        public void matchUserEnergy(String userInput) {
            EnergyLevel detected = detectUserEnergy(userInput);
            
            // Smooth transition to match user
            double transitionSmoothness = energyTransitionMap.getOrDefault(detected, 0.8);
            
            if (transitionSmoothness > 0.8) {
                // Direct match
                this.currentEnergy = detected;
            } else {
                // Gradual transition
                this.currentEnergy = blendEnergy(currentEnergy, detected);
            }
            
            this.targetEnergy = detected;
        }
        
        private EnergyLevel blendEnergy(EnergyLevel from, EnergyLevel to) {
            // Return intermediate based on context
            if (from == EnergyLevel.LOW && to == EnergyLevel.HIGH) {
                return EnergyLevel.MODERATE;
            }
            if (from == EnergyLevel.HIGH && to == EnergyLevel.LOW) {
                return EnergyLevel.MODERATE;
            }
            return to;
        }
        
        public double getEnergyMultiplier() {
            switch (currentEnergy) {
                case LOW: return 0.7;
                case MODERATE: return 1.0;
                case HIGH: return 1.3;
                case VARIABLE: return 1.0;
                default: return 1.0;
            }
        }
        
        public String getToneModifier() {
            switch (currentEnergy) {
                case LOW: return "gentle_calm";
                case MODERATE: return "balanced";
                case HIGH: return "enthusiastic";
                case VARIABLE: return "adaptive";
                default: return "balanced";
            }
        }
        
        private boolean containsAny(String input, String... phrases) {
            for (String phrase : phrases) {
                if (input.contains(phrase)) return true;
            }
            return false;
        }
    }
    
    /**
     * Get energy level matcher
     * @return Energy matcher
     */
    public EnergyLevelMatcher getEnergyMatcher() {
        return energyMatcher;
    }
    
    /**
     * Match energy level to user input
     * @param userInput User input
     */
    public void matchEnergyLevel(String userInput) {
        energyMatcher.matchUserEnergy(userInput);
    }
    
    /**
     * Get energy multiplier for response generation
     * @return Energy multiplier
     */
    public double getEnergyMultiplier() {
        return energyMatcher.getEnergyMultiplier();
    }
    
    /**
     * Get tone modifier based on energy
     * @return Tone modifier string
     */
    public String getToneModifier() {
        return energyMatcher.getToneModifier();
    }
    
    // ==================== INTEGRATED FLOW MANAGEMENT ====================
    
    /**
     * Process user input and update all flow components
     * @param userInput User input
     * @param currentTopic Current topic
     * @return Flow update result with all recommendations
     */
    public FlowUpdateResult processInput(String userInput, String currentTopic) {
        // Update all components
        pacingController.adaptPacing(userInput);
        energyMatcher.matchUserEnergy(userInput);
        
        ResponseLengthConfig.ResponseLength recommendedLength = 
            determineResponseLength(userInput, currentTopic);
        
        // Build result
        FlowUpdateResult result = new FlowUpdateResult();
        result.recommendedLength = recommendedLength;
        result.pacingStyle = pacingController.getCurrentPacing();
        result.energyLevel = energyMatcher.getCurrentEnergy();
        result.responseDelay = pacingController.getDelayBeforeResponse();
        result.energyMultiplier = energyMatcher.getEnergyMultiplier();
        result.toneModifier = energyMatcher.getToneModifier();
        
        return result;
    }
    
    /**
     * Flow update result
     */
    public static class FlowUpdateResult {
        public ResponseLengthConfig.ResponseLength recommendedLength;
        public PacingController.PacingStyle pacingStyle;
        public EnergyLevelMatcher.EnergyLevel energyLevel;
        public long responseDelay;
        public double energyMultiplier;
        public String toneModifier;
    }
    
    // ==================== SMOOTH TRANSITIONS ====================
    
    /**
     * Smooth transition manager for seamless topic changes
     */
    public static class SmoothTransitionManager {
        private TransitionStyle currentStyle;
        private Map<String, List<String>> transitionPhrases;
        private Map<String, String> topicBridges;
        private double transitionSmoothness;
        
        public enum TransitionStyle {
            ABRUPT,      // Direct topic change
            GRADUAL,     // Slow transition with overlap
            SEAMLESS,    // Smooth natural flow
            CREATIVE     // Creative bridge phrases
        }
        
        public SmoothTransitionManager() {
            this.currentStyle = TransitionStyle.SEAMLESS;
            this.transitionSmoothness = 0.8;
            this.transitionPhrases = new HashMap<>();
            this.topicBridges = new HashMap<>();
            initializeTransitionPhrases();
            initializeTopicBridges();
        }
        
        private void initializeTransitionPhrases() {
            // Transition phrases for different scenarios
            transitionPhrases.put("emotion_to_activity", Arrays.asList(
                "Speaking of feelings, have you tried",
                "That reminds me - on a lighter note",
                "I understand. Sometimes it helps to",
                "That's completely valid. Now, shifting gears"
            ));
            
            transitionPhrases.put("activity_to_emotion", Arrays.asList(
                "That's interesting! How does that make you feel?",
                "Fun! Does that help with your mood?",
                "Nice! Do you find it relaxing?",
                "Sounds engaging! Does it lift your spirits?"
            ));
            
            transitionPhrases.put("serious_to_light", Arrays.asList(
                "On a brighter note",
                "But enough heavy stuff -",
                "Let's take a breath -",
                "Speaking of which"
            ));
            
            transitionPhrases.put("light_to_serious", Arrays.asList(
                "That said,",
                "Which makes me wonder -",
                "That's fair. But regarding what you mentioned earlier",
                "Interesting perspective. So"
            ));
            
            transitionPhrases.put("topic_to_related", Arrays.asList(
                "That connects to",
                "Related to that",
                "Speaking of which",
                "That reminds me of"
            ));
            
            transitionPhrases.put("general_transition", Arrays.asList(
                "Let's talk about",
                "Moving on to",
                "I'd like to explore",
                "What about"
            ));
        }
        
        private void initializeTopicBridges() {
            // Pre-defined bridges between related topics
            topicBridges.put("stress_hobbies", "Dealing with stress - have you found any hobbies that help you unwind?");
            topicBridges.put("stress_achievements", "Everyone has accomplishments, even small ones. What recent achievement are you proud of?");
            topicBridges.put("sadness_hobbies", "When feeling down, sometimes engaging in activities can help. Do you have any hobbies?");
            topicBridges.put("sadness_relationships", "Support from others can help. How are your close relationships?");
            topicBridges.put("anxiety_breathing", "For anxiety, breathing exercises can help. Want to try one?");
            topicBridges.put("loneliness_activities", "Meeting new people can help loneliness. Any groups or activities interest you?");
            topicBridges.put("work_stress_goals", "Work can be overwhelming. What are your career goals?");
            topicBridges.put("school_stress_study_tips", "School stress is common. Want some study tips?");
            topicBridges.put("happiness_goals", "Great! What goals do you have for yourself?");
            topicBridges.put("relationships_friends", "Relationships are complex. How are your friendships?");
        }
        
        public TransitionStyle getCurrentStyle() { return currentStyle; }
        
        public void setTransitionStyle(TransitionStyle style) {
            this.currentStyle = style;
        }
        
        public double getTransitionSmoothness() { return transitionSmoothness; }
        
        public void setTransitionSmoothness(double smoothness) {
            this.transitionSmoothness = Math.max(0, Math.min(1, smoothness));
        }
        
        /**
         * Generate a smooth transition phrase between topics
         * @param fromTopic Source topic
         * @param toTopic Target topic
         * @return Transition phrase
         */
        public String generateTransitionPhrase(String fromTopic, String toTopic) {
            if (currentStyle == TransitionStyle.ABRUPT) {
                return "";
            }
            
            // Check for pre-defined bridge
            String bridgeKey = fromTopic + "_" + toTopic;
            if (topicBridges.containsKey(bridgeKey)) {
                return topicBridges.get(bridgeKey);
            }
            
            // Determine transition type
            String transitionType = determineTransitionType(fromTopic, toTopic);
            List<String> phrases = transitionPhrases.get(transitionType);
            
            if (phrases == null || phrases.isEmpty()) {
                phrases = transitionPhrases.get("general_transition");
            }
            
            // Select phrase based on style
            return selectPhrase(phrases, toTopic);
        }
        
        private String determineTransitionType(String from, String to) {
            from = from.toLowerCase();
            to = to.toLowerCase();
            
            boolean fromSerious = containsAny(from, "stress", "sad", "anxious", "worried", "depressed", "lonely");
            boolean toSerious = containsAny(to, "stress", "sad", "anxious", "worried", "depressed", "lonely");
            boolean fromLight = containsAny(from, "hobby", "fun", "game", "happy", "achievement");
            boolean toLight = containsAny(to, "hobby", "fun", "game", "happy", "achievement");
            boolean fromEmotional = containsAny(from, "feeling", "emotion", "sad", "happy", "anxious");
            boolean toEmotional = containsAny(to, "feeling", "emotion", "sad", "happy", "anxious");
            boolean related = isRelatedTopic(from, to);
            
            if (fromEmotional && toLight) return "emotion_to_activity";
            if (fromLight && toEmotional) return "activity_to_emotion";
            if (fromSerious && toLight) return "serious_to_light";
            if (fromLight && toSerious) return "light_to_serious";
            if (related) return "topic_to_related";
            
            return "general_transition";
        }
        
        private boolean isRelatedTopic(String topic1, String topic2) {
            // Define topic relationships
            Map<String, List<String>> topicRelations = new HashMap<>();
            topicRelations.put("stress", Arrays.asList("work", "school", "anxiety", "overwhelmed"));
            topicRelations.put("sad", Arrays.asList("depressed", "lonely", "relationships", "feeling"));
            topicRelations.put("happiness", Arrays.asList("achievement", "goals", "positive", "excited"));
            topicRelations.put("relationships", Arrays.asList("friends", "family", "social", "lonely"));
            
            for (Map.Entry<String, List<String>> entry : topicRelations.entrySet()) {
                if ((topic1.contains(entry.getKey()) && entry.getValue().stream().anyMatch(topic2::contains)) ||
                    (topic2.contains(entry.getKey()) && entry.getValue().stream().anyMatch(topic1::contains))) {
                    return true;
                }
            }
            return false;
        }
        
        private String selectPhrase(List<String> phrases, String topic) {
            if (phrases == null || phrases.isEmpty()) {
                return "Let's discuss " + topic;
            }
            
            Random rand = new Random();
            String phrase = phrases.get(rand.nextInt(phrases.size()));
            
            // Add topic reference if needed
            if (phrase.contains("{topic}")) {
                phrase = phrase.replace("{topic}", topic);
            }
            
            return phrase;
        }
        
        /**
         * Calculate transition smoothness score
         * @param fromTopic Source topic
         * @param toTopic Target topic
         * @return Smoothness score (0-1)
         */
        public double calculateSmoothness(String fromTopic, String toTopic) {
            double baseSmoothness = transitionSmoothness;
            
            // Related topics are smoother
            if (isRelatedTopic(fromTopic, toTopic)) {
                baseSmoothness += 0.2;
            }
            
            // Same category topics are smoother
            if (getTopicCategory(fromTopic).equals(getTopicCategory(toTopic))) {
                baseSmoothness += 0.1;
            }
            
            // Abrupt style reduces smoothness
            if (currentStyle == TransitionStyle.ABRUPT) {
                baseSmoothness -= 0.3;
            }
            
            // Creative style increases perceived smoothness
            if (currentStyle == TransitionStyle.CREATIVE) {
                baseSmoothness += 0.1;
            }
            
            return Math.max(0, Math.min(1, baseSmoothness));
        }
        
        private String getTopicCategory(String topic) {
            topic = topic.toLowerCase();
            
            if (containsAny(topic, "emotion", "feeling", "sad", "happy", "anxious", "stress")) {
                return "emotional";
            }
            if (containsAny(topic, "hobby", "game", "activity", "fun", "sport")) {
                return "activity";
            }
            if (containsAny(topic, "work", "school", "study", "career")) {
                return "academic";
            }
            if (containsAny(topic, "friend", "family", "relationship", "social")) {
                return "social";
            }
            
            return "general";
        }
        
        /**
         * Generate a gradual transition with overlap
         * @param currentTopic Current topic
         * @param newTopic Target topic
         * @return Transition text with bridge
         */
        public String generateGradualTransition(String currentTopic, String newTopic) {
            String bridge = generateTransitionPhrase(currentTopic, newTopic);
            double smoothness = calculateSmoothness(currentTopic, newTopic);
            
            if (smoothness > 0.7) {
                return bridge + " " + newTopic + "?";
            } else if (smoothness > 0.4) {
                return bridge + " I'm also curious about " + newTopic + ".";
            } else {
                return "Actually, let's talk about " + newTopic + " for a moment.";
            }
        }
        
        private boolean containsAny(String input, String... words) {
            for (String word : words) {
                if (input.contains(word)) return true;
            }
            return false;
        }
    }
    
    /**
     * Get smooth transition manager
     * @return Smooth transition manager
     */
    public SmoothTransitionManager getSmoothTransitionManager() {
        return smoothTransitionManager;
    }

    /**
     * Generate smooth transition between topics
     * @param fromTopic Source topic
     * @param toTopic Target topic
     * @return Transition phrase
     */
    public String generateSmoothTransition(String fromTopic, String toTopic) {
        if (smoothTransitionManager == null) {
            smoothTransitionManager = new SmoothTransitionManager();
        }
        return smoothTransitionManager.generateGradualTransition(fromTopic, toTopic);
    }
    
    /**
     * Calculate transition smoothness
     * @param fromTopic Source topic
     * @param toTopic Target topic
     * @return Smoothness score
     */
    public double calculateTransitionSmoothness(String fromTopic, String toTopic) {
        if (smoothTransitionManager == null) {
            smoothTransitionManager = new SmoothTransitionManager();
        }
        return smoothTransitionManager.calculateSmoothness(fromTopic, toTopic);
    }
    
    // ==================== THREAD FOLLOWING ====================
    
    /**
     * Thread follower for maintaining conversation context
     */
    public static class ThreadFollower {
        private List<ThreadElement> currentThread;
        private String mainTopic;
        private Map<String, Object> threadContext;
        private int maxThreadSize;
        private double threadCoherence;
        
        public ThreadFollower() {
            this.currentThread = new ArrayList<>();
            this.mainTopic = null;
            this.threadContext = new HashMap<>();
            this.maxThreadSize = 10;
            this.threadCoherence = 1.0;
        }
        
        /**
         * Thread element representing a conversation turn
         */
        public static class ThreadElement {
            String content;
            String topic;
            long timestamp;
            boolean isFromUser;
            
            public ThreadElement(String content, String topic, boolean isFromUser) {
                this.content = content;
                this.topic = topic;
                this.timestamp = System.currentTimeMillis();
                this.isFromUser = isFromUser;
            }
            
            public String getContent() { return content; }
            public String getTopic() { return topic; }
            public long getTimestamp() { return timestamp; }
            public boolean isFromUser() { return isFromUser; }
        }
        
        public void addElement(String content, String topic, boolean isFromUser) {
            ThreadElement element = new ThreadElement(content, topic, isFromUser);
            currentThread.add(element);
            
            // Update main topic if needed
            if (mainTopic == null) {
                mainTopic = topic;
            }
            
            // Maintain thread size
            if (currentThread.size() > maxThreadSize) {
                currentThread.remove(0);
            }
            
            // Recalculate coherence
            recalculateCoherence();
        }
        
        private void recalculateCoherence() {
            if (currentThread.size() < 2) {
                threadCoherence = 1.0;
                return;
            }
            
            // Check topic consistency
            String firstTopic = currentThread.get(0).topic;
            int consistentCount = 1;
            
            for (int i = 1; i < currentThread.size(); i++) {
                if (currentThread.get(i).topic.equals(firstTopic)) {
                    consistentCount++;
                }
            }
            
            threadCoherence = (double) consistentCount / currentThread.size();
        }
        
        public String getMainTopic() { return mainTopic; }
        public double getThreadCoherence() { return threadCoherence; }
        
        public void setContext(String key, Object value) {
            threadContext.put(key, value);
        }
        
        public Object getContext(String key) {
            return threadContext.get(key);
        }
        
        public Map<String, Object> getAllContext() {
            return new HashMap<>(threadContext);
        }
        
        public List<ThreadElement> getThreadElements() {
            return new ArrayList<>(currentThread);
        }
        
        public String getLastUserMessage() {
            for (int i = currentThread.size() - 1; i >= 0; i--) {
                if (currentThread.get(i).isFromUser()) {
                    return currentThread.get(i).content;
                }
            }
            return null;
        }
        
        public String getLastAssistantMessage() {
            for (int i = currentThread.size() - 1; i >= 0; i--) {
                if (!currentThread.get(i).isFromUser()) {
                    return currentThread.get(i).content;
                }
            }
            return null;
        }
        
        public boolean isOnSameTopic(String topic) {
            return mainTopic != null && mainTopic.equals(topic);
        }
        
        public List<String> getRelatedTopics() {
            Set<String> topics = new HashSet<>();
            for (ThreadElement el : currentThread) {
                if (el.topic != null) {
                    topics.add(el.topic);
                }
            }
            return new ArrayList<>(topics);
        }
        
        public void clearThread() {
            currentThread.clear();
            mainTopic = null;
            threadContext.clear();
            threadCoherence = 1.0;
        }
        
        public int getThreadLength() { return currentThread.size(); }
    }
    
    // Thread follower instance
    private ThreadFollower threadFollower;
    
    /**
     * Get thread follower
     * @return Thread follower
     */
    public ThreadFollower getThreadFollower() {
        return threadFollower;
    }
    
    /**
     * Initialize thread follower
     */
    private void initThreadFollower() {
        this.threadFollower = new ThreadFollower();
    }
    
    /**
     * Add user message to thread
     * @param message User message
     * @param topic Detected topic
     */
    public void addUserMessage(String message, String topic) {
        if (threadFollower == null) {
            threadFollower = new ThreadFollower();
        }
        threadFollower.addElement(message, topic, true);
    }
    
    /**
     * Add assistant message to thread
     * @param message Assistant message
     * @param topic Topic of response
     */
    public void addAssistantMessage(String message, String topic) {
        if (threadFollower == null) {
            threadFollower = new ThreadFollower();
        }
        threadFollower.addElement(message, topic, false);
    }
    
    /**
     * Check if conversation is on the same topic
     * @param topic Topic to check
     * @return true if on same topic
     */
    public boolean isOnSameTopic(String topic) {
        if (threadFollower == null) return false;
        return threadFollower.isOnSameTopic(topic);
    }
    
    /**
     * Get thread coherence score
     * @return Coherence (0-1)
     */
    public double getThreadCoherence() {
        if (threadFollower == null) return 1.0;
        return threadFollower.getThreadCoherence();
    }
    
/**
     * Get related topics from thread
     * @return List of related topics
     */
    public List<String> getRelatedTopics() {
        if (threadFollower == null) return new ArrayList<>();
        return threadFollower.getRelatedTopics();
    }
    
    // ==================== QUESTION FREQUENCY CALIBRATION ====================
    
    // TODO: Open vs closed questions - Implement explicit handling of open vs closed questions
    // - Add methods to classify questions as open-ended or closed
    // - Balance ratio of open vs closed questions based on user engagement
    // - Track user response quality to each type and adapt accordingly
    
    // TODO: Challenge level adjustment - Implement adaptive challenge levels in conversation
    // - Assess user capability and engagement
    // - Adjust question depth and complexity accordingly
    // - Balance between easy casual conversation and thought-provoking topics
    
    /**
     * Question frequency calibrator - tracks and calibrates question asking behavior
     * to maintain optimal engagement without overwhelming the user
     */
    public static class QuestionFrequencyCalibrator {
        private CalibrationMode currentMode;
        private CalibrationMode targetMode;
        private List<QuestionRecord> questionHistory;
        private List<UserResponseRecord> responseHistory;
        private int questionsInCurrentWindow;
        private long windowStartTime;
        private double engagementScore;
        private double optimalFrequency;
        
        public enum CalibrationMode {
            HIGH,       // Many questions (curious, engaging)
            MODERATE,   // Balanced question frequency
            LOW,        // Few questions (more statements)
            ADAPTIVE    // Automatically adjusts based on user responses
        }
        
        public enum QuestionType {
            OPEN_ENDED,     // "How do you feel about...?"
            YES_NO,         // "Do you think...?"
            RHETORICAL,     // "Isn't that interesting?"
            PROMPTING,      // "Want to tell me more?"
            FOLLOW_UP,      // "What happened then?"
            PROBING         // "Can you explain that?"
        }
        
        /**
         * Record of a question asked
         */
        private static class QuestionRecord {
            QuestionType type;
            long timestamp;
            String topic;
            boolean wasEngaging;
            
            QuestionRecord(QuestionType type, String topic) {
                this.type = type;
                this.topic = topic;
                this.timestamp = System.currentTimeMillis();
                this.wasEngaging = true;
            }
        }
        
        /**
         * Record of user's response to a question
         */
        private static class UserResponseRecord {
            long timestamp;
            int responseLength;
            double engagementLevel;
            boolean isSubstantive;
            
            UserResponseRecord(int responseLength, boolean isSubstantive) {
                this.timestamp = System.currentTimeMillis();
                this.responseLength = responseLength;
                this.isSubstantive = isSubstantive;
                this.engagementLevel = calculateEngagement(responseLength, isSubstantive);
            }
            
            private double calculateEngagement(int length, boolean substantive) {
                double base = substantive ? 0.7 : 0.3;
                if (length > 50) return Math.min(1.0, base + 0.3);
                if (length > 20) return Math.min(1.0, base + 0.15);
                return base;
            }
        }
        
        public QuestionFrequencyCalibrator() {
            this.currentMode = CalibrationMode.MODERATE;
            this.targetMode = CalibrationMode.MODERATE;
            this.questionHistory = new ArrayList<>();
            this.responseHistory = new ArrayList<>();
            this.questionsInCurrentWindow = 0;
            this.windowStartTime = System.currentTimeMillis();
            this.engagementScore = 0.5;
            this.optimalFrequency = 0.3; // 30% of responses should be questions
            initializeModeParameters();
        }
        
        private void initializeModeParameters() {
            // Mode-specific parameters will be applied based on current mode
        }
        
        public CalibrationMode getCurrentMode() { return currentMode; }
        
        public void setMode(CalibrationMode mode) {
            this.currentMode = mode;
            this.targetMode = mode;
        }
        
        /**
         * Determine if a question should be asked based on current context
         * @param userInput User's last message
         * @param topic Current conversation topic
         * @return true if a question should be asked
         */
        public boolean shouldAskQuestion(String userInput, String topic) {
            // Check if we're in a responsive state
            if (!isResponsiveState()) {
                return false;
            }
            
            // Check question frequency in current window
            if (questionsInCurrentWindow >= getMaxQuestionsPerWindow()) {
                return false;
            }
            
            // Check user engagement level
            if (engagementScore < 0.2) {
                // Low engagement - ask more engaging question or none
                return shouldAskEngagingQuestion(userInput);
            }
            
            // Analyze user input type
            if (isUserInitiatingTopic(userInput)) {
                // User is sharing - ask follow-up
                return Math.random() < 0.7;
            }
            
            if (isUserAskingQuestion(userInput)) {
                // User asked a question - we can answer but also reciprocate
                return Math.random() < getReciprocityChance();
            }
            
            if (isUserMakingStatement(userInput)) {
                // User made a statement - probe deeper
                return Math.random() < getProbingChance();
            }
            
            // Default: use mode-based probability
            return Math.random() < getModeProbability();
        }
        
        private boolean isResponsiveState() {
            // Check if enough time has passed since last question
            long timeSinceWindowStart = System.currentTimeMillis() - windowStartTime;
            if (timeSinceWindowStart > 60000) { // Reset window after 1 minute
                resetWindow();
            }
            return true;
        }
        
        private void resetWindow() {
            questionsInCurrentWindow = 0;
            windowStartTime = System.currentTimeMillis();
        }
        
        private int getMaxQuestionsPerWindow() {
            switch (currentMode) {
                case HIGH: return 5;
                case MODERATE: return 3;
                case LOW: return 1;
                case ADAPTIVE: return (int)(getAdaptiveMax() + 0.5);
                default: return 3;
            }
        }
        
        private double getAdaptiveMax() {
            // Adapt based on engagement score
            if (engagementScore > 0.7) return 4;
            if (engagementScore > 0.4) return 3;
            if (engagementScore > 0.2) return 2;
            return 1;
        }
        
        private boolean shouldAskEngagingQuestion(String userInput) {
            // In low engagement, only ask open-ended engaging questions
            return userInput.length() > 10 && Math.random() < 0.3;
        }
        
        private boolean isUserInitiatingTopic(String input) {
            String lower = input.toLowerCase();
            return lower.startsWith("i ") || lower.startsWith("my ") ||
                   lower.startsWith("i'm ") || lower.startsWith("i've ") ||
                   lower.startsWith("i'm feeling") || lower.contains("happened");
        }
        
        private boolean isUserAskingQuestion(String input) {
            return input.contains("?") || input.toLowerCase().matches(".*\\b(what|why|how|when|where|who|can you|do you|would you)\\b.*\\?.*");
        }
        
        private boolean isUserMakingStatement(String input) {
            return !isUserAskingQuestion(input) && input.length() > 15;
        }
        
        private double getReciprocityChance() {
            // When user asks, how often do we reciprocate with a question?
            switch (currentMode) {
                case HIGH: return 0.6;
                case MODERATE: return 0.4;
                case LOW: return 0.2;
                case ADAPTIVE: return 0.3 + (engagementScore * 0.3);
                default: return 0.4;
            }
        }
        
        private double getProbingChance() {
            // When user makes statement, how often do we probe?
            switch (currentMode) {
                case HIGH: return 0.8;
                case MODERATE: return 0.5;
                case LOW: return 0.25;
                case ADAPTIVE: return 0.3 + (engagementScore * 0.4);
                default: return 0.5;
            }
        }
        
        private double getModeProbability() {
            switch (currentMode) {
                case HIGH: return 0.7;
                case MODERATE: return 0.4;
                case LOW: return 0.15;
                case ADAPTIVE: return 0.25 + (engagementScore * 0.35);
                default: return 0.4;
            }
        }
        
        /**
         * Select the best type of question to ask
         * @param context Current conversation context
         * @return Selected question type
         */
        public QuestionType selectQuestionType(String context) {
            // Determine based on conversation flow and engagement
            if (engagementScore > 0.7) {
                // High engagement - ask open-ended questions
                return QuestionType.OPEN_ENDED;
            }
            
            if (engagementScore < 0.3) {
                // Low engagement - use prompting questions
                return QuestionType.PROMPTING;
            }
            
            // Mix of question types for moderate engagement
            double roll = Math.random();
            if (roll < 0.4) return QuestionType.OPEN_ENDED;
            if (roll < 0.6) return QuestionType.FOLLOW_UP;
            if (roll < 0.8) return QuestionType.PROBING;
            if (roll < 0.9) return QuestionType.YES_NO;
            return QuestionType.PROMPTING;
        }
        
        /**
         * Record that a question was asked
         * @param type Type of question asked
         * @param topic Topic of the question
         */
        public void recordQuestionAsked(QuestionType type, String topic) {
            QuestionRecord record = new QuestionRecord(type, topic);
            questionHistory.add(record);
            questionsInCurrentWindow++;
            
            // Maintain history size
            if (questionHistory.size() > 20) {
                questionHistory.remove(0);
            }
        }
        
        /**
         * Record user's response to gauge engagement
         * @param response User's response text
         */
        public void recordUserResponse(String response) {
            int length = response.split("\\s+").length;
            boolean substantive = isSubstantiveResponse(response);
            
            UserResponseRecord record = new UserResponseRecord(length, substantive);
            responseHistory.add(record);
            
            // Update engagement score
            updateEngagementScore(record);
            
            // Maintain history size
            if (responseHistory.size() > 20) {
                responseHistory.remove(0);
            }
            
            // Adapt mode if in adaptive mode
            if (currentMode == CalibrationMode.ADAPTIVE) {
                adaptMode();
            }
        }
        
        private boolean isSubstantiveResponse(String response) {
            // A substantive response has meaningful content
            if (response.length() < 10) return false;
            
            String lower = response.toLowerCase();
            // Check for meaningful content indicators
            String[] substantiveIndicators = {"because", "since", "actually", "really", 
                "think", "feel", "believe", "know", "want", "need", "like", "don't like",
                "sometimes", "often", "usually", "always", "never"};
            
            for (String indicator : substantiveIndicators) {
                if (lower.contains(indicator)) return true;
            }
            
            // Length-based fallback
            return response.split("\\s+").length > 15;
        }
        
        private void updateEngagementScore(UserResponseRecord record) {
            // Calculate weighted average of recent engagement
            double weight = 0.2; // New record weight
            engagementScore = (engagementScore * (1 - weight)) + (record.engagementLevel * weight);
            
            // Also update based on question response patterns
            if (questionHistory.size() > 0 && responseHistory.size() > 0) {
                double qResponseRate = calculateQuestionResponseRate();
                engagementScore = (engagementScore + qResponseRate) / 2;
            }
        }
        
        private double calculateQuestionResponseRate() {
            if (questionHistory.isEmpty() || responseHistory.isEmpty()) {
                return 0.5;
            }
            
            // Check how many recent questions got responses
            int questionsWithResponses = 0;
            int recentQuestions = Math.min(5, questionHistory.size());
            
            for (int i = 0; i < recentQuestions; i++) {
                long qTime = questionHistory.get(questionHistory.size() - 1 - i).timestamp;
                for (UserResponseRecord r : responseHistory) {
                    if (r.timestamp > qTime && r.isSubstantive) {
                        questionsWithResponses++;
                        break;
                    }
                }
            }
            
            return (double) questionsWithResponses / recentQuestions;
        }
        
        private void adaptMode() {
            // Adjust mode based on engagement patterns
            if (engagementScore > 0.6 && currentMode == CalibrationMode.LOW) {
                targetMode = CalibrationMode.MODERATE;
            } else if (engagementScore < 0.3 && currentMode == CalibrationMode.HIGH) {
                targetMode = CalibrationMode.MODERATE;
            }
            
            // Gradually transition
            if (targetMode != currentMode) {
                currentMode = targetMode;
            }
        }
        
        /**
         * Get recommended question frequency
         * @return Recommended frequency (0-1)
         */
        public double getRecommendedFrequency() {
            if (currentMode == CalibrationMode.ADAPTIVE) {
                return Math.min(0.8, Math.max(0.1, engagementScore * 0.8));
            }
            
            switch (currentMode) {
                case HIGH: return 0.6;
                case MODERATE: return 0.35;
                case LOW: return 0.15;
                default: return 0.3;
            }
        }
        
        /**
         * Get current engagement score
         * @return Engagement score (0-1)
         */
        public double getEngagementScore() {
            return engagementScore;
        }
        
        /**
         * Check if we should use a question to engage user
         * @param userInput Current user input
         * @return true if question recommended
         */
        public boolean recommendQuestion(String userInput) {
            return shouldAskQuestion(userInput, "general");
        }
        
        /**
         * Get statistics about question asking patterns
         * @return Map of statistics
         */
        public Map<String, Object> getQuestionStatistics() {
            Map<String, Object> stats = new HashMap<>();
            
            stats.put("currentMode", currentMode.toString());
            stats.put("engagementScore", engagementScore);
            stats.put("questionsInWindow", questionsInCurrentWindow);
            stats.put("totalQuestions", questionHistory.size());
            stats.put("totalResponses", responseHistory.size());
            stats.put("recommendedFrequency", getRecommendedFrequency());
            
            // Question type distribution
            Map<String, Integer> typeDistribution = new HashMap<>();
            for (QuestionRecord q : questionHistory) {
                String typeName = q.type.toString();
                typeDistribution.put(typeName, typeDistribution.getOrDefault(typeName, 0) + 1);
            }
            stats.put("questionTypeDistribution", typeDistribution);
            
            return stats;
        }
        
        /**
         * Reset calibration to default state
         */
        public void reset() {
            this.currentMode = CalibrationMode.MODERATE;
            this.targetMode = CalibrationMode.MODERATE;
            this.questionHistory.clear();
            this.responseHistory.clear();
            this.questionsInCurrentWindow = 0;
            this.windowStartTime = System.currentTimeMillis();
            this.engagementScore = 0.5;
        }
    }
    
    /**
     * Get question frequency calibrator
     * @return Question calibrator
     */
    public QuestionFrequencyCalibrator getQuestionCalibrator() {
        return questionCalibrator;
    }
    
    /**
     * Determine if a question should be asked
     * @param userInput User input
     * @param topic Current topic
     * @return true if question should be asked
     */
    public boolean shouldAskQuestion(String userInput, String topic) {
        return questionCalibrator.shouldAskQuestion(userInput, topic);
    }
    
    /**
     * Record a question was asked
     * @param type Type of question
     * @param topic Topic of question
     */
    public void recordQuestionAsked(QuestionFrequencyCalibrator.QuestionType type, String topic) {
        questionCalibrator.recordQuestionAsked(type, topic);
    }
    
    /**
     * Record user response for engagement tracking
     * @param response User response
     */
    public void recordUserResponse(String response) {
        questionCalibrator.recordUserResponse(response);
    }
    
    /**
     * Get recommended question frequency
     * @return Recommended frequency
     */
    public double getRecommendedQuestionFrequency() {
        return questionCalibrator.getRecommendedFrequency();
    }
    
    /**
     * Get engagement score from question calibration
     * @return Engagement score
     */
    public double getQuestionEngagementScore() {
        return questionCalibrator.getEngagementScore();
    }
    
    /**
     * Set question calibration mode
     * @param mode Calibration mode
     */
    public void setQuestionCalibrationMode(QuestionFrequencyCalibrator.CalibrationMode mode) {
        questionCalibrator.setMode(mode);
    }
    
    /**
     * Get question calibration statistics
     * @return Statistics map
     */
    public Map<String, Object> getQuestionStatistics() {
        return questionCalibrator.getQuestionStatistics();
    }
}

