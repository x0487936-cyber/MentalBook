import java.util.*;
import java.util.jar.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Plugin System for VirtualXander
 * Provides extensibility through dynamically loaded plugins
 */
public class PluginSystem {
    
    private Map<String, Plugin> plugins;
    private Map<String, PluginListener> listeners;
    private List<PluginDescriptor> pluginDescriptors;
    private String pluginDirectory;
    private boolean pluginsLoaded;
    
    public PluginSystem() {
        this.plugins = new HashMap<>();
        this.listeners = new HashMap<>();
        this.pluginDescriptors = new ArrayList<>();
        this.pluginDirectory = "plugins";
        this.pluginsLoaded = false;
    }
    
    /**
     * Sets the plugin directory
     */
    public void setPluginDirectory(String directory) {
        this.pluginDirectory = directory;
    }
    
    /**
     * Plugin descriptor class
     */
    public static class PluginDescriptor {
        String name;
        String version;
        String author;
        String description;
        List<String> dependencies;
        PluginState state;
        String className;
        
        public PluginDescriptor(String name, String version, String author, String description) {
            this.name = name;
            this.version = version;
            this.author = author;
            this.description = description;
            this.dependencies = new ArrayList<>();
            this.state = PluginState.NOT_LOADED;
            this.className = "";
        }
    }
    
    /**
     * Plugin lifecycle states
     */
    public enum PluginState {
        NOT_LOADED,
        LOADING,
        LOADED,
        ENABLED,
        DISABLED,
        ERROR
    }
    
    /**
     * Plugin listener interface
     */
    public interface PluginListener {
        void onPluginLoaded(Plugin plugin);
        void onPluginEnabled(Plugin plugin);
        void onPluginDisabled(Plugin plugin);
        void onPluginError(Plugin plugin, Exception error);
    }
    
    /**
     * Base interface for all plugins
     */
    public interface Plugin {
        void initialize(PluginSystem system);
        void enable();
        void disable();
        String getName();
        String getVersion();
        String getAuthor();
        String getDescription();
        PluginState getState();
    }
    
    /**
     * Plugin metadata annotation
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    public @interface PluginMetadata {
        String name();
        String version() default "1.0";
        String author() default "Unknown";
        String description() default "";
        String[] dependencies() default {};
    }
    
    /**
     * Registers a plugin listener
     */
    public void addPluginListener(String pluginName, PluginListener listener) {
        listeners.put(pluginName, listener);
    }
    
    /**
     * Removes a plugin listener
     */
    public void removePluginListener(String pluginName) {
        listeners.remove(pluginName);
    }
    
