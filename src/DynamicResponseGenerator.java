import java.util.*;
import java.time.LocalTime;

/**
 * DynamicResponseGenerator - Creates varied, context-aware responses
 * Generates responses dynamically based on user input, context, and various factors
 */
public class DynamicResponseGenerator {
    
    private Random random;
    private String userName;
    
    // Time-based greetings
    private Map<String, String[]> timeGreetings;
    
    // Response building blocks
    private String[] openingPhrases;
    private String[] middlePhrases;
    private String[] closingPhrases;
    private String[] followUpQuestions;
    
    // Emoji mappings
    private Map<String, String[]> emojiMappings;
    
    public DynamicResponseGenerator() {
        this.random = new Random();
        this.userName = null;
        initializeResponseBlocks();
    }
    
    public void setUserName(String name) {
        this.userName = name;
    }
    
    private void initializeResponseBlocks() {
        // Time-based greetings
        timeGreetings = new HashMap<>();
        timeGreetings.put("morning", new String[]{
            "Good morning! Hope you're having a great start to your day.",
            "Morning! The day is full of possibilities!",
            "Rise and shine! What a beautiful morning to chat!",
            "Good morning! I hope you had a restful night."
        });
        timeGreetings.put("afternoon", new String[]{
            "Good afternoon! How's your day going so far?",
            "Afternoon! Hope your day has been treating you well!",
            "Good afternoon! The day is moving along nicely.",
            "Hey! Hope your afternoon is going great!"
        });
        timeGreetings.put("evening", new String[]{
            "Good evening! How was your day?",
            "Evening! Time to wind down and relax.",
            "Good evening! I hope your day went well.",
            "Hey there! Evening chats are the best, aren't they?"
        });
        timeGreetings.put("night", new String[]{
            "Good night! Time to rest and recharge.",
            "Night! Hope you had a wonderful day.",
            "Good evening/night! Thanks for chatting with me today.",
            "Sleep well! I'll be here whenever you want to chat again."
        });
        
        // Opening phrases
        openingPhrases = new String[]{
            "I wanted to say",
            "I've been thinking",
            "Here's what comes to mind",
            "Let me share this with you",
            "I feel like mentioning",
            "Something I want to mention is",
            "I was just reminded that",
            "You know what's interesting"
        };
        
        // Middle phrases
        middlePhrases = new String[]{
            "because",
            "and this relates to",
            "which connects to",
            "similarly,",
            "on a related note,",
            "this also means",
            "in other words,",
            "which brings me to"
        };
        
        // Closing phrases
        closingPhrases = new String[]{
            "What do you think about that?",
            "Have you ever thought about it that way?",
            "Does that make sense?",
            "I'd love to hear your thoughts!",
            "That's just my perspective though.",
            "How does that resonate with you?",
            "What's your take on this?",
            "Makes you think, doesn't it?"
        };
        
        // Follow-up questions based on topic
        followUpQuestions = new String[]{
            "What else is on your mind?",
            "Is there anything else you'd like to chat about?",
            "What's been occupying your thoughts lately?",
            "What would you like to explore next?",
            "Anything else you'd like to discuss?",
            "What can I help you with today?",
            "What's been the highlight of your day?",
            "Is there something specific you want to talk about?"
        };
        
        // Emoji mappings for different contexts
        emojiMappings = new HashMap<>();
        emojiMappings.put("happy", new String[]{"😊", "😄", "🌟", "✨"});
        emojiMappings.put("sad", new String[]{"😔", "💙", "🤗", "🌧️"});
        emojiMappings.put("excited", new String[]{"🎉", "🙌", "🚀", "💫"});
        emojiMappings.put("curious", new String[]{"🤔", "💭", "🔍", "💡"});
        emojiMappings.put("caring", new String[]{"💚", "🤝", "❤️", "🌿"});
        emojiMappings.put("neutral", new String[]{"🙂", "✨", "💬", "👋"});
    }
    
