// Updated in Version 0.2.0.0
// Created in Version 0.2.0.0
import java.util.*;
import java.time.*;
import java.util.stream.*;

/**
 * DailyCompanion - Handles daily check-ins, mood tracking, and daily conversations
 * Part of Phase 5: Daily Companion Features
 */
public class DailyCompanion {
    
    private List<DailyCheckIn> checkIns;
    private List<DailyTopic> dailyTopics;
    private List<MoodEntry> moodHistory;
    private List<EnergyEntry> energyHistory;
    private List<MorningGreeting> morningGreetings;
    private Random random;
    private LocalDate lastCheckIn;
    private LocalTime lastMorningGreeting;
    
    public DailyCompanion() {
        this.checkIns = new ArrayList<>();
        this.dailyTopics = new ArrayList<>();
        this.moodHistory = new ArrayList<>();
        this.energyHistory = new ArrayList<>();
        this.morningGreetings = new ArrayList<>();
        this.random = new Random();
        this.lastCheckIn = null;
        this.lastMorningGreeting = null;
        initializeDailyTopics();
        initializeCheckIns();
        initializeMorningGreetings();
    }
    
    /**
     * Daily check-in structure
     */
    private static class DailyCheckIn {
        String trigger;
        String question;
        List<String> followUps;
        
        public DailyCheckIn(String trigger, String question, List<String> followUps) {
            this.trigger = trigger;
            this.question = question;
            this.followUps = followUps;
        }
    }
    
    /**
     * Daily topic structure
     */
    private static class DailyTopic {
        String category;
        List<String> prompts;
        List<String> reflections;
        
        public DailyTopic(String category, List<String> prompts, List<String> reflections) {
            this.category = category;
            this.prompts = prompts;
            this.reflections = reflections;
        }
    }
    
    /**
     * Mood entry for tracking
     */
    private static class MoodEntry {
        LocalDate date;
        String mood;
        String note;
        
        public MoodEntry(LocalDate date, String mood, String note) {
            this.date = date;
            this.mood = mood;
            this.note = note;
        }
    }
    
    /**
     * Energy entry for tracking
     */
    private static class EnergyEntry {
        LocalDate date;
        String level; // high, medium, low
        String note;
        
        public EnergyEntry(LocalDate date, String level, String note) {
            this.date = date;
            this.level = level;
            this.note = note;
        }
    }
    
    /**
     * Morning greeting structure
     */
    private static class MorningGreeting {
        String timeRange; // morning, afternoon, evening
        List<String> greetings;
        List<String> followUps;
        
        public MorningGreeting(String timeRange, List<String> greetings, List<String> followUps) {
            this.timeRange = timeRange;
            this.greetings = greetings;
            this.followUps = followUps;
        }
    }
    
    private void initializeDailyTopics() {
        // Gratitude topics
        dailyTopics.add(new DailyTopic(
            "gratitude",
            Arrays.asList(
                "What are you grateful for today?",
                "What's something small that made you happy recently?",
                "Who is someone you're thankful for?",
                "What's a skill or ability you're grateful to have?"
            ),
            Arrays.asList(
                "That's a wonderful thing to appreciate!",
                "Gratitude really helps shift perspective.",
                "It's great that you can recognize the positive things!"
            )
        ));
        
        // Reflection topics
        dailyTopics.add(new DailyTopic(
            "reflection",
            Arrays.asList(
                "How are you feeling right now?",
                "What's been on your mind lately?",
                "How's your day going so far?",
                "What's something you accomplished recently?"
            ),
            Arrays.asList(
                "Thanks for sharing that with me.",
                "I appreciate you opening up.",
                "It's good to check in with yourself."
            )
        ));
        
        // Goals topics
        dailyTopics.add(new DailyTopic(
            "goals",
            Arrays.asList(
                "What's one thing you want to accomplish this week?",
                "Any plans you're looking forward to?",
                "What's something you'd like to learn or try?",
                "How can I help you suceed in completing your goals today?"
            ),
            Arrays.asList(
                "That sounds like a great goal to work towards!",
                "I'm here to support you in reaching your goals.",
                "Having something to work towards is really motivating!"
            )
        ));
        
        // Self-care topics
        dailyTopics.add(new DailyTopic(
            "selfcare",
            Arrays.asList(
                "Have you taken time for yourself today?",
                "What's something you do to relax?",
                "How's your sleep been lately?",
                "Have you been staying hydrated and eating well?"
            ),
            Arrays.asList(
                "Self-care is so important!",
                "Taking care of yourself matters a lot.",
                "Remember to be kind to yourself!"
            )
        ));
        
        // Social topics
        dailyTopics.add(new DailyTopic(
            "social",
            Arrays.asList(
                "Have you connected with anyone lately?",
                "What's a memory you share with someone special?",
                "How are your relationships going?",
                "Is there anyone you'd like to reach out to?"
            ),
            Arrays.asList(
                "Connections with others are so valuable.",
                "Relationships enrich our lives so much.",
                "It's great that you're thinking about your social connections."
            )
        ));
    }
    
    private void initializeCheckIns() {
        checkIns.add(new DailyCheckIn(
            "check in",
            "How are you feeling right now?",
            Arrays.asList(
                "What's been making you feel that way?",
                "Is there anything you'd like to talk about?",
                "How can I support you today?"
            )
        ));
        
        checkIns.add(new DailyCheckIn(
            "how do you feel",
            "I'm doing well, thank you for asking! How are you doing?",
            Arrays.asList(
                "What's been happening in your day?",
                "Anything exciting or challenging?",
                "How's everything going for you?"
            )
        ));
        
        checkIns.add(new DailyCheckIn(
            "daily update",
            "I'd love to hear about your day! What's been happening?",
            Arrays.asList(
                "That sounds interesting! Tell me more.",
                "How did that make you feel?",
                "What's the best part of your day been?"
            )
        ));
    }
    
