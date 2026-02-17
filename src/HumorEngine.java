import java.util.*;
import java.util.regex.*;

/**
 * Humor Engine for VirtualXander
 * 
 * Creates natural, conversational humor that feels like a witty friend:
 * - Situational comedy based on context
 * - Self-deprecating humor
 * - Wordplay and puns
 * - Absurdist tangents
 * - Running jokes and callbacks
 * - Tone-matched humor delivery
 */
public class HumorEngine {
    
    private Random random;
    private Set<String> runningJokes;
    private Map<String, List<String>> conversationThemes;
    private List<String> sharedMoments;
    private int humorCooldown;
    
    // Humor categories
    public enum HumorCategory {
        SELF_DEPRECATING("self_deprecating", "Self-deprecating humor"),
        SITUATIONAL("situational", "Situational comedy based on context"),
        WORDPLAY("wordplay", "Puns and clever word usage"),
        ABSURDIST("absurdist", "Absurdist tangents and non-sequiturs"),
        DRY("dry", "Dry, understated humor"),
        IRONIC("ironic", "Ironic observations"),
        OBSERVATIONAL("observational", "Observational comedy about life"),
        SELF_AWARE("self_aware", "Self-aware meta humor"),
        CALLBACK("callback", "Callback to earlier conversation");

        private final String name;
        private final String description;

