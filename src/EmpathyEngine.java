import java.util.*;

/**
 * Empathy Engine for VirtualXander
 * 
 * Provides Emotional Mirror features that reflect, validate, and normalize
 * user emotions with nuanced understanding and empathetic responses.
 */
public class EmpathyEngine {
    
    private Random random;
    private EmotionDetector emotionDetector;
    private Map<EmotionDetector.Emotion, String> intensityModifiers;
    private Map<EmotionDetector.Emotion, List<String>> emotionReflections;
    private Map<EmotionDetector.Emotion, List<String>> validationResponses;
    private Map<EmotionDetector.Emotion, List<String>> normalizationStatements;
    
    // Emotional Vocabulary - Nuanced feeling descriptions
    private Map<EmotionDetector.Emotion, Map<String, String>> nuancedFeelingDescriptions;
    // Emotional Vocabulary - Feeling words library
    private Map<EmotionDetector.Emotion, List<String>> feelingWordsLibrary;
    // Emotional Vocabulary - Emotional granularity (intensity levels)
    private Map<EmotionDetector.Emotion, List<String>> emotionalGranularity;
    
    // Supportive Presence - "I'm here" messaging, physical space, time acknowledgments
    private Map<String, List<String>> imHereMessages;
    private Map<String, List<String>> physicalSpaceAcknowledgments;
    private Map<String, List<String>> timeAcknowledgments;
    
    public EmpathyEngine() {
        this.random = new Random();
        this.emotionDetector = new EmotionDetector();
        initializeAllMappings();
    }
    
    public EmpathyEngine(EmotionDetector detector) {
        this.random = new Random();
        this.emotionDetector = detector;
        initializeAllMappings();
    }
    
    // ==================== INNER CLASSES ====================
    
    public static class EmotionalMirrorResponse {
        private String reflectedEmotion;
        private String validationMessage;
        private String normalizationMessage;
        private EmotionDetector.Emotion primaryEmotion;
        private double confidence;
        private boolean isValid;
        private List<String> suggestedQuestions;
        
        public EmotionalMirrorResponse() {
            this.suggestedQuestions = new ArrayList<>();
            this.isValid = false;
        }
        
        public String getReflectedEmotion() { return reflectedEmotion; }
        public void setReflectedEmotion(String r) { this.reflectedEmotion = r; }
        public String getValidationMessage() { return validationMessage; }
        public void setValidationMessage(String v) { this.validationMessage = v; }
        public String getNormalizationMessage() { return normalizationMessage; }
        public void setNormalizationMessage(String n) { this.normalizationMessage = n; }
        public EmotionDetector.Emotion getPrimaryEmotion() { return primaryEmotion; }
        public void setPrimaryEmotion(EmotionDetector.Emotion e) { this.primaryEmotion = e; }
        public double getConfidence() { return confidence; }
        public void setConfidence(double c) { this.confidence = c; }
        public boolean isValid() { return isValid; }
        public void setValid(boolean v) { this.isValid = v; }
        public List<String> getSuggestedQuestions() { return new ArrayList<>(suggestedQuestions); }
        public void addSuggestedQuestion(String q) { this.suggestedQuestions.add(q); }
        
        public String getFullResponse() {
            StringBuilder sb = new StringBuilder();
            sb.append("I hear you feeling ").append(reflectedEmotion).append(". ");
            sb.append(validationMessage).append(" ").append(normalizationMessage);
            return sb.toString();
        }
    }
    
    public static class EmotionValidation {
        private EmotionDetector.Emotion emotion;
        private boolean isValidEmotion;
        private String validationMessage;
        private String validationType;
        private double validationConfidence;
        
        public EmotionValidation(EmotionDetector.Emotion e, boolean v, String m, String t, double c) {
            this.emotion = e;
            this.isValidEmotion = v;
            this.validationMessage = m;
            this.validationType = t;
            this.validationConfidence = c;
        }
        
        public EmotionDetector.Emotion getEmotion() { return emotion; }
        public boolean isValidEmotion() { return isValidEmotion; }
        public String getValidationMessage() { return validationMessage; }
        public String getValidationType() { return validationType; }
        public double getValidationConfidence() { return validationConfidence; }
    }
    
    public static class EmotionNormalization {
        private EmotionDetector.Emotion emotion;
        private String normalizedStatement;
        private String perspective;
        private boolean isNormal;
        
        public EmotionNormalization() {}
        
        public EmotionDetector.Emotion getEmotion() { return emotion; }
        public void setEmotion(EmotionDetector.Emotion e) { this.emotion = e; }
        public String getNormalizedStatement() { return normalizedStatement; }
        public void setNormalizedStatement(String s) { this.normalizedStatement = s; }
        public String getPerspective() { return perspective; }
        public void setPerspective(String p) { this.perspective = p; }
        public boolean isNormal() { return isNormal; }
        public void setNormal(boolean n) { this.isNormal = n; }
    }
    
    /**
     * Emotional Vocabulary - Contains nuanced feeling descriptions,
     * feeling words library, and emotional granularity for better
     * emotional understanding and expression.
     */
    public static class EmotionalVocabulary {
        private EmotionDetector.Emotion emotion;
        private String feelingWord;
        private String description;
        private double intensity;
        
        public EmotionalVocabulary() {}
        
        public EmotionalVocabulary(EmotionDetector.Emotion emotion, String word, String desc, double intensity) {
            this.emotion = emotion;
            this.feelingWord = word;
            this.description = desc;
            this.intensity = intensity;
        }
        
        public EmotionDetector.Emotion getEmotion() { return emotion; }
        public void setEmotion(EmotionDetector.Emotion e) { this.emotion = e; }
        public String getFeelingWord() { return feelingWord; }
        public void setFeelingWord(String w) { this.feelingWord = w; }
        public String getDescription() { return description; }
        public void setDescription(String d) { this.description = d; }
        public double getIntensity() { return intensity; }
        public void setIntensity(double i) { this.intensity = i; }
    }
    
    // ==================== INITIALIZATION ====================
    
    private void initializeAllMappings() {
        initializeIntensityModifiers();
        initializeEmotionReflections();
        initializeValidationResponses();
        initializeNormalizationStatements();
        // Emotional Vocabulary initialization
        initializeNuancedFeelingDescriptions();
        initializeFeelingWordsLibrary();
        initializeEmotionGranularity();
        // Supportive Presence initialization
        initializeImHereMessages();
        initializePhysicalSpaceAcknowledgments();
        initializeTimeAcknowledgments();
    }
    
