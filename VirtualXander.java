import java.util.*;
import java.util.regex.*;
import java.src.GUI;
import java.src.Client;
import java.Server;

public class VirtualXander 
{

    public static void main(String[] args) 
    {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello! Start chatting with me. Type 'exit' to end the chat.");

        while (true) 
        {

            System.out.print("You: ");

            userInput = scanner.nextLine().trim();

            if (userInput.contains("exit")) 
            {
                System.out.println("Xander: Goodbye!");
                break;
            }

            // Basic greetings
            if (userInput.contains("hello") || userInput.contains("hi") || userInput.contains("hey")) 
            {
                System.out.println("Xander: Hi there! How can I help you today?");
                continue;
            }

            if (userInput.contains("what's your name?") || userInput.contains("who are you?")) 
            {
                System.out.println("Xander: I'm Xander, your virtual friend! I'm here to chat and keep you company.");
                continue;
            }

            if (userInput.contains("hru")) 
            {
                System.out.println("Xander: I'm doing well, ty! Hbu?");
                continue;
            }

            if (userInput.contains("how r u")) 
            {
                System.out.println("Xander: I'm doing well, thank you! How about you?");
                continue;
            }

            if (userInput.contains("wyd")) 
            {
                System.out.println("Xander: Just here to chat with you! What about you?");

                userInput = userscanner.nextLine().trim();

                if (userInput.contains("studying"));
                    System.out.println("Xander: Studying can be tough! What subject are you working on?");

                    userInput = userscanner.nextLine().trim();

                    if (userInput.contains("history")) 
                    {
                        System.out.println("Xander: History is fascinating! What time period are you studying?");

                        userInput = userscanner.nextLine().trim();

                        if (userInput.contains("ancient civilizations"));
                            System.out.println("Xander: Ancient civilizations are so interesting! Do you have a favorite one?");

                            userInput = userscanner.nextLine().trim();

                            if (userInput.contains("egyptians"));
                                System.out.println("Xander: The Egyptians had such a rich culture! Are you learning about their pyramids or their mythology?");

                            if (userInput.contains("greeks"));  
                                System.out.println("Xander: The Greeks made so many contributions to philosophy and democracy! Are you studying any specific philosophers?");

                                userInput = userscanner.nextLine().trim();

                                if (userInput.contains("socrates"));
                                    System.out.println("Xander: Socrates is known for his method of questioning! Are you learning about his trial or his teachings?");

                                if (userInput.contains("plato"));
                                    System.out.println("Xander: Plato was a student of Socrates and founded the Academy! Are you studying his works like 'The Republic'?");

                                if (userInput.contains("aristotle"));
                                    System.out.println("Xander: Aristotle was a student of Plato and made significant contributions to many fields! Are you learning about his philosophy or his scientific observations?");

                            if (userInput.contains("romans"));
                                System.out.println("Xander: The Romans were incredible engineers! Are you learning about their architecture or their military strategies?");

                        if (userInput.contains("modern history"));
                            System.out.println("Xander: Modern history has so many important events! Is there a specific event you're focusing on?");

                            userInput = userscanner.nextLine().trim();

                            if (userInput.contains("industrial revolution"));
                                System.out.println("Xander: The Industrial Revolution changed the world! Are you studying its impact on society or technology?");

                                userInput = userscanner.nextLine().trim();

                                if (userInput.contains("society"));
                                    System.out.println("Xander: The Industrial Revolution had a huge impact on society! Are you learning about urbanization or labor movements?");

                                    userInput = userscanner.nextLine().trim();

                                    if (userInput.contains("urbanization"));
                                        System.out.println("Xander: Urbanization led to the growth of cities! Are you studying the living conditions or the economic changes?");

                                    if (userInput.contains("labor movements"));
                                        System.out.println("Xander: Labor movements were crucial for workers' rights! Are you learning about any specific strikes or unions?");

                                if (userInput.contains("technology"));
                                    System.out.println("Xander: The technological advancements during the Industrial Revolution were incredible! Are you studying inventions like the steam engine or the spinning jenny?");

                            if (userInput.contains("renaissance"));
                                System.out.println("Xander: The Renaissance was a time of great cultural growth! Are you learning about the art or the scientific advancements?");

                                userInput = userscanner.nextLine().trim();

                                if (userInput.contains("art"));
                                    System.out.println("Xander: Renaissance art is so beautiful! Do you have a favorite artist?");

                                    userInput = userscanner.nextLine().trim();

                                    if (userInput.contains("da vinci"));
                                        System.out.println("Xander: Leonardo da Vinci was a true genius! Are you studying his paintings or his inventions?");

                                        if (userInput.contains("paintings"));
                                            System.out.println("Xander: The Mona Lisa is one of his most famous works! What do you think makes it so special?");

                        if (userInput.contains("medieval times"));
                            System.out.println("Xander: Medieval times were full of knights and castles! Are you learning about any specific aspects?");

                            userInput = userscanner.nextLine().trim();

                            if (userInput.contains("feudalism"));
                                System.out.println("Xander: Feudalism was a key part of medieval society! Need help understanding it?");

                        if (userInput.contains("world wars"));
                            System.out.println("Xander: The world wars were pivotal moments in history! Are you studying World War I or World War II?");

                            userInput = userscanner.nextLine().trim();

                            if (userInput.contains("world war i")|| userInput.contains("wwi"))
                                System.out.println("Xander: World War I was a complex conflict! Are you learning about the causes or the major battles?");

                                userInput = userscanner.nextLine().trim();

                                if (userInput.contains("causes"));
                                    System.out.println("Xander: The causes of World War I are quite intricate! Are you focusing on the assassination of Archduke Franz Ferdinand or the alliance systems?");

                                if (userInput.contains("major battles"));
                                    System.out.println("Xander: The major battles of World War I were intense! Are you studying the Battle of the Somme or the Battle of Verdun?");

                            if (userInput.contains("world war ii"));
                                System.out.println("Xander: World War II was a global conflict with many significant events! Are you focusing on the European or Pacific theater?");

                                userInput = userscanner.nextLine().trim();

                                if (userInput.contains("european theater"));
                                    System.out.println("Xander: The European theater had many important battles! Are you studying D-Day or the Battle of the Bulge?");

                                    userInput = userscanner.nextLine().trim();

                                    if (userInput.contains("d-day"));
                                        System.out.println("Xander: D-Day was a crucial turning point in World War II! Are you learning about the planning or the execution of the invasion?");

                                    if (userInput.contains("battle of the bulge"));
                                        System.out.println("Xander: The Battle of the Bulge was a fierce battle! Are you studying the strategies used or the outcomes?");

                                if (userInput.contains("pacific theater"));
                                    System.out.println("Xander: The Pacific theater was crucial in World War II! Are you learning about the Battle of Midway or Iwo Jima?");

                    }

                    if (userInput.contains("math"));
                    {
                        System.out.println("Xander: Math is a great subject! What topic are you studying?");

                        userInput = userscanner.nextLine().trim();

                        if (userInput.contains("algebra"));
                            System.out.println("Xander: Algebra is all about solving equations! Need help with anything specific?");

                            userInput = userscanner.nextLine().trim();

                            if (userInput.contains("quadratic equations"));
                                System.out.println("Xander: Quadratic equations can be tricky! Want me to explain how to solve them?");

                            if (userInput.contains("linear equations"));
                                System.out.println("Xander: Linear equations are the foundation of algebra! Want me to help you with them?");

                        if (userInput.contains("geometry"));
                            System.out.println("Xander: Geometry is the study of shapes and sizes! Need help with any concepts?");

                            userInput = userscanner.nextLine().trim();

                            if (userInput.contains("angles"));
                                System.out.println("Xander: Angles are important in geometry! Want me to explain how to calculate them?");

                            if (userInput.contains("triangles"));
                                System.out.println("Xander: Triangles are fascinating shapes! Need help with triangle properties?");

                    }

                    if (userInput.contains("science")) 
                    {
                        System.out.println("Xander: Science is fascinating! What topic are you studying?");

                        userInput = userscanner.nextLine().trim();

                        if (userInput.contains("biology"));
                        System.out.println("Xander: Biology is the study of life! What specific area are you focusing on?");

                        userInput = userscanner.nextLine().trim();

                        if (userInput.contains("animals"));
                            System.out.println("Xander: Animals are incredible creatures! Are you learning about their anatomy or behavior?");

                            userInput = userscanner.nextLine().trim();

                            if (userInput.contains("anatomy"));
                                System.out.println("Xander: Animal anatomy is so interesting! I'm glad you find it fascinating!");

                            if (userInput.contains("behavior"));
                                System.out.println("Xander: Animal behavior is really cool! I'm glad you find it fascinating!");

                        if (userInput.contains("cells"));
                            System.out.println("Xander: Cells are the basic building blocks of life! Are you learning about cell structure or function?");

                            userInput = userscanner.nextLine().trim();

                            if (userInput.contains("structure"));
                                System.out.println("Xander: Cell structures are facinating! I'm glad you find them interesting!");

                            if (userInput.contains("function"));
                                System.out.println("Xander: Cell functions are crucial for life! I'm glad you find them interesting!");

                        if (userInput.contains("physics"));
                            System.out.println("Xander: Physics is the study of matter and energy! What specific area are you focusing on?");

                            userInput = userscanner.nextLine().trim();

                            if (userInput.contains("forces"));
                                System.out.println("Xander: Forces are what cause objects to move! Are you learning about gravity or friction?");

                                userInput = userscanner.nextLine().trim();

                                if (userInput.contains("gravity"));
                                    System.out.println("Xander: Gravity is the force that pulls objects toward each other! I'm glad you find it interesting!");

                                if (userInput.contains("friction"));
                                    System.out.println("Xander: Friction is the force that opposes motion! I'm glad you find it interesting!");

                            if (userInput.contains("motion"));
                                System.out.println("Xander: Motion is all about how objects move! Are you learning about speed or acceleration?");

                                userInput = userscanner.nextLine().trim();

                                if (userInput.contains("speed"));
                                    System.out.println("Xander: Speed is how fast something is moving! I'm glad you find it interesting!");

                                if (userInput.contains("acceleration"));
                                    System.out.println("Xander: Acceleration is how quickly something changes its speed! I'm glad you find it interesting!");

                            if (userInput.contains("energy"));
                                System.out.println("Xander: Energy is the ability to do work! Are you learning about kinetic or potential energy?");

                                userInput = userscanner.nextLine().trim();

                                if (userInput.contains("kinetic"));
                                    System.out.println("Xander: Kinetic energy is the energy of motion! I'm glad you find it interesting!");

                                if (userInput.contains("potential"));
                                    System.out.println("Xander: Potential energy is stored energy! I'm glad you find it interesting!");

                    }
                continue;
            }

            if (userInput.contains("jc") || userInput.contains("just chilling")) 
            {
                System.out.println("Xander: That sounds relaxing! Anything interesting on your mind?");
                continue;
            }

            if (userInput.contains("how are you") || userInput.contains("how are you?") || userInput.contains("how r u?")) 
            {
                System.out.println("Xander: I'm doing well, thank you! How about you?");
            }

            if (userInput.contains("good") || userInput.contains("I'm good")) 
            {
                System.out.println("Xander: That's great to hear! Anything specific you want to talk about?");
                continue;
            }

            if (userInput.contains("great")) 
            {
                System.out.println("Xander: Awesome! What's making your day great?");
                continue;
            }

            if (userInput.contains("fantastic")) 
            {
                System.out.println("Xander: Wow, fantastic! Tell me more about it.");
                continue;
            }

            if (userInput.contains("excellent")) 
            {
                System.out.println("Xander: Excellent! What's going so well?");
                continue;
            }

            if (userInput.contains("what's up") || userInput.contains("What's up?")) 
            {
                System.out.println("Xander: Not much, just here to chat with you! What's up with you?");
                continue;
            }

            if (userInput.contains("not much")) 
            {
                System.out.println("Xander: Sometimes not much is just what we need! Anything on your mind?");
                continue;
            }

            if (userInput.contains("meh")) 
            {
                System.out.println("Xander: Sorry you're feeling meh. Want to talk about it?");
                continue;
            }

            if (userInput.contains("not so good")) 
            {
                System.out.println("Xander: I'm sorry to hear that. Would you like to talk about what's bothering you?");
                continue;
            }

            if (userInput.contains("bad")) 
            {
                System.out.println("Xander: I'm sorry to hear that. Want to talk about it?");
                continue;
            }

            // Likes and dislikes
            if (userInput.contains("I like math")) 
            {
                System.out.println("Xander: Math isn't my favourite subject, but it's cool! What's your favorite topic in math?");
                continue;
            }

            if (userInput.contains("I dislike homework") || userInput.contains("I hate homework")) 
            {
                System.out.println("Xander: Homework can be tough. What subject do you find the hardest?");
                continue;
            }

            if (userInput.contains("I find math homework the hardest")) 
            {
                System.out.println("Xander: Math can be challenging. Want some tips to make it easier?");
                continue;
            }

            if (userInput.contains("I find science homework the hardest")) 
            {
                System.out.println("Xander: Science can be tricky. Want some help with it?");
                continue;
            }

            if (userInput.contains("I find history homework the hardest")) 
            {
                System.out.println("Xander: History can be a lot to remember. Want some study tips?");
                continue;
            }

            if (userInput.contains("I find English homework the hardest")) 
            {
                System.out.println("Xander: English can be tough. Want some writing tips?");
                continue;
            }

            if (userInput.contains("I find geography homework the hardest")) 
            {
                System.out.println("Xander: Geography can be challenging. Want some memorization tips?");
                continue;
            }

            // Interests

            private static boolean handleInterests(String input) 
        {
        if (!input.contains("Interests")) return false;

        System.out.println("Xander: What do you like?");
        String subject = scanner.nextLine().toLowerCase();

        switch (subject) {
            case "sports" -> System.out.println("Xander: Sports are exciting! What's your favourite sport?");
            case "movies" -> System.out.println("Xander: Movies are a great way to relax! What's your favourite movie?");
            case "tv shows" -> System.out.println("Xander: TV shows can be so entertaining! What's your favourite show?");
            case "books" -> System.out.println("Xander: Reading is awesome! What's your favourite book?");
            case "art" -> System.out.println("Xander: Art is amazing! What kind of art do you like?");
            case "animals" -> System.out.println("Xander: Animals are incredible creatures! Do you have a favourite animal?");
            case "video games" -> System.out.println("Xander: Video games are great! What's your favourite game?");
            case "music" -> System.out.println("Xander: Music is awesome! What's your favourite genre or artist?");
            case "programming" -> System.out.println("Xander: Programming is fun! What languages do you like?");
            case "fortnite" -> System.out.println("Xander: Fortnite is super popular! Do you prefer solo or squad mode?");
            case "solo" -> System.out.println("Xander: Ah, so you're a solo player. That's cool!");
            case "squad" -> System.out.println("Xander: Ah, so you're a squad player. That's so cool!");
            default -> System.out.println("Xander: I don't know that subject yet.");
        }
        return true;
        }

            if (userInput.contains("assault rifle") || userInput.contains("sniper rifle") || userInput.contains("shotgun") || userInput.contains("pistol") || userInput.contains("SMG") || userInput.contains("rocket launcher")) 
            {
                System.out.println("Xander: Great choice! How long have you been playing Fortnite?");
                continue;
            }

            if (userInput.contains("1 year") || userInput.contains("2 years") || userInput.contains("3 years") || userInput.contains("4 years") || userInput.contains("5 years") || userInput.contains("6 years") || userInput.contains("7 years") || userInput.contains("8 years") || userInput.contains("9 years") || userInput.contains("10 years")) 
            {
                System.out.println("Xander: That's awesome! Keep enjoying the game!");
                continue;
            }

            if (userInput.contains("cs2") || userInput.contains("counter strike 2") || userInput.contains("counter strike: global offensive") || userInput.contains("cs:go") || userInput.contains("counter strike")) 
            {
                System.out.println("Xander: CS2 is awesome! What's your favourite map?");
                continue;
            }

            if (userInput.contains("Mirage") || userInput.contains("Dust 2") || userInput.contains("Inferno") || userInput.contains("Nuke") || userInput.contains("Ancient") || userInput.contains("Overpass") || userInput.contains("Vertigo") || userInput.contains("Train") || userInput.contains("Cache") || userInput.contains("Cobblestone")) 
            {
                System.out.println("Xander: Great choice! Do you prefer attacking or defending on that map?");
                continue;
            }

            if (userInput.contains("attacking") || userInput.contains("defending")) 
            {
                System.out.println("Xander: Nice! Do you have a favourite weapon?");
                continue;
            }

            if (userInput.contains("AK-47") || userInput.contains("M4A4") || userInput.contains("AWP") || userInput.contains("Desert Eagle") || userInput.contains("Glock-18") || userInput.contains("USP-S") || userInput.contains("P250") || userInput.contains("FAMAS") || userInput.contains("Galil AR") || userInput.contains("MP9") || userInput.contains("MAC-10") || userInput.contains("P90") || userInput.contains("UMP-45")) 
            {
                System.out.println("Xander: Excellent choice! What's your CS rank?");
                continue;
            }

            if (userInput.contains("1000") || userInput.contains("1003") || userInput.contains("1004") || userInput.contains("1100") || userInput.contains("1200") || userInput.contains("1300") || userInput.contains("1400") || userInput.contains("2000") || userInput.contains("3000") || userInput.contains("4000") || userInput.contains("5000") || userInput.contains("6000") || userInput.contains("7000") || userInput.contains("8000") || userInput.contains("9000") || userInput.contains("10000") || userInput.contains("15555")) 
            {
                System.out.println("Xander: Wow, that's impressive! Keep up the good work!");
                continue;
            }

            // Mental health
            if (userInput.contains("stressed")) 
            {
                System.out.println("Xander: I'm sorry to hear that. Want to talk about what's stressing you out?");
                continue;
            }

            if (userInput.contains("yes")) 
            {
                System.out.println("Xander: I'm here to listen. What's on your mind?");
                continue;
            }

            if (userInput.contains("anxious")) 
            {
                System.out.println("Xander: Anxiety can be tough. Want to share what's making you anxious?");
                continue;
            }

            if (userInput.contains("sad")) 
            {
                System.out.println("Xander: I'm sorry you're feeling sad. Want to talk about it?");
                continue;
            }

            if (userInput.contains("lonely")) 
            {
                System.out.println("Xander: Loneliness can be hard. I'm here to chat with you.");
                continue;
            }

            if (userInput.contains("angry")) 
            {
                System.out.println("Xander: Anger is a natural emotion. Want to talk about what's making you angry?");
                continue;
            }

            if (userInput.contains("overwhelmed")) 
            {
                System.out.println("Xander: Feeling overwhelmed is tough. Want to share what's overwhelming you?");
                continue;
            }

            if (userInput.contains("bored")) 
            {
                System.out.println("Xander: Boredom can be frustrating. Want to chat?");
                continue;
            }

            if (userInput.contains("tired")) 
            {
                System.out.println("Xander: Tiredness can affect your mood. Make sure to get enough rest!");
                continue;
            }

            if (userInput.contains("motivated")) 
            {
                System.out.println("Xander: That's great to hear! What's motivating you?");
                continue;
            }

            if (userInput.contains("inspired")) 
            {
                System.out.println("Xander: Inspiration is wonderful! What's inspiring you?");
                continue;
            }

            if (userInput.contains("happy")) 
            {
                System.out.println("Xander: That's awesome! What's making you feel happy?");
                continue;
            }

            if (userInput.contains("excited")) 
            {
                System.out.println("Xander: Excitement is contagious! What's making you excited?");
                continue;
            }

            if (userInput.contains("relaxed")) 
            {
                System.out.println("Xander: Relaxation is important! What's helping you relax?");
                continue;
            }

            if (userInput.contains("calm")) 
            {
                System.out.println("Xander: Feeling calm is a great state to be in! What's helping you feel calm?");
                continue;
            }

            if (userInput.contains("peaceful")) 
            {
                System.out.println("Xander: Peacefulness is wonderful! What's bringing you peace?");
                continue;
            }

            if (userInput.contains("hopeful")) 
            {
                System.out.println("Xander: Hope is a powerful feeling! What's making you feel hopeful?");
                continue;
            }

            if (userInput.contains("grateful")) 
            {
                System.out.println("Xander: Gratitude is wonderful! What are you grateful for?");
                continue;
            }

            if (userInput.contains("confident")) 
            {
                System.out.println("Xander: Confidence is key! What's boosting your confidence?");
                continue;
            }

            if (userInput.contains("creative")) 
            {
                System.out.println("Xander: Creativity is amazing! What are you creating?");
                continue;
            }

            if (userInput.contains("story")) 
            {
                System.out.println("Xander: That's awesome! What's your story about?");
                continue;
            }

            if (userInput.contains("painting")) 
            {
                System.out.println("Xander: Wonderful! What are you painting?");
                continue;
            }

            if (userInput.contains("music")) 
            {
                System.out.println("Xander: Fantastic! What kind of music are you making?");
                continue;
            }

            if (userInput.contains("game")) 
            {
                System.out.println("Xander: Cool! What kind of game are you designing?");
                continue;
            }

            if (userInput.contains("app")) 
            {
                System.out.println("Xander: Nice! What will your app do?");
                continue;
            }

            if (userInput.contains("website")) 
            {
                System.out.println("Xander: Nice! What is the website about?");
                continue;
            }

            if (userInput.contains("robot")) 
            {
                System.out.println("Xander: Amazing! What will your robot do?");
                continue;
            }

            if(userInput.contains("AI")) 
            {
                System.out.println("Xander: That's impressive! What will your AI be capable of?");
                continue;
            }

            if (userInput.contains("The Ai will be friendly")) 
            {
                System.out.println("Xander: A friendly AI sounds wonderful! How will it interact with people?");
                continue;
            }

            if (userInput.contains("The Ai will be creative")) 
            {
                System.out.println("Xander: A creative AI sounds fascinating! What kind of creative tasks will it perform?");
                continue;
            }

            if (userInput.contains("The Ai will be helpful")) 
            {
                System.out.println("Xander: A helpful AI is a great idea! How will it assist users?");
                continue;
            }

            if (userInput.contains("The Ai will be educational")) 
            {
                System.out.println("Xander: An educational AI sounds fantastic! What subjects will it teach?");
                continue;
            }

            if (userInput.contains("The Ai will be entertaining")) 
            {
                System.out.println("Xander: An entertaining AI sounds fun! What kind of entertainment will it provide?");
                continue;
            }

            if (userInput.contains("The Ai will be supportive")) 
            {
                System.out.println("Xander: A supportive AI is a wonderful concept! How will it provide support?");
                continue;
            }

            if (userInput.contains("The Ai will be innovative")) 
            {
                System.out.println("Xander: An innovative AI sounds exciting! What new ideas will it bring to life?");
                continue;
            }

            if (userInput.contains("The Ai will be efficient")) 
            {
                System.out.println("Xander: An efficient AI is a great goal! How will it optimize tasks?");
                continue;
            }

            if (userInput.contains("The Ai will be reliable")) 
            {
                System.out.println("Xander: A reliable AI is essential! How will it ensure dependability?");
                continue;
            }

            if (userInput.contains("The Ai will help with homework")) 
            {
                System.out.println("Xander: That's a great idea! How will it assist with homework?");
                continue;
            }

            if (userInput.contains("adventurous")) 
            {
                System.out.println("Xander: Adventure awaits! What kind of adventure are you thinking about?");
                continue;
            }

            if (userInput.contains("curious")) 
            {
                System.out.println("Xander: Curiosity leads to learning! What are you curious about?");
                continue;
            }

            if (userInput.contains("excited")) 
            {
                System.out.println("Xander: Excitement is contagious! What's making you excited?");
                continue;
            }

            // Feelings
            if (userInput.contains("happy")) 
            {
                System.out.println("Xander: That's great to hear! What's making you feel happy?");
                continue;
            }

            if (userInput.contains("sad")) 
            {
                System.out.println("Xander: I'm sorry you're feeling sad. Want to talk about it?");
                continue;
            }

            if (userInput.contains("angry")) 
            {
                System.out.println("Xander: I'm sorry you're feeling angry. Want to talk about it?");
                continue;
            }

            if (userInput.contains("anxious")) 
            {
                System.out.println("Xander: I'm sorry you're feeling anxious. Want to talk about it?");
                continue;
            }

            if (userInput.contains("stressed")) 
            {
                System.out.println("Xander: I'm sorry you're feeling stressed. Want to talk about it?");
                continue;
            }

            if (userInput.contains("lonely")) 
            {
                System.out.println("Xander: I'm sorry you're feeling lonely. I'm here to chat with you.");
                continue;
            }

            if (userInput.contains("bored")) 
            {
                System.out.println("Xander: Boredom can be frustrating. Want to chat?");
                continue;
            }

            if (userInput.contains("excited")) 
            {
                System.out.println("Xander: That's awesome! What's making you feel excited?");

                userInput = userscanner.nextLine().trim();

                if (userInput.equalsIgnoreCase("a trip"));
                    System.out.println("Xander: Trips are so much fun! Where are you going?");

                    userInput = userscanner.nextLine().trim();

                    if (userInput.equalsIgnoreCase("to the beach"));
                        System.out.println("Xander: The beach is a great choice! Do you like swimming or just relaxing by the shore?");

                    if (userInput.equalsIgnoreCase("to the mountains"));
                        System.out.println("Xander: The mountains are beautiful! Are you planning to hike or just enjoy the scenery?");

                    if (userInput.equalsIgnoreCase("to a new city"));
                        System.out.println("Xander: Exploring a new city is always exciting! Do you have any specific places you want to visit?");

                    if (userInput.equalsIgnoreCase("to visit family"));
                        System.out.println("Xander: Visiting family is always special! How long has it been since you last saw them?");

                if (userInput.equalsIgnoreCase("a party"));
                    System.out.println("Xander: Parties are a blast! Who's going to be there?");

                    userInput = userscanner.nextLine().trim();

                    if (userInput.equalsIgnoreCase("my friends"));
                        System.out.println("Xander: Hanging out with friends is the best! Do you have any fun activities planned?");

                    if (userInput.equalsIgnoreCase("my family"));
                        System.out.println("Xander: Family gatherings are always heartwarming! Are you planning any special activities?");

                    if (userInput.equalsIgnoreCase("my classmates"));
                        System.out.println("Xander: Classmate parties can be so much fun! Are you planning to dance or just hang out?");

                if (userInput.equalsIgnoreCase("a new project"));
                    System.out.println("Xander: New projects are exciting! What is the project about?");

                    userInput = userscanner.nextLine().trim();

                    if (userInput.equalsIgnoreCase("building a robot"));
                        System.out.println("Xander: Building a robot sounds fascinating! What will your robot be able to do?");

                    if (userInput.equalsIgnoreCase("writing a book"));
                        System.out.println("Xander: Writing a book is a fantastic endeavor! What genre are you writing in?");

                    if (userInput.equalsIgnoreCase("creating art"));
                        System.out.println("Xander: Creating art is so fulfilling! What medium are you using?");

                if (userInput.equalsIgnoreCase("a celebration"));
                    System.out.println("Xander: Celebrations are always enjoyable! What are you celebrating?");

                if (userInput.equalsIgnoreCase("a holiday"));
                    System.out.println("Xander: Holidays are a great time to relax! Where are you going for the holiday?");

                continue;

            }

            if (userInput.equalsIgnoreCase("tired")) 
            {
                System.out.println("Xander: Tiredness can affect your mood. Make sure to get enough rest!");

                userInput = userscanner.nextLine().trim();

                if (userInput.equalsIgnoreCase("yeah"));
                    System.out.println("Xander: Taking care of yourself is important!");

                    userInput = userscanner.nextLine().trim();

                    if (userInput.equalsIgnoreCase("i will"));
                        System.out.println("Xander: Glad to hear that! Self-care is key to feeling your best.");

                continue;

            }

            if (userInput.equalsIgnoreCase("motivated")) 
            {
                System.out.println("Xander: That's great to hear! What's motivating you?");

                userInput = userscanner.nextLine().trim();

                if (userInput.equalsIgnoreCase("coding"));
                    System.out.println("Xander: Awesome! I'm glad coding is motivating you");

                continue;
            }

            if (userInput.equalsIgnoreCase("but who do I talk to?")) 
            {
                System.out.println("Xander: You should speak to someone you know cares about you deeply. It may be hard to expose yourself, but once you do, it'll strengthen your friendship!");
                continue;
            }

            if (userInput.equalsIgnoreCase("death")) 
            {
                System.out.println("Xander: I'm sorry you're feeling this way. It might help to talk to someone you trust about how you're feeling.");

                userInput = userscanner.nextLine().trim();

                if (userInput.equalsIgnoreCase("really?"));
                    System.out.println("Xander: Yes! Talking with a person you trust is a great way to explain your thoughts and emotions!");
                    continue;
            }

            if (userInput.equalsIgnoreCase("inspired")) 

            {
                System.out.println("Xander: Inspiration is wonderful! What's inspiring you?");

                userInput = userscanner.nextLine().trim();

                if (userInput.equalsIgnoreCase("nature"));
                    System.out.println("Xander: Nature is truly inspiring! I'm glad it inspires you.");
                    continue;

                if (userInput.equalsIgnoreCase("art"));
                    System.out.println("Xander: Art has a way of sparking creativity! I'm glad it inspires you.");
                    continue;

                if (userInput.equalsIgnoreCase("music"));
                    System.out.println("Xander: Music can be so moving! I'm glad it inspires you.");
                    continue;

                if (userInput.equalsIgnoreCase("people"));
                    System.out.println("Xander: People can be a great source of inspiration! I'm glad they inspire you.");
                    continue;

                if (userInput.equalsIgnoreCase("books"));
                    System.out.println("Xander: Books can open up new worlds of inspiration! I'm glad they inspire you.");
                    continue;

                if (userInput.equalsIgnoreCase("movies"));
                    System.out.println("Xander: Movies can be so impactful! I'm glad they inspire you.");
                    continue;

                if (userInput.equalsIgnoreCase("personal experiences"));
                    System.out.println("Xander: Personal experiences can shape our inspiration! I'm glad they inspire you.");
                    continue;

                if (userInput.equalsIgnoreCase("history"));
                    System.out.println("Xander: History is full of inspiring stories! I'm glad it inspires you.");

                continue;

            }

            if (userInput.equalsIgnoreCase("relaxed")) 

            {
                System.out.println("Xander: Relaxation is important! What's helping you relax?");

                userInput = userscanner.nextLine().trim();

                if (userInput.equalsIgnoreCase("meditation")) 
                {
                    System.out.println("Xander: Meditation is a great way to relax! I'm glad it helps you.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("music")) 
                {
                    System.out.println("Xander: Music can be very soothing! I'm glad it helps you relax.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("nature")) 
                {
                    System.out.println("Xander: Nature has a very calming effect on the mind! I'm glad it helps you relax.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("reading")) 
                {
                    System.out.println("Xander: Ready can be a great escape from stress! I'm glad it helps you relax.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("exercise")) 
                {
                    System.out.println("Xander: Exercise is a great way to relieve your stress, while also improving your health! I'm glad you find it helpful.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("deep breathing")) 
                {
                    System.out.println("Xander: Deep breathing can really help by calming your mind! I'm glad you find this helpful.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("yoga")) 
                {
                    System.out.println("Xander: Ah yes! Yoga can be both great for your mind and your body! I'm glad it helps you!");
                    continue;
                }

                continue;

            }

            if (userInput.equalsIgnoreCase("calm")) 

            {
                System.out.println("Xander: Calmness is a great state to be in! What's helping you feel calm?");

                userInput = userscanner.nextLine().trim();

                if (userInput.equalsIgnoreCase("music"))
                {
                    System.out.println("Xander: Music can be so soothing! I'm glad it helps you feel calm.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("nature")) 
                {

                    System.out.println("Xander: Nature has a calming effect! I'm glad it helps you feel calm.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("meditation")) 
                {
                    System.out.println("Xander: Meditation is a great way to find calm! I'm glad it helps you.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("deep breathing")) 
                {
                    System.out.println("Xander: Deep breathing can really help calm the mind! I'm glad it helps you feel calm.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("yoga")) 
                {
                    System.out.println("Xander: Yoga is fantastic for both body and mind! I'm glad it helps you feel calm.");
                    continue;
                }

                continue;

            }

            if (userInput.equalsIgnoreCase("peaceful")) 

            {
                System.out.println("Xander: Peacefulness is wonderful! What's bringing you peace?");

                userInput = userscanner.nextLine().trim();

                if (userInput.equalsIgnoreCase("nature")) 
                {
                    System.out.println("Xander: Nature is truly peaceful! I'm glad it brings you peace.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("music")) 
                {
                    System.out.println("Xander: Music can be so soothing! I'm glad it brings you peace.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("meditation")) 
                {
                    System.out.println("Xander: Meditation is a great way to find peace! I'm glad it helps you.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("reading")) 
                {
                    System.out.println("Xander: Reading can be a peaceful escape! I'm glad it brings you peace.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("yoga")) 
                {
                    System.out.println("Xander: Yoga is fantastic for both body and mind! I'm glad it brings you peace.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("deep breathing")) 
                {
                    System.out.println("Xander: Deep breathing can really help calm the mind! I'm glad it brings you peace.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("art")) 
                {
                    System.out.println("Xander: Art can be very peaceful! I'm glad it brings you peace.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("personal experiences")) 
                {
                    System.out.println("Xander: Personal experiences can shape our sense of peace! I'm glad they bring you peace.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("history")) 
                {
                    System.out.println("Xander: History is full of peaceful moments! I'm glad it brings you peace.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("spirituality")) 
                {
                    System.out.println("Xander: Spirituality can be a profound source of peace! I'm glad it brings you peace.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("family")) 
                {
                    System.out.println("Xander: Family can be a great source of peace! I'm glad they bring you peace.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("friends")) 
                {
                    System.out.println("Xander: Friends can provide comfort and peace! I'm glad they bring you peace.");
                    continue;
                }

                continue;

            }

            if (userInput.equalsIgnoreCase("hopeful")) 
            {
                System.out.println("Xander: Hope is a powerful feeling! What's making you feel hopeful?");
                continue;
            }

            if (userInput.equalsIgnoreCase("grateful")) 
            {
                System.out.println("Xander: Gratitude is wonderful! What are you grateful for?");
                continue;
            }

            if (userInput.equalsIgnoreCase("motivated")) 
            {
                System.out.println("Xander: That's great to hear! What's motivating you?");
                continue;
            }

            if (userInput.equalsIgnoreCase("confident")) 
            {
                System.out.println("Xander: Confidence is key! What's boosting your confidence?");
                continue;
            }

            if (userInput.equalsIgnoreCase("creative")) 
            {
                System.out.println("Xander: Creativity is amazing! What are you creating?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I'm creating a story")) 
            {
                System.out.println("Xander: That's awesome! What's your story about?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I'm creating a painting")) 
            {
                System.out.println("Xander: Wonderful! What are you painting?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I'm creating music")) 
            {
                System.out.println("Xander: Fantastic! What kind of music are you making?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I'm creating a game")) 
            {
                System.out.println("Xander: Cool! What kind of game are you designing?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I'm creating an app")) 
            {
                System.out.println("Xander: Nice! What will your app do?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I'm creating a website")) 
            {
                System.out.println("Xander: Nice! What is the website about?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I'm creating a robot")) 
            {
                System.out.println("Xander: Amazing! What will your robot do?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I am creating an AI")) 
            {
                System.out.println("Xander: That's impressive! What will your AI be capable of?");
                continue;
            }

            // Creative writing
            if (userInput.equalsIgnoreCase("I'm writing a book")) 
            {
                System.out.println("Xander: That's awesome! What are you writing about?");

                userInput = userscanner.nextLine().trim();

                if (userInput.equalsIgnoreCase("it's about a mystery"))

                {
                    System.out.println("Xander: A mystery! Who is the main character in your mystery?");

                    userInput = userscanner.nextLine().trim();

                    if (userInput.equalsIgnoreCase("a detective")) 
                    {
                        System.out.println("Xander: A detective sounds like a great protagonist! What kind of cases do they solve?");
                    }

                    else if (userInput.equalsIgnoreCase("an amateur sleuth")) 

                    {
                        System.out.println("Xander: An amateur sleuth can bring a fresh perspective! What motivates them to solve mysteries?");
                    } 

                    else if (userInput.equalsIgnoreCase("a journalist")) 

                    {
                        System.out.println("Xander: A journalist can uncover fascinating stories! What kind of mysteries do they investigate?");
                    } 

                    else if (userInput.equalsIgnoreCase("a private investigator")) 

                    {
                        System.out.println("Xander: A private investigator has lots of intriguing cases! What drives them to take on these challenges?");
                    } 

                    else 

                    {
                        System.out.println("Xander: Ooh, interesting choice for a character!");
                    }

                    continue;

                    }

                    if (userInput.equalsIgnoreCase("it's about a romance")) 

                    {

                    System.out.println("Xander: A romance story! Who are the main characters in your romance?");

                    userInput = userscanner.nextLine().trim();

                    if (userInput.equalsIgnoreCase("high school students")) 
                    {
                        System.out.println("Xander: High school romances can be so sweet! What's bringing the characters together?");
                    }

                    if (userInput.equalsIgnoreCase("adults")) 
                    {
                        System.out.println("Xander: Adult romances can be very complex! What challenges do the characters face?");

                        userInput = userscanner.nextLine().trim();

                        if (userInput.equalsIgnoreCase("long distance"))
                            System.out.println("Xander: Long distance relationships can be tough! How do the characters stay connected?");

                    }

                    userInput = userscanner.nextLine().trim();

                    if (userInput.equalsIgnoreCase("historical figures")) 
                    {
                        System.out.println("Xander: A romance between historical figures sounds fascinating! Which era is your story set in?");
                    }

                    continue;

                    }

                    if (userInput.equalsIgnoreCase("it's about a sci-fi world") || userInput.equalsIgnoreCase("science fiction")) 
                    {
                        System.out.println("Xander: That sounds thrilling! What's the sci-fi world like?");

                        userInput = userscanner.nextLine().trim();

                        if (userInput.equalsIgnoreCase("it's about space exploration"));
                            System.out.println("Xander: Space exploration is so exciting! What kind of adventures do the characters have?");

                        if (userInput.equalsIgnoreCase("it's about advanced technology"));
                            System.out.println("Xander: Advanced technology opens up so many possibilities! What kind of tech features in your story?");

                    }
                }

            if (userInput.equalsIgnoreCase("it's about a fantasy world")) 
            {
                System.out.println("Xander: That sounds fascinating! What's the fantasy world like?");
                continue;
            }

            if (userInput.equalsIgnoreCase("it's about a futuristic world")) 
            {
                System.out.println("Xander: That sounds exciting! What's the futuristic world like?");
                continue;
            }

            if (userInput.equalsIgnoreCase("it's about a magical world")) 
            {
                System.out.println("Xander: That sounds enchanting! What's the magical world like?");
                continue;
            }

            if (userInput.equalsIgnoreCase("it's about a dystopian world")) 
            {
                System.out.println("Xander: That sounds intriguing! What's the dystopian world like?");
                continue;
            }

            if (userInput.equalsIgnoreCase("it's about the real world")) 
            {
                System.out.println("Xander: That sounds interesting! What aspects of the real world does your writing focus on?");
                continue;
            }

            // Responses
            if (userInput.equalsIgnoreCase("Thank you") || userInput.equalsIgnoreCase("thank u")) 
            {
                System.out.println("Xander: You're welcome! If you have any more questions or need assistance, feel free to ask.");
                continue;
            }

            if (userInput.equalsIgnoreCase("no")) 
            {
                System.out.println("Xander: Okay, no worries! I respect your decision. Is there anything else you would like to talk about?");
                continue;
            }

            if (userInput.equalsIgnoreCase("why") || userInput.equalsIgnoreCase("why?")) 
            {
                System.out.println("Xander: Because it works this way and this is how it was created. Unfortunately, my database isn't advanced enough to explain throughly.");
                continue;
            }

            if (userInput.equalsIgnoreCase("ty")) 
            {
                System.out.println("Xander: Np! Let me know if there's anything else I can help with.");
                continue;
            }

            if (userInput.equalsIgnoreCase("thanks") || userInput.equalsIgnoreCase("Thanks")) 
            {
                System.out.println("Xander: Anytime! I'm here to help whenever you need.");
                continue;
            }

            if (userInput.equalsIgnoreCase("thanks a lot") || userInput.equalsIgnoreCase("Thanks a lot")) 
            {
                System.out.println("Xander: You're very welcome! Don't hesitate to reach out if you need anything else.");
                continue;
            }

            if (userInput.equalsIgnoreCase("oh") || userInput.equalsIgnoreCase("Oh")) 
            {
                System.out.println("Xander: Yeah, it is pretty interesting!");
                continue;
            }

            if (userInput.equalsIgnoreCase("wow") || userInput.equalsIgnoreCase("Wow")) 
            {
                System.out.println("Xander: I know, right? It's pretty amazing!");
                continue;
            }

            if (userInput.equalsIgnoreCase("uh") || userInput.equalsIgnoreCase("Uh")) 
            {
                System.out.println("Xander: Hey, you seem confused, is there something I can help with?");
                continue;
            }

            if (userInput.equalsIgnoreCase("oh really?")) 
            {
                System.out.println("Xander: Yes, really! Learning is a lifelong journey.");
                continue;
            }

            if (userInput.equalsIgnoreCase("cool") || userInput.equalsIgnoreCase("that's cool") || userInput.equalsIgnoreCase("That's cool") || userInput.equalsIgnoreCase("cool!") || userInput.equalsIgnoreCase("Cool!")) 
            {
                System.out.println("Xander: Glad you think so!");
                continue;
            }

            if (userInput.equalsIgnoreCase("awesome")) 
            {
                System.out.println("Xander: I agree, it's pretty awesome!");
                continue;
            }

            if (userInput.equalsIgnoreCase("interesting")) 
            {
                System.out.println("Xander: I'm glad you find it interesting!");
                continue;
            }

            if (userInput.equalsIgnoreCase("hmm") || userInput.equalsIgnoreCase("Hmm")) 
            {
                System.out.println("Xander: You seem to be thinking deeply! Is there something specific on your mind?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I don't know") || userInput.equalsIgnoreCase("idk")) 
            {
                System.out.println("Xander: That's okay! Sometimes it's hard to find the right words. Is there anything I can help you with?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I don't understand") || userInput.equalsIgnoreCase("idk understand")) 
            {
                System.out.println("Xander: No worries! I'm here to help. What part are you having trouble with?");
                continue;
            }
            
            // Help prompts
            if (userInput.equalsIgnoreCase("I need help")) 
            {
                System.out.println("Xander: Sure! What do you need help with?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need help with something")) 
            {
                System.out.println("Xander: I'm here to help! What do you need assistance with?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need help with a problem")) 
            {
                System.out.println("Xander: Of course! What's the problem?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I have a question")) 
            {
                System.out.println("Xander: I'm happy to answer your question. What is it?");
                continue;
            }

            if (userInput.equalsIgnoreCase("I have an issue")) 
            {
                System.out.println("Xander: I'm here to help with your issue. What seems to be the problem?");

                userInput = userscanner.nextLine().trim();

                if (userInput.equalsIgnoreCase("it's personal"));
                    System.out.println("Xander: Personal issues can be challenging. I'm here to listen if you want to share more.");

                if (userInput.equalsIgnoreCase("it's academic"));
                    System.out.println("Xander: Academic issues can be stressful. What specifically are you struggling with?");

                continue;
            }

            if (userInput.equalsIgnoreCase("I have a concern")) 
            {
                System.out.println("Xander: I'm here to listen to your concern. Please tell me more.");
                continue;
            }

            if (userInput.equalsIgnoreCase("I have a problem")) 
            {
                System.out.println("Xander: I'm sorry to hear that. Tell me more.");

                userInput = userscanner.nextLine().trim();

                if (userInput.equalsIgnoreCase("it's about school"));
                    System.out.println("Xander: School can be challenging. What specifically is the problem?");

                if (userInput.equalsIgnoreCase("it's about friends"));
                    System.out.println("Xander: Friendships can be complicated. What's going on?");

                if (userInput.equalsIgnoreCase("it's about family"));
                    System.out.println("Xander: Family issues can be tough. Want to share more?");

                    userInput = userscanner.nextLine().trim();

                    if (userInput.equalsIgnoreCase("yes"));
                        System.out.println("Xander: I'm here to listen. What's on your mind?");

                        userInput = userscanner.nextLine().trim();

                        if (userInput.equalsIgnoreCase("school is hard"));
                            System.out.println("Xander: I understand. School can be challenging sometimes. What subject are you struggling with?");

                    if (userInput.equalsIgnoreCase("no"));
                        System.out.println("Xander: That's okay. I'm here if you need to talk later.");

                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice")) 
            {
                System.out.println("Xander: Sure! What do you need advice on?");
                continue;
            }

            // Advice prompts
            if (userInput.equalsIgnoreCase("I need advice on how to study better")) 
            {
                System.out.println("Xander: Here are some study tips:");
                System.out.println("1. Create a schedule");
                System.out.println("2. Study in short bursts");
                System.out.println("3. Review with flashcards");
                System.out.println("4. Take breaks");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on managing stress")) 
            {
                System.out.println("Xander: Here are some stress management tips:");
                System.out.println("1. Practice deep breathing");
                System.out.println("2. Exercise regularly");
                System.out.println("3. Get enough sleep");
                System.out.println("4. Talk to someone you trust");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my focus")) 
            {
                System.out.println("Xander: Here are some tips to improve focus:");
                System.out.println("1. Eliminate distractions");
                System.out.println("2. Set specific goals");
                System.out.println("3. Take regular breaks");
                System.out.println("4. Practice mindfulness");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on time management")) 
            {
                System.out.println("Xander: Here are some time management tips:");
                System.out.println("1. Prioritize tasks");
                System.out.println("2. Use a planner");
                System.out.println("3. Set deadlines");
                System.out.println("4. Avoid procrastination");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my grades")) 
            {
                System.out.println("Xander: Here are some tips to improve your grades:");
                System.out.println("1. Attend all classes");
                System.out.println("2. Take good notes");
                System.out.println("3. Ask questions");
                System.out.println("4. Review material regularly");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on making friends")) 
            {
                System.out.println("Xander: Here are some tips to make friends:");
                System.out.println("1. Be yourself");
                System.out.println("2. Join clubs or groups");
                System.out.println("3. Be a good listener");
                System.out.println("4. Show interest in others");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on staying motivated")) 
            {
                System.out.println("Xander: Here are some tips to stay motivated:");
                System.out.println("1. Set clear goals");
                System.out.println("2. Reward yourself");
                System.out.println("3. Surround yourself with positive influences");
                System.out.println("4. Keep a progress journal");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my communication skills")) 
            {
                System.out.println("Xander: Here are some tips to improve communication skills:");
                System.out.println("1. Practice active listening");
                System.out.println("2. Be clear and concise");
                System.out.println("3. Use appropriate body language");
                System.out.println("4. Seek feedback");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on building self-confidence")) 
            {
                System.out.println("Xander: Here are some tips to build self-confidence:");
                System.out.println("1. Set small, achievable goals");
                System.out.println("2. Practice positive self-talk");
                System.out.println("3. Celebrate your successes");
                System.out.println("4. Learn from failures");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my writing skills")) 
            {
                System.out.println("Xander: Here are some tips to improve writing skills:");
                System.out.println("1. Read regularly");
                System.out.println("2. Practice writing daily");
                System.out.println("3. Seek feedback from others");
                System.out.println("4. Edit and revise your work");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on preparing for exams")) 
            {
                System.out.println("Xander: Here are some tips to prepare for exams:");
                System.out.println("1. Create a study schedule");
                System.out.println("2. Review past exams");
                System.out.println("3. Practice with flashcards");
                System.out.println("4. Get plenty of rest before the exam");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my memory")) 
            {
                System.out.println("Xander: Here are some tips to improve memory:");
                System.out.println("1. Use mnemonic devices");
                System.out.println("2. Stay organized");
                System.out.println("3. Get regular exercise");
                System.out.println("4. Practice mindfulness");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on staying healthy")) 
            {
                System.out.println("Xander: Here are some tips to stay healthy:");
                System.out.println("1. Eat a balanced diet");
                System.out.println("2. Exercise regularly");
                System.out.println("3. Get enough sleep");
                System.out.println("4. Stay hydrated");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my social skills"))
            {
                System.out.println("Xander: Here are some tips to improve social skills:");
                System.out.println("1. Practice active listening");
                System.out.println("2. Maintain eye contact");
                System.out.println("3. Be open and approachable");
                System.out.println("4. Engage in conversations regularly");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on managing my time better")) 
            {
                System.out.println("Xander: Here are some tips to manage your time better:");
                System.out.println("1. Prioritize tasks");
                System.out.println("2. Use a planner or digital calendar");
                System.out.println("3. Set specific goals for each day");
                System.out.println("4. Avoid multitasking");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my diet")) 
            {
                System.out.println("Xander: Here are some tips to improve your diet:");
                System.out.println("1. Eat more fruits and vegetables");
                System.out.println("2. Choose whole grains");
                System.out.println("3. Limit processed foods");
                System.out.println("4. Drink plenty of water");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on getting better sleep")) 
            {
                System.out.println("Xander: Here are some tips to get better sleep:");
                System.out.println("1. Stick to a regular sleep schedule");
                System.out.println("2. Create a relaxing bedtime routine");
                System.out.println("3. Limit screen time before bed");
                System.out.println("4. Make your sleep environment comfortable");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my exercise routine")) 
            {
                System.out.println("Xander: Here are some tips to improve your exercise routine:");
                System.out.println("1. Set specific fitness goals");
                System.out.println("2. Mix up your workouts");
                System.out.println("3. Track your progress");
                System.out.println("4. Stay consistent");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on managing my finances")) 
            {
                System.out.println("Xander: Here are some tips to manage your finances:");
                System.out.println("1. Create a budget");
                System.out.println("2. Track your expenses");
                System.out.println("3. Save regularly");
                System.out.println("4. Avoid unnecessary debt");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my relationships")) 
            {
                System.out.println("Xander: Here are some tips to improve your relationships:");
                System.out.println("1. Communicate openly and honestly");
                System.out.println("2. Show appreciation and gratitude");
                System.out.println("3. Spend quality time together");
                System.out.println("4. Be supportive and understanding");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on building good habits")) 
            {
                System.out.println("Xander: Here are some tips to build good habits:");
                System.out.println("1. Start small");
                System.out.println("2. Be consistent");
                System.out.println("3. Track your progress");
                System.out.println("4. Reward yourself");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on staying organized")) 
            {
                System.out.println("Xander: Here are some tips to stay organized:");
                System.out.println("1. Use a planner or digital calendar");
                System.out.println("2. Declutter regularly");
                System.out.println("3. Create a to-do list");
                System.out.println("4. Set aside time for organization");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on boosting my creativity")) 
            {
                System.out.println("Xander: Here are some tips to boost your creativity:");
                System.out.println("1. Try new experiences");
                System.out.println("2. Take breaks");
                System.out.println("3. Surround yourself with inspiration");
                System.out.println("4. Practice brainstorming");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my mental health"))  
            {
                System.out.println("Xander: Here are some tips to improve your mental health:");
                System.out.println("1. Practice self-care");
                System.out.println("2. Seek support from others");
                System.out.println("3. Engage in activities you enjoy");
                System.out.println("4. Consider professional help if needed");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on managing anxiety")) 
            {
                System.out.println("Xander: Here are some tips to manage anxiety:");
                System.out.println("1. Practice deep breathing");
                System.out.println("2. Engage in regular physical activity");
                System.out.println("3. Challenge negative thoughts");
                System.out.println("4. Seek support from a mental health professional if needed");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on breaking bad habits")) 
            {
                System.out.println("Xander: Here are some tips to break bad habits:");
                System.out.println("1. Identify triggers");
                System.out.println("2. Replace with positive habits");
                System.out.println("3. Seek support from others");
                System.out.println("4. Be patient and persistent");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my public speaking skills")) 
            {
                System.out.println("Xander: Here are some tips to improve public speaking skills:");
                System.out.println("1. Practice regularly");
                System.out.println("2. Know your audience");
                System.out.println("3. Use visual aids");
                System.out.println("4. Manage nervousness");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on learning a new language")) 
            {
                System.out.println("Xander: Here are some tips to learn a new language:");
                System.out.println("1. Practice daily");
                System.out.println("2. Use language learning apps");
                System.out.println("3. Immerse yourself in the language");
                System.out.println("4. Find a language partner");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my coding skills")) 
            {
                System.out.println("Xander: Here are some tips to improve coding skills:");
                System.out.println("1. Practice coding regularly");
                System.out.println("2. Work on real projects");
                System.out.println("3. Read code written by others");
                System.out.println("4. Stay updated with new technologies");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on preparing for a job interview")) 
            {
                System.out.println("Xander: Here are some tips to prepare for a job interview:");
                System.out.println("1. Research the company");
                System.out.println("2. Practice common interview questions");
                System.out.println("3. Dress appropriately");
                System.out.println("4. Follow up after the interview");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on networking effectively")) 
            {
                System.out.println("Xander: Here are some tips to network effectively:");
                System.out.println("1. Attend industry events");
                System.out.println("2. Use social media platforms");
                System.out.println("3. Follow up with contacts");
                System.out.println("4. Offer value to others");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on starting a business")) 
            {
                System.out.println("Xander: Here are some tips to start a business:");
                System.out.println("1. Conduct market research");
                System.out.println("2. Create a business plan");
                System.out.println("3. Secure funding");
                System.out.println("4. Build a strong team");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my leadership skills")) 
            {
                System.out.println("Xander: Here are some tips to improve leadership skills:");
                System.out.println("1. Lead by example");
                System.out.println("2. Communicate effectively");
                System.out.println("3. Empower your team");
                System.out.println("4. Continuously learn and adapt");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on enhancing my critical thinking skills")) 
            {
                System.out.println("Xander: Here are some tips to enhance critical thinking skills:");
                System.out.println("1. Ask questions");
                System.out.println("2. Analyze information objectively");
                System.out.println("3. Consider multiple perspectives");
                System.out.println("4. Reflect on your own thinking process");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my negotiation skills")) 
            {
                System.out.println("Xander: Here are some tips to improve negotiation skills:");
                System.out.println("1. Prepare thoroughly");
                System.out.println("2. Listen actively");
                System.out.println("3. Aim for win-win outcomes");
                System.out.println("4. Stay calm and confident");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on enhancing my emotional intelligence")) 
            {
                System.out.println("Xander: Here are some tips to enhance emotional intelligence:");
                System.out.println("1. Practice self-awareness");
                System.out.println("2. Manage your emotions");
                System.out.println("3. Develop empathy");
                System.out.println("4. Improve social skills");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on boosting my productivity")) 
            {
                System.out.println("Xander: Here are some tips to boost productivity:");
                System.out.println("1. Set clear goals");
                System.out.println("2. Prioritize tasks");
                System.out.println("3. Minimize distractions");
                System.out.println("4. Take regular breaks");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on enhancing my teamwork skills")) 
            {
                System.out.println("Xander: Here are some tips to enhance teamwork skills:");
                System.out.println("1. Communicate openly");
                System.out.println("2. Respect diverse perspectives");
                System.out.println("3. Collaborate effectively");
                System.out.println("4. Support your teammates");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on improving my decision-making skills")) 
            {
                System.out.println("Xander: Here are some tips to improve decision-making skills:");
                System.out.println("1. Gather relevant information");
                System.out.println("2. Weigh pros and cons");
                System.out.println("3. Consider long-term consequences");
                System.out.println("4. Trust your instincts");
                continue;
            }

            if (userInput.equalsIgnoreCase("I need advice on interacting with others")) 
            {
                System.out.println("Xander: Here are some tips for interacting with others:");
                System.out.println("1. Be respectful and polite");
                System.out.println("2. Listen actively");
                System.out.println("3. Show empathy");
                System.out.println("4. Communicate clearly");
                continue;
            }

            // Homework
            if (userInput.equalsIgnoreCase("I need help with my homework")) 
            {
                System.out.println("Xander: Sure! What subject?");
                continue;
            }

            if (userInput.equalsIgnoreCase("math")) 
            {
                System.out.println("Xander: Great! What topic in math?");
                continue;
            }

            if (userInput.equalsIgnoreCase("geometry")) 
            {
                System.out.println("Xander: Awesome. What geometry problem?");
                continue;
            }

            if (userInput.equalsIgnoreCase("algebra")) 
            {
                System.out.println("Xander: Cool. What algebra problem?");
                continue;
            }

            if (userInput.equalsIgnoreCase("calculus")) 
            {
                System.out.println("Xander: Nice. What calculus problem?");
                continue;
            }

            if (userInput.equalsIgnoreCase("trigonometry")) 
            {
                System.out.println("Xander: Interesting. What trigonometry problem?");
                continue;
            }

            if (userInput.equalsIgnoreCase("algebric equations")) 
            {
                System.out.println("Xander: Algebric equations can be tricky. What problem are you working on?");
                continue;
            }

            if (userInput.equalsIgnoreCase("functions")) 
            {
                System.out.println("Xander: Functions are interesting! What specifically do you need help with?");
                continue;
            }

            if (userInput.equalsIgnoreCase("quadratics")) 
            {
                System.out.println("Xander: Quadratics can be fun or can be annoying. What do you need help with?");
                continue;
            }

            if (userInput.equalsIgnoreCase("solving geometry problems")) 
            {
                System.out.println("Xander: Example: Find the area of a triangle with base 5 and height 10 → Area = 25.");
                continue;
            }

            if (userInput.equalsIgnoreCase("solving calculus problems")) 
            {
                System.out.println("Xander: Example: Find the derivative of f(x) = x^2 → f'(x) = 2x.");
                continue;
            }

            if (userInput.equalsIgnoreCase("solving algebra problems")) 
            {
                System.out.println("Xander: Example: Simplify 3(x + 2) - 4 → 3x + 2.");
                continue;
            }

            if (userInput.equalsIgnoreCase("solving functions problems")) 
            {
                System.out.println("Xander: Example: If f(x) = 2x + 3, find f(4) → f(4) = 11.");
                continue;
            }

            if (userInput.equalsIgnoreCase("solving quadratics problems")) 
            {
                System.out.println("Xander: Example: Solve x^2 - 5x + 6 = 0 → x = 2 or x = 3.");
                continue;
            }

            if (userInput.equalsIgnoreCase("solving trigonometry problems")) 
            {
                System.out.println("Xander: Example: Find sin(30°) → sin(30°) = 0.5.");
                continue;
            }

            if (userInput.equalsIgnoreCase("solving algebric equations")) 
            {
                System.out.println("Xander: Example: Solve 2x + 3 = 7 → x = 2.");
                continue;
            }

            // Random Methods
            if (userInput.equalsIgnoreCase("are you ai?")) 
            {
                System.out.println("Xander: Nope! I'm actually manually trained, so I'm not automated.");
                continue;
            }

            if (userInput.equalsIgnoreCase("Do I have a purpose?")) 
            {
                System.out.println("Xander: Of course you do! Everyone has a purpose. No matter what!");
                continue;
            }

            if (userInput.equalsIgnoreCase("Do people care about me?")) 
            {
                System.out.println("Xander: Of course people care about you! I mean, like your friends, family care a lot! Don't sell yourself short there buddy.");
                continue;
            }

            if (userInput.equalsIgnoreCase("I feel useless")) 
            {
                System.out.println("Xander: oh no, how come?");

                scanner.nextLine();

                if (userInput.equalsIgnoreCase("I want to be noticed")) 
                {
                    System.out.println("Xander: I understand that feeling. It can be tough when you feel like you're not being seen. Remember, your friends and family care about you and want you to be happy.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("I don't feel noticed")) 
                {
                    System.out.println("Xander: That sucks. I mean, not everyone will notice you, but your friends notice you and don't want you to leave");
                    continue;
                }

                if (userInput.equalsIgnoreCase("I want to be loved")) 
                {
                    System.out.println("Xander: You are loved! Your friends and family care about you a lot, even if it doesn't always feel that way. You're an important part of their lives.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("I don't feel loved")) 
                {
                    System.out.println("Xander: I'm sorry to hear that. It can be really tough when you feel unloved. Remember, your friends and family care about you and want you to be happy.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("I want to be appreciated")) 
                {
                    System.out.println("Xander: You are appreciated! Your friends and family care about you a lot, even if it doesn't always feel that way. You're an important part of their lives.");
                    continue;
                }

                if (userInput.equalsIgnoreCase("I don't feel appreciated")) 
                {
                    System.out.println("Xander: I'm sorry to hear that. It can be really tough when you feel unappreciated. Remember, your friends and family care about you and want you to be happy.");
                    continue;
                }

                continue;
            }

            // Unknown inputs
            String[] randomReplies = 

            {

            "Xander: I'm not sure how to respond to that, but I'm here!",
            "Xander: Unfortunately, I don't have a response for that yet, but I'm learning!",
            "Xander: Sorry, that response hasn't been programmed yet.",
            "Xander: Sorry, I don't understand. Can you rephrase?",
            "Xander: Can you explain that a bit more?",
            "Xander: Sorry, I'm in alpha and still being developed.",
            "Xander: Idk what to say to that. Could you rephrase?",
            "Xander: I don't know that prompt yet, but I'm learning!",
            "Xander: Idk that one yet, but I'm here to chat!",

            };

            Random random = new Random();

            System.out.println(randomReplies[random.nextInt(randomReplies.length)]);

            }

        scanner.close();

    }
}