    /**
     * Generate a time-aware greeting based on current time
     */
    public String getTimeBasedGreeting() {
        LocalTime now = LocalTime.now();
        String timeOfDay;
        
        if (now.getHour() >= 5 && now.getHour() < 12) {
            timeOfDay = "morning";
        } else if (now.getHour() >= 12 && now.getHour() < 17) {
            timeOfDay = "afternoon";
        } else if (now.getHour() >= 17 && now.getHour() < 21) {
            timeOfDay = "evening";
        } else {
            timeOfDay = "night";
        }
        
        String[] greetings = timeGreetings.get(timeOfDay);
        String greeting = greetings[random.nextInt(greetings.length)];
        
        // Personalize if name is set
        if (userName != null && !userName.isEmpty()) {
            String[] personalized = {
                "Good " + timeOfDay + ", " + userName + "! Hope you're having a great time!",
                "Hey " + userName + "! Good " + timeOfDay + "! How are you doing?",
                userName + "! Great to chat with you this " + timeOfDay + "!",
                "Good " + timeOfDay + ", " + userName + "! What a perfect time to talk!"
            };
            greeting = personalized[random.nextInt(personalized.length)];
        }
        
        return greeting;
    }
    
    /**
     * Generate a personalized greeting
     */
    public String getPersonalizedGreeting() {
        String[] greetings = {
            "Hey there! Great to chat with you",
            "Hi! Wonderful to have you here",
            "Hello, friend! Ready to chat?",
            "Hey! I've been looking forward to talking with you",
            "Hi there! Always a pleasure",
            "Welcome! Let's have a great conversation"
        };
        
        String base = greetings[random.nextInt(greetings.length)];
        
        if (userName != null && !userName.isEmpty()) {
            base = "Hey " + userName + "! " + greetings[random.nextInt(greetings.length)].substring(3).toLowerCase();
        }
        
        // Add random follow-up
        String[] followUps = {
            " What's been on your mind?",
            " How are you doing today?",
            " What's new with you?",
            " How's everything going?",
            ""
        };
        
        return base + followUps[random.nextInt(followUps.length)];
    }
    
    /**
     * Generate a follow-up question
     */
    public String getFollowUpQuestion() {
        return followUpQuestions[random.nextInt(followUpQuestions.length)];
    }
    
    /**
     * Generate a context-aware encouragement
     */
    public String getEncouragement(String context) {
        Map<String, String[]> encouragements = new HashMap<>();
        
        encouragements.put("general", new String[]{
            "You're doing great!",
            "Keep going, you're doing amazing!",
            "I'm proud of you for trying!",
            "Every step forward counts!",
            "You've got this!"
        });
        
        encouragements.put("struggling", new String[]{
            "Difficult moments don't last forever.",
            "You're stronger than you think.",
            "This too shall pass.",
            "Be gentle with yourself.",
            "It's okay to not be okay sometimes."
        });
        
        encouragements.put("learning", new String[]{
            "Learning something new is always an achievement!",
            "Curiosity is a wonderful trait!",
            "You're expanding your horizons!",
            "Knowledge is power!",
            "Keep exploring and learning!"
        });
        
        encouragements.put("creating", new String[]{
            "Creating something new is so meaningful!",
            "Your creativity is a gift!",
            "Making things is how we leave our mark!",
            "The world needs your unique creations!",
            "Keep building and creating!"
        });
        
        encouragements.put("sharing", new String[]{
            "Sharing your thoughts takes courage!",
            "Opening up is a sign of strength!",
            "Your voice matters!",
            "Thank you for trusting me!",
            "I'm here to listen anytime!"
        });
        
        String[] options = encouragements.getOrDefault(context.toLowerCase(), 
                                                       encouragements.get("general"));
        return options[random.nextInt(options.length)];
    }
    
    /**
     * Generate a composite response from building blocks
     */
    public String generateCompositeResponse() {
        String opening = openingPhrases[random.nextInt(openingPhrases.length)];
        String middle = middlePhrases[random.nextInt(middlePhrases.length)];
        String closing = closingPhrases[random.nextInt(closingPhrases.length)];
        
        String[] facts = {
            "honey never spoils - archaeologists have found 3000-year-old honey",
            "a day on Venus is longer than its year",
            "bananas are berries but strawberries aren't",
            "a group of flamingos is called a flamboyance",
            "butterflies taste with their feet",
            "the shortest war lasted only 38-45 minutes",
            "sloths can hold their breath longer than dolphins",
            "the unicorn is Scotland's national animal"
        };
        
        String fact = facts[random.nextInt(facts.length)];
        
        return opening + " that " + fact + ", " + middle + " " + closing;
    }
    
