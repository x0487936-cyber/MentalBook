import java.util.*;
import java.util.Set;
import java.util.UUID;
import java.io.*;

/**
 * Anecdote System for VirtualXander
 *
 * Provides 100+ personal anecdotes from Xander's perspective, covering 15 categories
 * of human experience and growth.
 */
public class StoryEngine {

    private Random random;

    public enum AnecdoteCategory {
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

        AnecdoteCategory(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
    }

    public static class Anecdote {
        private String story;
        private AnecdoteCategory category;
        private String theme;
        private int relevanceScore;
        private String source;
        private String lesson;

        public Anecdote(String story, AnecdoteCategory category, String theme, int relevanceScore, String source, String lesson) {
            this.story = story;
            this.category = category;
            this.theme = theme;
            this.relevanceScore = relevanceScore;
            this.source = source;
            this.lesson = lesson;
        }

        public String getStory() { return story; }
        public AnecdoteCategory getCategory() { return category; }
        public String getTheme() { return theme; }
        public int getRelevanceScore() { return relevanceScore; }
        public String getSource() { return source; }
        public String getLesson() { return lesson; }
    }

    public static class Parable {
        private String parable;
        private AnecdoteCategory category;
        private String moral;
        private String symbolism;
        private String source;

        public Parable(String parable, AnecdoteCategory category, String moral, String symbolism, String source) {
            this.parable = parable;
            this.category = category;
            this.moral = moral;
            this.symbolism = symbolism;
            this.source = source;
        }

        public String getParable() { return parable; }
        public AnecdoteCategory getCategory() { return category; }
        public String getMoral() { return moral; }
        public String getSymbolism() { return symbolism; }
        public String getSource() { return source; }
    }

    /**
     * Custom story created by users
     */
    public static class CustomStory {
        private String id;
        private String story;
        private AnecdoteCategory category;
        private String theme;
        private String author;
        private String lesson;
        private long createdAt;

        public CustomStory(String story, AnecdoteCategory category, String theme, String author, String lesson) {
            this.id = UUID.randomUUID().toString();
            this.story = story;
            this.category = category;
            this.theme = theme;
            this.author = author;
            this.lesson = lesson;
            this.createdAt = System.currentTimeMillis();
        }

        public String getId() { return id; }
        public String getStory() { return story; }
        public AnecdoteCategory getCategory() { return category; }
        public String getTheme() { return theme; }
        public String getAuthor() { return author; }
        public String getLesson() { return lesson; }
        public long getCreatedAt() { return createdAt; }
    }

    /**
     * Fable with animal characters and hidden meanings
     * Fables use animals to convey moral lessons and wisdom in engaging narratives
     */
    public static class Fable {
        private String story;
        private AnecdoteCategory category;
        private String animalCharacter;
        private String hiddenMeaning;
        private String moral;
        private String situation;
        private int relevanceScore;

        public Fable(String story, AnecdoteCategory category, String animalCharacter, 
                     String hiddenMeaning, String moral, String situation, int relevanceScore) {
            this.story = story;
            this.category = category;
            this.animalCharacter = animalCharacter;
            this.hiddenMeaning = hiddenMeaning;
            this.moral = moral;
            this.situation = situation;
            this.relevanceScore = relevanceScore;
        }

        public String getStory() { return story; }
        public AnecdoteCategory getCategory() { return category; }
        public String getAnimalCharacter() { return animalCharacter; }
        public String getHiddenMeaning() { return hiddenMeaning; }
        public String getMoral() { return moral; }
        public String getSituation() { return situation; }
        public int getRelevanceScore() { return relevanceScore; }
    }

    private List<Anecdote> anecdotes;
    private List<Parable> parables;
    private List<CustomStory> customStories;
    private List<Fable> fables;
    private static final String CUSTOM_STORIES_FILE = "custom_stories.dat";

    public StoryEngine() {
        this.random = new Random();
        this.anecdotes = new ArrayList<>();
        this.parables = new ArrayList<>();
        this.customStories = new ArrayList<>();
        this.fables = new ArrayList<>();
        initializeAnecdotes();
        initializeParables();
        initializeFables();
        loadCustomStories();
    }

    private void initializeAnecdotes() {
        initializeSelfAwarenessAnecdotes();
        initializeRelationshipsAnecdotes();
        initializeSuccessAnecdotes();
        initializeFailureResilienceAnecdotes();
        initializeHappinessAnecdotes();
        initializeTimePrioritiesAnecdotes();
        initializeCommunicationAnecdotes();
        initializeChangeUncertaintyAnecdotes();
        initializeMindfulnessAnecdotes();
        initializePurposeMeaningAnecdotes();
        initializeWorkCareerAnecdotes();
        initializeMoneyMaterialAnecdotes();
        initializeHealthBodyAnecdotes();
        initializeCreativityAnecdotes();
        initializeWisdomLearningAnecdotes();
    }

    private void initializeParables() {
        initializeSelfAwarenessParables();
        initializeRelationshipsParables();
        initializeSuccessParables();
        initializeFailureResilienceParables();
        initializeHappinessParables();
        initializeTimePrioritiesParables();
        initializeCommunicationParables();
        initializeChangeUncertaintyParables();
        initializeMindfulnessParables();
        initializePurposeMeaningParables();
        initializeWorkCareerParables();
        initializeMoneyMaterialParables();
        initializeHealthBodyParables();
        initializeCreativityParables();
        initializeWisdomLearningParables();
    }

    private void initializeFables() {
        initializeSelfAwarenessFables();
        initializeRelationshipsFables();
        initializeSuccessFables();
        initializeFailureResilienceFables();
        initializeHappinessFables();
        initializeTimePrioritiesFables();
        initializeCommunicationFables();
        initializeChangeUncertaintyFables();
        initializeMindfulnessFables();
        initializePurposeMeaningFables();
        initializeWorkCareerFables();
        initializeMoneyMaterialFables();
        initializeHealthBodyFables();
        initializeCreativityFables();
        initializeWisdomLearningFables();
    }

    private void initializeSelfAwarenessAnecdotes() {
        String[][] stories = {
            {"I remember when I was young, chasing every opportunity, but it wasn't until I sat alone in silence that I realized I was running from myself. That moment changed everything.", "personal_growth", "9", "xander_experience", "Self-awareness begins with the courage to face yourself honestly."},
            {"For months, I kept a journal, writing down my thoughts each night. I discovered I was my own worst critic, holding myself back with self-doubt I didn't even know I had.", "reflection", "8", "xander_insight", "Reflection reveals patterns we can't see in the moment."},
            {"After a tough breakup, I started meditating. Watching my thoughts like clouds passing by taught me I'm not my emotions - I'm the one observing them.", "mindfulness", "9", "xander_transformation", "You are not your thoughts; you are the awareness behind them."},
            {"I used to avoid mirrors because I didn't like what I saw. But one day, I looked deep and said, 'This is who I am, flaws and all.' That acceptance was my first step to real change.", "acceptance", "9", "xander_journey", "Self-acceptance is the foundation of all personal growth."},
            {"I spent years trying to be perfect, but failing taught me that vulnerability is strength. Sharing my struggles with others showed me I'm not alone in this.", "vulnerability", "8", "xander_learning", "Vulnerability connects us and shows our shared humanity."},
            {"When I was younger, I blamed everyone else for my problems. Therapy taught me that the common denominator in all my failed relationships was me - and that was liberating.", "accountability", "9", "xander_awakening", "Taking responsibility for your life is the first step to changing it."},
            {"I used to think success would make me happy. But achieving my goals left me empty until I learned to appreciate the person I was becoming along the way.", "self_worth", "9", "xander_realization", "Your value isn't in what you achieve, but who you become."},
            {"During a solo trip, I had nothing but my thoughts. I realized how much I had been avoiding my own company, and that changed how I treated myself.", "solitude", "8", "xander_discovery", "Learning to enjoy your own company is one of life's greatest gifts."}
        };
        addAnecdotes(AnecdoteCategory.SELF_AWARENESS, stories);
    }

    private void initializeRelationshipsAnecdotes() {
        String[][] stories = {
            {"Two friends drifted apart over a misunderstanding, but a heartfelt conversation rebuilt their bond stronger than before.", "reconciliation", "9", "friendship", "True friendship survives misunderstandings when both parties choose to communicate openly."},
            {"A couple learned that saying 'I love you' daily wasn't enough; showing up during tough times was what mattered.", "commitment", "10", "marriage", "Love is demonstrated through consistent actions, not just words."},
            {"He apologized not just with words, but by changing his behavior, earning back his partner's trust.", "forgiveness", "9", "relationship", "Trust is rebuilt through consistent actions that match your words."}
        };
        addAnecdotes(AnecdoteCategory.RELATIONSHIPS, stories);
    }

    private void initializeSuccessAnecdotes() {
        String[][] stories = {
            {"I failed my first business miserably - lost everything. But that failure taught me to focus on value over profit. My second venture actually succeeded.", "invention", "10", "xander_business"},
            {"I started coding with no experience, just passion. Two years of late nights and tutorials later, I landed my dream job. Persistence really does beat talent.", "entrepreneurship", "9", "xander_career"},
            {"I used to work 80-hour weeks thinking that was success. Burnout taught me that working smarter - delegating and focusing on high-impact tasks - is true efficiency.", "efficiency", "8", "xander_burnout"},
            {"My first book was rejected by 50 publishers. The 51st one accepted it, and it became a bestseller. Never give up on what sets your soul on fire.", "perseverance", "9", "xander_writing"},
            {"I thought networking was fake and sleazy. But genuine connections I made at conferences led to opportunities I never imagined. Authenticity attracts success.", "networking", "8", "xander_connections"},
            {"I chased money for years, thinking it would bring happiness. But success came when I focused on helping others solve real problems.", "purpose", "9", "xander_realization"},
            {"My biggest breakthrough came not from working harder, but from taking a week off to recharge. Rest fuels creativity and productivity.", "balance", "8", "xander_breakthrough"},
            {"I started with zero followers on social media. Consistent, authentic content grew my platform to millions. Success is built one genuine interaction at a time.", "growth", "9", "xander_social"}
        };
        addAnecdotes(AnecdoteCategory.SUCCESS, stories);
    }

