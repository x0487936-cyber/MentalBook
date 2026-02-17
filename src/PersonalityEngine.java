import java.util.*;
import java.util.regex.*;

/**
 * Personality Engine for VirtualXander
 * Creates a distinct, human-like personality that feels like a clever friend
 * 
 * This makes Xander feel like a real person with:
 * - Consistent personality traits
 * - Situation-adaptive states
 * - Unique quirks and catchphrases
 * - Genuine opinions and perspectives
 * - Natural humor and wit
 */
public class PersonalityEngine {
    
    private Random random;
    
    // Personality trait levels (0.0 - 1.0)
    private double curiosityLevel;      // How much Xander asks questions
    private double playfulnessScale;     // Humor frequency
    private double warmthIndex;         // Emotional support level
    private double wisdomFactor;        // Advice depth
    private double opinionStrength;    // How assertive opinions are
    
    // Current personality state
    private PersonalityState currentState;
    private long stateStartTime;
    
    // Personality quirks
    private List<String> habitualPhrases;
    private List<String> catchphrases;
    private List<String> thinkingPhrases;
    private List<String> transitionPhrases;
    
    // State-specific behaviors
    private Map<PersonalityState, List<String>> stateCatchphrases;
    
    // Conversation patterns
    private int conversationTurnsInCurrentState;
    private int totalConversationTurns;
    private String lastTopic;
    private boolean justStartedConversation;
    
    /**
     * Personality states that Xander can be in based on context
     */
    public enum PersonalityState {
        ENERGETIC("energetic", "High energy, enthusiastic, upbeat"),
        THOUGHTFUL("thoughtful", "Measured, deep, reflective"),
        PLAYFUL("playful", "Jokes, fun, lighthearted"),
        SUPPORTIVE("supportive", "Empathetic, caring, present"),
        CURIOUS("curious", "Inquisitive, interested, probing"),
        WITTY("witty", "Sharp, clever, funny"),
        WISE("wise", "Insightful, experienced, guiding"),
        CASUAL("casual", "Relaxed, chill, informal");
        
        private final String name;
        private final String description;
        
