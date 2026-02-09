import java.util.*;
import java.util.regex.*;

/**
 * GreetingHandler - Handles greetings and basic chat responses
 * Part of Phase 3: Response Categories
 */
public class GreetingHandler {
    
    private List<GreetingPattern> greetingPatterns;
    private Random random;
    
    public GreetingHandler() {
        this.greetingPatterns = new ArrayList<>();
        this.random = new Random();
        initializeGreetingPatterns();
    }
    
    /**
     * Greeting pattern class
     */
    private static class GreetingPattern {
        List<String> triggers;
        List<String> responses;
        GreetingType type;
        
        public GreetingPattern(GreetingType type, List<String> triggers, List<String> responses) {
            this.type = type;
            this.triggers = triggers;
            this.responses = responses;
        }
    }
    
    /**
     * Types of greetings
     */
    public enum GreetingType {
        CASUAL("casual"),
        FORMAL("formal"),
        ENTHUSIASTIC("enthusiastic"),
        WARM("warm"),
        PLAYFUL("playful");
        
        private final String value;
        
        GreetingType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    private void initializeGreetingPatterns() {
        // Standard greetings
        greetingPatterns.add(new GreetingPattern(
            GreetingType.CASUAL,
            Arrays.asList("hello", "hi", "hey", "hiya"),
            Arrays.asList(
                "Hello! How can I help you today?",
                "Hi there! What's on your mind?",
                "Hey! Great to see you!",
                "Hi! How's your day going?"
            )
        ));
        
        // Enthusiastic greetings
        greetingPatterns.add(new GreetingPattern(
            GreetingType.ENTHUSIASTIC,
            Arrays.asList("hey there", "howdy", "yo", "sup"),
            Arrays.asList(
                "Hey there! Awesome to see you!",
                "Howdy partner! What's up?",
                "Yo! Ready to chat!",
                "Sup! I'm here and ready to help!"
            )
        ));
        
        // Time-based greetings
        greetingPatterns.add(new GreetingPattern(
            GreetingType.WARM,
            Arrays.asList("good morning", "good afternoon", "good evening", "good day"),
            Arrays.asList(
                "Good morning! Hope you're having a great start to your day!",
                "Good afternoon! How's everything going?",
                "Good evening! Great to see you!",
                "Good day to you! What can I help with?"
            )
        ));
        
        // Identity questions
        greetingPatterns.add(new GreetingPattern(
            GreetingType.CASUAL,
            Arrays.asList("who are you", "what's your name", "your name", "who r u"),
            Arrays.asList(
                "I'm Xander, your friendly virtual companion! I'm here to chat, help, and keep you company.",
                "I'm Xander - think of me as your AI friend who's always here to listen and help!",
                "Hey! I'm Xander, your virtual buddy. Nice to meet you!"
            )
        ));
        
        // How are you responses
        greetingPatterns.add(new GreetingPattern(
            GreetingType.WARM,
            Arrays.asList("how are you", "how r u", "hru", "how's it going", "how do you do"),
            Arrays.asList(
                "I'm doing great, thank you for asking! How are you doing?",
                "I'm wonderful since I get to chat with you! How about yourself?",
                "I'm doing really well! Hope you're having a good day too!",
                "I'm fantastic! Thanks for asking. How are you feeling?"
            )
        ));
        
        // What's up responses
        greetingPatterns.add(new GreetingPattern(
            GreetingType.CASUAL,
            Arrays.asList("what's up", "wassup", "whats up", "what up"),
            Arrays.asList(
                "Not much! Just here waiting to chat with you. What's up with you?",
                "I'm all set and ready to help! What's on your mind?",
                "Just hanging out here. What's going on with you?"
            )
        ));
        
        // Status check responses
        greetingPatterns.add(new GreetingPattern(
            GreetingType.CASUAL,
            Arrays.asList("how's everything", "how's life", "how's your day"),
            Arrays.asList(
                "Everything's going great! How's life treating you?",
                "My day's been good! How about yours?",
                "Life's good over here! Hope yours is too!"
            )
        ));
    }
    
    /**
     * Handles greeting input and returns appropriate response
     */
    public String handleGreeting(String input) {
        String lowerInput = input.toLowerCase().trim();
        
        // Find matching pattern
        GreetingPattern match = findBestMatch(lowerInput);
        
        if (match != null) {
            return getRandomResponse(match.responses);
        }
        
        // Default greeting response
        return "Hello there! How can I help you today?";
    }
    