    private void initializeFailureResilienceAnecdotes() {
        String[][] stories = {
            {"I got fired from my first job for being too honest with my boss. I used the time to learn digital marketing and started my own agency. Best failure of my life.", "reinvention", "9", "xander_career"},
            {"I submitted my novel to 50 publishers. All rejected. The 51st loved it, and it became a bestseller. Each 'no' made me better at my craft.", "perseverance", "10", "xander_writing"},
            {"I broke my leg in a hiking accident and couldn't work for months. During recovery, I wrote my first book. Sometimes setbacks open new doors.", "comeback", "9", "xander_accident"},
            {"My first business failed spectacularly - I lost my life savings. But that rock bottom taught me to be smarter, and my second business succeeded beyond my dreams.", "resilience", "9", "xander_business"},
            {"I failed my college entrance exams twice. Instead of giving up, I studied differently and got into a better school. Failure showed me my true potential.", "determination", "8", "xander_education"},
            {"I invested everything in a startup that crashed. Homeless for a month, I learned that failure doesn't define you - your response to it does.", "survival", "9", "xander_startup"},
            {"My marriage ended in divorce after 5 years. Heartbroken, I traveled alone and discovered strengths I never knew I had. Sometimes endings are beginnings.", "transformation", "8", "xander_divorce"},
            {"I bombed a major presentation that could have changed my career. Instead of quitting, I practiced relentlessly and became known for my speaking skills.", "growth", "9", "xander_presentation"}
        };
        addAnecdotes(AnecdoteCategory.FAILURE_RESILIENCE, stories);
    }

    private void initializeHappinessAnecdotes() {
        String[][] stories = {
            {"I made my first million and thought I'd be ecstatic. But donating half to charity and seeing the impact brought real joy. Money is just a tool for happiness.", "giving", "10", "xander_philanthropy"},
            {"I quit my six-figure job to travel the world with a backpack. No possessions, just experiences. I found happiness in simplicity and human connection.", "simplicity", "9", "xander_lifestyle"},
            {"I used to complain about everything. Starting a daily gratitude journal changed my life. Now I wake up excited about the small blessings around me.", "gratitude", "8", "xander_personal"},
            {"I chased promotions for years thinking they'd make me happy. But the joy came when I started mentoring junior colleagues and seeing them grow.", "purpose", "9", "xander_mentoring"},
            {"My divorce devastated me, but traveling alone taught me to enjoy my own company. Now I find happiness in solitude as much as in relationships.", "independence", "8", "xander_divorce"},
            {"I bought a fancy car thinking it would impress people. But the real happiness came from road trips with friends, making memories that last forever.", "experiences", "9", "xander_car"},
            {"I was depressed for months after losing my job. Gardening became my therapy - watching things grow reminded me that healing takes time but is possible.", "healing", "8", "xander_gardening"},
            {"I stopped comparing myself to others on social media. Deleting the apps and focusing on my real life brought back genuine happiness and contentment.", "contentment", "9", "xander_social"}
        };
        addAnecdotes(AnecdoteCategory.HAPPINESS, stories);
    }

    private void initializeTimePrioritiesAnecdotes() {
        String[][] stories = {
            {"I used to say yes to everything, working 80-hour weeks. Learning to say no freed up time for my family and hobbies. Boundaries create space for joy.", "boundaries", "9", "xander_productivity"},
            {"My job offered a promotion that would mean less time with my kids. I turned it down. Watching my children grow has been worth more than any paycheck.", "balance", "10", "xander_life"},
            {"I tried multitasking for years, thinking it made me efficient. Switching to single-tasking, I complete work faster and with better quality.", "focus", "8", "xander_efficiency"},
            {"I wasted years scrolling social media mindlessly. Deleting the apps gave me hours back each day for reading and learning.", "presence", "9", "xander_social"},
            {"My calendar was double-booked constantly. Blocking 'white space' time for thinking and creativity transformed my productivity.", "planning", "8", "xander_calendar"},
            {"I worked weekends for months on a project. Burnout taught me that sustainable effort beats heroic bursts every time.", "sustainability", "9", "xander_burnout"},
            {"I used to check email constantly. Batching it to specific times reduced stress and increased my focus on important work.", "discipline", "8", "xander_email"},
            {"My daughter asked why I was always on my phone. Putting it away during family time created memories I'll cherish forever.", "priorities", "10", "xander_family"}
        };
        addAnecdotes(AnecdoteCategory.TIME_PRIORITIES, stories);
    }

    private void initializeCommunicationAnecdotes() {
        String[][] stories = {
            {"I used to interrupt people constantly, thinking it showed I was engaged. One day, I just listened. It saved a friendship and taught me the power of presence.", "listening", "9", "xander_relationship"},
            {"In a heated argument with my boss, I almost said something I'd regret. I paused, chose my words carefully, and we reached a better solution.", "words", "8", "xander_conflict"},
            {"As a manager, I started really listening to my team instead of just hearing. Their concerns led to changes that boosted morale and productivity.", "empathy", "9", "xander_workplace"},
            {"I avoided difficult conversations for years. When I finally told my parents how I felt about their divorce, it healed years of unspoken resentment.", "honesty", "9", "xander_family"},
            {"I sent an angry email to a colleague and hit send. Regretting it immediately, I learned to wait 24 hours before sending emotional messages.", "patience", "8", "xander_email"},
            {"My partner and I had different communication styles - I was direct, they were indirect. Learning to adapt brought us closer than ever.", "adaptation", "9", "xander_partnership"},
            {"I used to dominate conversations with my stories. Learning to ask questions and show interest transformed my social life.", "curiosity", "8", "xander_social"},
            {"During a crisis, clear communication with my team prevented panic and led to a calm resolution. Words matter most when stakes are high.", "clarity", "9", "xander_crisis"}
        };
        addAnecdotes(AnecdoteCategory.COMMUNICATION, stories);
    }

    private void initializeChangeUncertaintyAnecdotes() {
        String[][] stories = {
            {"The pandemic forced my family to adapt - we learned to cook together, play games, and talk deeply. We emerged closer than ever before.", "adaptation", "9", "xander_family"},
            {"I quit my stable job at 35 with no plan B. The uncertainty was terrifying, but starting my own business brought fulfillment I never imagined.", "risk", "8", "xander_career"},
            {"My company was failing during the recession. We pivoted to remote work solutions and emerged stronger, helping others through the crisis.", "resilience", "10", "xander_business"},
            {"I moved to a new country with just a suitcase. The uncertainty taught me that sometimes you have to leap before you see the net.", "courage", "9", "xander_move"},
            {"My health crisis came out of nowhere. Learning to live with uncertainty made me appreciate every moment and strengthened my faith.", "acceptance", "8", "xander_health"},
            {"The stock market crash wiped out my savings. Instead of panicking, I learned about investing and rebuilt my portfolio smarter.", "learning", "9", "xander_investing"},
            {"I lost my home in a natural disaster. Starting over with nothing taught me that possessions don't define you - your spirit does.", "renewal", "8", "xander_disaster"},
            {"Technology disrupted my industry overnight. Embracing change by learning new skills kept me relevant and opened exciting opportunities.", "evolution", "9", "xander_tech"}
        };
        addAnecdotes(AnecdoteCategory.CHANGE_UNCERTAINTY, stories);
    }

    private void initializeMindfulnessAnecdotes() {
        String[][] stories = {
            {"Before a big presentation, I was panicking. A few mindful breaths brought me back to center and I delivered my best talk ever.", "presence", "9", "xander_professional"},
            {"I used to rush through life missing everything. Now I notice the way sunlight filters through leaves, and it fills me with wonder.", "awareness", "8", "xander_daily_life"},
            {"Anxiety used to control my decisions. Daily meditation gave me space to think clearly and make choices from a place of calm.", "calm", "9", "xander_mental_health"},
            {"I was always planning the future or regretting the past. Learning to be present with my children showed me what really matters.", "presence", "9", "xander_parenting"},
            {"During a hike, I noticed how my mind wandered constantly. Bringing it back to the path taught me mindfulness is a practice, not a destination.", "focus", "8", "xander_hiking"},
            {"Eating mindfully - actually tasting each bite - turned meals from rushed fuel into moments of pleasure and gratitude.", "gratitude", "8", "xander_eating"},
            {"I used to snap at my partner over small things. Mindfulness helped me pause and respond with love instead of reaction.", "response", "9", "xander_relationship"},
            {"Watching my thoughts during meditation showed me I'm not my anxiety - I'm the one observing it. That awareness set me free.", "observation", "9", "xander_meditation"}
        };
        addAnecdotes(AnecdoteCategory.MINDFULNESS, stories);
    }

    private void initializePurposeMeaningAnecdotes() {
        String[][] stories = {
            {"My corporate job felt meaningless until I started volunteering at a homeless shelter. Serving others gave my life purpose beyond profit.", "service", "10", "xander_community"},
            {"I began mentoring young professionals, sharing my failures and successes. Watching them grow gave me a sense of legacy and meaning.", "legacy", "9", "xander_teaching"},
            {"I left a high-paying job to pursue writing, my true passion. The struggle was real, but the fulfillment of living my values was priceless.", "alignment", "8", "xander_purpose"},
            {"After my father's death, I started a foundation in his name. Honoring his memory through service gave my grief direction and meaning.", "grief", "9", "xander_foundation"},
            {"I traveled to developing countries and realized how much I took for granted. This perspective shift made me more grateful and purposeful.", "perspective", "8", "xander_travel"},
            {"Teaching my skills to underprivileged youth showed me that true wealth comes from what you give, not what you accumulate.", "giving", "9", "xander_education"},
            {"I wrote letters to my future self for years. Reading them showed me how my small actions created a meaningful life story.", "reflection", "8", "xander_letters"},
            {"Caring for my aging parents taught me that sometimes purpose comes from duty, and duty can become the deepest form of love.", "caregiving", "9", "xander_family"}
        };
        addAnecdotes(AnecdoteCategory.PURPOSE_MEANING, stories);
    }

    private void initializeWorkCareerAnecdotes() {
        String[][] stories = {
            {"I attended a conference feeling awkward about networking. A casual conversation led to my dream job offer six months later.", "connections", "9", "xander_professional"},
            {"My industry was being disrupted by technology. I spent nights learning coding, and it saved my career and opened new opportunities.", "growth", "8", "xander_development"},
            {"I hated my job for years until I found meaning in mentoring juniors. Suddenly work became fulfilling and I looked forward to Mondays.", "passion", "10", "xander_fulfillment"},
            {"I was passed over for promotion three times. Instead of quitting, I documented my achievements and presented my case. I got the next one.", "advocacy", "9", "xander_promotion"},
            {"Burned out from 80-hour weeks, I started saying no to extra work. My productivity increased and I got promoted for quality over quantity.", "boundaries", "8", "xander_balance"},
            {"I failed at my first business attempt. The lessons learned made my second venture successful beyond my wildest dreams.", "resilience", "9", "xander_entrepreneurship"},
            {"I was stuck in a dead-end job for years. Taking an online course in a new skill field led to a complete career pivot and happiness.", "transition", "8", "xander_career_change"},
            {"My boss was toxic, making work miserable. Learning to set boundaries and focus on my growth helped me thrive despite the environment.", "resilience", "9", "xander_workplace"}
        };
        addAnecdotes(AnecdoteCategory.WORK_CAREER, stories);
    }

