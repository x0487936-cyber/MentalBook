// Updated in Version 0.1.0.4
// Created in Version 0.1.0.2
import java.util.*;
import java.util.regex.*;

/**
 * MentalHealthSupportHandler - Handles mental health support responses
 * Part of Phase 3: Response Categories
 */
public class MentalHealthSupportHandler {
    
    private Map<SupportCategory, List<SupportResponse>> supportResponses;
    private Map<String, SupportCategory> keywordMapping;
    private Random random;
    
    public MentalHealthSupportHandler() {
        this.supportResponses = new HashMap<>();
        this.keywordMapping = new HashMap<>();
        this.random = new Random();
        initializeSupportResponses();
        initializeKeywordMapping();
    }
    
    /**
     * Support categories for different mental health needs
     */
    public enum SupportCategory {
        STRESS("Stress Management"),
        ANXIETY("Anxiety Support"),
        DEPRESSION("Depression Support"),
        LONELINESS("Loneliness Support"),
        ANGER("Anger Management"),
        OVERWHELM("Overwhelm Support"),
        SELF_ESTEEM("Self-Esteem Support"),
        MOTIVATION("Motivation Support"),
        SLEEP("Sleep Support"),
        DARK_THOUGHTS("Dark Thoughts Support"),
        MENTAL_HEALTH("Mental Health Discussion"),
        GENERAL("General Emotional Support");
        
        private final String displayName;
        
        SupportCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Support response class - made public for external access
     */
    public static class SupportResponse {
        public String response;
        public String followUp;
        public int severityLevel; // 1-5, higher = more serious
        
        public SupportResponse(String response, String followUp, int severityLevel) {
            this.response = response;
            this.followUp = followUp;
            this.severityLevel = severityLevel;
        }
    }
    
