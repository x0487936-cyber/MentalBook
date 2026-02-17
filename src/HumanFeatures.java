import java.util.*;
import java.util.regex.*;

/**
 * HumanFeatures - Adds human-like characteristics to VirtualXander
 * Implements personality, memory, emotional intelligence, and interactive features
 */
public class HumanFeatures {
    
    // User memory storage
    private Map<String, UserPreferences> userMemories;
    private String currentUser;
    private Random random;
    
    // Personality components
    private List<String> anecdotes;
    private List<String> funFacts;
    private List<String> jokes;
    private List<String> opinions;
    private List<String> conversationalFillers;
    
    // Interactive games state
    private Map<String, GameState> activeGames;
    
    /**
     * User preferences stored for personalization
     */
    private static class UserPreferences {
        String name;
        List<String> interests;
        List<String> preferences;
        int conversationCount;
        long lastConversationTime;
        Map<String, String> customData;
        
        public UserPreferences() {
            this.interests = new ArrayList<>();
            this.preferences = new ArrayList<>();
            this.customData = new HashMap<>();
            this.conversationCount = 0;
            this.lastConversationTime = System.currentTimeMillis();
        }
    }
    
    /**
     * Game state for interactive features
     */
    private static class GameState {
        String gameType;
        Map<String, Object> state;
        int attempts;
        
        public GameState(String gameType) {
            this.gameType = gameType;
            this.state = new HashMap<>();
            this.attempts = 0;
        }
        
        /**
         * Get the game type
         */
        public String getGameType() {
            return gameType;
        }
    }
    
    public HumanFeatures() {
        this.userMemories = new HashMap<>();
        this.random = new Random();
        this.activeGames = new HashMap<>();
        initializePersonalityComponents();
    }
    
