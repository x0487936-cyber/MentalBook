import java.util.*;
import javax.swing.JFrame;

/**
 * VirtualXanderCore - Main orchestrator for the VirtualXander chatbot
 * Integrates Intent Recognition, Conversation Context, Response Generation, and State Machine
 */

public class VirtualXanderCore {
    // Added in Version 0.1.0.0
    private IntentRecognizer intentRecognizer;
    private ConversationContext conversationContext;
    private ResponseGenerator responseGenerator;
    private ConversationStateMachine stateMachine;
    
    // Added in Version 0.1.0.0
    private EmotionDetector emotionDetector;
    private ContextAwareResponseLogic contextAwareLogic;
    private TopicClusteringSystem topicClusteringSystem;
    private PluginSystem pluginSystem;
    
    // Added in Version 0.1.0.4
    private MentalHealthSupportHandler mentalHealthSupportHandler;
    
    // Added in Version 0.2.0.0
    private EmpathyEngine empathyEngine;
    private MoodEngine moodEngine;
    private ContextEngine contextEngine;
    private ContextEngine.ImplicitTopicTracker topicTracker;
    private ContextEngine.PersistenceManager persistenceManager;
    
    // Added in Version 0.2.0.0 - New Engine Imports
    private InsightEngine insightEngine;
    private GrowthPartner growthPartner;
    private DailyCompanion dailyCompanion;
    private SocialDynamics socialDynamics;
    private DesktopEnhancements desktopEnhancements;
    private UserProfileDashboard userProfileDashboard;
    private AdaptiveLearner adaptiveLearner;
    private RelationshipMemory relationshipMemory;
    private PersonalMemory personalMemory;

    // Added in Version 0.2.0.1
    private WisdomEngine wisdomEngine;
    private ComfortEngine comfortEngine;
    private Configuration configuration;
    private CreativeWritingHandler creativeWritingHandler;
    private DynamicResponseGenerator dynamicResponseGenerator;
    private EntertainmentGamingHandler entertainmentGamingHandler;
    private ErrorHandler errorHandler;
    private FlowManager flowManager;
    private GreetingHandler greetingHandler;
    private HomeworkAcademicHandler homeworkAcademicHandler;
    private HumanFeatures humanFeatures;
    private HumorEngine humorEngine;
    private KnowledgeNetwork knowledgeNetwork;
    private NaturalProcessor naturalProcessor;
    private PersonalityEngine personalityEngine;
    private Perspectives perspectives;
    private RelationshipHandler relationshipHandler;
    private ResponseRouter responseRouter;
    private StoryEngine storyEngine;
    private TestFramework testFramework;
    // private WellnessEngine wellnessEngine; // WellnessEngine class doesn't exist

    // Added in Version 0.1.0.0
    private boolean isRunning;
    private Scanner scanner;
    
    public VirtualXanderCore() {
        this.intentRecognizer = new IntentRecognizer();
        this.conversationContext = new ConversationContext();
        this.responseGenerator = new ResponseGenerator();
        this.stateMachine = new ConversationStateMachine();
        this.emotionDetector = new EmotionDetector();
        this.contextAwareLogic = new ContextAwareResponseLogic();
        this.contextAwareLogic.setContext(conversationContext);
        this.contextAwareLogic.setEmotionDetector(emotionDetector);
        this.topicClusteringSystem = new TopicClusteringSystem();
        this.pluginSystem = new PluginSystem();
        
        // Added in Version 0.1.0.4
        this.mentalHealthSupportHandler = new MentalHealthSupportHandler(null, null);
        
        // Added in Version 0.2.0.0
        this.empathyEngine = new EmpathyEngine(emotionDetector);
        this.moodEngine = new MoodEngine(emotionDetector);
        this.contextEngine = new ContextEngine();
        this.topicTracker = new ContextEngine.ImplicitTopicTracker();
        this.persistenceManager = new ContextEngine.PersistenceManager();
        
        // Added in Version 0.2.0.1 - Initialize new engines
        this.insightEngine = new InsightEngine();
        this.growthPartner = new GrowthPartner();
        this.dailyCompanion = new DailyCompanion();
        this.socialDynamics = new SocialDynamics();
        this.desktopEnhancements = new DesktopEnhancements(null); // Pass null for CLI mode
        this.userProfileDashboard = new UserProfileDashboard();
        this.adaptiveLearner = new AdaptiveLearner();
        this.relationshipMemory = new RelationshipMemory();
        this.personalMemory = new PersonalMemory();
        this.wisdomEngine = new WisdomEngine();
        this.comfortEngine = new ComfortEngine();
        this.configuration = Configuration.getInstance();
        this.creativeWritingHandler = new CreativeWritingHandler();
        this.dynamicResponseGenerator = new DynamicResponseGenerator();
        this.entertainmentGamingHandler = new EntertainmentGamingHandler();
        this.errorHandler = ErrorHandler.getInstance();
        this.flowManager = new FlowManager();
        this.greetingHandler = new GreetingHandler();
        this.homeworkAcademicHandler = new HomeworkAcademicHandler();
        this.humanFeatures = new HumanFeatures();
        this.humorEngine = new HumorEngine();
        this.knowledgeNetwork = new KnowledgeNetwork();
        this.naturalProcessor = new NaturalProcessor();
        this.personalityEngine = new PersonalityEngine();
        this.perspectives = new Perspectives();
        this.relationshipHandler = new RelationshipHandler();
        this.responseRouter = new ResponseRouter();
        this.storyEngine = new StoryEngine();
        this.testFramework = null; // TestFramework - singleton pattern, not directly instantiated
        // this.wellnessEngine = new WellnessEngine(); // WellnessEngine class doesn't exist
        
        this.isRunning = false;
        this.scanner = new Scanner(System.in);
    }

