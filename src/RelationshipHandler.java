import java.util.*;
import java.util.regex.*;

/**
 * RelationshipHandler - Handles relationship discussions and support
 * Part of Phase 3: Response Categories
 */
public class RelationshipHandler {
    
    private Map<RelationshipCategory, List<RelationshipResponse>> relationshipResponses;
    private Map<String, RelationshipCategory> keywordMapping;
    private Random random;
    
    public RelationshipHandler() {
        this.relationshipResponses = new HashMap<>();
        this.keywordMapping = new HashMap<>();
        this.random = new Random();
        initializeRelationshipResponses();
        initializeKeywordMapping();
    }
    
    /**
     * Relationship categories
     */
    public enum RelationshipCategory {
        DATING("Dating & Romance"),
        BREAKUP("Breakups & Heartbreak"),
        MARRIAGE("Marriage & Commitment"),
        FAMILY("Family Relationships"),
        FRIENDSHIP("Friendship"),
        CONFLICT("Relationship Conflict"),
        GENERAL("General Relationship Support");
        
        private final String displayName;
        
        RelationshipCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Relationship response class
     */
    public static class RelationshipResponse {
        public String response;
        public String followUp;
        public int category; // 1 = positive, 2 = challenging, 3 = neutral
        
        public RelationshipResponse(String response, String followUp, int category) {
            this.response = response;
            this.followUp = followUp;
            this.category = category;
        }
    }
    
