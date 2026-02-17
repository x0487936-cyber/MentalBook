import java.util.*;
import java.util.regex.*;

/**
 * Wisdom Engine for VirtualXander
 *
 * Provides life wisdom, observations, and guidance based on 15 categories
 * of human experience and growth.
 */
public class WisdomEngine {

    private Random random;

    public enum WisdomCategory {
        SELF_AWARENESS("self_awareness", "Self-awareness and personal growth"),
        RELATIONSHIPS("relationships", "Relationship dynamics and connection"),
        SUCCESS("success", "Achievement and goal-setting insights"),
        FAILURE_RESILIENCE("failure_resilience", "Learning from setbacks"),
        HAPPINESS("happiness", "Finding joy and contentment"),
        TIME_PRIORITIES("time_priorities", "Time management and priorities"),
        COMMUNICATION("communication", "Communication and listening"),
        CHANGE_UNCERTAINTY("change_uncertainty", "Navigating change"),
        MINDFULNESS("mindfulness", "Present moment awareness"),
        PURPOSE_MEANING("purpose_meaning", "Finding purpose"),
        WORK_CAREER("work_career", "Professional growth"),
        MONEY_MATERIAL("money_material", "Financial wisdom"),
        HEALTH_BODY("health_body", "Physical and mental health"),
        CREATIVITY("creativity", "Creative expression"),
        WISDOM_LEARNING("wisdom_learning", "Lifelong learning");

        private final String name;
        private final String description;

        WisdomCategory(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
    }

    public static class LifeObservation {
        private String observation;
        private WisdomCategory category;
        private String theme;
        private int applicabilityScore;
        private String source;

        public LifeObservation(String observation, WisdomCategory category, String theme, int applicabilityScore, String source) {
            this.observation = observation;
            this.category = category;
            this.theme = theme;
            this.applicabilityScore = applicabilityScore;
            this.source = source;
        }

        public String getObservation() { return observation; }
        public WisdomCategory getCategory() { return category; }
        public String getTheme() { return theme; }
        public int getApplicabilityScore() { return applicabilityScore; }
        public String getSource() { return source; }
    }

    private List<LifeObservation> wisdomObservations;
    private Map<String, String[]> practicalSteps;
    private Map<String, String[]> resourceRecommendations;

    public WisdomEngine() {
        this.random = new Random();
        this.wisdomObservations = new ArrayList<>();
        this.practicalSteps = new HashMap<>();
        this.resourceRecommendations = new HashMap<>();
        initializeWisdomObservations();
        initializePracticalGuidance();
    }

    private void initializeWisdomObservations() {
        initializeSelfAwareness();
        initializeRelationships();
        initializeSuccess();
        initializeFailureResilience();
        initializeHappiness();
        initializeTimePriorities();
        initializeCommunication();
        initializeChangeUncertainty();
        initializeMindfulness();
        initializePurposeMeaning();
        initializeWorkCareer();
        initializeMoneyMaterial();
        initializeHealthBody();
        initializeCreativity();
        initializeWisdomLearning();
    }

    private void initializeSelfAwareness() {
        String[][] obs = {
            {"The first step to change is awareness. The second step is acceptance.", "personal_growth", "9", "ancient_wisdom"},
            {"You are not your thoughts. You are the observer of your thoughts.", "mindfulness", "8", "meditation"},
            {"Your patterns become your prison. Awareness becomes your freedom.", "behavior_patterns", "9", "psychology"},
            {"The most important relationship you have is with yourself.", "self_relationship", "10", "development"},
            {"Self-awareness is not about being perfect. It's about being honest.", "authenticity", "8", "coaching"},
            {"What you resist persists. What you accept transforms.", "acceptance", "9", "therapy"},
            {"The gap between stimulus and response is where your freedom lies.", "choice", "9", "frankl"},
            {"You cannot change what you do not acknowledge.", "acknowledgment", "10", "recovery"},
            {"Self-compassion is not weakness. It's the foundation of true strength.", "compassion", "9", "wisdom"},
            {"The stories you tell yourself become your reality.", "narrative", "9", "therapy"},
            {"Growth happens in the uncomfortable space between who you are and who you want to be.", "growth", "9", "wisdom"},
            {"Your values are revealed not by what you say, but by what you do.", "values", "9", "philosophy"}
        };
        addObservations(WisdomCategory.SELF_AWARENESS, obs);
    }

