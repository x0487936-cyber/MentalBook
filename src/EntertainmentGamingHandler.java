import java.util.*;
import java.util.regex.*;

/**
 * EntertainmentGamingHandler - Handles entertainment and gaming discussions
 * Part of Phase 3: Response Categories
 */
public class EntertainmentGamingHandler {
    
    private Map<GamingCategory, List<GamingResponse>> gamingResponses;
    private Map<EntertainmentCategory, List<EntertainmentResponse>> entertainmentResponses;
    private Map<String, GamingCategory> gamingKeywords;
    private Map<String, EntertainmentCategory> entertainmentKeywords;
    private Random random;
    
    public EntertainmentGamingHandler() {
        this.gamingResponses = new HashMap<>();
        this.entertainmentResponses = new HashMap<>();
        this.gamingKeywords = new HashMap<>();
        this.entertainmentKeywords = new HashMap<>();
        this.random = new Random();
        initializeGamingResponses();
        initializeEntertainmentResponses();
        initializeKeywords();
    }
    
    /**
     * Gaming categories
     */
    public enum GamingCategory {
        BATTLE_ROYALE("Battle Royale"),
        FPS("First-Person Shooter"),
        RPG("Role-Playing Games"),
        STRATEGY("Strategy Games"),
        SPORTS("Sports Games"),
        PUZZLE("Puzzle Games"),
        HORROR("Horror Games"),
        SANDBOX("Sandbox Games"),
        MOBILE("Mobile Games"),
        GENERAL("General Gaming");
        
        private final String displayName;
        
        GamingCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Entertainment categories
     */
    public enum EntertainmentCategory {
        MOVIES("Movies"),
        TV_SHOWS("TV Shows"),
        MUSIC("Music"),
        BOOKS("Books"),
        SPORTS("Sports"),
        HOBBIES("Hobbies"),
        MEMES("Memes & Fun"),
        GENERAL("General Entertainment");
        
        private final String displayName;
        
        EntertainmentCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Gaming response class
     */
    private static class GamingResponse {
        String response;
        String followUp;
        List<String> examples;
        
        public GamingResponse(String response, String followUp, List<String> examples) {
            this.response = response;
            this.followUp = followUp;
            this.examples = examples;
        }
    }
    
    /**
     * Entertainment response class
     */
    private static class EntertainmentResponse {
        String response;
        String followUp;
        List<String> suggestions;
        
        public EntertainmentResponse(String response, String followUp, List<String> suggestions) {
            this.response = response;
            this.followUp = followUp;
            this.suggestions = suggestions;
        }
    }
    