    private void initializeRelationshipResponses() {
        // Dating responses
        List<RelationshipResponse> datingResponses = Arrays.asList(
            new RelationshipResponse(
                "Dating can be exciting! What's on your mind about it?",
                "Are you seeing someone special or just thinking about dating in general?",
                3
            ),
            new RelationshipResponse(
                "Romantic relationships are a big part of life! What's your experience been like?",
                "Do you have any dating stories or questions you'd like to share?",
                3
            ),
            new RelationshipResponse(
                "I love hearing about people's dating experiences! What's going on?",
                "Is there something specific you'd like to talk about?",
                3
            ),
            new RelationshipResponse(
                "Dating is full of ups and downs! How has your dating life been lately?",
                "Any exciting news or challenges you're facing?",
                3
            )
        );
        relationshipResponses.put(RelationshipCategory.DATING, datingResponses);
        
        // Breakup responses
        List<RelationshipResponse> breakupResponses = Arrays.asList(
            new RelationshipResponse(
                "I'm really sorry you're going through a breakup. Those can be so painful.",
                "Would you like to talk about what happened? Sometimes it helps to share.",
                2
            ),
            new RelationshipResponse(
                "Breakups are never easy. It's okay to feel hurt and sad right now.",
                "Take all the time you need to heal. What's on your mind?",
                2
            ),
            new RelationshipResponse(
                "Heartbreak is one of the hardest experiences. Remember, this pain won't last forever.",
                "Would it help to talk about your feelings?",
                2
            ),
            new RelationshipResponse(
                "Going through a breakup is tough. Be gentle with yourself during this time.",
                "Is there anything specific you want to get off your chest?",
                2
            ),
            new RelationshipResponse(
                "I know it doesn't feel like it right now, but you'll get through this.",
                "Sometimes the hardest goodbyes lead to the best new beginnings.",
                2
            ),
            new RelationshipResponse(
                "When someone leaves, it can feel like your world is falling apart. But you will heal.",
                "Remember, their choice to leave says more about them than about you.",
                2
            ),
            new RelationshipResponse(
                "I'm so sorry they're gone. The pain of losing someone you loved is incredibly hard.",
                "It's okay to grieve and cry. Your feelings are completely valid.",
                2
            ),
            new RelationshipResponse(
                "Being left behind hurts so much. You didn't deserve to be treated that way.",
                "You deserve someone who chooses you every single day.",
                2
            ),
            new RelationshipResponse(
                "Dealing with a broken heart is one of life's hardest lessons. I'm here for you.",
                "Would you like to share what happened? Sometimes talking helps ease the pain.",
                2
            ),
            new RelationshipResponse(
                "I can't imagine how much pain you're in right now. Time does heal all wounds.",
                "Lean on your friends and family during this difficult time.",
                2
            ),
            new RelationshipResponse(
                "Thinking about an ex can bring up so many emotions. It's completely normal.",
                "What memories or thoughts are on your mind?",
                2
            ),
            new RelationshipResponse(
                "The silence after someone leaves can be so loud. You don't have to face this alone.",
                "I'm here to listen whenever you need to talk.",
                2
            )
        );
        relationshipResponses.put(RelationshipCategory.BREAKUP, breakupResponses);
        
        // Marriage responses
        List<RelationshipResponse> marriageResponses = Arrays.asList(
            new RelationshipResponse(
                "Marriage is such an important journey! What aspect of married life is on your mind?",
                "Are you newlywed, celebrating an anniversary, or navigating married life?",
                3
            ),
            new RelationshipResponse(
                "Relationships take work, especially in marriage. How are things going for you?",
                "Is there something specific you'd like to discuss about your marriage?",
                3
            ),
            new RelationshipResponse(
                "Marriage can bring so much joy and also challenges. What's been your experience?",
                "Would you like to share what's on your mind?",
                3
            ),
            new RelationshipResponse(
                "Congratulations on your marriage! That's wonderful! What are you looking forward to?",
                "Any wedding stories or marriage advice you'd like to share?",
                1
            )
        );
        relationshipResponses.put(RelationshipCategory.MARRIAGE, marriageResponses);
        
        // Family responses
        List<RelationshipResponse> familyResponses = Arrays.asList(
            new RelationshipResponse(
                "Family relationships can be so complex. What's going on with your family?",
                "Would you like to talk about a specific family situation?",
                3
            ),
            new RelationshipResponse(
                "Family is such an important part of our lives. How is your relationship with your family?",
                "Is there something on your mind about family matters?",
                3
            ),
            new RelationshipResponse(
                "I understand family situations can be challenging. I'm here to listen.",
                "Would it help to talk about what's bothering you?",
                2
            ),
            new RelationshipResponse(
                "Family bonds are special! What do you enjoy most about your family?",
                "Are there any family moments you're looking forward to?",
                1
            )
        );
        relationshipResponses.put(RelationshipCategory.FAMILY, familyResponses);
        
        // Friendship responses
        List<RelationshipResponse> friendshipResponses = Arrays.asList(
            new RelationshipResponse(
                "Friendships are so important! How are your friends doing?",
                "Is there something about your friendships you'd like to talk about?",
                3
            ),
            new RelationshipResponse(
                "Good friends are like family! What's going on with your friend circle?",
                "Are you looking to make new friends or strengthen existing bonds?",
                3
            ),
            new RelationshipResponse(
                "I love hearing about friendships! Any fun stories or adventures to share?",
                "What's been happening with your friends lately?",
                1
            ),
            new RelationshipResponse(
                "Sometimes friendships have their challenges. What's on your mind?",
                "Is there something specific about a friendship you'd like to discuss?",
                2
            )
        );
        relationshipResponses.put(RelationshipCategory.FRIENDSHIP, friendshipResponses);
        
        // Conflict responses
        List<RelationshipResponse> conflictResponses = Arrays.asList(
            new RelationshipResponse(
                "Conflict in relationships is tough. Would you like to talk about what's happening?",
                "Sometimes sharing what's bothering us can help clarify our feelings.",
                2
            ),
            new RelationshipResponse(
                "Disagreements happen in every relationship. What's causing the conflict?",
                "Would you like to talk through the situation?",
                2
            ),
            new RelationshipResponse(
                "I hear you. Relationship conflicts can be really stressful.",
                "Remember, it's possible to work through most disagreements with communication.",
                2
            ),
            new RelationshipResponse(
                "Conflict can be an opportunity for growth. What's the situation?",
                "Sometimes taking a step back helps gain perspective.",
                2
            )
        );
        relationshipResponses.put(RelationshipCategory.CONFLICT, conflictResponses);
        
        // General relationship responses
        List<RelationshipResponse> generalResponses = Arrays.asList(
            new RelationshipResponse(
                "Relationships are such an important part of life! What would you like to discuss?",
                "I'm here to listen without any judgment.",
                3
            ),
            new RelationshipResponse(
                "Human connections are what make life meaningful. What's on your mind?",
                "Is there something specific about relationships you're thinking about?",
                3
            ),
            new RelationshipResponse(
                "I'd love to hear about your relationship experiences! What's going on?",
                "Feel free to share anything you'd like to talk about.",
                3
            ),
            new RelationshipResponse(
                "Relationships can be complicated sometimes. What would you like to explore?",
                "I'm here to support you in whatever you're going through.",
                3
            )
        );
        relationshipResponses.put(RelationshipCategory.GENERAL, generalResponses);
    }
    
