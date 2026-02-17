import java.util.*;
import java.util.regex.*;

/**
 * ConversationGames - Interactive Games Module for VirtualXander
 * 
 * Implements engaging conversation games:
 * - 20 Questions with smart guessing
 * - Would You Rather with discussion prompts
 * - Trivia Quiz with score tracking
 * - Collaborative Story Builder
 * - Word Association game
 */
public class ConversationGames {
    
    private Random random;
    private Map<String, GameSession> activeGames;
    private String currentGame;
    
    // 20 Questions data
    private List<QuestionCategory> categories;
    private Map<String, List<String>> questionBank;
    private int questionsAsked;
    private int maxQuestions;
    
    // Would You Rather data
    private List<WouldYouRatherQuestion> wyrQuestions;
    
    // Trivia data
    private List<TriviaQuestion> triviaQuestions;
    private int triviaScore;
    private int triviaQuestionCount;
    
    // Story Builder data
    private StringBuilder collaborativeStory;
    private List<String> storyPrompts;
    
    // Word Association data
    private Set<String> usedWords;
    private String lastWord;
    private int associationScore;
    
    public ConversationGames() {
        this.random = new Random();
        this.activeGames = new HashMap<>();
        this.categories = initializeCategories();
        this.questionBank = initializeQuestionBank();
        this.wyrQuestions = initializeWYRQuestions();
        this.triviaQuestions = initializeTriviaQuestions();
        this.storyPrompts = initializeStoryPrompts();
        this.maxQuestions = 20;
        resetAllGames();
    }
    
    /**
     * Game session tracking
     */
    private static class GameSession {
        String gameType;
        int score;
        int turns;
        long startTime;
        Map<String, Object> gameData;
        
        GameSession(String type) {
            this.gameType = type;
            this.score = 0;
            this.turns = 0;
            this.startTime = System.currentTimeMillis();
            this.gameData = new HashMap<>();
        }
    }
    
    /**
     * Question category for 20 Questions
     */
    private static class QuestionCategory {
        String name;
        String description;
        List<String> commonItems;
        
        QuestionCategory(String name, String description, List<String> items) {
            this.name = name;
            this.description = description;
            this.commonItems = items;
        }
    }
    
    /**
     * Would You Rather question
     */
    private static class WouldYouRatherQuestion {
        String optionA;
        String optionB;
        String discussionPrompt;
        
        WouldYouRatherQuestion(String a, String b, String prompt) {
            this.optionA = a;
            this.optionB = b;
            this.discussionPrompt = prompt;
        }
    }
    
    /**
     * Trivia question
     */
    private static class TriviaQuestion {
        String question;
        String[] options;
        int correctAnswer;
        String category;
        String funFact;
        
        TriviaQuestion(String q, String[] opts, int correct, String cat, String fact) {
            this.question = q;
            this.options = opts;
            this.correctAnswer = correct;
            this.category = cat;
            this.funFact = fact;
        }
    }
    
    // ==================== GAME INITIALIZATION ====================
    
    private List<QuestionCategory> initializeCategories() {
        List<QuestionCategory> cats = new ArrayList<>();
        cats.add(new QuestionCategory("animal", "Animals", Arrays.asList(
            "dog", "cat", "elephant", "giraffe", "penguin", "dolphin", "eagle", "lion", 
            "tiger", "bear", "wolf", "fox", "rabbit", "deer", "horse", "cow", "pig", 
            "chicken", "duck", "goose", "turkey", "owl", "hawk", "falcon", "parrot",
            "snake", "lizard", "frog", "turtle", "crocodile", "shark", "whale", "octopus"
        )));
        cats.add(new QuestionCategory("food", "Food & Drinks", Arrays.asList(
            "pizza", "burger", "sushi", "pasta", "salad", "sandwich", "taco", "burrito",
            "steak", "chicken", "fish", "rice", "noodles", "soup", "bread", "cake",
            "ice cream", "chocolate", "cookie", "donut", "apple", "banana", "orange",
            "grape", "strawberry", "watermelon", "carrot", "broccoli", "potato", "corn"
        )));
        cats.add(new QuestionCategory("object", "Everyday Objects", Arrays.asList(
            "phone", "computer", "book", "pen", "pencil", "chair", "table", "bed", "lamp",
            "watch", "clock", "glasses", "shoes", "shirt", "pants", "hat", "bag", "wallet",
            "keys", "umbrella", "bottle", "cup", "plate", "fork", "spoon", "knife", "tv",
            "remote", "camera", "headphones", "speaker", "microwave", "refrigerator", "car"
        )));
        cats.add(new QuestionCategory("place", "Places", Arrays.asList(
            "beach", "mountain", "forest", "desert", "city", "village", "park", "zoo",
            "museum", "library", "school", "hospital", "restaurant", "cafe", "shop", "mall",
            "airport", "train station", "bus stop", "bridge", "tunnel", "castle", "palace",
            "temple", "church", "mosque", "synagogue", "stadium", "theater", "cinema"
        )));
        return cats;
    }
    
