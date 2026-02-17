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
        addIntentPattern("greeting", "^(hi|hello|hey|howdy|yo|sup|good morning|good afternoon|good evening|greetings|good night)$");
        addIntentPattern("greeting", "\\b(hi|hello|hey|howdy|yo)\\b");
        
        // Identity intents
        addIntentPattern("identity", "what'?s your name|who are you|name\\?|your identity");
        addIntentPattern("identity", "\\b(name|who)\\b.*\\b(you|your)\\b");
        
        // Wellbeing intents
        addIntentPattern("wellbeing_how", "how are you|how r u|hru|how'?s it going|how do you do");
        addIntentPattern("wellbeing_response", "i am (good|great|fine|well|okay|ok|alright)");
        addIntentPattern("wellbeing_response", "i'?m (good|great|fine|well|okay|ok|alright)");
        addIntentPattern("wellbeing_response", "(good|great|fine|well|okay|ok|alright).*hbu|how about you");
        // "wbu" abbreviation for "what about you"
        addIntentPattern("wellbeing_response", "\\bwbu\\b");
        // Specific "a lot" intent - should be checked before general positive responses
        addIntentPattern("wellbeing_a_lot", "^a lot$");
        addIntentPattern("wellbeing_a_lot", "\\ba lot\\b");
        addIntentPattern("wellbeing_response", "a lot|very much|lots|pretty good|pretty well");
        addIntentPattern("wellbeing_response", "\\b(good|great|fine|well|okay|ok|alright|fine\\.|doing well)\\b");
        addIntentPattern("wellbeing_day", "how was your day|how was your day\\?|how did your day go|how has your day been");
        addIntentPattern("wellbeing_negative", "(not (so )?good|bad|meh|terrible|awful|sad|depressed)");
        addIntentPattern("wellbeing_positive", "(great|excellent|fantastic|awesome|amazing|wonderful|a lot)");
        
        // Activities intents
        addIntentPattern("activity", "what are you doing|wyd|what'?s up|what doing|up to");
        addIntentPattern("activity_response", "(studying|just chilling|relaxing|hanging out|nothing much|bored)");
        // Typo-tolerant patterns for "nothing much" (common misspellings)
        addIntentPattern("activity_response", "(mothing much|nothin much|nothng much|nuth much|noting much)");
        addIntentPattern("activity_response", "(nothing muh|nothng mch|nothin mch|nuthn much)");

        
        // Academic intents
        addIntentPattern("homework_help", "(help with|help on|homework|assignment|task|project).*\\?");
        addIntentPattern("homework_help", "i need help.*(homework|study|subject|math|science|history|english)");
        addIntentPattern("homework_subject", "\\b(math|algebra|geometry|calculus|trigonometry|science|biology|physics|chemistry|history|english|geography)\\b");
        
        // Mental health intents
        addIntentPattern("mental_health_support", "(stressed|anxious|depressed|sad|lonely|angry|overwhelmed|tired|not feeling good)");
        addIntentPattern("mental_health_support", "(feeling (down|bad|low)|having a hard time|struggling|getting tough|life is tough)");
        addIntentPattern("mental_health_support", "(need to talk|feeling (down|bad|low)|having a hard time)");
        addIntentPattern("mental_health_support", "(dark thoughts|intrusive thoughts|negative thoughts)");
        addIntentPattern("mental_health_support", "(need someone to talk|need someone|i need someone|want to talk|need to vent)");
        addIntentPattern("mental_health_positive", "(happy|excited|motivated|inspired|relaxed|calm|peaceful|grateful|confident)");
        
        // Gaming intents
        addIntentPattern("gaming_game", "(fortnite|cs2|counter.?strike|cs:?go|valorant|apex|minecraft|roblox|gaming)");
        addIntentPattern("gaming", "(league of legends|league|lol|overwatch|pubg|gta)");
        addIntentPattern("gaming", "(want to play|let'?s play|i want to play|want to game|i want to game)");
        addIntentPattern("gaming", "(play (a )?game|playing games|play video games|video gaming)");
        addIntentPattern("gaming", "\\b(play|gaming|gamer|gamers)\\b");
        addIntentPattern("gaming_weapon", "(assault rifle|sniper|shotgun|pistol|smg|rocket launcher|ak-47|m4|awp|deagle)");
        addIntentPattern("gaming_map", "(mirage|dust ?2|inferno|nuke|ancient|overpass|vertigo|train|cache|cobblestone)");
        
        // Gaming recommendation intents
        addIntentPattern("gaming_recommendation", "(game recommendation|games recommendation|recommend.*game|recommend.*games|recommendation.*game|recommendation.*games)");
        addIntentPattern("gaming_recommendation", "(any game|any games|what game|what games|which game|which games)");
        addIntentPattern("gaming_recommendation", "(new game|new games|suggest.*game|suggest.*games)");
        addIntentPattern("gaming_recommendation", "(looking for.*game|looking for.*games|need.*game|need.*games)");
        addIntentPattern("gaming_recommendation", "(favorite game|favorite games|best game|best games|top game|top games)");
        
        // Creative writing intents
        addIntentPattern("creative_writing", "(writing a (book|story|poem|article)|creative writing|story idea|write about)");
        addIntentPattern("creative_writing", "(i want to|i'd like to|i need to|i will|i'm going to).*write");
        addIntentPattern("creative_writing_topic", "(mystery|romance|sci-fi|science fiction|fantasy|dystopian|historical|thriller|poetry|poem|writing)");
        
        // Entertainment intents
        addIntentPattern("entertainment_type", "(movie|tv|show|music|sport|game|hobby|interest)");
        addIntentPattern("entertainment", "\\b((favourite|best|top)\\b.*\\b(favorite))\\b");
        addIntentPattern("entertainment", "\\b(sports|movies|tv shows|books|art|animals|video games|music|programming)\\b");
        addIntentPattern("entertainment", "\\b(what's on (netflix)|what's on (hulu)|what's on (disney\\+)|what's on (prime video)|what's on (amazon prime)|what's on (hbomax))\\b");
        
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
        addIntentPattern("continue", "(make me laugh|lol|haha|funny|joke)");
        
        // Creative/AI project intents
        addIntentPattern("creative_project", "(building (a )?(robot|ai|app|website|game)|creating|designing|developing)");
        
        // Relationship intents
        addIntentPattern("relationship", "(my relationship|relationship|relationships|dating|boyfriend|girlfriend|husband|wife|married)");
        addIntentPattern("relationship", "\\b(relationship|dating|married)\\b");
        
        // Breakup intents (higher priority)
        addIntentPattern("breakup", "(my ex|ex boyfriend|ex girlfriend|broke up|break up|breakup|heartbroken|heartbreak|dumped|separated|I'm heartbroken|I broke up)");
        addIntentPattern("broke up", "\\b(broke up|breakup|break up|heartbroken|dumped)\\b");
        
        // Philosophical intents
        addIntentPattern("philosophical", "(purpose|meaning of life|life|mind|consciousness|existence)");
        
        // Confusion/Clarification intent for single words like "what", "huh"
        addIntentPattern("confusion", "^(what|huh|what\\?|why\\?|who\\?|where\\?|when\\?|how\\?|uh|excuse me\\?|why|hmm)$");
        addIntentPattern("confusion", "\\b(what|huh|why|hmm)\\b");
        
        // Hesitation intent for "um", "uh", "hm" - indicates user is thinking or hesitant
        addIntentPattern("hesitation", "^(um|uh|hm|hmm|er|ah|uhm|uhh|umm|umms)$");
        addIntentPattern("hesitation", "\\b(um|uh|hm|hmm|er|ah|uhm|uhh|umm|umms)\\b");
        
        // Milestone/Achievement intents
        addIntentPattern("milestone_celebration", "(i got|i've got|i got promoted|i graduated|i won|i achieved|i finished|i completed)");
        addIntentPattern("milestone_celebration", "\\b(promoted|graduated|won|achieved|finished|completed)\\b");
        addIntentPattern("milestone_celebration", "(promotion|graduation|championship|trophy|award)");
        
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