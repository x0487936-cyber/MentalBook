import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

/**
 * Topic Clustering System for VirtualXander
 * Groups related conversation topics and manages topic transitions
 */
public class TopicClusteringSystem {
    
    private Map<String, TopicCluster> topicClusters;
    private Map<String, String> topicKeywords;
    private Map<String, TopicCluster> activeClusters;
    private int maxClusterHistory;
    private Random random;
    
    public TopicClusteringSystem() {
        this.topicClusters = new HashMap<>();
        this.topicKeywords = new HashMap<>();
        this.activeClusters = new HashMap<>();
        this.maxClusterHistory = 5;
        this.random = new Random();
        initializeTopicClusters();
    }
    
    /**
     * Topic cluster definition
     */
    public static class TopicCluster {
        String clusterId;
        String clusterName;
        List<String> relatedTopics;
        List<String> keywords;
        double relevanceScore;
        long lastActiveTime;
        int visitCount;
        
        public TopicCluster(String clusterId, String clusterName, List<String> relatedTopics, List<String> keywords) {
            this.clusterId = clusterId;
            this.clusterName = clusterName;
            this.relatedTopics = relatedTopics;
            this.keywords = keywords;
            this.relevanceScore = 0.5;
            this.lastActiveTime = System.currentTimeMillis();
            this.visitCount = 0;
        }
        
        public void activate() {
            this.lastActiveTime = System.currentTimeMillis();
            this.visitCount++;
        }
        
        public double getRelevanceScore() {
            // Recency boost
            long timeSinceActive = System.currentTimeMillis() - lastActiveTime;
            double recencyBoost = Math.max(0, 1.0 - (timeSinceActive / (1000 * 60 * 30))); // 30 minute decay
            
            // Frequency boost
            double frequencyBoost = Math.min(0.5, visitCount * 0.05);
            
            return Math.min(1.0, relevanceScore + recencyBoost + frequencyBoost);
        }
    }
    
    /**
     * Topic transition suggestion
     */
    public static class TopicTransition {
        String fromTopic;
        String toTopic;
        String reason;
        double relevanceScore;
        
        public TopicTransition(String fromTopic, String toTopic, String reason, double relevanceScore) {
            this.fromTopic = fromTopic;
            this.toTopic = toTopic;
            this.reason = reason;
            this.relevanceScore = relevanceScore;
        }
    }
    
    private void initializeTopicClusters() {
        // Academic cluster
        TopicCluster academic = new TopicCluster(
            "academic",
            "Academic & Education",
            Arrays.asList("homework", "math", "science", "history", "english", "geography", "study", "school", "learning", "exam"),
            Arrays.asList("homework", "assignment", "test", "quiz", "grade", "teacher", "class", "subject", "study", "learn")
        );
        topicClusters.put("academic", academic);
        
        // Gaming cluster
        TopicCluster gaming = new TopicCluster(
            "gaming",
            "Gaming & Entertainment",
            Arrays.asList("game", "gaming", "fortnite", "cs2", "minecraft", "video games", "play", "gamer"),
            Arrays.asList("play", "game", "gaming", "level", "win", "lose", "multiplayer", "online", "esports")
        );
        topicClusters.put("gaming", gaming);
        
        // Mental Health cluster
        TopicCluster mentalHealth = new TopicCluster(
            "mental_health",
            "Mental Health & Emotions",
            Arrays.asList("feel", "emotion", "sad", "happy", "stressed", "anxious", "mental", "support", "therapy"),
            Arrays.asList("feel", "feeling", "emotion", "sad", "happy", "stressed", "anxious", "depressed", "lonely", "worried")
        );
        topicClusters.put("mental_health", mentalHealth);
        
        // Creative cluster
        TopicCluster creative = new TopicCluster(
            "creative",
            "Creative & Projects",
            Arrays.asList("write", "creative", "story", "art", "music", "project", "build", "create", "design"),
            Arrays.asList("write", "story", "art", "music", "creative", "painting", "drawing", "writing", "poem", "song")
        );
        topicClusters.put("creative", creative);
        
        // Social cluster
        TopicCluster social = new TopicCluster(
            "social",
            "Social & Relationships",
            Arrays.asList("friend", "family", "relationship", "social", "party", "hang out", "people"),
            Arrays.asList("friend", "friends", "family", "relationship", "social", "party", "hang out", "people", "connection")
        );
        topicClusters.put("social", social);
        
        // Technology cluster
        TopicCluster technology = new TopicCluster(
            "technology",
            "Technology & Computing",
            Arrays.asList("computer", "code", "programming", "tech", "ai", "robot", "app", "website", "software"),
            Arrays.asList("computer", "code", "programming", "tech", "technology", "ai", "robot", "app", "software", "internet")
        );
        topicClusters.put("technology", technology);
        
        // Lifestyle cluster
        TopicCluster lifestyle = new TopicCluster(
            "lifestyle",
            "Lifestyle & Wellness",
            Arrays.asList("health", "fitness", "exercise", "diet", "sleep", "hobby", "sport", "activity"),
            Arrays.asList("health", "fitness", "exercise", "diet", "sleep", "hobby", "sport", "activity", "workout", "nutrition")
        );
        topicClusters.put("lifestyle", lifestyle);
        
        // Philosophy cluster
        TopicCluster philosophy = new TopicCluster(
            "philosophy",
            "Philosophy & Deep Thoughts",
            Arrays.asList("life", "purpose", "meaning", "think", "believe", "opinion", "idea"),
            Arrays.asList("life", "purpose", "meaning", "philosophy", "think", "believe", "opinion", "idea", "worldview", "values")
        );
        topicClusters.put("philosophy", philosophy);
        
        // Initialize keyword mappings
        for (TopicCluster cluster : topicClusters.values()) {
            for (String keyword : cluster.keywords) {
                topicKeywords.put(keyword.toLowerCase(), cluster.clusterId);
            }
            for (String topic : cluster.relatedTopics) {
                topicKeywords.put(topic.toLowerCase(), cluster.clusterId);
            }
        }
    }
    