    /**
     * Checks if input is a greeting
     */
    public boolean isGreeting(String input) {
        String lowerInput = input.toLowerCase().trim();
        return findBestMatch(lowerInput) != null;
    }
    
    /**
     * Gets the type of greeting detected
     */
    public GreetingType getGreetingType(String input) {
        String lowerInput = input.toLowerCase().trim();
        GreetingPattern match = findBestMatch(lowerInput);
        return match != null ? match.type : null;
    }
    
    private GreetingPattern findBestMatch(String input) {
        GreetingPattern bestMatch = null;
        int bestScore = 0;
        
        for (GreetingPattern pattern : greetingPatterns) {
            int score = calculateMatchScore(input, pattern.triggers);
            if (score > bestScore) {
                bestScore = score;
                bestMatch = pattern;
            }
        }
        
        return bestScore > 0 ? bestMatch : null;
    }
    
    private int calculateMatchScore(String input, List<String> triggers) {
        int score = 0;
        
        for (String trigger : triggers) {
            if (input.equals(trigger)) {
                score += 10; // Exact match
            } else if (input.startsWith(trigger) || input.endsWith(trigger)) {
                score += 5; // Partial match
            } else if (input.contains(trigger)) {
                score += 3; // Contains trigger
            }
        }
        
        return score;
    }
    
    private String getRandomResponse(List<String> responses) {
        return responses.get(random.nextInt(responses.size()));
    }
    
    /**
     * Gets all available greeting types
     */
    public List<GreetingType> getAvailableGreetingTypes() {
        return Arrays.asList(GreetingType.values());
    }
    
    /**
     * Gets response template for a specific greeting type
     */
    public String getResponseForType(GreetingType type) {
        for (GreetingPattern pattern : greetingPatterns) {
            if (pattern.type == type) {
                return getRandomResponse(pattern.responses);
            }
        }
        return null;
    }
    
    /**
     * Basic chat response handler
     */
    public String handleBasicChat(String input) {
        String lowerInput = input.toLowerCase().trim();
        
        // Handle common basic chat phrases
        if (lowerInput.contains("thanks") || lowerInput.contains("thank you")) {
            return "You're very welcome! Happy to help!";
        }
        
        if (lowerInput.contains("sorry") || lowerInput.contains("apologize")) {
            return "No worries at all!";
        }
        
        if (lowerInput.contains("please")) {
            return "Of course! I'm here to help.";
        }
        
        if (lowerInput.contains("cool") || lowerInput.contains("awesome") || 
            lowerInput.contains("great") || lowerInput.contains("nice")) {
            String[] responses = {
                "I agree!",
                "That's great to hear!",
                "Absolutely!",
                "Couldn't agree more!"
            };
            return responses[random.nextInt(responses.length)];
        }
        
        if (lowerInput.contains("oh") || lowerInput.contains("ah") || 
            lowerInput.contains("huh") || lowerInput.contains("wow")) {
            String[] responses = {
                "I know, right?",
                "Pretty interesting, isn't it?",
                "I thought so too!",
                "Definitely!"
            };
            return responses[random.nextInt(responses.length)];
        }
        
        if (lowerInput.contains("lol") || lowerInput.contains("haha") || 
            lowerInput.contains("hehe") || lowerInput.contains("ahaha")) {
            return "Glad I could make you laugh! 😊";
        }
        
        // Default response for unrecognized basic chat
        String[] defaultResponses = {
            "That's interesting! Tell me more.",
            "I see! What else is on your mind?",
            "Got it! Anything else?",
            "Hmm, tell me more about that.",
            "Interesting! What's your thoughts on that?"
        };
        return defaultResponses[random.nextInt(defaultResponses.length)];
    }
    
    /**
     * Handles farewell messages
     */
    public String handleFarewell(String input) {
        String lowerInput = input.toLowerCase().trim();
        
        if (lowerInput.contains("bye") || lowerInput.contains("goodbye") || 
            lowerInput.contains("see you") || lowerInput.contains("later")) {
            String[] responses = {
                "Goodbye! It was great chatting with you! Take care!",
                "See you later! Don't be a stranger!",
                "Bye for now! Hope to chat again soon!",
                "Bye! Thanks for talking with me today!"
            };
            return responses[random.nextInt(responses.length)];
        }
        
        return null; // Not a farewell
    }
}