    private Map<String, List<String>> initializeQuestionBank() {
        Map<String, List<String>> bank = new HashMap<>();
        
        // Animal questions
        bank.put("animal", Arrays.asList(
            "Is it a mammal?", "Does it have fur?", "Can it fly?", "Does it live in water?",
            "Is it larger than a human?", "Is it a pet?", "Does it have four legs?", "Is it carnivorous?",
            "Does it lay eggs?", "Is it nocturnal?", "Does it hibernate?", "Is it endangered?",
            "Can it swim?", "Does it have a tail?", "Is it domesticated?", "Does it make a sound?",
            "Is it found on every continent?", "Does it have hooves?", "Does it have claws?", "Is it colorful?"
        ));
        
        // Food questions
        bank.put("food", Arrays.asList(
            "Is it sweet?", "Is it savory?", "Is it a fruit?", "Is it a vegetable?",
            "Is it meat?", "Is it dairy?", "Is it a dessert?", "Is it a drink?",
            "Is it served hot?", "Is it served cold?", "Is it a snack?", "Is it a main course?",
            "Is it breakfast food?", "Is it Italian?", "Is it Asian cuisine?", "Is it fast food?",
            "Is it healthy?", "Is it spicy?", "Is it raw?", "Is it baked?"
        ));
        
        // Object questions
        bank.put("object", Arrays.asList(
            "Is it electronic?", "Is it made of metal?", "Is it made of wood?", "Is it made of plastic?",
            "Is it larger than a shoebox?", "Is it used daily?", "Is it expensive?", "Is it portable?",
            "Does it require batteries?", "Is it used for communication?", "Is it used for entertainment?",
            "Is it found in a kitchen?", "Is it found in a bedroom?", "Is it found in an office?",
            "Is it worn on the body?", "Is it a tool?", "Is it a toy?", "Is it decorative?",
            "Is it used for cleaning?", "Is it used for cooking?"
        ));
        
        // Place questions
        bank.put("place", Arrays.asList(
            "Is it indoors?", "Is it outdoors?", "Is it natural?", "Is it man-made?",
            "Is it in a city?", "Is it in the countryside?", "Is it near water?", "Is it in the mountains?",
            "Is it a tourist destination?", "Is it free to enter?", "Is it educational?", "Is it for entertainment?",
            "Is it historical?", "Is it religious?", "Is it for shopping?", "Is it for eating?",
            "Is it for sports?", "Is it for transportation?", "Is it residential?", "Is it commercial?"
        ));
        
        return bank;
    }
    
    private List<WouldYouRatherQuestion> initializeWYRQuestions() {
        List<WouldYouRatherQuestion> questions = new ArrayList<>();
        questions.add(new WouldYouRatherQuestion(
            "be able to fly", "be able to breathe underwater",
            "What would you explore first with your new ability?"
        ));
        questions.add(new WouldYouRatherQuestion(
            "have unlimited money", "have unlimited time",
            "How would this change your daily life?"
        ));
        questions.add(new WouldYouRatherQuestion(
            "be famous", "be incredibly intelligent",
            "Which would bring you more fulfillment?"
        ));
        questions.add(new WouldYouRatherQuestion(
            "travel to the past", "travel to the future",
            "What era or time period interests you most?"
        ));
        questions.add(new WouldYouRatherQuestion(
            "never have to sleep", "never have to eat",
            "What would you do with all that extra time?"
        ));
        questions.add(new WouldYouRatherQuestion(
            "be the best at one thing", "be good at everything",
            "What skill would you choose to master?"
        ));
        questions.add(new WouldYouRatherQuestion(
            "live in a virtual reality", "live in space",
            "What would your ideal environment look like?"
        ));
        questions.add(new WouldYouRatherQuestion(
            "have a photographic memory", "be able to forget anything at will",
            "How would this affect your relationships?"
        ));
        questions.add(new WouldYouRatherQuestion(
            "always be 10 minutes late", "always be 20 minutes early",
            "Which would stress you out more?"
        ));
        questions.add(new WouldYouRatherQuestion(
            "be able to read minds", "be able to see the future",
            "Would you want to know everything, or just some things?"
        ));
        return questions;
    }
    
