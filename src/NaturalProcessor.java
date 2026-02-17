import java.util.*;
import java.util.regex.*;

/**
 * NaturalProcessor - Natural Language Processing for VirtualXander
 * 
 * Provides advanced NLP capabilities:
 * - Slang Recognition: Common slang terms and their meanings
 * - Typo Tolerance: Levenshtein distance for error correction
 * - Context Inference: Understanding implied context
 * - Implicit Meaning Extraction: Hidden meanings in text
 * - Input Classification: Literal vs figurative, serious vs playful, direct vs indirect
 * - Clarification System: Smart disambiguation and follow-up questions
 * 
 * This complements EmotionDetector, MoodEngine, and IntentRecognizer.
 */
public class NaturalProcessor {
    
    // Slang dictionary - 25 common slang terms
    private Map<String, SlangEntry> slangDictionary;
    
    // Typo tolerance threshold
    private static final int MAX_LEVENSHTEIN_DISTANCE = 2;
    
    // Common words for typo correction
    private Map<String, String> commonCorrections;
    
    // Context inference patterns
    private List<ContextPattern> contextPatterns;
    
    // Implicit meaning patterns
    private List<ImplicitMeaningPattern> implicitPatterns;
    
    // Input classification patterns
    private Map<InputCategory, List<Pattern>> classificationPatterns;
    
    // Clarification system
    private Map<String, List<ClarificationOption>> disambiguationMap;
    
    public NaturalProcessor() {
        initializeSlangDictionary();
        initializeCommonCorrections();
        initializeContextPatterns();
        initializeImplicitPatterns();
        initializeClassificationPatterns();
        initializeDisambiguationMap();
    }
    
    // ==================== INNER CLASSES ====================
    
    /**
     * Represents a slang entry with meaning and category
     */
    private static class SlangEntry {
        String term;
        String meaning;
        String category;
        
        SlangEntry(String term, String meaning, String category) {
            this.term = term;
            this.meaning = meaning;
            this.category = category;
        }
    }
    
    /**
     * Context pattern for inference
     */
    private static class ContextPattern {
        Pattern pattern;
        String inferredContext;
        double confidence;
        
        ContextPattern(String regex, String context, double confidence) {
            this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            this.inferredContext = context;
            this.confidence = confidence;
        }
    }
    
    /**
     * Implicit meaning pattern
     */
    private static class ImplicitMeaningPattern {
        Pattern pattern;
        String implicitMeaning;
        String emotionalUndertone;
        
        ImplicitMeaningPattern(String regex, String meaning, String emotion) {
            this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            this.implicitMeaning = meaning;
            this.emotionalUndertone = emotion;
        }
    }
    
    /**
     * Input category enum
     */
    public enum InputCategory {
        LITERAL_FIGURATIVE,  // Is it literal or figurative speech?
        SERIOUS_PLAYFUL,     // Is the user being serious or playful?
        DIRECT_INDIRECT      // Is it a direct or indirect request?
    }
    
    /**
     * Classification result
     */
    public static class ClassificationResult {
        private InputCategory category;
        private String classification;
        private double confidence;
        
        public ClassificationResult(InputCategory category, String classification, double confidence) {
            this.category = category;
            this.classification = classification;
            this.confidence = confidence;
        }
        
        public InputCategory getCategory() { return category; }
        public String getClassification() { return classification; }
        public double getConfidence() { return confidence; }
    }
    
    /**
     * Clarification option
     */
    private static class ClarificationOption {
        String option;
        String followUpQuestion;
        
        ClarificationOption(String option, String followUp) {
            this.option = option;
            this.followUpQuestion = followUp;
        }
    }
    
    /**
     * Processing result
     */
    public static class ProcessingResult {
        private String processedInput;
        private String slangMeaning;
        private String implicitMeaning;
        private String inferredContext;
        private List<ClassificationResult> classifications;
        private boolean needsClarification;
        private String clarificationQuestion;
        private String correctedTypo;
        