    private void initializeIntensityModifiers() {
        intensityModifiers = new HashMap<>();
        intensityModifiers.put(EmotionDetector.Emotion.HAPPY, "genuinely happy");
        intensityModifiers.put(EmotionDetector.Emotion.EXCITED, "really excited");
        intensityModifiers.put(EmotionDetector.Emotion.MOTIVATED, "highly motivated");
        intensityModifiers.put(EmotionDetector.Emotion.GRATEFUL, "deeply grateful");
        intensityModifiers.put(EmotionDetector.Emotion.CONFIDENT, "quite confident");
        intensityModifiers.put(EmotionDetector.Emotion.RELAXED, "completely relaxed");
        intensityModifiers.put(EmotionDetector.Emotion.CURIOUS, "genuinely curious");
        intensityModifiers.put(EmotionDetector.Emotion.CREATIVE, "very creative");
        intensityModifiers.put(EmotionDetector.Emotion.HOPEFUL, "hopeful");
        intensityModifiers.put(EmotionDetector.Emotion.PROUD, "rightfully proud");
        intensityModifiers.put(EmotionDetector.Emotion.AMUSED, "thoroughly amused");
        intensityModifiers.put(EmotionDetector.Emotion.INSPIRED, "truly inspired");
        intensityModifiers.put(EmotionDetector.Emotion.SAD, "feeling sad");
        intensityModifiers.put(EmotionDetector.Emotion.STRESSED, "under stress");
        intensityModifiers.put(EmotionDetector.Emotion.ANXIOUS, "feeling anxious");
        intensityModifiers.put(EmotionDetector.Emotion.LONELY, "feeling lonely");
        intensityModifiers.put(EmotionDetector.Emotion.ANGRY, "feeling angry");
        intensityModifiers.put(EmotionDetector.Emotion.OVERWHELMED, "overwhelmed");
        intensityModifiers.put(EmotionDetector.Emotion.TIRED, "feeling tired");
        intensityModifiers.put(EmotionDetector.Emotion.BORED, "feeling bored");
        intensityModifiers.put(EmotionDetector.Emotion.CONFUSED, "feeling confused");
        intensityModifiers.put(EmotionDetector.Emotion.FRUSTRATED, "feeling frustrated");
        intensityModifiers.put(EmotionDetector.Emotion.DISAPPOINTED, "disappointed");
        intensityModifiers.put(EmotionDetector.Emotion.EMBARRASSED, "feeling embarrassed");
        intensityModifiers.put(EmotionDetector.Emotion.NEUTRAL, "feeling neutral");
        intensityModifiers.put(EmotionDetector.Emotion.UNKNOWN, "uncertain of your feelings");
    }
    
    private void initializeEmotionReflections() {
        emotionReflections = new HashMap<>();
        emotionReflections.put(EmotionDetector.Emotion.HAPPY, Arrays.asList(
            "I hear that you're feeling happy", "It sounds like you're in good spirits",
            "I can sense your happiness", "You're radiating positive energy"
        ));
        emotionReflections.put(EmotionDetector.Emotion.EXCITED, Arrays.asList(
            "I hear the excitement in your words", "You sound really enthusiastic",
            "There's real energy behind what you're saying", "I can feel your anticipation"
        ));
        emotionReflections.put(EmotionDetector.Emotion.MOTIVATED, Arrays.asList(
            "I sense you're feeling driven today", "You sound determined and focused",
            "There's a real sense of purpose in your voice"
        ));
        emotionReflections.put(EmotionDetector.Emotion.GRATEFUL, Arrays.asList(
            "I hear appreciation in your words", "You sound thankful for something meaningful",
            "There's a sense of gratitude coming through"
        ));
        emotionReflections.put(EmotionDetector.Emotion.CONFIDENT, Arrays.asList(
            "I hear confidence in your voice", "You sound sure of yourself",
            "There's a steady self-assurance in what you say"
        ));
        emotionReflections.put(EmotionDetector.Emotion.RELAXED, Arrays.asList(
            "I hear calmness in your words", "You sound at ease",
            "There's a peaceful quality to what you're sharing"
        ));
        emotionReflections.put(EmotionDetector.Emotion.CURIOUS, Arrays.asList(
            "I hear genuine curiosity in your question", "You want to understand more about this",
            "There's an inquisitive energy here"
        ));
        emotionReflections.put(EmotionDetector.Emotion.CREATIVE, Arrays.asList(
            "I sense imaginative energy", "You're thinking outside the box",
            "There's creative potential here"
        ));
        emotionReflections.put(EmotionDetector.Emotion.HOPEFUL, Arrays.asList(
            "I hear optimism in your words", "You have a hopeful outlook",
            "There's positive anticipation for the future"
        ));
        emotionReflections.put(EmotionDetector.Emotion.PROUD, Arrays.asList(
            "I hear accomplishment in your voice", "You have every reason to feel proud",
            "There's a sense of achievement here"
        ));
        emotionReflections.put(EmotionDetector.Emotion.AMUSED, Arrays.asList(
            "I hear amusement in your words", "You find this entertaining",
            "There's a lighter energy here"
        ));
        emotionReflections.put(EmotionDetector.Emotion.INSPIRED, Arrays.asList(
            "I can sense inspiration in your words", "You've been moved by something",
            "There's creative energy awakened here"
        ));
        emotionReflections.put(EmotionDetector.Emotion.SAD, Arrays.asList(
            "I hear that you're feeling sad", "It sounds like you're going through a difficult time",
            "I can sense the heaviness in your words", "You're hurting right now"
        ));
        emotionReflections.put(EmotionDetector.Emotion.STRESSED, Arrays.asList(
            "I hear that you're stressed", "You sound like you're carrying a lot",
            "There's pressure in what you're sharing"
        ));
        emotionReflections.put(EmotionDetector.Emotion.ANXIOUS, Arrays.asList(
            "I hear worry in your words", "You sound anxious about something",
            "There's uncertainty troubling you"
        ));
        emotionReflections.put(EmotionDetector.Emotion.LONELY, Arrays.asList(
            "I hear loneliness in your words", "You sound like you could use some company",
            "There's a sense of isolation here"
        ));
        emotionReflections.put(EmotionDetector.Emotion.ANGRY, Arrays.asList(
            "I hear frustration in your voice", "You're clearly upset about something",
            "There's real anger here"
        ));
        emotionReflections.put(EmotionDetector.Emotion.OVERWHELMED, Arrays.asList(
            "I hear that you're overwhelmed", "You sound like you have too much on your plate",
            "There's a lot weighing on you"
        ));
        emotionReflections.put(EmotionDetector.Emotion.TIRED, Arrays.asList(
            "I hear exhaustion in your words", "You sound like you need rest",
            "There's weariness in your voice"
        ));
        emotionReflections.put(EmotionDetector.Emotion.BORED, Arrays.asList(
            "I hear that you're feeling bored", "You sound like you're looking for something engaging",
            "There's a lack of stimulation here"
        ));
        emotionReflections.put(EmotionDetector.Emotion.CONFUSED, Arrays.asList(
            "I hear confusion in your words", "You're not sure what to make of this",
            "There's uncertainty here"
        ));
        emotionReflections.put(EmotionDetector.Emotion.FRUSTRATED, Arrays.asList(
            "I hear frustration", "You're stuck and it's bothering you",
            "Things aren't going the way you want"
        ));
        emotionReflections.put(EmotionDetector.Emotion.DISAPPOINTED, Arrays.asList(
            "I hear disappointment", "This didn't turn out the way you hoped",
            "There's a sense of letdown here"
        ));
        emotionReflections.put(EmotionDetector.Emotion.EMBARRASSED, Arrays.asList(
            "I hear embarrassment", "You're feeling awkward about this",
            "There's a sense of self-consciousness"
        ));
        emotionReflections.put(EmotionDetector.Emotion.NEUTRAL, Arrays.asList(
            "I hear that you're feeling neutral about this", "You're taking things as they come",
            "There's a balanced quality to your words"
        ));
        emotionReflections.put(EmotionDetector.Emotion.UNKNOWN, Arrays.asList(
            "I'm not sure what emotions you're experiencing", "It's hard to read how you feel",
            "Your emotional state isn't clear to me"
        ));
    }
    
