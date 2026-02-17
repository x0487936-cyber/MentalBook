import java.util.*;
import java.text.SimpleDateFormat;

/**
 * EmotionalIntelligence - Advanced Emotional Awareness System
 * 
 * Features:
 * - Mood prediction based on conversation patterns
 * - Proactive check-ins when user seems stressed
 * - Emotional journey visualization
 * - Crisis detection with gentle resource suggestions
 * - Milestone celebrations for positive moments
 */
public class EmotionalIntelligence {
    
    private List<EmotionalPattern> emotionalHistory;
    private Map<String, EmotionalTrigger> triggerDatabase;
    private Map<String, CrisisResource> crisisResources;
    private Set<String> celebratedMilestones;
    private SimpleDateFormat dateFormat;
    private UserProfileDashboard userProfile;
    
    // Pattern detection thresholds
    private static final int STRESS_THRESHOLD = 7;
    private static final int CRISIS_KEYWORD_COUNT = 3;
    private static final long CHECKIN_INTERVAL = 24 * 60 * 60 * 1000; // 24 hours
    
    public EmotionalIntelligence(UserProfileDashboard profile) {
        this.emotionalHistory = new ArrayList<>();
        this.triggerDatabase = initializeTriggers();
        this.crisisResources = initializeCrisisResources();
        this.celebratedMilestones = new HashSet<>();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.userProfile = profile;
    }
    
    /**
     * Emotional pattern entry
     */
    private static class EmotionalPattern {
        Date timestamp;
        String detectedMood;
        double confidence;
        List<String> indicators;
        String context;
        int severity; // 1-10
        
        EmotionalPattern(String mood, double confidence, List<String> indicators, String context, int severity) {
            this.timestamp = new Date();
            this.detectedMood = mood;
            this.confidence = confidence;
            this.indicators = indicators;
            this.context = context;
            this.severity = severity;
        }
    }
    
    /**
     * Emotional trigger definition
     */
    private static class EmotionalTrigger {
        String emotion;
        List<String> keywords;
        List<String> phrases;
        int weight;
        
        EmotionalTrigger(String emotion, List<String> keywords, List<String> phrases, int weight) {
            this.emotion = emotion;
            this.keywords = keywords;
            this.phrases = phrases;
            this.weight = weight;
        }
    }
    
    /**
     * Crisis resource information
     */
    private static class CrisisResource {
        String name;
        String contact;
        String description;
        boolean isEmergency;
        
        CrisisResource(String name, String contact, String description, boolean isEmergency) {
            this.name = name;
            this.contact = contact;
            this.description = description;
            this.isEmergency = isEmergency;
        }
    }
    
    // ==================== INITIALIZATION ====================
    
    private Map<String, EmotionalTrigger> initializeTriggers() {
        Map<String, EmotionalTrigger> triggers = new HashMap<>();
        
        // Stress/Anxiety triggers
        triggers.put("stress", new EmotionalTrigger("stressed",
            Arrays.asList("stressed", "anxious", "worried", "overwhelmed", "pressure", "deadline", "panic"),
            Arrays.asList("can't handle", "too much", "burning out", "breaking down", "losing control"),
            3
        ));
        
        // Sadness triggers
        triggers.put("sadness", new EmotionalTrigger("sad",
            Arrays.asList("sad", "depressed", "unhappy", "miserable", "crying", "tears", "lonely", "empty"),
            Arrays.asList("feel empty", "no point", "giving up", "worthless", "hopeless"),
            4
        ));
        
        // Anger triggers
        triggers.put("anger", new EmotionalTrigger("angry",
            Arrays.asList("angry", "mad", "furious", "annoyed", "irritated", "frustrated", "hate"),
            Arrays.asList("so mad", "can't stand", "makes me furious", "want to scream"),
            2
        ));
        
        // Fear triggers
        triggers.put("fear", new EmotionalTrigger("afraid",
            Arrays.asList("scared", "afraid", "terrified", "fear", "worried", "nervous", "panic"),
            Arrays.asList("so scared", "frightened", "can't stop worrying"),
            3
        ));
        
        // Happiness triggers
        triggers.put("joy", new EmotionalTrigger("happy",
            Arrays.asList("happy", "joyful", "excited", "great", "amazing", "wonderful", "fantastic", "love"),
            Arrays.asList("so happy", "best day", "couldn't be better", "on top of the world"),
            2
        ));
        
        // Crisis indicators
        triggers.put("crisis", new EmotionalTrigger("crisis",
            Arrays.asList("suicide", "kill myself", "end it all", "don't want to live", "hurt myself", "self-harm"),
            Arrays.asList("better off dead", "no reason to live", "want to die", "end my life"),
            10
        ));
        
        return triggers;
    }
    