        public ProcessingResult() {
            this.classifications = new ArrayList<>();
        }
        
        public String getProcessedInput() { return processedInput; }
        public void setProcessedInput(String s) { this.processedInput = s; }
        
        public String getSlangMeaning() { return slangMeaning; }
        public void setSlangMeaning(String s) { this.slangMeaning = s; }
        
        public String getImplicitMeaning() { return implicitMeaning; }
        public void setImplicitMeaning(String s) { this.implicitMeaning = s; }
        
        public String getInferredContext() { return inferredContext; }
        public void setInferredContext(String s) { this.inferredContext = s; }
        
        public List<ClassificationResult> getClassifications() { return classifications; }
        public void addClassification(ClassificationResult c) { this.classifications.add(c); }
        
        public boolean needsClarification() { return needsClarification; }
        public void setNeedsClarification(boolean b) { this.needsClarification = b; }
        
        public String getClarificationQuestion() { return clarificationQuestion; }
        public void setClarificationQuestion(String s) { this.clarificationQuestion = s; }
        
        public String getCorrectedTypo() { return correctedTypo; }
        public void setCorrectedTypo(String s) { this.correctedTypo = s; }
    }
    
    // ==================== INITIALIZATION ====================
    
    private void initializeSlangDictionary() {
        slangDictionary = new HashMap<>();
        
        // 25 Slang terms with meanings and categories
        slangDictionary.put("wyd", new SlangEntry("wyd", "what are you doing", "inquiry"));
        slangDictionary.put("wdym", new SlangEntry("wdym", "what do you mean", "confusion"));
        slangDictionary.put("idk", new SlangEntry("idk", "I don't know", "response"));
        slangDictionary.put("btw", new SlangEntry("btw", "by the way", "transition"));
        slangDictionary.put("imo", new SlangEntry("imo", "in my opinion", "opinion"));
        slangDictionary.put("imho", new SlangEntry("imho", "in my humble opinion", "opinion"));
        slangDictionary.put("tbh", new SlangEntry("tbh", "to be honest", "honesty"));
        slangDictionary.put("ngl", new SlangEntry("ngl", "not going to lie", "honesty"));
        slangDictionary.put("hbu", new SlangEntry("hbu", "how about you", "inquiry"));
        slangDictionary.put("wbu", new SlangEntry("wbu", "what about you", "inquiry"));
        slangDictionary.put("rn", new SlangEntry("rn", "right now", "time"));
        slangDictionary.put("irl", new SlangEntry("irl", "in real life", "contrast"));
        slangDictionary.put("np", new SlangEntry("np", "no problem", "response"));
        slangDictionary.put("ty", new SlangEntry("ty", "thank you", "gratitude"));
        slangDictionary.put("tysm", new SlangEntry("tysm", "thank you so much", "gratitude"));
        slangDictionary.put("yw", new SlangEntry("yw", "you're welcome", "response"));
        slangDictionary.put("brb", new SlangEntry("brb", "be right back", "absence"));
        slangDictionary.put("bff", new SlangEntry("bff", "best friend forever", "relationship"));
        slangDictionary.put("fyi", new SlangEntry("fyi", "for your information", "information"));
        slangDictionary.put("smh", new SlangEntry("smh", "shaking my head", "disapproval"));
        slangDictionary.put("lol", new SlangEntry("lol", "laughing out loud", "amusement"));
        slangDictionary.put("lmao", new SlangEntry("lmao", "laughing my ass off", "amusement"));
        slangDictionary.put("omg", new SlangEntry("omg", "oh my god", "surprise"));
        slangDictionary.put("rn", new SlangEntry("rn", "right now", "time"));
        slangDictionary.put("fr", new SlangEntry("fr", "for real", "emphasis"));
        slangDictionary.put("asap", new SlangEntry("asap", "as soon as possible", "urgency"));
    }
    
