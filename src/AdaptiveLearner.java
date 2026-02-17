import java.util.*;
import java.time.LocalDateTime;
import java.time.Duration;

/**
 * AdaptiveLearner - Learns from user interactions to personalize the experience
 * 
 * Key capabilities:
 * - Tracks user interaction patterns and preferences
 * - Adapts response style based on user feedback
 * - Learns emotional patterns and triggers
 * - Improves topic engagement over time
 * - Personalizes pacing and question frequency
 * - Builds user profile through observation
 */
public class AdaptiveLearner {

    // ==================== INNER CLASSES ====================

    /**
     * Learning pattern for a specific interaction type
     */
    public static class LearningPattern {
        private String patternId;
        private String interactionType;
        private Map<String, Integer> responseCounts;
        private Map<String, Double> effectivenessScores;
        private LocalDateTime firstObserved;
        private LocalDateTime lastObserved;
        private int totalInteractions;
        private String preferredResponse;

        public LearningPattern(String interactionType) {
            this.patternId = UUID.randomUUID().toString();
            this.interactionType = interactionType;
            this.responseCounts = new HashMap<>();
            this.effectivenessScores = new HashMap<>();
            this.firstObserved = LocalDateTime.now();
            this.lastObserved = LocalDateTime.now();
            this.totalInteractions = 0;
        }

        public String getPatternId() { return patternId; }
        public String getInteractionType() { return interactionType; }
        public LocalDateTime getFirstObserved() { return firstObserved; }
        public LocalDateTime getLastObserved() { return lastObserved; }
        public int getTotalInteractions() { return totalInteractions; }
        public String getPreferredResponse() { return preferredResponse; }
        public Map<String, Integer> getResponseCounts() { return new HashMap<>(responseCounts); }
        public Map<String, Double> getEffectivenessScores() { return new HashMap<>(effectivenessScores); }

        public void recordInteraction(String response, boolean wasEffective) {
            responseCounts.merge(response, 1, Integer::sum);
            
            if (wasEffective) {
                effectivenessScores.merge(response, 1.0, Double::sum);
            } else {
                effectivenessScores.merge(response, -0.5, Double::sum);
            }
            
            totalInteractions++;
            lastObserved = LocalDateTime.now();
            
            // Update preferred response
            updatePreferredResponse();
        }

        private void updatePreferredResponse() {
            String best = null;
            double bestScore = Double.MIN_VALUE;
            
            for (Map.Entry<String, Double> entry : effectivenessScores.entrySet()) {
                if (entry.getValue() > bestScore) {
                    bestScore = entry.getValue();
                    best = entry.getKey();
                }
            }
            
            this.preferredResponse = best;
        }

        public double getPatternStrength() {
            long daysSinceFirst = Duration.between(firstObserved, LocalDateTime.now()).toDays();
            double recency = 1.0 / (1.0 + daysSinceFirst / 7.0);
            return Math.min(1.0, (totalInteractions * 0.2) + (recency * 0.3));
        }
    }

    /**
     * User interaction profile
     */
    public static class InteractionProfile {
        private String userId;
        private LocalDateTime firstInteraction;
        private LocalDateTime lastInteraction;
        private int totalInteractions;
        private Map<String, Integer> topicEngagement;
        private Map<String, Integer> emotionalTriggers;
        private List<String> preferredTopics;
        private List<String> avoidedTopics;
        private double engagementLevel;
        private double responseLengthPreference; // 0 = short, 1 = long
        private double questionFrequencyPreference; // 0 = few, 1 = many
        private int streakDays;
        
        // What works best - tracks effective response types
        private Map<String, Integer> responseTypeEffectiveness;
        private Map<String, Integer> toneEffectiveness;
        private Map<String, Integer> approachEffectiveness;
        
        // Timing preferences
        private Map<Integer, Integer> hourActivityDistribution; // Hour of day -> interaction count
        private Map<Integer, Integer> dayOfWeekActivity; // Day of week -> interaction count
        private double averageResponseTime; // Average time between user messages
        private List<Long> responseTimeHistory;
        private int preferredSessionLength; // Preferred conversation length in minutes
        
        // Engagement depth tracking
        private int deepEngagementCount; // Long, meaningful conversations
        private int quickInteractionCount; // Brief exchanges
        private double engagementDepthRatio; // deep / total
        
        // Response pattern success
        private Map<String, Integer> successfulResponsePatterns;
        
        // User habits tracking
        private Map<String, Integer> dailyHabitPatterns; // Habit -> count
        private List<String> recentHabits; // Recent habit observations
        private Map<String, LocalDateTime> lastHabitOccurrence;
        private int consistencyStreak; // Days with consistent interaction
        
        // Common topics
        private Map<String, Integer> topicFrequency; // Topic -> times mentioned
        private List<String> conversationStarters; // Topics that start conversations
        private Map<String, Integer> topicRecency; // Topic -> minutes since last discussed
        private String mostCommonTopic;
        
        // Typical moods
        private Map<String, Integer> moodFrequency; // Mood -> count
        private List<String> recentMoods; // Recent mood observations (last 10)
        private String dominantMood;
        private Map<String, List<String>> moodToTopics; // Mood -> associated topics
        private Map<String, List<String>> moodToResponses; // Mood -> effective responses
        
        // Tailored responses
        private Map<String, String> responseTemplates; // Key -> customized response template
        private Map<String, List<String>> preferredPhrases; // User's preferred words/expressions
        private Map<String, Integer> phraseFrequency; // How often user uses certain phrases
        private List<String> personalizedGreetings; // User's preferred greetings
        private String preferredGreeting; // Most used greeting
        
        // Individual quirks
        private Map<String, Integer> quirkyBehaviors; // User's unique quirks
        private Map<String, LocalDateTime> quirkLastObserved;
        private List<String> petPeeves; // Things user dislikes
        private List<String> insideJokes; // Shared jokes or references
        private Map<String, String> personalMeaning; // Words with special meaning to user
        
        // Custom interactions
        private Map<String, String> customResponses; // Custom responses for specific triggers
        private Map<String, Integer> interactionTriggers; // Trigger -> use count
        private Map<String, Long> lastTriggerTime; // Trigger -> last used timestamp
        private List<String> specialRituals; // User-specific interaction rituals
        private Map<String, String> nicknameMappings; // Name aliases
        
        public InteractionProfile(String userId) {
            this.userId = userId;
            this.firstInteraction = LocalDateTime.now();
            this.lastInteraction = LocalDateTime.now();
            this.totalInteractions = 0;
            this.topicEngagement = new HashMap<>();
            this.emotionalTriggers = new HashMap<>();
            this.preferredTopics = new ArrayList<>();
            this.avoidedTopics = new ArrayList<>();
            this.engagementLevel = 0.5;
            this.responseLengthPreference = 0.5;
            this.questionFrequencyPreference = 0.5;
            this.streakDays = 0;
            
            // Initialize new tracking fields
            this.responseTypeEffectiveness = new HashMap<>();
            this.toneEffectiveness = new HashMap<>();
            this.approachEffectiveness = new HashMap<>();
            this.hourActivityDistribution = new HashMap<>();
            this.dayOfWeekActivity = new HashMap<>();
            this.averageResponseTime = 0;
            this.responseTimeHistory = new ArrayList<>();
            this.preferredSessionLength = 10;
            this.deepEngagementCount = 0;
            this.quickInteractionCount = 0;
            this.engagementDepthRatio = 0.5;
            this.successfulResponsePatterns = new HashMap<>();
            
            // Initialize user habits, common topics, and typical moods
            this.dailyHabitPatterns = new HashMap<>();
            this.recentHabits = new ArrayList<>();
            this.lastHabitOccurrence = new HashMap<>();
            this.consistencyStreak = 0;
            
            this.topicFrequency = new HashMap<>();
            this.conversationStarters = new ArrayList<>();
            this.topicRecency = new HashMap<>();
            this.mostCommonTopic = null;
            
            this.moodFrequency = new HashMap<>();
            this.recentMoods = new ArrayList<>();
            this.dominantMood = null;
            this.moodToTopics = new HashMap<>();
            this.moodToResponses = new HashMap<>();
            
            // Initialize tailored responses
            this.responseTemplates = new HashMap<>();
            this.preferredPhrases = new HashMap<>();
            this.phraseFrequency = new HashMap<>();
            this.personalizedGreetings = new ArrayList<>();
            this.preferredGreeting = null;
            
            // Initialize individual quirks
            this.quirkyBehaviors = new HashMap<>();
            this.quirkLastObserved = new HashMap<>();
            this.petPeeves = new ArrayList<>();
            this.insideJokes = new ArrayList<>();
            this.personalMeaning = new HashMap<>();
            
            // Initialize custom interactions
            this.customResponses = new HashMap<>();
            this.interactionTriggers = new HashMap<>();
            this.lastTriggerTime = new HashMap<>();
            this.specialRituals = new ArrayList<>();
            this.nicknameMappings = new HashMap<>();
        }

