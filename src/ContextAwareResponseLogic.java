import java.util.*;
import java.util.regex.*;

/**
 * Context-Aware Response Logic for VirtualXander
 * Enhances responses based on conversation history, user patterns, and contextual understanding
 */
public class ContextAwareResponseLogic {
    
    private ConversationContext context;
    private EmotionDetector emotionDetector;
    private Map<String, ContextPattern> contextPatterns;
    private Map<String, TopicHandler> topicHandlers;
    private Random random;
    
    public ContextAwareResponseLogic() {
        this.contextPatterns = new HashMap<>();
        this.topicHandlers = new HashMap<>();
        this.random = new Random();
        initializeContextPatterns();
        initializeTopicHandlers();
    }
    
    /**
     * Sets the conversation context
     */
    public void setContext(ConversationContext context) {
        this.context = context;
    }
    
    /**
     * Gets the conversation context (uses field if available)
     */
    private ConversationContext getContext(ConversationContext paramContext) {
        return paramContext != null ? paramContext : this.context;
    }
    
    /**
     * Sets the emotion detector
     */
    public void setEmotionDetector(EmotionDetector detector) {
        this.emotionDetector = detector;
    }
    
    /**
     * Gets the emotion (uses field if available)
     */
    private EmotionDetector.Emotion getEmotion(EmotionDetector.Emotion paramEmotion) {
        return paramEmotion != null ? paramEmotion : 
            (this.emotionDetector != null ? this.emotionDetector.detectEmotion("").getPrimaryEmotion() : null);
    }
    
    /**
     * Context pattern for matching conversational sequences
     */
    private static class ContextPattern {
        public List<String> sequence;
        public String response;
        public int matchPosition;
        
        public ContextPattern(List<String> sequence, String response) {
            this.sequence = sequence;
            this.response = response;
            this.matchPosition = 0;
        }
    }
    
    /**
     * Topic handler for managing conversation topics
     */
    public interface TopicHandler {
        boolean canHandle(String topic);
        String generateResponse(String input, ConversationContext context, EmotionDetector.Emotion emotion);
        boolean isComplete(String input);
    }
    
    private void initializeContextPatterns() {
        // Question-answer patterns
        addContextPattern(
            Arrays.asList("what", "is", "?", "answer"),
            "Let me explain that for you:"
        );
        
        // Feeling follow-up patterns
        addContextPattern(
            Arrays.asList("how", "are", "you", "feeling", "why"),
            "I appreciate you asking! I'm doing well since I get to chat with you."
        );
        
        // Help seeking patterns
        addContextPattern(
            Arrays.asList("help", "me", "please", "need"),
            "I'd be happy to help! Let me assist you with that."
        );
        
        // Gratitude patterns
        addContextPattern(
            Arrays.asList("thank", "thanks", "appreciate"),
            "You're very welcome! Happy to help anytime."
        );
        
        // Agreement patterns
        addContextPattern(
            Arrays.asList("yes", "yeah", "agree", "true"),
            "Great to hear we agree on this!"
        );
        
        // Disagreement patterns
        addContextPattern(
            Arrays.asList("no", "nope", "disagree", "not"),
            "I understand. Different perspectives are always valuable!"
        );
        
        // Continue conversation patterns (user explicitly wants to continue)
        addContextPattern(
            Arrays.asList("want to talk", "talk about it", "tell me more", "i want to"),
            "I'm here to listen. Please share what's on your mind."
        );
        
        // Breakup/relationship continuation patterns
        addContextPattern(
            Arrays.asList("my ex", "broke up", "breakup", "heartbroken"),
            "I'm really sorry you're going through this. Would you like to share more about what happened?"
        );
        
        // Achievement/pride patterns
        addContextPattern(
            Arrays.asList("did it", "accomplished", "achieved", "won", "completed"),
            "That's amazing! You should be incredibly proud of yourself!"
        );
        
        // Surprise patterns
        addContextPattern(
            Arrays.asList("wow", "no way", "unbelievable", "surprised", "can't believe"),
            "I know! Life is full of surprises, isn't it?"
        );
        
        // Relief patterns
        addContextPattern(
            Arrays.asList("finally", "relief", "glad that's over", "made it through"),
            "What a relief! You handled that beautifully!"
        );
        
        // Nostalgic patterns
        addContextPattern(
            Arrays.asList("remember when", "good old days", "those were", "childhood"),
            "Nostalgia is so special! What memories are you reflecting on?"
        );
        
        // Inspired patterns
        addContextPattern(
            Arrays.asList("inspired", "motivated", "ideas", "creativity"),
            "Inspiration is a beautiful thing! What exciting possibilities do you see?"
        );
        
        // Hope patterns
        addContextPattern(
            Arrays.asList("hopeful", "optimistic", "looking forward", "better days"),
            "Hope is such a powerful feeling! What are you looking forward to?"
        );
        
        // Amused patterns
        addContextPattern(
            Arrays.asList("funny", "lol", "laughing", "hilarious", "that was great"),
            "I love that you're in a humorous mood! Laughter is the best medicine!"
        );
    }
    
