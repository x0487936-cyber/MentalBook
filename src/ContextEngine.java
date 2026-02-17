import java.util.*;
import java.util.regex.*;

/**
 * Context Engine for managing conversation context and memory
 * Integrates entity tracking, pronoun resolution, conversation threads,
 * topic clustering, and open question tracking
 */
public class ContextEngine {

    // ==================== ENUMS ====================

    /**
     * Enum representing different types of memory tiers
     */
    public enum MemoryTier {
        WORKING(60 * 1000, 10),       // 1 minute TTL, max 10 entities
        SHORT_TERM(30 * 60 * 1000, 50), // 30 minutes TTL, max 50 entities
        LONG_TERM(24 * 60 * 60 * 1000L, 200), // 24 hours TTL, max 200 entities
        FLASHBULB(-1, 50);              // No TTL, max 50 entities (permanent)

        private final long timeToLive;
        private final int maxEntities;

        MemoryTier(long timeToLive, int maxEntities) {
            this.timeToLive = timeToLive;
            this.maxEntities = maxEntities;
        }

        public long getTimeToLive() { return timeToLive; }
        public int getMaxEntities() { return maxEntities; }
        public boolean isPermanent() { return timeToLive < 0; }
    }

    /**
     * Enum representing question priority levels
     */
    public enum QuestionPriority {
        LOW(1), MEDIUM(2), HIGH(3), URGENT(4);
        private final int level;
        QuestionPriority(int level) { this.level = level; }
        public int getLevel() { return level; }
    }

    /**
     * Enum representing question statuses
     */
    public enum QuestionStatus {
        OPEN, ANSWERED, DEFERRED, DROPPED
    }

    // ==================== INNER CLASSES ====================

    /**
     * Class for tracking entities in the context
     */
    public static class ReferenceEntity {
        private String entityId;
        private String name;
        private String type;
        private MemoryTier memoryTier;
        private long lastAccessed;
        private long createdAt;
        private int accessCount;
        private Map<String, Object> attributes;

        public ReferenceEntity(String name, String type, MemoryTier memoryTier) {
            this.entityId = UUID.randomUUID().toString();
            this.name = name;
            this.type = type;
            this.memoryTier = memoryTier;
            this.createdAt = System.currentTimeMillis();
            this.lastAccessed = System.currentTimeMillis();
            this.accessCount = 0;
            this.attributes = new HashMap<>();
        }

        public String getEntityId() { return entityId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; updateLastAccessed(); }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; updateLastAccessed(); }
        public MemoryTier getMemoryTier() { return memoryTier; }
        public void setMemoryTier(MemoryTier memoryTier) { this.memoryTier = memoryTier; updateLastAccessed(); }
        public long getLastAccessed() { return lastAccessed; }
        public long getCreatedAt() { return createdAt; }
        public int getAccessCount() { return accessCount; }

        private void updateLastAccessed() {
            this.lastAccessed = System.currentTimeMillis();
            this.accessCount++;
        }

        public void setAttribute(String key, Object value) { attributes.put(key, value); }
        public Object getAttribute(String key) { return attributes.get(key); }
        public Map<String, Object> getAttributes() { return new HashMap<>(attributes); }

        @Override
        public String toString() {
            return "ReferenceEntity{entityId='" + entityId + "', name='" + name + "', type='" + type + "', memoryTier=" + memoryTier + "}";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            return entityId.equals(((ReferenceEntity) obj).entityId);
        }