    private Map<String, CrisisResource> initializeCrisisResources() {
        Map<String, CrisisResource> resources = new HashMap<>();
        
        resources.put("suicide_prevention", new CrisisResource(
            "988 Suicide & Crisis Lifeline",
            "Call or text 988",
            "24/7 free and confidential support for people in distress",
            true
        ));
        
        resources.put("crisis_text", new CrisisResource(
            "Crisis Text Line",
            "Text HOME to 741741",
            "Free 24/7 support via text message",
            true
        ));
        
        resources.put("nami", new CrisisResource(
            "NAMI HelpLine",
            "1-800-950-NAMI (6264)",
            "Mental health information and resources",
            false
        ));
        
        resources.put("samhsa", new CrisisResource(
            "SAMHSA National Helpline",
            "1-800-662-4357",
            "Free, confidential, 24/7 treatment referral",
            false
        ));
        
        return resources;
    }
    
    // ==================== MOOD PREDICTION ====================
    
    public EmotionalAssessment analyzeInput(String input) {
        String lowerInput = input.toLowerCase();
        Map<String, Integer> emotionScores = new HashMap<>();
        List<String> detectedIndicators = new ArrayList<>();
        
        // Check each trigger category
        for (EmotionalTrigger trigger : triggerDatabase.values()) {
            int score = 0;
            
            // Check keywords
            for (String keyword : trigger.keywords) {
                if (lowerInput.contains(keyword)) {
                    score += trigger.weight;
                    detectedIndicators.add(keyword);
                }
            }
            
            // Check phrases
            for (String phrase : trigger.phrases) {
                if (lowerInput.contains(phrase)) {
                    score += trigger.weight * 2; // Phrases weighted more
                    detectedIndicators.add(phrase);
                }
            }
            
            if (score > 0) {
                emotionScores.merge(trigger.emotion, score, Integer::sum);
            }
        }
        
        // Determine dominant emotion
        String dominantEmotion = "neutral";
        int maxScore = 0;
        int totalSeverity = 0;
        
        for (Map.Entry<String, Integer> entry : emotionScores.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                dominantEmotion = entry.getKey();
            }
            totalSeverity += entry.getValue();
        }
        
        // Calculate confidence
        double confidence = Math.min(1.0, maxScore / 10.0);
        int severity = Math.min(10, totalSeverity);
        
        // Check for crisis
        boolean isCrisis = checkForCrisis(lowerInput, emotionScores);
        
        // Record pattern
        EmotionalPattern pattern = new EmotionalPattern(
            dominantEmotion, confidence, detectedIndicators, input, severity
        );
        emotionalHistory.add(pattern);
        
        // Keep history manageable
        if (emotionalHistory.size() > 100) {
            emotionalHistory.remove(0);
        }
        
        // Record in user profile if available
        if (userProfile != null && userProfile.getCurrentProfile() != null) {
            userProfile.recordMood(dominantEmotion, input, severity, 
                detectedIndicators.isEmpty() ? "none" : detectedIndicators.get(0));
        }
        
