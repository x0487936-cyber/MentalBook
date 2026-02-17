/**
 * InsightEngine.java
 * Handles insight generation and analysis for the MentalBook application.
 * 
 * This engine analyzes user interactions and generates meaningful insights
 * about patterns, behaviors, and personal growth.
 * 
 * Pattern Insights:
 * - User behavior patterns
 * - Emotional cycles
 * - Interest trends
 * 
 * Perspective Insights:
 * - Blind spot identification
 * - Alternative viewpoints
 * - Growth opportunities
 * 
 * Life Insights:
 * - Recurring themes
 * - Strength identification
 * - Potential areas
 */

import java.util.*;
import java.time.*;

public class InsightEngine {
    
    // Data structures to store pattern information
    private Map<String, List<BehaviorRecord>> userBehaviorPatterns;
    private Map<String, List<EmotionalCycle>> emotionalCycles;
    private Map<String, List<InterestTrend>> interestTrends;
    
    // Data structures for perspective insights
    private Map<String, List<BlindSpot>> blindSpots;
    private Map<String, List<AlternativeViewpoint>> alternativeViewpoints;
    private Map<String, List<GrowthOpportunity>> growthOpportunities;
    
    // Data structures for life insights
    private Map<String, List<RecurringTheme>> recurringThemes;
    private Map<String, List<Strength>> strengths;
    private Map<String, List<PotentialArea>> potentialAreas;
    
    /**
     * Inner class to store behavior pattern records
     */
    private static class BehaviorRecord {
        String userId;
        String behaviorType;
        LocalDateTime timestamp;
        int frequency;
        
        BehaviorRecord(String userId, String behaviorType, LocalDateTime timestamp) {
            this.userId = userId;
            this.behaviorType = behaviorType;
            this.timestamp = timestamp;
            this.frequency = 1;
        }
    }
    
    /**
     * Inner class to store emotional cycle data
     */
    private static class EmotionalCycle {
        String userId;
        String emotion;
        LocalDateTime timestamp;
        int intensity;
        
        EmotionalCycle(String userId, String emotion, LocalDateTime timestamp, int intensity) {
            this.userId = userId;
            this.emotion = emotion;
            this.timestamp = timestamp;
            this.intensity = intensity;
        }
    }
    
    /**
     * Inner class to store interest trend data
     */
    private static class InterestTrend {
        String userId;
        String topic;
        LocalDateTime lastActive;
        double engagementScore;
        
        InterestTrend(String userId, String topic, LocalDateTime lastActive, double engagementScore) {
            this.userId = userId;
            this.topic = topic;
            this.lastActive = lastActive;
            this.engagementScore = engagementScore;
        }
    }
    
    /**
     * Inner class to store blind spot data
     */
    private static class BlindSpot {
        String userId;
        String area;
        String description;
        LocalDateTime identifiedAt;
        double severity;
        
        BlindSpot(String userId, String area, String description, double severity) {
            this.userId = userId;
            this.area = area;
            this.description = description;
            this.identifiedAt = LocalDateTime.now();
            this.severity = severity;
        }
    }
    
    /**
     * Inner class to store alternative viewpoint data
     */
    private static class AlternativeViewpoint {
        String userId;
        String topic;
        String viewpoint;
        LocalDateTime createdAt;
        
        AlternativeViewpoint(String userId, String topic, String viewpoint) {
            this.userId = userId;
            this.topic = topic;
            this.viewpoint = viewpoint;
            this.createdAt = LocalDateTime.now();
        }
    }
    
    /**
     * Inner class to store growth opportunity data
     */
    private static class GrowthOpportunity {
        String userId;
        String area;
        String description;
        double potentialImpact;
        LocalDateTime identifiedAt;
        
        GrowthOpportunity(String userId, String area, String description, double potentialImpact) {
            this.userId = userId;
            this.area = area;
            this.description = description;
            this.potentialImpact = potentialImpact;
            this.identifiedAt = LocalDateTime.now();
        }
    }
    
    /**
     * Inner class to store recurring theme data
     */
    private static class RecurringTheme {
        String userId;
        String theme;
        int occurrences;
        LocalDateTime lastSeen;
        
        RecurringTheme(String userId, String theme) {
            this.userId = userId;
            this.theme = theme;
            this.occurrences = 1;
            this.lastSeen = LocalDateTime.now();
        }
    }
    
