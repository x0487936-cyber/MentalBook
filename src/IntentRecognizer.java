// Created in Version 0.1.0.0
// Last Updated in Version 0.1.0.3
import java.util.*;
import java.util.regex.*;

/**
 * Intent Recognition System for VirtualXander
 * Recognizes user intents from natural language input
 */
public class IntentRecognizer {
    
    private Map<String, List<Pattern>> intentPatterns;
    private Map<String, Double> intentConfidence;
    
    public IntentRecognizer() {
        this.intentPatterns = new HashMap<>();
        this.intentConfidence = new HashMap<>();
        initializeIntentPatterns();
    }
    
    private void initializeIntentPatterns() {
        // Greetings intents
        addIntentPattern("greeting", "^(hi|hello|hey|howdy|yo|sup|good morning|good afternoon|good evening|greetings)$");
        addIntentPattern("greeting", "\\b(hi|hello|hey|howdy|yo)\\b");
        
        // Identity intents
        addIntentPattern("identity", "what'?s your name|who are you|name\\?|your identity");
        addIntentPattern("identity", "\\b(name|who)\\b.*\\b(you|your)\\b");
        
        // Wellbeing intents
        addIntentPattern("wellbeing_how", "how are you|how r u|hru|how'?s it going|how do you do");
        addIntentPattern("wellbeing_response", "i'?m (good|great|fine|well|okay|ok|alright)");
        addIntentPattern("wellbeing_response", "(good|great|fine|well|okay|ok|alright).*hbu|how about you");
        addIntentPattern("wellbeing_response", "a lot|very much|lots|pretty good|pretty well");
        addIntentPattern("wellbeing_negative", "(not (so )?good|bad|meh|terrible|awful|sad|depressed)");
        addIntentPattern("wellbeing_positive", "(great|excellent|fantastic|awesome|amazing|wonderful|a lot)");
        
        // Activities intents
        addIntentPattern("activity", "what are you doing|wyd|what'?s up|what doing|up to");
        addIntentPattern("activity_response", "(studying|just chilling|relaxing|hanging out|nothing much|bored)");
        
        // Academic intents
        addIntentPattern("homework_help", "(help with|help on|homework|assignment|task|project).*\\?");
        addIntentPattern("homework_help", "i need help.*(homework|study|subject|math|science|history|english)");
        addIntentPattern("homework_subject", "\\b(math|algebra|geometry|calculus|trigonometry|science|biology|physics|chemistry|history|english|geography)\\b");
        
        // Mental health intents
        addIntentPattern("mental_health_support", "(stressed|anxious|depressed|sad|lonely|angry|overwhelmed|tired|not feeling good)");
        addIntentPattern("mental_health_support", "(need to talk|feeling (down|bad|low)|having a hard time)");
        addIntentPattern("mental_health_support", "(dark thoughts|intrusive thoughts|negative thoughts)");
        addIntentPattern("mental_health_positive", "(happy|excited|motivated|inspired|relaxed|calm|peaceful|grateful|confident)");
        
        // Gaming intents
        addIntentPattern("gaming", "(fortnite|cs2|counter.?strike|cs:?go|valorant|apex|minecraft|roblox|gaming)");
        addIntentPattern("gaming_weapon", "(assault rifle|sniper|shotgun|pistol|smg|rocket launcher|ak-47|m4|awp|deagle)");
        addIntentPattern("gaming_map", "(mirage|dust ?2|inferno|nuke|ancient|overpass|vertigo|train|cache|cobblestone)");
        
        // Creative writing intents
        addIntentPattern("creative_writing", "(writing a (book|story|poem|article)|creative writing|story idea|write about)");
        addIntentPattern("creative_writing_topic", "(mystery|romance|sci-fi|science fiction|fantasy|dystopian|historical|thriller)");
        
        // Entertainment intents
        addIntentPattern("entertainment", "(movie|tv|show|music|sport|game|hobby|interest|favorite)");
        addIntentPattern("entertainment_type", "\\b(sports|movies|tv shows|books|art|animals|video games|music|programming)\\b");
        
        // Advice intents
        addIntentPattern("advice", "(need advice|tips|suggestions|how to|advice on|guidance)");
        addIntentPattern("advice_topic", "(study|study ?ing|stress|focus|time management|grades|friends|motivation)");
        
        // Help intents
        addIntentPattern("help_request", "(need help|can you help|help me|assist|support)");
        addIntentPattern("help_type", "(problem|issue|question|concern)");
        
        // Gratitude intents
        addIntentPattern("gratitude", "(thank|thanks|thx|ty|appreciate|cheers)");
        
        // Farewell intents
        addIntentPattern("farewell", "(bye|goodbye|see you|later|catch you|quit|exit)");
        
        // Unknown/Conversation continuation
        addIntentPattern("continue", "(yes|yeah|no|nope|maybe|i don'?t know|idk|oh|wow|cool|awesome)");
        
        // Creative/AI project intents
        addIntentPattern("creative_project", "(building (a )?(robot|ai|app|website|game)|creating|designing|developing)");
        
        // Philosophical intents
        addIntentPattern("philosophical", "(purpose|meaning of life|life|mind|consciousness|existence)");
        
        // Initialize confidence scores
        for (String intent : intentPatterns.keySet()) {
            intentConfidence.put(intent, 0.5);
        }
    }
    