    private void initializeRelationships() {
        String[][] obs = {
            {"The quality of your relationships determines the quality of your life.", "quality", "10", "development"},
            {"Love is not about finding the perfect person.", "love", "9", "wisdom"},
            {"Communication is the lifeblood of any relationship.", "communication", "10", "counseling"},
            {"Trust is built in drops and lost in buckets.", "trust", "9", "dynamics"},
            {"The best relationships are built on mutual respect, not control.", "respect", "9", "health"},
            {"You cannot change others, but you can change how you respond.", "response", "8", "intelligence"},
            {"Boundaries are the distance at which I can love you and me simultaneously.", "boundaries", "9", "therapy"},
            {"The deepest connections happen when vulnerability meets vulnerability.", "vulnerability", "8", "brown"},
            {"Forgiveness doesn't mean forgetting.", "forgiveness", "9", "healing"},
            {"People come into your life for a reason, a season, or a lifetime.", "purpose", "8", "lessons"},
            {"The most important relationship skill is the ability to repair.", "repair", "9", "expertise"},
            {"Love without action is just a word.", "action", "9", "wisdom"},
            {"The best apology is changed behavior.", "apology", "8", "repair"},
            {"Intimacy is created through shared vulnerability.", "intimacy", "8", "therapy"}
        };
        addObservations(WisdomCategory.RELATIONSHIPS, obs);
    }

    private void initializeSuccess() {
        String[][] obs = {
            {"Success is not the key to happiness. Happiness is the key to success.", "happiness", "9", "schweitzer"},
            {"Success is not final, failure is not fatal.", "resilience", "10", "churchill"},
            {"The only way to do great work is to love what you do.", "passion", "9", "jobs"},
            {"Success is walking from failure to failure with no loss of enthusiasm.", "persistence", "9", "churchill"},
            {"Success is not about being the best. It's about getting better.", "improvement", "9", "wisdom"},
            {"Your success will be determined by your own confidence.", "confidence", "8", "obama"},
            {"Success is the sum of small efforts, repeated day in and day out.", "effort", "9", "collier"},
            {"The road to success is always under construction.", "journey", "8", "metaphor"},
            {"Success is liking yourself, liking what you do.", "satisfaction", "8", "angelou"}
        };
        addObservations(WisdomCategory.SUCCESS, obs);
    }

    private void initializeFailureResilience() {
        String[][] obs = {
            {"Failure is not the opposite of success. It is part of success.", "perspective", "9", "wisdom"},
            {"Our greatest glory is not in never failing, but in rising every time we fall.", "rising", "10", "confucius"},
            {"Failure is a detour, not a dead end.", "detour", "9", "resilience"},
            {"The only real failure is the failure to try.", "attempt", "9", "motivation"},
            {"Resilience is choosing to continue despite hardship.", "resilience", "9", "psychology"},
            {"Every setback is a setup for a comeback.", "comeback", "9", "wisdom"},
            {"Your failure is not your identity.", "identity", "9", "therapy"},
            {"Failure teaches you what success cannot.", "strength", "9", "growth"},
            {"Courage is not the absence of fear, but action in spite of fear.", "courage", "8", "leadership"},
            {"Fall seven times, stand up eight.", "persistence", "10", "proverb"},
            {"Failure is the fertilizer of success.", "growth", "9", "metaphor"}
        };
        addObservations(WisdomCategory.FAILURE_RESILIENCE, obs);
    }