    /**
     * Identifies topics in user input
     */
    public List<String> identifyTopics(String input) {
        List<String> topics = new ArrayList<>();
        if (input == null || input.isEmpty()) {
            return topics;
        }
        
        String lowerInput = input.toLowerCase();
        Set<String> foundClusters = new HashSet<>();
        
        // Check for direct keyword matches
        for (String keyword : topicKeywords.keySet()) {
            if (lowerInput.contains(keyword)) {
                String clusterId = topicKeywords.get(keyword);
                if (!foundClusters.contains(clusterId)) {
                    foundClusters.add(clusterId);
                    topics.add(clusterId);
                }
            }
        }
        
        // Check for pattern matches
        for (TopicCluster cluster : topicClusters.values()) {
            for (String pattern : cluster.keywords) {
                if (Pattern.compile("\\b" + pattern + "\\b", Pattern.CASE_INSENSITIVE).matcher(lowerInput).find()) {
                    if (!topics.contains(cluster.clusterId)) {
                        topics.add(cluster.clusterId);
                    }
                }
            }
        }
        
        return topics;
    }
    
    /**
     * Finds the most relevant topic cluster for input
     */
    public TopicCluster findRelevantCluster(String input) {
        List<String> topics = identifyTopics(input);
        
        if (topics.isEmpty()) {
            return null;
        }
        
        String bestTopic = topics.get(0);
        TopicCluster bestCluster = topicClusters.get(bestTopic);
        
        // Find cluster with highest relevance
        for (String topic : topics) {
            TopicCluster cluster = topicClusters.get(topic);
            if (cluster != null && cluster.getRelevanceScore() > bestCluster.getRelevanceScore()) {
                bestCluster = cluster;
                bestTopic = topic;
            }
        }
        
        return bestCluster;
    }
    
    /**
     * Activates a topic cluster
     */
    public void activateCluster(String clusterId) {
        TopicCluster cluster = topicClusters.get(clusterId);
        if (cluster != null) {
            cluster.activate();
            activeClusters.put(clusterId, cluster);
            
            // Maintain max history
            if (activeClusters.size() > maxClusterHistory) {
                String oldestKey = activeClusters.entrySet().stream()
                    .min(Comparator.comparingLong(e -> e.getValue().lastActiveTime))
                    .map(Map.Entry::getKey)
                    .orElse(null);
                if (oldestKey != null) {
                    activeClusters.remove(oldestKey);
                }
            }
        }
    }
    