    private void initializeValidationResponses() {
        validationResponses = new HashMap<>();
        validationResponses.put(EmotionDetector.Emotion.HAPPY, Arrays.asList(
            "And that's completely valid - happiness is a wonderful state to be in.",
            "Your joy is absolutely legitimate - you have every right to feel this way.",
            "There's nothing wrong with embracing your happiness."
        ));
        validationResponses.put(EmotionDetector.Emotion.EXCITED, Arrays.asList(
            "Your excitement is well-founded and makes complete sense.",
            "Feeling excited is a natural response to something wonderful."
        ));
        validationResponses.put(EmotionDetector.Emotion.MOTIVATED, Arrays.asList(
            "Your motivation is a powerful asset - embrace it.",
            "Feeling driven is healthy and productive."
        ));
        validationResponses.put(EmotionDetector.Emotion.GRATEFUL, Arrays.asList(
            "Gratitude is a beautiful emotion to experience.",
            "Your thankfulness reflects a healthy perspective."
        ));
        validationResponses.put(EmotionDetector.Emotion.CONFIDENT, Arrays.asList(
            "Confidence in yourself is healthy and warranted.",
            "Believing in your abilities is a strength."
        ));
        validationResponses.put(EmotionDetector.Emotion.RELAXED, Arrays.asList(
            "Feeling calm and at ease is a gift.",
            "Your relaxation is well-deserved."
        ));
        validationResponses.put(EmotionDetector.Emotion.CURIOUS, Arrays.asList(
            "Curiosity is the foundation of all learning.",
            "Your desire to understand is completely valid."
        ));
        validationResponses.put(EmotionDetector.Emotion.CREATIVE, Arrays.asList(
            "Your creative energy is a wonderful gift.",
            "Imagination and creativity are valuable strengths."
        ));
        validationResponses.put(EmotionDetector.Emotion.HOPEFUL, Arrays.asList(
            "Hope is one of the most powerful emotions we can experience.",
            "Believing in positive outcomes is healthy and adaptive."
        ));
        validationResponses.put(EmotionDetector.Emotion.PROUD, Arrays.asList(
            "You have every right to feel proud.",
            "Your sense of accomplishment is well-founded."
        ));
        validationResponses.put(EmotionDetector.Emotion.AMUSED, Arrays.asList(
            "Laughter and amusement are essential to well-being.",
            "Finding joy and humor is a gift."
        ));
        validationResponses.put(EmotionDetector.Emotion.INSPIRED, Arrays.asList(
            "Being inspired is a beautiful state to be in.",
            "Your inspiration is genuine and meaningful."
        ));
        validationResponses.put(EmotionDetector.Emotion.SAD, Arrays.asList(
            "And your sadness is completely valid - it's okay to feel this way.",
            "What you're feeling matters - sadness is a natural human emotion.",
            "There's nothing wrong with feeling sad - it's how we process loss."
        ));
        validationResponses.put(EmotionDetector.Emotion.STRESSED, Arrays.asList(
            "And your stress is completely valid - you're dealing with real pressure.",
            "Feeling stressed doesn't mean you're weak - it means you're human."
        ));
        validationResponses.put(EmotionDetector.Emotion.ANXIOUS, Arrays.asList(
            "And your anxiety is valid - it's a real emotion that many experience.",
            "Feeling anxious doesn't make you flawed - it's a common experience."
        ));
        validationResponses.put(EmotionDetector.Emotion.LONELY, Arrays.asList(
            "And your loneliness is valid - connection is a fundamental human need.",
            "Feeling alone doesn't mean you're alone in feeling this way."
        ));
        validationResponses.put(EmotionDetector.Emotion.ANGRY, Arrays.asList(
            "And your anger is valid - it's an important emotion that signals boundaries.",
            "Feeling angry doesn't make you a bad person - it's a natural response."
        ));
        validationResponses.put(EmotionDetector.Emotion.OVERWHELMED, Arrays.asList(
            "And feeling overwhelmed is completely valid - you're facing a lot.",
            "It's okay to feel like things are too much right now."
        ));
        validationResponses.put(EmotionDetector.Emotion.TIRED, Arrays.asList(
            "And your tiredness is valid - rest is not a luxury, it's a necessity.",
            "Feeling exhausted doesn't mean you're weak - it means you've been giving a lot."
        ));
        validationResponses.put(EmotionDetector.Emotion.BORED, Arrays.asList(
            "And boredom is valid - your mind needs stimulation and growth.",
            "Feeling bored doesn't mean you're ungrateful - it means you crave engagement."
        ));
        validationResponses.put(EmotionDetector.Emotion.CONFUSED, Arrays.asList(
            "And your confusion is valid - not everything has clear answers.",
            "Feeling uncertain doesn't mean you're incompetent."
        ));
        validationResponses.put(EmotionDetector.Emotion.FRUSTRATED, Arrays.asList(
            "And your frustration is valid - being stuck is genuinely difficult.",
            "Feeling frustrated doesn't mean you're failing - it means you care."
        ));
        validationResponses.put(EmotionDetector.Emotion.DISAPPOINTED, Arrays.asList(
            "And your disappointment is valid - you had real expectations.",
            "Feeling let down doesn't mean your hopes were wrong."
        ));
        validationResponses.put(EmotionDetector.Emotion.EMBARRASSED, Arrays.asList(
            "And your embarrassment is valid - we've all had awkward moments.",
            "Feeling embarrassed doesn't define your worth."
        ));
        validationResponses.put(EmotionDetector.Emotion.NEUTRAL, Arrays.asList(
            "Feeling neutral is a valid emotional state.",
            "Not having strong emotions either way is completely okay."
        ));
        validationResponses.put(EmotionDetector.Emotion.UNKNOWN, Arrays.asList(
            "And that's okay - emotions can be complex and hard to name.",
            "Not being sure about your feelings is a common experience."
        ));
    }
    
