import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * RelationshipMemory - Manages relationship-specific memories and interactions
 * Tracks relationships with people in the user's life, interaction history,
 * relationship events, milestones, and dynamics between different individuals
 */
public class RelationshipMemory {

    // ==================== INNER CLASSES ====================

    /**
     * Represents a relationship between the user and another person
     */
    public static class Relationship {
        private String personId;
        private String name;
        private String relationshipType; // friend, family, colleague, romantic, etc.
        private String description;
        private LocalDateTime firstMet;
        private LocalDateTime lastInteraction;
        private int interactionCount;
        private double closenessLevel; // 0.0 - 1.0
        private Set<String> sharedInterests;
        private Map<String, Object> attributes;
        private boolean isActive;
        private List<String> tags;

        public Relationship(String personId, String name, String relationshipType) {
            this.personId = personId;
            this.name = name;
            this.relationshipType = relationshipType;
            this.description = "";
            this.firstMet = LocalDateTime.now();
            this.lastInteraction = LocalDateTime.now();
            this.interactionCount = 0;
            this.closenessLevel = 0.0;
            this.sharedInterests = new HashSet<>();
            this.attributes = new HashMap<>();
            this.isActive = true;
            this.tags = new ArrayList<>();
        }

        // Getters
        public String getPersonId() { return personId; }
        public String getName() { return name; }
        public String getRelationshipType() { return relationshipType; }
        public String getDescription() { return description; }
        public LocalDateTime getFirstMet() { return firstMet; }
        public LocalDateTime getLastInteraction() { return lastInteraction; }
        public int getInteractionCount() { return interactionCount; }
        public double getClosenessLevel() { return closenessLevel; }
        public Set<String> getSharedInterests() { return new HashSet<>(sharedInterests); }
        public Map<String, Object> getAttributes() { return new HashMap<>(attributes); }
        public boolean isActive() { return isActive; }
        public List<String> getTags() { return new ArrayList<>(tags); }

        // Setters
        public void setName(String name) { this.name = name; }
        public void setRelationshipType(String relationshipType) { this.relationshipType = relationshipType; }
        public void setDescription(String description) { this.description = description; }
        public void setClosenessLevel(double closenessLevel) { 
            this.closenessLevel = Math.max(0.0, Math.min(1.0, closenessLevel)); 
        }
        public void setActive(boolean active) { this.isActive = active; }

        // Methods
        public void addSharedInterest(String interest) {
            sharedInterests.add(interest.toLowerCase());
        }

        public void removeSharedInterest(String interest) {
            sharedInterests.remove(interest.toLowerCase());
        }

        public void setAttribute(String key, Object value) {
            attributes.put(key, value);
        }

        public Object getAttribute(String key) {
            return attributes.get(key);
        }

        public void addTag(String tag) {
            if (!tags.contains(tag.toLowerCase())) {
                tags.add(tag.toLowerCase());
            }
        }

        public void removeTag(String tag) {
            tags.remove(tag.toLowerCase());
        }

        public void recordInteraction() {
            this.lastInteraction = LocalDateTime.now();
            this.interactionCount++;
            updateCloseness();
        }

        private void updateCloseness() {
            // Increase closeness based on interaction count, capped at 1.0
            double baseCloseness = Math.min(1.0, interactionCount / 50.0);
            
            // Recency factor - if no interaction in 30 days, reduce closeness
            long daysSinceLastInteraction = java.time.Duration.between(lastInteraction, LocalDateTime.now()).toDays();
            double recencyFactor = daysSinceLastInteraction > 30 ? 0.5 : 1.0;
            
            this.closenessLevel = baseCloseness * recencyFactor;
        }

        public double getRecencyScore() {
            long hoursSinceLastInteraction = java.time.Duration.between(lastInteraction, LocalDateTime.now()).toHours();
            return 1.0 / (1.0 + hoursSinceLastInteraction / 24.0);
        }

        @Override
        public String toString() {
            return String.format("Relationship{name='%s', type='%s', closeness=%.2f, interactions=%d}",
                name, relationshipType, closenessLevel, interactionCount);
        }
    }

    /**
     * Represents an interaction event with a person
     */
    public static class Interaction {
        private String interactionId;
        private String personId;
        private LocalDateTime timestamp;
        private String type; // conversation, message, call, meeting, etc.
        private String summary;
        private Map<String, Object> context;
        private Set<String> topicsDiscussed;
        private String mood; // positive, neutral, negative
        private int significance; // 1-10
        
        // New fields for conversation highlights, jokes, and important moments
        private List<String> conversationHighlights; // Key memorable quotes or moments
        private List<String> sharedJokes; // Jokes or funny moments shared
        private List<String> importantMoments; // Significant or meaningful moments
        private Map<String, Object> emotionalNotes; // Emotional context or reactions
        
        // New fields for name usage patterns, inside references, and personal callbacks
        private Map<String, Integer> nameUsagePatterns; // How the person refers to user (nicknames, etc.)
        private List<String> insideReferences; // Inside jokes, shared references, private language
        private List<String> personalCallbacks; // Things to remember and reference later

        public Interaction(String personId, String type, String summary) {
            this.interactionId = UUID.randomUUID().toString();
            this.personId = personId;
            this.timestamp = LocalDateTime.now();
            this.type = type;
            this.summary = summary;
            this.context = new HashMap<>();
            this.topicsDiscussed = new HashSet<>();
            this.mood = "neutral";
            this.significance = 5;
            this.conversationHighlights = new ArrayList<>();
            this.sharedJokes = new ArrayList<>();
            this.importantMoments = new ArrayList<>();
            this.emotionalNotes = new HashMap<>();
            this.nameUsagePatterns = new HashMap<>();
            this.insideReferences = new ArrayList<>();
            this.personalCallbacks = new ArrayList<>();
        }