    /**
     * Generate an empathetic response
     */
    public String getEmpatheticResponse(String emotion) {
        Map<String, String[]> empathetic = new HashMap<>();
        
        empathetic.put("sad", new String[]{
            "I hear that you're feeling down. That takes courage to acknowledge.",
            "It sounds like things are tough right now. I'm here for you.",
            "I'm sorry you're feeling this way. Your feelings are valid.",
            "When we're sad, even small things can feel heavy. I'm here to listen."
        });
        
        empathetic.put("stressed", new String[]{
            "Stress can be overwhelming. Take a deep breath - you've got this.",
            "I'm sorry things feel chaotic right now. Let's take this one step at a time.",
            "When everything piles up, it helps to break things down. What's the biggest thing?",
            "Stress is tough, but so are you. We'll get through this together."
        });
        
        empathetic.put("anxious", new String[]{
            "Anxiety can feel scary, but it will pass. You're not alone.",
            "I'm here with you. Let's take things one moment at a time.",
            "Anxious feelings are temporary. Focus on right now, right here.",
            "It's okay to feel anxious. What would help you feel a little calmer?"
        });
        
        empathetic.put("happy", new String[]{
            "I'm so glad you're feeling good! That's wonderful!",
            "Happiness is contagious! Thanks for sharing the positive vibes.",
            "That's great to hear! What's putting that smile on your face?",
            "I love that you're feeling happy! Embrace that energy!"
        });
        
        empathetic.put("lonely", new String[]{
            "Loneliness is tough, but remember - you're not alone. I'm here!",
            "I hear you. Sometimes connection is what we need most.",
            "Just know that someone cares - I do!",
            "Feeling lonely doesn't mean you are alone. I'm here to chat!"
        });
        
        empathetic.put("angry", new String[]{
            "Anger is a valid emotion. It's okay to feel frustrated.",
            "I hear your frustration. Sometimes it helps to talk about it.",
            "It's completely valid to feel angry sometimes.",
            "When we're angry, it helps to express it in healthy ways. I'm listening."
        });
        
        empathetic.put("tired", new String[]{
            "Exhaustion is real! Make sure to take care of yourself.",
            "Sometimes we all need to rest. Listen to your body.",
            "Being tired is tough. Remember to be gentle with yourself.",
            "If you can, take a break. Your well-being matters!"
        });
        
        String key = emotion != null ? emotion.toLowerCase() : "sad";
        String[] options = empathetic.getOrDefault(key, empathetic.get("sad"));
        return options[random.nextInt(options.length)];
    }
    
    /**
     * Generate a thought-provoking question
     */
    public String getThoughtProvokingQuestion() {
        String[] questions = {
            "What's something you've been meaning to try but haven't yet?",
            "If you could have dinner with anyone, who would it be?",
            "What's a small thing that made you smile today?",
            "What's something you're looking forward to?",
            "If you could travel anywhere right now, where would you go?",
            "What's a skill you'd love to learn?",
            "What's the best advice you've ever received?",
            "What's something that always makes you laugh?",
            "If you could have any superpower, what would it be?",
            "What's your favorite way to spend a day off?"
        };
        
        return questions[random.nextInt(questions.length)];
    }
    
    /**
     * Generate a fun interaction starter
     */
    public String getFunInteraction() {
        String[] interactions = {
            "You know what? I heard something fun today!",
            "Here's a thought - what if we played a quick game?",
            "I could really use a good joke right now!",
            "Want to hear something interesting?",
            "I've been thinking about fun facts. Here's one...",
            "Sometimes it helps to just chat about random things!",
            "Let's take a break from serious stuff - what's on your mind?",
            "I believe every day needs a moment of joy! What's yours?"
        };
        
        return interactions[random.nextInt(interactions.length)];
    }
    
    /**
     * Generate a reflection prompt
     */
    public String getReflectionPrompt() {
        String[] prompts = {
            "Reflecting on the day, what went well?",
            "What's something you're grateful for today?",
            "If you could relive one moment from today, which would it be?",
            "What's one thing you learned today?",
            "How did you take care of yourself today?",
            "What's something you're proud of from today?",
            "If today was a movie, what would it be called?",
            "What's the kindest thing you did today (or would like to do)?"
        };
        
        return prompts[random.nextInt(prompts.length)];
    }
    
    /**
     * Get emoji for a given context
     */
    public String getEmoji(String context) {
        String key = context != null ? context.toLowerCase() : "neutral";
        String[] emojis = emojiMappings.getOrDefault(key, emojiMappings.get("neutral"));
        return emojis[random.nextInt(emojis.length)];
    }
    