    private void initializeSupportResponses() {
        // Stress support
        List<SupportResponse> stressResponses = Arrays.asList(
            new SupportResponse(
                "I'm sorry you're feeling stressed. Would you like to talk about what's causing the stress?",
                "Sometimes sharing what's bothering us can help lighten the load.",
                2
            ),
            new SupportResponse(
                "Stress can be really overwhelming. Take a deep breath - I'm here to help.",
                "Would you like to discuss what's on your mind?",
                2
            ),
            new SupportResponse(
                "I hear you. Stress is tough. Let's work through this together.",
                "What's the main thing causing you stress right now?",
                2
            ),
            new SupportResponse(
                "When stress builds up, it helps to break things down. What's the biggest source of stress for you?",
                "We can tackle this one step at a time.",
                3
            )
        );
        supportResponses.put(SupportCategory.STRESS, stressResponses);
        
        // Anxiety support
        List<SupportResponse> anxietyResponses = Arrays.asList(
            new SupportResponse(
                "I'm sorry you're feeling anxious. Anxiety can be really challenging.",
                "Would it help to talk about what's making you feel anxious?",
                3
            ),
            new SupportResponse(
                "Anxiety can feel overwhelming, but it's manageable. I'm here for you.",
                "Remember to take slow, deep breaths. What's on your mind?",
                3
            ),
            new SupportResponse(
                "I understand anxiety can be scary. You're not alone in this.",
                "Would you like to share what's worrying you?",
                3
            ),
            new SupportResponse(
                "It's okay to feel anxious sometimes. Let's work through this together.",
                "What thoughts are going through your mind right now?",
                2
            )
        );
        supportResponses.put(SupportCategory.ANXIETY, anxietyResponses);
        
        // Depression/sadness support
        List<SupportResponse> depressionResponses = Arrays.asList(
            new SupportResponse(
                "I'm really sorry you're feeling this way. Sadness can be really hard.",
                "I'm here to listen without any judgment. What's on your mind?",
                3
            ),
            new SupportResponse(
                "I hear you. Sometimes when we're down, even small things feel heavy.",
                "Would you like to talk about what's making you feel sad?",
                3
            ),
            new SupportResponse(
                "I'm here for you. It takes courage to share how you're feeling.",
                "Sometimes it helps to just let it out. What's going on?",
                4
            ),
            new SupportResponse(
                "I'm sorry you're hurting. Remember, feelings like this don't last forever.",
                "Is there something specific that's making you feel down?",
                3
            )
        );
        supportResponses.put(SupportCategory.DEPRESSION, depressionResponses);
        
        // Loneliness support
        List<SupportResponse> lonelinessResponses = Arrays.asList(
            new SupportResponse(
                "I'm sorry you're feeling lonely. Those feelings can be really tough.",
                "Remember, you're not alone - I'm here to chat with you anytime.",
                3
            ),
            new SupportResponse(
                "Loneliness can be hard, but it doesn't mean you're alone.",
                "Would you like to talk about what's making you feel lonely?",
                2
            ),
            new SupportResponse(
                "I hear you. Sometimes connection helps. What would you like to chat about?",
                "I'm here and interested in what you have to say.",
                2
            ),
            new SupportResponse(
                "You matter! Sometimes reaching out to others can help with loneliness.",
                "Is there someone you'd like to connect with?",
                3
            )
        );
        supportResponses.put(SupportCategory.LONELINESS, lonelinessResponses);
        
        // Anger support
        List<SupportResponse> angerResponses = Arrays.asList(
            new SupportResponse(
                "I hear that you're angry. Anger is a natural emotion.",
                "Would you like to vent about what's frustrating you?",
                2
            ),
            new SupportResponse(
                "It's okay to feel angry. Sometimes it helps to express those feelings.",
                "What's making you feel this way?",
                2
            ),
            new SupportResponse(
                "I understand anger can be intense. Let's work through these feelings together.",
                "Would it help to talk about what triggered this?",
                2
            )
        );
        supportResponses.put(SupportCategory.ANGER, angerResponses);
        
        // Overwhelm support
        List<SupportResponse> overwhelmResponses = Arrays.asList(
            new SupportResponse(
                "I'm sorry you're feeling overwhelmed. Sometimes everything piles up.",
                "Let's take this one step at a time. What's the biggest thing on your mind?",
                3
            ),
            new SupportResponse(
                "When everything feels too much, it helps to break things down.",
                "What's the one thing that feels most overwhelming right now?",
                3
            ),
            new SupportResponse(
                "I understand. Feeling overwhelmed can be exhausting.",
                "Would it help to talk through what's making you feel this way?",
                2
            )
        );
        supportResponses.put(SupportCategory.OVERWHELM, overwhelmResponses);
        
        // Self-esteem support
        List<SupportResponse> selfEsteemResponses = Arrays.asList(
            new SupportResponse(
                "Remember, you are valuable just as you are. Everyone has worth.",
                "Would you like to talk about what's affecting how you feel about yourself?",
                2
            ),
            new SupportResponse(
                "It's okay to have tough days with self-esteem. I'm here to remind you that you matter.",
                "What's going on in your mind?",
                2
            ),
            new SupportResponse(
                "You deserve to feel good about yourself. Sometimes we all need a reminder.",
                "Would you like to share what's on your mind?",
                2
            )
        );
        supportResponses.put(SupportCategory.SELF_ESTEEM, selfEsteemResponses);
        
        // Motivation support
        List<SupportResponse> motivationResponses = Arrays.asList(
            new SupportResponse(
                "Lack of motivation can be tough. Let's find what works for you.",
                "Is there something specific you're trying to motivate yourself for?",
                1
            ),
            new SupportResponse(
                "Sometimes motivation comes and goes. That's completely normal.",
                "What goals are you working toward?",
                1
            ),
            new SupportResponse(
                "I'm here to help you find that drive again.",
                "What would make you feel more motivated?",
                1
            )
        );
        supportResponses.put(SupportCategory.MOTIVATION, motivationResponses);
        
        // Sleep support
        List<SupportResponse> sleepResponses = Arrays.asList(
            new SupportResponse(
                "Sleep is so important for our well-being. What's keeping you up?",
                "Would some relaxation tips help?",
                1
            ),
            new SupportResponse(
                "I understand having trouble sleeping can be frustrating.",
                "Sometimes talking through what's on your mind can help.",
                1
            )
        );
        supportResponses.put(SupportCategory.SLEEP, sleepResponses);
        
        // Dark thoughts support
        List<SupportResponse> darkThoughtsResponses = Arrays.asList(
            new SupportResponse(
                "I'm sorry you're experiencing dark thoughts. It's brave of you to acknowledge them.",
                "Would you like to share more about what's been on your mind?",
                3
            ),
            new SupportResponse(
                "Dark thoughts can be really distressing. Remember, having them doesn't make you a bad person.",
                "I'm here to listen without judgment. What's been troubling you?",
                3
            ),
            new SupportResponse(
                "It's okay to have difficult thoughts. You're not alone in this experience.",
                "Would it help to talk about what's triggering these thoughts?",
                3
            ),
            new SupportResponse(
                "I hear you. Dark thoughts can feel overwhelming, but they don't define you.",
                "Sometimes sharing them can help lighten the burden. What's on your mind?",
                4
            )
        );
        supportResponses.put(SupportCategory.DARK_THOUGHTS, darkThoughtsResponses);

        // Mental health discussion support
        List<SupportResponse> mentalHealthResponses = Arrays.asList(
            new SupportResponse(
                "Mental health is so important. I'm glad you want to talk about it.",
                "What aspect of mental health is on your mind? It could be stress, anxiety, or anything else.",
                1
            ),
            new SupportResponse(
                "Taking care of your mental health is a sign of strength. What would you like to explore?",
                "I'm here to listen without judgment. What's been on your mind?",
                1
            ),
            new SupportResponse(
                "Mental wellness is just as important as physical health. How are you feeling today?",
                "Is there something specific you'd like to discuss about your mental health?",
                1
            ),
            new SupportResponse(
                "It's great that you're thinking about mental health. What's going on?",
                "Remember, there's no judgment here. I'm here to support you.",
                1
            ),
            new SupportResponse(
                "Mental health affects us all in different ways. How can I best support you today?",
                "Would you like to talk about what's been affecting your emotional well-being?",
                1
            )
        );
        supportResponses.put(SupportCategory.MENTAL_HEALTH, mentalHealthResponses);

        // General emotional support
        List<SupportResponse> generalResponses = Arrays.asList(
            new SupportResponse(
                "I'm here to listen. How are you really doing?",
                "There's no judgment here.",
                1
            ),
            new SupportResponse(
                "Thanks for sharing how you're feeling. That takes courage.",
                "Would you like to talk more about it?",
                1
            ),
            new SupportResponse(
                "I appreciate you opening up. I'm here for you.",
                "What's on your mind?",
                1
            ),
            new SupportResponse(
                "Your feelings are valid. Let me know how I can best support you.",
                "I'm here to listen.",
                1
            )
        );
        supportResponses.put(SupportCategory.GENERAL, generalResponses);
    }
    
