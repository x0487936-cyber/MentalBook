#!/bin/bash

# Chatbot Test Script for VirtualXander
# Tests various intents and responses

echo "============================================"
echo "  VirtualXander Chatbot Test Script"
echo "============================================"
echo ""

# Compile test file
echo "Compiling test file..."
cd /home/x0487936/MentalBook

# Create a simple test that runs in-line
cat > /tmp/TestRunner.java << 'EOF'
import java.util.*;
import java.lang.reflect.*;

public class TestRunner {
    public static void main(String[] args) throws Exception {
        // Load classes
        Class<?> intentClass = Class.forName("IntentRecognizer");
        Class<?> responseClass = Class.forName("ResponseGenerator");
        Class<?> contextClass = Class.forName("ConversationContext");
        
        Object intentRecognizer = intentClass.getDeclaredConstructor().newInstance();
        Object responseGenerator = responseClass.getDeclaredConstructor().newInstance();
        Object context = contextClass.getDeclaredConstructor().newInstance();
        
        Method recognizeIntent = intentClass.getMethod("recognizeIntent", String.class);
        Method generateResponse = responseClass.getMethod("generateResponse", String.class, String.class, contextClass);
        
        int passed = 0;
        int failed = 0;
        List<String> failures = new ArrayList<>();
        
        // Test cases: input -> expected intent
        String[][] tests = {
            // Greetings
            {"hi", "greeting"},
            {"hello", "greeting"},
            {"hey", "greeting"},
            {"good morning", "greeting"},
            
            // Identity
            {"what's your name", "identity"},
            {"who are you", "identity"},
            
            // Wellbeing
            {"how are you", "wellbeing_how"},
            {"i am good", "wellbeing_response"},
            {"i'm great", "wellbeing_response"},
            {"good", "wellbeing_response"},
            {"a lot", "wellbeing_a_lot"},
            {"not good", "wellbeing_negative"},
            {"i'm sad", "wellbeing_negative"},
            {"how was your day", "wellbeing_day"},
            
            // Activity
            {"what are you doing", "activity"},
            {"nothing much", "activity_response"},
            {"bored", "activity_response"},
            {"just chilling", "activity_response"},
            {"what's up", "activity"},
            
            // Homework
            {"i need help with homework", "homework_help"},
            {"help with math", "homework_subject"},
            {"algebra", "homework_subject"},
            
            // Mental Health
            {"i feel stressed", "mental_health_support"},
            {"i am anxious", "mental_health_support"},
            {"feeling down", "mental_health_support"},
            {"dark thoughts", "mental_health_support"},
            {"i feel happy", "mental_health_positive"},
            
            // Gaming
            {"i play fortnite", "gaming_game"},
            {"minecraft", "gaming_game"},
            {"mirage", "gaming_map"},
            {"i want to play", "gaming"},
            {"any game recommendations", "gaming_recommendation"},
            
            // Creative Writing
            {"i am writing a story", "creative_writing"},
            {"story idea", "creative_writing"},
            {"i want to write", "creative_writing"},
            {"i need a story idea", "creative_writing"},
            {"mystery", "creative_writing"},
            
            // Entertainment
            {"favorite movie", "entertainment_type"},
            {"i like music", "entertainment_type"},
            {"what's on netflix", "entertainment"},
            {"sports", "entertainment"},
            {"sports", "entertainment_type"},
            {"what's on youtube", "entertainment"},
            
            // Advice
            {"need advice", "advice"},
            {"any advice", "advice"},
            {"i need advice", "advice"},
            {"advice on studying", "advice_topic"},
            {"study tips", "advice_topic"},
            
            // Help
            {"need help", "help_request"},
            {"can you help me", "help_request"},
            {"i need help", "help_request"},
            {"i have a question", "help_type"},
            
            // Gratitude
            {"thank you", "gratitude"},
            {"thanks", "gratitude"},
            
            // Farewell
            {"bye", "farewell"},
            {"goodbye", "farewell"},
            {"see you later", "farewell"},
            
            // Relationship
            {"my relationship", "relationship"},
            {"dating", "relationship"},
            
            // Breakup
            {"my ex", "breakup"},
            {"i got dumped", "breakup"},
            {"i broke up", "breakup"},
            {"heartbroken", "breakup"},
            
            // Philosophical
            {"purpose of life", "philosophical"},
            {"meaning of life", "philosophical"},
            
            // Confusion
            {"what", "confusion"},
            {"huh", "confusion"},
            
            // Continue
            {"yes", "continue"},
            {"no", "continue"},
            {"cool", "continue"},
            
            // Creative Project
            {"building a robot", "creative_project"},
            
            // Special cases
            {"", "unknown"},
            {"   ", "unknown"}
        };
        
        System.out.println("Running Intent Recognition Tests...");
        System.out.println("=====================================\n");
        
        for (String[] test : tests) {
            String input = test[0];
            String expectedIntent = test[1];
            
            String actualIntent = (String) recognizeIntent.invoke(intentRecognizer, input);
            
            if (actualIntent.equals(expectedIntent)) {
                passed++;
                System.out.println("✓ '" + input + "' -> " + actualIntent);
            } else {
                failed++;
                String msg = "✗ '" + input + "' Expected: " + expectedIntent + ", Got: " + actualIntent;
                failures.add(msg);
                System.out.println(msg);
            }
        }
        
        // Test response generation
        System.out.println("\n=====================================");
        System.out.println("Testing Response Generation...");
        System.out.println("=====================================\n");
        
        String[] responseTests = {
            "hi", "how are you", "i am good", "thank you", "bye",
            "i need help", "fortnite", "nothing much",
            "i feel stressed", "what's your name", "purpose of life"
        };
        
        for (String input : responseTests) {
            String intent = (String) recognizeIntent.invoke(intentRecognizer, input);
            String response = (String) generateResponse.invoke(responseGenerator, intent, input, context);
            
            if (response != null && !response.isEmpty()) {
                passed++;
                System.out.println("✓ Response generated for: '" + input + "'");
                System.out.println("  Intent: " + intent);
                System.out.println("  Response: " + response.substring(0, Math.min(50, response.length())) + "...");
            } else {
                failed++;
                failures.add("Empty response for input: '" + input + "'");
                System.out.println("✗ Empty response for: '" + input + "'");
            }
        }
        
        // Print results
        System.out.println("\n=====================================");
        System.out.println("TEST RESULTS");
        System.out.println("=====================================");
        System.out.println("Total Tests: " + (passed + failed));
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Pass Rate: " + String.format("%.1f", (double) passed / (passed + failed) * 100) + "%");
        
        if (!failures.isEmpty()) {
            System.out.println("\nFAILURES:");
            for (String f : failures) {
                System.out.println("  " + f);
            }
        }
        
        System.out.println("\n" + (failed == 0 ? "✓ ALL TESTS PASSED!" : "✗ SOME TESTS FAILED!"));
    }
}
EOF

echo "Compiling test runner..."
javac -cp bin -d /tmp /tmp/TestRunner.java 2>&1

echo ""
echo "Running tests..."
echo "============================================"
java -cp bin:/tmp TestRunner 2>&1

# ============================================
# ComfortEngine Tests
# ============================================
echo ""
echo "============================================"
echo "  ComfortEngine Validation Tests"
echo "============================================"
echo ""

echo "Compiling ComfortEngineTest..."
javac -d bin -cp bin ComfortEngineTest.java 2>&1

echo ""
echo "Running ComfortEngine tests..."
echo "============================================"
java -cp bin ComfortEngineTest 2>&1

