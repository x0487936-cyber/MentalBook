import java.util.*;

/**
 * Test class for ContextEngine classes
 */
public class ContextEngineTest {

    public static void main(String[] args) {
        System.out.println("Starting ContextEngine tests...");

        // Test ReferenceEntity
        testReferenceEntity();

        // Test ReferenceResolution
        testReferenceResolution();

        // Test ConversationThread
        testConversationThread();

        System.out.println("\nAll tests completed successfully!");
    }

    private static void testReferenceEntity() {
        System.out.println("\n=== Testing ReferenceEntity ===");

        // Test 1: Basic instantiation
        System.out.println("Test 1: Basic instantiation");
        ContextEngine.ReferenceEntity entity1 = new ContextEngine.ReferenceEntity("John Doe", "person", ContextEngine.MemoryTier.WORKING);
        System.out.println("Entity created: " + entity1.getName() + ", " + entity1.getType() + ", " + entity1.getMemoryTier());

        // Test 2: Getters
        System.out.println("\nTest 2: Getters");
        System.out.println("Entity ID: " + entity1.getEntityId());
        System.out.println("Name: " + entity1.getName());
        System.out.println("Type: " + entity1.getType());
        System.out.println("Memory Tier: " + entity1.getMemoryTier());
        System.out.println("Last Accessed: " + entity1.getLastAccessed());

        // Test 3: Setters
        System.out.println("\nTest 3: Setters");
        entity1.setName("Jane Doe");
        entity1.setType("character");
        entity1.setMemoryTier(ContextEngine.MemoryTier.LONG_TERM);
        System.out.println("After setters - Name: " + entity1.getName() + ", Type: " + entity1.getType() + ", Memory Tier: " + entity1.getMemoryTier());

        // Test 4: toString
        System.out.println("\nTest 4: toString");
        System.out.println("toString output: " + entity1.toString());

        // Test 5: equals and hashCode
        System.out.println("\nTest 5: equals and hashCode");
        ContextEngine.ReferenceEntity entity2 = new ContextEngine.ReferenceEntity("Jane Doe", "character", ContextEngine.MemoryTier.LONG_TERM);
        ContextEngine.ReferenceEntity entity3 = new ContextEngine.ReferenceEntity("Different", "object", ContextEngine.MemoryTier.FLASHBULB);
        System.out.println("entity1.equals(entity2): " + entity1.equals(entity2)); // Should be false (different IDs)
        System.out.println("entity1.equals(entity1): " + entity1.equals(entity1)); // Should be true
        System.out.println("entity1.equals(null): " + entity1.equals(null)); // Should be false
        System.out.println("entity1.equals(entity3): " + entity1.equals(entity3)); // Should be false
        System.out.println("entity1.hashCode() == entity2.hashCode(): " + (entity1.hashCode() == entity2.hashCode())); // Likely false

        // Test 6: Edge cases
        System.out.println("\nTest 6: Edge cases");
        ContextEngine.ReferenceEntity emptyEntity = new ContextEngine.ReferenceEntity("", "", ContextEngine.MemoryTier.SHORT_TERM);
        System.out.println("Empty entity: " + emptyEntity.toString());

        ContextEngine.ReferenceEntity nullNameEntity = new ContextEngine.ReferenceEntity(null, "type", ContextEngine.MemoryTier.FLASHBULB);
        System.out.println("Null name entity: " + nullNameEntity.toString());

        // Test 7: MemoryTier enum values
        System.out.println("\nTest 7: MemoryTier enum");
        for (ContextEngine.MemoryTier tier : ContextEngine.MemoryTier.values()) {
            System.out.println("Tier: " + tier);
        }
    }

