import java.util.*;
import java.time.*;

/**
 * GrowthPartner - A class that helps users with their personal growth journey,
 * provides guidance, tracks progress, and offers supportive interactions.
 * 
 * This module works alongside other mental health components to support
 * continuous personal development.
 * 
 * Features:
 * - Goal Clarification: Help users define SMART goals and break them into actionable steps
 * - Progress Tracking: Detailed tracking with metrics, milestones, and visual feedback
 * - Accountability: Check-ins, reminders, commitments, and progress sharing
 * - Learning Companion: Knowledge exploration, skill encouragement, and curiosity feeding
 * - Challenge Helper: Problem tackling, obstacle navigation, and achievement celebration
 */
public class GrowthPartner {
    
    // Personal growth tracking
    private List<GrowthGoal> activeGoals;
    private List<GrowthMilestone> milestones;
    private Map<String, GrowthArea> growthAreas;
    private List<GrowthReflection> reflections;
    
    // User engagement
    private LocalDate lastInteraction;
    private int totalSessions;
    private Map<LocalDate, DailyGrowth> dailyProgress;
    
    // Goal Support - Accountability
    private List<AccountabilityCheckIn> checkIns;
    private List<Commitment> commitments;
    private LocalDateTime nextReminder;
    private boolean accountabilityEnabled;
    
    // Goal Support - Progress tracking metrics
    private Map<String, List<ProgressEntry>> progressHistory;
    private Map<String, Double> goalCompletionRates;
    
    // Learning Companion - Knowledge Exploration
    private List<KnowledgeTopic> explorationTopics;
    private Map<String, List<String>> topicConnections;
    private List<LearningPath> learningPaths;
    private List<CuriosityQuestion> curiosityQueue;
    
    // Learning Companion - Skill Encouragement
    private Map<String, SkillProgress> skillProgressMap;
    private List<SkillChallenge> activeChallenges;
    private List<String> encouragingMessages;
    
    // Learning Companion - Curiosity Feeding
    private List<WonderPrompt> wonderPrompts;
    private Map<LocalDate, List<String>> dailyDiscoveries;
    private int curiosityLevel;
    
    // Challenge Helper - Problem Tackling
    private List<Challenge> activeChallengesList;
    private Map<String, List<SolutionAttempt>> solutionAttempts;
    private List<String> problemSolvingStrategies;
    
    // Challenge Helper - Obstacle Navigation
    private Map<String, List<Obstacle>> obstaclesByChallenge;
    private Map<String, String> obstacleSolutions;
    
    // Challenge Helper - Achievement Celebration
    private List<Achievement> achievements;
    private List<String> celebrationMessages;
    private int totalCelebrations;
    
    // Growth partner personality
    private String personalityType;
    private List<String> encouragingPhrases;
    private List<GrowthStrategy> strategies;
    
    public GrowthPartner() {
        this.activeGoals = new ArrayList<>();
        this.milestones = new ArrayList<>();
        this.growthAreas = new HashMap<>();
        this.reflections = new ArrayList<>();
        this.dailyProgress = new HashMap<>();
        this.totalSessions = 0;
        this.lastInteraction = LocalDate.now();
        this.personalityType = "supportive";
        
        // Initialize Goal Support features
        this.checkIns = new ArrayList<>();
        this.commitments = new ArrayList<>();
        this.accountabilityEnabled = true;
        this.nextReminder = LocalDateTime.now().plusDays(1);
        this.progressHistory = new HashMap<>();
        this.goalCompletionRates = new HashMap<>();
        
        // Initialize Learning Companion features
        this.explorationTopics = new ArrayList<>();
        this.topicConnections = new HashMap<>();
        this.learningPaths = new ArrayList<>();
        this.curiosityQueue = new ArrayList<>();
        this.skillProgressMap = new HashMap<>();
        this.activeChallenges = new ArrayList<>();
        this.wonderPrompts = new ArrayList<>();
        this.dailyDiscoveries = new HashMap<>();
        this.curiosityLevel = 5; // Medium curiosity level (1-10)
        
        // Initialize Challenge Helper features
        this.activeChallengesList = new ArrayList<>();
        this.solutionAttempts = new HashMap<>();
        this.problemSolvingStrategies = new ArrayList<>();
        this.obstaclesByChallenge = new HashMap<>();
        this.obstacleSolutions = new HashMap<>();
        this.achievements = new ArrayList<>();
        this.celebrationMessages = new ArrayList<>();
        this.totalCelebrations = 0;
        
        initializeEncouragingPhrases();
        initializeStrategies();
        initializeGrowthAreas();
        initializeWonderPrompts();
        initializeEncouragingMessages();
        initializeProblemSolvingStrategies();
        initializeCelebrationMessages();
    }
    
    private void initializeEncouragingPhrases() {
        encouragingPhrases = Arrays.asList(
            "Every step forward is progress worth celebrating!",
            "You're capable of more than you know.",
            "Growth happens outside your comfort zone.",
            "Be patient with yourself - change is a journey.",
            "Your efforts are noticed and appreciated.",
            "Small progress is still progress.",
            "You've overcome challenges before, and you can do it again.",
            "Your potential is limitless.",
            "Every experience teaches us something valuable.",
            "Believe in your ability to grow and improve."
        );
    }
    
    private void initializeStrategies() {
        strategies = new ArrayList<>();
        strategies.add(new GrowthStrategy("small_wins", "Focus on small victories", 1));
        strategies.add(new GrowthStrategy("mindful_progress", "Practice mindful awareness", 2));
        strategies.add(new GrowthStrategy("reflective_learning", "Learn from reflections", 3));
        strategies.add(new GrowthStrategy("goal_breaking", "Break large goals into smaller steps", 4));
        strategies.add(new GrowthStrategy("positive_self_talk", "Cultivate positive inner dialogue", 5));
    }
    
