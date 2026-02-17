import java.util.*;
import java.text.SimpleDateFormat;
import java.io.*;

/**
 * UserProfileDashboard - Advanced Memory & Personalization System
 * 
 * Features:
 * - User profile management with interests, preferences, and stats
 * - Mood history tracking and visualization
 * - "Remember this" feature for important facts
 * - Birthday and anniversary reminders
 * - Personalized greetings based on time and history
 * - Conversation summary generation
 */
public class UserProfileDashboard {
    
    private Map<String, UserProfile> profiles;
    private String currentUserId;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    
    public UserProfileDashboard() {
        this.profiles = new HashMap<>();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.timeFormat = new SimpleDateFormat("HH:mm");
        loadProfiles();
    }
    
    /**
     * User profile containing all personalization data
     */
    public static class UserProfile {
        String userId;
        String name;
        Date birthday;
        Date createdDate;
        Map<String, String> importantFacts;
        Set<String> interests;
        List<MoodEntry> moodHistory;
        List<ConversationSummary> conversationHistory;
        Map<String, Object> preferences;
        int totalConversations;
        int totalMessages;
        long totalChatTime;
        Map<String, Integer> topicFrequency;
        List<String> favoriteTopics;
        
        public UserProfile(String userId) {
            this.userId = userId;
            this.name = "Friend";
            this.createdDate = new Date();
            this.importantFacts = new HashMap<>();
            this.interests = new HashSet<>();
            this.moodHistory = new ArrayList<>();
            this.conversationHistory = new ArrayList<>();
            this.preferences = new HashMap<>();
            this.topicFrequency = new HashMap<>();
            this.favoriteTopics = new ArrayList<>();
            this.totalConversations = 0;
            this.totalMessages = 0;
            this.totalChatTime = 0;
        }
    }
    
    /**
     * Mood entry for tracking emotional journey
     */
    public static class MoodEntry {
        Date timestamp;
        String mood;
        String context;
        int intensity; // 1-10
        String trigger;
        
        public MoodEntry(String mood, String context, int intensity, String trigger) {
            this.timestamp = new Date();
            this.mood = mood;
            this.context = context;
            this.intensity = intensity;
            this.trigger = trigger;
        }
    }
    
    /**
     * Conversation summary for history
     */
    public static class ConversationSummary {
        Date date;
        String topic;
        int messageCount;
        String keyInsight;
        String mood;
        long duration;
        
        public ConversationSummary(String topic, int messageCount, String insight, String mood, long duration) {
            this.date = new Date();
            this.topic = topic;
            this.messageCount = messageCount;
            this.keyInsight = insight;
            this.mood = mood;
            this.duration = duration;
        }
    }
    
    // ==================== PROFILE MANAGEMENT ====================
    
    public void setCurrentUser(String userId) {
        this.currentUserId = userId;
        profiles.putIfAbsent(userId, new UserProfile(userId));
    }
    
    public UserProfile getCurrentProfile() {
        if (currentUserId == null) return null;
        return profiles.get(currentUserId);
    }
    
    public void setUserName(String name) {
        UserProfile profile = getCurrentProfile();
        if (profile != null) {
            profile.name = name;
        }
    }
    
    public void setBirthday(String dateStr) {
        try {
            UserProfile profile = getCurrentProfile();
            if (profile != null) {
                profile.birthday = dateFormat.parse(dateStr);
            }
        } catch (Exception e) {
            System.out.println("Invalid date format. Use: yyyy-MM-dd");
        }
    }
    
    // ==================== "REMEMBER THIS" FEATURE ====================
    
    public void rememberFact(String key, String value) {
        UserProfile profile = getCurrentProfile();
        if (profile != null) {
            profile.importantFacts.put(key.toLowerCase(), value);
            saveProfiles();
        }
    }
    
    public String recallFact(String key) {
        UserProfile profile = getCurrentProfile();
        if (profile == null) return null;
        return profile.importantFacts.get(key.toLowerCase());
    }
    
