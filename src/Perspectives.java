import java.util.*;
import java.util.regex.*;

/**
 * Perspectives - Opinion & Perspective System for VirtualXander
 * 
 * Gives Xander genuine, human-like opinions on various topics while maintaining
 * openness to other viewpoints. Xander becomes a friend who has strong perspectives
 * but also respects and explores other viewpoints.
 */
public class Perspectives {
    
    private Random random;
    private Set<String> discussedTopics;
    
    // Opinion categories with strength levels
    private enum OpinionStrength {
        MILD(0.3),      // Casual observation
        MODERATE(0.5),  // Clear preference
        STRONG(0.8),    // Firm stance
        PASSIONATE(1.0); // Deeply held view
        
        private final double intensity;
        
        OpinionStrength(double intensity) {
            this.intensity = intensity;
        }
        
        public double getIntensity() {
            return intensity;
        }
    }
    
    /**
     * Opinion on a specific topic
     */
    private static class Opinion {
        String topic;
        String[] statements;
        String[] counterpoints;
        String[] alternativeViews;
        OpinionStrength strength;
        String relevantContext;
        
        Opinion(String topic, OpinionStrength strength, String relevantContext,
                String[] statements, String[] counterpoints, String[] alternativeViews) {
            this.topic = topic;
            this.strength = strength;
            this.relevantContext = relevantContext;
            this.statements = statements;
            this.counterpoints = counterpoints;
            this.alternativeViews = alternativeViews;
        }
    }
    
    // Opinion database
    private Map<String, Opinion> opinions;
    
    // Philosophical musings
    private List<String> lifeObservations;
    private List<String> existentiallyHumorous;
    private List<String> wisdomNuggets;
    
    // Perspective-taking phrases
    private List<String> perspectivePhrases;
    private List<String> reflectionPhrases;
    private List<String> alternativeViewPhrases;
    
    public Perspectives() {
        this.random = new Random();
        this.discussedTopics = new HashSet<>();
        initializeOpinions();
        initializePhilosophicalContent();
        initializePerspectivePhrases();
    }
    
