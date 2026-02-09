import java.util.Arrays;

/**
 * VirtualXander - Your Friendly Virtual Companion
 * 
 * This is the main entry point for the VirtualXander chatbot.
 * It uses a modular architecture with:
 * - Intent Recognition System
 * - Conversation Context Manager
 * - Response Generator with templates
 * - State Machine for conversation flow
 * 
 * The chatbot provides support for:
 * - Greetings & Basic Chat
 * - Mental Health Support
 * - Homework/Academic Help
 * - Entertainment/Gaming
 * - Creative Writing Assistance
 * - General Advice
 * 
 * @author VirtualXander Development Team
 * @version 2.0 (Phase 1 Complete)
 */
public class VirtualXander {
    
    public static void main(String[] args) {
        System.out.println("Initializing VirtualXander Core System...");
        System.out.println();
        
        // Create and start the VirtualXander core
        VirtualXanderCore core = new VirtualXanderCore();
        
        // Check if running in test mode (arguments provided)
        if (args != null && args.length > 0) {
            handleCommandLineMode(args, core);
        } else {
            // Start interactive chat mode
            core.start();
        }
    }
    
    /**
     * Handles command-line mode for testing and automation
     */
    private static void handleCommandLineMode(String[] args, VirtualXanderCore core) {
        if (args[0].equalsIgnoreCase("--test") || args[0].equalsIgnoreCase("-t")) {
            // Test mode - run sample conversation
            runTestMode(core);
        } else if (args[0].equalsIgnoreCase("--help") || args[0].equalsIgnoreCase("-h")) {
            printHelp();
        } else if (args[0].equalsIgnoreCase("--api") || args[0].equalsIgnoreCase("-a")) {
            // API mode - process single message
            String message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
            if (!message.isEmpty()) {
                String response = core.processMessage(message);
                System.out.println("Xander: " + response);
            }
        } else {
            // Default: treat all args as a single message
            String message = String.join(" ", args);
            String response = core.processMessage(message);
            System.out.println("Xander: " + response);
        }
    }
    
    /**
     * Runs test mode with sample conversation
     */
    private static void runTestMode(VirtualXanderCore core) {
        String[] testMessages = {
            "Hello!",
            "How are you?",
            "I'm doing great!",
            "I need help with my math homework",
            "What can you help me with?",
            "I'm feeling a bit stressed",
            "That's helpful, thank you!",
            "bye"
        };
        
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              VirtualXander Test Mode                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        
        for (String message : testMessages) {
            System.out.println("You: " + message);
            String response = core.processMessage(message);
            System.out.println("Xander: " + response);
            System.out.println();
            
            if (!core.isRunning()) {
                break;
            }
        }
        
        System.out.println("Test mode completed.");
    }
    
    /**
     * Prints help information
     */
    private static void printHelp() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              VirtualXander Help                           ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Usage: java VirtualXander [OPTIONS] [MESSAGE]             ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Options:                                                  ║");
        System.out.println("║   -h, --help       Show this help message                 ║");
        System.out.println("║   -t, --test       Run test mode with sample conversation ║");
        System.out.println("║   -a, --api        Process single message and exit        ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ Interactive Commands:                                     ║");
        System.out.println("║   exit, bye        End conversation                       ║");
        System.out.println("║   reset            Start new conversation                 ║");
        System.out.println("║   status           Show current status                    ║");
        System.out.println("║   help             Show help message                      ║");
        System.out.println("║   history          Show conversation history              ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }
}

