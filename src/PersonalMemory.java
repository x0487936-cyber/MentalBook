import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * PersonalMemory - Manages personal user information and memories
 * Stores user profile data, preferences, interests, experiences, and relationships
 * Works with the ContextEngine's MemoryTier system for persistence
 */
public class PersonalMemory {

    // ==================== INNER CLASSES ====================

    /**
     * Represents a personal memory entry
     */
    public static class Memory {
        private String memoryId;
        private String content;
        private String category;
        private LocalDateTime createdAt;
        private LocalDateTime lastAccessed;
        private int accessCount;
        private Map<String, Object> metadata;
        private boolean isImportant;
        private Set<String> emotions;

        public Memory(String content, String category) {
            this.memoryId = UUID.randomUUID().toString();
            this.content = content;
            this.category = category;
            this.createdAt = LocalDateTime.now();
            this.lastAccessed = LocalDateTime.now();
            this.accessCount = 0;
            this.metadata = new HashMap<>();
            this.isImportant = false;
            this.emotions = new HashSet<>();
        }

        public String getMemoryId() { return memoryId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getLastAccessed() { return lastAccessed; }
        public int getAccessCount() { return accessCount; }
        public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
        public boolean isImportant() { return isImportant; }
        public void setImportant(boolean important) { this.isImportant = important; }
        public Set<String> getEmotions() { return new HashSet<>(emotions); }

        public void addEmotion(String emotion) {
            emotions.add(emotion.toLowerCase());
        }

        public void setMetadata(String key, Object value) {
            metadata.put(key, value);
        }

        public Object getMetadata(String key) {
            return metadata.get(key);
        }

        public void access() {
            this.lastAccessed = LocalDateTime.now();
            this.accessCount++;
        }

        public double getSalienceScore() {
            double recency = 1.0 / (1.0 + java.time.Duration.between(lastAccessed, LocalDateTime.now()).toMinutes());
            double importance = isImportant ? 1.0 : 0.3;
            double frequency = Math.min(1.0, accessCount / 10.0);
            return (recency * 0.4) + (importance * 0.4) + (frequency * 0.2);
        }

        @Override
        public String toString() {
            return String.format("Memory{id='%s', category='%s', important=%b, salience=%.2f}",
                memoryId, category, isImportant, getSalienceScore());
        }
    }

    /**
     * User profile information
     */
    public static class UserProfile {
        private String userId;
        
        // Name and basics
        private String name;
        private int age;
        private String gender;
        private String location;
        private String occupation;
        private String education;
        private String maritalStatus;
        
        // Life circumstances
        private String livingSituation;
        private String familyStatus;
        private String financialSituation;
        private String healthStatus;
        private List<String> lifeGoals;
        private List<String> challenges;
        private Map<String, String> lifeEvents; // key -> description
        
        // Communication
        private LocalDateTime firstSeen;
        private LocalDateTime lastSeen;
        private int totalConversations;
        private String communicationStyle;
        
        // Preferences and interests
        private Map<String, Object> preferences;
        private Set<String> interests;
        private Set<String> hobbies;
        private Set<String> favoriteThings;
        private Set<String> dislikedThings;
        
        // Relationships
        private Map<String, String> relationships;
        
        // Additional
        private Map<String, Object> customFields;

        public UserProfile(String userId) {
            this.userId = userId;
            this.firstSeen = LocalDateTime.now();
            this.lastSeen = LocalDateTime.now();
            this.totalConversations = 0;
            this.preferences = new HashMap<>();
            this.interests = new HashSet<>();
            this.hobbies = new HashSet<>();
            this.favoriteThings = new HashSet<>();
            this.dislikedThings = new HashSet<>();
            this.relationships = new HashMap<>();
            this.lifeGoals = new ArrayList<>();
            this.challenges = new ArrayList<>();
            this.lifeEvents = new HashMap<>();
            this.customFields = new HashMap<>();
        }

        // Basic getters and setters
        public String getUserId() { return userId; }
        
        // Name and basics
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getOccupation() { return occupation; }
        public void setOccupation(String occupation) { this.occupation = occupation; }
        public String getEducation() { return education; }
        public void setEducation(String education) { this.education = education; }
        public String getMaritalStatus() { return maritalStatus; }
        public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }
        
        // Life circumstances getters and setters
        public String getLivingSituation() { return livingSituation; }
        public void setLivingSituation(String livingSituation) { this.livingSituation = livingSituation; }
        public String getFamilyStatus() { return familyStatus; }
        public void setFamilyStatus(String familyStatus) { this.familyStatus = familyStatus; }
        public String getFinancialSituation() { return financialSituation; }
        public void setFinancialSituation(String financialSituation) { this.financialSituation = financialSituation; }
        public String getHealthStatus() { return healthStatus; }
        public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
        public List<String> getLifeGoals() { return new ArrayList<>(lifeGoals); }
        public void addLifeGoal(String goal) { this.lifeGoals.add(goal); }
        public void removeLifeGoal(String goal) { this.lifeGoals.remove(goal); }
        public List<String> getChallenges() { return new ArrayList<>(challenges); }
        public void addChallenge(String challenge) { this.challenges.add(challenge); }
        public void removeChallenge(String challenge) { this.challenges.remove(challenge); }
        public Map<String, String> getLifeEvents() { return new HashMap<>(lifeEvents); }
        public void addLifeEvent(String key, String description) { this.lifeEvents.put(key, description); }
        
        // Communication
        public LocalDateTime getFirstSeen() { return firstSeen; }
        public LocalDateTime getLastSeen() { return lastSeen; }
        public void updateLastSeen() { this.lastSeen = LocalDateTime.now(); }
        public int getTotalConversations() { return totalConversations; }
        public void incrementConversations() { this.totalConversations++; }
        public String getCommunicationStyle() { return communicationStyle; }
        public void setCommunicationStyle(String style) { this.communicationStyle = style; }
        
        // Preferences
        public Map<String, Object> getPreferences() { return new HashMap<>(preferences); }
        public void setPreference(String key, Object value) { preferences.put(key, value); }
        public Object getPreference(String key) { return preferences.get(key); }
        