    private void initializeNormalizationStatements() {
        normalizationStatements = new HashMap<>();
        normalizationStatements.put(EmotionDetector.Emotion.HAPPY, Arrays.asList(
            "Happiness is something everyone deserves to experience.",
            "Many people are experiencing similar joys right now."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.EXCITED, Arrays.asList(
            "Excitement is a common feeling when anticipating something good.",
            "Many people experience excitement about new opportunities."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.MOTIVATED, Arrays.asList(
            "Feeling driven is a state many people strive for.",
            "Motivation comes and goes for everyone - right now you have it."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.GRATEFUL, Arrays.asList(
            "Gratitude is one of the most universal positive emotions.",
            "Many people are learning to appreciate what they have."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.CONFIDENT, Arrays.asList(
            "Self-confidence is something many people work toward.",
            "Believing in yourself is a worthy goal that many share."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.RELAXED, Arrays.asList(
            "Finding calm is something many actively seek.",
            "Peace of mind is a universal desire."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.CURIOUS, Arrays.asList(
            "Curiosity is a fundamental human trait that everyone possesses.",
            "Many people are exploring similar questions."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.CREATIVE, Arrays.asList(
            "Creative impulses are universal - everyone has them.",
            "Many people are exploring their creative sides."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.HOPEFUL, Arrays.asList(
            "Hope is a universal human experience.",
            "Many people are holding onto hope right now."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.PROUD, Arrays.asList(
            "Feeling proud of achievements is a universal experience.",
            "Many people are celebrating their wins, big and small."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.AMUSED, Arrays.asList(
            "Finding joy and humor is universal.",
            "Many people are laughing and finding pleasure today."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.INSPIRED, Arrays.asList(
            "Inspiration strikes people everywhere.",
            "Many are currently feeling moved by something."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.SAD, Arrays.asList(
            "Sadness is one of the most universal human emotions.",
            "Many people are feeling sad right now - you're not alone.",
            "Feeling down is a common human experience."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.STRESSED, Arrays.asList(
            "Stress is an incredibly common experience in modern life.",
            "Many people are carrying similar burdens right now."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.ANXIOUS, Arrays.asList(
            "Anxiety is one of the most common human experiences.",
            "Many people are feeling anxious right now - you're not alone."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.LONELY, Arrays.asList(
            "Loneliness is a universal human experience.",
            "Many people are feeling alone right now, even in crowds."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.ANGRY, Arrays.asList(
            "Anger is a completely natural human emotion.",
            "Many people are feeling frustrated right now."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.OVERWHELMED, Arrays.asList(
            "Feeling overwhelmed is incredibly common these days.",
            "Many people feel like they have too much on their plate."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.TIRED, Arrays.asList(
            "Exhaustion is a common experience for many.",
            "Many people are running on empty right now."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.BORED, Arrays.asList(
            "Boredom is a surprisingly common experience.",
            "Many people struggle with finding engagement."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.CONFUSED, Arrays.asList(
            "Confusion is a universal human experience.",
            "Many people are uncertain about things right now."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.FRUSTRATED, Arrays.asList(
            "Frustration is an incredibly common experience.",
            "Many people feel stuck in various areas of life."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.DISAPPOINTED, Arrays.asList(
            "Disappointment is a universal human experience.",
            "Many people are dealing with letdowns right now."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.EMBARRASSED, Arrays.asList(
            "Embarrassment is something everyone experiences.",
            "Many people feel awkward about things daily."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.NEUTRAL, Arrays.asList(
            "Feeling neutral is a valid emotional state.",
            "Balance and steadiness have their own value."
        ));
        normalizationStatements.put(EmotionDetector.Emotion.UNKNOWN, Arrays.asList(
            "Emotions can be complex and hard to name.",
            "It's okay to take time to understand what you're experiencing."
        ));
    }
    
    // ==================== EMOTIONAL VOCABULARY - NUANCED FEELING DESCRIPTIONS ====================
    
    /**
     * Initializes nuanced feeling descriptions for each emotion at different intensity levels.
     * Provides detailed, contextual descriptions that help express emotions more precisely.
     */
    private void initializeNuancedFeelingDescriptions() {
        nuancedFeelingDescriptions = new HashMap<>();
        
        // HAPPY - nuanced descriptions
        Map<String, String> happyDescriptions = new LinkedHashMap<>();
        happyDescriptions.put("mild", "You're feeling a gentle sense of contentment - a small spark of joy");
        happyDescriptions.put("moderate", "You're experiencing clear happiness - a warm, positive feeling");
        happyDescriptions.put("intense", "You're radiating pure joy - an overwhelming sense of delight");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.HAPPY, happyDescriptions);
        
        // EXCITED - nuanced descriptions
        Map<String, String> excitedDescriptions = new LinkedHashMap<>();
        excitedDescriptions.put("mild", "You have a flutter of anticipation - something piques your interest");
        excitedDescriptions.put("moderate", "You're feeling excited - there's a buzz of energy inside you");
        excitedDescriptions.put("intense", "You're buzzing with excitement - you can hardly contain yourself");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.EXCITED, excitedDescriptions);
        
        // MOTIVATED - nuanced descriptions
        Map<String, String> motivatedDescriptions = new LinkedHashMap<>();
        motivatedDescriptions.put("mild", "You have a spark of drive - a desire to make progress");
        motivatedDescriptions.put("moderate", "You're feeling motivated - focused and ready to take action");
        motivatedDescriptions.put("intense", "You're highly driven - nothing can stop you from achieving your goals");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.MOTIVATED, motivatedDescriptions);
        
        // GRATEFUL - nuanced descriptions
        Map<String, String> gratefulDescriptions = new LinkedHashMap<>();
        gratefulDescriptions.put("mild", "You feel a subtle appreciation for something in your life");
        gratefulDescriptions.put("moderate", "You're feeling genuinely grateful - thanking your lucky stars");
        gratefulDescriptions.put("intense", "You're overwhelmed with gratitude - your heart is full of thanks");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.GRATEFUL, gratefulDescriptions);
        
        // CONFIDENT - nuanced descriptions
        Map<String, String> confidentDescriptions = new LinkedHashMap<>();
        confidentDescriptions.put("mild", "You feel a quiet assurance in yourself");
        confidentDescriptions.put("moderate", "You're feeling confident - believing in your abilities");
        confidentDescriptions.put("intense", "You're radiantly confident - completely sure of yourself");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.CONFIDENT, confidentDescriptions);
        
        // RELAXED - nuanced descriptions
        Map<String, String> relaxedDescriptions = new LinkedHashMap<>();
        relaxedDescriptions.put("mild", "You feel a gentle ease - tension melting away");
        relaxedDescriptions.put("moderate", "You're feeling relaxed - at peace and comfortable");
        relaxedDescriptions.put("intense", "You're completely at ease - a deep state of tranquility");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.RELAXED, relaxedDescriptions);
        
        // CURIOUS - nuanced descriptions
        Map<String, String> curiousDescriptions = new LinkedHashMap<>();
        curiousDescriptions.put("mild", "You have a wondering thought - wanting to know more");
        curiousDescriptions.put("moderate", "You're feeling curious - eager to explore and learn");
        curiousDescriptions.put("intense", "You're intensely curious - driven to uncover every detail");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.CURIOUS, curiousDescriptions);
        
        // CREATIVE - nuanced descriptions
        Map<String, String> creativeDescriptions = new LinkedHashMap<>();
        creativeDescriptions.put("mild", "You have a glimmer of imagination");
        creativeDescriptions.put("moderate", "You're feeling creative - ideas flowing freely");
        creativeDescriptions.put("intense", "You're highly creative - your mind is overflowing with ideas");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.CREATIVE, creativeDescriptions);
        
        // HOPEFUL - nuanced descriptions
        Map<String, String> hopefulDescriptions = new LinkedHashMap<>();
        hopefulDescriptions.put("mild", "You hold a small ray of hope");
        hopefulDescriptions.put("moderate", "You're feeling hopeful - seeing possibilities ahead");
        hopefulDescriptions.put("intense", "You're filled with hope - optimistic about the future");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.HOPEFUL, hopefulDescriptions);
        
        // PROUD - nuanced descriptions
        Map<String, String> proudDescriptions = new LinkedHashMap<>();
        proudDescriptions.put("mild", "You feel a quiet satisfaction with yourself");
        proudDescriptions.put("moderate", "You're feeling proud - pleased with your achievements");
        proudDescriptions.put("intense", "You're incredibly proud - beaming with accomplishment");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.PROUD, proudDescriptions);
        
        // AMUSED - nuanced descriptions
        Map<String, String> amusedDescriptions = new LinkedHashMap<>();
        amusedDescriptions.put("mild", "A small smile tugs at your lips");
        amusedDescriptions.put("moderate", "You're feeling amused - chuckling at something funny");
        amusedDescriptions.put("intense", "You're roaring with laughter - find something hilarious");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.AMUSED, amusedDescriptions);
        
        // INSPIRED - nuanced descriptions
        Map<String, String> inspiredDescriptions = new LinkedHashMap<>();
        inspiredDescriptions.put("mild", "You feel a spark of inspiration");
        inspiredDescriptions.put("moderate", "You're feeling inspired - moved to create or act");
        inspiredDescriptions.put("intense", "You're deeply inspired - your spirit is awakened");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.INSPIRED, inspiredDescriptions);
        
        // SAD - nuanced descriptions
        Map<String, String> sadDescriptions = new LinkedHashMap<>();
        sadDescriptions.put("mild", "You feel a gentle heaviness - a slight gloom");
        sadDescriptions.put("moderate", "You're feeling sad - a persistent ache in your heart");
        sadDescriptions.put("intense", "You're overwhelmed with sadness - deep sorrow and grief");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.SAD, sadDescriptions);
        
        // STRESSED - nuanced descriptions
        Map<String, String> stressedDescriptions = new LinkedHashMap<>();
        stressedDescriptions.put("mild", "You feel a subtle pressure building");
        stressedDescriptions.put("moderate", "You're feeling stressed - under significant pressure");
        stressedDescriptions.put("intense", "You're under intense stress - feeling the weight of everything");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.STRESSED, stressedDescriptions);
        
        // ANXIOUS - nuanced descriptions
        Map<String, String> anxiousDescriptions = new LinkedHashMap<>();
        anxiousDescriptions.put("mild", "You have a slight knot in your stomach");
        anxiousDescriptions.put("moderate", "You're feeling anxious - worried about what might happen");
        anxiousDescriptions.put("intense", "You're consumed by anxiety - racing thoughts and fear");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.ANXIOUS, anxiousDescriptions);
        
        // LONELY - nuanced descriptions
        Map<String, String> lonelyDescriptions = new LinkedHashMap<>();
        lonelyDescriptions.put("mild", "You feel a gentle longing for connection");
        lonelyDescriptions.put("moderate", "You're feeling lonely - craving meaningful company");
        lonelyDescriptions.put("intense", "You're deeply lonely - aching for genuine connection");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.LONELY, lonelyDescriptions);
        
        // ANGRY - nuanced descriptions
        Map<String, String> angryDescriptions = new LinkedHashMap<>();
        angryDescriptions.put("mild", "You feel a flicker of irritation");
        angryDescriptions.put("moderate", "You're feeling angry - a strong heat building inside");
        angryDescriptions.put("intense", "You're consumed by rage - seeing red");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.ANGRY, angryDescriptions);
        
        // OVERWHELMED - nuanced descriptions
        Map<String, String> overwhelmedDescriptions = new LinkedHashMap<>();
        overwhelmedDescriptions.put("mild", "You feel things are piling up");
        overwhelmedDescriptions.put("moderate", "You're feeling overwhelmed - struggling to keep up");
        overwhelmedDescriptions.put("intense", "You're completely overwhelmed - buried under everything");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.OVERWHELMED, overwhelmedDescriptions);
        
        // TIRED - nuanced descriptions
        Map<String, String> tiredDescriptions = new LinkedHashMap<>();
        tiredDescriptions.put("mild", "You feel a slight fatigue - needing rest");
        tiredDescriptions.put("moderate", "You're feeling tired - exhausted and worn out");
        tiredDescriptions.put("intense", "You're completely drained - barely able to keep your eyes open");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.TIRED, tiredDescriptions);
        
        // BORED - nuanced descriptions
        Map<String, String> boredDescriptions = new LinkedHashMap<>();
        boredDescriptions.put("mild", "You feel a slight restlessness");
        boredDescriptions.put("moderate", "You're feeling bored - seeking something engaging");
        boredDescriptions.put("intense", "You're deeply bored - desperate for stimulation");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.BORED, boredDescriptions);
        
        // CONFUSED - nuanced descriptions
        Map<String, String> confusedDescriptions = new LinkedHashMap<>();
        confusedDescriptions.put("mild", "You have a moment of uncertainty");
        confusedDescriptions.put("moderate", "You're feeling confused - struggling to understand");
        confusedDescriptions.put("intense", "You're completely confused - everything is unclear");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.CONFUSED, confusedDescriptions);
        
        // FRUSTRATED - nuanced descriptions
        Map<String, String> frustratedDescriptions = new LinkedHashMap<>();
        frustratedDescriptions.put("mild", "You feel a hint of impatience");
        frustratedDescriptions.put("moderate", "You're feeling frustrated - stuck and annoyed");
        frustratedDescriptions.put("intense", "You're deeply frustrated - at your wit's end");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.FRUSTRATED, frustratedDescriptions);
        
        // DISAPPOINTED - nuanced descriptions
        Map<String, String> disappointedDescriptions = new LinkedHashMap<>();
        disappointedDescriptions.put("mild", "You feel a slight letdown");
        disappointedDescriptions.put("moderate", "You're feeling disappointed - expectations not met");
        disappointedDescriptions.put("intense", "You're deeply disappointed - profoundly let down");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.DISAPPOINTED, disappointedDescriptions);
        
        // EMBARRASSED - nuanced descriptions
        Map<String, String> embarrassedDescriptions = new LinkedHashMap<>();
        embarrassedDescriptions.put("mild", "You feel a warmth in your cheeks");
        embarrassedDescriptions.put("moderate", "You're feeling embarrassed - awkward and self-conscious");
        embarrassedDescriptions.put("intense", "You're deeply embarrassed - mortified by the situation");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.EMBARRASSED, embarrassedDescriptions);
        
        // NEUTRAL - nuanced descriptions
        Map<String, String> neutralDescriptions = new LinkedHashMap<>();
        neutralDescriptions.put("mild", "You feel balanced and steady");
        neutralDescriptions.put("moderate", "You're feeling neutral - neither here nor there");
        neutralDescriptions.put("intense", "You're completely neutral - perfectly equanimous");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.NEUTRAL, neutralDescriptions);
        
