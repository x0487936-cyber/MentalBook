import java.util.*;
import java.time.*;
import java.time.format.*;
import java.io.*;
import java.nio.file.*;

/**
 * Logging and Analytics System for VirtualXander
 * Comprehensive logging and analytics tracking
 * Part of Phase 4: Code Quality
 */
public class Logger {
    
    private static Logger instance;
    private LogLevel currentLevel;
    private String logFilePath;
    private boolean consoleOutput;
    private boolean fileOutput;
    private PrintWriter fileWriter;
    private Map<String, Integer> logCounts;
    private Analytics analytics;
    
    private Logger() {
        this.currentLevel = LogLevel.INFO;
        this.logFilePath = "logs/virtualxander.log";
        this.consoleOutput = true;
        this.fileOutput = false; // Default to false, enable after config loads
        this.logCounts = new HashMap<>();
        this.analytics = new Analytics();
        initializeLogging();
    }
    
    /**
     * Log levels
     */
    public enum LogLevel {
        DEBUG(0),
        INFO(1),
        WARN(2),
        ERROR(3),
        FATAL(4);
        
        private final int value;
        
        LogLevel(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    /**
     * Gets singleton instance
     */
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    /**
     * Initializes logging
     */
    private void initializeLogging() {
        // Create logs directory if it doesn't exist
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        
        // Open log file
        try {
            fileWriter = new PrintWriter(new FileWriter(logFilePath, true), true);
        } catch (IOException e) {
            System.err.println("Error opening log file: " + e.getMessage());
            fileOutput = false;
        }
        
        // Load configuration
        loadConfiguration();
    }
    
    /**
     * Loads logging configuration
     */
    private void loadConfiguration() {
        try {
            // Try to load Configuration from classpath
            Class<?> configClass = Class.forName("Configuration");
            Object config = configClass.getMethod("getInstance").invoke(null);
            
            String level = (String) configClass.getMethod("getProperty", String.class, String.class)
                .invoke(config, "logging.level", "INFO");
            setLogLevel(LogLevel.valueOf(level));
            
            consoleOutput = (Boolean) configClass.getMethod("getBooleanProperty", String.class, boolean.class)
                .invoke(config, "logging.console_output", true);
            
            String filePath = (String) configClass.getMethod("getProperty", String.class, String.class)
                .invoke(config, "logging.file_path", "logs/virtualxander.log");
            if (filePath != null) {
                logFilePath = filePath;
                fileOutput = true;
            }
            
        } catch (Exception e) {
            // Configuration not available, use defaults
            System.out.println("Logger: Using default configuration");
            fileOutput = false;
        }
    }
    
    /**
     * Sets the log level
     */
    public void setLogLevel(LogLevel level) {
        this.currentLevel = level;
        log(LogLevel.INFO, "Logger", "Log level set to: " + level);
    }
    
    /**
     * Gets current log level
     */
    public LogLevel getLogLevel() {
        return currentLevel;
    }
    
    /**
     * Logs a message
     */
    public void log(LogLevel level, String source, String message) {
        // Check if message should be logged
        if (level.getValue() < currentLevel.getValue()) {
            return;
        }
        
        // Format timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        // Format log entry
        String logEntry = String.format("[%s] [%s] [%s] %s", 
            timestamp, level.name(), source, message);
        
        // Update counts
        logCounts.merge(level.name(), 1, Integer::sum);
        
        // Log to console
        if (consoleOutput) {
            System.out.println(logEntry);
        }
        
        // Log to file
        if (fileOutput && fileWriter != null) {
            fileWriter.println(logEntry);
        }
        
        // Track in analytics
        if (level == LogLevel.ERROR || level == LogLevel.FATAL) {
            analytics.trackError(message);
        }
        analytics.trackLog(level.name());
    }
    
    /**
     * Convenience methods for logging
     */
    public void debug(String source, String message) {
        log(LogLevel.DEBUG, source, message);
    }
    
    public void info(String source, String message) {
        log(LogLevel.INFO, source, message);
    }
    
    public void warn(String source, String message) {
        log(LogLevel.WARN, source, message);
    }
    
    public void error(String source, String message) {
        log(LogLevel.ERROR, source, message);
    }
    
    public void error(String source, String message, Throwable throwable) {
        log(LogLevel.ERROR, source, message + " - Exception: " + throwable.getMessage());
        if (consoleOutput) {
            throwable.printStackTrace();
        }
    }
    
    public void fatal(String source, String message) {
        log(LogLevel.FATAL, source, message);
    }
    
    public void fatal(String source, String message, Throwable throwable) {
        log(LogLevel.FATAL, source, message + " - Exception: " + throwable.getMessage());
        if (consoleOutput) {
            throwable.printStackTrace();
        }
    }
    
    /**
     * Tracks conversation event
     */
    public void trackConversationEvent(String eventType, Map<String, Object> details) {
        analytics.trackConversationEvent(eventType, details);
        info("Analytics", "Conversation event: " + eventType);
    }
    
    /**
     * Tracks user interaction
     */
    public void trackUserInteraction(String inputType, String intent) {
        Map<String, Object> details = new HashMap<>();
        details.put("input_type", inputType);
        details.put("intent", intent);
        analytics.trackInteraction(inputType, intent);
    }
    
    /**
     * Gets log statistics
     */
    public Map<String, Integer> getLogStatistics() {
        return new HashMap<>(logCounts);
    }
    
    /**
     * Gets log summary
     */
    public String getLogSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Log Summary\n");
        summary.append("========\n");
        summary.append("Current Log Level: ").append(currentLevel).append("\n");
        summary.append("Console Output: ").append(consoleOutput).append("\n");
        summary.append("File Output: ").append(fileOutput).append("\n");
        summary.append("Log File: ").append(logFilePath).append("\n\n");
        
        summary.append("Log Counts:\n");
        for (Map.Entry<String, Integer> entry : logCounts.entrySet()) {
            summary.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        return summary.toString();
    }
    
    /**
     * Clears log statistics
     */
    public void clearLogStatistics() {
        logCounts.clear();
        info("Logger", "Log statistics cleared");
    }
    
    /**
     * Closes the logger
     */
    public void close() {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
    
    /**
     * Gets analytics instance
     */
    public Analytics getAnalytics() {
        return analytics;
    }
    
    /**
     * Analytics inner class for tracking metrics
     */
    public static class Analytics {
        private boolean enabled;
        private List<AnalyticsEvent> events;
        private Map<String, Integer> interactionCounts;
        private Map<String, Integer> intentCounts;
        private Map<String, Integer> errorCounts;
        private LocalDateTime sessionStart;
        private int totalMessages;
        private int totalErrors;
        
        private Analytics() {
            this.enabled = true;
            this.events = new ArrayList<>();
            this.interactionCounts = new HashMap<>();
            this.intentCounts = new HashMap<>();
            this.errorCounts = new HashMap<>();
            this.sessionStart = LocalDateTime.now();
            this.totalMessages = 0;
            this.totalErrors = 0;
        }
        
        /**
         * Sets analytics enabled state
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        /**
         * Tracks a conversation event
         */
        public void trackConversationEvent(String eventType, Map<String, Object> details) {
            if (!enabled) return;
            
            AnalyticsEvent event = new AnalyticsEvent(
                "conversation." + eventType,
                LocalDateTime.now(),
                details
            );
            events.add(event);
        }
        
        /**
         * Tracks user interaction
         */
        public void trackInteraction(String inputType, String intent) {
            if (!enabled) return;
            
            interactionCounts.merge(inputType, 1, Integer::sum);
            intentCounts.merge(intent, 1, Integer::sum);
            totalMessages++;
            
            trackConversationEvent("interaction", Map.of(
                "input_type", inputType,
                "intent", intent
            ));
        }
        
        /**
         * Tracks an error
         */
        public void trackError(String errorMessage) {
            if (!enabled) return;
            
            totalErrors++;
            
            // Extract error type from message
            String errorType = extractErrorType(errorMessage);
            errorCounts.merge(errorType, 1, Integer::sum);
        }
        
        private String extractErrorType(String message) {
            if (message.contains("NullPointer")) return "NullPointerException";
            if (message.contains("IOException")) return "IOException";
            if (message.contains("NumberFormat")) return "NumberFormatException";
            if (message.contains("ArrayIndexOutOfBounds")) return "ArrayIndexOutOfBounds";
            return "Other";
        }
        
        /**
         * Tracks log event
         */
        public void trackLog(String logLevel) {
            if (!enabled) return;
            // Could track log levels over time
        }
        
        /**
         * Gets analytics summary
         */
        public String getSummary() {
            StringBuilder summary = new StringBuilder();
            summary.append("Analytics Summary\n");
            summary.append("=================\n\n");
            
            summary.append("Session Information:\n");
            summary.append("  Start Time: ").append(sessionStart).append("\n");
            summary.append("  Duration: ").append(java.time.Duration.between(sessionStart, LocalDateTime.now())).append("\n");
            summary.append("  Total Messages: ").append(totalMessages).append("\n");
            summary.append("  Total Errors: ").append(totalErrors).append("\n\n");
            
            summary.append("Interaction Types:\n");
            for (Map.Entry<String, Integer> entry : interactionCounts.entrySet()) {
                summary.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            summary.append("\n");
            
            summary.append("Top Intents:\n");
            intentCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(entry -> summary.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n"));
            summary.append("\n");
            
            summary.append("Error Distribution:\n");
            for (Map.Entry<String, Integer> entry : errorCounts.entrySet()) {
                summary.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            
            return summary.toString();
        }
        
        /**
         * Exports analytics to file
         */
        public void exportToFile(String filePath) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                writer.println(getSummary());
                System.out.println("Analytics exported to: " + filePath);
            } catch (IOException e) {
                System.err.println("Error exporting analytics: " + e.getMessage());
            }
        }
        
        /**
          Resets analytics
         */
        public void reset() {
            events.clear();
            interactionCounts.clear();
            intentCounts.clear();
            errorCounts.clear();
            sessionStart = LocalDateTime.now();
            totalMessages = 0;
            totalErrors = 0;
        }
        
        /**
          Analytics event class
         */

        private static class AnalyticsEvent {
            String eventType;
            LocalDateTime timestamp;
            Map<String, Object> details;
            
            public AnalyticsEvent(String eventType, LocalDateTime timestamp, Map<String, Object> details) {
                this.eventType = eventType;
                this.timestamp = timestamp;
                this.details = details;
            }
        }
    }
}