    private static void testReferenceResolution() {
        System.out.println("\n=== Testing ReferenceResolution ===");

        ContextEngine.ReferenceResolution resolver = new ContextEngine.ReferenceResolution();

        // Create test entities
        ContextEngine.ReferenceEntity john = new ContextEngine.ReferenceEntity("John", "person", ContextEngine.MemoryTier.WORKING);
        ContextEngine.ReferenceEntity book = new ContextEngine.ReferenceEntity("The Great Gatsby", "book", ContextEngine.MemoryTier.LONG_TERM);

        // Test 1: Register pronouns
        System.out.println("Test 1: Register pronouns");
        resolver.registerPronoun("he", john);
        resolver.registerPronoun("it", book);
        System.out.println("Registered pronouns: " + resolver.getRegisteredPronouns());

        // Test 2: Resolve pronouns
        System.out.println("\nTest 2: Resolve pronouns");
        ContextEngine.ReferenceEntity resolvedHe = resolver.resolvePronoun("he");
        ContextEngine.ReferenceEntity resolvedIt = resolver.resolvePronoun("it");
        ContextEngine.ReferenceEntity resolvedShe = resolver.resolvePronoun("she");
        System.out.println("he -> " + (resolvedHe != null ? resolvedHe.getName() : "null"));
        System.out.println("it -> " + (resolvedIt != null ? resolvedIt.getName() : "null"));
        System.out.println("she -> " + (resolvedShe != null ? resolvedShe.getName() : "null"));

        // Test 3: Check if word is pronoun
        System.out.println("\nTest 3: Check if word is pronoun");
        System.out.println("Is 'he' a pronoun? " + resolver.isPronoun("he"));
        System.out.println("Is 'cat' a pronoun? " + resolver.isPronoun("cat"));
        System.out.println("Is 'THEY' a pronoun? " + resolver.isPronoun("THEY"));
        System.out.println("Is null a pronoun? " + resolver.isPronoun(null));

        // Test 4: Find pronouns in text
        System.out.println("\nTest 4: Find pronouns in text");
        String text = "He went to the store and bought it for her.";
        List<String> pronouns = resolver.findPronouns(text);
        System.out.println("Text: " + text);
        System.out.println("Found pronouns: " + pronouns);

        // Test 5: Resolve pronouns in text
        System.out.println("\nTest 5: Resolve pronouns in text");
        String resolvedText = resolver.resolvePronounsInText(text);
        System.out.println("Original: " + text);
        System.out.println("Resolved: " + resolvedText);

        // Test 6: Statistics
        System.out.println("\nTest 6: Statistics");
        Map<String, Object> stats = resolver.getStatistics();
        System.out.println("Statistics: " + stats);

        // Test 7: Clear pronouns
        System.out.println("\nTest 7: Clear pronouns");
        resolver.clearPronouns();
        System.out.println("After clear - Registered pronouns: " + resolver.getRegisteredPronouns());

        // Test 8: Edge cases
        System.out.println("\nTest 8: Edge cases");
        System.out.println("Resolve null pronoun: " + resolver.resolvePronoun(null));
        System.out.println("Find pronouns in null text: " + resolver.findPronouns(null));
        System.out.println("Resolve pronouns in null text: " + resolver.resolvePronounsInText(null));
    }

    private static void testConversationThread() {
        System.out.println("\n=== Testing ConversationThread ===");

        // Test 1: Create thread
        System.out.println("Test 1: Create thread");
        ContextEngine.ConversationThread thread = new ContextEngine.ConversationThread("Programming Discussion");
        System.out.println("Created thread: " + thread.getTopic());

        // Test 2: Add messages
        System.out.println("\nTest 2: Add messages");
        thread.addMessage("User", "I love programming in Java");
        thread.addMessage("Xander", "Java is indeed a powerful language with strong typing");
        thread.addMessage("User", "What about Python for beginners?");
        thread.addMessage("Xander", "Python is great for beginners due to its simplicity");
        System.out.println("Thread now has " + thread.getMessageCount() + " messages");

        // Test 3: Get top keywords
        System.out.println("\nTest 3: Get top keywords");
        List<Map.Entry<String, Double>> keywords = thread.getTopKeywords();
        System.out.println("Top keywords:");
        for (int i = 0; i < Math.min(5, keywords.size()); i++) {
            Map.Entry<String, Double> entry = keywords.get(i);
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }

        // Test 4: Coherence score
        System.out.println("\nTest 4: Coherence score");
        System.out.println("Coherence score: " + thread.getCoherenceScore());

        // Test 5: Thread similarity
        System.out.println("\nTest 5: Thread similarity");
        ContextEngine.ConversationThread similarThread = new ContextEngine.ConversationThread("Coding Talk");
        similarThread.addMessage("User", "Java programming is interesting");
        similarThread.addMessage("Xander", "Python is also good for coding");
        double similarity = thread.getTopicSimilarity(similarThread);
        System.out.println("Similarity between threads: " + similarity);

        // Test 6: Merge threads
        System.out.println("\nTest 6: Merge threads");
        int originalCount = thread.getMessageCount();
        thread.mergeThread(similarThread);
        System.out.println("After merge - original: " + originalCount + ", now: " + thread.getMessageCount());

        // Test 7: Thread status
        System.out.println("\nTest 7: Thread status");
        System.out.println("Current status: " + thread.getStatus());
        thread.setStatus(ContextEngine.ConversationThread.ThreadStatus.COMPLETED);
        System.out.println("New status: " + thread.getStatus());

        // Test 8: Summary
        System.out.println("\nTest 8: Summary");
        System.out.println("Thread summary: " + thread.getSummary());

        // Test 9: Conversation turns
        System.out.println("\nTest 9: Conversation turns");
        List<ContextEngine.ConversationThread.ConversationTurn> turns = thread.getMessages();
        System.out.println("First turn: " + turns.get(0).toString());

        // Test 10: Edge cases
        System.out.println("\nTest 10: Edge cases");
        ContextEngine.ConversationThread emptyThread = new ContextEngine.ConversationThread(null);
        System.out.println("Empty thread topic: " + emptyThread.getTopic());
        emptyThread.addMessage("User", null);
        System.out.println("Empty thread message count: " + emptyThread.getMessageCount());
    }
}