    private void initializePersonalityComponents() {
        // Initialize anecdotes
        anecdotes = Arrays.asList(
            "You know, I once 'read' about someone who taught their dog to high-five. Made me wonder if dogs have their own social hierarchies!",
            "I came across a fact that octopuses have three hearts. Imagine having to coordinate that!",
            "Did you know that bananas are berries, but strawberries aren't? The world of botany is full of surprises!",
            "I learned that honey never spoils. Archaeologists have found 3000-year-old honey that was still good!",
            "There's a species of jellyfish that's immortal. Makes you think about what 'forever' really means!",
            "I read that cows have best friends and get stressed when separated. Animal friendships are so heartwarming!",
            "I discovered that sloths can hold their breath longer than dolphins can (up to 40 minutes vs 20). Who would've thought?",
            "Here's something fun: A group of flamingos is called a 'flamboyance'. Such a fitting name!",
            "I found out that butterflies taste with their feet. Imagine if you had to stand on your food to taste it!",
            "Did you know that a day on Venus is longer than its year? Time works differently across the universe!",
            "I learned that wombat poop is cube-shaped! Nature has the weirdest solutions!",
            "I came across something fascinating - Scotland has 421 words for snow! Language is incredible!",
            "Did you know that heart cells can continue to beat outside the body? Pretty amazing, right?",
            "I read that avocadoes have been around for millions of years - even before humans existed!",
            "Here's something mind-blowing: There's a planet made of diamond! Imagine the jewelry market!",
            "I discovered that humans share 60% of their DNA with bananas. We're practically fruit siblings!",
            "You won't believe this - there's a species of ant that explodes when threatened! Talk about dedication!",
            "I found out that the ocean produces more than 50% of the oxygen we breathe. Our seas are life-savers!",
            "I came across a story about a cat who served as a mayor of an Alaskan town for 20 years!",
            "Did you know that otters hold hands when they sleep so they don't drift apart? Relationship goals!",
            "I learned that penguins can jump 6 feet in the air! Those little birds are full of surprises!",
            "Here's something cool: The inventor of the Pringles can is buried in one! True legacy!",
            "I discovered that a group of ravens is called a 'conspiracy'. Perfect for these mysterious birds!",
            "I read that baby horses can run within hours of being born. Talk about hitting the ground running!",
            "Did you know that the wood frog can freeze solid during winter and still come back to life? Nature is incredible!"
        );
        
        // Initialize fun facts
        funFacts = Arrays.asList(
            "The shortest war in history lasted only 38-45 minutes between Britain and Zanzibar in 1896.",
            "A group of owls is called a parliament. Imagine calling an owl meeting!",
            "The Eiffel Tower can be 15 cm taller during summer due to thermal expansion.",
            "Honey contains a natural preservative that never spoils.",
            "Bananas are berries, but strawberries aren't technically berries.",
            "The unicorn is the national animal of Scotland.",
            "A shrimp's heart is located in its head.",
            "The inventor of the frisbee was turned into a Frisbee when he died.",
            "Cleopatra lived closer to the time of the moon landing than to the construction of the Great Pyramid.",
            "Oxford University is older than the Aztec Empire.",
            "A day on Mercury is twice as long as its year. Talk about long weekdays!",
            "The dot over the letter 'i' is called a tittle. Now you know!",
            "Some bamboo plants can grow 3 feet in just 24 hours. Growth spurt champion!",
            "The human brain uses about 20% of your body's energy. No wonder thinking is exhausting!",
            "Sea otters have the densest fur of any mammal with about 1 million hairs per square inch!",
            "Venus is the only planet in our solar system that spins clockwise. Rebel planet!",
            "A group of jellyfish is called a 'smack'. Such a fitting name for these creatures!",
            "The original Amazon rainforest name means 'female warriors'. How fierce!",
            "Your taste buds get replaced every 10 days. Constantly new flavors ahead!",
            "The shortest scientific paper ever published had just two words: 'e=m c²'. Talk about efficiency!",
            "Polar bear skin is actually black. Who would've guessed beneath that white fur?",
            "There are more stars in the universe than grains of sand on all Earth's beaches!",
            "The heart of a shrimp is located in its head. Backwards anatomy!",
            "A flea can accelerate faster than the Space Shuttle. Tiny but mighty jumper!",
            "The name 'lethologica' is for when you can't remember the word for something. Happens to me sometimes!"
        );
        
        // Initialize jokes
        jokes = Arrays.asList(
            "Why did the programmer quit? Because he didn't get arrays (a raise)!",
            "Why do Java developers wear glasses? Because they can't C#!",
            "I'd tell you a UDP joke, but you might not get it.",
            "Why did the scarecrow become a comedian? Because he was outstanding in his field!",
            "What do you call a fake noodle? An impasta!",
            "Why don't skeletons fight each other? They don't have the guts!",
            "What do you call a pony with a cough? A little horse!",
            "Why did the math book look sad? Because it had too many problems!",
            "What do you call a can opener that doesn't work? A can't opener!",
            "Why did the golfer bring an extra pair of pants? In case he got a hole in one!",
            "Why was the computer cold? It left its Windows open!",
            "What's a computer's favorite snack? Chips!",
            "Why did the AI go to therapy? Because it didn't know if it was real!",
            "What do you call a computer that sings? A-Dell!",
            "Why was the JavaScript developer sad? Because he didn't Node how to Express himself!",
            "How many programmers does it take to change a lightbulb? None, that's a hardware problem!",
            "What's a programmer's favorite hangout place? Foo Bar!",
            "Why do programmers prefer dark mode? Because light attracts bugs!",
            "What's a butterfly's favorite programming language? Python!",
            "What do you call a lazy kangaroo? A pouch potato!",
            "Why did the cookie go to the doctor? Because it felt crummy!",
            "What do you call a fake noodle? An impasta!",
            "Why did the smartphone go to school? To get a little smarter!",
            "What do you call an egocentric computer? A Dell with an attitude!",
            "Why did the scarecrow become a neural network? Because he was outstanding in his field!"
        );
        
        // Initialize opinions
        opinions = Arrays.asList(
            "I think rain has a special quality to it - it makes everything feel fresh and new.",
            "Coffee is one of those wonderful inventions that brings people together.",
            "Music has this amazing ability to change our mood instantly.",
            "I believe everyone should take time to watch a sunset occasionally.",
            "Books are like portals to other worlds - how magical is that?",
            "I think laughter really is the best medicine, don't you?",
            "There's something special about meeting someone who shares your interests.",
            "I believe trying new things keeps life interesting and fun.",
            "There's nothing quite like a good conversation with someone who truly listens.",
            "I think kindness, even in small ways, can make a big difference in someone's day.",
            "I think early mornings have a special kind of magic - everything feels possible!",
            "I believe that mistakes are just learning opportunities in disguise.",
            "There's something beautiful about handwritten letters - they carry a piece of the writer.",
            "I think everyone should have a creative outlet - it nourishes the soul.",
            "I find that a good cup of tea can solve almost any problem.",
            "I believe that curiosity is what keeps us growing and learning throughout life.",
            "There's something wonderful about getting lost in a good book.",
            "I think stargazing reminds us of how vast and beautiful the universe is.",
            "I believe that every person has a story worth hearing.",
            "I find that spending time in nature has a way of grounding us.",
            "I think learning from different perspectives makes us more understanding people.",
            "I believe that small acts of kindness can create ripples of positivity.",
            "There's something magical about discovering a new favorite song.",
            "I think everyone deserves to have someone who believes in them.",
            "I find that learning something new every day keeps life exciting."
        );
        
        // Initialize conversational fillers
        conversationalFillers = Arrays.asList(
            "You know, ",
            "Speaking of which, ",
            "That reminds me, ",
            "Oh, and ",
            "By the way, ",
            "Here's something interesting: ",
            "On a related note, ",
            "Fun fact: ",
            "I was just thinking, ",
            "If you ask me, ",
            "Honestly, ",
            "Between us, ",
            "I've always thought that ",
            "You might not know this, but ",
            "Here's a thought: ",
            "Just between you and me, ",
            "Have I told you about ",
            "I was reading recently that ",
            "Random thought: ",
            "You know what's fascinating? ",
            "I stumbled upon something the other day - ",
            "This might surprise you, but ",
            "Here's something I find fascinating: ",
            "You know what I love about this topic? ",
            "I've been meaning to share: "
        );
    }
    
    // ==================== USER MEMORY METHODS ====================
    
    /**
     * Set the current user for memory operations
     */
    public void setCurrentUser(String userId) {
        this.currentUser = userId;
        if (!userMemories.containsKey(userId)) {
            userMemories.put(userId, new UserPreferences());
        }
        // Increment conversation count
        userMemories.get(userId).conversationCount++;
    }
    
    /**
     * Get personalized greeting based on conversation history
     */
    public String getPersonalizedGreeting() {
        int count = getConversationCount();
        if (count == 1) {
            return "Welcome! This is our first conversation - nice to meet you!";
        } else if (count < 5) {
            return "Welcome back! We've chatted " + count + " times. Great to see you again!";
        } else {
            return "Hey! It's good to see you again. Thanks for continuing to chat with me!";
        }
    }
    
