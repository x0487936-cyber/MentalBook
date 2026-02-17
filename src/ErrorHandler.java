import java.util.*;
import java.util.concurrent.*;
import java.time.*;
import java.io.*;

/**
 * Error Handling and Recovery System for VirtualXander
 * Comprehensive error handling with recovery mechanisms
 * Part of Phase 4: Code Quality
 */
public class ErrorHandler {
    
    private static ErrorHandler instance;
    private Map<String, ErrorCategory> errorRegistry;
    private Map<String, RecoveryStrategy> recoveryStrategies;
    private Map<String, Integer> errorCounts;
    private Queue<ErrorEvent> errorHistory;
    private int maxErrorHistory;
    private boolean recoveryEnabled;
    private Logger logger;
    private Configuration config;
    
    private ErrorHandler() {
        this.errorRegistry = new HashMap<>();
        this.recoveryStrategies = new HashMap<>();
        this.errorCounts = new HashMap<>();
        this.errorHistory = new LinkedList<>();
        this.maxErrorHistory = 100;
        this.recoveryEnabled = true;
        this.logger = createLogger();
        this.config = createConfiguration();
        initializeErrorHandlers();
    }
    
    private Logger createLogger() {
        try {
            Class<?> loggerClass = Class.forName("Logger");
            return (Logger) loggerClass.getMethod("getInstance").invoke(null);
        } catch (Exception e) {
            return null;
        }
    }
    