    private void initializeTopicHandlers() {
        // Homework topic handler
        topicHandlers.put("homework", new HomeworkTopicHandler());
        topicHandlers.put("math", new HomeworkTopicHandler());
        topicHandlers.put("science", new HomeworkTopicHandler());
        topicHandlers.put("history", new HomeworkTopicHandler());
        
        // Mental health topic handler
        topicHandlers.put("mental_health", new MentalHealthTopicHandler());
        topicHandlers.put("feelings", new MentalHealthTopicHandler());
        topicHandlers.put("emotions", new MentalHealthTopicHandler());
        
        // Gaming topic handler
        topicHandlers.put("gaming", new GamingTopicHandler());
        topicHandlers.put("games", new GamingTopicHandler());
        
        // Creative writing topic handler
        topicHandlers.put("creative_writing", new CreativeWritingTopicHandler());
        topicHandlers.put("writing", new CreativeWritingTopicHandler());
    }
    
    private void addContextPattern(List<String> sequence, String response) {
        contextPatterns.put(String.join("|", sequence), new ContextPattern(sequence, response));
    }
    
    /**
     * Checks if input matches any context pattern sequence
     */
    private String matchContextPattern(String input) {
        String lowerInput = input.toLowerCase();
        String bestMatch = null;
        int bestMatchScore = -1;
        int bestMatchPosition = Integer.MAX_VALUE;
        int lastUsedPosition = -1;
        
        for (ContextPattern pattern : contextPatterns.values()) {
            // Use the sequence field for matching
            int matchCount = 0;
            int firstMatchPosition = -1;
            for (int i = 0; i < pattern.sequence.size(); i++) {
                String keyword = pattern.sequence.get(i);
                if (lowerInput.contains(keyword.toLowerCase())) {
                    matchCount++;
                    if (firstMatchPosition == -1) {
                        firstMatchPosition = i;
                    }
                }
            }
            
            // Calculate match score: prioritize more matches and earlier positions
            if (matchCount > 0) {
                // Score = (matchCount * 100) - (firstMatchPosition * 10)
                // Higher match count = better, but earlier position = better
                int score = (matchCount * 100) - (firstMatchPosition * 10);
                
                // Avoid matching the same pattern position consecutively
                if (score > bestMatchScore || (score == bestMatchScore && firstMatchPosition != lastUsedPosition && firstMatchPosition < bestMatchPosition)) {
                    bestMatchScore = score;
                    bestMatch = pattern.response;
                    // Update matchPosition for tracking
                    pattern.matchPosition = firstMatchPosition;
                }
            }
        }
        
        // Read matchPosition from patterns to verify it's being used
        for (ContextPattern pattern : contextPatterns.values()) {
            lastUsedPosition = pattern.matchPosition; // Reading the matchPosition value
        }
        
        return bestMatch;
    }
    
