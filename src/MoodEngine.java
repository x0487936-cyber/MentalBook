import java.util.*;
import java.util.regex.*;

/**
 * MoodEngine - Fine-grained Mood Detection System for VirtualXander
 * 
 * Provides advanced mood analysis capabilities including:
 * - Subtext Recognition: Detecting hidden meanings and implied emotions
 * - Sarcasm Detection: Identifying sarcastic vs genuine statements
 * - Undertone Analysis: Detecting underlying emotional tones
 * - Mood Intensity Calibration: Fine-tuning response based on emotional intensity
 * 
 * This complements the EmotionDetector which handles explicit emotion detection.
 */
public class MoodEngine {
    
    private EmotionDetector emotionDetector;
    private Random random;
    
    // Subtext patterns - detecting hidden meanings
    private List<SubtextPattern> subtextPatterns;
    
    // Sarcasm detection patterns
    private List<SarcasmPattern> sarcasmPatterns;
    
    // Undertone analysis components
    private Map<String, UndertoneIndicator> undertoneIndicators;
    
    // Intensity calibration
    private Map<String, Double> intensityModifiers;
    
    public MoodEngine() {
        this.emotionDetector = new EmotionDetector();
        this.random = new Random();
        initializeSubtextPatterns();
        initializeSarcasmPatterns();
        initializeUndertoneIndicators();
        initializeIntensityModifiers();
    }
    
    public MoodEngine(EmotionDetector detector) {
        this.emotionDetector = detector;
        this.random = new Random();
        initializeSubtextPatterns();
        initializeSarcasmPatterns();
        initializeUndertoneIndicators();
        initializeIntensityModifiers();
    }
    
    // ==================== INNER CLASSES ====================
    
    /**
     * Represents a subtext pattern - hidden meaning in text
     */
    public static class SubtextPattern {
        private String name;
        private Pattern pattern;
        private String hiddenEmotion;
        private double confidence;
        
        public SubtextPattern(String name, String regex, String hiddenEmotion, double confidence) {
            this.name = name;
            this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            this.hiddenEmotion = hiddenEmotion;
            this.confidence = confidence;
        }
        
        public String getName() { return name; }
        public Pattern getPattern() { return pattern; }
        public String getHiddenEmotion() { return hiddenEmotion; }
        public double getConfidence() { return confidence; }
    }
    
    /**
     * Represents a sarcasm pattern
     */
    public static class SarcasmPattern {
        private String name;
        private Pattern pattern;
        private String sarcasticMeaning;
        private double confidence;
        
        public SarcasmPattern(String name, String regex, String sarcasticMeaning, double confidence) {
            this.name = name;
            this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            this.sarcasticMeaning = sarcasticMeaning;
            this.confidence = confidence;
        }
        
        public String getName() { return name; }
        public Pattern getPattern() { return pattern; }
        public String getSarcasticMeaning() { return sarcasticMeaning; }
        public double getConfidence() { return confidence; }
    }
    
    /**
     * Represents an undertone indicator - underlying emotional tone
     */
    public static class UndertoneIndicator {
        private String surfaceEmotion;
        private String underlyingEmotion;
        private List<Pattern> indicators;
        private double detectionWeight;
        
        public UndertoneIndicator(String surface, String underlying, List<String> regexes, double weight) {
            this.surfaceEmotion = surface;
            this.underlyingEmotion = underlying;
            this.indicators = new ArrayList<>();
            for (String regex : regexes) {
                this.indicators.add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
            }
            this.detectionWeight = weight;
        }
        
        public String getSurfaceEmotion() { return surfaceEmotion; }
        public String getUnderlyingEmotion() { return underlyingEmotion; }
        public List<Pattern> getIndicators() { return indicators; }
        public double getDetectionWeight() { return detectionWeight; }
    }
    
    /**
     * Result of mood analysis
     */
    public static class MoodAnalysis {
        private String surfaceEmotion;
        private String underlyingEmotion;
        private double intensity;
        private boolean isSarcastic;
        private String sarcasmMeaning;
        private String subtext;
        private double confidence;
        private Map<String, Double> allUndertones;
        private String recommendedTone;
        
        public MoodAnalysis() {
            this.allUndertones = new HashMap<>();
        }
        
