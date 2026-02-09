/**
 * Unit Tests for VirtualXander Core Components
 * Part of Phase 4: Code Quality
 */
import java.util.*;

/**
 * VirtualXanderTests - Unit Tests for VirtualXander
 */
public class VirtualXanderTests {
    
    private TestFramework test;
    
    public VirtualXanderTests() {
        this.test = TestFramework.getInstance();
    }
    
    // ==================== IntentRecognizer Tests ====================
    
    public void testIntentRecognizerGreeting() {
        IntentRecognizer recognizer = new IntentRecognizer();
        
        test.assertTrue("Should recognize greeting 'hello'", 
            recognizer.recognizeIntent("hello").equals("greeting"));
        
        test.assertTrue("Should recognize greeting 'hi'", 
            recognizer.recognizeIntent("hi").equals("greeting"));
        
        test.assertTrue("Should recognize greeting 'hey'", 
            recognizer.recognizeIntent("hey").equals("greeting"));
        
        test.assertTrue("Should recognize identity question", 
            recognizer.recognizeIntent("what's your name?").equals("identity"));
        
        test.assertTrue("Should recognize farewell", 
            recognizer.recognizeIntent("goodbye").equals("farewell"));
    }
    
    public void testIntentRecognizerWellbeing() {
        IntentRecognizer recognizer = new IntentRecognizer();
        
        test.assertTrue("Should recognize 'how are you'", 
            recognizer.recognizeIntent("how are you").equals("wellbeing_how"));
        
        test.assertTrue("Should recognize positive wellbeing", 
            recognizer.recognizeIntent("I'm doing great").equals("wellbeing_positive"));
        
        test.assertTrue("Should recognize negative wellbeing", 
            recognizer.recognizeIntent("I'm feeling sad").equals("wellbeing_negative"));
    }
    
    public void testIntentRecognizerHomework() {
        IntentRecognizer recognizer = new IntentRecognizer();
        
        test.assertTrue("Should recognize homework help request", 
            recognizer.recognizeIntent("I need help with homework").equals("homework_help"));
        
        test.assertTrue("Should recognize math subject", 
            recognizer.recognizeIntent("math is hard").contains("homework"));
    }
    
    public void testIntentRecognizerEmotion() {
        IntentRecognizer recognizer = new IntentRecognizer();
        
        test.assertNotNull("Should extract positive emotions", 
            recognizer.extractEntities("I'm so happy", "emotion"));
        
        test.assertNotNull("Should extract subjects", 
            recognizer.extractEntities("I love math", "subject"));
    }
    
    // ==================== ConversationContext Tests ====================
    
    public void testConversationContextBasic() {
        ConversationContext context = new ConversationContext();
        
        test.assertEquals("Initial state should be 'idle'", 
            "idle", context.getCurrentState());
        
        test.assertEquals("Initial topic should be 'general'", 
            "general", context.getCurrentTopic());
        
        test.assertEquals("Initial turn count should be 0", 
            0, context.getTurnCount());
        
        test.assertNull("Last user input should be null initially", 
            context.getLastUserInput());
    }
    
    public void testConversationContextTurns() {
        ConversationContext context = new ConversationContext();
        
        context.addTurn("hello", "greeting", "Hi there!", "greeting");
        
        test.assertEquals("Turn count should be 1", 
            1, context.getTurnCount());
        
        test.assertEquals("Last user input should be 'hello'", 
            "hello", context.getLastUserInput());
        
        test.assertEquals("Current topic should be 'greeting'", 
            "greeting", context.getCurrentTopic());
        
        context.addTurn("how are you", "wellbeing_how", "I'm good!", "general");
        
        test.assertEquals("Turn count should be 2", 
            2, context.getTurnCount());
        
        test.assertEquals("Recent history should have 4 entries", 
            4, context.getRecentHistory(10).size());
    }
    
    public void testConversationContextReset() {
        ConversationContext context = new ConversationContext();
        
        context.addTurn("hello", "greeting", "Hi!", "greeting");
        context.addTurn("help", "help_request", "Sure!", "general");
        
        test.assertEquals("Turn count should be 2", 
            2, context.getTurnCount());
        
        context.reset();
        
        test.assertEquals("Turn count should be 0 after reset", 
            0, context.getTurnCount());
        
        test.assertNull("Last user input should be null after reset", 
            context.getLastUserInput());
    }
    
    public void testConversationContextSessionData() {
        ConversationContext context = new ConversationContext();
        
        context.setSessionData("username", "John");
        context.setSessionData("age", 25);
        
        test.assertEquals("Should retrieve username", 
            "John", context.getSessionData("username"));
        
        test.assertEquals("Should retrieve age", 
            Integer.valueOf(25), context.getSessionData("age"));
        
        context.removeSessionData("age");
        
        test.assertNull("Age should be null after removal", 
            context.getSessionData("age"));
    }
    