    private void initializeKeywordMapping() {
        // Dating keywords
        keywordMapping.put("dating", RelationshipCategory.DATING);
        keywordMapping.put("date", RelationshipCategory.DATING);
        keywordMapping.put("dating app", RelationshipCategory.DATING);
        keywordMapping.put("tinder", RelationshipCategory.DATING);
        keywordMapping.put("bumble", RelationshipCategory.DATING);
        keywordMapping.put("hinge", RelationshipCategory.DATING);
        keywordMapping.put("crush", RelationshipCategory.DATING);
        keywordMapping.put("dating someone", RelationshipCategory.DATING);
        keywordMapping.put("in a relationship", RelationshipCategory.DATING);
        keywordMapping.put("girlfriend", RelationshipCategory.DATING);
        keywordMapping.put("boyfriend", RelationshipCategory.DATING);
        keywordMapping.put("partner", RelationshipCategory.DATING);
        keywordMapping.put("romance", RelationshipCategory.DATING);
        keywordMapping.put("romantic", RelationshipCategory.DATING);
        
        // Breakup keywords
        keywordMapping.put("breakup", RelationshipCategory.BREAKUP);
        keywordMapping.put("break up", RelationshipCategory.BREAKUP);
        keywordMapping.put("broke up", RelationshipCategory.BREAKUP);
        keywordMapping.put("broken up", RelationshipCategory.BREAKUP);
        keywordMapping.put("dumped", RelationshipCategory.BREAKUP);
        keywordMapping.put("heartbreak", RelationshipCategory.BREAKUP);
        keywordMapping.put("heartbroken", RelationshipCategory.BREAKUP);
        keywordMapping.put("miss my ex", RelationshipCategory.BREAKUP);
        keywordMapping.put("ex girlfriend", RelationshipCategory.BREAKUP);
        keywordMapping.put("ex boyfriend", RelationshipCategory.BREAKUP);
        keywordMapping.put("got dumped", RelationshipCategory.BREAKUP);
        keywordMapping.put("separated", RelationshipCategory.BREAKUP);
        keywordMapping.put("my ex", RelationshipCategory.BREAKUP);
        keywordMapping.put("he left me", RelationshipCategory.BREAKUP);
        keywordMapping.put("she left me", RelationshipCategory.BREAKUP);
        keywordMapping.put("left me", RelationshipCategory.BREAKUP);
        keywordMapping.put("cheated on me", RelationshipCategory.BREAKUP);
        keywordMapping.put(" infidelity", RelationshipCategory.BREAKUP);
        keywordMapping.put("unfollowed", RelationshipCategory.BREAKUP);
        keywordMapping.put("blocked me", RelationshipCategory.BREAKUP);
        
        // Marriage keywords
        keywordMapping.put("married", RelationshipCategory.MARRIAGE);
        keywordMapping.put("marriage", RelationshipCategory.MARRIAGE);
        keywordMapping.put("wedding", RelationshipCategory.MARRIAGE);
        keywordMapping.put("husband", RelationshipCategory.MARRIAGE);
        keywordMapping.put("wife", RelationshipCategory.MARRIAGE);
        keywordMapping.put("spouse", RelationshipCategory.MARRIAGE);
        keywordMapping.put("anniversary", RelationshipCategory.MARRIAGE);
        keywordMapping.put("propose", RelationshipCategory.MARRIAGE);
        keywordMapping.put("proposal", RelationshipCategory.MARRIAGE);
        keywordMapping.put("engaged", RelationshipCategory.MARRIAGE);
        
        // Family keywords
        keywordMapping.put("family", RelationshipCategory.FAMILY);
        keywordMapping.put("parents", RelationshipCategory.FAMILY);
        keywordMapping.put("mom", RelationshipCategory.FAMILY);
        keywordMapping.put("dad", RelationshipCategory.FAMILY);
        keywordMapping.put("siblings", RelationshipCategory.FAMILY);
        keywordMapping.put("brother", RelationshipCategory.FAMILY);
        keywordMapping.put("sister", RelationshipCategory.FAMILY);
        keywordMapping.put("son", RelationshipCategory.FAMILY);
        keywordMapping.put("daughter", RelationshipCategory.FAMILY);
        keywordMapping.put("kids", RelationshipCategory.FAMILY);
        keywordMapping.put("children", RelationshipCategory.FAMILY);
        
        // Friendship keywords
        keywordMapping.put("friend", RelationshipCategory.FRIENDSHIP);
        keywordMapping.put("friends", RelationshipCategory.FRIENDSHIP);
        keywordMapping.put("friendship", RelationshipCategory.FRIENDSHIP);
        keywordMapping.put("best friend", RelationshipCategory.FRIENDSHIP);
        keywordMapping.put("bestie", RelationshipCategory.FRIENDSHIP);
        keywordMapping.put("making friends", RelationshipCategory.FRIENDSHIP);
        
        // Conflict keywords
        keywordMapping.put("argument", RelationshipCategory.CONFLICT);
        keywordMapping.put("fight", RelationshipCategory.CONFLICT);
        keywordMapping.put("arguing", RelationshipCategory.CONFLICT);
        keywordMapping.put("disagree", RelationshipCategory.CONFLICT);
        keywordMapping.put("disagreement", RelationshipCategory.CONFLICT);
        keywordMapping.put("conflict", RelationshipCategory.CONFLICT);
        keywordMapping.put("not getting along", RelationshipCategory.CONFLICT);
    }
    