        // Getters
        public String getInteractionId() { return interactionId; }
        public String getPersonId() { return personId; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getType() { return type; }
        public String getSummary() { return summary; }
        public Map<String, Object> getContext() { return new HashMap<>(context); }
        public Set<String> getTopicsDiscussed() { return new HashSet<>(topicsDiscussed); }
        public String getMood() { return mood; }
        public int getSignificance() { return significance; }
        public List<String> getConversationHighlights() { return new ArrayList<>(conversationHighlights); }
        public List<String> getSharedJokes() { return new ArrayList<>(sharedJokes); }
        public List<String> getImportantMoments() { return new ArrayList<>(importantMoments); }
        public Map<String, Object> getEmotionalNotes() { return new HashMap<>(emotionalNotes); }
        public Map<String, Integer> getNameUsagePatterns() { return new HashMap<>(nameUsagePatterns); }
        public List<String> getInsideReferences() { return new ArrayList<>(insideReferences); }
        public List<String> getPersonalCallbacks() { return new ArrayList<>(personalCallbacks); }

        // Setters
        public void setType(String type) { this.type = type; }
        public void setSummary(String summary) { this.summary = summary; }
        public void setMood(String mood) { this.mood = mood.toLowerCase(); }
        public void setSignificance(int significance) { 
            this.significance = Math.max(1, Math.min(10, significance)); 
        }

        // Methods
        public void addContext(String key, Object value) {
            context.put(key, value);
        }

        public void addTopic(String topic) {
            topicsDiscussed.add(topic.toLowerCase());
        }

        public void addTopics(Collection<String> topics) {
            for (String topic : topics) {
                addTopic(topic);
            }
        }
        
        // Conversation highlights methods
        public void addConversationHighlight(String highlight) {
            if (highlight != null && !highlight.trim().isEmpty()) {
                conversationHighlights.add(highlight.trim());
            }
        }
        
        public void addConversationHighlights(Collection<String> highlights) {
            for (String highlight : highlights) {
                addConversationHighlight(highlight);
            }
        }
        
        // Shared jokes methods
        public void addSharedJoke(String joke) {
            if (joke != null && !joke.trim().isEmpty()) {
                sharedJokes.add(joke.trim());
            }
        }
        
        public void addSharedJokes(Collection<String> jokes) {
            for (String joke : jokes) {
                addSharedJoke(joke);
            }
        }
        
        // Important moments methods
        public void addImportantMoment(String moment) {
            if (moment != null && !moment.trim().isEmpty()) {
                importantMoments.add(moment.trim());
            }
        }
        
        public void addImportantMoments(Collection<String> moments) {
            for (String moment : moments) {
                addImportantMoment(moment);
            }
        }
        
        // Emotional notes methods
        public void addEmotionalNote(String key, Object value) {
            emotionalNotes.put(key, value);
        }
        
        public Object getEmotionalNote(String key) {
            return emotionalNotes.get(key);
        }
        
        // Name usage patterns methods
        public void recordNameUsage(String nickname) {
            if (nickname != null && !nickname.trim().isEmpty()) {
                nameUsagePatterns.merge(nickname.toLowerCase(), 1, Integer::sum);
            }
        }
        
        public String getMostUsedName() {
            if (nameUsagePatterns.isEmpty()) return null;
            return nameUsagePatterns.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        }
        
        // Inside references methods
        public void addInsideReference(String reference) {
            if (reference != null && !reference.trim().isEmpty() && !insideReferences.contains(reference)) {
                insideReferences.add(reference.trim());
            }
        }
        
        public void addInsideReferences(Collection<String> references) {
            for (String reference : references) {
                addInsideReference(reference);
            }
        }
        
        // Personal callbacks methods
        public void addPersonalCallback(String callback) {
            if (callback != null && !callback.trim().isEmpty()) {
                personalCallbacks.add(callback.trim());
            }
        }
        
        public void addPersonalCallbacks(Collection<String> callbacks) {
            for (String callback : callbacks) {
                addPersonalCallback(callback);
            }
        }

        @Override
        public String toString() {
            return String.format("Interaction{person='%s', type='%s', mood='%s', significance=%d}",
                personId, type, mood, significance);
        }
    }

    /**
     * Represents a relationship event or milestone
     */
    public static class RelationshipEvent {
        private String eventId;
        private String personId;
        private LocalDateTime timestamp;
        private String eventType; // milestone, conflict, bonding, update, etc.
        private String title;
        private String description;
        private int importance; // 1-10
        private Map<String, Object> details;
        private boolean isPositive;

        public RelationshipEvent(String personId, String eventType, String title) {
            this.eventId = UUID.randomUUID().toString();
            this.personId = personId;
            this.timestamp = LocalDateTime.now();
            this.eventType = eventType;
            this.title = title;
            this.description = "";
            this.importance = 5;
            this.details = new HashMap<>();
            this.isPositive = true;
        }