    private void initializeGrowthAreas() {
        growthAreas.put("emotional", new GrowthArea("Emotional Intelligence", "Understanding and managing emotions"));
        growthAreas.put("mental", new GrowthArea("Mental Clarity", "Developing focus and cognitive clarity"));
        growthAreas.put("social", new GrowthArea("Social Growth", "Building meaningful relationships"));
        growthAreas.put("physical", new GrowthArea("Physical Wellness", "Caring for physical health"));
        growthAreas.put("spiritual", new GrowthArea("Spiritual Growth", "Finding purpose and meaning"));
    }
    
    private void initializeWonderPrompts() {
        wonderPrompts.add(new WonderPrompt("What if you could learn one new thing today?", "curiosity", 8));
        wonderPrompts.add(new WonderPrompt("Have you ever wondered how things work?", "exploration", 6));
        wonderPrompts.add(new WonderPrompt("What topic have you always wanted to explore?", "interest", 7));
        wonderPrompts.add(new WonderPrompt("Every expert was once a beginner. What's your next skill?", "motivation", 9));
        wonderPrompts.add(new WonderPrompt("What connection could you discover today?", "connection", 5));
        wonderPrompts.add(new WonderPrompt("What would you do if you couldn't fail?", "imagination", 7));
        wonderPrompts.add(new WonderPrompt("What simple thing amazes you when you think about it?", "wonder", 6));
        wonderPrompts.add(new WonderPrompt("What's one question you've been curious about?", "curiosity", 8));
    }
    
    private void initializeEncouragingMessages() {
        encouragingMessages = Arrays.asList(
            "You're making great progress! Keep exploring!",
            "Every skill you develop opens new doors.",
            "Your curiosity is a gift - use it!",
            "Learning is a journey, not a destination.",
            "You've got this! One step at a time.",
            "Your potential to learn is unlimited!",
            "Each new thing you learn adds to your wisdom.",
            "The more you learn, the more you grow!",
            "Challenge yourself - you can do it!",
            "Learning something new today? That's wonderful!"
        );
    }
    
    private void initializeProblemSolvingStrategies() {
        problemSolvingStrategies = Arrays.asList(
            "Break the problem into smaller parts",
            "Look for patterns or similarities to past problems",
            "Try working backwards from the desired outcome",
            "Brainstorm multiple solutions without judgment",
            "Take a break and return with fresh eyes",
            "Ask for help or different perspectives",
            "Simplify the problem first",
            "Use visualization or diagrams",
            "Consider the opposite approach from similar challenges others",
            "Learn have faced"
        );
    }
    
    private void initializeCelebrationMessages() {
        celebrationMessages = Arrays.asList(
            "🎉 Amazing achievement! You did it!",
            "🌟 You're incredible! Keep shining!",
            "💪 Hard work pays off! Congratulations!",
            "⭐ This is a major milestone - celebrate it!",
            "🏆 You've earned this victory!",
            "👏 Bravo! Your dedication is inspiring!",
            "✨ Magic happens when you persist!",
            "🎊 Your perseverance paid off!",
            "🌈 Success looks good on you!",
            "🔥 You're on fire! Keep the momentum!"
        );
    }
    
    // ==================== CHALLENGE HELPER - PROBLEM TACKLING ====================
    
    /**
     * Problem Tackling: Create a new challenge
     */
    public Challenge createChallenge(String title, String description, String difficulty) {
        Challenge challenge = new Challenge(title, description, difficulty);
        activeChallengesList.add(challenge);
        solutionAttempts.put(title, new ArrayList<>());
        obstaclesByChallenge.put(title, new ArrayList<>());
        return challenge;
    }
    
    /**
     * Problem Tackling: Add a solution attempt
     */
    public void addSolutionAttempt(String challengeTitle, String approach, boolean successful) {
        SolutionAttempt attempt = new SolutionAttempt(approach, successful);
        solutionAttempts.computeIfAbsent(challengeTitle, k -> new ArrayList<>()).add(attempt);
    }
    
    /**
     * Problem Tackling: Get problem solving strategies
     */
    public List<String> getProblemSolvingStrategies() {
        return new ArrayList<>(problemSolvingStrategies);
    }
    
    /**
     * Problem Tackling: Get a random strategy suggestion
     */
    public String getStrategySuggestion() {
        Random random = new Random();
        return problemSolvingStrategies.get(random.nextInt(problemSolvingStrategies.size()));
    }
    
    /**
     * Problem Tackling: Get active challenges
     */
    public List<Challenge> getActiveChallenges() {
        return new ArrayList<>(activeChallengesList);
    }
    
    /**
     * Problem Tackling: Get solution attempts for a challenge
     */
    public List<SolutionAttempt> getSolutionAttempts(String challengeTitle) {
        return solutionAttempts.getOrDefault(challengeTitle, new ArrayList<>());
    }
    
    /**
     * Problem Tackling: Mark challenge as completed
     */
    public void completeChallenge(String challengeTitle) {
        for (Challenge challenge : activeChallengesList) {
            if (challenge.getTitle().equals(challengeTitle)) {
                challenge.markComplete();
                activeChallengesList.remove(challenge);
                break;
            }
        }
    }
    
    // ==================== CHALLENGE HELPER - OBSTACLE NAVIGATION ====================
    
    /**
     * Obstacle Navigation: Add an obstacle to a challenge
     */
    public void addObstacle(String challengeTitle, String obstacle, String suggestedSolution) {
        Obstacle obs = new Obstacle(obstacle, suggestedSolution);
        obstaclesByChallenge.computeIfAbsent(challengeTitle, k -> new ArrayList<>()).add(obs);
        obstacleSolutions.put(obstacle, suggestedSolution);
    }
    
    /**
     * Obstacle Navigation: Get obstacles for a challenge
     */
    public List<Obstacle> getObstacles(String challengeTitle) {
        return obstaclesByChallenge.getOrDefault(challengeTitle, new ArrayList<>());
    }
    
    /**
     * Obstacle Navigation: Get solution for an obstacle
     */
    public String getSolutionForObstacle(String obstacle) {
        return obstacleSolutions.getOrDefault(obstacle, "Keep pushing forward - you can overcome this!");
    }
    
