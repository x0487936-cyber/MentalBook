import java.util.*;
import java.io.*;
import java.nio.file.*;

/**
 * Configuration System for VirtualXander
 * Manages application settings and configurations
 * Part of Phase 4: Code Quality
 */
public class Configuration {
    
    private static Configuration instance;
    private Properties properties;
    private String configFilePath;
    private Map<String, Object> runtimeConfig;
    
    private Configuration() {
        this.properties = new Properties();
        this.runtimeConfig = new HashMap<>();
        this.configFilePath = "config.properties";
        loadConfiguration();
    }
    
    /**
     * Gets singleton instance
     */
    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
    
    /**
     * Loads configuration from file
     */
    private void loadConfiguration() {
        try {
            File configFile = new File(configFilePath);
            if (configFile.exists()) {
                try (FileInputStream fis = new FileInputStream(configFile)) {
                    properties.load(fis);
                }
                System.out.println("Configuration loaded from: " + configFilePath);
            } else {
                // Load default configuration
                loadDefaultConfiguration();
                // Save defaults to file
                saveConfiguration();
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            loadDefaultConfiguration();
        }
    }
    
    /**
     * Loads default configuration values
     */
    private void loadDefaultConfiguration() {
        // General settings
        setProperty("app.name", "VirtualXander");
        setProperty("app.version", "2.0");
        setProperty("app.environment", "development");
        
        // Conversation settings
        setProperty("conversation.max_history_size", "50");
        setProperty("conversation.context_timeout_minutes", "30");
        setProperty("conversation.auto_reset_enabled", "true");
        
        // Response settings
        setProperty("response.enable_templates", "true");
        setProperty("response.max_template_variations", "5");
        setProperty("response.contextual_followup_enabled", "true");
        
        // Emotion detection settings
        setProperty("emotion.detection_enabled", "true");
        setProperty("emotion.min_confidence_threshold", "0.3");
        setProperty("emotion.crisis_detection_enabled", "true");
        
        // Topic clustering settings
        setProperty("topics.clustering_enabled", "true");
        setProperty("topics.max_active_clusters", "5");
        setProperty("topics.relevance_decay_minutes", "30");
        
        // Logging settings
        setProperty("logging.enabled", "true");
        setProperty("logging.level", "INFO");
        setProperty("logging.file_path", "logs/virtualxander.log");
        setProperty("logging.console_output", "true");
        
        // Analytics settings
        setProperty("analytics.enabled", "true");
        setProperty("analytics.session_tracking", "true");
        setProperty("analytics.log_file", "logs/analytics.log");
        
        // Plugin settings
        setProperty("plugins.enabled", "true");
        setProperty("plugins.directory", "plugins");
        setProperty("plugins.auto_load", "false");
        
        // Error handling settings
        setProperty("error.show_details", "false");
        setProperty("error.recovery_enabled", "true");
        setProperty("error.max_retries", "3");
        
        // GUI settings (if applicable)
        setProperty("gui.enabled", "false");
        setProperty("gui.theme", "default");
        
        // Server settings
        setProperty("server.enabled", "false");
        setProperty("server.port", "8080");
        setProperty("server.host", "localhost");
    }
    
    /**
     * Saves configuration to file
     */
    public void saveConfiguration() {
        try (FileOutputStream fos = new FileOutputStream(configFilePath)) {
            properties.store(fos, "VirtualXander Configuration");
            System.out.println("Configuration saved to: " + configFilePath);
        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }
    
    /**
     * Sets a configuration property
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        runtimeConfig.put(key, value);
    }
    
    /**
     * Gets a configuration property
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Gets a configuration property with default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Gets an integer property
     */
    public int getIntProperty(String key) {
        return getIntProperty(key, 0);
    }
    
    /**
     * Gets an integer property with default
     */
    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Gets a boolean property
     */
    public boolean getBooleanProperty(String key) {
        return getBooleanProperty(key, false);
    }
    
    /**
     * Gets a boolean property with default
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(key, String.valueOf(defaultValue)));
    }
    
    /**
     * Gets a double property
     */
    public double getDoubleProperty(String key) {
        return getDoubleProperty(key, 0.0);
    }
    
    /**
     * Gets a double property with default
     */
    public double getDoubleProperty(String key, double defaultValue) {
        try {
            return Double.parseDouble(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Sets runtime configuration (not persisted)
     */
    public void setRuntimeConfig(String key, Object value) {
        runtimeConfig.put(key, value);
    }
    
    /**
     * Gets runtime configuration
     */
    public Object getRuntimeConfig(String key) {
        return runtimeConfig.get(key);
    }
    
    /**
     * Gets all configuration keys
     */
    public Set<String> getConfigKeys() {
        return properties.stringPropertyNames();
    }
    
    /**
     * Checks if a property exists
     */
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }
    
    /**
     * Removes a property
     */
    public void removeProperty(String key) {
        properties.remove(key);
        runtimeConfig.remove(key);
    }
    
    /**
     * Reloads configuration from file
     */
    public void reloadConfiguration() {
        properties.clear();
        runtimeConfig.clear();
        loadConfiguration();
    }
    
    /**
     * Gets configuration summary
     */
    public String getConfigurationSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Configuration Summary\n");
        summary.append("====================\n");
        summary.append("File: ").append(configFilePath).append("\n");
        summary.append("Total Properties: ").append(properties.size()).append("\n\n");
        
        summary.append("General Settings:\n");
        summary.append("  App Name: ").append(getProperty("app.name")).append("\n");
        summary.append("  Version: ").append(getProperty("app.version")).append("\n");
        summary.append("  Environment: ").append(getProperty("app.environment")).append("\n\n");
        
        summary.append("Conversation Settings:\n");
        summary.append("  Max History Size: ").append(getIntProperty("conversation.max_history_size")).append("\n");
        summary.append("  Context Timeout: ").append(getIntProperty("conversation.context_timeout_minutes")).append(" minutes\n");
        summary.append("  Auto Reset: ").append(getBooleanProperty("conversation.auto_reset_enabled")).append("\n\n");
        
        summary.append("Logging Settings:\n");
        summary.append("  Enabled: ").append(getBooleanProperty("logging.enabled")).append("\n");
        summary.append("  Level: ").append(getProperty("logging.level")).append("\n");
        summary.append("  Console Output: ").append(getBooleanProperty("logging.console_output")).append("\n\n");
        
        summary.append("Analytics Settings:\n");
        summary.append("  Enabled: ").append(getBooleanProperty("analytics.enabled")).append("\n");
        summary.append("  Session Tracking: ").append(getBooleanProperty("analytics.session_tracking")).append("\n\n");
        
        summary.append("Plugin Settings:\n");
        summary.append("  Enabled: ").append(getBooleanProperty("plugins.enabled")).append("\n");
        summary.append("  Directory: ").append(getProperty("plugins.directory")).append("\n");
        summary.append("  Auto Load: ").append(getBooleanProperty("plugins.auto_load")).append("\n");
        
        return summary.toString();
    }
    
    /**
     * Validates configuration
     */
    public boolean validateConfiguration() {
        List<String> errors = new ArrayList<>();
        
        // Validate required properties
        String[] requiredProps = {"app.name", "app.version"};
        for (String prop : requiredProps) {
            if (!hasProperty(prop)) {
                errors.add("Missing required property: " + prop);
            }
        }
        
        // Validate numeric properties
        try {
            Integer.parseInt(getProperty("conversation.max_history_size"));
        } catch (NumberFormatException e) {
            errors.add("Invalid max_history_size: must be an integer");
        }
        
        try {
            Integer.parseInt(getProperty("conversation.context_timeout_minutes"));
        } catch (NumberFormatException e) {
            errors.add("Invalid context_timeout_minutes: must be an integer");
        }
        
        // Print errors if any
        if (!errors.isEmpty()) {
            System.err.println("Configuration validation errors:");
            for (String error : errors) {
                System.err.println("  - " + error);
            }
            return false;
        }
        
        return true;
    }
    
    /**
     * Exports configuration to a different format
     */
    public void exportConfiguration(String format, String outputPath) throws IOException {
        switch (format.toLowerCase()) {
            case "json":
                exportToJson(outputPath);
                break;
            case "xml":
                exportToXml(outputPath);
                break;
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }
    
    /**
     * Exports to JSON format
     */
    private void exportToJson(String outputPath) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        List<String> keys = new ArrayList<>(properties.stringPropertyNames());
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = properties.getProperty(key).replace("\"", "\\\"");
            json.append("  \"").append(key).append("\": \"").append(value).append("\"");
            if (i < keys.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("}");
        
        Files.write(Paths.get(outputPath), json.toString().getBytes());
        System.out.println("Configuration exported to: " + outputPath);
    }
    
    /**
     * Exports to XML format
     */
    private void exportToXml(String outputPath) throws IOException {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<configuration>\n");
        
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key).replace("&", "&amp;").replace("<", "<");
            xml.append("  <property name=\"").append(key).append("\">").append(value).append("</property>\n");
        }
        
        xml.append("</configuration>");
        
        Files.write(Paths.get(outputPath), xml.toString().getBytes());
        System.out.println("Configuration exported to: " + outputPath);
    }
}

