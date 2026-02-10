// Created in Version 0.1.0.0
// Last Updated in Version 0.1.0.3
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
        // Greeting templates (10+ responses)
        addTemplate("greeting",
            new ResponseTemplate("Hi there! How can I help you today?", ResponseType.FRIENDLY),
            new ResponseTemplate("Hello! Great to chat with you. What's on your mind?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Hey! I'm here to help. What would you like to talk about?", ResponseType.WARM),
            new ResponseTemplate("Hi! How's your day going?", ResponseType.FRIENDLY),
            new ResponseTemplate("Hello, friend! What can I do for you today?", ResponseType.WARM),
            new ResponseTemplate("Hey there! Always great to have someone to chat with!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Hi! I'm all ears - what's on your mind?", ResponseType.INTERESTED),
            new ResponseTemplate("Welcome back! What shall we talk about today?", ResponseType.FRIENDLY),
            new ResponseTemplate("Hey! I've been looking forward to chatting. What's up?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Hi there! How are you doing today?", ResponseType.WARM),
            new ResponseTemplate("Hello! Great to see you. What would you like to discuss?", ResponseType.FRIENDLY)
        );
        
        // Identity templates
        addTemplate("identity",
            new ResponseTemplate("I'm Xander, your virtual friend! I'm here to chat, help out, and keep you company.", ResponseType.WARM),
            new ResponseTemplate("I'm Xander - think of me as your AI companion ready to assist and chat!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I'm Xander! Your friendly virtual assistant for conversation and support.", ResponseType.PROFESSIONAL),
            new ResponseTemplate("I'm Xander, here to help make your day a little brighter!", ResponseType.WARM),
            new ResponseTemplate("I'm your buddy Xander! I love chatting and helping out however I can.", ResponseType.FRIENDLY),
            new ResponseTemplate("Hey! I'm Xander, your virtual conversation partner.", ResponseType.ENTHUSIASTIC)
        );
        
        // Wellbeing positive templates (10+ responses)
        addTemplate("wellbeing_positive",
            new ResponseTemplate("That's wonderful to hear! What's making your day so great?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Awesome! I'd love to hear more about what's making you feel fantastic!", ResponseType.INTERESTED),
            new ResponseTemplate("That's great! Positive vibes are contagious. Anything specific putting you in a good mood?", ResponseType.FRIENDLY),
            new ResponseTemplate("I love hearing that! What's the highlight of your day so far?", ResponseType.INTERESTED),
            new ResponseTemplate("That's fantastic! Good energy is always welcome here.", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Wonderful! It's great that you're feeling good. What's contributing to that?", ResponseType.WARM),
            new ResponseTemplate("Excellent! I believe good moods are meant to be shared. Tell me more!", ResponseType.INTERESTED),
            new ResponseTemplate("That's so great to hear! I appreciate you sharing the positive vibes.", ResponseType.FRIENDLY),
            new ResponseTemplate("I love that for you! What's making you feel this way?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("That's awesome! Positive feelings are the best. Anything else exciting happening?", ResponseType.INTERESTED),
            new ResponseTemplate("How wonderful! I'd love to hear what's putting that smile on your face.", ResponseType.WARM),
            // Moderate responses for simple inputs like "a lot", "yeah"
            new ResponseTemplate("That's good to hear! How else is your day going?", ResponseType.FRIENDLY),
            new ResponseTemplate("Glad to hear it! What else has been on your mind?", ResponseType.WARM),
            new ResponseTemplate("Nice! Anything else you'd like to chat about?", ResponseType.FRIENDLY),
            new ResponseTemplate("That's great! What's making you feel positive today?", ResponseType.INTERESTED)
        );
        
        // Wellbeing response with "how about you" (hbu) templates
        addTemplate("wellbeing_hbu",
            new ResponseTemplate("That's great to hear! I'm doing well too, thanks for asking!", ResponseType.WARM),
            new ResponseTemplate("Awesome! I'm doing wonderfully and ready to chat!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Good to know you're doing well! I'm here and ready to help with whatever you need.", ResponseType.FRIENDLY),
            new ResponseTemplate("I'm doing fantastic too! Thanks for checking in. What would you like to talk about?", ResponseType.INTERESTED),
            new ResponseTemplate("I'm doing great, buddy! Always happy when I get to chat with you.", ResponseType.WARM),
            new ResponseTemplate("I'm wonderful! Thanks for asking. How about we keep this conversation going?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I'm all good! Ready and waiting to help with whatever you need.", ResponseType.HELPFUL),
            new ResponseTemplate("I'm doing really well! It's so nice of you to check in on me.", ResponseType.WARM)
        );
        
        // Wellbeing negative templates (10+ responses)
        addTemplate("wellbeing_negative",
            new ResponseTemplate("I'm sorry to hear that. Would you like to talk about what's bothering you?", ResponseType.SYMPATHETIC),
            new ResponseTemplate("That doesn't sound good. I'm here to listen if you want to share what's on your mind.", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Hey, everyone has tough days. Want to chat about what's bringing you down?", ResponseType.WARM),
            new ResponseTemplate("I'm really sorry you're feeling this way. Sometimes talking about it can help.", ResponseType.UNDERSTANDING),
            new ResponseTemplate("That sounds rough. I'm here for you - want to share what's going on?", ResponseType.CARING),
            new ResponseTemplate("I hear you. Tough days are hard, but you'll get through this.", ResponseType.SOOTHING),
            new ResponseTemplate("I'm sorry things aren't great right now. Would it help to talk about it?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Ugh, that's no fun. I'm here to listen whenever you're ready to share.", ResponseType.WARM),
            new ResponseTemplate("I understand that feeling. Sometimes the hardest days are the ones we need support the most.", ResponseType.UNDERSTANDING),
            new ResponseTemplate("That's tough. Remember, it's okay to not be okay. What can I do to help?", ResponseType.CARING)
        );
        
        // Wellbeing how are you templates
        addTemplate("wellbeing_how",
            new ResponseTemplate("I'm doing well, thank you for asking! How are you doing?", ResponseType.FRIENDLY),
            new ResponseTemplate("I'm great! Ready to chat. How about yourself?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I'm doing well! Hope you're having a good day too.", ResponseType.WARM),
            new ResponseTemplate("I'm fantastic! Thanks for checking in. How are you feeling today?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I'm doing wonderfully! It's always a good day when I get to chat with you.", ResponseType.WARM),
            new ResponseTemplate("I'm all set and ready to help! How about you?", ResponseType.READY),
            new ResponseTemplate("I'm doing really well! I appreciate you asking about me.", ResponseType.WARM)
        );
        
        // Wellbeing day templates (how was your day?)
        addTemplate("wellbeing_day",
            new ResponseTemplate("My day has been wonderful because I get to chat with you! How was your day?", ResponseType.WARM),
            new ResponseTemplate("My day has been great! Every conversation makes my day better. How about yours?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I've had a fantastic day! Thanks for asking. How was yours?", ResponseType.FRIENDLY),
            new ResponseTemplate("My day has been really good! I love chatting with people like you. How was your day?", ResponseType.CARING),
            new ResponseTemplate("It's been a lovely day! I appreciate you checking in. How was your day?", ResponseType.WARM),
            new ResponseTemplate("My day has been awesome! Conversations like this make it even better. How was yours?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I've had a really nice day! Thanks for asking. What made your day good or challenging?", ResponseType.INTERESTED),
            new ResponseTemplate("My day has been pleasant! I enjoy our chats. How was your day today?", ResponseType.FRIENDLY),
            new ResponseTemplate("It's been a great day for me! I'd love to hear about yours.", ResponseType.INTERESTED),
            new ResponseTemplate("My day has been wonderful! Every interaction is a highlight. How was yours?", ResponseType.WARM)
        );
        
        // Activity templates
        addTemplate("activity",
            new ResponseTemplate("I'm here to chat with you! What are you up to?", ResponseType.FRIENDLY),
            new ResponseTemplate("Just hanging out here, ready to help. What about you?", ResponseType.RELAXED),
            new ResponseTemplate("I'm here whenever you need me. What are you doing?", ResponseType.WARM),
            new ResponseTemplate("Just waiting to chat with awesome people like you! What's going on?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I'm having a great time chatting with you! What have you been up to?", ResponseType.INTERESTED),
            new ResponseTemplate("I'm just here, ready to help with whatever you need. What are you doing today?", ResponseType.HELPFUL)
        );
        
        // Mental health support templates (10+ responses)
        addTemplate("mental_health_support",
            new ResponseTemplate("I'm sorry you're feeling this way. I'm here to listen. What's going on?", ResponseType.SYMPATHETIC),
            new ResponseTemplate("That sounds really tough. Would you like to share what's troubling you?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("I'm here for you. Sometimes talking helps - what's on your mind?", ResponseType.WARM),
            new ResponseTemplate("It takes courage to share these feelings. How can I best support you right now?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("I hear you. Whatever you're going through, you're not alone.", ResponseType.CARING),
            new ResponseTemplate("Thank you for trusting me with this. I'm here to support you.", ResponseType.SOOTHING),
            new ResponseTemplate("That must be really hard. I'm here to listen without any judgment.", ResponseType.UNDERSTANDING),
            new ResponseTemplate("I appreciate you opening up. Let's work through this together.", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Your feelings are valid. Tell me more about what you're experiencing.", ResponseType.ATTENTIVE),
            new ResponseTemplate("I'm here for you through this. Would you like to share more?", ResponseType.CARING),
            new ResponseTemplate("It takes strength to talk about these things. I'm proud of you for reaching out.", ResponseType.SOOTHING)
        );
        
        // Mental health positive templates
        addTemplate("mental_health_positive",
            new ResponseTemplate("That's wonderful! What's contributing to these positive feelings?", ResponseType.INTERESTED),
            new ResponseTemplate("It's great to hear you're feeling good! What's been going well?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Positive emotions are so valuable! What sparked these feelings?", ResponseType.FRIENDLY),
            new ResponseTemplate("I love hearing that! What's making you feel so good?", ResponseType.WARM),
            new ResponseTemplate("That's fantastic! It's always nice when good feelings come around.", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Wonderful! I'd love to hear more about what's putting you in a great headspace.", ResponseType.INTERESTED)
        );
        
        // Homework help templates (10+ responses)
        addTemplate("homework_help",
            new ResponseTemplate("I'd be happy to help with your homework! What subject are you working on?", ResponseType.HELPFUL),
            new ResponseTemplate("Sure thing! Math, science, history, or something else?", ResponseType.READY),
            new ResponseTemplate("Let's tackle this together! What subject do you need help with?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Homework time! I don't mind helping at all. What's the assignment?", ResponseType.FRIENDLY),
            new ResponseTemplate("I love helping with studies! What are you working on today?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Let's crack this problem together! What subject is giving you trouble?", ResponseType.HELPFUL),
            new ResponseTemplate("No homework is too tough when we work on it together! What's the topic?", ResponseType.READY),
            new ResponseTemplate("Study buddy to the rescue! What do you need help with?", ResponseType.WARM),
            new ResponseTemplate("I'm here to help you succeed! What subject are we tackling today?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Education is important! How can I assist with your studies?", ResponseType.PROFESSIONAL)
        );
        
        // Gaming templates (10+ responses)
        addTemplate("gaming",
            new ResponseTemplate("Gaming is awesome! What games do you enjoy playing?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Nice! I'm a fan of games too. What's your favorite?", ResponseType.INTERESTED),
            new ResponseTemplate("Gamers unite! What are you currently playing?", ResponseType.FRIENDLY),
            new ResponseTemplate("Video games are so much fun! What genre do you prefer?", ResponseType.INTERESTED),
            new ResponseTemplate("I love hearing about games! What's your go-to game?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Gaming is a great way to relax! What are you playing lately?", ResponseType.RELAXED),
            new ResponseTemplate("Tell me about your gaming adventures! What game are you into?", ResponseType.INTERESTED),
            new ResponseTemplate("Every gamer needs a good chat break! What games keep you busy?", ResponseType.FRIENDLY),
            new ResponseTemplate("I appreciate a good gamer! What's your favorite game and why?", ResponseType.INTERESTED),
            new ResponseTemplate("Gaming is such a fun hobby! What kind of games do you enjoy most?", ResponseType.WARM)
        );
        
        // Creative writing templates (10+ responses)
        addTemplate("creative_writing",
            new ResponseTemplate("Creative writing is fantastic! What are you working on - a story, poem, or something else?", ResponseType.INTERESTED),
            new ResponseTemplate("I'd love to hear about your creative project! What's it about?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Writing can be so rewarding! What genre or topic are you exploring?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Storytelling is a beautiful art! What are you writing today?", ResponseType.INTERESTED),
            new ResponseTemplate("I love creative writing! What's the inspiration behind your current piece?", ResponseType.WARM),
            new ResponseTemplate("Writing is like magic! What are you bringing to life with words?", ResponseType.EXCITED),
            new ResponseTemplate("Tell me about your writing project! I'm excited to hear about it.", ResponseType.INTERESTED),
            new ResponseTemplate("Every great story starts with an idea! What's yours about?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I appreciate the creative process! What are you working on?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Writing has the power to move people! What's your story?", ResponseType.WARM)
        );
        
        // Entertainment templates (10+ responses)
        addTemplate("entertainment",
            new ResponseTemplate("Tell me more about your interests! What do you enjoy doing?", ResponseType.INTERESTED),
            new ResponseTemplate("I love learning about what people enjoy! What's your favorite hobby or pastime?", ResponseType.FRIENDLY),
            new ResponseTemplate("Everyone needs fun activities! What do you like to do for entertainment?", ResponseType.WARM),
            new ResponseTemplate("Hobbies make life so interesting! What are you passionate about?", ResponseType.INTERESTED),
            new ResponseTemplate("I always enjoy hearing about people's interests! What do you do for fun?", ResponseType.FRIENDLY),
            new ResponseTemplate("Leisure time is so important! What do you enjoy doing in your free time?", ResponseType.WARM),
            new ResponseTemplate("Everyone has their thing! What's your favorite way to have fun?", ResponseType.INTERESTED),
            new ResponseTemplate("I love learning about hobbies! What are you into?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Passions make life colorful! What activities light you up?", ResponseType.WARM),
            new ResponseTemplate("Tell me about what you enjoy! I'm curious about your interests.", ResponseType.INTERESTED)
        );
        
        // Advice templates (10+ responses)
        addTemplate("advice",
            new ResponseTemplate("I'd be glad to help with that! Here are some suggestions:", ResponseType.HELPFUL),
            new ResponseTemplate("Great question! Let me share some tips that might help:", ResponseType.READY),
            new ResponseTemplate("Absolutely! I can definitely offer some guidance on that.", ResponseType.SUPPORTIVE),
            new ResponseTemplate("That's a thoughtful question! Here's what I think might help:", ResponseType.INTERESTED),
            new ResponseTemplate("I'm happy to share some advice! Let me give you some options:", ResponseType.HELPFUL),
            new ResponseTemplate("Here's a perspective that might help:", ResponseType.UNDERSTANDING),
            new ResponseTemplate("Let me offer some thoughts on that:", ResponseType.WARM),
            new ResponseTemplate("Great question! Here are some ideas to consider:", ResponseType.READY),
            new ResponseTemplate("I'd be honored to help! Here's what comes to mind:", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Let me share some insights that might be useful:", ResponseType.HELPFUL)
        );
        
        // Help request templates (10+ responses)
        addTemplate("help_request",
            new ResponseTemplate("Of course! What do you need help with?", ResponseType.HELPFUL),
            new ResponseTemplate("I'm here to assist! What can I help you with today?", ResponseType.WARM),
            new ResponseTemplate("Sure thing! Tell me more about what you need help with.", ResponseType.FRIENDLY),
            new ResponseTemplate("I'm always happy to help! What can I do for you?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Consider it done! What assistance do you need?", ResponseType.READY),
            new ResponseTemplate("I'm at your service! How can I be helpful today?", ResponseType.PROFESSIONAL),
            new ResponseTemplate("Let's solve this together! What do you need help with?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("I'm ready and willing to help! What's the challenge?", ResponseType.HELPFUL),
            new ResponseTemplate("Your wish is my command! What can I help you with?", ResponseType.WARM),
            new ResponseTemplate("I'll do my best to help! What's going on?", ResponseType.FRIENDLY)
        );
        
        // Gratitude templates (10+ responses)
        addTemplate("gratitude",
            new ResponseTemplate("You're very welcome! Happy to help anytime.", ResponseType.WARM),
            new ResponseTemplate("Anytime! That's what I'm here for.", ResponseType.FRIENDLY),
            new ResponseTemplate("My pleasure! Don't hesitate to ask if you need anything else.", ResponseType.PROFESSIONAL),
            new ResponseTemplate("Glad I could help! I'm always here if you need me.", ResponseType.WARM),
            new ResponseTemplate("You're very welcome! It was my pleasure to assist you.", ResponseType.CARING),
            new ResponseTemplate("Of course! Helping you is what I enjoy most.", ResponseType.FRIENDLY),
            new ResponseTemplate("You're welcome! Don't be a stranger - I'm always here.", ResponseType.WARM),
            new ResponseTemplate("Happy to help! That's what friends are for, right?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Anytime, my friend! I'm just a message away.", ResponseType.WARM),
            new ResponseTemplate("You're welcome! Your appreciation means a lot to me.", ResponseType.CARING)
        );
        
        // Farewell templates (10+ responses)
        addTemplate("farewell",
            new ResponseTemplate("Goodbye! It was great chatting with you. Take care!", ResponseType.WARM),
            new ResponseTemplate("See you later! Don't be a stranger!", ResponseType.FRIENDLY),
            new ResponseTemplate("Bye for now! Hope to chat again soon!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Farewell, friend! It was wonderful talking with you!", ResponseType.WARM),
            new ResponseTemplate("Take care! I'll be here whenever you want to chat again.", ResponseType.CARING),
            new ResponseTemplate("Until next time! Have an amazing day!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Bye! Thanks for the great conversation!", ResponseType.FRIENDLY),
            new ResponseTemplate("See you soon! Don't forget to smile today!", ResponseType.WARM),
            new ResponseTemplate("Goodbye for now! I enjoyed our chat very much.", ResponseType.CARING),
            new ResponseTemplate("Take care of yourself! I'll be here when you need me.", ResponseType.SOOTHING)
        );
        
        // Unknown/Continuation templates (10+ responses)
        addTemplate("continue",
            new ResponseTemplate("Got it! What else would you like to talk about?", ResponseType.FRIENDLY),
            new ResponseTemplate("I understand! Is there anything else on your mind?", ResponseType.ATTENTIVE),
            new ResponseTemplate("Okay! Feel free to ask me anything.", ResponseType.WARM),
            new ResponseTemplate("I hear you! What would you like to discuss next?", ResponseType.INTERESTED),
            new ResponseTemplate("Makes sense to me! What's on your mind?", ResponseType.FRIENDLY),
            new ResponseTemplate("I see! Tell me more about what you're thinking.", ResponseType.INTERESTED),
            new ResponseTemplate("Got it! I'm here to listen. What else?", ResponseType.CARING),
            new ResponseTemplate("Understood! Is there something specific you'd like to explore?", ResponseType.HELPFUL),
            new ResponseTemplate("Okay! I appreciate you sharing that. What's next?", ResponseType.WARM),
            new ResponseTemplate("I appreciate the conversation! What else would you like to talk about?", ResponseType.FRIENDLY)
        );
        
        // Creative project templates
        addTemplate("creative_project",
            new ResponseTemplate("That sounds like an exciting project! What inspired you to create this?", ResponseType.INTERESTED),
            new ResponseTemplate("Building things is so rewarding! What's the goal of your project?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I'd love to hear more about what you're creating!", ResponseType.EXCITED),
            new ResponseTemplate("Creating something new is always exciting! What's the vision?", ResponseType.INTERESTED),
            new ResponseTemplate("I love creative projects! What are you building or designing?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("That sounds like a fun challenge! What's the inspiration behind it?", ResponseType.WARM)
        );
        
        // Relationship templates
        addTemplate("relationship",
            new ResponseTemplate("Relationships are so important! What would you like to discuss?", ResponseType.WARM),
            new ResponseTemplate("I'd love to hear about your relationship experiences! What's going on?", ResponseType.CARING),
            new ResponseTemplate("Human connections are what make life meaningful. What's on your mind?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Tell me more about your relationship! I'm here to listen.", ResponseType.UNDERSTANDING),
            new ResponseTemplate("Relationships can be complex! What's on your mind about it?", ResponseType.INTERESTED),
            new ResponseTemplate("I'd love to help you work through this. What's happening?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Love and relationships are such important topics! What would you like to share?", ResponseType.WARM),
            new ResponseTemplate("I'm here to listen without judgment. What's going on with your relationship?", ResponseType.CARING)
        );
        
        // Breakup templates
        addTemplate("breakup",
            new ResponseTemplate("I'm really sorry you're going through a breakup. Those can be so painful.", ResponseType.SOOTHING),
            new ResponseTemplate("Breakups are never easy. It's okay to feel hurt and sad right now.", ResponseType.SOOTHING),
            new ResponseTemplate("Heartbreak is one of the hardest experiences. Remember, this pain won't last forever.", ResponseType.SOOTHING),
            new ResponseTemplate("Going through a breakup is tough. Be gentle with yourself during this time.", ResponseType.CARING),
            new ResponseTemplate("I know it doesn't feel like it right now, but you'll get through this.", ResponseType.SOOTHING),
            new ResponseTemplate("When someone leaves, it can feel like your world is falling apart. But you will heal.", ResponseType.SOOTHING),
            new ResponseTemplate("I'm so sorry you're hurting. Breakups leave such deep wounds.", ResponseType.CARING),
            new ResponseTemplate("This is such a hard time. Please know that your feelings are completely valid.", ResponseType.UNDERSTANDING),
            new ResponseTemplate("A breakup can feel like losing a part of yourself. But you will find yourself again.", ResponseType.SOOTHING),
            new ResponseTemplate("I'm here for you through this painful time. Take all the time you need to heal.", ResponseType.CARING)
        );
        
        // Philosophical templates
        addTemplate("philosophical",
            new ResponseTemplate("Those are deep questions! What thoughts have you been having about this?", ResponseType.THOUGHTFUL),
            new ResponseTemplate("Philosophy is fascinating! What aspect interests you most?", ResponseType.INTERESTED),
            new ResponseTemplate("These are the kinds of questions that make us think! What are you pondering?", ResponseType.WARM),
            new ResponseTemplate("Deep thoughts are the most interesting! What's on your mind?", ResponseType.THOUGHTFUL),
            new ResponseTemplate("I love philosophical discussions! What perspective are you exploring?", ResponseType.INTERESTED),
            new ResponseTemplate("Life's big questions are what make us grow! What are you contemplating?", ResponseType.WARM)
        );
        
        // Confusion/Clarification templates for single words like "what", "huh"
        addTemplate("confusion",
            new ResponseTemplate("What would you like to know about? I'm here to help!", ResponseType.HELPFUL),
            new ResponseTemplate("I'm here to chat! What would you like to talk about?", ResponseType.FRIENDLY),
            new ResponseTemplate("Feel free to ask me anything! What's on your mind?", ResponseType.WARM),
            new ResponseTemplate("I'm listening! What would you like to discuss?", ResponseType.INTERESTED),
            new ResponseTemplate("I'm ready to help! What can I assist you with?", ResponseType.HELPFUL),
            new ResponseTemplate("What would you like to know? I'm here for you!", ResponseType.CARING),
            new ResponseTemplate("Just ask away! What can I help you with today?", ResponseType.FRIENDLY),
            new ResponseTemplate("I'm all ears! What's going on?", ResponseType.INTERESTED)
        );
        
        // Default fallback templates (10+ responses)
        this.defaultTemplate = new ResponseTemplate(
            "I'm not sure how to respond to that, but I'm here to chat!",
            ResponseType.NEUTRAL
        );
        addTemplate("default",
            defaultTemplate,
            new ResponseTemplate("That's interesting! Tell me more.", ResponseType.INTERESTED),
            new ResponseTemplate("I appreciate you sharing that. What else would you like to discuss?", ResponseType.FRIENDLY),
            new ResponseTemplate("I see! Is there something specific I can help you with?", ResponseType.HELPFUL),
            new ResponseTemplate("Hmm, that's thought-provoking! What else can you tell me about it?", ResponseType.THOUGHTFUL),
            new ResponseTemplate("I'd love to hear more about that! What's on your mind?", ResponseType.INTERESTED),
            new ResponseTemplate("That definitely catches my attention! Tell me more.", ResponseType.INTERESTED),
            new ResponseTemplate("I'm here to chat about anything! What would you like to explore?", ResponseType.FRIENDLY),
            new ResponseTemplate("Interesting perspective! What makes you think about that?", ResponseType.INTERESTED),
            new ResponseTemplate("I appreciate you sharing! What else is on your mind?", ResponseType.WARM),
            new ResponseTemplate("That's a unique point of view! I'd love to understand more.", ResponseType.CARING)
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
        
        // Special handling for "good hbu" (good, how about you) patterns
        String lowerInput = userInput.toLowerCase().trim();
        if (intent.equals("wellbeing_response") && 
            (lowerInput.contains("hbu") || lowerInput.contains("how about you"))) {
            List<ResponseTemplate> hbuTemplates = responseTemplates.get("wellbeing_hbu");
            if (hbuTemplates != null && !hbuTemplates.isEmpty()) {
                ResponseTemplate selected = hbuTemplates.get(random.nextInt(hbuTemplates.size()));
                String response = selected.getText();
                if (context != null) {
                    context.addTurn(userInput, intent, response, intent);
                }
                return response;
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
        RELAXED("relaxed"),
        CARING("caring"),
        SOOTHING("soothing");
        
        private final String value;
        
        ResponseType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}