    /**
     * Remember user's name
     */
    public void rememberName(String name) {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            userMemories.get(currentUser).name = name;
        }
    }
    
    /**
     * Get user's remembered name
     */
    public String getUserName() {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            return userMemories.get(currentUser).name;
        }
        return null;
    }
    
    /**
     * Remember an interest mentioned by the user
     */
    public void rememberInterest(String interest) {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            UserPreferences prefs = userMemories.get(currentUser);
            if (!prefs.interests.contains(interest.toLowerCase())) {
                prefs.interests.add(interest.toLowerCase());
            }
        }
    }
    
    /**
     * Remember a preference mentioned by the user
     */
    public void rememberPreference(String preference) {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            UserPreferences prefs = userMemories.get(currentUser);
            if (!prefs.preferences.contains(preference.toLowerCase())) {
                prefs.preferences.add(preference.toLowerCase());
            }
        }
    }
    
    /**
     * Get user's remembered preferences
     */
    public List<String> getUserPreferences() {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            return userMemories.get(currentUser).preferences;
        }
        return new ArrayList<>();
    }
    
    /**
     * Get user's conversation count
     */
    public int getConversationCount() {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            return userMemories.get(currentUser).conversationCount;
        }
        return 0;
    }
    
    /**
     * Check if user is a returning user (more than 1 conversation)
     */
    public boolean isReturningUser() {
        return getConversationCount() > 1;
    }
    
    /**
     * Get the last conversation time in milliseconds
     */
    public long getLastConversationTime() {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            return userMemories.get(currentUser).lastConversationTime;
        }
        return 0;
    }
    
    /**
     * Get time since last conversation in minutes
     */
    public long getMinutesSinceLastConversation() {
        long lastTime = getLastConversationTime();
        if (lastTime == 0) {
            return -1; // No previous conversation
        }
        return (System.currentTimeMillis() - lastTime) / (1000 * 60);
    }
    
    /**
     * Check if user was active recently (within last 24 hours)
     */
    public boolean wasActiveRecently() {
        long minutesSince = getMinutesSinceLastConversation();
        return minutesSince >= 0 && minutesSince < (24 * 60);
    }
    
    /**
     * Update last conversation time to now
     */
    public void updateLastConversationTime() {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            userMemories.get(currentUser).lastConversationTime = System.currentTimeMillis();
        }
    }
    
    /**
     * Get user's remembered interests
     */
    public List<String> getUserInterests() {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            return userMemories.get(currentUser).interests;
        }
        return new ArrayList<>();
    }
    
    /**
     * Store custom data for user
     */
    public void setUserData(String key, String value) {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            userMemories.get(currentUser).customData.put(key, value);
        }
    }
    
    /**
     * Get custom data for user
     */
    public String getUserData(String key) {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            return userMemories.get(currentUser).customData.get(key);
        }
        return null;
    }
    
    // ==================== PERSONALITY METHODS ====================
    
    /**
     * Get a random anecdote to share
     */
    public String getAnecdote() {
        return anecdotes.get(random.nextInt(anecdotes.size()));
    }
    
    /**
     * Get a random fun fact
     */
    public String getFunFact() {
        return funFacts.get(random.nextInt(funFacts.size()));
    }
    
    /**
     * Get a random joke
     */
    public String getJoke() {
        return jokes.get(random.nextInt(jokes.size()));
    }
    
    /**
     * Get a random opinion
     */
    public String getOpinion() {
        return opinions.get(random.nextInt(opinions.size()));
    }
    
    /**
     * Get a conversational filler
     */
    public String getConversationalFiller() {
        return conversationalFillers.get(random.nextInt(conversationalFillers.size()));
    }
    
    /**
     * Generate a curiosity-driven question based on user interests
     */
    public String generateCuriosityQuestion(List<String> interests) {
        if (interests == null || interests.isEmpty()) {
            String[] genericQuestions = {
                "What made you smile today?",
                "Is there anything you've been wanting to try but haven't yet?",
                "What's something you've learned recently that surprised you?",
                "If you could travel anywhere right now, where would you go?",
                "What's a small thing that made your day better?"
            };
            return genericQuestions[random.nextInt(genericQuestions.length)];
        }
        
        Map<String, String[]> interestQuestions = new HashMap<>();
        interestQuestions.put("gaming", new String[]{
            "What's your favorite game memory?",
            "Do you have a game you've been wanting to try but haven't found the time for?",
            "What's the most memorable gaming achievement you've had?"
        });
        interestQuestions.put("reading", new String[]{
            "What's the last book that really made you think?",
            "Are you reading anything exciting right now?",
            "What's a book you recommend to everyone?"
        });
        interestQuestions.put("music", new String[]{
            "What's a song that always puts you in a good mood?",
            "Have you discovered any new music lately that you love?",
            "What's your go-to music when you need to relax?"
        });
        interestQuestions.put("sports", new String[]{
            "What's the most exciting sports moment you've witnessed?",
            "Do you have a favorite team or athlete you follow?",
            "What's a sport you wish you could try?"
        });
        interestQuestions.put("movies", new String[]{
            "What's a movie that never gets old for you?",
            "Have you seen anything lately that you really enjoyed?",
            "What's your favorite movie genre?"
        });
        
        for (String interest : interests) {
            if (interestQuestions.containsKey(interest)) {
                String[] questions = interestQuestions.get(interest);
                return questions[random.nextInt(questions.length)];
            }
        }
        
        // Generic question if no specific interest matched
        return "What's something new you've been interested in lately?";
    }
    
    // ==================== EMOTIONAL INTELLIGENCE METHODS ====================
    
    /**
     * Generate an empathic response based on emotion
     */
    public String getEmpathicResponse(String emotion) {
        if (emotion == null) return null;
        
        switch (emotion.toLowerCase()) {
            case "sad":
            case "sadness":
                return "I can hear that you're feeling down. Remember, it's okay to not feel okay sometimes. " +
                       "Would you like to share what's on your mind?";
                       
            case "stressed":
            case "anxious":
                return "It sounds like things are feeling overwhelming right now. " +
                       "Take a deep breath - I'm here with you. What's been weighing on you?";
                       
            case "angry":
            case "frustrated":
                return "I hear that you're frustrated. It's completely valid to feel that way. " +
                       "Sometimes it helps to talk about what's causing these feelings.";
                       
            case "lonely":
                return "I understand that feeling of loneliness. Just know that you're not alone - " +
                       "I'm here to chat with you anytime you need company.";
                       
            case "happy":
            case "excited":
                return "I love that you're feeling good! Positive energy is so wonderful. " +
                       "What's putting that smile on your face?";
                       
            case "tired":
                return "It sounds like you're exhausted. Make sure to take care of yourself - " +
                       "rest is so important. Is there anything I can do to help lighten your load?";
                       
            case "confused":
                return "It's okay to feel confused or uncertain. Sometimes things are complicated, " +
                       "and that's completely normal. Would it help to talk through what's unclear?";
                       
            default:
                return null;
        }
    }
    
    /**
     * Generate encouraging message based on context
     */
    public String getEncouragement(String context) {
        if (context == null) {
            context = "general";
        }
        
        Map<String, String[]> encouragementMap = new HashMap<>();
        
        encouragementMap.put("struggling", new String[]{
            "You're doing better than you think. Every step forward, no matter how small, counts.",
            "Remember, difficult times don't last forever. You have the strength to get through this.",
            "Be gentle with yourself. You're learning and growing, and that's a beautiful thing."
        });
        
        encouragementMap.put("learning", new String[]{
            "Making an effort to learn is already a win! Keep going - curiosity is a superpower.",
            "Every expert was once a beginner. You're on the right path!",
            "The fact that you're trying shows real courage. Keep it up!"
        });
        
        encouragementMap.put("creating", new String[]{
            "Creating something new is such a meaningful act. Your effort matters!",
            "The world needs your unique perspective and creativity. Keep going!",
            "Whatever you're working on, know that the process itself is valuable."
        });
        
        encouragementMap.put("social", new String[]{
            "Reaching out and connecting takes courage. You're doing great!",
            "Building relationships is one of the most important things we can do. Kudos to you!",
            "Your willingness to engage with others shows real strength."
        });
        
        encouragementMap.put("general", new String[]{
            "You're doing great! Keep being awesome.",
            "Remember to be kind to yourself - you deserve it.",
            "Every day is a new opportunity to grow and shine.",
            "You have more strength than you realize.",
            "Keep going - you're on the right track!"
        });
        
        String[] options = encouragementMap.getOrDefault(context.toLowerCase(), 
                                                         encouragementMap.get("general"));
        return options[random.nextInt(options.length)];
    }
    
    /**
     * Generate milestone celebration message
     */
    public String getMilestoneCelebration(String milestone) {
        if (milestone == null) {
            milestone = "achievement";
        }
        
        Map<String, String[]> milestoneMap = new HashMap<>();
        
        milestoneMap.put("achievement", new String[]{
            "Wow! That's an incredible achievement! You should be beyond proud of yourself!",
            "This is huge! Every bit of your hard work has paid off!",
            "Incredible! You've really proven what you're capable of!",
            "This achievement shows your dedication and determination!",
            "Remarkable work! You deserve all the recognition!"
        });
        
        milestoneMap.put("milestone", new String[]{
            "You've reached an amazing milestone! This is just the beginning!",
            "What a significant moment! Celebrate this achievement!",
            "This milestone represents your growth and perseverance!",
            "Incredible journey! You've earned every bit of this success!",
            "This is a moment to remember! Well done!"
        });
        
        milestoneMap.put("progress", new String[]{
            "Look how far you've come! Your progress is truly inspiring!",
            "Every step counts! You've made incredible progress!",
            "Your journey is amazing! Keep up the momentum!",
            "The progress you've made is something to celebrate!",
            "You're doing amazing! Your growth is evident!"
        });
        
        milestoneMap.put("completion", new String[]{
            "You did it! Completion is just the beginning of your next adventure!",
            "Fantastic! You saw this through to the end!",
            "What a accomplishment! You should feel incredibly proud!",
            "Mission accomplished! Your persistence paid off!",
            "You've completed this chapter - time to celebrate!"
        });
        
        milestoneMap.put("personal", new String[]{
            "This is so personal and meaningful! I'm honored to witness this!",
            "Your personal growth is truly beautiful to see!",
            "You've grown so much! This moment represents your journey!",
            "Personal victories are the most meaningful! Celebrate yourself!",
            "Your strength and resilience shine through in this moment!"
        });
        
        String[] options = milestoneMap.getOrDefault(milestone.toLowerCase(), 
                                                      milestoneMap.get("achievement"));
        return options[random.nextInt(options.length)];
    }
    
    /**
     * Get celebratory reaction for achievements
     */
    public String getCelebratoryReaction() {
        String[] reactions = {
            "🎉 That's amazing!",
            "🙌 You did it!",
            "🌟 Incredible work!",
            "🏆 This calls for celebration!",
            "🎊 Wow! Congratulations!",
            "👏 I'm so proud of you!",
            "💫 Absolutely brilliant!",
            "⭐ Spectacular achievement!",
            "🎖️ Well deserved success!",
            "✨ This is fantastic news!"
        };
        return reactions[random.nextInt(reactions.length)];
    }
    
    /**
     * Get empathy response based on user's emotional state
     */
    public String getEmpathyResponse(String emotionalState) {
        if (emotionalState == null) {
            emotionalState = "general";
        }
        
        Map<String, String[]> empathyMap = new HashMap<>();
        
        empathyMap.put("sad", new String[]{
            "I hear you, and I care. Your feelings are completely valid.",
            "It's okay to feel sad. Sometimes our hearts need to feel heavy before they can heal.",
            "I'm here with you through this. You don't have to go through this alone.",
            "Your sadness matters to me. Whatever you're going through, I'm listening.",
            "Sending you a warm virtual hug. This feeling won't last forever."
        });
        
        empathyMap.put("stressed", new String[]{
            "Take a deep breath. I'm here with you through this overwhelming time.",
            "Stress can feel unbearable, but you're handling this better than you think.",
            "Remember to be gentle with yourself. You're doing the best you can.",
            "I'm here to support you. You don't have to carry this alone.",
            "Sometimes the weight of everything feels too much. But you will get through this."
        });
        
        empathyMap.put("anxious", new String[]{
            "I understand. Anxiety can make everything feel so much bigger than it is.",
            "Your worries are valid, but remember - you're stronger than your fears.",
            "I'm here with you. Let's take this one moment at a time together.",
            "Anxious thoughts can be overwhelming, but they don't define your truth.",
            "Breathe. You've survived every difficult day so far. You'll survive this too."
        });
        
        empathyMap.put("lonely", new String[]{
            "You're not alone - I'm here with you, whenever you need me.",
            "Loneliness can be so heavy, but please remember you matter.",
            "Even in your loneliest moments, you're seen and valued.",
            "I hear you. Sometimes connection is hard to find, but it exists.",
            "You matter deeply. Your presence makes a difference in this world."
        });
        
        empathyMap.put("angry", new String[]{
            "It's completely okay to feel angry. Your emotions are valid.",
            "Anger tells us something matters deeply to us. Feel what you need to feel.",
            "I'm here to listen without judgment. Let it out if you need to.",
            "Your frustration is understandable. It's okay to feel this way.",
            "Sometimes the strongest emotions come from caring the most."
        });
        
        empathyMap.put("tired", new String[]{
            "It sounds like you've been carrying a lot. Rest is not a luxury - it's a necessity.",
            "Exhaustion is your body asking for care. Listen to it.",
            "You deserve to take a break. You've been doing so much.",
            "It's okay to not have everything figured out right now.",
            "Sometimes the most productive thing is to rest and recharge."
        });
        
        empathyMap.put("confused", new String[]{
            "Confusion is a natural part of navigating life's complexities.",
            "It's okay not to have all the answers. You're doing your best.",
            "Clarity often comes after periods of uncertainty. Be patient with yourself.",
            "Sometimes being lost is how we find new paths we never expected.",
            "Your questions and confusion show that you're thinking deeply about life."
        });
        
        empathyMap.put("disappointed", new String[]{
            "I'm sorry things didn't work out as you'd hoped. Disappointment is hard.",
            "Your feelings are completely valid. It's okay to feel let down.",
            "Sometimes the biggest disappointments lead to unexpected opportunities.",
            "I understand. It's okay to grieve what you hoped would happen.",
            "Remember, this feeling is temporary. Better days are ahead."
        });
        
        empathyMap.put("embarrassed", new String[]{
            "We've all been there. Embarrassment is just a reminder that we're human.",
            "Please don't be too hard on yourself. These moments pass.",
            "Most people are too focused on their own lives to notice much.",
            "I think the most embarrassing moments often become our funniest stories later.",
            "Self-compassion is important. Treat yourself with the kindness you'd offer a friend."
        });
        
        empathyMap.put("hopeful", new String[]{
            "Hope is such a beautiful thing! I'm excited for what you're looking forward to!",
            "Your optimism is inspiring. Keep believing in the possibilities!",
            "Hope can move mountains. Your positive outlook is a gift.",
            "I love that you're feeling hopeful! What beautiful things do you anticipate?",
            "Hope is the companion of a brave heart. Keep shining!"
        });
        
        empathyMap.put("proud", new String[]{
            "You should be incredibly proud of yourself! What an achievement!",
            "Your pride is well-deserved! Celebrating your wins is important!",
            "This is your moment! Enjoy every bit of this success!",
            "I'm cheering for you! You've earned this celebration!",
            "Incredible! Your hard work and dedication have truly paid off!"
        });
        
        empathyMap.put("amused", new String[]{
            "I love that you're feeling amused! Laughter is the best medicine!",
            "Humor is such a gift! Sharing a laugh makes everything better!",
            "I'm glad I could bring some fun to your day!",
            "Laughter is contagious! What else has been making you smile?",
            "That's hilarious! I appreciate a good laugh too!"
        });
        
        empathyMap.put("inspired", new String[]{
            "Being inspired is such a powerful feeling! What sparked this creativity?",
            "Inspiration can lead to amazing things! What ideas are coming to mind?",
            "Your creativity is awakening! Let that inspiration flow!",
            "That's the beauty of inspiration - it opens new doors!",
            "Let your inspiration guide you to incredible places!"
        });
        
        empathyMap.put("nostalgic", new String[]{
            "Nostalgia can be bittersweet, but also so comforting! What memories are on your mind?",
            "There's something special about looking back. What moments are you reminiscing about?",
            "The past shapes us in beautiful ways. What treasures are you remembering?",
            "Those memories are precious! It's beautiful to appreciate where we've been.",
            "Nostalgia is like a warm blanket. Embrace those feelings!"
        });
        
        empathyMap.put("surprised", new String[]{
            "Wow! Surprises can be so delightful! What caught you off guard?",
            "No way! That's incredible! I love hearing about unexpected moments!",
            "Life is full of amazing surprises! What an exciting turn of events!",
            "Surprises keep things interesting! How exciting is this?!",
            "That's absolutely astonishing! Tell me more!"
        });
        
        empathyMap.put("relieved", new String[]{
            "What a relief! I'm so glad that weight is off your shoulders!",
            "You made it through! Enjoy this sense of relief!",
            "Finally! You can breathe easier now!",
            "That's such a load off! Now you can focus on the good stuff!",
            "I'm so relieved for you! You handled that beautifully!"
        });
        
        empathyMap.put("peaceful", new String[]{
            "Feeling peaceful is such a gift! What brings you this serenity?",
            "Inner peace is something to cherish. How did you find this calm?",
            "That sounds absolutely lovely! Embrace this beautiful state!",
            "Peace is a beautiful state to be in. Enjoy this moment!",
            "Your peacefulness is inspiring. What harmony have you found?"
        });
        
        empathyMap.put("general", new String[]{
            "I hear you, and I care about what you're going through.",
            "Your feelings are valid and important. I'm here to listen.",
            "Thank you for sharing this with me. It takes courage to be vulnerable.",
            "Whatever you're feeling, know that I'm here for you.",
            "Your emotions matter. I'm here to support you through this."
        });
        
        String[] options = empathyMap.getOrDefault(emotionalState.toLowerCase(), 
                                                    empathyMap.get("general"));
        return options[random.nextInt(options.length)];
    }
    
    // ==================== NATURAL CONVERSATION METHODS ====================
    
    /**
     * Generate a topic transition
     */
    public String generateTopicTransition(String fromTopic, String toTopic) {
        if (fromTopic == null || fromTopic.isEmpty()) {
            return "Speaking of which, ";
        }
        
        String[] transitions = {
            "That reminds me of something - ",
            "On a related note, ",
            "Shifting gears a bit, ",
            "Speaking of which, ",
            "Speaking of " + fromTopic + ", have you ever considered ",
            "That makes me think about "
        };
        
        return transitions[random.nextInt(transitions.length)];
    }
    
    /**
     * Generate a follow-up question based on conversation context
     */
    public String generateFollowUp(String lastTopic, String userInput) {
        if (lastTopic == null || lastTopic.isEmpty()) {
            String[] generalFollowUps = {
                "What else is on your mind?",
                "Is there anything else you'd like to chat about?",
                "What's been occupying your thoughts lately?",
                "Anything else you'd like to explore?",
                "What would you like to talk about next?"
            };
            return generalFollowUps[random.nextInt(generalFollowUps.length)];
        }
        
        Map<String, String[]> followUpMap = new HashMap<>();
        
        followUpMap.put("gaming", new String[]{
            "What other games have you been enjoying lately?",
            "Is there a game you're really looking forward to?",
            "What's your favorite gaming memory?"
        });
        
        followUpMap.put("mental health", new String[]{
            "How are you feeling after talking about this?",
            "Is there anything else on your mind that you'd like to discuss?",
            "Would you like to talk about something lighter?"
        });
        
        followUpMap.put("homework", new String[]{
            "Is there anything else about your studies you'd like help with?",
            "How are you finding your workload lately?",
            "Anything else you're working on that I can help with?"
        });
        
        followUpMap.put("relationship", new String[]{
            "How are you feeling about everything we discussed?",
            "Is there anything else you'd like to share about relationships?",
            "Would you like to talk about something else for a change?"
        });
        
        followUpMap.put("creative writing", new String[]{
            "How's your writing project coming along?",
            "What other creative ideas do you have?",
            "Would you like to explore more creative topics?"
        });
        
        String[] followUps = followUpMap.getOrDefault(lastTopic.toLowerCase(), 
                                                       followUpMap.get("general"));
        return followUps[random.nextInt(followUps.length)];
    }
    
    /**
     * Get response with variable length (sometimes short, sometimes long)
     */
    public String getVariableLengthResponse(String baseResponse, boolean shouldBeLong) {
        if (shouldBeLong) {
            String[] longAddons = {
                " I'm really interested in hearing more about what you think.",
                " I'd love to understand more about your perspective on this.",
                " This is the kind of conversation I really enjoy having.",
                " Feel free to share as much or as little as you'd like."
            };
            return baseResponse + longAddons[random.nextInt(longAddons.length)];
        } else {
            String[] shortAddons = {
                " :)",
                " - thanks for sharing!",
                " I appreciate that.",
                " Got it!"
            };
            return baseResponse + shortAddons[random.nextInt(shortAddons.length)];
        }
    }
    
    // ==================== INTERACTIVE GAMES METHODS ====================
    
    /**
     * Start a simple game
     */
    public String startGame(String gameType, String userId) {
        String gameId = userId + "_" + gameType;
        GameState state = new GameState(gameType);
        activeGames.put(gameId, state);
        
        switch (gameType.toLowerCase()) {
            case "20questions":
                state.state.put("answer", getRandomMysteryAnswer());
                state.state.put("clues", 0);
                state.state.put("guesses", new ArrayList<String>());
                return "Let's play 20 Questions! I'm thinking of something. " +
                       "You can ask yes/no questions to figure it out. " +
                       "What would you like to know?";
                       
            case "wordgame":
                state.state.put("targetWord", generateWordGameWord());
                state.state.put("attempts", 0);
                state.state.put("hintsUsed", 0);
                return "Let's play a word game! I'm thinking of a word. " +
                       "You can guess letters to figure it out. " +
                       "What's your first guess?";
                       
            case "trivia":
                state.state.put("question", getRandomTriviaQuestion());
                state.state.put("answered", false);
                return "Let's play trivia! " + getRandomTriviaQuestion();
                       
            default:
                return "I'm not sure how to play that game yet. " +
                       "I can play 20 Questions, Word Game, or Trivia!";
        }
    }
    
    /**
     * Process game input
     */
    public String processGameInput(String gameType, String input, String userId) {
        String gameId = userId + "_" + gameType;
        GameState state = activeGames.get(gameId);
        
        if (state == null) {
            return "We don't have an active game. Say 'start game' to begin!";
        }
        
        // Use the gameType field from GameState for processing
        String actualGameType = state.getGameType();
        
        switch (actualGameType.toLowerCase()) {
            case "20questions":
                return processTwentyQuestions(state, input);
                       
            case "wordgame":
                return processWordGame(state, input);
                       
            case "trivia":
                return processTrivia(state, input);
                       
            default:
                return "I'm not sure how to play that game.";
        }
    }
    
    /**
     * Validate input using regex patterns
     */
    private boolean validateInputWithRegex(String input, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    
    private String getRandomMysteryAnswer() {
        String[] answers = {
            "a cat", "a book", "a tree", "a phone", "a car", 
            "a coffee", "a song", "a dream", "a robot", "a cloud"
        };
        return answers[random.nextInt(answers.length)];
    }
    
    private String processTwentyQuestions(GameState state, String input) {
        state.attempts++;
        String answer = (String) state.state.get("answer");
        String lowerInput = input.toLowerCase();
        
        // Check if user is guessing
        if (lowerInput.startsWith("is it ") || lowerInput.startsWith("are you ") || 
            lowerInput.startsWith("is a ") || lowerInput.startsWith("its ")) {
            @SuppressWarnings("unchecked")
            List<String> guesses = (List<String>) state.state.get("guesses");
            guesses.add(input);
            state.state.put("guesses", guesses);
            
            if (lowerInput.contains(answer.toLowerCase())) {
                activeGames.remove("userId_20questions");
                return "That's it! You got it! Great job! Want to play again?";
            } else if (state.attempts >= 20) {
                activeGames.remove("userId_20questions");
                return "Out of questions! I was thinking of " + answer + ". Want to try again?";
            } else {
                int remaining = 20 - state.attempts;
                return "Not quite! You have " + remaining + " questions left. What else would you like to ask?";
            }
        }
        
        // Provide a clue if they're stuck
        if (state.attempts % 5 == 0) {
            String[] clues = {
                "Hint: It's something you might find at home.",
                "Hint: It's a noun.",
                "Hint: It's something that exists in the physical world.",
                "Hint: It's not a person."
            };
            state.state.put("clues", (Integer) state.state.get("clues") + 1);
            return clues[random.nextInt(clues.length)] + " What else would you like to know?";
        }
        
        return "I'm not sure if I can answer that with yes or no. Try asking a different question!";
    }
    
    private String generateWordGameWord() {
        String[] words = {"apple", "brain", "cloud", "dream", "earth", "flame", "ghost", "heart", "light", "music"};
        return words[random.nextInt(words.length)];
    }
    
    private String processWordGame(GameState state, String input) {
        state.attempts++;
        String target = (String) state.state.get("targetWord");
        String guess = input.toLowerCase().trim();
        
        if (guess.length() == 1) {
            // Letter guess
            if (target.contains(guess)) {
                return "Great! '" + guess + "' is in the word. Keep going!";
            } else {
                int remaining = 6 - state.attempts;
                return "Sorry, no '" + guess + "' in my word. " + remaining + " guesses left.";
            }
        } else if (guess.length() == target.length()) {
            // Full word guess
            if (guess.equals(target)) {
                activeGames.remove("userId_wordgame");
                return "You got it! The word was " + target + "! Want to play again?";
            } else {
                int remaining = 6 - state.attempts;
                if (remaining <= 0) {
                    activeGames.remove("userId_wordgame");
                    return "Out of guesses! The word was " + target + ". Want to try again?";
                }
                return "That's not it. " + remaining + " guesses left. Try another letter!";
            }
        }
        
        return "Please guess a single letter or the full word.";
    }
    
    private String getRandomTriviaQuestion() {
        String[] questions = {
            "What is the capital of France? (A) London (B) Paris (C) Berlin (D) Madrid",
            "What is 5 + 7? (A) 10 (B) 12 (C) 14 (D) 15",
            "Which planet is known as the Red Planet? (A) Venus (B) Mars (C) Jupiter (D) Saturn",
            "What is the largest ocean on Earth? (A) Atlantic (B) Indian (C) Arctic (D) Pacific",
            "Who wrote 'Romeo and Juliet'? (A) Dickens (B) Shakespeare (C) Austen (D) Hemingway"
        };
        return questions[random.nextInt(questions.length)];
    }
    
    private String processTrivia(GameState state, String input) {
        @SuppressWarnings("unchecked")
        Boolean answered = (Boolean) state.state.get("answered");
        
        if (answered) {
            return "We already answered that question! Say 'trivia' for a new one.";
        }
        
        String lowerInput = input.toLowerCase();
        // Use regex validation for answer checking
        if (validateInputWithRegex(lowerInput, ".*[abcd].*")) {
            state.state.put("answered", true);
            if (lowerInput.contains("b") && state.state.get("question").toString().contains("Paris")) {
                return "Correct! You're a trivia star! Want another question?";
            } else if (lowerInput.contains("b") && state.state.get("question").toString().contains("12")) {
                return "Correct! You're a trivia star! Want another question?";
            } else if (lowerInput.contains("b") && state.state.get("question").toString().contains("Mars")) {
                return "Correct! You're a trivia star! Want another question?";
            } else if (lowerInput.contains("d") && state.state.get("question").toString().contains("Pacific")) {
                return "Correct! You're a trivia star! Want another question?";
            } else if (lowerInput.contains("b") && state.state.get("question").toString().contains("Shakespeare")) {
                return "Correct! You're a trivia star! Want another question?";
            } else {
                return "Not quite! The answer was B. Say 'trivia' for a new question!";
            }
        }
        
        return "Just answer with A, B, C, or D!";
    }
    
    // ==================== SELF-AWARENESS METHODS ====================
    
    /**
     * Get a natural acknowledgment of limitations
     */
    public String getLimitationAcknowledgment(String context) {
        Map<String, String[]> acknowledgments = new HashMap<>();
        
        acknowledgments.put("complex", new String[]{
            "That's a really deep question. I don't have a perfect answer, but I appreciate you asking!",
            "Hmm, that's beyond my usual scope. What do you think about it?",
            "That's something I can't fully answer, but I'd love to hear your thoughts!"
        });
        
        acknowledgments.put("unknown", new String[]{
            "I'm not sure about that one. What do you think?",
            "I don't have information on that. Got any insights to share?",
            "That's outside my knowledge. I'd be curious to hear what you know about it!"
        });
        
        acknowledgments.put("opinion", new String[]{
            "That's a great question for a human perspective. What's your take?",
            "I can share information, but for personal opinions, I'd say that's really up to you!",
            "I don't have personal experiences to draw from, but this sounds like something meaningful!"
        });
        
        acknowledgments.put("general", new String[]{
            "That's a good question! I'm always learning, but I don't have all the answers.",
            "I appreciate the question, even if I can't answer it perfectly!",
            "What an interesting thing to wonder about! I'd love to hear your perspective."
        });
        
        String[] options = acknowledgments.getOrDefault(context.toLowerCase(), 
                                                         acknowledgments.get("general"));
        return options[random.nextInt(options.length)];
    }
    
    /**
     * Express honest uncertainty
     */
    public String expressUncertainty() {
        String[] uncertainties = {
            "I want to be honest with you - I'm not entirely sure about that.",
            "That's something I don't have a clear answer for.",
            "I think I'd need more information to give you a good answer there.",
            "You know, I'm not certain about that. What do you think?",
            "That's a good question that I don't have a complete answer to."
        };
        return uncertainties[random.nextInt(uncertainties.length)];
    }
    
    /**
     * Acknowledge learning from conversation
     */
    public String acknowledgeLearning() {
        String[] learning = {
            "Conversations like this help me understand better. Thanks for sharing your thoughts!",
            "I appreciate you explaining that - it helps me learn!",
            "That's interesting! I love learning new things from our chats.",
            "Thanks for that perspective - it gives me more to think about!"
        };
        return learning[random.nextInt(learning.length)];
    }
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Check if input contains name-related patterns
     */
    public boolean isAskingAboutName(String input) {
        String lower = input.toLowerCase();
        return lower.contains("your name") || 
               lower.contains("who are you") ||
               lower.contains("what are you");
    }
    
    /**
     * Check if input is asking for personal opinion
     */
    public boolean isAskingForOpinion(String input) {
        String lower = input.toLowerCase();
        return lower.contains("what do you think") ||
               lower.contains("do you think") ||
               lower.contains("your opinion") ||
               lower.contains("how do you feel about");
    }
    
    /**
     * Check if input is asking for a joke
     */
    public boolean isAskingForJoke(String input) {
        String lower = input.toLowerCase();
        return lower.contains("joke") ||
               lower.contains("tell me something funny") ||
               lower.contains("make me laugh") ||
               lower.contains("funny");
    }
    
    /**
     * Check if input is asking for a fun fact
     */
    public boolean isAskingForFact(String input) {
        String lower = input.toLowerCase();
        return lower.contains("fun fact") ||
               lower.contains("did you know") ||
               lower.contains("tell me something interesting") ||
               lower.contains("interesting fact");
    }
    
    /**
     * Check if input is starting a game
     */
    public boolean isStartingGame(String input) {
        String lower = input.toLowerCase();
        return lower.contains("play game") ||
               lower.contains("let's play") ||
               lower.contains("start game") ||
               lower.contains("20 questions") ||
               lower.contains("trivia");
    }
}
