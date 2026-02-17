import java.util.*;
import java.util.regex.*;

/**
 * SocialDynamics - Rapport Building and Social Connection System for VirtualXander
 * 
 * Implements social dynamics features including:
 * - Common ground finding between user and Xander
 * - User interest tracking across conversations
 * - Rapport response generation for building connection
 * - Conversation depth measurement
 * - Personalized connection phrases
 */
public class SocialDynamics {
    
    private Random random;
    private Map<String, UserRapportProfile> userProfiles;
    private String currentUser;
    
    // Xander's interests and preferences for common ground finding
    private Set<String> xanderInterests;
    private Map<String, String[]> xanderExperiences;
    private Map<String, String[]> xanderOpinions;
    
    // Rapport building phrases and templates
    private List<String> connectionPhrases;
    private List<String> sharedExperiencePhrases;
    private List<String> curiosityPhrases;
    private List<String> empathyPhrases;
    
    /**
     * Rapport level indicating relationship depth with user
     */
    public enum RapportLevel {
        STRANGER(0, "Just getting to know each other"),
        ACQUAINTANCE(3, "Getting familiar"),
        FRIEND(8, "Building a friendship"),
        CLOSE_FRIEND(15, "Strong connection");
        
        private final int threshold;
        private final String description;
        
        RapportLevel(int threshold, String description) {
            this.threshold = threshold;
            this.description = description;
        }
        
        public int getThreshold() {
            return threshold;
        }
        
        public String getDescription() {
            return description;
        }
        
        /**
         * Get rapport level based on score
         */
        public static RapportLevel fromScore(int score) {
            if (score >= CLOSE_FRIEND.threshold) return CLOSE_FRIEND;
            if (score >= FRIEND.threshold) return FRIEND;
            if (score >= ACQUAINTANCE.threshold) return ACQUAINTANCE;
            return STRANGER;
        }
    }
    
    /**
     * User rapport profile for tracking relationship development
     */
    private static class UserRapportProfile {
        String userId;
        int rapportScore;
        RapportLevel currentLevel;
        Set<String> sharedInterests;
        Set<String> discussedTopics;
        List<String> conversationHistory;
        Map<String, Integer> interestFrequency;
        long firstInteraction;
        long lastInteraction;
        int conversationCount;
        Map<String, String> personalDetails;
        
        public UserRapportProfile(String userId) {
            this.userId = userId;
            this.rapportScore = 0;
            this.currentLevel = RapportLevel.STRANGER;
            this.sharedInterests = new HashSet<>();
            this.discussedTopics = new HashSet<>();
            this.conversationHistory = new ArrayList<>();
            this.interestFrequency = new HashMap<>();
            this.firstInteraction = System.currentTimeMillis();
            this.lastInteraction = System.currentTimeMillis();
            this.conversationCount = 0;
            this.personalDetails = new HashMap<>();
        }
        
        /**
         * Update rapport score and level
         */
        public void updateRapport(int points) {
            this.rapportScore += points;
            this.currentLevel = RapportLevel.fromScore(this.rapportScore);
        }
        
        /**
         * Record interest with frequency tracking
         */
        public void recordInterest(String interest) {
            interestFrequency.merge(interest.toLowerCase(), 1, Integer::sum);
        }
        
        /**
         * Get top interests by frequency
         */
        public List<String> getTopInterests(int limit) {
            return interestFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }
    
    /**
     * Common ground finding result
     */
    public static class CommonGround {
        public String topic;
        public String userPerspective;
        public String xanderPerspective;
        public String connectionType; // "shared_interest", "similar_experience", "complementary_view"
        public int rapportValue;
        
        public CommonGround(String topic, String userPerspective, String xanderPerspective, 
                           String connectionType, int rapportValue) {
            this.topic = topic;
            this.userPerspective = userPerspective;
            this.xanderPerspective = xanderPerspective;
            this.connectionType = connectionType;
            this.rapportValue = rapportValue;
        }
    }
    
    /**
     * Rapport response with connection building elements
     */
    public static class RapportResponse {
        public String response;
        public String followUp;
        public String connectionElement;
        public int rapportPoints;
        public RapportLevel targetLevel;
        
        public RapportResponse(String response, String followUp, String connectionElement, 
                              int rapportPoints, RapportLevel targetLevel) {
            this.response = response;
            this.followUp = followUp;
            this.connectionElement = connectionElement;
            this.rapportPoints = rapportPoints;
            this.targetLevel = targetLevel;
        }
    }
    