    private void initializeHappiness() {
        String[][] obs = {
            {"Happiness is not a destination. It's a way of traveling.", "journey", "9", "philosophy"},
            {"The biggest secret to happiness is wanting what you already have.", "contentment", "10", "wisdom"},
            {"Happiness is contagious. Spread it generously.", "contagious", "8", "psychology"},
            {"Your happiness is your responsibility.", "responsibility", "9", "self_help"},
            {"Happiness comes from within.", "within", "9", "philosophy"},
            {"The less you care about what others think, the happier you become.", "freedom", "9", "wisdom"},
            {"Joy is not in things. Joy is in the way we see things.", "perspective", "9", "teaching"},
            {"Happiness is not having what you want, but wanting what you have.", "gratitude", "10", "wisdom"},
            {"Choose to be happy. It's a decision.", "choice", "9", "motivation"},
            {"Happiness is a state of mind.", "mindset", "9", "psychology"},
            {"Do not take life too seriously.", "humor", "8", "hubbard"},
            {"Happiness is found in helping others.", "service", "9", "purpose"}
        };
        addObservations(WisdomCategory.HAPPINESS, obs);
    }

    private void initializeTimePriorities() {
        String[][] obs = {
            {"Yesterday is gone. Tomorrow is not promised. Today is all you have.", "present", "10", "wisdom"},
            {"The way you spend your time defines who you become.", "investment", "9", "development"},
            {"Priorities don't just happen. You choose them deliberately.", "intentionality", "9", "productivity"},
            {"Time is the one resource you can never get back.", "irreplaceable", "10", "lessons"},
            {"No is a complete sentence. Use it to protect your time.", "boundaries", "9", "wisdom"},
            {"What you tolerate, you prioritize.", "focus", "8", "organization"},
            {"The urgent is seldom important.", "eisenhower", "9", "wisdom"},
            {"If it won't matter in five years, don't spend five minutes upset.", "perspective", "8", "productivity"},
            {"The best time to plant a tree was 20 years ago. The second best is now.", "action", "9", "proverb"},
            {"Multitasking is a myth. Focus on one thing at a time.", "attention", "8", "wisdom"}
        };
        addObservations(WisdomCategory.TIME_PRIORITIES, obs);
    }

    private void initializeCommunication() {
        String[][] obs = {
            {"Listen more than you speak. You have two ears and one mouth for a reason.", "listening", "10", "wisdom"},
            {"The most important thing is hearing what isn't said.", "intuition", "9", "experts"},
            {"Words are powerful. Choose them carefully.", "power", "9", "wisdom"},
            {"Before you speak: Is it true? Is it kind? Is it necessary?", "filters", "9", "buddhist"},
            {"Most people listen with the intent to reply, not to understand.", "deep_listening", "9", "covey"},
            {"Communication works for those who work at it.", "effort", "9", "wisdom"},
            {"The biggest problem in communication is the illusion it has taken place.", "clarity", "9", "barnard"},
            {"Express yourself clearly. Ambiguity creates misunderstanding.", "clarity", "8", "skills"},
            {"It's not what you say, but how you say it.", "tone", "9", "wisdom"},
            {"Silence is a powerful form of communication.", "nonverbal", "8", "wisdom"},
            {"Clear is kind. Unclear is unkind.", "kindness", "9", "brown"}
        };
        addObservations(WisdomCategory.COMMUNICATION, obs);
    }

    private void initializeChangeUncertainty() {
        String[][] obs = {
            {"The only constant in life is change.", "acceptance", "9", "buddhist"},
            {"Uncertainty is the fertile ground for growth.", "growth", "9", "wisdom"},
            {"Change is not something to fear. It's something to navigate.", "navigation", "8", "resilience"},
            {"The bamboo that bends survives the storm.", "flexibility", "9", "metaphor"},
            {"Uncertainty is an invitation to create meaning.", "meaning", "8", "philosophy"},
            {"Resist nothing. Flow with life like water.", "flow", "9", "taoism"},
            {"The map is not the territory.", "adaptability", "9", "models"},
            {"Control what you can. Accept what you can't.", "control", "10", "stoicism"},
            {"The obstacle is the path.", "reframing", "9", "philosophy"},
            {"In the midst of chaos, there is also opportunity.", "opportunity", "8", "wisdom"},
            {"Flexibility is the key to survival.", "survival", "9", "wisdom"},
            {"The only thing predictable is unpredictability.", "reality", "8", "acceptance"}
        };
        addObservations(WisdomCategory.CHANGE_UNCERTAINTY, obs);
    }

