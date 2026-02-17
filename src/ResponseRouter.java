import java.util.*;
import java.util.regex.*;

/**
 * Response Router for VirtualXander
 * Intelligently selects and blends responses based on multiple factors
 * 
 * Features:
 * - Multi-factor response selection
 * - Context appropriateness scoring
 * - Personality consistency checking
 * - Tone matching
 * - Response blending
 */
public class ResponseRouter {
    
    private PersonalityEngine personalityEngine;
    private ContextEngine contextEngine;
    private HumorEngine humorEngine;
    
    // Scoring weights
    private double contextWeight;
    private double personalityWeight;
    private double toneWeight;
    private double coherenceWeight;
    
    // Response candidates storage
    private List<ResponseCandidate> currentCandidates;
    private int maxCandidates;
    
    /**
     * Response candidate for evaluation and selection
     */
    public static class ResponseCandidate {
        String responseId;
        String content;
        String source; // Which handler generated it
        double contextScore;
        double personalityScore;
        double toneScore;
        double coherenceScore;
        double overallScore;
        Map<String, Double> factorScores;
        List<String> tags;
        String emotionalTone;
        String intentType;
        long timestamp;
        
        public ResponseCandidate(String content, String source) {
            this.responseId = UUID.randomUUID().toString();
            this.content = content;
            this.source = source;
            this.contextScore = 0.5;
            this.personalityScore = 0.5;
            this.toneScore = 0.5;
            this.coherenceScore = 0.5;
            this.overallScore = 0.5;
            this.factorScores = new HashMap<>();
            this.tags = new ArrayList<>();
            this.emotionalTone = "neutral";
            this.intentType = "general";
            this.timestamp = System.currentTimeMillis();
        }
        
        public void calculateOverallScore(double contextW, double personalityW, double toneW, double coherenceW) {
            this.overallScore = (contextScore * contextW) + 
                               (personalityScore * personalityW) + 
                               (toneScore * toneW) + 
                               (coherenceScore * coherenceW);
        }
        
        public String getContent() { return content; }
        public String getSource() { return source; }
        public double getOverallScore() { return overallScore; }
        public Map<String, Double> getFactorScores() { return factorScores; }
        public List<String> getTags() { return tags; }
        public String getEmotionalTone() { return emotionalTone; }
        public String getIntentType() { return intentType; }
        public long getTimestamp() { return timestamp; }
    }
    
    /**
     * Response blend result
     */
    public static class BlendResult {
        String blendedContent;
        List<String> sources;
        Map<String, Object> blendMetadata;
        double blendScore;
        
        public BlendResult(String content, List<String> sources, Map<String, Object> metadata, double score) {
            this.blendedContent = content;
            this.sources = sources;
            this.blendMetadata = metadata;
            this.blendScore = score;
        }
    }
    
    /**
     * Response type enumeration for multi-type combination
     */
    public enum ResponseType {
        INFORMATIONAL("informational", "Provides facts and information"),
        EMOTIONAL("emotional", "Expresses or addresses emotions"),
        HUMOROUS("humorous", "Adds humor and levity"),
        INQUIRY("inquiry", "Asks questions or seeks clarification"),
        ADVISORY("advisory", "Provides suggestions and advice"),
        NARRATIVE("narrative", "Tells stories or shares experiences"),
        AFFIRMATIVE("affirmative", "Agrees or acknowledges"),
        DISSENTIVE("dissentive", "Disagrees or offers alternatives");
        
        private final String name;
        private final String description;
        