    /**
     * Detects relationship category from input
     */
    public RelationshipCategory detectRelationshipCategory(String input) {
        String lowerInput = input.toLowerCase();
        
        for (Map.Entry<String, RelationshipCategory> entry : keywordMapping.entrySet()) {
            if (lowerInput.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        return RelationshipCategory.GENERAL;
    }
    
    /**
     * Checks if input is relationship-related
     */
    public boolean isRelationshipRelated(String input) {
        String lowerInput = input.toLowerCase();
        
        String[] relationshipTriggers = {
            "relationship", "dating", "boyfriend", "girlfriend", "wife", "husband",
            "married", "marriage", "breakup", "family", "friends", "friend"
        };
        
        for (String trigger : relationshipTriggers) {
            if (lowerInput.contains(trigger)) {
                return true;
            }
        }
        
        return detectRelationshipCategory(input) != RelationshipCategory.GENERAL;
    }
    
    /**
     * Gets relationship response for detected category
     */
    public RelationshipResponse getRelationshipResponse(RelationshipCategory category) {
        List<RelationshipResponse> responses = relationshipResponses.get(category);
        if (responses != null && !responses.isEmpty()) {
            return responses.get(random.nextInt(responses.size()));
        }
        
        List<RelationshipResponse> generalResponses = relationshipResponses.get(RelationshipCategory.GENERAL);
        return generalResponses.get(random.nextInt(generalResponses.size()));
    }
    
    /**
     * Gets all available relationship categories
     */
    public List<RelationshipCategory> getAvailableCategories() {
        return Arrays.asList(RelationshipCategory.values());
    }
    
    /**
     * Gets relationship advice based on category
     */
    public String getRelationshipAdvice(RelationshipCategory category) {
        switch (category) {
            case DATING:
                return "Be yourself and don't rush. Good relationships are built on trust and communication.";
            case BREAKUP:
                return "Take time to heal and focus on yourself. The right person will come along when you're ready.";
            case MARRIAGE:
                return "Communication and compromise are key. Always make time for each other.";
            case FAMILY:
                return "Family relationships take work from all sides. Patience and understanding go a long way.";
            case FRIENDSHIP:
                return "Good friends are there for you through thick and thin. Nurture those bonds.";
            case CONFLICT:
                return "Try to see things from the other person's perspective. Open communication helps resolve issues.";
            default:
                return "Every relationship is unique. What matters most is mutual respect and understanding.";
        }
    }
    
    /**
     * Gets conversation starter for relationships
     */
    public String getRelationshipConversationStarter() {
        String[] starters = {
            "Tell me about your relationships!",
            "What's the most important relationship in your life?",
            "How do you maintain your relationships?",
            "What qualities do you value most in relationships?",
            "Any relationship stories you'd like to share?",
            "What's your approach to building connections with others?",
            "How do you balance different relationships in your life?"
        };
        return starters[random.nextInt(starters.length)];
    }
}