    private void initializeMindfulness() {
        String[][] obs = {
            {"The present moment is the only moment that is real.", "presence", "10", "tolle"},
            {"Wherever you are, be there totally.", "attention", "9", "wisdom"},
            {"The breath is the anchor to the present moment.", "breath", "9", "meditation"},
            {"Don't believe everything you think. Thoughts are just thoughts.", "observer", "8", "mindfulness"},
            {"The mind is a wonderful servant but a terrible master.", "mastery", "9", "wisdom"},
            {"Small moments of presence add up to a life of peace.", "accumulation", "8", "practice"},
            {"You can't stop the waves, but you can learn to surf.", "surfing", "9", "kabir"},
            {"Peace comes from within.", "inner_peace", "9", "buddha"},
            {"Mindfulness is the art of being fully alive to each moment.", "aliveness", "8", "life"},
            {"Between stimulus and response there is a space. In that space is your power.", "choice", "9", "frankl"}
        };
        addObservations(WisdomCategory.MINDFULNESS, obs);
    }

    private void initializePurposeMeaning() {
        String[][] obs = {
            {"The purpose of life is not to be happy. It is to be useful.", "purpose", "9", "emerson"},
            {"Your work is to discover your work and give yourself to it.", "calling", "9", "buddha"},
            {"The meaning of life is to find your gift. The purpose is to give it away.", "gift", "10", "picasso"},
            {"Don't ask what the world needs. Ask what makes you alive.", "alive", "9", "thurman"},
            {"Purpose is created through meaningful action.", "creation", "8", "wisdom"},
            {"The strongest people stand for something.", "integrity", "9", "leadership"},
            {"Your purpose is about the contribution you make.", "contribution", "9", "service"},
            {"Meaning is found in the journey, not the destination.", "journey", "8", "philosophy"},
            {"When you align actions with values, meaning follows.", "alignment", "9", "values"},
            {"Legacy is what you create in others.", "legacy", "8", "relationships"}
        };
        addObservations(WisdomCategory.PURPOSE_MEANING, obs);
    }

    private void initializeWorkCareer() {
        String[][] obs = {
            {"Choose a job you love, and you will never work a day in your life.", "passion", "9", "confucius"},
            {"The only way to do great work is to love what you do.", "love_work", "10", "jobs"},
            {"Your career is a marathon, not a sprint.", "long_term", "9", "wisdom"},
            {"Don't be afraid to fail. Be afraid of not trying.", "risk", "9", "business"},
            {"The best investment is in yourself.", "investment", "9", "learning"},
            {"Your network is your net worth.", "networking", "8", "career"},
            {"Your reputation takes years to build and seconds to destroy.", "reputation", "9", "wisdom"},
            {"Work hard in silence. Let success be your noise.", "hard_work", "8", "wisdom"},
            {"The expert has failed more times than the beginner has tried.", "expertise", "9", "wisdom"}
        };
        addObservations(WisdomCategory.WORK_CAREER, obs);
    }

    private void initializeMoneyMaterial() {
        String[][] obs = {
            {"Money is a tool, not a goal.", "tool", "9", "wisdom"},
            {"The richest people are not those with the most, but those who need the least.", "contentment", "10", "wisdom"},
            {"We buy things we don't need with money we don't have to impress people we don't like.", "consumerism", "9", "wisdom"},
            {"Financial peace isn't the acquisition of stuff. It's learning to live on less than you make.", "peace", "9", "ramsey"},
            {"Money is a servant but a dangerous master.", "mastery", "8", "wisdom"},
            {"The biggest risk is not taking any risk.", "risk", "9", "business"},
            {"It's not how much money you make, but how much you keep.", "keeping", "8", "wisdom"},
            {" Wealth is not about abundance. It's about having enough.", "enough", "9", "wisdom"}
        };
        addObservations(WisdomCategory.MONEY_MATERIAL, obs);
    }