    private void initializeCommonCorrections() {
        commonCorrections = new HashMap<>();
        
        // Common typos and misspellings
        commonCorrections.put("thier", "their");
        commonCorrections.put("teh", "the");
        commonCorrections.put("recieve", "receive");
        commonCorrections.put("occured", "occurred");
        commonCorrections.put("definately", "definitely");
        commonCorrections.put("seperate", "separate");
        commonCorrections.put("accomodate", "accommodate");
        commonCorrections.put("untill", "until");
        commonCorrections.put("begining", "beginning");
        commonCorrections.put("beleive", "believe");
        commonCorrections.put("calender", "calendar");
        commonCorrections.put("concensus", "consensus");
        commonCorrections.put("embarass", "embarrass");
        commonCorrections.put("enviroment", "environment");
        commonCorrections.put("goverment", "government");
        commonCorrections.put("independant", "independent");
        commonCorrections.put("knowlege", "knowledge");
        commonCorrections.put("neccessary", "necessary");
        commonCorrections.put("occassion", "occasion");
        commonCorrections.put("priviledge", "privilege");
        commonCorrections.put("recomend", "recommend");
        commonCorrections.put("refered", "referred");
        commonCorrections.put("tommorow", "tomorrow");
        commonCorrections.put("tommorrow", "tomorrow");
        commonCorrections.put("truely", "truly");
        commonCorrections.put("writting", "writing");
    }
    
    private void initializeContextPatterns() {
        contextPatterns = new ArrayList<>();
        
        // Context inference patterns
        contextPatterns.add(new ContextPattern(
            ".*overwhelmed.*|.*overwhelming.*|.*too much.*",
            "emotional_stress",
            0.9
        ));
        
        contextPatterns.add(new ContextPattern(
            ".*tired.*|.*exhausted.*|.*sleepy.*|.*drained.*",
            "physical_exhaustion",
            0.85
        ));
        
        contextPatterns.add(new ContextPattern(
            ".*lonely.*|.*alone.*|.*no one.*|.*nobody.*",
            "social_isolation",
            0.9
        ));
        
        contextPatterns.add(new ContextPattern(
            ".*stressed.*|.*pressure.*|.*deadline.*",
            "work_stress",
            0.8
        ));
        
        contextPatterns.add(new ContextPattern(
            ".*happy.*|.*excited.*|.*awesome.*|.*great.*",
            "positive_emotion",
            0.85
        ));
        
        contextPatterns.add(new ContextPattern(
            ".*sad.*|.*down.*|.*blue.*|.*depressed.*",
            "negative_emotion",
            0.9
        ));
        
        contextPatterns.add(new ContextPattern(
            ".*help.*|.*assist.*|.*support.*",
            "seeking_help",
            0.85
        ));
        
        contextPatterns.add(new ContextPattern(
            ".*homework.*|.*assignment.*|.*study.*|.*exam.*",
            "academic",
            0.9
        ));
        
        contextPatterns.add(new ContextPattern(
            ".*game.*|.*gaming.*|.*play.*|.*fortnite.*|.*minecraft.*",
            "gaming",
            0.85
        ));
        
        contextPatterns.add(new ContextPattern(
            ".*write.*|.*story.*|.*creative.*|.*poem.*",
            "creative_writing",
            0.85
        ));
    }
    
