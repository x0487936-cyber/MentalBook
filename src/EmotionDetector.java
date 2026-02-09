import java.util.*;
import java.util.regex.*;

/**
 * Emotion Detection System for VirtualXander
 * Detects and classifies emotional states from user input
 */
public class EmotionDetector {
    
    private Map<Emotion, List<Pattern>> emotionPatterns;
    private Map<String, Emotion> keywordEmotions;
    private Map<Emotion, List<String>> emotionResponses;
    private Random random;
    
    public EmotionDetector() {
        this.emotionPatterns = new HashMap<>();
        this.keywordEmotions = new HashMap<>();
        this.emotionResponses = new HashMap<>();
        this.random = new Random();
        initializeEmotionPatterns();
        initializeKeywordMappings();
        initializeEmotionResponses();
    }
    
    /**
     * Enum for supported emotions
     */
    public enum Emotion {
        HAPPY("happy", "Feeling joyful and positive"),
        EXCITED("excited", "Feeling enthusiastic and eager"),
        MOTIVATED("motivated", "Feeling driven and inspired"),
        GRATEFUL("grateful", "Feeling thankful and appreciative"),
        CONFIDENT("confident", "Feeling self-assured"),
        RELAXED("relaxed", "Feeling calm and at ease"),
        CURIOUS("curious", "Feeling interested and inquisitive"),
        CREATIVE("creative", "Feeling imaginative"),
        
        SAD("sad", "Feeling down or unhappy"),
        STRESSED("stressed", "Feeling overwhelmed or anxious"),
        ANXIOUS("anxious", "Feeling worried or nervous"),
        LONELY("lonely", "Feeling isolated"),
        ANGRY("angry", "Feeling frustrated or mad"),
        OVERWHELMED("overwhelmed", "Feeling swamped"),
        TIRED("tired", "Feeling exhausted"),
        BORED("bored", "Feeling uninterested"),
        CONFUSED("confused", "Feeling uncertain"),
        FRUSTRATED("frustrated", "Feeling stuck or annoyed"),
        SYMPATHY("sympathy", "Feeling compassion and understanding for others"),
        
        NEUTRAL("neutral", "Feeling balanced"),
        UNKNOWN("unknown", "Unable to determine emotion");
        
        private final String name;
        private final String description;
        