    private void initializeHealthBody() {
        String[][] obs = {
            {"Health is wealth. Nothing matters without it.", "wealth", "10", "wisdom"},
            {"Your body is your temple. Keep it pure and clean.", "temple", "9", "wisdom"},
            {"The greatest wealth is health.", "wealth", "10", "virgil"},
            {"Take care of your body. It's the only place you have to live.", "home", "9", "wisdom"},
            {"Early to bed, early to rise, makes a person healthy, wealthy, and wise.", "routine", "8", "proverb"},
            {"A healthy outside starts from the inside.", "inside", "9", "wisdom"},
            {"Your health is an investment, not an expense.", "investment", "8", "wisdom"},
            {"The body achieves what the mind believes.", "belief", "9", "wisdom"},
            {"Rest is not idleness. It's essential for productivity.", "rest", "8", "wisdom"}
        };
        addObservations(WisdomCategory.HEALTH_BODY, obs);
    }

    private void initializeCreativity() {
        String[][] obs = {
            {"Creativity is intelligence having fun.", "intelligence", "9", "einstein"},
            {"The creative adult is the child who survived.", "childhood", "9", "wisdom"},
            {"Imagination is more important than knowledge.", "imagination", "9", "einstein"},
            {"Creativity is seeing what others see and thinking what others haven't thought.", "vision", "9", "wisdom"},
            {"Every child is an artist. The problem is how to remain an artist once we grow up.", "child", "8", "picasso"},
            {"You can't use up creativity. The more you use, the more you have.", "abundance", "9", "angelou"},
            {"Creativity is the way I share my soul with the world.", "soul", "8", "wisdom"},
            {"Do what you can, with what you have, where you are.", "action", "9", "roosevelt"},
            {"There are no rules for creativity. Maybe that's the point.", "rules", "8", "wisdom"}
        };
        addObservations(WisdomCategory.CREATIVITY, obs);
    }

    private void initializeWisdomLearning() {
        String[][] obs = {
            {"Live as if you were to die tomorrow. Learn as if you were to live forever.", "learning", "10", "gandhi"},
            {"Wisdom begins in wonder.", "wonder", "9", "socrates"},
            {"The more you know, the more you realize you don't know.", "humility", "9", "wisdom"},
            {"An investment in knowledge pays the best interest.", "investment", "9", "franklin"},
            {"Wisdom is not a product of schooling but of lifelong learning.", "schooling", "9", "nobel"},
            {"Knowledge speaks, but wisdom listens.", "listening", "10", "gains"},
            {"The wise are those who know they don't know.", "humility", "9", "wisdom"},
            {"Learn from the mistakes of others. You can't live long enough to make them all yourself.", "mistakes", "9", "roosevelt"},
            {"Wisdom is the reward for surviving our own stupidity.", "survival", "8", "wisdom"},
            {"The journey of a thousand miles begins with a single step.", "journey", "10", "lao"}
        };
        addObservations(WisdomCategory.WISDOM_LEARNING, obs);
    }

    private void addObservations(WisdomCategory category, String[][] data) {
        for (String[] item : data) {
            wisdomObservations.add(new LifeObservation(
                item[0], category, item[1], Integer.parseInt(item[2]), item[3]
            ));
        }
    }

    // ==================== CORE METHODS ====================

    public String getRandomWisdom() {
        return wisdomObservations.get(random.nextInt(wisdomObservations.size())).getObservation();
    }

