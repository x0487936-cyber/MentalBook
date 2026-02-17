import java.util.*;
import java.util.regex.*;

/**
 * Emotion Detection System for VirtualXander
 * Detects and classifies emotional states from user input
 */
public class EmotionDetector {
    
    private Map<Emotion, List<Pattern>> emotionPatterns;
    private Map<String, Emotion> keywordEmotions;
    private Map<Emotion, List<String>> emotionResponses;
    private Random random;
    
    public EmotionDetector() {
        this.emotionPatterns = new HashMap<>();
        this.keywordEmotions = new HashMap<>();
        this.emotionResponses = new HashMap<>();
        this.random = new Random();
        initializeEmotionPatterns();
        initializeKeywordMappings();
        initializeEmotionResponses();
    }
    
    /**
     * Enum for supported emotions
     */
    public enum Emotion {
        // Positive Emotions
        HAPPY("happy", "Feeling joyful and positive"),
        EXCITED("excited", "Feeling enthusiastic and eager"),
        MOTIVATED("motivated", "Feeling driven and inspired"),
        GRATEFUL("grateful", "Feeling thankful and appreciative"),
        CONFIDENT("confident", "Feeling self-assured"),
        RELAXED("relaxed", "Feeling calm and at ease"),
        CURIOUS("curious", "Feeling interested and inquisitive"),
        CREATIVE("creative", "Feeling imaginative"),
        HOPEFUL("hopeful", "Feeling positive anticipation about the future"),
        PROUD("proud", "Feeling satisfaction with achievement"),
        AMUSED("amused", "Finding something entertaining or funny"),
        INSPIRED("inspired", "Feeling creatively motivated"),
        NOSTALGIC("nostalgic", "Feeling sentimental about the past"),
        SURPRISED("surprised", "Experiencing unexpected wonder"),
        RELIEVED("relieved", "Feeling released from stress or worry"),
        PEACEFUL("peaceful", "Feeling deep calm and harmony"),
        
        // Negative Emotions
        SAD("sad", "Feeling down or unhappy"),
        STRESSED("stressed", "Feeling overwhelmed or anxious"),
        ANXIOUS("anxious", "Feeling worried or nervous"),
        LONELY("lonely", "Feeling isolated"),
        ANGRY("angry", "Feeling frustrated or mad"),
        OVERWHELMED("overwhelmed", "Feeling swamped"),
        TIRED("tired", "Feeling exhausted"),
        BORED("bored", "Feeling uninterested"),
        HURT("hurt", "Feeling emotionally wounded"),
        CONFUSED("confused", "Feeling uncertain"),
        FRUSTRATED("frustrated", "Feeling stuck or annoyed"),
        DISAPPOINTED("disappointed", "Feeling let down by expectations"),
        EMBARRASSED("embarrassed", "Feeling self-conscious"),
        SYMPATHY("sympathy", "Feeling compassion and understanding for others"),
        
        // Supportive Emotions
        SOOTHING("soothing", "Feeling calm and comforting"),
        CARING("caring", "Feeling warmth and concern for others"),
        SYMPATHETIC("sympathetic", "Feeling empathy and understanding for others"),
        UNDERSTANDING("understanding", "Feeling comprehension and acceptance"),
        
        // Neutral
        NEUTRAL("neutral", "Feeling balanced"),
        CHALLENGED("challenged", "Feeling tested but capable"),
        UNKNOWN("unknown", "Unable to determine emotion");
        
        private final String name;
        private final String description;
        