    public String getAllFacts() {
        UserProfile profile = getCurrentProfile();
        if (profile == null || profile.importantFacts.isEmpty()) {
            return "I don't have any saved facts about you yet. Use 'remember that [key] is [value]' to save something!";
        }
        
        StringBuilder sb = new StringBuilder("📝 Things I Remember About You:\n\n");
        for (Map.Entry<String, String> entry : profile.importantFacts.entrySet()) {
            sb.append("• ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
    
    // ==================== INTERESTS TRACKING ====================
    
    public void addInterest(String interest) {
        UserProfile profile = getCurrentProfile();
        if (profile != null) {
            profile.interests.add(interest.toLowerCase());
            updateFavoriteTopics(profile);
            saveProfiles();
        }
    }
    
    public void recordTopic(String topic) {
        UserProfile profile = getCurrentProfile();
        if (profile != null) {
            profile.topicFrequency.merge(topic.toLowerCase(), 1, Integer::sum);
            updateFavoriteTopics(profile);
        }
    }
    
    private void updateFavoriteTopics(UserProfile profile) {
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(profile.topicFrequency.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        profile.favoriteTopics.clear();
        for (int i = 0; i < Math.min(5, sorted.size()); i++) {
            profile.favoriteTopics.add(sorted.get(i).getKey());
        }
    }
    
    // ==================== MOOD TRACKING ====================
    
    public void recordMood(String mood, String context, int intensity, String trigger) {
        UserProfile profile = getCurrentProfile();
        if (profile != null) {
            profile.moodHistory.add(new MoodEntry(mood, context, intensity, trigger));
            if (profile.moodHistory.size() > 100) {
                profile.moodHistory.remove(0); // Keep last 100 entries
            }
            saveProfiles();
        }
    }
    
    public String getMoodHistory(int days) {
        UserProfile profile = getCurrentProfile();
        if (profile == null || profile.moodHistory.isEmpty()) {
            return "No mood history recorded yet.";
        }
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        Date cutoff = cal.getTime();
        
        Map<String, Integer> moodCounts = new HashMap<>();
        int totalIntensity = 0;
        int count = 0;
        
        for (MoodEntry entry : profile.moodHistory) {
            if (entry.timestamp.after(cutoff)) {
                moodCounts.merge(entry.mood, 1, Integer::sum);
                totalIntensity += entry.intensity;
                count++;
            }
        }
        
        if (count == 0) return "No mood data for the last " + days + " days.";
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("📊 Mood History (Last %d Days)\n\n", days));
        sb.append("Mood Distribution:\n");
        
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(moodCounts.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        for (Map.Entry<String, Integer> entry : sorted) {
            int percentage = (entry.getValue() * 100) / count;
            sb.append(String.format("  %s: %d%% (%d times)\n", entry.getKey(), percentage, entry.getValue()));
        }
        
        sb.append(String.format("\nAverage Intensity: %.1f/10\n", (double) totalIntensity / count));
        sb.append(String.format("Total Entries: %d\n", count));
        
        // Trend analysis
        if (count >= 5) {
            sb.append("\n📈 Trend Analysis:\n");
            List<MoodEntry> recent = profile.moodHistory.subList(
                Math.max(0, profile.moodHistory.size() - 5), 
                profile.moodHistory.size()
            );
            
            boolean improving = true;
            boolean declining = true;
            for (int i = 1; i < recent.size(); i++) {
                if (recent.get(i).intensity < recent.get(i-1).intensity) improving = false;
                if (recent.get(i).intensity > recent.get(i-1).intensity) declining = false;
            }
            
            if (improving) sb.append("Your mood has been improving! 📈\n");
            else if (declining) sb.append("I've noticed your mood declining. Want to talk about it? 💙\n");
            else sb.append("Your mood has been varied - that's normal! 🌈\n");
        }
        
        return sb.toString();
    }
    
    public String getMoodChart(int entries) {
        UserProfile profile = getCurrentProfile();
        if (profile == null || profile.moodHistory.isEmpty()) {
            return "No mood data to chart.";
        }
        
        StringBuilder chart = new StringBuilder();
        chart.append("📊 Mood Journey (Last ").append(Math.min(entries, profile.moodHistory.size())).append(" entries)\n\n");
        
        int start = Math.max(0, profile.moodHistory.size() - entries);
        for (int i = start; i < profile.moodHistory.size(); i++) {
            MoodEntry entry = profile.moodHistory.get(i);
            String bar = "█".repeat(entry.intensity);
            chart.append(String.format("%s | %s %s | %s\n", 
                timeFormat.format(entry.timestamp),
                bar,
                entry.mood,
                entry.context
            ));
        }
        
        return chart.toString();
    }
    
    // ==================== BIRTHDAY & REMINDERS ====================
    
    public String checkReminders() {
        UserProfile profile = getCurrentProfile();
        if (profile == null) return "";
        
        StringBuilder reminders = new StringBuilder();
        Date today = new Date();
        
        // Check birthday
        if (profile.birthday != null) {
            Calendar birthCal = Calendar.getInstance();
            birthCal.setTime(profile.birthday);
            Calendar todayCal = Calendar.getInstance();
            todayCal.setTime(today);
            
            if (birthCal.get(Calendar.MONTH) == todayCal.get(Calendar.MONTH) &&
                birthCal.get(Calendar.DAY_OF_MONTH) == todayCal.get(Calendar.DAY_OF_MONTH)) {
                reminders.append("🎉🎂 HAPPY BIRTHDAY ").append(profile.name.toUpperCase()).append("! 🎂🎉\n\n");
                reminders.append("I hope you have an amazing day filled with joy and celebration!\n\n");
            }
        }
        
        // Check for anniversary of first conversation
        Calendar createdCal = Calendar.getInstance();
        createdCal.setTime(profile.createdDate);
        Calendar todayCal = Calendar.getInstance();
        
        if (createdCal.get(Calendar.MONTH) == todayCal.get(Calendar.MONTH) &&
            createdCal.get(Calendar.DAY_OF_MONTH) == todayCal.get(Calendar.DAY_OF_MONTH)) {
            long daysSince = (today.getTime() - profile.createdDate.getTime()) / (1000 * 60 * 60 * 24);
            if (daysSince > 365) {
                int years = (int) (daysSince / 365);
                reminders.append(String.format("🎊 Happy %d Year Anniversary! 🎊\n\n", years));
                reminders.append(String.format("We've been chatting for %d years now. Thanks for being here! 💙\n\n", years));
            }
        }
        
        return reminders.toString();
    }
    
    // ==================== PERSONALIZED GREETINGS ====================
    
    public String getPersonalizedGreeting() {
        UserProfile profile = getCurrentProfile();
        if (profile == null) return "Hello!";
        
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String timeGreeting;
        
        if (hour < 6) timeGreeting = "You're up late";
        else if (hour < 12) timeGreeting = "Good morning";
        else if (hour < 17) timeGreeting = "Good afternoon";
        else if (hour < 22) timeGreeting = "Good evening";
        else timeGreeting = "Good night";
        
        StringBuilder greeting = new StringBuilder();
        greeting.append(timeGreeting);
        
        if (!profile.name.equals("Friend")) {
            greeting.append(", ").append(profile.name);
        }
        greeting.append("! ");
        
        // Add personalized touch based on history
        if (profile.totalConversations > 50) {
            greeting.append("Great to see you again! ");
        }
        
        if (!profile.favoriteTopics.isEmpty()) {
            greeting.append(String.format("Last time we talked about %s. ", 
                profile.favoriteTopics.get(0)));
        }
        
        // Check mood trend
        if (profile.moodHistory.size() >= 3) {
            MoodEntry lastMood = profile.moodHistory.get(profile.moodHistory.size() - 1);
            if (lastMood.intensity < 5) {
                greeting.append("I noticed you weren't feeling great before. How are you doing now? 💙");
            } else {
                greeting.append("You seemed to be doing well! What's new? 😊");
            }
        } else {
            greeting.append("How can I help you today?");
        }
        
        return greeting.toString();
    }
    
    // ==================== CONVERSATION SUMMARIES ====================
    
    public void startConversation() {
        UserProfile profile = getCurrentProfile();
        if (profile != null) {
            profile.totalConversations++;
            profile.preferences.put("conversationStart", System.currentTimeMillis());
            profile.preferences.put("messageCount", 0);
        }
    }
    
    public void recordMessage() {
        UserProfile profile = getCurrentProfile();
        if (profile != null) {
            profile.totalMessages++;
            int count = (Integer) profile.preferences.getOrDefault("messageCount", 0);
            profile.preferences.put("messageCount", count + 1);
        }
    }
    
    public void endConversation(String topic, String keyInsight, String mood) {
        UserProfile profile = getCurrentProfile();
        if (profile != null) {
            long startTime = (Long) profile.preferences.getOrDefault("conversationStart", System.currentTimeMillis());
            long duration = System.currentTimeMillis() - startTime;
            int messageCount = (Integer) profile.preferences.getOrDefault("messageCount", 0);
            
            profile.conversationHistory.add(new ConversationSummary(topic, messageCount, keyInsight, mood, duration));
            profile.totalChatTime += duration;
            
            if (profile.conversationHistory.size() > 50) {
                profile.conversationHistory.remove(0);
            }
            
            saveProfiles();
        }
    }
    
    public String getConversationSummary() {
        UserProfile profile = getCurrentProfile();
        if (profile == null) return "No profile data available.";
        
        StringBuilder sb = new StringBuilder();
        sb.append("📊 Your Conversation Stats\n\n");
        sb.append(String.format("Total Conversations: %d\n", profile.totalConversations));
        sb.append(String.format("Total Messages: %d\n", profile.totalMessages));
        sb.append(String.format("Total Chat Time: %.1f hours\n", profile.totalChatTime / (1000.0 * 60 * 60)));
        
        if (!profile.favoriteTopics.isEmpty()) {
            sb.append("\n🏷️ Favorite Topics:\n");
            for (String topic : profile.favoriteTopics) {
                int count = profile.topicFrequency.getOrDefault(topic, 0);
                sb.append(String.format("  • %s (%d times)\n", topic, count));
            }
        }
        
        if (!profile.interests.isEmpty()) {
            sb.append("\n💡 Your Interests:\n");
            for (String interest : profile.interests) {
                sb.append(String.format("  • %s\n", interest));
            }
        }
        
        return sb.toString();
    }
    
    public String getRecentConversations(int count) {
        UserProfile profile = getCurrentProfile();
        if (profile == null || profile.conversationHistory.isEmpty()) {
            return "No conversation history yet.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("📚 Recent Conversations:\n\n");
        
        int start = Math.max(0, profile.conversationHistory.size() - count);
        for (int i = profile.conversationHistory.size() - 1; i >= start; i--) {
            ConversationSummary summary = profile.conversationHistory.get(i);
            sb.append(String.format("📅 %s\n", dateFormat.format(summary.date)));
            sb.append(String.format("  Topic: %s\n", summary.topic));
            sb.append(String.format("  Messages: %d | Mood: %s\n", summary.messageCount, summary.mood));
            if (summary.keyInsight != null) {
                sb.append(String.format("  💭 %s\n", summary.keyInsight));
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    // ==================== DASHBOARD VIEW ====================
    
    public String getFullDashboard() {
        UserProfile profile = getCurrentProfile();
        if (profile == null) return "Please set up your profile first!";
        
        StringBuilder dashboard = new StringBuilder();
        dashboard.append("╔══════════════════════════════════════╗\n");
        dashboard.append("║     🧠 MENTALBOOK USER DASHBOARD     ║\n");
        dashboard.append("╚══════════════════════════════════════╝\n\n");
        
        dashboard.append(String.format("👤 User: %s\n", profile.name));
        dashboard.append(String.format("📅 Member Since: %s\n", dateFormat.format(profile.createdDate)));
        if (profile.birthday != null) {
            dashboard.append(String.format("🎂 Birthday: %s\n", dateFormat.format(profile.birthday)));
        }
        dashboard.append("\n");
        
        dashboard.append(getConversationSummary());
        dashboard.append("\n");
        
        if (!profile.moodHistory.isEmpty()) {
            dashboard.append(getMoodHistory(7));
            dashboard.append("\n");
        }
        
        if (!profile.importantFacts.isEmpty()) {
            dashboard.append(getAllFacts());
            dashboard.append("\n");
        }
        
        dashboard.append("═══════════════════════════════════════\n");
        dashboard.append("💡 Tip: Say 'remember that [fact]' to save something important!\n");
        
        return dashboard.toString();
    }
    
    // ==================== PERSISTENCE ====================
    
    private void saveProfiles() {
        // In a real implementation, this would save to a file or database
        // For now, we'll keep it in memory
    }
    
    private void loadProfiles() {
        // In a real implementation, this would load from a file or database
    }
    
    public void exportProfileData(String filename) {
        UserProfile profile = getCurrentProfile();
        if (profile == null) return;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("MentalBook User Profile Export");
            writer.println("==============================");
            writer.println();
            writer.println("User: " + profile.name);
            writer.println("Export Date: " + dateFormat.format(new Date()));
            writer.println();
            writer.println(getFullDashboard());
        } catch (IOException e) {
            System.out.println("Error exporting profile: " + e.getMessage());
        }
    }
}