    /**
     * Obstacle Navigation: Get overcoming tips
     */
    public List<String> getOvercomingTips() {
        return Arrays.asList(
            "Take one small step at a time",
            "Remember why you started",
            "Focus on what you can control",
            "Break it down into manageable pieces",
            "Seek support when needed",
            "Learn from the obstacle",
            "Stay flexible and adapt",
            "Keep your eye on the goal"
        );
    }
    
    // ==================== CHALLENGE HELPER - ACHIEVEMENT CELEBRATION ====================
    
    /**
     * Achievement Celebration: Create an achievement
     */
    public Achievement createAchievement(String title, String description, String category) {
        Achievement achievement = new Achievement(title, description, category);
        achievements.add(achievement);
        return achievement;
    }
    
    /**
     * Achievement Celebration: Get celebration message
     */
    public String getCelebrationMessage() {
        Random random = new Random();
        totalCelebrations++;
        return celebrationMessages.get(random.nextInt(celebrationMessages.size()));
    }
    
    /**
     * Achievement Celebration: Get total celebrations count
     */
    public int getTotalCelebrations() {
        return totalCelebrations;
    }
    
    /**
     * Achievement Celebration: Get all achievements
     */
    public List<Achievement> getAchievements() {
        return new ArrayList<>(achievements);
    }
    
    /**
     * Achievement Celebration: Generate achievement report
     */
    public String getAchievementReport() {
        if (achievements.isEmpty()) {
            return "No achievements yet. Keep working on your challenges!";
        }
        return String.format("You have %d achievements! %s", 
            achievements.size(), getCelebrationMessage());
    }
    
    // ==================== LEARNING COMPANION - KNOWLEDGE EXPLORATION ====================
    
    /**
     * Knowledge Exploration: Add a new topic to explore
     */
    public void addKnowledgeTopic(String topicName, String description, List<String> subtopics) {
        KnowledgeTopic topic = new KnowledgeTopic(topicName, description, subtopics);
        explorationTopics.add(topic);
        
        // Create connections to existing topics
        for (KnowledgeTopic existing : explorationTopics) {
            if (!existing.getName().equals(topicName) && hasConnection(existing.getSubtopics(), subtopics)) {
                topicConnections.computeIfAbsent(topicName, k -> new ArrayList<>()).add(existing.getName());
                topicConnections.computeIfAbsent(existing.getName(), k -> new ArrayList<>()).add(topicName);
            }
        }
    }
    
    private boolean hasConnection(List<String> subtopics1, List<String> subtopics2) {
        for (String s1 : subtopics1) {
            for (String s2 : subtopics2) {
                if (s1.equalsIgnoreCase(s2)) return true;
            }
        }
        return false;
    }
    
    /**
     * Knowledge Exploration: Get connected topics
     */
    public List<String> getConnectedTopics(String topicName) {
        return topicConnections.getOrDefault(topicName, new ArrayList<>());
    }
    
    /**
     * Knowledge Exploration: Create a learning path
     */
    public LearningPath createLearningPath(String goal, List<String> topics) {
        LearningPath path = new LearningPath(goal, topics);
        learningPaths.add(path);
        return path;
    }
    
    /**
     * Knowledge Exploration: Get suggested next topic based on current learning
     */
    public String getSuggestedNextTopic() {
        if (explorationTopics.isEmpty()) {
            return "Start by exploring a topic that interests you!";
        }
        Random random = new Random();
        KnowledgeTopic randomTopic = explorationTopics.get(random.nextInt(explorationTopics.size()));
        return "How about exploring: " + randomTopic.getName() + "?";
    }
    
    /**
     * Knowledge Exploration: Get all available topics
     */
    public List<KnowledgeTopic> getExplorationTopics() {
        return new ArrayList<>(explorationTopics);
    }
    
    // ==================== LEARNING COMPANION - SKILL ENCOURAGEMENT ====================
    
    /**
     * Skill Encouragement: Start tracking a new skill
     */
    public void startSkill(String skillName, String description) {
        skillProgressMap.put(skillName, new SkillProgress(skillName, description));
    }
    
    /**
     * Skill Encouragement: Update skill progress
     */
    public void updateSkillProgress(String skillName, double progressPercent, String activity) {
        if (skillProgressMap.containsKey(skillName)) {
            skillProgressMap.get(skillName).addProgress(progressPercent, activity);
        }
    }
    
    /**
     * Skill Encouragement: Get skill encouragement message
     */
    public String getSkillEncouragement(String skillName) {
        if (!skillProgressMap.containsKey(skillName)) {
            return "Start learning " + skillName + " today!";
        }
        
        SkillProgress skill = skillProgressMap.get(skillName);
        double progress = skill.getProgressPercent();
        
        if (progress < 25) {
            return "You're just getting started with " + skillName + "! Every expert was once a beginner.";
        } else if (progress < 50) {
            return "Great progress on " + skillName + "! You're building a solid foundation.";
        } else if (progress < 75) {
            return "Impressive! You're over halfway there with " + skillName + "!";
        } else if (progress < 100) {
            return "Almost there! " + skillName + " is within your reach!";
        } else {
            return "Congratulations! You've mastered " + skillName + "!";
        }
    }
    
    /**
     * Skill Encouragement: Create a skill challenge
     */
    public SkillChallenge createSkillChallenge(String skillName, String challenge, int days) {
        SkillChallenge challengeObj = new SkillChallenge(skillName, challenge, days);
        activeChallenges.add(challengeObj);
        return challengeObj;
    }
    
    /**
     * Skill Encouragement: Get all skill progress
     */
    public Map<String, SkillProgress> getAllSkillProgress() {
        return new HashMap<>(skillProgressMap);
    }
    
    /**
     * Skill Encouragement: Get encouraging message
     */
    public String getEncouragingMessage() {
        Random random = new Random();
        return encouragingMessages.get(random.nextInt(encouragingMessages.size()));
    }
    