        return new EmotionalAssessment(dominantEmotion, confidence, detectedIndicators, 
            severity, isCrisis, generateResponse(dominantEmotion, severity, isCrisis));
    }
    
    private boolean checkForCrisis(String input, Map<String, Integer> scores) {
        // Check for crisis keywords
        EmotionalTrigger crisisTrigger = triggerDatabase.get("crisis");
        if (crisisTrigger != null) {
            for (String keyword : crisisTrigger.keywords) {
                if (input.contains(keyword)) return true;
            }
            for (String phrase : crisisTrigger.phrases) {
                if (input.contains(phrase)) return true;
            }
        }
        
        // Check for multiple negative indicators
        int negativeScore = scores.getOrDefault("sadness", 0) + 
                           scores.getOrDefault("stress", 0) +
                           scores.getOrDefault("fear", 0);
        
        return negativeScore >= CRISIS_KEYWORD_COUNT * 3;
    }
    
    // ==================== RESPONSE GENERATION ====================
    
    private String generateResponse(String emotion, int severity, boolean isCrisis) {
        if (isCrisis) {
            return generateCrisisResponse();
        }
        
        if (severity >= STRESS_THRESHOLD) {
            return generateSupportResponse(emotion, severity);
        }
        
        if (emotion.equals("happy") || emotion.equals("joy")) {
            return generateCelebrationResponse(severity);
        }
        
        return null; // No special response needed
    }
    
    private String generateCrisisResponse() {
        StringBuilder response = new StringBuilder();
        response.append("💙 I hear you, and I want you to know that your life matters. 💙\n\n");
        response.append("It sounds like you're going through an incredibly difficult time. ");
        response.append("Please reach out to a professional who can provide the support you deserve:\n\n");
        
        for (CrisisResource resource : crisisResources.values()) {
            if (resource.isEmergency) {
                response.append("🚨 **").append(resource.name).append("**\n");
                response.append("   ").append(resource.contact).append("\n");
                response.append("   ").append(resource.description).append("\n\n");
            }
        }
        
        response.append("You don't have to face this alone. People care about you and want to help. ");
        response.append("Please consider reaching out to someone you trust as well.\n\n");
        response.append("Would you like me to stay here and chat with you?");
        
        return response.toString();
    }
    
    private String generateSupportResponse(String emotion, int severity) {
        String[] stressResponses = {
            "I can hear the stress in your words. Would you like to talk about what's weighing on you?",
            "It sounds like you're carrying a heavy load right now. I'm here to listen.",
            "That sounds really overwhelming. Want to break it down together?",
            "I notice you seem stressed. Sometimes talking about it can help. What's on your mind?",
            "You sound like you could use a moment to breathe. I'm here when you're ready to talk."
        };
        
        String[] sadnessResponses = {
            "I hear sadness in your message. I'm here with you. Want to share more?",
            "That sounds really hard. You don't have to go through this alone.",
            "I'm sorry you're feeling this way. Would talking about it help?",
            "It sounds like you're hurting. I'm here to listen without judgment.",
            "Sometimes life feels heavy. I'm here to sit with you in that feeling."
        };
        
        String[] anxietyResponses = {
            "I can sense the anxiety in your words. Would grounding techniques help?",
            "That sounds worrying. Want to explore what's making you feel this way?",
            "Anxiety can be so overwhelming. I'm here to help you work through it.",
            "It sounds like your mind is racing. Would you like to try a calming exercise?",
            "I notice you seem anxious. Let's take this one step at a time together."
        };
        
        String[] responses;
        switch (emotion) {
            case "stressed": responses = stressResponses; break;
            case "sad": responses = sadnessResponses; break;
            case "afraid": responses = anxietyResponses; break;
            default: responses = stressResponses;
        }
        
        return responses[new Random().nextInt(responses.length)];
    }
    
    private String generateCelebrationResponse(int severity) {
        String[] celebrations = {
            "🎉 That's wonderful! I love hearing good news!",
            "✨ That's amazing! You deserve this happiness!",
            "🌟 Fantastic! What's making you feel so great?",
            "💫 I'm so happy for you! Tell me more!",
            "🎊 This is worth celebrating! What's the occasion?"
        };
        
        return celebrations[new Random().nextInt(celebrations.length)];
    }
    
    // ==================== PROACTIVE CHECK-INS ====================
    
    public String checkProactiveSupport() {
        if (emotionalHistory.isEmpty()) return null;
        
        // Check for declining mood trend
        List<EmotionalPattern> recent = getRecentPatterns(5);
        if (recent.size() < 3) return null;
        
        boolean declining = true;
        boolean stressed = false;
        
        for (EmotionalPattern pattern : recent) {
            if (pattern.severity < 5) declining = false;
            if (pattern.detectedMood.equals("stressed") || pattern.severity > 7) {
                stressed = true;
            }
        }
        
        if (declining && stressed) {
            long lastCheckin = getLastCheckinTime();
            if (System.currentTimeMillis() - lastCheckin > CHECKIN_INTERVAL) {
                recordCheckin();
                return generateCheckinMessage(recent.get(recent.size() - 1));
            }
        }
        
        return null;
    }
    
    private String generateCheckinMessage(EmotionalPattern lastPattern) {
        String[] checkins = {
            "Hey, I've noticed you seem to be going through a tough time lately. How are you doing today?",
            "I wanted to check in - you seemed stressed in our last conversation. Is everything okay?",
            "Thinking of you. How have you been feeling since we last talked?",
            "Just wanted to reach out and see how you're doing. I'm here if you need to talk.",
            "I noticed things seemed difficult before. Have things improved at all?"
        };
        
        return checkins[new Random().nextInt(checkins.length)];
    }
    
    // ==================== MILESTONE CELEBRATIONS ====================
    
    public String checkMilestones() {
        if (userProfile == null || userProfile.getCurrentProfile() == null) return null;
        
        StringBuilder celebrations = new StringBuilder();
        UserProfileDashboard.UserProfile profile = userProfile.getCurrentProfile();
        
        // Check conversation milestones
        int conversations = profile.totalConversations;
        String[] conversationMilestones = {"10", "50", "100", "500", "1000"};
        
        for (String milestone : conversationMilestones) {
            int m = Integer.parseInt(milestone);
            if (conversations == m && !celebratedMilestones.contains("conv_" + milestone)) {
                celebratedMilestones.add("conv_" + milestone);
                celebrations.append(String.format("🎊 Milestone! We've had %d conversations together! ", m));
                celebrations.append("Thanks for making me part of your journey! 💙\n\n");
            }
        }
        
        // Check positive mood streaks
        List<UserProfileDashboard.MoodEntry> recentMoods = getRecentMoods(7);
        if (recentMoods.size() >= 5) {
            boolean allPositive = recentMoods.stream()
                .allMatch(m -> m.mood.equals("happy") || m.mood.equals("joy") || m.intensity > 6);
            
            if (allPositive && !celebratedMilestones.contains("positive_week")) {
                celebratedMilestones.add("positive_week");
                celebrations.append("🌟 Wow! You've had a whole week of positive moods! ");
                celebrations.append("That's amazing! What's your secret? 😊\n\n");
            }
        }
        
        // Check improvement from negative to positive
        if (emotionalHistory.size() >= 10) {
            List<EmotionalPattern> firstHalf = emotionalHistory.subList(0, emotionalHistory.size() / 2);
            List<EmotionalPattern> secondHalf = emotionalHistory.subList(emotionalHistory.size() / 2, emotionalHistory.size());
            
            double firstAvg = firstHalf.stream().mapToInt(p -> p.severity).average().orElse(5);
            double secondAvg = secondHalf.stream().mapToInt(p -> p.severity).average().orElse(5);
            
            if (firstAvg > 6 && secondAvg < 4 && !celebratedMilestones.contains("improvement")) {
                celebratedMilestones.add("improvement");
                celebrations.append("💪 I'm so proud of you! Your mood has really improved lately. ");
                celebrations.append("Keep up the great work! 🎉\n\n");
            }
        }
        
        return celebrations.length() > 0 ? celebrations.toString() : null;
    }
    
    // ==================== EMOTIONAL JOURNEY VISUALIZATION ====================
    
    public String getEmotionalJourneyReport(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        Date cutoff = cal.getTime();
        
        List<EmotionalPattern> relevant = new ArrayList<>();
        for (EmotionalPattern pattern : emotionalHistory) {
            if (pattern.timestamp.after(cutoff)) {
                relevant.add(pattern);
            }
        }
        
        if (relevant.isEmpty()) {
            return "No emotional data recorded for the last " + days + " days.";
        }
        
        StringBuilder report = new StringBuilder();
        report.append("📊 Emotional Journey Report (Last ").append(days).append(" Days)\n\n");
        
        // Mood distribution
        Map<String, Integer> moodCounts = new HashMap<>();
        int totalSeverity = 0;
        int crisisCount = 0;
        
        for (EmotionalPattern pattern : relevant) {
            moodCounts.merge(pattern.detectedMood, 1, Integer::sum);
            totalSeverity += pattern.severity;
            if (pattern.severity >= STRESS_THRESHOLD) crisisCount++;
        }
        
        report.append("🎭 Mood Distribution:\n");
        for (Map.Entry<String, Integer> entry : moodCounts.entrySet()) {
            int pct = (entry.getValue() * 100) / relevant.size();
            report.append(String.format("  %s: %d%% (%d times)\n", 
                capitalize(entry.getKey()), pct, entry.getValue()));
        }
        
        report.append(String.format("\n📈 Average Intensity: %.1f/10\n", 
            (double) totalSeverity / relevant.size()));
        report.append(String.format("⚠️ High-stress moments: %d\n", crisisCount));
        
        // Trend analysis
        if (relevant.size() >= 5) {
            report.append("\n📉 Trend Analysis:\n");
            
            List<EmotionalPattern> first = relevant.subList(0, relevant.size() / 2);
            List<EmotionalPattern> second = relevant.subList(relevant.size() / 2, relevant.size());
            
            double firstAvg = first.stream().mapToInt(p -> p.severity).average().orElse(5);
            double secondAvg = second.stream().mapToInt(p -> p.severity).average().orElse(5);
            
            if (secondAvg < firstAvg - 1) {
                report.append("  ✓ Your emotional state has been improving! 📈\n");
            } else if (secondAvg > firstAvg + 1) {
                report.append("  ⚠ Your emotional state has declined. Consider self-care. 📉\n");
            } else {
                report.append("  → Your emotional state has been stable. 🔄\n");
            }
        }
        
        // Insights
        report.append("\n💡 Insights:\n");
        if (crisisCount > 3) {
            report.append("  • You've had several high-stress moments. Consider stress management techniques.\n");
        }
        if (moodCounts.getOrDefault("happy", 0) + moodCounts.getOrDefault("joy", 0) > relevant.size() / 2) {
            report.append("  • You've maintained mostly positive moods. Great job! 🌟\n");
        }
        if (moodCounts.getOrDefault("stressed", 0) > relevant.size() / 3) {
            report.append("  • Stress appears frequently. What triggers can you identify?\n");
        }
        
        return report.toString();
    }
    
    public String getEmotionalJourneyChart(int entries) {
        if (emotionalHistory.isEmpty()) {
            return "No emotional journey data available.";
        }
        
        StringBuilder chart = new StringBuilder();
        chart.append("📊 Emotional Journey Chart (Last ").append(entries).append(" Interactions)\n\n");
        
        int start = Math.max(0, emotionalHistory.size() - entries);
        
        for (int i = start; i < emotionalHistory.size(); i++) {
            EmotionalPattern pattern = emotionalHistory.get(i);
            String emoji = getMoodEmoji(pattern.detectedMood);
            String bar = "█".repeat(pattern.severity);
            String time = dateFormat.format(pattern.timestamp);
            
            chart.append(String.format("%s %s | %s %s | %.0f%% confidence\n",
                time, emoji, bar, capitalize(pattern.detectedMood), 
                pattern.confidence * 100));
        }
        
        return chart.toString();
    }
    
    // ==================== UTILITY METHODS ====================
    
    private List<EmotionalPattern> getRecentPatterns(int count) {
        int start = Math.max(0, emotionalHistory.size() - count);
        return emotionalHistory.subList(start, emotionalHistory.size());
    }
    
    private List<UserProfileDashboard.MoodEntry> getRecentMoods(int days) {
        if (userProfile == null) return new ArrayList<>();
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        Date cutoff = cal.getTime();
        
        List<UserProfileDashboard.MoodEntry> recent = new ArrayList<>();
        for (UserProfileDashboard.MoodEntry entry : userProfile.getCurrentProfile().moodHistory) {
            if (entry.timestamp.after(cutoff)) {
                recent.add(entry);
            }
        }
        return recent;
    }
    
    private long getLastCheckinTime() {
        // In a real implementation, this would be persisted
        return 0;
    }
    
    private void recordCheckin() {
        // In a real implementation, this would be persisted
    }
    
    private String getMoodEmoji(String mood) {
        switch (mood) {
            case "happy": case "joy": return "😊";
            case "sad": return "😢";
            case "stressed": return "😰";
            case "angry": return "😠";
            case "afraid": return "😨";
            case "neutral": return "😐";
            case "crisis": return "🚨";
            default: return "🤔";
        }
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    // ==================== PUBLIC CLASSES ====================
    
    public static class EmotionalAssessment {
        public String dominantMood;
        public double confidence;
        public List<String> indicators;
        public int severity;
        public boolean isCrisis;
        public String suggestedResponse;
        
        public EmotionalAssessment(String mood, double confidence, List<String> indicators,
                                   int severity, boolean isCrisis, String response) {
            this.dominantMood = mood;
            this.confidence = confidence;
            this.indicators = indicators;
            this.severity = severity;
            this.isCrisis = isCrisis;
            this.suggestedResponse = response;
        }
    }
}