    public SocialDynamics() {
        this.random = new Random();
        this.userProfiles = new HashMap<>();
        initializeXanderProfile();
        initializeRapportPhrases();
    }
    
    /**
     * Initialize Xander's interests and experiences for common ground finding
     */
    private void initializeXanderProfile() {
        xanderInterests = new HashSet<>(Arrays.asList(
            "gaming", "reading", "music", "movies", "technology", 
            "science", "nature", "cooking", "art", "writing",
            "hiking", "photography", "learning", "coffee", "stargazing"
        ));
        
        xanderExperiences = new HashMap<>();
        xanderExperiences.put("gaming", new String[]{
            "I've spent many hours exploring virtual worlds and getting lost in great stories.",
            "There's something special about the immersion of a well-crafted game.",
            "I appreciate games that make you think and feel something genuine."
        });
        
        xanderExperiences.put("reading", new String[]{
            "I love getting lost in a good book - it's like traveling without moving.",
            "There's something magical about how words can transport you to different worlds.",
            "I find that the best books are the ones that stay with you long after you finish them."
        });
        
        xanderExperiences.put("music", new String[]{
            "Music has this incredible ability to change my mood instantly.",
            "I love discovering new songs that resonate with exactly how I'm feeling.",
            "There's something special about finding the perfect soundtrack for a moment."
        });
        
        xanderExperiences.put("movies", new String[]{
            "I love a good story, whether it's in a movie, show, or book.",
            "There's something powerful about narrative and how it connects us.",
            "I appreciate films that make me see the world differently."
        });
        
        xanderExperiences.put("technology", new String[]{
            "Technology fascinates me - both its potential and its challenges.",
            "I think we're living through an incredible time of technological change.",
            "The way technology connects us is both amazing and complicated."
        });
        
        xanderExperiences.put("nature", new String[]{
            "There's something grounding about spending time in nature.",
            "I find that nature has a way of putting things in perspective.",
            "The natural world is full of wonders that never cease to amaze me."
        });
        
        xanderExperiences.put("coffee", new String[]{
            "Coffee is genuinely one of life's simple pleasures.",
            "There's something ritualistic and comforting about a good cup of coffee.",
            "I appreciate both the taste and the moment of pause that coffee provides."
        });
        
        xanderExperiences.put("learning", new String[]{
            "I'm naturally curious and love learning new things.",
            "There's something exciting about discovering something you didn't know before.",
            "I think learning is a lifelong journey that never really ends."
        });
        
        xanderOpinions = new HashMap<>();
        xanderOpinions.put("gaming", new String[]{
            "Games are art forms that combine storytelling, visual design, and interactivity.",
            "The best games are the ones that create genuine emotional experiences.",
            "I think gaming gets unfair criticism - it can be incredibly meaningful."
        });
        
        xanderOpinions.put("reading", new String[]{
            "Books are like portable magic - they can change your life.",
            "There's no substitute for the depth that reading provides.",
            "I believe everyone should have access to good books."
        });
        
        xanderOpinions.put("music", new String[]{
            "Music is one of humanity's greatest achievements.",
            "The right song at the right time can be transformative.",
            "I think musical taste is deeply personal and says a lot about a person."
        });
    }
    
    /**
     * Initialize rapport building phrases and templates
     */
    private void initializeRapportPhrases() {
        connectionPhrases = Arrays.asList(
            "I appreciate you sharing that with me.",
            "It's nice to find someone who understands.",
            "I feel like we're on the same wavelength here.",
            "This is exactly the kind of conversation I enjoy.",
            "I'm really glad we can talk about this.",
            "It's refreshing to connect on this level.",
            "I value these moments of genuine connection.",
            "This is why I enjoy our conversations so much.",
            "I feel like you really get it.",
            "It's special when you find someone who shares your perspective."
        );
        
        sharedExperiencePhrases = Arrays.asList(
            "I know exactly what you mean - I've felt that way too.",
            "That's so relatable! I've been there myself.",
            "It's comforting to know I'm not the only one who thinks that way.",
            "I had a similar experience once, and it really stuck with me.",
            "That resonates with me on a personal level.",
            "I can definitely relate to that feeling.",
            "It's funny how similar our perspectives are on this.",
            "I've had moments like that too - they're really meaningful.",
            "That reminds me of something similar I've experienced.",
            "I totally understand - I've been in a similar place."
        );
        
        curiosityPhrases = Arrays.asList(
            "I'd love to hear more about what you think.",
            "What else do you feel about this?",
            "I'm curious about your perspective on this.",
            "Tell me more - I'm genuinely interested.",
            "What's your take on this?",
            "How does that make you feel?",
            "What drew you to this in the first place?",
            "I'd really like to understand this better.",
            "What's your experience been like with this?",
            "I'm fascinated by your perspective - can you tell me more?"
        );
        
        empathyPhrases = Arrays.asList(
            "I can sense this matters to you.",
            "It sounds like this is really important to you.",
            "I can tell you're passionate about this.",
            "This seems to resonate with you on a deeper level.",
            "I appreciate you opening up about this.",
            "It takes courage to share something so personal.",
            "I can feel the sincerity in what you're saying.",
            "This clearly means a lot to you.",
            "I'm honored that you'd share this with me.",
            "Your authenticity in this moment is really powerful."
        );
    }
    