        public String getUserId() { return userId; }
        public LocalDateTime getFirstInteraction() { return firstInteraction; }
        public LocalDateTime getLastInteraction() { return lastInteraction; }
        public int getTotalInteractions() { return totalInteractions; }
        public Map<String, Integer> getTopicEngagement() { return new HashMap<>(topicEngagement); }
        public Map<String, Integer> getEmotionalTriggers() { return new HashMap<>(emotionalTriggers); }
        public List<String> getPreferredTopics() { return new ArrayList<>(preferredTopics); }
        public List<String> getAvoidedTopics() { return new ArrayList<>(avoidedTopics); }
        public double getEngagementLevel() { return engagementLevel; }
        public double getResponseLengthPreference() { return responseLengthPreference; }
        public double getQuestionFrequencyPreference() { return questionFrequencyPreference; }
        public int getStreakDays() { return streakDays; }
        
        // New getters
        public Map<String, Integer> getResponseTypeEffectiveness() { return new HashMap<>(responseTypeEffectiveness); }
        public Map<String, Integer> getToneEffectiveness() { return new HashMap<>(toneEffectiveness); }
        public Map<String, Integer> getApproachEffectiveness() { return new HashMap<>(approachEffectiveness); }
        public Map<Integer, Integer> getHourActivityDistribution() { return new HashMap<>(hourActivityDistribution); }
        public Map<Integer, Integer> getDayOfWeekActivity() { return new HashMap<>(dayOfWeekActivity); }
        public double getAverageResponseTime() { return averageResponseTime; }
        public int getPreferredSessionLength() { return preferredSessionLength; }
        public double getEngagementDepthRatio() { return engagementDepthRatio; }
        public Map<String, Integer> getSuccessfulResponsePatterns() { return new HashMap<>(successfulResponsePatterns); }
        
        // New getters for habits, topics, moods
        public Map<String, Integer> getDailyHabitPatterns() { return new HashMap<>(dailyHabitPatterns); }
        public List<String> getRecentHabits() { return new ArrayList<>(recentHabits); }
        public int getConsistencyStreak() { return consistencyStreak; }
        
        public Map<String, Integer> getTopicFrequency() { return new HashMap<>(topicFrequency); }
        public List<String> getConversationStarters() { return new ArrayList<>(conversationStarters); }
        public String getMostCommonTopic() { return mostCommonTopic; }
        
