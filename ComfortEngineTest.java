/**
 * ComfortEngine Validation Tests
 * Tests for the ComfortEngine class functionality
 * Part of Phase 4: Code Quality
 */
import java.util.*;

/**
 * ComfortEngineTest - Validation Scripts for ComfortEngine
 */
public class ComfortEngineTest {
    
    private TestFramework test;
    
    public ComfortEngineTest() {
        this.test = TestFramework.getInstance();
    }
    
    // ==================== Constructor Tests ====================
    
    public void testComfortEngineConstruction() {
        ComfortEngine engine = new ComfortEngine();
        
        test.assertNotNull("ComfortEngine should be created", engine);
    }
    
    // ==================== Null Handling Tests ====================
    
    public void testGenerateComfortWithNullEmotionResult() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        String response = engine.generateComfort(null, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
        test.assertTrue("Response should contain gentle presence message", 
            response.toLowerCase().contains("here") || 
            response.toLowerCase().contains("whatever you're feeling"));
    }
    
    public void testGenerateComfortWithNullContext() {
        ComfortEngine engine = new ComfortEngine();
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I'm feeling sad");
        
        String response = engine.generateComfort(emotionResult, null, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    // ==================== Emotion Detection Tests ====================
    
    public void testGenerateComfortWithSadEmotion() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        EmotionDetector detector = new EmotionDetector();
        
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I'm feeling really sad and down");
        
        test.assertTrue("Should detect SAD emotion", 
            emotionResult.getPrimaryEmotion() == EmotionDetector.Emotion.SAD);
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.EMPATHETIC);
        
        test.assertNotNull("Response should not be null", response);
        test.assertTrue("Response should contain comforting text", 
            response.toLowerCase().contains("sorry") || 
            response.toLowerCase().contains("pain") ||
            response.toLowerCase().contains("feelings"));
    }
    
    public void testGenerateComfortWithAnxiousEmotion() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        EmotionDetector detector = new EmotionDetector();
        
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I'm so anxious about this");
        