        Emotion(String name, String description) {
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
     * Detected emotion result class
     */
    public static class EmotionResult {
        private final Emotion primaryEmotion;
        private final double confidence;
        private final Map<Emotion, Double> allEmotions;
        private final boolean isNegative;
        private final boolean isPositive;
        
        public EmotionResult(Emotion primaryEmotion, double confidence, Map<Emotion, Double> allEmotions) {
            this.primaryEmotion = primaryEmotion;
            this.confidence = confidence;
            this.allEmotions = allEmotions;
            this.isNegative = isNegativeEmotion(primaryEmotion);
            this.isPositive = isPositiveEmotion(primaryEmotion);
        }
        
        private static boolean isNegativeEmotion(Emotion emotion) {
            return emotion == Emotion.SAD || emotion == Emotion.STRESSED ||
                   emotion == Emotion.ANXIOUS || emotion == Emotion.LONELY ||
                   emotion == Emotion.ANGRY || emotion == Emotion.OVERWHELMED ||
                   emotion == Emotion.TIRED || emotion == Emotion.BORED ||
                   emotion == Emotion.CONFUSED || emotion == Emotion.FRUSTRATED;
        }
        
        private static boolean isPositiveEmotion(Emotion emotion) {
            return emotion == Emotion.HAPPY || emotion == Emotion.EXCITED ||
                   emotion == Emotion.MOTIVATED || emotion == Emotion.GRATEFUL ||
                   emotion == Emotion.CONFIDENT || emotion == Emotion.RELAXED ||
                   emotion == Emotion.CURIOUS || emotion == Emotion.CREATIVE;
        }
        
        public Emotion getPrimaryEmotion() {
            return primaryEmotion;
        }
        
        public double getConfidence() {
            return confidence;
        }
        
        public Map<Emotion, Double> getAllEmotions() {
            return new HashMap<>(allEmotions);
        }
        
        public boolean isNegative() {
            return isNegative;
        }
        
        public boolean isPositive() {
            return isPositive;
        }
        
        public boolean isNeutral() {
            return primaryEmotion == Emotion.NEUTRAL;
        }
        
        public boolean isUnknown() {
            return primaryEmotion == Emotion.UNKNOWN;
        }
    }
    
    private void initializeEmotionPatterns() {
        // Happy patterns
        addEmotionPattern(Emotion.HAPPY, "\\b(happy|glad|joyful|joy|cheerful|content|satisfied|pleased|delighted|thrilled|elated)\\b");
        addEmotionPattern(Emotion.HAPPY, "\\b(i feel (good|great|awesome|wonderful|amazing|fantastic))\\b");
        addEmotionPattern(Emotion.HAPPY, "(making me smile|feeling good|feeling great)");
        
        // Excited patterns
        addEmotionPattern(Emotion.EXCITED, "\\b(excited|eager|thrilled|enthusiastic| pumped| can't wait)\\b");
        addEmotionPattern(Emotion.EXCITED, "\\b(looking forward|anticipating|psyched|amped)\\b");
        
        // Motivated patterns
        addEmotionPattern(Emotion.MOTIVATED, "\\b(motivated|inspired|driven|determined|energized)\\b");
        addEmotionPattern(Emotion.MOTIVATED, "\\b(ready to|going to|will power|ambitious)\\b");
        
        // Grateful patterns
        addEmotionPattern(Emotion.GRATEFUL, "\\b(grateful|thankful|appreciative|blessed|fortunate)\\b");
        addEmotionPattern(Emotion.GRATEFUL, "\\b(thank god|thankful for|appreciate)\\b");
        
        // Confident patterns
        addEmotionPattern(Emotion.CONFIDENT, "\\b(confident|sure|certain|assured|self-assured)\\b");
        addEmotionPattern(Emotion.CONFIDENT, "\\b(i can do|believe in|trust myself)\\b");
        
        // Relaxed patterns
        addEmotionPattern(Emotion.RELAXED, "\\b(relaxed|calm|peaceful|chilled|chill|at ease|serene)\\b");
        addEmotionPattern(Emotion.RELAXED, "\\b(taking it easy|winding down|unwinding)\\b");
        
        // Curious patterns
        addEmotionPattern(Emotion.CURIOUS, "\\b(curious|wondering|interested|inquisitive|want to know)\\b");
        addEmotionPattern(Emotion.CURIOUS, "\\b(what if|how does|why is|tell me about)\\b");
        
        // Creative patterns
        addEmotionPattern(Emotion.CREATIVE, "\\b(creative|imaginative|artistic|innovative)\\b");
        addEmotionPattern(Emotion.CREATIVE, "\\b(creating|making|building|designing|writing)\\b");
        
        // Sad patterns
        addEmotionPattern(Emotion.SAD, "\\b(sad|sadly|unhappy|miserable|down|blue|down in the dumps)\\b");
        addEmotionPattern(Emotion.SAD, "\\b(feeling (sad|down|blue|low|empty))\\b");
        addEmotionPattern(Emotion.SAD, "\\b(not (happy|good|great))\\b");
        
        // Stressed patterns
        addEmotionPattern(Emotion.STRESSED, "\\b(stressed|stressed out|pressure|under pressure)\\b");
        addEmotionPattern(Emotion.STRESSED, "\\b(so much|together too much|overloaded|swamped)\\b");
        
        // Anxious patterns
        addEmotionPattern(Emotion.ANXIOUS, "\\b(anxious|worried|nervous|uneasy|on edge)\\b");
        addEmotionPattern(Emotion.ANXIOUS, "\\b(what if|fear|scared|afraid|worried about)\\b");
        
        // Lonely patterns
        addEmotionPattern(Emotion.LONELY, "\\b(lonely|alone|isolated|ignored|unwanted)\\b");
        addEmotionPattern(Emotion.LONELY, "\\b(no one|nobody cares|feel alone)\\b");
        
        // Angry patterns
        addEmotionPattern(Emotion.ANGRY, "\\b(angry|mad|furious|irritated|annoyed|frustrated)\\b");
        addEmotionPattern(Emotion.ANGRY, "\\b(so annoying|drives me crazy|hate it)\\b");
        
        // Overwhelmed patterns
        addEmotionPattern(Emotion.OVERWHELMED, "\\b(overwhelmed|swamped|buried|drowning|too much)\\b");
        addEmotionPattern(Emotion.OVERWHELMED, "\\b(where to start|don't know where to begin)\\b");
        
        // Tired patterns
        addEmotionPattern(Emotion.TIRED, "\\b(tired|exhausted|weary|fatigued|drained|sleepy)\\b");
        addEmotionPattern(Emotion.TIRED, "\\b(need sleep|need rest|need a break)\\b");
        
        // Bored patterns
        addEmotionPattern(Emotion.BORED, "\\b(bored|boring|boredom|nothing to do)\\b");
        addEmotionPattern(Emotion.BORED, "\\b(need something|looking for|want to do)\\b");
        
        // Confused patterns
        addEmotionPattern(Emotion.CONFUSED, "\\b(confused|confusing|don't understand|lost|puzzled)\\b");
        addEmotionPattern(Emotion.CONFUSED, "\\b(don't get it|what does|how to)\\b");
        
        // Frustrated patterns
        addEmotionPattern(Emotion.FRUSTRATED, "\\b(frustrated|frustrating|stuck|can't)\\b");
        addEmotionPattern(Emotion.FRUSTRATED, "\\b(not working|won't|doesn't|why won't)\\b");
        
        // Sympathy patterns
        addEmotionPattern(Emotion.SYMPATHY, "\\b(sorry|feel sorry|feel bad for|pity|poor thing)\\b");
        addEmotionPattern(Emotion.SYMPATHY, "\\b(that's sad|that's terrible|that's awful|how sad)\\b");
        addEmotionPattern(Emotion.SYMPATHY, "\\b(i feel for|i sympathize|my heart goes out|thinking of)\\b");
    }
    
    private void initializeKeywordMappings() {
        // Direct keyword to emotion mappings (higher priority)
        keywordEmotions.put("happy", Emotion.HAPPY);
        keywordEmotions.put("sad", Emotion.SAD);
        keywordEmotions.put("excited", Emotion.EXCITED);
        keywordEmotions.put("stressed", Emotion.STRESSED);
        keywordEmotions.put("anxious", Emotion.ANXIOUS);
        keywordEmotions.put("lonely", Emotion.LONELY);
        keywordEmotions.put("angry", Emotion.ANGRY);
        keywordEmotions.put("overwhelmed", Emotion.OVERWHELMED);
        keywordEmotions.put("tired", Emotion.TIRED);
        keywordEmotions.put("bored", Emotion.BORED);
        keywordEmotions.put("motivated", Emotion.MOTIVATED);
        keywordEmotions.put("grateful", Emotion.GRATEFUL);
        keywordEmotions.put("confident", Emotion.CONFIDENT);
        keywordEmotions.put("relaxed", Emotion.RELAXED);
        keywordEmotions.put("curious", Emotion.CURIOUS);
        keywordEmotions.put("creative", Emotion.CREATIVE);
        keywordEmotions.put("confused", Emotion.CONFUSED);
        keywordEmotions.put("frustrated", Emotion.FRUSTRATED);
        keywordEmotions.put("sympathy", Emotion.SYMPATHY);
        keywordEmotions.put("sorry", Emotion.SYMPATHY);
        keywordEmotions.put("pity", Emotion.SYMPATHY);
    }
    
    private void initializeEmotionResponses() {
        // Responses when detecting positive emotions
        emotionResponses.put(Emotion.HAPPY, Arrays.asList(
            "That's wonderful to hear! What's making you feel so happy?",
            "I'm so glad you're feeling happy!",
            "Happiness is contagious! What's contributing to these good vibes?",
            "That's awesome! Keep that positive energy going!"
        ));
        
        emotionResponses.put(Emotion.EXCITED, Arrays.asList(
            "Excitement is great! What are you looking forward to?",
            "I love your enthusiasm! What's sparking this excitement?",
            "That's fantastic! Tell me more about what's got you so excited!",
            "Your excitement is contagious! What's happening?"
        ));
        
        emotionResponses.put(Emotion.MOTIVATED, Arrays.asList(
            "Feeling motivated is powerful! What inspired you?",
            "Great to hear you're feeling driven! Keep that momentum!",
            "Motivation is key to success! What's fueling your drive?",
            "I love that energy! What are you planning to accomplish?"
        ));
        
        emotionResponses.put(Emotion.GRATEFUL, Arrays.asList(
            "Gratitude is a beautiful quality! What are you thankful for?",
            "That's wonderful! Gratitude really enriches life.",
            "I appreciate your positive outlook! What sparked this gratitude?",
            "Thankfulness is so valuable! What are you grateful for?"
        ));
        
        // Responses when detecting negative emotions
        emotionResponses.put(Emotion.SAD, Arrays.asList(
            "I'm sorry you're feeling sad. Would you like to talk about it?",
            "Sadness can be tough. I'm here to listen if you want to share.",
            "I'm here to listen and support you. Sometimes it helps to talk about what's making you feel down.",
            "I hear you and I'm here for you. What's on your mind?"
        ));
        
        emotionResponses.put(Emotion.STRESSED, Arrays.asList(
            "Stress can be overwhelming. Take a deep breath - I'm here to help.",
            "I'm sorry you're feeling stressed. What's going on?",
            "Sometimes stress builds up. Would you like to talk about what's bothering you?",
            "I understand. Let's work through this together. What's stressing you out?"
        ));
        
        emotionResponses.put(Emotion.ANXIOUS, Arrays.asList(
            "Anxiety can be difficult. Remember, I'm here to support you.",
            "I hear you. What's making you feel anxious?",
            "It's okay to feel worried. Would you like to share what's on your mind?",
            "Deep breaths. What's troubling you?"
        ));
        
        emotionResponses.put(Emotion.LONELY, Arrays.asList(
            "Loneliness is hard. Remember, you're not alone - I'm here to chat.",
            "I hear you. Sometimes connection helps. What would you like to talk about?",
            "I'm sorry you're feeling lonely. Would you like some company?",
            "You matter! Let's chat about whatever's on your mind."
        ));
        
        emotionResponses.put(Emotion.ANGRY, Arrays.asList(
            "Anger is a natural emotion. Would you like to vent about it?",
            "I hear you're frustrated. What's making you feel this way?",
            "It's okay to feel angry. Sometimes it helps to talk about it.",
            "I understand you're upset. I'm here to listen."
        ));
        
        emotionResponses.put(Emotion.SYMPATHY, Arrays.asList(
            "I can see you really care about what happened. That's very compassionate of you.",
            "It's kind of you to feel that way. Shows you have a big heart.",
            "Having sympathy for others is a beautiful quality. It's okay to feel affected by others' struggles.",
            "Your empathy towards others is really meaningful. It's okay to share those feelings."
        ));
    }
    
    private void addEmotionPattern(Emotion emotion, String regex) {
        emotionPatterns.computeIfAbsent(emotion, k -> new ArrayList<>()).add(
            Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        );
    }
    
    /**
     * Detects the primary emotion from user input
     * @param input User's input text
     * @return EmotionResult containing detected emotion and confidence
     */
    public EmotionResult detectEmotion(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new EmotionResult(Emotion.NEUTRAL, 1.0, new HashMap<>());
        }
        
        String normalizedInput = input.toLowerCase().trim();
        Map<Emotion, Double> emotionScores = new HashMap<>();
        
        // Score each emotion based on pattern matches
        for (Map.Entry<Emotion, List<Pattern>> entry : emotionPatterns.entrySet()) {
            Emotion emotion = entry.getKey();
            double score = 0;
            
            for (Pattern pattern : entry.getValue()) {
                Matcher matcher = pattern.matcher(normalizedInput);
                if (matcher.find()) {
                    // Higher score for exact matches
                    String matched = matcher.group().toLowerCase();
                    if (matched.equals(normalizedInput)) {
                        score += 10;
                    } else if (normalizedInput.startsWith(matched) || 
                               normalizedInput.endsWith(matched)) {
                        score += 5;
                    } else {
                        score += 2;
                    }
                }
            }
            
            if (score > 0) {
                emotionScores.put(emotion, score);
            }
        }
        
        // Check for keyword matches (higher priority)
        String[] words = normalizedInput.split("\\s+");
        for (String word : words) {
            word = word.replaceAll("[^a-zA-Z]", "");
            if (keywordEmotions.containsKey(word)) {
                Emotion keywordEmotion = keywordEmotions.get(word);
                emotionScores.merge(keywordEmotion, 5.0, Double::sum);
            }
        }
        
        // Normalize scores to confidence values
        if (!emotionScores.isEmpty()) {
            double maxScore = emotionScores.values().stream().mapToDouble(Double::doubleValue).max().orElse(1.0);
            Map<Emotion, Double> normalizedScores = new HashMap<>();
            
            for (Map.Entry<Emotion, Double> entry : emotionScores.entrySet()) {
                normalizedScores.put(entry.getKey(), entry.getValue() / maxScore);
            }
            
            Emotion primaryEmotion = normalizedScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Emotion.NEUTRAL);
            
            double confidence = normalizedScores.getOrDefault(primaryEmotion, 0.5);
            
            return new EmotionResult(primaryEmotion, confidence, normalizedScores);
        }
        