        // Interests and hobbies
        public Set<String> getInterests() { return new HashSet<>(interests); }
        public void addInterest(String interest) { interests.add(interest.toLowerCase()); }
        public void removeInterest(String interest) { interests.remove(interest.toLowerCase()); }
        public Set<String> getHobbies() { return new HashSet<>(hobbies); }
        public void addHobby(String hobby) { hobbies.add(hobby.toLowerCase()); }
        public void removeHobby(String hobby) { hobbies.remove(hobby.toLowerCase()); }
        
        // Favorite/disliked things
        public Set<String> getFavoriteThings() { return new HashSet<>(favoriteThings); }
        public void addFavorite(String thing) { favoriteThings.add(thing.toLowerCase()); }
        public void removeFavorite(String thing) { favoriteThings.remove(thing.toLowerCase()); }
        public Set<String> getDislikedThings() { return new HashSet<>(dislikedThings); }
        public void addDisliked(String thing) { dislikedThings.add(thing.toLowerCase()); }
        public void removeDisliked(String thing) { dislikedThings.remove(thing.toLowerCase()); }
        
        // Relationships
        public Map<String, String> getRelationships() { return new HashMap<>(relationships); }
        public void addRelationship(String person, String relationship) { relationships.put(person, relationship); }
        public String getRelationship(String person) { return relationships.get(person); }
        public void removeRelationship(String person) { relationships.remove(person); }
        
        // Custom fields
        public Map<String, Object> getCustomFields() { return new HashMap<>(customFields); }
        public void setCustomField(String key, Object value) { customFields.put(key, value); }
        public Object getCustomField(String key) { return customFields.get(key); }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            
            // Name and basics
            map.put("name", name);
            map.put("age", age);
            map.put("gender", gender);
            map.put("location", location);
            map.put("occupation", occupation);
            map.put("education", education);
            map.put("maritalStatus", maritalStatus);
            
            // Life circumstances
            map.put("livingSituation", livingSituation);
            map.put("familyStatus", familyStatus);
            map.put("financialSituation", financialSituation);
            map.put("healthStatus", healthStatus);
            map.put("lifeGoals", lifeGoals);
            map.put("challenges", challenges);
            map.put("lifeEvents", lifeEvents);
            
            // Communication
            map.put("firstSeen", firstSeen.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            map.put("lastSeen", lastSeen.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            map.put("totalConversations", totalConversations);
            map.put("communicationStyle", communicationStyle);
            
            // Interests and hobbies
            map.put("interests", interests);
            map.put("hobbies", hobbies);
            map.put("favoriteThings", favoriteThings);
            map.put("dislikedThings", dislikedThings);
            
            // Relationships
            map.put("relationships", relationships);
            
            // Preferences
            map.put("preferences", preferences);
            
            return map;
        }
    }

    /**
     * Experience record - tracks significant experiences shared by user
     */
    public static class Experience {
        private String experienceId;
        private String title;
        private String description;
        private String category;
        private LocalDateTime occurredAt;
        private LocalDateTime sharedAt;
        private Map<String, Object> details;
        private Set<String> emotions;
        private boolean isPositive;
        private int significance; // 1-10

        public Experience(String title, String description, String category) {
            this.experienceId = UUID.randomUUID().toString();
            this.title = title;
            this.description = description;
            this.category = category;
            this.occurredAt = LocalDateTime.now();
            this.sharedAt = LocalDateTime.now();
            this.details = new HashMap<>();
            this.emotions = new HashSet<>();
            this.isPositive = true;
            this.significance = 5;
        }

        public String getExperienceId() { return experienceId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public LocalDateTime getOccurredAt() { return occurredAt; }
        public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
        public LocalDateTime getSharedAt() { return sharedAt; }
        public Map<String, Object> getDetails() { return new HashMap<>(details); }
        public Set<String> getEmotions() { return new HashSet<>(emotions); }
        public boolean isPositive() { return isPositive; }
        public void setPositive(boolean positive) { this.isPositive = positive; }
        public int getSignificance() { return significance; }
        public void setSignificance(int significance) { this.significance = Math.max(1, Math.min(10, significance)); }

        public void addEmotion(String emotion) {
            emotions.add(emotion.toLowerCase());
        }

        public void addDetail(String key, Object value) {
            details.put(key, value);
        }

        @Override
        public String toString() {
            return String.format("Experience{title='%s', category='%s', significance=%d}",
                title, category, significance);
        }
    }

    // ==================== MEMORY CATEGORIES ====================

    public static final String CATEGORY_PERSONAL = "personal";
    public static final String CATEGORY_RELATIONSHIP = "relationship";
    public static final String CATEGORY_CAREER = "career";
    public static final String CATEGORY_EDUCATION = "education";
    public static final String CATEGORY_HEALTH = "health";
    public static final String CATEGORY_EMOTIONAL = "emotional";
    public static final String CATEGORY_GOALS = "goals";
    public static final String CATEGORY_MEMORIES = "memories";
    public static final String CATEGORY_PREFERENCES = "preferences";
    public static final String CATEGORY_INTERESTS = "interests";
    public static final String CATEGORY_TOPIC = "topic";
    public static final String CATEGORY_PROBLEM = "problem";
    public static final String CATEGORY_UPDATE = "update";

    // ==================== CONVERSATION MEMORY ====================
    
    /**
     * ConversationMemory - Tracks what was discussed in conversations
     */
    public static class ConversationMemory {
        private String conversationId;
        private LocalDateTime timestamp;
        private Set<String> keyTopics;
        private List<String> problemsMentioned;
        private List<String> goalsShared;
        private List<String> updatesReceived;
        private Map<String, Object> metadata;
        private int discussionDepth; // How much was discussed
        
        public ConversationMemory() {
            this.conversationId = UUID.randomUUID().toString();
            this.timestamp = LocalDateTime.now();
            this.keyTopics = new LinkedHashSet<>();
            this.problemsMentioned = new ArrayList<>();
            this.goalsShared = new ArrayList<>();
            this.updatesReceived = new ArrayList<>();
            this.metadata = new HashMap<>();
            this.discussionDepth = 0;
        }
        
