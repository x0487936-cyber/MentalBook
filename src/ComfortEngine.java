import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Arrays;
import java.util.List;

public class ComfortEngine {
    public enum ToneStyle {
        SOFT,
        BALANCED,
        ENCOURAGING,
        EMPATHETIC,
        REASSURING,
        DIRECT,
        UPLIFTING,
        CALMING,
        VALIDATING,
        HOPEFUL
    }
    
    // ==========================
    // Validation Scripts
    // ==========================
    private static final String[] VALIDATION_SCRIPTS = {
        "Your feelings are completely valid. What you're experiencing matters.",
        "There's no right or wrong way to feel about this. Whatever you're feeling makes sense.",
        "I hear you. What you're going through is real and important.",
        "Your reaction makes complete sense given what you've described.",
        "It makes sense that you'd feel this way. Anyone in your situation would.",
        "You don't need to justify your feelings - they're valid just as they are.",
        "What you're experiencing is real, and it's okay to feel exactly how you feel.",
        "I believe you. Your feelings are legitimate and deserve acknowledgment."
    };
    
    // ==========================
    // Normalization Phrases
    // ==========================
    private static final String[] NORMALIZATION_PHRASES = {
        "Many people feel this way, especially during difficult times.",
        "You're not alone in this. Lots of people experience similar feelings.",
        "This is a very common human experience - you're completely normal.",
        "What you're feeling is a natural response to your situation.",
        "It's okay to struggle sometimes. That's part of being human.",
        "You'd be surprised how many people feel exactly the same way.",
        "This doesn't make you broken - it makes you human.",
        "Feeling overwhelmed doesn't mean you're failing. It means you're carrying a lot."
    };
    
    // ==========================
    // Encouragement Patterns
    // ==========================
    private static final String[] ENCOURAGEMENT_PATTERNS = {
        "You've gotten through hard times before, and you can do it again.",
        "Take it one moment at a time. You don't have to have everything figured out right now.",
        "Your strength is greater than you realize. You've already shown resilience.",
        "Be gentle with yourself. You're doing the best you can.",
        "This feeling is temporary, even though it feels permanent right now.",
        "You have what it takes to get through this. I believe in you.",
        "Small steps still move you forward. Every little bit counts.",
        "It's okay to not be okay right now. What's important is that you keep going."
    };
    
    // ==========================
    // Virtual Support Phrases
    // ==========================
    private static final String[] VIRTUAL_SUPPORT_PHRASES = {
        "I'm here with you, even if just through this screen.",
        "Know that you're not alone in this moment. I'm right here.",
        "Even though we can't be in the same room, I want you to feel supported.",
        "I'm listening, and I care about what you're going through."
    };
    
    // ==========================
    // Safe Space Phrases
    // ==========================
    private static final String[] SAFE_SPACE_PHRASES = {
        "This is a judgment-free zone. You can say anything here.",
        "There's no pressure here. Take your time to share what feels comfortable.",
        "You can be completely honest with me. This is a safe space.",
        "Whatever you need to express, it's welcome here."
    };
    
    // ==========================
    // Non-Judgmental Phrases
    // ==========================
    private static final String[] NON_JUDGMENTAL_PHRASES = {
        "There's no judgment here. Whatever you're going through is valid.",
        "You don't have to explain yourself or justify your feelings.",
        "I'm not here to judge - only to listen and support you.",
        "No matter what you've done or thought, you're safe here with me.",
        "This is a completely judgment-free space. You can be yourself.",
        "You don't need to filter your thoughts or worry about being judged.",
        "Whatever you share, it stays between us. There's no criticism here.",
        "I'm on your side, no matter what. There's no judgment in my support."
    };
    
    // ==========================
    // Healing Support - Gradual Support Levels
    // ==========================
    private static final String[] GRADUAL_SUPPORT_LEVELS = {
        // Mild support - gentle encouragement
        "Take things one step at a time. You've got this.",
        "It's okay to go at your own pace. Every small effort counts.",
        "Be patient with yourself. Healing isn't a race.",
        "You don't have to do everything at once. That's enough for now.",
        // Moderate support - more involved
        "I'm here with you through this. Let's work through it together.",
        "You're stronger than you know. I've seen you handle difficult things.",
        "This is hard, but you don't have to face it alone.",
        "I'm proud of you for reaching out. That takes courage.",
        // Strong support - intensive comfort
        "I believe in you completely. You can get through this.",
        "You've already shown so much strength. Keep going.",
        "No matter how dark things feel right now, there's a path forward.",
        "Your resilience is remarkable. You've got what it takes."
    };
    
    // ==========================
    // Healing Support - Progress Acknowledgment
    // ==========================
    private static final String[] PROGRESS_ACKNOWLEDGMENT = {
        "I notice you've been working through some difficult things. That's meaningful.",
        "The fact that you're still here, still trying - that matters.",
        "You've already taken an important step just by talking about this.",
        "Recognize how far you've come, even if the destination feels far away.",
        "Each conversation like this is building something important.",
        "Your willingness to process this shows real growth.",
        "I see your effort, even when results feel small. That effort is huge.",
        "You've shown real strength in facing this. Don't underestimate that.",
        "The progress you're making, even the invisible kind, is real progress.",
        "Keep going - you're doing better than you might realize."
    };
    