    private void addIntentPattern(String intent, String regex) {
        intentPatterns.computeIfAbsent(intent, k -> new ArrayList<>()).add(
            Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        );
    }
    
    /**
     * Recognizes the primary intent from user input
     * @param input User's input text
     * @return Recognized intent string
     */
    public String recognizeIntent(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "unknown";
        }
        
        String normalizedInput = input.toLowerCase().trim();
        Map<String, Integer> intentScores = new HashMap<>();
        
        // Score each intent based on pattern matches
        for (Map.Entry<String, List<Pattern>> entry : intentPatterns.entrySet()) {
            String intent = entry.getKey();
            int score = 0;
            
            for (Pattern pattern : entry.getValue()) {
                Matcher matcher = pattern.matcher(normalizedInput);
                if (matcher.find()) {
                    // Give higher score for exact matches and multiple matches
                    if (matcher.group().equals(normalizedInput)) {
                        score += 10;
                    } else if (normalizedInput.startsWith(matcher.group()) || 
                               normalizedInput.endsWith(matcher.group())) {
                        score += 5;
                    } else {
                        score += 2;
                    }
                }
            }
            
            if (score > 0) {
                intentScores.put(intent, score);
            }
        }
        
        // Return the highest scoring intent
        if (intentScores.isEmpty()) {
            return "unknown";
        }
        
        return intentScores.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("unknown");
    }
    
    /**
     * Gets confidence score for recognized intent
     * @param input User's input text
     * @return Confidence score between 0 and 1
     */
    public double getConfidence(String input) {
        String intent = recognizeIntent(input);
        if (intent.equals("unknown")) {
            return 0.0;
        }
        return intentConfidence.getOrDefault(intent, 0.5);
    }
    
    /**
     * Gets all matching intents with their scores
     * @param input User's input text
     * @return Map of intents to scores
     */
    public Map<String, Integer> recognizeAllIntents(String input) {
        Map<String, Integer> intentScores = new HashMap<>();
        String normalizedInput = input.toLowerCase().trim();
        
        for (Map.Entry<String, List<Pattern>> entry : intentPatterns.entrySet()) {
            String intent = entry.getKey();
            int score = 0;
            
            for (Pattern pattern : entry.getValue()) {
                Matcher matcher = pattern.matcher(normalizedInput);
                if (matcher.find()) {
                    score += 2;
                }
            }
            
            if (score > 0) {
                intentScores.put(intent, score);
            }
        }
        
        return intentScores;
    }
    
    /**
     * Extracts entities from input (e.g., subjects, topics)
     * @param input User's input text
     * @param entityType Type of entity to extract
     * @return List of extracted entities
     */
    public List<String> extractEntities(String input, String entityType) {
        List<String> entities = new ArrayList<>();
        String normalizedInput = input.toLowerCase();
        
        switch (entityType) {
            case "subject":
                String[] subjects = {"math", "algebra", "geometry", "calculus", "trigonometry",
                                   "science", "biology", "physics", "chemistry", "history",
                                   "english", "geography", "literature", "art", "music"};
                for (String subject : subjects) {
                    if (normalizedInput.contains(subject)) {
                        entities.add(subject);
                    }
                }
                break;
                
            case "emotion":
                String[] positiveEmotions = {"happy", "excited", "motivated", "inspired", "relaxed",
                                           "calm", "peaceful", "grateful", "confident", "fantastic",
                                           "great", "awesome", "excellent"};
                String[] negativeEmotions = {"sad", "stressed", "anxious", "depressed", "lonely",
                                            "angry", "overwhelmed", "tired", "bad", "meh", "terrible"};
                for (String emotion : positiveEmotions) {
                    if (normalizedInput.contains(emotion)) {
                        entities.add("positive:" + emotion);
                    }
                }
                for (String emotion : negativeEmotions) {
                    if (normalizedInput.contains(emotion)) {
                        entities.add("negative:" + emotion);
                    }
                }
                break;
                
            case "game":
                String[] games = {"fortnite", "cs2", "counter strike", "valorant", "apex legends",
                                "minecraft", "roblox", "league", "overwatch", "pubg", "gta"};
                for (String game : games) {
                    if (normalizedInput.contains(game)) {
                        entities.add(game);
                    }
                }
                break;
                
            case "activity":
                String[] activities = {"studying", "gaming", "reading", "watching", "listening",
                                      "hanging out", "relaxing", "exercising", "coding", "creating"};
                for (String activity : activities) {
                    if (normalizedInput.contains(activity)) {
                        entities.add(activity);
                    }
                }
                break;
        }
        
        return entities;
    }
    
    /**
     * Checks if input matches a specific pattern
     * @param input User's input text
     * @param regex Regular expression pattern
     * @return true if matches
     */
    public boolean matchesPattern(String input, String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(input).find();
    }
}