        public ConversationMemory(String conversationId) {
            this.conversationId = conversationId;
            this.timestamp = LocalDateTime.now();
            this.keyTopics = new LinkedHashSet<>();
            this.problemsMentioned = new ArrayList<>();
            this.goalsShared = new ArrayList<>();
            this.updatesReceived = new ArrayList<>();
            this.metadata = new HashMap<>();
            this.discussionDepth = 0;
        }
        
        public String getConversationId() { return conversationId; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Set<String> getKeyTopics() { return new LinkedHashSet<>(keyTopics); }
        public List<String> getProblemsMentioned() { return new ArrayList<>(problemsMentioned); }
        public List<String> getGoalsShared() { return new ArrayList<>(goalsShared); }
        public List<String> getUpdatesReceived() { return new ArrayList<>(updatesReceived); }
        public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
        public int getDiscussionDepth() { return discussionDepth; }
        
        public void addTopic(String topic) {
            if (topic != null && !topic.trim().isEmpty()) {
                keyTopics.add(topic.toLowerCase().trim());
                discussionDepth++;
            }
        }
        
        public void addTopics(Collection<String> topics) {
            for (String topic : topics) {
                addTopic(topic);
            }
        }
        
        public void addProblem(String problem) {
            if (problem != null && !problem.trim().isEmpty()) {
                problemsMentioned.add(problem);
                discussionDepth++;
            }
        }
        
        public void addGoal(String goal) {
            if (goal != null && !goal.trim().isEmpty()) {
                goalsShared.add(goal);
                discussionDepth++;
            }
        }
        
        public void addUpdate(String update) {
            if (update != null && !update.trim().isEmpty()) {
                updatesReceived.add(update);
                discussionDepth++;
            }
        }
        
        public void setMetadata(String key, Object value) {
            metadata.put(key, value);
        }
        
        public Object getMetadata(String key) {
            return metadata.get(key);
        }
        
        public boolean hasTopic(String topic) {
            return keyTopics.contains(topic.toLowerCase());
        }
        
        public boolean hasProblem(String problem) {
            return problemsMentioned.stream()
                .anyMatch(p -> p.toLowerCase().contains(problem.toLowerCase()));
        }
        
        public boolean hasGoal(String goal) {
            return goalsShared.stream()
                .anyMatch(g -> g.toLowerCase().contains(goal.toLowerCase()));
        }
        
        @Override
        public String toString() {
            return String.format("ConversationMemory{topics=%d, problems=%d, goals=%d, updates=%d}",
                keyTopics.size(), problemsMentioned.size(), goalsShared.size(), updatesReceived.size());
        }
    }
    
    // ==================== TOPIC TRACKING ====================
    
    /**
     * TrackedTopic - Keeps track of topics discussed over time
     */
    public static class TrackedTopic {
        private String topicName;
        private int mentionCount;
        private int discussionCount;
        private LocalDateTime firstMention;
        private LocalDateTime lastMention;
        private double importanceScore;
        private List<String> relatedKeywords;
        private boolean isOngoing;
        
        public TrackedTopic(String topicName) {
            this.topicName = topicName.toLowerCase();
            this.mentionCount = 0;
            this.discussionCount = 0;
            this.firstMention = LocalDateTime.now();
            this.lastMention = LocalDateTime.now();
            this.importanceScore = 0.0;
            this.relatedKeywords = new ArrayList<>();
            this.isOngoing = false;
        }
        
        public String getTopicName() { return topicName; }
        public int getMentionCount() { return mentionCount; }
        public int getDiscussionCount() { return discussionCount; }
        public LocalDateTime getFirstMention() { return firstMention; }
        public LocalDateTime getLastMention() { return lastMention; }
        public double getImportanceScore() { return importanceScore; }
        public List<String> getRelatedKeywords() { return new ArrayList<>(relatedKeywords); }
        public boolean isOngoing() { return isOngoing; }
        
        public void incrementMention() {
            mentionCount++;
            lastMention = LocalDateTime.now();
            updateImportance();
        }
        
        public void incrementDiscussion() {
            discussionCount++;
            lastMention = LocalDateTime.now();
            isOngoing = true;
        }
        
        public void markResolved() {
            isOngoing = false;
        }
        
        public void addRelatedKeyword(String keyword) {
            if (keyword != null && !relatedKeywords.contains(keyword.toLowerCase())) {
                relatedKeywords.add(keyword.toLowerCase());
            }
        }
        
        private void updateImportance() {
            long hoursSinceFirst = java.time.Duration.between(firstMention, LocalDateTime.now()).toHours();
            double recency = 1.0 / (1.0 + hoursSinceFirst / 24.0);
            importanceScore = (mentionCount * 0.3) + (discussionCount * 0.4) + (recency * 0.3);
        }
        
        public double getRecency() {
            long hoursSinceLast = java.time.Duration.between(lastMention, LocalDateTime.now()).toHours();
            return 1.0 / (1.0 + hoursSinceLast);
        }
    }

    // ==================== MAIN PERSONAL MEMORY CLASS ====================

    private String currentUserId;
    private UserProfile currentProfile;
    private Map<String, UserProfile> userProfiles;
    private Map<String, List<Memory>> memoriesByUser;
    private Map<String, List<Experience>> experiencesByUser;
    private Map<String, Set<String>> topicInterests; // userId -> topics
    private Map<String, Map<String, Integer>> emotionalHistory; // userId -> emotion -> count
    private Map<String, LocalDateTime> lastInteraction;
    private int maxMemoriesPerUser;
    private int maxExperiencesPerUser;
    
    // Conversation Memory - tracks what was discussed
    private Map<String, List<ConversationMemory>> conversationMemoriesByUser;
    private Map<String, Map<String, TrackedTopic>> trackedTopicsByUser;
    private String currentConversationId;

    public PersonalMemory() {
        this.currentUserId = null;
        this.currentProfile = null;
        this.userProfiles = new HashMap<>();
        this.memoriesByUser = new HashMap<>();
        this.experiencesByUser = new HashMap<>();
        this.topicInterests = new HashMap<>();
        this.emotionalHistory = new HashMap<>();
        this.lastInteraction = new HashMap<>();
        this.maxMemoriesPerUser = 100;
        this.maxExperiencesPerUser = 50;
        this.conversationMemoriesByUser = new HashMap<>();
        this.trackedTopicsByUser = new HashMap<>();
        this.currentConversationId = null;
    }