    private void initializeGamingResponses() {
        // Fortnite responses
        List<GamingResponse> fortniteResponses = Arrays.asList(
            new GamingResponse(
                "Fortnite is super popular! What do you like most about it?",
                "Are you a solo player or do you squad up?",
                Arrays.asList("Battle Royale", "Zero Build Mode", "Creative Mode")
            ),
            new GamingResponse(
                "Fortnite has so much content! What kind of player are you?",
                "Competitive or more casual?",
                Arrays.asList("Victory Royales", "Skins", "Crossovers", "Events")
            )
        );
        gamingResponses.put(GamingCategory.BATTLE_ROYALE, fortniteResponses);
        
        // FPS responses
        List<GamingResponse> fpsResponses = Arrays.asList(
            new GamingResponse(
                "FPS games are intense! What's your favorite?",
                "Do you prefer fast-paced or tactical shooters?",
                Arrays.asList("CS2", "Valorant", "Call of Duty", "Apex Legends")
            ),
            new GamingResponse(
                "FPS games require quick reflexes! How long have you been playing?",
                "What's your main game right now?",
                Arrays.asList("Aim training", "Map knowledge", "Game sense")
            )
        );
        gamingResponses.put(GamingCategory.FPS, fpsResponses);
        
        // CS2/CS:GO responses
        List<GamingResponse> csResponses = Arrays.asList(
            new GamingResponse(
                "CS2 is a classic! What rank are you?",
                "What's your favorite map to play?",
                Arrays.asList("Mirage", "Inferno", "Nuke", "Vertigo", "Ancient")
            ),
            new GamingResponse(
                "Counter-Strike requires so much skill! What's your main weapon?",
                "Do you prefer CT or T side?",
                Arrays.asList("AK-47", "M4A4", "AWP", "Deagle")
            ),
            new GamingResponse(
                "CS:GO is a timeless classic! How's your aim been lately?",
                "Do you play matchmaking or prefer faceit?",
                Arrays.asList("Headshot practice", "Smoke spreads", "Economy management")
            ),
            new GamingResponse(
                "Counter-Strike is all about game sense and teamwork!",
                "Are you playing with friends or solo queue?",
                Arrays.asList("Clutch moments", "Eco rounds", "Buy strategies")
            ),
            new GamingResponse(
                "I love hearing about Counter-Strike! How long have you been playing?",
                "What's your best rank ever achieved?",
                Arrays.asList("Wingman", "Danger Zone", "Premier mode")
            ),
            new GamingResponse(
                "Nothing beats a good CS:GO session! What's your favorite crosshair setup?",
                "Do you use any specific aim training routines?",
                Arrays.asList("Deathmatch", "Aim botz", "Reflex training")
            )
        );
        gamingResponses.put(GamingCategory.FPS, csResponses);
        
        // RPG responses
        List<GamingResponse> rpgResponses = Arrays.asList(
            new GamingResponse(
                "RPGs are amazing for storytelling! What kind of RPGs do you enjoy?",
                "Do you prefer action RPGs or turn-based?",
                Arrays.asList("Final Fantasy", "Elden Ring", "Skyrim", " Witcher 3")
            )
        );
        gamingResponses.put(GamingCategory.RPG, rpgResponses);
        
        // General gaming responses
        List<GamingResponse> generalGamingResponses = Arrays.asList(
            new GamingResponse(
                "Gaming is such a great hobby! What games have you been playing lately?",
                "Do you have a favorite genre?",
                Arrays.asList("Multiplayer", "Single-player", "Co-op")
            ),
            new GamingResponse(
                "I love hearing about gaming! What's your favorite game?",
                "Are you currently playing any games?",
                Arrays.asList("New releases", "Classics", "Indie games")
            )
        );
        gamingResponses.put(GamingCategory.GENERAL, generalGamingResponses);
    }
    
    private void initializeEntertainmentResponses() {
        // Movies responses
        List<EntertainmentResponse> movieResponses = Arrays.asList(
            new EntertainmentResponse(
                "Movies are a great way to relax! What kind of movies do you enjoy?",
                "Are you into any specific genres?",
                Arrays.asList("Action", "Comedy", "Sci-Fi", "Drama", "Horror")
            ),
            new EntertainmentResponse(
                "Film night! What's your all-time favorite movie?",
                "Who are your favorite actors or directors?",
                Arrays.asList("Classics", "Blockbusters", "Indie films")
            )
        );
        entertainmentResponses.put(EntertainmentCategory.MOVIES, movieResponses);
        
        // TV Shows responses
        List<EntertainmentResponse> tvResponses = Arrays.asList(
            new EntertainmentResponse(
                "TV shows are so engaging! What are you currently watching?",
                "Do you have a favorite series?",
                Arrays.asList("Drama", "Comedy", "Thriller", "Fantasy")
            ),
            new EntertainmentResponse(
                "Binge-watching season! What's your go-to genre?",
                "Are you following any shows right now?",
                Arrays.asList("Netflix", "Hulu", "Disney+", " HBO")
            )
        );
        entertainmentResponses.put(EntertainmentCategory.TV_SHOWS, tvResponses);
        
        // Music responses
        List<EntertainmentResponse> musicResponses = Arrays.asList(
            new EntertainmentResponse(
                "Music is so personal! What kind of music do you listen to?",
                "Do you have a favorite artist?",
                Arrays.asList("Pop", "Rock", "Hip-Hop", "Classical", "Electronic")
            ),
            new EntertainmentResponse(
                "Music makes everything better! What's your current favorite song?",
                "Do you prefer listening or making music?",
                Arrays.asList("Playlists", "Concerts", "Discovering new artists")
            )
        );
        entertainmentResponses.put(EntertainmentCategory.MUSIC, musicResponses);
        
        // Sports responses
        List<EntertainmentResponse> sportsResponses = Arrays.asList(
            new EntertainmentResponse(
                "Sports are exciting! Do you play or watch?",
                "What's your favorite sport?",
                Arrays.asList("Football", "Basketball", "Soccer", "Tennis")
            ),
            new EntertainmentResponse(
                "Athletics are so exciting! Are you following any teams or events?",
                "Do you have a favorite player?",
                Arrays.asList("Professional", "College", "Local teams")
            )
        );
        entertainmentResponses.put(EntertainmentCategory.SPORTS, sportsResponses);
        
        // General entertainment responses
        List<EntertainmentResponse> generalEntertainmentResponses = Arrays.asList(
            new EntertainmentResponse(
                "Everyone needs entertainment! What do you do for fun?",
                "Any new hobbies or interests lately?",
                Arrays.asList("Creative pursuits", "Games", "Social activities")
            ),
            new EntertainmentResponse(
                "Work hard, play hard! What helps you unwind?",
                "Do you have any favorite pastimes?",
                Arrays.asList("Relaxing", "Adventure", "Learning")
            )
        );
        entertainmentResponses.put(EntertainmentCategory.GENERAL, generalEntertainmentResponses);
    }
    