    // ==================== LEARNING COMPANION - CURIOSITY FEEDING ====================
    
    /**
     * Curiosity Feeding: Get a wonder prompt to spark curiosity
     */
    public WonderPrompt getWonderPrompt() {
        if (wonderPrompts.isEmpty()) {
            return new WonderPrompt("What will you discover today?", "curiosity", 5);
        }
        Random random = new Random();
        return wonderPrompts.get(random.nextInt(wonderPrompts.size()));
    }
    
    /**
     * Curiosity Feeding: Record a discovery
     */
    public void recordDiscovery(String discovery) {
        LocalDate today = LocalDate.now();
        dailyDiscoveries.computeIfAbsent(today, k -> new ArrayList<>()).add(discovery);
        
        // Increase curiosity level with each discovery
        if (curiosityLevel < 10) {
            curiosityLevel++;
        }
    }
    
    /**
     * Curiosity Feeding: Get today's discoveries
     */
    public List<String> getTodayDiscoveries() {
        return dailyDiscoveries.getOrDefault(LocalDate.now(), new ArrayList<>());
    }
    
    /**
     * Curiosity Feeding: Get curiosity level
     */
    public int getCuriosityLevel() {
        return curiosityLevel;
    }
    
    /**
     * Curiosity Feeding: Generate new curiosity questions
     */
    public List<CuriosityQuestion> generateCuriosityQuestions(String topic) {
        List<CuriosityQuestion> questions = new ArrayList<>();
        questions.add(new CuriosityQuestion("Why does " + topic + " work that way?", topic));
        questions.add(new CuriosityQuestion("What would happen if " + topic + " didn't exist?", topic));
        questions.add(new CuriosityQuestion("How can " + topic + " be applied in daily life?", topic));
        questions.add(new CuriosityQuestion("What's the history behind " + topic + "?", topic));
        questions.add(new CuriosityQuestion("What future developments might affect " + topic + "?", topic));
        return questions;
    }
    
    /**
     * Curiosity Feeding: Get learning recommendation based on curiosity
     */
    public String getLearningRecommendation() {
        if (curiosityLevel <= 3) {
            return "Let's start with something simple and build from there.";
        } else if (curiosityLevel <= 6) {
            return "You're ready for some intermediate topics!";
        } else if (curiosityLevel <= 8) {
            return "Your curiosity is thriving! Let's explore deep concepts.";
        } else {
            return "You're a true learner! Let's tackle advanced and interdisciplinary topics.";
        }
    }
    
    // ==================== GOAL CLARIFICATION ====================
    
    /**
     * Goal Clarification: Help users define a SMART goal
     * Returns questions to help clarify the goal
     */
    public List<String> getGoalClarificationQuestions() {
        return Arrays.asList(
            "What specifically do you want to achieve?",
            "How will you measure success?",
            "What is your target deadline?",
            "What resources or support do you need?",
            "What obstacles might you face and how will you overcome them?",
            "Why is this goal important to you?",
            "Who can support you in achieving this goal?",
            "What first step can you take today?"
        );
    }
    
    /**
     * Goal Clarification: Create a SMART goal with all components
     */
    public GrowthGoal createSmartGoal(String title, String specificOutcome, 
            String measurableMetric, LocalDate deadline, List<String> resources,
            List<String> obstacles, String motivation, String firstStep) {
        
        String description = String.format(
            "Goal: %s | Specific: %s | Measurable: %s | Deadline: %s | " +
            "Resources: %s | Obstacles: %s | Motivation: %s | First Step: %s",
            title, specificOutcome, measurableMetric, deadline.toString(),
            String.join(", ", resources), String.join(", ", obstacles),
            motivation, firstStep
        );
        
        Duration duration = Duration.between(LocalDate.now(), deadline);
        GrowthGoal goal = new GrowthGoal(title, description, "smart", duration);
        goal.setSpecificOutcome(specificOutcome);
        goal.setMeasurableMetric(measurableMetric);
        goal.setDeadline(deadline);
        goal.setFirstStep(firstStep);
        goal.setSmart(true);
        
        activeGoals.add(goal);
        progressHistory.put(title, new ArrayList<>());
        
        return goal;
    }
    
    /**
     * Goal Clarification: Break a large goal into smaller actionable steps
     */
    public List<GoalStep> breakIntoSteps(String goalTitle, int numberOfSteps) {
        List<GoalStep> steps = new ArrayList<>();
        for (int i = 1; i <= numberOfSteps; i++) {
            steps.add(new GoalStep(goalTitle, i, "Step " + i + " - Action needed"));
        }
        return steps;
    }
    
    /**
     * Goal Clarification: Evaluate if a goal is well-defined
     */
    public String evaluateGoalClarity(String title, String description) {
        List<String> feedback = new ArrayList<>();
        
        if (title == null || title.length() < 5) {
            feedback.add("Goal title is too short. Make it more specific.");
        } else {
            feedback.add("✓ Good goal title");
        }
        
        if (description == null || description.length() < 20) {
            feedback.add("Goal description needs more detail.");
        } else {
            feedback.add("✓ Description provides clarity");
        }
        
        return String.join("\n", feedback);
    }
    
    // ==================== PROGRESS TRACKING ====================
    
    /**
     * Progress Tracking: Record detailed progress with metrics
     */
    public void recordDetailedProgress(String goalTitle, String progressNote, 
            double progressPercent, Map<String, Object> metrics) {
        for (GrowthGoal goal : activeGoals) {
            if (goal.getTitle().equals(goalTitle)) {
                goal.addProgress(progressNote, progressPercent);
                
                // Track in history
                ProgressEntry entry = new ProgressEntry(progressNote, progressPercent, metrics);
                progressHistory.computeIfAbsent(goalTitle, k -> new ArrayList<>()).add(entry);
                
                // Update completion rate
                goalCompletionRates.put(goalTitle, progressPercent);
                
                if (progressPercent >= 100.0) {
                    markGoalComplete(goalTitle);
                }
                break;
            }
        }
    }
    