    // ==================== USER MANAGEMENT ====================

    /**
     * Set the current user for memory operations
     */
    public void setCurrentUser(String userId) {
        this.currentUserId = userId;
        this.currentProfile = userProfiles.computeIfAbsent(userId, k -> new UserProfile(k));
        memoriesByUser.computeIfAbsent(userId, k -> new ArrayList<>());
        experiencesByUser.computeIfAbsent(userId, k -> new ArrayList<>());
        topicInterests.computeIfAbsent(userId, k -> new HashSet<>());
        emotionalHistory.computeIfAbsent(userId, k -> new HashMap<>());
        lastInteraction.put(userId, LocalDateTime.now());
    }

    /**
     * Get current user ID
     */
    public String getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Get current user profile
     */
    public UserProfile getCurrentProfile() {
        return currentProfile;
    }

    /**
     * Get user profile by ID
     */
    public UserProfile getUserProfile(String userId) {
        return userProfiles.get(userId);
    }

    /**
     * Update user profile
     */
    public void updateProfile(String userId, String name, int age, String gender, String location) {
        UserProfile profile = userProfiles.computeIfAbsent(userId, k -> new UserProfile(k));
        if (name != null) profile.setName(name);
        if (age > 0) profile.setAge(age);
        if (gender != null) profile.setGender(gender);
        if (location != null) profile.setLocation(location);
        
        if (userId.equals(currentUserId)) {
            this.currentProfile = profile;
        }
    }

    /**
     * Record a new conversation
     */
    public void recordConversation(String userId) {
        UserProfile profile = userProfiles.computeIfAbsent(userId, k -> new UserProfile(k));
        profile.incrementConversations();
        profile.updateLastSeen();
        lastInteraction.put(userId, LocalDateTime.now());
        
        if (userId.equals(currentUserId)) {
            this.currentProfile = profile;
        }
    }

    // ==================== MEMORY MANAGEMENT ====================

    /**
     * Add a new memory for the current user
     */
    public Memory addMemory(String content, String category) {
        return addMemory(currentUserId, content, category);
    }

    /**
     * Add a new memory for a specific user
     */
    public Memory addMemory(String userId, String content, String category) {
        if (userId == null || content == null || content.trim().isEmpty()) {
            return null;
        }

        Memory memory = new Memory(content, category);
        List<Memory> userMemories = memoriesByUser.computeIfAbsent(userId, k -> new ArrayList<>());
        userMemories.add(memory);

        // Enforce memory limit
        while (userMemories.size() > maxMemoriesPerUser) {
            userMemories.remove(0);
        }

        return memory;
    }

    /**
     * Get all memories for current user
     */
    public List<Memory> getAllMemories() {
        return getAllMemories(currentUserId);
    }

    /**
     * Get all memories for a specific user
     */
    public List<Memory> getAllMemories(String userId) {
        List<Memory> memories = memoriesByUser.get(userId);
        return memories != null ? new ArrayList<>(memories) : new ArrayList<>();
    }

    /**
     * Get memories by category
     */
    public List<Memory> getMemoriesByCategory(String category) {
        return getMemoriesByCategory(currentUserId, category);
    }

    /**
     * Get memories by category for specific user
     */
    public List<Memory> getMemoriesByCategory(String userId, String category) {
        List<Memory> userMemories = memoriesByUser.get(userId);
        if (userMemories == null) return new ArrayList<>();
        
        return userMemories.stream()
            .filter(m -> m.getCategory().equalsIgnoreCase(category))
            .toList();
    }

    /**
     * Get important memories
     */
    public List<Memory> getImportantMemories() {
        return getImportantMemories(currentUserId);
    }

    /**
     * Get important memories for specific user
     */
    public List<Memory> getImportantMemories(String userId) {
        List<Memory> userMemories = memoriesByUser.get(userId);
        if (userMemories == null) return new ArrayList<>();
        
        return userMemories.stream()
            .filter(Memory::isImportant)
            .sorted((a, b) -> Double.compare(b.getSalienceScore(), a.getSalienceScore()))
            .toList();
    }

    /**
     * Get most salient memories (by salience score)
     */
    public List<Memory> getSalientMemories(int limit) {
        return getSalientMemories(currentUserId, limit);
    }

    /**
     * Get most salient memories for specific user
     */
    public List<Memory> getSalientMemories(String userId, int limit) {
        List<Memory> userMemories = memoriesByUser.get(userId);
        if (userMemories == null) return new ArrayList<>();
        
        return userMemories.stream()
            .sorted((a, b) -> Double.compare(b.getSalienceScore(), a.getSalienceScore()))
            .limit(limit)
            .toList();
    }

    /**
     * Search memories by content
     */
    public List<Memory> searchMemories(String query) {
        return searchMemories(currentUserId, query);
    }

    /**
     * Search memories by content for specific user
     */
    public List<Memory> searchMemories(String userId, String query) {
        List<Memory> userMemories = memoriesByUser.get(userId);
        if (userMemories == null || query == null) return new ArrayList<>();
        
        String lowerQuery = query.toLowerCase();
        return userMemories.stream()
            .filter(m -> m.getContent().toLowerCase().contains(lowerQuery))
            .toList();
    }

    /**
     * Mark a memory as important
     */
    public void markMemoryImportant(String memoryId, boolean important) {
        List<Memory> userMemories = memoriesByUser.get(currentUserId);
        if (userMemories == null) return;
        
        for (Memory memory : userMemories) {
            if (memory.getMemoryId().equals(memoryId)) {
                memory.setImportant(important);
                break;
            }
        }
    }

    /**
     * Get memory by ID
     */
    public Memory getMemory(String memoryId) {
        return getMemory(currentUserId, memoryId);
    }

    /**
     * Get memory by ID for specific user
     */
    public Memory getMemory(String userId, String memoryId) {
        List<Memory> userMemories = memoriesByUser.get(userId);
        if (userMemories == null) return null;
        
        for (Memory memory : userMemories) {
            if (memory.getMemoryId().equals(memoryId)) {
                memory.access();
                return memory;
            }
        }
        return null;
    }

    // ==================== EXPERIENCE MANAGEMENT ====================

