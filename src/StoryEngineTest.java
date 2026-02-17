
import java.util.*;

public class StoryEngineTest {
    public static void main(String[] args) {
        StoryEngine engine = new StoryEngine();

        // Critical-path testing: basic functionality
        System.out.println("Total anecdotes: " + engine.getTotalAnecdotes());
        System.out.println("Random anecdote: " + engine.getRandomAnecdote());

        // Test each category
        for (StoryEngine.AnecdoteCategory category : StoryEngine.AnecdoteCategory.values()) {
            String anecdote = engine.getAnecdoteByCategory(category);
            System.out.println("Category " + category.getName() + ": " + anecdote);
        }

        // Test situation-based anecdotes
        String[] situations = {"I failed an exam", "I'm feeling happy", "I'm too busy", "relationship problems"};
        for (String situation : situations) {
            String anecdote = engine.getAnecdoteForSituation(situation);
            System.out.println("Situation '" + situation + "': " + anecdote);
        }

        // Edge case: non-matching situation
        String nonMatching = "something unrelated";
        String anecdote = engine.getAnecdoteForSituation(nonMatching);
        System.out.println("Non-matching situation: " + anecdote);

        System.out.println("All tests passed!");
    }
}