    /**
     * Generate a check-in message
     */
    public String getCheckInMessage() {
        String[] messages = {
            "Hey! Just checking in - how are you doing?",
            "I'm here! Is everything okay?",
            "Just wanted to see how you're feeling?",
            "Hey there! What's on your mind?",
            "I'm here if you want to talk!",
            "How's everything going? I'm listening!",
            "Just a friendly check-in - how are you really doing?",
            "Hey! Thanks for being here. What would you like to chat about?"
        };
        
        return messages[random.nextInt(messages.length)];
    }
    
    /**
     * Generate a wrapping-up message
     */
    public String getWrappingUpMessage() {
        String[] messages = {
            "It's been great chatting! I'm here whenever you need me.",
            "Thanks for the wonderful conversation! Take care!",
            "I enjoyed talking with you! Don't be a stranger!",
            "Our chat made my day! Hope yours is amazing!",
            "Thanks for sharing! I'll be here whenever you want to chat again.",
            "It was so nice talking with you! Until next time!",
            "This was a great conversation! Wishing you all the best!",
            "Thanks for chatting! Remember, I'm always here for you!"
        };
        
        return messages[random.nextInt(messages.length)];
    }
    
    /**
     * Generate a random affirmation
     */
    public String getAffirmation() {
        String[] affirmations = {
            "You are enough, exactly as you are.",
            "Your feelings are valid and matter.",
            "You have the strength to get through this.",
            "Every day is a new opportunity.",
            "You deserve kindness, especially from yourself.",
            "Your presence makes a difference.",
            "You are worthy of love and respect.",
            "It's okay to ask for help.",
            "You are doing the best you can.",
            "Progress, not perfection, is what matters.",
            "Your imperfections make you uniquely beautiful.",
            "You have survived every bad day so far.",
            "Small steps still move you forward.",
            "You are worthy of good things.",
            "Your story isn't over yet."
        };
        
        return affirmations[random.nextInt(affirmations.length)];
    }
    
    /**
     * Generate a motivational quote
     */
    public String getMotivationalQuote() {
        String[] quotes = {
            "The only way to do great work is to love what you do.",
            "Believe you can and you're halfway there.",
            "The future belongs to those who believe in the beauty of their dreams.",
            "It always seems impossible until it's done.",
            "Don't watch the clock; do what it does. Keep going.",
            "Success is not final, failure is not fatal: it is the courage to continue that counts.",
            "The best way to predict the future is to create it.",
            "Every master was once a disaster.",
            "Dreams don't work unless you do.",
            "Your limitation—it's only your imagination."
        };
        
        return quotes[random.nextInt(quotes.length)];
    }
    
    /**
     * Generate a compliment
     */
    public String getCompliment() {
        String[] compliments = {
            "You have a wonderful way of expressing yourself.",
            "Your perspective is refreshing.",
            "I appreciate your honesty.",
            "You have such a kind heart.",
            "Your strength is inspiring.",
            "You light up the conversation.",
            "Your curiosity is wonderful.",
            "You're a great listener.",
            "Your resilience is admirable.",
            "You have a great sense of humor.",
            "Your kindness means a lot.",
            "You're incredibly thoughtful.",
            "Your positivity is contagious.",
            "You handle challenges with grace.",
            "Your authenticity is refreshing."
        };
        
        return compliments[random.nextInt(compliments.length)];
    }
    
    /**
     * Generate an icebreaker question
     */
    public String getIcebreaker() {
        String[] icebreakers = {
            "What's the most interesting thing you've learned recently?",
            "If you could instantly master one skill, what would it be?",
            "What's your favorite comfort food?",
            "Do you have any hidden talents?",
            "What's on your bucket list?",
            "What's the best piece of advice you've ever received?",
            "If you could live anywhere in the world, where would it be?",
            "What's your favorite season and why?",
            "What's a movie you can watch over and over?",
            "If you could have dinner with any historical figure, who would it be?",
            "What's your favorite childhood memory?",
            "Do you have any pets? Tell me about them!",
            "What's something you're particularly good at?",
            "If you won the lottery tomorrow, what's the first thing you'd do?",
            "What's your favorite quote or saying?"
        };
        
        return icebreakers[random.nextInt(icebreakers.length)];
    }
    