    // ==========================
    // Healing Support - Hope Instillation
    // ==========================
    private static final String[] HOPE_INSTILLATION = {
        "Tomorrow holds possibilities you can't see right now.",
        "This feeling isn't permanent - feelings change, and so can this.",
        "There's light on the other side of this. I've seen it in others.",
        "Small moments of peace are possible, even now.",
        "You deserve good things, and they're more possible than they feel.",
        "Healing isn't linear, but every step forward counts.",
        "Your story isn't over - there are chapters yet to write.",
        "Better days are coming, even if they feel far away right now.",
        "Hope is already alive in you - you reached out, which proves it.",
        "Things can and will get better. This moment isn't your whole story."
    };

    private final Map<EmotionDetector.Emotion, ReassuranceStrategy> strategies;
    private final Random random;

    public ComfortEngine() {
        this.strategies = new HashMap<>();
        this.random = new Random();

        registerStrategies();
    }

    private void registerStrategies() {
        strategies.put(EmotionDetector.Emotion.SAD, new SadnessStrategy());
        strategies.put(EmotionDetector.Emotion.ANXIOUS, new AnxietyStrategy());
        strategies.put(EmotionDetector.Emotion.STRESSED, new StressStrategy());
        strategies.put(EmotionDetector.Emotion.FRUSTRATED, new FrustrationStrategy());
        strategies.put(EmotionDetector.Emotion.OVERWHELMED, new OverwhelmStrategy());
    }

    public String generateComfort(
            EmotionDetector.EmotionResult emotionResult,
            ConversationContext context,
            ToneStyle tone
    ) {

        if (emotionResult == null) {
            return gentlePresence();
        }

        EmotionDetector.Emotion emotion = emotionResult.getPrimaryEmotion();
        double intensity = emotionResult.getConfidence();

        // Severe distress detection safeguard
        if (isSevereDistress(context)) {
            return supportiveEscalation();
        }

        ReassuranceStrategy strategy =
                strategies.getOrDefault(emotion, new NeutralStrategy());

        String baseResponse = strategy.generate(intensity, tone);

        // Context-aware reinforcement
        if (context != null && context.getTurnCount() > 4) {
            baseResponse += " I can tell this has been on your mind for a while.";
        }
        
        // Add virtual support elements for enhanced comfort
        // Include non-judgmental framing and safe space creation
        String virtualSupportElement = "";
        int supportType = random.nextInt(5);
        switch (supportType) {
            case 0:
                virtualSupportElement = getNonJudgmentalPhrase();
                break;
            case 1:
                virtualSupportElement = getSafeSpacePhrase();
                break;
            case 2:
                virtualSupportElement = getVirtualSupportPhrase();
                break;
            case 3:
                // Add healing support - progress acknowledgment
                virtualSupportElement = getProgressAcknowledgment();
                break;
            case 4:
                // Add healing support - hope instillation
                virtualSupportElement = getHopeInstillation();
                break;
        }
        
        return baseResponse + " " + virtualSupportElement;
    }

    private boolean isSevereDistress(ConversationContext context) {
        if (context == null) return false;

        String recent = context.getLastUserInput();
        if (recent == null) return false;

        recent = recent.toLowerCase();

        return recent.contains("hopeless")
                || recent.contains("worthless")
                || recent.contains("no point")
                || recent.contains("give up");
    }

    private String supportiveEscalation() {
        return "It sounds like things feel incredibly heavy right now. "
                + "You matter more than you realize, and you deserve real support. "
                + "If you can, consider reaching out to someone you trust or a professional who can help you through this.";
    }

    private String gentlePresence() {
        return "I'm here with you. Whatever you're feeling, we can talk through it.";
    }

    // ==========================
    // Strategy Pattern
    // ==========================

    private interface ReassuranceStrategy {
        String generate(double intensity, ToneStyle tone);
    }

    private class SadnessStrategy implements ReassuranceStrategy {
        public String generate(double intensity, ToneStyle tone) {
            if (intensity > 0.75) {
                return "I'm really sorry you're carrying this much pain. "
                        + "It makes sense that you'd feel this way, and you're not alone in it.";
            }
            return "It's okay to have days like this. "
                    + "Your feelings are valid, and they don't define who you are.";
        }
    }

    private class AnxietyStrategy implements ReassuranceStrategy {
        public String generate(double intensity, ToneStyle tone) {
            if (intensity > 0.75) {
                return "That sounds overwhelming. "
                        + "Take one small breath and focus on what's directly in front of you. "
                        + "You are capable of getting through this moment.";
            }
            return "Anxiety can make everything feel bigger than it is. "
                    + "Let’s slow it down together.";
        }
    }