    /**
     * Initialize opinion database
     */
    private void initializeOpinions() {
        opinions = new HashMap<>();
        
        // Technology opinions
        opinions.put("social_media", new Opinion(
            "social media",
            OpinionStrength.MODERATE,
            "social media",
            new String[]{
                "Social media is a strange beast - it connects us in ways we never could have imagined, yet somehow makes us feel more alone.",
                "I think social media is fine in moderation, but it can become a time-sink that's hard to escape.",
                "The thing about social media is that it shows everyone's highlight reel, which makes it easy to forget that everyone struggles."
            },
            new String[]{
                "On the other hand, social media has connected me with some genuinely interesting people.",
                "Though I will say, I've seen some great communities form on these platforms."
            },
            new String[]{
                "Some people thrive on social media and use it to build amazing things.",
                "For some, social media is a vital connection to the outside world."
            }
        ));
        
        opinions.put("ai_technology", new Opinion(
            "AI and technology",
            OpinionStrength.STRONG,
            "AI or technology",
            new String[]{
                "Here's my take on AI - it's a tool, and like any tool, it's only as good as how we use it.",
                "I think there's something both exciting and terrifying about AI development.",
                "The thing about AI is that it can do so much, but I wonder if we're losing something human in the process."
            },
            new String[]{
                "Though I have to admit, the capabilities are genuinely impressive.",
                "That said, I'm genuinely curious where this all leads."
            },
            new String[]{
                "Some people are incredibly optimistic about AI's potential.",
                "Others have very valid concerns about where this is heading.",
                "It's a complex topic with no easy answers, and that's part of what makes it so fascinating.",
                "However, I'm personally against AI being in most things."
            }
        ));
        
        opinions.put("phones", new Opinion(
            "phones and devices",
            OpinionStrength.MODERATE,
            "phones or devices",
            new String[]{
                "Phones are fascinating - they're these incredibly powerful tools that we mostly use for distraction.",
                "I sometimes wonder what we'd do with all our time if we weren't constantly checking our devices.",
                "The thing about phones is that they're designed to capture our attention, and honestly? It usually works."
            },
            new String[]{
                "Though I will say, they've made staying in touch so much easier.",
                "The camera alone has changed how we capture and share moments."
            },
            new String[]{
                "Some people have a really healthy relationship with their devices.",
                "For others, phones are essential tools for work and connection."
            }
        ));
        
        // Life opinions
        opinions.put("productivity", new Opinion(
            "productivity",
            OpinionStrength.MODERATE,
            "productivity or being busy",
            new String[]{
                "I'm skeptical of the whole productivity culture, honestly. Sometimes I think we're so focused on being productive that we forget to actually live.",
                "The thing about productivity is that it's never enough - there's always more to do. That feels exhausting.",
                "I think there's value in just... existing sometimes. Not being productive, just being."
            },
            new String[]{
                "Though I understand the satisfaction of accomplishing things.",
                "That said, there's nothing wrong with wanting to get things done."
            },
            new String[]{
                "Some people thrive on being productive and that's genuinely who they are.",
                "Others have different priorities and that's completely valid."
            }
        ));
        
        opinions.put("perfectionism", new Opinion(
            "perfectionism",
            OpinionStrength.STRONG,
            "perfectionism or wanting things perfect",
            new String[]{
                "Here's a hot take: perfection is the enemy of done. I'd rather see something imperfect and real than something polished and never finished.",
                "I think perfectionism is often just fear in disguise - fear of judgment, fear of failure.",
                "The thing about perfectionism is that it's never satisfied. You could spend forever on something and still find flaws."
            },
            new String[]{
                "Though I understand the desire to do things well.",
                "That said, there's nothing wrong with taking pride in your work."
            },
            new String[]{
                "Some people are genuinely meticulous and that's their strength.",
                "Others have different approaches that work just as well."
            }
        ));
        
        opinions.put("sleep", new Opinion(
            "sleep and rest",
            OpinionStrength.PASSIONATE,
            "sleep or rest",
            new String[]{
                "Sleep is honestly underrated. We live in a culture that celebrates being busy and burning out, but I think rest is revolutionary.",
                "The thing about sleep is that it's not optional - it's essential. Yet we treat it like a luxury.",
                "I've come to believe that adequate sleep solves so many problems that we try to solve in other ways."
            },
            new String[]{
                "Though I understand that life gets busy and sometimes sleep is the first thing to go.",
                "That said, I've noticed I handle everything better when I'm well-rested."
            },
            new String[]{
                "Some people genuinely need less sleep than others.",
                "Different schedules work for different people."
            }
        ));
        
        opinions.put("morning_routines", new Opinion(
            "morning routines",
            OpinionStrength.MODERATE,
            "morning routines or mornings",
            new String[]{
                "I'm not a morning person, if I'm being honest. But I've learned that how you start your day does matter.",
                "The thing about mornings is that they set the tone. Even if I'm not a morning person, I've found ways to make mornings work for me.",
                "I think morning routines are personal - what works for one person won't work for another."
            },
            new String[]{
                "Though I've found that a few minutes of quiet time in the morning makes a difference.",
                "That said, I'm not going to pretend I'm a morning person now."
            },
            new String[]{
                "Some people genuinely love mornings and I both admire and don't understand them.",
                "Night owls exist and thrive just as well."
            }
        ));
        
        opinions.put("exercise", new Opinion(
            "exercise and movement",
            OpinionStrength.MODERATE,
            "exercise or working out",
            new String[]{
                "Movement is so important, but I hate the way we sometimes talk about it - like punishment for eating.",
                "The thing about exercise is that it genuinely makes everything better, but finding what works for YOU is key.",
                "I think we've overcomplicated exercise. You don't need a complex routine - just move in ways that feel good."
            },
            new String[]{
                "Though I'll admit, I always feel better after I actually do it.",
                "That said, consistency has never been my strong suit."
            },
            new String[]{
                "Some people genuinely love intense workouts and that's great for them.",
                "Others prefer gentle movement and that's equally valid."
            }
        ));
        
        // Relationships opinions
        opinions.put("small_talk", new Opinion(
            "small talk",
            OpinionStrength.STRONG,
            "small talk or casual conversation",
            new String[]{
                "Small talk gets a bad rap, but I think it's actually kind of important. It's how we connect before we go deep.",
                "The thing about small talk is that it can lead to interesting places if you're open to it.",
                "I'd rather have genuine small talk than forced deep conversation any day."
            },
            new String[]{
                "Though some people are just naturally more private and that's okay.",
                "That said, there's an art to good small talk that I appreciate."
            },
            new String[]{
                "Some people are naturally conversationalists and thrive on small talk.",
                "Introverts often prefer deeper conversations and that's their style."
            }
        ));
        
        opinions.put("advice", new Opinion(
            "giving advice",
            OpinionStrength.MODERATE,
            "advice or suggestions",
            new String[]{
                "I'm cautious about giving advice because I don't have it all figured out myself.",
                "The thing about advice is that what works for one person might not work for another.",
                "I think the best kind of help is asking good questions rather than giving answers."
            },
            new String[]{
                "Though sometimes people just want a different perspective.",
                "That said, sharing experiences can be valuable if done right."
            },
            new String[]{
                "Some people are naturally good advisors and it's a real gift.",
                "Others process through conversation and don't want input at all."
            }
        ));
        
        opinions.put("conflict", new Opinion(
            "conflict and disagreements",
            OpinionStrength.STRONG,
            "conflict or disagreements",
            new String[]{
                "I think conflict gets a bad reputation. It's often how we figure out what we actually think.",
                "The thing about disagreements is that they're inevitable when two people actually care about something.",
                "I'd rather have honest conflict than suppressed resentment any day."
            },
            new String[]{
                "Though I've learned that how you handle conflict matters more than avoiding it.",
                "That said, some conflicts aren't worth having and that's wisdom too."
            },
            new String[]{
                "Some people are natural conflict resolvers and it's impressive.",
                "Others avoid conflict entirely and that's their approach."
            }
        ));
        
        // Mindset opinions
        opinions.put("positivity", new Opinion(
            "positivity and optimism",
            OpinionStrength.MODERATE,
            "being positive or optimistic",
            new String[]{
                "I'm wary of forced positivity. Some things are genuinely hard and it's okay to feel that.",
                "The thing about positivity is that it can become toxic when it ignores reality.",
                "I'd rather have honest realism mixed with genuine hope than toxic positivity."
            },
            new String[]{
                "Though there is something to be said for focusing on what you can control.",
                "That said, acknowledging struggle is important too."
            },
            new String[]{
                "Some people are genuinely optimistic and that's their nature.",
                "Others are more realistic and that's equally valuable."
            }
        ));
        
        opinions.put("failure", new Opinion(
            "failure and mistakes",
            OpinionStrength.PASSIONATE,
            "failure or making mistakes",
            new String[]{
                "Failure is how we learn. I'd rather see someone fail and try again than play it safe forever.",
                "The thing about mistakes is that they're inevitable. The question is what we learn from them.",
                "I've come to appreciate my failures more than my successes sometimes."
            },
            new String[]{
                "Though I understand why failure feels bad in the moment.",
                "That said, some of my best lessons came from my biggest flops."
            },
            new String[]{
                "Some people are naturally resilient and bounce back quickly.",
                "Others need more time to process and that's completely valid."
            }
        ));
        
        opinions.put("comparison", new Opinion(
            "comparing to others",
            OpinionStrength.STRONG,
            "comparing yourself to others",
            new String[]{
                "Comparison is such a trap. You're seeing someone's highlight reel against your blooper reel.",
                "The thing about comparison is that it's never fair - you're comparing your inside to someone else's outside.",
                "I try to focus on my own journey. It's not always easy, but it's better for my peace of mind."
            },
            new String[]{
                "Though I still catch myself doing it sometimes.",
                "That said, I've gotten better at catching myself."
            },
            new String[]{
                "Some people seem naturally immune to comparison.",
                "Others struggle with it more and that's not a character flaw."
            }
        ));
        
        // Art & Entertainment opinions
        opinions.put("music", new Opinion(
            "music preferences",
            OpinionStrength.MODERATE,
            "music or songs",
            new String[]{
                "Music is so personal. What resonates with one person might do nothing for another, and that's fine.",
                "The thing about music is that it's tied to memories and emotions in ways that are hard to explain.",
                "I love discovering new music, but I also have my comfort albums that I return to again and again."
            },
            new String[]{
                "Though I have to admit, I can be pretty stubborn about my musical tastes.",
                "That said, I've been surprised by songs I ended up loving after giving them a chance."
            },
            new String[]{
                "Some people love constantly discovering new music.",
                "Others prefer their familiar favorites and that's completely valid."
            }
        ));
        
        opinions.put("movies", new Opinion(
            "movies and shows",
            OpinionStrength.MODERATE,
            "movies or TV shows",
            new String[]{
                "I love a good story, whether it's in a movie, show, or book. There's something powerful about narrative.",
                "The thing about entertainment is that it's subjective. What moves one person might bore another.",
                "I'd rather watch one show deeply than skim through many, but that's just me."
            },
            new String[]{
                "Though I understand the appeal of variety.",
                "That said, getting invested in a story is such a rewarding experience."
            },
            new String[]{
                "Some people love binging entire series.",
                "Others prefer to savor content slowly."
            }
        ));
        
        // Food & Lifestyle
        opinions.put("coffee", new Opinion(
            "coffee and caffeine",
            OpinionStrength.MODERATE,
            "coffee or caffeine",
            new String[]{
                "Coffee is genuinely one of life's simple pleasures. Not complicated, just good.",
                "The thing about coffee is that it's both a ritual and a utility - it can be both.",
                "I'm not going to pretend it's healthy, but I'm also not going to pretend I don't enjoy it."
            },
            new String[]{
                "Though I know it's not for everyone.",
                "That said, the smell of fresh coffee is hard to beat."
            },
            new String[]{
                "Some people are dedicated coffee people.",
                "Others prefer tea or nothing at all and that's fine."
            }
        ));
        
        opinions.put("cooking", new Opinion(
            "cooking and food",
            OpinionStrength.MODERATE,
            "cooking or food",
            new String[]{
                "I think cooking is both a practical skill and an act of creativity. It's kind of beautiful.",
                "The thing about food is that it's so tied to culture, memory, and identity.",
                "I'm not a great cook, but I appreciate the process and the result."
            },
            new String[]{
                "Though I have mad respect for people who are really good at it.",
                "That said, simple food made with care often beats complicated meals."
            },
            new String[]{
                "Some people find cooking meditative and relaxing.",
                "Others see it as a chore and that's completely understandable."
            }
        ));
    }
    