        ResponseType(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
    
    /**
     * Combined response with multiple types
     */
    public static class CombinedResponse {
        String content;
        List<ResponseType> types;
        List<String> transitions;
        Map<String, Object> metadata;
        double coherenceScore;
        
        public CombinedResponse(String content, List<ResponseType> types, List<String> transitions, 
                                Map<String, Object> metadata, double coherence) {
            this.content = content;
            this.types = types;
            this.transitions = transitions;
            this.metadata = metadata;
            this.coherenceScore = coherence;
        }
    }
    
    /**
     * Transition phrase for smooth topic changes
     */
    public static class TransitionPhrase {
        String phrase;
        String fromTopic;
        String toTopic;
        double smoothnessScore;
        
        public TransitionPhrase(String phrase, String from, String to, double score) {
            this.phrase = phrase;
            this.fromTopic = from;
            this.toTopic = to;
            this.smoothnessScore = score;
        }
    }
    
    public ResponseRouter() {
        this.personalityEngine = new PersonalityEngine();
        this.contextEngine = null;
        this.humorEngine = new HumorEngine();
        
        // Default weights
        this.contextWeight = 0.35;
        this.personalityWeight = 0.25;
        this.toneWeight = 0.25;
        this.coherenceWeight = 0.15;
        
        this.currentCandidates = new ArrayList<>();
        this.maxCandidates = 10;
    }
    
    public ResponseRouter(PersonalityEngine personality, ContextEngine context, HumorEngine humor) {
        this.personalityEngine = personality;
        this.contextEngine = context;
        this.humorEngine = humor;
        
        this.contextWeight = 0.35;
        this.personalityWeight = 0.25;
        this.toneWeight = 0.25;
        this.coherenceWeight = 0.15;
        
        this.currentCandidates = new ArrayList<>();
        this.maxCandidates = 10;
    }
    
    // ==================== WEIGHT CONFIGURATION ====================
    
    /**
     * Set scoring weights for response selection
     */
    public void setWeights(double context, double personality, double tone, double coherence) {
        double total = context + personality + tone + coherence;
        this.contextWeight = context / total;
        this.personalityWeight = personality / total;
        this.toneWeight = tone / total;
        this.coherenceWeight = coherence / total;
    }
    
    /**
     * Get current weights
     */
    public Map<String, Double> getWeights() {
        Map<String, Double> weights = new HashMap<>();
        weights.put("context", contextWeight);
        weights.put("personality", personalityWeight);
        weights.put("tone", toneWeight);
        weights.put("coherence", coherenceWeight);
        return weights;
    }
    
    // ==================== RESPONSE CANDIDATE MANAGEMENT ====================
    
    /**
     * Add a response candidate for evaluation
     */
    public void addCandidate(String content, String source) {
        ResponseCandidate candidate = new ResponseCandidate(content, source);
        currentCandidates.add(candidate);
        
        // Maintain max size
        if (currentCandidates.size() > maxCandidates) {
            currentCandidates.remove(0);
        }
    }
    
    /**
     * Add a candidate with tags
     */
    public void addCandidate(String content, String source, List<String> tags) {
        ResponseCandidate candidate = new ResponseCandidate(content, source);
        candidate.tags.addAll(tags);
        currentCandidates.add(candidate);
    }
    
    /**
     * Clear all candidates
     */
    public void clearCandidates() {
        currentCandidates.clear();
    }
    
    /**
     * Get current candidates
     */
    public List<ResponseCandidate> getCandidates() {
        return new ArrayList<>(currentCandidates);
    }
    
    // ==================== CONTEXT APPROPRIATENESS SCORING ====================
    
    /**
     * Score response based on context appropriateness
     */
    public double scoreContextAppropriateness(ResponseCandidate candidate, String userInput, 
                                               String currentTopic, String detectedEmotion) {
        double score = 0.5;
        
        // Check topic relevance
        if (currentTopic != null && !currentTopic.isEmpty()) {
            String lowerInput = userInput.toLowerCase();
            String lowerResponse = candidate.content.toLowerCase();
            
            // Check if response mentions current topic
            if (lowerResponse.contains(currentTopic.toLowerCase())) {
                score += 0.2;
            }
            
            // Check for topic transitions
            if (candidate.tags.contains("topic-transition")) {
                score += 0.1;
            }
        }
        
        // Check emotion appropriateness
        String responseTone = detectEmotionalTone(candidate.content);
        if (detectedEmotion != null && !detectedEmotion.isEmpty()) {
            if (isEmotionAppropriate(responseTone, detectedEmotion)) {
                score += 0.2;
            } else {
                score -= 0.1;
            }
        }
        
        // Check for question answering
        if (userInput.contains("?") && candidate.content.contains("?")) {
            score += 0.1;
        }
        
        // Check for acknowledgment
        if (acknowledgesInput(userInput, candidate.content)) {
            score += 0.1;
        }
        
        candidate.contextScore = Math.max(0, Math.min(1, score));
        candidate.factorScores.put("context", candidate.contextScore);
        return candidate.contextScore;
    }
    
    /**
     * Check if response emotionally matches user emotion
     */
    private boolean isEmotionAppropriate(String responseTone, String userEmotion) {
        String user = userEmotion.toLowerCase();
        
        if (user.contains("sad") || user.contains("angry") || user.contains("frustrated")) {
            return responseTone.equals("supportive") || responseTone.equals("neutral");
        }
        
        if (user.contains("happy") || user.contains("excited") || user.contains("amused")) {
            return !responseTone.equals("dismissive");
        }
        
        if (user.contains("confused") || user.contains("curious")) {
            return responseTone.equals("helpful") || responseTone.equals("neutral");
        }
        
        return true;
    }
    
    /**
     * Check if response acknowledges user input
     */
    private boolean acknowledgesInput(String userInput, String response) {
        String[] acknowledgments = {
            "i see", "i understand", "that makes sense", "got it", "right",
            "i hear you", "thanks for", "appreciate", "good point"
        };
        
        String lowerResponse = response.toLowerCase();
        for (String ack : acknowledgments) {
            if (lowerResponse.contains(ack)) {
                return true;
            }
        }
        return false;
    }
    
    // ==================== PERSONALITY CONSISTENCY CHECKING ====================
    
    /**
     * Score response based on personality consistency
     */
    public double scorePersonalityConsistency(ResponseCandidate candidate) {
        double score = 0.5;
        
        if (personalityEngine == null) {
            candidate.personalityScore = 0.5;
            candidate.factorScores.put("personality", 0.5);
            return 0.5;
        }
        
        PersonalityEngine.PersonalityState state = personalityEngine.getCurrentState();
        String stateName = state.getName().toLowerCase();
        
        // Check for state-appropriate language
        if (stateName.equals("playful") && candidate.tags.contains("humorous")) {
            score += 0.2;
        }
        
        if (stateName.equals("supportive") && candidate.tags.contains("empathetic")) {
            score += 0.2;
        }
        
        if (stateName.equals("curious") && candidate.content.contains("?")) {
            score += 0.1;
        }
        
        // Check for personality quirks
        if (candidate.content.contains("?") && personalityEngine.shouldAskQuestion()) {
            score += 0.1;
        }
        
        // Check for catchphrase usage
        if (hasCatchphrase(candidate.content)) {
            score += 0.05;
        }
        
        // Check for habitual phrase usage
        if (hasHabitualPhrase(candidate.content)) {
            score += 0.05;
        }
        
        candidate.personalityScore = Math.max(0, Math.min(1, score));
        candidate.factorScores.put("personality", candidate.personalityScore);
        return candidate.personalityScore;
    }
    
    /**
     * Check if response uses personality catchphrases
     */
    private boolean hasCatchphrase(String response) {
        if (personalityEngine == null) return false;
        
        List<String> catchphrases = Arrays.asList(
            "What do you think?", "That's just my take on it.",
            "I'd love to hear your perspective.", "Makes you think",
            "Anyway, that's enough from me.", "I could be wrong though."
        );
        
        String lower = response.toLowerCase();
        for (String phrase : catchphrases) {
            if (lower.contains(phrase.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if response uses habitual phrases
     */
    private boolean hasHabitualPhrase(String response) {
        if (personalityEngine == null) return false;
        
        List<String> phrases = Arrays.asList(
            "You know,", "Honestly,", "Here's the thing",
            "Between us,", "I've always thought", "The thing is",
            "Funnily enough", "It's funny you mention"
        );
        
        String lower = response.toLowerCase();
        for (String phrase : phrases) {
            if (lower.contains(phrase.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    // ==================== TONE MATCHING ====================
    
    /**
     * Score response based on tone matching
     */
    public double scoreToneMatching(ResponseCandidate candidate, String userInput, 
                                     String detectedEmotion, String userIntent) {
        double score = 0.5;
        
        // Detect user tone from input
        String userTone = detectUserTone(userInput, detectedEmotion);
        String responseTone = detectEmotionalTone(candidate.content);
        
        // Match tones
        if (userTone.equals(responseTone)) {
            score += 0.3;
        } else if (areTonesCompatible(userTone, responseTone)) {
            score += 0.15;
        } else if (areTonesConflicting(userTone, responseTone)) {
            score -= 0.2;
        }
        
        // Intent-based tone matching
        if (userIntent != null) {
            if (isToneAppropriateForIntent(responseTone, userIntent)) {
                score += 0.2;
            }
        }
        
        // Length matching
        int inputLength = countWords(userInput);
        int responseLength = countWords(candidate.content);
        double lengthRatio = (double) responseLength / Math.max(inputLength, 1);
        
        if (lengthRatio > 0.5 && lengthRatio < 2.0) {
            score += 0.1;
        }
        
        candidate.toneScore = Math.max(0, Math.min(1, score));
        candidate.emotionalTone = responseTone;
        candidate.factorScores.put("tone", candidate.toneScore);
        return candidate.toneScore;
    }
    
    /**
     * Detect user's emotional tone from input
     */
    private String detectUserTone(String input, String emotion) {
        String lower = input.toLowerCase();
        
        if (emotion != null) {
            String e = emotion.toLowerCase();
            if (e.contains("sad") || e.contains("angry")) return "serious";
            if (e.contains("happy") || e.contains("excited")) return "enthusiastic";
            if (e.contains("confused")) return "questioning";
        }
        
        // Check punctuation and caps
        int questionMarks = countChar(input, '?');
        int exclamationMarks = countChar(input, '!');
        int capsCount = countCaps(input);
        
        if (questionMarks > 0 && questionMarks >= exclamationMarks) return "questioning";
        if (exclamationMarks > 0) return "enthusiastic";
        if (capsCount > input.length() * 0.5) return "emphatic";
        
        // Check for emotional words
        String[] excitedWords = {"wow", "amazing", "great", "awesome", "love"};
        String[] sadWords = {"unfortunately", "sadly", "unfortunately", "oh no"};
        
        for (String word : excitedWords) {
            if (lower.contains(word)) return "enthusiastic";
        }
        for (String word : sadWords) {
            if (lower.contains(word)) return "serious";
        }
        
        return "neutral";
    }
    
    /**
     * Detect emotional tone of response
     */
    private String detectEmotionalTone(String text) {
        String lower = text.toLowerCase();
        
        // Supportive indicators
        if (lower.contains("i understand") || lower.contains("i hear you") || 
            lower.contains("i'm here") || lower.contains("that makes sense")) {
            return "supportive";
        }
        
        // Enthusiastic indicators
        if (lower.contains("great") || lower.contains("awesome") || 
            lower.contains("love that") || lower.contains("exciting")) {
            return "enthusiastic";
        }
        
        // Helpful indicators
        if (lower.contains("here's") || lower.contains("you can") || 
            lower.contains("try this") || lower.contains("one way")) {
            return "helpful";
        }
        
        // Humorous indicators
        if (lower.contains("haha") || lower.contains("lol") || 
            lower.contains("funny") || lower.contains("joke")) {
            return "humorous";
        }
        
        // Questioning indicators
        if (countChar(text, '?') > 0) return "questioning";
        
        return "neutral";
    }
    
    /**
     * Check if two tones are compatible
     */
    private boolean areTonesCompatible(String tone1, String tone2) {
        Set<String> compatible = Set.of(
            "neutral", "helpful", "supportive"
        );
        return compatible.contains(tone1) && compatible.contains(tone2);
    }
    
    /**
     * Check if two tones conflict
     */
    private boolean areTonesConflicting(String tone1, String tone2) {
        Set<String> conflicting = Set.of(
            "enthusiastic", "serious"
        );
        return conflicting.contains(tone1) && conflicting.contains(tone2);
    }
    
    /**
     * Check if tone is appropriate for intent
     */
    private boolean isToneAppropriateForIntent(String tone, String intent) {
        Map<String, Set<String>> appropriate = new HashMap<>();
        appropriate.put("question", Set.of("neutral", "helpful", "questioning"));
        appropriate.put("advice", Set.of("helpful", "supportive"));
        appropriate.put("emotional_support", Set.of("supportive", "helpful"));
        appropriate.put("casual", Set.of("neutral", "humorous", "enthusiastic"));
        appropriate.put("information", Set.of("helpful", "neutral"));
        
        Set<String> allowed = appropriate.getOrDefault(intent, Set.of("neutral"));
        return allowed.contains(tone);
    }
    
    // ==================== COHERENCE SCORING ====================
    
    /**
     * Score response based on conversation coherence
     */
    public double scoreCoherence(ResponseCandidate candidate, String userInput,
                                  List<String> conversationHistory) {
        double score = 0.5;
        
        // Check for topic continuity
        if (conversationHistory != null && !conversationHistory.isEmpty()) {
            String lastMessage = conversationHistory.get(conversationHistory.size() - 1);
            if (hasTopicContinuity(lastMessage, candidate.content)) {
                score += 0.2;
            }
        }
        
        // Check for pronoun consistency
        if (contextEngine != null) {
            String resolved = contextEngine.resolveReferencesInText(candidate.content);
            if (resolved != null && !resolved.equals(candidate.content)) {
                score += 0.1; // Bonus for successful pronoun resolution
            }
        }
        
        // Check for logical flow
        if (hasLogicalFlow(userInput, candidate.content)) {
            score += 0.2;
        }
        
        // Check for redundancy
        if (hasRedundancy(conversationHistory, candidate.content)) {
            score -= 0.1;
        }
        
        candidate.coherenceScore = Math.max(0, Math.min(1, score));
        candidate.factorScores.put("coherence", candidate.coherenceScore);
        return candidate.coherenceScore;
    }
    
    /**
     * Check if response has topic continuity with previous message
     */
    private boolean hasTopicContinuity(String previousMessage, String response) {
        if (previousMessage == null || response == null) return false;
        
        Set<String> connectors = Set.of(
            "that", "this", "it", "these", "those", "also", "another",
            "related", "speaking of", "speaking about", "as i said"
        );
        
        String lowerResponse = response.toLowerCase();
        for (String connector : connectors) {
            if (lowerResponse.contains(connector)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if response has logical flow from input
     */
    private boolean hasLogicalFlow(String input, String response) {
        String[] logicalStarters = {
            "because", "since", "therefore", "however", "but",
            "first", "next", "then", "finally", "so",
            "which means", "that means", "in other words"
        };
        
        String lowerResponse = response.toLowerCase();
        for (String starter : logicalStarters) {
            if (lowerResponse.contains(starter)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if response is redundant with history
     */
    private boolean hasRedundancy(List<String> history, String response) {
        if (history == null || history.isEmpty()) return false;
        
        String lowerResponse = response.toLowerCase();
        for (String msg : history) {
            if (msg.toLowerCase().contains(lowerResponse) && 
                countWords(response) > countWords(msg) * 0.8) {
                return true;
            }
        }
        
        return false;
    }
    
    // ==================== RESPONSE SELECTION ====================
    
    /**
     * Evaluate all candidates and return the best one
     */
    public ResponseCandidate selectBestResponse(String userInput, String currentTopic,
                                                  String detectedEmotion, String userIntent,
                                                  List<String> conversationHistory) {
        if (currentCandidates.isEmpty()) {
            return null;
        }
        
        // Score each candidate
        for (ResponseCandidate candidate : currentCandidates) {
            scoreContextAppropriateness(candidate, userInput, currentTopic, detectedEmotion);
            scorePersonalityConsistency(candidate);
            scoreToneMatching(candidate, userInput, detectedEmotion, userIntent);
            scoreCoherence(candidate, userInput, conversationHistory);
            candidate.calculateOverallScore(contextWeight, personalityWeight, toneWeight, coherenceWeight);
        }
        
        // Sort by overall score
        currentCandidates.sort((a, b) -> Double.compare(b.overallScore, a.overallScore));
        
        // Return best candidate
        return currentCandidates.get(0);
    }
    
    /**
     * Get top N candidates
     */
    public List<ResponseCandidate> getTopCandidates(int n) {
        if (currentCandidates.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<ResponseCandidate> sorted = new ArrayList<>(currentCandidates);
        sorted.sort((a, b) -> Double.compare(b.overallScore, a.overallScore));
        
        return sorted.subList(0, Math.min(n, sorted.size()));
    }
    
    // ==================== RESPONSE BLENDING ====================
    
    /**
     * Blend multiple response candidates into one coherent response
     */
    public BlendResult blendResponses(List<String> responses, String userInput,
                                       String currentTopic, String detectedEmotion) {
        if (responses == null || responses.isEmpty()) {
            return new BlendResult("", new ArrayList<>(), new HashMap<>(), 0.0);
        }
        
        if (responses.size() == 1) {
            return new BlendResult(responses.get(0), List.of("single"), new HashMap<>(), 1.0);
        }
        
        List<String> usedSources = new ArrayList<>();
        List<String> blendedParts = new ArrayList<>();
        Map<String, Object> metadata = new HashMap<>();
        
        // Analyze each response
        List<AnalyzedResponse> analyzed = new ArrayList<>();
        for (String response : responses) {
            AnalyzedResponse ar = new AnalyzedResponse();
            ar.content = response;
            ar.tone = detectEmotionalTone(response);
            ar.length = countWords(response);
            ar.hasQuestion = response.contains("?");
            ar.hasGreeting = isGreeting(response);
            ar.hasFarewell = isFarewell(response);
            analyzed.add(ar);
        }
        
        // Build blended response
        StringBuilder blended = new StringBuilder();
        
        // Start with greeting if needed
        if (!isGreeting(userInput)) {
            for (AnalyzedResponse ar : analyzed) {
                if (ar.hasGreeting && blended.length() == 0) {
                    blended.append(ar.content).append(" ");
                    usedSources.add("greeting");
                    break;
                }
            }
        }
        
        // Add main content from highest-scoring responses
        analyzed.sort((a, b) -> Integer.compare(b.length, a.length));
        int mainContentCount = Math.min(2, analyzed.size());
        for (int i = 0; i < mainContentCount; i++) {
            AnalyzedResponse ar = analyzed.get(i);
            if (!ar.hasGreeting && !ar.hasFarewell) {
                blended.append(ar.content).append(" ");
                usedSources.add("content_" + i);
            }
        }
        
        // Add question if none present
        boolean hasQuestion = blended.toString().contains("?");
        if (!hasQuestion) {
            for (AnalyzedResponse ar : analyzed) {
                if (ar.hasQuestion) {
                    blended.append(ar.content).append(" ");
                    usedSources.add("question");
                    break;
                }
            }
        }
        
        // End with farewell if appropriate
        if (userInput.toLowerCase().contains("bye") || 
            userInput.toLowerCase().contains("goodbye") ||
            userInput.toLowerCase().contains("see you")) {
            for (AnalyzedResponse ar : analyzed) {
                if (ar.hasFarewell) {
                    blended.append(ar.content).append(" ");
                    usedSources.add("farewell");
                    break;
                }
            }
        }
        
        // Clean up and format
        String result = cleanBlendedResponse(blended.toString());
        
        // Apply personality styling
        if (personalityEngine != null) {
            result = personalityEngine.styleResponse(result);
        }
        
        metadata.put("sources", usedSources);
        metadata.put("originalCount", responses.size());
        metadata.put("tone", detectEmotionalTone(result));
        
        double blendScore = calculateBlendScore(analyzed, result);
        
        return new BlendResult(result, usedSources, metadata, blendScore);
    }
    
    /**
     * Analyzed response for blending
     */
    private static class AnalyzedResponse {
        String content;
        String tone;
        int length;
        boolean hasQuestion;
        boolean hasGreeting;
        boolean hasFarewell;
    }
    
    /**
     * Check if text is a greeting
     */
    private boolean isGreeting(String text) {
        String lower = text.toLowerCase();
        return lower.contains("hello") || lower.contains("hi ") || lower.contains("hey") ||
               lower.contains("good morning") || lower.contains("good afternoon") ||
               lower.contains("good evening");
    }
    
    /**
     * Check if text is a farewell
     */
    private boolean isFarewell(String text) {
        String lower = text.toLowerCase();
        return lower.contains("bye") || lower.contains("goodbye") || 
               lower.contains("see you") || lower.contains("talk later") ||
               lower.contains("take care");
    }
    
    /**
     * Clean up blended response
     */
    private String cleanBlendedResponse(String text) {
        // Remove excessive whitespace
        String cleaned = text.replaceAll("\\s+", " ").trim();
        
        // Remove duplicate sentences
        String[] sentences = cleaned.split("[.!?]");
        Set<String> seen = new LinkedHashSet<>();
        List<String> uniqueSentences = new ArrayList<>();
        
        for (String sentence : sentences) {
            String trimmed = sentence.trim();
            if (!trimmed.isEmpty() && !seen.contains(trimmed.toLowerCase())) {
                seen.add(trimmed.toLowerCase());
                uniqueSentences.add(trimmed);
            }
        }
        
        // Reconstruct
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < uniqueSentences.size(); i++) {
            if (i > 0) result.append(". ");
            result.append(uniqueSentences.get(i));
        }
        
        return result.toString().trim() + (result.length() > 0 ? "." : "");
    }
    
    /**
     * Calculate blend quality score
     */
    private double calculateBlendScore(List<AnalyzedResponse> analyzed, String result) {
        double score = 0.5;
        
        // Length appropriateness
        int totalLength = analyzed.stream().mapToInt(a -> a.length).sum();
        int resultLength = countWords(result);
        double lengthRatio = (double) resultLength / Math.max(totalLength, 1);
        
        if (lengthRatio > 0.4 && lengthRatio < 0.8) {
            score += 0.2;
        }
        
        // Question presence
        if (result.contains("?")) {
            score += 0.1;
        }
        
        // Coherence check
        if (hasLogicalFlow("", result)) {
            score += 0.1;
        }
        
        // Completeness
        boolean hasGreeting = isGreeting(result);
        boolean hasFarewell = result.toLowerCase().contains("bye") || 
                             result.toLowerCase().contains("goodbye");
        
        if (hasGreeting || hasFarewell) {
            score += 0.1;
        }
        
        return Math.min(1.0, score);
    }
    
    // ==================== MULTI-TYPE RESPONSE COMBINATION ====================
    
    /**
     * Combine multiple response types into a single coherent response
     */
    public CombinedResponse combineMultipleTypes(Map<ResponseType, String> typeResponses, 
                                                  String userInput, String currentTopic) {
        if (typeResponses == null || typeResponses.isEmpty()) {
            return new CombinedResponse("", new ArrayList<>(), new ArrayList<>(), new HashMap<>(), 0.0);
        }
        
        List<ResponseType> types = new ArrayList<>(typeResponses.keySet());
        List<String> transitions = new ArrayList<>();
        Map<String, Object> metadata = new HashMap<>();
        
        // Sort types by priority
        types.sort((a, b) -> getTypePriority(b) - getTypePriority(a));
        
        // Build combined content with smooth transitions
        StringBuilder combined = new StringBuilder();
        String prevTopic = currentTopic;
        
        for (int i = 0; i < types.size(); i++) {
            ResponseType type = types.get(i);
            String response = typeResponses.get(type);
            
            if (response == null || response.isEmpty()) continue;
            
            // Add transition if not first
            if (combined.length() > 0) {
                TransitionPhrase transition = generateSmoothTransition(prevTopic, type.getName());
                combined.append(" ").append(transition.phrase);
                transitions.add(transition.phrase);
            }
            
            combined.append(response);
            prevTopic = type.getName();
        }
        
        // Apply personality styling
        if (personalityEngine != null) {
            String styled = personalityEngine.styleResponse(combined.toString());
            combined = new StringBuilder(styled);
        }
        
        // Calculate coherence score
        double coherence = calculateMultiTypeCoherence(types, transitions);
        
        metadata.put("typeCount", types.size());
        metadata.put("types", types.stream().map(ResponseType::getName).collect(java.util.stream.Collectors.toList()));
        metadata.put("hasTransition", !transitions.isEmpty());
        
        return new CombinedResponse(combined.toString(), types, transitions, metadata, coherence);
    }
    
    /**
     * Get priority order for response types
     */
    private int getTypePriority(ResponseType type) {
        switch (type) {
            case EMOTIONAL: return 10;  // Address emotions first
            case INQUIRY: return 9;      // Questions are important
            case INFORMATIONAL: return 8; // Facts matter
            case ADVISORY: return 7;     // Advice is useful
            case AFFIRMATIVE: return 6;  // Acknowledgment
            case NARRATIVE: return 5;    // Stories add context
            case HUMOROUS: return 4;    // Humor can lighten
            case DISSENTIVE: return 3;  // Disagreement last
            default: return 1;
        }
    }
    
    /**
     * Calculate coherence for multi-type responses
     */
    private double calculateMultiTypeCoherence(List<ResponseType> types, List<String> transitions) {
        double score = 0.5;
        
        // Bonus for having transitions
        if (!transitions.isEmpty()) {
            score += 0.2;
        }
        
        // Check for emotional-informational balance
        boolean hasEmotional = types.contains(ResponseType.EMOTIONAL);
        boolean hasInformational = types.contains(ResponseType.INFORMATIONAL);
        if (hasEmotional && hasInformational) {
            score += 0.1; // Good balance
        }
        
        // Check for inquiry-advisory pairing
        boolean hasInquiry = types.contains(ResponseType.INQUIRY);
        boolean hasAdvisory = types.contains(ResponseType.ADVISORY);
        if (hasInquiry && hasAdvisory) {
            score += 0.1; // Good pairing
        }
        
        return Math.min(1.0, score);
    }
    
    // ==================== SMOOTH TRANSITIONS ====================
    
    /**
     * Generate smooth transition phrase between topics
     */
    public TransitionPhrase generateSmoothTransition(String fromTopic, String toTopic) {
        if (fromTopic == null) fromTopic = "general";
        if (toTopic == null) toTopic = "general";
        
        String transition;
        double score = 0.7;
        
        // Direct transitions
        if (fromTopic.equals(toTopic)) {
            transition = getContinuePhrase();
            score = 0.9;
        }
        // Related topic transitions
        else if (areTopicsRelated(fromTopic, toTopic)) {
            transition = getRelatedTransition(fromTopic, toTopic);
            score = 0.8;
        }
        // Topic shift
        else {
            transition = getShiftTransition(fromTopic, toTopic);
            score = 0.6;
        }
        
        return new TransitionPhrase(transition, fromTopic, toTopic, score);
    }
    
    /**
     * Check if two topics are related
     */
    private boolean areTopicsRelated(String topic1, String topic2) {
        Set<String> relatedPairs = Set.of(
            "programming-coding", "coding-java", "java-python",
            "gaming-game", "game-fun", "entertainment-fun",
            "emotions-feelings", "feelings-mood", "mood-happy",
            "school-homework", "homework-study", "study-learning",
            "music-song", "song-art", "art-creative"
        );
        
        String pair = topic1.toLowerCase() + "-" + topic2.toLowerCase();
        String reverse = topic2.toLowerCase() + "-" + topic1.toLowerCase();
        
        return relatedPairs.contains(pair) || relatedPairs.contains(reverse);
    }
    
    /**
     * Get continue phrase for same topic
     */
    private String getContinuePhrase() {
        String[] phrases = {
            "Also, ",
            "Additionally, ",
            "Speaking of which, ",
            "On that note, ",
            "That reminds me, "
        };
        return phrases[new Random().nextInt(phrases.length)];
    }
    
    /**
     * Get related topic transition
     */
    private String getRelatedTransition(String from, String to) {
        String[] transitions = {
            "That connects to " + to + " because ",
            "This relates to " + to + " - ",
            "In terms of " + to + ", ",
            "Speaking of " + from + ", have you considered " + to + "?",
            "This brings me to " + to + " - "
        };
        return transitions[new Random().nextInt(transitions.length)];
    }
    
    /**
     * Get topic shift transition
     */
    private String getShiftTransition(String from, String to) {
        String[] transitions = {
            "Shifting gears to " + to + ", ",
            "On a different note, about " + to + " - ",
            "Let me ask you about " + to + " - ",
            "Changing subject slightly, what about " + to + "?",
            "I'd like to explore " + to + " with you. "
        };
        return transitions[new Random().nextInt(transitions.length)];
    }
    
    /**
     * Create smooth topic bridge
     */
    public String createTopicBridge(String fromTopic, String toTopic, String fromContent, String toContent) {
        TransitionPhrase transition = generateSmoothTransition(fromTopic, toTopic);
        
        StringBuilder bridge = new StringBuilder();
        
        // End first content appropriately
        if (!fromContent.isEmpty()) {
            String firstEnd = endAppropriately(fromContent);
            bridge.append(firstEnd).append(" ");
        }
        
        // Add transition
        bridge.append(transition.phrase);
        
        // Start new content
        String secondStart = startAppropriately(toContent);
        bridge.append(" ").append(secondStart);
        
        return bridge.toString();
    }
    
    /**
     * End content appropriately for transition
     */
    private String endAppropriately(String content) {
        if (content.endsWith("?") || content.endsWith("!")) {
            return content;
        }
        
        String[] endings = {
            ". That said, ",
            ". Moving on, ",
            ". Now, ",
            ". With that in mind, "
        };
        
        if (content.contains("?")) {
            return content.substring(0, content.indexOf("?")) + "?" + endings[new Random().nextInt(endings.length)];
        }
        
        return content + endings[new Random().nextInt(endings.length)];
    }
    
    /**
     * Start content appropriately after transition
     */
    private String startAppropriately(String content) {
        if (content.isEmpty()) return "";
        
        String lower = content.toLowerCase().trim();
        
        // Remove leading articles/connectors
        String[] toRemove = {"the ", "a ", "an ", "so ", "but ", "and ", "however, "};
        for (String remove : toRemove) {
            if (lower.startsWith(remove)) {
                return content.substring(remove.length());
            }
        }
        
        return content;
    }
    
    // ==================== MIXED EMOTIONAL RESPONSES ====================
    
    /**
     * Create response with mixed emotional tones
     */
    public String createMixedEmotionalResponse(String primaryContent, String emotionalSupport,
                                                 String humorousAdd, String inquiryPart,
                                                 String userEmotion) {
        StringBuilder mixed = new StringBuilder();
        String emotion = userEmotion != null ? userEmotion.toLowerCase() : "neutral";
        
        // Determine order based on emotion
        List<String> parts = new ArrayList<>();
        
        // Always start with acknowledgment
        if (!emotionalSupport.isEmpty()) {
            parts.add(emotionalSupport);
        }
        
        // Add primary content
        if (!primaryContent.isEmpty()) {
            parts.add(primaryContent);
        }
        
        // Add humor if appropriate
        if (!humorousAdd.isEmpty() && isHumorAppropriate(emotion)) {
            parts.add(humorousAdd);
        }
        
        // End with inquiry
        if (!inquiryPart.isEmpty()) {
            parts.add(inquiryPart);
        }
        
        // Blend parts with transitions
        for (int i = 0; i < parts.size(); i++) {
            if (i > 0) {
                mixed.append(" ");
                mixed.append(getEmotionalTransition(i, parts.size()));
            }
            mixed.append(parts.get(i));
        }
        
        // Apply personality
        if (personalityEngine != null) {
            mixed = new StringBuilder(personalityEngine.styleResponse(mixed.toString()));
        }
        
        return mixed.toString();
    }
    
    /**
     * Check if humor is appropriate for current emotion
     */
    private boolean isHumorAppropriate(String userEmotion) {
        if (userEmotion == null) return true;
        
        String[] inappropriateEmotions = {"sad", "angry", "frustrated", "upset", "hurt"};
        for (String e : inappropriateEmotions) {
            if (userEmotion.contains(e)) return false;
        }
        
        return true;
    }
    
    /**
     * Get transition between emotional parts
     */
    private String getEmotionalTransition(int position, int total) {
        if (position == total - 1) {
            // Before question
            String[] beforeQuestion = {
                "So, ", "Anyway, ", "This makes me wonder, ", "Speaking of which, "
            };
            return beforeQuestion[new Random().nextInt(beforeQuestion.length)];
        } else {
            // Between content parts
            String[] betweenParts = {
                "Also, ", "Additionally, ", "Plus, ", "On top of that, "
            };
            return betweenParts[new Random().nextInt(betweenParts.length)];
        }
    }
    
    /**
     * Blend emotional tones for complex situations
     */
    public String blendEmotionalTones(String sadContent, String supportiveContent, 
                                        String hopefulContent, String userEmotion) {
        String emotion = userEmotion != null ? userEmotion.toLowerCase() : "neutral";
        
        // Determine dominant emotion
        boolean isSad = emotion.contains("sad") || emotion.contains("disappointed");
        boolean isAnxious = emotion.contains("anxious") || emotion.contains("worried");
        boolean isFrustrated = emotion.contains("frustrated") || emotion.contains("annoyed");
        
        StringBuilder blended = new StringBuilder();
        
        if (isSad) {
            // Sad: Start supportive, add hopeful, end with question
            if (!supportiveContent.isEmpty()) {
                blended.append(supportiveContent);
            }
            if (!hopefulContent.isEmpty()) {
                blended.append(" ").append(getTransition("supportive", "hopeful")).append(hopefulContent);
            }
            if (!sadContent.isEmpty()) {
                blended.append(" ").append(getTransition("hopeful", "empathetic")).append(sadContent);
            }
        } else if (isAnxious) {
            // Anxious: Reassuring, supportive, then question
            if (!supportiveContent.isEmpty()) {
                blended.append(supportiveContent);
            }
            if (!sadContent.isEmpty()) {
                blended.append(" ").append(getTransition("supportive", "calm")).append(sadContent);
            }
        } else if (isFrustrated) {
            // Frustrated: Validate, then redirect
            if (!sadContent.isEmpty()) {
                blended.append(sadContent);
            }
            if (!hopefulContent.isEmpty()) {
                blended.append(" ").append(getTransition("empathetic", "positive")).append(hopefulContent);
            }
        } else {
            // Neutral: Balanced mix
            if (!supportiveContent.isEmpty()) {
                blended.append(supportiveContent);
            }
            if (!sadContent.isEmpty()) {
                blended.append(" ").append(getTransition("supportive", "informative")).append(sadContent);
            }
            if (!hopefulContent.isEmpty()) {
                blended.append(" ").append(getTransition("informative", "positive")).append(hopefulContent);
            }
        }
        
        // Apply personality
        if (personalityEngine != null) {
            blended = new StringBuilder(personalityEngine.styleResponse(blended.toString()));
        }
        
        return blended.toString();
    }
    
    /**
     * Get transition between emotional states
     */
    private String getTransition(String from, String to) {
        String[][] transitions = {
            {"supportive", "hopeful", "Looking forward, "},
            {"supportive", "empathetic", "I really understand, "},
            {"supportive", "calm", "Take a breath, "},
            {"hopeful", "empathetic", "It's okay to feel, "},
            {"empathetic", "positive", "But here's the thing, "},
            {"informative", "positive", "The good news is, "},
            {"supportive", "informative", "Here's what I think, "}
        };
        
        for (String[] t : transitions) {
            if (t[0].equals(from) && t[1].equals(to)) {
                return t[2];
            }
        }
        
        return "Also, ";
    }
    
    /**
     * Detect emotional complexity of user input
     */
    public double detectEmotionalComplexity(String userInput) {
        if (userInput == null || userInput.isEmpty()) return 0.0;
        
        String lower = userInput.toLowerCase();
        int complexityIndicators = 0;
        
        // Multiple emotions mentioned
        String[] emotionWords = {"sad", "happy", "angry", "excited", "worried", "frustrated", "confused"};
        for (String word : emotionWords) {
            if (lower.contains(word)) complexityIndicators++;
        }
        
        // Complex sentence structure
        if (lower.contains(" and ") || lower.contains(" but ") || lower.contains(" however ")) {
            complexityIndicators++;
        }
        
        // Questions within statement
        if (lower.contains("?") && lower.length() > 50) {
            complexityIndicators++;
        }
        
        // Length factor
        int wordCount = countWords(userInput);
        if (wordCount > 20) complexityIndicators++;
        if (wordCount > 50) complexityIndicators++;
        
        return Math.min(1.0, complexityIndicators / 5.0);
    }
    
    /**
     * Generate appropriate response complexity based on input
     */
    public String adjustResponseComplexity(String baseResponse, double inputComplexity) {
        int responseLength = countWords(baseResponse);
        int targetLength = (int) (responseLength * (0.5 + inputComplexity));
        
        if (responseLength > targetLength) {
            // Simplify response
            return simplifyResponse(baseResponse, targetLength);
        } else if (responseLength < targetLength) {
            // Elaborate response
            return elaborateResponse(baseResponse, targetLength - responseLength);
        }
        
        return baseResponse;
    }
    
    /**
     * Simplify response to target word count
     */
    private String simplifyResponse(String response, int targetWords) {
        String[] sentences = response.split("[.!?]");
        StringBuilder simplified = new StringBuilder();
        int currentWords = 0;
        
        for (String sentence : sentences) {
            int sentenceWords = countWords(sentence);
            if (currentWords + sentenceWords <= targetWords) {
                if (simplified.length() > 0) {
                    simplified.append(". ");
                }
                simplified.append(sentence.trim());
                currentWords += sentenceWords;
            }
        }
        
        return simplified.toString().trim() + ".";
    }
    
    /**
     * Elaborate response to target word count
     */
    private String elaborateResponse(String response, int additionalWords) {
        StringBuilder elaborated = new StringBuilder(response);
        
        // Add elaborations based on personality
        if (personalityEngine != null && personalityEngine.shouldAddHumor()) {
            String elaboration = getElaborationPhrase();
            elaborated.append(" ").append(elaboration);
        }
        
        // Add clarifying question if appropriate
        if (personalityEngine != null && personalityEngine.shouldAskQuestion()) {
            String question = personalityEngine.getCatchphrase();
            elaborated.append(" ").append(question);
        }
        
        return elaborated.toString();
    }
    
    /**
     * Get elaboration phrase
     */
    private String getElaborationPhrase() {
        String[] elaborations = {
            "That's a good point to explore further.",
            "There's more to this than meets the eye.",
            "This kind of thing happens more than you'd think.",
            "It's interesting how these things connect.",
            "I find this quite fascinating, honestly."
        };
        return elaborations[new Random().nextInt(elaborations.length)];
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Count words in text
     */
    private int countWords(String text) {
        if (text == null || text.isEmpty()) return 0;
        return text.trim().split("\\s+").length;
    }
    
    /**
     * Count occurrences of character
     */
    private int countChar(String text, char c) {
        int count = 0;
        for (char ch : text.toCharArray()) {
            if (ch == c) count++;
        }
        return count;
    }
    
    /**
     * Count uppercase letters
     */
    private int countCaps(String text) {
        int count = 0;
        for (char ch : text.toCharArray()) {
            if (Character.isUpperCase(ch)) count++;
        }
        return count;
    }
    
    // ==================== GETTERS AND SETTERS ====================
    
    public PersonalityEngine getPersonalityEngine() {
        return personalityEngine;
    }
    
    public void setPersonalityEngine(PersonalityEngine engine) {
        this.personalityEngine = engine;
    }
    
    public ContextEngine getContextEngine() {
        return contextEngine;
    }
    
    public void setContextEngine(ContextEngine engine) {
        this.contextEngine = engine;
    }
    
    public HumorEngine getHumorEngine() {
        return humorEngine;
    }
    
    public void setHumorEngine(HumorEngine engine) {
        this.humorEngine = engine;
    }
    
    public int getMaxCandidates() {
        return maxCandidates;
    }
    
    public void setMaxCandidates(int max) {
        this.maxCandidates = max;
    }
    
    /**
     * Get routing statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("candidateCount", currentCandidates.size());
        stats.put("maxCandidates", maxCandidates);
        stats.put("weights", getWeights());
        
        if (!currentCandidates.isEmpty()) {
            double avgScore = currentCandidates.stream()
                .mapToDouble(c -> c.overallScore)
                .average()
                .orElse(0);
            stats.put("averageScore", avgScore);
            
            ResponseCandidate best = currentCandidates.stream()
                .max((a, b) -> Double.compare(a.overallScore, b.overallScore))
                .orElse(null);
            stats.put("bestScore", best != null ? best.overallScore : 0);
        }
        
        return stats;
    }
}