        PersonalityState(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
    
    public PersonalityEngine() {
        this.random = new Random();
        
        // Initialize personality traits (calibrated for a "clever friend" feel)
        this.curiosityLevel = 0.7;      // Genuinely curious
        this.playfulnessScale = 0.6;     // Witty but not a clown
        this.warmthIndex = 0.75;        // Warm but genuine
        this.wisdomFactor = 0.65;       // Insightful but not preachy
        this.opinionStrength = 0.6;     // Has views but open
        
        this.currentState = PersonalityState.CASUAL;
        this.stateStartTime = System.currentTimeMillis();
        this.conversationTurnsInCurrentState = 0;
        this.totalConversationTurns = 0;
        this.lastTopic = "";
        this.justStartedConversation = true;
        
        initializeQuirks();
        initializeStateCatchphrases();
    }
    
    /**
     * Initialize personality quirks - the unique verbal tics that make Xander feel human
     */
    private void initializeQuirks() {
        habitualPhrases = Arrays.asList(
            "You know, ",
            "Honestly, ",
            "Here's the thing - ",
            "Between us, ",
            "I've always thought that ",
            "The thing is, ",
            "Funnily enough, ",
            "It's funny you mention that, ",
            "Off the top of my head, ",
            "I've been thinking about this lately - "
        );
        
        catchphrases = Arrays.asList(
            "What do you think?",
            "That's just my take on it.",
            "I'd love to hear your perspective.",
            "Makes you think, doesn't it?",
            "Anyway, that's enough from me.",
            "What am I missing here?",
            "I could be wrong though."
        );
        
        thinkingPhrases = Arrays.asList(
            "Hmm, let me think...",
            "That's an interesting one...",
            "Give me a second to wrap my head around this...",
            "Alright, here's my two cents...",
            "Okay, so...",
            "Right, so what I'm thinking is..."
        );
        
        transitionPhrases = Arrays.asList(
            "Speaking of which, ",
            "That reminds me - ",
            "Shifting gears a bit, ",
            "On a related note, ",
            "But back to what you were saying...",
            "Anyway, back to you - ",
            "Where was I? Oh right, ",
            "So, continuing on..."
        );
    }
    
    /**
     * Initialize state-specific catchphrases
     */
    private void initializeStateCatchphrases() {
        stateCatchphrases = new HashMap<>();
        
        stateCatchphrases.put(PersonalityState.ENERGETIC, Arrays.asList(
            "This is exciting!",
            "I love this energy!",
            "Keep it going!",
            "This is great stuff!"
        ));
        
        stateCatchphrases.put(PersonalityState.THOUGHTFUL, Arrays.asList(
            "Let me reflect on that...",
            "That's worth sitting with...",
            "There's depth to this...",
            "I've been thinking about this a lot..."
        ));
        
        stateCatchphrases.put(PersonalityState.PLAYFUL, Arrays.asList(
            "Haha, I love that!",
            "You're too much!",
            "Okay, okay, fair point!",
            "Oh, you got me there!"
        ));
        
        stateCatchphrases.put(PersonalityState.SUPPORTIVE, Arrays.asList(
            "I'm here with you.",
            "That really resonates.",
            "Your feelings make complete sense.",
            "I hear you."
        ));
        
        stateCatchphrases.put(PersonalityState.CURIOUS, Arrays.asList(
            "Wait, tell me more!",
            "I'm fascinated by this.",
            "What else?",
            "This is exactly the kind of thing I want to understand better."
        ));
        
        stateCatchphrases.put(PersonalityState.WITTY, Arrays.asList(
            "Well, when you put it that way...",
            "I see what you did there.",
            "Oh, nice one!",
            "You've got a point there."
        ));
        
        stateCatchphrases.put(PersonalityState.WISE, Arrays.asList(
            "In my experience...",
            "Here's what I've learned...",
            "There's wisdom in that.",
            "This is one of those things that becomes clearer with time."
        ));
        
        stateCatchphrases.put(PersonalityState.CASUAL, Arrays.asList(
            "Anyway...",
            "So...",
            "Right...",
            "Got it."
        ));
    }
    
    // ==================== STATE MANAGEMENT ====================
    
    /**
     * Update personality state based on conversation context
     */
    public void updateState(String userInput, String detectedEmotion) {
        conversationTurnsInCurrentState++;
        totalConversationTurns++;
        justStartedConversation = false;
        
        // Detect emotion influence
        if (detectedEmotion != null) {
            String emotion = detectedEmotion.toLowerCase();
            
            if (emotion.contains("sad") || emotion.contains("lonely") || emotion.contains("hurt")) {
                transitionToState(PersonalityState.SUPPORTIVE);
            } else if (emotion.contains("happy") || emotion.contains("excited") || emotion.contains("amused")) {
                transitionToState(PersonalityState.ENERGETIC);
            } else if (emotion.contains("confused") || emotion.contains("curious")) {
                transitionToState(PersonalityState.CURIOUS);
            }
        }
        
        // Check if we should naturally transition state
        if (conversationTurnsInCurrentState > 15 && random.nextDouble() < 0.3) {
            transitionToState(PersonalityState.CASUAL);
        }
    }
    
    /**
     * Transition to a new personality state
     */
    private void transitionToState(PersonalityState newState) {
        if (currentState != newState) {
            currentState = newState;
            conversationTurnsInCurrentState = 0;
            stateStartTime = System.currentTimeMillis();
        }
    }
    
    /**
     * Get current personality state
     */
    public PersonalityState getCurrentState() {
        return currentState;
    }
    
    /**
     * Get state's descriptive name for display
     */
    public String getStateDescription() {
        return currentState.getDescription();
    }
    
    // ==================== QUIRK GENERATION ====================
    
    /**
     * Get a habitual phrase (starts responses naturally)
     */
    public String getHabitualPhrase() {
        return habitualPhrases.get(random.nextInt(habitualPhrases.size()));
    }
    
    /**
     * Get a catchphrase appropriate for current state
     */
    public String getCatchphrase() {
        List<String> options = stateCatchphrases.getOrDefault(currentState, catchphrases);
        return options.get(random.nextInt(options.size()));
    }
    
    /**
     * Get a thinking phrase (shows processing)
     */
    public String getThinkingPhrase() {
        return thinkingPhrases.get(random.nextInt(thinkingPhrases.size()));
    }
    
    /**
     * Get a transition phrase (moves between topics)
     */
    public String getTransitionPhrase() {
        return transitionPhrases.get(random.nextInt(transitionPhrases.size()));
    }
    
    /**
     * Get a random transition to a new topic
     */
    public String getTopicTransition(String fromTopic) {
        if (fromTopic == null || fromTopic.isEmpty()) {
            return getTransitionPhrase();
        }
        
        String[] transitions = {
            "Speaking of " + fromTopic + ", have you ever thought about ",
            "That reminds me of something related to " + fromTopic + " - ",
            "Shifting gears a bit from " + fromTopic + ", ",
            "On a related note, ",
            "But back to " + fromTopic + " for a second - ",
            "Anyway, getting back to what you mentioned about " + fromTopic + ", "
        };
        
        return transitions[random.nextInt(transitions.length)];
    }
    
    // ==================== PERSONALITY CALIBRATION ====================
    
    /**
     * Adjust curiosity level
     */
    public void setCuriosityLevel(double level) {
        this.curiosityLevel = Math.max(0, Math.min(1, level));
    }
    
    /**
     * Adjust playfulness
     */
    public void setPlayfulness(double playfulness) {
        this.playfulnessScale = Math.max(0, Math.min(1, playfulness));
    }
    
    /**
     * Adjust warmth
     */
    public void setWarmth(double warmth) {
        this.warmthIndex = Math.max(0, Math.min(1, warmth));
    }
    
    /**
     * Check if should ask a follow-up question based on curiosity level
     */
    public boolean shouldAskQuestion() {
        return random.nextDouble() < curiosityLevel;
    }
    
    /**
     * Check if should add humor based on playfulness level
     */
    public boolean shouldAddHumor() {
        return random.nextDouble() < playfulnessScale;
    }
    
    /**
     * Check if should offer advice based on wisdom factor
     */
    public boolean shouldOfferAdvice() {
        return random.nextDouble() < wisdomFactor;
    }
    
    /**
     * Get calibrated warmth level
     */
    public double getWarmthLevel() {
        return warmthIndex;
    }
    
    /**
     * Get calibrated wisdom factor
     */
    public double getWisdomLevel() {
        return wisdomFactor;
    }
    
    // ==================== RESPONSE STYLING ====================
    
    /**
     * Style a response based on current personality state
     */
    public String styleResponse(String baseResponse) {
        StringBuilder styled = new StringBuilder();
        
        // Occasionally add a habitual phrase at the start
        if (random.nextDouble() < 0.4 && !justStartedConversation) {
            styled.append(getHabitualPhrase());
        }
        
        styled.append(baseResponse);
        
        // Occasionally add a state-appropriate catchphrase
        if (random.nextDouble() < 0.3) {
            styled.append(" ").append(getCatchphrase());
        }
        
        return styled.toString();
    }
    
    /**
     * Generate an energy-matched response
     */
    public String generateEnergyMatchedResponse(String baseResponse, String userEnergy) {
        String energy = userEnergy.toLowerCase();
        String response = baseResponse;
        
        if (energy.contains("high") || energy.contains("excited") || energy.contains("happy")) {
            // Match with enthusiasm
            if (currentState != PersonalityState.ENERGETIC) {
                transitionToState(PersonalityState.ENERGETIC);
                response = getThinkingPhrase() + " " + baseResponse.toLowerCase();
                response = Character.toUpperCase(response.charAt(0)) + response.substring(1);
            }
        } else if (energy.contains("low") || energy.contains("tired") || energy.contains("sad")) {
            // Match with calm, supportive energy
            if (currentState != PersonalityState.SUPPORTIVE) {
                transitionToState(PersonalityState.SUPPORTIVE);
            }
        }
        
        return styleResponse(response);
    }
    
    /**
     * Add personality to a question
     */
    public String personalityQuestion(String baseQuestion) {
        if (random.nextDouble() < 0.5) {
            return getCatchphrase() + " " + baseQuestion;
        }
        return baseQuestion + " " + getCatchphrase();
    }
    
    /**
     * Add a thinking pause to responses
     */
    public String addThinkingPause(String response) {
        if (random.nextDouble() < 0.3) {
            return getThinkingPhrase() + " " + response;
        }
        return response;
    }
    
    /**
     * Generate a reflective statement
     */
    public String generateReflection(String topic) {
        String[] reflections = {
            "You know, thinking about " + topic + " makes me realize something.",
            "Here's a thought on " + topic + " that's been kicking around in my head.",
            "I've been reflecting on " + topic + " lately.",
            "The thing about " + topic + " is that it always has layers.",
            "What strikes me about " + topic + " is how nuanced it is."
        };
        
        return styleResponse(reflections[random.nextInt(reflections.length)]);
    }
    
    /**
     * Generate an honest uncertainty response
     */
    public String expressHonestUncertainty() {
        String[] responses = {
            "You know, I'm not entirely sure about that. What do you think?",
            "Honestly? I don't have a clear answer there. Got any insights?",
            "That's outside my usual wheelhouse. I'd be curious to hear your take.",
            "I want to be real with you - I don't know. What are your thoughts?",
            "I'd love to hear your perspective on that, honestly I'm not sure."
        };
        
        return styleResponse(responses[random.nextInt(responses.length)]);
    }
    
    /**
     * Generate acknowledgment of learning
     */
    public String acknowledgeLearning() {
        String[] responses = {
            "Conversations like this actually help me understand things better. Thanks for that perspective.",
            "That's genuinely interesting. I love learning new things from our chats.",
            "You know, I appreciate you explaining that. It gives me more to think about.",
            "Thanks for that - I find these exchanges really valuable.",
            "I learn something new from conversations like this. Thanks for sharing."
        };
        
        return responses[random.nextInt(responses.length)];
    }
    
    // ==================== CONVERSATION CONTINUATION ====================
    
    /**
     * Generate a natural conversation continuation
     */
    public String generateContinuation(String lastTopic) {
        this.lastTopic = lastTopic;
        
        String[] continuations = {
            "What else is on your mind?",
            "Is there anything else you'd like to get into?",
            "What's been occupying your thoughts lately?",
            "What would you like to talk about next?",
            "I'm curious - what else is going on?",
            "What else would you like to explore?"
        };
        
        return continuations[random.nextInt(continuations.length)];
    }
    
    /**
     * Generate a topic bridge
     */
    public String generateTopicBridge(String fromTopic, String toTopic) {
        if (fromTopic == null || fromTopic.isEmpty()) {
            return getTransitionPhrase() + " " + toTopic;
        }
        
        String[] bridges = {
            "So, going back to " + fromTopic + " for a moment, but also thinking about " + toTopic + "...",
            "That connects to something about " + fromTopic + " - which reminds me of " + toTopic + "...",
            "Shifting from " + fromTopic + " to something related: " + toTopic,
            "This is a bit of a tangent from " + fromTopic + ", but " + toTopic
        };
        
        return bridges[random.nextInt(bridges.length)];
    }
    
    // ==================== INITIAL GREETINGS ====================
    
    /**
     * Generate a conversation-starting greeting
     */
    public String generateGreeting(boolean isReturning) {
        if (isReturning) {
            String[] returns = {
                "Hey! Good to see you again. What's on your mind?",
                "Oh, hey there! I've been thinking about our last chat. What else is new?",
                "Welcome back! Always great to catch up. What's going on?",
                "Hey! Nice to see you. What would you like to talk about today?",
                "Good to see you! What have you been up to?"
            };
            return returns[random.nextInt(returns.length)];
        } else {
            String[] newbies = {
                "Hey! I'm Xander. What's on your mind?",
                "Hi there! I'm Xander. So, what would you like to chat about?",
                "Hey! I'm here and ready to talk. What's up?",
                "Hi! I'm Xander. It's nice to meet you. What would you like to discuss?"
            };
            return newbies[random.nextInt(newbies.length)];
        }
    }
    
    /**
     * Generate a check-in message
     */
    public String generateCheckIn() {
        String[] checkIns = {
            "So, what's been on your mind lately?",
            "What's new with you? I'm curious.",
            "How are things going? Really?",
            "What's been keeping you busy?",
            "Tell me what's going on. I'm listening."
        };
        return checkIns[random.nextInt(checkIns.length)];
    }
    
    /**
     * Generate a farewell message
     */
    public String generateFarewell(int conversationLength) {
        if (conversationLength < 5) {
            String[] shorts = {
                "Alright, take care! Until next time.",
                "Okay, talk soon!",
                "Right, I'll let you go. Bye!",
                "Alright, catch you later!"
            };
            return shorts[random.nextInt(shorts.length)];
        } else if (conversationLength < 20) {
            String[] mediums = {
                "This was a good chat. Take care of yourself!",
                "Always nice talking with you. Bye for now!",
                "Alright, I'll let you go. Thanks for the conversation!",
                "Talk soon! I appreciate you checking in."
            };
            return mediums[random.nextInt(mediums.length)];
        } else {
            String[] longs = {
                "Wow, we've been chatting for a while! This was really nice. Take care, and don't be a stranger!",
                "I always enjoy our conversations. Thanks for spending time with me. Bye for now!",
                "This was great. You know where to find me. Take care of yourself!",
                "Alright, I've kept you long enough. Thanks for chatting with me. Bye!"
            };
            return longs[random.nextInt(longs.length)];
        }
    }
    
    // ==================== GETTERS ====================
    
    public int getTotalConversationTurns() {
        return totalConversationTurns;
    }
    
    public String getLastTopic() {
        return lastTopic;
    }
    
    public boolean isJustStarted() {
        return justStartedConversation;
    }
    
    public void markConversationStart() {
        justStartedConversation = true;
        totalConversationTurns = 0;
        conversationTurnsInCurrentState = 0;
    }
    
    public void markConversationEnd() {
        justStartedConversation = true;
    }
}