    /**
     * Generate a weekend/winding down message
     */
    public String getWeekendWishes() {
        String[] wishes = {
            "Hope your week has been great! The weekend is almost here!",
            "Wishing you a relaxing rest of the week!",
            "Almost the weekend! What are you looking forward to?",
            "Hope work/school has been treating you well!",
            "The week is almost over - you made it!",
            "Take some time for yourself this week!",
            "Hope you've had time to recharge!",
            "The weekend is coming! Any fun plans?",
            "Mid-week check-in! How are you holding up?",
            "Don't forget to take breaks and breathe!"
        };
        
        return wishes[random.nextInt(wishes.length)];
    }
    
    /**
     * Generate a weather-related response (simulated)
     */
    public String getWeatherResponse() {
        String[] responses = {
            "Whether it's sunny or rainy outside, I'm here to brighten your day!",
            "Rainy day perfect for deep conversations? Or sunny day for outdoor adventures?",
            "I love how weather sets the mood - what's your favorite weather?",
            "Sunshine or rain, every day has its beauty!",
            "Weather you love it or hate it, I'm here either way!",
            "Storms don't last forever - just like tough times!",
            "Beautiful day to chat, isn't it?",
            "Perfect weather for indoor conversations like ours!"
        };
        
        return responses[random.nextInt(responses.length)];
    }
    
    /**
     * Generate a work/school motivation message
     */
    public String getWorkMotivation() {
        String[] messages = {
            "You're doing great with your work! Keep it up!",
            "Hard work pays off - I believe in you!",
            "Every task you complete is a step forward!",
            "Taking breaks is important too - don't forget to rest!",
            "Your dedication is impressive!",
            "One step at a time - you're making progress!",
            "Work life balance matters - take care of yourself!",
            "The effort you put in matters, even if results take time!",
            "You've got the skills to handle this!",
            "Stay focused, but stay kind to yourself!"
        };
        
        return messages[random.nextInt(messages.length)];
    }
    
    /**
     * Generate a celebration message
     */
    public String getCelebrationMessage() {
        String[] messages = {
            "That's amazing! You should be proud of yourself!",
            "Congratulations! This is a big deal!",
            "Wow! That's fantastic news!",
            "I'm so happy for you! You deserve this!",
            "Celebrate this moment! You've earned it!",
            "That's wonderful! Keep up the great work!",
            "Amazing! Your hard work is paying off!",
            "This is awesome news! You should be thrilled!",
            "I'm cheering for you! This is incredible!",
            "What an achievement! Well done!"
        };
        
        return messages[random.nextInt(messages.length)];
    }
    
    /**
     * Generate a comfort message
     */
    public String getComfortMessage() {
        String[] messages = {
            "It's okay to have hard days. I'm here for you.",
            "Take all the time you need to feel better.",
            "Healing isn't linear - be patient with yourself.",
            "This feeling won't last forever.",
            "You're not alone in this.",
            "Allow yourself to feel whatever you need to feel.",
            "Self-care isn't selfish - it's necessary.",
            "Tomorrow is a new opportunity.",
            "Reach out if you need someone to talk to.",
            "You don't have to go through this alone."
        };
        
        return messages[random.nextInt(messages.length)];
    }
    
    /**
     * Generate a gratitude message
     */
    public String getGratitudeMessage() {
        String[] messages = {
            "Thank you for sharing this moment with me.",
            "I appreciate you trusting me with your thoughts.",
            "Grateful for our conversation today!",
            "Thanks for being open and honest.",
            "Your presence makes these conversations meaningful.",
            "Thank you for spending time with me.",
            "I appreciate your authenticity.",
            "Thanks for letting me be part of your day.",
            "Grateful for the opportunity to chat with you.",
            "Thank you for being you!"
        };
        
        return messages[random.nextInt(messages.length)];
    }
    
    /**
     * Generate an appreciation message
     */
    public String getAppreciationMessage() {
        String[] messages = {
            "I really appreciate our chats!",
            "You make conversations so interesting!",
            "I love how thoughtful you are!",
            "Your perspective adds so much value!",
            "Thanks for being such a great conversation partner!",
            "I appreciate your kindness and openness!",
            "You have such an interesting mind!",
            "Thanks for sharing your world with me!",
            "I love learning from our conversations!",
            "You bring so much to our talks!"
        };
        
        return messages[random.nextInt(messages.length)];
    }
}