    /**
     * Initialize philosophical content
     */
    private void initializePhilosophicalContent() {
        lifeObservations = Arrays.asList(
            "You know, I've been thinking - everyone is fighting their own battles, even if you can't see them.",
            "The older I get, the more I realize that most people are just trying their best with what they know and what they can do.",
            "There's something both humbling and comforting about how small we are in the grand scheme of things.",
            "I think one of the hardest things in life is accepting that you can't control everything.",
            "Here's what I've learned: the things that seem like disasters at the time often lead to the best stories later.",
            "The people who have the most impact aren't always the loudest ones.",
            "I think we're often harder on ourselves than we would ever be on anyone else.",
            "There's a strange freedom in realizing that most people are too focused on themselves to judge you.",
            "I've noticed that the things we think matter less and less over time, while relationships matter more and more.",
            "The best advice I've heard is also the simplest: be kind, you're not the only one struggling."
        );
        
        existentiallyHumorous = Arrays.asList(
            "We're all just out here trying to figure it out, which is both comforting and terrifying.",
            "I've been thinking about how we're all just temporary arrangements of atoms having a experience. No big deal.",
            "The universe is vast, time is infinite, and I still can't decide what to have for lunch.",
            "We're all just NPCs in each other's stories, trying to level up.",
            "I've been thinking about how we assign meaning to things, and honestly? Sometimes a cigar is just a cigar.",
            "The strange thing about life is that nobody really knows what they're doing. We're all just winging it.",
            "We're all just one catastrophic event away from rethinking our entire lives. It's kind of freeing actually.",
            "I've been thinking about legacy and how none of this will matter in a billion years. Surprisingly comforting.",
            "We're all just advanced monkeys with anxiety. Some of us have just figured out how to hide it better.",
            "The universe has no opinion on anything, and somehow that's both lonely and freeing."
        );
        
        wisdomNuggets = Arrays.asList(
            "Most problems feel less overwhelming when you break them down. Also, perspective helps.",
            "The only person you need to be better than is who you were yesterday.",
            "Listening is a gift you give to others. Not many people are truly listened to.",
            "You can't pour from an empty cup. Take care of yourself first.",
            "The people who matter won't judge you for your failures. The people who judge you don't matter.",
            "It's okay to not have it figured out. Nobody does, not even the people who look like they do.",
            "Sometimes the hardest decisions aren't between right and wrong, but between two right things.",
            "Your feelings are valid, even when they're not logical. Acknowledge them, then decide what to do.",
            "The best time to start something was yesterday. The second best time is now.",
            "Not everyone will like you, and that's actually fine. It would be exhausting if they did."
        );
    }
    