        test.assertTrue("Should detect ANXIOUS emotion", 
            emotionResult.getPrimaryEmotion() == EmotionDetector.Emotion.ANXIOUS);
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.CALMING);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testGenerateComfortWithStressedEmotion() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        EmotionDetector detector = new EmotionDetector();
        
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I'm so stressed out");
        
        test.assertTrue("Should detect STRESSED emotion", 
            emotionResult.getPrimaryEmotion() == EmotionDetector.Emotion.STRESSED);
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.UPLIFTING);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testGenerateComfortWithFrustratedEmotion() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        EmotionDetector detector = new EmotionDetector();
        
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I'm so frustrated");
        
        test.assertTrue("Should detect FRUSTRATED emotion", 
            emotionResult.getPrimaryEmotion() == EmotionDetector.Emotion.FRUSTRATED);
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.REASSURING);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testGenerateComfortWithOverwhelmedEmotion() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        EmotionDetector detector = new EmotionDetector();
        
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I feel overwhelmed");
        
        test.assertTrue("Should detect OVERWHELMED emotion", 
            emotionResult.getPrimaryEmotion() == EmotionDetector.Emotion.OVERWHELMED);
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    // ==================== Neutral Emotion Tests ====================
    
    public void testGenerateComfortWithNeutralEmotion() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        EmotionDetector detector = new EmotionDetector();
        
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("Hello there");
        
        // May not be NEUTRAL but should still produce a response
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.BALANCED);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    // ==================== Intensity Tests ====================
    
    public void testGenerateComfortHighIntensity() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        // Create a high intensity emotion result directly
        EmotionDetector.EmotionResult highIntensitySad = 
            new EmotionDetector.EmotionResult(EmotionDetector.Emotion.SAD, 0.9, new HashMap<>());
        
        String response = engine.generateComfort(highIntensitySad, context, ComfortEngine.ToneStyle.EMPATHETIC);
        
        test.assertNotNull("Response should not be null", response);
        // High intensity should trigger more empathetic response
    }
    
    public void testGenerateComfortLowIntensity() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        // Create a low intensity emotion result directly
        EmotionDetector.EmotionResult lowIntensitySad = 
            new EmotionDetector.EmotionResult(EmotionDetector.Emotion.SAD, 0.3, new HashMap<>());
        
        String response = engine.generateComfort(lowIntensitySad, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    // ==================== Severe Distress Tests ====================
    
    public void testGenerateComfortWithHopelessKeyword() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        // Add turn with "hopeless" keyword
        context.addTurn("I feel hopeless", "I'm sorry to hear that", "support");
        
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I feel hopeless");
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
        test.assertTrue("Response should contain escalation message", 
            response.toLowerCase().contains("you matter") || 
            response.toLowerCase().contains("support") ||
            response.toLowerCase().contains("reach out"));
    }
    
    public void testGenerateComfortWithWorthlessKeyword() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        context.addTurn("I feel worthless", "That's not true", "support");
        
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I feel worthless");
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
        test.assertTrue("Response should contain supportive escalation", 
            response.toLowerCase().contains("you matter"));
    }
    
    public void testGenerateComfortWithNoPointKeyword() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        context.addTurn("There's no point", "That's not true", "support");
        
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("There's no point");
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testGenerateComfortWithGiveUpKeyword() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        context.addTurn("I want to give up", "Please don't", "support");
        
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I want to give up");
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    // ==================== Context-Aware Reinforcement Tests ====================
    
    public void testGenerateComfortContextReinforcement() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        // Add 5+ turns to trigger context reinforcement
        context.addTurn("hello", "Hi there!", "greeting");
        context.addTurn("how are you", "I'm good!", "general");
        context.addTurn("I'm feeling down", "I'm sorry to hear that", "support");
        context.addTurn("it's been rough", "Can you tell me more?", "support");
        context.addTurn("I don't know what to do", "I'm here for you", "support");
        context.addTurn("I'm sad", "Let me help", "support");
        
        test.assertTrue("Turn count should be > 4", context.getTurnCount() > 4);
        
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I'm sad");
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.EMPATHETIC);
        
        test.assertNotNull("Response should not be null", response);
        // Response should contain context-aware reinforcement
        test.assertTrue("Response should mention they've been on user's mind", 
            response.toLowerCase().contains("been on your mind") ||
            response.toLowerCase().contains("can tell"));
    }
    
    public void testGenerateComfortNoContextReinforcement() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        // Only 1 turn - should not trigger reinforcement
        context.addTurn("I'm sad", "I'm sorry", "support");
        
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I'm sad");
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    // ==================== ToneStyle Tests ====================
    
    public void testGenerateComfortWithAllToneStyles() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I'm feeling sad");
        
        for (ComfortEngine.ToneStyle tone : ComfortEngine.ToneStyle.values()) {
            String response = engine.generateComfort(emotionResult, context, tone);
            test.assertNotNull("Response should not be null for tone: " + tone, response);
        }
    }
    
    public void testGenerateComfortWithSoftTone() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I'm sad");
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testGenerateComfortWithEncouragingTone() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I'm stressed");
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.ENCOURAGING);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testGenerateComfortWithDirectTone() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I'm frustrated");
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.DIRECT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    // ==================== Emotion Strategy Tests ====================
    
    public void testSadnessStrategyHighIntensity() {
        ComfortEngine engine = new ComfortEngine();
        
        // Test via reflection or direct method call
        // We'll test through generateComfort
        EmotionDetector.EmotionResult highIntensitySad = 
            new EmotionDetector.EmotionResult(EmotionDetector.Emotion.SAD, 0.85, new HashMap<>());
        
        ConversationContext context = new ConversationContext();
        String response = engine.generateComfort(highIntensitySad, context, ComfortEngine.ToneStyle.EMPATHETIC);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testSadnessStrategyLowIntensity() {
        ComfortEngine engine = new ComfortEngine();
        
        EmotionDetector.EmotionResult lowIntensitySad = 
            new EmotionDetector.EmotionResult(EmotionDetector.Emotion.SAD, 0.4, new HashMap<>());
        
        ConversationContext context = new ConversationContext();
        String response = engine.generateComfort(lowIntensitySad, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testAnxietyStrategyHighIntensity() {
        ComfortEngine engine = new ComfortEngine();
        
        EmotionDetector.EmotionResult highIntensityAnxious = 
            new EmotionDetector.EmotionResult(EmotionDetector.Emotion.ANXIOUS, 0.9, new HashMap<>());
        
        ConversationContext context = new ConversationContext();
        String response = engine.generateComfort(highIntensityAnxious, context, ComfortEngine.ToneStyle.CALMING);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testStressStrategy() {
        ComfortEngine engine = new ComfortEngine();
        
        EmotionDetector.EmotionResult stressedResult = 
            new EmotionDetector.EmotionResult(EmotionDetector.Emotion.STRESSED, 0.6, new HashMap<>());
        
        ConversationContext context = new ConversationContext();
        String response = engine.generateComfort(stressedResult, context, ComfortEngine.ToneStyle.UPLIFTING);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testFrustrationStrategy() {
        ComfortEngine engine = new ComfortEngine();
        
        EmotionDetector.EmotionResult frustratedResult = 
            new EmotionDetector.EmotionResult(EmotionDetector.Emotion.FRUSTRATED, 0.8, new HashMap<>());
        
        ConversationContext context = new ConversationContext();
        String response = engine.generateComfort(frustratedResult, context, ComfortEngine.ToneStyle.REASSURING);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testOverwhelmStrategy() {
        ComfortEngine engine = new ComfortEngine();
        
        EmotionDetector.EmotionResult overwhelmedResult = 
            new EmotionDetector.EmotionResult(EmotionDetector.Emotion.OVERWHELMED, 0.5, new HashMap<>());
        
        ConversationContext context = new ConversationContext();
        String response = engine.generateComfort(overwhelmedResult, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testNeutralStrategy() {
        ComfortEngine engine = new ComfortEngine();
        
        // Use an emotion not in the strategies map
        EmotionDetector.EmotionResult happyResult = 
            new EmotionDetector.EmotionResult(EmotionDetector.Emotion.HAPPY, 0.5, new HashMap<>());
        
        ConversationContext context = new ConversationContext();
        String response = engine.generateComfort(happyResult, context, ComfortEngine.ToneStyle.BALANCED);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    // ==================== Edge Case Tests ====================
    
    public void testGenerateComfortWithEmptyInput() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        EmotionDetector detector = new EmotionDetector();
        
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("");
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testGenerateComfortWithVeryLowIntensity() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        EmotionDetector.EmotionResult veryLowIntensity = 
            new EmotionDetector.EmotionResult(EmotionDetector.Emotion.SAD, 0.05, new HashMap<>());
        
        String response = engine.generateComfort(veryLowIntensity, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testGenerateComfortWithVeryHighIntensity() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        EmotionDetector.EmotionResult veryHighIntensity = 
            new EmotionDetector.EmotionResult(EmotionDetector.Emotion.ANXIOUS, 0.99, new HashMap<>());
        
        String response = engine.generateComfort(veryHighIntensity, context, ComfortEngine.ToneStyle.CALMING);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testGenerateComfortResponseVariety() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I'm sad");
        
        Set<String> responses = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            // Note: Since strategy doesn't use randomness, responses will be the same
            // This test is more for future-proofing if randomness is added
            String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.SOFT);
            responses.add(response);
        }
        
        test.assertNotNull("Should have at least one response", responses.size() >= 1);
    }
    
    // ==================== Severe Distress Null Context Tests ====================
    
    public void testSevereDistressWithNullContext() {
        ComfortEngine engine = new ComfortEngine();
        
        // Should not throw exception with null context
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("I feel hopeless");
        
        String response = engine.generateComfort(emotionResult, null, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testSevereDistressWithNullLastInput() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        
        // Context with null last user input
        // Don't add any turns - last input will be null
        
        EmotionDetector detector = new EmotionDetector();
        EmotionDetector.EmotionResult emotionResult = detector.detectEmotion("Hello");
        
        String response = engine.generateComfort(emotionResult, context, ComfortEngine.ToneStyle.SOFT);
        
        test.assertNotNull("Response should not be null", response);
    }
    
    // ==================== Multiple Emotions in Sequence Tests ====================
    
    public void testMultipleEmotionsInSequence() {
        ComfortEngine engine = new ComfortEngine();
        ConversationContext context = new ConversationContext();
        EmotionDetector detector = new EmotionDetector();
        
        // Test SAD
        EmotionDetector.EmotionResult sadResult = detector.detectEmotion("I'm really sad");
        String sadResponse = engine.generateComfort(sadResult, context, ComfortEngine.ToneStyle.EMPATHETIC);
        test.assertNotNull("Sad response should not be null", sadResponse);
        
        // Test ANXIOUS
        EmotionDetector.EmotionResult anxiousResult = detector.detectEmotion("I'm anxious");
        String anxiousResponse = engine.generateComfort(anxiousResult, context, ComfortEngine.ToneStyle.CALMING);
        test.assertNotNull("Anxious response should not be null", anxiousResponse);
        
        // Test STRESSED
        EmotionDetector.EmotionResult stressedResult = detector.detectEmotion("I'm stressed");
        String stressedResponse = engine.generateComfort(stressedResult, context, ComfortEngine.ToneStyle.UPLIFTING);
        test.assertNotNull("Stressed response should not be null", stressedResponse);
        
        // Test FRUSTRATED
        EmotionDetector.EmotionResult frustratedResult = detector.detectEmotion("I'm frustrated");
        String frustratedResponse = engine.generateComfort(frustratedResult, context, ComfortEngine.ToneStyle.REASSURING);
        test.assertNotNull("Frustrated response should not be null", frustratedResponse);
        
        // Test OVERWHELMED
        EmotionDetector.EmotionResult overwhelmedResult = detector.detectEmotion("I'm overwhelmed");
        String overwhelmedResponse = engine.generateComfort(overwhelmedResult, context, ComfortEngine.ToneStyle.SOFT);
        test.assertNotNull("Overwhelmed response should not be null", overwhelmedResponse);
    }
    
    // ==================== Test Runner ====================
    
    public static void main(String[] args) {
        System.out.println("Running ComfortEngine Validation Tests...\n");
        
        ComfortEngineTest tests = new ComfortEngineTest();
        
        TestFramework testFramework = TestFramework.getInstance();
        
        // Constructor Tests
        testFramework.runTest("ComfortEngine - Construction", () -> tests.testComfortEngineConstruction());
        
        // Null Handling Tests
        testFramework.runTest("ComfortEngine - Null EmotionResult", () -> tests.testGenerateComfortWithNullEmotionResult());
        testFramework.runTest("ComfortEngine - Null Context", () -> tests.testGenerateComfortWithNullContext());
        
        // Emotion Detection Tests
        testFramework.runTest("ComfortEngine - Sad Emotion", () -> tests.testGenerateComfortWithSadEmotion());
        testFramework.runTest("ComfortEngine - Anxious Emotion", () -> tests.testGenerateComfortWithAnxiousEmotion());
        testFramework.runTest("ComfortEngine - Stressed Emotion", () -> tests.testGenerateComfortWithStressedEmotion());
        testFramework.runTest("ComfortEngine - Frustrated Emotion", () -> tests.testGenerateComfortWithFrustratedEmotion());
        testFramework.runTest("ComfortEngine - Overwhelmed Emotion", () -> tests.testGenerateComfortWithOverwhelmedEmotion());
        testFramework.runTest("ComfortEngine - Neutral Emotion", () -> tests.testGenerateComfortWithNeutralEmotion());
        
        // Intensity Tests
        testFramework.runTest("ComfortEngine - High Intensity", () -> tests.testGenerateComfortHighIntensity());
        testFramework.runTest("ComfortEngine - Low Intensity", () -> tests.testGenerateComfortLowIntensity());
        
        // Severe Distress Tests
        testFramework.runTest("ComfortEngine - Hopeless Keyword", () -> tests.testGenerateComfortWithHopelessKeyword());
        testFramework.runTest("ComfortEngine - Worthless Keyword", () -> tests.testGenerateComfortWithWorthlessKeyword());
        testFramework.runTest("ComfortEngine - No Point Keyword", () -> tests.testGenerateComfortWithNoPointKeyword());
        testFramework.runTest("ComfortEngine - Give Up Keyword", () -> tests.testGenerateComfortWithGiveUpKeyword());
        
        // Context-Aware Reinforcement Tests
        testFramework.runTest("ComfortEngine - Context Reinforcement Active", () -> tests.testGenerateComfortContextReinforcement());
        testFramework.runTest("ComfortEngine - Context Reinforcement Inactive", () -> tests.testGenerateComfortNoContextReinforcement());
        
        // ToneStyle Tests
        testFramework.runTest("ComfortEngine - All ToneStyles", () -> tests.testGenerateComfortWithAllToneStyles());
        testFramework.runTest("ComfortEngine - Soft Tone", () -> tests.testGenerateComfortWithSoftTone());
        testFramework.runTest("ComfortEngine - Encouraging Tone", () -> tests.testGenerateComfortWithEncouragingTone());
        testFramework.runTest("ComfortEngine - Direct Tone", () -> tests.testGenerateComfortWithDirectTone());
        
        // Strategy Tests
        testFramework.runTest("ComfortEngine - Sadness Strategy High Intensity", () -> tests.testSadnessStrategyHighIntensity());
        testFramework.runTest("ComfortEngine - Sadness Strategy Low Intensity", () -> tests.testSadnessStrategyLowIntensity());
        testFramework.runTest("ComfortEngine - Anxiety Strategy High Intensity", () -> tests.testAnxietyStrategyHighIntensity());
        testFramework.runTest("ComfortEngine - Stress Strategy", () -> tests.testStressStrategy());
        testFramework.runTest("ComfortEngine - Frustration Strategy", () -> tests.testFrustrationStrategy());
        testFramework.runTest("ComfortEngine - Overwhelm Strategy", () -> tests.testOverwhelmStrategy());
        testFramework.runTest("ComfortEngine - Neutral Strategy", () -> tests.testNeutralStrategy());
        
        // Edge Case Tests
        testFramework.runTest("ComfortEngine - Empty Input", () -> tests.testGenerateComfortWithEmptyInput());
        testFramework.runTest("ComfortEngine - Very Low Intensity", () -> tests.testGenerateComfortWithVeryLowIntensity());
        testFramework.runTest("ComfortEngine - Very High Intensity", () -> tests.testGenerateComfortWithVeryHighIntensity());
        testFramework.runTest("ComfortEngine - Response Variety", () -> tests.testGenerateComfortResponseVariety());
        
        // Severe Distress Null Context Tests
        testFramework.runTest("ComfortEngine - Severe Distress Null Context", () -> tests.testSevereDistressWithNullContext());
        testFramework.runTest("ComfortEngine - Severe Distress Null Last Input", () -> tests.testSevereDistressWithNullLastInput());
        
        // Multiple Emotions Tests
        testFramework.runTest("ComfortEngine - Multiple Emotions Sequence", () -> tests.testMultipleEmotionsInSequence());
        
        // Print results
        testFramework.printResults();
        
        // Exit with appropriate code
        System.exit(testFramework.allTestsPassed() ? 0 : 1);
    }
}