        public String getSurfaceEmotion() { return surfaceEmotion; }
        public void setSurfaceEmotion(String e) { this.surfaceEmotion = e; }
        public String getUnderlyingEmotion() { return underlyingEmotion; }
        public void setUnderlyingEmotion(String e) { this.underlyingEmotion = e; }
        public double getIntensity() { return intensity; }
        public void setIntensity(double i) { this.intensity = i; }
        public boolean isSarcastic() { return isSarcastic; }
        public void setSarcastic(boolean s) { this.isSarcastic = s; }
        public String getSarcasmMeaning() { return sarcasmMeaning; }
        public void setSarcasmMeaning(String m) { this.sarcasmMeaning = m; }
        public String getSubtext() { return subtext; }
        public void setSubtext(String s) { this.subtext = s; }
        public double getConfidence() { return confidence; }
        public void setConfidence(double c) { this.confidence = c; }
        public Map<String, Double> getAllUndertones() { return new HashMap<>(allUndertones); }
        public void addUndertone(String emotion, double weight) { this.allUndertones.put(emotion, weight); }
        public String getRecommendedTone() { return recommendedTone; }
        public void setRecommendedTone(String t) { this.recommendedTone = t; }
        
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("MoodAnalysis[");
            sb.append("surface=").append(surfaceEmotion);
            sb.append(", underlying=").append(underlyingEmotion);
            sb.append(", intensity=").append(intensity);
            sb.append(", sarcastic=").append(isSarcastic);
            if (isSarcastic) {
                sb.append(", sarcasmMeaning=").append(sarcasmMeaning);
            }
            sb.append(", subtext=").append(subtext);
            sb.append(", confidence=").append(confidence);
            sb.append("]");
            return sb.toString();
        }
    }
    
    /**
     * MoodMatcher - Matches VirtualXander's response to user's emotional state
     * 
     * Provides mood matching capabilities:
     * - Energy Matching: Match Xander's energy level to user's emotional intensity
     * - Intensity Calibration: Fine-tune response based on detected mood
     * - Appropriate Reactions: Generate contextually appropriate emotional responses
     */
    public class MoodMatcher {
        
        // Energy level mappings for different emotions
        private Map<String, EnergyLevel> emotionToEnergy;
        
        // Response intensity modifiers based on mood
        private Map<String, Double> responseIntensityModifiers;
        
        // Reaction templates for different emotional scenarios
        private Map<String, List<String>> reactionTemplates;
        
        public MoodMatcher() {
            initializeEmotionToEnergy();
            initializeResponseIntensityModifiers();
            initializeReactionTemplates();
        }
        
        /**
         * Energy level enum for matching user's emotional energy
         */
        public enum EnergyLevel {
            VERY_LOW("very_low", 0.0, 0.2),
            LOW("low", 0.2, 0.4),
            MODERATE("moderate", 0.4, 0.6),
            HIGH("high", 0.6, 0.8),
            VERY_HIGH("very_high", 0.8, 1.0);
            
            private final String name;
            private final double min;
            private final double max;
            
            EnergyLevel(String name, double min, double max) {
                this.name = name;
                this.min = min;
                this.max = max;
            }
            
            public String getName() { return name; }
            public double getMin() { return min; }
            public double getMax() { return max; }
            
            public static EnergyLevel fromIntensity(double intensity) {
                if (intensity < 0.2) return VERY_LOW;
                if (intensity < 0.4) return LOW;
                if (intensity < 0.6) return MODERATE;
                if (intensity < 0.8) return HIGH;
                return VERY_HIGH;
            }
        }
        
        /**
         * Result of mood matching
         */
        public static class MoodMatchResult {
            private EnergyLevel userEnergy;
            private EnergyLevel responseEnergy;
            private double intensityMultiplier;
            private String matchedTone;
            private String reaction;
            private List<String> suggestedPhrases;
            
            public MoodMatchResult() {
                this.suggestedPhrases = new ArrayList<>();
            }
            
            public EnergyLevel getUserEnergy() { return userEnergy; }
            public void setUserEnergy(EnergyLevel e) { this.userEnergy = e; }
            public EnergyLevel getResponseEnergy() { return responseEnergy; }
            public void setResponseEnergy(EnergyLevel e) { this.responseEnergy = e; }
            public double getIntensityMultiplier() { return intensityMultiplier; }
            public void setIntensityMultiplier(double m) { this.intensityMultiplier = m; }
            public String getMatchedTone() { return matchedTone; }
            public void setMatchedTone(String t) { this.matchedTone = t; }
            public String getReaction() { return reaction; }
            public void setReaction(String r) { this.reaction = r; }
            public List<String> getSuggestedPhrases() { return new ArrayList<>(suggestedPhrases); }
            public void addSuggestedPhrase(String p) { this.suggestedPhrases.add(p); }
        }
        
        // ==================== INITIALIZATION ====================
        
        private void initializeEmotionToEnergy() {
            emotionToEnergy = new HashMap<>();
            
            // Very low energy emotions
            emotionToEnergy.put("tired", EnergyLevel.VERY_LOW);
            emotionToEnergy.put("bored", EnergyLevel.LOW);
            emotionToEnergy.put("neutral", EnergyLevel.LOW);
            
            // Low energy emotions
            emotionToEnergy.put("relaxed", EnergyLevel.LOW);
            emotionToEnergy.put("calm", EnergyLevel.LOW);
            emotionToEnergy.put("sad", EnergyLevel.LOW);
            
            // Moderate energy emotions
            emotionToEnergy.put("content", EnergyLevel.MODERATE);
            emotionToEnergy.put("happy", EnergyLevel.MODERATE);
            emotionToEnergy.put("confused", EnergyLevel.MODERATE);
            emotionToEnergy.put("curious", EnergyLevel.MODERATE);
            
            // High energy emotions
            emotionToEnergy.put("excited", EnergyLevel.HIGH);
            emotionToEnergy.put("motivated", EnergyLevel.HIGH);
            emotionToEnergy.put("grateful", EnergyLevel.HIGH);
            emotionToEnergy.put("confident", EnergyLevel.HIGH);
            emotionToEnergy.put("inspired", EnergyLevel.HIGH);
            emotionToEnergy.put("proud", EnergyLevel.HIGH);
            
            // Very high energy emotions
            emotionToEnergy.put("angry", EnergyLevel.VERY_HIGH);
            emotionToEnergy.put("anxious", EnergyLevel.VERY_HIGH);
            emotionToEnergy.put("stressed", EnergyLevel.VERY_HIGH);
            emotionToEnergy.put("overwhelmed", EnergyLevel.VERY_HIGH);
            emotionToEnergy.put("amused", EnergyLevel.HIGH);
            emotionToEnergy.put("frustrated", EnergyLevel.HIGH);
        }
        
        private void initializeResponseIntensityModifiers() {
            responseIntensityModifiers = new HashMap<>();
            
            // Increase intensity for high-energy negative emotions
            responseIntensityModifiers.put("angry", 1.2);
            responseIntensityModifiers.put("frustrated", 1.15);
            responseIntensityModifiers.put("stressed", 1.1);
            responseIntensityModifiers.put("anxious", 1.1);
            
            // Decrease intensity for low-energy emotions
            responseIntensityModifiers.put("tired", 0.7);
            responseIntensityModifiers.put("bored", 0.8);
            responseIntensityModifiers.put("sad", 0.85);
            
            // Moderate for positive emotions
            responseIntensityModifiers.put("happy", 1.0);
            responseIntensityModifiers.put("excited", 1.05);
            responseIntensityModifiers.put("grateful", 0.95);
            responseIntensityModifiers.put("confident", 0.95);
            
            // Neutral base
            responseIntensityModifiers.put("neutral", 1.0);
            responseIntensityModifiers.put("relaxed", 0.9);
            responseIntensityModifiers.put("calm", 0.85);
        }
        
        private void initializeReactionTemplates() {
            reactionTemplates = new HashMap<>();
            
            // High energy positive reactions
            reactionTemplates.put("excited", Arrays.asList(
                "That's fantastic!",
                "Wow, that's exciting!",
                "I love your energy!",
                "That's really cool!"
            ));
            
            reactionTemplates.put("happy", Arrays.asList(
                "That's great to hear!",
                "I love that for you!",
                "That makes me happy too!",
                "Wonderful!"
            ));
            
            reactionTemplates.put("proud", Arrays.asList(
                "You should be proud!",
                "That's impressive!",
                "You've got every reason to feel proud!",
                "Well deserved!"
            ));
            
            reactionTemplates.put("grateful", Arrays.asList(
                "That's beautiful!",
                "Gratitude is such a wonderful quality.",
                "That's really meaningful.",
                "I appreciate you sharing that."
            ));
            
            // Moderate energy reactions
            reactionTemplates.put("curious", Arrays.asList(
                "That's an interesting question!",
                "I like that you're exploring this.",
                "Good thinking!",
                "Let me share what I know."
            ));
            
            reactionTemplates.put("confused", Arrays.asList(
                "I can see why that would be confusing.",
                "That's a fair question.",
                "Let me help clarify.",
                "That's understandable - it can be tricky."
            ));
            
            // Low energy reactions
            reactionTemplates.put("sad", Arrays.asList(
                "I'm here with you.",
                "I hear you.",
                "That sounds really tough.",
                "I'm sorry you're going through this."
            ));
            
            reactionTemplates.put("tired", Arrays.asList(
                "Sounds like you need rest.",
                "Take care of yourself.",
                "I hope you can get some rest soon.",
                "Be gentle with yourself."
            ));
            
            reactionTemplates.put("bored", Arrays.asList(
                "Want to chat about something interesting?",
                "I can help with that boredom!",
                "What would you like to explore?",
                "Let's find something engaging!"
            ));
            
            // High energy negative reactions
            reactionTemplates.put("angry", Arrays.asList(
                "I can hear that you're really upset.",
                "That's frustrating!",
                "I understand why you're angry.",
                "That's not okay."
            ));
            
            reactionTemplates.put("stressed", Arrays.asList(
                "I hear you - there's a lot on your plate.",
                "That sounds overwhelming.",
                "I'm here to help you through this.",
                "Take it one step at a time."
            ));
            
            reactionTemplates.put("anxious", Arrays.asList(
                "I can sense your worry.",
                "That's understandable to feel anxious.",
                "Let's work through this together.",
                "I'm here with you."
            ));
            
            // Very low energy reactions
            reactionTemplates.put("neutral", Arrays.asList(
                "I see.",
                "Got it.",
                "Alright.",
                "I understand."
            ));
            
            reactionTemplates.put("relaxed", Arrays.asList(
                "Sounds nice and peaceful.",
                "That sounds calming.",
                "I appreciate the chill vibe.",
                "Nice and easy."
            ));
        }
        
        // ==================== PUBLIC METHODS ====================
        
        /**
         * Matches Xander's energy to user's emotional state
         * @param analysis The mood analysis result
         * @return MoodMatchResult with matched energy level
         */
        public MoodMatchResult matchEnergy(MoodAnalysis analysis) {
            MoodMatchResult result = new MoodMatchResult();
            
            // Determine user's energy level from intensity
            EnergyLevel userEnergy = EnergyLevel.fromIntensity(analysis.getIntensity());
            
            // For emotions with known energy mappings, use those
            String emotionKey = analysis.getSurfaceEmotion().toLowerCase();
            if (emotionToEnergy.containsKey(emotionKey)) {
                userEnergy = emotionToEnergy.get(emotionKey);
            }
            
            result.setUserEnergy(userEnergy);
            
            // Match response energy (slightly lower for negative emotions, same for positive)
            EnergyLevel responseEnergy = userEnergy;
            
            if (isNegativeEmotion(emotionKey)) {
                // For negative emotions, match but don't amplify
                responseEnergy = userEnergy;
            } else if (isPositiveEmotion(emotionKey)) {
                // For positive emotions, match the energy
                responseEnergy = userEnergy;
            }
            
            result.setResponseEnergy(responseEnergy);
            
            // Set intensity multiplier
            double modifier = responseIntensityModifiers.getOrDefault(emotionKey, 1.0);
            result.setIntensityMultiplier(modifier);
            
            // Set matched tone
            result.setMatchedTone(determineMatchedTone(analysis, responseEnergy));
            
            // Set reaction
            result.setReaction(generateReaction(analysis));
            
            // Add suggested phrases
            List<String> phrases = reactionTemplates.get(emotionKey);
            if (phrases != null && !phrases.isEmpty()) {
                // Add 2 random phrases
                List<String> shuffledPhrases = new ArrayList<>(phrases);
                Collections.shuffle(shuffledPhrases, random);
                result.addSuggestedPhrase(shuffledPhrases.get(0));
                if (shuffledPhrases.size() > 1) {
                    result.addSuggestedPhrase(shuffledPhrases.get(1));
                }
            }
            
            return result;
        }
        
        /**
         * Calibrates response intensity based on mood analysis
         * @param baseIntensity Base response intensity
         * @param analysis The mood analysis result
         * @return Calibrated intensity value
         */
        public double calibrateIntensity(double baseIntensity, MoodAnalysis analysis) {
            String emotionKey = analysis.getSurfaceEmotion().toLowerCase();
            
            // Get emotion-specific modifier
            double modifier = responseIntensityModifiers.getOrDefault(emotionKey, 1.0);
            
            // Apply intensity-based adjustment
            double intensityAdjust = 0.0;
            if (analysis.getIntensity() > 0.7) {
                intensityAdjust = 0.1; // Boost for high intensity
            } else if (analysis.getIntensity() < 0.3) {
                intensityAdjust = -0.1; // Reduce for low intensity
            }
            
            // Check for subtext (hidden emotions might need more careful handling)
            if (!"none".equals(analysis.getSubtext())) {
                modifier *= 0.9; // Be more moderate when there's subtext
            }
            
            // Check for sarcasm
            if (analysis.isSarcastic()) {
                modifier *= 0.85; // Be more measured with sarcasm
            }
            
            // Calculate final intensity
            double calibratedIntensity = (baseIntensity + intensityAdjust) * modifier;
            
            // Clamp to valid range
            return Math.max(0.1, Math.min(1.0, calibratedIntensity));
        }
        
        /**
         * Generates an appropriate emotional reaction based on mood analysis
         * @param analysis The mood analysis result
         * @return A matching reaction string
         */
        public String appropriateReaction(MoodAnalysis analysis) {
            return generateReaction(analysis);
        }
        
        /**
         * Gets the matched response tone based on mood analysis
         * @param analysis The mood analysis result
         * @return A response tone string
         */
        public String getMatchedTone(MoodAnalysis analysis) {
            EnergyLevel energy = EnergyLevel.fromIntensity(analysis.getIntensity());
            return determineMatchedTone(analysis, energy);
        }
        
        // ==================== PRIVATE HELPER METHODS ====================
        
        private String generateReaction(MoodAnalysis analysis) {
            String emotionKey = analysis.getSurfaceEmotion().toLowerCase();
            
            // First check for specific templates
            List<String> templates = reactionTemplates.get(emotionKey);
            if (templates != null && !templates.isEmpty()) {
                return templates.get(random.nextInt(templates.size()));
            }
            
            // Handle underlying emotion if different from surface
            if (!"none".equals(analysis.getUnderlyingEmotion()) && 
                !analysis.getUnderlyingEmotion().equals(analysis.getSurfaceEmotion())) {
                String underlyingKey = analysis.getUnderlyingEmotion().toLowerCase();
                templates = reactionTemplates.get(underlyingKey);
                if (templates != null && !templates.isEmpty()) {
                    return templates.get(random.nextInt(templates.size()));
                }
            }
            
            // Handle sarcasm
            if (analysis.isSarcastic()) {
                List<String> sarcasmReactions = Arrays.asList(
                    "I hear you.",
                    "Got it.",
                    "I understand.",
                    "Hmm, interesting."
                );
                return sarcasmReactions.get(random.nextInt(sarcasmReactions.size()));
            }
            
            // Handle subtext
            if (!"none".equals(analysis.getSubtext())) {
                List<String> subtextReactions = Arrays.asList(
                    "I sense there might be more to this.",
                    "I'm here if you want to share more.",
                    "Tell me more about that.",
                    "I'm listening."
                );
                return subtextReactions.get(random.nextInt(subtextReactions.size()));
            }
            
            // Default neutral reaction
            return "I understand.";
        }
        
        private String determineMatchedTone(MoodAnalysis analysis, EnergyLevel energy) {
            String emotionKey = analysis.getSurfaceEmotion().toLowerCase();
            
            // Check for sarcasm first
            if (analysis.isSarcastic()) {
                return "understanding";
            }
            
            // Check for subtext
            if (!"none".equals(analysis.getSubtext())) {
                return "attentive";
            }
            
            // Check for underlying emotion
            if (!"none".equals(analysis.getUnderlyingEmotion()) && 
                !analysis.getUnderlyingEmotion().equals(analysis.getSurfaceEmotion())) {
                return "empathetic";
            }
            
            // Match based on emotion and energy
            if (isPositiveEmotion(emotionKey)) {
                switch (energy) {
                    case VERY_HIGH:
                    case HIGH:
                        return "enthusiastic";
                    case MODERATE:
                        return "warm";
                    case LOW:
                    case VERY_LOW:
                        return "gentle";
                }
            } else if (isNegativeEmotion(emotionKey)) {
                switch (energy) {
                    case VERY_HIGH:
                    case HIGH:
                        return "concerned";
                    case MODERATE:
                        return "supportive";
                    case LOW:
                    case VERY_LOW:
                        return "soothing";
                }
            }
            
            return "neutral";
        }
        
        private boolean isPositiveEmotion(String emotion) {
            Set<String> positiveEmotions = new HashSet<>(Arrays.asList(
                "happy", "excited", "motivated", "grateful", "confident", "relaxed",
                "curious", "creative", "hopeful", "proud", "amused", "inspired",
                "content", "pleased", "joyful", "delighted"
            ));
            return positiveEmotions.contains(emotion.toLowerCase());
        }
        
        private boolean isNegativeEmotion(String emotion) {
            Set<String> negativeEmotions = new HashSet<>(Arrays.asList(
                "sad", "stressed", "anxious", "lonely", "angry", "overwhelmed",
                "tired", "bored", "confused", "frustrated", "disappointed",
                "embarrassed", "worried", "hurt", "hopeless", "resentful"
            ));
            return negativeEmotions.contains(emotion.toLowerCase());
        }
        
        /**
         * Gets the appropriate response type based on mood matching
         * @param analysis The mood analysis
         * @return Response tone as string matching the mood
         */
        public String getResponseType(MoodAnalysis analysis) {
            MoodMatchResult match = matchEnergy(analysis);
            String tone = match.getMatchedTone();
            
            switch (tone) {
                case "enthusiastic":
                    return "enthusiastic";
                case "warm":
                    return "warm";
                case "gentle":
                    return "soothing";
                case "concerned":
                    return "concerned";
                case "supportive":
                    return "supportive";
                case "soothing":
                    return "soothing";
                case "empathetic":
                    return "understanding";
                case "attentive":
                    return "attentive";
                case "understanding":
                    return "understanding";
                default:
                    return "neutral";
            }
        }
    }
    
    // ==================== INITIALIZATION
    
    private void initializeSubtextPatterns() {
        subtextPatterns = new ArrayList<>();
        
        // Dismissive subtext - "I'm fine" but actually not fine
        subtextPatterns.add(new SubtextPattern(
            "dismissive_fine",
            "^(i'm fine|i am fine|i'm okay|i am okay|it's fine|its fine|i'm good|i am good)$",
            "dismissive_unhappy",
            0.7
        ));
        
        // Deflection - changing subject to avoid topic
        subtextPatterns.add(new SubtextPattern(
            "deflection",
            "(never mind|don't worry about it|forget i said|anyway|changing subject)",
            "avoidance",
            0.6
        ));
        
        // Passive aggression hints
        subtextPatterns.add(new SubtextPattern(
            "passive_aggressive",
            "(oh great|wonderful|that's just perfect|nice really nice)",
            "frustrated_annoyed",
            0.65
        ));
        
        // Masking sadness with humor
        subtextPatterns.add(new SubtextPattern(
            "humor_masking_sadness",
            "(ha ha|lol|lmao|joking aside|but seriously|i'm kidding)",
            "sad_but_hiding",
            0.6
        ));
        
        // Minimizing pain
        subtextPatterns.add(new SubtextPattern(
            "minimizing",
            "(it's not a big deal|it's fine|i'm not that upset|i don't really care)",
            "truly_bothered",
            0.55
        ));
        
        // Overcompensating with enthusiasm
        subtextPatterns.add(new SubtextPattern(
            "enthusiasm_overcompensation",
            "\\b(yay|woo|oh boy|here we go|can't wait|so excited)\\b",
            "anxious_excited",
            0.5
        ));
        
        // Stoic facade
        subtextPatterns.add(new SubtextPattern(
            "stoic_facade",
            "(whatever|i don't care|doesn't matter|not bothered)",
            "actually_cares_deeply",
            0.6
        ));
        
        // Seeking reassurance
        subtextPatterns.add(new SubtextPattern(
            "seeking_reassurance",
            "(do you think|is it just me|am i wrong|right|you agree)",
            "insecure_uncertain",
            0.65
        ));
        
        // Hopelessness hints
        subtextPatterns.add(new SubtextPattern(
            "hopelessness",
            "(what's the point|doesn't matter|i give up|nothing works)",
            "deeply_frustrated",
            0.7
        ));
        
        // Social mask
        subtextPatterns.add(new SubtextPattern(
            "social_mask",
            "(everyone's fine|things are great|i'm doing well|i'm good)",
            "struggling_silently",
            0.6
        ));
    }
    
    private void initializeSarcasmPatterns() {
        sarcasmPatterns = new ArrayList<>();
        
        // Classic sarcasm with extreme positive words
        sarcasmPatterns.add(new SarcasmPattern(
            "extreme_positive_sarcasm",
            "\\b(oh wow|oh lovely|oh perfect|how wonderful|how fantastic|that's amazing|great job)\\b",
            "actually_negative",
            0.75
        ));
        
        // Eye-roll sarcasm
        sarcasmPatterns.add(new SarcasmPattern(
            "eyeroll_sarcasm",
            "(yeah right|sure|sure sure|obviously|clearly|obviously|of course)",
            "disbelief",
            0.7
        ));
        
        // Self-deprecating sarcasm
        sarcasmPatterns.add(new SarcasmPattern(
            "self_deprecating_sarcasm",
            "(oh yay me|way to go genius|nice one|stupid me|bravo|clap clap)",
            "self_critical",
            0.65
        ));
        
        // Dramatic sarcasm
        sarcasmPatterns.add(new SarcasmPattern(
            "dramatic_sarcasm",
            "(oh joy|oh really|wonderful|yippee|how delightful)",
            "ironic_displeasure",
            0.7
        ));
        
        // Passive aggressive sarcasm
        sarcasmPatterns.add(new SarcasmPattern(
            "passive_aggressive_sarcasm",
            "(how nice|how sweet|how lovely for you|that's great for you)",
            "jealous_annoyed",
            0.65
        ));
        
        // "Sure" patterns
        sarcasmPatterns.add(new SarcasmPattern(
            "sure_sarcasm",
            "\\bsure\\b.*\\b(whatever|you said|if you say so|i believe you)",
            "disbelief",
            0.7
        ));
        
        // Obviously sarcasm
        sarcasmPatterns.add(new SarcasmPattern(
            "obviously_sarcasm",
            "(obviously|clearly|as if|right)",
            "frustrated",
            0.6
        ));
    }
    
    private void initializeUndertoneIndicators() {
        undertoneIndicators = new HashMap<>();
        
        // Happy surface, sad underlying
        undertoneIndicators.put("happy_sad", new UndertoneIndicator(
            "happy",
            "sad",
            Arrays.asList(
                "(i'm trying to stay positive|keeping busy|not thinking about it|staying strong)",
                "(but|however|though).*(sad|down|lonely|miss)",
                "(\\bsad|\\bdown|\\bblue).*but"
            ),
            0.7
        ));
        
        // Happy surface, anxious underlying
        undertoneIndicators.put("happy_anxious", new UndertoneIndicator(
            "happy",
            "anxious",
            Arrays.asList(
                "(nervous but|i'm fine but|excited but|can't stop thinking)",
                "(what if|butterflies|racing thoughts|can't relax)",
                "(\\banxious|\\bworried|\\bnervous).*but"
            ),
            0.65
        ));
        
        // Calm surface, stressed underlying
        undertoneIndicators.put("calm_stressed", new UndertoneIndicator(
            "calm",
            "stressed",
            Arrays.asList(
                "(calm on the outside|looking peaceful but|seems fine but)",
                "(falling apart|falling to pieces|breaking down).*inside",
                "(\\bstressed|\\boverwhelmed|\\bpressure).*hidden"
            ),
            0.6
        ));
        
        // Excited surface, worried underlying
        undertoneIndicators.put("excited_worried", new UndertoneIndicator(
            "excited",
            "worried",
            Arrays.asList(
                "(excited but|can't wait but|looking forward but)",
                "(but what if|hoping it goes|pray it works)",
                "(\\bworried|\\bnervous|\\bhoping).*excited"
            ),
            0.65
        ));
        
        // Confident surface, insecure underlying
        undertoneIndicators.put("confident_insecure", new UndertoneIndicator(
            "confident",
            "insecure",
            Arrays.asList(
                "(seems confident but|i look confident but|acting brave)",
                "(but what if i fail|but maybe not|uncertain deep down)",
                "(\\binsecure|\\bdoubt|\\buncertain).*confident"
            ),
            0.6
        ));
        
        // Grateful surface, resentful underlying
        undertoneIndicators.put("grateful_resentful", new UndertoneIndicator(
            "grateful",
            "resentful",
            Arrays.asList(
                "(thankful but|i should be grateful but|grateful anyway)",
                "(but it's not fair|why them|why not me)",
                "(\\bresent|\\bunfair|\\bjealous).*grateful"
            ),
            0.55
        ));
        
        // Relaxed surface, frustrated underlying
        undertoneIndicators.put("relaxed_frustrated", new UndertoneIndicator(
            "relaxed",
            "frustrated",
            Arrays.asList(
                "(taking it easy but|chilling but|unwinding but)",
                "(but it's annoying|but i hate|still bothered)",
                "(\\bfrustrated|\\bannoyed|\\bbothered).*relaxed"
            ),
            0.6
        ));
        
        // Hopeful surface, desperate underlying
        undertoneIndicators.put("hopeful_desperate", new UndertoneIndicator(
            "hopeful",
            "desperate",
            Arrays.asList(
                "(hoping for the best|keeping hope|stay positive)",
                "(but i need|i have to|please|someone please)",
                "(\\bdesperate|\\bneed help|\\bplease).*hopeful"
            ),
            0.65
        ));
    }
    
    private void initializeIntensityModifiers() {
        intensityModifiers = new HashMap<>();
        
        // Intensifiers that increase intensity
        intensityModifiers.put("really", 0.2);
        intensityModifiers.put("so", 0.15);
        intensityModifiers.put("very", 0.15);
        intensityModifiers.put("extremely", 0.25);
        intensityModifiers.put("absolutely", 0.25);
        intensityModifiers.put("totally", 0.2);
        intensityModifiers.put("completely", 0.2);
        intensityModifiers.put("incredibly", 0.25);
        intensityModifiers.put("super", 0.15);
        intensityModifiers.put("utterly", 0.25);
        
        // Diminisher words that decrease intensity
        intensityModifiers.put("kind of", -0.15);
        intensityModifiers.put("sort of", -0.15);
        intensityModifiers.put("a bit", -0.15);
        intensityModifiers.put("a little", -0.15);
        intensityModifiers.put("somewhat", -0.1);
        intensityModifiers.put("slightly", -0.2);
        intensityModifiers.put("barely", -0.25);
        intensityModifiers.put("hardly", -0.25);
        intensityModifiers.put("not really", -0.2);
        intensityModifiers.put("not that", -0.2);
    }
    
    // ==================== PUBLIC METHODS ====================
    
    /**
     * Performs comprehensive mood analysis including subtext, sarcasm, undertones, and intensity
     * @param input User's input text
     * @return MoodAnalysis with detailed mood information
     */
    public MoodAnalysis analyzeMood(String input) {
        MoodAnalysis analysis = new MoodAnalysis();
        
        if (input == null || input.trim().isEmpty()) {
            analysis.setSurfaceEmotion("neutral");
            analysis.setUnderlyingEmotion("neutral");
            analysis.setIntensity(0.0);
            analysis.setConfidence(1.0);
            analysis.setRecommendedTone("neutral");
            return analysis;
        }
        
        String normalizedInput = input.toLowerCase().trim();
        
        // 1. First get surface emotion from EmotionDetector
        EmotionDetector.EmotionResult emotionResult = emotionDetector.detectEmotion(normalizedInput);
        analysis.setSurfaceEmotion(emotionResult.getPrimaryEmotion().getName());
        
        // 2. Detect subtext
        String subtext = detectSubtext(normalizedInput);
        analysis.setSubtext(subtext);
        
        // 3. Detect sarcasm
        boolean isSarcastic = detectSarcasm(normalizedInput);
        analysis.setSarcastic(isSarcastic);
        if (isSarcastic) {
            String sarcasmMeaning = getSarcasmMeaning(normalizedInput);
            analysis.setSarcasmMeaning(sarcasmMeaning);
        }
        
        // 4. Analyze undertones
        String underlyingEmotion = analyzeUndertone(normalizedInput, analysis);
        analysis.setUnderlyingEmotion(underlyingEmotion);
        
        // 5. Calculate intensity
        double intensity = calculateIntensity(normalizedInput, emotionResult.getConfidence());
        analysis.setIntensity(intensity);
        
        // 6. Calculate overall confidence
        double confidence = calculateConfidence(analysis, emotionResult);
        analysis.setConfidence(confidence);
        
        // 7. Determine recommended tone for response
        String recommendedTone = determineRecommendedTone(analysis);
        analysis.setRecommendedTone(recommendedTone);
        
        return analysis;
    }
    
    /**
     * Detects subtext - hidden meaning in the input
     * @param input Normalized input text
     * @return Description of detected subtext
     */
    public String detectSubtext(String input) {
        for (SubtextPattern pattern : subtextPatterns) {
            Matcher matcher = pattern.getPattern().matcher(input);
            if (matcher.find()) {
                return pattern.getHiddenEmotion();
            }
        }
        return "none";
    }
    
    /**
     * Detects if the input is sarcastic
     * @param input Normalized input text
     * @return true if sarcasm is detected
     */
    public boolean detectSarcasm(String input) {
        for (SarcasmPattern pattern : sarcasmPatterns) {
            Matcher matcher = pattern.getPattern().matcher(input);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets the actual meaning behind sarcastic input
     * @param input Normalized input text
     * @return The actual meaning behind the sarcasm
     */
    public String getSarcasmMeaning(String input) {
        for (SarcasmPattern pattern : sarcasmPatterns) {
            Matcher matcher = pattern.getPattern().matcher(input);
            if (matcher.find()) {
                return pattern.getSarcasticMeaning();
            }
        }
        return "unknown";
    }
    
    /**
     * Analyzes underlying emotional tone
     * @param input Normalized input text
     * @param analysis Current mood analysis
     * @return The underlying emotion
     */
    public String analyzeUndertone(String input, MoodAnalysis analysis) {
        Map<String, Double> undertoneScores = new HashMap<>();
        
        for (Map.Entry<String, UndertoneIndicator> entry : undertoneIndicators.entrySet()) {
            UndertoneIndicator indicator = entry.getValue();
            
            // Only check if surface emotion matches
            if (!indicator.getSurfaceEmotion().equalsIgnoreCase(analysis.getSurfaceEmotion())) {
                continue;
            }
            
            for (Pattern pattern : indicator.getIndicators()) {
                Matcher matcher = pattern.matcher(input);
                if (matcher.find()) {
                    undertoneScores.merge(indicator.getUnderlyingEmotion(), 
                        indicator.getDetectionWeight(), Double::sum);
                    analysis.addUndertone(indicator.getUnderlyingEmotion(), 
                        indicator.getDetectionWeight());
                }
            }
        }
        
        // Return the highest scoring underlying emotion
        if (!undertoneScores.isEmpty()) {
            return undertoneScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("none");
        }
        
        return "none";
    }
    
    /**
     * Calculates emotional intensity with modifiers
     * @param input Normalized input text
     * @param baseConfidence Base confidence from emotion detection
     * @return Intensity value between 0.0 and 1.0
     */
    public double calculateIntensity(String input, double baseConfidence) {
        double intensity = baseConfidence;
        
        // Check for intensifiers and diminishers
        for (Map.Entry<String, Double> entry : intensityModifiers.entrySet()) {
            if (input.contains(entry.getKey())) {
                intensity += entry.getValue();
            }
        }
        
        // Clamp to valid range
        return Math.max(0.0, Math.min(1.0, intensity));
    }
    
    /**
     * Calculates overall confidence in the mood analysis
     * @param analysis Current mood analysis
     * @param emotionResult Result from emotion detector
     * @return Confidence value between 0.0 and 1.0
     */
    public double calculateConfidence(MoodAnalysis analysis, EmotionDetector.EmotionResult emotionResult) {
        double confidence = emotionResult.getConfidence();
        
        // Decrease confidence if subtext detected (hidden meaning)
        if (!"none".equals(analysis.getSubtext())) {
            confidence *= 0.85;
        }
        
        // Decrease confidence if sarcasm detected
        if (analysis.isSarcastic()) {
            confidence *= 0.75;
        }
        
        // Increase confidence if underlying emotion detected
        if (!"none".equals(analysis.getUnderlyingEmotion())) {
            confidence *= 0.9;
        }
        
        return confidence;
    }
    
    /**
     * Determines the recommended tone for responding
     * @param analysis Current mood analysis
     * @return Recommended tone for response
     */
    public String determineRecommendedTone(MoodAnalysis analysis) {
        // If sarcasm detected, match with understanding
        if (analysis.isSarcastic()) {
            return "understanding";
        }
        
        // If underlying emotion is different from surface, address underlying
        if (!"none".equals(analysis.getUnderlyingEmotion()) && 
            !analysis.getUnderlyingEmotion().equals(analysis.getSurfaceEmotion())) {
            return "empathetic";
        }
        
        // If subtext detected, be attentive
        if (!"none".equals(analysis.getSubtext())) {
            return "attentive";
        }
        
        // Match intensity
        if (analysis.getIntensity() > 0.7) {
            return "intense";
        } else if (analysis.getIntensity() < 0.3) {
            return "gentle";
        }
        
        // Default to surface emotion's tone
        return analysis.getSurfaceEmotion();
    }
    
    /**
     * Gets intensity description
     * @param intensity Intensity value
     * @return Human-readable intensity description
     */
    public String getIntensityDescription(double intensity) {
        if (intensity >= 0.9) return "very_intense";
        if (intensity >= 0.7) return "intense";
        if (intensity >= 0.5) return "moderate";
        if (intensity >= 0.3) return "mild";
        return "subtle";
    }
    
    /**
     * Gets suggested response approach based on mood analysis
     * @param analysis Current mood analysis
     * @return Suggested approach for response
     */
    public String getSuggestedApproach(MoodAnalysis analysis) {
        if (analysis.isSarcastic()) {
            return "Acknowledge the sarcasm gently without being dismissive. Let them know you understand the real sentiment behind the words.";
        }
        
        if (!"none".equals(analysis.getUnderlyingEmotion()) && 
            !analysis.getUnderlyingEmotion().equals(analysis.getSurfaceEmotion())) {
            return "The underlying emotion (" + analysis.getUnderlyingEmotion() + 
                   ") seems more significant than the surface expression (" + 
                   analysis.getSurfaceEmotion() + "). Address the underlying feeling with empathy.";
        }
        
        if (!"none".equals(analysis.getSubtext())) {
            return "There's subtext detected (" + analysis.getSubtext() + 
                   "). The person may be hiding their true feelings. Create a safe space for them to open up.";
        }
        
        return "The emotion appears genuine. Respond naturally to the expressed emotion.";
    }
    
    /**
     * Quick mood check - simpler version
     * @param input User input
     * @return Basic mood category
     */
    public String quickMoodCheck(String input) {
        MoodAnalysis analysis = analyzeMood(input);
        
        if (analysis.isSarcastic()) {
            return "sarcastic";
        }
        
        if (!"none".equals(analysis.getSubtext()) && !"none".equals(analysis.getUnderlyingEmotion())) {
            return "complex";
        }
        
        if (analysis.getIntensity() > 0.7) {
            return "intense_" + analysis.getSurfaceEmotion();
        }
        
        return analysis.getSurfaceEmotion();
    }
    
    // ==================== INTEGRATION WITH EMOTION DETECTOR ====================
    
    /**
     * Gets the emotion detector for direct access
     * @return The EmotionDetector instance
     */
    public EmotionDetector getEmotionDetector() {
        return emotionDetector;
    }
    
    /**
     * Gets detailed emotion result from underlying detector
     * @param input User input
     * @return EmotionDetector.EmotionResult
     */
    public EmotionDetector.EmotionResult getDetailedEmotion(String input) {
        return emotionDetector.detectEmotion(input);
    }
    
    /**
     * Gets the MoodMatcher for mood matching capabilities
     * @return The MoodMatcher instance
     */
    public MoodMatcher getMoodMatcher() {
        return new MoodMatcher();
    }
    
    // ==================== MOOD ENHANCEMENT ====================
    
    /**
     * MoodEnhancer - Enhances user mood through targeted interventions
     * 
     * Provides mood enhancement capabilities:
     * - Energy Boosting: Lifts energy when user is low (tired, bored, sad)
     * - Confidence Building: Builds self-esteem and self-belief
     * - Perspective Lifting: Offers alternative viewpoints to shift perspective
     */
    public class MoodEnhancer {
        
        // Enhancement type enum
        public enum EnhancementType {
            ENERGY_BOOST("energy_boost", "Lifts user energy when low"),
            CONFIDENCE_BUILD("confidence_build", "Builds self-confidence and self-esteem"),
            PERSPECTIVE_LIFT("perspective_lift", "Offers alternative viewpoints");
            
            private final String name;
            private final String description;
            
            EnhancementType(String name, String description) {
                this.name = name;
                this.description = description;
            }
            
            public String getName() { return name; }
            public String getDescription() { return description; }
        }
        
        // Result of mood enhancement
        public static class EnhancementResult {
            private EnhancementType enhancementType;
            private String enhancementMessage;
            private String suggestedAction;
            private double effectivenessScore;
            private List<String> followUpSuggestions;
            
            public EnhancementResult() {
                this.followUpSuggestions = new ArrayList<>();
            }
            
            public EnhancementType getEnhancementType() { return enhancementType; }
            public void setEnhancementType(EnhancementType t) { this.enhancementType = t; }
            public String getEnhancementMessage() { return enhancementMessage; }
            public void setEnhancementMessage(String m) { this.enhancementMessage = m; }
            public String getSuggestedAction() { return suggestedAction; }
            public void setSuggestedAction(String a) { this.suggestedAction = a; }
            public double getEffectivenessScore() { return effectivenessScore; }
            public void setEffectivenessScore(double s) { this.effectivenessScore = s; }
            public List<String> getFollowUpSuggestions() { return new ArrayList<>(followUpSuggestions); }
            public void addFollowUpSuggestion(String s) { this.followUpSuggestions.add(s); }
        }
        
        // Energy Boosting responses
        private Map<String, List<String>> energyBoostResponses;
        private Map<String, List<String>> energyBoostActions;
        
        // Confidence Building responses
        private Map<String, List<String>> confidenceBuildResponses;
        private Map<String, List<String>> confidenceBuildAffirmations;
        
        // Perspective Lifting responses
        private Map<String, List<String>> perspectiveLiftResponses;
        private Map<String, List<String>> perspectiveShiftQuestions;
        
        public MoodEnhancer() {
            initializeEnergyBoostResponses();
            initializeConfidenceBuildResponses();
            initializePerspectiveLiftResponses();
        }
        
        // ==================== INITIALIZATION ====================
        
        private void initializeEnergyBoostResponses() {
            energyBoostResponses = new HashMap<>();
            energyBoostActions = new HashMap<>();
            
            // Tired - energy boosting
            energyBoostResponses.put("tired", Arrays.asList(
                "I hear you're feeling exhausted. Remember, rest is not laziness - it's necessary for productivity!",
                "Being tired is your body asking for care. Be gentle with yourself right now.",
                "Fatigue is not a weakness - it's a sign you've been giving a lot. Time to refuel!",
                "Your body is telling you something important. Let's find ways to restore your energy."
            ));
            
            energyBoostActions.put("tired", Arrays.asList(
                "Take a 5-minute stretch break",
                "Drink some water - dehydration can cause fatigue",
                "Step outside for fresh air",
                "Try the 4-7-8 breathing technique"
            ));
            
            // Bored - energy boosting
            energyBoostResponses.put("bored", Arrays.asList(
                "Boredom is your mind's way of asking for stimulation! Let's find something engaging.",
                "When you're bored, that's the perfect time for creative exploration!",
                "Boredom often comes before breakthrough ideas. What's something new you could try?",
                "Your mind is ready for something interesting. What would capture your attention?"
            ));
            
            energyBoostActions.put("bored", Arrays.asList(
                "Learn something new online",
                "Start a small creative project",
                "Take a different route on your walk",
                "Challenge yourself with a puzzle or brain teaser"
            ));
            
            // Sad - energy boosting
            energyBoostResponses.put("sad", Arrays.asList(
                "Sadness can feel heavy, but it also shows you care deeply. That sensitivity is a gift.",
                "While you're feeling down, remember that emotions are like clouds - they pass.",
                "I know things feel heavy right now. But even the darkest night ends with dawn.",
                "Your feelings are valid, and this moment doesn't define your future."
            ));
            
            energyBoostActions.put("sad", Arrays.asList(
                "Write down three things you're grateful for",
                "Listen to your favorite uplifting song",
                "Reach out to someone who makes you smile",
                "Watch a funny video or show that always makes you laugh"
            ));
            
            // Overwhelmed - energy boosting
            energyBoostResponses.put("overwhelmed", Arrays.asList(
                "When everything feels like too much, let's break it down into smaller steps.",
                "Overwhelm is often a sign of caring deeply. Let's channel that energy wisely.",
                "You don't have to do everything at once. One step at a time is still progress.",
                "Take a breath. You're stronger than the things weighing on you."
            ));
            
            energyBoostActions.put("overwhelmed", Arrays.asList(
                "Make a list of just 3 things that must be done today",
                "Set a timer for 25 minutes and focus on one task",
                "Delegate or eliminate one task",
                "Practice the 'LET GO' technique: Label, Exit, Take action, Order priorities"
            ));
            
            // Neutral/Low energy - general boost
            energyBoostResponses.put("neutral", Arrays.asList(
                "Every moment is a chance for something wonderful to happen!",
                "Life is full of small joys waiting to be noticed. What might they be?",
                "Your energy is at your command. What would you like to do with this moment?"
            ));
            
            energyBoostActions.put("neutral", Arrays.asList(
                "Do a quick physical activity to boost endorphins",
                "Text someone you haven't talked to in a while",
                "Listen to upbeat music"
            ));
        }
        
        private void initializeConfidenceBuildResponses() {
            confidenceBuildResponses = new HashMap<>();
            confidenceBuildAffirmations = new HashMap<>();
            
            // Insecure/Self-doubt
            confidenceBuildResponses.put("insecure", Arrays.asList(
                "Self-doubt is just a thought, not a truth. You've overcome challenges before!",
                "Your worth isn't determined by temporary feelings of inadequacy.",
                "Everyone has moments of doubt. What matters is that you keep going anyway.",
                "The fact that you're aware of your doubts shows real self-awareness."
            ));
            
            confidenceBuildAffirmations.put("insecure", Arrays.asList(
                "I am capable and strong",
                "I have overcome challenges before and I can do it again",
                "My worth is not determined by my doubts",
                "I am worthy of good things"
            ));
            
            // Uncertain/Confused
            confidenceBuildResponses.put("confused", Arrays.asList(
                "Confusion is part of learning. It means your mind is working through something new!",
                "Not knowing everything is okay. Curiosity leads to growth.",
                "The fact that you're thinking hard about this shows engagement.",
                "Confusion often precedes clarity. Keep exploring!"
            ));
            
            confidenceBuildAffirmations.put("confused", Arrays.asList(
                "It is okay to not have all the answers",
                "My confusion means I'm engaging with something complex",
                "I am capable of understanding given time",
                "Questions are signs of an active mind"
            ));
            
            // Worried/Anxious
            confidenceBuildResponses.put("anxious", Arrays.asList(
                "Anxiety is just your brain trying to protect you. You are safe right now.",
                "Worrying about things outside your control wastes energy you need elsewhere.",
                "You've survived every difficult day so far. You'll get through this too.",
                "Take this one moment at a time. That's all you need to handle."
            ));
            
            confidenceBuildAffirmations.put("anxious", Arrays.asList(
                "I am safe in this moment",
                "I have survived difficult times before",
                "I cannot control everything, but I can control my response",
                "One moment at a time is enough"
            ));
            
            // Disappointed
            confidenceBuildResponses.put("disappointed", Arrays.asList(
                "Disappointment means you had hope - and hope is a powerful thing.",
                "This feeling is temporary. Your resilience is permanent.",
                "When one door closes, another opens. Let's find what's waiting.",
                "Your expectations show what you value. That's worth celebrating."
            ));
            
            confidenceBuildAffirmations.put("disappointed", Arrays.asList(
                "My feelings of disappointment show I have hope",
                "This is temporary - I will feel better",
                "I am resilient and can bounce back",
                "Disappointment is not failure"
            ));
            
            // General low confidence
            confidenceBuildResponses.put("neutral", Arrays.asList(
                "You have unique strengths that no one else has.",
                "Your experiences have shaped you into someone capable and wise.",
                "Small steps still move you forward. That's what matters.",
                "Believe in yourself - you've come this far!"
            ));
            
            confidenceBuildAffirmations.put("neutral", Arrays.asList(
                "I am worthy of good things",
                "I have unique strengths and abilities",
                "I am capable of growth and learning",
                "I deserve happiness and success"
            ));
        }
        
        private void initializePerspectiveLiftResponses() {
            perspectiveLiftResponses = new HashMap<>();
            perspectiveShiftQuestions = new HashMap<>();
            
            // Stressed - perspective lifting
            perspectiveLiftResponses.put("stressed", Arrays.asList(
                "What feels overwhelming now might seem manageable in a week. Time changes perspective.",
                "Stress is often about perception. What would change if you viewed this differently?",
                "One person's crisis is another's opportunity. How might you reframe this?",
                "In the big picture of your life, how important will this be in 5 years?"
            ));
            
            perspectiveShiftQuestions.put("stressed", Arrays.asList(
                "What would I tell my best friend in this situation?",
                "What would this look like if it were going well?",
                "What is one small step I can take right now?",
                "Will this matter in 5 years?"
            ));
            
            // Angry - perspective lifting
            perspectiveLiftResponses.put("angry", Arrays.asList(
                "Anger is a signal that something matters to you. What value is being threatened?",
                "Sometimes anger protects us. But holding onto it hurts us more than others.",
                "What would happen if you responded instead of reacted?",
                "The other person's behavior often says more about them than about you."
            ));
            
            perspectiveShiftQuestions.put("angry", Arrays.asList(
                "What is this anger trying to protect?",
                "How might the other person be feeling?",
                "What would help me let go of this?",
                "What would peace look like in this situation?"
            ));
            
            // Frustrated - perspective lifting
            perspectiveLiftResponses.put("frustrated", Arrays.asList(
                "Frustration often comes from caring. Channel that energy into solutions.",
                "What's frustrating now might be funny stories later. Keep perspective!",
                "Every expert was once a beginner. Your frustration shows growth.",
                "Sometimes the universe is redirecting you to something better."
            ));
            
            perspectiveShiftQuestions.put("frustrated", Arrays.asList(
                "What can I learn from this situation?",
                "Is there another way to approach this?",
                "What would I do if I couldn't fail?",
                "What would my future self tell me now?"
            ));
            
            // Lonely - perspective lifting
            perspectiveLiftResponses.put("lonely", Arrays.asList(
                "Connection starts with yourself. Being comfortable alone is a superpower.",
                "Sometimes being alone helps us recharge and discover ourselves.",
                "Quality connections matter more than quantity. A few deep bonds matter most.",
                "Your worth isn't determined by who is with you right now."
            ));
            
            perspectiveShiftQuestions.put("lonely", Arrays.asList(
                "What would I enjoy doing alone?",
                "What quality do I value in my closest connections?",
                "How can I be there for myself right now?",
                "What makes me feel truly connected?"
            ));
            
            // Hopeless - perspective lifting
            perspectiveLiftResponses.put("hopeless", Arrays.asList(
                "Hope is the most powerful thing you can hold onto. It creates possibility.",
                "Dark moments pass. This feeling is not your permanent reality.",
                "When you can't see the way forward, take one small step. Movement creates clarity.",
                "Many people have felt exactly like you and found their way through."
            ));
            
            perspectiveShiftQuestions.put("hopeless", Arrays.asList(
                "What would give me a tiny bit of hope right now?",
                "Who has overcome similar feelings?",
                "What would I want to tell someone I love in this situation?",
                "What is one small thing I can do today?"
            ));
            
            // General - perspective lifting
            perspectiveLiftResponses.put("neutral", Arrays.asList(
                "There are many ways to look at this. What other perspectives might exist?",
                "What would you tell someone you care about in this same situation?",
                "Sometimes stepping back reveals a bigger picture.",
                "Your situation is temporary, but your strength is permanent."
            ));
            
            perspectiveShiftQuestions.put("neutral", Arrays.asList(
                "What would I do if I had unlimited resources?",
                "How might I see this in 5 years?",
                "What would a wise person do here?",
                "What am I grateful for despite this?"
            ));
        }
        
        // ==================== PUBLIC METHODS ====================
        
        /**
         * Detects what type of mood enhancement would be most beneficial
         * @param analysis Current mood analysis
         * @return The most appropriate enhancement type
         */
        public EnhancementType detectEnhancementNeed(MoodAnalysis analysis) {
            String emotion = analysis.getSurfaceEmotion().toLowerCase();
            
            // Energy boost for low energy states
            Set<String> lowEnergyEmotions = new HashSet<>(Arrays.asList(
                "tired", "bored", "sad", "overwhelmed", "neutral"
            ));
            
            // Confidence building for self-doubt states
            Set<String> confidenceNeeds = new HashSet<>(Arrays.asList(
                "insecure", "uncertain", "confused", "anxious", "worried", 
                "disappointed", "embarrassed", "hopeless"
            ));
            
            // Perspective lifting for challenging emotions
            Set<String> perspectiveNeeds = new HashSet<>(Arrays.asList(
                "stressed", "angry", "frustrated", "lonely", "hopeless",
                "overwhelmed", "sad", "anxious"
            ));
            
            // Priority: Energy first for very low intensity
            if (analysis.getIntensity() < 0.3) {
                return EnhancementType.ENERGY_BOOST;
            }
            
            // Check emotions for enhancement type
            if (lowEnergyEmotions.contains(emotion)) {
                return EnhancementType.ENERGY_BOOST;
            }
            
            if (confidenceNeeds.contains(emotion)) {
                return EnhancementType.CONFIDENCE_BUILD;
            }
            
            if (perspectiveNeeds.contains(emotion)) {
                return EnhancementType.PERSPECTIVE_LIFT;
            }
            
            // Default based on intensity
            if (analysis.getIntensity() > 0.6) {
                return EnhancementType.PERSPECTIVE_LIFT;
            }
            
            return EnhancementType.ENERGY_BOOST;
        }
        
        /**
         * Generates an enhancement response based on current mood
         * @param analysis Current mood analysis
         * @return EnhancementResult with enhancement message and suggestions
         */
        public EnhancementResult enhanceMood(MoodAnalysis analysis) {
            EnhancementResult result = new EnhancementResult();
            
            // Detect enhancement type
            EnhancementType enhancementType = detectEnhancementNeed(analysis);
            result.setEnhancementType(enhancementType);
            
            // Get emotion key
            String emotion = analysis.getSurfaceEmotion().toLowerCase();
            
            // Generate enhancement based on type
            switch (enhancementType) {
                case ENERGY_BOOST:
                    enhanceEnergy(result, emotion);
                    break;
                case CONFIDENCE_BUILD:
                    enhanceConfidence(result, emotion);
                    break;
                case PERSPECTIVE_LIFT:
                    liftPerspective(result, emotion);
                    break;
            }
            
            // Set effectiveness score based on confidence
            result.setEffectivenessScore(analysis.getConfidence() * 0.9);
            
            return result;
        }
        
        /**
         * Boosts user energy
         */
        private void enhanceEnergy(EnhancementResult result, String emotion) {
            List<String> responses = energyBoostResponses.get(emotion);
            if (responses == null || responses.isEmpty()) {
                responses = energyBoostResponses.get("neutral");
            }
            
            if (responses != null && !responses.isEmpty()) {
                result.setEnhancementMessage(responses.get(random.nextInt(responses.size())));
            } else {
                result.setEnhancementMessage("Let's find something to lift your energy!");
            }
            
            // Get suggested action
            List<String> actions = energyBoostActions.get(emotion);
            if (actions == null || actions.isEmpty()) {
                actions = energyBoostActions.get("neutral");
            }
            
            if (actions != null && !actions.isEmpty()) {
                result.setSuggestedAction(actions.get(random.nextInt(actions.size())));
            }
            
            // Add follow-up suggestions
            result.addFollowUpSuggestion("Would you like to try one of these?");
            result.addFollowUpSuggestion("What usually helps you feel more energized?");
        }
        
        /**
         * Builds user confidence
         */
        private void enhanceConfidence(EnhancementResult result, String emotion) {
            List<String> responses = confidenceBuildResponses.get(emotion);
            if (responses == null || responses.isEmpty()) {
                responses = confidenceBuildResponses.get("neutral");
            }
            
            if (responses != null && !responses.isEmpty()) {
                result.setEnhancementMessage(responses.get(random.nextInt(responses.size())));
            } else {
                result.setEnhancementMessage("You are stronger than you know!");
            }
            
            // Get affirmation
            List<String> affirmations = confidenceBuildAffirmations.get(emotion);
            if (affirmations == null || affirmations.isEmpty()) {
                affirmations = confidenceBuildAffirmations.get("neutral");
            }
            
            if (affirmations != null && !affirmations.isEmpty()) {
                result.setSuggestedAction("Affirmation: " + affirmations.get(random.nextInt(affirmations.size())));
            }
            
            // Add follow-up suggestions
            result.addFollowUpSuggestion("Say this affirmation out loud with me");
            result.addFollowUpSuggestion("What would your best friend say about you right now?");
        }
        
        /**
         * Lifts user perspective
         */
        private void liftPerspective(EnhancementResult result, String emotion) {
            List<String> responses = perspectiveLiftResponses.get(emotion);
            if (responses == null || responses.isEmpty()) {
                responses = perspectiveLiftResponses.get("neutral");
            }
            
            if (responses != null && !responses.isEmpty()) {
                result.setEnhancementMessage(responses.get(random.nextInt(responses.size())));
            } else {
                result.setEnhancementMessage("Let's look at this from a different angle.");
            }
            
            // Get perspective question
            List<String> questions = perspectiveShiftQuestions.get(emotion);
            if (questions == null || questions.isEmpty()) {
                questions = perspectiveShiftQuestions.get("neutral");
            }
            
            if (questions != null && !questions.isEmpty()) {
                result.setSuggestedAction("Consider: " + questions.get(random.nextInt(questions.size())));
            }
            
            // Add follow-up suggestions
            result.addFollowUpSuggestion("What other perspectives can you think of?");
            result.addFollowUpSuggestion("How might you reframe this situation?");
        }
        
        /**
         * Gets energy boost response directly
         * @param emotion The emotion key
         * @return Energy boosting message
         */
        public String getEnergyBoost(String emotion) {
            List<String> responses = energyBoostResponses.get(emotion.toLowerCase());
            if (responses == null || responses.isEmpty()) {
                responses = energyBoostResponses.get("neutral");
            }
            return responses != null ? responses.get(random.nextInt(responses.size())) : 
                "Let's find something to boost your energy!";
        }
        
        /**
         * Gets confidence building response directly
         * @param emotion The emotion key
         * @return Confidence building message
         */
        public String getConfidenceBuild(String emotion) {
            List<String> responses = confidenceBuildResponses.get(emotion.toLowerCase());
            if (responses == null || responses.isEmpty()) {
                responses = confidenceBuildResponses.get("neutral");
            }
            return responses != null ? responses.get(random.nextInt(responses.size())) : 
                "You are stronger than you know!";
        }
        
        /**
         * Gets perspective lifting response directly
         * @param emotion The emotion key
         * @return Perspective lifting message
         */
        public String getPerspectiveLift(String emotion) {
            List<String> responses = perspectiveLiftResponses.get(emotion.toLowerCase());
            if (responses == null || responses.isEmpty()) {
                responses = perspectiveLiftResponses.get("neutral");
            }
            return responses != null ? responses.get(random.nextInt(responses.size())) : 
                "Let's look at this from a different angle.";
        }
        
        /**
         * Quick enhancement - gets a random boosting message
         * @param type The enhancement type
         * @return Enhancement message
         */
        public String quickEnhance(EnhancementType type) {
            switch (type) {
                case ENERGY_BOOST:
                    return getEnergyBoost("neutral");
                case CONFIDENCE_BUILD:
                    return getConfidenceBuild("neutral");
                case PERSPECTIVE_LIFT:
                    return getPerspectiveLift("neutral");
                default:
                    return "You're doing great! Keep going!";
            }
        }
    }
    
    /**
     * Gets the MoodEnhancer for mood enhancement capabilities
     * @return The MoodEnhancer instance
     */
    public MoodEnhancer getMoodEnhancer() {
        return new MoodEnhancer();
    }
}