    private void initializeKeywords() {
        // Gaming keywords
        gamingKeywords.put("fortnite", GamingCategory.BATTLE_ROYALE);
        gamingKeywords.put("battle royale", GamingCategory.BATTLE_ROYALE);
        gamingKeywords.put("cs2", GamingCategory.FPS);
        gamingKeywords.put("counter strike", GamingCategory.FPS);
        gamingKeywords.put("cs:go", GamingCategory.FPS);
        gamingKeywords.put("csgo", GamingCategory.FPS);
        gamingKeywords.put("valorant", GamingCategory.FPS);
        gamingKeywords.put("call of duty", GamingCategory.FPS);
        gamingKeywords.put("apex", GamingCategory.FPS);
        gamingKeywords.put("cod", GamingCategory.FPS);
        gamingKeywords.put("rpg", GamingCategory.RPG);
        gamingKeywords.put("role playing", GamingCategory.RPG);
        gamingKeywords.put("elden ring", GamingCategory.RPG);
        gamingKeywords.put("witcher", GamingCategory.RPG);
        gamingKeywords.put("skyrim", GamingCategory.RPG);
        gamingKeywords.put("minecraft", GamingCategory.SANDBOX);
        gamingKeywords.put("sandbox", GamingCategory.SANDBOX);
        gamingKeywords.put("gaming", GamingCategory.GENERAL);
        gamingKeywords.put("game", GamingCategory.GENERAL);
        gamingKeywords.put("gamer", GamingCategory.GENERAL);
        gamingKeywords.put("play", GamingCategory.GENERAL);
        gamingKeywords.put("video game", GamingCategory.GENERAL);
        
        // Entertainment keywords
        entertainmentKeywords.put("movie", EntertainmentCategory.MOVIES);
        entertainmentKeywords.put("films", EntertainmentCategory.MOVIES);
        entertainmentKeywords.put("cinema", EntertainmentCategory.MOVIES);
        entertainmentKeywords.put("tv", EntertainmentCategory.TV_SHOWS);
        entertainmentKeywords.put("show", EntertainmentCategory.TV_SHOWS);
        entertainmentKeywords.put("series", EntertainmentCategory.TV_SHOWS);
        entertainmentKeywords.put("netflix", EntertainmentCategory.TV_SHOWS);
        entertainmentKeywords.put("music", EntertainmentCategory.MUSIC);
        entertainmentKeywords.put("song", EntertainmentCategory.MUSIC);
        entertainmentKeywords.put("songs", EntertainmentCategory.MUSIC);
        entertainmentKeywords.put("book", EntertainmentCategory.BOOKS);
        entertainmentKeywords.put("reading", EntertainmentCategory.BOOKS);
        entertainmentKeywords.put("sports", EntertainmentCategory.SPORTS);
        entertainmentKeywords.put("football", EntertainmentCategory.SPORTS);
        entertainmentKeywords.put("basketball", EntertainmentCategory.SPORTS);
        entertainmentKeywords.put("soccer", EntertainmentCategory.SPORTS);
        entertainmentKeywords.put("hobby", EntertainmentCategory.HOBBIES);
        entertainmentKeywords.put("hobbies", EntertainmentCategory.HOBBIES);
        entertainmentKeywords.put("fun", EntertainmentCategory.GENERAL);
        entertainmentKeywords.put("entertainment", EntertainmentCategory.GENERAL);
    }
    