    private List<TriviaQuestion> initializeTriviaQuestions() {
        List<TriviaQuestion> questions = new ArrayList<>();
        questions.add(new TriviaQuestion(
            "What is the largest planet in our solar system?",
            new String[]{"Earth", "Mars", "Jupiter", "Saturn"},
            2, "Science", "Jupiter is so large that over 1,300 Earths could fit inside it!"
        ));
        questions.add(new TriviaQuestion(
            "Who painted the Mona Lisa?",
            new String[]{"Vincent van Gogh", "Leonardo da Vinci", "Pablo Picasso", "Michelangelo"},
            1, "Art", "Leonardo da Vinci carried the Mona Lisa with him for years, constantly refining it."
        ));
        questions.add(new TriviaQuestion(
            "What is the smallest country in the world?",
            new String[]{"Monaco", "Vatican City", "San Marino", "Liechtenstein"},
            1, "Geography", "Vatican City is only 0.44 square kilometers!"
        ));
        questions.add(new TriviaQuestion(
            "How many bones are in the adult human body?",
            new String[]{"206", "216", "196", "186"},
            0, "Biology", "Babies are born with about 270 bones that fuse together as they grow."
        ));
        questions.add(new TriviaQuestion(
            "What is the hardest natural substance on Earth?",
            new String[]{"Gold", "Iron", "Diamond", "Platinum"},
            2, "Science", "Diamonds are formed under extreme pressure about 100 miles below Earth's surface."
        ));
        questions.add(new TriviaQuestion(
            "Which planet is known as the Red Planet?",
            new String[]{"Venus", "Mars", "Jupiter", "Mercury"},
            1, "Astronomy", "Mars appears red due to iron oxide (rust) on its surface."
        ));
        questions.add(new TriviaQuestion(
            "What is the largest ocean on Earth?",
            new String[]{"Atlantic", "Indian", "Arctic", "Pacific"},
            3, "Geography", "The Pacific Ocean covers more than 30% of Earth's surface."
        ));
        questions.add(new TriviaQuestion(
            "Who wrote 'Romeo and Juliet'?",
            new String[]{"Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain"},
            1, "Literature", "Shakespeare invented over 1,700 words still used in English today."
        ));
        questions.add(new TriviaQuestion(
            "What is the chemical symbol for gold?",
            new String[]{"Go", "Gd", "Au", "Ag"},
            2, "Chemistry", "Au comes from the Latin word 'aurum' meaning shining dawn."
        ));
        questions.add(new TriviaQuestion(
            "How many continents are there?",
            new String[]{"5", "6", "7", "8"},
            2, "Geography", "The seven continents are: Asia, Africa, North America, South America, Antarctica, Europe, and Australia."
        ));
        return questions;
    }
    
    private List<String> initializeStoryPrompts() {
        return Arrays.asList(
            "Once upon a time, in a land where colors had sounds...",
            "The old lighthouse keeper had a secret that nobody knew...",
            "When the clock struck midnight, the museum exhibits came to life...",
            "In a world where dreams were currency...",
            "The last book in the world was about to be opened...",
            "A letter arrived that changed everything...",
            "The cat sat on the windowsill, watching something invisible...",
            "Deep beneath the city, a door appeared that wasn't there yesterday...",
            "The robot learned to feel emotions on a Tuesday...",
            "Magic returned to the world after 500 years of absence..."
        );
    }
    
    // ==================== GAME CONTROL ====================
    
    public void resetAllGames() {
        this.questionsAsked = 0;
        this.triviaScore = 0;
        this.triviaQuestionCount = 0;
        this.collaborativeStory = new StringBuilder();
        this.usedWords = new HashSet<>();
        this.associationScore = 0;
        this.currentGame = null;
    }
    
