import java.util.*;
import java.util.regex.*;

/**
 * Response Generator with Templates for VirtualXander
 * Generates contextually appropriate responses based on intents
 */
public class ResponseGenerator {
    
    private Map<String, List<ResponseTemplate>> responseTemplates;
    private Random random;
    private ResponseTemplate defaultTemplate;
    
    public ResponseGenerator() {
        this.responseTemplates = new HashMap<>();
        this.random = new Random();
        initializeTemplates();
    }
    
    private void initializeTemplates() {
        // Greeting templates
        addTemplate("greeting",
            new ResponseTemplate("Hi there! How can I help you today?", ResponseType.FRIENDLY),
            new ResponseTemplate("Hello! Great to chat with you. What's on your mind?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Hey! I'm here to help. What would you like to talk about?", ResponseType.WARM),
            new ResponseTemplate("Hi! How's your day going?", ResponseType.FRIENDLY)
        );
        
        // Identity templates
        addTemplate("identity",
            new ResponseTemplate("I'm Xander, your virtual friend! I'm here to chat, help out, and keep you company.", ResponseType.WARM),
            new ResponseTemplate("I'm Xander - think of me as your AI companion ready to assist and chat!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I'm Xander! Your friendly virtual assistant for conversation and support.", ResponseType.PROFESSIONAL)
        );
        
        // Wellbeing positive templates
        addTemplate("wellbeing_positive",
            new ResponseTemplate("That's wonderful to hear! What's making your day so great?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Awesome! I'd love to hear more about what's making you feel fantastic!", ResponseType.INTERESTED),
            new ResponseTemplate("That's great! Positive vibes are contagious. Anything specific putting you in a good mood?", ResponseType.FRIENDLY)
        );
        
        // Wellbeing negative templates
        addTemplate("wellbeing_negative",
            new ResponseTemplate("I'm sorry to hear that. Would you like to talk about what's bothering you?", ResponseType.SYMPATHETIC),
            new ResponseTemplate("That doesn't sound good. I'm here to listen if you want to share what's on your mind.", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Hey, everyone has tough days. Want to chat about what's bringing you down?", ResponseType.WARM)
        );
        
        // Wellbeing how are you templates
        addTemplate("wellbeing_how",
            new ResponseTemplate("I'm doing well, thank you for asking! How are you doing?", ResponseType.FRIENDLY),
            new ResponseTemplate("I'm great! Ready to chat. How about yourself?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I'm doing well! Hope you're having a good day too.", ResponseType.WARM)
        );
        
        // Activity templates
        addTemplate("activity",
            new ResponseTemplate("I'm here to chat with you! What are you up to?", ResponseType.FRIENDLY),
            new ResponseTemplate("Just hanging out here, ready to help. What about you?", ResponseType.RELAXED),
            new ResponseTemplate("I'm here whenever you need me. What are you doing?", ResponseType.WARM)
        );
        
        // Mental health support templates
        addTemplate("mental_health_support",
            new ResponseTemplate("I'm sorry you're feeling this way. I'm here to listen. What's going on?", ResponseType.SYMPATHETIC),
            new ResponseTemplate("That sounds really tough. Would you like to share what's troubling you?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("I'm here for you. Sometimes talking helps - what's on your mind?", ResponseType.WARM),
            new ResponseTemplate("It takes courage to share these feelings. How can I best support you right now?", ResponseType.UNDERSTANDING)
        );
        
        // Mental health positive templates
        addTemplate("mental_health_positive",
            new ResponseTemplate("That's wonderful! What's contributing to these positive feelings?", ResponseType.INTERESTED),
            new ResponseTemplate("It's great to hear you're feeling good! What's been going well?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Positive emotions are so valuable! What sparked these feelings?", ResponseType.FRIENDLY)
        );
        
        // Homework help templates
        addTemplate("homework_help",
            new ResponseTemplate("I'd be happy to help with your homework! What subject are you working on?", ResponseType.HELPFUL),
            new ResponseTemplate("Sure thing! Math, science, history, or something else?", ResponseType.READY),
            new ResponseTemplate("Let's tackle this together! What subject do you need help with?", ResponseType.SUPPORTIVE)
        );
        
        // Gaming templates
        addTemplate("gaming",
            new ResponseTemplate("Gaming is awesome! What games do you enjoy playing?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Nice! I'm a fan of games too. What's your favorite?", ResponseType.INTERESTED),
            new ResponseTemplate("Gamers unite! What are you currently playing?", ResponseType.FRIENDLY)
        );
        
        // Creative writing templates
        addTemplate("creative_writing",
            new ResponseTemplate("Creative writing is fantastic! What are you working on - a story, poem, or something else?", ResponseType.INTERESTED),
            new ResponseTemplate("I'd love to hear about your creative project! What's it about?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Writing can be so rewarding! What genre or topic are you exploring?", ResponseType.SUPPORTIVE)
        );
        
        // Entertainment templates
        addTemplate("entertainment",
            new ResponseTemplate("Tell me more about your interests! What do you enjoy doing?", ResponseType.INTERESTED),
            new ResponseTemplate("I love learning about what people enjoy! What's your favorite hobby or pastime?", ResponseType.FRIENDLY),
            new ResponseTemplate("Everyone needs fun activities! What do you like to do for entertainment?", ResponseType.WARM)
        );
        
        // Advice templates
        addTemplate("advice",
            new ResponseTemplate("I'd be glad to help with that! Here are some suggestions:", ResponseType.HELPFUL),
            new ResponseTemplate("Great question! Let me share some tips that might help:", ResponseType.READY),
            new ResponseTemplate("Absolutely! I can definitely offer some guidance on that.", ResponseType.SUPPORTIVE)
        );
        
        // Help request templates
        addTemplate("help_request",
            new ResponseTemplate("Of course! What do you need help with?", ResponseType.HELPFUL),
            new ResponseTemplate("I'm here to assist! What can I help you with today?", ResponseType.WARM),
            new ResponseTemplate("Sure thing! Tell me more about what you need help with.", ResponseType.FRIENDLY)
        );
        
        // Gratitude templates
        addTemplate("gratitude",
            new ResponseTemplate("You're very welcome! Happy to help anytime.", ResponseType.WARM),
            new ResponseTemplate("Anytime! That's what I'm here for.", ResponseType.FRIENDLY),
            new ResponseTemplate("My pleasure! Don't hesitate to ask if you need anything else.", ResponseType.PROFESSIONAL)
        );
        
        // Farewell templates
        addTemplate("farewell",
            new ResponseTemplate("Goodbye! It was great chatting with you. Take care!", ResponseType.WARM),
            new ResponseTemplate("See you later! Don't be a stranger!", ResponseType.FRIENDLY),
            new ResponseTemplate("Bye for now! Hope to chat again soon!", ResponseType.ENTHUSIASTIC)
        );
        
        // Unknown/Continuation templates
        addTemplate("continue",
            new ResponseTemplate("Got it! What else would you like to talk about?", ResponseType.FRIENDLY),
            new ResponseTemplate("I understand! Is there anything else on your mind?", ResponseType.ATTENTIVE),
            new ResponseTemplate("Okay! Feel free to ask me anything.", ResponseType.WARM)
        );
        
        // Creative project templates
        addTemplate("creative_project",
            new ResponseTemplate("That sounds like an exciting project! What inspired you to create this?", ResponseType.INTERESTED),
            new ResponseTemplate("Building things is so rewarding! What's the goal of your project?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I'd love to hear more about what you're creating!", ResponseType.EXCITED)
        );
        
        // Philosophical templates
        addTemplate("philosophical",
            new ResponseTemplate("Those are deep questions! What thoughts have you been having about this?", ResponseType.THOUGHTFUL),
            new ResponseTemplate("Philosophy is fascinating! What aspect interests you most?", ResponseType.INTERESTED),
            new ResponseTemplate("These are the kinds of questions that make us think! What are you pondering?", ResponseType.WARM)
        );
        
        // Default fallback templates
        this.defaultTemplate = new ResponseTemplate(
            "I'm not sure how to respond to that, but I'm here to chat!",
            ResponseType.NEUTRAL
        );
        addTemplate("default",
            defaultTemplate,
            new ResponseTemplate("That's interesting! Tell me more.", ResponseType.INTERESTED),
            new ResponseTemplate("I appreciate you sharing that. What else would you like to discuss?", ResponseType.FRIENDLY),
            new ResponseTemplate("I see! Is there something specific I can help you with?", ResponseType.HELPFUL)
        );
    }
    
    private void addTemplate(String intent, ResponseTemplate... templates) {
        responseTemplates.computeIfAbsent(intent, k -> new ArrayList<>()).addAll(Arrays.asList(templates));
    }
    
    /**
     * Generates a response based on intent and context
     */
    public String generateResponse(String intent, String userInput, ConversationContext context) {
        // Check for contextual follow-up responses
        if (context != null && context.getRecentHistory(2).size() > 0) {
            String contextualResponse = generateContextualResponse(intent, userInput, context);
            if (contextualResponse != null) {
                return contextualResponse;
            }
        }
        
        // Get templates for the intent
        List<ResponseTemplate> templates = responseTemplates.get(intent);
        
        // If no templates found, use default
        if (templates == null || templates.isEmpty()) {
            templates = responseTemplates.get("default");
        }
        
        if (templates == null || templates.isEmpty()) {
            return defaultTemplate.getText();
        }
        
        // Select a random template
        ResponseTemplate selected = templates.get(random.nextInt(templates.size()));
        String response = selected.getText();
        
        // Add turn to context if context exists
        if (context != null) {
            context.addTurn(userInput, intent, response, intent);
        }
        
        return response;
    }
    
    /**
     * Generates contextual follow-up responses based on conversation history
     */
    private String generateContextualResponse(String intent, String userInput, ConversationContext context) {
        String lastInput = context.getLastUserInput();
        
        if (lastInput == null) return null;
        
        // Handle homework subject follow-ups
        if (lastInput.toLowerCase().contains("what subject") || 
            lastInput.toLowerCase().contains("what topic")) {
            if (userInput.toLowerCase().contains("math") || 
                userInput.toLowerCase().contains("algebra") ||
                userInput.toLowerCase().contains("geometry")) {
                return "Math can be challenging! What specific topic or problem are you working on?";
            }
            if (userInput.toLowerCase().contains("science") ||
                userInput.toLowerCase().contains("biology") ||
                userInput.toLowerCase().contains("physics")) {
                return "Science is fascinating! What specific area are you studying?";
            }
            if (userInput.toLowerCase().contains("history")) {
                return "History is so interesting! What time period or event are you learning about?";
            }
        }
        
        // Handle emotion follow-ups
        if (lastInput.toLowerCase().contains("how are you") ||
            lastInput.toLowerCase().contains("how r u")) {
            if (userInput.toLowerCase().contains("good") ||
                userInput.toLowerCase().contains("great") ||
                userInput.toLowerCase().contains("fine")) {
                return "That's wonderful to hear! What made your day good?";
            }
            if (userInput.toLowerCase().contains("bad") ||
                userInput.toLowerCase().contains("not good")) {
                return "I'm sorry to hear that. Would you like to talk about what's making you feel this way?";
            }
        }
        
        // Handle yes/no follow-ups
        if (userInput.equalsIgnoreCase("yes") || userInput.equalsIgnoreCase("yeah")) {
            return "That's great! Tell me more about it.";
        }
        
        if (userInput.equalsIgnoreCase("no") || userInput.equalsIgnoreCase("nope")) {
            return "No worries! Is there something else you'd like to talk about?";
        }
        
        return null;
    }
    
    /**
     * Gets available response types
     */
    public ResponseType getResponseTone(String intent) {
        List<ResponseTemplate> templates = responseTemplates.get(intent);
        if (templates != null && !templates.isEmpty()) {
            return templates.get(0).getType();
        }
        return ResponseType.NEUTRAL;
    }
    
    /**
     * Response template inner class
     */
    public static class ResponseTemplate {
        private String text;
        private ResponseType type;
        
        public ResponseTemplate(String text, ResponseType type) {
            this.text = text;
            this.type = type;
        }
        
        public String getText() {
            return text;
        }
        
        public ResponseType getType() {
            return type;
        }
    }
    
    /**
     * Enum for response types/tones
     */
    public enum ResponseType {
        NEUTRAL("neutral"),
        FRIENDLY("friendly"),
        ENTHUSIASTIC("enthusiastic"),
        WARM("warm"),
        HELPFUL("helpful"),
        SUPPORTIVE("supportive"),
        SYMPATHETIC("sympathetic"),
        UNDERSTANDING("understanding"),
        INTERESTED("interested"),
        PROFESSIONAL("professional"),
        ATTENTIVE("attentive"),
        THOUGHTFUL("thoughtful"),
        EXCITED("excited"),
        READY("ready"),
        RELAXED("relaxed");
        
        private final String value;
        
        ResponseType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}