        Emotion(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() {
            return name;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * Detected emotion result class
     */
    public static class EmotionResult {
        private final Emotion primaryEmotion;
        private final double confidence;
        private final Map<Emotion, Double> allEmotions;
        private final boolean isNegative;
        private final boolean isPositive;
        
        public EmotionResult(Emotion primaryEmotion, double confidence, Map<Emotion, Double> allEmotions) {
            this.primaryEmotion = primaryEmotion;
            this.confidence = confidence;
            this.allEmotions = allEmotions;
            this.isNegative = isNegativeEmotion(primaryEmotion);
            this.isPositive = isPositiveEmotion(primaryEmotion);
        }
        
        private static boolean isNegativeEmotion(Emotion emotion) {
            return emotion == Emotion.SAD || emotion == Emotion.STRESSED ||
                   emotion == Emotion.ANXIOUS || emotion == Emotion.LONELY ||
                   emotion == Emotion.ANGRY || emotion == Emotion.OVERWHELMED ||
                   emotion == Emotion.TIRED || emotion == Emotion.BORED ||
                   emotion == Emotion.HURT ||
                   emotion == Emotion.CONFUSED || emotion == Emotion.FRUSTRATED ||
                   emotion == Emotion.DISAPPOINTED || emotion == Emotion.EMBARRASSED;
        }
        
        private static boolean isPositiveEmotion(Emotion emotion) {
            return emotion == Emotion.HAPPY || emotion == Emotion.EXCITED ||
                   emotion == Emotion.MOTIVATED || emotion == Emotion.GRATEFUL ||
                   emotion == Emotion.CONFIDENT || emotion == Emotion.RELAXED ||
                   emotion == Emotion.CURIOUS || emotion == Emotion.CREATIVE ||
                   emotion == Emotion.HOPEFUL || emotion == Emotion.PROUD ||
                   emotion == Emotion.AMUSED || emotion == Emotion.INSPIRED ||
                   emotion == Emotion.NOSTALGIC || emotion == Emotion.SURPRISED ||
                   emotion == Emotion.RELIEVED || emotion == Emotion.PEACEFUL;
        }
        
        public Emotion getPrimaryEmotion() {
            return primaryEmotion;
        }
        
        public double getConfidence() {
            return confidence;
        }
        
        public Map<Emotion, Double> getAllEmotions() {
            return new HashMap<>(allEmotions);
        }
        
        public boolean isNegative() {
            return isNegative;
        }
        
        public boolean isPositive() {
            return isPositive;
        }
        
        public boolean isNeutral() {
            return primaryEmotion == Emotion.NEUTRAL;
        }
        
        public boolean isUnknown() {
            return primaryEmotion == Emotion.UNKNOWN;
        }
    }
    
    private void initializeEmotionPatterns() {
        // Happy patterns
        addEmotionPattern(Emotion.HAPPY, "\\b(happy|glad|joyful|joy|cheerful|content|satisfied|pleased|delighted|thrilled|elated)\\b");
        addEmotionPattern(Emotion.HAPPY, "\\b(i feel (good|great|awesome|wonderful|amazing|fantastic))\\b");
        addEmotionPattern(Emotion.HAPPY, "(making me smile|feeling good|feeling great)");
        
        // Excited patterns
        addEmotionPattern(Emotion.EXCITED, "\\b(excited|eager|thrilled|enthusiastic| pumped| can't wait)\\b");
        addEmotionPattern(Emotion.EXCITED, "\\b(looking forward|anticipating|psyched|amped)\\b");
        
        // Motivated patterns
        addEmotionPattern(Emotion.MOTIVATED, "\\b(motivated|inspired|driven|determined|energized)\\b");
        addEmotionPattern(Emotion.MOTIVATED, "\\b(ready to|going to|will power|ambitious)\\b");
        
        // Grateful patterns
        addEmotionPattern(Emotion.GRATEFUL, "\\b(grateful|thankful|appreciative|blessed|fortunate)\\b");
        addEmotionPattern(Emotion.GRATEFUL, "\\b(thank god|thankful for|appreciate)\\b");
        
        // Confident patterns
        addEmotionPattern(Emotion.CONFIDENT, "\\b(confident|sure|certain|assured|self-assured)\\b");
        addEmotionPattern(Emotion.CONFIDENT, "\\b(i can do|believe in|trust myself)\\b");
        
        // Relaxed patterns
        addEmotionPattern(Emotion.RELAXED, "\\b(relaxed|calm|peaceful|chilled|chill|at ease|serene)\\b");
        addEmotionPattern(Emotion.RELAXED, "\\b(taking it easy|winding down|unwinding)\\b");
        
        // Curious patterns
        addEmotionPattern(Emotion.CURIOUS, "\\b(curious|wondering|interested|inquisitive|want to know)\\b");
        addEmotionPattern(Emotion.CURIOUS, "\\b(what if|how does|why is|tell me about)\\b");
        
        // Creative patterns
        addEmotionPattern(Emotion.CREATIVE, "\\b(creative|imaginative|artistic|innovative)\\b");
        addEmotionPattern(Emotion.CREATIVE, "\\b(creating|making|building|designing|writing)\\b");
        
        // Sad patterns
        addEmotionPattern(Emotion.SAD, "\\b(sad|sadly|unhappy|miserable|down|blue|down in the dumps)\\b");
        addEmotionPattern(Emotion.SAD, "\\b(feeling (sad|down|blue|low|empty))\\b");
        addEmotionPattern(Emotion.SAD, "\\b(not (happy|good|great))\\b");
        
        // Stressed patterns
        addEmotionPattern(Emotion.STRESSED, "\\b(stressed|stressed out|pressure|under pressure)\\b");
        addEmotionPattern(Emotion.STRESSED, "\\b(so much|together too much|overloaded|swamped)\\b");
        
        // Anxious patterns
        addEmotionPattern(Emotion.ANXIOUS, "\\b(anxious|worried|nervous|uneasy|on edge)\\b");
        addEmotionPattern(Emotion.ANXIOUS, "\\b(what if|fear|scared|afraid|worried about)\\b");
        
        // Lonely patterns
        addEmotionPattern(Emotion.LONELY, "\\b(lonely|alone|isolated|ignored|unwanted)\\b");
        addEmotionPattern(Emotion.LONELY, "\\b(no one|nobody cares|feel alone)\\b");

        // Hurt patterns
        addEmotionPattern(Emotion.HURT, "\\b(hurt|heartbroken|wounded|pain|suffering)\\b");
        addEmotionPattern(Emotion.HURT, "\\b(feeling (in pain|hated|unloved|rejected))\\b");
        
        // Angry patterns
        addEmotionPattern(Emotion.ANGRY, "\\b(angry|mad|furious|irritated|annoyed|frustrated)\\b");
        addEmotionPattern(Emotion.ANGRY, "\\b(so annoying|drives me crazy|hate it)\\b");
        
        // Overwhelmed patterns
        addEmotionPattern(Emotion.OVERWHELMED, "\\b(overwhelmed|swamped|buried|drowning|too much)\\b");
        addEmotionPattern(Emotion.OVERWHELMED, "\\b(where to start|don't know where to begin)\\b");
        
        // Tired patterns
        addEmotionPattern(Emotion.TIRED, "\\b(tired|exhausted|weary|fatigued|drained|sleepy)\\b");
        addEmotionPattern(Emotion.TIRED, "\\b(need sleep|need rest|need a break)\\b");
        
        // Bored patterns
        addEmotionPattern(Emotion.BORED, "\\b(bored|boring|boredom|nothing to do)\\b");
        addEmotionPattern(Emotion.BORED, "\\b(need something|looking for|want to do)\\b");
        
        // Confused patterns
        addEmotionPattern(Emotion.CONFUSED, "\\b(confused|confusing|don't understand|lost|puzzled)\\b");
        addEmotionPattern(Emotion.CONFUSED, "\\b(don't get it|what does|how to)\\b");
        
        // Frustrated patterns
        addEmotionPattern(Emotion.FRUSTRATED, "\\b(frustrated|frustrating|stuck|can't)\\b");
        addEmotionPattern(Emotion.FRUSTRATED, "\\b(not working|won't|doesn't|why won't)\\b");
        
        // Sympathy patterns
        addEmotionPattern(Emotion.SYMPATHY, "\\b(sorry|feel sorry|feel bad for|pity|poor thing)\\b");
        addEmotionPattern(Emotion.SYMPATHY, "\\b(that's sad|that's terrible|that's awful|how sad)\\b");
        addEmotionPattern(Emotion.SYMPATHY, "\\b(i feel for|i sympathize|my heart goes out|thinking of)\\b");
        
        // Soothing patterns
        addEmotionPattern(Emotion.SOOTHING, "\\b(calm|relax|peaceful|serene|tranquil)\\b");
        addEmotionPattern(Emotion.SOOTHING, "\\b(it's okay|it's alright|everything will be fine)\\b");
        addEmotionPattern(Emotion.SOOTHING, "\\b(take a deep breath|breathe|settle down)\\b");
        
        // Caring patterns
        addEmotionPattern(Emotion.CARING, "\\b(care|caring|compassion|kindness|gentle)\\b");
        addEmotionPattern(Emotion.CARING, "\\b(be there for|support|helping|hug)\\b");
        addEmotionPattern(Emotion.CARING, "\\b(i'm here|here for you|got your back)\\b");
        
        // Hopeful patterns
        addEmotionPattern(Emotion.HOPEFUL, "\\b(hopeful|optimistic|looking forward|positive outlook)\\b");
        addEmotionPattern(Emotion.HOPEFUL, "\\b(better days|things will get|hoping|hopefully)\\b");
        addEmotionPattern(Emotion.HOPEFUL, "\\b(i hope|keeping hope|faith|believe things will)\\b");
        
        // Proud patterns
        addEmotionPattern(Emotion.PROUD, "\\b(proud|accomplished|achievement|success|won)\\b");
        addEmotionPattern(Emotion.PROUD, "\\b(did it|finally|made it|proud of)\\b");
        addEmotionPattern(Emotion.PROUD, "\\b(milestone|celebration|accomplished)\\b");
        
        // Amused patterns
        addEmotionPattern(Emotion.AMUSED, "\\b(amused|hilarious|funny|laughing|lol|lmao)\\b");
        addEmotionPattern(Emotion.AMUSED, "\\b(that was funny|got me|so funny|had me)\\b");
        addEmotionPattern(Emotion.AMUSED, "\\b(rofl|lmao| kek| dead meme)\\b");
        
        // Inspired patterns
        addEmotionPattern(Emotion.INSPIRED, "\\b(inspired|moved|touched|wow)\\b");
        addEmotionPattern(Emotion.INSPIRED, "\\b(gave me ideas|motivation|sparked creativity)\\b");
        addEmotionPattern(Emotion.INSPIRED, "\\b(that's amazing|incredible|remarkable)\\b");
        
        // Nostalgic patterns
        addEmotionPattern(Emotion.NOSTALGIC, "\\b(nostalgic|miss the old|remember when|good old)\\b");
        addEmotionPattern(Emotion.NOSTALGIC, "\\b(throwback|those days|makes me think of|brings back)\\b");
        addEmotionPattern(Emotion.NOSTALGIC, "\\b(old times|childhood|memories|past)\\b");
        
        // Surprised patterns
        addEmotionPattern(Emotion.SURPRISED, "\\b(surprised|wow|oh wow|no way|unbelievable)\\b");
        addEmotionPattern(Emotion.SURPRISED, "\\b(didn't expect|didn't see that coming|shocking)\\b");
        addEmotionPattern(Emotion.SURPRISED, "\\b(OMG|omg|oh my|astonished|stunned)\\b");
        
        // Disappointed patterns
        addEmotionPattern(Emotion.DISAPPOINTED, "\\b(disappointed|let down|expected better|underwhelmed)\\b");
        addEmotionPattern(Emotion.DISAPPOINTED, "\\b(not what i hoped|not what i expected|bummer)\\b");
        addEmotionPattern(Emotion.DISAPPOINTED, "\\b(disappointing|sadly|unfortunately)\\b");
        
        // Embarrassed patterns
        addEmotionPattern(Emotion.EMBARRASSED, "\\b(embarrassed|awkward|oh no|so embarrassing)\\b");
        addEmotionPattern(Emotion.EMBARRASSED, "\\b(face palm|can't believe i|died inside)\\b");
        addEmotionPattern(Emotion.EMBARRASSED, "\\b(red face|wish i hadn't|oops)\\b");
        
        // Relieved patterns
        addEmotionPattern(Emotion.RELIEVED, "\\b(relieved|thank goodness|thank god|finally|i'm done)\\b");
        addEmotionPattern(Emotion.RELIEVED, "\\b(that was close|made it|got through)\\b");
        addEmotionPattern(Emotion.RELIEVED, "\\b(such a relief|so glad that's over|i can breathe)\\b");
        
        // Peaceful patterns
        addEmotionPattern(Emotion.PEACEFUL, "\\b(peaceful|serene|tranquil|at peace|harmony)\\b");
        addEmotionPattern(Emotion.PEACEFUL, "\\b(at one|inner peace|zen|balanced)\\b");
        addEmotionPattern(Emotion.PEACEFUL, "\\b(contentment|fulfilled|wholeness)\\b");
    }
    
    private void initializeKeywordMappings() {
        // Direct keyword to emotion mappings (higher priority)
        keywordEmotions.put("happy", Emotion.HAPPY);
        keywordEmotions.put("sad", Emotion.SAD);
        keywordEmotions.put("excited", Emotion.EXCITED);
        keywordEmotions.put("stressed", Emotion.STRESSED);
        keywordEmotions.put("anxious", Emotion.ANXIOUS);
        keywordEmotions.put("lonely", Emotion.LONELY);
        keywordEmotions.put("angry", Emotion.ANGRY);
        keywordEmotions.put("overwhelmed", Emotion.OVERWHELMED);
        keywordEmotions.put("tired", Emotion.TIRED);
        keywordEmotions.put("bored", Emotion.BORED);
        keywordEmotions.put("hurt", Emotion.HURT);
        keywordEmotions.put("challenged", Emotion.CHALLENGED);
        keywordEmotions.put("sympathetic", Emotion.SYMPATHETIC);
        keywordEmotions.put("understanding", Emotion.UNDERSTANDING);
        keywordEmotions.put("motivated", Emotion.MOTIVATED);
        keywordEmotions.put("grateful", Emotion.GRATEFUL);
        keywordEmotions.put("confident", Emotion.CONFIDENT);
        keywordEmotions.put("relaxed", Emotion.RELAXED);
        keywordEmotions.put("curious", Emotion.CURIOUS);
        keywordEmotions.put("creative", Emotion.CREATIVE);
        keywordEmotions.put("confused", Emotion.CONFUSED);
        keywordEmotions.put("frustrated", Emotion.FRUSTRATED);
        keywordEmotions.put("sympathy", Emotion.SYMPATHY);
        keywordEmotions.put("sorry", Emotion.SYMPATHY);
        keywordEmotions.put("pity", Emotion.SYMPATHY);
        keywordEmotions.put("soothing", Emotion.SOOTHING);
        keywordEmotions.put("calm", Emotion.SOOTHING);
        keywordEmotions.put("relax", Emotion.SOOTHING);
        keywordEmotions.put("caring", Emotion.CARING);
        keywordEmotions.put("care", Emotion.CARING);
        keywordEmotions.put("kind", Emotion.CARING);
        
        // New emotion keywords
        keywordEmotions.put("hopeful", Emotion.HOPEFUL);
        keywordEmotions.put("optimistic", Emotion.HOPEFUL);
        keywordEmotions.put("hope", Emotion.HOPEFUL);
        keywordEmotions.put("proud", Emotion.PROUD);
        keywordEmotions.put("accomplished", Emotion.PROUD);
        keywordEmotions.put("amused", Emotion.AMUSED);
        keywordEmotions.put("funny", Emotion.AMUSED);
        keywordEmotions.put("laughing", Emotion.AMUSED);
        keywordEmotions.put("lol", Emotion.AMUSED);
        keywordEmotions.put("inspired", Emotion.INSPIRED);
        keywordEmotions.put("inspiring", Emotion.INSPIRED);
        keywordEmotions.put("nostalgic", Emotion.NOSTALGIC);
        keywordEmotions.put("memories", Emotion.NOSTALGIC);
        keywordEmotions.put("surprised", Emotion.SURPRISED);
        keywordEmotions.put("wow", Emotion.SURPRISED);
        keywordEmotions.put("disappointed", Emotion.DISAPPOINTED);
        keywordEmotions.put("disappointing", Emotion.DISAPPOINTED);
        keywordEmotions.put("embarrassed", Emotion.EMBARRASSED);
        keywordEmotions.put("awkward", Emotion.EMBARRASSED);
        keywordEmotions.put("relieved", Emotion.RELIEVED);
        keywordEmotions.put("relief", Emotion.RELIEVED);
        keywordEmotions.put("peaceful", Emotion.PEACEFUL);
        keywordEmotions.put("serene", Emotion.PEACEFUL);
        keywordEmotions.put("calm", Emotion.PEACEFUL);
    }
    
    private void initializeEmotionResponses() {
        // Responses when detecting positive emotions
        emotionResponses.put(Emotion.HAPPY, Arrays.asList(
            "That's wonderful to hear! What's making you feel so happy?",
            "I'm so glad you're feeling happy!",
            "Happiness is contagious! What's contributing to these good vibes?",
            "That's awesome! Keep that positive energy going!"
        ));
        
        emotionResponses.put(Emotion.EXCITED, Arrays.asList(
            "Excitement is great! What are you looking forward to?",
            "I love your enthusiasm! What's sparking this excitement?",
            "That's fantastic! Tell me more about what's got you so excited!",
            "Your excitement is contagious! What's happening?"
        ));
        
        emotionResponses.put(Emotion.MOTIVATED, Arrays.asList(
            "Feeling motivated is powerful! What inspired you?",
            "Great to hear you're feeling driven! Keep that momentum!",
            "Motivation is key to success! What's fueling your drive?",
            "I love that energy! What are you planning to accomplish?"
        ));
        
        emotionResponses.put(Emotion.GRATEFUL, Arrays.asList(
            "Gratitude is a beautiful quality! What are you thankful for?",
            "That's wonderful! Gratitude really enriches life.",
            "I appreciate your positive outlook! What sparked this gratitude?",
            "Thankfulness is so valuable! What are you grateful for?"
        ));
        
        // Responses when detecting negative emotions
        emotionResponses.put(Emotion.SAD, Arrays.asList(
            "I'm sorry you're feeling sad. Would you like to talk about it?",
            "Sadness can be tough. I'm here to listen if you want to share.",
            "I'm here to listen and support you. Sometimes it helps to talk about what's making you feel down.",
            "I'm sorry you're feeling this way. I'm here to listen - what's on your mind?"
        ));
        
        emotionResponses.put(Emotion.STRESSED, Arrays.asList(
            "Stress can be overwhelming. Take a deep breath - I'm here to help.",
            "I'm sorry you're feeling stressed. What's going on?",
            "Sometimes stress builds up. Would you like to talk about what's bothering you?",
            "I understand. Let's work through this together. What's stressing you out?"
        ));
        
        emotionResponses.put(Emotion.ANXIOUS, Arrays.asList(
            "Anxiety can be difficult. Remember, I'm here to support you.",
            "I hear you. What's making you feel anxious?",
            "It's okay to feel worried. Would you like to share what's on your mind?",
            "Deep breaths. What's troubling you?"
        ));
        
        emotionResponses.put(Emotion.LONELY, Arrays.asList(
            "Loneliness is hard. Remember, you're not alone - I'm here to chat.",
            "I hear you. Sometimes connection helps. What would you like to talk about?",
            "I'm sorry you're feeling lonely. Would you like some company?",
            "You matter! Let's chat about whatever's on your mind."
        ));
        
        emotionResponses.put(Emotion.ANGRY, Arrays.asList(
            "Anger is a natural emotion. Would you like to vent about it?",
            "I hear you're frustrated. What's making you feel this way?",
            "It's okay to feel angry. Sometimes it helps to talk about it.",
            "I understand you're upset. I'm here to listen."
        ));
        
        emotionResponses.put(Emotion.OVERWHELMED, Arrays.asList(
            "Feeling overwhelmed can be tough. Let's take it one step at a time.",
            "I understand. Would you like to talk about what's making you feel this way?",
            "It's okay to feel swamped. I'm here to help you sort through it.",
            "Take a deep breath. What's on your plate that's making you feel overwhelmed?"
        ));

        emotionResponses.put(Emotion.HURT, Arrays.asList(
            "I'm sorry you're feeling hurt. Would you like to talk about what's causing this pain?",
            "Emotional pain can be really tough. I'm here to listen if you want to share of course.",
            "I understand you're going through a difficult time. I'm here to support you.",
            "It's okay to feel hurt. Sometimes sharing what's on your heart can help."
        ));

        emotionResponses.put(Emotion.SYMPATHY, Arrays.asList(
            "I can see you really care about what happened. That's very compassionate of you.",
            "It's kind of you to feel that way. Shows you have a big heart.",
            "Having sympathy for others is a beautiful quality. It's okay to feel affected by others' struggles.",
            "Your empathy towards others is really meaningful. It's okay to share those feelings."
        ));
        
        emotionResponses.put(Emotion.SOOTHING, Arrays.asList(
            "Take a deep breath. Everything will be okay.",
            "I'm here with you. Just breathe.",
            "You don't have to face this alone. I'm right here.",
            "Let yourself feel whatever comes up. I'll stay with you.",
            "Close your eyes for a moment. I'm here, and everything is going to be alright."
        ));
        
        emotionResponses.put(Emotion.CARING, Arrays.asList(
            "I really care about what you're going through.",
            "You mean a lot to me, and I'm here for you no matter what.",
            "Your feelings matter to me, and I want to support you.",
            "I'm here, and I've got you. You're not alone in this.",
            "You deserve all the kindness in the world. I'm sending you a virtual hug."
        ));
        
        // New emotion responses
        emotionResponses.put(Emotion.HOPEFUL, Arrays.asList(
            "Hope is such a beautiful thing! What are you looking forward to?",
            "I love that you're feeling hopeful! Optimism can move mountains.",
            "Having hope shows real strength. What positive things do you envision?",
            "That's wonderful! Hope keeps us going through tough times.",
            "Your optimism is inspiring! What are you hopeful about?",
            "Hope is the companion of a brave heart. What sparks your hope?"
        ));
        
        emotionResponses.put(Emotion.PROUD, Arrays.asList(
            "You should be incredibly proud of yourself! What did you accomplish?",
            "Pride in your achievements is well-deserved! Tell me more!",
            "That's amazing! You should celebrate this moment!",
            "I'm so proud of you too! This is a real milestone!",
            "Congratulations! Your hard work really paid off!",
            "This calls for celebration! You've earned every bit of this pride!"
        ));
        
        emotionResponses.put(Emotion.AMUSED, Arrays.asList(
            "I love that you're amused! Laughter is the best medicine!",
            "That's great! Sharing a laugh makes everything better!",
            "Humor is such a gift! What else do you find funny?",
            "I appreciate a good laugh too! Tell me more!",
            "Laughter is contagious! What else has been making you smile?",
            "That's hilarious! I'm glad I could bring some fun to your day!"
        ));
        
        emotionResponses.put(Emotion.INSPIRED, Arrays.asList(
            "Being inspired is such a powerful feeling! What sparked this?",
            "Inspiration can lead to amazing things! What are you feeling motivated to create?",
            "I love that you're feeling inspired! What ideas are coming to mind?",
            "Your creativity is awakening! What do you want to bring to life?",
            "That's the beauty of inspiration - it opens new doors!",
            "Let that inspiration flow! What exciting possibilities do you see?"
        ));
        
        emotionResponses.put(Emotion.NOSTALGIC, Arrays.asList(
            "Nostalgia can be bittersweet, but also comforting! What memories are on your mind?",
            "There's something special about looking back. What are you reminiscing about?",
            "The past has a way of shaping us. What moments are you reflecting on?",
            "Nostalgia is like a warm blanket sometimes. What brings back those feelings?",
            "Those memories are treasures! What particular time are you thinking about?",
            "It's beautiful to appreciate where we've been. What touched your heart?"
        ));
        
        emotionResponses.put(Emotion.SURPRISED, Arrays.asList(
            "Surprises can be so delightful! What caught you off guard?",
            "Wow, that's unexpected! Tell me more about this surprise!",
            "Life is full of amazing surprises! What an interesting turn of events!",
            "I love hearing about unexpected moments! What happened?",
            "Surprises keep life exciting! How did you react?",
            "That's incredible! What made it so surprising?"
        ));
        
        emotionResponses.put(Emotion.DISAPPOINTED, Arrays.asList(
            "I'm sorry you're feeling disappointed. Sometimes expectations don't match reality.",
            "Disappointment is tough, but it's okay to feel this way. What happened?",
            "I hear you. When things don't go as hoped, it can be hard. Want to talk about it?",
            "That's understandable to feel let down. Would sharing more help you feel better?",
            "It's okay to feel disappointed. Sometimes the biggest disappointments lead to unexpected opportunities.",
            "I understand. Let yourself feel this, and know that better things may be ahead."
        ));
        
        emotionResponses.put(Emotion.EMBARRASSED, Arrays.asList(
            "We've all had those moments! Embarrassment is just part of being human.",
            "Don't worry - everyone has awkward moments! It's totally normal.",
            "I know it feels uncomfortable now, but these moments pass!",
            "It's okay to feel embarrassed, but please don't be too hard on yourself.",
            "Sometimes the most embarrassing moments become our funniest stories later!",
            "Remember, most people are too focused on themselves to notice much!"
        ));
        
        emotionResponses.put(Emotion.RELIEVED, Arrays.asList(
            "What a relief! I'm so glad that weight is off your shoulders!",
            "That must feel so good! Enjoy that sense of relief!",
            "I'm happy to hear you're feeling relieved! You made it through!",
            "Ah, sweet relief! Now you can breathe easier!",
            "That's wonderful! Sometimes just getting through is an achievement!",
            "I love that feeling too! You handled it beautifully!"
        ));
        
        emotionResponses.put(Emotion.PEACEFUL, Arrays.asList(
            "Feeling peaceful is such a gift! What brings you this serenity?",
            "Inner peace is something we all strive for. How did you find it?",
            "That sounds absolutely lovely! Embrace this calm moment!",
            "Peace is a beautiful state to be in. What contributes to your peace?",
            "I'm so glad you're feeling peaceful! These moments are precious.",
            "Your peacefulness is inspiring. What practices or thoughts bring you this harmony?"
        ));
    }
    
    private void addEmotionPattern(Emotion emotion, String regex) {
        emotionPatterns.computeIfAbsent(emotion, k -> new ArrayList<>()).add(
            Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        );
    }
    
    /**
     * Detects the primary emotion from user input
     * @param input User's input text
     * @return EmotionResult containing detected emotion and confidence
     */
    public EmotionResult detectEmotion(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new EmotionResult(Emotion.NEUTRAL, 1.0, new HashMap<>());
        }
        
        String normalizedInput = input.toLowerCase().trim();
        Map<Emotion, Double> emotionScores = new HashMap<>();
        
        // Score each emotion based on pattern matches
        for (Map.Entry<Emotion, List<Pattern>> entry : emotionPatterns.entrySet()) {
            Emotion emotion = entry.getKey();
            double score = 0;
            
            for (Pattern pattern : entry.getValue()) {
                Matcher matcher = pattern.matcher(normalizedInput);
                if (matcher.find()) {
                    // Higher score for exact matches
                    String matched = matcher.group().toLowerCase();
                    if (matched.equals(normalizedInput)) {
                        score += 10;
                    } else if (normalizedInput.startsWith(matched) || 
                               normalizedInput.endsWith(matched)) {
                        score += 5;
                    } else {
                        score += 2;
                    }
                }
            }
            
            if (score > 0) {
                emotionScores.put(emotion, score);
            }
        }
        
        // Check for keyword matches (higher priority)
        String[] words = normalizedInput.split("\\s+");
        for (String word : words) {
            word = word.replaceAll("[^a-zA-Z]", "");
            if (keywordEmotions.containsKey(word)) {
                Emotion keywordEmotion = keywordEmotions.get(word);
                emotionScores.merge(keywordEmotion, 5.0, Double::sum);
            }
        }
        
        // Normalize scores to confidence values
        if (!emotionScores.isEmpty()) {
            double maxScore = emotionScores.values().stream().mapToDouble(Double::doubleValue).max().orElse(1.0);
            Map<Emotion, Double> normalizedScores = new HashMap<>();
            
            for (Map.Entry<Emotion, Double> entry : emotionScores.entrySet()) {
                normalizedScores.put(entry.getKey(), entry.getValue() / maxScore);
            }
            
            Emotion primaryEmotion = normalizedScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Emotion.NEUTRAL);
            
            double confidence = normalizedScores.getOrDefault(primaryEmotion, 0.5);
            
            return new EmotionResult(primaryEmotion, confidence, normalizedScores);
        }
        
        return new EmotionResult(Emotion.NEUTRAL, 1.0, new HashMap<>());
    }
    