    /**
     * Gets currently active topic clusters
     */
    public List<TopicCluster> getActiveClusters() {
        List<TopicCluster> clusters = new ArrayList<>(activeClusters.values());
        clusters.sort((a, b) -> Long.compare(b.lastActiveTime, a.lastActiveTime));
        return clusters;
    }
    
    /**
     * Gets suggested topic transitions based on current cluster
     */
    public List<TopicTransition> suggestTransitions(String currentClusterId) {
        List<TopicTransition> transitions = new ArrayList<>();
        TopicCluster current = topicClusters.get(currentClusterId);
        
        if (current == null) {
            return transitions;
        }
        
        // Find related clusters (excluding current)
        for (TopicCluster cluster : topicClusters.values()) {
            if (!cluster.clusterId.equals(currentClusterId)) {
                // Calculate relatedness based on shared keywords
                Set<String> currentKeywords = new HashSet<>(current.keywords);
                Set<String> clusterKeywords = new HashSet<>(cluster.keywords);
                
                int sharedKeywords = 0;
                for (String keyword : currentKeywords) {
                    if (clusterKeywords.contains(keyword)) {
                        sharedKeywords++;
                    }
                }
                
                double relevance = sharedKeywords * 0.1;
                
                if (sharedKeywords > 0) {
                    String reason = "Related topic based on shared interests";
                    transitions.add(new TopicTransition(currentClusterId, cluster.clusterId, reason, relevance));
                }
            }
        }
        
        // Sort by relevance
        transitions.sort((a, b) -> Double.compare(b.relevanceScore, a.relevanceScore));
        
        return transitions;
    }
    
    /**
     * Gets the dominant topic cluster from conversation history
     */
    public TopicCluster getDominantCluster(List<String> conversationHistory) {
        Map<String, Integer> clusterFrequency = new HashMap<>();
        
        for (String message : conversationHistory) {
            List<String> topics = identifyTopics(message);
            for (String topic : topics) {
                clusterFrequency.merge(topic, 1, Integer::sum);
            }
        }
        
        if (clusterFrequency.isEmpty()) {
            return topicClusters.get("general");
        }
        
        String dominantTopic = clusterFrequency.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("general");
        
        return topicClusters.get(dominantTopic);
    }
    
    /**
     * Calculates topic similarity between two inputs
     */
    public double calculateTopicSimilarity(String input1, String input2) {
        List<String> topics1 = identifyTopics(input1);
        List<String> topics2 = identifyTopics(input2);
        
        if (topics1.isEmpty() && topics2.isEmpty()) {
            return 0.5; // Neutral similarity
        }
        
        if (topics1.isEmpty() || topics2.isEmpty()) {
            return 0.2; // Low similarity
        }
        
        // Calculate Jaccard similarity
        Set<String> set1 = new HashSet<>(topics1);
        Set<String> set2 = new HashSet<>(topics2);
        
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        
        return (double) intersection.size() / union.size();
    }
    
    /**
     * Gets all available topic clusters
     */
    public List<TopicCluster> getAllClusters() {
        return new ArrayList<>(topicClusters.values());
    }
    
    /**
     * Gets a specific cluster by ID
     */
    public TopicCluster getCluster(String clusterId) {
        return topicClusters.get(clusterId);
    }
    
    /**
     * Gets topic cluster information for display
     */
    public String getClusterInfo(String clusterId) {
        TopicCluster cluster = topicClusters.get(clusterId);
        if (cluster == null) {
            return "Unknown topic";
        }
        
        return String.format("Topic: %s\nKeywords: %s\nRelated: %s\nRelevance: %.2f\nVisits: %d",
            cluster.clusterName,
            String.join(", ", cluster.keywords.stream().limit(5).collect(Collectors.toList())),
            String.join(", ", cluster.relatedTopics.stream().limit(5).collect(Collectors.toList())),
            cluster.getRelevanceScore(),
            cluster.visitCount
        );
    }
    
    /**
     * Resets cluster statistics
     */
    public void resetClusters() {
        for (TopicCluster cluster : topicClusters.values()) {
            cluster.visitCount = 0;
            cluster.lastActiveTime = 0;
            cluster.relevanceScore = 0.5;
        }
        activeClusters.clear();
    }
    
    /**
     * Gets cluster statistics
     */
    public Map<String, Integer> getClusterStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        for (TopicCluster cluster : topicClusters.values()) {
            stats.put(cluster.clusterName, cluster.visitCount);
        }
        return stats;
    }
}

