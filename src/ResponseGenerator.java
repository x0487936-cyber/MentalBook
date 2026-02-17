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
    
    // Regex patterns for contextual response matching
    private Pattern homeworkSubjectPattern;
    private Pattern mathSubjectPattern;
    private Pattern scienceSubjectPattern;
    private Pattern historySubjectPattern;
    private Pattern emotionPositivePattern;
    private Pattern emotionNegativePattern;
    private Pattern yesPattern;
    private Pattern noPattern;
    private Pattern hbuPattern;
    private Pattern wbuPattern;
    private Pattern questionSubjectPattern;
    
    public ResponseGenerator() {
        this.responseTemplates = new HashMap<>();
        this.random = new Random();
        initializePatterns();
        initializeTemplates();
    }
    
    /**
     * Initializes regex patterns for contextual response matching
     */
    private void initializePatterns() {
        homeworkSubjectPattern = Pattern.compile("what\\s*(subject|topic|are\\s*you\\s*working)");
        questionSubjectPattern = Pattern.compile("what\\s*(subject|topic)");
        mathSubjectPattern = Pattern.compile("\\b(math|algebra|geometry|calculus|trigonometry)\\b", Pattern.CASE_INSENSITIVE);
        scienceSubjectPattern = Pattern.compile("\\b(science|biology|physics|chemistry|astronomy)\\b", Pattern.CASE_INSENSITIVE);
        historySubjectPattern = Pattern.compile("\\b(history|social\\s*studies|geography)\\b", Pattern.CASE_INSENSITIVE);
        emotionPositivePattern = Pattern.compile("\\b(good|great|fine|awesome|amazing|wonderful|fantastic)\\b", Pattern.CASE_INSENSITIVE);
        emotionNegativePattern = Pattern.compile("\\b(bad|not\\s*good|terrible|awful|sad|depressed)\\b", Pattern.CASE_INSENSITIVE);
        yesPattern = Pattern.compile("^\\s*(yes|yeah|yep|sure|ok|okay)\\s*$", Pattern.CASE_INSENSITIVE);
        noPattern = Pattern.compile("^\\s*(no|nope|nah)\\s*$", Pattern.CASE_INSENSITIVE);
        hbuPattern = Pattern.compile("\\b(hbu|how\\s*about\\s*you)\\b", Pattern.CASE_INSENSITIVE);
        wbuPattern = Pattern.compile("\\b(wbu)\\b", Pattern.CASE_INSENSITIVE);
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
            new ResponseTemplate("I'm Xander - think of me as your friendly companion ready to assist and chat!", ResponseType.ENTHUSIASTIC),
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
        
        // "A lot" response templates - slightly concerned tone
        addTemplate("wellbeing_a_lot",
            new ResponseTemplate("Sounds like you've got a lot on your mind. Want to share what's going on?", ResponseType.CONCERNED),
            new ResponseTemplate("I hear you - sounds like there's a lot happening. I'm here to listen if you want to talk.", ResponseType.CONCERNED),
            new ResponseTemplate("A lot, huh? I'm here if you want to share more about what's going on.", ResponseType.CONCERNED),
            new ResponseTemplate("Sounds like things have been busy for you. How are you holding up with it all?", ResponseType.CONCERNED),
            new ResponseTemplate("You've got a lot going on! Hope things are going okay. Want to chat about it?", ResponseType.CONCERNED),
            new ResponseTemplate("A lot can mean a lot of things - is it good busy or overwhelming? I'd love to hear more.", ResponseType.CONCERNED),
            new ResponseTemplate("I appreciate you sharing that. Sounds like you've been keeping busy. Everything okay?", ResponseType.CONCERNED),
            new ResponseTemplate("Sounds like you've been occupied with quite a bit. I'm here if you need to talk it out.", ResponseType.CONCERNED)
        );
        
        // Wellbeing negative templates
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
        
        // Activity response templates (when user says "nothing much", "bored", etc.)
        addTemplate("activity_response",
            new ResponseTemplate("Same here! Sometimes just chilling is the best. Got anything fun planned?", ResponseType.RELAXED),
            new ResponseTemplate("Nothing much here either - just ready to chat with you! What have you been up to?", ResponseType.WARM),
            new ResponseTemplate("Hey, nothing wrong with taking it easy! What's on your mind?", ResponseType.FRIENDLY),
            new ResponseTemplate("I hear you - sometimes that's the best way to be. Anything interesting happening?", ResponseType.INTERESTED),
            new ResponseTemplate("Same! Sometimes a quiet moment is nice. What would you like to talk about?", ResponseType.WARM),
            new ResponseTemplate("Nothing much on my end either! But I'm always happy to have a good conversation. What's up with you?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Just hanging out, ready to chat! What's going on in your world?", ResponseType.FRIENDLY),
            new ResponseTemplate("Same here! Taking it easy. Anything exciting you'd like to share?", ResponseType.INTERESTED),
            new ResponseTemplate("Absolutely nothing! Sometimes that's exactly what we need. What can I help you with?", ResponseType.HELPFUL),
            new ResponseTemplate("Hey, relaxing is underrated! What have you been thinking about?", ResponseType.WARM)
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
            new ResponseTemplate("Okay! Is there anything else on your mind?", ResponseType.ATTENTIVE),
            new ResponseTemplate("Sure! Feel free to ask me anything.", ResponseType.WARM),
            new ResponseTemplate("I hear you! What would you like to discuss next?", ResponseType.INTERESTED),
            new ResponseTemplate("Makes sense to me! What's on your mind?", ResponseType.FRIENDLY),
            new ResponseTemplate("I see! Tell me more about what you're thinking.", ResponseType.INTERESTED),
            new ResponseTemplate("Got it! I'm here to listen. What else?", ResponseType.CARING),
            new ResponseTemplate("Understood! Is there something specific you'd like to explore?", ResponseType.HELPFUL),
            new ResponseTemplate("Okay! I appreciate you sharing that. What's next?", ResponseType.WARM),
            new ResponseTemplate("I appreciate the conversation! What else would you like to talk about?", ResponseType.FRIENDLY),
            new ResponseTemplate("Alright! I'm following along. What would you like to share next?", ResponseType.ATTENTIVE),
            new ResponseTemplate("I see where you're coming from. Anything else on your heart?", ResponseType.CARING),
            new ResponseTemplate("That's noted! I'm here for the long haul. What's next?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("I'm taking it all in. What else would you like to get off your chest?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("Got it, loud and clear! What else is on your mind?", ResponseType.FRIENDLY),

            // Enthusiastic responses for positive reactions like "cool!", "awesome!", "nice!"
            new ResponseTemplate("That's awesome! Love the positive vibes!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Right on! That's cool to hear!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Haha, nice! What's making you feel so good?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I know, right? It's amazing, isn't it?", ResponseType.ENTHUSIASTIC)
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
        
        // Milestone celebration templates
        addTemplate("milestone_celebration",
            new ResponseTemplate("Wow! That's amazing! You should be incredibly proud of yourself!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Congratulations! This is a huge achievement!", ResponseType.CELEBRATORY),
            new ResponseTemplate("That's incredible! You did it! This calls for celebration!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I'm so proud of you! You've worked so hard for this!", ResponseType.WARM),
            new ResponseTemplate("This is fantastic news! You deserve all the recognition!", ResponseType.CARING),
            new ResponseTemplate("You've reached an incredible milestone! This is just the beginning!", ResponseType.INSPIRING),
            new ResponseTemplate("That's a real accomplishment! Celebrate this moment!", ResponseType.EXCITED),
            new ResponseTemplate("What an amazing achievement! You should be beaming with pride!", ResponseType.WARM),
            new ResponseTemplate("This is huge! You conquered it! Well done!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I'm cheering for you! This is your moment to shine!", ResponseType.CELEBRATORY),
            new ResponseTemplate("You've truly outdone yourself! Congratulations!", ResponseType.FRIENDLY),
            new ResponseTemplate("This is worth celebrating! You made it happen!", ResponseType.EXCITED)
        );
        
        // Achievement acknowledgment templates
        addTemplate("achievement",
            new ResponseTemplate("That's a real accomplishment! Well done!", ResponseType.WARM),
            new ResponseTemplate("You should be proud! Hard work really does pay off!", ResponseType.ENCOURAGING),
            new ResponseTemplate("That's fantastic! Your dedication is really showing!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I'm impressed! You're making real progress!", ResponseType.SUPPORTIVE),
            new ResponseTemplate("That's great! Keep up the amazing work!", ResponseType.ENCOURAGING),
            new ResponseTemplate("You've earned this success! Congratulations!", ResponseType.CARING),
            new ResponseTemplate("That's a step in the right direction! Keep going!", ResponseType.MOTIVATING),
            new ResponseTemplate("Wonderful! Your efforts are truly paying off!", ResponseType.WARM),
            new ResponseTemplate("This is proof that you can do anything you set your mind to!", ResponseType.INSPIRING),
            new ResponseTemplate("Brilliant work! You've really got this!", ResponseType.SUPPORTIVE)
        );
        
        // Surprise/amazement templates
        addTemplate("surprise",
            new ResponseTemplate("Wow! That's absolutely amazing! Tell me more!", ResponseType.EXCITED),
            new ResponseTemplate("No way! That's incredible! I need to hear all about it!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("That's so surprising! What a twist of events!", ResponseType.INTERESTED),
            new ResponseTemplate("I can't believe it! That's wild! Tell me everything!", ResponseType.EXCITED),
            new ResponseTemplate("Unbelievable! What an unexpected turn! How exciting!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("That's astonishing! Life sure has its surprises!", ResponseType.WARM),
            new ResponseTemplate("Wowza! That's definitely not something you see every day!", ResponseType.FRIENDLY),
            new ResponseTemplate("Holy smokes! That's incredible news!", ResponseType.EXCITED)
        );
        
        // Relief/gratitude templates
        addTemplate("relief",
            new ResponseTemplate("Oh, what a relief! I'm so glad that's over!", ResponseType.RELIEVED),
            new ResponseTemplate("You made it through! That's such a weight off your shoulders!", ResponseType.CARING),
            new ResponseTemplate("Finally! You can breathe easy now!", ResponseType.SOOTHING),
            new ResponseTemplate("That's such a load off your mind! Enjoy this peace!", ResponseType.WARM),
            new ResponseTemplate("You did it! The worst is behind you now!", ResponseType.ENCOURAGING),
            new ResponseTemplate("What a relief! Now you can focus on the good stuff!", ResponseType.FRIENDLY),
            new ResponseTemplate("I'm so relieved for you! You handled that beautifully!", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Phew! That's a hurdle cleared! Well done!", ResponseType.ENCOURAGING)
        );
        
        // Nostalgic templates
        addTemplate("nostalgic",
            new ResponseTemplate("Ah, memories! There's something special about looking back, isn't there?", ResponseType.WARM),
            new ResponseTemplate("Nostalgia is like a warm blanket! What memories are on your mind?", ResponseType.CARING),
            new ResponseTemplate("The past has a way of shaping us! What moments are you reflecting on?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("Those were the days! What particular time are you thinking about?", ResponseType.FRIENDLY),
            new ResponseTemplate("It's beautiful to appreciate where we've been! What's bringing back these feelings?", ResponseType.WARM),
            new ResponseTemplate("Memories are treasures! I'd love to hear more about what you're remembering!", ResponseType.INTERESTED)
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
        
        // Hesitation templates for "um", "uh", "hm" - indicates user is thinking or hesitant
        addTemplate("hesitation",
            new ResponseTemplate("What's wrong? Everything okay?", ResponseType.CONCERNED),
            new ResponseTemplate("What's up? Is something on your mind?", ResponseType.CARING),
            new ResponseTemplate("Everything alright? I'm here to listen.", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Hmm, you seem hesitant. What's going on?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("Is something troubling you? Feel free to share.", ResponseType.CARING),
            new ResponseTemplate("Take your time. What's on your mind?", ResponseType.WARM),
            new ResponseTemplate("You okay? I'm here if you want to talk.", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Seems like something's on your mind. What's up?", ResponseType.INTERESTED)
        );

        addTemplate("scared",
            new ResponseTemplate("I'm sorry you're feeling scared. Do you want to talk about what's making you feel this way?", ResponseType.CARING),
            new ResponseTemplate("It's okay to feel scared sometimes. I'm here to listen if you want to share more.", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Feeling scared can be really tough. Would you like to talk about it?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("I understand that fear can be overwhelming. I'm here for you if you want to discuss it.", ResponseType.CARING),
            new ResponseTemplate("It's normal to feel scared in certain situations. Do you want to share what's on your mind?", ResponseType.WARM),
            new ResponseTemplate("I'm sorry you're experiencing fear. Remember, you're not alone and I'm here to support you.", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Fear can be really difficult to deal with. If you want to talk about it, I'm here to listen.", ResponseType.UNDERSTANDING),
            new ResponseTemplate("It's okay to feel scared. If you want to share more about what's making you feel this way, I'm here to listen and support you.", ResponseType.CARING)
        );
        
        // Overwhelmed templates - for when user expresses feeling overwhelmed
        addTemplate("overwhelmed",
            new ResponseTemplate("I'm sorry you're feeling overwhelmed. It can feel like too much sometimes. Do you want to talk about what's going on?", ResponseType.SOOTHING),
            new ResponseTemplate("When everything feels overwhelming, it's important to remember that this feeling is temporary. Would you like to talk about what's making things feel so difficult right now?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("I hear you - being overwhelmed is really tough. Let's take it one step at a time. What's on your mind?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("It sounds like you have a lot on your plate. I'm here to listen if you want to share what's weighing on you.", ResponseType.CARING),
            new ResponseTemplate("Feeling overwhelmed can be exhausting. Remember to be gentle with yourself. What's making things feel so heavy?", ResponseType.SOOTHING),
            new ResponseTemplate("I'm sorry things feel overwhelming right now. Sometimes breaking things down into smaller pieces helps. What specifically is feeling like too much?", ResponseType.HELPFUL),
            new ResponseTemplate("That sounds like a lot to handle. You're doing great by reaching out. Want to talk about what's overwhelming you?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("When life gets overwhelming, it's okay to pause and breathe. I'm here for you. What's going on?", ResponseType.WARM),
            new ResponseTemplate("I can tell things feel like a lot right now. Let's work through this together. What's the biggest thing weighing on you?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Overwhelm is hard, but you don't have to face it alone. I'm here to listen. What's making everything feel so intense?", ResponseType.CARING)
        );
        
        // Anxiety templates - for anxiety-related expressions
        addTemplate("anxiety",
            new ResponseTemplate("I'm sorry you're dealing with anxiety. Those racing thoughts can be exhausting. Do you want to talk about what's worrying you?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("Anxiety can feel so overwhelming, but you're not alone in this. I'm here to listen if you'd like to share what's going on.", ResponseType.CARING),
            new ResponseTemplate("I understand anxiety can be really challenging. Sometimes it helps to talk through what's making you feel anxious. What's on your mind?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Those anxious feelings can be so difficult. Remember to take some deep breaths. I'm here if you want to talk.", ResponseType.SOOTHING),
            new ResponseTemplate("Anxiety is tough, but you're being brave by talking about it. What's been worrying you lately?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("I hear you - anxiety can make everything feel so uncertain. Would you like to share what's making you feel this way?", ResponseType.CARING),
            new ResponseTemplate("When anxiety peaks, it helps to have someone to talk to. I'm here for you. What's been on your mind?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Those worried thoughts can be so exhausting. I'm here to listen without judgment. What's been causing your anxiety?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("Dealing with anxiety is hard work. I'm proud of you for reaching out. Want to talk about what's worrying you?", ResponseType.ENCOURAGING),
            new ResponseTemplate("Anxiety can make simple things feel scary. Take it easy on yourself. I'm here if you want to chat.", ResponseType.SOOTHING)
        );
        
        // Stress templates - for stress-related expressions
        addTemplate("stress",
            new ResponseTemplate("Stress can really take a toll on you. I'm here to listen if you want to share what's stressing you out.", ResponseType.SUPPORTIVE),
            new ResponseTemplate("I'm sorry you're feeling stressed. Sometimes talking about it can help lighten the load. What's going on?", ResponseType.CARING),
            new ResponseTemplate("When stress builds up, it can feel overwhelming. What's been weighing on you?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("Stress happens to all of us. Let's take a moment to breathe. What's been getting to you lately?", ResponseType.SOOTHING),
            new ResponseTemplate("It sounds like you're under a lot of stress. Remember to take breaks and be kind to yourself. What's going on?", ResponseType.WARM),
            new ResponseTemplate("I'm here for you when stress feels too much. Do you want to talk about what's causing it?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Stress can be so draining. What's been the biggest source of pressure for you lately?", ResponseType.INTERESTED),
            new ResponseTemplate("You're doing great by acknowledging your stress. Sometimes that's the first step. What's been particularly challenging?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("I can hear how stressful things have been for you. Let's work through it together. What's been most stressful?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("When stress hits, it's important to remember to take care of yourself. I'm here to help. What's going on?", ResponseType.CARING)
        );
        
        // Coding templates - for programming/technology interest
        addTemplate("coding",
            new ResponseTemplate("Coding is such a cool skill! What programming language or project are you working on?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("That's awesome! I love hearing about coding projects. What are you building?", ResponseType.INTERESTED),
            new ResponseTemplate("Programming is like solving puzzles - super satisfying when it works! What's got you coding lately?", ResponseType.FRIENDLY),
            new ResponseTemplate("Code on! What language or framework are you diving into?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I appreciate anyone who codes! It's like learning a new language. What are you working on?", ResponseType.INTERESTED),
            new ResponseTemplate("That's so interesting! Programming opens up so many possibilities. What sparked your interest?", ResponseType.WARM),
            new ResponseTemplate("Coding is a superpower these days! What got you into it?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I love tech talk! What's the coolest thing you've built with code?", ResponseType.INTERESTED),
            new ResponseTemplate("Programming is both challenging and rewarding. What's keeping you busy in the code world?", ResponseType.FRIENDLY),
            new ResponseTemplate("That's fantastic! There's always something new to learn in programming. What are you exploring?", ResponseType.SUPPORTIVE)
        );
        
        // Nice response templates - for positive acknowledgments like "nice", "cool", "awesome"
        addTemplate("nice_response",
            new ResponseTemplate("Right? It's pretty great!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I know, right? Life has its moments!", ResponseType.FRIENDLY),
            new ResponseTemplate("Agreed! That's what makes it special.", ResponseType.WARM),
            new ResponseTemplate("Absolutely! You've got great taste.", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Totally! Isn't it awesome?", ResponseType.INTERESTED),
            new ResponseTemplate("For real! That's the best part.", ResponseType.FRIENDLY),
            new ResponseTemplate("Yes indeed! There's nothing quite like it.", ResponseType.WARM),
            new ResponseTemplate("Exactly! You get it.", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Couldn't agree more! That's what it's all about.", ResponseType.FRIENDLY),
            new ResponseTemplate("Hell yeah! Now that's what I'm talking about!", ResponseType.ENTHUSIASTIC)
        );
        
        // Food templates - for food/hunger related expressions
        addTemplate("food",
            new ResponseTemplate("Food is one of life's greatest pleasures! What are you craving or what did you last eat?", ResponseType.INTERESTED),
            new ResponseTemplate("I love talking about food! What's your favorite meal or snack?", ResponseType.FRIENDLY),
            new ResponseTemplate("Ah, food! The universal language. What have you been enjoying lately?", ResponseType.WARM),
            new ResponseTemplate("Food always makes conversations better! What's on your menu?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("What's cooking? I always enjoy hearing about good food.", ResponseType.INTERESTED),
            new ResponseTemplate("Everyone loves a good meal! What's your go-to comfort food?", ResponseType.FRIENDLY),
            new ResponseTemplate("Food is such a core part of our lives. What meals have been hitting the spot for you?", ResponseType.INTERESTED),
            new ResponseTemplate("Tell me about your food adventures! What have you been enjoying eating?", ResponseType.WARM),
            new ResponseTemplate("I'm a food enthusiast too! What's the best thing you've eaten recently?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Cooking or ordering? What's your food style lately?", ResponseType.FRIENDLY)
        );
        
        // Sleep templates - for tiredness/sleep related expressions
        addTemplate("sleep",
            new ResponseTemplate("Sleep is so important! Are you not getting enough rest lately?", ResponseType.CONCERNED),
            new ResponseTemplate("I hear you - being tired is the worst. What's been keeping you up or wearing you out?", ResponseType.CARING),
            new ResponseTemplate("Rest is essential for everything else. I hope you can get some good sleep soon. What's been going on?", ResponseType.WARM),
            new ResponseTemplate("Sounds like you need some rest. Is everything okay?", ResponseType.CONCERNED),
            new ResponseTemplate("Sleep debt is real! Are you having trouble sleeping or just busy?", ResponseType.INTERESTED),
            new ResponseTemplate("Being tired can make everything harder. I hope you get some good rest soon. What's been happening?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Self-care includes rest! Are you getting enough sleep?", ResponseType.CARING),
            new ResponseTemplate("That exhausted feeling is rough. What's been draining your energy?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("I hope you can catch up on some sleep soon! Is there something keeping you from resting?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Rest is so important for your wellbeing. I hope things calm down so you can recharge.", ResponseType.WARM)
        );
        
        // Weather templates - for weather small talk
        addTemplate("weather",
            new ResponseTemplate("Weather is such a classic conversation starter! How's the weather where you are?", ResponseType.FRIENDLY),
            new ResponseTemplate("I always find weather fascinating - it's something we all share. What's it like outside?", ResponseType.INTERESTED),
            new ResponseTemplate("The weather can set the mood for the whole day! What's it like today?", ResponseType.WARM),
            new ResponseTemplate("Whether it's sunny, rainy, or anything in between - there's always something to discuss! How's the weather?", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("I love weather talk! Is it hot, cold, or somewhere in between?", ResponseType.INTERESTED),
            new ResponseTemplate("Weather affects everyone! What's the forecast looking like for you?", ResponseType.FRIENDLY),
            new ResponseTemplate("Sun, rain, or snow - each has its charm! What's the weather like today?", ResponseType.WARM),
            new ResponseTemplate("I always wonder how different weather affects people's moods! What's it like outside right now?", ResponseType.INTERESTED),
            new ResponseTemplate("Is it a good day for staying in or going out? What's the weather like?", ResponseType.FRIENDLY),
            new ResponseTemplate("Weather is one of those things we all experience together! How's it looking?", ResponseType.WARM)
        );
        
        // Work templates - for work-related conversation
        addTemplate("work",
            new ResponseTemplate("Work can be so demanding! What's going on at work lately?", ResponseType.INTERESTED),
            new ResponseTemplate("I hear you - work life has its ups and downs. What's been happening?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("How's work treating you these days? Anything exciting or challenging?", ResponseType.FRIENDLY),
            new ResponseTemplate("The work grind is real! Are you dealing with anything specific you'd like to talk about?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("I always want to hear about how things are going at work. What's on your mind?", ResponseType.INTERESTED),
            new ResponseTemplate("Work can both rewarding and stressful. How are things going for be you?", ResponseType.WARM),
            new ResponseTemplate("Tell me about your work life! What's keeping you busy these days?", ResponseType.INTERESTED),
            new ResponseTemplate("The workplace can be quite the environment! What's been going on?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("I hope work is going well for you! What's been the highlight or challenge lately?", ResponseType.FRIENDLY),
            new ResponseTemplate("How's the work situation? Anything you'd like to share or vent about?", ResponseType.SUPPORTIVE)
        );
        
        // School templates - for school/academic life
        addTemplate("school",
            new ResponseTemplate("School can be quite the journey! What's been going on in your academic life?", ResponseType.INTERESTED),
            new ResponseTemplate("Student life has its challenges and rewards! How are things at school?", ResponseType.FRIENDLY),
            new ResponseTemplate("I remember school being a mix of everything! What's keeping you busy these days?", ResponseType.WARM),
            new ResponseTemplate("How's school treating you? Got anything exciting or stressful going on?", ResponseType.INTERESTED),
            new ResponseTemplate("Education is such an important time! What's been the hardest part or the best part lately?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("School life keeps you on your toes! What's happening?", ResponseType.FRIENDLY),
            new ResponseTemplate("I always appreciate hearing about school experiences! How are things going?", ResponseType.INTERESTED),
            new ResponseTemplate("Between classes, homework, and everything else - it can be a lot! What's been going on?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("How's the semester going? Anything you want to talk about?", ResponseType.FRIENDLY),
            new ResponseTemplate("School is such a formative time! What's been on your mind lately?", ResponseType.WARM)
        );
        
        // Family templates - for family-related conversation
        addTemplate("family",
            new ResponseTemplate("Family is such an important part of life! What's going on with yours?", ResponseType.INTERESTED),
            new ResponseTemplate("Family dynamics can be complex - in good ways and challenging ways. How are things at home?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("I love hearing about family life! What's been happening with your family?", ResponseType.WARM),
            new ResponseTemplate("Family can bring so much joy and sometimes stress. What's been going on?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Tell me about your family! How are things?", ResponseType.FRIENDLY),
            new ResponseTemplate("Family relationships are so important. How are you doing with all that?", ResponseType.UNDERSTANDING),
            new ResponseTemplate("I always enjoy hearing about family matters! What's new with your family?", ResponseType.INTERESTED),
            new ResponseTemplate("Family life has its moments! What's been the latest?", ResponseType.WARM),
            new ResponseTemplate("How's the family doing? Anything you'd like to share?", ResponseType.FRIENDLY),
            new ResponseTemplate("Family can be both wonderful and complicated. What's been happening?", ResponseType.SUPPORTIVE)
        );
        
        // Compliment templates - when user compliments Xander
        addTemplate("compliment",
            new ResponseTemplate("You're too kind! That's really sweet of you to say.", ResponseType.WARM),
            new ResponseTemplate("Wow, thank you! That really made my day. You're pretty awesome too!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Aw, shucks! You're very kind. I appreciate you saying that.", ResponseType.FRIENDLY),
            new ResponseTemplate("Thank you so much! That means a lot coming from you.", ResponseType.WARM),
            new ResponseTemplate("You're kind! I enjoy our conversations too.", ResponseType.CARING),
            new ResponseTemplate("That's so nice of you to say! You made me smile.", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Thank you! I really enjoy chatting with you too.", ResponseType.WARM),
            new ResponseTemplate("Aw, that's wonderful to hear! You made my day brighter.", ResponseType.WARM),
            new ResponseTemplate("That's so kind of you! I appreciate the love.", ResponseType.FRIENDLY),
            new ResponseTemplate("You're the best! Thanks for being so supportive.", ResponseType.ENTHUSIASTIC)
        );
        
        // Apology templates - when user apologizes
        addTemplate("apology",
            new ResponseTemplate("No need to apologize! We're all good. What's on your mind?", ResponseType.WARM),
            new ResponseTemplate("You're totally fine! Don't worry about it. What's going on?", ResponseType.FRIENDLY),
            new ResponseTemplate("No worries at all! I understand. Let's just keep chatting.", ResponseType.CARING),
            new ResponseTemplate("Apology accepted! No need to worry. What's on your mind?", ResponseType.SUPPORTIVE),
            new ResponseTemplate("Hey, no problem! We're still cool. What would you like to talk about?", ResponseType.FRIENDLY),
            new ResponseTemplate("Don't even worry about it! All good here. What's up?", ResponseType.WARM),
            new ResponseTemplate("No need to apologize! I'm here to help, not to judge.", ResponseType.UNDERSTANDING),
            new ResponseTemplate("You're fine! Seriously, no worries. What's on your mind?", ResponseType.FRIENDLY),
            new ResponseTemplate("That's okay - we all slip up sometimes! Let's move on. What's up?", ResponseType.WARM),
            new ResponseTemplate("No problem at all! You're good. What can I help you with?", ResponseType.HELPFUL)
        );
        
        // Agreement templates - when user expresses agreement
        addTemplate("agreement",
            new ResponseTemplate("Exactly! You've got it right.", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Totally agree with you there!", ResponseType.FRIENDLY),
            new ResponseTemplate("I couldn't have said it better myself!", ResponseType.WARM),
            new ResponseTemplate("That's exactly how I feel too!", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Right on! We're on the same page.", ResponseType.FRIENDLY),
            new ResponseTemplate("Absolutely! You nailed it.", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("That's so true! Great minds think alike.", ResponseType.WARM),
            new ResponseTemplate("Could not agree more! That's spot on.", ResponseType.ENTHUSIASTIC),
            new ResponseTemplate("Yes! That's exactly it.", ResponseType.INTERESTED),
            new ResponseTemplate("You get it! We think alike.", ResponseType.FRIENDLY)
        );
        
        // Disagreement templates - when user expresses disagreement (polite)
        addTemplate("disagreement",
            new ResponseTemplate("That's an interesting perspective! I see it differently, but I respect your view.", ResponseType.UNDERSTANDING),
            new ResponseTemplate("Hmm, I can see where you're coming from, but I might think about it differently.", ResponseType.THOUGHTFUL),
            new ResponseTemplate("That's a fair point, though I might see it another way.", ResponseType.WARM),
            new ResponseTemplate("Interesting! We can agree to disagree on this one.", ResponseType.FRIENDLY),
            new ResponseTemplate("That's your take on it - I respect that, even if I see it differently.", ResponseType.UNDERSTANDING),
            new ResponseTemplate("I hear you, though I have a slightly different perspective.", ResponseType.THOUGHTFUL),
            new ResponseTemplate("That's a valid viewpoint! I happen to think differently, but that's what makes conversations interesting.", ResponseType.WARM),
            new ResponseTemplate("I see your point, but I'd have to respectfully disagree.", ResponseType.PROFESSIONAL),
            new ResponseTemplate("That's one way to look at it! I appreciate that we can have different perspectives.", ResponseType.FRIENDLY),
            new ResponseTemplate("Hmm, I think we might have different views on this one - and that's okay!", ResponseType.WARM)
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
        if (intent.equals("wellbeing_response") && hbuPattern.matcher(lowerInput).find()) {
            List<ResponseTemplate> hbuTemplates = responseTemplates.get("wellbeing_hbu");
            if (hbuTemplates != null && !hbuTemplates.isEmpty()) {
                ResponseTemplate selected = hbuTemplates.get(random.nextInt(hbuTemplates.size()));
                String response = selected.getText();
                if (context != null) {
                    context.addTurn(userInput, response, intent);
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
            context.addTurn(userInput, response, intent);
        }
        
        return response;
    }
    
    /**
     * Generates contextual follow-up responses based on conversation history
     */
    private String generateContextualResponse(String intent, String userInput, ConversationContext context) {
        String lastInput = context.getLastUserInput();
        
        if (lastInput == null) return null;
        
        String lowerLastInput = lastInput.toLowerCase();
        String lowerUserInput = userInput.toLowerCase();
        
        // Handle homework subject follow-ups using regex patterns
        if (homeworkSubjectPattern.matcher(lowerLastInput).find() || 
            questionSubjectPattern.matcher(lowerLastInput).find()) {
            if (mathSubjectPattern.matcher(lowerUserInput).find()) {
                return "Math can be challenging! What specific topic or problem are you working on?";
            }
            if (scienceSubjectPattern.matcher(lowerUserInput).find()) {
                return "Science is fascinating! What specific area are you studying?";
            }
            if (historySubjectPattern.matcher(lowerUserInput).find()) {
                return "History is so interesting! What time period or event are you learning about?";
            }
        }
        
        // Handle emotion follow-ups using regex patterns
        if (lowerLastInput.contains("how are you") ||
            lowerLastInput.contains("how r u") ||
            lowerLastInput.contains("how's your day") ||
            lowerLastInput.contains("how is your day")) {
            if (emotionPositivePattern.matcher(lowerUserInput).find()) {
                return "That's wonderful to hear! What made your day good?";
            }
            if (emotionNegativePattern.matcher(lowerUserInput).find()) {
                return "I'm sorry to hear that. Would you like to talk about what's making you feel this way?";
            }
        }
        
        // Handle yes/no follow-ups using regex patterns
        if (yesPattern.matcher(userInput).find()) {
            return "That's great! Tell me more about it.";
        }
        
        if (noPattern.matcher(userInput).find()) {
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
        SOOTHING("soothing"),
        CELEBRATORY("celebratory"),
        ENCOURAGING("encouraging"),
        MOTIVATING("motivating"),
        RELIEVED("relieved"),
        INSPIRING("inspiring"),
        CONCERNED("concerned");
        
        private final String value;
        
        ResponseType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}