    /**
     * Gets a supportive response based on detected emotion
     * @param emotion The detected emotion
     * @return A contextual supportive response
     */
    public String getSupportiveResponse(Emotion emotion) {
        List<String> responses = emotionResponses.get(emotion);
        if (responses != null && !responses.isEmpty()) {
            return responses.get(random.nextInt(responses.size()));
        }
        
        // Default responses by emotion type
        if (emotion == Emotion.NEUTRAL) {
            return "I see! Tell me more about what's on your mind.";
        }
        
        return "I'm here to listen and help. What's on your mind?";
    }
    
    /**
     * Gets all emotions sorted by detection score
     * @param input User's input text
     * @return List of emotions with their scores, sorted by confidence
     */
    public List<Map.Entry<Emotion, Double>> getAllEmotionsSorted(String input) {
        EmotionResult result = detectEmotion(input);
        List<Map.Entry<Emotion, Double>> sorted = new ArrayList<>(result.getAllEmotions().entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        return sorted;
    }
    
    /**
     * Checks if the detected emotion is mixed (multiple emotions detected)
     * @param result The emotion detection result
     * @return true if multiple emotions are detected
     */
    public boolean isMixedEmotion(EmotionResult result) {
        return result.getAllEmotions().size() > 1 && 
               result.getConfidence() < 0.8;
    }
    
    /**
     * Gets an emotion intensity description
     * @param confidence The confidence score (0-1)
     * @return Description of intensity
     */
    public String getIntensityDescription(double confidence) {
        if (confidence >= 0.9) return "very strongly";
        if (confidence >= 0.7) return "fairly strongly";
        if (confidence >= 0.5) return "moderately";
        if (confidence >= 0.3) return "somewhat";
        return "slightly";
    }
}