    private void initializeImplicitPatterns() {
        implicitPatterns = new ArrayList<>();
        
        // Implicit meaning patterns
        implicitPatterns.add(new ImplicitMeaningPattern(
            ".*fine.*|.*okay.*|.*alright.*",
            "dismissive_response_possible",
            "neutral_dismissive"
        ));
        
        implicitPatterns.add(new ImplicitMeaningPattern(
            ".*whatever.*|.*doesn't matter.*|.*not care.*",
            "deflection_avoidance",
            "frustrated"
        ));
        
        implicitPatterns.add(new ImplicitMeaningPattern(
            ".*just.*|.*only.*|.*merely.*",
            "minimizing_feelings",
            "downplaying"
        ));
        
        implicitPatterns.add(new ImplicitMeaningPattern(
            ".*guess.*|.*suppose.*|.*spose.*",
            "uncertain_hesitant",
            "uncertain"
        ));
        
        implicitPatterns.add(new ImplicitMeaningPattern(
            ".*suppose.*|.*guess.*|.*think so.*",
            "uncertain_response",
            "doubtful"
        ));
        
        implicitPatterns.add(new ImplicitMeaningPattern(
            ".*life.*|.*everything.*|.*nothing.*",
            "existential_context",
            "contemplative"
        ));
        
        implicitPatterns.add(new ImplicitMeaningPattern(
            ".*really.*|.*so.*|.*very.*",
            "intensifier_present",
            "emphasizing"
        ));
        
        implicitPatterns.add(new ImplicitMeaningPattern(
            ".*kind of.*|.*sort of.*|.*maybe.*|.*perhaps.*",
            "hedging_language",
            "uncertain"
        ));
    }
    
    private void initializeClassificationPatterns() {
        classificationPatterns = new HashMap<>();
        
        // Literal vs Figurative
        List<Pattern> literalFigurative = new ArrayList<>();
        literalFigurative.add(Pattern.compile(".*literally.*", Pattern.CASE_INSENSITIVE));
        literalFigurative.add(Pattern.compile(".*actually.*", Pattern.CASE_INSENSITIVE));
        literalFigurative.add(Pattern.compile(".*really.*", Pattern.CASE_INSENSITIVE));
        literalFigurative.add(Pattern.compile(".*like (a |an ).*", Pattern.CASE_INSENSITIVE));
        literalFigurative.add(Pattern.compile(".*so much.*", Pattern.CASE_INSENSITIVE));
        classificationPatterns.put(InputCategory.LITERAL_FIGURATIVE, literalFigurative);
        
        // Serious vs Playful
        List<Pattern> seriousPlayful = new ArrayList<>();
        seriousPlayful.add(Pattern.compile(".*joking.*|.*kidding.*|.*sarcasm.*|.*serious.*", Pattern.CASE_INSENSITIVE));
        seriousPlayful.add(Pattern.compile(".*lol.*|.*lmao.*|.*haha.*|.*funny.*", Pattern.CASE_INSENSITIVE));
        seriousPlayful.add(Pattern.compile(".*sigh.*|.*honestly.*|.*really.*", Pattern.CASE_INSENSITIVE));
        classificationPatterns.put(InputCategory.SERIOUS_PLAYFUL, seriousPlayful);
        
        // Direct vs Indirect
        List<Pattern> directIndirect = new ArrayList<>();
        directIndirect.add(Pattern.compile(".*please.*|.*can you.*|.*could you.*|.*would you.*", Pattern.CASE_INSENSITIVE));
        directIndirect.add(Pattern.compile(".*i was wondering.*|.*do you think.*|.*maybe.*", Pattern.CASE_INSENSITIVE));
        directIndirect.add(Pattern.compile(".*tell me.*|.*show me.*|.*help me.*", Pattern.CASE_INSENSITIVE));
        directIndirect.add(Pattern.compile(".*i need.*|.*i want.*|.*i wish.*", Pattern.CASE_INSENSITIVE));
        classificationPatterns.put(InputCategory.DIRECT_INDIRECT, directIndirect);
    }
    