    /**
     * Registers a plugin manually
     */
    public boolean registerPlugin(Plugin plugin) {
        try {
            String name = plugin.getName();
            
            if (plugins.containsKey(name)) {
                System.err.println("Plugin with name '" + name + "' already exists.");
                return false;
            }
            
            // Create descriptor
            PluginDescriptor descriptor = new PluginDescriptor(
                name,
                plugin.getVersion(),
                plugin.getAuthor(),
                plugin.getDescription()
            );
            descriptor.state = PluginState.LOADED;
            
            // Initialize plugin
            plugin.initialize(this);
            
            // Store plugin
            plugins.put(name, plugin);
            pluginDescriptors.add(descriptor);
            
            // Notify listeners
            notifyPluginLoaded(plugin);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error registering plugin: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Enables a plugin
     */
    public boolean enablePlugin(String name) {
        Plugin plugin = plugins.get(name);
        if (plugin == null) {
            System.err.println("Plugin '" + name + "' not found.");
            return false;
        }
        
        try {
            plugin.enable();
            
            // Update descriptor
            for (PluginDescriptor desc : pluginDescriptors) {
                if (desc.name.equals(name)) {
                    desc.state = PluginState.ENABLED;
                    break;
                }
            }
            
            // Notify listeners
            notifyPluginEnabled(plugin);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error enabling plugin '" + name + "': " + e.getMessage());
            handlePluginError(plugin, e);
            return false;
        }
    }
    
    /**
     * Disables a plugin
     */
    public boolean disablePlugin(String name) {
        Plugin plugin = plugins.get(name);
        if (plugin == null) {
            return false;
        }
        
        try {
            plugin.disable();
            
            // Update descriptor
            for (PluginDescriptor desc : pluginDescriptors) {
                if (desc.name.equals(name)) {
                    desc.state = PluginState.DISABLED;
                    break;
                }
            }
            
            // Notify listeners
            notifyPluginDisabled(plugin);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error disabling plugin '" + name + "': " + e.getMessage());
            handlePluginError(plugin, e);
            return false;
        }
    }
    
    /**
     * Unregisters a plugin
     */
    public boolean unregisterPlugin(String name) {
        Plugin plugin = plugins.remove(name);
        if (plugin == null) {
            return false;
        }
        
        // Disable first if enabled
        if (plugin.getState() == PluginState.ENABLED) {
            plugin.disable();
        }
        
        // Remove from descriptors
        pluginDescriptors.removeIf(desc -> desc.name.equals(name));
        
        return true;
    }
    
    /**
     * Loads plugins from the plugin directory
     */
    public void loadPlugins() {
        if (pluginsLoaded) {
            System.err.println("Plugins already loaded.");
            return;
        }
        
        File dir = new File(pluginDirectory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Plugin directory not found: " + pluginDirectory);
            return;
        }
        
        // Create directory if it doesn't exist
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("Created plugin directory: " + pluginDirectory);
            return;
        }
        
        File[] jarFiles = dir.listFiles((d, name) -> name.endsWith(".jar"));
        if (jarFiles == null || jarFiles.length == 0) {
            System.out.println("No plugin JARs found in: " + pluginDirectory);
            return;
        }
        
        System.out.println("Found " + jarFiles.length + " potential plugin(s)...");
        
        for (File jarFile : jarFiles) {
            try {
                loadPluginFromJar(jarFile);
            } catch (Exception e) {
                System.err.println("Error loading plugin from " + jarFile.getName() + ": " + e.getMessage());
            }
        }
        
        pluginsLoaded = true;
        System.out.println("Plugin loading complete. Loaded " + plugins.size() + " plugin(s).");
    }
    
    /**
     * Loads a plugin from a JAR file
     */
    private void loadPluginFromJar(File jarFile) throws Exception {
        URLClassLoader classLoader = new URLClassLoader(
            new URL[]{jarFile.toURI().toURL()},
            getClass().getClassLoader()
        );
        
        // Find classes implementing Plugin interface
        Enumeration<URL> resources = classLoader.getResources("");
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            // Note: In a real implementation, you would scan the JAR for Plugin implementations
        }
        
        // Try to load main class from manifest
        JarFile jar = new JarFile(jarFile);
        Manifest manifest = jar.getManifest();
        
        if (manifest != null) {
            String mainClass = manifest.getMainAttributes().getValue("Main-Class");
            if (mainClass != null) {
                Class<?> clazz = classLoader.loadClass(mainClass);
                if (Plugin.class.isAssignableFrom(clazz)) {
                    Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
                    registerPlugin(plugin);
                    System.out.println("Loaded plugin: " + plugin.getName() + " v" + plugin.getVersion());
                }
            }
        }
        
        jar.close();
    }
    
    /**
     * Gets a plugin by name
     */
    public Plugin getPlugin(String name) {
        return plugins.get(name);
    }
    
    /**
     * Gets all registered plugins
     */
    public Collection<Plugin> getAllPlugins() {
        return plugins.values();
    }
    
    /**
     * Gets all plugin descriptors
     */
    public List<PluginDescriptor> getPluginDescriptors() {
        return new ArrayList<>(pluginDescriptors);
    }
    
    /**
     * Gets plugin descriptor by name
     */
    public PluginDescriptor getPluginDescriptor(String name) {
        for (PluginDescriptor desc : pluginDescriptors) {
            if (desc.name.equals(name)) {
                return desc;
            }
        }
        return null;
    }
    
    /**
     * Checks if a plugin is loaded
     */
    public boolean isPluginLoaded(String name) {
        return plugins.containsKey(name);
    }
    
    /**
     * Checks if a plugin is enabled
     */
    public boolean isPluginEnabled(String name) {
        Plugin plugin = plugins.get(name);
        return plugin != null && plugin.getState() == PluginState.ENABLED;
    }
    
    /**
     * Gets the count of loaded plugins
     */
    public int getPluginCount() {
        return plugins.size();
    }
    
    /**
     * Enables all loaded plugins
     */
    public void enableAllPlugins() {
        for (Plugin plugin : plugins.values()) {
            if (plugin.getState() == PluginState.LOADED) {
                enablePlugin(plugin.getName());
            }
        }
    }
    
    /**
     * Disables all plugins
     */
    public void disableAllPlugins() {
        for (Plugin plugin : plugins.values()) {
            if (plugin.getState() == PluginState.ENABLED) {
                disablePlugin(plugin.getName());
            }
        }
    }
    