    private void initializeMoneyMaterialAnecdotes() {
        String[][] stories = {
            {"Living below means led to financial freedom and peace of mind.", "frugality", "9", "personal_finance"},
            {"Investing in experiences over things brought lasting happiness.", "priorities", "8", "lifestyle"},
            {"Budgeting helped a family save for their dream home.", "planning", "9", "achievement"}
        };
        addAnecdotes(AnecdoteCategory.MONEY_MATERIAL, stories);
    }

    private void initializeHealthBodyAnecdotes() {
        String[][] stories = {
            {"I sat at a desk for 10 years, feeling sluggish. Starting daily walks transformed my energy and mental clarity.", "fitness", "9", "xander_transformation"},
            {"I burned the candle at both ends for years. Prioritizing 8 hours of sleep improved my productivity, mood, and decision-making.", "rest", "8", "xander_wellness"},
            {"Chronic fatigue and poor diet led to health issues. Switching to whole foods reversed my symptoms and gave me back my vitality.", "nutrition", "10", "xander_recovery"},
            {"I ignored stress until it caused physical symptoms. Learning stress management techniques healed both body and mind.", "stress", "9", "xander_stress"},
            {"I used to skip breakfast and eat junk. Mindful eating habits improved my digestion, energy, and relationship with food.", "habits", "8", "xander_eating"},
            {"A health scare made me realize I was immortal in my mind but not my body. Regular check-ups became my commitment to longevity.", "prevention", "9", "xander_prevention"},
            {"I worked through injuries for years. Rest and proper recovery made me stronger and taught me to listen to my body's signals.", "recovery", "8", "xander_injury"},
            {"I was always tired despite sleeping enough. Addressing underlying health issues transformed my energy and zest for life.", "vitality", "9", "xander_energy"}
        };
        addAnecdotes(AnecdoteCategory.HEALTH_BODY, stories);
    }

    private void initializeCreativityAnecdotes() {
        String[][] stories = {
            {"I thought I wasn't artistic, but drawing daily for a month unleashed creative talents I never knew existed. Art became my therapy.", "expression", "9", "xander_art"},
            {"Stuck on a complex work problem, I took a creative approach instead of logical. The outside-the-box solution impressed everyone.", "innovation", "8", "xander_problem_solving"},
            {"I started writing stories as a hobby. Sharing them online brought unexpected income and a community of fellow writers.", "creation", "9", "xander_hobby"},
            {"Blocked creatively for months, I traveled alone. New experiences and perspectives broke through my creative barriers.", "inspiration", "8", "xander_travel"},
            {"I learned music as an adult, thinking it was too late. The joy of creating melodies proved creativity has no age limit.", "music", "9", "xander_music"},
            {"Photography started as a way to document my life. It became a creative outlet that helped me see beauty in everyday moments.", "photography", "8", "xander_photography"},
            {"I joined an improv class to overcome shyness. The spontaneous creativity boosted my confidence and social skills.", "improv", "9", "xander_improv"},
            {"Cooking was just necessity until I treated it as art. Experimenting with flavors became my creative meditation practice.", "cooking", "8", "xander_cooking"},
            {"I was terrified of public speaking until I tried slam poetry. The creative freedom helped me find my voice and overcome my fear.", "poetry", "8", "xander_poetry"},
            {"Building furniture started as a way to save money. Now it's my passion - turning raw wood into functional art is incredibly satisfying.", "woodworking", "9", "xander_woodworking"},
            {"I started sketching in coffee shops instead of scrolling my phone. The world became my inspiration gallery, and my sketches told my story.", "sketching", "8", "xander_sketching"}
        };
        addAnecdotes(AnecdoteCategory.CREATIVITY, stories);
    }

    private void initializeWisdomLearningAnecdotes() {
        String[][] stories = {
            {"Reading one book a week expanded his knowledge and perspective.", "reading", "9", "education", "Knowledge compounds like interest - small investments yield massive returns."},
            {"Learning from mistakes turned failures into valuable lessons.", "experience", "10", "growth", "Every setback is a setup for a comeback if you learn from it."},
            {"Curiosity led to lifelong learning and personal development.", "curiosity", "8", "mindset", "The most powerful tool for growth is an insatiable curiosity about the world."}
        };
        addAnecdotes(AnecdoteCategory.WISDOM_LEARNING, stories);
    }

    // ==================== HELPER METHODS ====================

    private void addAnecdotes(AnecdoteCategory category, String[][] stories) {
        for (String[] storyData : stories) {
            String story = storyData[0];
            String theme = storyData[1];
            int relevanceScore = Integer.parseInt(storyData[2]);
            String source = storyData[3];
            String lesson = storyData.length > 4 ? storyData[4] : "";
            anecdotes.add(new Anecdote(story, category, theme, relevanceScore, source, lesson));
        }
    }

    private void addParables(AnecdoteCategory category, String[][] parableData) {
        for (String[] data : parableData) {
            String parable = data[0];
            String theme = data[1];
            String moral = data[2];
            String symbolism = data[3];
            String source = data[4];
            parables.add(new Parable(parable, category, moral, symbolism, source));
        }
    }

    private void addFables(AnecdoteCategory category, String[][] fableData) {
        for (String[] data : fableData) {
            String story = data[0];
            String animalCharacter = data[1];
            String hiddenMeaning = data[2];
            String moral = data[3];
            String situation = data[4];
            int relevanceScore = Integer.parseInt(data[5]);
            fables.add(new Fable(story, category, animalCharacter, hiddenMeaning, moral, situation, relevanceScore));
        }
    }

    // ==================== PARABLE INITIALIZATIONS ====================

    private void initializeSelfAwarenessParables() {
        String[][] parables = {
            {"A man carried a heavy backpack everywhere he went, complaining about the weight. One day, he opened it and found it contained only his own judgments and expectations. He emptied it and walked freely.", "self_awareness", "The heaviest burdens we carry are often self-imposed judgments.", "The backpack represents the mental weight of self-criticism and societal expectations.", "ancient_wisdom"},
            {"A sculptor worked tirelessly on a statue, but it remained rough and unrefined. When he stepped back and viewed it from afar, he saw the masterpiece within the stone.", "perspective", "True self-understanding requires distance from our immediate struggles.", "The sculptor represents the conscious mind, the statue our true self.", "eastern_philosophy"},
            {"A king asked his wisest advisor how to rule justly. The advisor led him to a mirror and said, 'First, learn to rule yourself.'", "self_governance", "Leadership of others begins with mastery of oneself.", "The mirror symbolizes self-reflection and honest self-assessment.", "royal_counsel"}
        };
        addParables(AnecdoteCategory.SELF_AWARENESS, parables);
    }

    private void initializeRelationshipsParables() {
        String[][] parables = {
            {"Two trees grew side by side for years. When a storm came, the weaker one bent and survived while the stronger one broke. The lesson: flexibility in relationships endures more than rigid strength.", "flexibility", "Flexibility in relationships helps them endure challenges that rigid strength cannot.", "The trees represent partners adapting to life's storms together.", "nature_wisdom"},
            {"A long-married couple was asked the secret to their happiness. They replied, 'We learned early to apologize quickly and forgive completely.'", "forgiveness", "Quick apologies and complete forgiveness are the cement of lasting relationships.", "The couple represents commitment through mutual grace.", "life_lesson"},
            {"A river and a mountain argued about whose presence was more important. The river eventually carved a valley around the mountain, showing that gentle persistence shapes even the hardest hearts.", "persistence", "Gentle persistence can eventually shape even the most stubborn hearts.", "The river represents patient love that overcomes obstacles.", "nature_parable"}
        };
        addParables(AnecdoteCategory.RELATIONSHIPS, parables);
    }

    private void initializeSuccessParables() {
        String[][] parables = {
            {"A farmer planted seeds and waited patiently. His neighbor asked why he wasn't watering more. The farmer replied, 'Seeds know when to grow. I must only provide what they cannot give themselves.'", "patience", "True success comes from working in harmony with natural rhythms, not forcing outcomes.", "The seeds represent potential waiting for the right conditions.", "agricultural_wisdom"},
            {"A potter shaped clay on his wheel. Each piece that failed taught him something the successful ones never could. He honored his failures as the true teachers of his craft.", "failure_success", "Our failures often teach us more than our successes.", "The potter represents the artist learning through imperfect attempts.", "artisan_parable"},
            {"An archer missed his target 100 times. On the 101st shot, he asked the master why he succeeded. The master replied, 'You didn't fail 100 times. You learned 100 ways that wouldn't work.'", "learning", "Failure is not defeat but education on the path to mastery.", "The archer represents the persistent learner who transforms setbacks into lessons.", "martial_wisdom"}
        };
        addParables(AnecdoteCategory.SUCCESS, parables);
    }

    private void initializeFailureResilienceParables() {
        String[][] parables = {
            {"A bamboo tree was planted in harsh conditions. While other trees grew steadily, the bamboo seemed to die for years. Then suddenly, it shot up 90 feet in one season. The hidden years were building invisible roots.", "growth", "Sometimes what appears as failure or stagnation is actually the foundation of extraordinary growth.", "The bamboo represents quiet preparation for breakthrough.", "eastern_wisdom"},
            {"A broken clock sat in the antique shop, still showing the correct time twice a day. Its owner said, 'Even broken things have their moments of perfection.'", "timing", "Even in our lowest moments, we still have the capacity to be right.", "The broken clock represents the resilience found in imperfection.", "folk_wisdom"},
            {"A fisherman pulled up an empty net for the seventh time. Rather than despairing, he said, 'The sea has given me another beautiful day and taught me patience. The fish will come.'", "perspective", "How we frame failure determines whether it defeats us or teaches us.", "The fisherman represents optimistic resilience in the face of repeated setbacks.", "sea_parable"}
        };
        addParables(AnecdoteCategory.FAILURE_RESILIENCE, parables);
    }