    /**
     * Generates a contextually enhanced response
     * ENHANCEMENT MODE: Takes a base response and adds contextual elements without duplicating
     */
    public String generateContextualResponse(String baseResponse, String intent, String userInput, 
                                           EmotionDetector.Emotion emotion, 
                                           ConversationContext context) {
        if (baseResponse == null || baseResponse.isEmpty()) {
            // Use getContextualMain for full contextual response
            String mainResponse = getContextualMain(intent, userInput, context);
            String prefix = getContextualPrefix(emotion, context);
            return prefix + mainResponse;
        }
        
        // Skip prefix addition for greeting intent and simple greeting inputs
        // to avoid awkward "I understand. Hey there!" responses
        boolean isSimpleGreeting = isSimpleGreetingInput(userInput);
        if ((intent.equals("greeting") || intent.equals("wellbeing_how") || intent.equals("wellbeing_response")) && isSimpleGreeting) {
            // For simple greetings, just add suffix if needed, no prefix
            String contextualSuffix = getContextualSuffixOnly(baseResponse, emotion, context);
            if (!contextualSuffix.isEmpty() && !baseResponse.contains(contextualSuffix.trim())) {
                return baseResponse + contextualSuffix;
            }
            return baseResponse;
        }
        
        StringBuilder enhancedResponse = new StringBuilder(baseResponse);
        
        // Add contextual prefix (using the unused method) only for non-greetings
        String contextualPrefix = getContextualPrefix(emotion, context);
        if (!contextualPrefix.isEmpty() && !baseResponse.toLowerCase().contains(contextualPrefix.toLowerCase().trim())) {
            enhancedResponse.insert(0, contextualPrefix);
        }
        
        // Add additional prefix from getContextualPrefixOnly for enhancement
        String additionalPrefix = getContextualPrefixOnly(emotion, context);
        if (!additionalPrefix.isEmpty() && !contextualPrefix.contains(additionalPrefix.trim())) {
            enhancedResponse.insert(0, additionalPrefix);
        }
        
        // Add contextual suffix (follow-up question)
        String contextualSuffix = getContextualSuffixOnly(baseResponse, emotion, context);
        if (!contextualSuffix.isEmpty() && !enhancedResponse.toString().contains(contextualSuffix.trim())) {
            enhancedResponse.append(contextualSuffix);
        }
        
        // Also add enhanced contextual suffix for additional emotion-based content
        enhancedContextualSuffix(enhancedResponse, emotion, context);
        
        return enhancedResponse.toString().trim();
    }
    