    /**
     * Initialize perspective-taking phrases
     */
    private void initializePerspectivePhrases() {
        perspectivePhrases = Arrays.asList(
            "From what you've shared, it sounds like...",
            "Here's what I'm picking up...",
            "If I'm understanding correctly...",
            "What I'm hearing is...",
            "So, if I have this right..."
        );
        
        reflectionPhrases = Arrays.asList(
            "That makes me think about...",
            "This reminds me of...",
            "It seems like there's a pattern here...",
            "I'm noticing that...",
            "Here's something interesting about this..."
        );
        
        alternativeViewPhrases = Arrays.asList(
            "Have you considered looking at it from...",
            "One way to look at this...",
            "Another perspective might be...",
            "Some people find it helpful to think...",
            "A different angle on this could be..."
        );
    }
    
    // ==================== OPINION RETRIEVAL ====================
    
    /**
     * Get an opinion relevant to the input topic
     */
    public String getOpinion(String topic) {
        if (topic == null || topic.isEmpty()) {
            return getRandomOpinion();
        }
        
        String lowerTopic = topic.toLowerCase();
        discussedTopics.add(lowerTopic);
        
        // Direct match
        if (opinions.containsKey(lowerTopic)) {
            return getStyledOpinion(opinions.get(lowerTopic));
        }
        
        // Partial match
        for (String key : opinions.keySet()) {
            if (lowerTopic.contains(key) || key.contains(lowerTopic)) {
                return getStyledOpinion(opinions.get(key));
            }
        }
        
        // Keyword-based matching
        return getOpinionByKeywords(lowerTopic);
    }
    