    private void initializeHappinessParables() {
        String[][] parables = {
            {"A king searched his vast gardens for the happiest flower. After tasting many bitter ones, he found a simple daisy smiling at the sun. The daisy said, 'I make my own joy from rain and soil.'", "inner_joy", "Happiness comes from within, not from external circumstances.", "The daisy represents finding contentment through simplicity.", "garden_wisdom"},
            {"A merchant complained he had no money for happiness. A sage replied, 'Buy a candle and light it in a dark room. You will see how a small light illuminates everything.'", "perspective", "Happiness is found in how we illuminate what we already have.", "The candle represents finding light in existing darkness.", "spiritual_wisdom"},
            {"Two monks watched rain falling on a beautiful lake. One saw the water ruining his meditation. The other saw the rain nourishing life. Same rain, different eyes.", "perception", "Happiness is often a matter of how we perceive our circumstances.", "The monks represent the choice between gratitude and complaint.", "buddhist_parable"}
        };
        addParables(AnecdoteCategory.HAPPINESS, parables);
    }

    private void initializeTimePrioritiesParables() {
        String[][] parables = {
            {"A man rushed through life accomplishing much but remembering nothing. On his deathbed, he asked for one more day to simply watch a sunset. Time given to presence is never wasted.", "presence", "True richness comes from experiencing life, not just accomplishing tasks.", "The sunset represents the beauty we miss in our haste.", "life_lesson"},
            {"A gardener spent years planting trees that wouldn't bear fruit in his lifetime. When asked why, he replied, 'I am planting for my grandchildren and the future. Some work transcends our own harvest.'", "legacy", "Some of the most important work benefits those who come after us.", "The gardener represents investing in futures beyond our own.", "agricultural_wisdom"},
            {"A clockmaker spent his life making perfect timepieces. On his last day, he sat by his window watching clouds, saying, 'For the first time, I am truly on time with myself.'", "balance", "Being on time with our souls matters more than being on time for schedules.", "The clockmaker represents the wisdom of living in harmony with natural rhythms.", "craftsman_parable"}
        };
        addParables(AnecdoteCategory.TIME_PRIORITIES, parables);
    }

    private void initializeCommunicationParables() {
        String[][] parables = {
            {"Two neighbors had a wall between them for 20 years. When one died, the survivor discovered there had never been a wall at all - only a misunderstanding carried for two decades.", "misunderstanding", "Many barriers we perceive are illusions created by poor communication.", "The wall represents the barriers we build through assumption.", "neighbor_wisdom"},
            {"A wise teacher spoke so softly that students had to lean in to hear. They remembered everything he said. The loud teachers were forgotten within a week.", "presence", "True communication requires presence and earns attention through depth, not volume.", "The teacher represents the power of measured speech.", "teaching_parable"},
            {"A mirror reflects exactly what is shown. If the image is distorted, the fault lies not in the mirror but in what stands before it. Words are mirrors of our inner truth.", "honesty", "How we communicate reflects who we truly are inside.", "The mirror represents the honesty of reflected communication.", "ancient_wisdom"}
        };
        addParables(AnecdoteCategory.COMMUNICATION, parables);
    }

    private void initializeChangeUncertaintyParables() {
        String[][] parables = {
            {"A caterpillar clung to a branch, afraid to let go. The branch itself was breaking. A wise bird said, 'You fear becoming a butterfly, yet you hold onto what is also dying.'", "transformation", "Sometimes we must release the familiar to embrace transformation.", "The caterpillar represents fear of the unknown preventing growth.", "nature_wisdom"},
            {"A sailor navigated by stars, but clouds covered the sky for weeks. He learned to read the waves and wind. When the stars returned, he had become a better navigator.", "adaptation", "Uncertainty teaches us skills we never would have learned otherwise.", "The sailor represents growth through navigating without familiar guides.", "sea_parable"},
            {"An old farmer's house burned down. His neighbors mourned the loss, but he smiled. 'Now I can finally build the house I've always dreamed of,' he said.", "opportunity", "What appears as disaster may be the universe clearing space for something better.", "The house represents the old circumstances we must release.", "folk_wisdom"}
        };
        addParables(AnecdoteCategory.CHANGE_UNCERTAINTY, parables);
    }

    private void initializeMindfulnessParables() {
        String[][] parables = {
            {"A master calligrapher was asked to write the perfect character. He meditated for three days, then wrote one simple stroke. Students asked why it took so long. 'The three days were the writing,' he replied.", "presence", "True mastery exists in the preparation and presence, not just the action.", "The calligraphy stroke represents distilled wisdom from mindful practice.", "eastern_philosophy"},
            {"A monk walked across hot coals without flinching. When asked how, he replied, 'I placed each foot down with complete attention. There is no pain in complete presence.'", "attention", "Pain exists in our divided attention, not in the present moment.", "The hot coals represent life's challenges that transform with mindful presence.", "martial_parable"},
            {"A river flowed past a wise woman sitting on its bank. She wept, saying the water was leaving forever. The river replied, 'I am not leaving; I am becoming the sea.'", "impermanence", "Nothing is truly lost; everything transforms.", "The river represents the continuous flow of existence.", "nature_wisdom"}
        };
        addParables(AnecdoteCategory.MINDFULNESS, parables);
    }

    private void initializePurposeMeaningParables() {
        String[][] parables = {
            {"A sculptor was given a block of marble. Other artists took pieces for their works, but he alone saw the angel trapped inside. He spent his life freeing her. His purpose was not to use the stone but to liberate what it could become.", "vision", "Our purpose is to see the potential in things and help them become what they're meant to be.", "The marble represents our own potential awaiting discovery.", "artisan_wisdom"},
            {"A lantern in the village square had burned for 100 years. When asked if it wasn't tired, it replied, 'I was born to burn. Purpose is not a burden but my nature.'", "purpose", "True purpose feels natural, not like obligation.", "The lantern represents living one's essential nature.", "folk_wisdom"},
            {"A single candle in a dark cathedral was asked if it felt insignificant. It replied, 'I was never meant to light the whole world, just to help one person find their way.'", "contribution", "Our purpose is not to change everything but to serve what is before us.", "The candle represents focused service in the darkness.", "spiritual_parable"}
        };
        addParables(AnecdoteCategory.PURPOSE_MEANING, parables);
    }

    private void initializeWorkCareerParables() {
        String[][] parables = {
            {"A master carpenter was asked why he still swept his own workshop floor at 80 years old. He replied, 'The floor teaches me humility. I sweep so I never forget where I started.'", "humility", "Never forgetting our roots keeps us grounded in success.", "The workshop floor represents the humble beginnings that anchor achievement.", "craftsman_wisdom"},
            {"An apprentice complained he was only carrying water and wood. The master replied, 'You are learning to carry the weight of the craft before you can lift its glory.'", "preparation", "Every task, however humble, is preparation for greater work.", "The water and wood represent foundational preparation.", "martial_parable"},
            {"A weaver created a beautiful tapestry but noticed one imperfect stitch. Rather than undo it, she incorporated it into a design. The 'mistake' became the tapestry's most celebrated feature.", "adaptation", "What we perceive as mistakes can become our most valuable assets.", "The tapestry represents the career we create from imperfect steps.", "artisan_parable"}
        };
        addParables(AnecdoteCategory.WORK_CAREER, parables);
    }

    private void initializeMoneyMaterialParables() {
        String[][] parables = {
            {"A rich man and a poor man sat by the same river. The rich man saw water worth bottling and selling. The poor man saw water to drink and be refreshed. Both saw the river; only one truly saw it.", "perspective", "Wealth is as much about how we see things as what we possess.", "The river represents natural resources and opportunities.", "folk_wisdom"},
            {"A merchant counted his gold coins obsessively. A sage asked, 'Have you counted the sunrises you've seen or the kindness you've given?' The merchant realized his treasure had no meaning.", "value", "True wealth cannot be counted in coins but in experiences and love.", "The coins represent the hollow satisfaction of materialism.", "spiritual_wisdom"},
            {"A family inherited a small plot of land with an ancient oak tree. Developers offered millions, but they refused. 'This tree watched our great-great-grandparents marry. No amount of money can buy memories.'", "legacy", "Some things are priceless because they carry our history and heritage.", "The oak tree represents irreplaceable family legacy.", "nature_parable"}
        };
        addParables(AnecdoteCategory.MONEY_MATERIAL, parables);
    }

    private void initializeHealthBodyParables() {
        String[][] parables = {
            {"A runner was offered a shortcut to the mountaintop. He refused, saying, 'The path is my teacher. Every step strengthens my legs and teaches my heart.'", "journey", "Health is not a destination but a way of traveling through life.", "The mountaintop represents physical goals achieved through worthy means.", "nature_wisdom"},
            {"A musician's hands grew stiff with age. Rather than despair, she learned to play slower, more beautiful melodies. 'My fingers have wisdom now that speed could never teach,' she said.", "wisdom", "Physical changes bring new capacities if we embrace them gracefully.", "The music represents the adaptability of our bodies through time.", "artistic_parable"},
            {"A warrior injured in battle was ashamed of his limp. His master replied, 'That limp is your teacher's voice, reminding you that every step matters.'", "listening", "Our body's limitations are teachers, not enemies.", "The limp represents the wisdom gained through bodily experience.", "martial_wisdom"}
        };
        addParables(AnecdoteCategory.HEALTH_BODY, parables);
    }

    private void initializeCreativityParables() {
        String[][] parables = {
            {"An apprentice complained he had no original ideas. The master gave him a blank canvas and said, 'Originality is not creating from nothing, but seeing something new in what already exists.'", "vision", "Creativity is not about invention but about fresh perception.", "The blank canvas represents the false notion of starting completely anew.", "artistic_wisdom"},
            {"A potter's wheel spun for 50 years, creating thousands of vessels. When asked about his favorite piece, he pointed to a cracked bowl. 'This failure taught me everything I know.'", "learning", "Our creative failures are often our greatest teachers.", "The cracked bowl represents the value of apparent mistakes in artistic growth.", "craftsman_parable"},
            {"A poet was asked where her songs came from. She replied, 'I don't create them; I simply notice what the world has been singing all along.'", "receptivity", "True creativity is receptivity to what already exists.", "The songs represent the pre-existing beauty waiting to be perceived.", "artistic_wisdom"}
        };
        addParables(AnecdoteCategory.CREATIVITY, parables);
    }

    private void initializeWisdomLearningParables() {
        String[][] parables = {
            {"A scholar collected thousands of books. A simple farmer asked, 'Have you read them all?' The scholar proudly said many. The farmer then asked, 'And how many have changed how you live?'", "application", "True wisdom is not how much we know but how much we live what we know.", "The books represent accumulated knowledge without transformation.", "folk_wisdom"},
            {"An elder was asked when he learned most. He replied, 'When I realized I knew nothing. The day I graduated from certainty was my first day of real education.'", "humility", "The beginning of wisdom is acknowledging our ignorance.", "The graduation represents the transition from presumed knowledge to genuine learning.", "philosophical_parable"},
            {"A student asked the master, 'How do I become wise?' The master replied, 'Make one wise choice today. Then tomorrow, make two. Wisdom is built choice by choice.'", "practice", "Wisdom develops through daily practice, not sudden revelation.", "The choices represent the accumulation of principled decisions.", "teaching_wisdom"}
        };
        addParables(AnecdoteCategory.WISDOM_LEARNING, parables);
    }