    /**
     * Progress Tracking: Get progress summary for a goal
     */
    public ProgressSummary getProgressSummary(String goalTitle) {
        List<ProgressEntry> history = progressHistory.getOrDefault(goalTitle, new ArrayList<>());
        double currentRate = goalCompletionRates.getOrDefault(goalTitle, 0.0);
        
        int totalEntries = history.size();
        double averageProgress = history.stream()
            .mapToDouble(ProgressEntry::getPercent)
            .average()
            .orElse(0.0);
        
        String trend = calculateTrend(history);
        
        return new ProgressSummary(goalTitle, currentRate, totalEntries, averageProgress, trend);
    }
    
    /**
     * Progress Tracking: Calculate progress trend
     */
    private String calculateTrend(List<ProgressEntry> history) {
        if (history.size() < 2) return "Just started";
        
        double recentAvg = history.subList(Math.max(0, history.size() - 3), history.size())
            .stream()
            .mapToDouble(ProgressEntry::getPercent)
            .average()
            .orElse(0.0);
        
        double earlierAvg = history.subList(0, Math.min(3, history.size() / 2))
            .stream()
            .mapToDouble(ProgressEntry::getPercent)
            .average()
            .orElse(0.0);
        
        if (recentAvg > earlierAvg + 5) return "Improving";
        if (recentAvg < earlierAvg - 5) return "Declining";
        return "Stable";
    }
    
    /**
     * Progress Tracking: Get all goals with their progress rates
     */
    public Map<String, Double> getAllProgressRates() {
        return new HashMap<>(goalCompletionRates);
    }
    
