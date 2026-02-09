import java.util.*;
import java.util.regex.*;

/**
 * HomeworkAcademicHandler - Handles homework and academic help responses
 * Part of Phase 3: Response Categories
 */
public class HomeworkAcademicHandler {
    
    private Map<Subject, List<SubjectResponse>> subjectResponses;
    private Map<String, Subject> keywordMapping;
    private Random random;
    
    public HomeworkAcademicHandler() {
        this.subjectResponses = new HashMap<>();
        this.keywordMapping = new HashMap<>();
        this.random = new Random();
        initializeSubjectResponses();
        initializeKeywordMapping();
    }
    
    /**
     * Academic subjects
     */
    public enum Subject {
        MATH("Mathematics"),
        SCIENCE("Science"),
        BIOLOGY("Biology"),
        PHYSICS("Physics"),
        CHEMISTRY("Chemistry"),
        HISTORY("History"),
        ENGLISH("English"),
        LITERATURE("Literature"),
        GEOGRAPHY("Geography"),
        COMPUTER_SCIENCE("Computer Science"),
        ART("Art"),
        MUSIC("Music"),
        PHYSICAL_EDUCATION("Physical Education"),
        FOREIGN_LANGUAGE("Foreign Language"),
        SOCIAL_STUDIES("Social Studies"),
        GENERAL("General Academic");
        
        private final String displayName;
        