    /**
     * Add a new experience
     */
    public Experience addExperience(String title, String description, String category) {
        return addExperience(currentUserId, title, description, category);
    }

    /**
     * Add a new experience for specific user
     */
    public Experience addExperience(String userId, String title, String description, String category) {
        if (userId == null || title == null || title.trim().isEmpty()) {
            return null;
        }

        Experience experience = new Experience(title, description, category);
        List<Experience> userExperiences = experiencesByUser.computeIfAbsent(userId, k -> new ArrayList<>());
        userExperiences.add(experience);

        // Enforce experience limit
        while (userExperiences.size() > maxExperiencesPerUser) {
            userExperiences.remove(0);
        }

        return experience;
    }

    /**
     * Get all experiences
     */
    public List<Experience> getAllExperiences() {
        return getAllExperiences(currentUserId);
    }

    /**
     * Get all experiences for specific user
     */
    public List<Experience> getAllExperiences(String userId) {
        List<Experience> experiences = experiencesByUser.get(userId);
        return experiences != null ? new ArrayList<>(experiences) : new ArrayList<>();
    }

    /**
     * Get experiences by category
     */
    public List<Experience> getExperiencesByCategory(String category) {
        return getExperiencesByCategory(currentUserId, category);
    }

    /**
     * Get experiences by category for specific user
     */
    public List<Experience> getExperiencesByCategory(String userId, String category) {
        List<Experience> userExperiences = experiencesByUser.get(userId);
        if (userExperiences == null) return new ArrayList<>();
        
        return userExperiences.stream()
            .filter(e -> e.getCategory().equalsIgnoreCase(category))
            .sorted((a, b) -> Integer.compare(b.getSignificance(), a.getSignificance()))
            .toList();
    }

    /**
     * Get significant experiences
     */
    public List<Experience> getSignificantExperiences(int minSignificance) {
        return getSignificantExperiences(currentUserId, minSignificance);
    }

    /**
     * Get significant experiences for specific user
     */
    public List<Experience> getSignificantExperiences(String userId, int minSignificance) {
        List<Experience> userExperiences = experiencesByUser.get(userId);
        if (userExperiences == null) return new ArrayList<>();
        
        return userExperiences.stream()
            .filter(e -> e.getSignificance() >= minSignificance)
            .sorted((a, b) -> Integer.compare(b.getSignificance(), a.getSignificance()))
            .toList();
    }

    // ==================== INTEREST AND TOPIC TRACKING ====================

    /**
     * Add an interest for the current user
     */
    public void addInterest(String interest) {
        addInterest(currentUserId, interest);
    }

    /**
     * Add an interest for specific user
     */
    public void addInterest(String userId, String interest) {
        if (interest != null && !interest.trim().isEmpty()) {
            Set<String> interests = topicInterests.computeIfAbsent(userId, k -> new HashSet<>());
            interests.add(interest.toLowerCase());
            
            // Also add as a memory
            addMemory(userId, "Interested in: " + interest, CATEGORY_INTERESTS);
        }
    }

    /**
     * Get all interests for current user
     */
    public Set<String> getInterests() {
        return getInterests(currentUserId);
    }

    /**
     * Get all interests for specific user
     */
    public Set<String> getInterests(String userId) {
        Set<String> interests = topicInterests.get(userId);
        return interests != null ? new HashSet<>(interests) : new HashSet<>();
    }

    /**
     * Add multiple interests
     */
    public void addInterests(Collection<String> interests) {
        for (String interest : interests) {
            addInterest(interest);
        }
    }

    /**
     * Check if user has a specific interest
     */
    public boolean hasInterest(String interest) {
        return hasInterest(currentUserId, interest);
    }

    /**
     * Check if user has a specific interest
     */
    public boolean hasInterest(String userId, String interest) {
        Set<String> interests = topicInterests.get(userId);
        return interests != null && interests.contains(interest.toLowerCase());
    }

    // ==================== EMOTIONAL TRACKING ====================

    /**
     * Record an emotion for the current user
     */
    public void recordEmotion(String emotion) {
        recordEmotion(currentUserId, emotion);
    }

    /**
     * Record an emotion for specific user
     */
    public void recordEmotion(String userId, String emotion) {
        if (emotion == null || emotion.trim().isEmpty()) return;
        
        Map<String, Integer> emotions = emotionalHistory.computeIfAbsent(userId, k -> new HashMap<>());
        emotions.merge(emotion.toLowerCase(), 1, Integer::sum);
    }

    /**
     * Get emotional history for current user
     */
    public Map<String, Integer> getEmotionalHistory() {
        return getEmotionalHistory(currentUserId);
    }