    /**
     * Progress Tracking: Get visual progress indicator
     */
    public String getProgressBar(double percentage) {
        int totalBars = 20;
        int filledBars = (int) (percentage / 100.0 * totalBars);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) bar.append("█");
            else bar.append("░");
        }
        bar.append("] ").append(String.format("%.0f%%", percentage));
        return bar.toString();
    }
    
    /**
     * Progress Tracking: Get weekly progress report
     */
    public WeeklyProgressReport getWeeklyProgressReport() {
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        List<ProgressEntry> weekEntries = new ArrayList<>();
        
        for (List<ProgressEntry> entries : progressHistory.values()) {
            for (ProgressEntry entry : entries) {
                if (entry.getTimestamp().toLocalDate().isAfter(weekAgo)) {
                    weekEntries.add(entry);
                }
            }
        }
        
        int totalProgress = weekEntries.size();
        double avgProgress = weekEntries.stream()
            .mapToDouble(ProgressEntry::getPercent)
            .average()
            .orElse(0.0);
        
        return new WeeklyProgressReport(totalProgress, avgProgress, weekAgo, LocalDate.now());
    }
    
    // ==================== ACCOUNTABILITY ====================
    
    /**
     * Accountability: Schedule a check-in
     */
    public void scheduleCheckIn(String goalTitle, LocalDateTime dateTime, String focus) {
        AccountabilityCheckIn checkIn = new AccountabilityCheckIn(goalTitle, dateTime, focus);
        checkIns.add(checkIn);
    }
    
    /**
     * Accountability: Record a check-in response
     */
    public void recordCheckIn(String goalTitle, String response, int commitmentLevel) {
        for (AccountabilityCheckIn checkIn : checkIns) {
            if (checkIn.getGoalTitle().equals(goalTitle) && !checkIn.isCompleted()) {
                checkIn.recordResponse(response, commitmentLevel);
                break;
            }
        }
    }
    
    /**
     * Accountability: Get upcoming check-ins
     */
    public List<AccountabilityCheckIn> getUpcomingCheckIns() {
        LocalDateTime now = LocalDateTime.now();
        return checkIns.stream()
            .filter(c -> c.getScheduledTime().isAfter(now) && !c.isCompleted())
            .sorted(Comparator.comparing(AccountabilityCheckIn::getScheduledTime))
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Accountability: Create a commitment
     */
    public Commitment createCommitment(String goalTitle, String commitment, LocalDate byDate) {
        Commitment newCommitment = new Commitment(goalTitle, commitment, byDate);
        commitments.add(newCommitment);
        return newCommitment;
    }
    
    /**
     * Accountability: Mark commitment as fulfilled
     */
    public void fulfillCommitment(String commitmentText) {
        for (Commitment commitment : commitments) {
            if (commitment.getText().equals(commitmentText)) {
                commitment.markFulfilled();
                break;
            }
        }
    }
    
    /**
     * Accountability: Get active commitments
     */
    public List<Commitment> getActiveCommitments() {
        return commitments.stream()
            .filter(c -> !c.isFulfilled())
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Accountability: Set reminder time
     */
    public void setReminder(LocalDateTime dateTime) {
        this.nextReminder = dateTime;
    }
    
    /**
     * Accountability: Get reminder status
     */
    public boolean isReminderDue() {
        return LocalDateTime.now().isAfter(nextReminder);
    }
    
    /**
     * Accountability: Enable/disable accountability features
     */
    public void setAccountabilityEnabled(boolean enabled) {
        this.accountabilityEnabled = enabled;
    }
    
    /**
     * Accountability: Generate accountability report
     */
    public AccountabilityReport generateAccountabilityReport() {
        long completedCheckIns = checkIns.stream().filter(AccountabilityCheckIn::isCompleted).count();
        long upcomingCheckIns = getUpcomingCheckIns().size();
        long fulfilledCommitments = commitments.stream().filter(Commitment::isFulfilled).count();
        long activeCommitments = commitments.stream().filter(c -> !c.isFulfilled()).count();
        
        return new AccountabilityReport(completedCheckIns, upcomingCheckIns, 
            fulfilledCommitments, activeCommitments, accountabilityEnabled);
    }
    
    // ==================== EXISTING METHODS (kept for compatibility) ====================
    
    /**
     * Add a new growth goal for the user
     */
    public void addGoal(String title, String description, String area, Duration targetDuration) {
        GrowthGoal goal = new GrowthGoal(title, description, area, targetDuration);
        activeGoals.add(goal);
        progressHistory.put(title, new ArrayList<>());
    }
    
    /**
     * Get an encouraging message based on user's current state
     */
    public String getEncouragement() {
        Random random = new Random();
        return encouragingPhrases.get(random.nextInt(encouragingPhrases.size()));
    }
    
    /**
     * Get an encouraging message for a specific growth area
     */
    public String getEncouragementForArea(String area) {
        if (growthAreas.containsKey(area)) {
            GrowthArea ga = growthAreas.get(area);
            return "Keep working on " + ga.getName() + "! " + getEncouragement();
        }
        return getEncouragement();
    }
    
    /**
     * Record progress on a specific goal
     */
    public void recordProgress(String goalTitle, String progressNote, double progressPercent) {
        recordDetailedProgress(goalTitle, progressNote, progressPercent, new HashMap<>());
    }
    
    /**
     * Mark a goal as completed
     */
    public void markGoalComplete(String goalTitle) {
        Iterator<GrowthGoal> iterator = activeGoals.iterator();
        while (iterator.hasNext()) {
            GrowthGoal goal = iterator.next();
            if (goal.getTitle().equals(goalTitle)) {
                goal.markComplete();
                milestones.add(new GrowthMilestone(goalTitle, LocalDate.now()));
                goalCompletionRates.put(goalTitle, 100.0);
                iterator.remove();
                break;
            }
        }
    }
    
    /**
     * Get personalized growth strategy recommendation
     */
    public GrowthStrategy getRecommendedStrategy(String userMood) {
        if (userMood.equalsIgnoreCase("low") || userMood.equalsIgnoreCase("sad")) {
            return strategies.get(0);
        } else if (userMood.equalsIgnoreCase("neutral")) {
            return strategies.get(2);
        } else if (userMood.equalsIgnoreCase("happy") || userMood.equalsIgnoreCase("energetic")) {
            return strategies.get(3);
        }
        return strategies.get(4);
    }
    
    /**
     * Add a reflection entry
     */
    public void addReflection(String content, Map<String, Integer> moodRatings) {
        GrowthReflection reflection = new GrowthReflection(content, moodRatings);
        reflections.add(reflection);
    }
    
    /**
     * Get reflection insights
     */
    public String getReflectionInsights() {
        if (reflections.isEmpty()) {
            return "Start journaling to gain insights from your reflections.";
        }
        
        int totalReflections = reflections.size();
        return "You have " + totalReflections + " reflections. Keep reflecting to gain deeper insights!";
    }
    
    /**
     * Get daily growth summary
     */
    public DailyGrowth getTodayProgress() {
        LocalDate today = LocalDate.now();
        return dailyProgress.computeIfAbsent(today, k -> new DailyGrowth(today));
    }
    
    /**
     * Update today's progress
     */
    public void updateTodayProgress(String activity, int minutes, String note) {
        DailyGrowth today = getTodayProgress();
        today.addActivity(activity, minutes, note);
    }
    
    /**
     * Get active goals count
     */
    public int getActiveGoalsCount() {
        return activeGoals.size();
    }
    
    /**
     * Get all active goals
     */
    public List<GrowthGoal> getActiveGoals() {
        return new ArrayList<>(activeGoals);
    }
    
    /**
     * Get milestones achieved
     */
    public List<GrowthMilestone> getMilestones() {
        return new ArrayList<>(milestones);
    }
    
    /**
     * Get growth areas
     */
    public Map<String, GrowthArea> getGrowthAreas() {
        return new HashMap<>(growthAreas);
    }
    
    /**
     * Increment session count
     */
    public void incrementSession() {
        totalSessions++;
        lastInteraction = LocalDate.now();
    }
    
    /**
     * Get total sessions
     */
    public int getTotalSessions() {
        return totalSessions;
    }
    
    // ==================== INNER CLASSES ====================
    
    public static class GrowthGoal {
        private String title;
        private String description;
        private String area;
        private Duration targetDuration;
        private LocalDate createdDate;
        private List<String> progressNotes;
        private double progressPercent;
        private boolean completed;
        
        // SMART goal properties
        private String specificOutcome;
        private String measurableMetric;
        private LocalDate deadline;
        private String firstStep;
        private boolean isSmart;
        
        public GrowthGoal(String title, String description, String area, Duration targetDuration) {
            this.title = title;
            this.description = description;
            this.area = area;
            this.targetDuration = targetDuration;
            this.createdDate = LocalDate.now();
            this.progressNotes = new ArrayList<>();
            this.progressPercent = 0.0;
            this.completed = false;
            this.isSmart = false;
        }
        
        public void addProgress(String note, double percent) {
            progressNotes.add(note);
            progressPercent = Math.min(percent, 100.0);
        }
        
        public void markComplete() {
            this.completed = true;
            this.progressPercent = 100.0;
        }
        
        // Getters and setters
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getArea() { return area; }
        public Duration getTargetDuration() { return targetDuration; }
        public LocalDate getCreatedDate() { return createdDate; }
        public double getProgressPercent() { return progressPercent; }
        public boolean isCompleted() { return completed; }
        public List<String> getProgressNotes() { return new ArrayList<>(progressNotes); }
        
        public void setSpecificOutcome(String specificOutcome) { this.specificOutcome = specificOutcome; }
        public void setMeasurableMetric(String measurableMetric) { this.measurableMetric = measurableMetric; }
        public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
        public void setFirstStep(String firstStep) { this.firstStep = firstStep; }
        public void setSmart(boolean smart) { isSmart = smart; }
        
        public String getSpecificOutcome() { return specificOutcome; }
        public String getMeasurableMetric() { return measurableMetric; }
        public LocalDate getDeadline() { return deadline; }
        public String getFirstStep() { return firstStep; }
        public boolean isSmart() { return isSmart; }
    }
    
    public static class GrowthMilestone {
        private String title;
        private LocalDate achievedDate;
        
        public GrowthMilestone(String title, LocalDate achievedDate) {
            this.title = title;
            this.achievedDate = achievedDate;
        }
        
        public String getTitle() { return title; }
        public LocalDate getAchievedDate() { return achievedDate; }
    }
    
    public static class GrowthArea {
        private String name;
        private String description;
        
        public GrowthArea(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
    
    public static class GrowthStrategy {
        private String id;
        private String name;
        private int priority;
        
        public GrowthStrategy(String id, String name, int priority) {
            this.id = id;
            this.name = name;
            this.priority = priority;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public int getPriority() { return priority; }
    }
    
    public static class GrowthReflection {
        private String content;
        private LocalDateTime timestamp;
        private Map<String, Integer> moodRatings;
        
        public GrowthReflection(String content, Map<String, Integer> moodRatings) {
            this.content = content;
            this.timestamp = LocalDateTime.now();
            this.moodRatings = moodRatings != null ? new HashMap<>(moodRatings) : new HashMap<>();
        }
        
        public String getContent() { return content; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Map<String, Integer> getMoodRatings() { return new HashMap<>(moodRatings); }
    }
    
    public static class DailyGrowth {
        private LocalDate date;
        private List<GrowthActivity> activities;
        
        public DailyGrowth(LocalDate date) {
            this.date = date;
            this.activities = new ArrayList<>();
        }
        
        public void addActivity(String name, int minutes, String note) {
            activities.add(new GrowthActivity(name, minutes, note));
        }
        
        public LocalDate getDate() { return date; }
        public List<GrowthActivity> getActivities() { return new ArrayList<>(activities); }
    }
    
    public static class GrowthActivity {
        private String name;
        private int minutes;
        private String note;
        
        public GrowthActivity(String name, int minutes, String note) {
            this.name = name;
            this.minutes = minutes;
            this.note = note;
        }
        
        public String getName() { return name; }
        public int getMinutes() { return minutes; }
        public String getNote() { return note; }
    }
    
    // New inner classes for Goal Support features
    
    public static class GoalStep {
        private String goalTitle;
        private int stepNumber;
        private String description;
        private boolean completed;
        
        public GoalStep(String goalTitle, int stepNumber, String description) {
            this.goalTitle = goalTitle;
            this.stepNumber = stepNumber;
            this.description = description;
            this.completed = false;
        }
        
        public void markComplete() { this.completed = true; }
        public String getGoalTitle() { return goalTitle; }
        public int getStepNumber() { return stepNumber; }
        public String getDescription() { return description; }
        public boolean isCompleted() { return completed; }
    }
    
    public static class ProgressEntry {
        private String note;
        private double percent;
        private Map<String, Object> metrics;
        private LocalDateTime timestamp;
        
        public ProgressEntry(String note, double percent, Map<String, Object> metrics) {
            this.note = note;
            this.percent = percent;
            this.metrics = metrics != null ? new HashMap<>(metrics) : new HashMap<>();
            this.timestamp = LocalDateTime.now();
        }
        
        public String getNote() { return note; }
        public double getPercent() { return percent; }
        public Map<String, Object> getMetrics() { return new HashMap<>(metrics); }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    public static class ProgressSummary {
        private String goalTitle;
        private double currentRate;
        private int totalEntries;
        private double averageProgress;
        private String trend;
        
        public ProgressSummary(String goalTitle, double currentRate, int totalEntries, 
                double averageProgress, String trend) {
            this.goalTitle = goalTitle;
            this.currentRate = currentRate;
            this.totalEntries = totalEntries;
            this.averageProgress = averageProgress;
            this.trend = trend;
        }
        
        public String getGoalTitle() { return goalTitle; }
        public double getCurrentRate() { return currentRate; }
        public int getTotalEntries() { return totalEntries; }
        public double getAverageProgress() { return averageProgress; }
        public String getTrend() { return trend; }
    }
    
    public static class WeeklyProgressReport {
        private int totalProgress;
        private double averageProgress;
        private LocalDate startDate;
        private LocalDate endDate;
        
        public WeeklyProgressReport(int totalProgress, double averageProgress, 
                LocalDate startDate, LocalDate endDate) {
            this.totalProgress = totalProgress;
            this.averageProgress = averageProgress;
            this.startDate = startDate;
            this.endDate = endDate;
        }
        
        public int getTotalProgress() { return totalProgress; }
        public double getAverageProgress() { return averageProgress; }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
    }
    
    public static class AccountabilityCheckIn {
        private String goalTitle;
        private LocalDateTime scheduledTime;
        private String focus;
        private String response;
        private int commitmentLevel;
        private boolean completed;
        
        public AccountabilityCheckIn(String goalTitle, LocalDateTime scheduledTime, String focus) {
            this.goalTitle = goalTitle;
            this.scheduledTime = scheduledTime;
            this.focus = focus;
            this.completed = false;
        }
        
        public void recordResponse(String response, int commitmentLevel) {
            this.response = response;
            this.commitmentLevel = commitmentLevel;
            this.completed = true;
        }
        
        public String getGoalTitle() { return goalTitle; }
        public LocalDateTime getScheduledTime() { return scheduledTime; }
        public String getFocus() { return focus; }
        public String getResponse() { return response; }
        public int getCommitmentLevel() { return commitmentLevel; }
        public boolean isCompleted() { return completed; }
    }
    
    public static class Commitment {
        private String goalTitle;
        private String text;
        private LocalDate byDate;
        private boolean fulfilled;
        
        public Commitment(String goalTitle, String text, LocalDate byDate) {
            this.goalTitle = goalTitle;
            this.text = text;
            this.byDate = byDate;
            this.fulfilled = false;
        }
        
        public void markFulfilled() { this.fulfilled = true; }
        
        public String getGoalTitle() { return goalTitle; }
        public String getText() { return text; }
        public LocalDate getByDate() { return byDate; }
        public boolean isFulfilled() { return fulfilled; }
    }
    
    public static class AccountabilityReport {
        private long completedCheckIns;
        private long upcomingCheckIns;
        private long fulfilledCommitments;
        private long activeCommitments;
        private boolean accountabilityEnabled;
        
        public AccountabilityReport(long completedCheckIns, long upcomingCheckIns,
                long fulfilledCommitments, long activeCommitments, boolean accountabilityEnabled) {
            this.completedCheckIns = completedCheckIns;
            this.upcomingCheckIns = upcomingCheckIns;
            this.fulfilledCommitments = fulfilledCommitments;
            this.activeCommitments = activeCommitments;
            this.accountabilityEnabled = accountabilityEnabled;
        }
        
        public long getCompletedCheckIns() { return completedCheckIns; }
        public long getUpcomingCheckIns() { return upcomingCheckIns; }
        public long getFulfilledCommitments() { return fulfilledCommitments; }
        public long getActiveCommitments() { return activeCommitments; }
        public boolean isAccountabilityEnabled() { return accountabilityEnabled; }
    }
    
    // ==================== LEARNING COMPANION INNER CLASSES ====================
    
    public static class KnowledgeTopic {
        private String name;
        private String description;
        private List<String> subtopics;
        
        public KnowledgeTopic(String name, String description, List<String> subtopics) {
            this.name = name;
            this.description = description;
            this.subtopics = subtopics != null ? new ArrayList<>(subtopics) : new ArrayList<>();
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public List<String> getSubtopics() { return new ArrayList<>(subtopics); }
    }
    
    public static class LearningPath {
        private String goal;
        private List<String> topics;
        private int currentStep;
        
        public LearningPath(String goal, List<String> topics) {
            this.goal = goal;
            this.topics = topics != null ? new ArrayList<>(topics) : new ArrayList<>();
            this.currentStep = 0;
        }
        
        public String getGoal() { return goal; }
        public List<String> getTopics() { return new ArrayList<>(topics); }
        public int getCurrentStep() { return currentStep; }
        public void advanceStep() { currentStep++; }
        public String getCurrentTopic() {
            return currentStep < topics.size() ? topics.get(currentStep) : null;
        }
    }
    
    public static class SkillProgress {
        private String skillName;
        private String description;
        private double progressPercent;
        private List<String> activities;
        
        public SkillProgress(String skillName, String description) {
            this.skillName = skillName;
            this.description = description;
            this.progressPercent = 0.0;
            this.activities = new ArrayList<>();
        }
        
        public void addProgress(double percent, String activity) {
            this.progressPercent = Math.min(percent, 100.0);
            if (activity != null) this.activities.add(activity);
        }
        
        public String getSkillName() { return skillName; }
        public String getDescription() { return description; }
        public double getProgressPercent() { return progressPercent; }
        public List<String> getActivities() { return new ArrayList<>(activities); }
    }
    
    public static class SkillChallenge {
        private String skillName;
        private String challenge;
        private int daysDuration;
        private LocalDate startDate;
        private boolean completed;
        
        public SkillChallenge(String skillName, String challenge, int days) {
            this.skillName = skillName;
            this.challenge = challenge;
            this.daysDuration = days;
            this.startDate = LocalDate.now();
            this.completed = false;
        }
        
        public void markComplete() { this.completed = true; }
        
        public String getSkillName() { return skillName; }
        public String getChallenge() { return challenge; }
        public int getDaysDuration() { return daysDuration; }
        public LocalDate getStartDate() { return startDate; }
        public boolean isCompleted() { return completed; }
    }
    
    public static class WonderPrompt {
        private String prompt;
        private String category;
        private int inspirationLevel;
        
        public WonderPrompt(String prompt, String category, int inspirationLevel) {
            this.prompt = prompt;
            this.category = category;
            this.inspirationLevel = Math.min(Math.max(inspirationLevel, 1), 10);
        }
        
        public String getPrompt() { return prompt; }
        public String getCategory() { return category; }
        public int getInspirationLevel() { return inspirationLevel; }
    }
    
    public static class CuriosityQuestion {
        private String question;
        private String relatedTopic;
        
        public CuriosityQuestion(String question, String relatedTopic) {
            this.question = question;
            this.relatedTopic = relatedTopic;
        }
        
        public String getQuestion() { return question; }
        public String getRelatedTopic() { return relatedTopic; }
    }
    
    // ==================== CHALLENGE HELPER INNER CLASSES ====================
    
    public static class Challenge {
        private String title;
        private String description;
        private String difficulty;
        private LocalDate createdDate;
        private boolean completed;
        
        public Challenge(String title, String description, String difficulty) {
            this.title = title;
            this.description = description;
            this.difficulty = difficulty;
            this.createdDate = LocalDate.now();
            this.completed = false;
        }
        
        public void markComplete() { this.completed = true; }
        
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getDifficulty() { return difficulty; }
        public LocalDate getCreatedDate() { return createdDate; }
        public boolean isCompleted() { return completed; }
    }
    
    public static class SolutionAttempt {
        private String approach;
        private boolean successful;
        private LocalDateTime timestamp;
        
        public SolutionAttempt(String approach, boolean successful) {
            this.approach = approach;
            this.successful = successful;
            this.timestamp = LocalDateTime.now();
        }
        
        public String getApproach() { return approach; }
        public boolean isSuccessful() { return successful; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    public static class Obstacle {
        private String description;
        private String suggestedSolution;
        
        public Obstacle(String description, String suggestedSolution) {
            this.description = description;
            this.suggestedSolution = suggestedSolution;
        }
        
        public String getDescription() { return description; }
        public String getSuggestedSolution() { return suggestedSolution; }
    }
    
    public static class Achievement {
        private String title;
        private String description;
        private String category;
        private LocalDate achievedDate;
        
        public Achievement(String title, String description, String category) {
            this.title = title;
            this.description = description;
            this.category = category;
            this.achievedDate = LocalDate.now();
        }
        
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getCategory() { return category; }
        public LocalDate getAchievedDate() { return achievedDate; }
    }
}