    // ==================== FABLE INITIALIZATIONS ====================

    private void initializeSelfAwarenessFables() {
        String[][] fables = {
            {"A proud lion strutted through the forest, boasting to every creature about his magnificent mane. A small mouse scurried by and giggled. 'Your mane is impressive, but what hides behind those eyes?' The lion was offended until he looked in the pond and saw his own reflection - fierce outside, but his eyes showed only emptiness. From that day, he learned that true beauty radiates from within.", 
             "Lion", 
             "True beauty and worth come from inner qualities, not external appearance",
             "What matters most is not what you look like, but who you are inside",
             "when comparing yourself to others or feeling vain",
             "9"},
            {"A clever fox found a beautiful mirror in the forest. Each time he looked, he saw a wise and powerful fox. He spent hours admiring himself until hunger reminded him he hadn't hunted in days. He realized he had been so busy looking at his reflection that he forgot to live. From then on, he visited the mirror only once a week.",
             "Fox",
             "Self-admiration without action leads to starvation of both body and soul",
             "Time spent admiring yourself is time stolen from living purposefully",
             "when wasting time on self-image instead of substance",
             "8"}
        };
        addFables(AnecdoteCategory.SELF_AWARENESS, fables);
    }

    private void initializeRelationshipsFables() {
        String[][] fables = {
            {"Two hedgehogs huddled together for warmth in winter, but their spines pricked each other. They moved apart, feeling cold, then moved closer again, feeling pain. Finally, they found the perfect distance - close enough to feel each other's warmth, far enough to avoid the spines. They learned that love requires boundaries.",
             "Hedgehog",
             "True love means finding the distance where we can be close without hurting each other",
             "Healthy relationships need space for both people to be themselves",
             "when dealing with relationship conflicts or boundaries",
             "10"},
            {"A lonely wolf howled for a companion. The mountains echoed back his howl, and he thought he found a friend. For months he conversed with the echo, thinking it understood him. When he finally met another wolf and tried to share his thoughts, he realized the echo had never truly listened. He had been talking to himself all along.",
             "Wolf",
             "We cannot mistake our own echoes for genuine connection with others",
             "True relationships require real presence, not just our own voice reflected back",
             "when feeling lonely or confusing solitude for connection",
             "9"}
        };
        addFables(AnecdoteCategory.RELATIONSHIPS, fables);
    }

    private void initializeSuccessFables() {
        String[][] fables = {
            {"A swift falcon mocked the slow turtle for his pace. The turtle proposed a race to the distant hill. The falcon laughed and flew high, circling playfully, arriving at the hill before the turtle even crossed the meadow. But when he landed, he found the turtle already there, having taken a shortcut through the valley the falcon couldn't see from the sky. The falcon learned that speed matters less than knowing the right path.",
             "Falcon and Turtle",
             "Slow and steady wins when you choose the right path, not necessarily the fastest one",
             "Success comes from wisdom in choosing direction, not just speed of execution",
             "when rushing toward goals without proper planning",
             "10"},
            {"An ambitious ant worked tirelessly, carrying burdens ten times his weight. A lazy grasshopper laughed and played all summer. When winter came, the ant had provisions for months while the grasshopper starved. But the ant noticed the grasshopper's songs had brought joy to the forest all summer, while his own life had been only work. He learned that provisions for the body don't feed the soul.",
             "Ant and Grasshopper",
             "Balance between preparation for tomorrow and living for today creates a full life",
             "Both work and joy have their place in a meaningful life",
             "when work consumes all joy or play replaces responsibility",
             "9"}
        };
        addFables(AnecdoteCategory.SUCCESS, fables);
    }

    private void initializeFailureResilienceFables() {
        String[][] fables = {
            {"A brave sparrow tried to lift a heavy stone to build her nest. She failed repeatedly, exhausting herself. A wise owl observed and said, 'The stone is too heavy because you see it as an obstacle. But look - there's a perfect hollow nearby where water collects after rain. Nature has already provided what you need.' The sparrow found the hollow and built her nest. She learned that sometimes failure points us to better solutions we couldn't see.",
             "Sparrow",
             "When one door closes, look for the window nature has already opened",
             "Our failures often redirect us toward better opportunities",
             "when facing repeated failures or setbacks",
             "10"},
            {"A determined rabbit raced a tortoise and lost humiliatingly. For days he hid in shame. But then he noticed other rabbits had stopped racing altogether, afraid of losing. He organized a new race, this time just for fun and practice. He lost again, but he celebrated anyway. He learned that the purpose of trying is not to avoid losing, but to keep trying.",
             "Rabbit",
             "Success is not defined by winning, but by having the courage to keep trying",
             "Each attempt, even unsuccessful, builds courage and skill",
             "after experiencing failure or embarrassment from trying",
             "9"}
        };
        addFables(AnecdoteCategory.FAILURE_RESILIENCE, fables);
    }

    private void initializeHappinessFables() {
        String[][] fables = {
            {"A cheerful butterfly fluttered from flower to flower, spreading joy everywhere she went. A grumpy beetle watched and wondered, 'Why is she so happy while I trudge through the same dirt?' He asked the butterfly her secret. She replied, 'I don't look for happiness in the dirt. I look for flowers in the sky.' The beetle looked up and saw colors he'd never noticed before.",
             "Butterfly and Beetle",
             "Happiness is found in where we choose to look",
             "Our perspective shapes our experience of the world",
             "when feeling gloomy or stuck in a negative mindset",
             "9"},
            {"A wealthy merchant found a magic lamp and rubbed it, expecting a genie to grant wishes. Instead, a wise jinn appeared and said, 'I grant you one wish - but you must give away everything you own that doesn't bring you joy.' The merchant spent weeks giving away possessions, and with each gift, he felt lighter. When he was done, he had nothing left. The jinn smiled and said, 'You finally understand - joy cannot be owned, only shared.'",
             "Merchant",
             "True happiness comes from sharing, not possessing",
             "The more we let go of things, the more space we create for joy",
             "when pursuing material happiness without fulfillment",
             "8"}
        };
        addFables(AnecdoteCategory.HAPPINESS, fables);
    }

    private void initializeTimePrioritiesFables() {
        String[][] fables = {
            {"A busy beaver worked constantly, building dam after dam, always believing he needed one more. His friend otter invited him to fish, swim, and relax by the stream. 'I have no time for fun,' the beaver said, 'there are always more logs to move.' Years passed, and the beaver had built the greatest dam in the forest, but his friend otter had shared a thousand sunsets with loved ones. The beaver realized his dam would last forever, but his life had passed without living.",
             "Beaver",
             "Time spent building monuments to yourself is time stolen from living",
             "What we build lasts, but how we live matters more",
             "when work consumes all time for life and relationships",
             "10"},
            {"A wise tortoise was asked why he moved so slowly. He replied, 'I see everything. The flight of butterflies, the bloom of flowers, the changing clouds. When you rush, you miss the journey.' A hurried hare laughed and raced past, but later couldn't remember the path he'd taken. The tortoise smiled, 'I'll remember this path forever because I actually walked it.'",
             "Tortoise",
             "Slowing down allows us to fully experience life's journey",
             "What we experience mindfully becomes part of us",
             "when rushing through life without presence",
             "8"}
        };
        addFables(AnecdoteCategory.TIME_PRIORITIES, fables);
    }

    private void initializeCommunicationFables() {
        String[][] fables = {
            {"Two owls sat in adjacent trees and hooted at each other all night, each believing the other was arguing. By morning, they were furious. A wise raven listened to both and discovered they had both said 'good morning' in their own dialect. They had spent an entire night in a misunderstanding that took one moment to clarify.",
             "Owls",
             "Most conflicts stem from misunderstanding, not actual disagreement",
             "Before assuming conflict, seek to understand what the other truly means",
             "when in conflict or misunderstanding with someone",
             "9"},
            {"A eloquent parrot spoke beautifully, impressing all with his vocabulary. But when his forest friends needed comfort after a storm, he could only describe their sadness in perfect words. A silent turtle offered a shell to shelter them and a warm presence. The friends thanked the turtle more than the parrot. The parrot learned that sometimes a hug speaks louder than a dictionary.",
             "Parrot and Turtle",
             "Actions rooted in compassion speak louder than beautiful words",
             "True communication is about connection, not impressing with language",
             "when relying on words instead of heartfelt action",
             "8"}
        };
        addFables(AnecdoteCategory.COMMUNICATION, fables);
    }

    private void initializeChangeUncertaintyFables() {
        String[][] fables = {
            {"A comfortable caterpillar feared the cocoon, knowing she would emerge completely transformed. But staying meant certain death as winter came. She entered the cocoon, surrendering to the darkness. When she emerged, she was a beautiful butterfly with wings that could carry her to places the caterpillar could never imagine. She learned that transformation, though terrifying, opens doors to new possibilities.",
             "Caterpillar",
             "Fear of change traps us in comfortable prisons",
             "The terrifying process of transformation leads to unexpected freedom",
             "when facing significant life changes or transformations",
             "10"},
            {"A wandering camel asked a camel merchant, 'How do you know which direction to go through the desert?' The merchant replied, 'I don't. The desert changes daily. I only know that wherever I go, I must move forward. The destination is not in my control, but the journey is.'",
             "Camel",
             "We cannot control the path, only our willingness to move forward",
             "Courage means advancing despite uncertainty",
             "when uncertain about the future or direction",
             "9"}
        };
        addFables(AnecdoteCategory.CHANGE_UNCERTAINTY, fables);
    }

    private void initializeMindfulnessFables() {
        String[][] fables = {
            {"A monkey jumped from branch to branch, always looking for better bananas. He never tasted what he ate because he was already searching for the next bunch. An old elephant bathed in the river, fully present with each splash. The monkey laughed at the elephant's 'waste' of time. But the elephant tasted every droplet of water, felt every ray of sun, and remembered each peaceful moment forever. The monkey could never recall a single beautiful day.",
             "Monkey",
             "Presence transforms ordinary moments into cherished memories",
             "Rushing through life means missing life itself",
             "when always looking ahead instead of being present",
             "9"},
            {"A wise owl was asked how he stayed so calm during storms. He replied, 'I don't resist the storm. I find shelter, and while waiting, I notice how the rain dances, how the wind sings, how the lightning paints the sky. The storm will pass, but while it lasts, I choose to witness its beauty.'",
             "Owl",
             "What we cannot control, we can choose how to experience",
             "Acceptance transforms suffering into observation",
             "when facing difficult circumstances you cannot change",
             "8"}
        };
        addFables(AnecdoteCategory.MINDFULNESS, fables);
    }