        @Override
        public int hashCode() { return entityId.hashCode(); }
    }

    /**
     * Class for pronoun resolution in conversation context
     * Handles personal, reflexive, demonstrative, and interrogative pronouns
     */
    public static class ReferenceResolution {
        private Map<String, ReferenceEntity> pronounMap;
        private Set<String> commonPronouns;
        private int resolutionCount;
        private int unresolvedCount;
        private Deque<ReferenceEntity> recentEntities;
        
        // Context-aware tracking for demonstrative pronouns
        private Deque<ReferenceEntity> recentObjects;   // For "it"
        private Deque<ReferenceEntity> recentTopics;    // For "this/that"
        private Deque<ReferenceEntity> recentPlaces;    // For "there"
        private ReferenceEntity lastMentionedEntity;    // Fallback for any pronoun
        
        // Demonstrative pronoun categories
        private static final Set<String> OBJECT_PRONOUNS = Set.of("it", "its");
        private static final Set<String> TOPIC_PRONOUNS = Set.of("this", "that", "these", "those");
        private static final Set<String> PLACE_PRONOUNS = Set.of("there", "here");
        private static final Set<String> PERSON_PRONOUNS = Set.of("he", "him", "his", "she", "her", "hers", "they", "them", "their", "theirs");

        public ReferenceResolution() {
            this.pronounMap = new HashMap<>();
            this.commonPronouns = initializeCommonPronouns();
            this.resolutionCount = 0;
            this.unresolvedCount = 0;
            this.recentEntities = new ArrayDeque<>(10);
            this.recentObjects = new ArrayDeque<>(5);
            this.recentTopics = new ArrayDeque<>(5);
            this.recentPlaces = new ArrayDeque<>(5);
            this.lastMentionedEntity = null;
        }

        private Set<String> initializeCommonPronouns() {
            Set<String> pronouns = new HashSet<>();
            pronouns.addAll(Arrays.asList(
                "i", "me", "my", "mine", "you", "your", "yours", "he", "him", "his", "she", "her", "hers",
                "it", "its", "we", "us", "our", "ours", "they", "them", "their", "theirs",
                "myself", "yourself", "himself", "herself", "itself", "ourselves", "yourselves", "themselves",
                "this", "that", "these", "those", "who", "whom", "whose", "which", "that",
                "what", "where", "when", "why", "how",
                "anyone", "anything", "anybody", "everyone", "everything", "everybody",
                "someone", "something", "somebody", "no one", "nothing", "nobody",
                "here", "there"
            ));
            return pronouns;
        }

        /**
         * Register a pronoun to refer to an entity with category-aware tracking
         */
        public void registerPronoun(String pronoun, ReferenceEntity entity) {
            if (pronoun == null || entity == null) return;
            
            String lowerPronoun = pronoun.toLowerCase();
            pronounMap.put(lowerPronoun, entity);
            
            // Update recent entities
            recentEntities.remove(entity);
            recentEntities.addFirst(entity);
            if (recentEntities.size() > 10) recentEntities.removeLast();
            
            // Update category-specific deques for demonstrative pronouns
            updateCategoryDeque(lowerPronoun, entity);
            
            // Update last mentioned
            lastMentionedEntity = entity;
        }

        /**
         * Update category-specific deque based on pronoun type
         */
        private void updateCategoryDeque(String pronoun, ReferenceEntity entity) {
            Deque<ReferenceEntity> targetDeque;
            
            if (OBJECT_PRONOUNS.contains(pronoun)) {
                targetDeque = recentObjects;
            } else if (TOPIC_PRONOUNS.contains(pronoun)) {
                targetDeque = recentTopics;
            } else if (PLACE_PRONOUNS.contains(pronoun)) {
                targetDeque = recentPlaces;
            } else {
                return; // Don't track non-demonstrative pronouns in special deques
            }
            
            targetDeque.remove(entity);
            targetDeque.addFirst(entity);
        }

        /**
         * Resolve a pronoun to its referenced entity with context awareness
         */
        public ReferenceEntity resolvePronoun(String pronoun) {
            if (pronoun == null) return null;
            
            String lowerPronoun = pronoun.toLowerCase();
            
            // First, check explicit pronoun registrations
            ReferenceEntity entity = pronounMap.get(lowerPronoun);
            if (entity != null) {
                resolutionCount++;
                entity.updateLastAccessed();
                return entity;
            }
            
            // Second, use context-aware resolution for demonstrative pronouns
            entity = resolveDemonstrativePronoun(lowerPronoun);
            if (entity != null) {
                resolutionCount++;
                entity.updateLastAccessed();
                return entity;
            }
            
            // Third, try category-based fallback
            entity = resolveByCategory(lowerPronoun);
            if (entity != null) {
                resolutionCount++;
                entity.updateLastAccessed();
                return entity;
            }
            
            // Finally, return last mentioned entity as fallback
            unresolvedCount++;
            return lastMentionedEntity;
        }

        /**
         * Resolve demonstrative pronouns (it, this, that, etc.) contextually
         */
        private ReferenceEntity resolveDemonstrativePronoun(String pronoun) {
            if (OBJECT_PRONOUNS.contains(pronoun)) {
                // "it" usually refers to objects or abstract concepts
                return findMostRecentEntityByTypes(Set.of("object", "thing", "concept", "book", "game", "item", "idea"));
            } else if (TOPIC_PRONOUNS.contains(pronoun)) {
                // "this/that" usually refers to topics or subjects
                return findMostRecentEntityByTypes(Set.of("topic", "subject", "idea", "question", "matter", "situation"));
            } else if (PLACE_PRONOUNS.contains(pronoun)) {
                // "here/there" usually refers to places
                return findMostRecentEntityByTypes(Set.of("place", "location", "city", "country", "building", "room"));
            }
            return null;
        }

        /**
         * Resolve by pronoun category (person, object, place, etc.)
         */
        private ReferenceEntity resolveByCategory(String pronoun) {
            if (PERSON_PRONOUNS.contains(pronoun)) {
                return findMostRecentEntityByTypes(Set.of("person", "character", "user", "friend"));
            }
            return null;
        }

        /**
         * Find the most recent entity matching any of the given types
         */
        private ReferenceEntity findMostRecentEntityByTypes(Set<String> targetTypes) {
            // First check recentEntities
            for (ReferenceEntity entity : recentEntities) {
                if (targetTypes.stream().anyMatch(t -> t.equalsIgnoreCase(entity.getType()))) {
                    return entity;
                }
            }
            
            // Fallback to last mentioned entity
            return lastMentionedEntity;
        }

        /**
         * Resolve a pronoun with explicit context - for when you know what it refers to
         */
        public ReferenceEntity resolvePronounInContext(String pronoun, String contextHint) {
            ReferenceEntity result = resolvePronoun(pronoun);
            
            // If we have a context hint, try to find a better match
            if (contextHint != null && result == null) {
                for (ReferenceEntity entity : recentEntities) {
                    if (entity.getName().toLowerCase().contains(contextHint.toLowerCase()) ||
                        entity.getType().toLowerCase().contains(contextHint.toLowerCase())) {
                        return entity;
                    }
                }
            }
            
            return result;
        }

        public boolean isPronoun(String word) {
            return word != null && commonPronouns.contains(word.toLowerCase());
        }

        public List<String> findPronouns(String text) {
            List<String> foundPronouns = new ArrayList<>();
            if (text == null) return foundPronouns;
            String[] words = text.split("\\s+");
            for (String word : words) {
                String cleanWord = word.replaceAll("[^a-zA-Z]", "");
                if (isPronoun(cleanWord)) foundPronouns.add(cleanWord.toLowerCase());
            }
            return foundPronouns;
        }

        /**
         * Resolve all pronouns in text with context awareness
         */
        public String resolvePronounsInText(String text) {
            return resolvePronounsInText(text, null);
        }

        /**
         * Resolve pronouns in text with optional context
         */
        public String resolvePronounsInText(String text, String contextHint) {
            if (text == null) return null;
            Pattern wordPattern = Pattern.compile("\\b\\w+\\b");
            Matcher matcher = wordPattern.matcher(text);
            StringBuffer sb = new StringBuffer();

            while (matcher.find()) {
                String word = matcher.group();
                String cleanWord = word.replaceAll("[^a-zA-Z]", "");
                if (isPronoun(cleanWord)) {
                    ReferenceEntity entity = resolvePronounInContext(cleanWord, contextHint);
                    if (entity != null) {
                        matcher.appendReplacement(sb, entity.getName());
                    }
                }
            }
            matcher.appendTail(sb);
            return sb.toString();
        }

        public Set<String> getRegisteredPronouns() { return new HashSet<>(pronounMap.keySet()); }

        public void clearPronouns() {
            pronounMap.clear();
            resolutionCount = 0;
            unresolvedCount = 0;
            recentEntities.clear();
            recentObjects.clear();
            recentTopics.clear();
            recentPlaces.clear();
            lastMentionedEntity = null;
        }

        public Map<String, Object> getStatistics() {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalPronouns", pronounMap.size());
            stats.put("resolutionCount", resolutionCount);
            stats.put("unresolvedCount", unresolvedCount);
            double rate = resolutionCount + unresolvedCount > 0 ? (double) resolutionCount / (resolutionCount + unresolvedCount) : 0.0;
            stats.put("resolutionRate", rate);
            stats.put("recentObjectsCount", recentObjects.size());
            stats.put("recentTopicsCount", recentTopics.size());
            return stats;
        }

        public void updatePronounReference(String oldPronoun, String newPronoun, ReferenceEntity entity) {
            if (oldPronoun != null) pronounMap.remove(oldPronoun.toLowerCase());
            if (newPronoun != null && entity != null) registerPronoun(newPronoun, entity);
        }

        public void removePronoun(String pronoun) {
            if (pronoun != null) pronounMap.remove(pronoun.toLowerCase());
        }

        public ReferenceEntity getMostRecentEntity(String pronounType) {
            return recentEntities.stream()
                .filter(e -> pronounType.equalsIgnoreCase(e.getType()))
                .findFirst()
                .orElse(pronounMap.get(pronounType.toLowerCase()));
        }

        /**
         * Get recently mentioned entities for debugging/display
         */
        public List<ReferenceEntity> getRecentEntities() {
            return new ArrayList<>(recentEntities);
        }

        /**
         * Get recently mentioned objects (for "it" resolution)
         */
        public List<ReferenceEntity> getRecentObjects() {
            return new ArrayList<>(recentObjects);
        }

        /**
         * Get recently mentioned topics (for "this/that" resolution)
         */
        public List<ReferenceEntity> getRecentTopics() {
            return new ArrayList<>(recentTopics);
        }
    }

    /**
     * ConversationThread class for managing topic clusters in conversations
     */
    public static class ConversationThread {

        /**
         * Enum representing thread statuses
         */
        public enum ThreadStatus {
            ACTIVE, PAUSED, COMPLETED, ARCHIVED
        }

        /**
         * Represents a single turn in a conversation thread
         */
        public static class ConversationTurn {
            private String sender;
            private String content;
            private long timestamp;

            public ConversationTurn(String sender, String content) {
                this.sender = sender;
                this.content = content;
                this.timestamp = System.currentTimeMillis();
            }

            public String getSender() { return sender; }
            public String getContent() { return content; }
            public long getTimestamp() { return timestamp; }

            @Override
            public String toString() {
                String truncated = content != null && content.length() > 50 ? content.substring(0, 50) + "..." : content;
                return "ConversationTurn{sender='" + sender + "', content='" + truncated + "'}";
            }
        }

        private String threadId;
        private String topic;
        private List<ConversationTurn> messages;
        private Map<String, Integer> keywordFrequency;
        private ThreadStatus status;
        private long createdAt;
        private long lastActivityAt;
        private Set<String> stopWords;

        public ConversationThread(String topic) {
            this.threadId = UUID.randomUUID().toString();
            this.topic = topic;
            this.messages = new ArrayList<>();
            this.keywordFrequency = new HashMap<>();
            this.status = ThreadStatus.ACTIVE;
            this.createdAt = System.currentTimeMillis();
            this.lastActivityAt = System.currentTimeMillis();
            this.stopWords = initializeStopWords();
        }

        private Set<String> initializeStopWords() {
            Set<String> stopWords = new HashSet<>();
            stopWords.addAll(Arrays.asList(
                "a", "an", "the", "and", "but", "or", "nor", "in", "on", "at", "to", "for",
                "of", "with", "by", "from", "about", "i", "you", "he", "she", "it", "we",
                "they", "me", "him", "her", "us", "them", "my", "your", "his", "its",
                "our", "their", "is", "am", "are", "was", "were", "be", "been", "being",
                "have", "has", "had", "do", "does", "did", "will", "would", "could",
                "should", "may", "might", "can", "must", "this", "that", "these", "those",
                "what", "which", "who", "whom", "whose", "when", "where", "why", "how",
                "all", "each", "every", "both", "few", "more", "most", "other", "some",
                "such", "no", "not", "only", "own", "same", "so", "than", "too", "very",
                "just", "also"
            ));
            return stopWords;
        }

        public void addMessage(String sender, String content) {
            if (content == null) content = "";
            ConversationTurn turn = new ConversationTurn(sender, content);
            messages.add(turn);
            lastActivityAt = System.currentTimeMillis();
            extractKeywords(content);
        }

        private void extractKeywords(String content) {
            if (content == null || content.isEmpty()) return;
            String cleaned = content.toLowerCase().replaceAll("[^a-zA-Z\\s]", "");
            String[] words = cleaned.split("\\s+");
            for (String word : words) {
                if (!word.isEmpty() && word.length() > 2 && !stopWords.contains(word)) {
                    keywordFrequency.merge(word, 1, Integer::sum);
                }
            }
        }

        public int getMessageCount() { return messages.size(); }
        public List<ConversationTurn> getMessages() { return new ArrayList<>(messages); }
        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }
        public String getThreadId() { return threadId; }
        public long getCreatedAt() { return createdAt; }
        public long getLastActivityAt() { return lastActivityAt; }
        public ThreadStatus getStatus() { return status; }
        public void setStatus(ThreadStatus status) { this.status = status; }

        public List<Map.Entry<String, Double>> getTopKeywords() {
            List<Map.Entry<String, Double>> result = new ArrayList<>();
            int maxFreq = keywordFrequency.values().stream().max(Integer::compareTo).orElse(1);
            for (Map.Entry<String, Integer> entry : keywordFrequency.entrySet()) {
                double score = (double) entry.getValue() / maxFreq;
                result.add(new AbstractMap.SimpleEntry<>(entry.getKey(), score));
            }
            result.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
            return result;
        }

        public double getCoherenceScore() {
            if (messages.size() < 2) return 1.0;
            if (keywordFrequency.isEmpty()) return 0.5;
            int total = keywordFrequency.values().stream().mapToInt(Integer::intValue).sum();
            double repeatedRatio = keywordFrequency.values().stream().filter(f -> f > 1).mapToInt(f -> f).sum() / (double) total;
            double lengthFactor = Math.min(1.0, messages.size() / 10.0) * 0.3;
            return Math.min(1.0, Math.max(0.0, repeatedRatio * 0.7 + lengthFactor));
        }

        public double getTopicSimilarity(ConversationThread other) {
            if (other == null) return 0.0;
            Set<String> thisKeys = keywordFrequency.keySet();
            Set<String> otherKeys = other.keywordFrequency.keySet();
            if (thisKeys.isEmpty() && otherKeys.isEmpty()) return 0.5;
            if (thisKeys.isEmpty() || otherKeys.isEmpty()) return 0.2;
            Set<String> intersection = new HashSet<>(thisKeys);
            intersection.retainAll(otherKeys);
            Set<String> union = new HashSet<>(thisKeys);
            union.addAll(otherKeys);
            return (double) intersection.size() / union.size();
        }

        public void mergeThread(ConversationThread other) {
            if (other == null || other == this) return;
            for (ConversationTurn turn : other.messages) {
                addMessage(turn.getSender(), turn.getContent());
            }
            if ((topic == null || topic.isEmpty()) && other.topic != null && !other.topic.isEmpty()) {
                topic = other.topic;
            }
        }

        public String getSummary() {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread: ").append(topic != null ? topic : "Untitled").append("\n");
            sb.append("Messages: ").append(messages.size()).append("\n");
            sb.append("Status: ").append(status).append("\n");
            sb.append("Coherence: ").append(String.format("%.2f", getCoherenceScore()));
            return sb.toString();
        }

        @Override
        public String toString() {
            return "ConversationThread{topic='" + topic + "', messages=" + messages.size() + "}";
        }
    }

    /**
     * OpenQuestion class for tracking unanswered questions
     */
    public static class OpenQuestion {
        private String questionId;
        private String questionText;
        private String askedBy;
        private String context;
        private QuestionPriority priority;
        private QuestionStatus status;
        private long createdAt;
        private long answeredAt;
        private String answerText;
        private String relatedTopic;
        private String relatedEntity;
        private int followUpCount;

        public OpenQuestion(String questionText, String askedBy, String context) {
            this.questionId = UUID.randomUUID().toString();
            this.questionText = questionText;
            this.askedBy = askedBy;
            this.context = context;
            this.priority = QuestionPriority.MEDIUM;
            this.status = QuestionStatus.OPEN;
            this.createdAt = System.currentTimeMillis();
            this.answeredAt = 0;
            this.followUpCount = 0;
        }

        public String getQuestionId() { return questionId; }
        public String getQuestionText() { return questionText; }
        public String getAskedBy() { return askedBy; }
        public String getContext() { return context; }
        public QuestionPriority getPriority() { return priority; }
        public void setPriority(QuestionPriority priority) { this.priority = priority; }
        public QuestionStatus getStatus() { return status; }
        public long getCreatedAt() { return createdAt; }
        public long getAnsweredAt() { return answeredAt; }
        public String getAnswerText() { return answerText; }
        public String getRelatedTopic() { return relatedTopic; }
        public void setRelatedTopic(String topic) { this.relatedTopic = topic; }
        public String getRelatedEntity() { return relatedEntity; }
        public void setRelatedEntity(String entity) { this.relatedEntity = entity; }
        public int getFollowUpCount() { return followUpCount; }
        public void incrementFollowUpCount() { this.followUpCount++; }

        public void markAsAnswered(String answerText) {
            this.status = QuestionStatus.ANSWERED;
            this.answeredAt = System.currentTimeMillis();
            this.answerText = answerText;
        }

        public boolean isAnswered() { return status == QuestionStatus.ANSWERED; }
        public void defer(String reason) { this.status = QuestionStatus.DEFERRED; }
        public void drop() { this.status = QuestionStatus.DROPPED; }

        public long getTimeOpen() {
            return status == QuestionStatus.OPEN ? System.currentTimeMillis() - createdAt : answeredAt - createdAt;
        }

        public String getSummary() {
            StringBuilder sb = new StringBuilder();
            sb.append("Question: ").append(questionText).append("\n");
            sb.append("Asked by: ").append(askedBy).append("\n");
            sb.append("Priority: ").append(priority).append("\n");
            sb.append("Status: ").append(status);
            if (status == QuestionStatus.ANSWERED) {
                sb.append("\nAnswer: ").append(answerText);
            }
            return sb.toString();
        }

        @Override
        public String toString() {
            return "OpenQuestion{questionId='" + questionId + "', priority=" + priority + "}";
        }
    }

    // ==================== MAIN CONTEXT ENGINE ====================

    private Map<String, ReferenceEntity> entities;
    private Map<MemoryTier, Set<String>> tieredEntities;
    private ReferenceResolution referenceResolution;
    private List<ConversationThread> conversationThreads;
    private List<OpenQuestion> openQuestions;
    private ConversationThread currentThread;
    private long lastContextUpdate;
    private int maxWorkingMemory;
    private int maxShortTermMemory;
    private int maxLongTermMemory;

    public ContextEngine() {
        this.entities = new HashMap<>();
        this.tieredEntities = new HashMap<>();
        for (MemoryTier tier : MemoryTier.values()) {
            tieredEntities.put(tier, new LinkedHashSet<>());
        }
        this.referenceResolution = new ReferenceResolution();
        this.conversationThreads = new ArrayList<>();
        this.openQuestions = new ArrayList<>();
        this.currentThread = new ConversationThread("General");
        this.conversationThreads.add(currentThread);
        this.lastContextUpdate = System.currentTimeMillis();
        this.maxWorkingMemory = 10;
        this.maxShortTermMemory = 50;
        this.maxLongTermMemory = 200;
        this.persistenceManager = new PersistenceManager();
    }

    // ==================== ENTITY MANAGEMENT ====================

    public ReferenceEntity addEntity(String name, String type, MemoryTier tier) {
        ReferenceEntity entity = new ReferenceEntity(name, type, tier);
        entities.put(entity.getEntityId(), entity);
        tieredEntities.get(tier).add(entity.getEntityId());
        enforceMemoryLimits(tier);
        return entity;
    }

    public ReferenceEntity getEntity(String entityId) {
        ReferenceEntity entity = entities.get(entityId);
        if (entity != null) entity.updateLastAccessed();
        return entity;
    }

    public ReferenceEntity findEntityByName(String name) {
        return entities.values().stream().filter(e -> name.equalsIgnoreCase(e.getName())).findFirst().orElse(null);
    }

    public List<ReferenceEntity> findEntitiesByType(String type) {
        return entities.values().stream().filter(e -> type.equalsIgnoreCase(e.getType())).toList();
    }

    public void moveEntityToTier(String entityId, MemoryTier newTier) {
        ReferenceEntity entity = entities.get(entityId);
        if (entity != null) {
            tieredEntities.get(entity.getMemoryTier()).remove(entityId);
            entity.setMemoryTier(newTier);
            tieredEntities.get(newTier).add(entityId);
        }
    }

    public void removeEntity(String entityId) {
        ReferenceEntity entity = entities.remove(entityId);
        if (entity != null) {
            tieredEntities.get(entity.getMemoryTier()).remove(entityId);
            referenceResolution.removePronoun(entity.getName());
        }
    }

    public List<ReferenceEntity> getAllEntities() { return new ArrayList<>(entities.values()); }

    public List<ReferenceEntity> getEntitiesByTier(MemoryTier tier) {
        return tieredEntities.get(tier).stream().map(entities::get).filter(Objects::nonNull).toList();
    }

    private void enforceMemoryLimits(MemoryTier tier) {
        Set<String> tierSet = tieredEntities.get(tier);
        int limit = switch (tier) {
            case WORKING -> maxWorkingMemory;
            case SHORT_TERM -> maxShortTermMemory;
            case LONG_TERM -> maxLongTermMemory;
            case FLASHBULB -> Integer.MAX_VALUE;
        };

        while (tierSet.size() > limit) {
            String oldestId = tierSet.iterator().next();
            ReferenceEntity oldest = entities.get(oldestId);
            tierSet.remove(oldestId);
            if (oldest != null && oldest.getMemoryTier() == tier && !tier.isPermanent()) {
                entities.remove(oldestId);
            }
        }
    }

    // ==================== CONVERSATION THREAD MANAGEMENT ====================

    public ConversationThread createThread(String topic) {
        ConversationThread thread = new ConversationThread(topic);
        conversationThreads.add(thread);
        return thread;
    }

    public void setCurrentThread(ConversationThread thread) { this.currentThread = thread; }
    public ConversationThread getCurrentThread() { return currentThread; }

    public void addMessageToCurrentThread(String sender, String content) {
        if (currentThread == null) {
            currentThread = new ConversationThread("General");
            conversationThreads.add(currentThread);
        }
        currentThread.addMessage(sender, content);
        lastContextUpdate = System.currentTimeMillis();
    }

    public List<ConversationThread> getAllThreads() { return new ArrayList<>(conversationThreads); }

    public List<ConversationThread> getActiveThreads() {
        return conversationThreads.stream().filter(t -> t.getStatus() == ConversationThread.ThreadStatus.ACTIVE).toList();
    }

    public ConversationThread findMostSimilarThread(String content) {
        ConversationThread tempThread = new ConversationThread("temp");
        tempThread.addMessage("User", content);
        return conversationThreads.stream().filter(t -> t != currentThread)
            .max((a, b) -> Double.compare(a.getTopicSimilarity(tempThread), b.getTopicSimilarity(tempThread)))
            .orElse(null);
    }

    public void mergeThreads(ConversationThread source, ConversationThread target) {
        target.mergeThread(source);
        source.setStatus(ConversationThread.ThreadStatus.ARCHIVED);
    }

    // ==================== OPEN QUESTION MANAGEMENT ====================

    public OpenQuestion createOpenQuestion(String questionText, String askedBy, String context) {
        OpenQuestion question = new OpenQuestion(questionText, askedBy, context);
        openQuestions.add(question);
        return question;
    }

    public OpenQuestion createOpenQuestion(String questionText, String askedBy, String context, QuestionPriority priority) {
        OpenQuestion question = new OpenQuestion(questionText, askedBy, context);
        question.setPriority(priority);
        openQuestions.add(question);
        return question;
    }

    public void markQuestionAnswered(String questionId, String answer) {
        openQuestions.stream().filter(q -> q.getQuestionId().equals(questionId)).findFirst()
            .ifPresent(q -> q.markAsAnswered(answer));
    }

    public List<OpenQuestion> getOpenQuestions() {
        return openQuestions.stream().filter(q -> q.getStatus() == QuestionStatus.OPEN)
            .sorted((a, b) -> Integer.compare(b.getPriority().getLevel(), a.getPriority().getLevel())).toList();
    }

    public List<OpenQuestion> getAllQuestions() { return new ArrayList<>(openQuestions); }

    public OpenQuestion findOldestOpenQuestion() {
        return openQuestions.stream().filter(q -> q.getStatus() == QuestionStatus.OPEN)
            .min(Comparator.comparingLong(OpenQuestion::getCreatedAt)).orElse(null);
    }

    public OpenQuestion findHighestPriorityQuestion() {
        return openQuestions.stream().filter(q -> q.getStatus() == QuestionStatus.OPEN)
            .max(Comparator.comparingInt(q -> q.getPriority().getLevel())).orElse(null);
    }

    public List<OpenQuestion> getQuestionsForFollowUp() {
        return openQuestions.stream().filter(q -> q.getStatus() == QuestionStatus.OPEN)
            .filter(q -> q.getTimeOpen() > 5 * 60 * 1000).filter(q -> q.getFollowUpCount() < 3)
            .sorted(Comparator.comparingLong(OpenQuestion::getTimeOpen).reversed()).toList();
    }

    // ==================== PRONOUN RESOLUTION ====================

    public void registerReference(String pronoun, ReferenceEntity entity) {
        referenceResolution.registerPronoun(pronoun, entity);
    }

    public ReferenceEntity resolveReference(String pronoun) {
        return referenceResolution.resolvePronoun(pronoun);
    }

    public String resolveReferencesInText(String text) {
        return referenceResolution.resolvePronounsInText(text);
    }

    public Map<String, Object> getReferenceStatistics() {
        return referenceResolution.getStatistics();
    }

    // ==================== CONTEXT SUMMARY ====================

    public Map<String, Object> getContextSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalEntities", entities.size());
        summary.put("workingMemory", getEntitiesByTier(MemoryTier.WORKING).size());
        summary.put("shortTermMemory", getEntitiesByTier(MemoryTier.SHORT_TERM).size());
        summary.put("longTermMemory", getEntitiesByTier(MemoryTier.LONG_TERM).size());
        summary.put("flashbulbMemory", getEntitiesByTier(MemoryTier.FLASHBULB).size());
        summary.put("activeThreads", getActiveThreads().size());
        summary.put("openQuestions", getOpenQuestions().size());
        summary.put("currentThreadTopic", currentThread != null ? currentThread.getTopic() : null);
        summary.put("lastUpdate", lastContextUpdate);
        
        if (currentThread != null) {
            summary.put("currentCoherence", currentThread.getCoherenceScore());
            summary.put("currentKeywords", currentThread.getTopKeywords().stream().limit(5).map(Map.Entry::getKey).toList());
        }
        
        return summary;
    }

    // ==================== CLEANUP ====================

    public void cleanupExpiredEntities() {
        long now = System.currentTimeMillis();
        for (MemoryTier tier : MemoryTier.values()) {
            if (tier.isPermanent()) continue;
            Set<String> tierSet = tieredEntities.get(tier);
            Iterator<String> iterator = tierSet.iterator();
            while (iterator.hasNext()) {
                String entityId = iterator.next();
                ReferenceEntity entity = entities.get(entityId);
                if (entity != null && now - entity.getLastAccessed() > tier.getTimeToLive()) {
                    iterator.remove();
                    entities.remove(entityId);
                }
            }
        }
    }

    public void clearAll() {
        entities.clear();
        tieredEntities.forEach((tier, set) -> set.clear());
        referenceResolution.clearPronouns();
        conversationThreads.clear();
        openQuestions.clear();
        currentThread = new ConversationThread("General");
        conversationThreads.add(currentThread);
        lastContextUpdate = System.currentTimeMillis();
    }

    // ==================== STATISTICS ====================

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("entityCount", entities.size());
        stats.put("threadCount", conversationThreads.size());
        stats.put("activeThreadCount", getActiveThreads().size());
        stats.put("openQuestionCount", getOpenQuestions().size());
        stats.put("resolvedQuestionCount", openQuestions.stream().filter(q -> q.isAnswered()).count());
        stats.putAll(getReferenceStatistics());
        return stats;
    }

    @Override
    public String toString() {
        return String.format("ContextEngine{entities=%d, threads=%d, openQuestions=%d}",
            entities.size(), conversationThreads.size(), getOpenQuestions().size());
    }

    // ==================== IMPLICIT TOPIC TRACKING ====================

    /**
     * ImplicitTopicTracker - Automatically extracts and tracks topics from conversation
     * Uses NLP patterns to identify topics without explicit labeling
     */
    public static class ImplicitTopicTracker {
        
        /**
         * Represents an implicitly detected topic
         */
        public static class TrackedTopic {
            private String topicId;
            private String topicName;
            private String category;
            private double confidence;
            private long firstMention;
            private long lastMention;
            private int mentionCount;
            private Set<String> relatedKeywords;
            private Set<String> contextPhrases;

            public TrackedTopic(String topicName, String category, double confidence) {
                this.topicId = UUID.randomUUID().toString();
                this.topicName = topicName;
                this.category = category;
                this.confidence = confidence;
                this.firstMention = System.currentTimeMillis();
                this.lastMention = System.currentTimeMillis();
                this.mentionCount = 1;
                this.relatedKeywords = new HashSet<>();
                this.contextPhrases = new HashSet<>();
            }

            public void update(double newConfidence) {
                this.confidence = (this.confidence + newConfidence) / 2;
                this.lastMention = System.currentTimeMillis();
                this.mentionCount++;
            }

            public void addKeyword(String keyword) {
                relatedKeywords.add(keyword.toLowerCase());
            }

            public void addContextPhrase(String phrase) {
                contextPhrases.add(phrase.toLowerCase());
            }

            public String getTopicId() { return topicId; }
            public String getTopicName() { return topicName; }
            public String getCategory() { return category; }
            public double getConfidence() { return confidence; }
            public long getFirstMention() { return firstMention; }
            public long getLastMention() { return lastMention; }
            public int getMentionCount() { return mentionCount; }
            public Set<String> getRelatedKeywords() { return new HashSet<>(relatedKeywords); }
            public Set<String> getContextPhrases() { return new HashSet<>(contextPhrases); }
            
            public double getRecencyScore() {
                long timeSinceLast = System.currentTimeMillis() - lastMention;
                return Math.max(0, 1.0 - (timeSinceLast / (1000 * 60 * 30.0)));
            }
            
            public double getSalienceScore() {
                return (confidence * 0.4) + (mentionCount * 0.1) + (getRecencyScore() * 0.5);
            }

            @Override
            public String toString() {
                return String.format("TrackedTopic{name='%s', category='%s', salience=%.2f}", 
                    topicName, category, getSalienceScore());
            }
        }

        private static final Map<String, Set<String>> TOPIC_PATTERNS = new HashMap<>();
        
        static {
            TOPIC_PATTERNS.put("technology", Set.of(
                "computer", "software", "app", "phone", "internet", "code", "programming",
                "ai", "robot", "device", "digital", "online", "website", "data"
            ));
            TOPIC_PATTERNS.put("gaming", Set.of(
                "game", "play", "player", "level", "win", "score", "gaming", "video game",
                "fortnite", "minecraft", "cs2", "console", "gamer", "multiplayer"
            ));
            TOPIC_PATTERNS.put("academics", Set.of(
                "school", "college", "university", "study", "homework", "exam", "test",
                "grade", "class", "subject", "teacher", "assignment", "quiz"
            ));
            TOPIC_PATTERNS.put("relationships", Set.of(
                "friend", "family", "relationship", "dating", "marriage", "love", "partner",
                "parents", "sibling", "boyfriend", "girlfriend"
            ));
            TOPIC_PATTERNS.put("health", Set.of(
                "health", "exercise", "fitness", "workout", "gym", "diet", "nutrition",
                "sleep", "mental health", "anxiety", "depression", "stress"
            ));
            TOPIC_PATTERNS.put("entertainment", Set.of(
                "movie", "film", "series", "show", "music", "song", "book", "reading",
                "tv", "netflix", "youtube", "streaming", "anime"
            ));
            TOPIC_PATTERNS.put("food", Set.of(
                "food", "eat", "cooking", "recipe", "restaurant", "breakfast", "lunch", 
                "dinner", "snack", "healthy", "delicious", "cuisine"
            ));
            TOPIC_PATTERNS.put("travel", Set.of(
                "travel", "trip", "vacation", "holiday", "flight", "hotel", "destination",
                "tourism", "adventure", "country", "city", "beach"
            ));
            TOPIC_PATTERNS.put("work", Set.of(
                "job", "work", "career", "office", "boss", "employee", "salary",
                "interview", "resume", "profession", "business"
            ));
        }

        private Map<String, TrackedTopic> trackedTopics;
        private Deque<TrackedTopic> topicHistory;
        private int maxTrackedTopics;
        private double minConfidence;
        private Pattern ngramPattern;

        public ImplicitTopicTracker() {
            this.trackedTopics = new HashMap<>();
            this.topicHistory = new ArrayDeque<>(20);
            this.maxTrackedTopics = 50;
            this.minConfidence = 0.3;
            this.ngramPattern = Pattern.compile("\\b\\w+(?:\\s+\\w+){0,2}\\b");
        }

        public List<TrackedTopic> processText(String text) {
            List<TrackedTopic> detectedTopics = new ArrayList<>();
            if (text == null || text.isEmpty()) return detectedTopics;

            String lowerText = text.toLowerCase();
            
            for (Map.Entry<String, Set<String>> entry : TOPIC_PATTERNS.entrySet()) {
                String category = entry.getKey();
                Set<String> keywords = entry.getValue();
                
                double maxConfidence = 0;
                String bestMatch = null;
                
                for (String keyword : keywords) {
                    if (lowerText.contains(keyword)) {
                        int count = countOccurrences(lowerText, keyword);
                        double confidence = Math.min(1.0, 0.3 + (count * 0.1));
                        if (confidence > maxConfidence) {
                            maxConfidence = confidence;
                            bestMatch = keyword;
                        }
                    }
                }
                
                if (bestMatch != null && maxConfidence >= minConfidence) {
                    TrackedTopic topic = getOrCreateTopic(bestMatch, category, maxConfidence);
                    detectedTopics.add(topic);
                }
            }
            
            Matcher matcher = ngramPattern.matcher(lowerText);
            while (matcher.find()) {
                String phrase = matcher.group().trim();
                if (phrase.length() > 3 && !isCommonPhrase(phrase)) {
                    TrackedTopic topic = getOrCreateTopic(phrase, "custom", 0.4);
                    topic.addContextPhrase(phrase);
                    if (!detectedTopics.contains(topic)) {
                        detectedTopics.add(topic);
                    }
                }
            }
            
            return detectedTopics;
        }

        private TrackedTopic getOrCreateTopic(String topicName, String category, double confidence) {
            String key = topicName.toLowerCase();
            TrackedTopic topic = trackedTopics.get(key);
            
            if (topic == null) {
                topic = new TrackedTopic(topicName, category, confidence);
                trackedTopics.put(key, topic);
                topicHistory.addFirst(topic);
                
                if (trackedTopics.size() > maxTrackedTopics) {
                    TrackedTopic removed = topicHistory.removeLast();
                    if (removed != null) {
                        trackedTopics.remove(removed.getTopicName().toLowerCase());
                    }
                }
            } else {
                topic.update(confidence);
                topicHistory.remove(topic);
                topicHistory.addFirst(topic);
            }
            
            return topic;
        }

        private int countOccurrences(String text, String word) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b");
            Matcher matcher = pattern.matcher(text);
            int count = 0;
            while (matcher.find()) count++;
            return count;
        }

        private boolean isCommonPhrase(String phrase) {
            Set<String> common = Set.of(
                "i think", "i feel", "i want", "i need", "i like", "i love",
                "what about", "how about", "do you", "can you", "would you"
            );
            return common.contains(phrase);
        }

        public List<TrackedTopic> getTopTopics(int limit) {
            return trackedTopics.values().stream()
                .sorted((a, b) -> Double.compare(b.getSalienceScore(), a.getSalienceScore()))
                .limit(limit)
                .toList();
        }

        public TrackedTopic getDominantTopic() {
            return getTopTopics(1).stream().findFirst().orElse(null);
        }

        public List<TrackedTopic> getRecentTopics(int limit) {
            return topicHistory.stream()
                .filter(t -> System.currentTimeMillis() - t.getLastMention() < 1000 * 60 * 10)
                .limit(limit)
                .toList();
        }

        public List<TrackedTopic> detectTopicTransitions() {
            List<TrackedTopic> transitions = new ArrayList<>();
            TrackedTopic current = getDominantTopic();
            if (current == null) return transitions;

            for (TrackedTopic topic : trackedTopics.values()) {
                if (topic != current && 
                    topic.getMentionCount() > 1 && 
                    topic.getSalienceScore() < current.getSalienceScore() * 0.5) {
                    transitions.add(topic);
                }
            }
            
            transitions.sort((a, b) -> Double.compare(b.getSalienceScore(), a.getSalienceScore()));
            return transitions;
        }

        public List<String> suggestRelatedTopics(String currentTopic) {
            List<String> suggestions = new ArrayList<>();
            TrackedTopic topic = trackedTopics.get(currentTopic.toLowerCase());
            
            if (topic != null) {
                for (TrackedTopic t : trackedTopics.values()) {
                    if (t != topic && t.getCategory().equals(topic.getCategory())) {
                        suggestions.add(t.getTopicName());
                    }
                }
            }
            
            return suggestions.stream().limit(5).toList();
        }

        public boolean hasTopicBeenDiscussed(String topicName) {
            return trackedTopics.containsKey(topicName.toLowerCase());
        }

        public TrackedTopic getTopic(String topicName) {
            return trackedTopics.get(topicName.toLowerCase());
        }

        public void clear() {
            trackedTopics.clear();
            topicHistory.clear();
        }

        public Map<String, Object> getStatistics() {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalTopics", trackedTopics.size());
            stats.put("dominantTopic", getDominantTopic() != null ? getDominantTopic().getTopicName() : null);
            stats.put("categoriesFound", trackedTopics.values().stream()
                .map(TrackedTopic::getCategory)
                .distinct()
                .toList());
            return stats;
        }
    }

    // ==================== PENDING TOPICS QUEUE ====================

    /**
     * PendingTopicsQueue - Manages a queue of topics that need to be addressed
     * Tracks topics that have been mentioned but not yet fully explored
     */
    public static class PendingTopicsQueue {
        
        /**
         * Represents a pending topic in the queue
         */
        public static class PendingTopic {
            private String topicId;
            private String topicName;
            private String context;
            private int priority;
            private long addedAt;
            private int revisitCount;
            private double relevanceScore;
            private Set<String> relatedKeywords;

            public PendingTopic(String topicName, String context, double relevanceScore) {
                this.topicId = UUID.randomUUID().toString();
                this.topicName = topicName;
                this.context = context;
                this.priority = 0;
                this.addedAt = System.currentTimeMillis();
                this.revisitCount = 0;
                this.relevanceScore = relevanceScore;
                this.relatedKeywords = new HashSet<>();
            }

            public void incrementRevisitCount() { this.revisitCount++; }
            public void setPriority(int priority) { this.priority = priority; }

            public String getTopicId() { return topicId; }
            public String getTopicName() { return topicName; }
            public String getContext() { return context; }
            public int getPriority() { return priority; }
            public long getAddedAt() { return addedAt; }
            public int getRevisitCount() { return revisitCount; }
            public double getRelevanceScore() { return relevanceScore; }
            public Set<String> getRelatedKeywords() { return new HashSet<>(relatedKeywords); }
            public void addKeyword(String keyword) { relatedKeywords.add(keyword.toLowerCase()); }

            public long getTimeWaiting() {
                return System.currentTimeMillis() - addedAt;
            }

            public double getUrgencyScore() {
                double recencyBoost = Math.min(1.0, getTimeWaiting() / (1000 * 60 * 60.0)); // 1 hour decay
                return (relevanceScore * 0.5) + (priority * 0.2) + (recencyBoost * 0.3);
            }

            @Override
            public String toString() {
                return String.format("PendingTopic{name='%s', urgency=%.2f}", topicName, getUrgencyScore());
            }
        }

        private Deque<PendingTopic> pendingTopics;
        private Map<String, PendingTopic> topicIndex;
        private int maxSize;
        private double minRelevance;

        public PendingTopicsQueue() {
            this.pendingTopics = new ArrayDeque<>();
            this.topicIndex = new HashMap<>();
            this.maxSize = 20;
            this.minRelevance = 0.3;
        }

        /**
         * Add a topic to the pending queue
         */
        public void addTopic(String topicName, String context, double relevanceScore) {
            if (topicName == null || topicName.trim().isEmpty()) return;
            if (relevanceScore < minRelevance) return;

            String key = topicName.toLowerCase();
            if (topicIndex.containsKey(key)) {
                // Topic already exists, update relevance
                PendingTopic existing = topicIndex.get(key);
                existing.relevanceScore = Math.max(existing.relevanceScore, relevanceScore);
                existing.addedAt = System.currentTimeMillis();
                return;
            }

            PendingTopic topic = new PendingTopic(topicName, context, relevanceScore);
            pendingTopics.addFirst(topic);
            topicIndex.put(key, topic);

            // Enforce max size
            if (pendingTopics.size() > maxSize) {
                PendingTopic removed = pendingTopics.removeLast();
                if (removed != null) {
                    topicIndex.remove(removed.getTopicName().toLowerCase());
                }
            }
        }

        /**
         * Add a topic from detected topics
         */
        public void addFromDetectedTopics(List<ImplicitTopicTracker.TrackedTopic> topics) {
            for (ImplicitTopicTracker.TrackedTopic t : topics) {
                addTopic(t.getTopicName(), t.getCategory(), t.getSalienceScore());
            }
        }

        /**
         * Get the next topic to address (highest urgency)
         */
        public PendingTopic getNextTopic() {
            return pendingTopics.stream()
                .max(Comparator.comparingDouble(PendingTopic::getUrgencyScore))
                .orElse(null);
        }

        /**
         * Get topics sorted by urgency
         */
        public List<PendingTopic> getTopicsByUrgency(int limit) {
            return pendingTopics.stream()
                .sorted((a, b) -> Double.compare(b.getUrgencyScore(), a.getUrgencyScore()))
                .limit(limit)
                .toList();
        }

        /**
         * Get recently added topics
         */
        public List<PendingTopic> getRecentTopics(int limit) {
            return pendingTopics.stream()
                .filter(t -> System.currentTimeMillis() - t.getAddedAt() < 1000 * 60 * 30) // Last 30 mins
                .limit(limit)
                .toList();
        }

        /**
         * Mark a topic as addressed and remove from queue
         */
        public PendingTopic addressTopic(String topicName) {
            String key = topicName.toLowerCase();
            PendingTopic topic = topicIndex.get(key);
            if (topic != null) {
                pendingTopics.remove(topic);
                topicIndex.remove(key);
            }
            return topic;
        }

        /**
         * Defer a topic (move to bottom)
         */
        public void deferTopic(String topicName) {
            String key = topicName.toLowerCase();
            PendingTopic topic = topicIndex.get(key);
            if (topic != null) {
                pendingTopics.remove(topic);
                pendingTopics.addLast(topic);
            }
        }

        /**
         * Bump a topic (move to top)
         */
        public void bumpTopic(String topicName) {
            String key = topicName.toLowerCase();
            PendingTopic topic = topicIndex.get(key);
            if (topic != null) {
                pendingTopics.remove(topic);
                pendingTopics.addFirst(topic);
            }
        }

        /**
         * Check if a topic is in the pending queue
         */
        public boolean hasTopic(String topicName) {
            return topicIndex.containsKey(topicName.toLowerCase());
        }

        /**
         * Get the count of pending topics
         */
        public int size() {
            return pendingTopics.size();
        }

        /**
         * Clear all pending topics
         */
        public void clear() {
            pendingTopics.clear();
            topicIndex.clear();
        }

        /**
         * Get statistics about the pending topics queue
         */
        public Map<String, Object> getStatistics() {
            Map<String, Object> stats = new HashMap<>();
            stats.put("pendingCount", pendingTopics.size());
            stats.put("maxSize", maxSize);
            stats.put("nextTopic", getNextTopic() != null ? getNextTopic().getTopicName() : null);
            
            double avgUrgency = pendingTopics.stream()
                .mapToDouble(PendingTopic::getUrgencyScore)
                .average()
                .orElse(0);
            stats.put("averageUrgency", avgUrgency);
            
            return stats;
        }
    }

    // ==================== MEMORY PERSISTENCE ====================

    /**
     * PersistenceManager - Handles saving and loading context data
     * Supports short-term and long-term memory persistence
     */
    public static class PersistenceManager {
        
        /**
         * Data container for persisted memory
         */
        public static class PersistedData {
            private String version;
            private long timestamp;
            private List<PersistedEntity> entities;
            private List<PersistedThread> threads;
            private List<PersistedQuestion> questions;

            public PersistedData() {
                this.version = "1.0";
                this.timestamp = System.currentTimeMillis();
                this.entities = new ArrayList<>();
                this.threads = new ArrayList<>();
                this.questions = new ArrayList<>();
            }

            public String getVersion() { return version; }
            public void setVersion(String version) { this.version = version; }
            public long getTimestamp() { return timestamp; }
            public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
            public List<PersistedEntity> getEntities() { return entities; }
            public void setEntities(List<PersistedEntity> entities) { this.entities = entities; }
            public List<PersistedThread> getThreads() { return threads; }
            public void setThreads(List<PersistedThread> threads) { this.threads = threads; }
            public List<PersistedQuestion> getQuestions() { return questions; }
            public void setQuestions(List<PersistedQuestion> questions) { this.questions = questions; }
        }

        /**
         * Persisted entity data
         */
        public static class PersistedEntity {
            private String entityId;
            private String name;
            private String type;
            private String memoryTier;
            private long createdAt;
            private Map<String, Object> attributes;

            public String getEntityId() { return entityId; }
            public void setEntityId(String entityId) { this.entityId = entityId; }
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
            public String getType() { return type; }
            public void setType(String type) { this.type = type; }
            public String getMemoryTier() { return memoryTier; }
            public void setMemoryTier(String memoryTier) { this.memoryTier = memoryTier; }
            public long getCreatedAt() { return createdAt; }
            public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
            public Map<String, Object> getAttributes() { return attributes; }
            public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }
        }

        /**
         * Persisted thread data
         */
        public static class PersistedThread {
            private String threadId;
            private String topic;
            private long createdAt;
            private long lastActivityAt;
            private List<PersistedTurn> messages;

            public String getThreadId() { return threadId; }
            public void setThreadId(String threadId) { this.threadId = threadId; }
            public String getTopic() { return topic; }
            public void setTopic(String topic) { this.topic = topic; }
            public long getCreatedAt() { return createdAt; }
            public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
            public long getLastActivityAt() { return lastActivityAt; }
            public void setLastActivityAt(long lastActivityAt) { this.lastActivityAt = lastActivityAt; }
            public List<PersistedTurn> getMessages() { return messages; }
            public void setMessages(List<PersistedTurn> messages) { this.messages = messages; }
        }

        /**
         * Persisted conversation turn
         */
        public static class PersistedTurn {
            private String sender;
            private String content;
            private long timestamp;

            public String getSender() { return sender; }
            public void setSender(String sender) { this.sender = sender; }
            public String getContent() { return content; }
            public void setContent(String content) { this.content = content; }
            public long getTimestamp() { return timestamp; }
            public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        }

        /**
         * Persisted question data
         */
        public static class PersistedQuestion {
            private String questionId;
            private String questionText;
            private String askedBy;
            private String context;
            private String status;
            private long createdAt;
            private String answerText;

            public String getQuestionId() { return questionId; }
            public void setQuestionId(String questionId) { this.questionId = questionId; }
            public String getQuestionText() { return questionText; }
            public void setQuestionText(String questionText) { this.questionText = questionText; }
            public String getAskedBy() { return askedBy; }
            public void setAskedBy(String askedBy) { this.askedBy = askedBy; }
            public String getContext() { return context; }
            public void setContext(String context) { this.context = context; }
            public String getStatus() { return status; }
            public void setStatus(String status) { this.status = status; }
            public long getCreatedAt() { return createdAt; }
            public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
            public String getAnswerText() { return answerText; }
            public void setAnswerText(String answerText) { this.answerText = answerText; }
        }

        private String basePath;

        public PersistenceManager() {
            this.basePath = "data/context";
        }

        public PersistenceManager(String basePath) {
            this.basePath = basePath;
        }

        /**
         * Serialize entity to persisted format
         */
        private PersistedEntity serializeEntity(ReferenceEntity entity) {
            PersistedEntity pe = new PersistedEntity();
            pe.setEntityId(entity.getEntityId());
            pe.setName(entity.getName());
            pe.setType(entity.getType());
            pe.setMemoryTier(entity.getMemoryTier().name());
            pe.setCreatedAt(entity.getCreatedAt());
            pe.setAttributes(entity.getAttributes());
            return pe;
        }

        /**
         * Deserialize entity from persisted format
         */
        private ReferenceEntity deserializeEntity(PersistedEntity pe) {
            ReferenceEntity entity = new ReferenceEntity(
                pe.getName(),
                pe.getType(),
                MemoryTier.valueOf(pe.getMemoryTier())
            );
            // Note: entityId is generated in constructor, can't be restored
            pe.getAttributes().forEach(entity::setAttribute);
            return entity;
        }

        /**
         * Serialize thread to persisted format
         */
        private PersistedThread serializeThread(ConversationThread thread) {
            PersistedThread pt = new PersistedThread();
            pt.setThreadId(thread.getThreadId());
            pt.setTopic(thread.getTopic());
            pt.setCreatedAt(thread.getCreatedAt());
            pt.setLastActivityAt(thread.getLastActivityAt());
            
            List<PersistedTurn> turns = new ArrayList<>();
            for (ConversationThread.ConversationTurn turn : thread.getMessages()) {
                PersistedTurn ptTurn = new PersistedTurn();
                ptTurn.setSender(turn.getSender());
                ptTurn.setContent(turn.getContent());
                ptTurn.setTimestamp(turn.getTimestamp());
                turns.add(ptTurn);
            }
            pt.setMessages(turns);
            return pt;
        }

        /**
         * Deserialize thread from persisted format
         */
        private ConversationThread deserializeThread(PersistedThread pt) {
            ConversationThread thread = new ConversationThread(pt.getTopic());
            // Can't restore threadId, messages will be added
            for (PersistedTurn turn : pt.getMessages()) {
                thread.addMessage(turn.getSender(), turn.getContent());
            }
            return thread;
        }

        /**
         * Serialize question to persisted format
         */
        private PersistedQuestion serializeQuestion(OpenQuestion question) {
            PersistedQuestion pq = new PersistedQuestion();
            pq.setQuestionId(question.getQuestionId());
            pq.setQuestionText(question.getQuestionText());
            pq.setAskedBy(question.getAskedBy());
            pq.setContext(question.getContext());
            pq.setStatus(question.getStatus().name());
            pq.setCreatedAt(question.getCreatedAt());
            pq.setAnswerText(question.getAnswerText());
            return pq;
        }

        /**
         * Deserialize question from persisted format
         */
        private OpenQuestion deserializeQuestion(PersistedQuestion pq) {
            OpenQuestion question = new OpenQuestion(
                pq.getQuestionText(),
                pq.getAskedBy(),
                pq.getContext()
            );
            // Note: questionId is generated in constructor
            return question;
        }

        /**
         * Save short-term memory to file
         */
        public boolean saveShortTermMemory(ContextEngine engine, String filename) {
            return saveToFile(engine, filename, MemoryTier.SHORT_TERM);
        }

        /**
         * Save long-term memory to file
         */
        public boolean saveLongTermMemory(ContextEngine engine, String filename) {
            return saveToFile(engine, filename, MemoryTier.LONG_TERM);
        }

        /**
         * Save specific memory tier to file
         */
        public boolean saveToFile(ContextEngine engine, String filename, MemoryTier tier) {
            try {
                PersistedData data = new PersistedData();
                data.setTimestamp(System.currentTimeMillis());

                // Serialize entities for the tier
                List<PersistedEntity> persistedEntities = new ArrayList<>();
                for (ReferenceEntity entity : engine.getEntitiesByTier(tier)) {
                    persistedEntities.add(serializeEntity(entity));
                }
                data.setEntities(persistedEntities);

                // Serialize active threads for LONG_TERM, or all for SHORT_TERM
                List<PersistedThread> persistedThreads = new ArrayList<>();
                List<ConversationThread> threadsToSave = tier == MemoryTier.LONG_TERM
                    ? engine.getActiveThreads()
                    : engine.getAllThreads();
                for (ConversationThread thread : threadsToSave) {
                    persistedThreads.add(serializeThread(thread));
                }
                data.setThreads(persistedThreads);

                // Serialize open questions
                List<PersistedQuestion> persistedQuestions = new ArrayList<>();
                for (OpenQuestion question : engine.getOpenQuestions()) {
                    persistedQuestions.add(serializeQuestion(question));
                }
                data.setQuestions(persistedQuestions);

                // Simple JSON-like serialization (could use Jackson/Gson for full JSON)
                String serialized = serializeToString(data);
                java.nio.file.Files.write(
                    java.nio.file.Paths.get(basePath + "/" + filename),
                    serialized.getBytes()
                );
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        /**
         * Simple serialization to string (basic format)
         */
        private String serializeToString(PersistedData data) {
            StringBuilder sb = new StringBuilder();
            sb.append("VERSION:").append(data.getVersion()).append("\n");
            sb.append("TIMESTAMP:").append(data.getTimestamp()).append("\n");
            sb.append("ENTITIES:\n");
            for (PersistedEntity entity : data.getEntities()) {
                sb.append("  ENTITY|").append(entity.getEntityId())
                  .append("|").append(entity.getName())
                  .append("|").append(entity.getType())
                  .append("|").append(entity.getMemoryTier())
                  .append("|").append(entity.getCreatedAt()).append("\n");
            }
            sb.append("THREADS:\n");
            for (PersistedThread thread : data.getThreads()) {
                sb.append("  THREAD|").append(thread.getThreadId())
                  .append("|").append(thread.getTopic())
                  .append("|").append(thread.getCreatedAt()).append("\n");
                for (PersistedTurn turn : thread.getMessages()) {
                    sb.append("    TURN|").append(turn.getSender())
                      .append("|").append(turn.getContent().replace("\n", "\\n"))
                      .append("|").append(turn.getTimestamp()).append("\n");
                }
            }
            sb.append("QUESTIONS:\n");
            for (PersistedQuestion question : data.getQuestions()) {
                sb.append("  QUESTION|").append(question.getQuestionId())
                  .append("|").append(question.getQuestionText().replace("|", "\\|"))
                  .append("|").append(question.getAskedBy())
                  .append("|").append(question.getContext())
                  .append("|").append(question.getStatus()).append("\n");
            }
            return sb.toString();
        }

        /**
         * Load short-term memory from file
         */
        public boolean loadShortTermMemory(ContextEngine engine, String filename) {
            return loadFromFile(engine, filename, MemoryTier.SHORT_TERM);
        }

        /**
         * Load long-term memory from file
         */
        public boolean loadLongTermMemory(ContextEngine engine, String filename) {
            return loadFromFile(engine, filename, MemoryTier.LONG_TERM);
        }

        /**
         * Load memory from file
         */
        public boolean loadFromFile(ContextEngine engine, String filename, MemoryTier tier) {
            try {
                java.nio.file.Path path = java.nio.file.Paths.get(basePath + "/" + filename);
                if (!java.nio.file.Files.exists(path)) {
                    return false;
                }

                String content = new String(java.nio.file.Files.readAllBytes(path));
                PersistedData data = deserializeFromString(content);

                // Load entities
                for (PersistedEntity pe : data.getEntities()) {
                    ReferenceEntity entity = deserializeEntity(pe);
                    engine.entities.put(entity.getEntityId(), entity);
                    engine.tieredEntities.get(MemoryTier.valueOf(pe.getMemoryTier())).add(entity.getEntityId());
                }

                // Load threads
                for (PersistedThread pt : data.getThreads()) {
                    ConversationThread thread = deserializeThread(pt);
                    engine.conversationThreads.add(thread);
                }

                // Load questions
                for (PersistedQuestion pq : data.getQuestions()) {
                    OpenQuestion question = deserializeQuestion(pq);
                    engine.openQuestions.add(question);
                }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        /**
         * Simple deserialization from string
         */
        private PersistedData deserializeFromString(String content) {
            PersistedData data = new PersistedData();
            String[] lines = content.split("\n");
            int i = 0;

            if (i < lines.length && lines[i].startsWith("VERSION:")) {
                data.setVersion(lines[i].substring(8));
                i++;
            }
            if (i < lines.length && lines[i].startsWith("TIMESTAMP:")) {
                data.setTimestamp(Long.parseLong(lines[i].substring(10)));
                i++;
            }

            List<PersistedEntity> entities = new ArrayList<>();
            List<PersistedThread> threads = new ArrayList<>();
            List<PersistedQuestion> questions = new ArrayList<>();

            while (i < lines.length) {
                String line = lines[i];
                if (line.equals("ENTITIES:")) {
                    i++;
                    while (i < lines.length && lines[i].startsWith("  ENTITY|")) {
                        String[] parts = lines[i].split("\\|");
                        PersistedEntity pe = new PersistedEntity();
                        pe.setEntityId(parts[1]);
                        pe.setName(parts[2]);
                        pe.setType(parts[3]);
                        pe.setMemoryTier(parts[4]);
                        pe.setCreatedAt(Long.parseLong(parts[5]));
                        entities.add(pe);
                        i++;
                    }
                } else if (line.equals("THREADS:")) {
                    i++;
                    while (i < lines.length && lines[i].startsWith("  THREAD|")) {
                        String[] parts = lines[i].split("\\|");
                        PersistedThread pt = new PersistedThread();
                        pt.setThreadId(parts[1]);
                        pt.setTopic(parts[2]);
                        pt.setCreatedAt(Long.parseLong(parts[3]));
                        i++;

                        List<PersistedTurn> turns = new ArrayList<>();
                        while (i < lines.length && lines[i].startsWith("    TURN|")) {
                            String[] turnParts = lines[i].split("\\|");
                            PersistedTurn ptTurn = new PersistedTurn();
                            ptTurn.setSender(turnParts[1]);
                            ptTurn.setContent(turnParts[2].replace("\\n", "\n"));
                            ptTurn.setTimestamp(Long.parseLong(turnParts[3]));
                            turns.add(ptTurn);
                            i++;
                        }
                        pt.setMessages(turns);
                        threads.add(pt);
                    }
                } else if (line.equals("QUESTIONS:")) {
                    i++;
                    while (i < lines.length && lines[i].startsWith("  QUESTION|")) {
                        String[] parts = lines[i].split("\\|");
                        PersistedQuestion pq = new PersistedQuestion();
                        pq.setQuestionId(parts[1]);
                        pq.setQuestionText(parts[2].replace("\\|", "|"));
                        pq.setAskedBy(parts[3]);
                        pq.setContext(parts[4]);
                        pq.setStatus(parts[5]);
                        questions.add(pq);
                        i++;
                    }
                } else {
                    i++;
                }
            }

            data.setEntities(entities);
            data.setThreads(threads);
            data.setQuestions(questions);
            return data;
        }

        /**
         * Auto-save short-term memory periodically
         */
        public void autoSaveShortTerm(ContextEngine engine, String filename, long intervalMs) {
            java.util.Timer timer = new java.util.Timer();
            timer.schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    saveShortTermMemory(engine, filename);
                }
            }, intervalMs, intervalMs);
        }

        /**
         * Get list of saved memory files
         */
        public List<String> getSavedMemories() {
            List<String> files = new ArrayList<>();
            try {
                java.nio.file.Path path = java.nio.file.Paths.get(basePath);
                if (java.nio.file.Files.exists(path)) {
                    java.nio.file.DirectoryStream<java.nio.file.Path> stream = 
                        java.nio.file.Files.newDirectoryStream(path);
                    for (java.nio.file.Path entry : stream) {
                        if (entry.toString().endsWith(".mem")) {
                            files.add(entry.getFileName().toString());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return files;
        }

        /**
         * Delete a saved memory file
         */
        public boolean deleteMemory(String filename) {
            try {
                return java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(basePath + "/" + filename));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        /**
         * Get file size of saved memory
         */
        public long getMemorySize(String filename) {
            try {
                return java.nio.file.Files.size(java.nio.file.Paths.get(basePath + "/" + filename));
            } catch (Exception e) {
                return -1;
            }
        }
    }

    // ==================== PERSISTENCE CONVENIENCE METHODS ====================

    private PersistenceManager persistenceManager;

    private void initializePersistence() {
        this.persistenceManager = new PersistenceManager();
    }

    /**
     * Save short-term memory to default file
     */
    public boolean saveShortTermMemory() {
        return saveShortTermMemory("shortterm_" + System.currentTimeMillis() + ".mem");
    }

    /**
     * Save short-term memory to specified file
     */
    public boolean saveShortTermMemory(String filename) {
        return persistenceManager.saveShortTermMemory(this, filename);
    }

    /**
     * Save long-term memory to default file
     */
    public boolean saveLongTermMemory() {
        return saveLongTermMemory("longterm_" + System.currentTimeMillis() + ".mem");
    }

    /**
     * Save long-term memory to specified file
     */
    public boolean saveLongTermMemory(String filename) {
        return persistenceManager.saveLongTermMemory(this, filename);
    }

    /**
     * Load short-term memory from file
     */
    public boolean loadShortTermMemory(String filename) {
        return persistenceManager.loadShortTermMemory(this, filename);
    }

    /**
     * Load long-term memory from file
     */
    public boolean loadLongTermMemory(String filename) {
        return persistenceManager.loadLongTermMemory(this, filename);
    }

    /**
     * Get list of saved memory files
     */
    public List<String> getSavedMemories() {
        return persistenceManager.getSavedMemories();
    }

    /**
     * Delete a saved memory file
     */
    public boolean deleteMemory(String filename) {
        return persistenceManager.deleteMemory(filename);
    }

    /**
     * Get size of saved memory file
     */
    public long getMemorySize(String filename) {
        return persistenceManager.getMemorySize(filename);
    }

    /**
     * Promote short-term memories to long-term
     */
    public void promoteToLongTerm() {
        for (ReferenceEntity entity : getEntitiesByTier(MemoryTier.SHORT_TERM)) {
            moveEntityToTier(entity.getEntityId(), MemoryTier.LONG_TERM);
        }
    }

    /**
     * Demote long-term memories to short-term (forgetting)
     */
    public void demoteToShortTerm() {
        for (ReferenceEntity entity : getEntitiesByTier(MemoryTier.LONG_TERM)) {
            if (entity.getAccessCount() < 3) {
                moveEntityToTier(entity.getEntityId(), MemoryTier.SHORT_TERM);
            }
        }
    }
}