    public String startGame(String gameType) {
        resetAllGames();
        this.currentGame = gameType.toLowerCase();
        GameSession session = new GameSession(gameType);
        activeGames.put(gameType, session);
        
        switch (currentGame) {
            case "20questions":
            case "20 questions":
                return startTwentyQuestions();
            case "wyr":
            case "would you rather":
                return startWouldYouRather();
            case "trivia":
                return startTrivia();
            case "story":
            case "story builder":
                return startStoryBuilder();
            case "word":
            case "word association":
                return startWordAssociation();
            default:
                return "I don't know that game. Available games: 20 Questions, Would You Rather, Trivia, Story Builder, Word Association";
        }
    }
    
    public String processGameInput(String input) {
        if (currentGame == null) {
            return "No game is currently active. Type 'play [game name]' to start!";
        }
        
        GameSession session = activeGames.get(currentGame);
        if (session != null) {
            session.turns++;
        }
        
        switch (currentGame) {
            case "20questions":
                return processTwentyQuestions(input);
            case "wyr":
                return processWouldYouRather(input);
            case "trivia":
                return processTrivia(input);
            case "story":
                return processStoryBuilder(input);
            case "word":
                return processWordAssociation(input);
            default:
                return "Game error. Please start a new game.";
        }
    }
    
    public boolean isGameActive() {
        return currentGame != null;
    }
    
    public String getCurrentGame() {
        return currentGame;
    }
    
    public String endGame() {
        if (currentGame == null) {
            return "No game is currently active.";
        }
        
        String gameName = currentGame;
        GameSession session = activeGames.get(gameName);
        String summary = generateGameSummary(session);
        resetAllGames();
        return summary;
    }
    
    private String generateGameSummary(GameSession session) {
        if (session == null) return "Game ended.";
        
        long duration = (System.currentTimeMillis() - session.startTime) / 1000;
        return String.format("🎮 Game Over! 🎮\n\nGame: %s\nScore: %d\nTurns: %d\nTime: %d seconds\n\nGreat playing with you!",
            session.gameType, session.score, session.turns, duration);
    }
    
    // ==================== 20 QUESTIONS ====================
    
    private String startTwentyQuestions() {
        QuestionCategory category = categories.get(random.nextInt(categories.size()));
        String target = category.commonItems.get(random.nextInt(category.commonItems.size()));
        
        GameSession session = activeGames.get("20questions");
        session.gameData.put("target", target);
        session.gameData.put("category", category.name);
        session.gameData.put("possibleItems", new ArrayList<>(category.commonItems));
        
        return String.format("🎯 20 Questions! 🎯\n\nI'm thinking of something in the category: %s\n\nYou have 20 questions to guess what it is! Ask me yes/no questions to narrow it down.\n\nExample questions:\n• Is it an animal?\n• Is it bigger than a breadbox?\n• Can you eat it?\n\nWhat's your first question?",
            category.description);
    }
    
    private String processTwentyQuestions(String input) {
        GameSession session = activeGames.get("20questions");
        List<String> possibleItems = (List<String>) session.gameData.get("possibleItems");
        String target = (String) session.gameData.get("target");
        
        questionsAsked++;
        
        if (questionsAsked >= maxQuestions) {
            return String.format("❌ Game Over! You've used all 20 questions.\n\nI was thinking of: %s\n\nType 'play 20 questions' to try again!", target);
        }
        
        // Check if they're guessing
        String lowerInput = input.toLowerCase().trim();
        if (lowerInput.startsWith("is it ") || lowerInput.startsWith("are they ")) {
            String guess = lowerInput.replace("is it ", "").replace("are they ", "").replace("?", "").trim();
            if (guess.equals(target.toLowerCase())) {
                session.score = Math.max(0, (maxQuestions - questionsAsked) * 10);
                return String.format("🎉 CORRECT! 🎉\n\nYou guessed '%s' in %d questions!\n\nScore: %d points\n\nGreat job! Type 'play 20 questions' to play again!",
                    target, questionsAsked, session.score);
            } else {
                return String.format("❌ No, it's not %s.\n\nQuestions remaining: %d\n\nKeep asking!",
                    guess, maxQuestions - questionsAsked);
            }
        }
        
        // Process yes/no question
        String answer = answerYesNoQuestion(input, target, possibleItems);
        
        // Narrow down possibilities based on answer
        if (answer.equals("Yes!") || answer.equals("Yes")) {
            narrowPossibilities(possibleItems, input, true);
        } else if (answer.equals("No.")) {
            narrowPossibilities(possibleItems, input, false);
        }
        
        return String.format("%s\n\n(Question %d of %d)", answer, questionsAsked, maxQuestions);
    }
    