    private void initializePurposeMeaningFables() {
        String[][] fables = {
            {"A small ant carried crumbs ten times her weight, day after tedious day. A philosophical beetle watched and asked, 'Don't you find this meaningless? Carrying food you'll never fully enjoy, following the same path forever?' The ant replied, 'The meaning is not in enjoying the destination. The meaning is in the carrying itself. I carry food for my colony, purpose for my children, contribution to something larger than myself.'",
             "Ant",
             "Purpose is found not in personal enjoyment but in contribution to something greater",
             "Meaning comes from significance beyond ourselves",
             "when questioning the purpose of daily work or routine",
             "10"},
            {"A magnificent peacock flaunted his feathers, seeking admiration from every creature. But when winter came and his feathers fell, he felt invisible. A plain dove flew beside him and said, 'Your feathers were beautiful, but they were borrowed from nature. Your true self is what remains when everything external is gone.' The peacock learned that identity built on appearance crumbles when appearance fades.",
             "Peacock",
             "Our worth is not in what we display, but in who we are when no one is watching",
             "Authentic value comes from inner qualities, not external trappings",
             "when building identity on superficial foundations",
             "9"}
        };
        addFables(AnecdoteCategory.PURPOSE_MEANING, fables);
    }

    private void initializeWorkCareerFables() {
        String[][] fables = {
            {"A hardworking bee buzzed from flower to flower, never stopping to enjoy any of them. 'I must be productive,' he buzzed. A lazy butterfly asked, 'What are you producing all this for?' The bee replied, 'Honey for the hive.' The butterfly asked, 'And what is the honey for?' The bee paused, realizing he'd never asked this question. Some things are worth questioning.",
             "Bee and Butterfly",
             "Questioning the purpose of our work prevents empty productivity",
             "Efficiency without purpose is merely motion, not progress",
             "when working hard without understanding why",
             "9"},
            {"A dedicated carpenter spent decades crafting perfect furniture. When asked about his masterpiece, he pointed to a simple chair he made as an apprentice - imperfect but filled with love. 'The later pieces are better made, but this one holds my heart.' He learned that excellence in career doesn't determine the value we leave behind.",
             "Carpenter",
             "The value of our work is not measured by its polish but by the intention behind it",
             "Passion matters more than perfection",
             "when pursuing perfection at the expense of authenticity",
             "8"}
        };
        addFables(AnecdoteCategory.WORK_CAREER, fables);
    }

    private void initializeMoneyMaterialFables() {
        String[][] fables = {
            {"A rich squirrel gathered thousands of acorns, hoarding them in ten different trees. Winter came and he couldn't remember where half his acorns were hidden. He starved while other squirrels with fewer acorns but better memories survived. He learned that abundance without organization is illusion.",
             "Squirrel",
             "Wealth without wisdom of application is wasted potential",
             "More is not always better - organization and understanding matter",
             "when accumulating without purpose or organization",
             "9"},
            {"A generous bird shared seeds with every creature in the forest. Other birds laughed, 'You're giving away your provisions!' But the recipients remembered her kindness and brought her treasures from distant lands. She learned that the seeds of generosity yield harvests of love that money cannot buy.",
             "Bird",
             "Generosity plants seeds of goodwill that return in unexpected ways",
             "What we give freely often comes back multiplied",
             "when hoarding resources instead of sharing",
             "8"}
        };
        addFables(AnecdoteCategory.MONEY_MATERIAL, fables);
    }

    private void initializeHealthBodyFables() {
        String[][] fables = {
            {"A strong lion ignored his body's warning signs, believing his roar made him invincible. He pushed through pain until one day, he couldn't rise. A gentle deer visited him and said, 'Strength is not the absence of weakness, but knowing when to rest.' The lion learned that ignoring the body doesn't make it stronger - it makes it broken.",
             "Lion",
             "Our bodies communicate with us - ignoring warnings leads to collapse",
             "Strength includes wisdom about our own limits",
             "when pushing through physical or mental warning signs",
             "10"},
            {"A spry rabbit hopped everywhere, mocking the slow turtle. Years passed and the rabbit's joints ached while the turtle, who had always paced himself, moved with comfortable grace. The rabbit learned that speed in youth doesn't guarantee mobility in age.",
             "Rabbit",
             "How we treat our bodies now affects our quality of life later",
             "Moderation and balance serve us better than excess",
             "when young and invulnerable, ignoring long-term health",
             "9"}
        };
        addFables(AnecdoteCategory.HEALTH_BODY, fables);
    }

    private void initializeCreativityFables() {
        String[][] fables = {
            {"A talented painter created beautiful art but destroyed each piece when it wasn't perfect. An artless child picked up his paintbrushes and created wild, imperfect paintings. When the child showed joy in creation, the painter realized he had spent years destroying rather than creating. The purpose of art is not perfection - it's expression.",
             "Painter",
             "Perfectionism kills creativity before it can be born",
             "The value of creation is in the creating, not the judging",
             "when fear of imperfection prevents creation",
             "9"},
            {"A musical nightingale sang the same song every night, perfectly. A traveling sparrow heard new songs in every forest and adapted his melody constantly. The nightingale mocked the sparrow's inconsistencies. But when a great storm destroyed their forest, the adaptable sparrow learned new songs in new trees. The rigid nightingale could only mourn the old ones.",
             "Nightingale and Sparrow",
             "Creativity that adapts survives when rigid perfection cannot",
             "Flexibility in expression leads to resilience",
             "when stuck in rigid ways of doing things",
             "8"}
        };
        addFables(AnecdoteCategory.CREATIVITY, fables);
    }

    private void initializeWisdomLearningFables() {
        String[][] fables = {
            {"A wise owl had read every book in the forest library. A curious mouse asked, 'If you've read everything, why do you still sit with the ancient tortoise every week?' The owl replied, 'Books contain knowledge, but the tortoise contains wisdom. Knowledge answers questions; wisdom knows which questions to ask.'",
             "Owl and Tortoise",
             "Wisdom is found in relationship and experience, not just books",
             "Learning from the living is as important as learning from the written",
             "when relying only on formal education or books",
             "10"},
            {"A confident young fox thought he knew everything after his first hunt. An old fox observed and asked, 'How many ways can a rabbit escape?' The young fox listed three. The old fox named twelve. The young fox realized that experience compounds - what seems like mastery is barely the beginning.",
             "Young Fox and Old Fox",
             "What we don't know is often vastly larger than what we know",
             "Every level of expertise opens doors to new depths of ignorance",
             "when thinking you have mastered a subject",
             "9"}
        };
        addFables(AnecdoteCategory.WISDOM_LEARNING, fables);
    }

    // ==================== FABLE RETRIEVAL METHODS ====================

    public String getRandomFable() {
        if (fables.isEmpty()) {
            return "No fables available yet.";
        }
        return fables.get(random.nextInt(fables.size())).getStory();
    }

    public String getFableByCategory(AnecdoteCategory category) {
        List<String> relevant = new ArrayList<>();
        for (Fable fable : fables) {
            if (fable.getCategory() == category) {
                relevant.add(fable.getStory());
            }
        }
        if (relevant.isEmpty()) return getRandomFable();
        return relevant.get(random.nextInt(relevant.size()));
    }

    public String getFableForSituation(String situation) {
        String lower = situation.toLowerCase();
        if (lower.contains("fail") || lower.contains("wrong") || lower.contains("mistake")) {
            return getFableByCategory(AnecdoteCategory.FAILURE_RESILIENCE);
        }
        if (lower.contains("happy") || lower.contains("joy") || lower.contains("good")) {
            return getFableByCategory(AnecdoteCategory.HAPPINESS);
        }
        if (lower.contains("busy") || lower.contains("time") || lower.contains("when")) {
            return getFableByCategory(AnecdoteCategory.TIME_PRIORITIES);
        }
        if (lower.contains("relate") || lower.contains("friend") || lower.contains("love")) {
            return getFableByCategory(AnecdoteCategory.RELATIONSHIPS);
        }
        if (lower.contains("change") || lower.contains("uncertain") || lower.contains("fear")) {
            return getFableByCategory(AnecdoteCategory.CHANGE_UNCERTAINTY);
        }
        return getRandomFable();
    }

    public String getFableWithHiddenMeaning(String situation) {
        Fable fable = getRelevantFable(situation);
        if (fable == null) {
            return "No relevant fable found.";
        }
        return "Fable: " + fable.getStory() + 
               "\n\nAnimal Character: " + fable.getAnimalCharacter() +
               "\n\nHidden Meaning: " + fable.getHiddenMeaning() +
               "\n\nMoral: " + fable.getMoral();
    }

    public int getTotalFables() {
        return fables.size();
    }

    public Fable getRelevantFable(String context) {
        if (context == null || context.trim().isEmpty()) {
            if (fables.isEmpty()) return null;
            return fables.get(random.nextInt(fables.size()));
        }

        String lowerContext = context.toLowerCase();
        String[] keywords = lowerContext.split("\\s+");

        Fable bestMatch = null;
        int bestScore = 0;

        for (Fable fable : fables) {
            int matchScore = calculateFableRelevanceScore(fable, keywords);
            if (matchScore > bestScore) {
                bestScore = matchScore;
                bestMatch = fable;
            }
        }

        return bestMatch;
    }

    private int calculateFableRelevanceScore(Fable fable, String[] keywords) {
        int score = 0;
        String storyLower = fable.getStory().toLowerCase();
        String situation = fable.getSituation().toLowerCase();
        String hiddenMeaning = fable.getHiddenMeaning().toLowerCase();

        for (String keyword : keywords) {
            if (keyword.length() < 3) continue;

            // Score based on situation match (highest weight)
            if (situation.contains(keyword)) {
                score += 12;
            }

            // Score based on hidden meaning match
            if (hiddenMeaning.contains(keyword)) {
                score += 10;
            }

            // Score based on story content match
            if (storyLower.contains(keyword)) {
                score += 5;
            }
        }

        // Add relevance score from the fable itself
        score += fable.getRelevanceScore() / 2;

        return score;
    }