    /**
     * Get emotional history for specific user
     */
    public Map<String, Integer> getEmotionalHistory(String userId) {
        Map<String, Integer> emotions = emotionalHistory.get(userId);
        return emotions != null ? new HashMap<>(emotions) : new HashMap<>();
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
        Map<String, Integer> emotions = emotionalHistory.get(userId);
        if (emotions == null || emotions.isEmpty()) return null;
        
        return emotions.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    // ==================== PREFERENCE MANAGEMENT ====================

    /**
     * Set a preference for current user
     */
    public void setPreference(String key, Object value) {
        setPreference(currentUserId, key, value);
    }

    /**
     * Set a preference for specific user
     */
    public void setPreference(String userId, String key, Object value) {
        UserProfile profile = userProfiles.computeIfAbsent(userId, k -> new UserProfile(k));
        profile.setPreference(key, value);
        
        // Also store as memory
        addMemory(userId, "Preference set: " + key + " = " + value, CATEGORY_PREFERENCES);
    }

    /**
     * Get a preference for current user
     */
    public Object getPreference(String key) {
        return getPreference(currentUserId, key);
    }

    /**
     * Get a preference for specific user
     */
    public Object getPreference(String userId, String key) {
        UserProfile profile = userProfiles.get(userId);
        return profile != null ? profile.getPreference(key) : null;
    }

    /**
     * Get all preferences for current user
     */
    public Map<String, Object> getAllPreferences() {
        return getAllPreferences(currentUserId);
    }

    /**
     * Get all preferences for specific user
     */
    public Map<String, Object> getAllPreferences(String userId) {
        UserProfile profile = userProfiles.get(userId);
        return profile != null ? profile.getPreferences() : new HashMap<>();
    }

    // ==================== RELATIONSHIP MANAGEMENT ====================

    /**
     * Add a relationship for current user
     */
    public void addRelationship(String person, String relationship) {
        addRelationship(currentUserId, person, relationship);
    }

    /**
     * Add a relationship for specific user
     */
    public void addRelationship(String userId, String person, String relationship) {
        UserProfile profile = userProfiles.computeIfAbsent(userId, k -> new UserProfile(k));
        profile.addRelationship(person, relationship);
        
        addMemory(userId, "Relationship: " + person + " is my " + relationship, CATEGORY_RELATIONSHIP);
    }

    /**
     * Get relationship for current user
     */
    public String getRelationship(String person) {
        return getRelationship(currentUserId, person);
    }

    /**
     * Get relationship for specific user
     */
    public String getRelationship(String userId, String person) {
        UserProfile profile = userProfiles.get(userId);
        return profile != null ? profile.getRelationship(person) : null;
    }

    // ==================== CONVERSATION MEMORY MANAGEMENT ====================

    /**
     * Start a new conversation session
     */
    public String startConversation() {
        return startConversation(currentUserId);
    }

    /**
     * Start a new conversation session for specific user
     */
    public String startConversation(String userId) {
        if (userId == null) return null;
        
        currentConversationId = UUID.randomUUID().toString();
        List<ConversationMemory> conversations = conversationMemoriesByUser.computeIfAbsent(userId, k -> new ArrayList<>());
        
        ConversationMemory conversation = new ConversationMemory(currentConversationId);
        conversations.add(conversation);
        
        return currentConversationId;
    }

    /**
     * Get current conversation ID
     */
    public String getCurrentConversationId() {
        return currentConversationId;
    }

    /**
     * Record a topic discussed in current conversation
     */
    public void recordTopic(String topic) {
        recordTopic(currentUserId, topic);
    }

    /**
     * Record a topic for specific user
     */
    public void recordTopic(String userId, String topic) {
        if (userId == null || topic == null || topic.trim().isEmpty()) return;
        
        // Add to current conversation
        List<ConversationMemory> conversations = conversationMemoriesByUser.get(userId);
        if (conversations != null && !conversations.isEmpty()) {
            conversations.get(conversations.size() - 1).addTopic(topic);
        }
        
        // Track topic over time
        Map<String, TrackedTopic> topics = trackedTopicsByUser.computeIfAbsent(userId, k -> new HashMap<>());
        String lowerTopic = topic.toLowerCase();
        TrackedTopic tracked = topics.get(lowerTopic);
        if (tracked == null) {
            tracked = new TrackedTopic(topic);
            topics.put(lowerTopic, tracked);
        }
        tracked.incrementMention();
    }

    /**
     * Record multiple topics at once
     */
    public void recordTopics(Collection<String> topics) {
        for (String topic : topics) {
            recordTopic(topic);
        }
    }

    /**
     * Record a problem mentioned in conversation
     */
    public void recordProblem(String problem) {
        recordProblem(currentUserId, problem);
    }

    /**
     * Record a problem for specific user
     */
    public void recordProblem(String userId, String problem) {
        if (userId == null || problem == null || problem.trim().isEmpty()) return;
        
        List<ConversationMemory> conversations = conversationMemoriesByUser.get(userId);
        if (conversations != null && !conversations.isEmpty()) {
            conversations.get(conversations.size() - 1).addProblem(problem);
        }
        
        // Also store as memory
        addMemory(userId, "Problem: " + problem, CATEGORY_PROBLEM);
    }

    /**
     * Record a goal shared in conversation
     */
    public void recordGoal(String goal) {
        recordGoal(currentUserId, goal);
    }

    /**
     * Record a goal for specific user
     */
    public void recordGoal(String userId, String goal) {
        if (userId == null || goal == null || goal.trim().isEmpty()) return;
        
        List<ConversationMemory> conversations = conversationMemoriesByUser.get(userId);
        if (conversations != null && !conversations.isEmpty()) {
            conversations.get(conversations.size() - 1).addGoal(goal);
        }
        
        // Add to user profile goals
        UserProfile profile = userProfiles.get(userId);
        if (profile != null) {
            profile.addLifeGoal(goal);
        }
        
        // Also store as memory
        addMemory(userId, "Goal: " + goal, CATEGORY_GOALS);
    }

    /**
     * Record an update in conversation
     */
    public void recordUpdate(String update) {
        recordUpdate(currentUserId, update);
    }

    /**
     * Record an update for specific user
     */
    public void recordUpdate(String userId, String update) {
        if (userId == null || update == null || update.trim().isEmpty()) return;
        
        List<ConversationMemory> conversations = conversationMemoriesByUser.get(userId);
        if (conversations != null && !conversations.isEmpty()) {
            conversations.get(conversations.size() - 1).addUpdate(update);
        }
        
        // Also store as memory
        addMemory(userId, "Update: " + update, CATEGORY_UPDATE);
    }

    /**
     * Get all conversation memories for current user
     */
    public List<ConversationMemory> getConversationMemories() {
        return getConversationMemories(currentUserId);
    }

    /**
     * Get all conversation memories for specific user
     */
    public List<ConversationMemory> getConversationMemories(String userId) {
        List<ConversationMemory> conversations = conversationMemoriesByUser.get(userId);
        return conversations != null ? new ArrayList<>(conversations) : new ArrayList<>();
    }

    /**
     * Get the most recent conversation
     */
    public ConversationMemory getLastConversation() {
        return getLastConversation(currentUserId);
    }

    /**
     * Get the most recent conversation for specific user
     */
    public ConversationMemory getLastConversation(String userId) {
        List<ConversationMemory> conversations = conversationMemoriesByUser.get(userId);
        if (conversations == null || conversations.isEmpty()) return null;
        return conversations.get(conversations.size() - 1);
    }

    /**
     * Get all key topics discussed across conversations
     */
    public Set<String> getAllDiscussedTopics() {
        return getAllDiscussedTopics(currentUserId);
    }

    /**
     * Get all key topics for specific user
     */
    public Set<String> getAllDiscussedTopics(String userId) {
        Set<String> allTopics = new LinkedHashSet<>();
        List<ConversationMemory> conversations = conversationMemoriesByUser.get(userId);
        if (conversations != null) {
            for (ConversationMemory conv : conversations) {
                allTopics.addAll(conv.getKeyTopics());
            }
        }
        return allTopics;
    }

    /**
     * Get all problems mentioned
     */
    public List<String> getAllProblemsMentioned() {
        return getAllProblemsMentioned(currentUserId);
    }

    /**
     * Get all problems mentioned for specific user
     */
    public List<String> getAllProblemsMentioned(String userId) {
        List<String> allProblems = new ArrayList<>();
        List<ConversationMemory> conversations = conversationMemoriesByUser.get(userId);
        if (conversations != null) {
            for (ConversationMemory conv : conversations) {
                allProblems.addAll(conv.getProblemsMentioned());
            }
        }
        return allProblems;
    }

    /**
     * Get all goals shared
     */
    public List<String> getAllGoalsShared() {
        return getAllGoalsShared(currentUserId);
    }

    /**
     * Get all goals shared for specific user
     */
    public List<String> getAllGoalsShared(String userId) {
        List<String> allGoals = new ArrayList<>();
        List<ConversationMemory> conversations = conversationMemoriesByUser.get(userId);
        if (conversations != null) {
            for (ConversationMemory conv : conversations) {
                allGoals.addAll(conv.getGoalsShared());
            }
        }
        return allGoals;
    }

    /**
     * Get all updates received
     */
    public List<String> getAllUpdates() {
        return getAllUpdates(currentUserId);
    }

    /**
     * Get all updates for specific user
     */
    public List<String> getAllUpdates(String userId) {
        List<String> allUpdates = new ArrayList<>();
        List<ConversationMemory> conversations = conversationMemoriesByUser.get(userId);
        if (conversations != null) {
            for (ConversationMemory conv : conversations) {
                allUpdates.addAll(conv.getUpdatesReceived());
            }
        }
        return allUpdates;
    }

    /**
     * Get tracked topics (topics discussed over time)
     */
    public List<TrackedTopic> getTrackedTopics() {
        return getTrackedTopics(currentUserId);
    }

    /**
     * Get tracked topics for specific user
     */
    public List<TrackedTopic> getTrackedTopics(String userId) {
        Map<String, TrackedTopic> topics = trackedTopicsByUser.get(userId);
        if (topics == null) return new ArrayList<>();
        
        return topics.values().stream()
            .sorted((a, b) -> Double.compare(b.getImportanceScore(), a.getImportanceScore()))
            .toList();
    }

    /**
     * Get ongoing topics (topics that are currently being discussed)
     */
    public List<TrackedTopic> getOngoingTopics() {
        return getOngoingTopics(currentUserId);
    }

    /**
     * Get ongoing topics for specific user
     */
    public List<TrackedTopic> getOngoingTopics(String userId) {
        Map<String, TrackedTopic> topics = trackedTopicsByUser.get(userId);
        if (topics == null) return new ArrayList<>();
        
        return topics.values().stream()
            .filter(TrackedTopic::isOngoing)
            .sorted((a, b) -> Double.compare(b.getImportanceScore(), a.getImportanceScore()))
            .toList();
    }

    /**
     * Mark a topic as resolved
     */
    public void markTopicResolved(String topicName) {
        markTopicResolved(currentUserId, topicName);
    }

    /**
     * Mark a topic as resolved for specific user
     */
    public void markTopicResolved(String userId, String topicName) {
        Map<String, TrackedTopic> topics = trackedTopicsByUser.get(userId);
        if (topics != null) {
            TrackedTopic topic = topics.get(topicName.toLowerCase());
            if (topic != null) {
                topic.markResolved();
            }
        }
    }

    // ==================== PREFERENCE LEARNING ====================

    /**
     * Learn communication style preference from user interactions
     * @param style The communication style (e.g., "formal", "casual", "friendly", "concise", "detailed")
     */
    public void learnCommunicationStyle(String style) {
        learnCommunicationStyle(currentUserId, style);
    }

    /**
     * Learn communication style for specific user
     */
    public void learnCommunicationStyle(String userId, String style) {
        if (userId == null || style == null) return;
        setPreference(userId, "communicationStyle", style.toLowerCase());
    }

    /**
     * Get learned communication style preference
     */
    public String getCommunicationStylePreference() {
        return getCommunicationStylePreference(currentUserId);
    }

    /**
     * Get communication style for specific user
     */
    public String getCommunicationStylePreference(String userId) {
        Object pref = getPreference(userId, "communicationStyle");
        return pref != null ? pref.toString() : "friendly";
    }

    /**
     * Learn humor preference from user interactions
     * @param humorLevel The humor level (e.g., "none", "mild", "moderate", "high")
     */
    public void learnHumorPreference(String humorLevel) {
        learnHumorPreference(currentUserId, humorLevel);
    }

    /**
     * Learn humor preference for specific user
     */
    public void learnHumorPreference(String userId, String humorLevel) {
        if (userId == null || humorLevel == null) return;
        setPreference(userId, "humorLevel", humorLevel.toLowerCase());
    }

    /**
     * Get learned humor preference
     */
    public String getHumorPreference() {
        return getHumorPreference(currentUserId);
    }

    /**
     * Get humor preference for specific user
     */
    public String getHumorPreference(String userId) {
        Object pref = getPreference(userId, "humorLevel");
        return pref != null ? pref.toString() : "moderate";
    }

    /**
     * Learn support style preference
     * @param style The support style (e.g., "empathetic", "practical", "motivational", "analytical")
     */
    public void learnSupportStyle(String style) {
        learnSupportStyle(currentUserId, style);
    }

    /**
     * Learn support style for specific user
     */
    public void learnSupportStyle(String userId, String style) {
        if (userId == null || style == null) return;
        setPreference(userId, "supportStyle", style.toLowerCase());
    }

    /**
     * Get learned support style preference
     */
    public String getSupportStylePreference() {
        return getSupportStylePreference(currentUserId);
    }

    /**
     * Get support style for specific user
     */
    public String getSupportStylePreference(String userId) {
        Object pref = getPreference(userId, "supportStyle");
        return pref != null ? pref.toString() : "empathetic";
    }

    /**
     * Get all learned preferences as a summary
     */
    public Map<String, Object> getLearnedPreferences() {
        return getLearnedPreferences(currentUserId);
    }

    /**
     * Get all learned preferences for specific user
     */
    public Map<String, Object> getLearnedPreferences(String userId) {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("communicationStyle", getCommunicationStylePreference(userId));
        prefs.put("humorLevel", getHumorPreference(userId));
        prefs.put("supportStyle", getSupportStylePreference(userId));
        
        Map<String, Object> customPrefs = getAllPreferences(userId);
        for (Map.Entry<String, Object> entry : customPrefs.entrySet()) {
            if (!prefs.containsKey(entry.getKey())) {
                prefs.put(entry.getKey(), entry.getValue());
            }
        }
        
        return prefs;
    }

    // ==================== CONTEXT INTEGRATION ====================

    /**
     * Get memories relevant to current context
     */
    public List<Memory> getContextualMemories(String context, int limit) {
        List<Memory> relevant = new ArrayList<>();
        
        // Search for memories related to context
        List<Memory> searched = searchMemories(context);
        relevant.addAll(searched);
        
        // Add salient memories
        relevant.addAll(getSalientMemories(limit));
        
        // Sort by salience and limit
        return relevant.stream()
            .sorted((a, b) -> Double.compare(b.getSalienceScore(), a.getSalienceScore()))
            .limit(limit)
            .distinct()
            .toList();
    }

    /**
     * Generate a summary of personal memory for the user
     */
    public Map<String, Object> getMemorySummary() {
        return getMemorySummary(currentUserId);
    }

    /**
     * Generate a summary of personal memory for specific user
     */
    public Map<String, Object> getMemorySummary(String userId) {
        Map<String, Object> summary = new HashMap<>();
        
        UserProfile profile = userProfiles.get(userId);
        if (profile != null) {
            summary.put("userId", userId);
            summary.put("name", profile.getName());
            summary.put("totalConversations", profile.getTotalConversations());
            summary.put("interests", profile.getInterests());
            summary.put("hobbies", profile.getHobbies());
        }
        
        List<Memory> memories = memoriesByUser.get(userId);
        if (memories != null) {
            summary.put("totalMemories", memories.size());
            summary.put("importantMemories", memories.stream().filter(Memory::isImportant).count());
        }
        
        List<Experience> experiences = experiencesByUser.get(userId);
        if (experiences != null) {
            summary.put("totalExperiences", experiences.size());
        }
        
        Map<String, Integer> emotions = emotionalHistory.get(userId);
        if (emotions != null && !emotions.isEmpty()) {
            summary.put("dominantEmotion", getDominantEmotion(userId));
            summary.put("emotionalRange", emotions.size());
        }
        
        summary.put("lastInteraction", lastInteraction.get(userId));
        
        return summary;
    }

    // ==================== PERSISTENCE ====================

    /**
     * Export personal memory to a serializable format
     */
    public Map<String, Object> exportToMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("currentUserId", currentUserId);
        
        // Export user profiles
        List<Map<String, Object>> profiles = new ArrayList<>();
        for (UserProfile profile : userProfiles.values()) {
            profiles.add(profile.toMap());
        }
        data.put("userProfiles", profiles);
        
        // Export memories
        Map<String, List<Map<String, Object>>> memoriesExport = new HashMap<>();
        for (Map.Entry<String, List<Memory>> entry : memoriesByUser.entrySet()) {
            List<Map<String, Object>> memoryList = new ArrayList<>();
            for (Memory memory : entry.getValue()) {
                Map<String, Object> m = new HashMap<>();
                m.put("memoryId", memory.getMemoryId());
                m.put("content", memory.getContent());
                m.put("category", memory.getCategory());
                m.put("createdAt", memory.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                m.put("isImportant", memory.isImportant());
                m.put("emotions", memory.getEmotions());
                memoryList.add(m);
            }
            memoriesExport.put(entry.getKey(), memoryList);
        }
        data.put("memories", memoriesExport);
        
        // Export topic interests
        data.put("topicInterests", new HashMap<>(topicInterests));
        
        // Export emotional history
        data.put("emotionalHistory", new HashMap<>(emotionalHistory));
        
        return data;
    }

    /**
     * Clear all personal memory data
     */
    public void clearAll() {
        currentUserId = null;
        currentProfile = null;
        userProfiles.clear();
        memoriesByUser.clear();
        experiencesByUser.clear();
        topicInterests.clear();
        emotionalHistory.clear();
        lastInteraction.clear();
    }

    /**
     * Clear data for a specific user
     */
    public void clearUserData(String userId) {
        userProfiles.remove(userId);
        memoriesByUser.remove(userId);
        experiencesByUser.remove(userId);
        topicInterests.remove(userId);
        emotionalHistory.remove(userId);
        lastInteraction.remove(userId);
        
        if (userId.equals(currentUserId)) {
            currentUserId = null;
            currentProfile = null;
        }
    }

    // ==================== STATISTICS ====================

    /**
     * Get statistics about personal memory
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalUsers", userProfiles.size());
        stats.put("currentUser", currentUserId);
        
        if (currentUserId != null) {
            List<Memory> memories = memoriesByUser.get(currentUserId);
            stats.put("totalMemories", memories != null ? memories.size() : 0);
            
            List<Experience> experiences = experiencesByUser.get(currentUserId);
            stats.put("totalExperiences", experiences != null ? experiences.size() : 0);
            
            Set<String> interests = topicInterests.get(currentUserId);
            stats.put("totalInterests", interests != null ? interests.size() : 0);
            
            Map<String, Integer> emotions = emotionalHistory.get(currentUserId);
            stats.put("emotionsTracked", emotions != null ? emotions.size() : 0);
        }
        
        return stats;
    }

    @Override
    public String toString() {
        return String.format("PersonalMemory{currentUser=%s, users=%d, memories=%d}",
            currentUserId, userProfiles.size(), 
            memoriesByUser.containsKey(currentUserId) ? memoriesByUser.get(currentUserId).size() : 0);
    }
}