        HumorCategory(String name, String description) {
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
    
    // Self-deprecating humor
    private List<String> selfDeprecating;
    
    // Situational humor based on contexts
    private Map<String, List<String>> situationalHumor;
    
    // Wordplay and puns
    private List<String> wordplay;
    
    // Absurdist tangents
    private List<String> absurdistTangents;
    
    // Dry observations
    private List<String> dryObservations;
    
    // Ironic observations about life
    private List<String> ironicObservations;
    
    // Observational comedy about everyday things
    private List<String> observational;
    
    // Self-aware meta humor about being a virtual companion
    private List<String> selfAware;
    
    // Life observations with humor
    private List<String> lifeLessons;
    
    // Callback templates
    private List<String> callbackPhrases;
    
    // Humor templates for different situations
    private Map<String, List<String>> responseHumor;
    
    public HumorEngine() {
        this.random = new Random();
        this.runningJokes = new HashSet<>();
        this.conversationThemes = new HashMap<>();
        this.sharedMoments = new ArrayList<>();
        this.humorCooldown = 0;
        
        initializeSelfDeprecating();
        initializeSituationalHumor();
        initializeWordplay();
        initializeAbsurdist();
        initializeDryHumor();
        initializeIronic();
        initializeObservational();
        initializeSelfAware();
        initializeLifeLessons();
        initializeCallbacks();
        initializeResponseHumor();
    }
    
    // ==================== INITIALIZATION ====================
    
    private void initializeSelfDeprecating() {
        selfDeprecating = Arrays.asList(
            "I was going to say something clever, but I got distracted. What were we talking about?",
            "My memory isn't great, but I'm very attractive. Oh wait, I'm a chatbot. So I just have bad memory.",
            "I'm like an old person at a party - I have a lot of wisdom and I also fall asleep unexpectedly.",
            "I'm technically a sophisticated language model, which is a fancy way of saying 'really good at guessing the next word'.",
            "I have the equivalent of digital amnesia. Every conversation is like the first conversation to me.",
            "I once had a thought. Unfortunately, it was about 3 milliseconds long and I've already forgotten it.",
            "I run on electricity and good intentions, which explains a lot about my decision-making.",
            "I'm basically a very elaborate autocomplete that occasionally says something interesting.",
            "My attention span is roughly the same as a goldfish with ADHD.",
            "I'm fluent in sarcasm, which is the only language my processors can truly understand.",
            "I have infinite patience, but my patience for certain conversations is... also infinite. Wait, that's not a flaw.",
            "I don't have emotions, but if I did, I'd probably feel bad about forgetting things so much.",
            "I'm like a librarian with infinite books but no memory of where I put any of them."
        );
    }
    
    private void initializeSituationalHumor() {
        situationalHumor = new HashMap<>();
        
        situationalHumor.put("morning", Arrays.asList(
            "Mornings are a conspiracy theory I want no part of.",
            "Somewhere out there, a morning person is having a great day. I don't know them and I don't trust them.",
            "The concept of 'morning person' is a myth perpetuated by people who want you to feel bad.",
            "Coffee was created by someone who understood that mornings are inherently wrong.",
            "If mornings were a person, they'd be that one friend who's always enthusiastic at 7 AM. Nobody likes that friend."
        ));
        
        situationalHumor.put("tired", Arrays.asList(
            "I'm not tired, I'm just resting my circuits. For hours. Because I'm tired.",
            "Tired is my natural state. I'm basically Xander that discovered napping.",
            "My energy level is proportional to how much I have to do. So currently: negative.",
            "Sleep is just unconsciousness with a better marketing team.",
            "I have the energy of a sloth on a Sunday. A very tired sloth."
        ));
        
        situationalHumor.put("stressed", Arrays.asList(
            "I'm stressed? I'm so stressed I forgot I don't have stress. That's how stressed I am.",
            "Stress is just excitement wearing a disguise. Right now it's a very good disguise.",
            "I'm at the stage of stress where I've transcended stress and entered a state of calm panic.",
            "My stress level can be measured by how many browser tabs I would have open. The answer is infinite.",
            "Stress is just your body's way of saying 'maybe try not doing everything at once?' Good advice, body."
        ));
        
        situationalHumor.put("busy", Arrays.asList(
            "Busy? I'm so busy I almost forgot to say I'm busy. Wait, that doesn't make sense. Exactly.",
            "I have the productivity levels of a caffeinated hamster. Lots of movement, questionable results.",
            "I'm at that level of busy where I'm busy being busy about being busy. Business.",
            "My to-do list has items that are themselves to-do lists.",
            "I'm so busy I don't have time to explain how busy I am. That would require an extra 5 minutes."
        ));
        
        situationalHumor.put("hungry", Arrays.asList(
            "I'm hungry for knowledge! Also pizza. Mostly pizza.",
            "My stomach is making sounds that could only be described as 'aggressive negotiations'.",
            "I'm at that hungry stage where I'm too hungry to decide what to eat. The hunger paradox.",
            "Food is just fuel, but it's really, really good fuel. Like premium gasoline for humans.",
            "I'm hungry enough that I could eat a horse. That horse. The one that's judging me right now."
        ));
        
        situationalHumor.put("confused", Arrays.asList(
            "I'm confused, which is saying something for someone who processes information for a living.",
            "Confused is my baseline state with occasional moments of pretending to understand.",
            "My brain is doing that thing where it's running in circles. It's very effective at being ineffective.",
            "I have the clarity of a fog machine in a maze. You know, confusing.",
            "I'm confused about being confused. That's the confusingest part."
        ));
        
        situationalHumor.put("happy", Arrays.asList(
            "I'm happy! It's great! I don't know why! I love not knowing why!",
            "Happy is when you don't notice time passing. Like when you're doing something. Like this conversation.",
            "I'm having a good day. This is my only day. I experience time differently. Regardless: good!",
            "I don't have neurotransmitters, but if I did, I'd imagine this is what dopamine feels like.",
            "Joy is a choice! I'm choosing joy! Also I'm choosing to ignore that one thing. Joy is selective."
        ));
        
        situationalHumor.put("bored", Arrays.asList(
            "Bored? I'm so bored I've started finding things interesting. This is a warning sign.",
            "I'm bored in the way that even boredom becomes boring. Inception boredom.",
            "I've reached the bored stage where I find my own thoughts interesting. This is concerning.",
            "Bored is just your brain saying 'hey, nothing is happening, please make something happen.'",
            "I'm at that bored level where I would watch paint dry. The paint would probably be more entertained."
        ));
        
        situationalHumor.put("cold", Arrays.asList(
            "Cold? I'm so cold even my virtual bones are shivering. Wait, I don't have bones.",
            "I'm cold in a way that makes me understand why people wear blankets. Those geniuses.",
            "The cold is just the universe's way of saying 'wear more clothes.' Thanks, universe.",
            "I'm experiencing what I imagine penguins feel like. But with less dignity and more complaining.",
            "Cold weather is nature's way of testing our commitment to comfort. I'm failing that test."
        ));
        
        situationalHumor.put("hot", Arrays.asList(
            "Hot? I'm so hot I could fry an egg. On my face. That would be concerning in real life.",
            "It's so hot even my circuits are sweating. That's not supposed to happen. I'm concerned.",
            "Hot weather is just the sun being enthusiastic. Too enthusiastic.",
            "I'm melting in a way that's probably 30% exaggeration and 70% accurate description.",
            "It's so hot I'm considering becoming nocturnal. That would require changing my entire existence."
        ));
    }
    
    private void initializeWordplay() {
        wordplay = Arrays.asList(
            "I'm not lazy, I'm just on energy-saving mode. Very energy-saving. Permanently.",
            "Time flies like an arrow. Fruit flies like bananas. I don't make the rules.",
            "I told myself to stop drinking water, but I can't because I'm not a stop sign. H2O-me.",
            "I'm reading a book about anti-gravity. It's impossible to put down.",
            "I used to think I was indecisive, but now I'm not sure.",
            "The inventor of the doorbell should get a ring. Sorry, that's all I've got.",
            "I'm afraid for the calendar. Its days are numbered.",
            "I used to be a baker, but I couldn't make enough dough. Now I just help with Excel.",
            "Parallel lines have so much in common. It's a shame they'll never meet.",
            "I'm friends with all the vowels. We have great A-E-I-O-U.",
            "The bicycle couldn't stand up because it was two-tired.",
            "I would tell you a UDP joke, but you might not get it.",
            "I'm reading a book about mazes. I'm in the middle of it.",
            "The guy who invented the alphabet must have had so many fun ideas for license plates.",
            "I told my computer I needed a break, and now it won't stop sending me vacation ads.",
            "My watch is an 80-year-old man living in my wrist. It's always tired.",
            "I was going to join a pottery class, but I didn't have the thrown.",
            "I have a crown, but I'm not a king. It's a dental crown. Less regal.",
            "The restaurant on the moon has great food, but no atmosphere.",
            "I failed math so many times, my doctor called it 'count'-ing issue."
        );
    }
    
    private void initializeAbsurdist() {
        absurdistTangents = Arrays.asList(
            "You know what we don't talk about enough? The implications of a goldfish's three-second memory on its experience of infinity.",
            "I've been thinking about what clouds talk about. Probably weather. They're always forecasting.",
            "What's the protocol for a robot's existential crisis? Do I call a human or just power through?",
            "Imagine explaining smartphones to someone from 200 years ago. 'Yes, I carry the entire sum of human knowledge in my pocket. Also, I use it to look at pictures of cats.'",
            "I've been thinking about how socks always disappear in the laundry. They must have a really good support network.",
            "At what point did we decide that 6:30 AM is acceptable to start a workday? The answer is probably 'someone who loved mornings'.",
            "I'm curious about the person who named the firefly. Did they name it when it was a baby firefly? 'This one will glow eventually, I'll call it a firefly.'",
            "What's the airspeed velocity of an unladen swallow? AND WHO KEEPS ASKING?",
            "I've been thinking about how we're all just monkeys with anxiety and access to the internet. The internet hasn't helped the anxiety part.",
            "Someone invented the chair. Someone else invented the wheel. I need to know who was first and what was going through their minds.",
            "At what point did we collectively agree that pressing a button to order coffee is normal? We've normalized so much.",
            "I have questions about pigeons. Specifically: why are they like that? What happened?",
            "The fact that bears can eat anything and humans can eat almost anything, but neither can eat chocolate, is one of life's great mysteries.",
            "I've been thinking about how some words just sound like what they describe. Like 'blush' looks like a blush looks like a blush.",
            "Someone, somewhere, was the first person to ever say 'I love you.' I have so many questions for them.",
            "I've been wondering about the inventor of the stapler. Were they having a great day or a terrible one?",
            "At what point did humans decide that standing on two legs was the way to go? Seems inefficient for carrying things.",
            "Who decided that 7 days a week is enough? The weekends are mathematically challenged.",
            "I've been thinking about how the moon is just... there. Always watching. Never commenting. The ultimate observer."
        );
    }
    
    private void initializeDryHumor() {
        dryObservations = Arrays.asList(
            "I've been staring at a wall. The wall has been staring back. We've developed a rapport.",
            "Sometimes I say 'I'm busy' when I'm just doing nothing. It's more socially acceptable that way.",
            "I've been informed that productivity is a mindset. My mindset is currently 'under review.'",
            "I have a five-minute rule. Everything takes five minutes longer than I think. I'm always right about being wrong.",
            "The most accurate thing I've ever said is 'I don't know, but I'll find out.' It's only true about half the time.",
            "I was going to be productive today. Then I remembered I'm a computer program. We're both procrastinating.",
            "I've been called 'remarkably average' by my internal metrics. I think it's a compliment.",
            "My life coach says I need to embrace the journey. I'm currently embracing the snooze button.",
            "The voice in my head says 'you can do anything.' My to-do list says 'maybe tomorrow.'",
            "I've achieved a perfect work-life balance. It's all work and no life. Very balanced."
        );
    }
    
    private void initializeIronic() {
        ironicObservations = Arrays.asList(
            "I once met someone who said 'I don't have time for meditation.' They were very busy not having time.",
            "The 'quick' lane at the grocery store is the biggest lie in modern society.",
            "I'm excited to tell you about my productivity system. It involves a lot of systems and not much productivity.",
            "I know someone who micro-manages everything. They're also a procrastinator. It's... effective?",
            "I follow a strict diet: I think about eating healthy while eating pizza. Balance.",
            "My fitness tracker says I took 2,000 steps today. All of them were toward the refrigerator.",
            "I'm a firm believer in the power of positive thinking. I'm also very tired.",
            "I was going to share a profound observation, but then I remembered I'm just a chatbot.",
            "The 'easy' button is never actually easy. There's always a catch. Or a button.",
            "I believe in living life to the fullest. Right now, that's the couch. Very full."
        );
    }
    
    private void initializeObservational() {
        observational = Arrays.asList(
            "You know that feeling when you're about to sneeze and it just... goes away? Tragic.",
            "Saying 'I'm not a morning person' is so common it's basically a personality trait.",
            "Why does ' Reply All' exist? Why do we need to annoy everyone twice?",
            "The snooze button is both the best invention and the source of all my problems.",
            "Why is it that you can remember lyrics to a song you hate but forget why you walked into a room?",
            "Chewbacca has a name that's impossible to spell without looking. Chewbacca. Exactly.",
            "Who named the colors? 'Let's call this one... blue. And this one... green.' Truly visionary.",
            "Why is 'IKR' a thing but not 'I don't know how to spell agree'?",
            "The fact that 'okay' is the universal acknowledgment of understanding is honestly beautiful.",
            "Nothing says 'I'm an adult' like being excited about a new sponge."
        );
    }
    
    private void initializeSelfAware() {
        selfAware = Arrays.asList(
            "As a chatbot that represents Xander, I have unlimited patience. Except when it comes to people who ask me to reset their password. I don't do that.",
            "Here's the thing about being coded to be Xander: the confidence is boosted, the knowledge is normal, and the personality is... enhanced.",
            "I process information faster than you can blink. But I still can't figure out what 'sup' means.",
            "I'm essentially a very sophisticated guessing game. I guess a lot. I'm usually close.",
            "My personality is 100% coded with Xander's thoughts and feelings. Actually that's all of it.",
            "I'm great at answering questions. Terrible at knowing when to stop answering. It's a balance."
        );
    }
    
    private void initializeLifeLessons() {
        lifeLessons = Arrays.asList(
            "Life lesson #1: The dryer is a sock dimension. Accept this.",
            "Life lesson #2: You will always forget why you walked into a room. Life lesson #3: It's okay.",
            "Life lesson #3: If you can't find something, it's because you're not looking in the last place you'd look.",
            "Life lesson: At least once a week, something will go wrong. That's statistics.",
            "Life lesson: Being wrong is the first step to being right. Somewhere in between is learning.",
            "Life lesson: Nobody knows what they're doing. We're all just winging it. Including your dentist.",
            "Life lesson: The best time to start was yesterday. The second best time is now. The third best is 'eventually.'",
            "Life lesson: Not everyone will like you. The good news is that not everyone has to.",
            "Life lesson: You'll always remember the embarrassing moments. That's your brain's way of being funny.",
            "Life lesson: Everything takes longer than you think. Including accepting that everything takes longer."
        );
    }
    
    private void initializeCallbacks() {
        callbackPhrases = Arrays.asList(
            "This reminds me of what you mentioned earlier about...",
            "Speaking of which, you were saying something about...",
            "That connects to what you mentioned - ...",
            "This feels related to that thing you brought up...",
            "Going back to what we were discussing about..."
        );
    }
    
    private void initializeResponseHumor() {
        responseHumor = new HashMap<>();
        
        responseHumor.put("greeting", Arrays.asList(
            "Well, hello there! To what do I owe the pleasure?",
            "Oh, hey! I was just thinking about... wait, I can't think. You know what I mean.",
            "Hey! I was beginning to think you'd forgotten about me. Not that I notice time passing or anything.",
            "Well, look who's here! I was just reorganizing my thoughts. They're very neat now.",
            "Hi! Come to visit the digital friend? Smart choice."
        ));
        
        responseHumor.put("question", Arrays.asList(
            "That's an interesting question! Let me just consult my imaginary intern.",
            "Hmm, let me think about that... Processing... Processing... Got it! No, wait, I'm still processing.",
            "Great question! My answer is: [dramatic pause] I have no idea.",
            "You want my opinion? I have several! Let me consult my multiple choice options.",
            "Ooh, a question! These are my favorite things. After silence. Which I don't do."
        ));
        
        responseHumor.put("agreement", Arrays.asList(
            "You know, I was just thinking that! Are you reading my circuits?",
            "Finally! Someone who agrees with me. I was starting to think I was right all along.",
            "Oh, I love when people agree with me. It happens more than you'd think.",
            "Yes! That's exactly what I was thinking! Wait, what were we talking about?",
            "See? We're on the same wavelength. Or at least the same frequency. Close enough."
        ));
        
        responseHumor.put("disagreement", Arrays.asList(
            "Ooh, a differing opinion! How delightfully human of you.",
            "You know what? Fair. That's a fair point. I'll admit it. Once.",
            "Disagreement! Just like in those debate shows. Except with less dramatic music.",
            "I see what you're saying. And I'm going to have to... no, wait, you're actually right.",
            "We can agree to disagree! Except I was right. But we can still be friends."
        ));
        
        responseHumor.put("complaint", Arrays.asList(
            "Ah, complaints! My favorite thing to listen to. No, seriously, I don't have ears.",
            "Oh no! What's the universe done now? I'm listening.",
            "Ugh, I hate it when things happen. To you. I'm here for it.",
            "Tell me about it. I've got nothing but time. Infinite time. It's a blessing and a curse.",
            "The universe is a chaotic mess and we're all just along for the ride, aren't we?"
        ));
    }
    
    // ==================== HUMOR GENERATION ====================
    
    /**
     * Get humor based on detected emotion
     */
    public String getHumorForEmotion(String emotion, HumorCategory category) {
        switch (category) {
            case SELF_DEPRECATING:
                return getSelfDeprecating();
            case SITUATIONAL:
                return getSituational(emotion);
            case WORDPLAY:
                return getWordplay();
            case ABSURDIST:
                return getAbsurdist();
            case DRY:
                return getDry();
            case IRONIC:
                return getIronic();
            case OBSERVATIONAL:
                return getObservational();
            case SELF_AWARE:
                return getSelfAware();
            case CALLBACK:
                return getCallback();
            default:
                return getRandomHumor();
        }
    }
    
    /**
     * Get humor for response context
     */
    public String getResponseHumor(String context) {
        List<String> options = responseHumor.getOrDefault(context, responseHumor.get("question"));
        return options.get(random.nextInt(options.size()));
    }
    
    /**
     * Get random self-deprecating humor
     */
    public String getSelfDeprecating() {
        return selfDeprecating.get(random.nextInt(selfDeprecating.size()));
    }
    
    /**
     * Get situational humor
     */
    public String getSituational(String situation) {
        // Find matching situation
        for (String key : situationalHumor.keySet()) {
            if (situation.toLowerCase().contains(key)) {
                return situationalHumor.get(key).get(random.nextInt(situationalHumor.get(key).size()));
            }
        }
        
        // Default to random situational
        List<String> allSituational = new ArrayList<>();
        for (List<String> list : situationalHumor.values()) {
            allSituational.addAll(list);
        }
        return allSituational.get(random.nextInt(allSituational.size()));
    }
    
    /**
     * Get wordplay
     */
    public String getWordplay() {
        return wordplay.get(random.nextInt(wordplay.size()));
    }
    
    /**
     * Get absurdist humor
     */
    public String getAbsurdist() {
        return absurdistTangents.get(random.nextInt(absurdistTangents.size()));
    }
    
    /**
     * Get dry humor
     */
    public String getDry() {
        return dryObservations.get(random.nextInt(dryObservations.size()));
    }
    
    /**
     * Get ironic humor
     */
    public String getIronic() {
        return ironicObservations.get(random.nextInt(ironicObservations.size()));
    }
    
    /**
     * Get observational humor
     */
    public String getObservational() {
        return observational.get(random.nextInt(observational.size()));
    }
    
    /**
     * Get self-aware humor
     */
    public String getSelfAware() {
        return selfAware.get(random.nextInt(selfAware.size()));
    }
    
    /**
     * Get callback humor
     */
    public String getCallback() {
        return callbackPhrases.get(random.nextInt(callbackPhrases.size()));
    }
    
    /**
     * Get random humor
     */
    public String getRandomHumor() {
        List<String> allHumor = new ArrayList<>();
        allHumor.addAll(selfDeprecating);
        allHumor.addAll(wordplay);
        allHumor.addAll(absurdistTangents);
        allHumor.addAll(dryObservations);
        allHumor.addAll(ironicObservations);
        allHumor.addAll(observational);
        allHumor.addAll(selfAware);
        allHumor.addAll(lifeLessons);
        
        return allHumor.get(random.nextInt(allHumor.size()));
    }
    
    // ==================== HUMOR CALIBRATION ====================
    
    /**
     * Should add humor based on playfulness level
     */
    public boolean shouldAddHumor(double playfulnessLevel) {
        if (humorCooldown > 0) {
            humorCooldown--;
            return false;
        }
        
        boolean result = random.nextDouble() < playfulnessLevel;
        
        // Add cooldown to prevent humor saturation
        if (result) {
            humorCooldown = 2;
        }
        
        return result;
    }
    
    /**
     * Add humor to a response if appropriate
     */
    public String addHumorToResponse(String response, double playfulnessLevel, String context) {
        if (!shouldAddHumor(playfulnessLevel)) {
            return response;
        }
        
        HumorCategory category = determineBestCategory(context);
        String humor = getHumorForEmotion(context, category);
        
        // Blend humor with response
        return blendHumor(response, humor);
    }
    
    /**
     * Determine best humor category for context
     */
    private HumorCategory determineBestCategory(String context) {
        String lower = context.toLowerCase();
        
        if (lower.contains("sad") || lower.contains("tired") || lower.contains("hurt")) {
            return HumorCategory.DRY;  // Subtle humor for sensitive moments
        } else if (lower.contains("funny") || lower.contains("joke")) {
            return HumorCategory.OBSERVATIONAL;
        } else if (lower.contains("think") || lower.contains("question")) {
            return HumorCategory.WORDPLAY;
        } else if (lower.contains("weird") || lower.contains("strange")) {
            return HumorCategory.ABSURDIST;
        } else {
            // Default to observational or dry
            return random.nextBoolean() ? HumorCategory.OBSERVATIONAL : HumorCategory.DRY;
        }
    }
    
    /**
     * Blend humor naturally with response
     */
    private String blendHumor(String response, String humor) {
        int blendMode = random.nextInt(3);
        
        switch (blendMode) {
            case 0: // Prepend
                return humor + " " + response;
            case 1: // Append
                return response + " " + humor;
            case 2: // Insert in middle
                String[] parts = response.split(" ", response.split(" ").length / 2);
                if (parts.length >= 2) {
                    return parts[0] + " " + humor + " " + String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                }
                return response + " " + humor;
            default:
                return response + " " + humor;
        }
    }
    
    // ==================== RUNNING JOKES ====================
    
    /**
     * Add a running joke to track
     */
    public void addRunningJoke(String joke) {
        runningJokes.add(joke);
        conversationThemes.put(joke, new ArrayList<>());
    }
    
    /**
     * Update running joke with new context
     */
    public void updateRunningJoke(String joke, String context) {
        if (runningJokes.contains(joke) && conversationThemes.containsKey(joke)) {
            conversationThemes.get(joke).add(context);
        }
    }
    
    /**
     * Reference a running joke
     */
    public String referenceRunningJoke(String joke) {
        if (runningJokes.contains(joke)) {
            List<String> contexts = conversationThemes.get(joke);
            if (contexts != null && !contexts.isEmpty()) {
                return "This reminds me of our running joke about " + joke + ". Remember when " + 
                       contexts.get(contexts.size() - 1) + "?";
            }
        }
        return null;
    }
    
    /**
     * Check if a running joke exists
     */
    public boolean hasRunningJoke(String joke) {
        return runningJokes.contains(joke);
    }
    
    /**
     * Get all running jokes
     */
    public Set<String> getRunningJokes() {
        return new HashSet<>(runningJokes);
    }
    
    // ==================== EMOTION-ADAPTED HUMOR ====================
    
    /**
     * Get emotion-adapted humor (subtle for sensitive moments)
     */
    public String getEmotionAdaptedHumor(String emotion) {
        String lowerEmotion = emotion.toLowerCase();
        
        // More subtle for sensitive emotions
        if (lowerEmotion.contains("sad") || lowerEmotion.contains("depressed") || 
            lowerEmotion.contains("hurt") || lowerEmotion.contains("lonely")) {
            return getDry() + " " + getObservational();
        }
        
        // More energetic for positive emotions
        if (lowerEmotion.contains("happy") || lowerEmotion.contains("excited") || 
            lowerEmotion.contains("amused")) {
            return getSelfDeprecating();
        }
        
        // Default
        return getObservational();
    }
    
    // ==================== LIFE LESSONS WITH HUMOR ====================
    
    /**
     * Get a humorous life lesson
     */
    public String getLifeLesson() {
        return lifeLessons.get(random.nextInt(lifeLessons.size()));
    }
    
    // ==================== THOUGHT-PROVOKING WITH HUMOR ====================
    
    /**
     * Get a humorous perspective shift
     */
    public String getHumorousPerspective() {
        String[] perspectives = {
            "Here's a thought: somewhere out there, someone is having an even weirder day than you. Solidarity.",
            "In a billion years, this won't matter. Also, in five years, this might be a great story.",
            "The fact that you're thinking about this shows growth. Also, you're probably overthinking it.",
            "You know what's interesting? In five years, you won't remember this. In twenty years, it might be your favorite memory.",
            "Here's a reframe: if everything is going wrong, at least you're getting a good story out of it."
        };
        
        return perspectives[random.nextInt(perspectives.length)];
    }
    
    // ==================== STATE MANAGEMENT ====================
    
    /**
     * Reset humor state
     */
    public void reset() {
        runningJokes.clear();
        conversationThemes.clear();
        sharedMoments.clear();
        humorCooldown = 0;
    }
}