    // ==================== ResponseGenerator Tests ====================
    
    public void testResponseGeneratorBasic() {
        ResponseGenerator generator = new ResponseGenerator();
        ConversationContext context = new ConversationContext();
        
        String response = generator.generateResponse("greeting", "hello", context);
        
        test.assertNotNull("Response should not be null", response);
        test.assertTrue("Response should contain greeting", 
            response.toLowerCase().contains("hi") || 
            response.toLowerCase().contains("hello") ||
            response.toLowerCase().contains("hey"));
    }
    
    public void testResponseGeneratorVariety() {
        ResponseGenerator generator = new ResponseGenerator();
        ConversationContext context = new ConversationContext();
        
        Set<String> responses = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            responses.add(generator.generateResponse("greeting", "hello", context));
        }
        
        test.assertTrue("Should have varied responses", responses.size() > 1);
    }
    
    // ==================== EmotionDetector Tests ====================
    
    public void testEmotionDetectorBasic() {
        EmotionDetector detector = new EmotionDetector();
        
        EmotionDetector.EmotionResult happyResult = detector.detectEmotion("I'm so happy today!");
        test.assertTrue("Should detect happy emotion", 
            happyResult.getPrimaryEmotion() == EmotionDetector.Emotion.HAPPY);
        
        EmotionDetector.EmotionResult sadResult = detector.detectEmotion("I'm feeling sad");
        test.assertTrue("Should detect sad emotion", 
            sadResult.getPrimaryEmotion() == EmotionDetector.Emotion.SAD);
        
        EmotionDetector.EmotionResult stressedResult = detector.detectEmotion("I'm so stressed about this");
        test.assertTrue("Should detect stressed emotion", 
            stressedResult.getPrimaryEmotion() == EmotionDetector.Emotion.STRESSED);
    }
    
    public void testEmotionDetectorConfidence() {
        EmotionDetector detector = new EmotionDetector();
        
        EmotionDetector.EmotionResult result = detector.detectEmotion("I'm extremely happy and excited!");
        
        test.assertTrue("Confidence should be positive", 
            result.getConfidence() > 0);
        test.assertTrue("Should be positive emotion", 
            result.isPositive());
        test.assertFalse("Should not be negative emotion", 
            result.isNegative());
    }
    
    public void testEmotionDetectorSupportiveResponse() {
        EmotionDetector detector = new EmotionDetector();
        
        String response = detector.getSupportiveResponse(EmotionDetector.Emotion.SAD);
        
        test.assertNotNull("Supportive response should not be null", response);
        test.assertTrue("Response should contain supportive text", 
            response.toLowerCase().contains("sorry") || 
            response.toLowerCase().contains("listen"));
    }
    
    // ==================== TopicClusteringSystem Tests ====================
    
    public void testTopicClusteringSystemIdentification() {
        TopicClusteringSystem system = new TopicClusteringSystem();
        
        List<String> topics = system.identifyTopics("I need help with my math homework");
        
        test.assertTrue("Should identify academic topic", 
            topics.contains("academic"));
    }
    
    public void testTopicClusteringSystemActivation() {
        TopicClusteringSystem system = new TopicClusteringSystem();
        
        system.activateCluster("academic");
        
        test.assertEquals("Should have 1 active cluster", 
            1, system.getActiveClusters().size());
        
        test.assertEquals("Active cluster should be academic", 
            "academic", system.getActiveClusters().get(0).clusterId);
    }
    
    public void testTopicClusteringSystemSimilarity() {
        TopicClusteringSystem system = new TopicClusteringSystem();
        
        double similarity = system.calculateTopicSimilarity(
            "I love playing video games", 
            "Fortnite is my favorite game"
        );
        
        test.assertTrue("Should calculate positive similarity", similarity > 0);
    }
    
    // ==================== Configuration Tests ====================
    
    public void testConfigurationLoad() {
        Configuration config = Configuration.getInstance();
        
        test.assertNotNull("App name should not be null", 
            config.getProperty("app.name"));
        
        test.assertEquals("App name should be VirtualXander", 
            "VirtualXander", config.getProperty("app.name"));
        
        test.assertTrue("Should get integer property", 
            config.getIntProperty("conversation.max_history_size") > 0);
        
        test.assertTrue("Should get boolean property", 
            config.getBooleanProperty("conversation.auto_reset_enabled"));
    }
    
    public void testConfigurationDefaults() {
        Configuration config = Configuration.getInstance();
        
        test.assertEquals("Should return default for missing property", 
            "default_value", config.getProperty("nonexistent.property", "default_value"));
    }
    
    // ==================== ErrorHandler Tests ====================
    
    public void testErrorHandlerBasic() {
        ErrorHandler handler = ErrorHandler.getInstance();
        
        ErrorHandler.ErrorResult result = handler.handleError(
            "TestException", 
            "Test error message", 
            "TestSource"
        );
        
        test.assertTrue("Should handle error successfully", result.isHandled());
    }
    
    public void testErrorHandlerValidation() {
        ErrorHandler handler = ErrorHandler.getInstance();
        
        ErrorHandler.ErrorResult validResult = handler.validateInput(
            "test input", 
            ".+", 
            "Input"
        );
        test.assertTrue("Should validate non-empty input", validResult.isHandled());
        
        ErrorHandler.ErrorResult invalidResult = handler.validateInput(
            "", 
            ".+", 
            "Input"
        );
        test.assertFalse("Should reject empty input", invalidResult.isHandled());
    }
    
    public void testErrorHandlerSafeExecutor() {
        ErrorHandler handler = ErrorHandler.getInstance();
        ErrorHandler.SafeExecutor executor = handler.createSafeExecutor();
        
        ErrorHandler.ErrorResult result = executor.execute(
            () -> { /* successful operation */ },
            "TestOperation"
        );
        
        test.assertTrue("Safe execution should succeed", result.isHandled());
    }
    
    // ==================== Logger Tests ====================
    
    public void testLoggerBasic() {
        Logger logger = Logger.getInstance();
        
        test.assertNotNull("Logger should not be null", logger);
        
        // Test logging doesn't throw exception
        logger.info("Test", "Test message");
        logger.debug("Test", "Debug message");
        logger.warn("Test", "Warning message");
        logger.error("Test", "Error message");
        
        test.assertTrue("Should have log statistics", 
            logger.getLogStatistics().containsKey("INFO"));
    }
    
    // ==================== State Machine Tests ====================
    
    public void testConversationStateMachine() {
        ConversationStateMachine stateMachine = new ConversationStateMachine();
        
        test.assertEquals("Initial state should be IDLE", 
            ConversationStateMachine.ConversationState.IDLE, 
            stateMachine.getCurrentState());
        
        // Process greeting intent
        ConversationStateMachine.ConversationState newState = stateMachine.processIntent("greeting");
        
        test.assertTrue("Should transition to GREETING", 
            newState == ConversationStateMachine.ConversationState.GREETING ||
            newState == ConversationStateMachine.ConversationState.GENERAL_CHAT);
        
        // Process farewell
        newState = stateMachine.processIntent("farewell");
        
        test.assertEquals("Should transition to CLOSING", 
            ConversationStateMachine.ConversationState.CLOSING, newState);
    }
    
    public void testConversationStateMachineTransitions() {
        ConversationStateMachine stateMachine = new ConversationStateMachine();
        
        List<String> transitions = stateMachine.getAvailableTransitions();
        
        test.assertTrue("Should have available transitions", 
            !transitions.isEmpty());
    }
    
    // ==================== Handlers Tests ====================
    
    public void testGreetingHandler() {
        GreetingHandler handler = new GreetingHandler();
        
        test.assertTrue("Should recognize greeting 'hello'", 
            handler.isGreeting("hello"));
        
        test.assertTrue("Should recognize greeting 'hi'", 
            handler.isGreeting("hi"));
        
        String response = handler.handleGreeting("hello");
        test.assertNotNull("Response should not be null", response);
    }
    
    public void testMentalHealthHandler() {
        MentalHealthSupportHandler handler = new MentalHealthSupportHandler();
        
        test.assertTrue("Should detect stress category", 
            handler.detectSupportCategory("I'm so stressed") == 
            MentalHealthSupportHandler.SupportCategory.STRESS);
        
        test.assertTrue("Should detect anxiety category", 
            handler.detectSupportCategory("I'm feeling anxious") == 
            MentalHealthSupportHandler.SupportCategory.ANXIETY);
        
        test.assertFalse("Should not be crisis level for normal input", 
            handler.isCrisisLevel("I'm feeling sad"));
        
        test.assertTrue("Should detect crisis level", 
            handler.isCrisisLevel("I want to die"));
    }
    
    public void testHomeworkHandler() {
        HomeworkAcademicHandler handler = new HomeworkAcademicHandler();
        
        test.assertTrue("Should detect math subject", 
            handler.detectSubject("I need help with math") == 
            HomeworkAcademicHandler.Subject.MATH);
        
        test.assertTrue("Should detect science subject", 
            handler.detectSubject("I have a science question") == 
            HomeworkAcademicHandler.Subject.SCIENCE);
        
        test.assertTrue("Should be academic", 
            handler.isAcademic("I need help with my homework"));
    }
    
    public void testEntertainmentHandler() {
        EntertainmentGamingHandler handler = new EntertainmentGamingHandler();
        
        test.assertTrue("Should detect gaming related", 
            handler.isGamingRelated("I love playing fortnite"));
        
        test.assertTrue("Should detect entertainment related", 
            handler.isEntertainmentRelated("I watched a great movie"));
    }
    
    public void testCreativeWritingHandler() {
        CreativeWritingHandler handler = new CreativeWritingHandler();
        
        test.assertTrue("Should detect writing related", 
            handler.isWritingRelated("I'm working on a story"));
        
        test.assertTrue("Should detect sci-fi genre", 
            handler.detectGenre("I'm writing science fiction") == 
            CreativeWritingHandler.WritingGenre.SCIFI);
    }
    
    // ==================== Test Runner ====================
    
    public static void main(String[] args) {
        System.out.println("Running VirtualXander Unit Tests...\n");
        
        VirtualXanderTests tests = new VirtualXanderTests();
        
        TestFramework testFramework = TestFramework.getInstance();
        
        // Run all tests
        testFramework.runTest("IntentRecognizer - Greeting", () -> tests.testIntentRecognizerGreeting());
        testFramework.runTest("IntentRecognizer - Wellbeing", () -> tests.testIntentRecognizerWellbeing());
        testFramework.runTest("IntentRecognizer - Homework", () -> tests.testIntentRecognizerHomework());
        testFramework.runTest("IntentRecognizer - Emotion", () -> tests.testIntentRecognizerEmotion());
        
        testFramework.runTest("ConversationContext - Basic", () -> tests.testConversationContextBasic());
        testFramework.runTest("ConversationContext - Turns", () -> tests.testConversationContextTurns());
        testFramework.runTest("ConversationContext - Reset", () -> tests.testConversationContextReset());
        testFramework.runTest("ConversationContext - SessionData", () -> tests.testConversationContextSessionData());
        
        testFramework.runTest("ResponseGenerator - Basic", () -> tests.testResponseGeneratorBasic());
        testFramework.runTest("ResponseGenerator - Variety", () -> tests.testResponseGeneratorVariety());
        
        testFramework.runTest("EmotionDetector - Basic", () -> tests.testEmotionDetectorBasic());
        testFramework.runTest("EmotionDetector - Confidence", () -> tests.testEmotionDetectorConfidence());
        testFramework.runTest("EmotionDetector - SupportiveResponse", () -> tests.testEmotionDetectorSupportiveResponse());
        
        testFramework.runTest("TopicClustering - Identification", () -> tests.testTopicClusteringSystemIdentification());
        testFramework.runTest("TopicClustering - Activation", () -> tests.testTopicClusteringSystemActivation());
        testFramework.runTest("TopicClustering - Similarity", () -> tests.testTopicClusteringSystemSimilarity());
        
        testFramework.runTest("Configuration - Load", () -> tests.testConfigurationLoad());
        testFramework.runTest("Configuration - Defaults", () -> tests.testConfigurationDefaults());
        
        testFramework.runTest("ErrorHandler - Basic", () -> tests.testErrorHandlerBasic());
        testFramework.runTest("ErrorHandler - Validation", () -> tests.testErrorHandlerValidation());
        testFramework.runTest("ErrorHandler - SafeExecutor", () -> tests.testErrorHandlerSafeExecutor());
        
        testFramework.runTest("Logger - Basic", () -> tests.testLoggerBasic());
        
        testFramework.runTest("StateMachine - Basic", () -> tests.testConversationStateMachine());
        testFramework.runTest("StateMachine - Transitions", () -> tests.testConversationStateMachineTransitions());
        
        testFramework.runTest("GreetingHandler", () -> tests.testGreetingHandler());
        testFramework.runTest("MentalHealthHandler", () -> tests.testMentalHealthHandler());
        testFramework.runTest("HomeworkHandler", () -> tests.testHomeworkHandler());
        testFramework.runTest("EntertainmentHandler", () -> tests.testEntertainmentHandler());
        testFramework.runTest("CreativeWritingHandler", () -> tests.testCreativeWritingHandler());
        
        // Print results
        testFramework.printResults();
        
        // Exit with appropriate code
        System.exit(testFramework.allTestsPassed() ? 0 : 1);
    }
}