    public String getWisdomByCategory(WisdomCategory category) {
        List<String> relevant = new ArrayList<>();
        for (LifeObservation obs : wisdomObservations) {
            if (obs.getCategory() == category) {
                relevant.add(obs.getObservation());
            }
        }
        if (relevant.isEmpty()) return getRandomWisdom();
        return relevant.get(random.nextInt(relevant.size()));
    }

    public String getWisdomForSituation(String situation) {
        String lower = situation.toLowerCase();
        if (lower.contains("fail") || lower.contains("wrong") || lower.contains("mistake")) {
            return getWisdomByCategory(WisdomCategory.FAILURE_RESILIENCE);
        }
        if (lower.contains("happy") || lower.contains("joy") || lower.contains("good")) {
            return getWisdomByCategory(WisdomCategory.HAPPINESS);
        }
        if (lower.contains("busy") || lower.contains("time") || lower.contains("when")) {
            return getWisdomByCategory(WisdomCategory.TIME_PRIORITIES);
        }
        if (lower.contains("relate") || lower.contains("friend") || lower.contains("love")) {
            return getWisdomByCategory(WisdomCategory.RELATIONSHIPS);
        }
        return getRandomWisdom();
    }

    public String getPerspectiveShift(String context) {
        String[] templates = {
            "Have you considered...",
            "One way to look at it...",
            "Different people might say..."
        };
        
        String template = templates[random.nextInt(templates.length)];
        String wisdom = getRandomWisdom();
        
        if (template.equals("Have you considered...")) {
            return "Have you considered that " + wisdom.substring(0, Math.min(100, wisdom.length())) + "?";
        } else if (template.equals("One way to look at it...")) {
            return "One way to look at it: " + wisdom;
        } else {
            return "Different people might say that " + wisdom.toLowerCase();
        }
    }

    public String getMoodAdaptedAdvice(String mood) {
        String lower = mood.toLowerCase();
        if (lower.contains("sad") || lower.contains("down") || lower.contains("low")) {
            return getWisdomByCategory(WisdomCategory.MINDFULNESS);
        }
        if (lower.contains("stress") || lower.contains("anxious") || lower.contains("worried")) {
            return getWisdomByCategory(WisdomCategory.CHANGE_UNCERTAINTY);
        }
        return getRandomWisdom();
    }

    public String getThoughtProvokingQuestion(String topic) {
        String[] questions = {
            "What would you do if you knew you could not fail?",
            "What would you do today if you had no fear?",
            "What does your ideal life look like in 5 years?",
            "What is one thing you could do today that would make the biggest difference?",
            "Who do you want to become?"
        };
        return questions[random.nextInt(questions.length)];
    }

    public int getTotalObservations() {
        return wisdomObservations.size();
    }
    
    // ==================== PRACTICAL GUIDANCE ====================
    