    /**
     * Inner class to store strength data
     */
    private static class Strength {
        String userId;
        String name;
        double score;
        LocalDateTime identifiedAt;
        
        Strength(String userId, String name, double score) {
            this.userId = userId;
            this.name = name;
            this.score = score;
            this.identifiedAt = LocalDateTime.now();
        }
    }
    
    /**
     * Inner class to store potential area data
     */
    private static class PotentialArea {
        String userId;
        String area;
        String description;
        double developmentPotential;
        LocalDateTime identifiedAt;
        
        PotentialArea(String userId, String area, String description, double developmentPotential) {
            this.userId = userId;
            this.area = area;
            this.description = description;
            this.developmentPotential = developmentPotential;
            this.identifiedAt = LocalDateTime.now();
        }
    }
    
    public InsightEngine() {
        this.userBehaviorPatterns = new HashMap<>();
        this.emotionalCycles = new HashMap<>();
        this.interestTrends = new HashMap<>();
        this.blindSpots = new HashMap<>();
        this.alternativeViewpoints = new HashMap<>();
        this.growthOpportunities = new HashMap<>();
        this.recurringThemes = new HashMap<>();
        this.strengths = new HashMap<>();
        this.potentialAreas = new HashMap<>();
    }
    
    /**
     * Generates an insight based on recent interaction patterns.
     * @param userId The user's unique identifier
     * @return A string containing the generated insight
     */
    public String generateInsight(String userId) {
        StringBuilder insight = new StringBuilder();
        
        // Analyze user behavior patterns
        List<BehaviorRecord> behaviors = userBehaviorPatterns.get(userId);
        if (behaviors != null && !behaviors.isEmpty()) {
            insight.append("Behavior Pattern: You've shown consistent engagement in ");
            insight.append(behaviors.size());
            insight.append(" interaction areas. ");
        }
        
        // Analyze emotional cycles
        List<EmotionalCycle> emotions = emotionalCycles.get(userId);
        if (emotions != null && !emotions.isEmpty()) {
            insight.append("Emotional Cycle: Your emotional patterns show ");
            insight.append(analyzeEmotionalCycle(emotions));
            insight.append(". ");
        }
        
        // Analyze interest trends
        List<InterestTrend> interests = interestTrends.get(userId);
        if (interests != null && !interests.isEmpty()) {
            insight.append("Interest Trends: You're currently most engaged with ");
            insight.append(getTopInterest(interests));
            insight.append(" topics.");
        }
        
        if (insight.length() == 0) {
            return "Not enough data to generate insights yet. Keep interacting to build your pattern profile.";
        }
        
        return insight.toString();
    }
    