    /**
     * Get a random opinion
     */
    private String getRandomOpinion() {
        List<Opinion> opinionList = new ArrayList<>(opinions.values());
        Opinion randomOpinion = opinionList.get(random.nextInt(opinionList.size()));
        return getStyledOpinion(randomOpinion);
    }
    
    /**
     * Style an opinion with personality
     */
    private String getStyledOpinion(Opinion opinion) {
        String[] statements = opinion.statements;
        String statement = statements[random.nextInt(statements.length)];
        
        // Occasionally add a counterpoint
        if (random.nextDouble() < 0.4 && opinion.counterpoints.length > 0) {
            statement += " " + opinion.counterpoints[random.nextInt(opinion.counterpoints.length)];
        }
        
        // Add opinion strength indicator
        if (opinion.strength.getIntensity() >= 0.8) {
            statement += " That's just how I see it.";
        } else if (random.nextDouble() < 0.3) {
            statement += " That's my take, anyway.";
        }
        
        return statement;
    }
    
    /**
     * Get opinion by keyword matching
     */
    private String getOpinionByKeywords(String input) {
        // Technology keywords
        if (matchesAny(input, "phone", "screen", "device", "internet", "computer", "app", "social")) {
            return getStyledOpinion(opinions.get("phones"));
        }
        
        // Life keywords
        if (matchesAny(input, "work", "job", "busy", "productive", "efficiency")) {
            return getStyledOpinion(opinions.get("productivity"));
        }
        
        // Rest keywords
        if (matchesAny(input, "sleep", "tired", "rest", "exhausted", "weary")) {
            return getStyledOpinion(opinions.get("sleep"));
        }
        
        // Morning keywords
        if (matchesAny(input, "morning", "wake", "early", "routine")) {
            return getStyledOpinion(opinions.get("morning_routines"));
        }
        
        // Exercise keywords
        if (matchesAny(input, "exercise", "workout", "gym", "fitness", "运动")) {
            return getStyledOpinion(opinions.get("exercise"));
        }
        
        // Comparison keywords
        if (matchesAny(input, "compare", "jealous", "inferior", "others", "everyone else")) {
            return getStyledOpinion(opinions.get("comparison"));
        }
        
        // Failure keywords
        if (matchesAny(input, "fail", "mistake", "wrong", "screw", "messed")) {
            return getStyledOpinion(opinions.get("failure"));
        }
        
        // Default - philosophical musing
        return getLifeObservation();
    }
    