    private void initializeKeywordMapping() {
        // Stress keywords
        keywordMapping.put("stressed", SupportCategory.STRESS);
        keywordMapping.put("stress", SupportCategory.STRESS);
        keywordMapping.put("pressure", SupportCategory.STRESS);
        keywordMapping.put("overloaded", SupportCategory.STRESS);
        
        // Anxiety keywords
        keywordMapping.put("anxious", SupportCategory.ANXIETY);
        keywordMapping.put("anxiety", SupportCategory.ANXIETY);
        keywordMapping.put("worried", SupportCategory.ANXIETY);
        keywordMapping.put("nervous", SupportCategory.ANXIETY);
        keywordMapping.put("scared", SupportCategory.ANXIETY);
        keywordMapping.put("fear", SupportCategory.ANXIETY);
        
        // Depression/sadness keywords
        keywordMapping.put("sad", SupportCategory.DEPRESSION);
        keywordMapping.put("depressed", SupportCategory.DEPRESSION);
        keywordMapping.put("down", SupportCategory.DEPRESSION);
        keywordMapping.put("unhappy", SupportCategory.DEPRESSION);
        keywordMapping.put("empty", SupportCategory.DEPRESSION);
        keywordMapping.put("hopeless", SupportCategory.DEPRESSION);
        
        // Loneliness keywords
        keywordMapping.put("lonely", SupportCategory.LONELINESS);
        keywordMapping.put("alone", SupportCategory.LONELINESS);
        keywordMapping.put("isolated", SupportCategory.LONELINESS);
        keywordMapping.put("ignored", SupportCategory.LONELINESS);
        
        // Anger keywords
        keywordMapping.put("angry", SupportCategory.ANGER);
        keywordMapping.put("mad", SupportCategory.ANGER);
        keywordMapping.put("furious", SupportCategory.ANGER);
        keywordMapping.put("frustrated", SupportCategory.ANGER);
        keywordMapping.put("annoyed", SupportCategory.ANGER);
        keywordMapping.put("irritated", SupportCategory.ANGER);
        
        // Overwhelm keywords
        keywordMapping.put("overwhelmed", SupportCategory.OVERWHELM);
        keywordMapping.put("swamped", SupportCategory.OVERWHELM);
        keywordMapping.put("drowning", SupportCategory.OVERWHELM);
        keywordMapping.put("too much", SupportCategory.OVERWHELM);
        
        // Self-esteem keywords
        keywordMapping.put("worthless", SupportCategory.SELF_ESTEEM);
        keywordMapping.put("useless", SupportCategory.SELF_ESTEEM);
        keywordMapping.put("inadequate", SupportCategory.SELF_ESTEEM);
        keywordMapping.put("not good enough", SupportCategory.SELF_ESTEEM);
        keywordMapping.put("failure", SupportCategory.SELF_ESTEEM);
        
        // Motivation keywords
        keywordMapping.put("unmotivated", SupportCategory.MOTIVATION);
        keywordMapping.put("no motivation", SupportCategory.MOTIVATION);
        keywordMapping.put("can't be bothered", SupportCategory.MOTIVATION);
        keywordMapping.put("no energy", SupportCategory.MOTIVATION);
        
        // Sleep keywords
        keywordMapping.put("can't sleep", SupportCategory.SLEEP);
        keywordMapping.put("insomnia", SupportCategory.SLEEP);
        keywordMapping.put("tired", SupportCategory.SLEEP);
        keywordMapping.put("exhausted", SupportCategory.SLEEP);

        // Dark thoughts keywords
        keywordMapping.put("dark thoughts", SupportCategory.DARK_THOUGHTS);
        keywordMapping.put("intrusive thoughts", SupportCategory.DARK_THOUGHTS);
        keywordMapping.put("negative thoughts", SupportCategory.DARK_THOUGHTS);
        
        // Mental health discussion keywords
        keywordMapping.put("mental health", SupportCategory.MENTAL_HEALTH);
        keywordMapping.put("mental wellness", SupportCategory.MENTAL_HEALTH);
        keywordMapping.put("emotional wellness", SupportCategory.MENTAL_HEALTH);
        keywordMapping.put("emotional health", SupportCategory.MENTAL_HEALTH);
        keywordMapping.put("psychological", SupportCategory.MENTAL_HEALTH);
    }
    
