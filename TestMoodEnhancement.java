import java.util.*;

public class TestMoodEnhancement {
    public static void main(String[] args) {
        System.out.println("Testing MoodEnhancer Implementation...");
        System.out.println();
        
        EmotionDetector ed = new EmotionDetector();
        MoodEngine me = new MoodEngine(ed);
        
        // Test 1: Tired user - should get energy boost
        System.out.println("Test 1: User feeling tired");
        MoodEngine.MoodAnalysis ma1 = me.analyzeMood("I am feeling really tired today");
        MoodEngine.MoodEnhancer enhancer1 = me.getMoodEnhancer();
        MoodEngine.MoodEnhancer.EnhancementResult result1 = enhancer1.enhanceMood(ma1);
        System.out.println("  Detected Emotion: " + ma1.getSurfaceEmotion());
        System.out.println("  Enhancement Type: " + result1.getEnhancementType().getName());
        System.out.println("  Message: " + result1.getEnhancementMessage());
        System.out.println("  Action: " + result1.getSuggestedAction());
        System.out.println();
        
        // Test 2: Stressed user - should get perspective lifting
        System.out.println("Test 2: User feeling stressed");
        MoodEngine.MoodAnalysis ma2 = me.analyzeMood("I'm so stressed about work");
        MoodEngine.MoodEnhancer enhancer2 = me.getMoodEnhancer();
        MoodEngine.MoodEnhancer.EnhancementResult result2 = enhancer2.enhanceMood(ma2);
        System.out.println("  Detected Emotion: " + ma2.getSurfaceEmotion());
        System.out.println("  Enhancement Type: " + result2.getEnhancementType().getName());
        System.out.println("  Message: " + result2.getEnhancementMessage());
        System.out.println("  Action: " + result2.getSuggestedAction());
        System.out.println();
        
        // Test 3: Anxious user - should get confidence building
        System.out.println("Test 3: User feeling anxious");
        MoodEngine.MoodAnalysis ma3 = me.analyzeMood("I'm so anxious about the exam");
        MoodEngine.MoodEnhancer enhancer3 = me.getMoodEnhancer();
        MoodEngine.MoodEnhancer.EnhancementResult result3 = enhancer3.enhanceMood(ma3);
        System.out.println("  Detected Emotion: " + ma3.getSurfaceEmotion());
        System.out.println("  Enhancement Type: " + result3.getEnhancementType().getName());
        System.out.println("  Message: " + result3.getEnhancementMessage());
        System.out.println("  Action: " + result3.getSuggestedAction());
        System.out.println();
        
        // Test 4: Sad user - should get energy boost
        System.out.println("Test 4: User feeling sad");
        MoodEngine.MoodAnalysis ma4 = me.analyzeMood("I feel so sad and down");
        MoodEngine.MoodEnhancer enhancer4 = me.getMoodEnhancer();
        MoodEngine.MoodEnhancer.EnhancementResult result4 = enhancer4.enhanceMood(ma4);
        System.out.println("  Detected Emotion: " + ma4.getSurfaceEmotion());
        System.out.println("  Enhancement Type: " + result4.getEnhancementType().getName());
        System.out.println("  Message: " + result4.getEnhancementMessage());
        System.out.println("  Action: " + result4.getSuggestedAction());
        System.out.println();
        
        // Test 5: Bored user - should get energy boost
        System.out.println("Test 5: User feeling bored");
        MoodEngine.MoodAnalysis ma5 = me.analyzeMood("I'm so bored right now");
        MoodEngine.MoodEnhancer enhancer5 = me.getMoodEnhancer();
        MoodEngine.MoodEnhancer.EnhancementResult result5 = enhancer5.enhanceMood(ma5);
        System.out.println("  Detected Emotion: " + ma5.getSurfaceEmotion());
        System.out.println("  Enhancement Type: " + result5.getEnhancementType().getName());
        System.out.println("  Message: " + result5.getEnhancementMessage());
        System.out.println("  Action: " + result5.getSuggestedAction());
        System.out.println();
        
        System.out.println("All tests completed!");
    }
}
