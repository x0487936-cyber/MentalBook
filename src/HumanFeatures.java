import java.util.*;
import java.util.regex.*;

/**
 * HumanFeatures - Adds human-like characteristics to VirtualXander
 * Implements personality, memory, emotional intelligence, and interactive features
 */
public class HumanFeatures {
    
    // User memory storage
    private Map<String, UserPreferences> userMemories;
    private String currentUser;
    private Random random;
    
    // Personality components
    private List<String> anecdotes;
    private List<String> funFacts;
    private List<String> jokes;
    private List<String> opinions;
    private List<String> conversationalFillers;
    
    // Interactive games state
    private Map<String, GameState> activeGames;
    
    /**
     * User preferences stored for personalization
     */
    private static class UserPreferences {
        String name;
        List<String> interests;
        List<String> preferences;
        int conversationCount;
        long lastConversationTime;
        Map<String, String> customData;
        
        public UserPreferences() {
            this.interests = new ArrayList<>();
            this.preferences = new ArrayList<>();
            this.customData = new HashMap<>();
            this.conversationCount = 0;
            this.lastConversationTime = System.currentTimeMillis();
        }
    }
    
    /**
     * Game state for interactive features
     */
    private static class GameState {
        String gameType;
        Map<String, Object> state;
        int attempts;
        
        public GameState(String gameType) {
            this.gameType = gameType;
            this.state = new HashMap<>();
            this.attempts = 0;
        }
    }
    
    public HumanFeatures() {
        this.userMemories = new HashMap<>();
        this.random = new Random();
        this.activeGames = new HashMap<>();
        initializePersonalityComponents();
    }
    
    private void initializePersonalityComponents() {
        // Initialize anecdotes
        anecdotes = Arrays.asList(
            "You know, I once 'read' about someone who taught their dog to high-five. Made me wonder if dogs have their own social hierarchies!",
            "I came across a fact that octopuses have three hearts. Imagine having to coordinate that!",
            "Did you know that bananas are berries, but strawberries aren't? The world of botany is full of surprises!",
            "I learned that honey never spoils. Archaeologists have found 3000-year-old honey that was still good!",
            "There's a species of jellyfish that's immortal. Makes you think about what 'forever' really means!",
            "I read that cows have best friends and get stressed when separated. Animal friendships are so heartwarming!",
            "I discovered that sloths can hold their breath longer than dolphins can (up to 40 minutes vs 20). Who would've thought?",
            "Here's something fun: A group of flamingos is called a 'flamboyance'. Such a fitting name!",
            "I found out that butterflies taste with their feet. Imagine if you had to stand on your food to taste it!",
            "Did you know that a day on Venus is longer than its year? Time works differently across the universe!"
        );
        
        // Initialize fun facts
        funFacts = Arrays.asList(
            "The shortest war in history lasted only 38-45 minutes between Britain and Zanzibar in 1896.",
            "A group of owls is called a parliament. Imagine calling an owl meeting!",
            "The Eiffel Tower can be 15 cm taller during summer due to thermal expansion.",
            "Honey contains a natural preservative that never spoils.",
            "Bananas are berries, but strawberries aren't technically berries.",
            "The unicorn is the national animal of Scotland.",
            "A shrimp's heart is located in its head.",
            "The inventor of the frisbee was turned into a Frisbee when he died.",
            "Cleopatra lived closer to the time of the moon landing than to the construction of the Great Pyramid.",
            "Oxford University is older than the Aztec Empire."
        );
        
        // Initialize jokes
        jokes = Arrays.asList(
            "Why did the programmer quit? Because he didn't get arrays (a raise)!",
            "Why do Java developers wear glasses? Because they can't C#!",
            "I'd tell you a UDP joke, but you might not get it.",
            "Why did the scarecrow become a comedian? Because he was outstanding in his field!",
            "What do you call a fake noodle? An impasta!",
            "Why don't skeletons fight each other? They don't have the guts!",
            "What do you call a pony with a cough? A little horse!",
            "Why did the math book look sad? Because it had too many problems!",
            "What do you call a can opener that doesn't work? A can't opener!",
            "Why did the golfer bring an extra pair of pants? In case he got a hole in one!"
        );
        
        // Initialize opinions
        opinions = Arrays.asList(
            "I think rain has a special quality to it - it makes everything feel fresh and new.",
            "Coffee is one of those wonderful inventions that brings people together.",
            "Music has this amazing ability to change our mood instantly.",
            "I believe everyone should take time to watch a sunset occasionally.",
            "Books are like portals to other worlds - how magical is that?",
            "I think laughter really is the best medicine, don't you?",
            "There's something special about meeting someone who shares your interests.",
            "I believe trying new things keeps life interesting and fun.",
            "There's nothing quite like a good conversation with someone who truly listens.",
            "I think kindness, even in small ways, can make a big difference in someone's day."
        );
        
        // Initialize conversational fillers
        conversationalFillers = Arrays.asList(
            "You know, ",
            "Speaking of which, ",
            "That reminds me, ",
            "Oh, and ",
            "By the way, ",
            "Here's something interesting: ",
            "On a related note, ",
            "Fun fact: ",
            "I was just thinking, ",
            "If you ask me, "
        );
    }
    