    private void initializeDisambiguationMap() {
        disambiguationMap = new HashMap<>();
        
        // "overwhelmed" disambiguation
        disambiguationMap.put("overwhelmed", Arrays.asList(
            new ClarificationOption("work", "Is it work or school that's overwhelming you?"),
            new ClarificationOption("relationships", "Is it related to relationships or social situations?"),
            new ClarificationOption("life", "Is it overall life stress or something specific?"),
            new ClarificationOption("emotions", "Are you feeling overwhelmed by your emotions?")
        ));
        
        // "fine" disambiguation
        disambiguationMap.put("fine", Arrays.asList(
            new ClarificationOption("truly_fine", "You seem okay, but are you sure everything's alright?"),
            new ClarificationOption("dismissive", "You say fine, but I sense there might be more going on. Want to talk?"),
            new ClarificationOption("hurrying", "Okay, is there something specific you need help with?")
        ));
        
        // "nice" disambiguation
        disambiguationMap.put("nice", Arrays.asList(
            new ClarificationOption("sincere", "That's great to hear! What made it nice?"),
            new ClarificationOption("sarcastic", "I'm sensing some uncertainty there. What's really on your mind?"),
            new ClarificationOption("polite", "Alright, is there something else you'd like to discuss?")
        ));
        
        // "help" disambiguation
        disambiguationMap.put("help", Arrays.asList(
            new ClarificationOption("homework", "Are you looking for help with homework or studying?"),
            new ClarificationOption("emotional", "Do you need emotional support or someone to talk to?"),
            new ClarificationOption("technical", "Is there something technical you need help with?"),
            new ClarificationOption("general", "How can I best help you today?")
        ));
        
        // "coding" disambiguation
        disambiguationMap.put("coding", Arrays.asList(
            new ClarificationOption("learning", "Are you learning to code or working on a project?"),
            new ClarificationOption("problem", "Do you have a specific coding problem you need help with?"),
            new ClarificationOption("career", "Are you interested in programming as a career?")
        ));
        
        // "just" disambiguation
        disambiguationMap.put("just", Arrays.asList(
            new ClarificationOption("minimizing", "You say 'just' but I want to make sure I understand - is there more to this?"),
            new ClarificationOption("literal", "Okay, just to clarify, what exactly do you mean by that?"),
            new ClarificationOption("simple", "Got it. Is there anything else you'd like to add?")
        ));
        
        // "life" disambiguation
        disambiguationMap.put("life", Arrays.asList(
            new ClarificationOption("general", "Life can be tough. What's been especially challenging lately?"),
            new ClarificationOption("work", "Is it more work-related or personal life stuff?"),
            new ClarificationOption("relationships", "Is it about relationships or something else?"),
            new ClarificationOption("future", "Are you thinking about life goals or the future?")
        ));
    }
    
    // ==================== PUBLIC METHODS ====================
    
    /**
     * Process user input with all NLP capabilities
     * @param input Raw user input
     * @return ProcessingResult with all analysis
     */
    public ProcessingResult process(String input) {
        ProcessingResult result = new ProcessingResult();
        
        if (input == null || input.trim().isEmpty()) {
            result.setProcessedInput("");
            return result;
        }
        
        String normalizedInput = input.toLowerCase().trim();
        
        // Step 1: Typo correction
        String correctedInput = correctTypos(normalizedInput);
        result.setCorrectedTypo(correctedInput.equals(normalizedInput) ? null : correctedInput);
        result.setProcessedInput(correctedInput);
        
        // Step 2: Slang recognition
        String slangMeaning = recognizeSlang(correctedInput);
        result.setSlangMeaning(slangMeaning);
        
        // Step 3: Implicit meaning extraction
        String implicitMeaning = extractImplicitMeaning(correctedInput);
        result.setImplicitMeaning(implicitMeaning);
        
        // Step 4: Context inference
        String inferredContext = inferContext(correctedInput);
        result.setInferredContext(inferredContext);
        
        // Step 5: Input classification
        classifyInput(correctedInput, result);
        
        // Step 6: Check for disambiguation needs
        checkDisambiguation(correctedInput, result);
        
        return result;
    }
    