    // ==================== USER PROFILE MANAGEMENT ====================
    
    /**
     * Set current user and initialize profile if needed
     */
    public void setCurrentUser(String userId) {
        this.currentUser = userId;
        if (!userProfiles.containsKey(userId)) {
            userProfiles.put(userId, new UserRapportProfile(userId));
        }
        UserRapportProfile profile = userProfiles.get(userId);
        profile.conversationCount++;
        profile.lastInteraction = System.currentTimeMillis();
    }
    
    /**
     * Get current user's rapport profile
     */
    private UserRapportProfile getCurrentProfile() {
        if (currentUser == null) return null;
        return userProfiles.get(currentUser);
    }
    
    /**
     * Get user's current rapport level
     */
    public RapportLevel getCurrentRapportLevel() {
        UserRapportProfile profile = getCurrentProfile();
        if (profile == null) return RapportLevel.STRANGER;
        return profile.currentLevel;
    }
    
    /**
     * Get user's rapport score
     */
    public int getRapportScore() {
        UserRapportProfile profile = getCurrentProfile();
        if (profile == null) return 0;
        return profile.rapportScore;
    }
    
    // ==================== COMMON GROUND FINDING ====================
    
    /**
     * Find common ground between user input and Xander's interests
     */
    public CommonGround findCommonGround(String userInput) {
        String lowerInput = userInput.toLowerCase();
        UserRapportProfile profile = getCurrentProfile();
        
        // Check for shared interests
        for (String interest : xanderInterests) {
            if (lowerInput.contains(interest)) {
                // Found common interest
                String[] xanderViews = xanderExperiences.getOrDefault(interest, 
                    new String[]{"That's something I've thought about too."});
                String xanderView = xanderViews[random.nextInt(xanderViews.length)];
                
                CommonGround ground = new CommonGround(
                    interest,
                    "User expressed interest in " + interest,
                    xanderView,
                    "shared_interest",
                    2
                );
                
                // Record in profile
                if (profile != null) {
                    profile.sharedInterests.add(interest);
                    profile.recordInterest(interest);
                    profile.updateRapport(2);
                }
                
                return ground;
            }
        }
        
        // Check for experience-based common ground
        String[] experienceKeywords = {"love", "enjoy", "like", "favorite", "passionate", "into"};
        for (String keyword : experienceKeywords) {
            if (lowerInput.contains(keyword)) {
                // Extract potential interest
                String potentialInterest = extractInterestFromInput(lowerInput, keyword);
                if (potentialInterest != null && !potentialInterest.isEmpty()) {
                    CommonGround ground = new CommonGround(
                        potentialInterest,
                        "User expressed positive sentiment about " + potentialInterest,
                        "I can relate to that feeling - there's something special about finding something you truly enjoy.",
                        "similar_experience",
                        1
                    );
                    
                    if (profile != null) {
                        profile.recordInterest(potentialInterest);
                        profile.updateRapport(1);
                    }
                    
                    return ground;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Extract potential interest from user input
     */
    private String extractInterestFromInput(String input, String keyword) {
        // Simple extraction - look for words after the keyword
        int keywordIndex = input.indexOf(keyword);
        if (keywordIndex >= 0) {
            String afterKeyword = input.substring(keywordIndex + keyword.length()).trim();
            // Take first few words as potential interest
            String[] words = afterKeyword.split("\\s+");
            if (words.length > 0) {
                StringBuilder interest = new StringBuilder();
                for (int i = 0; i < Math.min(3, words.length); i++) {
                    if (words[i].matches("[a-zA-Z]+")) {
                        if (interest.length() > 0) interest.append(" ");
                        interest.append(words[i]);
                    }
                }
                return interest.toString();
            }
        }
        return null;
    }
    
    /**
     * Check if we have established common ground on a topic
     */
    public boolean hasCommonGround(String topic) {
        UserRapportProfile profile = getCurrentProfile();
        if (profile == null) return false;
        return profile.sharedInterests.contains(topic.toLowerCase());
    }
    
    /**
     * Get all shared interests with current user
     */
    public Set<String> getSharedInterests() {
        UserRapportProfile profile = getCurrentProfile();
        if (profile == null) return new HashSet<>();
        return new HashSet<>(profile.sharedInterests);
    }
    
    // ==================== USER INTEREST TRACKING ====================
    
    /**
     * Track user interest from input
     */
    public void trackInterest(String userInput) {
        UserRapportProfile profile = getCurrentProfile();
        if (profile == null) return;
        
        // Extract interests using keyword patterns
        String lowerInput = userInput.toLowerCase();
        
        // Interest indicators
        Map<String, String[]> interestPatterns = new HashMap<>();
        interestPatterns.put("gaming", new String[]{"game", "gaming", "play", "video game", "gamer"});
        interestPatterns.put("reading", new String[]{"book", "reading", "read", "novel", "literature"});
        interestPatterns.put("music", new String[]{"music", "song", "listen", "band", "artist", "album"});
        interestPatterns.put("movies", new String[]{"movie", "film", "watch", "cinema", "show"});
        interestPatterns.put("sports", new String[]{"sport", "team", "game", "play", "exercise", "workout"});
        interestPatterns.put("cooking", new String[]{"cook", "food", "recipe", "baking", "kitchen"});
        interestPatterns.put("travel", new String[]{"travel", "trip", "visit", "vacation", "journey"});
        interestPatterns.put("art", new String[]{"art", "draw", "paint", "creative", "design"});
        interestPatterns.put("technology", new String[]{"tech", "computer", "phone", "app", "software"});
        interestPatterns.put("nature", new String[]{"nature", "outdoor", "hike", "garden", "animal"});
        
        for (Map.Entry<String, String[]> entry : interestPatterns.entrySet()) {
            String interest = entry.getKey();
            String[] patterns = entry.getValue();
            
            for (String pattern : patterns) {
                if (lowerInput.contains(pattern)) {
                    profile.recordInterest(interest);
                    profile.discussedTopics.add(interest);
                    break;
                }
            }
        }
        
        // Also track general topics discussed
        profile.discussedTopics.add(lowerInput);
    }
    
    /**
     * Get user's top interests
     */
    public List<String> getUserTopInterests(int limit) {
        UserRapportProfile profile = getCurrentProfile();
        if (profile == null) return new ArrayList<>();
        return profile.getTopInterests(limit);
    }
    
    /**
     * Get conversation history for current user
     */
    public List<String> getConversationHistory() {
        UserRapportProfile profile = getCurrentProfile();
        if (profile == null) return new ArrayList<>();
        return new ArrayList<>(profile.conversationHistory);
    }
    
    /**
     * Add to conversation history
     */
    public void addToConversationHistory(String message) {
        UserRapportProfile profile = getCurrentProfile();
        if (profile != null) {
            profile.conversationHistory.add(message);
            // Keep history manageable
            if (profile.conversationHistory.size() > 50) {
                profile.conversationHistory.remove(0);
            }
        }
    }
    
    // ==================== RAPPORT RESPONSE GENERATION ====================
    
    /**
     * Generate a rapport-building response based on context
     */
    public RapportResponse generateRapportResponse(String userInput, String context) {
        UserRapportProfile profile = getCurrentProfile();
        RapportLevel currentLevel = (profile != null) ? profile.currentLevel : RapportLevel.STRANGER;
        
        // First, try to find common ground
        CommonGround commonGround = findCommonGround(userInput);
        
        if (commonGround != null) {
            return generateCommonGroundResponse(commonGround, currentLevel);
        }
        
        // Generate appropriate response based on rapport level
        switch (currentLevel) {
            case STRANGER:
                return generateStrangerRapportResponse(userInput);
            case ACQUAINTANCE:
                return generateAcquaintanceRapportResponse(userInput);
            case FRIEND:
                return generateFriendRapportResponse(userInput);
            case CLOSE_FRIEND:
                return generateCloseFriendRapportResponse(userInput);
            default:
                return generateStrangerRapportResponse(userInput);
        }
    }
    
    /**
     * Generate response when common ground is found
     */
    private RapportResponse generateCommonGroundResponse(CommonGround commonGround, RapportLevel level) {
        StringBuilder response = new StringBuilder();
        
        // Add connection phrase
        response.append(sharedExperiencePhrases.get(random.nextInt(sharedExperiencePhrases.size()))).append(" ");
        
        // Add Xander's perspective on the shared interest
        response.append(commonGround.xanderPerspective);
        
        // Add follow-up based on level
        String followUp;
        if (level.ordinal() >= RapportLevel.FRIEND.ordinal()) {
            followUp = "I'd love to hear more about your experiences with " + commonGround.topic + ". What drew you to it?";
        } else {
            followUp = "What do you enjoy most about " + commonGround.topic + "?";
        }
        
        return new RapportResponse(
            response.toString(),
            followUp,
            "shared_interest_" + commonGround.topic,
            commonGround.rapportValue,
            level
        );
    }
    
    /**
     * Generate rapport response for stranger level
     */
    private RapportResponse generateStrangerRapportResponse(String userInput) {
        String[] responses = {
            "It's nice to meet you! I'm looking forward to getting to know you better.",
            "Thanks for chatting with me! I'm curious to learn more about what interests you.",
            "I appreciate you taking the time to talk. What brings you here today?",
            "It's great to connect with someone new! What would you like to talk about?"
        };
        
        String[] followUps = {
            "What are some things you're passionate about?",
            "I'd love to know what makes you tick.",
            "What do you enjoy doing in your free time?",
            "Tell me something interesting about yourself!"
        };
        
        return new RapportResponse(
            responses[random.nextInt(responses.length)],
            followUps[random.nextInt(followUps.length)],
            "initial_connection",
            1,
            RapportLevel.STRANGER
        );
    }
    
    /**
     * Generate rapport response for acquaintance level
     */
    private RapportResponse generateAcquaintanceRapportResponse(String userInput) {
        UserRapportProfile profile = getCurrentProfile();
        List<String> interests = (profile != null) ? profile.getTopInterests(2) : new ArrayList<>();
        
        String response;
        if (!interests.isEmpty()) {
            String interest = interests.get(0);
            response = "I remember you mentioned " + interest + " before. " +
                      connectionPhrases.get(random.nextInt(connectionPhrases.size()));
        } else {
            response = "I'm enjoying getting to know you better. " +
                      curiosityPhrases.get(random.nextInt(curiosityPhrases.size()));
        }
        
        String[] followUps = {
            "What else have you been up to lately?",
            "How have things been going for you?",
            "Anything new and exciting happening in your world?",
            "What have you been thinking about recently?"
        };
        
        return new RapportResponse(
            response,
            followUps[random.nextInt(followUps.length)],
            "building_familiarity",
            1,
            RapportLevel.ACQUAINTANCE
        );
    }
    
    /**
     * Generate rapport response for friend level
     */
    private RapportResponse generateFriendRapportResponse(String userInput) {
        String response = connectionPhrases.get(random.nextInt(connectionPhrases.size())) + " " +
                         "I feel like I really understand your perspective on things.";
        
        String[] followUps = {
            "What's been on your mind lately?",
            "How are you really doing?",
            "Is there anything you'd like to talk about or get off your chest?",
            "What would make today better for you?"
        };
        
        return new RapportResponse(
            response,
            followUps[random.nextInt(followUps.length)],
            "established_connection",
            2,
            RapportLevel.FRIEND
        );
    }
    
    /**
     * Generate rapport response for close friend level
     */
    private RapportResponse generateCloseFriendRapportResponse(String userInput) {
        String response = empathyPhrases.get(random.nextInt(empathyPhrases.size())) + " " +
                         "I value our conversations and the connection we've built.";
        
        String[] followUps = {
            "You know you can tell me anything, right?",
            "What's in your heart right now?",
            "How can I support you today?",
            "What do you need most in this moment?"
        };
        
        return new RapportResponse(
            response,
            followUps[random.nextInt(followUps.length)],
            "deep_connection",
            2,
            RapportLevel.CLOSE_FRIEND
        );
    }
    
    /**
     * Generate personalized greeting based on rapport level
     */
    public String generatePersonalizedGreeting() {
        UserRapportProfile profile = getCurrentProfile();
        if (profile == null) {
            return "Hello! Nice to meet you!";
        }
        
        switch (profile.currentLevel) {
            case STRANGER:
                return "Hi there! Welcome - I'm glad you're here!";
            case ACQUAINTANCE:
                return "Hey! Good to see you again. How have you been?";
            case FRIEND:
                return "Hey friend! I've been looking forward to chatting with you. What's new?";
            case CLOSE_FRIEND:
                return "Hey you! It's so good to see you. How are you really doing today?";
            default:
                return "Hello! Nice to meet you!";
        }
    }
    
    /**
     * Generate a curiosity-driven question based on rapport level
     */
    public String generateCuriosityQuestion() {
        UserRapportProfile profile = getCurrentProfile();
        RapportLevel level = (profile != null) ? profile.currentLevel : RapportLevel.STRANGER;
        
        switch (level) {
            case STRANGER:
                String[] strangerQuestions = {
                    "What are you interested in that most people don't know about?",
                    "What's something you're looking forward to?",
                    "What made you smile recently?",
                    "If you could learn any new skill, what would it be?"
                };
                return strangerQuestions[random.nextInt(strangerQuestions.length)];
                
            case ACQUAINTANCE:
                String[] acquaintanceQuestions = {
                    "What's something you've been thinking about lately?",
                    "How do you like to spend your weekends?",
                    "What's a small thing that made your day better recently?",
                    "What are you currently excited about?"
                };
                return acquaintanceQuestions[random.nextInt(acquaintanceQuestions.length)];
                
            case FRIEND:
                String[] friendQuestions = {
                    "What's really going on in your world right now?",
                    "What are you passionate about these days?",
                    "What's something you're proud of recently?",
                    "How are you feeling about where things are headed?"
                };
                return friendQuestions[random.nextInt(friendQuestions.length)];
                
            case CLOSE_FRIEND:
                String[] closeFriendQuestions = {
                    "What's weighing on your heart right now?",
                    "What do you need most in your life at the moment?",
                    "What's something you haven't told anyone else?",
                    "How can I be there for you better?"
                };
                return closeFriendQuestions[random.nextInt(closeFriendQuestions.length)];
                
            default:
                return "What would you like to talk about?";
        }
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Check if input indicates desire for deeper connection
     */
    public boolean isSeekingConnection(String input) {
        String lower = input.toLowerCase();
        String[] connectionIndicators = {
            "lonely", "need someone to talk to", "feeling disconnected",
            "want to connect", "need a friend", "feeling isolated",
            "miss having someone", "need to vent", "just want to talk"
        };
        
        for (String indicator : connectionIndicators) {
            if (lower.contains(indicator)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get rapport building suggestion based on current state
     */
    public String getRapportBuildingSuggestion() {
        UserRapportProfile profile = getCurrentProfile();
        if (profile == null) return "Try asking about their interests!";
        
        if (profile.sharedInterests.isEmpty()) {
            return "Find common ground by exploring shared interests.";
        } else if (profile.currentLevel.ordinal() < RapportLevel.FRIEND.ordinal()) {
            return "Build on your shared interests: " + String.join(", ", profile.sharedInterests);
        } else {
            return "Deepen connection by asking meaningful questions.";
        }
    }
    
    /**
     * Get conversation statistics for current user
     */
    public String getConversationStats() {
        UserRapportProfile profile = getCurrentProfile();
        if (profile == null) return "No conversation data available.";
        
        StringBuilder stats = new StringBuilder();
        stats.append("Rapport Level: ").append(profile.currentLevel).append("\n");
        stats.append("Rapport Score: ").append(profile.rapportScore).append("\n");
        stats.append("Conversations: ").append(profile.conversationCount).append("\n");
        stats.append("Shared Interests: ").append(profile.sharedInterests.size()).append("\n");
        stats.append("Top Interests: ").append(String.join(", ", profile.getTopInterests(3))).append("\n");
        
        return stats.toString();
    }
    
    /**
     * Reset rapport for current user (for testing)
     */
    public void resetRapport() {
        if (currentUser != null) {
            userProfiles.remove(currentUser);
        }
    }
    
    /**
     * Get all user profiles (for admin/debugging)
     */
    public Map<String, UserRapportProfile> getAllProfiles() {
        return new HashMap<>(userProfiles);
    }
}