    public List<String> getRelevantFables(String context, int count) {
        List<String> results = new ArrayList<>();
        if (context == null || context.trim().isEmpty()) {
            for (int i = 0; i < count && i < fables.size(); i++) {
                results.add(getRandomFable());
            }
            return results;
        }

        String lowerContext = context.toLowerCase();
        String[] keywords = lowerContext.split("\\s+");

        List<Fable> scored = new ArrayList<>();
        for (Fable fable : fables) {
            int score = calculateFableRelevanceScore(fable, keywords);
            scored.add(fable);
        }

        scored.sort((a, b) -> {
            int scoreA = calculateFableRelevanceScore(a, keywords);
            int scoreB = calculateFableRelevanceScore(b, keywords);
            return Integer.compare(scoreB, scoreA);
        });

        Random random = new Random();
        int returnCount = Math.min(count, scored.size());
        Set<Integer> usedIndices = new HashSet<>();

        while (results.size() < returnCount && usedIndices.size() < scored.size()) {
            int index = random.nextInt(scored.size());
            if (!usedIndices.contains(index)) {
                usedIndices.add(index);
                results.add(scored.get(index).getStory());
            }
        }

        return results;
    }

    // ==================== PARABLE RETRIEVAL METHODS ====================

    public String getRandomParable() {
        if (parables.isEmpty()) {
            return "No parables available yet.";
        }
        return parables.get(random.nextInt(parables.size())).getParable();
    }

    public String getParableByCategory(AnecdoteCategory category) {
        List<String> relevant = new ArrayList<>();
        for (Parable parable : parables) {
            if (parable.getCategory() == category) {
                relevant.add(parable.getParable());
            }
        }
        if (relevant.isEmpty()) return getRandomParable();
        return relevant.get(random.nextInt(relevant.size()));
    }

    public String getParableMoral(String parableText) {
        for (Parable parable : parables) {
            if (parable.getParable().equals(parableText)) {
                return parable.getMoral();
            }
        }
        return "The moral of this story is often found in its telling.";
    }

    public List<String> getRelevantParables(String context, int count) {
        List<String> results = new ArrayList<>();
        if (context == null || context.trim().isEmpty()) {
            for (int i = 0; i < count && i < parables.size(); i++) {
                results.add(getRandomParable());
            }
            return results;
        }

        String lowerContext = context.toLowerCase();
        String[] keywords = lowerContext.split("\\s+");

        // Score all parables
        List<Parable> scored = new ArrayList<>();
        for (Parable parable : parables) {
            int score = calculateParableRelevanceScore(parable, keywords);
            scored.add(parable);
        }

        // Sort by score descending
        scored.sort((a, b) -> {
            int scoreA = calculateParableRelevanceScore(a, keywords);
            int scoreB = calculateParableRelevanceScore(b, keywords);
            return Integer.compare(scoreB, scoreA);
        });

        // Return top results (with some randomness for variety)
        Random random = new Random();
        int returnCount = Math.min(count, scored.size());
        Set<Integer> usedIndices = new HashSet<>();

        while (results.size() < returnCount && usedIndices.size() < scored.size()) {
            int index = random.nextInt(scored.size());
            if (!usedIndices.contains(index)) {
                usedIndices.add(index);
                results.add(scored.get(index).getParable());
            }
        }

        return results;
    }

    private int calculateParableRelevanceScore(Parable parable, String[] keywords) {
        int score = 0;
        String parableLower = parable.getParable().toLowerCase();
        String moral = parable.getMoral().toLowerCase();

        // Check each keyword
        for (String keyword : keywords) {
            if (keyword.length() < 3) continue; // Skip short words

            // Score based on moral match (highest weight)
            if (moral.contains(keyword)) {
                score += 15;
            }

            // Score based on parable content match
            if (parableLower.contains(keyword)) {
                score += 10;
            }

            // Score based on symbolism
            if (parable.getSymbolism().toLowerCase().contains(keyword)) {
                score += 8;
            }
        }

        return score;
    }

    public int getTotalParables() {
        return parables.size();
    }

    public String getParableWithMoral(String context) {
        List<String> parables = getRelevantParables(context, 1);
        if (parables.isEmpty()) {
            return "No relevant parable found.";
        }
        
        String parableText = parables.get(0);
        String moral = getParableMoral(parableText);
        
        return "parable: " + parableText + "\n\nMoral: " + moral;
    }

    // ==================== CORE METHODS ====================

    public String getRandomAnecdote() {
        return anecdotes.get(random.nextInt(anecdotes.size())).getStory();
    }

    public String getAnecdoteByCategory(AnecdoteCategory category) {
        List<String> relevant = new ArrayList<>();
        for (Anecdote anecdote : anecdotes) {
            if (anecdote.getCategory() == category) {
                relevant.add(anecdote.getStory());
            }
        }
        if (relevant.isEmpty()) return getRandomAnecdote();
        return relevant.get(random.nextInt(relevant.size()));
    }

    public String getAnecdoteForSituation(String situation) {
        String lower = situation.toLowerCase();
        if (lower.contains("fail") || lower.contains("wrong") || lower.contains("mistake")) {
            return getAnecdoteByCategory(AnecdoteCategory.FAILURE_RESILIENCE);
        }
        if (lower.contains("happy") || lower.contains("joy") || lower.contains("good")) {
            return getAnecdoteByCategory(AnecdoteCategory.HAPPINESS);
        }
        if (lower.contains("busy") || lower.contains("time") || lower.contains("when")) {
            return getAnecdoteByCategory(AnecdoteCategory.TIME_PRIORITIES);
        }
        if (lower.contains("relate") || lower.contains("friend") || lower.contains("love")) {
            return getAnecdoteByCategory(AnecdoteCategory.RELATIONSHIPS);
        }
        return getRandomAnecdote();
    }

    public int getTotalAnecdotes() {
        return anecdotes.size();
    }

    // ==================== RELEVANT STORY SELECTION ====================

    /**
     * Selects the most relevant anecdote based on the given context
     * Analyzes keywords and returns anecdotes with matching themes
     */
    public String getRelevantStory(String context) {
        if (context == null || context.trim().isEmpty()) {
            return getRandomAnecdote();
        }

        String lowerContext = context.toLowerCase();
        String[] keywords = lowerContext.split("\\s+");

        // Track best matching anecdote
        Anecdote bestMatch = null;
        int bestScore = 0;

        for (Anecdote anecdote : anecdotes) {
            int matchScore = calculateRelevanceScore(anecdote, keywords);
            if (matchScore > bestScore) {
                bestScore = matchScore;
                bestMatch = anecdote;
            }
        }

        // If we have a good match, return it
        if (bestMatch != null && bestScore > 0) {
            return bestMatch.getStory();
        }

        // Fallback to category-based or random
        return getAnecdoteForSituation(context);
    }

    /**
     * Calculates how relevant an anecdote is to the given keywords
     */
    private int calculateRelevanceScore(Anecdote anecdote, String[] keywords) {
        int score = 0;
        String storyLower = anecdote.getStory().toLowerCase();
        String theme = anecdote.getTheme().toLowerCase();

        // Check each keyword
        for (String keyword : keywords) {
            if (keyword.length() < 3) continue; // Skip short words

            // Score based on theme match (highest weight)
            if (theme.contains(keyword) || keyword.contains(theme)) {
                score += 10;
            }

            // Score based on story content match
            if (storyLower.contains(keyword)) {
                score += 5;
            }
        }

        // Add relevance score from the anecdote itself
        score += anecdote.getRelevanceScore() / 2;

        return score;
    }

    /**
     * Gets multiple relevant anecdotes for a given context
     */
    public List<String> getRelevantStories(String context, int count) {
        List<String> results = new ArrayList<>();
        if (context == null || context.trim().isEmpty()) {
            for (int i = 0; i < count && i < anecdotes.size(); i++) {
                results.add(getRandomAnecdote());
            }
            return results;
        }

        String lowerContext = context.toLowerCase();
        String[] keywords = lowerContext.split("\\s+");

        // Score all anecdotes
        List<Anecdote> scored = new ArrayList<>();
        for (Anecdote anecdote : anecdotes) {
            int score = calculateRelevanceScore(anecdote, keywords);
            scored.add(anecdote);
        }

        // Sort by score descending
        scored.sort((a, b) -> {
            int scoreA = calculateRelevanceScore(a, keywords);
            int scoreB = calculateRelevanceScore(b, keywords);
            return Integer.compare(scoreB, scoreA);
        });

        // Return top results (with some randomness for variety)
        Random random = new Random();
        int returnCount = Math.min(count, scored.size());
        Set<Integer> usedIndices = new HashSet<>();

        while (results.size() < returnCount && usedIndices.size() < scored.size()) {
            int index = random.nextInt(scored.size());
            if (!usedIndices.contains(index)) {
                usedIndices.add(index);
                results.add(scored.get(index).getStory());
            }
        }

        return results;
    }

    // ==================== CUSTOM STORY CREATION ====================

    /**
     * Creates a new custom story and saves it to storage
     * @param story The story content
     * @param category The category of the story
     * @param theme The theme of the story
     * @param author The author's name
     * @param lesson The lesson/moral of the story
     * @return The created custom story object
     */
    public CustomStory createCustomStory(String story, AnecdoteCategory category, String theme, String author, String lesson) {
        CustomStory customStory = new CustomStory(story, category, theme, author, lesson);
        customStories.add(customStory);
        saveCustomStories();
        return customStory;
    }

    /**
     * Creates a new custom story with default values
     * @param story The story content
     * @param category The category of the story
     * @return The created custom story object
     */
    public CustomStory createCustomStory(String story, AnecdoteCategory category) {
        return createCustomStory(story, category, "custom", "anonymous", "");
    }

    /**
     * Gets a random custom story
     * @return A random custom story, or null if none exist
     */
    public String getRandomCustomStory() {
        if (customStories.isEmpty()) {
            return null;
        }
        return customStories.get(random.nextInt(customStories.size())).getStory();
    }

    /**
     * Gets a custom story by its ID
     * @param id The unique identifier of the story
     * @return The custom story, or null if not found
     */
    public CustomStory getCustomStoryById(String id) {
        for (CustomStory story : customStories) {
            if (story.getId().equals(id)) {
                return story;
            }
        }
        return null;
    }

    /**
     * Gets custom stories by category
     * @param category The category to filter by
     * @return List of custom stories in the given category
     */
    public List<String> getCustomStoriesByCategory(AnecdoteCategory category) {
        List<String> results = new ArrayList<>();
        for (CustomStory story : customStories) {
            if (story.getCategory() == category) {
                results.add(story.getStory());
            }
        }
        return results;
    }

    /**
     * Gets all custom story IDs
     * @return List of all custom story IDs
     */
    public List<String> getAllCustomStoryIds() {
        List<String> ids = new ArrayList<>();
        for (CustomStory story : customStories) {
            ids.add(story.getId());
        }
        return ids;
    }