    private String answerYesNoQuestion(String question, String target, List<String> possibleItems) {
        String lowerQ = question.toLowerCase();
        
        // Check for common question patterns
        if (lowerQ.contains("animal") && target.matches(".*(dog|cat|lion|tiger|bear|wolf|fox|eagle|shark|whale|dolphin|penguin|owl|hawk|snake|lizard|frog|turtle|crocodile|octopus).*")) {
            return "Yes!";
        }
        if (lowerQ.contains("food") || lowerQ.contains("eat")) {
            return target.matches(".*(pizza|burger|sushi|pasta|salad|sandwich|taco|burrito|steak|chicken|fish|rice|noodles|soup|bread|cake|ice cream|chocolate|cookie|donut|apple|banana|orange|grape|strawberry|watermelon|carrot|broccoli|potato|corn).*") ? "Yes!" : "No.";
        }
        if (lowerQ.contains("bigger than") || lowerQ.contains("larger than")) {
            // Simplified size comparison
            return random.nextBoolean() ? "Yes!" : "No.";
        }
        if (lowerQ.contains("color") || lowerQ.contains("colour")) {
            return "It has various colors!";
        }
        
        // Default random response with some logic
        boolean yes = random.nextDouble() < 0.6; // Slightly favor yes for engagement
        return yes ? "Yes!" : "No.";
    }
    
    private void narrowPossibilities(List<String> possibleItems, String question, boolean answer) {
        // Simplified narrowing - in a real implementation, this would use more sophisticated logic
        if (possibleItems.size() > 5 && random.nextBoolean()) {
            possibleItems.remove(random.nextInt(possibleItems.size()));
        }
    }
    
    // ==================== WOULD YOU RATHER ====================
    
    private String startWouldYouRather() {
        return getNextWYRQuestion();
    }
    
    private String getNextWYRQuestion() {
        WouldYouRatherQuestion q = wyrQuestions.get(random.nextInt(wyrQuestions.size()));
        
        GameSession session = activeGames.get("wyr");
        session.gameData.put("currentQuestion", q);
        
        return String.format("🤔 Would You Rather... 🤔\n\nA) %s\n\nOR\n\nB) %s\n\nReply with A or B, and tell me why!",
            q.optionA, q.optionB);
    }
    
    private String processWouldYouRather(String input) {
        String lower = input.toLowerCase().trim();
        GameSession session = activeGames.get("wyr");
        WouldYouRatherQuestion current = (WouldYouRatherQuestion) session.gameData.get("currentQuestion");
        
        if (current == null) {
            return getNextWYRQuestion();
        }
        
        String response = "";
        if (lower.startsWith("a") || lower.contains("first") || lower.contains(current.optionA.toLowerCase())) {
            response = String.format("You chose A) %s! ", current.optionA);
            session.score += 10;
        } else if (lower.startsWith("b") || lower.contains("second") || lower.contains(current.optionB.toLowerCase())) {
            response = String.format("You chose B) %s! ", current.optionB);
            session.score += 10;
        } else {
            return "Please choose A or B! Or type 'next' for a new question.";
        }
        
        response += "\n\n💭 " + current.discussionPrompt;
        response += "\n\nType 'next' for another question, or 'end' to stop playing.";
        
        return response;
    }
    
    // ==================== TRIVIA ====================
    
    private String startTrivia() {
        triviaScore = 0;
        triviaQuestionCount = 0;
        return getNextTriviaQuestion();
    }
    
    private String getNextTriviaQuestion() {
        if (triviaQuestionCount >= triviaQuestions.size()) {
            return String.format("🏆 Trivia Complete! 🏆\n\nFinal Score: %d/%d\n\nGreat job! Type 'play trivia' to play again!",
                triviaScore, triviaQuestions.size());
        }
        
        TriviaQuestion q = triviaQuestions.get(triviaQuestionCount);
        GameSession session = activeGames.get("trivia");
        session.gameData.put("currentQuestion", q);
        session.gameData.put("questionNumber", triviaQuestionCount + 1);
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("❓ Trivia Question %d/%d ❓\n\n", triviaQuestionCount + 1, triviaQuestions.size()));
        sb.append(String.format("Category: %s\n\n", q.category));
        sb.append(q.question).append("\n\n");
        