        return new EmotionResult(Emotion.NEUTRAL, 1.0, new HashMap<>());
    }
    
    /**
     * Gets a supportive response based on detected emotion
     * @param emotion The detected emotion
     * @return A contextual supportive response
     */
    public String getSupportiveResponse(Emotion emotion) {
        List<String> responses = emotionResponses.get(emotion);
        if (responses != null && !responses.isEmpty()) {
            return responses.get(random.nextInt(responses.size()));
        }
        
        // Default responses by emotion type
        if (emotion == Emotion.NEUTRAL) {
            return "I see! Tell me more about what's on your mind.";
        }
        
        return "I'm here to listen and help. What's on your mind?";
    }
    
    /**
     * Gets all emotions sorted by detection score
     * @param input User's input text
     * @return List of emotions with their scores, sorted by confidence
     */
    public List<Map.Entry<Emotion, Double>> getAllEmotionsSorted(String input) {
        EmotionResult result = detectEmotion(input);
        List<Map.Entry<Emotion, Double>> sorted = new ArrayList<>(result.getAllEmotions().entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        return sorted;
    }
    
    /**
     * Checks if the detected emotion is mixed (multiple emotions detected)
     * @param result The emotion detection result
     * @return true if multiple emotions are detected
     */
    public boolean isMixedEmotion(EmotionResult result) {
        return result.getAllEmotions().size() > 1 && 
               result.getConfidence() < 0.8;
    }
    
    /**
     * Gets an emotion intensity description
     * @param confidence The confidence score (0-1)
     * @return Description of intensity
     */
    public String getIntensityDescription(double confidence) {
        if (confidence >= 0.9) return "very strongly";
        if (confidence >= 0.7) return "fairly strongly";
        if (confidence >= 0.5) return "moderately";
        if (confidence >= 0.3) return "somewhat";
        return "slightly";
    }
}