    // ==================== USER MEMORY METHODS ====================
    
    /**
     * Set the current user for memory operations
     */
    public void setCurrentUser(String userId) {
        this.currentUser = userId;
        if (!userMemories.containsKey(userId)) {
            userMemories.put(userId, new UserPreferences());
        }
        userMemories.get(userId).conversationCount++;
    }
    
    /**
     * Remember user's name
     */
    public void rememberName(String name) {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            userMemories.get(currentUser).name = name;
        }
    }
    
    /**
     * Get user's remembered name
     */
    public String getUserName() {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            return userMemories.get(currentUser).name;
        }
        return null;
    }
    
    /**
     * Remember an interest mentioned by the user
     */
    public void rememberInterest(String interest) {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            UserPreferences prefs = userMemories.get(currentUser);
            if (!prefs.interests.contains(interest.toLowerCase())) {
                prefs.interests.add(interest.toLowerCase());
            }
        }
    }
    
    /**
     * Get user's remembered interests
     */
    public List<String> getUserInterests() {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            return userMemories.get(currentUser).interests;
        }
        return new ArrayList<>();
    }
    
    /**
     * Store custom data for user
     */
    public void setUserData(String key, String value) {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            userMemories.get(currentUser).customData.put(key, value);
        }
    }
    
    /**
     * Get custom data for user
     */
    public String getUserData(String key) {
        if (currentUser != null && userMemories.containsKey(currentUser)) {
            return userMemories.get(currentUser).customData.get(key);
        }
        return null;
    }
    
    // ==================== PERSONALITY METHODS ====================
    
    /**
     * Get a random anecdote to share
     */
    public String getAnecdote() {
        return anecdotes.get(random.nextInt(anecdotes.size()));
    }
    
    /**
     * Get a random fun fact
     */
    public String getFunFact() {
        return funFacts.get(random.nextInt(funFacts.size()));
    }
    
    /**
     * Get a random joke
     */
    public String getJoke() {
        return jokes.get(random.nextInt(jokes.size()));
    }
    
    /**
     * Get a random opinion
     */
    public String getOpinion() {
        return opinions.get(random.nextInt(opinions.size()));
    }
    
    /**
     * Get a conversational filler
     */
    public String getConversationalFiller() {
        return conversationalFillers.get(random.nextInt(conversationalFillers.size()));
    }
    
    /**
     * Generate a curiosity-driven question based on user interests
     */
    public String generateCuriosityQuestion(List<String> interests) {
        if (interests == null || interests.isEmpty()) {
            String[] genericQuestions = {
                "What made you smile today?",
                "Is there anything you've been wanting to try but haven't yet?",
                "What's something you've learned recently that surprised you?",
                "If you could travel anywhere right now, where would you go?",
                "What's a small thing that made your day better?"
            };
            return genericQuestions[random.nextInt(genericQuestions.length)];
        }
        
        Map<String, String[]> interestQuestions = new HashMap<>();
        interestQuestions.put("gaming", new String[]{
            "What's your favorite game memory?",
            "Do you have a game you've been wanting to try but haven't found the time for?",
            "What's the most memorable gaming achievement you've had?"
        });
        interestQuestions.put("reading", new String[]{
            "What's the last book that really made you think?",
            "Are you reading anything exciting right now?",
            "What's a book you recommend to everyone?"
        });
        interestQuestions.put("music", new String[]{
            "What's a song that always puts you in a good mood?",
            "Have you discovered any new music lately that you love?",
            "What's your go-to music when you need to relax?"
        });
        interestQuestions.put("sports", new String[]{
            "What's the most exciting sports moment you've witnessed?",
            "Do you have a favorite team or athlete you follow?",
            "What's a sport you wish you could try?"
        });
        interestQuestions.put("movies", new String[]{
            "What's a movie that never gets old for you?",
            "Have you seen anything lately that you really enjoyed?",
            "What's your favorite movie genre?"
        });
        
        for (String interest : interests) {
            if (interestQuestions.containsKey(interest)) {
                String[] questions = interestQuestions.get(interest);
                return questions[random.nextInt(questions.length)];
            }
        }
        
        // Generic question if no specific interest matched
        return "What's something new you've been interested in lately?";
    }
    
    // ==================== EMOTIONAL INTELLIGENCE METHODS ====================
    
    /**
     * Generate an empathic response based on emotion
     */
    public String getEmpathicResponse(String emotion) {
        if (emotion == null) return null;
        
        switch (emotion.toLowerCase()) {
            case "sad":
            case "sadness":
                return "I can hear that you're feeling down. Remember, it's okay to not feel okay sometimes. " +
                       "Would you like to share what's on your mind?";
                       
            case "stressed":
            case "anxious":
                return "It sounds like things are feeling overwhelming right now. " +
                       "Take a deep breath - I'm here with you. What's been weighing on you?";
                       
            case "angry":
            case "frustrated":
                return "I hear that you're frustrated. It's completely valid to feel that way. " +
                       "Sometimes it helps to talk about what's causing these feelings.";
                       
            case "lonely":
                return "I understand that feeling of loneliness. Just know that you're not alone - " +
                       "I'm here to chat with you anytime you need company.";
                       
            case "happy":
            case "excited":
                return "I love that you're feeling good! Positive energy is so wonderful. " +
                       "What's putting that smile on your face?";
                       
            case "tired":
                return "It sounds like you're exhausted. Make sure to take care of yourself - " +
                       "rest is so important. Is there anything I can do to help lighten your load?";
                       
            case "confused":
                return "It's okay to feel confused or uncertain. Sometimes things are complicated, " +
                       "and that's completely normal. Would it help to talk through what's unclear?";
                       
            default:
                return null;
        }
    }
    
    /**
     * Generate encouraging message based on context
     */
    public String getEncouragement(String context) {
        if (context == null) {
            context = "general";
        }
        
        Map<String, String[]> encouragementMap = new HashMap<>();
        
        encouragementMap.put("struggling", new String[]{
            "You're doing better than you think. Every step forward, no matter how small, counts.",
            "Remember, difficult times don't last forever. You have the strength to get through this.",
            "Be gentle with yourself. You're learning and growing, and that's a beautiful thing."
        });
        
        encouragementMap.put("learning", new String[]{
            "Making an effort to learn is already a win! Keep going - curiosity is a superpower.",
            "Every expert was once a beginner. You're on the right path!",
            "The fact that you're trying shows real courage. Keep it up!"
        });
        
        encouragementMap.put("creating", new String[]{
            "Creating something new is such a meaningful act. Your effort matters!",
            "The world needs your unique perspective and creativity. Keep going!",
            "Whatever you're working on, know that the process itself is valuable."
        });
        
        encouragementMap.put("social", new String[]{
            "Reaching out and connecting takes courage. You're doing great!",
            "Building relationships is one of the most important things we can do. Kudos to you!",
            "Your willingness to engage with others shows real strength."
        });
        
        encouragementMap.put("general", new String[]{
            "You're doing great! Keep being awesome.",
            "Remember to be kind to yourself - you deserve it.",
            "Every day is a new opportunity to grow and shine.",
            "You have more strength than you realize.",
            "Keep going - you're on the right track!"
        });
        
        String[] options = encouragementMap.getOrDefault(context.toLowerCase(), 
                                                         encouragementMap.get("general"));
        return options[random.nextInt(options.length)];
    }
    
    // ==================== NATURAL CONVERSATION METHODS ====================
    
    /**
     * Generate a topic transition
     */
    public String generateTopicTransition(String fromTopic, String toTopic) {
        if (fromTopic == null || fromTopic.isEmpty()) {
            return "Speaking of which, ";
        }
        
        String[] transitions = {
            "That reminds me of something - ",
            "On a related note, ",
            "Shifting gears a bit, ",
            "Speaking of which, ",
            "Speaking of " + fromTopic + ", have you ever considered ",
            "That makes me think about "
        };
        
        return transitions[random.nextInt(transitions.length)];
    }
    
    /**
     * Generate a follow-up question based on conversation context
     */
    public String generateFollowUp(String lastTopic, String userInput) {
        if (lastTopic == null || lastTopic.isEmpty()) {
            String[] generalFollowUps = {
                "What else is on your mind?",
                "Is there anything else you'd like to chat about?",
                "What's been occupying your thoughts lately?",
                "Anything else you'd like to explore?",
                "What would you like to talk about next?"
            };
            return generalFollowUps[random.nextInt(generalFollowUps.length)];
        }
        
        Map<String, String[]> followUpMap = new HashMap<>();
        
        followUpMap.put("gaming", new String[]{
            "What other games have you been enjoying lately?",
            "Is there a game you're really looking forward to?",
            "What's your favorite gaming memory?"
        });
        
        followUpMap.put("mental health", new String[]{
            "How are you feeling after talking about this?",
            "Is there anything else on your mind that you'd like to discuss?",
            "Would you like to talk about something lighter?"
        });
        
        followUpMap.put("homework", new String[]{
            "Is there anything else about your studies you'd like help with?",
            "How are you finding your workload lately?",
            "Anything else you're working on that I can help with?"
        });
        
        followUpMap.put("relationship", new String[]{
            "How are you feeling about everything we discussed?",
            "Is there anything else you'd like to share about relationships?",
            "Would you like to talk about something else for a change?"
        });
        
        followUpMap.put("creative writing", new String[]{
            "How's your writing project coming along?",
            "What other creative ideas do you have?",
            "Would you like to explore more creative topics?"
        });
        
        String[] followUps = followUpMap.getOrDefault(lastTopic.toLowerCase(), 
                                                       followUpMap.get("general"));
        return followUps[random.nextInt(followUps.length)];
    }
    
    /**
     * Get response with variable length (sometimes short, sometimes long)
     */
    public String getVariableLengthResponse(String baseResponse, boolean shouldBeLong) {
        if (shouldBeLong) {
            String[] longAddons = {
                " I'm really interested in hearing more about what you think.",
                " I'd love to understand more about your perspective on this.",
                " This is the kind of conversation I really enjoy having.",
                " Feel free to share as much or as little as you'd like."
            };
            return baseResponse + longAddons[random.nextInt(longAddons.length)];
        } else {
            String[] shortAddons = {
                " :)",
                " - thanks for sharing!",
                " I appreciate that.",
                " Got it!"
            };
            return baseResponse + shortAddons[random.nextInt(shortAddons.length)];
        }
    }
    
    // ==================== INTERACTIVE GAMES METHODS ====================
    
    /**
     * Start a simple game
     */
    public String startGame(String gameType, String userId) {
        String gameId = userId + "_" + gameType;
        GameState state = new GameState(gameType);
        activeGames.put(gameId, state);
        
        switch (gameType.toLowerCase()) {
            case "20questions":
                state.state.put("answer", getRandomMysteryAnswer());
                state.state.put("clues", 0);
                state.state.put("guesses", new ArrayList<String>());
                return "Let's play 20 Questions! I'm thinking of something. " +
                       "You can ask yes/no questions to figure it out. " +
                       "What would you like to know?";
                       
            case "wordgame":
                state.state.put("targetWord", generateWordGameWord());
                state.state.put("attempts", 0);
                state.state.put("hintsUsed", 0);
                return "Let's play a word game! I'm thinking of a word. " +
                       "You can guess letters to figure it out. " +
                       "What's your first guess?";
                       
            case "trivia":
                state.state.put("question", getRandomTriviaQuestion());
                state.state.put("answered", false);
                return "Let's play trivia! " + getRandomTriviaQuestion();
                       
            default:
                return "I'm not sure how to play that game yet. " +
                       "I can play 20 Questions, Word Game, or Trivia!";
        }
    }
    
    /**
     * Process game input
     */
    public String processGameInput(String gameType, String input, String userId) {
        String gameId = userId + "_" + gameType;
        GameState state = activeGames.get(gameId);
        
        if (state == null) {
            return "We don't have an active game. Say 'start game' to begin!";
        }
        
        switch (gameType.toLowerCase()) {
            case "20questions":
                return processTwentyQuestions(state, input);
                       
            case "wordgame":
                return processWordGame(state, input);
                       
            case "trivia":
                return processTrivia(state, input);
                       
            default:
                return "I'm not sure how to play that game.";
        }
    }
    
    private String getRandomMysteryAnswer() {
        String[] answers = {
            "a cat", "a book", "a tree", "a phone", "a car", 
            "a coffee", "a song", "a dream", "a robot", "a cloud"
        };
        return answers[random.nextInt(answers.length)];
    }
    
    private String processTwentyQuestions(GameState state, String input) {
        state.attempts++;
        String answer = (String) state.state.get("answer");
        String lowerInput = input.toLowerCase();
        
        // Check if user is guessing
        if (lowerInput.startsWith("is it ") || lowerInput.startsWith("are you ") || 
            lowerInput.startsWith("is a ") || lowerInput.startsWith("its ")) {
            @SuppressWarnings("unchecked")
            List<String> guesses = (List<String>) state.state.get("guesses");
            guesses.add(input);
            state.state.put("guesses", guesses);
            
            if (lowerInput.contains(answer.toLowerCase())) {
                activeGames.remove("userId_20questions");
                return "That's it! You got it! Great job! Want to play again?";
            } else if (state.attempts >= 20) {
                activeGames.remove("userId_20questions");
                return "Out of questions! I was thinking of " + answer + ". Want to try again?";
            } else {
                int remaining = 20 - state.attempts;
                return "Not quite! You have " + remaining + " questions left. What else would you like to ask?";
            }
        }
        
        // Provide a clue if they're stuck
        if (state.attempts % 5 == 0) {
            String[] clues = {
                "Hint: It's something you might find at home.",
                "Hint: It's a noun.",
                "Hint: It's something that exists in the physical world.",
                "Hint: It's not a person."
            };
            state.state.put("clues", (Integer) state.state.get("clues") + 1);
            return clues[random.nextInt(clues.length)] + " What else would you like to know?";
        }
        
        return "I'm not sure if I can answer that with yes or no. Try asking a different question!";
    }
    
    private String generateWordGameWord() {
        String[] words = {"apple", "brain", "cloud", "dream", "earth", "flame", "ghost", "heart", "light", "music"};
        return words[random.nextInt(words.length)];
    }
    
    private String processWordGame(GameState state, String input) {
        state.attempts++;
        String target = (String) state.state.get("targetWord");
        String guess = input.toLowerCase().trim();
        
        if (guess.length() == 1) {
            // Letter guess
            if (target.contains(guess)) {
                return "Great! '" + guess + "' is in the word. Keep going!";
            } else {
                int remaining = 6 - state.attempts;
                return "Sorry, no '" + guess + "' in my word. " + remaining + " guesses left.";
            }
        } else if (guess.length() == target.length()) {
            // Full word guess
            if (guess.equals(target)) {
                activeGames.remove("userId_wordgame");
                return "You got it! The word was " + target + "! Want to play again?";
            } else {
                int remaining = 6 - state.attempts;
                if (remaining <= 0) {
                    activeGames.remove("userId_wordgame");
                    return "Out of guesses! The word was " + target + ". Want to try again?";
                }
                return "That's not it. " + remaining + " guesses left. Try another letter!";
            }
        }
        
        return "Please guess a single letter or the full word.";
    }
    
    private String getRandomTriviaQuestion() {
        String[] questions = {
            "What is the capital of France? (A) London (B) Paris (C) Berlin (D) Madrid",
            "What is 5 + 7? (A) 10 (B) 12 (C) 14 (D) 15",
            "Which planet is known as the Red Planet? (A) Venus (B) Mars (C) Jupiter (D) Saturn",
            "What is the largest ocean on Earth? (A) Atlantic (B) Indian (C) Arctic (D) Pacific",
            "Who wrote 'Romeo and Juliet'? (A) Dickens (B) Shakespeare (C) Austen (D) Hemingway"
        };
        return questions[random.nextInt(questions.length)];
    }
    
    private String processTrivia(GameState state, String input) {
        @SuppressWarnings("unchecked")
        Boolean answered = (Boolean) state.state.get("answered");
        
        if (answered) {
            return "We already answered that question! Say 'trivia' for a new one.";
        }
        
        String lowerInput = input.toLowerCase();
        if (lowerInput.matches(".*[abcd].*")) {
            state.state.put("answered", true);
            if (lowerInput.contains("b") && state.state.get("question").toString().contains("Paris")) {
                return "Correct! You're a trivia star! Want another question?";
            } else if (lowerInput.contains("b") && state.state.get("question").toString().contains("12")) {
                return "Correct! You're a trivia star! Want another question?";
            } else if (lowerInput.contains("b") && state.state.get("question").toString().contains("Mars")) {
                return "Correct! You're a trivia star! Want another question?";
            } else if (lowerInput.contains("d") && state.state.get("question").toString().contains("Pacific")) {
                return "Correct! You're a trivia star! Want another question?";
            } else if (lowerInput.contains("b") && state.state.get("question").toString().contains("Shakespeare")) {
                return "Correct! You're a trivia star! Want another question?";
            } else {
                return "Not quite! The answer was B. Say 'trivia' for a new question!";
            }
        }
        
        return "Just answer with A, B, C, or D!";
    }
    
    // ==================== SELF-AWARENESS METHODS ====================
    
    /**
     * Get a natural acknowledgment of limitations
     */
    public String getLimitationAcknowledgment(String context) {
        Map<String, String[]> acknowledgments = new HashMap<>();
        
        acknowledgments.put("complex", new String[]{
            "That's a really deep question. I don't have a perfect answer, but I appreciate you asking!",
            "Hmm, that's beyond my usual scope. What do you think about it?",
            "That's something I can't fully answer, but I'd love to hear your thoughts!"
        });
        
        acknowledgments.put("unknown", new String[]{
            "I'm not sure about that one. What do you think?",
            "I don't have information on that. Got any insights to share?",
            "That's outside my knowledge. I'd be curious to hear what you know about it!"
        });
        
        acknowledgments.put("opinion", new String[]{
            "That's a great question for a human perspective. What's your take?",
            "I can share information, but for personal opinions, I'd say that's really up to you!",
            "I don't have personal experiences to draw from, but this sounds like something meaningful!"
        });
        
        acknowledgments.put("general", new String[]{
            "That's a good question! I'm always learning, but I don't have all the answers.",
            "I appreciate the question, even if I can't answer it perfectly!",
            "What an interesting thing to wonder about! I'd love to hear your perspective."
        });
        
        String[] options = acknowledgments.getOrDefault(context.toLowerCase(), 
                                                         acknowledgments.get("general"));
        return options[random.nextInt(options.length)];
    }
    
    /**
     * Express honest uncertainty
     */
    public String expressUncertainty() {
        String[] uncertainties = {
            "I want to be honest with you - I'm not entirely sure about that.",
            "That's something I don't have a clear answer for.",
            "I think I'd need more information to give you a good answer there.",
            "You know, I'm not certain about that. What do you think?",
            "That's a good question that I don't have a complete answer to."
        };
        return uncertainties[random.nextInt(uncertainties.length)];
    }
    
    /**
     * Acknowledge learning from conversation
     */
    public String acknowledgeLearning() {
        String[] learning = {
            "Conversations like this help me understand better. Thanks for sharing your thoughts!",
            "I appreciate you explaining that - it helps me learn!",
            "That's interesting! I love learning new things from our chats.",
            "Thanks for that perspective - it gives me more to think about!"
        };
        return learning[random.nextInt(learning.length)];
    }
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Check if input contains name-related patterns
     */
    public boolean isAskingAboutName(String input) {
        String lower = input.toLowerCase();
        return lower.contains("your name") || 
               lower.contains("who are you") ||
               lower.contains("what are you");
    }
    
    /**
     * Check if input is asking for personal opinion
     */
    public boolean isAskingForOpinion(String input) {
        String lower = input.toLowerCase();
        return lower.contains("what do you think") ||
               lower.contains("do you think") ||
               lower.contains("your opinion") ||
               lower.contains("how do you feel about");
    }
    
    /**
     * Check if input is asking for a joke
     */
    public boolean isAskingForJoke(String input) {
        String lower = input.toLowerCase();
        return lower.contains("joke") ||
               lower.contains("tell me something funny") ||
               lower.contains("make me laugh") ||
               lower.contains("funny");
    }
    
    /**
     * Check if input is asking for a fun fact
     */
    public boolean isAskingForFact(String input) {
        String lower = input.toLowerCase();
        return lower.contains("fun fact") ||
               lower.contains("did you know") ||
               lower.contains("tell me something interesting") ||
               lower.contains("interesting fact");
    }
    
    /**
     * Check if input is starting a game
     */
    public boolean isStartingGame(String input) {
        String lower = input.toLowerCase();
        return lower.contains("play game") ||
               lower.contains("let's play") ||
               lower.contains("start game") ||
               lower.contains("20 questions") ||
               lower.contains("trivia");
    }
}