        public Map<String, Integer> getMoodFrequency() { return new HashMap<>(moodFrequency); }
        public List<String> getRecentMoods() { return new ArrayList<>(recentMoods); }
        public String getDominantMood() { return dominantMood; }
        public Map<String, List<String>> getMoodToTopics() { 
            Map<String, List<String>> result = new HashMap<>();
            for (var entry : moodToTopics.entrySet()) {
                result.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            return result;
        }
        public Map<String, List<String>> getMoodToResponses() {
            Map<String, List<String>> result = new HashMap<>();
            for (var entry : moodToResponses.entrySet()) {
                result.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            return result;
        }

        public void updateLastInteraction() {
            long hoursSinceLast = Duration.between(lastInteraction, LocalDateTime.now()).toHours();
            
            if (hoursSinceLast < 24) {
                streakDays++;
            } else if (hoursSinceLast > 48) {
                streakDays = 0;
            }
            
            this.lastInteraction = LocalDateTime.now();
            this.totalInteractions++;
        }

        public void recordTopicEngagement(String topic, int engagementScore) {
            topicEngagement.merge(topic.toLowerCase(), engagementScore, Integer::sum);
            
            if (engagementScore > 5) {
                if (!preferredTopics.contains(topic.toLowerCase())) {
                    preferredTopics.add(topic.toLowerCase());
                }
                avoidedTopics.remove(topic.toLowerCase());
            } else if (engagementScore < 2) {
                if (!avoidedTopics.contains(topic.toLowerCase())) {
                    avoidedTopics.add(topic.toLowerCase());
                }
                preferredTopics.remove(topic.toLowerCase());
            }
        }

        public void recordEmotionalTrigger(String emotion, int intensity) {
            emotionalTriggers.merge(emotion.toLowerCase(), intensity, Integer::sum);
        }

        public void updatePreferences(double responseLength, double questionFrequency) {
            // Blend new preferences with existing ones
            this.responseLengthPreference = (this.responseLengthPreference * 0.7) + (responseLength * 0.3);
            this.questionFrequencyPreference = (this.questionFrequencyPreference * 0.7) + (questionFrequency * 0.3);
        }

        public void updateEngagementLevel(double change) {
            this.engagementLevel = Math.max(0.0, Math.min(1.0, this.engagementLevel + change));
        }
        
        // ==================== WHAT WORKS BEST TRACKING ====================
        
        /**
         * Record that a response type was effective
         * @param responseType The type of response (e.g., "empathetic", "factual", "humorous")
         * @param wasEffective Whether the response was effective
         */
        public void recordResponseTypeEffectiveness(String responseType, boolean wasEffective) {
            if (wasEffective) {
                responseTypeEffectiveness.merge(responseType.toLowerCase(), 1, Integer::sum);
            } else {
                responseTypeEffectiveness.merge(responseType.toLowerCase(), -1, Integer::sum);
            }
        }
        
        /**
         * Record that a tone was effective
         * @param tone The tone used (e.g., "supportive", "direct", "playful")
         * @param wasEffective Whether the tone was effective
         */
        public void recordToneEffectiveness(String tone, boolean wasEffective) {
            if (wasEffective) {
                toneEffectiveness.merge(tone.toLowerCase(), 1, Integer::sum);
            } else {
                toneEffectiveness.merge(tone.toLowerCase(), -1, Integer::sum);
            }
        }
        
        /**
         * Record that an approach was effective
         * @param approach The approach used (e.g., "questioning", "listening", "advice")
         * @param wasEffective Whether the approach was effective
         */
        public void recordApproachEffectiveness(String approach, boolean wasEffective) {
            if (wasEffective) {
                approachEffectiveness.merge(approach.toLowerCase(), 1, Integer::sum);
            } else {
                approachEffectiveness.merge(approach.toLowerCase(), -1, Integer::sum);
            }
        }
        
        /**
         * Record a successful response pattern
         * @param pattern The response pattern (e.g., "short_empathetic", "long_detailed")
         * @param successScore Score from 1-10
         */
        public void recordResponsePattern(String pattern, int successScore) {
            successfulResponsePatterns.merge(pattern.toLowerCase(), successScore, Integer::sum);
        }
        
        /**
         * Get the most effective response type
         */
        public String getMostEffectiveResponseType() {
            return responseTypeEffectiveness.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        }
        
        /**
         * Get the most effective tone
         */
        public String getMostEffectiveTone() {
            return toneEffectiveness.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        }
        
        /**
         * Get the most effective approach
         */
        public String getMostEffectiveApproach() {
            return approachEffectiveness.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        }
        
        /**
         * Get top performing response patterns
         */
        public List<String> getTopResponsePatterns(int limit) {
            return successfulResponsePatterns.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
        }
        
        // ==================== TIMING PREFERENCES ====================
        
        /**
         * Record activity at current time
         */
        public void recordActivityTiming() {
            LocalDateTime now = LocalDateTime.now();
            int hour = now.getHour();
            int dayOfWeek = now.getDayOfWeek().getValue();
            
            hourActivityDistribution.merge(hour, 1, Integer::sum);
            dayOfWeekActivity.merge(dayOfWeek, 1, Integer::sum);
        }
        
        /**
         * Record time between user messages (in seconds)
         */
        public void recordResponseTime(long responseTimeSeconds) {
            responseTimeHistory.add(responseTimeSeconds);
            
            // Keep only last 100 response times
            while (responseTimeHistory.size() > 100) {
                responseTimeHistory.remove(0);
            }
            
            // Calculate average
            averageResponseTime = responseTimeHistory.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
        }
        
        /**
         * Get user's most active hour
         */
        public Integer getMostActiveHour() {
            return hourActivityDistribution.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(12); // Default to noon
        }
        
        /**
         * Get user's most active day
         */
        public Integer getMostActiveDay() {
            return dayOfWeekActivity.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(1); // Default to Monday
        }
        
        /**
         * Check if user is likely active now
         */
        public boolean isLikelyActiveNow() {
            int currentHour = LocalDateTime.now().getHour();
            Integer activityCount = hourActivityDistribution.get(currentHour);
            
            if (activityCount == null || activityCount < 2) return false;
            
            // Check if this hour is above average
            double avgActivity = hourActivityDistribution.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(1.0);
            
            return activityCount > avgActivity * 0.5;
        }
        
        /**
         * Update preferred session length
         */
        public void updatePreferredSessionLength(int sessionLengthMinutes) {
            // Blend with existing preference
            this.preferredSessionLength = (int) ((this.preferredSessionLength * 0.7) + (sessionLengthMinutes * 0.3));
        }
        
        // ==================== ENGAGEMENT DEPTH ====================
        
        /**
         * Record a deep engagement (long, meaningful conversation)
         */
        public void recordDeepEngagement() {
            deepEngagementCount++;
            updateEngagementDepthRatio();
        }
        
        /**
         * Record a quick interaction (brief exchange)
         */
        public void recordQuickInteraction() {
            quickInteractionCount++;
            updateEngagementDepthRatio();
        }
        
        private void updateEngagementDepthRatio() {
            int total = deepEngagementCount + quickInteractionCount;
            if (total > 0) {
                this.engagementDepthRatio = (double) deepEngagementCount / total;
            }
        }
        
        /**
         * Check if user prefers deep engagement
         */
        public boolean prefersDeepEngagement() {
            return engagementDepthRatio > 0.5;
        }
        
        // ==================== USER HABITS ====================
        
        /**
         * Record a user habit
         * @param habit The habit to record (e.g., "morning", "evening", "late_night", "weekend")
         */
        public void recordHabit(String habit) {
            String lowerHabit = habit.toLowerCase();
            dailyHabitPatterns.merge(lowerHabit, 1, Integer::sum);
            
            recentHabits.add(lowerHabit);
            while (recentHabits.size() > 20) {
                recentHabits.remove(0);
            }
            
            lastHabitOccurrence.put(lowerHabit, LocalDateTime.now());
            updateConsistencyStreak();
        }
        
        private void updateConsistencyStreak() {
            long daysSinceFirst = Duration.between(firstInteraction, LocalDateTime.now()).toDays();
            if (daysSinceFirst > 0) {
                consistencyStreak = (int) Math.min(daysSinceFirst, 
                    dailyHabitPatterns.values().stream().mapToInt(Integer::intValue).sum() / 2);
            }
        }
        
        /**
         * Get most common habit
         */
        public String getMostCommonHabit() {
            return dailyHabitPatterns.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        }
        
        /**
         * Get typical time of day for interactions
         */
        public String getTypicalTimeOfDay() {
            if (hourActivityDistribution.isEmpty()) return "unknown";
            
            int peakHour = hourActivityDistribution.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(12);
            
            if (peakHour >= 5 && peakHour < 12) return "morning";
            if (peakHour >= 12 && peakHour < 17) return "afternoon";
            if (peakHour >= 17 && peakHour < 21) return "evening";
            return "night";
        }
        
        // ==================== COMMON TOPICS ====================
        
        /**
         * Record a topic discussion
         * @param topic The topic discussed
         * @param isStarter Whether this topic started the conversation
         */
        public void recordTopic(String topic, boolean isStarter) {
            String lowerTopic = topic.toLowerCase();
            
            topicFrequency.merge(lowerTopic, 1, Integer::sum);
            topicRecency.put(lowerTopic, (int) Duration.between(lastInteraction, LocalDateTime.now()).toMinutes());
            
            if (isStarter) {
                conversationStarters.add(lowerTopic);
                while (conversationStarters.size() > 10) {
                    conversationStarters.remove(0);
                }
            }
            
            // Update most common topic
            updateMostCommonTopic();
        }
        
        private void updateMostCommonTopic() {
            this.mostCommonTopic = topicFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        }
        
        /**
         * Get most discussed topics
         */
        public List<String> getMostCommonTopics(int limit) {
            return topicFrequency.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
        }
        
        /**
         * Get recently discussed topics
         */
        public List<String> getRecentTopics(int limit) {
            return topicRecency.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
        }
        
        /**
         * Get topics that typically start conversations
         */
        public List<String> getFrequentConversationStarters() {
            return conversationStarters.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    java.util.function.Function.identity(), 
                    java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
        }
        
        /**
         * Check if topic was recently discussed
         */
        public boolean wasTopicRecentlyDiscussed(String topic) {
            Integer minutes = topicRecency.get(topic.toLowerCase());
            return minutes != null && minutes < 60; // Within last hour
        }
        
        // ==================== TYPICAL MOODS ====================
        
        /**
         * Record user mood
         * @param mood The mood (e.g., "happy", "sad", "anxious", "calm", "stressed")
         * @param topic Associated topic (can be null)
         * @param effectiveResponse Response that worked well (can be null)
         */
        public void recordMood(String mood, String topic, String effectiveResponse) {
            String lowerMood = mood.toLowerCase();
            
            moodFrequency.merge(lowerMood, 1, Integer::sum);
            
            recentMoods.add(lowerMood);
            while (recentMoods.size() > 10) {
                recentMoods.remove(0);
            }
            
            // Update dominant mood
            updateDominantMood();
            
            // Associate mood with topic
            if (topic != null && !topic.isEmpty()) {
                moodToTopics.computeIfAbsent(lowerMood, k -> new ArrayList<>()).add(topic.toLowerCase());
            }
            
            // Associate mood with effective response
            if (effectiveResponse != null && !effectiveResponse.isEmpty()) {
                moodToResponses.computeIfAbsent(lowerMood, k -> new ArrayList<>()).add(effectiveResponse);
            }
        }
        
        private void updateDominantMood() {
            this.dominantMood = moodFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        }
        
        /**
         * Get recent mood trend (last few moods)
         */
        public String getMoodTrend() {
            if (recentMoods.isEmpty()) return "unknown";
            
            long positiveCount = recentMoods.stream()
                .filter(m -> m.equals("happy") || m.equals("calm") || m.equals("content") || m.equals("excited"))
                .count();
            
            double positiveRatio = (double) positiveCount / recentMoods.size();
            
            if (positiveRatio > 0.6) return "improving";
            if (positiveRatio < 0.4) return "declining";
            return "stable";
        }
        
        /**
         * Get topics associated with a mood
         */
        public List<String> getTopicsForMood(String mood) {
            List<String> topics = moodToTopics.get(mood.toLowerCase());
            return topics != null ? new ArrayList<>(topics) : new ArrayList<>();
        }
        
        /**
         * Get effective responses for a mood
         */
        public List<String> getEffectiveResponsesForMood(String mood) {
            List<String> responses = moodToResponses.get(mood.toLowerCase());
            return responses != null ? new ArrayList<>(responses) : new ArrayList<>();
        }
        
        /**
         * Check if mood has changed significantly
         */
        public boolean hasMoodChanged() {
            if (recentMoods.size() < 3) return false;
            
            String lastMood = recentMoods.get(recentMoods.size() - 1);
            String firstMood = recentMoods.get(0);
            
            return !lastMood.equals(firstMood);
        }
        
        // ==================== TAILORED RESPONSES ====================
        
        /**
         * Record a phrase the user frequently uses
         */
        public void recordUserPhrase(String phrase) {
            String lower = phrase.toLowerCase();
            phraseFrequency.merge(lower, 1, Integer::sum);
            
            // Track in preferred phrases if used frequently
            if (phraseFrequency.get(lower) > 3) {
                preferredPhrases.computeIfAbsent("frequent", k -> new ArrayList<>()).add(lower);
            }
        }
        
        /**
         * Set a customized response template
         */
        public void setResponseTemplate(String key, String template) {
            responseTemplates.put(key, template);
        }
        
        /**
         * Get a customized response template
         */
        public String getResponseTemplate(String key) {
            return responseTemplates.get(key);
        }
        
        /**
         * Record user's preferred greeting
         */
        public void recordGreeting(String greeting) {
            personalizedGreetings.add(greeting);
            while (personalizedGreetings.size() > 20) {
                personalizedGreetings.remove(0);
            }
            
            // Update most used greeting
            preferredGreeting = personalizedGreetings.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    java.util.function.Function.identity(),
                    java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .max((a, b) -> Long.compare(a.getValue(), b.getValue()))
                .map(Map.Entry::getKey)
                .orElse("hello");
        }
        
        /**
         * Get user's preferred greeting
         */
        public String getPreferredGreeting() {
            return preferredGreeting != null ? preferredGreeting : "hello";
        }
        
        /**
         * Get frequently used phrases
         */
        public List<String> getFrequentPhrases(int limit) {
            return phraseFrequency.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
        }
        
        // ==================== INDIVIDUAL QUIRKS ====================
        
        /**
         * Record a quirky behavior
         */
        public void recordQuirk(String quirk) {
            String lower = quirk.toLowerCase();
            quirkyBehaviors.merge(lower, 1, Integer::sum);
            quirkLastObserved.put(lower, LocalDateTime.now());
        }
        
        /**
         * Record a pet peeve
         */
        public void recordPetPeave(String petPeave) {
            if (!petPeeves.contains(petPeave.toLowerCase())) {
                petPeeves.add(petPeave.toLowerCase());
            }
        }
        
        /**
         * Get all pet peeves
         */
        public List<String> getPetPeeves() {
            return new ArrayList<>(petPeeves);
        }
        
        /**
         * Add an inside joke
         */
        public void addInsideJoke(String joke) {
            if (!insideJokes.contains(joke.toLowerCase())) {
                insideJokes.add(joke.toLowerCase());
            }
        }
        
        /**
         * Get inside jokes
         */
        public List<String> getInsideJokes() {
            return new ArrayList<>(insideJokes);
        }
        
        /**
         * Set personal meaning for a word
         */
        public void setPersonalMeaning(String word, String meaning) {
            personalMeaning.put(word.toLowerCase(), meaning);
        }
        
        /**
         * Get personal meaning for a word
         */
        public String getPersonalMeaning(String word) {
            return personalMeaning.get(word.toLowerCase());
        }
        
        /**
         * Get most notable quirks
         */
        public List<String> getNotableQuirks(int limit) {
            return quirkyBehaviors.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
        }
        
        // ==================== CUSTOM INTERACTIONS ====================
        
        /**
         * Set a custom response for a trigger
         */
        public void setCustomResponse(String trigger, String response) {
            customResponses.put(trigger.toLowerCase(), response);
            interactionTriggers.merge(trigger.toLowerCase(), 0, Integer::sum);
        }
        
        /**
         * Get custom response for a trigger
         */
        public String getCustomResponse(String trigger) {
            String response = customResponses.get(trigger.toLowerCase());
            if (response != null) {
                // Update usage count
                interactionTriggers.merge(trigger.toLowerCase(), 1, Integer::sum);
                lastTriggerTime.put(trigger.toLowerCase(), System.currentTimeMillis());
            }
            return response;
        }
        
        /**
         * Check if custom response exists for trigger
         */
        public boolean hasCustomResponse(String trigger) {
            return customResponses.containsKey(trigger.toLowerCase());
        }
        
        /**
         * Get most used triggers
         */
        public List<String> getMostUsedTriggers(int limit) {
            return interactionTriggers.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
        }
        
        /**
         * Record a special ritual
         */
        public void addSpecialRitual(String ritual) {
            if (!specialRituals.contains(ritual.toLowerCase())) {
                specialRituals.add(ritual.toLowerCase());
            }
        }
        
        /**
         * Get special rituals
         */
        public List<String> getSpecialRituals() {
            return new ArrayList<>(specialRituals);
        }
        
        /**
         * Set a nickname mapping
         */
        public void setNicknameMapping(String alias, String actualName) {
            nicknameMappings.put(alias.toLowerCase(), actualName);
        }
        
        /**
         * Get actual name from alias
         */
        public String getActualName(String alias) {
            return nicknameMappings.get(alias.toLowerCase());
        }
        
        /**
         * Get all nickname mappings
         */
        public Map<String, String> getNicknameMappings() {
            return new HashMap<>(nicknameMappings);
        }
    }

    /**
     * Feedback record for learning
     */
    public static class FeedbackRecord {
        private String feedbackId;
        private String interactionId;
        private String response;
        private boolean wasHelpful;
        private boolean wasAppropriate;
        private String userComment;
        private LocalDateTime timestamp;
        private Map<String, Object> context;

        public FeedbackRecord(String interactionId, String response) {
            this.feedbackId = UUID.randomUUID().toString();
            this.interactionId = interactionId;
            this.response = response;
            this.wasHelpful = true; // Default assumption
            this.wasAppropriate = true;
            this.timestamp = LocalDateTime.now();
            this.context = new HashMap<>();
        }

        public String getFeedbackId() { return feedbackId; }
        public String getInteractionId() { return interactionId; }
        public String getResponse() { return response; }
        public boolean wasHelpful() { return wasHelpful; }
        public boolean wasAppropriate() { return wasAppropriate; }
        public String getUserComment() { return userComment; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Map<String, Object> getContext() { return new HashMap<>(context); }

        public void setHelpful(boolean helpful) { this.wasHelpful = helpful; }
        public void setAppropriate(boolean appropriate) { this.wasAppropriate = appropriate; }
        public void setUserComment(String comment) { this.userComment = comment; }
        public void setContext(String key, Object value) { this.context.put(key, value); }
    }

    /**
     * Adaptation recommendation
     */
    public static class AdaptationRecommendation {
        private String recommendationId;
        private String adaptationType;
        private String description;
        private double confidence;
        private LocalDateTime generatedAt;
        private Map<String, Object> parameters;

        public AdaptationRecommendation(String adaptationType, String description, double confidence) {
            this.recommendationId = UUID.randomUUID().toString();
            this.adaptationType = adaptationType;
            this.description = description;
            this.confidence = confidence;
            this.generatedAt = LocalDateTime.now();
            this.parameters = new HashMap<>();
        }

        public String getRecommendationId() { return recommendationId; }
        public String getAdaptationType() { return adaptationType; }
        public String getDescription() { return description; }
        public double getConfidence() { return confidence; }
        public LocalDateTime getGeneratedAt() { return generatedAt; }
        public Map<String, Object> getParameters() { return new HashMap<>(parameters); }

        public void setParameter(String key, Object value) {
            parameters.put(key, value);
        }
    }

    // ==================== LEARNING CATEGORIES ====================

    public static final String CATEGORY_RESPONSE_STYLE = "response_style";
    public static final String CATEGORY_TOPIC_PREFERENCE = "topic_preference";
    public static final String CATEGORY_EMOTIONAL_PATTERN = "emotional_pattern";
    public static final String CATEGORY_ENGAGEMENT = "engagement";
    public static final String CATEGORY_PACING = "pacing";
    public static final String CATEGORY_SUPPORT_STYLE = "support_style";

    // ==================== MAIN ADAPTIVE LEARNER ====================

    private String currentUserId;
    private Map<String, InteractionProfile> userProfiles;
    private Map<String, Map<String, LearningPattern>> learningPatterns;
    private List<FeedbackRecord> recentFeedback;
    private Map<String, List<AdaptationRecommendation>> recommendations;
    
    // Learning configuration
    private int maxPatternsPerUser;
    private int maxFeedbackHistory;
    private double learningRate;
    private double adaptationThreshold;
    private int cooldownPeriod; // Hours between major adaptations

    // Learning statistics
    private int totalLearningEvents;
    private Map<String, Integer> categoryLearningCounts;

    public AdaptiveLearner() {
        this.currentUserId = null;
        this.userProfiles = new HashMap<>();
        this.learningPatterns = new HashMap<>();
        this.recentFeedback = new ArrayList<>();
        this.recommendations = new HashMap<>();
        
        this.maxPatternsPerUser = 50;
        this.maxFeedbackHistory = 100;
        this.learningRate = 0.1;
        this.adaptationThreshold = 0.6;
        this.cooldownPeriod = 2;
        
        this.totalLearningEvents = 0;
        this.categoryLearningCounts = new HashMap<>();
    }

    // ==================== USER MANAGEMENT ====================

    /**
     * Set the current user for learning
     */
    public void setCurrentUser(String userId) {
        this.currentUserId = userId;
        userProfiles.computeIfAbsent(userId, k -> new InteractionProfile(k));
        learningPatterns.computeIfAbsent(userId, k -> new HashMap<>());
        recommendations.computeIfAbsent(userId, k -> new ArrayList<>());
    }

    /**
     * Get current user ID
     */
    public String getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Get interaction profile for current user
     */
    public InteractionProfile getInteractionProfile() {
        return getInteractionProfile(currentUserId);
    }

    /**
     * Get interaction profile for specific user
     */
    public InteractionProfile getInteractionProfile(String userId) {
        return userProfiles.get(userId);
    }

    // ==================== LEARNING METHODS ====================

    /**
     * Record a user interaction for learning
     */
    public void recordInteraction(String topic, String emotion, int engagementScore) {
        if (currentUserId == null) return;
        
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.updateLastInteraction();
            profile.recordTopicEngagement(topic, engagementScore);
            profile.recordEmotionalTrigger(emotion, engagementScore);
            
            // Update engagement level based on interaction
            profile.updateEngagementLevel(0.02 * engagementScore);
        }
        
        totalLearningEvents++;
    }

    /**
     * Record user feedback on a response
     */
    public FeedbackRecord recordFeedback(String interactionId, String response, boolean wasHelpful) {
        return recordFeedback(currentUserId, interactionId, response, wasHelpful);
    }

    /**
     * Record feedback for specific user
     */
    public FeedbackRecord recordFeedback(String userId, String interactionId, String response, boolean wasHelpful) {
        FeedbackRecord feedback = new FeedbackRecord(interactionId, response);
        feedback.setHelpful(wasHelpful);
        
        recentFeedback.add(feedback);
        
        // Keep feedback history limited
        while (recentFeedback.size() > maxFeedbackHistory) {
            recentFeedback.remove(0);
        }
        
        // Learn from feedback
        learnFromFeedback(userId, feedback);
        
        return feedback;
    }

    /**
     * Learn from feedback to improve patterns
     */
    private void learnFromFeedback(String userId, FeedbackRecord feedback) {
        if (userId == null) return;
        
        Map<String, LearningPattern> patterns = learningPatterns.get(userId);
        if (patterns == null) return;
        
        // Find or create relevant pattern
        String patternKey = "feedback_" + (feedback.wasHelpful() ? "positive" : "negative");
        LearningPattern pattern = patterns.get(patternKey);
        
        if (pattern == null) {
            pattern = new LearningPattern(patternKey);
            patterns.put(patternKey, pattern);
        }
        
        pattern.recordInteraction(feedback.getResponse(), feedback.wasHelpful());
        
        // Update category counts
        categoryLearningCounts.merge(patternKey, 1, Integer::sum);
        
        // Update user profile engagement
        InteractionProfile profile = userProfiles.get(userId);
        if (profile != null) {
            double change = feedback.wasHelpful() ? 0.01 : -0.01;
            profile.updateEngagementLevel(change);
        }
    }

    /**
     * Record user preference directly
     */
    public void recordPreference(String preferenceType, Object value) {
        if (currentUserId == null) return;
        
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile == null) return;
        
        if (preferenceType.equals("responseLength")) {
            double lengthValue = value instanceof Number ? ((Number) value).doubleValue() : 0.5;
            profile.updatePreferences(lengthValue, profile.getQuestionFrequencyPreference());
        } else if (preferenceType.equals("questionFrequency")) {
            double freqValue = value instanceof Number ? ((Number) value).doubleValue() : 0.5;
            profile.updatePreferences(profile.getResponseLengthPreference(), freqValue);
        }
    }