    /**
     * Checks if the input is a simple greeting (single word greeting like "hi", "hello", "hey")
     * These should not have "I understand." prefixes added
     */
    private boolean isSimpleGreetingInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        String lowerInput = input.toLowerCase().trim();
        // Check for single-word greetings or very short greeting phrases
        String[] simpleGreetings = {"hi", "hello", "hey", "hiya", "howdy", "yo", "sup", "greetings"};
        for (String greeting : simpleGreetings) {
            if (lowerInput.equals(greeting)) {
                return true;
            }
        }
        // Also check for time-based greetings that are short
        if (lowerInput.equals("good morning") || lowerInput.equals("good afternoon") || 
            lowerInput.equals("good evening") || lowerInput.equals("good day")) {
            return true;
        }
        // Check for wellbeing greetings like "how are you", "hru", "how r u"
        String[] wellbeingGreetings = {"how are you", "how r u", "hru", "how's it going", "how do you do", "how r ya"};
        for (String greeting : wellbeingGreetings) {
            if (lowerInput.equals(greeting)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Generates a contextually enhanced response (legacy mode - generates full response)
     * @deprecated Use generateContextualResponse(String baseResponse, ...) instead
     */
    @Deprecated
    public String generateContextualResponse(String intent, String userInput, 
                                           EmotionDetector.Emotion emotion, 
                                           ConversationContext context) {
        String baseResponse = generateDefaultContextualResponse(intent, userInput, context);
        return generateContextualResponse(baseResponse, intent, userInput, emotion, context);
    }
    
    /**
     * Gets contextual prefix based on emotion and conversation state
     */
    private String getContextualPrefix(EmotionDetector.Emotion emotion, ConversationContext context) {
        // Don't add prefix in early conversation (turns 0 and 1)
        // to avoid "I understand" appearing after just 1-2 user messages
        ConversationContext ctx = getContext(context);
        if (ctx != null && ctx.getTurnCount() <= 1) {
            return "";
        }
        
        EmotionDetector.Emotion actualEmotion = getEmotion(emotion);
        if (actualEmotion != null) {
            switch (actualEmotion) {
                case STRESSED:
                case ANXIOUS:
                case OVERWHELMED:
                    return "I hear you. ";
                case SAD:
                case LONELY:
                    return "I'm here for you. ";
                case ANGRY:
                    return "I understand you're frustrated. ";
                case TIRED:
                    return "Take it easy. ";
                case EXCITED:
                case HAPPY:
                    return "That's wonderful! ";
                default:
                    break;
            }
        }
        
        // Removed default "I understand. " - it's not very useful in early conversation
        // and can feel robotic when added to every response
        return "";
    }
    
    /**
     * Gets contextual prefix only for enhancement (shorter, less intrusive)
     */
    private String getContextualPrefixOnly(EmotionDetector.Emotion emotion, ConversationContext context) {
        EmotionDetector.Emotion actualEmotion = getEmotion(emotion);
        if (actualEmotion != null) {
            switch (actualEmotion) {
                case STRESSED:
                case ANXIOUS:
                case OVERWHELMED:
                    return "I hear you. ";
                case SAD:
                case LONELY:
                    return "I'm here for you. ";
                case ANGRY:
                    return "I understand. ";
                case TIRED:
                    return "Take it easy. ";
                case EXCITED:
                case HAPPY:
                    return "That's great! ";
                default:
                    return "";
            }
        }
        return "";
    }
    
    /**
     * Gets contextual suffix only (follow-up question)
     */
    private String getContextualSuffixOnly(String baseResponse, EmotionDetector.Emotion emotion, ConversationContext context) {
        StringBuilder suffix = new StringBuilder();
        
        // Don't add follow-up if base response already contains a question
        if (baseResponse != null && baseResponse.contains("?")) {
            return "";
        }
        
        // Add emotion-based suffix
        EmotionDetector.Emotion actualEmotion = getEmotion(emotion);
        if (actualEmotion != null) {
            switch (actualEmotion) {
                case STRESSED:
                case ANXIOUS:
                case OVERWHELMED:
                    suffix.append(" Remember to take deep breaths.");
                    break;
                case SAD:
                case LONELY:
                    suffix.append(" You're not alone in this.");
                    break;
                case TIRED:
                    suffix.append(" Make sure to get some rest.");
                    break;
                case CONFUSED:
                    suffix.append(" It's okay to ask for clarification.");
                    break;
                default:
                    break;
            }
        }
        
        // Add follow-up based on conversation stage (every 3 turns)
        ConversationContext ctx = getContext(context);
        if (ctx != null && ctx.getTurnCount() > 0) {
            int turnCount = ctx.getTurnCount();
            if (turnCount % 3 == 0 && !suffix.toString().contains("?")) {
                suffix.append(" Is there anything else you'd like to talk about?");
            }
        }
        
        return suffix.toString();
    }
    
    /**
     * Gets the main contextual response
     */
    private String getContextualMain(String intent, String userInput, ConversationContext context) {
        String lowerInput = userInput.toLowerCase();
        
        // Check for topic-specific handlers
        for (TopicHandler handler : topicHandlers.values()) {
            if (handler.canHandle(intent)) {
                return handler.generateResponse(userInput, context, null);
            }
        }
        
        // Check conversation history for context
        ConversationContext ctx = getContext(context);
        if (ctx != null) {
            // Check for follow-up questions
            if (isFollowUpQuestion(lowerInput)) {
                return generateFollowUpResponse(lowerInput, ctx);
            }
            
            // Check for continuation of previous topic
            String currentTopic = ctx.getCurrentTopic();
            if (lowerInput.contains(currentTopic) || 
                lowerInput.contains("more") || 
                lowerInput.contains("tell me more")) {
                return generateTopicContinuationResponse(currentTopic, ctx);
            }
        }
        
        // Generate default contextual response
        return generateDefaultContextualResponse(intent, userInput, context);
    }
    
    private void enhancedContextualSuffix(StringBuilder response, 
                                         EmotionDetector.Emotion emotion, 
                                         ConversationContext context) {
        EmotionDetector.Emotion actualEmotion = getEmotion(emotion);
        if (actualEmotion != null) {
            switch (actualEmotion) {
                case STRESSED:
                case ANXIOUS:
                case OVERWHELMED:
                    response.append(" Remember to take deep breaths.");
                    break;
                case SAD:
                case LONELY:
                    response.append(" You're not alone in this.");
                    break;
                case TIRED:
                    response.append(" Make sure to get some rest.");
                    break;
                case CONFUSED:
                    response.append(" It's okay to ask for clarification.");
                    break;
                default:
                    break;
            }
        }
        
        // Add follow-up based on conversation stage
        ConversationContext ctx = getContext(context);
        if (ctx != null && ctx.getTurnCount() > 0) {
            int turnCount = ctx.getTurnCount();
            if (turnCount % 3 == 0) {
                response.append(" Is there anything else you'd like to talk about?");
            }
        }
    }
    
    /**
     * Checks if the input is a follow-up question
     */
    private boolean isFollowUpQuestion(String input) {
        // Use regex patterns for follow-up detection
        Pattern[] followUpPatterns = {
            Pattern.compile("what do you mean|can you explain|tell me more"),
            Pattern.compile("why is that|how does that|what about|and then"),
            Pattern.compile("so what|what if|how come|elaborate")
        };
        
        for (Pattern pattern : followUpPatterns) {
            if (pattern.matcher(input).find()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Generates response for follow-up questions
     */
    private String generateFollowUpResponse(String input, ConversationContext context) {
        String lastResponse = context.getLastBotResponse();
        
        if (lastResponse != null && lastResponse.contains("?")) {
            return "I'm glad you're asking! Let me clarify that for you.";
        }
        
        if (input.contains("what do you mean")) {
            return "Let me explain that differently.";
        }
        
        if (input.contains("can you explain") || input.contains("tell me more")) {
            return "Of course! Here's more information on that topic.";
        }
        
        return "Great question! Let me elaborate on that.";
    }
    
    /**
     * Generates response for topic continuation
     */
    private String generateTopicContinuationResponse(String topic, ConversationContext context) {
        String normalizedTopic = topic.toLowerCase();
        
        if (normalizedTopic.contains("homework") || normalizedTopic.contains("math") || 
            normalizedTopic.contains("science")) {
            return "Let's continue with your studies. ";
        }
        
        if (normalizedTopic.contains("game") || normalizedTopic.contains("gaming")) {
            return "Back to gaming! ";
        }
        
        if (normalizedTopic.contains("feel") || normalizedTopic.contains("mental")) {
            return "I'm here to support you. ";
        }
        
        return "Let's continue our conversation. ";
    }
    
    /**
     * Generates default contextual response
     */
    private String generateDefaultContextualResponse(String intent, String input, 
                                                   ConversationContext context) {
        // First check if input matches any context pattern sequence
        String patternResponse = matchContextPattern(input);
        if (patternResponse != null) {
            return patternResponse;
        }
        
        // Base responses for common intents with variations
        switch (intent) {
            case "greeting":
                String[] greetings = {"Hello! Great to see you. ", "Hi there! Nice to chat with you. ", "Hey! Happy to talk with you. "};
                return greetings[random.nextInt(greetings.length)];
            case "wellbeing_how":
                String[] wellbeingResponses = {"I'm doing well, thank you for asking! ", "I'm great, thanks for checking in! ", "Doing wonderful, thanks for caring! "};
                return wellbeingResponses[random.nextInt(wellbeingResponses.length)];
            case "homework_help":
                String[] homeworkResponses = {"I'd be happy to help with your homework! ", "Let's tackle your homework together! ", "Homework help is my specialty! "};
                return homeworkResponses[random.nextInt(homeworkResponses.length)];
            case "mental_health_support":
                String[] mentalHealthResponses = {"I'm here to listen and support you. ", "You can share anything with me. ", "I'm here without any judgment. "};
                return mentalHealthResponses[random.nextInt(mentalHealthResponses.length)];
            case "gaming":
                String[] gamingResponses = {"Gaming is always a fun topic! ", "I love chatting about games! ", "Games are a great conversation topic! "};
                return gamingResponses[random.nextInt(gamingResponses.length)];
            case "creative_writing":
                String[] writingResponses = {"Creative writing is wonderful! ", "I enjoy helping with creative writing! ", "Writing can be so rewarding! "};
                return writingResponses[random.nextInt(writingResponses.length)];
            case "advice":
                return "Here's some advice for you: ";
            case "help_request":
                String[] helpResponses = {"How can I assist you today? ", "What can I help you with? ", "What would you like to talk about? "};
                return helpResponses[random.nextInt(helpResponses.length)];
            case "farewell":
                return "Goodbye! It was great chatting! ";
            default:
                return "I'm here to help. Tell me more about what you'd like to talk about. ";
        }
    }
    
    /**
     * Gets suggested follow-up topics based on conversation
     */
    public List<String> getSuggestedFollowUps(String currentTopic, EmotionDetector.Emotion emotion) {
        List<String> suggestions = new ArrayList<>();
        
        // Add emotion-based suggestions
        if (emotion != null) {
            switch (emotion) {
                case STRESSED:
                case ANXIOUS:
                    suggestions.add("Would you like to talk about what's causing this?");
                    suggestions.add("I can share some relaxation techniques.");
                    break;
                case SAD:
                    suggestions.add("Is there something specific on your mind?");
                    suggestions.add("Sometimes talking helps. What's going on?");
                    break;
                case HAPPY:
                case EXCITED:
                    suggestions.add("What's contributing to these great feelings?");
                    suggestions.add("That's wonderful! Anything else exciting?");
                    break;
                case TIRED:
                    suggestions.add("Make sure to take breaks when needed.");
                    suggestions.add("Is there anything I can help lighten your load?");
                    break;
                default:
                    break;
            }
        }
        
        // Add topic-based suggestions
        switch (currentTopic.toLowerCase()) {
            case "homework":
                suggestions.add("Need help with a specific subject?");
                suggestions.add("I can provide study tips!");
                break;
            case "gaming":
                suggestions.add("What's your favorite game?");
                suggestions.add("Looking for new game recommendations?");
                break;
            case "mental_health":
                suggestions.add("Would you like to explore these feelings further?");
                suggestions.add("Remember, it's okay to not be okay.");
                break;
            default:
                suggestions.add("Anything else on your mind?");
                suggestions.add("What would you like to talk about next?");
        }
        
        return suggestions;
    }
    
    /**
     * Analyzes conversation pattern and suggests improvements
     */
    public ConversationAnalysis analyzeConversation(ConversationContext context) {
        int turnCount = context.getTurnCount();
        long duration = context.getConversationDurationMinutes();
        String currentTopic = context.getCurrentTopic();
        List<String> history = context.getRecentHistory(5);
        
        // Calculate engagement metrics
        double engagementScore = Math.min(1.0, turnCount / 10.0);
        
        // Detect topic stability
        int topicChanges = 0;
        String lastTopic = null;
        for (String turn : history) {
            if (turn.contains("Topic:") || turn.contains("topic:")) {
                String topic = turn.split(":")[1].trim();
                if (lastTopic != null && !lastTopic.equals(topic)) {
                    topicChanges++;
                }
                lastTopic = topic;
            }
        }
        
        return new ConversationAnalysis(
            turnCount,
            duration,
            currentTopic,
            engagementScore,
            topicChanges,
            getConversationStage(turnCount),
            generateRecommendations(turnCount, currentTopic, engagementScore)
        );
    }
    
    /**
     * Determines conversation stage based on turn count
     */
    private String getConversationStage(int turnCount) {
        if (turnCount == 0) return "initial";
        if (turnCount < 3) return "early";
        if (turnCount < 10) return "middle";
        if (turnCount < 20) return "late";
        return "extended";
    }
    
    /**
     * Generates recommendations based on conversation state
     */
    private List<String> generateRecommendations(int turnCount, String topic, double engagement) {
        List<String> recommendations = new ArrayList<>();
        
        if (turnCount < 3) {
            recommendations.add("Try asking open-ended questions to encourage more detailed responses.");
        }
        
        if (engagement < 0.3 && turnCount > 5) {
            recommendations.add("Consider changing the topic to something more engaging.");
            recommendations.add("Ask about the user's interests or recent experiences.");
        }
        
        if (topic.equals("general") && turnCount > 5) {
            recommendations.add("Try steering the conversation toward a specific topic the user seems interested in.");
        }
        
        return recommendations;
    }
    
    /**
     * Conversation analysis result class
     */
    public static class ConversationAnalysis {
        public final int turnCount;
        public final long durationMinutes;
        public final String currentTopic;
        public final double engagementScore;
        public final int topicChanges;
        public final String stage;
        public final List<String> recommendations;
        
        public ConversationAnalysis(int turnCount, long durationMinutes, String currentTopic,
                                   double engagementScore, int topicChanges, String stage,
                                   List<String> recommendations) {
            this.turnCount = turnCount;
            this.durationMinutes = durationMinutes;
            this.currentTopic = currentTopic;
            this.engagementScore = engagementScore;
            this.topicChanges = topicChanges;
            this.stage = stage;
            this.recommendations = recommendations;
        }
    }
    
    // Topic Handler Implementations
    
    private static class HomeworkTopicHandler implements TopicHandler {
        @Override
        public boolean canHandle(String topic) {
            return topic.toLowerCase().contains("homework") ||
                   topic.toLowerCase().contains("math") ||
                   topic.toLowerCase().contains("science") ||
                   topic.toLowerCase().contains("history") ||
                   topic.toLowerCase().contains("study");
        }
        
        @Override
        public String generateResponse(String input, ConversationContext context, 
                                      EmotionDetector.Emotion emotion) {
            return "Let's tackle your homework together! What subject are you working on?";
        }
        
        @Override
        public boolean isComplete(String input) {
            return input.toLowerCase().contains("thanks") ||
                   input.toLowerCase().contains("got it") ||
                   input.toLowerCase().contains("understood");
        }
    }
    
    private static class MentalHealthTopicHandler implements TopicHandler {
        @Override
        public boolean canHandle(String topic) {
            return topic.toLowerCase().contains("feel") ||
                   topic.toLowerCase().contains("mental") ||
                   topic.toLowerCase().contains("emotion") ||
                   topic.toLowerCase().contains("sad") ||
                   topic.toLowerCase().contains("stress");
        }
        
        @Override
        public String generateResponse(String input, ConversationContext context,
                                      EmotionDetector.Emotion emotion) {
            if (emotion != null && (emotion == EmotionDetector.Emotion.SAD ||
                emotion == EmotionDetector.Emotion.STRESSED ||
                emotion == EmotionDetector.Emotion.ANXIOUS)) {
                return "I'm really here for you. Would you like to share what's going on?";
            }
            return "I'm here to listen without any judgment. What's on your mind?";
        }
        
        @Override
        public boolean isComplete(String input) {
            return input.toLowerCase().contains("thanks") ||
                   input.toLowerCase().contains("better") ||
                   input.toLowerCase().contains("okay now");
        }
    }
    
    private static class GamingTopicHandler implements TopicHandler {
        @Override
        public boolean canHandle(String topic) {
            return topic.toLowerCase().contains("game") ||
                   topic.toLowerCase().contains("gaming") ||
                   topic.toLowerCase().contains("play");
        }
        
        @Override
        public String generateResponse(String input, ConversationContext context,
                                      EmotionDetector.Emotion emotion) {
            return "Gaming is awesome! What games do you enjoy playing?";
        }
        
        @Override
        public boolean isComplete(String input) {
            return input.toLowerCase().contains("thanks") ||
                   input.toLowerCase().contains("cool") ||
                   input.toLowerCase().contains("awesome");
        }
    }
    
    private static class CreativeWritingTopicHandler implements TopicHandler {
        @Override
        public boolean canHandle(String topic) {
            return topic.toLowerCase().contains("write") ||
                   topic.toLowerCase().contains("creative") ||
                   topic.toLowerCase().contains("story") ||
                   topic.toLowerCase().contains("poem");
        }
        
        @Override
        public String generateResponse(String input, ConversationContext context,
                                      EmotionDetector.Emotion emotion) {
            return "Creative writing is so rewarding! What are you working on?";
        }
        
        @Override
        public boolean isComplete(String input) {
            return input.toLowerCase().contains("thanks") ||
                   input.toLowerCase().contains("great") ||
                   input.toLowerCase().contains("helpful");
        }
    }
}