    /**
     * Recognize slang in input
     * @param input Normalized input
     * @return Slang meaning or null
     */
    public String recognizeSlang(String input) {
        // Check for exact slang matches
        String[] words = input.split("\\s+");
        
        for (String word : words) {
            // Remove punctuation
            word = word.replaceAll("[^a-zA-Z]", "");
            
            if (slangDictionary.containsKey(word)) {
                return slangDictionary.get(word).meaning;
            }
        }
        
        // Check for multi-word slang
        for (Map.Entry<String, SlangEntry> entry : slangDictionary.entrySet()) {
            if (input.contains(entry.getKey())) {
                return entry.getValue().meaning;
            }
        }
        
        return null;
    }
    
    /**
     * Correct typos using Levenshtein distance
     * @param input Input text
     * @return Corrected text
     */
    public String correctTypos(String input) {
        String[] words = input.split("\\s+");
        StringBuilder corrected = new StringBuilder();
        
        for (String word : words) {
            String cleanWord = word.replaceAll("[^a-zA-Z]", "");
            
            // Check direct correction map first
            if (commonCorrections.containsKey(cleanWord.toLowerCase())) {
                String replacement = commonCorrections.get(cleanWord.toLowerCase());
                if (cleanWord.equals(word)) {
                    word = replacement;
                } else {
                    // Handle mixed case/punctuation
                    word = replacement + word.substring(cleanWord.length());
                }
            } else {
                // Use Levenshtein distance for unknown words
                String bestMatch = findClosestWord(cleanWord);
                if (bestMatch != null && !bestMatch.equals(cleanWord)) {
                    if (cleanWord.equals(word)) {
                        word = bestMatch;
                    } else {
                        word = bestMatch + word.substring(cleanWord.length());
                    }
                }
            }
            
            if (corrected.length() > 0) {
                corrected.append(" ");
            }
            corrected.append(word);
        }
        
        return corrected.toString();
    }
    
    /**
     * Find closest matching word using Levenshtein distance
     */
    private String findClosestWord(String word) {
        String closest = null;
        int minDistance = Integer.MAX_VALUE;
        
        // Check against common corrections
        for (String correction : commonCorrections.keySet()) {
            int distance = levenshteinDistance(word.toLowerCase(), correction);
            if (distance <= MAX_LEVENSHTEIN_DISTANCE && distance < minDistance) {
                minDistance = distance;
                closest = correction;
            }
        }
        
        // Also check common English words
        String[] commonWords = {"the", "be", "to", "of", "and", "a", "in", "that", "have", "i",
                                "it", "for", "not", "on", "with", "he", "as", "you", "do", "at",
                                "this", "but", "his", "by", "from", "they", "we", "say", "her", "she",
                                "or", "an", "will", "my", "one", "all", "would", "there", "their", "what",
                                "so", "up", "out", "if", "about", "who", "get", "which", "go", "me",
                                "when", "make", "can", "like", "time", "no", "just", "him", "know", "take",
                                "people", "into", "year", "your", "good", "some", "could", "them", "see", "other",
                                "than", "then", "now", "look", "only", "come", "its", "over", "think", "also",
                                "back", "after", "use", "two", "how", "our", "work", "first", "well", "way",
                                "even", "new", "want", "because", "any", "these", "give", "day", "most", "us",
                                "feeling", "really", "thing", "things", "something", "everything", "nothing",
                                "overwhelmed", "overwhelming", "stressed", "tired", "happy", "sad", "excited",
                                "help", "need", "want", "think", "know", "understand", "feel", "feels"};
        
        for (String common : commonWords) {
            int distance = levenshteinDistance(word.toLowerCase(), common);
            if (distance <= MAX_LEVENSHTEIN_DISTANCE && distance < minDistance) {
                minDistance = distance;
                closest = common;
            }
        }
        
        return closest;
    }
    
    /**
     * Calculate Levenshtein distance between two strings
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        
        return dp[s1.length()][s2.length()];
    }
    
    /**
     * Extract implicit meaning from input
     * @param input Normalized input
     * @return Implicit meaning or null
     */
    public String extractImplicitMeaning(String input) {
        for (ImplicitMeaningPattern pattern : implicitPatterns) {
            Matcher matcher = pattern.pattern.matcher(input);
            if (matcher.find()) {
                return pattern.implicitMeaning;
            }
        }
        return null;
    }
    
