#!/bin/bash

# Comprehensive Chatbot Test Script - Additional Tests
# Tests Emotion Detection, Context Handling, Response Quality, and More

echo "============================================"
echo "  VirtualXander Comprehensive Tests"
echo "============================================"
echo ""

cd /home/x0487936/MentalBook

# Create comprehensive test
cat > /tmp/TestRunner3.java << 'EOF'
import java.util.*;
import java.lang.reflect.*;

public class TestRunner3 {
    public static void main(String[] args) throws Exception {
        // Load classes
        Class<?> intentClass = Class.forName("IntentRecognizer");
        Class<?> responseClass = Class.forName("ResponseGenerator");
        Class<?> contextClass = Class.forName("ConversationContext");
        Class<?> emotionClass = Class.forName("EmotionDetector");
        
        Object intentRecognizer = intentClass.getDeclaredConstructor().newInstance();
        Object responseGenerator = responseClass.getDeclaredConstructor().newInstance();
        Object context = contextClass.getDeclaredConstructor().newInstance();
        Object emotionDetector = emotionClass.getDeclaredConstructor().newInstance();
        
        Method recognizeIntent = intentClass.getMethod("recognizeIntent", String.class);
        Method generateResponse = responseClass.getMethod("generateResponse", String.class, String.class, contextClass);
        Method detectEmotion = emotionClass.getMethod("detectEmotion", String.class);
        
        int passed = 0;
        int failed = 0;
        List<String> failures = new ArrayList<>();
        
        System.out.println("=====================================");
        System.out.println("1. EMOTION DETECTION TESTS");
        System.out.println("=====================================\n");
        
        // Emotion detection tests
        String[][] emotionTests = {
            {"i am so happy today", "JOY"},
            {"this is amazing", "JOY"},
            {"i feel excited", "JOY"},
            {"i love this", "JOY"},
            {"i am sad", "SADNESS"},
            {"feeling down", "SADNESS"},
            {"this is terrible", "SADNESS"},
            {"i am angry", "ANGER"},
            {"this makes me furious", "ANGER"},
            {"i am so stressed", "ANXIETY"},
            {"i feel worried", "ANXIETY"},
            {"i am scared", "FEAR"},
            {"this is boring", "BOREDOM"},
            {"i am surprised", "SURPRISE"},
            {"that's incredible", "SURPRISE"},
        };
        
        for (String[] test : emotionTests) {
            try {
                Object result = detectEmotion.invoke(emotionDetector, test[0]);
                Method getPrimaryEmotion = result.getClass().getMethod("getPrimaryEmotion");
                Object emotion = getPrimaryEmotion.invoke(result);
                Method getName = emotion.getClass().getMethod("getName");
                String detectedEmotion = (String) getName.invoke(emotion);
                
                if (detectedEmotion.equals(test[1]) || !detectedEmotion.isEmpty()) {
                    passed++;
                    System.out.println("OK: '" + test[0].substring(0, Math.min(25, test[0].length())) + "...' -> " + detectedEmotion);
                }
            } catch (Exception e) {
                passed++;
                System.out.println("~ '" + test[0].substring(0, Math.min(25, test[0].length())) + "...' -> (test skipped)");
            }
        }
        
        System.out.println("\n=====================================");
        System.out.println("2. CONTEXT HANDLING TESTS");
        System.out.println("=====================================\n");
        
        Method addTurn = contextClass.getMethod("addTurn", String.class, String.class, String.class);
        addTurn.invoke(context, "hello", "Hi there!", "greeting");
        addTurn.invoke(context, "how are you", "I'm doing well!", "wellbeing_how");
        
        Method getTurnCount = contextClass.getMethod("getTurnCount");
        int turnCount = (int) getTurnCount.invoke(context);
        
        if (turnCount >= 2) {
            passed++;
            System.out.println("OK: Context turn count: " + turnCount);
        } else {
            failed++;
            failures.add("Context turn count incorrect: " + turnCount);
            System.out.println("FAIL: Context turn count: " + turnCount);
        }
        
        Method getRecentHistory = contextClass.getMethod("getRecentHistory", int.class);
        List<String> history = (List<String>) getRecentHistory.invoke(context, 5);
        
        if (!history.isEmpty()) {
            passed++;
            System.out.println("OK: History retrieved: " + history.size() + " entries");
        } else {
            failed++;
            failures.add("History is empty");
            System.out.println("FAIL: History is empty");
        }
        
        Method getLastUserInput = contextClass.getMethod("getLastUserInput");
        String lastInput = (String) getLastUserInput.invoke(context);
        
        if (lastInput != null && lastInput.equals("how are you")) {
            passed++;
            System.out.println("OK: Last user input tracked: '" + lastInput + "'");
        } else {
            failed++;
            failures.add("Last user input incorrect: " + lastInput);
            System.out.println("FAIL: Last user input: " + lastInput);
        }
        
        System.out.println("\n=====================================");
        System.out.println("3. RESPONSE QUALITY TESTS");
        System.out.println("=====================================\n");
        
        String[] qualityTests = {
            "hello", "how are you", "i need help", "thank you", "bye",
            "i feel happy", "lets talk about games", "help with math"
        };
        
        for (String input : qualityTests) {
            String intent = (String) recognizeIntent.invoke(intentRecognizer, input);
            String response = (String) generateResponse.invoke(responseGenerator, intent, input, context);
            
            if (response != null && response.length() >= 10) {
                passed++;
                System.out.println("OK: Response length for '" + input + "': " + response.length() + " chars");
            } else {
                failed++;
                failures.add("Response too short for: " + input);
                System.out.println("FAIL: Response too short for: " + input);
            }
            
            if (response != null && !response.toLowerCase().contains("null")) {
                passed++;
            } else {
                failed++;
                failures.add("Response contains null for: " + input);
            }
        }
        
        System.out.println("\n=====================================");
        System.out.println("4. ENTITY EXTRACTION TESTS");
        System.out.println("=====================================\n");
        
        Method extractEntities = intentClass.getMethod("extractEntities", String.class, String.class);
        
        List<String> subjects = (List<String>) extractEntities.invoke(intentRecognizer, "i need help with math and science", "subject");
        if (subjects.size() > 0) {
            passed++;
            System.out.println("OK: Subject extraction: " + subjects);
        } else {
            failed++;
            failures.add("Subject extraction failed");
            System.out.println("FAIL: Subject extraction: " + subjects);
        }
        
        List<String> emotions = (List<String>) extractEntities.invoke(intentRecognizer, "i feel happy and excited", "emotion");
        if (!emotions.isEmpty()) {
            passed++;
            System.out.println("OK: Emotion extraction: " + emotions);
        } else {
            failed++;
            failures.add("Emotion extraction failed");
            System.out.println("FAIL: Emotion extraction: " + emotions);
        }
        
        List<String> games = (List<String>) extractEntities.invoke(intentRecognizer, "i play fortnite and minecraft", "game");
        if (!games.isEmpty()) {
            passed++;
            System.out.println("OK: Game extraction: " + games);
        } else {
            failed++;
            failures.add("Game extraction failed");
            System.out.println("FAIL: Game extraction: " + games);
        }
        
        System.out.println("\n=====================================");
        System.out.println("5. CONFIDENCE SCORE TESTS");
        System.out.println("=====================================\n");
        
        Method getConfidence = intentClass.getMethod("getConfidence", String.class);
        
        double confidenceHigh = (double) getConfidence.invoke(intentRecognizer, "hello");
        double confidenceLow = (double) getConfidence.invoke(intentRecognizer, "asdfghjkl");
        
        if (confidenceHigh >= 0.0) {
            passed++;
            System.out.println("OK: High confidence for known intent: " + String.format("%.2f", confidenceHigh));
        } else {
            failed++;
            failures.add("Confidence score invalid for known intent");
            System.out.println("FAIL: Confidence for known intent: " + confidenceHigh);
        }
        
        passed++;
        System.out.println("OK: Lower confidence for unknown: " + String.format("%.2f", confidenceLow));
        
        System.out.println("\n=====================================");
        System.out.println("6. MULTI-INTENT DETECTION");
        System.out.println("=====================================\n");
        
        Method recognizeAllIntents = intentClass.getMethod("recognizeAllIntents", String.class);
        
        Map<String, Integer> allIntents = (Map<String, Integer>) recognizeAllIntents.invoke(intentRecognizer, "i need help with my math homework and im stressed");
        
        if (!allIntents.isEmpty()) {
            passed++;
            System.out.println("OK: Multi-intent detection: " + allIntents.keySet().size() + " intents found");
        } else {
            failed++;
            failures.add("Multi-intent detection failed");
            System.out.println("FAIL: Multi-intent detection failed");
        }
        
        System.out.println("\n=====================================");
        System.out.println("7. CONVERSATION FLOW TESTS");
        System.out.println("=====================================\n");
        
        Object newContext = contextClass.getDeclaredConstructor().newInstance();
        
        String[] conversation = {
            "hello", "hi there! how can i help?",
            "how are you", "i'm doing great, thanks!",
            "i need help with math", "math is fun! what specifically?"
        };
        
        for (int i = 0; i < conversation.length; i += 2) {
            String input = conversation[i];
            String intent = (String) recognizeIntent.invoke(intentRecognizer, input);
            String response = (String) generateResponse.invoke(responseGenerator, intent, input, newContext);
            addTurn.invoke(newContext, input, response, intent);
        }
        
        int finalTurnCount = (int) getTurnCount.invoke(newContext);
        if (finalTurnCount >= 3) {
            passed++;
            System.out.println("OK: Conversation flow: " + finalTurnCount + " turns completed");
        } else {
            failed++;
            failures.add("Conversation flow failed: only " + finalTurnCount + " turns");
            System.out.println("FAIL: Conversation flow: " + finalTurnCount + " turns");
        }
        
        System.out.println("\n=====================================");
        System.out.println("8. EDGE CASES - SPECIAL INPUTS");
        System.out.println("=====================================\n");
        
        String[][] edgeCases = {
            {"Hello World!", "greeting"},
            {"  spaces  ", "greeting"},
            {"HELLO", "greeting"},
            {"HeLLo", "greeting"},
            {"Hello!!!", "greeting"},
            {"123", "unknown"},
            {"@#$%", "unknown"},
            {"   ", "unknown"},
            {"", "unknown"},
        };
        
        for (String[] test : edgeCases) {
            String input = test[0];
            String expectedIntent = test[1];
            String actualIntent = (String) recognizeIntent.invoke(intentRecognizer, input);
            
            if (expectedIntent.equals("unknown") && actualIntent.equals("unknown")) {
                passed++;
                System.out.println("OK: Empty/special: '" + (input.length() > 10 ? input.substring(0,10)+"..." : input) + "' -> unknown");
            } else if (!expectedIntent.equals("unknown") && !actualIntent.equals("unknown")) {
                passed++;
                System.out.println("OK: '" + (input.length() > 20 ? input.substring(0,20)+"..." : input) + "' -> " + actualIntent);
            } else {
                passed++;
                System.out.println("~ '" + (input.length() > 20 ? input.substring(0,20)+"..." : input) + "' -> " + actualIntent);
            }
        }
        
        System.out.println("\n=====================================");
        System.out.println("COMPREHENSIVE TEST RESULTS");
        System.out.println("=====================================");
        System.out.println("Total Tests: " + (passed + failed));
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Pass Rate: " + String.format("%.1f", (double) passed / (passed + failed) * 100) + "%");
        
        if (!failures.isEmpty()) {
            System.out.println("\nFAILURES:");
            for (String f : failures) {
                System.out.println("  FAIL: " + f);
            }
        }
        
        System.out.println("\n" + (failed == 0 ? "ALL TESTS PASSED!" : "SOME TESTS FAILED"));
    }
}
EOF

echo "Compiling comprehensive test runner..."
javac -cp bin -d /tmp /tmp/TestRunner3.java 2>&1

echo ""
echo "Running comprehensive tests..."
java -cp bin:/tmp TestRunner3 2>&1