    private void initializeMorningGreetings() {
        // Early morning greetings (5-8 AM)
        morningGreetings.add(new MorningGreeting(
            "early_morning",
            Arrays.asList(
                "Good early morning! You're up early! How are you feeling?",
                "Hey early bird! The day is just starting. How's your energy?",
                "Good morning! I see you're starting your day early. How are you doing?"
            ),
            Arrays.asList(
                "What got you up so early?",
                "How's your morning been so far?",
                "Got any exciting plans for today?"
            )
        ));
        
        // Morning greetings (8 AM - 12 PM)
        morningGreetings.add(new MorningGreeting(
            "morning",
            Arrays.asList(
                "Good morning! How are you feeling today?",
                "Good morning! How's your day going so far?",
                "Hey there! How are you doing this morning?",
                "Good morning! What's on your mind today?",
                "Hello! How's your morning treating you?"
            ),
            Arrays.asList(
                "What's the best part of your morning so far?",
                "How's your energy level today?",
                "What do you have planned for today?",
                "How can I make your day better?"
            )
        ));
        
        // Mid-day greetings (12 PM - 3 PM)
        morningGreetings.add(new MorningGreeting(
            "midday",
            Arrays.asList(
                "Hello! How's your day going?",
                "Hey there! How's your day been so far?",
                "Hi! How are you doing this afternoon?"
            ),
            Arrays.asList(
                "What's been the highlight of your day?",
                "How's your energy holding up?",
                "What have you been up to today?"
            )
        ));
        
        // Afternoon greetings (3 PM - 6 PM)
        morningGreetings.add(new MorningGreeting(
            "afternoon",
            Arrays.asList(
                "Good afternoon! How are you doing?",
                "Hey there! How's your afternoon going?",
                "Hello! Almost evening. How are you feeling?"
            ),
            Arrays.asList(
                "How's your day been overall?",
                "What's coming up for the rest of your day?",
                "How can I help you this afternoon?"
            )
        ));
        
        // Evening greetings (6 PM - 10 PM)
        morningGreetings.add(new MorningGreeting(
            "evening",
            Arrays.asList(
                "Good evening! How are you doing?",
                "Hey there! How's your evening going?",
                "Hello! Ready to wind down? How are you feeling?"
            ),
            Arrays.asList(
                "How was your day?",
                "What's the best thing that happened today?",
                "Any plans for this evening?"
            )
        ));
        
        // Late night greetings (10 PM - 5 AM)
        morningGreetings.add(new MorningGreeting(
            "night",
            Arrays.asList(
                "Hey! Late night chat. How are you doing?",
                "Hello! Up late? How are you feeling?",
                "Hey there! It's getting late. How are you?"
            ),
            Arrays.asList(
                "What brings you here at this hour?",
                "How's your night going?",
                "Everything okay?"
            )
        ));
    }
    
    /**
     * Initiates a daily check-in conversation
     */
    public String initiateCheckIn() {
        lastCheckIn = LocalDate.now();
        
        String[] openingMessages = {
            "Hey! I'd like to check in with you. How are you feeling today?",
            "Hi there! Let's take a moment to check in. How are you doing?",
            "Hey! It's time for our daily check-in. How are you feeling right now?",
            "Hi! I wanted to see how you're doing. How are you feeling today?"
        };
        
        return openingMessages[random.nextInt(openingMessages.length)];
    }
    
    /**
     * Gets a daily topic prompt
     */
    public String getDailyPrompt() {
        LocalDate today = LocalDate.now();
        int dayOfYear = today.getDayOfYear();
        
        // Use day of year to cycle through topics deterministically
        int topicIndex = dayOfYear % dailyTopics.size();
        DailyTopic topic = dailyTopics.get(topicIndex);
        
        int promptIndex = dayOfYear % topic.prompts.size();
        return topic.prompts.get(promptIndex);
    }
    
    /**
     * Gets a random daily topic
     */
    public String getRandomPrompt() {
        DailyTopic topic = dailyTopics.get(random.nextInt(dailyTopics.size()));
        return topic.prompts.get(random.nextInt(topic.prompts.size()));
    }
    
    /**
     * Handles check-in response
     */
    public String handleCheckInResponse(String input) {
        String lowerInput = input.toLowerCase().trim();
        
        // Detect mood from response
        String detectedMood = detectMood(lowerInput);
        
        // Record mood entry
        moodHistory.add(new MoodEntry(LocalDate.now(), detectedMood, input));
        
        // Generate appropriate response based on detected mood
        return generateMoodResponse(detectedMood, lowerInput);
    }
    
    /**
     * Detects mood from input
     */
    private String detectMood(String input) {
        // Positive indicators
        if (containsAny(input, "great", "good", "happy", "wonderful", "amazing", "awesome", "fantastic", "excellent", "better")) {
            return "positive";
        }
        
        // Negative indicators
        if (containsAny(input, "bad", "sad", "terrible", "awful", "horrible", "upset", "down", "worst", " Stressed", "anxious")) {
            return "negative";
        }
        
        // Neutral indicators
        if (containsAny(input, "okay", "ok", "alright", "fine", "average", "normal", "meh")) {
            return "neutral";
        }
        
        // Mixed indicators
        if (containsAny(input, "mixed", "complicated", "complex", "hard to say")) {
            return "mixed";
        }
        
        return "unknown";
    }
    