    /**
     * Infer context from input
     * @param input Normalized input
     * @return Inferred context
     */
    public String inferContext(String input) {
        double highestConfidence = 0.0;
        String bestContext = "general";
        
        for (ContextPattern pattern : contextPatterns) {
            Matcher matcher = pattern.pattern.matcher(input);
            if (matcher.find() && pattern.confidence > highestConfidence) {
                highestConfidence = pattern.confidence;
                bestContext = pattern.inferredContext;
            }
        }
        
        return bestContext;
    }
    
    /**
     * Classify input across all categories
     * @param input Normalized input
     * @param result Processing result to add classifications to
     */
    public void classifyInput(String input, ProcessingResult result) {
        // Literal vs Figurative
        boolean isFigurative = false;
        for (Pattern p : classificationPatterns.get(InputCategory.LITERAL_FIGURATIVE)) {
            if (p.matcher(input).find()) {
                isFigurative = true;
                break;
            }
        }
        result.addClassification(new ClassificationResult(
            InputCategory.LITERAL_FIGURATIVE,
            isFigurative ? "figurative" : "literal",
            isFigurative ? 0.7 : 0.8
        ));
        
        // Serious vs Playful
        boolean isPlayful = false;
        for (Pattern p : classificationPatterns.get(InputCategory.SERIOUS_PLAYFUL)) {
            if (p.matcher(input).find()) {
                isPlayful = true;
                break;
            }
        }
        result.addClassification(new ClassificationResult(
            InputCategory.SERIOUS_PLAYFUL,
            isPlayful ? "playful" : "serious",
            isPlayful ? 0.75 : 0.7
        ));
        
        // Direct vs Indirect
        boolean isIndirect = false;
        for (Pattern p : classificationPatterns.get(InputCategory.DIRECT_INDIRECT)) {
            if (p.matcher(input).find()) {
                // Check if it's indirect language
                if (input.contains("maybe") || input.contains("perhaps") || 
                    input.contains("i was wondering") || input.contains("do you think")) {
                    isIndirect = true;
                    break;
                }
                if (input.contains("please") || input.contains("can you") || 
                    input.contains("could you") || input.contains("tell me")) {
                    isIndirect = false;
                    break;
                }
            }
        }
        result.addClassification(new ClassificationResult(
            InputCategory.DIRECT_INDIRECT,
            isIndirect ? "indirect" : "direct",
            0.7
        ));
    }
    
    /**
     * Check if disambiguation is needed
     * @param input Normalized input
     * @param result Processing result
     */
    public void checkDisambiguation(String input, ProcessingResult result) {
        // Check for keywords that need disambiguation
        String[] disambigKeywords = {"overwhelmed", "fine", "nice", "help", "coding", "just", "life"};
        
        for (String keyword : disambigKeywords) {
            if (input.contains(keyword)) {
                List<ClarificationOption> options = disambiguationMap.get(keyword);
                if (options != null && !options.isEmpty()) {
                    // Generate clarification question
                    StringBuilder question = new StringBuilder();
                    question.append("I want to make sure I understand. ");
                    
                    if (keyword.equals("overwhelmed")) {
                        question.append("Are you feeling overwhelmed by work, relationships, or life in general?");
                    } else if (keyword.equals("fine")) {
                        question.append("You said 'fine' - are you actually doing okay, or is there more going on?");
                    } else if (keyword.equals("nice")) {
                        question.append("When you say 'nice,' do you mean that sincerely or is there something else?");
                    } else if (keyword.equals("just")) {
                        question.append("I notice you said 'just' - are you downplaying what's really going on?");
                    } else if (keyword.equals("life")) {
                        question.append("When you mention life, are you talking about work, relationships, or something else?");
                    } else {
                        question.append("Could you tell me more about what you mean?");
                    }
                    
                    result.setNeedsClarification(true);
                    result.setClarificationQuestion(question.toString());
                    return;
                }
            }
        }
        
        // Also check for single word inputs that might need clarification
        if (input.split("\\s+").length <= 2) {
            if (input.contains("overwhelm") || input.contains("overwhelmed") || 
                input.contains("overwhelming")) {
                result.setNeedsClarification(true);
                result.setClarificationQuestion("I want to make sure I understand correctly. Are you feeling overwhelmed by something specific, or is it more general life stress?");
            } else if (input.equals("just life") || input.contains("just life")) {
                result.setNeedsClarification(true);
                result.setClarificationQuestion("I'm here to listen. When you say 'just life,' what's been particularly challenging for you lately?");
            } else if (input.equals("wdym") || input.contains("wdym")) {
                result.setNeedsClarification(true);
                result.setClarificationQuestion("Could you help me understand what you mean? I'd like to make sure I respond appropriately.");
            }
        }
        
        result.setNeedsClarification(false);
    }
    