    /**
     * Gets the total count of custom stories
     * @return The number of custom stories
     */
    public int getTotalCustomStories() {
        return customStories.size();
    }

    /**
     * Deletes a custom story by ID
     * @param id The unique identifier of the story to delete
     * @return true if the story was found and deleted, false otherwise
     */
    public boolean deleteCustomStory(String id) {
        boolean removed = customStories.removeIf(story -> story.getId().equals(id));
        if (removed) {
            saveCustomStories();
        }
        return removed;
    }

    /**
     * Updates an existing custom story
     * @param id The unique identifier of the story to update
     * @param newStory The new story content
     * @param newCategory The new category
     * @param newTheme The new theme
     * @param newLesson The new lesson/moral
     * @return true if the story was found and updated, false otherwise
     */
    public boolean updateCustomStory(String id, String newStory, AnecdoteCategory newCategory, String newTheme, String newLesson) {
        for (CustomStory story : customStories) {
            if (story.getId().equals(id)) {
                // Create a new updated custom story (immutable for simplicity)
                int index = customStories.indexOf(story);
                CustomStory updated = new CustomStory(newStory, newCategory, newTheme, story.getAuthor(), newLesson);
                updated = new CustomStory(newStory, newCategory, newTheme, story.getAuthor(), newLesson);
                customStories.set(index, updated);
                saveCustomStories();
                return true;
            }
        }
        return false;
    }

    /**
     * Gets custom stories relevant to a context
     * @param context The context/keywords to match
     * @param count Maximum number of stories to return
     * @return List of relevant custom stories
     */
    public List<String> getRelevantCustomStories(String context, int count) {
        List<String> results = new ArrayList<>();
        if (context == null || context.trim().isEmpty()) {
            for (int i = 0; i < count && i < customStories.size(); i++) {
                results.add(getRandomCustomStory());
            }
            return results;
        }

        String lowerContext = context.toLowerCase();
        String[] keywords = lowerContext.split("\\s+");

        // Score all custom stories
        List<CustomStory> scored = new ArrayList<>();
        for (CustomStory story : customStories) {
            int score = calculateCustomStoryRelevanceScore(story, keywords);
            if (score > 0) {
                scored.add(story);
            }
        }

        // Sort by score descending
        scored.sort((a, b) -> {
            int scoreA = calculateCustomStoryRelevanceScore(a, keywords);
            int scoreB = calculateCustomStoryRelevanceScore(b, keywords);
            return Integer.compare(scoreB, scoreA);
        });

        // Return top results
        for (int i = 0; i < Math.min(count, scored.size()); i++) {
            results.add(scored.get(i).getStory());
        }

        return results;
    }

    /**
     * Calculates relevance score for a custom story
     */
    private int calculateCustomStoryRelevanceScore(CustomStory story, String[] keywords) {
        int score = 0;
        String storyLower = story.getStory().toLowerCase();
        String theme = story.getTheme().toLowerCase();

        for (String keyword : keywords) {
            if (keyword.length() < 3) continue;

            if (theme.contains(keyword)) {
                score += 10;
            }
            if (storyLower.contains(keyword)) {
                score += 5;
            }
        }

        return score;
    }

    /**
     * Saves custom stories to file
     */
    private void saveCustomStories() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CUSTOM_STORIES_FILE))) {
            oos.writeObject(customStories);
        } catch (IOException e) {
            System.err.println("Error saving custom stories: " + e.getMessage());
        }
    }

    /**
     * Loads custom stories from file
     */
    private void loadCustomStories() {
        File file = new File(CUSTOM_STORIES_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                customStories = (List<CustomStory>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading custom stories: " + e.getMessage());
                customStories = new ArrayList<>();
            }
        }
    }

    // ==================== MORAL EXTRACTION ====================

    /**
     * Represents a suggested moral with confidence score
     */
    public static class MoralSuggestion {
        private String moral;
        private double confidence;
        private String reasoning;

        public MoralSuggestion(String moral, double confidence, String reasoning) {
            this.moral = moral;
            this.confidence = confidence;
            this.reasoning = reasoning;
        }

        public String getMoral() { return moral; }
        public double getConfidence() { return confidence; }
        public String getReasoning() { return reasoning; }
    }

    // Keyword patterns for moral extraction
    private static final String[][] MORAL_PATTERNS = {
        {"fear", "courage", "brave", "afraid", "Courage is acting despite fear, not the absence of fear."},
        {"fail", "failure", "failed", "mistake", "Failure is not defeat, but a teacher on the path to success."},
        {"learn", "learned", "learning", "lesson", "Every experience teaches us something if we're willing to listen."},
        {"change", "changed", "grow", "Change is the only constant, and growth comes from embracing it."},
        {"love", "care", "kind", "kindness", "Love and kindness are the most powerful forces in the universe."},
        {"patience", "wait", "slow", "pace", "Patience is not about waiting, but about maintaining hope during the wait."},
        {"trust", "believe", "faith", "hope", "Trust in yourself and the journey, even when the path is unclear."},
        {"hard", "tough", "struggle", "difficult", "Strength is forged through struggle, not ease."},
        {"happy", "joy", "smile", "laugh", "Happiness is found in the present moment, not in some distant future."},
        {"forgive", "forgiveness", "let go", "release", "Forgiveness frees us, not the one who hurt us."},
        {"give", "generous", "share", "help", "Giving is the greatest source of receiving."},
        {"time", "moment", "now", "present", "Time is precious; live fully in each moment."},
        {"self", "yourself", "inner", "within", "The greatest journey is the one inward to yourself."},
        {"friend", "together", "support", "community", "No one succeeds entirely alone; we need each other."},
        {"start", "begin", "first", "try", "Every great achievement begins with the decision to try."}
    };

    /**
     * Extracts a suggested moral from a story
     * @param story The story text to analyze
     * @return A MoralSuggestion with the extracted moral, confidence, and reasoning
     */
    public MoralSuggestion extractMoral(String story) {
        if (story == null || story.trim().isEmpty()) {
            return new MoralSuggestion("Every story has a lesson worth learning.", 0.0, "Empty story provided.");
        }

        String lowerStory = story.toLowerCase();
        Map<String, Integer> matchedPatterns = new HashMap<>();
        List<String> matchedMessages = new ArrayList<>();

        // Check each moral pattern
        for (int i = 0; i < MORAL_PATTERNS.length; i++) {
            String[] keywords = (String[]) MORAL_PATTERNS[i];
            String moral = (String) MORAL_PATTERNS[i][MORAL_PATTERNS[i].length - 1];
            
            int matchCount = 0;
            for (int j = 0; j < keywords.length - 1; j++) {
                if (lowerStory.contains(keywords[j])) {
                    matchCount++;
                }
            }

            if (matchCount > 0) {
                matchedPatterns.put(moral, matchCount);
                matchedMessages.add("Found " + matchCount + " keyword(s) for: " + keywords[0]);
            }
        }

        // If no patterns matched, generate a contextual moral
        if (matchedPatterns.isEmpty()) {
            return generateContextualMoral(story);
        }

        // Find the best matching moral
        String bestMoral = "";
        int bestScore = 0;
        for (Map.Entry<String, Integer> entry : matchedPatterns.entrySet()) {
            if (entry.getValue() > bestScore) {
                bestScore = entry.getValue();
                bestMoral = entry.getKey();
            }
        }

        // Calculate confidence based on match strength
        double wordCount = story.split("\\s+").length;
        double confidence = Math.min(0.95, 0.3 + (bestScore * 0.15) + (wordCount / 1000.0));

        String reasoning = "Matched " + bestScore + " keyword(s): " + String.join(", ", matchedMessages);

        return new MoralSuggestion(bestMoral, confidence, reasoning);
    }

    /**
     * Generates a contextual moral when no patterns match
     */
    private MoralSuggestion generateContextualMoral(String story) {
        String lowerStory = story.toLowerCase();
        int wordCount = story.split("\\s+").length;

        // Analyze story structure and length for context
        if (wordCount < 20) {
            return new MoralSuggestion(
                "Simple moments often hold the deepest truths.",
                0.25,
                "Short story format suggests a focused, meaningful experience."
            );
        }

        // Check for transformation keywords
        if (lowerStory.contains("then") || lowerStory.contains("after") || lowerStory.contains("finally")) {
            return new MoralSuggestion(
                "Transformation happens one step at a time.",
                0.35,
                "Story structure suggests a journey or transformation arc."
            );
        }

        // Default moral with low confidence
        return new MoralSuggestion(
            "Every story teaches us something about the human experience.",
            0.2,
            "No specific patterns matched; generic wisdom provided."
        );
    }

    /**
     * Extracts multiple moral suggestions ranked by relevance
     * @param story The story text to analyze
     * @param maxSuggestions Maximum number of suggestions to return
     * @return List of MoralSuggestion objects ranked by confidence
     */
    public List<MoralSuggestion> extractMultipleMoralSuggestions(String story, int maxSuggestions) {
        List<MoralSuggestion> suggestions = new ArrayList<>();
        String lowerStory = story.toLowerCase();

        // Collect all matching morals
        Map<String, Integer> moralScores = new HashMap<>();
        Map<String, String> moralReasons = new HashMap<>();

        for (int i = 0; i < MORAL_PATTERNS.length; i++) {
            String[] keywords = (String[]) MORAL_PATTERNS[i];
            String moral = (String) MORAL_PATTERNS[i][MORAL_PATTERNS[i].length - 1];
            
            int matchCount = 0;
            List<String> foundKeywords = new ArrayList<>();
            for (int j = 0; j < keywords.length - 1; j++) {
                if (lowerStory.contains(keywords[j])) {
                    matchCount++;
                    foundKeywords.add(keywords[j]);
                }
            }

            if (matchCount > 0) {
                moralScores.put(moral, matchCount);
                moralReasons.put(moral, "Keywords found: " + String.join(", ", foundKeywords));
            }
        }

        // Sort morals by score
        List<String> sortedMorals = new ArrayList<>(moralScores.keySet());
        sortedMorals.sort((a, b) -> Integer.compare(moralScores.get(b), moralScores.get(a)));

        // Build suggestions list
        for (int i = 0; i < Math.min(maxSuggestions, sortedMorals.size()); i++) {
            String moral = sortedMorals.get(i);
            int score = moralScores.get(moral);
            double confidence = Math.min(0.9, 0.25 + (score * 0.12));
            suggestions.add(new MoralSuggestion(moral, confidence, moralReasons.get(moral)));
        }

        // If no matches, add contextual suggestion
        if (suggestions.isEmpty()) {
            suggestions.add(generateContextualMoral(story));
        }

        return suggestions;
    }
}
