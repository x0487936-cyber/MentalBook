import java.util.*;
import java.util.regex.*;

/**
 * CreativeWritingHandler - Handles creative writing assistance
 * Part of Phase 3: Response Categories
 */
public class CreativeWritingHandler {
    
    private Map<WritingGenre, List<WritingResponse>> genreResponses;
    private Map<String, WritingGenre> genreKeywords;
    private Random random;
    
    public CreativeWritingHandler() {
        this.genreResponses = new HashMap<>();
        this.genreKeywords = new HashMap<>();
        this.random = new Random();
        initializeGenreResponses();
        initializeGenreKeywords();
    }
    
    /**
     * Writing genres
     */
    public enum WritingGenre {
        FICTION("Fiction"),
        NON_FICTION("Non-Fiction"),
        POETRY("Poetry"),
        SCIFI("Science Fiction"),
        FANTASY("Fantasy"),
        MYSTERY("Mystery"),
        ROMANCE("Romance"),
        THRILLER("Thriller"),
        HORROR("Horror"),
        HISTORICAL("Historical Fiction"),
        ADVENTURE("Adventure"),
        COMEDY("Comedy/Satire"),
        DRAMA("Drama"),
        SHORT_STORY("Short Story"),
        BLOG("Blog Post"),
        SCRIPT("Script/Screenplay"),
        GENERAL("General Creative Writing");
        
        private final String displayName;
        