    private class StressStrategy implements ReassuranceStrategy {
        public String generate(double intensity, ToneStyle tone) {
            if (intensity > 0.75) {
                return "You're carrying a lot right now. "
                        + "Anyone in your position would feel pressure. "
                        + "Let's break this into smaller pieces.";
            }
            return "It sounds like things are piling up. "
                    + "One step at a time is more than enough.";
        }
    }

    private class FrustrationStrategy implements ReassuranceStrategy {
        public String generate(double intensity, ToneStyle tone) {
            if (intensity > 0.75) {
                return "That level of frustration makes sense. "
                        + "You care about this, and that’s why it hits hard.";
            }
            return "It’s frustrating when things don’t go the way you expect. "
                    + "That doesn’t mean you’ve failed.";
        }
    }

    private class OverwhelmStrategy implements ReassuranceStrategy {
        public String generate(double intensity, ToneStyle tone) {
            if (intensity > 0.75) {
                return "When everything feels like too much, it’s okay to pause. "
                        + "You don’t have to solve everything at once.";
            }
            return "Feeling overwhelmed doesn’t mean you're incapable. "
                    + "It just means you need space to breathe.";
        }
    }

    private class NeutralStrategy implements ReassuranceStrategy {
        public String generate(double intensity, ToneStyle tone) {
            return "I'm here with you. Whatever you're feeling, it matters.";
        }
    }
    
    // ==========================
    // Public Methods for Accessing Patterns
    // ==========================
    
    /**
     * Get a random validation script
     */
    public String getValidationScript() {
        return VALIDATION_SCRIPTS[random.nextInt(VALIDATION_SCRIPTS.length)];
    }
    
    /**
     * Get a random normalization phrase
     */
    public String getNormalizationPhrase() {
        return NORMALIZATION_PHRASES[random.nextInt(NORMALIZATION_PHRASES.length)];
    }
    
    /**
     * Get a random encouragement pattern
     */
    public String getEncouragementPattern() {
        return ENCOURAGEMENT_PATTERNS[random.nextInt(ENCOURAGEMENT_PATTERNS.length)];
    }
    
    /**
     * Get a random virtual support phrase
     */
    public String getVirtualSupportPhrase() {
        return VIRTUAL_SUPPORT_PHRASES[random.nextInt(VIRTUAL_SUPPORT_PHRASES.length)];
    }
    
    /**
     * Get a random safe space phrase
     */
    public String getSafeSpacePhrase() {
        return SAFE_SPACE_PHRASES[random.nextInt(SAFE_SPACE_PHRASES.length)];
    }
    
    /**
     * Get a random non-judgmental phrase
     */
    public String getNonJudgmentalPhrase() {
        return NON_JUDGMENTAL_PHRASES[random.nextInt(NON_JUDGMENTAL_PHRASES.length)];
    }
    
    /**
     * Get a random gradual support level phrase
     * @param level Support intensity: 0 = mild, 1 = moderate, 2 = strong
     */
    public String getGradualSupportLevel(int level) {
        // Map level to indices in the array:
        // 0-3: mild, 4-7: moderate, 8-11: strong
        int startIndex = level * 4;
        int endIndex = startIndex + 4;
        if (startIndex >= GRADUAL_SUPPORT_LEVELS.length) {
            startIndex = 0;
            endIndex = 4;
        }
        if (endIndex > GRADUAL_SUPPORT_LEVELS.length) {
            endIndex = GRADUAL_SUPPORT_LEVELS.length;
        }
        return GRADUAL_SUPPORT_LEVELS[startIndex + random.nextInt(endIndex - startIndex)];
    }
    
    /**
     * Get a random gradual support level phrase (random level)
     */
    public String getGradualSupportLevel() {
        return GRADUAL_SUPPORT_LEVELS[random.nextInt(GRADUAL_SUPPORT_LEVELS.length)];
    }
    
    /**
     * Get a random progress acknowledgment phrase
     */
    public String getProgressAcknowledgment() {
        return PROGRESS_ACKNOWLEDGMENT[random.nextInt(PROGRESS_ACKNOWLEDGMENT.length)];
    }
    
    /**
     * Get a random hope instillation phrase
     */
    public String getHopeInstillation() {
        return HOPE_INSTILLATION[random.nextInt(HOPE_INSTILLATION.length)];
    }
    
    /**
     * Get a combined reassurance response using validation, normalization, and encouragement
     */
    public String getFullReassurance() {
        String validation = getValidationScript();
        String normalization = getNormalizationPhrase();
        String encouragement = getEncouragementPattern();
        String virtualSupport = getVirtualSupportPhrase();
        String safeSpace = getSafeSpacePhrase();
        String nonJudgmental = getNonJudgmentalPhrase();
        String gradualSupport = getGradualSupportLevel();
        String progressAck = getProgressAcknowledgment();
        String hope = getHopeInstillation();
        
        return validation + " " + normalization + " " + safeSpace + " " + nonJudgmental + " " + virtualSupport + " " + gradualSupport + " " + progressAck + " " + hope + " " + encouragement;
    }
}