    /**
     * Record topic preference
     */
    public void recordTopicPreference(String topic, boolean isPreferred) {
        if (currentUserId == null) return;
        
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile == null) return;
        
        if (isPreferred) {
            if (!profile.getPreferredTopics().contains(topic.toLowerCase())) {
                profile.getPreferredTopics().add(topic.toLowerCase());
            }
        } else {
            if (!profile.getAvoidedTopics().contains(topic.toLowerCase())) {
                profile.getAvoidedTopics().add(topic.toLowerCase());
            }
        }
    }

    // ==================== WHAT WORKS BEST ====================

    /**
     * Record that a response type was effective
     * @param responseType The type of response (e.g., "empathetic", "factual", "humorous", "supportive")
     * @param wasEffective Whether the response was effective
     */
    public void recordResponseTypeEffectiveness(String responseType, boolean wasEffective) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordResponseTypeEffectiveness(responseType, wasEffective);
        }
    }

    /**
     * Record that a tone was effective
     * @param tone The tone used (e.g., "supportive", "direct", "playful", "serious")
     * @param wasEffective Whether the tone was effective
     */
    public void recordToneEffectiveness(String tone, boolean wasEffective) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordToneEffectiveness(tone, wasEffective);
        }
    }

    /**
     * Record that an approach was effective
     * @param approach The approach used (e.g., "questioning", "listening", "advice", "validation")
     * @param wasEffective Whether the approach was effective
     */
    public void recordApproachEffectiveness(String approach, boolean wasEffective) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordApproachEffectiveness(approach, wasEffective);
        }
    }

    /**
     * Record a successful response pattern
     * @param pattern The response pattern (e.g., "short_empathetic", "long_detailed", "questioning_supportive")
     * @param successScore Score from 1-10
     */
    public void recordResponsePattern(String pattern, int successScore) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordResponsePattern(pattern, successScore);
        }
    }

    /**
     * Get the most effective response type for current user
     */
    public String getMostEffectiveResponseType() {
        if (currentUserId == null) return null;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getMostEffectiveResponseType() : null;
    }

    /**
     * Get the most effective tone for current user
     */
    public String getMostEffectiveTone() {
        if (currentUserId == null) return null;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getMostEffectiveTone() : null;
    }

    /**
     * Get the most effective approach for current user
     */
    public String getMostEffectiveApproach() {
        if (currentUserId == null) return null;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getMostEffectiveApproach() : null;
    }

    /**
     * Get top performing response patterns
     */
    public List<String> getTopResponsePatterns(int limit) {
        if (currentUserId == null) return new ArrayList<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getTopResponsePatterns(limit) : new ArrayList<>();
    }

    // ==================== TIMING PREFERENCES ====================

    /**
     * Record activity timing for the current user
     */
    public void recordActivityTiming() {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordActivityTiming();
        }
    }

    /**
     * Record time between user messages
     * @param responseTimeSeconds Time in seconds between user messages
     */
    public void recordResponseTime(long responseTimeSeconds) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordResponseTime(responseTimeSeconds);
        }
    }

    /**
     * Get user's most active hour (0-23)
     */
    public int getMostActiveHour() {
        if (currentUserId == null) return 12;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getMostActiveHour() : 12;
    }

    /**
     * Get user's most active day of week (1=Monday, 7=Sunday)
     */
    public int getMostActiveDay() {
        if (currentUserId == null) return 1;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getMostActiveDay() : 1;
    }

    /**
     * Check if user is likely active now based on their typical patterns
     */
    public boolean isUserLikelyActiveNow() {
        if (currentUserId == null) return false;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null && profile.isLikelyActiveNow();
    }

    /**
     * Get user's preferred session length in minutes
     */
    public int getPreferredSessionLength() {
        if (currentUserId == null) return 10;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getPreferredSessionLength() : 10;
    }

    /**
     * Update preferred session length
     * @param sessionLengthMinutes The actual session length in minutes
     */
    public void updatePreferredSessionLength(int sessionLengthMinutes) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.updatePreferredSessionLength(sessionLengthMinutes);
        }
    }

    /**
     * Get average response time in seconds
     */
    public double getAverageResponseTime() {
        if (currentUserId == null) return 0;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getAverageResponseTime() : 0;
    }

    // ==================== ENGAGEMENT DEPTH ====================

    /**
     * Record a deep engagement (long, meaningful conversation)
     */
    public void recordDeepEngagement() {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordDeepEngagement();
        }
    }

    /**
     * Record a quick interaction (brief exchange)
     */
    public void recordQuickInteraction() {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordQuickInteraction();
        }
    }

    /**
     * Check if user prefers deep engagement
     */
    public boolean prefersDeepEngagement() {
        if (currentUserId == null) return false;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null && profile.prefersDeepEngagement();
    }

    /**
     * Get engagement depth ratio (0 = quick only, 1 = deep only)
     */
    public double getEngagementDepthRatio() {
        if (currentUserId == null) return 0.5;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getEngagementDepthRatio() : 0.5;
    }

    // ==================== USER HABITS ====================

    /**
     * Record a user habit
     * @param habit The habit to record (e.g., "morning", "evening", "late_night", "weekend")
     */
    public void recordHabit(String habit) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordHabit(habit);
        }
    }

    /**
     * Get most common habit for current user
     */
    public String getMostCommonHabit() {
        if (currentUserId == null) return null;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getMostCommonHabit() : null;
    }

    /**
     * Get typical time of day for interactions (morning/afternoon/evening/night)
     */
    public String getTypicalTimeOfDay() {
        if (currentUserId == null) return "unknown";
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getTypicalTimeOfDay() : "unknown";
    }

    /**
     * Get user's consistency streak
     */
    public int getConsistencyStreak() {
        if (currentUserId == null) return 0;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getConsistencyStreak() : 0;
    }

    /**
     * Get daily habit patterns
     */
    public Map<String, Integer> getDailyHabitPatterns() {
        if (currentUserId == null) return new HashMap<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getDailyHabitPatterns() : new HashMap<>();
    }

    // ==================== COMMON TOPICS ====================

    /**
     * Record a topic discussion
     * @param topic The topic discussed
     * @param isStarter Whether this topic started the conversation
     */
    public void recordTopic(String topic, boolean isStarter) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordTopic(topic, isStarter);
        }
    }

    /**
     * Get most common topics
     * @param limit Maximum number of topics to return
     */
    public List<String> getMostCommonTopics(int limit) {
        if (currentUserId == null) return new ArrayList<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getMostCommonTopics(limit) : new ArrayList<>();
    }

    /**
     * Get most common topic
     */
    public String getMostCommonTopic() {
        if (currentUserId == null) return null;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getMostCommonTopic() : null;
    }

    /**
     * Get recently discussed topics
     */
    public List<String> getRecentTopics(int limit) {
        if (currentUserId == null) return new ArrayList<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getRecentTopics(limit) : new ArrayList<>();
    }

    /**
     * Get topics that typically start conversations
     */
    public List<String> getFrequentConversationStarters() {
        if (currentUserId == null) return new ArrayList<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getFrequentConversationStarters() : new ArrayList<>();
    }

    /**
     * Check if topic was recently discussed
     */
    public boolean wasTopicRecentlyDiscussed(String topic) {
        if (currentUserId == null) return false;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null && profile.wasTopicRecentlyDiscussed(topic);
    }

    /**
     * Get topic frequency map
     */
    public Map<String, Integer> getTopicFrequency() {
        if (currentUserId == null) return new HashMap<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getTopicFrequency() : new HashMap<>();
    }

    // ==================== TYPICAL MOODS ====================

    /**
     * Record user mood
     * @param mood The mood (e.g., "happy", "sad", "anxious", "calm", "stressed")
     * @param topic Associated topic (can be null)
     * @param effectiveResponse Response that worked well (can be null)
     */
    public void recordMood(String mood, String topic, String effectiveResponse) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordMood(mood, topic, effectiveResponse);
        }
    }

    /**
     * Get dominant mood (most frequent)
     */
    public String getDominantMood() {
        if (currentUserId == null) return null;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getDominantMood() : null;
    }

    /**
     * Get mood frequency map
     */
    public Map<String, Integer> getMoodFrequency() {
        if (currentUserId == null) return new HashMap<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getMoodFrequency() : new HashMap<>();
    }

    /**
     * Get recent mood trend (improving/declining/stable)
     */
    public String getMoodTrend() {
        if (currentUserId == null) return "unknown";
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getMoodTrend() : "unknown";
    }

    /**
     * Get recent moods
     */
    public List<String> getRecentMoods() {
        if (currentUserId == null) return new ArrayList<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getRecentMoods() : new ArrayList<>();
    }

    /**
     * Check if mood has changed significantly
     */
    public boolean hasMoodChanged() {
        if (currentUserId == null) return false;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null && profile.hasMoodChanged();
    }

    /**
     * Get topics associated with a mood
     */
    public List<String> getTopicsForMood(String mood) {
        if (currentUserId == null) return new ArrayList<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getTopicsForMood(mood) : new ArrayList<>();
    }

    /**
     * Get effective responses for a mood
     */
    public List<String> getEffectiveResponsesForMood(String mood) {
        if (currentUserId == null) return new ArrayList<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getEffectiveResponsesForMood(mood) : new ArrayList<>();
    }

    // ==================== TAILORED RESPONSES ====================

    /**
     * Record a phrase the user frequently uses
     */
    public void recordUserPhrase(String phrase) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordUserPhrase(phrase);
        }
    }

    /**
     * Set a customized response template
     */
    public void setResponseTemplate(String key, String template) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.setResponseTemplate(key, template);
        }
    }

    /**
     * Get a customized response template
     */
    public String getResponseTemplate(String key) {
        if (currentUserId == null) return null;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getResponseTemplate(key) : null;
    }

    /**
     * Record user's preferred greeting
     */
    public void recordGreeting(String greeting) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordGreeting(greeting);
        }
    }

    /**
     * Get user's preferred greeting
     */
    public String getPreferredGreeting() {
        if (currentUserId == null) return "hello";
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getPreferredGreeting() : "hello";
    }

    /**
     * Get frequently used phrases
     */
    public List<String> getFrequentPhrases(int limit) {
        if (currentUserId == null) return new ArrayList<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getFrequentPhrases(limit) : new ArrayList<>();
    }

    // ==================== INDIVIDUAL QUIRKS ====================

    /**
     * Record a quirky behavior
     */
    public void recordQuirk(String quirk) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordQuirk(quirk);
        }
    }

    /**
     * Record a pet peeve
     */
    public void recordPetPeave(String petPeave) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.recordPetPeave(petPeave);
        }
    }

    /**
     * Get all pet peeves
     */
    public List<String> getPetPeeves() {
        if (currentUserId == null) return new ArrayList<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getPetPeeves() : new ArrayList<>();
    }

    /**
     * Add an inside joke
     */
    public void addInsideJoke(String joke) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.addInsideJoke(joke);
        }
    }

    /**
     * Get inside jokes
     */
    public List<String> getInsideJokes() {
        if (currentUserId == null) return new ArrayList<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getInsideJokes() : new ArrayList<>();
    }

    /**
     * Set personal meaning for a word
     */
    public void setPersonalMeaning(String word, String meaning) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.setPersonalMeaning(word, meaning);
        }
    }

    /**
     * Get personal meaning for a word
     */
    public String getPersonalMeaning(String word) {
        if (currentUserId == null) return null;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getPersonalMeaning(word) : null;
    }

    /**
     * Get most notable quirks
     */
    public List<String> getNotableQuirks(int limit) {
        if (currentUserId == null) return new ArrayList<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getNotableQuirks(limit) : new ArrayList<>();
    }

    // ==================== CUSTOM INTERACTIONS ====================

    /**
     * Set a custom response for a trigger
     */
    public void setCustomResponse(String trigger, String response) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.setCustomResponse(trigger, response);
        }
    }

    /**
     * Get custom response for a trigger
     */
    public String getCustomResponse(String trigger) {
        if (currentUserId == null) return null;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getCustomResponse(trigger) : null;
    }

    /**
     * Check if custom response exists for trigger
     */
    public boolean hasCustomResponse(String trigger) {
        if (currentUserId == null) return false;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null && profile.hasCustomResponse(trigger);
    }

    /**
     * Get most used triggers
     */
    public List<String> getMostUsedTriggers(int limit) {
        if (currentUserId == null) return new ArrayList<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getMostUsedTriggers(limit) : new ArrayList<>();
    }

    /**
     * Record a special ritual
     */
    public void addSpecialRitual(String ritual) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.addSpecialRitual(ritual);
        }
    }

    /**
     * Get special rituals
     */
    public List<String> getSpecialRituals() {
        if (currentUserId == null) return new ArrayList<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getSpecialRituals() : new ArrayList<>();
    }

    /**
     * Set a nickname mapping
     */
    public void setNicknameMapping(String alias, String actualName) {
        if (currentUserId == null) return;
        InteractionProfile profile = userProfiles.get(currentUserId);
        if (profile != null) {
            profile.setNicknameMapping(alias, actualName);
        }
    }

    /**
     * Get actual name from alias
     */
    public String getActualName(String alias) {
        if (currentUserId == null) return null;
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getActualName(alias) : null;
    }

    /**
     * Get all nickname mappings
     */
    public Map<String, String> getNicknameMappings() {
        if (currentUserId == null) return new HashMap<>();
        InteractionProfile profile = userProfiles.get(currentUserId);
        return profile != null ? profile.getNicknameMappings() : new HashMap<>();
    }

    // ==================== ADAPTATION METHODS ====================

    /**
     * Get adapted response length
     */
    public double getAdaptedResponseLength() {
        return getAdaptedResponseLength(currentUserId);
    }

    /**
     * Get adapted response length for specific user
     */
    public double getAdaptedResponseLength(String userId) {
        InteractionProfile profile = userProfiles.get(userId);
        if (profile == null) return 0.5;
        
        // Start with default, adapt based on engagement
        double base = profile.getResponseLengthPreference();
        double engagement = profile.getEngagementLevel();
        
        // Higher engagement = can handle longer responses
        return Math.max(0.2, Math.min(0.9, base * 0.7 + engagement * 0.3));
    }

    /**
     * Get adapted question frequency
     */
    public double getAdaptedQuestionFrequency() {
        return getAdaptedQuestionFrequency(currentUserId);
    }

    /**
     * Get adapted question frequency for specific user
     */
    public double getAdaptedQuestionFrequency(String userId) {
        InteractionProfile profile = userProfiles.get(userId);
        if (profile == null) return 0.5;
        
        double base = profile.getQuestionFrequencyPreference();
        double engagement = profile.getEngagementLevel();
        
        // Higher engagement = more questions OK
        return Math.max(0.1, Math.min(0.8, base * 0.7 + engagement * 0.3));
    }

    /**
     * Get preferred topics for current user
     */
    public List<String> getPreferredTopics() {
        return getPreferredTopics(currentUserId);
    }

    /**
     * Get preferred topics for specific user
     */
    public List<String> getPreferredTopics(String userId) {
        InteractionProfile profile = userProfiles.get(userId);
        if (profile == null) return new ArrayList<>();
        
        // Sort by engagement score
        Map<String, Integer> engagement = profile.getTopicEngagement();
        
        return engagement.entrySet().stream()
            .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
            .limit(10)
            .map(Map.Entry::getKey)
            .toList();
    }

    /**
     * Get avoided topics for current user
     */
    public List<String> getAvoidedTopics() {
        return getAvoidedTopics(currentUserId);
    }

    /**
     * Get avoided topics for specific user
     */
    public List<String> getAvoidedTopics(String userId) {
        InteractionProfile profile = userProfiles.get(userId);
        if (profile == null) return new ArrayList<>();
        return profile.getAvoidedTopics();
    }

    /**
     * Get engagement level for current user
     */
    public double getEngagementLevel() {
        return getEngagementLevel(currentUserId);
    }

    /**
     * Get engagement level for specific user
     */
    public double getEngagementLevel(String userId) {
        InteractionProfile profile = userProfiles.get(userId);
        if (profile == null) return 0.5;
        return profile.getEngagementLevel();
    }

    /**
     * Check if user is highly engaged
     */
    public boolean isHighlyEngaged() {
        return getEngagementLevel() > 0.7;
    }

    /**
     * Check if user needs more support
     */
    public boolean needsMoreSupport() {
        return getEngagementLevel() < 0.4;
    }

    // ==================== PATTERN LEARNING ====================

    /**
     * Learn a new interaction pattern
     */
    public void learnPattern(String patternType, String response, boolean wasEffective) {
        if (currentUserId == null) return;
        
        Map<String, LearningPattern> patterns = learningPatterns.get(currentUserId);
        if (patterns == null) return;
        
        LearningPattern pattern = patterns.get(patternType);
        
        if (pattern == null) {
            pattern = new LearningPattern(patternType);
            patterns.put(patternType, pattern);
            
            // Limit patterns per user
            if (patterns.size() > maxPatternsPerUser) {
                removeWeakestPattern(patterns);
            }
        }
        
        pattern.recordInteraction(response, wasEffective);
    }

    /**
     * Get learned pattern for a specific type
     */
    public LearningPattern getPattern(String patternType) {
        return getPattern(currentUserId, patternType);
    }

    /**
     * Get pattern for specific user
     */
    public LearningPattern getPattern(String userId, String patternType) {
        Map<String, LearningPattern> patterns = learningPatterns.get(userId);
        if (patterns == null) return null;
        return patterns.get(patternType);
    }

    /**
     * Remove weakest pattern to make room for new ones
     */
    private void removeWeakestPattern(Map<String, LearningPattern> patterns) {
        String weakest = null;
        double weakestStrength = Double.MAX_VALUE;
        
        for (Map.Entry<String, LearningPattern> entry : patterns.entrySet()) {
            double strength = entry.getValue().getPatternStrength();
            if (strength < weakestStrength) {
                weakestStrength = strength;
                weakest = entry.getKey();
            }
        }
        
        if (weakest != null) {
            patterns.remove(weakest);
        }
    }

    /**
     * Get all learned patterns for current user
     */
    public Map<String, LearningPattern> getAllPatterns() {
        return getAllPatterns(currentUserId);
    }

    /**
     * Get all patterns for specific user
     */
    public Map<String, LearningPattern> getAllPatterns(String userId) {
        Map<String, LearningPattern> patterns = learningPatterns.get(userId);
        return patterns != null ? new HashMap<>(patterns) : new HashMap<>();
    }

    // ==================== RECOMMENDATIONS ====================

    /**
     * Generate adaptation recommendations
     */
    public List<AdaptationRecommendation> generateRecommendations() {
        return generateRecommendations(currentUserId);
    }

    /**
     * Generate recommendations for specific user
     */
    public List<AdaptationRecommendation> generateRecommendations(String userId) {
        List<AdaptationRecommendation> results = new ArrayList<>();
        
        InteractionProfile profile = userProfiles.get(userId);
        if (profile == null) return results;
        
        // Recommendation 1: Response length
        double responseLength = profile.getResponseLengthPreference();
        String lengthDesc = responseLength > 0.6 ? "Use longer, more detailed responses" :
                           responseLength < 0.4 ? "Use shorter, more concise responses" :
                           "Use balanced response length";
        results.add(new AdaptationRecommendation(CATEGORY_RESPONSE_STYLE, lengthDesc, Math.abs(responseLength - 0.5) * 2));
        
        // Recommendation 2: Question frequency
        double questionFreq = profile.getQuestionFrequencyPreference();
        String freqDesc = questionFreq > 0.6 ? "Ask more follow-up questions" :
                         questionFreq < 0.4 ? "Reduce questions, listen more" :
                         "Maintain balanced questioning";
        results.add(new AdaptationRecommendation(CATEGORY_PACING, freqDesc, Math.abs(questionFreq - 0.5) * 2));
        
        // Recommendation 3: Engagement-based
        double engagement = profile.getEngagementLevel();
        if (engagement < 0.4) {
            results.add(new AdaptationRecommendation(CATEGORY_ENGAGEMENT, "User may need more support and encouragement", 0.8));
        } else if (engagement > 0.7) {
            results.add(new AdaptationRecommendation(CATEGORY_ENGAGEMENT, "User is highly engaged, can introduce more topics", 0.7));
        }
        
        // Recommendation 4: Topic preferences
        List<String> preferred = profile.getPreferredTopics();
        if (!preferred.isEmpty()) {
            results.add(new AdaptationRecommendation(CATEGORY_TOPIC_PREFERENCE, 
                "Continue discussing: " + String.join(", ", preferred.subList(0, Math.min(3, preferred.size()))), 0.6));
        }
        
        // Recommendation 5: Emotional patterns
        Map<String, Integer> triggers = profile.getEmotionalTriggers();
        if (!triggers.isEmpty()) {
            String topEmotion = triggers.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
            
            if (topEmotion != null) {
                results.add(new AdaptationRecommendation(CATEGORY_EMOTIONAL_PATTERN, 
                    "User frequently expresses: " + topEmotion, 0.5));
            }
        }
        
        return results;
    }

    /**
     * Get stored recommendations for current user
     */
    public List<AdaptationRecommendation> getRecommendations() {
        return getRecommendations(currentUserId);
    }

    /**
     * Get recommendations for specific user
     */
    public List<AdaptationRecommendation> getRecommendations(String userId) {
        List<AdaptationRecommendation> recs = recommendations.get(userId);
        return recs != null ? new ArrayList<>(recs) : new ArrayList<>();
    }

    // ==================== INSIGHTS ====================

    /**
     * Get learning insights for current user
     */
    public Map<String, Object> getInsights() {
        return getInsights(currentUserId);
    }

    /**
     * Get insights for specific user
     */
    public Map<String, Object> getInsights(String userId) {
        Map<String, Object> insights = new HashMap<>();
        
        InteractionProfile profile = userProfiles.get(userId);
        if (profile != null) {
            insights.put("totalInteractions", profile.getTotalInteractions());
            insights.put("engagementLevel", profile.getEngagementLevel());
            insights.put("streakDays", profile.getStreakDays());
            insights.put("preferredTopics", profile.getPreferredTopics());
            insights.put("avoidedTopics", profile.getAvoidedTopics());
            insights.put("responseLengthPreference", profile.getResponseLengthPreference());
            insights.put("questionFrequencyPreference", profile.getQuestionFrequencyPreference());
            
            // Days since first interaction
            long daysSinceFirst = Duration.between(profile.getFirstInteraction(), LocalDateTime.now()).toDays();
            insights.put("daysSinceFirst", daysSinceFirst);
            
            // Interactions per day
            double interactionsPerDay = daysSinceFirst > 0 ? 
                (double) profile.getTotalInteractions() / daysSinceFirst : profile.getTotalInteractions();
            insights.put("interactionsPerDay", interactionsPerDay);
        }
        
        Map<String, LearningPattern> patterns = learningPatterns.get(userId);
        if (patterns != null) {
            insights.put("learnedPatterns", patterns.size());
            
            // Pattern strength distribution
            double totalStrength = patterns.values().stream()
                .mapToDouble(LearningPattern::getPatternStrength)
                .sum();
            insights.put("totalPatternStrength", totalStrength);
        }
        
        insights.put("totalLearningEvents", totalLearningEvents);
        
        return insights;
    }

    /**
     * Get emotional pattern analysis
     */
    public Map<String, Integer> getEmotionalPatterns() {
        return getEmotionalPatterns(currentUserId);
    }

    /**
     * Get emotional patterns for specific user
     */
    public Map<String, Integer> getEmotionalPatterns(String userId) {
        InteractionProfile profile = userProfiles.get(userId);
        if (profile == null) return new HashMap<>();
        
        return new HashMap<>(profile.getEmotionalTriggers());
    }

    /**
     * Get dominant emotion
     */
    public String getDominantEmotion() {
        return getDominantEmotion(currentUserId);
    }

    /**
     * Get dominant emotion for specific user
     */
    public String getDominantEmotion(String userId) {
        Map<String, Integer> emotions = getEmotionalPatterns(userId);
        if (emotions.isEmpty()) return null;
        
        return emotions.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    // ==================== ADAPTATION SCORE ====================

    /**
     * Calculate overall adaptation score (how well we've learned the user)
     */
    public double getAdaptationScore() {
        return getAdaptationScore(currentUserId);
    }

    /**
     * Calculate adaptation score for specific user
     */
    public double getAdaptationScore(String userId) {
        double score = 0.0;
        int factors = 0;
        
        InteractionProfile profile = userProfiles.get(userId);
        if (profile != null) {
            // Profile completeness
            score += profile.getTotalInteractions() > 10 ? 0.2 : profile.getTotalInteractions() / 50.0;
            factors++;
            
            // Engagement tracking
            score += 0.2;
            factors++;
            
            // Topic preferences known
            if (!profile.getPreferredTopics().isEmpty()) {
                score += 0.2;
            }
            factors++;
        }
        
        Map<String, LearningPattern> patterns = learningPatterns.get(userId);
        if (patterns != null) {
            // Patterns learned
            score += Math.min(0.4, patterns.size() * 0.05);
            factors++;
        }
        
        return factors > 0 ? score / factors : 0.0;
    }

    // ==================== RESET AND CLEAR ====================

    /**
     * Reset learning for current user
     */
    public void resetLearning() {
        resetLearning(currentUserId);
    }

    /**
     * Reset learning for specific user
     */
    public void resetLearning(String userId) {
        if (userId == null) return;
        
        userProfiles.remove(userId);
        learningPatterns.remove(userId);
        recommendations.remove(userId);
    }

    /**
     * Clear all learning data
     */
    public void clearAll() {
        currentUserId = null;
        userProfiles.clear();
        learningPatterns.clear();
        recentFeedback.clear();
        recommendations.clear();
        totalLearningEvents = 0;
        categoryLearningCounts.clear();
    }

    // ==================== STATISTICS ====================

    /**
     * Get learning statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalUsers", userProfiles.size());
        stats.put("currentUser", currentUserId);
        stats.put("totalLearningEvents", totalLearningEvents);
        stats.put("feedbackHistorySize", recentFeedback.size());
        stats.put("categoryLearningCounts", new HashMap<>(categoryLearningCounts));
        
        if (currentUserId != null) {
            InteractionProfile profile = userProfiles.get(currentUserId);
            if (profile != null) {
                stats.put("userInteractions", profile.getTotalInteractions());
                stats.put("userEngagement", profile.getEngagementLevel());
            }
            
            Map<String, LearningPattern> patterns = learningPatterns.get(currentUserId);
            if (patterns != null) {
                stats.put("userPatterns", patterns.size());
            }
        }
        
        return stats;
    }

    @Override
    public String toString() {
        return String.format("AdaptiveLearner{currentUser=%s, users=%d, events=%d}",
            currentUserId, userProfiles.size(), totalLearningEvents);
    }
}