        WritingGenre(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Writing response class
     */
    private static class WritingResponse {
        String response;
        String prompt;
        List<String> tips;
        
        public WritingResponse(String response, String prompt, List<String> tips) {
            this.response = response;
            this.prompt = prompt;
            this.tips = tips;
        }
    }
    
    private void initializeGenreResponses() {
        // Fiction responses
        List<WritingResponse> fictionResponses = Arrays.asList(
            new WritingResponse(
                "Fiction is a wonderful way to explore imaginary worlds! What kind of story are you working on?",
                "Try starting with: 'The door creaked open, revealing...'",
                Arrays.asList("Develop compelling characters", "Create a clear plot structure", "Show, don't tell")
            )
        );
        genreResponses.put(WritingGenre.FICTION, fictionResponses);
        
        // Science Fiction responses
        List<WritingResponse> scifiResponses = Arrays.asList(
            new WritingResponse(
                "Science fiction lets us explore 'what if' scenarios! What aspect are you writing about?",
                "Try starting with: 'In the year 2450, humanity had finally cracked the code of...'",
                Arrays.asList("Ground sci-fi in real science", "Consider future implications", "Create believable technology")
            )
        );
        genreResponses.put(WritingGenre.SCIFI, scifiResponses);
        
        // Fantasy responses
        List<WritingResponse> fantasyResponses = Arrays.asList(
            new WritingResponse(
                "Fantasy is all about magic and imagination! What world are you creating?",
                "Try starting with: 'The ancient spell had been dormant for centuries, until...'",
                Arrays.asList("Build consistent magic systems", "Create detailed world-building", "Develop unique cultures")
            )
        );
        genreResponses.put(WritingGenre.FANTASY, fantasyResponses);
        
        // Mystery responses
        List<WritingResponse> mysteryResponses = Arrays.asList(
            new WritingResponse(
                "Mystery writing keeps readers on the edge! What's the central puzzle in your story?",
                "Try starting with: 'The detective examined the room, noting three things that didn't add up...'",
                Arrays.asList("Plant clues carefully", "Create red herrings", "Build to a satisfying reveal")
            )
        );
        genreResponses.put(WritingGenre.MYSTERY, mysteryResponses);
        
        // Romance responses
        List<WritingResponse> romanceResponses = Arrays.asList(
            new WritingResponse(
                "Romance captures the heart! What kind of love story are you telling?",
                "Try starting with: 'When their eyes met across the crowded room, neither expected...'",
                Arrays.asList("Build emotional tension", "Create believable obstacles", "Develop chemistry between characters")
            )
        );
        genreResponses.put(WritingGenre.ROMANCE, romanceResponses);
        
        // Poetry responses
        List<WritingResponse> poetryResponses = Arrays.asList(
            new WritingResponse(
                "Poetry is the art of expression! What style do you prefer?",
                "Try starting with: A haiku about nature, or free verse about an emotion...",
                Arrays.asList("Play with rhythm and sound", "Use vivid imagery", "Find the perfect word")
            )
        );
        genreResponses.put(WritingGenre.POETRY, poetryResponses);
        
        // Horror responses
        List<WritingResponse> horrorResponses = Arrays.asList(
            new WritingResponse(
                "Horror taps into our deepest fears! What scares you most?",
                "Try starting with: 'The shadows in the corner weren't quite right. They moved when...'",
                Arrays.asList("Build dread slowly", "Use sensory details", "Leave something to imagination")
            )
        );
        genreResponses.put(WritingGenre.HORROR, horrorResponses);
        
        // Adventure responses
        List<WritingResponse> adventureResponses = Arrays.asList(
            new WritingResponse(
                "Adventure stories are thrilling! Where does your hero's journey take them?",
                "Try starting with: 'The map showed a route no one had taken in a hundred years...'",
                Arrays.asList("Create exciting obstacles", "Build stakes high", "Keep pacing fast")
            )
        );
        genreResponses.put(WritingGenre.ADVENTURE, adventureResponses);
        
        // Blog responses
        List<WritingResponse> blogResponses = Arrays.asList(
            new WritingResponse(
                "Blog writing is a great way to share ideas! What's your topic?",
                "Try starting with: A compelling question, a personal story, or a surprising fact...",
                Arrays.asList("Know your audience", "Use engaging headlines", "Include visuals")
            )
        );
        genreResponses.put(WritingGenre.BLOG, blogResponses);
        
        // General writing responses
        List<WritingResponse> generalResponses = Arrays.asList(
            new WritingResponse(
                "Creative writing is a powerful form of expression! What are you working on?",
                "Just start writing - the words will come!",
                Arrays.asList("Write regularly", "Read widely", "Don't be afraid to edit")
            ),
            new WritingResponse(
                "I'd love to hear about your creative project! What are you creating?",
                "Every story starts with a single word...",
                Arrays.asList("Trust your creative voice", "Embrace the revision process", "Find your writing routine")
            )
        );
        genreResponses.put(WritingGenre.GENERAL, generalResponses);
    }
    
    private void initializeGenreKeywords() {
        // Genre keywords - prioritized by specificity (more specific first)
        
        // Science Fiction (specific phrases first)
        genreKeywords.put("science fiction", WritingGenre.SCIFI);
        genreKeywords.put("sci-fi", WritingGenre.SCIFI);
        genreKeywords.put("scifi", WritingGenre.SCIFI);
        
        // Fantasy
        genreKeywords.put("fantasy", WritingGenre.FANTASY);
        genreKeywords.put("dragons", WritingGenre.FANTASY);
        
        // Mystery
        genreKeywords.put("mystery", WritingGenre.MYSTERY);
        genreKeywords.put("detective", WritingGenre.MYSTERY);
        genreKeywords.put("whodunit", WritingGenre.MYSTERY);
        
        // Romance
        genreKeywords.put("romance", WritingGenre.ROMANCE);
        genreKeywords.put("love story", WritingGenre.ROMANCE);
        
        // Horror
        genreKeywords.put("horror", WritingGenre.HORROR);
        genreKeywords.put("scary", WritingGenre.HORROR);
        genreKeywords.put("spooky", WritingGenre.HORROR);
        
        // Thriller
        genreKeywords.put("thriller", WritingGenre.THRILLER);
        genreKeywords.put("suspense", WritingGenre.THRILLER);
        
        // Historical
        genreKeywords.put("historical fiction", WritingGenre.HISTORICAL);
        genreKeywords.put("historical", WritingGenre.HISTORICAL);
        
        // Poetry
        genreKeywords.put("poetry", WritingGenre.POETRY);
        genreKeywords.put("poem", WritingGenre.POETRY);
        genreKeywords.put("sonnet", WritingGenre.POETRY);
        
        // Adventure
        genreKeywords.put("adventure", WritingGenre.ADVENTURE);
        
        // Blog
        genreKeywords.put("blog post", WritingGenre.BLOG);
        genreKeywords.put("blog", WritingGenre.BLOG);
        
        // Script/Screenplay
        genreKeywords.put("screenplay", WritingGenre.SCRIPT);
        genreKeywords.put("script", WritingGenre.SCRIPT);
        genreKeywords.put("dialogue", WritingGenre.SCRIPT);
        
        // Fiction variants
        genreKeywords.put("short story", WritingGenre.SHORT_STORY);
        genreKeywords.put("novel", WritingGenre.FICTION);
        genreKeywords.put("fiction", WritingGenre.FICTION);
        
        // Writing process keywords (general - checked last)
        genreKeywords.put("creative writing", WritingGenre.GENERAL);
        genreKeywords.put("creative", WritingGenre.GENERAL);
        genreKeywords.put("writer", WritingGenre.GENERAL);
        genreKeywords.put("writing", WritingGenre.GENERAL);
        genreKeywords.put("story", WritingGenre.GENERAL);
        genreKeywords.put("character", WritingGenre.GENERAL);
        genreKeywords.put("plot", WritingGenre.GENERAL);
    }
    
    /**
     * Detects writing genre from input
     * Prioritizes multi-word phrases over single words
     */
    public WritingGenre detectGenre(String input) {
        String lowerInput = input.toLowerCase();
        
        // First pass: Check for multi-word phrases (more specific)
        String[] multiWordPhrases = {
            "science fiction", "sci-fi", "scifi",
            "historical fiction", "love story", 
            "short story", "creative writing",
            "blog post", "screenplay"
        };
        
        for (String phrase : multiWordPhrases) {
            if (lowerInput.contains(phrase)) {
                return genreKeywords.get(phrase);
            }
        }
        
        // Second pass: Check for single-word keywords
        for (Map.Entry<String, WritingGenre> entry : genreKeywords.entrySet()) {
            String keyword = entry.getKey();
            // Skip multi-word phrases (already checked)
            if (!keyword.contains(" ") && lowerInput.contains(keyword)) {
                return entry.getValue();
            }
        }
        
        return WritingGenre.GENERAL;
    }
    
    /**
     * Checks if input is writing-related
     */
    public boolean isWritingRelated(String input) {
        String lowerInput = input.toLowerCase();
        
        String[] writingTriggers = {
            "write", "writing", "story", "book", "novel", "poem",
            "character", "plot", "chapter", "scene", "narrative",
            "creative", "fiction", "genre"
        };
        
        for (String trigger : writingTriggers) {
            if (lowerInput.contains(trigger)) {
                return true;
            }
        }
        
        return detectGenre(input) != WritingGenre.GENERAL;
    }
    
    /**
     * Gets writing response for genre
     */
    public WritingResponse getResponse(WritingGenre genre) {
        List<WritingResponse> responses = genreResponses.get(genre);
        if (responses != null && !responses.isEmpty()) {
            return responses.get(random.nextInt(responses.size()));
        }
        
        List<WritingResponse> generalResponses = genreResponses.get(WritingGenre.GENERAL);
        return generalResponses.get(random.nextInt(generalResponses.size()));
    }
    
    /**
     * Gets writing prompts for a genre
     */
    public List<String> getWritingPrompts(WritingGenre genre) {
        switch (genre) {
            case SCIFI:
                return Arrays.asList(
                    "What if dreams could be recorded and replayed?",
                    "In a world where emotions can be bought and sold...",
                    "First contact: humanity receives a message from deep space",
                    "What would society look like if death was cured?",
                    "A colony ship has been traveling for 500 years..."
                );
            case FANTASY:
                return Arrays.asList(
                    "A young person discovers they can talk to animals",
                    "Magic is real but has strict rules and consequences",
                    "The last dragon on Earth has been hiding in plain sight",
                    "A wizard's apprentice accidentally transforms their master",
                    "What if your shadow had a mind of its own?"
                );
            case MYSTERY:
                return Arrays.asList(
                    "Something was missing from the crime scene - nothing",
                    "The witness changed their testimony three times",
                    "A letter arrived 50 years too late, containing a confession",
                    "The detective's prime suspect has an alibi, but...",
                    "What if the victim was the murderer all along?"
                );
            case ROMANCE:
                return Arrays.asList(
                    "Two rival florists compete for the same contract",
                    "A wedding planner falls for the bride's sibling",
                    "Childhood sweethearts reunite after 15 years",
                    "A fake dating situation becomes very real",
                    "What if your soulmate was your enemy?"
                );
            case HORROR:
                return Arrays.asList(
                    "The house has been empty for years, but someone keeps watering the plants",
                    "Everyone in town remembers the incident differently",
                    "The reflection in the mirror is half a second behind",
                    "What if your doppelgänger is trying to replace you?",
                    "The phone call came from your number - but you never made it"
                );
            case ADVENTURE:
                return Arrays.asList(
                    "A treasure map shows up in an antique book",
                    "The mountain hasn't been climbed in a century",
                    "What lies beyond the known boundaries of the world?",
                    "An ancient artifact awakens something powerful",
                    "The journey home is more dangerous than the journey away"
                );
            case POETRY:
                return Arrays.asList(
                    "Write about the color of emotions",
                    "Describe a season using only taste metaphors",
                    "A letter to your younger self",
                    "The moment before dawn breaks",
                    "Write about something broken that is still beautiful"
                );
            default:
                return Arrays.asList(
                    "Start with a character who wants something",
                    "Begin with an unexpected encounter",
                    "Write about a secret revealed",
                    "Start in the middle of the action",
                    "Begin with a powerful image"
                );
        }
    }
    
    /**
     * Gets writing tips
     */
    public List<String> getWritingTips(WritingGenre genre) {
        WritingResponse response = getResponse(genre);
        return response.tips;
    }
    
    /**
     * General writing advice
     */
    public String getGeneralWritingAdvice() {
        String[] advice = {
            "Write what interests you - your passion will show through!",
            "Read voraciously in your genre and beyond.",
            "Don't worry about perfection in your first draft.",
            "Characters should want things, even contradictory things.",
            "Conflict is the engine of story - create obstacles for your characters.",
            "Show, don't tell. Use sensory details.",
            "Kill your darlings - be willing to cut what doesn't serve the story.",
            "Write regularly, even if just for a few minutes a day.",
            "Join a writing group or find a writing buddy for accountability.",
            "Revisions are where the real writing happens."
        };
        return advice[random.nextInt(advice.length)];
    }
    
    /**
     * Handles character development questions
     */
    public String handleCharacterDevelopment(String input) {
        String lowerInput = input.toLowerCase();
        
        if (lowerInput.contains("name")) {
            return "Character names should hint at personality or be memorable. Try: What would this person answer to?";
        }
        
        if (lowerInput.contains("motivation") || lowerInput.contains("want")) {
            return "Every character needs a goal, a want, and a need. What's driving your character?";
        }
        
        if (lowerInput.contains("flaw") || lowerInput.contains("weakness")) {
            return "Flaws make characters relatable. What's your character's blind spot or weakness?";
        }
        
        if (lowerInput.contains("backstory")) {
            return "Backstory informs present action. What defining moment shaped your character?";
        }
        
        if (lowerInput.contains("voice")) {
            return "Voice is how your character talks and thinks. What's unique about their perspective?";
        }
        
        return "Here are some character development questions:\n" +
               "• What does your character want more than anything?\n" +
               "• What do they fear most?\n" +
               "• How do they handle stress?\n" +
               "• What would they never do?\n" +
               "• What makes them laugh?";
    }
    
    /**
     * Handles plot structure questions
     */
    public String handlePlotStructure(String input) {
        String lowerInput = input.toLowerCase();
        
        if (lowerInput.contains("structure") || lowerInput.contains("outline")) {
            return "Classic story structure:\n" +
                   "1. Setup - Introduce character and world\n" +
                   "2. Inciting incident - Disrupts normal life\n" +
                   "3. Rising action - Complications build\n" +
                   "4. Midpoint - Major revelation or change\n" +
                   "5. All is lost - Lowest point\n" +
                   "6. Resolution - Character overcomes challenges";
        }
        
        if (lowerInput.contains("beginning") || lowerInput.contains("start")) {
            return "Great openings hook readers immediately. Try starting with:\n" +
                   "• A question or mystery\n" +
                   "• An unusual situation\n" +
                   "• Strong, distinctive voice\n" +
                   "• Immediate conflict or tension";
        }
        
        if (lowerInput.contains("ending") || lowerInput.contains("end")) {
            return "Satisfying endings should:\n" +
                   "• Resolve the central conflict\n" +
                   "• Show character growth\n" +
                   "• Feel inevitable yet surprising\n" +
                   "• Leave readers with a feeling";
        }
        
        if (lowerInput.contains("writer's block") || lowerInput.contains("stuck")) {
            return "Writer's block happens! Try:\n" +
                   "• Free writing for 10 minutes\n" +
                   "• Writing out of order\n" +
                   "• Changing your environment\n" +
                   "• Writing a scene you'll delete\n" +
                   "• Taking a break and returning fresh";
        }
        
        return "Plot is what happens. Story is why it matters. Focus on both!";
    }
    
    /**
     * Handles dialogue questions
     */
    public String handleDialogue(String input) {
        String lowerInput = input.toLowerCase();
        
        if (lowerInput.contains("how to write") || lowerInput.contains("writing dialogue")) {
            return "Good dialogue tips:\n" +
                   "• Each character should sound distinct\n" +
                   "• Subtext is powerful - what characters DON'T say\n" +
                   "• Read it aloud - does it sound natural?\n" +
                   "• Use dialogue to advance plot or reveal character\n" +
                   "• Avoid on-the-nose exposition";
        }
        
        if (lowerInput.contains("description") || lowerInput.contains("tags")) {
            return "Dialogue tags: Use 'said' most of the time. Other options:\n" +
                   "• asked, replied, answered (for questions/statements)\n" +
                   "• whispered, shouted, murmured (for volume)\n" +
                   "• groaned, laughed, sighed (for emotion)\n" +
                   "• Be careful with -ly adverbs!";
        }
        
        return "Dialogue brings characters to life. Make each voice unique!";
    }
    
    /**
     * Gets story ideas
     */
    public List<String> getStoryIdeas() {
        return Arrays.asList(
            "A character finds a door that wasn't there yesterday",
            "Everyone wakes up with the same dream, but different details",
            "A small town where something changes every Tuesday",
            "An assassin who only takes contracts to help people",
            "A teacher who discovers their student can see the future",
            "Two enemies trapped together during a disaster",
            "A world where lies become visible as marks on skin",
            "A person who can hear objects' memories",
            "An immortal who has lived through every historical event",
            "A detective who can only solve cases while dreaming"
        );
    }
}