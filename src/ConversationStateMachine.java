import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * State Machine for Conversation Flow in VirtualXander
 * Manages conversation states and transitions
 */
public class ConversationStateMachine {
    
    private Map<String, ConversationState> states;
    private ConversationState currentState;
    private Map<StateTransition, ConversationState> transitions;
    private Map<String, Object> stateData;
    private List<StateChangeListener> listeners;
    private Random random;
    
    public ConversationStateMachine() {
        this.states = new HashMap<>();
        this.transitions = new HashMap<>();
        this.stateData = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.random = new Random();
        initializeStates();
        initializeTransitions();
    }
    
    /**
     * Conversation state definitions
     */
    public enum ConversationState {
        IDLE("idle", "Waiting for user input"),
        GREETING("greeting", "Processing greeting"),
        GENERAL_CHAT("general_chat", "Having general conversation"),
        HOMEWORK_HELP("homework_help", "Providing homework assistance"),
        MENTAL_HEALTH_SUPPORT("mental_health_support", "Offering mental health support"),
        ENTERTAINMENT("entertainment", "Discussing entertainment topics"),
        CREATIVE_WRITING("creative_writing", "Helping with creative writing"),
        GAMING("gaming", "Discussing gaming"),
        ADVICE("advice", "Providing advice"),
        CLOSING("closing", "Ending conversation"),
        UNKNOWN("unknown", "Processing unknown input");
        
        private final String name;
        private final String description;
        