        Subject(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Subject response class
     */
    private static class SubjectResponse {
        String response;
        String followUp;
        List<String> exampleTopics;
        StudyTip studyTip;
        
        public SubjectResponse(String response, String followUp, List<String> exampleTopics, StudyTip studyTip) {
            this.response = response;
            this.followUp = followUp;
            this.exampleTopics = exampleTopics;
            this.studyTip = studyTip;
        }
    }
    
    /**
     * Study tip class
     */
    private static class StudyTip {
        String title;
        List<String> steps;
        
        public StudyTip(String title, List<String> steps) {
            this.title = title;
            this.steps = steps;
        }
    }
    
    private void initializeSubjectResponses() {
        // Math responses
        List<SubjectResponse> mathResponses = Arrays.asList(
            new SubjectResponse(
                "Math is all about practice! What specific topic are you working on?",
                "Math builds on itself, so it's great that you're asking for help.",
                Arrays.asList("Algebra", "Geometry", "Calculus", "Trigonometry", "Statistics"),
                new StudyTip("The Step-by-Step Approach", Arrays.asList(
                    "1. Understand the problem type",
                    "2. Review the formula or concept",
                    "3. Work through a similar example",
                    "4. Practice with similar problems",
                    "5. Check your work"
                ))
            ),
            new SubjectResponse(
                "I'd be happy to help with math! What concept are you struggling with?",
                "Once you get the basics, math becomes much easier!",
                Arrays.asList("Equations", "Functions", "Graphs", "Probability"),
                new StudyTip("Practice Makes Perfect", Arrays.asList(
                    "1. Start with easier problems",
                    "2. Gradually increase difficulty",
                    "3. Don't be afraid to make mistakes",
                    "4. Review what you got wrong",
                    "5. Keep practicing daily"
                ))
            )
        );
        subjectResponses.put(Subject.MATH, mathResponses);
        
        // Science responses
        List<SubjectResponse> scienceResponses = Arrays.asList(
            new SubjectResponse(
                "Science is fascinating! What branch are you studying?",
                "Science helps us understand the world around us.",
                Arrays.asList("Biology", "Physics", "Chemistry", "Earth Science"),
                new StudyTip("The Scientific Method", Arrays.asList(
                    "1. Make an observation",
                    "2. Ask a question",
                    "3. Form a hypothesis",
                    "4. Conduct an experiment",
                    "5. Analyze results",
                    "6. Draw conclusions"
                ))
            )
        );
        subjectResponses.put(Subject.SCIENCE, scienceResponses);
        
        // History responses
        List<SubjectResponse> historyResponses = Arrays.asList(
            new SubjectResponse(
                "History is full of amazing stories! What time period or event are you learning about?",
                "Understanding the past helps us shape the future.",
                Arrays.asList("Ancient Civilizations", "Middle Ages", "Renaissance", "World Wars", "Modern History"),
                new StudyTip("Remembering Historical Facts", Arrays.asList(
                    "1. Create a timeline",
                    "2. Connect events to stories",
                    "3. Use flashcards",
                    "4. Watch documentaries",
                    "5. Discuss with others"
                ))
            )
        );
        subjectResponses.put(Subject.HISTORY, historyResponses);
        
        // English responses
        List<SubjectResponse> englishResponses = Arrays.asList(
            new SubjectResponse(
                "English is great for self-expression! What do you need help with?",
                "Writing and reading skills open many doors.",
                Arrays.asList("Grammar", "Essay Writing", "Reading Comprehension", "Vocabulary"),
                new StudyTip("Improving Writing Skills", Arrays.asList(
                    "1. Read regularly",
                    "2. Practice writing daily",
                    "3. Learn new vocabulary",
                    "4. Get feedback on your writing",
                    "5. Edit and revise your work"
                ))
            )
        );
        subjectResponses.put(Subject.ENGLISH, englishResponses);
        
        // Computer Science responses
        List<SubjectResponse> csResponses = Arrays.asList(
            new SubjectResponse(
                "Computer Science is the future! What programming concept are you working on?",
                "Coding is like solving puzzles - methodical and fun!",
                Arrays.asList("Variables", "Loops", "Functions", "Arrays", "Objects"),
                new StudyTip("Learning to Code", Arrays.asList(
                    "1. Start with basics",
                    "2. Practice coding every day",
                    "3. Work on small projects",
                    "4. Debug your own code",
                    "5. Learn from others' code"
                ))
            )
        );
        subjectResponses.put(Subject.COMPUTER_SCIENCE, csResponses);
        
        // General academic responses
        List<SubjectResponse> generalResponses = Arrays.asList(
            new SubjectResponse(
                "I'd be happy to help with your homework! What subject are you working on?",
                "Asking for help is the first step to understanding!",
                Arrays.asList("Studying", "Test Prep", "Time Management", "Organization"),
                new StudyTip("Effective Study Habits", Arrays.asList(
                    "1. Find a quiet study space",
                    "2. Remove distractions",
                    "3. Take regular breaks",
                    "4. Use study guides",
                    "5. Teach concepts to others"
                ))
            ),
            new SubjectResponse(
                "Homework time can be challenging. What can I help you with?",
                "You've got this! Let's tackle it together.",
                Arrays.asList("Motivation", "Focus", "Understanding", "Time Management"),
                new StudyTip("Managing Homework Stress", Arrays.asList(
                    "1. Break tasks into smaller parts",
                    "2. Start with hardest subject",
                    "3. Take breaks every 30-45 minutes",
                    "4. Stay hydrated",
                    "5. Celebrate small wins"
                ))
            )
        );
        subjectResponses.put(Subject.GENERAL, generalResponses);
    }
    
    private void initializeKeywordMapping() {
        // Math keywords
        keywordMapping.put("math", Subject.MATH);
        keywordMapping.put("mathematics", Subject.MATH);
        keywordMapping.put("algebra", Subject.MATH);
        keywordMapping.put("geometry", Subject.MATH);
        keywordMapping.put("calculus", Subject.MATH);
        keywordMapping.put("trigonometry", Subject.MATH);
        keywordMapping.put("statistics", Subject.MATH);
        keywordMapping.put("equation", Subject.MATH);
        keywordMapping.put("formula", Subject.MATH);
        
        // Science keywords
        keywordMapping.put("science", Subject.SCIENCE);
        keywordMapping.put("scientific", Subject.SCIENCE);
        
        // Biology keywords
        keywordMapping.put("biology", Subject.BIOLOGY);
        keywordMapping.put("cells", Subject.BIOLOGY);
        keywordMapping.put("animals", Subject.BIOLOGY);
        keywordMapping.put("plants", Subject.BIOLOGY);
        keywordMapping.put("human body", Subject.BIOLOGY);
        
        // Physics keywords
        keywordMapping.put("physics", Subject.PHYSICS);
        keywordMapping.put("forces", Subject.PHYSICS);
        keywordMapping.put("motion", Subject.PHYSICS);
        keywordMapping.put("energy", Subject.PHYSICS);
        keywordMapping.put("gravity", Subject.PHYSICS);
        
        // Chemistry keywords
        keywordMapping.put("chemistry", Subject.CHEMISTRY);
        keywordMapping.put("atoms", Subject.CHEMISTRY);
        keywordMapping.put("molecules", Subject.CHEMISTRY);
        keywordMapping.put("reactions", Subject.CHEMISTRY);
        
        // History keywords
        keywordMapping.put("history", Subject.HISTORY);
        keywordMapping.put("historical", Subject.HISTORY);
        keywordMapping.put("war", Subject.HISTORY);
        keywordMapping.put("ancient", Subject.HISTORY);
        
        // English keywords
        keywordMapping.put("english", Subject.ENGLISH);
        keywordMapping.put("grammar", Subject.ENGLISH);
        keywordMapping.put("writing", Subject.ENGLISH);
        keywordMapping.put("essay", Subject.ENGLISH);
        keywordMapping.put("reading", Subject.ENGLISH);
        
        // Literature keywords
        keywordMapping.put("literature", Subject.LITERATURE);
        keywordMapping.put("book", Subject.LITERATURE);
        keywordMapping.put("novel", Subject.LITERATURE);
        keywordMapping.put("poetry", Subject.LITERATURE);
        keywordMapping.put("story", Subject.LITERATURE);
        
        // Geography keywords
        keywordMapping.put("geography", Subject.GEOGRAPHY);
        keywordMapping.put("countries", Subject.GEOGRAPHY);
        keywordMapping.put("maps", Subject.GEOGRAPHY);
        keywordMapping.put("continents", Subject.GEOGRAPHY);
        
        // Computer Science keywords
        keywordMapping.put("coding", Subject.COMPUTER_SCIENCE);
        keywordMapping.put("programming", Subject.COMPUTER_SCIENCE);
        keywordMapping.put("computer", Subject.COMPUTER_SCIENCE);
        keywordMapping.put("code", Subject.COMPUTER_SCIENCE);
        keywordMapping.put("java", Subject.COMPUTER_SCIENCE);
        keywordMapping.put("python", Subject.COMPUTER_SCIENCE);
        keywordMapping.put("javascript", Subject.COMPUTER_SCIENCE);
        
        // Homework general keywords
        keywordMapping.put("homework", Subject.GENERAL);
        keywordMapping.put("assignment", Subject.GENERAL);
        keywordMapping.put("project", Subject.GENERAL);
        keywordMapping.put("studying", Subject.GENERAL);
        keywordMapping.put("study", Subject.GENERAL);
        keywordMapping.put("exam", Subject.GENERAL);
        keywordMapping.put("test", Subject.GENERAL);
        keywordMapping.put("quiz", Subject.GENERAL);
    }
    
    /**
     * Detects the subject from input
     */
    public Subject detectSubject(String input) {
        String lowerInput = input.toLowerCase();
        
        // Check for subject-specific keywords
        for (Map.Entry<String, Subject> entry : keywordMapping.entrySet()) {
            if (lowerInput.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        return Subject.GENERAL;
    }
    
    /**
     * Gets a response for the detected subject
     */
    public SubjectResponse getResponse(Subject subject) {
        List<SubjectResponse> responses = subjectResponses.get(subject);
        if (responses != null && !responses.isEmpty()) {
            return responses.get(random.nextInt(responses.size()));
        }
        
        // Return general response if subject not found
        List<SubjectResponse> generalResponses = subjectResponses.get(Subject.GENERAL);
        return generalResponses.get(random.nextInt(generalResponses.size()));
    }
    
    /**
     * Checks if input is academic-related
     */
    public boolean isAcademic(String input) {
        String lowerInput = input.toLowerCase();
        
        // Check for homework keywords
        String[] homeworkKeywords = {
            "homework", "assignment", "project", "essay", "paper",
            "studying", "study", "exam", "test", "quiz",
            "teacher", "class", "school", "college", "university"
        };
        
        for (String keyword : homeworkKeywords) {
            if (lowerInput.contains(keyword)) {
                return true;
            }
        }
        
        return detectSubject(input) != Subject.GENERAL;
    }
    
    /**
     * Gets study tips for a subject
     */
    public StudyTip getStudyTip(Subject subject) {
        SubjectResponse response = getResponse(subject);
        return response.studyTip;
    }
    
    /**
     * Gets available subjects
     */
    public List<Subject> getAvailableSubjects() {
        return Arrays.asList(Subject.values());
    }
    
    /**
     * Gets help for a specific problem type
     */
    public String getProblemHelp(String problemType) {
        String lowerProblem = problemType.toLowerCase();
        
        if (lowerProblem.contains("how to") || lowerProblem.contains("help me")) {
            return "Sure! Let me break this down for you step by step. What specifically are you trying to understand?";
        }
        
        if (lowerProblem.contains("don't understand") || lowerProblem.contains("confused")) {
            return "That's okay! Confusion is the first step to understanding. What part is confusing you?";
        }
        
        if (lowerProblem.contains("hard") || lowerProblem.contains("difficult")) {
            return "Difficult problems are opportunities to learn! Let's tackle this together. What's the hardest part?";
        }
        
        if (lowerProblem.contains("easy") || lowerProblem.contains("simple")) {
            return "Great! If it's easy for you, that's awesome. Is there anything else you'd like to explore?";
        }
        
        return "I'm here to help! What specific aspect would you like me to explain?";
    }
    
    /**
     * Provides motivation for studying
     */
    public String getStudyMotivation() {
        String[] motivations = {
            "You've got this! Every minute you spend studying brings you closer to your goals.",
            "Learning something new is always worth it. Keep going!",
            "Small steps lead to big achievements. You're on the right track!",
            "Remember why you started. This effort will pay off!",
            "You're investing in yourself, and that's amazing!",
            "Challenges help us grow. Embrace the learning process!",
            "The only way to do great work is to love what you do. Find the excitement in learning!"
        };
        return motivations[random.nextInt(motivations.length)];
    }
    
    /**
     * Gets time management advice
     */
    public String getTimeManagementAdvice() {
        String[] tips = {
            "Try the Pomodoro Technique: 25 minutes of focused study, then a 5-minute break.",
            "Break your study sessions into manageable chunks with regular breaks.",
            "Tackle your hardest subjects when you're most alert.",
            "Use a planner or calendar to schedule your study time.",
            "Start with the most important tasks first.",
            "Eliminate distractions - put your phone away during study time!",
            "Get enough sleep - it's essential for memory and learning."
        };
        return tips[random.nextInt(tips.length)];
    }
    
    /**
     * Handles test preparation questions
     */
    public String handleTestPrep(String input) {
        String lowerInput = input.toLowerCase();
        
        if (lowerInput.contains("nervous") || lowerInput.contains("anxious")) {
            return "Feeling nervous before a test is normal! Here are some tips:\n" +
                   "• Get plenty of sleep the night before\n" +
                   "• Eat a good breakfast\n" +
                   "• Arrive early so you can relax\n" +
                   "• Take deep breaths\n" +
                   "• Remember, this is just one moment in your journey";
        }
        
        if (lowerInput.contains("how to prepare") || lowerInput.contains("study for")) {
            return "Great question! Here's a study plan:\n" +
                   "1. Start reviewing 2-3 days before the test\n" +
                   "2. Review your notes and highlights\n" +
                   "3. Practice with sample questions\n" +
                   "4. Study with a friend (or alone, whichever works for you)\n" +
                   "5. Get a good night's sleep";
        }
        
        if (lowerInput.contains("what to study") || lowerInput.contains("important")) {
            return "Focus on:\n" +
                   "• Key concepts and definitions\n" +
                   "• Formulas and how to apply them\n" +
                   "• Sample problems and solutions\n" +
                   "• Your notes from class\n" +
                   "• Anything the teacher emphasized";
        }
        
        return "I'm here to support your test preparation! What specific aspect would you like help with?";
    }
    
    /**
     * Handles homework help requests
     */
    public String handleHomeworkHelp(String input) {
        Subject subject = detectSubject(input);
        SubjectResponse response = getResponse(subject);
        
        StringBuilder result = new StringBuilder();
        result.append(response.response);
        
        if (response.exampleTopics != null && !response.exampleTopics.isEmpty()) {
            result.append("\n\nSome topics in this subject include: ");
            result.append(String.join(", ", response.exampleTopics.subList(0, Math.min(4, response.exampleTopics.size()))));
        }
        
        return result.toString();
    }
}