    /**
     * Clears all plugins
     */
    public void clearPlugins() {
        disableAllPlugins();
        plugins.clear();
        pluginDescriptors.clear();
        pluginsLoaded = false;
    }
    
    /**
     * Gets a plugin by its capability
     */
    public List<Plugin> getPluginsWithCapability(String capability) {
        List<Plugin> capablePlugins = new ArrayList<>();
        
        for (Plugin plugin : plugins.values()) {
            if (plugin instanceof CapablePlugin) {
                CapablePlugin capable = (CapablePlugin) plugin;
                if (capable.hasCapability(capability)) {
                    capablePlugins.add(plugin);
                }
            }
        }
        
        return capablePlugins;
    }
    
    /**
     * Executes a capability across all capable plugins
     */
    public <T> Map<String, T> executeCapability(String capability, CapabilityExecutor<T> executor) {
        Map<String, T> results = new HashMap<>();
        
        for (Plugin plugin : plugins.values()) {
            if (plugin instanceof CapablePlugin) {
                CapablePlugin capable = (CapablePlugin) plugin;
                if (capable.hasCapability(capability)) {
                    try {
                        T result = executor.execute(plugin);
                        results.put(plugin.getName(), result);
                    } catch (Exception e) {
                        System.err.println("Error executing capability '" + capability + "' on plugin '" + plugin.getName() + "': " + e.getMessage());
                    }
                }
            }
        }
        
        return results;
    }
    
    /**
     * Interface for plugins with capabilities
     */
    public interface CapablePlugin extends Plugin {
        List<String> getCapabilities();
        boolean hasCapability(String capability);
    }
    
    /**
     * Capability executor interface
     */
    public interface CapabilityExecutor<T> {
        T execute(Plugin plugin) throws Exception;
    }
    
    /**
     * Gets a summary of all loaded plugins
     */
    public String getPluginSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Plugin System Summary\n");
        summary.append("====================\n");
        summary.append("Total Plugins: ").append(plugins.size()).append("\n");
        summary.append("Plugins Loaded: ").append(pluginsLoaded).append("\n\n");
        
        for (PluginDescriptor desc : pluginDescriptors) {
            summary.append("Name: ").append(desc.name).append("\n");
            summary.append("  Version: ").append(desc.version).append("\n");
            summary.append("  Author: ").append(desc.author).append("\n");
            summary.append("  State: ").append(desc.state).append("\n");
            summary.append("  Description: ").append(desc.description).append("\n\n");
        }
        
        return summary.toString();
    }
    
    private void notifyPluginLoaded(Plugin plugin) {
        PluginListener listener = listeners.get(plugin.getName());
        if (listener != null) {
            listener.onPluginLoaded(plugin);
        }
    }
    
    private void notifyPluginEnabled(Plugin plugin) {
        PluginListener listener = listeners.get(plugin.getName());
        if (listener != null) {
            listener.onPluginEnabled(plugin);
        }
    }
    
    private void notifyPluginDisabled(Plugin plugin) {
        PluginListener listener = listeners.get(plugin.getName());
        if (listener != null) {
            listener.onPluginDisabled(plugin);
        }
    }
    
    private void handlePluginError(Plugin plugin, Exception error) {
        PluginListener listener = listeners.get(plugin.getName());
        if (listener != null) {
            listener.onPluginError(plugin, error);
        }
    }
    
    /**
     * Creates a sample plugin for demonstration
     */
    public static class SamplePlugin implements Plugin {
        private PluginState state;
        private PluginSystem system;
        
        public SamplePlugin() {
            this.state = PluginState.NOT_LOADED;
        }
        
        @Override
        public void initialize(PluginSystem system) {
            this.system = system;
            this.state = PluginState.LOADED;
        }
        
        @Override
        public void enable() {
            this.state = PluginState.ENABLED;
            System.out.println("Sample Plugin enabled!");
        }
        
        @Override
        public void disable() {
            this.state = PluginState.DISABLED;
            System.out.println("Sample Plugin disabled!");
        }
        
        @Override
        public String getName() {
            return "SamplePlugin";
        }
        
        @Override
        public String getVersion() {
            return "1.0";
        }
        
        @Override
        public String getAuthor() {
            return "VirtualXander Team";
        }
        
        @Override
        public String getDescription() {
            return "A sample plugin demonstrating the plugin system";
        }
        
        @Override
        public PluginState getState() {
            return state;
        }
    }
}