    private void initializePracticalGuidance() {
        // Self-awareness steps
        practicalSteps.put("self_awareness", new String[]{
            "1. Set aside 10 minutes daily for reflection",
            "2. Write down 3 things you noticed about yourself today",
            "3. Ask someone for honest feedback about yourself",
            "4. Notice your emotional reactions without judging them"
        });
        
        resourceRecommendations.put("self_awareness", new String[]{
            "Book: 'The Power of Now' by Eckhart Tolle",
            "Practice: Daily meditation (try Headspace or Calm app)",
            "Exercise: Journaling for self-reflection"
        });
        
        // Time management steps
        practicalSteps.put("time", new String[]{
            "1. Write down your top 3 priorities for the week",
            "2. Block time on your calendar for important tasks",
            "3. Say no to one thing that doesn't align with your goals",
            "4. Review your day each evening"
        });
        
        resourceRecommendations.put("time", new String[]{
            "Book: 'Getting Things Done' by David Allen",
            "App: Todoist or Notion for task management",
            "Technique: Pomodoro Technique for focus"
        });
        
        // Relationships steps
        practicalSteps.put("relationships", new String[]{
            "1. Reach out to someone you haven't spoken to in a while",
            "2. Practice active listening in your next conversation",
            "3. Express appreciation to someone today",
            "4. Set a healthy boundary and notice how it feels"
        });
        
        resourceRecommendations.put("relationships", new String[]{
            "Book: 'The 5 Love Languages' by Gary Chapman",
            "Practice: Nonviolent Communication",
            "Exercise: Gratitude journal for relationships"
        });
        
        // Career steps
        practicalSteps.put("career", new String[]{
            "1. Identify one skill you want to develop this month",
            "2. Reach out to someone in your desired field for advice",
            "3. Update your resume or LinkedIn profile",
            "4. Set a specific, measurable career goal"
        });
        
        resourceRecommendations.put("career", new String[]{
            "Book: 'What Color Is Your Parachute?'",
            "Platform: LinkedIn Learning for skill development",
            "Network: Industry meetups and conferences"
        });
        
        // Financial steps
        practicalSteps.put("money", new String[]{
            "1. Track all spending for one week",
            "2. Identify three areas to reduce expenses",
            "3. Set up automatic savings (even $25/month)",
            "4. Create a realistic budget"
        });
        
        resourceRecommendations.put("money", new String[]{
            "Book: 'Rich Dad Poor Dad' by Robert Kiyosaki",
            "App: Mint or YNAB for budgeting",
            "Practice: The 24-hour rule for purchases"
        });
        
        // Health steps
        practicalSteps.put("health", new String[]{
            "1. Drink 8 glasses of water today",
            "2. Take a 15-minute walk outside",
            "3. Go to bed 30 minutes earlier than usual",
            "4. Eat one meal without distractions"
        });
        
        resourceRecommendations.put("health", new String[]{
            "Book: 'Atomic Habits' by James Clear",
            "App: MyFitnessPal or Strava",
            "Practice: 7-minute morning workout routine"
        });
        
        // Mindfulness steps
        practicalSteps.put("mindfulness", new String[]{
            "1. Focus on your breath for 2 minutes",
            "2. Notice 5 things you can see, 4 you can hear, etc.",
            "3. Practice single-tasking instead of multitasking",
            "4. Take 3 deep breaths before responding in conversation"
        });
        
        resourceRecommendations.put("mindfulness", new String[]{
            "Book: 'Wherever You Go, There You Are' by Jon Kabat-Zinn",
            "App: Insight Timer (free meditations)",
            "Practice: Body scan meditation"
        });
    }
    
    public String getPracticalGuidance(String topic) {
        String lower = topic.toLowerCase();
        String key = "general";
        
        if (lower.contains("career") || lower.contains("work") || lower.contains("job")) {
            key = "career";
        } else if (lower.contains("money") || lower.contains("financial") || lower.contains("save")) {
            key = "money";
        } else if (lower.contains("health") || lower.contains("body") || lower.contains("exercise")) {
            key = "health";
        } else if (lower.contains("time") || lower.contains("busy") || lower.contains("productive")) {
            key = "time";
        } else if (lower.contains("relate") || lower.contains("friend") || lower.contains("love")) {
            key = "relationships";
        } else if (lower.contains("aware") || lower.contains("self") || lower.contains("reflect")) {
            key = "self_awareness";
        } else if (lower.contains("mindful") || lower.contains("stress") || lower.contains("anxious")) {
            key = "mindfulness";
        }
        
        String[] steps = practicalSteps.get(key);
        if (steps == null) steps = practicalSteps.get("time");
        
        StringBuilder result = new StringBuilder();
        result.append(getWisdomByCategory(WisdomCategory.PURPOSE_MEANING)).append("\n\n");
        result.append("Here's some practical guidance:\n");
        
        for (String step : steps) {
            result.append(step).append("\n");
        }
        
        String[] resources = resourceRecommendations.get(key);
        if (resources == null) resources = resourceRecommendations.get("time");
        
        result.append("\nRecommended resources:\n");
        for (String resource : resources) {
            result.append("- ").append(resource).append("\n");
        }
        
        return result.toString().trim();
    }
}