    /**
     * Detects gaming category from input
     */
    public GamingCategory detectGamingCategory(String input) {
        String lowerInput = input.toLowerCase();
        
        for (Map.Entry<String, GamingCategory> entry : gamingKeywords.entrySet()) {
            if (lowerInput.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        return GamingCategory.GENERAL;
    }
    
    /**
     * Detects entertainment category from input
     */
    public EntertainmentCategory detectEntertainmentCategory(String input) {
        String lowerInput = input.toLowerCase();
        
        for (Map.Entry<String, EntertainmentCategory> entry : entertainmentKeywords.entrySet()) {
            if (lowerInput.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        return EntertainmentCategory.GENERAL;
    }
    
    /**
     * Checks if input is gaming-related
     */
    public boolean isGamingRelated(String input) {
        String lowerInput = input.toLowerCase();
        
        String[] gamingTriggers = {
            "game", "gaming", "play", "player", "gamer", "rank",
            "fortnite", "cs2", "csgo", "valorant", "minecraft", "apex"
        };
        
        for (String trigger : gamingTriggers) {
            if (lowerInput.contains(trigger)) {
                return true;
            }
        }
        
        return detectGamingCategory(input) != GamingCategory.GENERAL;
    }
    
    /**
     * Checks if input is entertainment-related
     */
    public boolean isEntertainmentRelated(String input) {
        String lowerInput = input.toLowerCase();
        
        String[] entertainmentTriggers = {
            "movie", "music", "show", "sport", "hobby", "fun",
            "book", "read", "watch", "listen"
        };
        
        for (String trigger : entertainmentTriggers) {
            if (lowerInput.contains(trigger)) {
                return true;
            }
        }
        
        return detectEntertainmentCategory(input) != EntertainmentCategory.GENERAL;
    }
    
    /**
     * Gets gaming response
     */
    public GamingResponse getGamingResponse(GamingCategory category) {
        List<GamingResponse> responses = gamingResponses.get(category);
        if (responses != null && !responses.isEmpty()) {
            return responses.get(random.nextInt(responses.size()));
        }
        
        List<GamingResponse> generalResponses = gamingResponses.get(GamingCategory.GENERAL);
        return generalResponses.get(random.nextInt(generalResponses.size()));
    }
    
    /**
     * Gets entertainment response
     */
    public EntertainmentResponse getEntertainmentResponse(EntertainmentCategory category) {
        List<EntertainmentResponse> responses = entertainmentResponses.get(category);
        if (responses != null && !responses.isEmpty()) {
            return responses.get(random.nextInt(responses.size()));
        }
        
        List<EntertainmentResponse> generalResponses = entertainmentResponses.get(EntertainmentCategory.GENERAL);
        return generalResponses.get(generalResponses.size() - 1);
    }
    
    /**
     * Gets gaming recommendations
     */
    public List<String> getGamingRecommendations(GamingCategory category) {
        switch (category) {
            case BATTLE_ROYALE:
                return Arrays.asList(
                    "Fortnite - Great for all skill levels",
                    "Apex Legends - Amazing movement mechanics",
                    "PUBG - Realistic battle royale experience",
                    "Fall Guys - Fun and chaotic"
                );
            case FPS:
                return Arrays.asList(
                    "CS2 - Classic tactical shooter",
                    "Valorant - Strategic ability-based FPS",
                    "Call of Duty: Warzone - Fast-paced action",
                    "Overwatch 2 - Hero-based team shooter"
                );
            case RPG:
                return Arrays.asList(
                    "Elden Ring - Challenging open world",
                    "The Witcher 3 - Incredible storytelling",
                    "Final Fantasy XVI - Stunning visuals",
                    "Baldur's Gate 3 - Deep RPG mechanics"
                );
            case SANDBOX:
                return Arrays.asList(
                    "Minecraft - Endless creativity",
                    "Roblox - User-generated games",
                    "Garry's Mod - Unlimited possibilities",
                    "Terraria - Adventure and crafting"
                );
            default:
                return Arrays.asList(
                    "Explore different genres!",
                    "Try games with friends",
                    "Check out indie gems",
                    "Look for game sales"
                );
        }
    }
    
    /**
     * Gets entertainment recommendations
     */
    public List<String> getEntertainmentRecommendations(EntertainmentCategory category) {
        switch (category) {
            case MOVIES:
                return Arrays.asList(
                    "Check out recent releases",
                    "Explore different genres",
                    "Watch critically acclaimed films",
                    "Try foreign films"
                );
            case TV_SHOWS:
                return Arrays.asList(
                    "Discover new series",
                    "Binge-watch a classic",
                    "Explore international shows",
                    "Try different streaming platforms"
                );
            case MUSIC:
                return Arrays.asList(
                    "Create new playlists",
                    "Explore different genres",
                    "Discover new artists",
                    "Attend concerts"
                );
            case BOOKS:
                return Arrays.asList(
                    "Try different genres",
                    "Join a book club",
                    "Read bestsellers",
                    "Explore classics"
                );
            default:
                return Arrays.asList(
                    "Find what brings you joy",
                    "Try new experiences",
                    "Balance different activities",
                    "Make time for fun"
                );
        }
    }
    
    /**
     * Gets conversation starter for gaming
     */
    public String getGamingConversationStarter() {
        String[] starters = {
            "What's your favorite game of all time?",
            "Are you currently playing any games?",
            "What's your gaming setup like?",
            "Do you prefer single-player or multiplayer?",
            "What's the best gaming moment you've experienced?",
            "Are you excited about any upcoming releases?",
            "What's your favorite gaming memory?",
            "Do you have a favorite game soundtrack?",
            "What's your go-to game when you want to relax?",
            "Have you discovered any hidden gem games lately?"
        };
        return starters[random.nextInt(starters.length)];
    }
    
    /**
     * Gets conversation starter for entertainment
     */
    public String getEntertainmentConversationStarter() {
        String[] starters = {
            "What's your favorite movie?",
            "What TV shows are you watching right now?",
            "What music have you been listening to lately?",
            "Do you have a favorite book?",
            "What's your favorite way to relax and have fun?",
            "Have you seen any good movies recently?",
            "What kind of hobbies have you been into?",
            "Are you looking forward to any game or movie releases?",
            "What's your all-time favorite song?",
            "Do you have any interesting plans for the weekend?"
        };
        return starters[random.nextInt(starters.length)];
    }
    
    /**
     * Handles gaming questions
     */
    public String handleGamingQuestion(String input) {
        String lowerInput = input.toLowerCase();
        
        // Weapon questions
        if (lowerInput.contains("weapon") || lowerInput.contains("gun") || lowerInput.contains("favorite weapon")) {
            return "What's your go-to weapon? I find it says a lot about your playstyle!";
        }
        
        // Map questions
        if (lowerInput.contains("map") || lowerInput.contains("favorite map")) {
            return "Map preference says a lot about your strategy! What's your favorite?";
        }
        
        // Rank questions
        if (lowerInput.contains("rank") || lowerInput.contains("elo") || lowerInput.contains("mmr")) {
            return "Ranks can be frustrating but rewarding! What rank are you aiming for?";
        }
        
        // Tips questions
        if (lowerInput.contains("tip") || lowerInput.contains("advice") || lowerInput.contains("how to")) {
            return "Great question! Here's a tip: Practice consistently and learn from your mistakes. What specific aspect do you want to improve?";
        }
        
        // General gaming question
        return getGamingConversationStarter();
    }
    
    /**
     * Handles entertainment questions
     */
    public String handleEntertainmentQuestion(String input) {
        String lowerInput = input.toLowerCase();
        
        // Recommendation requests
        if (lowerInput.contains("recommend") || lowerInput.contains("suggestion")) {
            return "Recommendations are fun! What mood are you in? I can suggest something based on your taste.";
        }
        
        // Opinion questions
        if (lowerInput.contains("opinion") || lowerInput.contains("think about")) {
            return "That's interesting! I'd love to hear your thoughts. What aspect are you curious about?";
        }
        
        // General entertainment question
        return getEntertainmentConversationStarter();
    }
}