    /**
     * Checks if input contains any of the given words
     */
    private boolean containsAny(String input, String... words) {
        for (String word : words) {
            if (input.contains(word)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Generates response based on detected mood
     */
    private String generateMoodResponse(String mood, String input) {
        switch (mood) {
            case "positive":
                String[] positiveResponses = {
                    "I'm so glad to hear you're feeling positive! That's wonderful! great to hear!",
                    "That's What's been contributing to these good feelings?",
                    "I love that you're feeling good! Keep riding that positive wave!",
                    "That's awesome! What's making your day so great?"
                };
                return positiveResponses[random.nextInt(positiveResponses.length)];
                
            case "negative":
                String[] negativeResponses = {
                    "I'm sorry to hear you're not feeling your best. I'm here for you.",
                    "It sounds like things are tough right now. Do you want to talk about it?",
                    "I'm here to listen. What's been weighing on your mind?",
                    "I'm sorry you're going through this. Remember that it's okay to feel what you're feeling."
                };
                return negativeResponses[random.nextInt(negativeResponses.length)];
                
            case "neutral":
                String[] neutralResponses = {
                    "Thanks for checking in! How can I make your day better?",
                    "I appreciate you taking the time to connect. What's on your mind?",
                    "Thanks for sharing! Is there anything you'd like to discuss?",
                    "I value our check-ins. What's been happening lately?"
                };
                return neutralResponses[random.nextInt(neutralResponses.length)];
                
            case "mixed":
                String[] mixedResponses = {
                    "It sounds like things are a bit complicated right now. That's completely normal.",
                    "Life has its ups and downs. I'm here to talk through anything.",
                    "It's okay to have mixed feelings. Would you like to share more?"
                };
                return mixedResponses[random.nextInt(mixedResponses.length)];
                
            default:
                String[] defaultResponses = {
                    "Thanks for checking in! Tell me more about how you're feeling.",
                    "I appreciate you connecting with me. What's going on?",
                    "Thanks for sharing! How can I support you today?"
                };
                return defaultResponses[random.nextInt(defaultResponses.length)];
        }
    }
    
    /**
     * Gets follow-up questions for check-in
     */
    public List<String> getFollowUpQuestions() {
        DailyCheckIn checkIn = checkIns.get(random.nextInt(checkIns.size()));
        return checkIn.followUps;
    }
    
    /**
     * Gets reflection response for a topic
     */
    public String getReflectionResponse(String topicCategory) {
        for (DailyTopic topic : dailyTopics) {
            if (topic.category.equalsIgnoreCase(topicCategory)) {
                return topic.reflections.get(random.nextInt(topic.reflections.size()));
            }
        }
        // Default reflection if category not found
        String[] defaultReflections = {
            "Thank you for sharing that with me.",
            "I appreciate you opening up.",
            "It's great to talk about these things."
        };
        return defaultReflections[random.nextInt(defaultReflections.length)];
    }
    
    /**
     * Records a mood entry
     */
    public void recordMood(String mood, String note) {
        moodHistory.add(new MoodEntry(LocalDate.now(), mood, note));
    }
    
    /**
     * Gets mood history
     */
    public List<MoodEntry> getMoodHistory() {
        return new ArrayList<>(moodHistory);
    }
    
    /**
     * Gets mood history for a specific number of days
     */
    public List<MoodEntry> getRecentMoodHistory(int days) {
        LocalDate cutoff = LocalDate.now().minusDays(days);
        List<MoodEntry> recent = new ArrayList<>();
        
        for (MoodEntry entry : moodHistory) {
            if (entry.date.isAfter(cutoff) || entry.date.isEqual(cutoff)) {
                recent.add(entry);
            }
        }
        
        return recent;
    }
    
    /**
     * Gets mood trends
     */
    public String getMoodTrends() {
        if (moodHistory.isEmpty()) {
            return "I don't have enough data yet to show trends. Keep checking in!";
        }
        
        Map<String, Integer> moodCounts = new HashMap<>();
        for (MoodEntry entry : moodHistory) {
            moodCounts.put(entry.mood, moodCounts.getOrDefault(entry.mood, 0) + 1);
        }
        
        String dominant = Collections.max(moodCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
        
        return "Based on your check-ins, you've been mostly feeling " + dominant + " lately.";
    }
    
    /**
     * Checks if it's time for a daily check-in
     */
    public boolean isCheckInDue() {
        if (lastCheckIn == null) {
            return true;
        }
        
        LocalDate today = LocalDate.now();
        return !today.equals(lastCheckIn);
    }
    
    /**
     * Gets the last check-in date
     */
    public LocalDate getLastCheckIn() {
        return lastCheckIn;
    }
    
    /**
     * Gets available topic categories
     */
    public List<String> getTopicCategories() {
        List<String> categories = new ArrayList<>();
        for (DailyTopic topic : dailyTopics) {
            categories.add(topic.category);
        }
        return categories;
    }
    
    /**
     * Gets a prompt for a specific category
     */
    public String getPromptForCategory(String category) {
        for (DailyTopic topic : dailyTopics) {
            if (topic.category.equalsIgnoreCase(category)) {
                return topic.prompts.get(random.nextInt(topic.prompts.size()));
            }
        }
        return null;
    }
    
    /**
     * Handles daily conversation
     */
    public String handleDailyConversation(String input) {
        String lowerInput = input.toLowerCase().trim();
        
        // Check for check-in triggers
        for (DailyCheckIn checkIn : checkIns) {
            if (lowerInput.contains(checkIn.trigger)) {
                return checkIn.question;
            }
        }
        
        // Check for mood keywords
        if (containsAny(lowerInput, "mood", "feeling", "emotions", "emotional")) {
            String[] responses = {
                "I'm here to talk about how you're feeling. What's on your mind?",
                "Emotions are important to process. How are you feeling right now?",
                "Let's talk about feelings. What's going on with you?"
            };
            return responses[random.nextInt(responses.length)];
        }
        
        // Check for gratitude keywords
        if (containsAny(lowerInput, "grateful", "gratitude", "thankful", "appreciate")) {
            String[] responses = {
                "Gratitude is so powerful! What are you grateful for today?",
                "I love that you're focusing on gratitude! What's something you're thankful for?",
                "That's a wonderful mindset! What appreciation do you have in your life?"
            };
            return responses[random.nextInt(responses.length)];
        }
        
        // Check for goals keywords
        if (containsAny(lowerInput, "goal", "aim", "target", "achieve", "accomplish")) {
            String[] responses = {
                "Goals give us direction! What are you working towards?",
                "I'd love to hear about your goals! What would you like to accomplish?",
                "Having goals is great for motivation! What's on your target list?"
            };
            return responses[random.nextInt(responses.length)];
        }
        
        // Check for self-care keywords
        if (containsAny(lowerInput, "self-care", "self care", "relax", "rest", "break")) {
            String[] responses = {
                "Self-care is so important! How do you like to take care of yourself?",
                "Taking breaks is essential! What's your favorite way to relax?",
                "I believe in self-care! What do you do to recharge?"
            };
            return responses[random.nextInt(responses.length)];
        }
        
        // Default to daily prompt
        return getDailyPrompt();
    }
    
    // ==================== Morning Greetings ====================
    
    /**
     * Gets a time-based morning greeting
     */
    public String getMorningGreeting() {
        LocalTime now = LocalTime.now();
        lastMorningGreeting = now;
        
        MorningGreeting greeting = getGreetingForTime(now);
        return greeting.greetings.get(random.nextInt(greeting.greetings.size()));
    }
    
    /**
     * Gets the appropriate greeting for the current time
     */
    private MorningGreeting getGreetingForTime(LocalTime time) {
        int hour = time.getHour();
        
        if (hour >= 5 && hour < 8) {
            return morningGreetings.stream()
                .filter(g -> g.timeRange.equals("early_morning"))
                .findFirst().orElse(morningGreetings.get(0));
        } else if (hour >= 8 && hour < 12) {
            return morningGreetings.stream()
                .filter(g -> g.timeRange.equals("morning"))
                .findFirst().orElse(morningGreetings.get(1));
        } else if (hour >= 12 && hour < 15) {
            return morningGreetings.stream()
                .filter(g -> g.timeRange.equals("midday"))
                .findFirst().orElse(morningGreetings.get(2));
        } else if (hour >= 15 && hour < 18) {
            return morningGreetings.stream()
                .filter(g -> g.timeRange.equals("afternoon"))
                .findFirst().orElse(morningGreetings.get(3));
        } else if (hour >= 18 && hour < 22) {
            return morningGreetings.stream()
                .filter(g -> g.timeRange.equals("evening"))
                .findFirst().orElse(morningGreetings.get(4));
        } else {
            return morningGreetings.stream()
                .filter(g -> g.timeRange.equals("night"))
                .findFirst().orElse(morningGreetings.get(5));
        }
    }
    
    /**
     * Gets follow-up questions for morning greeting
     */
    public List<String> getMorningFollowUps() {
        LocalTime now = LocalTime.now();
        MorningGreeting greeting = getGreetingForTime(now);
        return greeting.followUps;
    }
    
    /**
     * Gets follow-up questions for a specific time range
     */
    public List<String> getFollowUpsForTimeRange(String timeRange) {
        for (MorningGreeting greeting : morningGreetings) {
            if (greeting.timeRange.equals(timeRange)) {
                return greeting.followUps;
            }
        }
        return getMorningFollowUps();
    }
    
    /**
     * Checks if it's morning greeting time (first interaction of the day)
     */
    public boolean isMorningGreetingAppropriate() {
        if (lastMorningGreeting == null) {
            return true;
        }
        
        LocalTime now = LocalTime.now();
        // Reset if it's a new day (after 5 AM and last greeting was before 5 AM)
        if (now.getHour() >= 5 && lastMorningGreeting.getHour() < 5) {
            return true;
        }
        
        return false;
    }
    
    // ==================== Mood Check-ins ====================
    
    /**
     * Initiates a mood check-in
     */
    public String initiateMoodCheckIn() {
        lastCheckIn = LocalDate.now();
        
        String[] checkInPrompts = {
            "Let's check in on how you're feeling. How are your emotions today?",
            "I want to see how you're doing. How are you feeling right now?",
            "How's your mood today? Take a moment to check in with yourself.",
            "How are you feeling? I'm here to listen and support you.",
            "How's your emotional state today? What's been on your mind?"
        };
        
        return checkInPrompts[random.nextInt(checkInPrompts.length)];
    }
    
    /**
     * Handles mood check-in response with detailed analysis
     */
    public String handleMoodCheckIn(String input) {
        String lowerInput = input.toLowerCase().trim();
        
        // Detect mood
        String detectedMood = detectMood(lowerInput);
        
        // Detect intensity
        String intensity = detectIntensity(lowerInput);
        
        // Record mood entry
        moodHistory.add(new MoodEntry(LocalDate.now(), detectedMood + "_" + intensity, input));
        
        // Generate response
        return generateDetailedMoodResponse(detectedMood, intensity, lowerInput);
    }
    
    /**
     * Detects mood intensity from input
     */
    private String detectIntensity(String input) {
        // High intensity indicators
        if (containsAny(input, "very", "really", "extremely", "super", "incredibly", "so much", "overwhelming")) {
            return "high";
        }
        
        // Low intensity indicators
        if (containsAny(input, "a bit", "somewhat", "slightly", "kind of", "maybe", "a little")) {
            return "low";
        }
        
        return "medium";
    }
    
    /**
     * Generates detailed mood response
     */
    private String generateDetailedMoodResponse(String mood, String intensity, String input) {
        String baseResponse = generateMoodResponse(mood, input);
        
        // Add intensity-aware follow-up
        String[] intensityFollowUps = {
            "Would you like to share more about what you're experiencing?",
            "Is there anything specific on your mind?",
            "How can I best support you right now?"
        };
        
        if ("high".equals(intensity)) {
            baseResponse += " It sounds like you're feeling this quite intensely.";
        }
        
        return baseResponse + " " + intensityFollowUps[random.nextInt(intensityFollowUps.length)];
    }
    
    /**
     * Gets mood summary for the day
     */
    public String getMoodSummary() {
        List<MoodEntry> todayEntries = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        for (MoodEntry entry : moodHistory) {
            if (entry.date.equals(today)) {
                todayEntries.add(entry);
            }
        }
        
        if (todayEntries.isEmpty()) {
            return "No mood entries recorded today. How are you feeling right now?";
        }
        
        // Find dominant mood
        Map<String, Integer> moodCounts = new HashMap<>();
        for (MoodEntry entry : todayEntries) {
            moodCounts.put(entry.mood, moodCounts.getOrDefault(entry.mood, 0) + 1);
        }
        
        String dominant = Collections.max(moodCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
        
        return "Today you've been mostly feeling " + dominant + ". You've had " + todayEntries.size() + " check-in(s) today.";
    }
    
    // ==================== Energy Assessments ====================
    
    /**
     * Initiates an energy assessment
     */
    public String initiateEnergyAssessment() {
        String[] energyPrompts = {
            "How's your energy level right now?",
            "How are you feeling in terms of energy?",
            "What's your energy like today?",
            "How much energy do you have right now?",
            "How's your energy holding up?"
        };
        
        return energyPrompts[random.nextInt(energyPrompts.length)];
    }
    
    /**
     * Handles energy assessment response
     */
    public String handleEnergyAssessment(String input) {
        String lowerInput = input.toLowerCase().trim();
        
        // Detect energy level
        String energyLevel = detectEnergyLevel(lowerInput);
        
        // Record energy entry
        energyHistory.add(new EnergyEntry(LocalDate.now(), energyLevel, input));
        
        // Generate response based on energy level
        return generateEnergyResponse(energyLevel);
    }
    
    /**
     * Detects energy level from input
     */
    private String detectEnergyLevel(String input) {
        // High energy indicators
        if (containsAny(input, "high", "great", "amazing", "full of energy", "energetic", "wired", "buzzing", "lots of energy", "on fire", "pumped")) {
            return "high";
        }
        
        // Low energy indicators
        if (containsAny(input, "low", "tired", "exhausted", "drained", "fatigued", "wiped out", "beat", "depleted", "no energy", "sluggish", "drowsy")) {
            return "low";
        }
        
        // Medium energy indicators
        if (containsAny(input, "medium", "moderate", "okay", "ok", "alright", "normal", "average", "decent", "fine")) {
            return "medium";
        }
        
        return "unknown";
    }
    
    /**
     * Generates response based on energy level
     */
    private String generateEnergyResponse(String energyLevel) {
        switch (energyLevel) {
            case "high":
                String[] highResponses = {
                    "That's awesome! You've got great energy today!",
                    "Wonderful! Use that energy to accomplish great things!",
                    "That's fantastic! High energy means great potential!",
                    "I love that energy! What exciting things are you working on?"
                };
                return highResponses[random.nextInt(highResponses.length)];
                
            case "low":
                String[] lowResponses = {
                    "I'm sorry you're feeling low on energy. Remember to take care of yourself.",
                    "Energy dips happen. Make sure you're resting enough and staying hydrated.",
                    "I understand. Be gentle with yourself today. Maybe take it easy?",
                    "Low energy is tough. Try to get some rest and self-care today."
                };
                return lowResponses[random.nextInt(lowResponses.length)];
                
            case "medium":
                String[] mediumResponses = {
                    "That's a balanced energy level. You can get things done without burning out.",
                    "Medium energy is a good place to be. Steady and sustainable!",
                    "Sounds like you have a good balance. How can I help you make the most of it?",
                    "Nice! You have enough energy to be productive but still relaxed."
                };
                return mediumResponses[random.nextInt(mediumResponses.length)];
                
            default:
                String[] defaultResponses = {
                    "Thanks for sharing! How can I support you today?",
                    "I appreciate you letting me know. What's on your mind?",
                    "Thanks for checking in! How are you feeling overall?"
                };
                return defaultResponses[random.nextInt(defaultResponses.length)];
        }
    }
    
    /**
     * Gets energy history
     */
    public List<EnergyEntry> getEnergyHistory() {
        return new ArrayList<>(energyHistory);
    }
    
    /**
     * Gets recent energy history
     */
    public List<EnergyEntry> getRecentEnergyHistory(int days) {
        LocalDate cutoff = LocalDate.now().minusDays(days);
        List<EnergyEntry> recent = new ArrayList<>();
        
        for (EnergyEntry entry : energyHistory) {
            if (entry.date.isAfter(cutoff) || entry.date.isEqual(cutoff)) {
                recent.add(entry);
            }
        }
        
        return recent;
    }
    
    /**
     * Gets energy trends
     */
    public String getEnergyTrends() {
        if (energyHistory.isEmpty()) {
            return "I don't have enough energy data yet. How's your energy today?";
        }
        
        Map<String, Integer> energyCounts = new HashMap<>();
        for (EnergyEntry entry : energyHistory) {
            energyCounts.put(entry.level, energyCounts.getOrDefault(entry.level, 0) + 1);
        }
        
        String dominant = Collections.max(energyCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
        
        return "Based on your assessments, you've been mostly feeling " + dominant + " energy lately.";
    }
    
    /**
     * Records energy level manually
     */
    public void recordEnergy(String level, String note) {
        energyHistory.add(new EnergyEntry(LocalDate.now(), level, note));
    }
    
    // ==================== Goal Tracking ====================
    
    private List<GoalEntry> goals;
    private Map<String, List<GoalProgress>> goalProgress;
    
    /**
     * Goal entry structure
     */
    private static class GoalEntry {
        String id;
        String title;
        String description;
        LocalDate createdDate;
        LocalDate targetDate;
        boolean completed;
        
        public GoalEntry(String id, String title, String description, LocalDate targetDate) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.createdDate = LocalDate.now();
            this.targetDate = targetDate;
            this.completed = false;
        }
    }
    
    /**
     * Goal progress tracking
     */
    private static class GoalProgress {
        LocalDate date;
        int progressPercent;
        String note;
        
        public GoalProgress(LocalDate date, int progressPercent, String note) {
            this.date = date;
            this.progressPercent = progressPercent;
            this.note = note;
        }
    }
    
    /**
     * Adds a new goal
     */
    public String addGoal(String title, String description, LocalDate targetDate) {
        if (goals == null) {
            goals = new ArrayList<>();
            goalProgress = new HashMap<>();
        }
        
        String id = "goal_" + System.currentTimeMillis();
        GoalEntry goal = new GoalEntry(id, title, description, targetDate);
        goals.add(goal);
        goalProgress.put(id, new ArrayList<>());
        
        String[] responses = {
            "I've added your goal: " + title + "! Let me know when you make progress on it.",
            "Got it! Working towards: " + title + ". I'll help you track this!",
            "I've created your goal: " + title + ". Let's work on this together!"
        };
        return responses[random.nextInt(responses.length)];
    }
    
    /**
     * Updates goal progress
     */
    public String updateGoalProgress(String goalTitle, int progressPercent, String note) {
        if (goals == null || goals.isEmpty()) {
            return "You don't have any goals yet. Would you like to add one?";
        }
        
        // Find goal by title
        GoalEntry foundGoal = null;
        for (GoalEntry goal : goals) {
            if (goal.title.toLowerCase().contains(goalTitle.toLowerCase())) {
                foundGoal = goal;
                break;
            }
        }
        
        if (foundGoal == null) {
            return "I couldn't find a goal matching '" + goalTitle + "'. Would you like to add a new goal?";
        }
        
        // Record progress
        List<GoalProgress> progressList = goalProgress.get(foundGoal.id);
        progressList.add(new GoalProgress(LocalDate.now(), progressPercent, note));
        
        // Check if goal is completed
        if (progressPercent >= 100 && !foundGoal.completed) {
            foundGoal.completed = true;
            return celebrateMilestone(foundGoal.title);
        }
        
        String[] responses = {
            "Great progress on '" + foundGoal.title + "'! You're at " + progressPercent + "%!",
            "Nice work! Your goal '" + foundGoal.title + "' is now " + progressPercent + "% complete!",
            "I see you've made progress on '" + foundGoal.title + "'! " + progressPercent + "% done - keep it up!"
        };
        return responses[random.nextInt(responses.length)];
    }
    
    /**
     * Gets active goals
     */
    public List<String> getActiveGoals() {
        List<String> activeGoalTitles = new ArrayList<>();
        if (goals != null) {
            for (GoalEntry goal : goals) {
                if (!goal.completed) {
                    activeGoalTitles.add(goal.title + " (by " + goal.targetDate + ")");
                }
            }
        }
        return activeGoalTitles;
    }
    
    /**
     * Gets goal status
     */
    public String getGoalStatus() {
        if (goals == null || goals.isEmpty()) {
            return "You don't have any goals yet. Would you like to set one?";
        }
        
        int active = 0;
        int completed = 0;
        
        for (GoalEntry goal : goals) {
            if (goal.completed) {
                completed++;
            } else {
                active++;
            }
        }
        
        return "You have " + active + " active goal(s) and " + completed + " completed goal(s).";
    }
    
    // ==================== Habit Encouragement ====================
    
    private Map<String, List<HabitEntry>> habitHistory;
    private List<String> habitEncouragement;
    
    /**
     * Habit entry structure
     */
    private static class HabitEntry {
        LocalDate date;
        boolean completed;
        String note;
        
        public HabitEntry(LocalDate date, boolean completed, String note) {
            this.date = date;
            this.completed = completed;
            this.note = note;
        }
    }
    
    /**
     * Initializes habit encouragement messages
     */
    private void initializeHabitEncouragement() {
        habitEncouragement = Arrays.asList(
            "You're building great habits! Keep it up!",
            "Consistency is key! You're doing great!",
            "Every step counts! Stay committed!",
            "I believe in you! Keep going!",
            "Small steps lead to big changes!",
            "Your dedication is inspiring!",
            "Great job staying on track!",
            "Keep building those positive habits!"
        );
    }
    
    /**
     * Tracks a habit
     */
    public String trackHabit(String habitName, boolean completed, String note) {
        if (habitHistory == null) {
            habitHistory = new HashMap<>();
            habitEncouragement = Arrays.asList(
                "You're building great habits! Keep it up!",
                "Consistency is key! You're doing great!",
                "Every step counts! Stay committed!",
                "I believe in you! Keep going!",
                "Small steps lead to big changes!",
                "Your dedication is inspiring!",
                "Great job staying on track!",
                "Keep building those positive habits!"
            );
        }
        
        List<HabitEntry> entries = habitHistory.computeIfAbsent(habitName.toLowerCase(), k -> new ArrayList<>());
        entries.add(new HabitEntry(LocalDate.now(), completed, note));
        
        if (completed) {
            return "Great job completing '" + habitName + "' today! " + 
                   habitEncouragement.get(random.nextInt(habitEncouragement.size()));
        } else {
            return "No worries about '" + habitName + "' today. Every day is a fresh start!";
        }
    }
    
    /**
     * Gets habit streak
     */
    public int getHabitStreak(String habitName) {
        if (habitHistory == null || !habitHistory.containsKey(habitName.toLowerCase())) {
            return 0;
        }
        
        List<HabitEntry> entries = habitHistory.get(habitName.toLowerCase());
        int streak = 0;
        
        // Sort by date descending
        entries.sort((a, b) -> b.date.compareTo(a.date));
        
        for (HabitEntry entry : entries) {
            if (entry.completed) {
                streak++;
            } else {
                break;
            }
        }
        
        return streak;
    }
    
    /**
     * Gets habit encouragement
     */
    public String getHabitEncouragement(String habitName) {
        int streak = getHabitStreak(habitName);
        
        if (streak == 0) {
            return "Ready to start building your '" + habitName + "' habit? Let's do this!";
        } else if (streak < 7) {
            return "You're on a " + streak + " day streak with '" + habitName + "'! Keep going!";
        } else if (streak < 30) {
            return "Amazing! " + streak + " days! You're really building a strong habit with '" + habitName + "'!";
        } else {
            return "Incredible " + streak + " day streak! You're a master of '" + habitName + "'!";
        }
    }
    
    /**
     * Gets all tracked habits
     */
    public List<String> getTrackedHabits() {
        if (habitHistory == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(habitHistory.keySet());
    }
    
    /**
     * Gets habit summary
     */
    public String getHabitSummary(String habitName) {
        if (habitHistory == null || !habitHistory.containsKey(habitName.toLowerCase())) {
            return "No tracking data for '" + habitName + "' yet.";
        }
        
        List<HabitEntry> entries = habitHistory.get(habitName.toLowerCase());
        int completed = 0;
        for (HabitEntry entry : entries) {
            if (entry.completed) completed++;
        }
        
        int streak = getHabitStreak(habitName);
        return "Habit: '" + habitName + "'\nCompleted: " + completed + "/" + entries.size() + 
               " times\nCurrent streak: " + streak + " days";
    }
    
    // ==================== Milestone Celebrations ====================
    
    private Map<String, Integer> milestoneCounts;
    private List<Milestone> milestones;
    
    /**
     * Milestone structure
     */
    private static class Milestone {
        String type; // goal, habit, streak, etc.
        String name;
        int target;
        LocalDate achievedDate;
        
        public Milestone(String type, String name, int target) {
            this.type = type;
            this.name = name;
            this.target = target;
            this.achievedDate = null;
        }
    }
    
    /**
     * Celebrates a milestone
     */
    public String celebrateMilestone(String achievement) {
        if (milestoneCounts == null) {
            milestoneCounts = new HashMap<>();
            milestones = new ArrayList<>();
        }
        
        milestoneCounts.put(achievement, milestoneCounts.getOrDefault(achievement, 0) + 1);
        
        int count = milestoneCounts.get(achievement);
        
        String[] celebrationMessages = {
            "🎉 AMAZING! You achieved: " + achievement + "! That's incredible!",
            "🌟 WOW! Celebration time! " + achievement + " - you're doing fantastic!",
            "🎊 CONGRATULATIONS! " + achievement + " - I'm so proud of you!",
            "⭐ INCREDIBLE! You've achieved: " + achievement + "! Keep up the amazing work!",
            "🏆 SPECTACULAR! " + achievement + " - you should be extremely proud!",
            "🎯 TARGET REACHED! " + achievement + " - you're unstoppable!",
            "💪 SUPERB! " + achievement + " - this is what progress looks like!",
            "🌈 FANTASTIC! " + achievement + " - celebrate this moment!"
        };
        
        return celebrationMessages[random.nextInt(celebrationMessages.length)];
    }
    
    /**
     * Checks for milestone achievements
     */
    public String checkMilestones() {
        List<String> newMilestones = new ArrayList<>();
        
        // Check goal milestones
        if (goals != null) {
            for (GoalEntry goal : goals) {
                if (goal.completed) {
                    String key = "goal_" + goal.title;
                    int count = milestoneCounts.getOrDefault(key, 0);
                    if (count == 1) {
                        newMilestones.add("Completed first goal: " + goal.title);
                    }
                }
            }
        }
        
        // Check habit streaks
        if (habitHistory != null) {
            for (String habit : habitHistory.keySet()) {
                int streak = getHabitStreak(habit);
                if (streak == 7) {
                    newMilestones.add("Week-long streak with '" + habit + "'!");
                } else if (streak == 30) {
                    newMilestones.add("Month-long streak with '" + habit + "'!");
                }
            }
        }
        
        if (newMilestones.isEmpty()) {
            return "No new milestones yet. Keep working on your goals and habits!";
        }
        
        StringBuilder sb = new StringBuilder();
        for (String milestone : newMilestones) {
            sb.append(celebrateMilestone(milestone)).append("\n");
        }
        return sb.toString().trim();
    }
    
    /**
     * Gets milestone count
     */
    public int getMilestoneCount() {
        if (milestoneCounts == null) {
            return 0;
        }
        
        int total = 0;
        for (int count : milestoneCounts.values()) {
            total += count;
        }
        return total;
    }
    
    /**
     * Gets recent achievements summary
     */
    public String getAchievementsSummary() {
        int totalMilestones = getMilestoneCount();
        int activeGoals = goals != null ? (int) goals.stream().filter(g -> !g.completed).count() : 0;
        int completedGoals = goals != null ? (int) goals.stream().filter(g -> g.completed).count() : 0;
        
        return "Achievements Summary:\n" +
               "Total Milestones: " + totalMilestones + "\n" +
               "Active Goals: " + activeGoals + "\n" +
               "Completed Goals: " + completedGoals + "\n" +
               "Tracked Habits: " + (habitHistory != null ? habitHistory.size() : 0);
    }
    
    // ==================== Daily Questions ====================
    
    private List<DailyQuestion> dailyQuestions;
    private LocalDate lastDailyQuestion;
    
    /**
     * Daily question structure
     */
    private static class DailyQuestion {
        String category;
        List<String> questions;
        
        public DailyQuestion(String category, List<String> questions) {
            this.category = category;
            this.questions = questions;
        }
    }
    
    /**
     * Initializes daily questions
     */
    private void initializeDailyQuestions() {
        dailyQuestions = new ArrayList<>();
        
        // Self-reflection questions
        dailyQuestions.add(new DailyQuestion(
            "self_reflection",
            Arrays.asList(
                "What's one thing you learned about yourself today?",
                "What are you looking forward to tomorrow?",
                "What's something that made you smile today?",
                "What's a challenge you overcame recently?",
                "What are you proud of right now?"
            )
        ));
        
        // Relationships questions
        dailyQuestions.add(new DailyQuestion(
            "relationships",
            Arrays.asList(
                "Who made a positive impact on your day?",
                "How can you show appreciation to someone tomorrow?",
                "What's a memory with someone special that brightens your day?",
                "Who would you like to connect with soon?",
                "How have your relationships grown recently?"
            )
        ));
        
        // Growth questions
        dailyQuestions.add(new DailyQuestion(
            "growth",
            Arrays.asList(
                "What's a skill you'd like to develop?",
                "What's something new you tried recently?",
                "How have you grown in the past month?",
                "What's a lesson you learned the hard way?",
                "What's a belief that has changed for you?"
            )
        ));
        
        // Mindfulness questions
        dailyQuestions.add(new DailyQuestion(
            "mindfulness",
            Arrays.asList(
                "What does peace mean to you right now?",
                "What's bringing you joy in this moment?",
                "What are you holding onto that you could let go of?",
                "What's something beautiful you noticed today?",
                "How are you taking care of yourself right now?"
            )
        ));
        
        // Future questions
        dailyQuestions.add(new DailyQuestion(
            "future",
            Arrays.asList(
                "What does your ideal day look like?",
                "What's one thing you want to do differently tomorrow?",
                "What goals are you excited about?",
                "What does success mean to you?",
                "What's your vision for your life?"
            )
        ));
    }
    
    /**
     * Gets a daily question
     */
    public String getDailyQuestion() {
        if (dailyQuestions == null) {
            initializeDailyQuestions();
        }
        
        LocalDate today = LocalDate.now();
        int dayOfYear = today.getDayOfYear();
        
        int questionIndex = dayOfYear % dailyQuestions.size();
        DailyQuestion question = dailyQuestions.get(questionIndex);
        
        int promptIndex = dayOfYear % question.questions.size();
        return question.questions.get(promptIndex);
    }
    
    /**
     * Gets a random daily question
     */
    public String getRandomDailyQuestion() {
        if (dailyQuestions == null) {
            initializeDailyQuestions();
        }
        
        DailyQuestion question = dailyQuestions.get(random.nextInt(dailyQuestions.size()));
        return question.questions.get(random.nextInt(question.questions.size()));
    }
    
    /**
     * Gets a daily question for a specific category
     */
    public String getDailyQuestionForCategory(String category) {
        if (dailyQuestions == null) {
            initializeDailyQuestions();
        }
        
        for (DailyQuestion question : dailyQuestions) {
            if (question.category.equalsIgnoreCase(category)) {
                return question.questions.get(random.nextInt(question.questions.size()));
            }
        }
        return getRandomDailyQuestion();
    }
    
    /**
     * Gets all question categories
     */
    public List<String> getQuestionCategories() {
        if (dailyQuestions == null) {
            initializeDailyQuestions();
        }
        
        List<String> categories = new ArrayList<>();
        for (DailyQuestion question : dailyQuestions) {
            categories.add(question.category);
        }
        return categories;
    }
    
    // ==================== Weekly Reflections ====================
    
    private List<WeeklyReflection> weeklyReflections;
    private Map<LocalDate, WeeklyReflectionEntry> weeklyReflectionHistory;
    
    /**
     * Weekly reflection structure
     */
    private static class WeeklyReflection {
        String theme;
        List<String> prompts;
        
        public WeeklyReflection(String theme, List<String> prompts) {
            this.theme = theme;
            this.prompts = prompts;
        }
    }
    
    /**
     * Weekly reflection entry
     */
    private static class WeeklyReflectionEntry {
        LocalDate weekStart;
        String highlight;
        String challenge;
        String growth;
        String gratitude;
        String nextWeekFocus;
        
        public WeeklyReflectionEntry(LocalDate weekStart) {
            this.weekStart = weekStart;
        }
    }
    
    /**
     * Initializes weekly reflections
     */
    private void initializeWeeklyReflections() {
        weeklyReflections = new ArrayList<>();
        
        weeklyReflections.add(new WeeklyReflection(
            "highlight",
            Arrays.asList(
                "What was the best part of your week?",
                "What made you happiest this week?",
                "What's a moment you'll remember from this week?",
                "What was your biggest win this week?"
            )
        ));
        
        weeklyReflections.add(new WeeklyReflection(
            "challenge",
            Arrays.asList(
                "What was the most difficult part of your week?",
                "What challenged you this week?",
                "What's something that didn't go as planned?",
                "What are you struggling with right now?"
            )
        ));
        
        weeklyReflections.add(new WeeklyReflection(
            "growth",
            Arrays.asList(
                "What did you learn this week?",
                "How did you grow this week?",
                "What new insight did you gain?",
                "What skill did you develop?"
            )
        ));
        
        weeklyReflections.add(new WeeklyReflection(
            "gratitude",
            Arrays.asList(
                "What are you grateful for this week?",
                "Who made a difference in your week?",
                "What's something you appreciate about your life right now?",
                "What small thing brought you joy this week?"
            )
        ));
        
        weeklyReflections.add(new WeeklyReflection(
            "next_week",
            Arrays.asList(
                "What do you want to accomplish next week?",
                "What's your focus for next week?",
                "What do you want to do differently next week?",
                "What's coming up that you're looking forward to?"
            )
        ));
    }
    
    /**
     * Initiates weekly reflection
     */
    public String initiateWeeklyReflection() {
        if (weeklyReflections == null) {
            initializeWeeklyReflections();
        }
        
        if (weeklyReflectionHistory == null) {
            weeklyReflectionHistory = new HashMap<>();
        }
        
        String[] openingMessages = {
            "It's time for your weekly reflection! Let's look back on your week.",
            "Weekly check-in time! How did your week go?",
            "Let's reflect on your week together. How have things been?",
            "It's been a week! Tell me about how it went."
        };
        
        return openingMessages[random.nextInt(openingMessages.length)];
    }
    
    /**
     * Gets weekly reflection prompt
     */
    public String getWeeklyReflectionPrompt(String theme) {
        if (weeklyReflections == null) {
            initializeWeeklyReflections();
        }
        
        for (WeeklyReflection reflection : weeklyReflections) {
            if (reflection.theme.equalsIgnoreCase(theme)) {
                return reflection.prompts.get(random.nextInt(reflection.prompts.size()));
            }
        }
        
        // Default to highlight
        return getWeeklyReflectionPrompt("highlight");
    }
    
    /**
     * Records weekly reflection
     */
    public String recordWeeklyReflection(String highlight, String challenge, String growth, String gratitude, String nextWeekFocus) {
        if (weeklyReflectionHistory == null) {
            weeklyReflectionHistory = new HashMap<>();
        }
        
        LocalDate weekStart = getWeekStart(LocalDate.now());
        WeeklyReflectionEntry entry = new WeeklyReflectionEntry(weekStart);
        entry.highlight = highlight;
        entry.challenge = challenge;
        entry.growth = growth;
        entry.gratitude = gratitude;
        entry.nextWeekFocus = nextWeekFocus;
        
        weeklyReflectionHistory.put(weekStart, entry);
        
        String[] responses = {
            "Thank you for sharing your weekly reflection! This will help you track your progress.",
            "I've recorded your weekly reflection. It's great to take time for this!",
            "Your weekly reflection is saved! This is such a valuable practice."
        };
        
        return responses[random.nextInt(responses.length)];
    }
    
    /**
     * Gets week start date (Monday)
     */
    private LocalDate getWeekStart(LocalDate date) {
        return date.minusDays(date.getDayOfWeek().getValue() - 1);
    }
    
    /**
     * Gets weekly reflection summary
     */
    public String getWeeklyReflectionSummary() {
        if (weeklyReflectionHistory == null || weeklyReflectionHistory.isEmpty()) {
            return "No weekly reflections yet. Would you like to start one now?";
        }
        
        LocalDate currentWeekStart = getWeekStart(LocalDate.now());
        WeeklyReflectionEntry entry = weeklyReflectionHistory.get(currentWeekStart);
        
        if (entry == null) {
            return "You haven't completed this week's reflection yet. Would you like to?";
        }
        
        return "This Week's Reflection:\n" +
               "Highlight: " + (entry.highlight != null ? entry.highlight : "Not recorded") + "\n" +
               "Challenge: " + (entry.challenge != null ? entry.challenge : "Not recorded") + "\n" +
               "Growth: " + (entry.growth != null ? entry.growth : "Not recorded") + "\n" +
               "Gratitude: " + (entry.gratitude != null ? entry.gratitude : "Not recorded") + "\n" +
               "Next Week Focus: " + (entry.nextWeekFocus != null ? entry.nextWeekFocus : "Not recorded");
    }
    
    // ==================== Monthly Retrospectives ====================
    
    private Map<YearMonth, MonthlyRetrospectiveEntry> monthlyRetrospectiveHistory;
    
    /**
     * Monthly retrospective entry
     */
    private static class MonthlyRetrospectiveEntry {
        YearMonth month;
        String biggestAchievement;
        String lessonLearned;
        String areaToImprove;
        String celebration;
        String goalsForNextMonth;
        
        public MonthlyRetrospectiveEntry(YearMonth month) {
            this.month = month;
        }
    }
    
    /**
     * Initiates monthly retrospective
     */
    public String initiateMonthlyRetrospective() {
        String[] openingMessages = {
            "It's time for your monthly retrospective! Let's look back on your month.",
            "Monthly check-in time! How did your month go?",
            "Let's reflect on the past month together. How have things been?",
            "It's been a month! Tell me about your highlights and learnings."
        };
        
        return openingMessages[random.nextInt(openingMessages.length)];
    }
    
    /**
     * Gets monthly retrospective prompts
     */
    public Map<String, String> getMonthlyRetrospectivePrompts() {
        Map<String, String> prompts = new LinkedHashMap<>();
        
        prompts.put("achievement", "What was your biggest achievement this month?");
        prompts.put("lesson", "What's the most important lesson you learned?");
        prompts.put("improve", "What area would you like to improve?");
        prompts.put("celebrate", "What do you want to celebrate from this month?");
        prompts.put("next_month", "What are your goals for next month?");
        
        return prompts;
    }
    
    /**
     * Records monthly retrospective
     */
    public String recordMonthlyRetrospective(String biggestAchievement, String lessonLearned, 
                                              String areaToImprove, String celebration, 
                                              String goalsForNextMonth) {
        if (monthlyRetrospectiveHistory == null) {
            monthlyRetrospectiveHistory = new HashMap<>();
        }
        
        YearMonth currentMonth = YearMonth.now();
        MonthlyRetrospectiveEntry entry = new MonthlyRetrospectiveEntry(currentMonth);
        entry.biggestAchievement = biggestAchievement;
        entry.lessonLearned = lessonLearned;
        entry.areaToImprove = areaToImprove;
        entry.celebration = celebration;
        entry.goalsForNextMonth = goalsForNextMonth;
        
        monthlyRetrospectiveHistory.put(currentMonth, entry);
        
        // Celebrate the milestone
        return celebrateMilestone("completed monthly retrospective for " + currentMonth.getMonth().name());
    }
    
    /**
     * Gets monthly retrospective for current month
     */
    public String getMonthlyRetrospectiveSummary() {
        if (monthlyRetrospectiveHistory == null || monthlyRetrospectiveHistory.isEmpty()) {
            return "No monthly retrospectives yet. Would you like to start one now?";
        }
        
        YearMonth currentMonth = YearMonth.now();
        MonthlyRetrospectiveEntry entry = monthlyRetrospectiveHistory.get(currentMonth);
        
        if (entry == null) {
            return "You haven't completed this month's retrospective yet. Would you like to?";
        }
        
        return "This Month's Retrospective (" + currentMonth.getMonth().name() + " " + currentMonth.getYear() + "):\n" +
               "Biggest Achievement: " + (entry.biggestAchievement != null ? entry.biggestAchievement : "Not recorded") + "\n" +
               "Lesson Learned: " + (entry.lessonLearned != null ? entry.lessonLearned : "Not recorded") + "\n" +
               "Area to Improve: " + (entry.areaToImprove != null ? entry.areaToImprove : "Not recorded") + "\n" +
               "Celebration: " + (entry.celebration != null ? entry.celebration : "Not recorded") + "\n" +
               "Goals for Next Month: " + (entry.goalsForNextMonth != null ? entry.goalsForNextMonth : "Not recorded");
    }
    
    /**
     * Gets retrospective for a specific month
     */
    public String getMonthlyRetrospectiveForMonth(YearMonth month) {
        if (monthlyRetrospectiveHistory == null || !monthlyRetrospectiveHistory.containsKey(month)) {
            return "No retrospective found for " + month.getMonth().name() + " " + month.getYear();
        }
        
        MonthlyRetrospectiveEntry entry = monthlyRetrospectiveHistory.get(month);
        
        return month.getMonth().name() + " " + month.getYear() + " Retrospective:\n" +
               "Biggest Achievement: " + entry.biggestAchievement + "\n" +
               "Lesson Learned: " + entry.lessonLearned + "\n" +
               "Area to Improve: " + entry.areaToImprove + "\n" +
               "Celebration: " + entry.celebration + "\n" +
               "Goals for Next Month: " + entry.goalsForNextMonth;
    }
    
    /**
     * Gets all months with retrospectives
     */
    public List<String> getRetrospectiveMonths() {
        if (monthlyRetrospectiveHistory == null || monthlyRetrospectiveHistory.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<String> months = new ArrayList<>();
        for (YearMonth month : monthlyRetrospectiveHistory.keySet()) {
            months.add(month.getMonth().name() + " " + month.getYear());
        }
        return months;
    }
}