    /**
     * Analyzes emotional patterns over time.
     * @param userId The user's unique identifier
     * @return Analysis result as a string
     */
    public String analyzePatterns(String userId) {
        List<EmotionalCycle> emotions = emotionalCycles.get(userId);
        
        if (emotions == null || emotions.isEmpty()) {
            return "No emotional data available for pattern analysis.";
        }
        
        // Calculate emotional cycle metrics
        Map<String, Integer> emotionCounts = new HashMap<>();
        for (EmotionalCycle cycle : emotions) {
            emotionCounts.put(cycle.emotion, emotionCounts.getOrDefault(cycle.emotion, 0) + 1);
        }
        
        StringBuilder analysis = new StringBuilder();
        analysis.append("Emotional Pattern Analysis:\n");
        
        // Find dominant emotion
        String dominantEmotion = Collections.max(emotionCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
        analysis.append("- Dominant emotion: ").append(dominantEmotion).append("\n");
        
        // Calculate average intensity
        double avgIntensity = emotions.stream().mapToInt(e -> e.intensity).average().orElse(0);
        analysis.append("- Average emotional intensity: ").append(String.format("%.1f", avgIntensity)).append("/10\n");
        
        // Identify cycle patterns
        analysis.append("- Cycle pattern: ").append(analyzeEmotionalCycle(emotions));
        
        return analysis.toString();
    }
    
    /**
     * Provides personalized recommendations based on insights.
     * @param userId The user's unique identifier
     * @return Recommendation as a string
     */
    public String getRecommendation(String userId) {
        List<EmotionalCycle> emotions = emotionalCycles.get(userId);
        List<InterestTrend> interests = interestTrends.get(userId);
        
        StringBuilder recommendation = new StringBuilder();
        recommendation.append("Personalized Recommendations:\n");
        
        // Emotional-based recommendations
        if (emotions != null && !emotions.isEmpty()) {
            String dominantEmotion = emotions.stream()
                .collect(java.util.stream.Collectors.groupingBy(e -> e.emotion, java.util.stream.Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("neutral");
            
            switch (dominantEmotion.toLowerCase()) {
                case "sad":
                case "sadness":
                    recommendation.append("- Consider engaging in activities you enjoy or reaching out to a support system.\n");
                    break;
                case "stress":
                case "stressed":
                    recommendation.append("- Try taking breaks and practicing relaxation techniques.\n");
                    break;
                case "happy":
                case "happiness":
                    recommendation.append("- Great time to tackle challenging tasks or social activities!\n");
                    break;
                default:
                    recommendation.append("- Maintain your balanced emotional state with regular activities.\n");
            }
        }
        
        // Interest-based recommendations
        if (interests != null && !interests.isEmpty()) {
            String topInterest = getTopInterest(interests);
            recommendation.append("- Continue exploring ").append(topInterest).append(" topics for continued growth.\n");
        }
        
        if (recommendation.length() <= "Personalized Recommendations:\n".length()) {
            recommendation.append("- Continue interacting to receive personalized recommendations.\n");
        }
        
        return recommendation.toString();
    }
    
    // Helper methods
    
    /**
     * Analyzes emotional cycle patterns
     */
    private String analyzeEmotionalCycle(List<EmotionalCycle> emotions) {
        if (emotions == null || emotions.isEmpty()) {
            return "insufficient data";
        }
        
        // Sort by timestamp
        List<EmotionalCycle> sorted = new ArrayList<>(emotions);
        sorted.sort(Comparator.comparing(e -> e.timestamp));
        
        // Check for patterns (e.g., time-based cycles)
        long timeSpan = java.time.Duration.between(
            sorted.get(0).timestamp, 
            sorted.get(sorted.size() - 1).timestamp
        ).toDays();
        
        if (timeSpan < 7) {
            return "recent emotional fluctuations";
        } else if (timeSpan < 30) {
            return "weekly emotional patterns";
        } else {
            return "monthly emotional cycles";
        }
    }
    
    /**
     * Gets the top interest from trends
     */
    private String getTopInterest(List<InterestTrend> interests) {
        return interests.stream()
            .max(Comparator.comparingDouble(i -> i.engagementScore))
            .map(i -> i.topic)
            .orElse("various");
    }
    
    // Data addition methods
    
    /**
     * Records a user behavior pattern
     */
    public void recordBehavior(String userId, String behaviorType) {
        userBehaviorPatterns.computeIfAbsent(userId, k -> new ArrayList<>())
            .add(new BehaviorRecord(userId, behaviorType, LocalDateTime.now()));
    }
    
    /**
     * Records an emotional cycle entry
     */
    public void recordEmotion(String userId, String emotion, int intensity) {
        emotionalCycles.computeIfAbsent(userId, k -> new ArrayList<>())
            .add(new EmotionalCycle(userId, emotion, LocalDateTime.now(), intensity));
    }
    
    /**
     * Records an interest trend
     */
    public void recordInterest(String userId, String topic, double engagementScore) {
        interestTrends.computeIfAbsent(userId, k -> new ArrayList<>())
            .add(new InterestTrend(userId, topic, LocalDateTime.now(), engagementScore));
    }
    
    // ==================== Perspective Insights ====================
    
    /**
     * Identifies blind spots based on user interaction patterns
     * @param userId The user's unique identifier
     * @return Blind spot identification result as a string
     */
    public String identifyBlindSpots(String userId) {
        List<BlindSpot> spots = blindSpots.get(userId);
        
        if (spots == null || spots.isEmpty()) {
            // Auto-identify blind spots based on behavior patterns
            List<BehaviorRecord> behaviors = userBehaviorPatterns.get(userId);
            if (behaviors == null || behaviors.size() < 5) {
                return "Not enough interaction data to identify blind spots yet.";
            }
            
            // Simple heuristic: areas with no interaction are potential blind spots
            Set<String> exploredAreas = new HashSet<>();
            for (BehaviorRecord b : behaviors) {
                exploredAreas.add(b.behaviorType);
            }
            
            StringBuilder result = new StringBuilder();
            result.append("Potential Blind Spots Identified:\n");
            
            if (!exploredAreas.contains("social")) {
                result.append("- Social interactions: You may benefit from more social engagement.\n");
            }
            if (!exploredAreas.contains("emotional")) {
                result.append("- Emotional awareness: Consider tracking your emotional patterns more.\n");
            }
            if (!exploredAreas.contains("learning")) {
                result.append("- Learning activities: Exploring new topics could broaden your perspective.\n");
            }
            
            if (result.toString().equals("Potential Blind Spots Identified:\n")) {
                return "Good coverage across areas. No significant blind spots detected.";
            }
            
            return result.toString();
        }
        
        // Return recorded blind spots
        StringBuilder result = new StringBuilder();
        result.append("Identified Blind Spots:\n");
        for (BlindSpot spot : spots) {
            result.append("- ").append(spot.area).append(": ").append(spot.description).append("\n");
        }
        return result.toString();
    }
    
    /**
     * Generates alternative viewpoints for a given topic
     * @param userId The user's unique identifier
     * @param topic The topic to get alternative viewpoints for
     * @return Alternative viewpoints as a string
     */
    public String getAlternativeViewpoints(String userId, String topic) {
        List<AlternativeViewpoint> viewpoints = alternativeViewpoints.get(userId);
        
        StringBuilder result = new StringBuilder();
        result.append("Alternative Viewpoints on \"").append(topic).append("\":\n");
        
        // Check for recorded viewpoints
        if (viewpoints != null && !viewpoints.isEmpty()) {
            for (AlternativeViewpoint v : viewpoints) {
                if (v.topic.equalsIgnoreCase(topic)) {
                    result.append("- ").append(v.viewpoint).append("\n");
                }
            }
        }
        
        // Add default alternative viewpoints
        if (result.length() <= ("Alternative Viewpoints on \"" + topic + "\":\n").length()) {
            result.append("- Consider looking at this from a different cultural perspective.\n");
            result.append("- Think about how this might appear to someone with different experiences.\n");
            result.append("- What would be the opposite viewpoint? Sometimes exploring the other side reveals new insights.\n");
            result.append("- Consider the long-term vs short-term implications.\n");
        }
        
        return result.toString();
    }
    
    /**
     * Identifies growth opportunities based on user patterns
     * @param userId The user's unique identifier
     * @return Growth opportunities as a string
     */
    public String identifyGrowthOpportunities(String userId) {
        List<GrowthOpportunity> opportunities = growthOpportunities.get(userId);
        
        StringBuilder result = new StringBuilder();
        result.append("Growth Opportunities:\n");
        
        // Check for recorded opportunities
        if (opportunities != null && !opportunities.isEmpty()) {
            // Sort by potential impact
            opportunities.sort((a, b) -> Double.compare(b.potentialImpact, a.potentialImpact));
            for (GrowthOpportunity opp : opportunities) {
                result.append("- [").append(String.format("%.0f%%", opp.potentialImpact * 100))
                    .append(" impact] ").append(opp.area).append(": ").append(opp.description).append("\n");
            }
        } else {
            // Auto-identify based on emotional patterns and interests
            List<EmotionalCycle> emotions = emotionalCycles.get(userId);
            List<InterestTrend> interests = interestTrends.get(userId);
            
            if (emotions != null && !emotions.isEmpty()) {
                // Find dominant negative emotion and suggest growth
                Map<String, Long> emotionCounts = emotions.stream()
                    .collect(java.util.stream.Collectors.groupingBy(e -> e.emotion, java.util.stream.Collectors.counting()));
                
                if (emotionCounts.containsKey("stress") || emotionCounts.containsKey("anxiety")) {
                    result.append("- [80% impact] Stress Management: Practice mindfulness and breathing techniques.\n");
                }
                if (emotionCounts.containsKey("sad") || emotionCounts.containsKey("sadness")) {
                    result.append("- [75% impact] Emotional Resilience: Build coping strategies through journaling.\n");
                }
            }
            
            if (interests != null && !interests.isEmpty()) {
                result.append("- [90% impact] Deepen Expertise: Continue exploring your interest in ")
                    .append(getTopInterest(interests)).append(" topics.\n");
            }
            
            if (result.length() <= "Growth Opportunities:\n".length()) {
                result.append("- Continue interacting to unlock personalized growth opportunities.\n");
            }
        }
        
        return result.toString();
    }
    
    // Methods to record perspective insights
    
    /**
     * Records a blind spot for the user
     */
    public void recordBlindSpot(String userId, String area, String description, double severity) {
        blindSpots.computeIfAbsent(userId, k -> new ArrayList<>())
            .add(new BlindSpot(userId, area, description, severity));
    }
    
    /**
     * Records an alternative viewpoint for the user
     */
    public void recordAlternativeViewpoint(String userId, String topic, String viewpoint) {
        alternativeViewpoints.computeIfAbsent(userId, k -> new ArrayList<>())
            .add(new AlternativeViewpoint(userId, topic, viewpoint));
    }
    
    /**
     * Records a growth opportunity for the user
     */
    public void recordGrowthOpportunity(String userId, String area, String description, double potentialImpact) {
        growthOpportunities.computeIfAbsent(userId, k -> new ArrayList<>())
            .add(new GrowthOpportunity(userId, area, description, potentialImpact));
    }
    
    // ==================== Life Insights ====================
    
    /**
     * Analyzes recurring themes in user's interactions
     * @param userId The user's unique identifier
     * @return Recurring themes as a string
     */
    public String analyzeRecurringThemes(String userId) {
        List<RecurringTheme> themes = recurringThemes.get(userId);
        
        StringBuilder result = new StringBuilder();
        result.append("Recurring Themes in Your Life:\n");
        
        // Check for recorded themes
        if (themes != null && !themes.isEmpty()) {
            // Sort by occurrences
            themes.sort((a, b) -> Integer.compare(b.occurrences, a.occurrences));
            for (RecurringTheme theme : themes) {
                result.append("- \"").append(theme.theme).append("\" (appeared ")
                    .append(theme.occurrences).append(" times)\n");
            }
        } else {
            // Auto-detect from behaviors, emotions, and interests
            Map<String, Integer> themeCounts = new HashMap<>();
            
            List<BehaviorRecord> behaviors = userBehaviorPatterns.get(userId);
            if (behaviors != null) {
                for (BehaviorRecord b : behaviors) {
                    themeCounts.merge(b.behaviorType, 1, Integer::sum);
                }
            }
            
            List<InterestTrend> interests = interestTrends.get(userId);
            if (interests != null) {
                for (InterestTrend i : interests) {
                    themeCounts.merge(i.topic, 1, Integer::sum);
                }
            }
            
            if (!themeCounts.isEmpty()) {
                result.append("Based on your interaction patterns:\n");
                themeCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(5)
                    .forEach(e -> result.append("- \"").append(e.getKey())
                        .append("\" appears frequently in your interactions\n"));
            } else {
                result.append("- Not enough data to identify recurring themes yet.\n");
                result.append("- Continue interacting to discover your life patterns.\n");
            }
        }
        
        return result.toString();
    }
    
    /**
     * Identifies user strengths based on patterns
     * @param userId The user's unique identifier
     * @return Identified strengths as a string
     */
    public String identifyStrengths(String userId) {
        List<Strength> userStrengths = strengths.get(userId);
        
        StringBuilder result = new StringBuilder();
        result.append("Your Strengths:\n");
        
        // Check for recorded strengths
        if (userStrengths != null && !userStrengths.isEmpty()) {
            userStrengths.sort((a, b) -> Double.compare(b.score, a.score));
            for (Strength s : userStrengths) {
                result.append("- ").append(s.name).append(" (score: ")
                    .append(String.format("%.0f%%", s.score * 100)).append(")\n");
            }
        } else {
            // Auto-identify based on behaviors and engagement
            List<BehaviorRecord> behaviors = userBehaviorPatterns.get(userId);
            List<InterestTrend> interests = interestTrends.get(userId);
            
            if (behaviors != null && interests != null) {
                // High engagement + consistency = potential strength
                if (behaviors.size() >= 5) {
                    result.append("- Consistency: You've maintained regular engagement patterns.\n");
                }
                if (!interests.isEmpty()) {
                    result.append("- Curiosity: You explore multiple topics with high engagement.\n");
                }
            }
            
            List<EmotionalCycle> emotions = emotionalCycles.get(userId);
            if (emotions != null && !emotions.isEmpty()) {
                long positiveEmotions = emotions.stream()
                    .filter(e -> e.emotion.equalsIgnoreCase("happy") || 
                                 e.emotion.equalsIgnoreCase("joy") ||
                                 e.emotion.equalsIgnoreCase("excited"))
                    .count();
                if (positiveEmotions > emotions.size() * 0.5) {
                    result.append("- Emotional Resilience: You maintain a positive emotional balance.\n");
                }
            }
            
            if (result.toString().equals("Your Strengths:\n")) {
                result.append("- Keep interacting to discover your unique strengths.\n");
            }
        }
        
        return result.toString();
    }
    
    /**
     * Identifies potential areas for development
     * @param userId The user's unique identifier
     * @return Potential areas as a string
     */
    public String identifyPotentialAreas(String userId) {
        List<PotentialArea> areas = potentialAreas.get(userId);
        
        StringBuilder result = new StringBuilder();
        result.append("Potential Areas for Development:\n");
        
        // Check for recorded areas
        if (areas != null && !areas.isEmpty()) {
            areas.sort((a, b) -> Double.compare(b.developmentPotential, a.developmentPotential));
            for (PotentialArea area : areas) {
                result.append("- [").append(String.format("%.0f%%", area.developmentPotential * 100))
                    .append(" potential] ").append(area.area).append(": ")
                    .append(area.description).append("\n");
            }
        } else {
            // Auto-identify based on blind spots and lack of engagement
            List<BlindSpot> spots = blindSpots.get(userId);
            List<BehaviorRecord> behaviors = userBehaviorPatterns.get(userId);
            
            Set<String> exploredAreas = new HashSet<>();
            if (behaviors != null) {
                for (BehaviorRecord b : behaviors) {
                    exploredAreas.add(b.behaviorType);
                }
            }
            
            // Suggest areas not yet explored
            if (!exploredAreas.contains("social")) {
                result.append("- [70% potential] Social Skills: Building connections can enhance wellbeing.\n");
            }
            if (!exploredAreas.contains("creative")) {
                result.append("- [65% potential] Creativity: Exploring creative activities can reduce stress.\n");
            }
            if (!exploredAreas.contains("physical")) {
                result.append("- [80% potential] Physical Wellness: Integrating movement can improve mood.\n");
            }
            
            // Use blind spots as development areas
            if (spots != null && !spots.isEmpty()) {
                for (BlindSpot spot : spots) {
                    result.append("- [").append(String.format("%.0f%%", (1 - spot.severity) * 100))
                        .append(" potential] ").append(spot.area)
                        .append(": Room for growth in this underrepresented area.\n");
                }
            }
            
            if (result.toString().equals("Potential Areas for Development:\n")) {
                result.append("- Great balance across areas! Keep exploring to find new opportunities.\n");
            }
        }
        
        return result.toString();
    }
    
    // Methods to record life insights
    
    /**
     * Records a recurring theme for the user
     */
    public void recordRecurringTheme(String userId, String theme) {
        List<RecurringTheme> themes = recurringThemes.computeIfAbsent(userId, k -> new ArrayList<>());
        
        // Check if theme already exists
        Optional<RecurringTheme> existing = themes.stream()
            .filter(t -> t.theme.equalsIgnoreCase(theme))
            .findFirst();
        
        if (existing.isPresent()) {
            existing.get().occurrences++;
            existing.get().lastSeen = LocalDateTime.now();
        } else {
            themes.add(new RecurringTheme(userId, theme));
        }
    }
    
    /**
     * Records a strength for the user
     */
    public void recordStrength(String userId, String name, double score) {
        strengths.computeIfAbsent(userId, k -> new ArrayList<>())
            .add(new Strength(userId, name, score));
    }
    
    /**
     * Records a potential development area for the user
     */
    public void recordPotentialArea(String userId, String area, String description, double developmentPotential) {
        potentialAreas.computeIfAbsent(userId, k -> new ArrayList<>())
            .add(new PotentialArea(userId, area, description, developmentPotential));
    }
}