        ConversationState(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() {
            return name;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * State transition definition
     */
    private static class StateTransition {
        ConversationState from;
        String intent;
        Predicate<ConversationStateMachine> condition;
        
        public StateTransition(ConversationState from, String intent) {
            this.from = from;
            this.intent = intent;
            this.condition = sm -> true;
        }
        
        public StateTransition(ConversationState from, String intent, Predicate<ConversationStateMachine> condition) {
            this.from = from;
            this.intent = intent;
            this.condition = condition;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StateTransition that = (StateTransition) o;
            return from == that.from && Objects.equals(intent, that.intent);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(from, intent);
        }
    }
    
    /**
     * State change listener interface
     */
    public interface StateChangeListener {
        void onStateChanged(ConversationState oldState, ConversationState newState, String triggerIntent);
    }
    
    private void initializeStates() {
        // The states are already defined as enum values
        // Just set initial state
        currentState = ConversationState.IDLE;
    }
    
    private void initializeTransitions() {
        // IDLE state transitions
        addTransition(ConversationState.IDLE, "greeting", ConversationState.GREETING);
        addTransition(ConversationState.IDLE, "wellbeing_how", ConversationState.GREETING);
        addTransition(ConversationState.IDLE, "wellbeing_positive", ConversationState.GREETING);
        addTransition(ConversationState.IDLE, "wellbeing_negative", ConversationState.GENERAL_CHAT);
        addTransition(ConversationState.IDLE, "activity", ConversationState.GENERAL_CHAT);
        addTransition(ConversationState.IDLE, "homework_help", ConversationState.HOMEWORK_HELP);
        addTransition(ConversationState.IDLE, "mental_health_support", ConversationState.MENTAL_HEALTH_SUPPORT);
        addTransition(ConversationState.IDLE, "mental_health_positive", ConversationState.GENERAL_CHAT);
        addTransition(ConversationState.IDLE, "gaming", ConversationState.GAMING);
        addTransition(ConversationState.IDLE, "creative_writing", ConversationState.CREATIVE_WRITING);
        addTransition(ConversationState.IDLE, "entertainment", ConversationState.ENTERTAINMENT);
        addTransition(ConversationState.IDLE, "advice", ConversationState.ADVICE);
        addTransition(ConversationState.IDLE, "help_request", ConversationState.GENERAL_CHAT);
        addTransition(ConversationState.IDLE, "farewell", ConversationState.CLOSING);
        addTransition(ConversationState.IDLE, "identity", ConversationState.GREETING);
        addTransition(ConversationState.IDLE, "philosophical", ConversationState.GENERAL_CHAT);
        addTransition(ConversationState.IDLE, "creative_project", ConversationState.GENERAL_CHAT);
        
        // GREETING state transitions
        addTransition(ConversationState.GREETING, "wellbeing_response", ConversationState.GENERAL_CHAT);
        addTransition(ConversationState.GREETING, "continue", ConversationState.GENERAL_CHAT);
        addTransition(ConversationState.GREETING, "homework_help", ConversationState.HOMEWORK_HELP);
        addTransition(ConversationState.GREETING, "farewell", ConversationState.CLOSING);
        
        // GENERAL_CHAT state transitions
        addTransition(ConversationState.GENERAL_CHAT, "homework_help", ConversationState.HOMEWORK_HELP);
        addTransition(ConversationState.GENERAL_CHAT, "mental_health_support", ConversationState.MENTAL_HEALTH_SUPPORT);
        addTransition(ConversationState.GENERAL_CHAT, "gaming", ConversationState.GAMING);
        addTransition(ConversationState.GENERAL_CHAT, "creative_writing", ConversationState.CREATIVE_WRITING);
        addTransition(ConversationState.GENERAL_CHAT, "entertainment", ConversationState.ENTERTAINMENT);
        addTransition(ConversationState.GENERAL_CHAT, "advice", ConversationState.ADVICE);
        addTransition(ConversationState.GENERAL_CHAT, "farewell", ConversationState.CLOSING);
        addTransition(ConversationState.GENERAL_CHAT, "continue", ConversationState.GENERAL_CHAT);
        
        // HOMEWORK_HELP state transitions
        addTransition(ConversationState.HOMEWORK_HELP, "homework_subject", ConversationState.HOMEWORK_HELP);
        addTransition(ConversationState.HOMEWORK_HELP, "continue", ConversationState.GENERAL_CHAT);
        addTransition(ConversationState.HOMEWORK_HELP, "farewell", ConversationState.CLOSING);
        
        // MENTAL_HEALTH_SUPPORT state transitions
        addTransition(ConversationState.MENTAL_HEALTH_SUPPORT, "mental_health_positive", ConversationState.GENERAL_CHAT);
        addTransition(ConversationState.MENTAL_HEALTH_SUPPORT, "continue", ConversationState.MENTAL_HEALTH_SUPPORT);
        addTransition(ConversationState.MENTAL_HEALTH_SUPPORT, "advice", ConversationState.ADVICE);
        addTransition(ConversationState.MENTAL_HEALTH_SUPPORT, "farewell", ConversationState.CLOSING);
        
        // GAMING state transitions
        addTransition(ConversationState.GAMING, "continue", ConversationState.GENERAL_CHAT);
        addTransition(ConversationState.GAMING, "gaming", ConversationState.GAMING);
        addTransition(ConversationState.GAMING, "farewell", ConversationState.CLOSING);
        
        // ENTERTAINMENT state transitions
        addTransition(ConversationState.ENTERTAINMENT, "continue", ConversationState.GENERAL_CHAT);
        addTransition(ConversationState.ENTERTAINMENT, "farewell", ConversationState.CLOSING);
        
        // CREATIVE_WRITING state transitions
        addTransition(ConversationState.CREATIVE_WRITING, "continue", ConversationState.GENERAL_CHAT);
        addTransition(ConversationState.CREATIVE_WRITING, "creative_writing", ConversationState.CREATIVE_WRITING);
        addTransition(ConversationState.CREATIVE_WRITING, "farewell", ConversationState.CLOSING);
        
        // ADVICE state transitions
        addTransition(ConversationState.ADVICE, "continue", ConversationState.GENERAL_CHAT);
        addTransition(ConversationState.ADVICE, "farewell", ConversationState.CLOSING);
        
        // CLOSING state transitions
        addTransition(ConversationState.CLOSING, "farewell", ConversationState.IDLE);
        addTransition(ConversationState.CLOSING, "greeting", ConversationState.GREETING);
        
        // Any state to closing on farewell
        for (ConversationState state : ConversationState.values()) {
            if (state != ConversationState.CLOSING && state != ConversationState.IDLE) {
                addTransition(state, "farewell", ConversationState.CLOSING);
            }
        }
    }
    
    private void addTransition(ConversationState from, String intent, ConversationState to) {
        transitions.put(new StateTransition(from, intent), to);
    }
    
    /**
     * Processes an intent and returns the new state
     */
    public ConversationState processIntent(String intent) {
        StateTransition transition = new StateTransition(currentState, intent);
        ConversationState newState = transitions.get(transition);
        
        if (newState == null) {
            // Try wildcard transitions (stay in current state or go to general chat)
            if (intent.equals("farewell")) {
                newState = ConversationState.CLOSING;
            } else if (intent.equals("unknown")) {
                newState = ConversationState.UNKNOWN;
            } else {
                // Stay in current state or transition to general chat
                newState = currentState;
            }
        }
        
        if (newState != currentState) {
            ConversationState oldState = currentState;
            currentState = newState;
            notifyStateChangeListeners(oldState, newState, intent);
        }
        
        return currentState;
    }
    
    /**
     * Gets the current state
     */
    public ConversationState getCurrentState() {
        return currentState;
    }
    
    /**
     * Gets the state name
     */
    public String getCurrentStateName() {
        return currentState.getName();
    }
    
    /**
     * Gets the state description
     */
    public String getCurrentStateDescription() {
        return currentState.getDescription();
    }
    
    /**
     * Checks if we should continue the current conversation flow
     */
    public boolean shouldContinueConversation() {
        return currentState != ConversationState.CLOSING && 
               currentState != ConversationState.IDLE;
    }
    
    /**
     * Checks if the conversation is in a supportive state
     */
    public boolean isInSupportiveState() {
        return currentState == ConversationState.MENTAL_HEALTH_SUPPORT ||
               currentState == ConversationState.HOMEWORK_HELP;
    }
    
    /**
     * Stores data for the current state
     */
    public void setStateData(String key, Object value) {
        stateData.put(key, value);
    }
    
    /**
     * Gets data for the current state
     */
    @SuppressWarnings("unchecked")
    public <T> T getStateData(String key) {
        return (T) stateData.get(key);
    }
    
    /**
     * Clears state-specific data
     */
    public void clearStateData() {
        stateData.clear();
    }
    
    /**
     * Adds a state change listener
     */
    public void addStateChangeListener(StateChangeListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Removes a state change listener
     */
    public void removeStateChangeListener(StateChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyStateChangeListeners(ConversationState oldState, ConversationState newState, String triggerIntent) {
        for (StateChangeListener listener : listeners) {
            listener.onStateChanged(oldState, newState, triggerIntent);
        }
    }
    
    /**
     * Resets the state machine to initial state
     */
    public void reset() {
        currentState = ConversationState.IDLE;
        stateData.clear();
        notifyStateChangeListeners(ConversationState.IDLE, ConversationState.IDLE, "reset");
    }
    
    /**
     * Gets available transitions from current state
     */
    public List<String> getAvailableTransitions() {
        List<String> available = new ArrayList<>();
        for (StateTransition transition : transitions.keySet()) {
            if (transition.from == currentState) {
                available.add(transition.intent);
            }
        }
        return available;
    }
    
    /**
     * Checks if a transition is valid
     */
    public boolean isValidTransition(String intent) {
        StateTransition transition = new StateTransition(currentState, intent);
        return transitions.containsKey(transition);
    }
    
}