        for (int i = 0; i < q.options.length; i++) {
            sb.append(String.format("%d) %s\n", i + 1, q.options[i]));
        }
        
        sb.append("\nReply with the number of your answer!");
        return sb.toString();
    }
    
    private String processTrivia(String input) {
        String lower = input.toLowerCase().trim();
        GameSession session = activeGames.get("trivia");
        TriviaQuestion current = (TriviaQuestion) session.gameData.get("currentQuestion");
        
        if (lower.equals("next") || lower.equals("skip")) {
            triviaQuestionCount++;
            return getNextTriviaQuestion();
        }
        
        int answer;
        try {
            answer = Integer.parseInt(lower.replaceAll("[^0-9]", "")) - 1;
        } catch (Exception e) {
            return "Please reply with a number (1, 2, 3, or 4)!";
        }
        
        if (answer < 0 || answer >= current.options.length) {
            return "Please choose a valid option (1-4)!";
        }
        
        StringBuilder response = new StringBuilder();
        
        if (answer == current.correctAnswer) {
            triviaScore++;
            session.score = triviaScore * 10;
            response.append("✅ CORRECT! 🎉\n\n");
        } else {
            response.append(String.format("❌ Wrong! The correct answer was: %d) %s\n\n",
                current.correctAnswer + 1, current.options[current.correctAnswer]));
        }
        
        response.append(String.format("💡 Fun Fact: %s\n\n", current.funFact));
        response.append(String.format("Score: %d/%d\n\n", triviaScore, triviaQuestionCount + 1));
        
        triviaQuestionCount++;
        
        if (triviaQuestionCount < triviaQuestions.size()) {
            response.append("Type 'next' for the next question!");
        } else {
            response.append(String.format("🏆 Quiz Complete! Final Score: %d/%d\n\nType 'play trivia' to play again!",
                triviaScore, triviaQuestions.size()));
        }
        
        return response.toString();
    }
    
    // ==================== STORY BUILDER ====================
    
    private String startStoryBuilder() {
        String prompt = storyPrompts.get(random.nextInt(storyPrompts.size()));
        collaborativeStory = new StringBuilder(prompt + "\n\n");
        
        GameSession session = activeGames.get("story");
        session.gameData.put("wordCount", 0);
        
        return String.format("📖 Collaborative Story Builder! 📖\n\nLet's create a story together. I'll start:\n\n%s\n\nNow you add the next sentence or paragraph! Be creative and let's see where the story goes.\n\nType 'end story' when we're done, and I'll read back our masterpiece!",
            prompt);
    }
    
    private String processStoryBuilder(String input) {
        String lower = input.toLowerCase().trim();
        GameSession session = activeGames.get("story");
        
        if (lower.equals("end story") || lower.equals("finish") || lower.equals("done")) {
            return endStoryBuilder();
        }
        
        collaborativeStory.append(input).append("\n\n");
        
        int wordCount = (Integer) session.gameData.get("wordCount");
        String[] words = input.split("\\s+");
        wordCount += words.length;
        session.gameData.put("wordCount", wordCount);
        
        // Add a creative continuation occasionally
        if (random.nextDouble() < 0.3) {
            String[] continuations = {
                "Suddenly, something unexpected happened...",
                "Little did they know, everything was about to change...",
                "But that was only the beginning...",
                "The situation became more complicated when...",
                "Just then, a surprising twist occurred..."
            };
            String continuation = continuations[random.nextInt(continuations.length)];
            collaborativeStory.append(continuation).append("\n\n");
            
            return String.format("Great addition! 📖\n\n%s\n\nYour turn again! (Story so far: %d words)",
                continuation, wordCount);
        }
        
        return String.format("Excellent! The story grows... 📖\n\nYour turn again! (Story so far: %d words)\n\nType 'end story' when we're done.",
            wordCount);
    }
    
    private String endStoryBuilder() {
        GameSession session = activeGames.get("story");
        int wordCount = (Integer) session.gameData.get("wordCount");
        
        String story = collaborativeStory.toString();
        String[] sentences = story.split("[.!?]");
        session.score = sentences.length * 5;
        
        return String.format("📚 Our Complete Story! 📚\n\n%s\n\n📝 Story Stats:\n• %d words\n• %d sentences\n• Collaborative creativity score: %d\n\nGreat work creating this together!",
            story, wordCount, sentences.length, session.score);
    }
    
    // ==================== WORD ASSOCIATION ====================
    
    private String startWordAssociation() {
        usedWords.clear();
        associationScore = 0;
        
        String[] starters = {"ocean", "fire", "dream", "music", "journey", "star", "time", "heart"};
        lastWord = starters[random.nextInt(starters.length)];
        usedWords.add(lastWord);
        
        GameSession session = activeGames.get("word");
        session.gameData.put("chainLength", 1);
        
        return String.format("🔗 Word Association Game! 🔗\n\nI'll say a word, you say the first word that comes to mind.\nThen I'll respond with a word associated with yours.\n\nLet's see how long our chain can get!\n\nStarting word: **%s**\n\nWhat's your word?",
            lastWord);
    }
    
    private String processWordAssociation(String input) {
        String word = input.toLowerCase().trim().replaceAll("[^a-z]", "");
        
        if (word.isEmpty()) {
            return "Please give me a single word!";
        }
        
        if (usedWords.contains(word)) {
            return String.format("We already used '%s'! Try a different word.", word);
        }
        
        GameSession session = activeGames.get("word");
        int chainLength = (Integer) session.gameData.get("chainLength");
        
        usedWords.add(word);
        lastWord = word;
        chainLength++;
        associationScore += word.length();
        session.score = associationScore;
        session.gameData.put("chainLength", chainLength);
        
        // Generate response word
        String responseWord = generateAssociatedWord(word);
        usedWords.add(responseWord);
        lastWord = responseWord;
        chainLength++;
        session.gameData.put("chainLength", chainLength);
        
        return String.format("🔗 %s → **%s** 🔗\n\nChain length: %d words | Score: %d\n\nYour turn! (Type 'end' to stop)",
            word, responseWord, chainLength, associationScore);
    }
    
    private String generateAssociatedWord(String input) {
        // Simple association logic - in a real implementation, this would use a word database
        Map<String, List<String>> associations = new HashMap<>();
        associations.put("ocean", Arrays.asList("wave", "beach", "sail", "deep", "blue"));
        associations.put("fire", Arrays.asList("flame", "heat", "burn", "light", "warm"));
        associations.put("dream", Arrays.asList("sleep", "imagine", "wish", "night", "hope"));
        associations.put("music", Arrays.asList("song", "melody", "rhythm", "dance", "sound"));
        associations.put("journey", Arrays.asList("travel", "road", "adventure", "path", "trip"));
        associations.put("star", Arrays.asList("sky", "night", "shine", "space", "bright"));
        associations.put("time", Arrays.asList("clock", "moment", "past", "future", "now"));
        associations.put("heart", Arrays.asList("love", "beat", "red", "life", "emotion"));
        
        List<String> associated = associations.get(input);
        if (associated != null) {
            for (String word : associated) {
                if (!usedWords.contains(word)) {
                    return word;
                }
            }
        }
        
        // Fallback to random common words
        String[] fallbacks = {"life", "world", "mind", "soul", "spirit", "energy", "peace", "joy"};
        for (String word : fallbacks) {
            if (!usedWords.contains(word)) {
                return word;
            }
        }
        
        return "connection"; // Ultimate fallback
    }
    
    // ==================== UTILITY METHODS ====================
    
    public List<String> getAvailableGames() {
        return Arrays.asList(
            "20 Questions - Guess what I'm thinking",
            "Would You Rather - Make tough choices",
            "Trivia - Test your knowledge",
            "Story Builder - Create together",
            "Word Association - Chain words together"
        );
    }
    
    public String getGameInstructions(String gameType) {
        switch (gameType.toLowerCase()) {
            case "20questions":
            case "20 questions":
                return "20 Questions: I think of something, you ask yes/no questions to guess it. You have 20 questions!";
            case "wyr":
            case "would you rather":
                return "Would You Rather: I'll give you two options, you choose one and tell me why!";
            case "trivia":
                return "Trivia: Answer questions across various topics. Test your knowledge!";
            case "story":
            case "story builder":
                return "Story Builder: We take turns adding to a collaborative story. Be creative!";
            case "word":
            case "word association":
                return "Word Association: I say a word, you say what it makes you think of. Build a chain!";
            default:
                return "Unknown game. Available: 20 Questions, Would You Rather, Trivia, Story Builder, Word Association";
        }
    }
}