    // Added in Version 0.1.0.0
    /**
     * Starts the conversation
     */
    public void start() {
        isRunning = true;
        printWelcome();
        
        while (isRunning) {
            try {
                processUserInput();
            } catch (Exception e) {
                handleError(e);
            }
        }
    }
    
    /**
     * Starts the conversation with custom scanner (for testing/integration)
     */
    public void start(Scanner customScanner) {
        this.scanner = customScanner;
        start();
    }
    // Added in Version 0.1.0.0, Updated in Version 0.2.0.0
    private void printWelcome() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                      VirtualXander                        ║");
        System.out.println("║            Your Friendly Virtual Companion                ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  I'm here to chat, help with homework, discuss games,     ║");
        System.out.println("║  provide mental health support, creative writing help,    ║");
        System.out.println("║  and much more! Now with enhanced emotion detection!      ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  Commands:                                                ║");
        System.out.println("║  - Type 'exit' or 'bye' to end the conversation           ║");
        System.out.println("║  - Type 'reset' to start a new conversation               ║");
        System.out.println("║  - Type 'status' to see current state                     ║");
        System.out.println("║  - Type 'emotion' to see detected emotion                 ║");
        System.out.println("║  - Type 'topics' to see topic clusters                    ║");
        System.out.println("║  - Type 'plugins' to see loaded plugins                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Hello! Start chatting with me. Type 'exit' to end the chat.");
        System.out.println();
    }
    // Added in Version 0.1.0.0
    private void processUserInput() {
        System.out.print("You: ");
        // Added in Version 0.2.0.0
        // Check if there's input available before reading
        if (!scanner.hasNextLine()) {
            // No input available - handle gracefully
            System.out.println("\nXander: It seems there's no more input. Goodbye!");
            isRunning = false;
            return;
        }
        
        // Added in Version 0.1.0.0
        String userInput = scanner.nextLine().trim();
        
        // Handle special commands
        if (handleSpecialCommands(userInput)) {
            return;
        }
        
        // Check for empty input
        if (userInput.isEmpty()) {
            System.out.println("Xander: I didn't catch that. Could you say something?");
            return;
        }
        // Added in Version 0.2.0.0
        // Check if user said "oh" after Topic Clusters were just listed
        if (conversationContext.isTopicClustersJustListed() && 
            userInput.toLowerCase().trim().equals("oh")) {
            // Provide explanation about Topic Clusters
            System.out.println("Xander: Topic Clusters are categories that help me understand what you're interested in talking about. ");
            System.out.println("Based on our conversation, I organize topics into different clusters like:");
            System.out.println("  • Academic - homework, studies, learning");
            System.out.println("  • Gaming - video games, gaming discussions");
            System.out.println("  • Mental Health - emotional support, well-being");
            System.out.println("  • Creative - writing, creative projects");
            System.out.println("  • Social - relationships, friendships");
            System.out.println("  • And many more...");
            System.out.println("\nEach cluster tracks how often we discuss that topic, so I can better understand what matters to you!");
            System.out.println();
            
            // Reset the flag
            conversationContext.setTopicClustersJustListed(false);
            return;
        }
        
        // Reset the flag if user says something else
        if (conversationContext.isTopicClustersJustListed()) {
            conversationContext.setTopicClustersJustListed(false);
        }
        
        // Phase 4: Track conversation in ContextEngine
        contextEngine.addMessageToCurrentThread("User", userInput);
        
        // Track topics using ImplicitTopicTracker
        List<ContextEngine.ImplicitTopicTracker.TrackedTopic> detectedTopics = 
            topicTracker.processText(userInput);
        if (!detectedTopics.isEmpty()) {
            ContextEngine.ImplicitTopicTracker.TrackedTopic dominant = topicTracker.getDominantTopic();
            if (dominant != null) {
                contextEngine.getCurrentThread().setTopic(dominant.getTopicName());
            }
        }

        // Added in Version 0.1.0.0
        // Phase 2: Detect emotions
        EmotionDetector.EmotionResult emotionResult = emotionDetector.detectEmotion(userInput);
        // Phase 2: Identify topics
        List<String> identifiedTopics = topicClusteringSystem.identifyTopics(userInput);
        if (!identifiedTopics.isEmpty()) {
            String dominantTopic = identifiedTopics.get(0);
            topicClusteringSystem.activateCluster(dominantTopic);
        }

        // Added in Version 0.1.0.4
        // Phase 3: Check for mental health support needs
        String response;
        if (mentalHealthSupportHandler.isMentalHealthSupportNeeded(userInput)) {
            // Handle mental health support
            response = handleMentalHealthSupport(userInput);
        } else {
            // Normal conversation flow
            // Recognize intent
            String intent = intentRecognizer.recognizeIntent(userInput);
            // Check for farewell command
            if (intent.equals("farewell") || userInput.toLowerCase().contains("exit")) {
                farewell();
                return;
            }
            // Process through state machine
            stateMachine.processIntent(intent);
            // Generate base response
            response = responseGenerator.generateResponse(intent, userInput, conversationContext);
            // Apply context-aware enhancements (pass base response to avoid duplication)
            response = contextAwareLogic.generateContextualResponse(
                response, intent, userInput, emotionResult.getPrimaryEmotion(), conversationContext
            );
        }

        // Added in Version 0.2.0.0
        // Add Xander response to ContextEngine
        contextEngine.addMessageToCurrentThread("Xander", response);
        
        // Added in Version 0.1.0.0
        // Print response
        System.out.println("Xander: " + response);
        System.out.println();
        
        // Log state and emotion info (for debugging)

        // Added in Version 0.1.0.0, Updated in Version 0.1.0.4
        logStateInfo("mental_health_support", ConversationStateMachine.ConversationState.GENERAL_CHAT, emotionResult);
    }
    
    /**
     * Handles mental health support including dark thoughts
     */
    private String handleMentalHealthSupport(String userInput) {
        // First check if it's a crisis level
        if (mentalHealthSupportHandler.isCrisisLevel(userInput)) {
            return mentalHealthSupportHandler.getCrisisResponse();
        }
        
        // Detect the specific support category
        MentalHealthSupportHandler.SupportCategory category = 
            mentalHealthSupportHandler.detectSupportCategory(userInput);
        
        // Get appropriate response
        MentalHealthSupportHandler.SupportResponse supportResponse = 
            mentalHealthSupportHandler.getSupportResponse(category);
        
        StringBuilder response = new StringBuilder();
        response.append(supportResponse.response);
        
        // Add coping suggestions for dark thoughts specifically
        if (category == MentalHealthSupportHandler.SupportCategory.DARK_THOUGHTS) {
            List<String> suggestions = mentalHealthSupportHandler.getCopingSuggestions(category);
            if (suggestions != null && !suggestions.isEmpty()) {
                response.append("\n\nSome suggestions that might help:\n");
                for (int i = 0; i < Math.min(3, suggestions.size()); i++) {
                    response.append("• ").append(suggestions.get(i)).append("\n");
                }
            }
            // Add encouragement
            response.append("\n").append(mentalHealthSupportHandler.getEncouragement(category));
        }
        
        // Add follow-up question
        response.append("\n").append(supportResponse.followUp);
        
        return response.toString();
    }
    // Added in Version 0.1.0.0
    private boolean handleSpecialCommands(String input) {
        String lowerInput = input.toLowerCase().trim();
        
        if (lowerInput.equals("exit") || lowerInput.equals("bye")) {
            farewell();
            return true;
        }
        
        if (lowerInput.equals("reset")) {
            resetConversation();
            return true;
        }
        
        if (lowerInput.equals("status")) {
            printStatus();
            return true;
        }
        
        if (lowerInput.equals("help")) {
            printHelp();
            return true;
        }
        
        if (lowerInput.equals("history")) {
            printConversationHistory();
            return true;
        }
        
        // Phase 2 special commands
        if (lowerInput.equals("emotion")) {
            printEmotionStatus();
            return true;
        }
        
        if (lowerInput.equals("topics") || lowerInput.equals("topic")) {
            printTopicClusters();
            return true;
        }
        
        // Phase 5: Empathy Engine special command
        if (lowerInput.equals("empathy") || lowerInput.equals("mirror")) {
            printEmpathyStatus();
            return true;
        }
        
        // Phase 4.2: Mood Engine special command
        if (lowerInput.equals("mood") || lowerInput.equals("moodanalysis")) {
            printMoodStatus();
            return true;
        }
        
        // Phase 4.2: Mood Matcher special command
        if (lowerInput.equals("match") || lowerInput.equals("moodmatch")) {
            printMoodMatchStatus();
            return true;
        }
        
        // Phase 4.2: Mood Enhancement special command
        if (lowerInput.equals("enhance") || lowerInput.equals("boost") || lowerInput.equals("moodenhance")) {
            printMoodEnhancementStatus();
            return true;
        }
        
        if (lowerInput.equals("plugins")) {
            printPluginStatus();
            return true;
        }
        
        if (lowerInput.equals("analyze")) {
            analyzeConversation();
            return true;
        }
        
        // Phase 4: ContextEngine commands
        if (lowerInput.equals("context") || lowerInput.equals("ctx")) {
            printContextEngineStatus();
            return true;
        }
        
        if (lowerInput.equals("questions") || lowerInput.equals("openq")) {
            printOpenQuestions();
            return true;
        }
        
        if (lowerInput.equals("topics") || lowerInput.equals("topic")) {
            printTopicClusters();
            return true;
        }
        
        if (lowerInput.equals("entities") || lowerInput.equals("ents")) {
            printEntities();
            return true;
        }
        
        if (lowerInput.equals("threads")) {
            printThreads();
            return true;
        }
        
        if (lowerInput.equals("save")) {
            saveContext();
            return true;
        }
        
        if (lowerInput.equals("load")) {
            loadContext();
            return true;
        }
        
        // New Engine Commands - Version 0.2.0.0
        // Insight Engine commands
        if (lowerInput.equals("insight") || lowerInput.equals("insights")) {
            printInsightStatus();
            return true;
        }
        
        // Growth Partner commands
        if (lowerInput.equals("growth") || lowerInput.equals("growthpartner")) {
            printGrowthStatus();
            return true;
        }
        
        // Daily Companion commands
        if (lowerInput.equals("companion") || lowerInput.equals("daily") || lowerInput.equals("checkin")) {
            printCompanionStatus();
            return true;
        }
        
        // Social Dynamics commands
        if (lowerInput.equals("social") || lowerInput.equals("socialdynamics")) {
            printSocialStatus();
            return true;
        }
        
        // User Profile Dashboard commands
        if (lowerInput.equals("profile") || lowerInput.equals("dashboard") || lowerInput.equals("userprofile")) {
            printProfileStatus();
            return true;
        }
        
        // Adaptive Learner commands
        if (lowerInput.equals("adaptive") || lowerInput.equals("learn") || lowerInput.equals("learning")) {
            printAdaptiveStatus();
            return true;
        }
        
        // Relationship Memory commands
        if (lowerInput.equals("relationship") || lowerInput.equals("relations") || lowerInput.equals("relationships")) {
            printRelationshipStatus();
            return true;
        }
        
        // Personal Memory commands
        if (lowerInput.equals("personal") || lowerInput.equals("pmemory") || lowerInput.equals("mymemory")) {
            printPersonalMemoryStatus();
            return true;
        }
        
        // Desktop Enhancements commands
        if (lowerInput.equals("desktop") || lowerInput.equals("desktopenhance")) {
            printDesktopStatus();
            return true;
        }
        
        return false;
    }
    
    private void farewell() {
        System.out.println("Xander: Goodbye! It was great chatting with you. Take care!");
        System.out.println();
        System.out.println("Conversation Statistics:");
        System.out.println("  - Total turns: " + conversationContext.getTurnCount());
        System.out.println("  - Duration: " + conversationContext.getConversationDurationMinutes() + " minutes");
        System.out.println("  - Topics explored: " + topicClusteringSystem.getActiveClusters().size());
        isRunning = false;
    }
    
    private void resetConversation() {
        conversationContext.reset();
        stateMachine.reset();
        topicClusteringSystem.resetClusters();
        conversationContext.setTopicClustersJustListed(false);
        System.out.println("Xander: Conversation reset! Let's start fresh. Hi there!");
        System.out.println();
    }
    
    private void printStatus() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Current Status                              ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ State: " + String.format("%-40s", stateMachine.getCurrentStateName()) + "           ║");
        System.out.println("║ Turns: " + String.format("%-41s", conversationContext.getTurnCount()) + "          ║");
        System.out.println("║ Duration: " + String.format("%-37s", conversationContext.getConversationDurationMinutes() + " min") + "           ║");
        System.out.println("║ Topic: " + String.format("%-40s", conversationContext.getCurrentTopic()) + "           ║");
        System.out.println("║ Plugins: " + String.format("%-38s", pluginSystem.getPluginCount() + " loaded") + "           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void printEmotionStatus() {
        String lastInput = conversationContext.getLastUserInput();
        if (lastInput == null) {
            System.out.println("Xander: No conversation yet to analyze emotions.");
            return;
        }
        
        EmotionDetector.EmotionResult result = emotionDetector.detectEmotion(lastInput);
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Emotion Analysis                            ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Primary Emotion: " + String.format("%-35s", result.getPrimaryEmotion().getName()) + "║");
        System.out.println("║ Confidence: " + String.format("%-38s", String.format("%.2f", result.getConfidence())) + "║");
        System.out.println("║ Is Positive: " + String.format("%-37s", result.isPositive()) + "║");
        System.out.println("║ Is Negative: " + String.format("%-37s", result.isNegative()) + "║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Print Empathy Engine status
     */
    private void printEmpathyStatus() {
        String lastInput = conversationContext.getLastUserInput();
        if (lastInput == null) {
            System.out.println("Xander: No conversation yet to analyze with Empathy Engine.");
            return;
        }
        
        EmpathyEngine.EmotionalMirrorResponse response = empathyEngine.reflectEmotion(lastInput);
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Emotional Mirror Analysis                   ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Detected Emotion: " + String.format("%-35s", response.getPrimaryEmotion().getName()) + "║");
        System.out.println("║ Confidence: " + String.format("%-38s", String.format("%.2f", response.getConfidence())) + "║");
        System.out.println("║ Reflection: " + String.format("%-37s", response.getReflectedEmotion()) + "║");
        System.out.println("║ Validation: " + String.format("%-37s", response.getValidationMessage()) + "║");
        System.out.println("║ Normalization: " + String.format("%-35s", response.getNormalizationMessage()) + "║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Print Mood Engine status (Fine-grained Mood Analysis)
     */
    private void printMoodStatus() {
        String lastInput = conversationContext.getLastUserInput();
        if (lastInput == null) {
            System.out.println("Xander: No conversation yet to analyze with Mood Engine.");
            return;
        }
        
        MoodEngine.MoodAnalysis analysis = moodEngine.analyzeMood(lastInput);
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║            Fine-grained Mood Analysis                     ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Surface Emotion: " + String.format("%-35s", analysis.getSurfaceEmotion()) + "║");
        System.out.println("║ Underlying Emotion: " + String.format("%-33s", analysis.getUnderlyingEmotion()) + "║");
        System.out.println("║ Intensity: " + String.format("%-40s", moodEngine.getIntensityDescription(analysis.getIntensity())) + "║");
        System.out.println("║ Is Sarcastic: " + String.format("%-37s", analysis.isSarcastic()) + "║");
        if (analysis.isSarcastic()) {
            System.out.println("║ Sarcasm Meaning: " + String.format("%-34s", analysis.getSarcasmMeaning()) + "║");
        }
        System.out.println("║ Subtext: " + String.format("%-41s", analysis.getSubtext()) + "║");
        System.out.println("║ Confidence: " + String.format("%-38s", String.format("%.2f", analysis.getConfidence())) + "║");
        System.out.println("║ Recommended Tone: " + String.format("%-33s", analysis.getRecommendedTone()) + "║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Show suggested approach
        System.out.println("Suggested Approach:");
        System.out.println("  " + moodEngine.getSuggestedApproach(analysis));
        System.out.println();
    }
    
    /**
     * Print Mood Matcher status (Energy Matching and Intensity Calibration)
     */
    private void printMoodMatchStatus() {
        String lastInput = conversationContext.getLastUserInput();
        if (lastInput == null) {
            System.out.println("Xander: No conversation yet to analyze with Mood Matcher.");
            return;
        }
        
        // Get mood analysis
        MoodEngine.MoodAnalysis moodAnalysis = moodEngine.analyzeMood(lastInput);
        
        // Get mood matcher and perform matching
        MoodEngine.MoodMatcher moodMatcher = moodEngine.getMoodMatcher();
        MoodEngine.MoodMatcher.MoodMatchResult matchResult = moodMatcher.matchEnergy(moodAnalysis);
        
        // Calibrate intensity
        double calibratedIntensity = moodMatcher.calibrateIntensity(0.5, moodAnalysis);
        
        // Get appropriate reaction
        String reaction = moodMatcher.appropriateReaction(moodAnalysis);
        
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║            Mood Matching Analysis                        ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ User Energy Level: " + String.format("%-32s", matchResult.getUserEnergy().getName()) + "║");
        System.out.println("║ Response Energy: " + String.format("%-33s", matchResult.getResponseEnergy().getName()) + "║");
        System.out.println("║ Intensity Multiplier: " + String.format("%-30s", String.format("%.2f", matchResult.getIntensityMultiplier())) + "║");
        System.out.println("║ Calibrated Intensity: " + String.format("%-30s", String.format("%.2f", calibratedIntensity)) + "║");
        System.out.println("║ Matched Tone: " + String.format("%-36s", matchResult.getMatchedTone()) + "║");
        System.out.println("║ Reaction: " + String.format("%-40s", reaction) + "║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Show suggested phrases
        if (!matchResult.getSuggestedPhrases().isEmpty()) {
            System.out.println("Suggested Phrases:");
            for (String phrase : matchResult.getSuggestedPhrases()) {
                System.out.println("  • " + phrase);
            }
            System.out.println();
        }
    }
    
    /**
     * Print Mood Enhancement status (Energy Boosting, Confidence Building, Perspective Lifting)
     */
    private void printMoodEnhancementStatus() {
        String lastInput = conversationContext.getLastUserInput();
        if (lastInput == null) {
            System.out.println("Xander: No conversation yet to analyze with Mood Enhancement.");
            return;
        }
        
        // Get mood analysis
        MoodEngine.MoodAnalysis moodAnalysis = moodEngine.analyzeMood(lastInput);
        
        // Get mood enhancer and generate enhancement
        MoodEngine.MoodEnhancer moodEnhancer = moodEngine.getMoodEnhancer();
        MoodEngine.MoodEnhancer.EnhancementResult enhancementResult = moodEnhancer.enhanceMood(moodAnalysis);
        
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║            Mood Enhancement Analysis                    ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Current Emotion: " + String.format("%-35s", moodAnalysis.getSurfaceEmotion()) + "║");
        System.out.println("║ Intensity: " + String.format("%-40s", moodEngine.getIntensityDescription(moodAnalysis.getIntensity())) + "║");
        System.out.println("║ Enhancement Type: " + String.format("%-32s", enhancementResult.getEnhancementType().getName()) + "║");
        System.out.println("║ Effectiveness Score: " + String.format("%-30s", String.format("%.2f", enhancementResult.getEffectivenessScore())) + "║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Show enhancement message
        System.out.println("Enhancement Message:");
        System.out.println("  " + enhancementResult.getEnhancementMessage());
        System.out.println();
        
        // Show suggested action
        if (enhancementResult.getSuggestedAction() != null && !enhancementResult.getSuggestedAction().isEmpty()) {
            System.out.println("Suggested Action:");
            System.out.println("  " + enhancementResult.getSuggestedAction());
            System.out.println();
        }
        
        // Show follow-up suggestions
        if (!enhancementResult.getFollowUpSuggestions().isEmpty()) {
            System.out.println("Follow-up Options:");
            for (String suggestion : enhancementResult.getFollowUpSuggestions()) {
                System.out.println("  • " + suggestion);
            }
            System.out.println();
        }
    }
    
    private void printTopicClusters() {
        List<TopicClusteringSystem.TopicCluster> activeClusters = topicClusteringSystem.getActiveClusters();
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Topic Clusters                              ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        
        if (activeClusters.isEmpty()) {
            System.out.println("║ No active topics yet. Start chatting to explore topics!  ║");
        } else {
            for (TopicClusteringSystem.TopicCluster cluster : activeClusters) {
                System.out.println("║ " + String.format("%-45s", cluster.clusterName + " (Visits: " + cluster.visitCount + ")") + "║");
            }
        }
        
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Set flag to track that Topic Clusters were just listed
        conversationContext.setTopicClustersJustListed(true);
    }
    
    private void printPluginStatus() {
        System.out.println(pluginSystem.getPluginSummary());
    }
    
    private void analyzeConversation() {
        ContextAwareResponseLogic.ConversationAnalysis analysis = 
            contextAwareLogic.analyzeConversation(conversationContext);
        
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Conversation Analysis                       ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Total Turns: " + String.format("%-37s", analysis.turnCount) + "║");
        System.out.println("║ Duration: " + String.format("%-38s", analysis.durationMinutes + " minutes") + "║");
        System.out.println("║ Current Topic: " + String.format("%-35s", analysis.currentTopic) + "║");
        System.out.println("║ Engagement Score: " + String.format("%-33s", String.format("%.2f", analysis.engagementScore)) + "║");
        System.out.println("║ Conversation Stage: " + String.format("%-33s", analysis.stage) + "║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        
        if (!analysis.recommendations.isEmpty()) {
            System.out.println("Recommendations:");
            for (String rec : analysis.recommendations) {
                System.out.println("  - " + rec);
            }
            System.out.println();
        }
    }
    
    private void printHelp() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                  Help & Commands                          ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  I can help you with:                                     ║");
        System.out.println("║  • Greetings and casual chat                              ║");
        System.out.println("║  • Homework and academic questions                        ║");
        System.out.println("║  • Mental health and emotional support                    ║");
        System.out.println("║  • Gaming discussions                                     ║");
        System.out.println("║  • Creative writing assistance                            ║");
        System.out.println("║  • Entertainment recommendations                          ║");
        System.out.println("║  • General advice and tips                                ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  Core Commands:                                           ║");
        System.out.println("║  • exit/bye - End conversation                            ║");
        System.out.println("║  • reset - Start new conversation                         ║");
        System.out.println("║  • status - Show current status                           ║");
        System.out.println("║  • help - Show this help message                          ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  Advanced Commands:                                       ║");
        System.out.println("║  • emotion - Show emotion analysis                        ║");
        System.out.println("║  • topics - Show topic clusters                          ║");
        System.out.println("║  • analyze - Analyze conversation                         ║");
        System.out.println("║  • plugins - Show loaded plugins                         ║");
        System.out.println("║  • mood - Show mood analysis                             ║");
        System.out.println("║  • match/moodmatch - Show mood matching                  ║");
        System.out.println("║  • enhance/boost - Show mood enhancement                 ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  ContextEngine Commands:                                  ║");
        System.out.println("║  • context/ctx - ContextEngine status                     ║");
        System.out.println("║  • questions/openq - Show open questions                  ║");
        System.out.println("║  • entities/ents - Show tracked entities                  ║");
        System.out.println("║  • threads - Show conversation threads                    ║");
        System.out.println("║  • save - Save context to memory                          ║");
        System.out.println("║  • load - List saved memories                             ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  New Engine Commands (v0.2.0.0):                         ║");
        System.out.println("║  • insight/insights - Show insight analysis              ║");
        System.out.println("║  • growth - Show growth partner status                   ║");
        System.out.println("║  • companion/daily - Show daily companion status          ║");
        System.out.println("║  • social - Show social dynamics status                  ║");
        System.out.println("║  • profile - Show user profile dashboard                 ║");
        System.out.println("║  • adaptive - Show adaptive learner status                ║");
        System.out.println("║  • relationship - Show relationship memory status         ║");
        System.out.println("║  • personal - Show personal memory status                ║");
        System.out.println("║  • desktop - Show desktop enhancements status            ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void printConversationHistory() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              Conversation History                         ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        List<String> history = conversationContext.getRecentHistory(10);
        for (String turn : history) {
            String displayTurn = turn.length() > 45 ? turn.substring(0, 45) : turn;
            System.out.println("║ " + String.format("%-45s", displayTurn) + "║");
        }
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
    }
    
    private void logStateInfo(String intent, ConversationStateMachine.ConversationState state, 
                              EmotionDetector.EmotionResult emotion) {
        // Uncomment for debugging
        // System.err.println("[DEBUG] Intent: " + intent + " -> State: " + state.getName() + " -> Emotion: " + emotion.getPrimaryEmotion().getName());
    }
    
    private void handleError(Exception e) {
        System.out.println("Xander: Oops! Something went wrong. Let's try again!");
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
    }
    
    /**
     * Processes a single message and returns the response (for API/integration use)
     */
    public String processMessage(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return "I didn't catch that. Could you say something?";
        }
        
        String normalizedInput = userInput.trim().toLowerCase();
        
        // Handle special commands
        if (normalizedInput.equals("exit") || normalizedInput.equals("bye")) {
            isRunning = false;
            return "Goodbye! It was great chatting with you.";
        }
        
        // Phase 3: Check for mental health support needs
        if (mentalHealthSupportHandler.isMentalHealthSupportNeeded(userInput)) {
            return handleMentalHealthSupport(userInput);
        }
        
        // Phase 2: Detect emotions
        EmotionDetector.EmotionResult emotionResult = emotionDetector.detectEmotion(userInput);
        
        // Phase 2: Identify topics
        List<String> identifiedTopics = topicClusteringSystem.identifyTopics(userInput);
        if (!identifiedTopics.isEmpty()) {
            topicClusteringSystem.activateCluster(identifiedTopics.get(0));
        }
        
        // Recognize intent
        String intent = intentRecognizer.recognizeIntent(userInput);
        
        // Process through state machine
        stateMachine.processIntent(intent);
        
        // Generate base response
        String response = responseGenerator.generateResponse(intent, userInput, conversationContext);
        
        // Apply context-aware enhancements (pass base response to avoid duplication)
        response = contextAwareLogic.generateContextualResponse(
            response, intent, userInput, emotionResult.getPrimaryEmotion(), conversationContext
        );
        
        return response;
    }
    
    // Phase 2: Getters for advanced features
    
    /**
     * Gets the emotion detector
     */
    public EmotionDetector getEmotionDetector() {
        return emotionDetector;
    }
    
    /**
     * Gets the context-aware response logic
     */
    public ContextAwareResponseLogic getContextAwareLogic() {
        return contextAwareLogic;
    }
    
    /**
     * Gets the topic clustering system
     */
    public TopicClusteringSystem getTopicClusteringSystem() {
        return topicClusteringSystem;
    }
    
    /**
     * Gets the plugin system
     */
    public PluginSystem getPluginSystem() {
        return pluginSystem;
    }
    
    /**
     * Gets the current state machine
     */
    public ConversationStateMachine getStateMachine() {
        return stateMachine;
    }
    
    /**
     * Gets the conversation context
     */
    public ConversationContext getConversationContext() {
        return conversationContext;
    }
    
    /**
     * Gets the intent recognizer
     */
    public IntentRecognizer getIntentRecognizer() {
        return intentRecognizer;
    }
    
    /**
     * Gets the response generator
     */
    public ResponseGenerator getResponseGenerator() {
        return responseGenerator;
    }
    
    /**
     * Gets the mental health support handler
     */
    public MentalHealthSupportHandler getMentalHealthSupportHandler() {
        return mentalHealthSupportHandler;
    }
    
    /**
     * Gets the Empathy Engine for Emotional Mirror features
     */
    public EmpathyEngine getEmpathyEngine() {
        return empathyEngine;
    }
    
    /**
     * Gets the Mood Engine for Fine-grained Mood Detection
     */
    public MoodEngine getMoodEngine() {
        return moodEngine;
    }
    
    /**
     * Gets the ContextEngine for advanced context management
     */
    public ContextEngine getContextEngine() {
        return contextEngine;
    }
    
    /**
     * Gets the implicit topic tracker
     */
    public ContextEngine.ImplicitTopicTracker getTopicTracker() {
        return topicTracker;
    }
    
    /**
     * Gets the Insight Engine for pattern and behavior analysis
     */
    public InsightEngine getInsightEngine() {
        return insightEngine;
    }
    
    /**
     * Gets the Growth Partner for personal growth tracking
     */
    public GrowthPartner getGrowthPartner() {
        return growthPartner;
    }
    
    /**
     * Gets the Daily Companion for daily check-ins
     */
    public DailyCompanion getDailyCompanion() {
        return dailyCompanion;
    }
    
    /**
     * Gets the Social Dynamics engine
     */
    public SocialDynamics getSocialDynamics() {
        return socialDynamics;
    }
    
    /**
     * Gets the Desktop Enhancements
     */
    public DesktopEnhancements getDesktopEnhancements() {
        return desktopEnhancements;
    }
    
    /**
     * Gets the User Profile Dashboard
     */
    public UserProfileDashboard getUserProfileDashboard() {
        return userProfileDashboard;
    }
    
    /**
     * Gets the Adaptive Learner
     */
    public AdaptiveLearner getAdaptiveLearner() {
        return adaptiveLearner;
    }
    
    /**
     * Gets the Relationship Memory
     */
    public RelationshipMemory getRelationshipMemory() {
        return relationshipMemory;
    }
    
    /**
     * Gets the Personal Memory
     */
    public PersonalMemory getPersonalMemory() {
        return personalMemory;
    }
    
    /**
     * Gets the Wisdom Engine for life wisdom and guidance
     */
    public WisdomEngine getWisdomEngine() {
        return wisdomEngine;
    }
    
    /**
     * Checks if the conversation is running
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Stops the conversation
     */
    public void stop() {
        isRunning = false;
        if (scanner != null) {
            scanner.close();
        }
    }
    
    // ==================== CONTEXT ENGINE COMMANDS ====================
    
    /**
     * Print ContextEngine status
     */
    private void printContextEngineStatus() {
        Map<String, Object> summary = contextEngine.getContextSummary();
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               ContextEngine Status                        ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Total Entities: " + String.format("%-35s", summary.get("totalEntities")) + "║");
        System.out.println("║ Working Memory: " + String.format("%-35s", summary.get("workingMemory")) + "║");
        System.out.println("║ Short-Term Memory: " + String.format("%-32s", summary.get("shortTermMemory")) + "║");
        System.out.println("║ Long-Term Memory: " + String.format("%-33s", summary.get("longTermMemory")) + "║");
        System.out.println("║ Active Threads: " + String.format("%-36s", summary.get("activeThreads")) + "║");
        System.out.println("║ Open Questions: " + String.format("%-36s", summary.get("openQuestions")) + "║");
        System.out.println("║ Current Thread: " + String.format("%-36s", summary.get("currentThreadTopic")) + "║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Print open questions
     */
    private void printOpenQuestions() {
        List<ContextEngine.OpenQuestion> questions = contextEngine.getOpenQuestions();
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Open Questions                              ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        
        if (questions.isEmpty()) {
            System.out.println("║ No open questions at the moment.                         ║");
        } else {
            for (ContextEngine.OpenQuestion q : questions.stream().limit(5).toList()) {
                String truncated = q.getQuestionText().length() > 40 ? 
                    q.getQuestionText().substring(0, 40) + "..." : q.getQuestionText();
                System.out.println("║ " + String.format("%-45s", truncated) + "║");
                System.out.println("║   Priority: " + q.getPriority() + " | Status: " + q.getStatus() + "                      ║");
            }
        }
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Print tracked entities
     */
    private void printEntities() {
        List<ContextEngine.ReferenceEntity> entities = contextEngine.getAllEntities();
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Tracked Entities                            ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        
        if (entities.isEmpty()) {
            System.out.println("║ No entities tracked yet.                                 ║");
        } else {
            for (ContextEngine.ReferenceEntity e : entities.stream().limit(10).toList()) {
                System.out.println("║ " + String.format("%-45s", e.getName() + " (" + e.getType() + ")") + "║");
            }
        }
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Print conversation threads
     */
    private void printThreads() {
        List<ContextEngine.ConversationThread> threads = contextEngine.getActiveThreads();
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Conversation Threads                        ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        
        if (threads.isEmpty()) {
            System.out.println("║ No active threads.                                        ║");
        } else {
            for (ContextEngine.ConversationThread t : threads) {
                System.out.println("║ " + String.format("%-45s", t.getTopic() + " (" + t.getMessageCount() + " msgs)") + "║");
                System.out.println("║   Status: " + t.getStatus() + " | Coherence: " + String.format("%.2f", t.getCoherenceScore()) + "                    ║");
            }
        }
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Save context to memory
     */
    private void saveContext() {
        boolean success = contextEngine.saveShortTermMemory();
        if (success) {
            System.out.println("Xander: Context saved successfully!");
        } else {
            System.out.println("Xander: Failed to save context.");
        }
        System.out.println();
    }
    
    /**
     * Load context from memory
     */
    private void loadContext() {
        List<String> savedMemories = contextEngine.getSavedMemories();
        if (savedMemories.isEmpty()) {
            System.out.println("Xander: No saved memories found.");
        } else {
            System.out.println("Xander: Available saved memories:");
            for (String mem : savedMemories) {
                System.out.println("  - " + mem);
            }
        }
        System.out.println();
    }
    
    // ==================== NEW ENGINE STATUS METHODS ====================
    
    /**
     * Print Insight Engine status
     */
    private void printInsightStatus() {
        String lastInput = conversationContext.getLastUserInput();
        if (lastInput == null) {
            System.out.println("Xander: No conversation yet to analyze with Insight Engine.");
            return;
        }
        
        // Generate insight based on conversation
        String userId = "default_user";
        insightEngine.recordBehavior(userId, "conversation");
        insightEngine.recordEmotion(userId, emotionDetector.detectEmotion(lastInput).getPrimaryEmotion().getName(), 5);
        
        String insight = insightEngine.generateInsight(userId);
        String patterns = insightEngine.analyzePatterns(userId);
        String recommendations = insightEngine.getRecommendation(userId);
        
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Insight Engine Analysis                     ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println(insight);
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println(patterns);
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println(recommendations);
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Print Growth Partner status
     */
    private void printGrowthStatus() {
        String lastInput = conversationContext.getLastUserInput();
        if (lastInput == null) {
            System.out.println("Xander: No conversation yet with Growth Partner.");
            return;
        }
        
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Growth Partner Status                       ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Growth Partner is active and ready to help!               ║");
        System.out.println("║ - Personal growth tracking                                ║");
        System.out.println("║ - Goal setting and accountability                         ║");
        System.out.println("║ - Learning paths and skill development                    ║");
        System.out.println("║ - Progress tracking and reflections                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Print Daily Companion status
     */
    private void printCompanionStatus() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Daily Companion Status                      ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        
        // Check if check-in is due
        boolean checkInDue = dailyCompanion.isCheckInDue();
        System.out.println("║ Check-in Due: " + String.format("%-35s", checkInDue ? "Yes" : "No") + "║");
        
        // Get mood summary if available
        String moodSummary = dailyCompanion.getMoodSummary();
        System.out.println("║ Mood: " + String.format("%-41s", moodSummary) + "║");
        
        // Get energy trends if available
        String energyTrends = dailyCompanion.getEnergyTrends();
        System.out.println("║ Energy: " + String.format("%-40s", energyTrends) + "║");
        
        // Get goal status
        String goalStatus = dailyCompanion.getGoalStatus();
        System.out.println("║ Goals: " + String.format("%-40s", goalStatus) + "║");
        
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Print Social Dynamics status
     */
    private void printSocialStatus() {
        String lastInput = conversationContext.getLastUserInput();
        if (lastInput == null) {
            System.out.println("Xander: No conversation yet with Social Dynamics.");
            return;
        }
        
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Social Dynamics Status                      ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Social Dynamics is active and ready!                      ║");
        System.out.println("║ - Relationship insights                                   ║");
        System.out.println("║ - Social interaction patterns                             ║");
        System.out.println("║ - Communication style analysis                            ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Print User Profile Dashboard status
     */
    private void printProfileStatus() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               User Profile Dashboard                      ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ User Profile Dashboard is active!                         ║");
        System.out.println("║ - User preferences and settings                           ║");
        System.out.println("║ - Interaction history                                     ║");
        System.out.println("║ - Personalized experience                                 ║");
        System.out.println("║ - Usage analytics                                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Print Adaptive Learner status
     */
    private void printAdaptiveStatus() {
        String lastInput = conversationContext.getLastUserInput();
        if (lastInput == null) {
            System.out.println("Xander: No conversation yet with Adaptive Learner.");
            return;
        }
        
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Adaptive Learner Status                     ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Adaptive Learner is active!                               ║");
        System.out.println("║ - Learning from interactions                              ║");
        System.out.println("║ - Adapting to user preferences                            ║");
        System.out.println("║ - Personalized recommendations                            ║");
        System.out.println("║ - Knowledge expansion                                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Print Relationship Memory status
     */
    private void printRelationshipStatus() {
        String lastInput = conversationContext.getLastUserInput();
        if (lastInput == null) {
            System.out.println("Xander: No conversation yet with Relationship Memory.");
            return;
        }
        
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Relationship Memory Status                  ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Relationship Memory is active!                            ║");
        System.out.println("║ - Remembering important people                            ║");
        System.out.println("║ - Tracking relationship dynamics                          ║");
        System.out.println("║ - Interaction history with contacts                       ║");
        System.out.println("║ - Relationship insights                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Print Personal Memory status
     */
    private void printPersonalMemoryStatus() {
        String lastInput = conversationContext.getLastUserInput();
        if (lastInput == null) {
            System.out.println("Xander: No conversation yet with Personal Memory.");
            return;
        }
        
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Personal Memory Status                      ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Personal Memory is active!                                ║");
        System.out.println("║ - Remembering user preferences                            ║");
        System.out.println("║ - Storing conversation context                            ║");
        System.out.println("║ - Personal facts and interests                            ║");
        System.out.println("║ - Long-term memory storage                                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Print Desktop Enhancements status
     */
    private void printDesktopStatus() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Desktop Enhancements Status                 ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Desktop Enhancements are available!                       ║");
        System.out.println("║ - System notifications                                    ║");
        System.out.println("║ - Quick actions and shortcuts                             ║");
        System.out.println("║ - Desktop integration features                            ║");
        System.out.println("║ - System tray capabilities                                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Print Social Dynamics status
     */
    private void printSocialDynamicsStatus() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               Social Dynamics Status                      ║");
        System.out.println("╠═══════════════════════════════════╤╤╤╤╤╤╤╤╤╤╤╤╤╤╤╤═╣");
        System.out.println("║ Social Dynamics are active!                       ║");
        System.out.println("║ - Tracking social interactions                  ║");
        System.out.println("║ - Analyzing social patterns                     ║");
        System.out.println("║ - Managing social relationships                 ║");
        System.out.println("╚═╧╧╧╧╧╧╧╧╧╧╧╧╧╧╧╧╧╧╧╧╧╧╧╧╧╧╧╩═╝");
        System.out.println();
    }
}