        // Getters
        public String getEventId() { return eventId; }
        public String getPersonId() { return personId; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getEventType() { return eventType; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public int getImportance() { return importance; }
        public Map<String, Object> getDetails() { return new HashMap<>(details); }
        public boolean isPositive() { return isPositive; }

        // Setters
        public void setDescription(String description) { this.description = description; }
        public void setImportance(int importance) { 
            this.importance = Math.max(1, Math.min(10, importance)); 
        }
        public void setPositive(boolean positive) { this.isPositive = positive; }

        // Methods
        public void addDetail(String key, Object value) {
            details.put(key, value);
        }

        @Override
        public String toString() {
            return String.format("RelationshipEvent{person='%s', type='%s', title='%s', importance=%d}",
                personId, eventType, title, importance);
        }
    }

    /**
     * Represents dynamics between two people in the user's life
     */
    public static class PeopleDynamics {
        private String personId1;
        private String personId2;
        private String relationship; // how they know each other
        private LocalDateTime since;
        private double affinity; // how well they get along -1.0 to 1.0
        private Set<String> sharedContexts;
        private Map<String, Object> notes;

        public PeopleDynamics(String personId1, String personId2, String relationship) {
            // Ensure consistent ordering
            if (personId1.compareTo(personId2) > 0) {
                String temp = personId1;
                personId1 = personId2;
                personId2 = temp;
            }
            this.personId1 = personId1;
            this.personId2 = personId2;
            this.relationship = relationship;
            this.since = LocalDateTime.now();
            this.affinity = 0.0;
            this.sharedContexts = new HashSet<>();
            this.notes = new HashMap<>();
        }

        // Getters
        public String getPersonId1() { return personId1; }
        public String getPersonId2() { return personId2; }
        public String getRelationship() { return relationship; }
        public LocalDateTime getSince() { return since; }
        public double getAffinity() { return affinity; }
        public Set<String> getSharedContexts() { return new HashSet<>(sharedContexts); }
        public Map<String, Object> getNotes() { return new HashMap<>(notes); }

        // Setters
        public void setRelationship(String relationship) { this.relationship = relationship; }
        public void setAffinity(double affinity) { 
            this.affinity = Math.max(-1.0, Math.min(1.0, affinity)); 
        }

        // Methods
        public void addSharedContext(String context) {
            sharedContexts.add(context.toLowerCase());
        }

        public void setNote(String key, Object value) {
            notes.put(key, value);
        }

        public Object getNote(String key) {
            return notes.get(key);
        }

        public boolean involvesPerson(String personId) {
            return personId1.equals(personId) || personId2.equals(personId);
        }

        public String getOtherPerson(String personId) {
            if (personId1.equals(personId)) return personId2;
            if (personId2.equals(personId)) return personId1;
            return null;
        }

        @Override
        public String toString() {
            return String.format("PeopleDynamics{%s <-> %s, affinity=%.2f}",
                personId1, personId2, affinity);
        }
    }

    /**
     * Represents a conversation anniversary - marks important conversation milestones
     */
    public static class ConversationAnniversary {
        private String anniversaryId;
        private String personId;
        private LocalDateTime conversationDate;
        private LocalDateTime anniversaryDate;
        private int anniversaryNumber; // 1 year, 2 years, etc.
        private String significance; // first_met, milestone_conversation, etc.
        private String note;
        private Map<String, Object> context;

        public ConversationAnniversary(String personId, LocalDateTime conversationDate, int anniversaryNumber) {
            this.anniversaryId = UUID.randomUUID().toString();
            this.personId = personId;
            this.conversationDate = conversationDate;
            this.anniversaryDate = LocalDateTime.now();
            this.anniversaryNumber = anniversaryNumber;
            this.significance = "";
            this.note = "";
            this.context = new HashMap<>();
        }

        // Getters
        public String getAnniversaryId() { return anniversaryId; }
        public String getPersonId() { return personId; }
        public LocalDateTime getConversationDate() { return conversationDate; }
        public LocalDateTime getAnniversaryDate() { return anniversaryDate; }
        public int getAnniversaryNumber() { return anniversaryNumber; }
        public String getSignificance() { return significance; }
        public String getNote() { return note; }
        public Map<String, Object> getContext() { return new HashMap<>(context); }

        // Setters
        public void setSignificance(String significance) { this.significance = significance; }
        public void setNote(String note) { this.note = note; }

        // Methods
        public void addContext(String key, Object value) {
            context.put(key, value);
        }

        @Override
        public String toString() {
            return String.format("ConversationAnniversary{person='%s', number=%d years, date=%s}",
                personId, anniversaryNumber, anniversaryDate.toLocalDate());
        }
    }

    /**
     * Represents a user achievement - tracks accomplishments and milestones
     */
    public static class UserAchievement {
        private String achievementId;
        private String personId; // Who achieved something (could be the user or another person)
        private LocalDateTime achievedAt;
        private String title;
        private String description;
        private String category; // career, personal, academic, health, etc.
        private int significance; // 1-10
        private Set<String> relatedPeople; // People who were involved/celebrated
        private Map<String, Object> details;
        private boolean isCelebrated;

        public UserAchievement(String personId, String title, String category) {
            this.achievementId = UUID.randomUUID().toString();
            this.personId = personId;
            this.achievedAt = LocalDateTime.now();
            this.title = title;
            this.description = "";
            this.category = category;
            this.significance = 5;
            this.relatedPeople = new HashSet<>();
            this.details = new HashMap<>();
            this.isCelebrated = false;
        }

        // Getters
        public String getAchievementId() { return achievementId; }
        public String getPersonId() { return personId; }
        public LocalDateTime getAchievedAt() { return achievedAt; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getCategory() { return category; }
        public int getSignificance() { return significance; }
        public Set<String> getRelatedPeople() { return new HashSet<>(relatedPeople); }
        public Map<String, Object> getDetails() { return new HashMap<>(details); }
        public boolean isCelebrated() { return isCelebrated; }

        // Setters
        public void setDescription(String description) { this.description = description; }
        public void setCategory(String category) { this.category = category; }
        public void setSignificance(int significance) { 
            this.significance = Math.max(1, Math.min(10, significance)); 
        }
        public void setCelebrated(boolean celebrated) { this.isCelebrated = celebrated; }

        // Methods
        public void addRelatedPerson(String person) {
            relatedPeople.add(person);
        }

        public void addDetail(String key, Object value) {
            details.put(key, value);
        }

        @Override
        public String toString() {
            return String.format("UserAchievement{person='%s', title='%s', category='%s', significance=%d}",
                personId, title, category, significance);
        }
    }

    /**
     * Represents a recovery moment - tracks emotional/mental health recovery progress
     */
    public static class RecoveryMoment {
        private String recoveryId;
        private String personId;
        private LocalDateTime timestamp;
        private String type; // emotional, mental_health, progress, breakthrough, etc.
        private String title;
        private String description;
        private int progressLevel; // 1-10
        private String emotionalState;
        private Set<String> supportingFactors; // What helped in recovery
        private Map<String, Object> notes;
        private boolean isMilestone;

        public RecoveryMoment(String personId, String type, String title) {
            this.recoveryId = UUID.randomUUID().toString();
            this.personId = personId;
            this.timestamp = LocalDateTime.now();
            this.type = type;
            this.title = title;
            this.description = "";
            this.progressLevel = 5;
            this.emotionalState = "";
            this.supportingFactors = new HashSet<>();
            this.notes = new HashMap<>();
            this.isMilestone = false;
        }

        // Getters
        public String getRecoveryId() { return recoveryId; }
        public String getPersonId() { return personId; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getType() { return type; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public int getProgressLevel() { return progressLevel; }
        public String getEmotionalState() { return emotionalState; }
        public Set<String> getSupportingFactors() { return new HashSet<>(supportingFactors); }
        public Map<String, Object> getNotes() { return new HashMap<>(notes); }
        public boolean isMilestone() { return isMilestone; }

        // Setters
        public void setDescription(String description) { this.description = description; }
        public void setProgressLevel(int progressLevel) { 
            this.progressLevel = Math.max(1, Math.min(10, progressLevel)); 
        }
        public void setEmotionalState(String emotionalState) { this.emotionalState = emotionalState; }
        public void setMilestone(boolean milestone) { this.isMilestone = milestone; }

        // Methods
        public void addSupportingFactor(String factor) {
            supportingFactors.add(factor.toLowerCase());
        }

        public void addNote(String key, Object value) {
            notes.put(key, value);
        }

        @Override
        public String toString() {
            return String.format("RecoveryMoment{person='%s', type='%s', title='%s', progress=%d, milestone=%b}",
                personId, type, title, progressLevel, isMilestone);
        }
    }

    // ==================== RELATIONSHIP TYPES ====================

    public static final String TYPE_FAMILY = "family";
    public static final String TYPE_FRIEND = "friend";
    public static final String TYPE_COLLEAGUE = "colleague";
    public static final String TYPE_ROMANTIC = "romantic";
    public static final String TYPE_ACQUAINTANCE = "acquaintance";
    public static final String TYPE_MENTOR = "mentor";
    public static final String TYPE_CLASSMATE = "classmate";
    public static final String TYPE_NEIGHBOR = "neighbor";
    public static final String TYPE_ONLINE = "online";
    public static final String TYPE_OTHER = "other";

    // ==================== INTERACTION TYPES ====================

    public static final String INTERACTION_CONVERSATION = "conversation";
    public static final String INTERACTION_MESSAGE = "message";
    public static final String INTERACTION_CALL = "call";
    public static final String INTERACTION_MEETING = "meeting";
    public static final String INTERACTION_EVENT = "event";
    public static final String INTERACTION_ONLINE = "online";

    // ==================== EVENT TYPES ====================

    public static final String EVENT_MILESTONE = "milestone";
    public static final String EVENT_CONFLICT = "conflict";
    public static final String EVENT_BONDING = "bonding";
    public static final String EVENT_UPDATE = "update";
    public static final String EVENT_SUPPORT = "support";
    public static final String EVENT_CELEBRATION = "celebration";
    public static final String EVENT_ANNIVERSARY = "anniversary";
    public static final String EVENT_ACHIEVEMENT = "achievement";
    public static final String EVENT_RECOVERY = "recovery";

    // ==================== MAIN CLASS ====================

    private String currentUserId;
    private Map<String, Relationship> relationships; // personId -> Relationship
    private Map<String, List<Interaction>> interactionsByPerson; // personId -> interactions
    private Map<String, List<RelationshipEvent>> eventsByPerson; // personId -> events
    private Map<String, PeopleDynamics> dynamicsBetween; // "id1-id2" -> dynamics
    private Map<String, List<ConversationAnniversary>> anniversariesByPerson; // personId -> anniversaries
    private Map<String, List<UserAchievement>> achievementsByPerson; // personId -> achievements
    private Map<String, List<RecoveryMoment>> recoveryMomentsByPerson; // personId -> recovery moments
    private int maxInteractionsPerPerson;
    private int maxEventsPerPerson;
    private int maxAnniversariesPerPerson;
    private int maxAchievementsPerPerson;
    private int maxRecoveryMomentsPerPerson;

    public RelationshipMemory() {
        this.currentUserId = null;
        this.relationships = new HashMap<>();
        this.interactionsByPerson = new HashMap<>();
        this.eventsByPerson = new HashMap<>();
        this.dynamicsBetween = new HashMap<>();
        this.anniversariesByPerson = new HashMap<>();
        this.achievementsByPerson = new HashMap<>();
        this.recoveryMomentsByPerson = new HashMap<>();
        this.maxInteractionsPerPerson = 100;
        this.maxEventsPerPerson = 50;
        this.maxAnniversariesPerPerson = 50;
        this.maxAchievementsPerPerson = 50;
        this.maxRecoveryMomentsPerPerson = 50;
    }

    // ==================== USER MANAGEMENT ====================

    /**
     * Set the current user for relationship memory operations
     */
    public void setCurrentUser(String userId) {
        this.currentUserId = userId;
    }

    /**
     * Get current user ID
     */
    public String getCurrentUserId() {
        return currentUserId;
    }

    // ==================== RELATIONSHIP MANAGEMENT ====================

    /**
     * Add or update a relationship with a person
     */
    public Relationship addRelationship(String personId, String name, String relationshipType) {
        Relationship relationship = relationships.computeIfAbsent(personId, 
            k -> new Relationship(personId, name, relationshipType));
        
        if (name != null) {
            relationship.setName(name);
        }
        if (relationshipType != null) {
            relationship.setRelationshipType(relationshipType);
        }
        
        return relationship;
    }

    /**
     * Get a relationship by person ID
     */
    public Relationship getRelationship(String personId) {
        return relationships.get(personId);
    }

    /**
     * Get all relationships
     */
    public List<Relationship> getAllRelationships() {
        return new ArrayList<>(relationships.values());
    }

    /**
     * Get active relationships
     */
    public List<Relationship> getActiveRelationships() {
        return relationships.values().stream()
            .filter(Relationship::isActive)
            .sorted((a, b) -> Double.compare(b.getClosenessLevel(), a.getClosenessLevel()))
            .toList();
    }

    /**
     * Get relationships by type
     */
    public List<Relationship> getRelationshipsByType(String type) {
        return relationships.values().stream()
            .filter(r -> r.getRelationshipType().equalsIgnoreCase(type))
            .sorted((a, b) -> Double.compare(b.getClosenessLevel(), a.getClosenessLevel()))
            .toList();
    }

    /**
     * Get close relationships (closeness > 0.5)
     */
    public List<Relationship> getCloseRelationships() {
        return relationships.values().stream()
            .filter(r -> r.getClosenessLevel() > 0.5 && r.isActive())
            .sorted((a, b) -> Double.compare(b.getClosenessLevel(), a.getClosenessLevel()))
            .toList();
    }

    /**
     * Search relationships by name
     */
    public List<Relationship> searchRelationships(String query) {
        String lowerQuery = query.toLowerCase();
        return relationships.values().stream()
            .filter(r -> r.getName().toLowerCase().contains(lowerQuery))
            .toList();
    }

    /**
     * Remove a relationship
     */
    public void removeRelationship(String personId) {
        Relationship removed = relationships.remove(personId);
        if (removed != null) {
            interactionsByPerson.remove(personId);
            eventsByPerson.remove(personId);
            
            // Remove related dynamics
            dynamicsBetween.keySet().removeIf(key -> 
                key.contains(personId));
        }
    }

    /**
     * Update relationship description
     */
    public void updateRelationshipDescription(String personId, String description) {
        Relationship relationship = relationships.get(personId);
        if (relationship != null) {
            relationship.setDescription(description);
        }
    }

    /**
     * Update relationship closeness
     */
    public void updateCloseness(String personId, double closeness) {
        Relationship relationship = relationships.get(personId);
        if (relationship != null) {
            relationship.setClosenessLevel(closeness);
        }
    }

    /**
     * Get relationship by name (first match)
     */
    public Relationship findRelationshipByName(String name) {
        String lowerName = name.toLowerCase();
        return relationships.values().stream()
            .filter(r -> r.getName().toLowerCase().equals(lowerName))
            .findFirst()
            .orElse(null);
    }

    // ==================== INTERACTION MANAGEMENT ====================

    /**
     * Record an interaction with a person
     */
    public Interaction recordInteraction(String personId, String type, String summary) {
        // Ensure relationship exists
        if (!relationships.containsKey(personId)) {
            addRelationship(personId, "Unknown", TYPE_ACQUAINTANCE);
        }
        
        Interaction interaction = new Interaction(personId, type, summary);
        List<Interaction> interactions = interactionsByPerson.computeIfAbsent(personId, k -> new ArrayList<>());
        interactions.add(interaction);
        
        // Update relationship
        Relationship relationship = relationships.get(personId);
        if (relationship != null) {
            relationship.recordInteraction();
        }
        
        // Enforce limit
        while (interactions.size() > maxInteractionsPerPerson) {
            interactions.remove(0);
        }
        
        return interaction;
    }

    /**
     * Get all interactions with a person
     */
    public List<Interaction> getInteractions(String personId) {
        List<Interaction> interactions = interactionsByPerson.get(personId);
        return interactions != null ? new ArrayList<>(interactions) : new ArrayList<>();
    }

    /**
     * Get recent interactions with a person
     */
    public List<Interaction> getRecentInteractions(String personId, int limit) {
        List<Interaction> interactions = interactionsByPerson.get(personId);
        if (interactions == null) return new ArrayList<>();
        
        return interactions.stream()
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .limit(limit)
            .toList();
    }

    /**
     * Get last interaction with a person
     */
    public Interaction getLastInteraction(String personId) {
        List<Interaction> interactions = interactionsByPerson.get(personId);
        if (interactions == null || interactions.isEmpty()) return null;
        return interactions.get(interactions.size() - 1);
    }

    /**
     * Get all unique topics discussed with a person
     */
    public Set<String> getTopicsDiscussed(String personId) {
        Set<String> topics = new HashSet<>();
        List<Interaction> interactions = interactionsByPerson.get(personId);
        if (interactions != null) {
            for (Interaction interaction : interactions) {
                topics.addAll(interaction.getTopicsDiscussed());
            }
        }
        return topics;
    }

    /**
     * Get all conversation highlights from interactions with a person
     */
    public List<String> getConversationHighlights(String personId) {
        List<String> highlights = new ArrayList<>();
        List<Interaction> interactions = interactionsByPerson.get(personId);
        if (interactions != null) {
            for (Interaction interaction : interactions) {
                highlights.addAll(interaction.getConversationHighlights());
            }
        }
        return highlights;
    }

    /**
     * Get all shared jokes from interactions with a person
     */
    public List<String> getSharedJokes(String personId) {
        List<String> jokes = new ArrayList<>();
        List<Interaction> interactions = interactionsByPerson.get(personId);
        if (interactions != null) {
            for (Interaction interaction : interactions) {
                jokes.addAll(interaction.getSharedJokes());
            }
        }
        return jokes;
    }

    /**
     * Get all important moments from interactions with a person
     */
    public List<String> getImportantMoments(String personId) {
        List<String> moments = new ArrayList<>();
        List<Interaction> interactions = interactionsByPerson.get(personId);
        if (interactions != null) {
            for (Interaction interaction : interactions) {
                moments.addAll(interaction.getImportantMoments());
            }
        }
        return moments;
    }

    /**
     * Get all conversation highlights across all relationships
     */
    public List<String> getAllConversationHighlights() {
        List<String> highlights = new ArrayList<>();
        for (List<Interaction> interactions : interactionsByPerson.values()) {
            for (Interaction interaction : interactions) {
                highlights.addAll(interaction.getConversationHighlights());
            }
        }
        return highlights;
    }

    /**
     * Get all shared jokes across all relationships
     */
    public List<String> getAllSharedJokes() {
        List<String> jokes = new ArrayList<>();
        for (List<Interaction> interactions : interactionsByPerson.values()) {
            for (Interaction interaction : interactions) {
                jokes.addAll(interaction.getSharedJokes());
            }
        }
        return jokes;
    }

    /**
     * Get all important moments across all relationships
     */
    public List<String> getAllImportantMoments() {
        List<String> moments = new ArrayList<>();
        for (List<Interaction> interactions : interactionsByPerson.values()) {
            for (Interaction interaction : interactions) {
                moments.addAll(interaction.getImportantMoments());
            }
        }
        return moments;
    }

    /**
     * Get all name usage patterns from interactions with a person
     */
    public Map<String, Integer> getNameUsagePatterns(String personId) {
        Map<String, Integer> patterns = new HashMap<>();
        List<Interaction> interactions = interactionsByPerson.get(personId);
        if (interactions != null) {
            for (Interaction interaction : interactions) {
                for (Map.Entry<String, Integer> entry : interaction.getNameUsagePatterns().entrySet()) {
                    patterns.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }
        }
        return patterns;
    }

    /**
     * Get all inside references from interactions with a person
     */
    public List<String> getInsideReferences(String personId) {
        List<String> references = new ArrayList<>();
        List<Interaction> interactions = interactionsByPerson.get(personId);
        if (interactions != null) {
            for (Interaction interaction : interactions) {
                references.addAll(interaction.getInsideReferences());
            }
        }
        return references;
    }

    /**
     * Get all personal callbacks from interactions with a person
     */
    public List<String> getPersonalCallbacks(String personId) {
        List<String> callbacks = new ArrayList<>();
        List<Interaction> interactions = interactionsByPerson.get(personId);
        if (interactions != null) {
            for (Interaction interaction : interactions) {
                callbacks.addAll(interaction.getPersonalCallbacks());
            }
        }
        return callbacks;
    }

    /**
     * Get all name usage patterns across all relationships
     */
    public Map<String, Integer> getAllNameUsagePatterns() {
        Map<String, Integer> patterns = new HashMap<>();
        for (List<Interaction> interactions : interactionsByPerson.values()) {
            for (Interaction interaction : interactions) {
                for (Map.Entry<String, Integer> entry : interaction.getNameUsagePatterns().entrySet()) {
                    patterns.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }
        }
        return patterns;
    }

    /**
     * Get all inside references across all relationships
     */
    public List<String> getAllInsideReferences() {
        List<String> references = new ArrayList<>();
        for (List<Interaction> interactions : interactionsByPerson.values()) {
            for (Interaction interaction : interactions) {
                references.addAll(interaction.getInsideReferences());
            }
        }
        return references;
    }

    /**
     * Get all personal callbacks across all relationships
     */
    public List<String> getAllPersonalCallbacks() {
        List<String> callbacks = new ArrayList<>();
        for (List<Interaction> interactions : interactionsByPerson.values()) {
            for (Interaction interaction : interactions) {
                callbacks.addAll(interaction.getPersonalCallbacks());
            }
        }
        return callbacks;
    }

    /**
     * Get all interactions across all relationships
     */
    public List<Interaction> getAllInteractions() {
        List<Interaction> allInteractions = new ArrayList<>();
        for (List<Interaction> interactions : interactionsByPerson.values()) {
            allInteractions.addAll(interactions);
        }
        return allInteractions.stream()
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .toList();
    }

    // ==================== EVENT MANAGEMENT ====================

    /**
     * Record a relationship event
     */
    public RelationshipEvent recordEvent(String personId, String eventType, String title) {
        RelationshipEvent event = new RelationshipEvent(personId, eventType, title);
        List<RelationshipEvent> events = eventsByPerson.computeIfAbsent(personId, k -> new ArrayList<>());
        events.add(event);
        
        // Update relationship closeness based on event
        Relationship relationship = relationships.get(personId);
        if (relationship != null) {
            if (event.isPositive()) {
                relationship.setClosenessLevel(relationship.getClosenessLevel() + 0.05);
            } else {
                relationship.setClosenessLevel(relationship.getClosenessLevel() - 0.05);
            }
        }
        
        // Enforce limit
        while (events.size() > maxEventsPerPerson) {
            events.remove(0);
        }
        
        return event;
    }

    /**
     * Get all events with a person
     */
    public List<RelationshipEvent> getEvents(String personId) {
        List<RelationshipEvent> events = eventsByPerson.get(personId);
        return events != null ? new ArrayList<>(events) : new ArrayList<>();
    }

    /**
     * Get recent events
     */
    public List<RelationshipEvent> getRecentEvents(String personId, int limit) {
        List<RelationshipEvent> events = eventsByPerson.get(personId);
        if (events == null) return new ArrayList<>();
        
        return events.stream()
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .limit(limit)
            .toList();
    }

    /**
     * Get important events
     */
    public List<RelationshipEvent> getImportantEvents(String personId, int minImportance) {
        List<RelationshipEvent> events = eventsByPerson.get(personId);
        if (events == null) return new ArrayList<>();
        
        return events.stream()
            .filter(e -> e.getImportance() >= minImportance)
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .toList();
    }

    // ==================== ANNIVERSARY MANAGEMENT ====================

    /**
     * Record a conversation anniversary
     */
    public ConversationAnniversary recordAnniversary(String personId, LocalDateTime conversationDate, int anniversaryNumber) {
        ConversationAnniversary anniversary = new ConversationAnniversary(personId, conversationDate, anniversaryNumber);
        List<ConversationAnniversary> anniversaries = anniversariesByPerson.computeIfAbsent(personId, k -> new ArrayList<>());
        anniversaries.add(anniversary);
        
        // Enforce limit
        while (anniversaries.size() > maxAnniversariesPerPerson) {
            anniversaries.remove(0);
        }
        
        return anniversary;
    }

    /**
     * Get all anniversaries for a person
     */
    public List<ConversationAnniversary> getAnniversaries(String personId) {
        List<ConversationAnniversary> anniversaries = anniversariesByPerson.get(personId);
        return anniversaries != null ? new ArrayList<>(anniversaries) : new ArrayList<>();
    }

    /**
     * Get recent anniversaries
     */
    public List<ConversationAnniversary> getRecentAnniversaries(String personId, int limit) {
        List<ConversationAnniversary> anniversaries = anniversariesByPerson.get(personId);
        if (anniversaries == null) return new ArrayList<>();
        
        return anniversaries.stream()
            .sorted((a, b) -> b.getAnniversaryDate().compareTo(a.getAnniversaryDate()))
            .limit(limit)
            .toList();
    }

    /**
     * Get all anniversaries across all relationships
     */
    public List<ConversationAnniversary> getAllAnniversaries() {
        List<ConversationAnniversary> allAnniversaries = new ArrayList<>();
        for (List<ConversationAnniversary> anniversaries : anniversariesByPerson.values()) {
            allAnniversaries.addAll(anniversaries);
        }
        return allAnniversaries.stream()
            .sorted((a, b) -> b.getAnniversaryDate().compareTo(a.getAnniversaryDate()))
            .toList();
    }

    // ==================== ACHIEVEMENT MANAGEMENT ====================

    /**
     * Record a user achievement
     */
    public UserAchievement recordAchievement(String personId, String title, String category) {
        UserAchievement achievement = new UserAchievement(personId, title, category);
        List<UserAchievement> achievements = achievementsByPerson.computeIfAbsent(personId, k -> new ArrayList<>());
        achievements.add(achievement);
        
        // Enforce limit
        while (achievements.size() > maxAchievementsPerPerson) {
            achievements.remove(0);
        }
        
        return achievement;
    }

    /**
     * Get all achievements for a person
     */
    public List<UserAchievement> getAchievements(String personId) {
        List<UserAchievement> achievements = achievementsByPerson.get(personId);
        return achievements != null ? new ArrayList<>(achievements) : new ArrayList<>();
    }

    /**
     * Get recent achievements
     */
    public List<UserAchievement> getRecentAchievements(String personId, int limit) {
        List<UserAchievement> achievements = achievementsByPerson.get(personId);
        if (achievements == null) return new ArrayList<>();
        
        return achievements.stream()
            .sorted((a, b) -> b.getAchievedAt().compareTo(a.getAchievedAt()))
            .limit(limit)
            .toList();
    }

    /**
     * Get significant achievements
     */
    public List<UserAchievement> getSignificantAchievements(String personId, int minSignificance) {
        List<UserAchievement> achievements = achievementsByPerson.get(personId);
        if (achievements == null) return new ArrayList<>();
        
        return achievements.stream()
            .filter(a -> a.getSignificance() >= minSignificance)
            .sorted((a, b) -> b.getAchievedAt().compareTo(a.getAchievedAt()))
            .toList();
    }

    /**
     * Get achievements by category
     */
    public List<UserAchievement> getAchievementsByCategory(String personId, String category) {
        List<UserAchievement> achievements = achievementsByPerson.get(personId);
        if (achievements == null) return new ArrayList<>();
        
        return achievements.stream()
            .filter(a -> a.getCategory().equalsIgnoreCase(category))
            .sorted((a, b) -> b.getAchievedAt().compareTo(a.getAchievedAt()))
            .toList();
    }

    /**
     * Get all achievements across all relationships
     */
    public List<UserAchievement> getAllAchievements() {
        List<UserAchievement> allAchievements = new ArrayList<>();
        for (List<UserAchievement> achievements : achievementsByPerson.values()) {
            allAchievements.addAll(achievements);
        }
        return allAchievements.stream()
            .sorted((a, b) -> b.getAchievedAt().compareTo(a.getAchievedAt()))
            .toList();
    }

    // ==================== RECOVERY MOMENT MANAGEMENT ====================

    /**
     * Record a recovery moment
     */
    public RecoveryMoment recordRecoveryMoment(String personId, String type, String title) {
        RecoveryMoment recovery = new RecoveryMoment(personId, type, title);
        List<RecoveryMoment> recoveries = recoveryMomentsByPerson.computeIfAbsent(personId, k -> new ArrayList<>());
        recoveries.add(recovery);
        
        // Enforce limit
        while (recoveries.size() > maxRecoveryMomentsPerPerson) {
            recoveries.remove(0);
        }
        
        return recovery;
    }

    /**
     * Get all recovery moments for a person
     */
    public List<RecoveryMoment> getRecoveryMoments(String personId) {
        List<RecoveryMoment> recoveries = recoveryMomentsByPerson.get(personId);
        return recoveries != null ? new ArrayList<>(recoveries) : new ArrayList<>();
    }

    /**
     * Get recent recovery moments
     */
    public List<RecoveryMoment> getRecentRecoveryMoments(String personId, int limit) {
        List<RecoveryMoment> recoveries = recoveryMomentsByPerson.get(personId);
        if (recoveries == null) return new ArrayList<>();
        
        return recoveries.stream()
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .limit(limit)
            .toList();
    }

    /**
     * Get milestone recovery moments
     */
    public List<RecoveryMoment> getMilestoneRecoveryMoments(String personId) {
        List<RecoveryMoment> recoveries = recoveryMomentsByPerson.get(personId);
        if (recoveries == null) return new ArrayList<>();
        
        return recoveries.stream()
            .filter(RecoveryMoment::isMilestone)
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .toList();
    }

    /**
     * Get all recovery moments across all relationships
     */
    public List<RecoveryMoment> getAllRecoveryMoments() {
        List<RecoveryMoment> allRecoveries = new ArrayList<>();
        for (List<RecoveryMoment> recoveries : recoveryMomentsByPerson.values()) {
            allRecoveries.addAll(recoveries);
        }
        return allRecoveries.stream()
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .toList();
    }

    // ==================== DYNAMICS MANAGEMENT ====================

    /**
     * Record dynamics between two people
     */
    public PeopleDynamics recordDynamics(String personId1, String personId2, String relationship) {
        String key = getDynamicsKey(personId1, personId2);
        return dynamicsBetween.computeIfAbsent(key, 
            k -> new PeopleDynamics(personId1, personId2, relationship));
    }

    /**
     * Get dynamics between two people
     */
    public PeopleDynamics getDynamics(String personId1, String personId2) {
        String key = getDynamicsKey(personId1, personId2);
        return dynamicsBetween.get(key);
    }

    /**
     * Update affinity between two people
     */
    public void updateAffinity(String personId1, String personId2, double affinity) {
        PeopleDynamics dynamics = getDynamics(personId1, personId2);
        if (dynamics != null) {
            dynamics.setAffinity(affinity);
        }
    }

    /**
     * Get all dynamics involving a person
     */
    public List<PeopleDynamics> getDynamicsForPerson(String personId) {
        return dynamicsBetween.values().stream()
            .filter(d -> d.involvesPerson(personId))
            .toList();
    }

    /**
     * Get all known connections between people
     */
    public List<PeopleDynamics> getAllDynamics() {
        return new ArrayList<>(dynamicsBetween.values());
    }

    private String getDynamicsKey(String personId1, String personId2) {
        // Ensure consistent ordering
        if (personId1.compareTo(personId2) > 0) {
            String temp = personId1;
            personId1 = personId2;
            personId2 = temp;
        }
        return personId1 + "-" + personId2;
    }

    // ==================== QUERY METHODS ====================

    /**
     * Get people who were recently interacted with
     */
    public List<Relationship> getRecentlyInteractedWith(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        return relationships.values().stream()
            .filter(r -> r.getLastInteraction().isAfter(cutoff))
            .sorted((a, b) -> b.getLastInteraction().compareTo(a.getLastInteraction()))
            .toList();
    }

    /**
     * Get people who haven't been interacted with in a while
     */
    public List<Relationship> getPeopleToReachOutTo(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        return relationships.values().stream()
            .filter(r -> r.isActive() && r.getLastInteraction().isBefore(cutoff))
            .sorted((a, b) -> a.getLastInteraction().compareTo(b.getLastInteraction()))
            .toList();
    }

    /**
     * Get people with shared interests
     */
    public List<Relationship> getPeopleWithSharedInterest(String interest) {
        String lowerInterest = interest.toLowerCase();
        return relationships.values().stream()
            .filter(r -> r.getSharedInterests().contains(lowerInterest))
            .toList();
    }

    /**
     * Get people by tag
     */
    public List<Relationship> getPeopleByTag(String tag) {
        return relationships.values().stream()
            .filter(r -> r.getTags().contains(tag.toLowerCase()))
            .toList();
    }

    /**
     * Get relationship statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalRelationships", relationships.size());
        
        // Count by type
        Map<String, Long> byType = new HashMap<>();
        for (Relationship r : relationships.values()) {
            byType.merge(r.getRelationshipType(), 1L, Long::sum);
        }
        stats.put("byType", byType);
        
        // Average closeness
        double avgCloseness = relationships.values().stream()
            .mapToDouble(Relationship::getClosenessLevel)
            .average()
            .orElse(0.0);
        stats.put("averageCloseness", avgCloseness);
        
        // Active relationships
        long activeCount = relationships.values().stream()
            .filter(Relationship::isActive)
            .count();
        stats.put("activeRelationships", activeCount);
        
        // Total interactions
        int totalInteractions = interactionsByPerson.values().stream()
            .mapToInt(List::size)
            .sum();
        stats.put("totalInteractions", totalInteractions);
        
        return stats;
    }

    // ==================== SUMMARY METHODS ====================

    /**
     * Get a summary of relationship with a person
     */
    public Map<String, Object> getRelationshipSummary(String personId) {
        Map<String, Object> summary = new HashMap<>();
        
        Relationship relationship = relationships.get(personId);
        if (relationship != null) {
            summary.put("personId", personId);
            summary.put("name", relationship.getName());
            summary.put("type", relationship.getRelationshipType());
            summary.put("closeness", relationship.getClosenessLevel());
            summary.put("interactionCount", relationship.getInteractionCount());
            summary.put("lastInteraction", relationship.getLastInteraction());
            summary.put("sharedInterests", relationship.getSharedInterests());
            summary.put("tags", relationship.getTags());
        }
        
        List<Interaction> interactions = interactionsByPerson.get(personId);
        if (interactions != null) {
            summary.put("totalInteractions", interactions.size());
        }
        
        List<RelationshipEvent> events = eventsByPerson.get(personId);
        if (events != null) {
            summary.put("totalEvents", events.size());
        }
        
        return summary;
    }

    /**
     * Get all people who know each other (dynamics)
     */
    public Map<String, List<String>> getPeopleNetwork() {
        Map<String, List<String>> network = new HashMap<>();
        
        for (PeopleDynamics dynamics : dynamicsBetween.values()) {
            network.computeIfAbsent(dynamics.getPersonId1(), k -> new ArrayList<>())
                .add(dynamics.getPersonId2());
            network.computeIfAbsent(dynamics.getPersonId2(), k -> new ArrayList<>())
                .add(dynamics.getPersonId1());
        }
        
        return network;
    }

    // ==================== PERSISTENCE ====================

    /**
     * Export relationship memory to a serializable format
     */
    public Map<String, Object> exportToMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("currentUserId", currentUserId);
        
        // Export relationships
        List<Map<String, Object>> relationshipsExport = new ArrayList<>();
        for (Relationship r : relationships.values()) {
            Map<String, Object> rel = new HashMap<>();
            rel.put("personId", r.getPersonId());
            rel.put("name", r.getName());
            rel.put("relationshipType", r.getRelationshipType());
            rel.put("description", r.getDescription());
            rel.put("firstMet", r.getFirstMet().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            rel.put("lastInteraction", r.getLastInteraction().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            rel.put("interactionCount", r.getInteractionCount());
            rel.put("closenessLevel", r.getClosenessLevel());
            rel.put("sharedInterests", r.getSharedInterests());
            rel.put("isActive", r.isActive());
            rel.put("tags", r.getTags());
            relationshipsExport.add(rel);
        }
        data.put("relationships", relationshipsExport);
        
        // Export dynamics
        List<Map<String, Object>> dynamicsExport = new ArrayList<>();
        for (PeopleDynamics d : dynamicsBetween.values()) {
            Map<String, Object> dyn = new HashMap<>();
            dyn.put("personId1", d.getPersonId1());
            dyn.put("personId2", d.getPersonId2());
            dyn.put("relationship", d.getRelationship());
            dyn.put("affinity", d.getAffinity());
            dyn.put("sharedContexts", d.getSharedContexts());
            dynamicsExport.add(dyn);
        }
        data.put("dynamics", dynamicsExport);
        
        return data;
    }

    /**
     * Clear all relationship memory data
     */
    public void clearAll() {
        currentUserId = null;
        relationships.clear();
        interactionsByPerson.clear();
        eventsByPerson.clear();
        dynamicsBetween.clear();
    }

    /**
     * Clear data for a specific person
     */
    public void clearPersonData(String personId) {
        relationships.remove(personId);
        interactionsByPerson.remove(personId);
        eventsByPerson.remove(personId);
        
        // Remove related dynamics
        dynamicsBetween.keySet().removeIf(key -> key.contains(personId));
    }

    @Override
    public String toString() {
        return String.format("RelationshipMemory{relationships=%d, interactions=%d, dynamics=%d}",
            relationships.size(), 
            interactionsByPerson.values().stream().mapToInt(List::size).sum(),
            dynamicsBetween.size());
    }
}