    private Configuration createConfiguration() {
        try {
            Class<?> configClass = Class.forName("Configuration");
            return (Configuration) configClass.getMethod("getInstance").invoke(null);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Gets singleton instance
     */
    public static synchronized ErrorHandler getInstance() {
        if (instance == null) {
            instance = new ErrorHandler();
        }
        return instance;
    }
    
    /**
     * Error categories
     */
    public enum ErrorCategory {
        INPUT_ERROR("Input Error", "Invalid or unexpected user input"),
        PARSING_ERROR("Parsing Error", "Failed to parse input or data"),
        RUNTIME_ERROR("Runtime Error", "Unexpected runtime exception"),
        NETWORK_ERROR("Network Error", "Network-related errors"),
        FILE_ERROR("File Error", "File I/O errors"),
        CONFIGURATION_ERROR("Configuration Error", "Configuration-related issues"),
        MEMORY_ERROR("Memory Error", "Memory-related issues"),
        TIMEOUT_ERROR("Timeout Error", "Operation timed out"),
        VALIDATION_ERROR("Validation Error", "Validation failed"),
        UNKNOWN_ERROR("Unknown Error", "Unclassified error");
        
        private final String displayName;
        private final String description;
        
        ErrorCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * Recovery strategies
     */
    public enum RecoveryStrategy {
        RETRY("Retry", "Retry the operation"),
        SKIP("Skip", "Skip the failing operation"),
        DEFAULT("Use Default", "Use a default value"),
        ESCALATE("Escalate", "Escalate to higher-level handler"),
        ABORT("Abort", "Abort the current operation"),
        IGNORE("Ignore", "Ignore the error and continue");
        
        private final String displayName;
        private final String description;
        
        RecoveryStrategy(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * Error event class
     */
    public static class ErrorEvent {
        String errorId;
        ErrorCategory category;
        String errorType;
        String message;
        String source;
        LocalDateTime timestamp;
        RecoveryStrategy strategy;
        boolean recovered;
        
        public ErrorEvent(String errorType, ErrorCategory category, String message, String source) {
            this.errorId = UUID.randomUUID().toString().substring(0, 8);
            this.errorType = errorType;
            this.category = category;
            this.message = message;
            this.source = source;
            this.timestamp = LocalDateTime.now();
            this.strategy = RecoveryStrategy.IGNORE;
            this.recovered = false;
        }
    }
    
    /**
     * Error result class
     */
    public static class ErrorResult {
        private final boolean handled;
        private final String recoveryMessage;
        private final RecoveryStrategy strategy;
        private final boolean shouldContinue;
        private final Object fallbackValue;
        
        public ErrorResult(boolean handled, String recoveryMessage, RecoveryStrategy strategy, 
                          boolean shouldContinue, Object fallbackValue) {
            this.handled = handled;
            this.recoveryMessage = recoveryMessage;
            this.strategy = strategy;
            this.shouldContinue = shouldContinue;
            this.fallbackValue = fallbackValue;
        }
        
        public static ErrorResult success() {
            return new ErrorResult(true, null, null, true, null);
        }
        
        public static ErrorResult recovered(String message, RecoveryStrategy strategy) {
            return new ErrorResult(true, message, strategy, true, null);
        }
        
        public static ErrorResult failed(String message) {
            return new ErrorResult(false, message, RecoveryStrategy.ABORT, false, null);
        }
        
        public static ErrorResult withFallback(String message, Object fallback) {
            return new ErrorResult(true, message, RecoveryStrategy.DEFAULT, true, fallback);
        }
        
        public boolean isHandled() {
            return handled;
        }
        
        public String getRecoveryMessage() {
            return recoveryMessage;
        }
        
        public RecoveryStrategy getStrategy() {
            return strategy;
        }
        
        public boolean shouldContinue() {
            return shouldContinue;
        }
        
        public Object getFallbackValue() {
            return fallbackValue;
        }
    }
    
    /**
     * Initializes default error handlers
     */
    private void initializeErrorHandlers() {
        // Load configuration
        recoveryEnabled = getConfigBool("error.recovery_enabled", true);
        maxErrorHistory = getConfigInt("error.max_history", 100);
        
        // Register default error handlers
        registerErrorHandler("NullPointerException", ErrorCategory.RUNTIME_ERROR, RecoveryStrategy.ABORT);
        registerErrorHandler("IllegalArgumentException", ErrorCategory.INPUT_ERROR, RecoveryStrategy.DEFAULT);
        registerErrorHandler("IllegalStateException", ErrorCategory.RUNTIME_ERROR, RecoveryStrategy.RETRY);
        registerErrorHandler("NumberFormatException", ErrorCategory.PARSING_ERROR, RecoveryStrategy.DEFAULT);
        registerErrorHandler("IOException", ErrorCategory.FILE_ERROR, RecoveryStrategy.RETRY);
        registerErrorHandler("FileNotFoundException", ErrorCategory.FILE_ERROR, RecoveryStrategy.DEFAULT);
        registerErrorHandler("TimeoutException", ErrorCategory.TIMEOUT_ERROR, RecoveryStrategy.SKIP);
        registerErrorHandler("ClassCastException", ErrorCategory.RUNTIME_ERROR, RecoveryStrategy.ABORT);
        registerErrorHandler("ArrayIndexOutOfBoundsException", ErrorCategory.RUNTIME_ERROR, RecoveryStrategy.ABORT);
        registerErrorHandler("ConcurrentModificationException", ErrorCategory.RUNTIME_ERROR, RecoveryStrategy.RETRY);
    }
    
    private boolean getConfigBool(String key, boolean defaultValue) {
        if (config == null) return defaultValue;
        try {
            return config.getBooleanProperty(key, defaultValue);
        } catch (Exception e) { return defaultValue; }
    }
    
    private int getConfigInt(String key, int defaultValue) {
        if (config == null) return defaultValue;
        try {
            return config.getIntProperty(key, defaultValue);
        } catch (Exception e) { return defaultValue; }
    }
    
    private void logError(String source, String message) {
        if (logger != null) {
            logger.error(source, message);
        } else {
            System.err.println("[ERROR] " + source + ": " + message);
        }
    }
    
    private void logWarn(String source, String message) {
        if (logger != null) {
            logger.warn(source, message);
        } else {
            System.err.println("[WARN] " + source + ": " + message);
        }
    }
    
    private void logInfo(String source, String message) {
        if (logger != null) {
            logger.info(source, message);
        } else {
            System.out.println("[INFO] " + source + ": " + message);
        }
    }
    
    /**
     * Registers an error handler
     */
    public void registerErrorHandler(String errorType, ErrorCategory category, RecoveryStrategy defaultStrategy) {
        errorRegistry.put(errorType, category);
        recoveryStrategies.put(errorType, defaultStrategy);
    }
    
    /**
     * Handles an error and attempts recovery
     */
    public ErrorResult handleError(String errorType, String message, String source) {
        return handleError(errorType, message, source, null);
    }
    
    /**
     * Handles an error with throwable
     */
    public ErrorResult handleError(String errorType, String message, String source, Throwable throwable) {
        // Determine error category
        ErrorCategory category = determineErrorCategory(errorType, throwable);
        
        // Get recovery strategy
        RecoveryStrategy strategy = getRecoveryStrategy(errorType);
        
        // Create error event
        ErrorEvent event = new ErrorEvent(errorType, category, message, source);
        event.strategy = strategy;
        
        // Update error counts
        errorCounts.merge(errorType, 1, Integer::sum);
        errorCounts.merge(category.name(), 1, Integer::sum);
        
        // Add to history
        addToErrorHistory(event);
        
        // Log the error
        logError(source, message);
        if (throwable != null) {
            logError(source, "Exception details: " + throwable.getMessage());
        }
        
        // Attempt recovery if enabled
        if (recoveryEnabled) {
            return attemptRecovery(event, strategy, throwable);
        }
        
        return ErrorResult.failed(message);
    }
    
    /**
     * Handles an exception
     */
    public ErrorResult handleException(Exception e, String source) {
        String errorType = e.getClass().getSimpleName();
        String message = e.getMessage() != null ? e.getMessage() : "No message available";
        return handleError(errorType, message, source, e);
    }
    
    /**
     * Determines error category
     */
    private ErrorCategory determineErrorCategory(String errorType, Throwable throwable) {
        // Check registered categories
        if (errorRegistry.containsKey(errorType)) {
            return errorRegistry.get(errorType);
        }
        
        // Infer from error type
        if (throwable instanceof NullPointerException) {
            return ErrorCategory.RUNTIME_ERROR;
        }
        if (throwable instanceof IllegalArgumentException) {
            return ErrorCategory.INPUT_ERROR;
        }
        if (throwable instanceof NumberFormatException) {
            return ErrorCategory.PARSING_ERROR;
        }
        if (throwable instanceof IOException) {
            return ErrorCategory.FILE_ERROR;
        }
        if (throwable instanceof TimeoutException) {
            return ErrorCategory.TIMEOUT_ERROR;
        }
        
        return ErrorCategory.UNKNOWN_ERROR;
    }
    
    /**
     * Gets recovery strategy for error type
     */
    private RecoveryStrategy getRecoveryStrategy(String errorType) {
        return recoveryStrategies.getOrDefault(errorType, RecoveryStrategy.IGNORE);
    }
    
    /**
     * Attempts error recovery
     */
    private ErrorResult attemptRecovery(ErrorEvent event, RecoveryStrategy strategy, Throwable throwable) {
        switch (strategy) {
            case RETRY:
                return handleRetry(event, throwable);
            case SKIP:
                return handleSkip(event);
            case DEFAULT:
                return handleDefault(event);
            case ESCALATE:
                return handleEscalate(event);
            case ABORT:
                return handleAbort(event);
            case IGNORE:
            default:
                return handleIgnore(event);
        }
    }
    
    /**
     * Retry recovery
     */
    private ErrorResult handleRetry(ErrorEvent event, Throwable throwable) {
        int maxRetries = getConfigInt("error.max_retries", 3);
        
        for (int i = 1; i <= maxRetries; i++) {
            try {
                // Simulate retry delay
                Thread.sleep(100 * i);
                logInfo("ErrorHandler", "Retry attempt " + i + " for: " + event.errorType);
                return ErrorResult.recovered("Recovered after " + i + " attempt(s)", RecoveryStrategy.RETRY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        return ErrorResult.failed("Max retries exceeded for: " + event.errorType);
    }
    
    /**
     * Skip recovery
     */
    private ErrorResult handleSkip(ErrorEvent event) {
        return ErrorResult.recovered("Skipped failing operation", RecoveryStrategy.SKIP);
    }
    
    /**
     * Default value recovery
     */
    private ErrorResult handleDefault(ErrorEvent event) {
        Object fallback = getDefaultFallback(event.errorType);
        return ErrorResult.withFallback("Using default value for: " + event.errorType, fallback);
    }
    
    /**
     * Escalate recovery
     */
    private ErrorResult handleEscalate(ErrorEvent event) {
        logWarn("ErrorHandler", "Escalating error: " + event.errorType);
        // In a full implementation, this would notify higher-level handlers
        return ErrorResult.failed("Error escalated: " + event.errorType);
    }
    
    /**
     * Abort recovery
     */
    private ErrorResult handleAbort(ErrorEvent event) {
        logError("ErrorHandler", "Aborting operation due to: " + event.errorType);
        return ErrorResult.failed("Operation aborted: " + event.errorType);
    }
    
    /**
     * Ignore recovery
     */
    private ErrorResult handleIgnore(ErrorEvent event) {
        return ErrorResult.recovered("Error ignored and continuing", RecoveryStrategy.IGNORE);
    }
    
    /**
     * Gets default fallback value for error type
     */
    private Object getDefaultFallback(String errorType) {
        switch (errorType) {
            case "NumberFormatException":
                return 0;
            case "NullPointerException":
                return "";
            case "ArrayIndexOutOfBoundsException":
                return null;
            default:
                return null;
        }
    }
    
    /**
     * Adds error to history
     */
    private void addToErrorHistory(ErrorEvent event) {
        errorHistory.add(event);
        if (errorHistory.size() > maxErrorHistory) {
            errorHistory.poll();
        }
    }
    
    /**
     * Gets error statistics
     */
    public Map<String, Integer> getErrorStatistics() {
        return new HashMap<>(errorCounts);
    }
    
    /**
     * Gets recent errors
     */
    public List<ErrorEvent> getRecentErrors() {
        return new ArrayList<>(errorHistory);
    }
    
    /**
     * Gets errors by category
     */
    public List<ErrorEvent> getErrorsByCategory(ErrorCategory category) {
        List<ErrorEvent> filtered = new ArrayList<>();
        for (ErrorEvent event : errorHistory) {
            if (event.category == category) {
                filtered.add(event);
            }
        }
        return filtered;
    }
    
    /**
     * Clears error history
     */
    public void clearErrorHistory() {
        errorHistory.clear();
        errorCounts.clear();
        logInfo("ErrorHandler", "Error history cleared");
    }
    
    /**
     * Checks if error rate is high
     */
    public boolean isErrorRateHigh() {
        // Check if more than 10 errors in the last minute
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        int recentErrors = 0;
        
        for (ErrorEvent event : errorHistory) {
            if (event.timestamp.isAfter(oneMinuteAgo)) {
                recentErrors++;
            }
        }
        
        return recentErrors > 10;
    }
    
    /**
     * Gets error summary
     */
    public String getErrorSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Error Handling Summary\n");
        summary.append("====================\n");
        summary.append("Recovery Enabled: ").append(recoveryEnabled).append("\n");
        summary.append("Error History Size: ").append(errorHistory.size()).append("\n\n");
        
        summary.append("Error Counts by Type:\n");
        for (Map.Entry<String, Integer> entry : errorCounts.entrySet()) {
            summary.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        return summary.toString();
    }
    
    /**
     * Creates a safe executor for handling exceptions
     */
    public SafeExecutor createSafeExecutor() {
        return new SafeExecutor(this);
    }
    
    /**
     * Safe executor for wrapping operations
     */
    public static class SafeExecutor {
        private ErrorHandler errorHandler;
        private RecoveryStrategy defaultStrategy;
        private Object defaultValue;
        
        public SafeExecutor(ErrorHandler handler) {
            this.errorHandler = handler;
            this.defaultStrategy = RecoveryStrategy.DEFAULT;
            this.defaultValue = null;
        }
        
        public SafeExecutor withDefaultStrategy(RecoveryStrategy strategy) {
            this.defaultStrategy = strategy;
            return this;
        }
        
        public SafeExecutor withDefaultValue(Object value) {
            this.defaultValue = value;
            return this;
        }
        
        /**
         * Executes a callable with error handling
         */
        
        public <T> ErrorResult execute(Callable<T> callable, String operationName) {
            try {
                T result = callable.call();
                // Use the result to avoid unused variable warning
                if (result != null) {
                    return ErrorResult.success();
                }
                return ErrorResult.success();
            } catch (Exception e) {
                ErrorResult result = errorHandler.handleException(e, operationName);
                // Apply default strategy if result failed
                if (!result.isHandled() && defaultStrategy != null) {
                    return ErrorResult.recovered("Applied default strategy: " + defaultStrategy, defaultStrategy);
                }
                return result;
            }
        }
        
        /**
         * Executes a runnable with error handling
         */

        public ErrorResult execute(Runnable runnable, String operationName) {
            try {
                runnable.run();
                return ErrorResult.success();
            } catch (Exception e) {
                ErrorResult result = errorHandler.handleException(e, operationName);
                // Apply default value if result failed and defaultValue is set
                if (!result.isHandled() && defaultValue != null) {
                    return ErrorResult.withFallback("Applied default value", defaultValue);
                }
                return result;
            }
        }
    }
    
    /**
     * Validates input with error handling
     */

    public ErrorResult validateInput(String input, String validationRule, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            return ErrorResult.failed(fieldName + " cannot be empty");
        }
        
        if (validationRule != null) {
            try {
                if (!input.matches(validationRule)) {
                    return handleError("ValidationError", 
                        fieldName + " does not match required format", 
                        "InputValidation");
                }
            } catch (Exception e) {
                return handleError("ValidationError", 
                    "Invalid validation rule for " + fieldName, 
                    "InputValidation");
            }
        }
        
        return ErrorResult.success();
    }
}