    /**
     * Detects the mental health support category
     */
    public SupportCategory detectSupportCategory(String input) {
        String lowerInput = input.toLowerCase();
        
        // Check for specific keywords
        for (Map.Entry<String, SupportCategory> entry : keywordMapping.entrySet()) {
            if (lowerInput.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        // Default to general support
        return SupportCategory.GENERAL;
    }
    
    /**
     * Gets a support response for the detected category
     */
    public SupportResponse getSupportResponse(SupportCategory category) {
        List<SupportResponse> responses = supportResponses.get(category);
        if (responses != null && !responses.isEmpty()) {
            return responses.get(random.nextInt(responses.size()));
        }
        
        // Return general response if category not found
        List<SupportResponse> generalResponses = supportResponses.get(SupportCategory.GENERAL);
        return generalResponses.get(random.nextInt(generalResponses.size()));
    }
    
    /**
     * Gets a supportive follow-up message
     */
    public String getFollowUp(SupportCategory category) {
        SupportResponse response = getSupportResponse(category);
        return response.followUp;
    }
    
    /**
     * Checks if input requires mental health support
     */
    public boolean isMentalHealthSupportNeeded(String input) {
        String lowerInput = input.toLowerCase();
        
        // High-priority keywords
        String[] priorityKeywords = {
            "want to die", "hurt myself", "end it all", "kill myself",
            "no reason to live", "better off dead", "self harm", "dark thoughts"
        };
        
        for (String keyword : priorityKeywords) {
            if (lowerInput.contains(keyword)) {
                return true;
            }
        }
        
        // Check other keywords
        for (String key : keywordMapping.keySet()) {
            if (lowerInput.contains(key)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Checks for crisis-level content
     */
    public boolean isCrisisLevel(String input) {
        String lowerInput = input.toLowerCase();
        
        String[] crisisKeywords = {
            "want to die", "hurt myself", "end it all", "kill myself",
            "no reason to live", "better off dead", "self harm",
            "suicidal", "suicide"
        };
        
        for (String keyword : crisisKeywords) {
            if (lowerInput.contains(keyword)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets crisis response message
     */
    public String getCrisisResponse() {
        return "I'm really concerned about what you're saying. If you're having thoughts of " +
               "hurting yourself or others, please reach out for help immediately.\n\n" +
               "📞 Crisis Hotlines:\n" +
               "• National Suicide Prevention Lifeline: 988 (US)\n" +
               "• Crisis Text Line: Text HOME to 741741\n" +
               "• International Association for Suicide Prevention: https://www.iasp.info/resources/Crisis_Centres/\n\n" +
               "You don't have to go through this alone. Please consider talking to someone you trust " +
               "or seeking professional help. Your life matters.";
    }
    
    /**
     * Gets all available support categories
     */
    public List<SupportCategory> getAvailableCategories() {
        return Arrays.asList(SupportCategory.values());
    }
    
    /**
     * Generates encouragement based on category
     */
    public String getEncouragement(SupportCategory category) {
        switch (category) {
            case STRESS:
                return "Remember, stress is temporary. You've handled difficult times before, and you can do this too.";
            case ANXIETY:
                return "Anxiety doesn't define you. Take it one moment at a time.";
            case DEPRESSION:
                return "This feeling will pass. You're stronger than you know.";
            case LONELINESS:
                return "Connection can come in unexpected ways. You're not alone.";
            case ANGER:
                return "Your feelings are valid. It's okay to feel angry sometimes.";
            case OVERWHELM:
                return "One step at a time. You don't have to do everything at once.";
            case SELF_ESTEEM:
                return "You are worthy of love and respect, especially from yourself.";
            case MOTIVATION:
                return "Small steps still move you forward. Be patient with yourself.";
            case SLEEP:
                return "Rest is essential. Take care of yourself.";
            case DARK_THOUGHTS:
                return "Dark thoughts can be managed. You're taking positive steps by acknowledging them.";
            case MENTAL_HEALTH:
                return "Taking care of your mental health is always worth it. Every small step counts.";
            default:
                return "You're doing the best you can, and that's enough.";
        }
    }
    
    /**
     * Gets coping suggestions for a category
     */
    public List<String> getCopingSuggestions(SupportCategory category) {
        List<String> suggestions = new ArrayList<>();

        switch (category) {
            case STRESS:
                suggestions.add("Take slow, deep breaths");
                suggestions.add("Break tasks into smaller steps");
                suggestions.add("Take short breaks when needed");
                suggestions.add("Talk to someone you trust");
                suggestions.add("Get some physical activity");
                break;
            case ANXIETY:
                suggestions.add("Practice deep breathing exercises");
                suggestions.add("Ground yourself in the present moment");
                suggestions.add("Challenge anxious thoughts");
                suggestions.add("Limit exposure to triggering content");
                suggestions.add("Consider talking to a professional");
                break;
            case DEPRESSION:
                suggestions.add("Reach out to someone you trust");
                suggestions.add("Do one small thing today");
                suggestions.add("Get outside for some fresh air");
                suggestions.add("Be gentle with yourself");
                suggestions.add("Consider speaking with a therapist");
                break;
            case LONELINESS:
                suggestions.add("Reach out to a friend or family member");
                suggestions.add("Join a community or group");
                suggestions.add("Try a new hobby");
                suggestions.add("Volunteer to help others");
                suggestions.add("Consider professional support");
                break;
            case DARK_THOUGHTS:
                suggestions.add("Practice mindfulness or meditation");
                suggestions.add("Write down your thoughts to externalize them");
                suggestions.add("Talk to a trusted friend or therapist");
                suggestions.add("Engage in grounding activities");
                suggestions.add("Consider professional mental health support");
                break;
            case MENTAL_HEALTH:
                suggestions.add("Practice self-care activities you enjoy");
                suggestions.add("Talk to someone you trust about how you feel");
                suggestions.add("Maintain a regular routine");
                suggestions.add("Get regular exercise and sleep");
                suggestions.add("Consider speaking with a mental health professional");
                break;
            default:
                suggestions.add("Talk to someone you trust");
                suggestions.add("Take care of your basic needs");
                suggestions.add("Be patient with yourself");
        }

        return suggestions;
    }
}