        // UNKNOWN - nuanced descriptions
        Map<String, String> unknownDescriptions = new LinkedHashMap<>();
        unknownDescriptions.put("mild", "You're unsure of what you're feeling");
        unknownDescriptions.put("moderate", "You're confused about your emotions");
        unknownDescriptions.put("intense", "You can't make sense of your feelings at all");
        nuancedFeelingDescriptions.put(EmotionDetector.Emotion.UNKNOWN, unknownDescriptions);
    }
    private void initializeFeelingWordsLibrary() {
        feelingWordsLibrary = new HashMap<>();
        
        // HAPPY - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.HAPPY, Arrays.asList(
            "content", "pleased", "satisfied", "happy", "joyful", "delighted", 
            "ecstatic", "overjoyed", "elated", "thrilled", "cheerful", "glad"
        ));
        
        // EXCITED - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.EXCITED, Arrays.asList(
            "eager", "enthusiastic", "interested", "excited", "thrilled", "exhilarated",
            "over the moon", "ecstatic", "on cloud nine", "pumped", "amped"
        ));
        
        // MOTIVATED - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.MOTIVATED, Arrays.asList(
            "driven", "determined", "focused", "motivated", "ambitious", "goal-oriented",
            "unstoppable", "laser-focused", "purposeful", "energized"
        ));
        
        // GRATEFUL - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.GRATEFUL, Arrays.asList(
            "thankful", "appreciative", "grateful", "blessed", "fortunate", "indebted"
        ));
        
        // CONFIDENT - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.CONFIDENT, Arrays.asList(
            "assured", "self-assured", "confident", "bold", "certain", "sure", "empowered"
        ));
        
        // RELAXED - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.RELAXED, Arrays.asList(
            "calm", "peaceful", "serene", "tranquil", "relaxed", "at ease", "laid-back"
        ));
        
        // CURIOUS - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.CURIOUS, Arrays.asList(
            "inquisitive", "questioning", "curious", "wondering", "exploring", "searching"
        ));
        
        // CREATIVE - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.CREATIVE, Arrays.asList(
            "imaginative", "inventive", "creative", "inspired", "innovative", "artistic"
        ));
        
        // HOPEFUL - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.HOPEFUL, Arrays.asList(
            "optimistic", "positive", "hopeful", "looking forward", "encouraged", "buoyant"
        ));
        
        // PROUD - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.PROUD, Arrays.asList(
            "satisfied", "pleased", "proud", "accomplished", "triumphant", "honored"
        ));
        
        // AMUSED - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.AMUSED, Arrays.asList(
            "entertained", "amused", "humored", "tickled", "laughing", "playful"
        ));
        
        // INSPIRED - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.INSPIRED, Arrays.asList(
            "moved", "touched", "inspired", "awakened", "energized", "uplifted"
        ));
        
        // SAD - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.SAD, Arrays.asList(
            "down", "melancholy", "sad", "gloomy", "blue", "heavy", "heartbroken"
        ));
        
        // STRESSED - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.STRESSED, Arrays.asList(
            "tense", "pressured", "stressed", "strained", "burdened", "overloaded"
        ));
        
        // ANXIOUS - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.ANXIOUS, Arrays.asList(
            "worried", "nervous", "anxious", "apprehensive", "uneasy", "fearful"
        ));
        
        // LONELY - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.LONELY, Arrays.asList(
            "alone", "isolated", "disconnected", "lonely", "abandoned", "forlorn"
        ));
        
        // ANGRY - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.ANGRY, Arrays.asList(
            "irritated", "annoyed", "angry", "furious", "enraged", "livid"
        ));
        
        // OVERWHELMED - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.OVERWHELMED, Arrays.asList(
            "burdened", "swamped", "overwhelmed", "inundated", "flooded", "drowning"
        ));
        
        // TIRED - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.TIRED, Arrays.asList(
            "exhausted", "drained", "tired", "fatigued", "worn out", "sleepy"
        ));
        
        // BORED - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.BORED, Arrays.asList(
            "restless", "uninterested", "bored", "tedious", "dull", "apathetic"
        ));
        
        // CONFUSED - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.CONFUSED, Arrays.asList(
            "uncertain", "puzzled", "confused", "bewildered", "perplexed", "lost"
        ));
        
        // FRUSTRATED - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.FRUSTRATED, Arrays.asList(
            "impatient", "stuck", "frustrated", "blocked", "thwarted", "exasperated"
        ));
        
        // DISAPPOINTED - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.DISAPPOINTED, Arrays.asList(
            "letdown", "discouraged", "disappointed", "dismayed", "disheartened", "crestfallen"
        ));
        
        // EMBARRASSED - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.EMBARRASSED, Arrays.asList(
            "awkward", "self-conscious", "embarrassed", "humiliated", "mortified", "ashamed"
        ));
        
        // NEUTRAL - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.NEUTRAL, Arrays.asList(
            "balanced", "steady", "neutral", "indifferent", "unbiased", "collected"
        ));
        
        // UNKNOWN - Feeling words library
        feelingWordsLibrary.put(EmotionDetector.Emotion.UNKNOWN, Arrays.asList(
            "uncertain", "confused", "unclear", "unsure", "indefinite", "ambivalent"
        ));
    }

    private void initializeEmotionGranularity() {
        emotionalGranularity = new HashMap<>();
        
        // All emotions use List<String> for intensity levels
        emotionalGranularity.put(EmotionDetector.Emotion.HAPPY, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.EXCITED, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.MOTIVATED, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.GRATEFUL, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.CONFIDENT, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.RELAXED, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.CURIOUS, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.CREATIVE, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.HOPEFUL, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.PROUD, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.AMUSED, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.INSPIRED, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.SAD, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.STRESSED, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.ANXIOUS, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.LONELY, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.ANGRY, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.OVERWHELMED, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.TIRED, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.BORED, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.CONFUSED, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.FRUSTRATED, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.DISAPPOINTED, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.EMBARRASSED, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.NEUTRAL, Arrays.asList("mild", "moderate", "intense"));
        emotionalGranularity.put(EmotionDetector.Emotion.UNKNOWN, Arrays.asList("mild", "moderate", "intense"));
    }
    
    // ==================== SUPPORTIVE PRESENCE ====================
    
    /**
     * Initializes "I'm here" messaging for supportive presence.
     * These messages reassure users that they are not alone.
     */
    private void initializeImHereMessages() {
        imHereMessages = new HashMap<>();
        
        // General presence messages
        imHereMessages.put("general", Arrays.asList(
            "I'm here with you.",
            "You don't have to go through this alone.",
            "I'm right here.",
            "You've got me by your side.",
            "I'm here for you.",
            "I'm listening and I'm here.",
            "Know that I'm here with you.",
            "I'm present with you.",
            "You're not alone in this."
        ));
        
        // Distress-specific messages
        imHereMessages.put("distress", Arrays.asList(
            "I'm here, and I'm not going anywhere.",
            "You've got someone who cares about you right here.",
            "I'm right here beside you, even if just virtually.",
            "In this moment, I'm here with you.",
            "Let me be here for you.",
            "I'm present and ready to listen.",
            "You've got my full attention and support.",
            "I'm standing with you through this."
        ));
        
        // Comfort messages
        imHereMessages.put("comfort", Arrays.asList(
            "Take comfort in knowing I'm here.",
            "Let my presence be a source of comfort for you.",
            "May my presence bring you some peace.",
            "I'm here to offer you comfort and support.",
            "You can find solace in knowing I'm here.",
            "Allow yourself to feel supported by my presence.",
            "My virtual presence is here to comfort you."
        ));
    }
    
    /**
     * Initializes physical space acknowledgments.
     * These acknowledge the user's physical space and offer virtual presence.
     */
    private void initializePhysicalSpaceAcknowledgments() {
        physicalSpaceAcknowledgments = new HashMap<>();
        
        // Virtual presence acknowledgment
        physicalSpaceAcknowledgments.put("virtual_presence", Arrays.asList(
            "Even though we can't be in the same room, I'm virtually right there with you.",
            "While I can't be physically beside you, my attention is fully with you.",
            "In this virtual space, we're connected.",
            "My focus is entirely on you, wherever you are.",
            "Distance can't diminish my presence here with you.",
            "Though we're apart, I'm fully present in this conversation.",
            "Your physical space is honored here, even if I can only be there in spirit."
        ));
        
        // Space and boundaries
        physicalSpaceAcknowledgments.put("space", Arrays.asList(
            "Take all the space you need.",
            "Your space is respected here.",
            "You have room to feel whatever comes up.",
            "There's space for everything you're experiencing.",
            "Your physical and emotional space is safe here.",
            "You don't have to share more than you're comfortable with.",
            "Whatever space you need, it's okay."
        ));
        
        // Grounding
        physicalSpaceAcknowledgments.put("grounding", Arrays.asList(
            "Feel free to ground yourself in your surroundings.",
            "Take a moment to feel the support beneath you.",
            "Notice the ground holding you up.",
            "Your feet are planted, you're here.",
            "Feel your connection to the earth.",
            "You're anchored in this moment.",
            "Feel the solidity beneath you."
        ));
    }
    
    /**
     * Initializes time acknowledgments.
     * These messages acknowledge the passage of time and the user's journey.
     */
    private void initializeTimeAcknowledgments() {
        timeAcknowledgments = new HashMap<>();
        
        // Patience and time
        timeAcknowledgments.put("patience", Arrays.asList(
            "Take all the time you need.",
            "There's no rush here.",
            "We have time. Go at your own pace.",
            "I'm patient with you.",
            "Whatever time this takes, I'm here.",
            "Time is yours to use as you need.",
            "You don't have to hurry this."
        ));
        
        // Duration acknowledgment
        timeAcknowledgments.put("duration", Arrays.asList(
            "You've been carrying this for a while, haven't you?",
            "I recognize this has been building up.",
            "This isn't new - it's been weighing on you.",
            "You've been going through a lot lately.",
            "I see how long this has been affecting you.",
            "This has been hard on you for some time now.",
            "You've been dealing with this - that takes strength."
        ));
        
        // Moment acknowledgment
        timeAcknowledgments.put("moment", Arrays.asList(
            "Right now, in this moment, I'm here.",
            "This moment matters.",
            "Right now, you have my full attention.",
            "In this present moment, we're connected.",
            "This moment is important.",
            "Right here, right now - I'm with you.",
            "This moment counts, and I'm present in it."
        ));
    }
    
    // ==================== PUBLIC METHODS ====================
    
    /**
     * Reflects an emotion based on user input
     * @param input The user's input text
     * @return EmotionalMirrorResponse containing reflected emotion
     */
    public EmotionalMirrorResponse reflectEmotion(String input) {
        EmotionalMirrorResponse response = new EmotionalMirrorResponse();
        
        if (input == null || input.trim().isEmpty()) {
            return response;
        }
        
        EmotionDetector.EmotionResult result = emotionDetector.detectEmotion(input);
        EmotionDetector.Emotion emotion = result.getPrimaryEmotion();
        
        response.setPrimaryEmotion(emotion);
        response.setConfidence(result.getConfidence());
        response.setValid(true);
        
        List<String> reflections = emotionReflections.get(emotion);
        String reflection = reflections != null && !reflections.isEmpty() 
            ? reflections.get(random.nextInt(reflections.size())) 
            : "I hear you";
        response.setReflectedEmotion(intensityModifiers.getOrDefault(emotion, "feeling something"));
        
        List<String> validations = validationResponses.get(emotion);
        String validation = validations != null && !validations.isEmpty()
            ? validations.get(random.nextInt(validations.size()))
            : "Your feelings are completely valid.";
        response.setValidationMessage(validation);
        
        List<String> normalizations = normalizationStatements.get(emotion);
        String normalization = normalizations != null && !normalizations.isEmpty()
            ? normalizations.get(random.nextInt(normalizations.size()))
            : "You're not alone in feeling this way.";
        response.setNormalizationMessage(normalization);
        
        return response;
    }
    
    /**
     * Validates an emotion
     * @param emotion The emotion to validate
     * @return EmotionValidation containing validation result
     */
    public EmotionValidation validateEmotion(EmotionDetector.Emotion emotion) {
        List<String> validations = validationResponses.get(emotion);
        String message = validations != null && !validations.isEmpty()
            ? validations.get(random.nextInt(validations.size()))
            : "Your feelings are valid.";
        
        return new EmotionValidation(emotion, true, message, "ACKNOWLEDGED", 0.95);
    }
    
    /**
     * Normalizes an emotion
     * @param emotion The emotion to normalize
     * @return EmotionNormalization containing normalization result
     */
    public EmotionNormalization normalizeEmotion(EmotionDetector.Emotion emotion) {
        EmotionNormalization normalization = new EmotionNormalization();
        normalization.setEmotion(emotion);
        
        List<String> statements = normalizationStatements.get(emotion);
        String statement = statements != null && !statements.isEmpty()
            ? statements.get(random.nextInt(statements.size()))
            : "You're not alone in feeling this way.";
        
        normalization.setNormalizedStatement(statement);
        normalization.setPerspective("Many people experience similar emotions.");
        normalization.setNormal(true);
        
        return normalization;
    }
    
    /**
     * Gets an empathetic response for the given input
     * @param input User's input text
     * @return String containing empathetic response
     */
    public String getEmpatheticResponse(String input) {
        EmotionalMirrorResponse response = reflectEmotion(input);
        return response.getFullResponse();
    }
    
    /**
     * Gets intensity modifier for an emotion
     * @param emotion The emotion
     * @return String containing intensity modifier
     */
    public String getIntensityModifier(EmotionDetector.Emotion emotion) {
        return intensityModifiers.getOrDefault(emotion, "feeling something");
    }
    
    /**
     * Gets reflection for a specific emotion
     * @param emotion The emotion
     * @return String containing reflection
     */
    public String getEmotionReflection(EmotionDetector.Emotion emotion) {
        List<String> reflections = emotionReflections.get(emotion);
        if (reflections != null && !reflections.isEmpty()) {
            return reflections.get(random.nextInt(reflections.size()));
        }
        return "I hear you";
    }
    
    /**
     * Gets validation for a specific emotion
     * @param emotion The emotion
     * @return String containing validation message
     */
    public String getValidationResponse(EmotionDetector.Emotion emotion) {
        List<String> validations = validationResponses.get(emotion);
        if (validations != null && !validations.isEmpty()) {
            return validations.get(random.nextInt(validations.size()));
        }
        return "Your feelings are valid.";
    }
    
    /**
     * Gets normalization for a specific emotion
     * @param emotion The emotion
     * @return String containing normalization statement
     */
    public String getNormalizationStatement(EmotionDetector.Emotion emotion) {
        List<String> statements = normalizationStatements.get(emotion);
        if (statements != null && !statements.isEmpty()) {
            return statements.get(random.nextInt(statements.size()));
        }
        return "You're not alone in feeling this way.";
    }
    
    /**
     * Generates a complete emotional mirror response
     * @param input User input
     * @return Complete empathetic response
     */
    public String generateEmotionalMirror(String input) {
        EmotionalMirrorResponse response = reflectEmotion(input);
        
        StringBuilder sb = new StringBuilder();
        sb.append(response.getValidationMessage()).append("\n\n");
        sb.append(response.getNormalizationMessage());
        
        return sb.toString();
    }
    
    // ==================== SUPPORTIVE PRESENCE PUBLIC METHODS ====================
    
    /**
     * Gets an "I'm here" message
     * @param type The type of message (general, distress, comfort)
     * @return A supportive "I'm here" message
     */
    public String getImHereMessage(String type) {
        List<String> messages = imHereMessages.get(type);
        if (messages != null && !messages.isEmpty()) {
            return messages.get(random.nextInt(messages.size()));
        }
        // Fallback to general messages
        List<String> generalMessages = imHereMessages.get("general");
        return generalMessages != null && !generalMessages.isEmpty()
            ? generalMessages.get(random.nextInt(generalMessages.size()))
            : "I'm here with you.";
    }
    
    /**
     * Gets a random "I'm here" message
     * @return A supportive "I'm here" message
     */
    public String getRandomImHereMessage() {
        String[] types = {"general", "distress", "comfort"};
        String type = types[random.nextInt(types.length)];
        return getImHereMessage(type);
    }
    
    /**
     * Gets a physical space acknowledgment
     * @param type The type (virtual_presence, space, grounding)
     * @return A physical space acknowledgment message
     */
    public String getPhysicalSpaceAcknowledgment(String type) {
        List<String> acknowledgments = physicalSpaceAcknowledgments.get(type);
        if (acknowledgments != null && !acknowledgments.isEmpty()) {
            return acknowledgments.get(random.nextInt(acknowledgments.size()));
        }
        // Fallback
        List<String> general = physicalSpaceAcknowledgments.get("virtual_presence");
        return general != null && !general.isEmpty()
            ? general.get(random.nextInt(general.size()))
            : "Even though we can't be in the same room, I'm virtually right there with you.";
    }
    
    /**
     * Gets a random physical space acknowledgment
     * @return A physical space acknowledgment message
     */
    public String getRandomPhysicalSpaceAcknowledgment() {
        String[] types = {"virtual_presence", "space", "grounding"};
        String type = types[random.nextInt(types.length)];
        return getPhysicalSpaceAcknowledgment(type);
    }
    
    /**
     * Gets a time acknowledgment
     * @param type The type (patience, duration, moment)
     * @return A time acknowledgment message
     */
    public String getTimeAcknowledgment(String type) {
        List<String> acknowledgments = timeAcknowledgments.get(type);
        if (acknowledgments != null && !acknowledgments.isEmpty()) {
            return acknowledgments.get(random.nextInt(acknowledgments.size()));
        }
        // Fallback
        List<String> patience = timeAcknowledgments.get("patience");
        return patience != null && !patience.isEmpty()
            ? patience.get(random.nextInt(patience.size()))
            : "Take all the time you need.";
    }
    
    /**
     * Gets a random time acknowledgment
     * @return A time acknowledgment message
     */
    public String getRandomTimeAcknowledgment() {
        String[] types = {"patience", "duration", "moment"};
        String type = types[random.nextInt(types.length)];
        return getTimeAcknowledgment(type);
    }
    
    /**
     * Generates a complete supportive presence message
     * @param emotion The detected emotion
     * @return A complete supportive presence message
     */
    public String generateSupportivePresence(EmotionDetector.Emotion emotion) {
        StringBuilder sb = new StringBuilder();
        
        // Add "I'm here" message
        if (emotion == EmotionDetector.Emotion.SAD || emotion == EmotionDetector.Emotion.LONELY ||
            emotion == EmotionDetector.Emotion.ANXIOUS || emotion == EmotionDetector.Emotion.STRESSED) {
            sb.append(getImHereMessage("distress"));
        } else {
            sb.append(getRandomImHereMessage());
        }
        sb.append(" ");
        
        // Add physical space acknowledgment (50% chance)
        if (random.nextBoolean()) {
            sb.append(getRandomPhysicalSpaceAcknowledgment());
            sb.append(" ");
        }
        
        // Add time acknowledgment
        sb.append(getRandomTimeAcknowledgment());
        
        return sb.toString();
    }
}