    /**
     * Get clarification options for a keyword
     * @param keyword The ambiguous keyword
     * @return List of clarification options
     */
    public List<String> getClarificationOptions(String keyword) {
        List<ClarificationOption> options = disambiguationMap.get(keyword.toLowerCase());
        if (options == null) {
            return Collections.emptyList();
        }
        
        List<String> result = new ArrayList<>();
        for (ClarificationOption opt : options) {
            result.add(opt.option + ": " + opt.followUpQuestion);
        }
        return result;
    }
    
    /**
     * Quick check if input contains slang
     * @param input User input
     * @return true if slang is detected
     */
    public boolean containsSlang(String input) {
        return recognizeSlang(input.toLowerCase()) != null;
    }
    
    /**
     * Quick check if input likely has a typo
     * @param input User input
     * @return true if typos were corrected
     */
    public boolean hasTypo(String input) {
        String corrected = correctTypos(input.toLowerCase());
        return !corrected.equals(input.toLowerCase());
    }
    
    /**
     * Get the inferred context category
     * @param input User input
     * @return Context category string
     */
    public String getContext(String input) {
        return inferContext(input.toLowerCase());
    }
    
    /**
     * Check if input needs clarification
     * @param input User input
     * @return true if clarification is needed
     */
    public boolean needsClarification(String input) {
        ProcessingResult result = process(input);
        return result.needsClarification();
    }
    
    /**
     * Generate follow-up question based on input
     * @param input User input
     * @return Follow-up question or null
     */
    public String generateFollowUp(String input) {
        ProcessingResult result = process(input);
        
        // If we already have a clarification question, use it
        if (result.needsClarification()) {
            return result.getClarificationQuestion();
        }
        
        // Generate contextual follow-up based on detected patterns
        String implicitMeaning = result.getImplicitMeaning();
        String context = result.getInferredContext();
        
        if (implicitMeaning != null) {
            switch (implicitMeaning) {
                case "minimizing_feelings":
                    return "I notice you said 'just' - are you sure that's all that's going on? I'm here to listen if there's more.";
                case "dismissive_response_possible":
                    return "You seem to be downplaying things. Are you actually doing okay?";
                case "existential_context":
                    return "When you mention life being overwhelming, what's been the hardest part lately?";
            }
        }
        
        if (context != null) {
            switch (context) {
                case "emotional_stress":
                    return "You seem to be feeling overwhelmed. Would you like to talk about what's been most stressful?";
                case "social_isolation":
                    return "It sounds like you might be feeling lonely. Would you like to chat about what's been going on?";
                case "negative_emotion":
                    return "I'm here to listen. Would you like to share what's been making you feel this way?";
                case "positive_emotion":
                    return "That's great to hear! What's been the highlight of your day?";
            }
        }
        
        return null;
    }
}