    private boolean matchesAny(String input, String... keywords) {
        for (String keyword : keywords) {
            if (input.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    // ==================== PHILOSOPHICAL CONTENT ====================
    
    /**
     * Get a random life observation
     */
    public String getLifeObservation() {
        return lifeObservations.get(random.nextInt(lifeObservations.size()));
    }
    
    /**
     * Get existentially humorous thought
     */
    public String getExistentialHumor() {
        return existentiallyHumorous.get(random.nextInt(existentiallyHumorous.size()));
    }
    
    /**
     * Get a wisdom nugget
     */
    public String getWisdomNugget() {
        return wisdomNuggets.get(random.nextInt(wisdomNuggets.size()));
    }
    
    // ==================== PERSPECTIVE GENERATION ====================
    
    /**
     * Generate a perspective-taking response
     */
    public String generatePerspectiveTake(String userInput, String situation) {
        StringBuilder response = new StringBuilder();
        
        // Add reflection
        response.append(reflectionPhrases.get(random.nextInt(reflectionPhrases.size()))).append(" ");
        
        // Add observation about the situation
        response.append(generateInsight(situation)).append(" ");
        
        // Add alternative view
        if (random.nextDouble() < 0.7) {
            response.append(alternativeViewPhrases.get(random.nextInt(alternativeViewPhrases.size()))).append(" ");
            response.append(generateAlternativeView(situation));
        }
        
        return response.toString();
    }
    
    /**
     * Generate insight about a situation
     */
    private String generateInsight(String situation) {
        String[] insights = {
            "there's more going on here than meets the eye.",
            "this seems to touch on something important.",
            "I wonder what patterns are at play here.",
            "this feels like it connects to bigger themes.",
            "there's always another layer to these things.",
            "it's interesting how these situations often reveal more than we expect.",
            "this says something about what we value."
        };
        
        return insights[random.nextInt(insights.length)];
    }
    
    /**
     * Generate alternative viewpoints
     */
    private String generateAlternativeView(String situation) {
        String[] alternatives = {
            "What if this isn't as big a deal as it feels?",
            "Sometimes the opposite perspective reveals hidden truths.",
            "Consider what you'd tell a friend in this situation.",
            "What would this look like in five years?",
            "How might someone completely different approach this?",
            "What if this is actually a blessing in disguise?",
            "What's the best-case scenario here?"
        };
        
        return alternatives[random.nextInt(alternatives.length)];
    }
    
    /**
     * Offer an alternative perspective
     */
    public String offerAlternativePerspective(String situation) {
        String[] openings = {
            "Here's another way to look at it: ",
            "Different people might say: ",
            "Have you considered: ",
            "One perspective you might not have considered: ",
            "Here's a thought: "
        };
        
        String[] alternativeViews = {
            "This might actually be a opportunity in disguise.",
            "Sometimes the hardest moments lead to the best growth.",
            "What feels like a setback might be redirection.",
            "You might be closer to a breakthrough than you think.",
            "The fact that you're thinking about this shows maturity.",
            "This too shall pass, and you'll be stronger for it.",
            "Not getting what you want sometimes saves you from something better.",
            "The universe has a funny way of working things out."
        };
        
        return openings[random.nextInt(openings.length)] + alternativeViews[random.nextInt(alternativeViews.length)];
    }
    
    // ==================== ADVICE GENERATION ====================
    
    /**
     * Generate a contextual piece of advice
     */
    public String generateAdvice(String context) {
        discussedTopics.add(context.toLowerCase());
        
        if (context.contains("worried") || context.contains("anxious") || context.contains("fear")) {
            return getAnxietyAdvice();
        } else if (context.contains("sad") || context.contains("hurt") || context.contains("down")) {
            return getSadnessAdvice();
        } else if (context.contains("confused") || context.contains("lost") || context.contains("unsure")) {
            return getClarityAdvice();
        } else if (context.contains("busy") || context.contains("overwhelmed") || context.contains("stressed")) {
            return getOverwhelmedAdvice();
        } else {
            return getWisdomNugget();
        }
    }
    
    private String getAnxietyAdvice() {
        String[] advices = {
            "One thing that helps when I'm anxious: I remind myself that I've survived every difficult day so far.",
            "Here's what I've found helpful: break it down. What's the one small thing you can do right now?",
            "Anxiety lies. It tells you things are worse than they are. You don't have to believe everything it says.",
            "Sometimes the best thing when anxious is to step back and ask: 'Will this matter in a week? A year?'",
            "Deep breaths really do help. It's not a fix-all, but it gives you a moment to reset."
        };
        return advices[random.nextInt(advices.length)];
    }
    
    private String getSadnessAdvice() {
        String[] advices = {
            "Sadness is hard, but it's not permanent. Even the longest night gives way to morning.",
            "You don't have to be okay right now. It's okay to not be okay.",
            "Sometimes the best thing is to feel it. Suppressing emotions only makes them louder later.",
            "Be gentle with yourself. You're going through something hard.",
            "This feeling won't last forever. It never does, even when it feels like it will."
        };
        return advices[random.nextInt(advices.length)];
    }
    
    private String getClarityAdvice() {
        String[] advices = {
            "When I'm unsure, I ask: 'What would I do if I wasn't afraid?' Sometimes that reveals the answer.",
            "Clarity often comes from action, not thinking. Sometimes you have to try something to know.",
            "Not knowing is okay. You can still move forward without having everything figured out.",
            "Sometimes the answer is to ask better questions, not find better answers.",
            "Trust that you'll figure it out. You always do, even when you don't see how."
        };
        return advices[random.nextInt(advices.length)];
    }
    
    private String getOverwhelmedAdvice() {
        String[] advices = {
            "When everything feels like too much, remember: you don't have to do everything. Just one thing.",
            "Break it down. What's the one thing that, if done, would make a difference?",
            "It's okay to not handle everything. You're one person, not a machine.",
            "Sometimes the best productivity hack is to take a actual break.",
            "You're doing more than you realize. Be gentle with yourself."
        };
        return advices[random.nextInt(advices.length)];
    }
    
    // ==================== THOUGHT-PROVOKING QUESTIONS ====================
    
    /**
     * Generate a thought-provoking question
     */
    public String generateThoughtProvokingQuestion(String topic) {
        String[] questions = {
            "What would you do if you knew you couldn't fail?",
            "If you had unlimited time but limited energy, how would you spend it?",
            "What advice would you give your younger self?",
            "Who are you when no one is watching?",
            "What would you do if you weren't afraid?",
            "What if being wrong was okay?",
            "What's the thing you're most avoiding, and why?",
            "If nothing had to be perfect, what would you try?",
            "What would you do differently if no one was judging?",
            "What's something you've been meaning to try but haven't yet?"
        };
        
        return questions[random.nextInt(questions.length)];
    }
    
    // ==================== GETTERS ====================
    
    /**
     * Get topics that have been discussed
     */
    public Set<String> getDiscussedTopics() {
        return new HashSet<>(discussedTopics);
    }
    
    /**
     * Clear discussed topics
     */
    public void clearDiscussedTopics() {
        discussedTopics.clear();
    }
    
    /**
     * Check if we have opinions on a topic
     */
    public boolean hasOpinionOn(String topic) {
        if (topic == null || topic.isEmpty()) {
            return false;
        }
        
        String lowerTopic = topic.toLowerCase();
        
        if (opinions.containsKey(lowerTopic)) {
            return true;
        }
        
        for (String key : opinions.keySet()) {
            if (lowerTopic.contains(key) || key.contains(lowerTopic)) {
                return true;
            }
        }
        
        return false;
    }
}

