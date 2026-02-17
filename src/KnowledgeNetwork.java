import java.util.*;
import java.util.regex.*;

/**
 * Knowledge Network for VirtualXander
 * Creates connections between concepts, topics, and ideas
 * 
 * Features:
 * - Related topic linking
 * - Cross-domain associations
 * - Interesting tangents
 * - Knowledge Depth Levels (Surface → Expert)
 */
public class KnowledgeNetwork {
    
    private Random random;
    
    // Concept graph nodes
    private Map<String, ConceptNode> conceptNodes;
    
    // Domain clusters
    private Map<String, Set<String>> domainClusters;
    
    // Interesting tangents
    private List<Tangent> tangents;
    
    /**
     * Knowledge depth levels for graded responses
     */
    public enum DepthLevel {
        SURFACE,      // Basic facts anyone would know
        NUANCE,       // Interesting nuances and interesting details
        DEEP,         // Deep explanations with context
        EXPERT        // Expert-level details and advanced concepts
    }
    
    /**
     * Knowledge at a specific depth level
     */
    private static class DepthKnowledge {
        String content;
        String source;
        double confidence;
        List<String> tags;
        
        DepthKnowledge(String content, String source, double confidence, List<String> tags) {
            this.content = content;
            this.source = source;
            this.confidence = confidence;
            this.tags = tags;
        }
        
        DepthKnowledge(String content, String source, double confidence) {
            this(content, source, confidence, new ArrayList<>());
        }
    }
    
    /**
     * Concept node in the knowledge network
     */
    private static class ConceptNode {
        String concept;
        Set<String> relatedConcepts;
        Set<String> domains;
        double relevanceScore;
        List<String> funFacts;
        List<String> connections;
        Map<DepthLevel, List<DepthKnowledge>> knowledgeAtDepths;
        
        ConceptNode(String concept) {
            this.concept = concept;
            this.relatedConcepts = new HashSet<>();
            this.domains = new HashSet<>();
            this.relevanceScore = 0.5;
            this.funFacts = new ArrayList<>();
            this.connections = new ArrayList<>();
            this.knowledgeAtDepths = new EnumMap<>(DepthLevel.class);
            for (DepthLevel level : DepthLevel.values()) {
                knowledgeAtDepths.put(level, new ArrayList<>());
            }
        }
    }
    
    /**
     * Interesting tangent for conversation
     */
    private static class Tangent {
        String trigger;
        String tangent;
        String connection;
        double surpriseFactor;
        
        Tangent(String trigger, String tangent, String connection, double surprise) {
            this.trigger = trigger;
            this.tangent = tangent;
            this.connection = connection;
            this.surpriseFactor = surprise;
        }
    }
    
    public KnowledgeNetwork() {
        this.random = new Random();
        this.conceptNodes = new HashMap<>();
        this.domainClusters = new HashMap<>();
        this.tangents = new ArrayList<>();
        
        initializeDomainClusters();
        initializeTangents();
        initializeConceptConnections();
        initializeDepthKnowledge();
    }
    
    // ==================== INITIALIZATION ====================
    
    /**
     * Initialize domain clusters for cross-domain associations
     */
    private void initializeDomainClusters() {
        // Technology domain
        Set<String> tech = new HashSet<>();
        tech.add("ai");
        tech.add("computers");
        tech.add("internet");
        tech.add("programming");
        tech.add("robots");
        tech.add("phones");
        tech.add("software");
        domainClusters.put("technology", tech);
        
        // Science domain
        Set<String> science = new HashSet<>();
        science.add("physics");
        science.add("biology");
        science.add("chemistry");
        science.add("space");
        science.add("universe");
        science.add("evolution");
        science.add("quantum");
        domainClusters.put("science", science);
        
        // Philosophy domain
        Set<String> philosophy = new HashSet<>();
        philosophy.add("meaning");
        philosophy.add("purpose");
        philosophy.add("consciousness");
        philosophy.add("identity");
        philosophy.add("ethics");
        philosophy.add("reality");
        philosophy.add("existence");
        domainClusters.put("philosophy", philosophy);
        
        // Psychology domain
        Set<String> psychology = new HashSet<>();
        psychology.add("emotions");
        psychology.add("behavior");
        psychology.add("memory");
        psychology.add("learning");
        psychology.add("motivation");
        psychology.add("perception");
        psychology.add("intelligence");
        domainClusters.put("psychology", psychology);
        
        // Arts domain
        Set<String> arts = new HashSet<>();
        arts.add("music");
        arts.add("writing");
        arts.add("painting");
        arts.add("film");
        arts.add("creativity");
        arts.add("design");
        arts.add("literature");
        domainClusters.put("arts", arts);
        
        // Life domain
        Set<String> life = new HashSet<>();
        life.add("relationships");
        life.add("career");
        life.add("health");
        life.add("happiness");
        life.add("success");
        life.add("growth");
        life.add("productivity");
        domainClusters.put("life", life);
    }
    
    /**
     * Initialize interesting tangents
     */
    private void initializeTangents() {
        tangents.add(new Tangent(
            "time",
            "Did you know that time moves differently depending on your perspective?",
            "Time perception is linked to dopamine levels and novel experiences.",
            0.7
        ));
        
        tangents.add(new Tangent(
            "memory",
            "Speaking of memory, your brain actually reconstructs memories each time you recall them.",
            "This means memories can subtly change with each recollection.",
            0.8
        ));
        
        tangents.add(new Tangent(
            "language",
            "Here's a fun fact: some languages don't have words for 'left' and 'right' - only cardinal directions.",
            "This affects how speakers think about space and direction.",
            0.9
        ));
        
        tangents.add(new Tangent(
            "music",
            "Music affects your brain chemistry - even sad music can trigger dopamine release.",
            "This is why we seek out emotional music during different life phases.",
            0.7
        ));
        
        tangents.add(new Tangent(
            "sleep",
            "Your brain cleans itself while you sleep - it's like a nightly maintenance cycle.",
            "This is why sleep deprivation affects cognition so severely.",
            0.8
        ));
        
        tangents.add(new Tangent(
            "learning",
            "The most effective learning happens when you're slightly uncomfortable.",
            "This is called the 'zone of proximal development' - just beyond your current skills.",
            0.6
        ));
        
        tangents.add(new Tangent(
            "emotions",
            "Emotions are actually predictions your brain makes about bodily states.",
            "This is why 'trust your gut' can sometimes be right - your body knows before your mind does.",
            0.9
        ));
        
        tangents.add(new Tangent(
            "technology",
            "The average smartphone has more computing power than the Apollo spacecraft.",
            "We carry supercomputers in our pockets and use them to look at pictures of food.",
            0.7
        ));
        
        tangents.add(new Tangent(
            "relationships",
            "Research shows that close relationships are the strongest predictor of happiness.",
            "Even more than wealth or success. Quality time matters more than quantity.",
            0.6
        ));
        
        tangents.add(new Tangent(
            "creativity",
            "Your brain is most creative when it's slightly bored.",
            "This is why great ideas often come during mundane activities like showering or walking.",
            0.8
        ));
    }
    
    /**
     * Initialize concept connections
     */
    private void initializeConceptConnections() {
        // Create concept nodes with related concepts
        addConcept("motivation", Set.of("goals", "habits", "discipline", "purpose", "inspiration"),
                  "psychology", "Motivation is the drive that initiates behavior.");
        
        addConcept("creativity", Set.of("imagination", "innovation", "art", "problem-solving", "curiosity"),
                  "arts", "Creativity emerges from the intersection of different domains.");
        
        addConcept("learning", Set.of("memory", "practice", "feedback", "curiosity", "growth"),
                  "psychology", "Learning is the process of acquiring new understanding.");
        
        addConcept("communication", Set.of("listening", "empathy", "clarity", "body language", "context"),
                  "psychology", "Communication is more than words - it's understanding.");
        
        addConcept("productivity", Set.of("focus", "time management", "priorities", "habits", "energy"),
                  "life", "Productivity is about effectiveness, not just efficiency.");
        
        addConcept("happiness", Set.of("gratitude", "purpose", "relationships", "health", "achievement"),
                  "psychology", "Happiness is less about achievement and more about appreciation.");
        
        addConcept("success", Set.of("failure", "resilience", "learning", "goals", "mindset"),
                  "life", "Success is a journey, not a destination.");
        
        addConcept("relationships", Set.of("trust", "communication", "boundaries", "empathy", "respect"),
                  "life", "Relationships are the mirror by which we discover ourselves.");
        
        addConcept("technology", Set.of("ai", "innovation", "automation", "connection", "ethics"),
                  "technology", "Technology shapes how we think, feel, and connect.");
        
        addConcept("consciousness", Set.of("awareness", "perception", "identity", "mind", "experience"),
                  "philosophy", "Consciousness remains one of science's greatest mysteries.");
    }
    
    /**
     * Add a concept node
     */
    private void addConcept(String concept, Set<String> related, String domain, String description) {
        ConceptNode node = new ConceptNode(concept);
        node.relatedConcepts.addAll(related);
        node.domains.add(domain);
        node.connections.add(description);
        
        // Add fun facts
        node.funFacts.addAll(generateFunFacts(concept));
        
        conceptNodes.put(concept.toLowerCase(), node);
    }
    
    /**
     * Generate fun facts for a concept
     */
    private List<String> generateFunFacts(String concept) {
        List<String> facts = new ArrayList<>();
        
        switch (concept.toLowerCase()) {
            case "motivation":
                facts.add("Motivation follows action, not the other way around.");
                facts.add("The motivation to avoid loss is twice as powerful as the motivation to gain.");
                break;
            case "creativity":
                facts.add("Daydreaming activates the brain's default mode network, linked to creativity.");
                facts.add("Boredom can actually boost creative thinking by encouraging mental exploration.");
                break;
            case "learning":
                facts.add("Spaced repetition is 40% more effective than cramming.");
                facts.add("Teaching what you learn can increase retention by up to 90%.");
                break;
            case "happiness":
                facts.add("Happiness is 50% genetic, 10% circumstances, and 40% daily habits.");
                facts.add("Acts of kindness release dopamine, creating a 'helper's high.'");
                break;
            case "relationships":
                facts.add("It takes about 50 hours of shared time to go from acquaintance to friend.");
                facts.add("Deep conversations trigger the same brain regions as physical pleasure.");
                break;
        }
        
        return facts;
    }
    
    // ==================== KNOWLEDGE DEPTH LEVELS ====================
    
    /**
     * Initialize knowledge at different depth levels for key concepts
     */
    private void initializeDepthKnowledge() {
        // MOTIVATION
        addDepthKnowledge("motivation", DepthLevel.SURFACE,
            "Motivation is what drives you to take action.", "Common knowledge", 0.95);
        addDepthKnowledge("motivation", DepthLevel.SURFACE,
            "People feel motivated by rewards and punishments.", "Basic psychology", 0.90);
        
        addDepthKnowledge("motivation", DepthLevel.NUANCE,
            "Motivation comes in waves - understanding your natural rhythms can help.", "Behavioral science", 0.85);
        addDepthKnowledge("motivation", DepthLevel.NUANCE,
            "The type of motivation (intrinsic vs extrinsic) affects long-term persistence.", "Self-determination theory", 0.88);
        addDepthKnowledge("motivation", DepthLevel.NUANCE,
            "Starting with small actions can 'trick' your brain into feeling motivated.", "Habit formation research", 0.82);
        
        addDepthKnowledge("motivation", DepthLevel.DEEP,
            "Dopamine pathways in the brain create anticipation of rewards, driving motivated behavior. When you visualize completing a goal, your brain releases dopamine as if you've already succeeded.", "Neuroscience of motivation", 0.80);
        addDepthKnowledge("motivation", DepthLevel.DEEP,
            "The prefrontal cortex balances long-term goals against immediate gratification. This is why willpower feels like a finite resource - executive function literally tires the brain.", "Cognitive neuroscience", 0.78);
        
        addDepthKnowledge("motivation", DepthLevel.EXPERT,
            "Motivation operates through a complex feedback loop involving the ventral tegmental area (VTA), nucleus accumbens, and prefrontal cortex. Optimum challenge levels trigger 'flow states' where intrinsic motivation peaks. Self-efficacy theory suggests that perceived capability moderates goal pursuit intensity through approach-avoidance mechanisms.", "Advanced neuroscience literature", 0.75);
        
        // CREATIVITY
        addDepthKnowledge("creativity", DepthLevel.SURFACE,
            "Creativity is the ability to generate new ideas.", "Common definition", 0.95);
        addDepthKnowledge("creativity", DepthLevel.SURFACE,
            "Creative people often think 'outside the box'.", "General perception", 0.85);
        
        addDepthKnowledge("creativity", DepthLevel.NUANCE,
            "Creativity requires both divergent (generating ideas) and convergent (evaluating) thinking.", "Guilford's model", 0.88);
        addDepthKnowledge("creativity", DepthLevel.NUANCE,
            "Constraints often boost creativity by narrowing possibilities.", "Research on creative constraints", 0.82);
        addDepthKnowledge("creativity", DepthLevel.NUANCE,
            "The default mode network, active during mind-wandering, is linked to creative insight.", "Neuroscience findings", 0.80);
        
        addDepthKnowledge("creativity", DepthLevel.DEEP,
            "Incubation periods during creative problem-solving allow unconscious processing. This explains why showers and walks often produce breakthroughs - reduced cognitive load allows pattern recognition across disparate domains.", "Cognitive psychology research", 0.78);
        addDepthKnowledge("creativity", DepthLevel.DEEP,
            "Psychological safety in teams correlates strongly with creative output. Fear of judgment literally activates threat responses that inhibit the divergent thinking needed for innovation.", "Organizational psychology", 0.76);
        
        addDepthKnowledge("creativity", DepthLevel.EXPERT,
            "Creativity emerges from the dynamic interaction of the executive control network (focused thinking) and the default mode network (mind-wandering). Optimal creativity occurs when these networks are both active but loosely coupled. The 'Aha!' moment involves gamma-band synchronization in the temporal cortex, signaling unconscious insight reaching conscious awareness.", "Computational neuroscience of creativity", 0.72);
        
        // LEARNING
        addDepthKnowledge("learning", DepthLevel.SURFACE,
            "Learning is acquiring new knowledge or skills.", "Basic definition", 0.95);
        addDepthKnowledge("learning", DepthLevel.SURFACE,
            "Practice makes you better at things.", "Common wisdom", 0.92);
        
        addDepthKnowledge("learning", DepthLevel.NUANCE,
            "Spaced repetition is more effective than cramming for long-term retention.", "Memory research", 0.90);
        addDepthKnowledge("learning", DepthLevel.NUANCE,
            "Retrieval practice (testing yourself) strengthens memory more than re-reading.", "Cognitive science", 0.88);
        addDepthKnowledge("learning", DepthLevel.NUANCE,
            "Making mistakes during learning actually improves retention (the 'testing effect').", "Educational psychology", 0.85);
        
        addDepthKnowledge("learning", DepthLevel.DEEP,
            "Learning involves synaptic strengthening (LTP) and neurogenesis in the hippocampus. Sleep plays a crucial role in memory consolidation, transferring short-term memories to long-term storage through hippocampal-neocortical dialogue.", "Neuroscience of learning", 0.80);
        addDepthKnowledge("learning", DepthLevel.DEEP,
            "The 'zone of proximal development' - learning just beyond current ability with guidance - maximizes skill acquisition. This explains why frustrated learners stall and bored learners disengage.", "Vygotskian theory applied", 0.78);
        
        addDepthKnowledge("learning", DepthLevel.EXPERT,
            "Learning follows a power law of practice with exponential decay in learning rate. Metacognitive monitoring accuracy varies with expertise level. The declarative-to-procedural transition involves basal ganglia circuitry and chunk automation through the hippocampus. interleaved practice promotes schema formation by increasing discriminative learning in motor and cognitive domains.", "Computational learning theory and neurobiology", 0.70);
        
        // HAPPINESS
        addDepthKnowledge("happiness", DepthLevel.SURFACE,
            "Happiness is a positive emotional state.", "Common understanding", 0.95);
        addDepthKnowledge("happiness", DepthLevel.SURFACE,
            "Happy people tend to be healthier.", "General observation", 0.88);
        
        addDepthKnowledge("happiness", DepthLevel.NUANCE,
            "Happiness is 50% genetic, but 40% comes from intentional activities.", "Positive psychology research", 0.85);
        addDepthKnowledge("happiness", DepthLevel.NUANCE,
            "The 'hedonic treadmill' means we adapt to good and bad events, returning to a baseline.", "Brickman & Campbell study", 0.82);
        addDepthKnowledge("happiness", DepthLevel.NUANCE,
            "Social connections are one of the strongest predictors of happiness.", "Harvard longitudinal study", 0.88);
        
        addDepthKnowledge("happiness", DepthLevel.DEEP,
            "The posterior cingulate cortex and medial prefrontal cortex form the 'default mode network' that processes self-referential thoughts. Hedonic adaptation involves dopamine receptor downregulation. Purpose and meaning activate the ventral striatum in ways that produce more sustainable happiness than pleasure.", "Affective neuroscience", 0.78);
        
        addDepthKnowledge("happiness", DepthLevel.EXPERT,
            "Subjective well-being correlates with left prefrontal cortex asymmetry and reduced amygdala reactivity. The PERMA model (Positive emotion, Engagement, Relationships, Meaning, Accomplishment) accounts for 90% of variance in life satisfaction. Eudaimonic well-being (purpose) and hedonic well-being (pleasure) show distinct neural signatures, with purpose-related activation in regions associated with goal pursuit and self-transcendence.", "Frontiers in affective neuroscience", 0.70);
        
        // RELATIONSHIPS
        addDepthKnowledge("relationships", DepthLevel.SURFACE,
            "Relationships are connections between people.", "Basic definition", 0.95);
        addDepthKnowledge("relationships", DepthLevel.SURFACE,
            "Good relationships make us happier.", "Common knowledge", 0.90);
        
        addDepthKnowledge("relationships", DepthLevel.NUANCE,
            "It takes about 50 hours of shared time to become casual friends.", "Research findings", 0.88);
        addDepthKnowledge("relationships", DepthLevel.NUANCE,
            "Deep conversations trigger the same brain regions as physical pleasure.", "Social neuroscience", 0.85);
        addDepthKnowledge("relationships", DepthLevel.NUANCE,
            "Vulnerability and reciprocal self-disclosure strengthen bonds.", "Social penetration theory", 0.82);
        
        addDepthKnowledge("relationships", DepthLevel.DEEP,
            "Attachment styles formed in early childhood influence adult relationships through mental models. Secure attachment correlates with better emotional regulation and relationship satisfaction. The anterior cingulate cortex processes social pain similarly to physical pain, explaining why social rejection hurts literally.", "Developmental and social neuroscience", 0.78);
        
        addDepthKnowledge("relationships", DepthLevel.EXPERT,
            "Long-term pair bonding involves vasopressin and oxytocin receptor polymorphisms in the ventral pallidum and nucleus accumbens. The ' Michelangelo effect' describes how partners facilitate each other's self-actualization. Implicit Partner Modelling (IPM) in the mPFC allows automatic prediction and adjustment to partner behavior in successful relationships.", "Neurobiology of social bonding", 0.70);
        
        // TECHNOLOGY
        addDepthKnowledge("technology", DepthLevel.SURFACE,
            "Technology includes tools and machines we use.", "Basic definition", 0.95);
        addDepthKnowledge("technology", DepthLevel.SURFACE,
            "Smartphones are powerful computers we carry everywhere.", "Modern technology", 0.90);
        
        addDepthKnowledge("technology", DepthLevel.NUANCE,
            "The average smartphone has more computing power than the Apollo spacecraft.", "Computing history", 0.88);
        addDepthKnowledge("technology", DepthLevel.NUANCE,
            "Technology shapes how we think, feel, and connect with others.", "Sociotech research", 0.82);
        addDepthKnowledge("technology", DepthLevel.NUANCE,
            "Attention economy: tech companies compete for your limited attention.", "Digital economy research", 0.80);
        
        addDepthKnowledge("technology", DepthLevel.DEEP,
            "The brain's prefrontal cortex is susceptible to ' technoference' - interruptions from devices that fragment attention and reduce deep thinking capacity. The hippocampus, critical for memory, shows reduced activity when we rely on digital 'external memory'.", "Cognitive science of technology use", 0.78);
        
        addDepthKnowledge("technology", DepthLevel.EXPERT,
            "Persuasive technology operates through variable ratio reinforcement schedules, similar to gambling. UX design leverages cognitive load theory and the Fogg Behavior Model (motivation, ability, trigger). Ambient analytics create ' data shadows' that externalize cognition. The techno-utopian/dystopian discourse reflects broader cultural anxieties about agency and autonomy.", "Critical technology studies and behavioral design", 0.70);
        
        // CONSCIOUSNESS
        addDepthKnowledge("consciousness", DepthLevel.SURFACE,
            "Consciousness is being aware of yourself and your surroundings.", "Basic definition", 0.95);
        addDepthKnowledge("consciousness", DepthLevel.SURFACE,
            "Scientists study consciousness but don't fully understand it.", "Current state of research", 0.88);
        
        addDepthKnowledge("consciousness", DepthLevel.NUANCE,
            "The 'hard problem of consciousness' asks why subjective experience exists at all.", "Philosophy of mind", 0.85);
        addDepthKnowledge("consciousness", DepthLevel.NUANCE,
            "Conscious awareness lags behind brain activity by hundreds of milliseconds (Libet's experiments).", "Neuroscience findings", 0.82);
        addDepthKnowledge("consciousness", DepthLevel.NUANCE,
            "Different theories propose consciousness arises from integration, information, or synchronization.", "Consciousness theories", 0.78);
        
        addDepthKnowledge("consciousness", DepthLevel.DEEP,
            "The Global Workspace Theory suggests consciousness arises when information is ' broadcast' across brain regions. The thalamus acts as a gatekeeper, and gamma oscillations (40Hz) may bind conscious experiences. 'Zombie modes' show the brain can process information without conscious awareness.", "Cognitive neuroscience", 0.78);
        
        addDepthKnowledge("consciousness", DepthLevel.EXPERT,
            "Integrated Information Theory (IIT) quantifies consciousness as phi (Φ), measuring integrated information above and beyond individual parts. The Dynamic Core Hypothesis describes reentrant neural activity in thalamocortical loops. The binding problem - how distributed processing becomes unified experience - may be solved through temporal synchronization. Penrose-Hameroff orchestrated objective reduction proposes quantum effects in microtubules.", "Frontiers of consciousness research", 0.68);
        
        // SUCCESS
        addDepthKnowledge("success", DepthLevel.SURFACE,
            "Success is achieving your goals.", "Basic definition", 0.95);
        addDepthKnowledge("success", DepthLevel.SURFACE,
            "Successful people work hard and persist.", "Common perception", 0.88);
        
        addDepthKnowledge("success", DepthLevel.NUANCE,
            "Failure is often part of the path to success.", "Growth mindset research", 0.85);
        addDepthKnowledge("success", DepthLevel.NUANCE,
            "Success means different things to different people.", "Personal values research", 0.82);
        addDepthKnowledge("success", DepthLevel.NUANCE,
            "Resilience - the ability to bounce back - is crucial for long-term success.", "Psychological research", 0.80);
        
        addDepthKnowledge("success", DepthLevel.DEEP,
            "Grit (passion + perseverance) predicts success more than IQ. The anterior cingulate cortex monitors conflict and drives persistent behavior. Failure activates the amygdala, but successful people develop strategies to reframe failure as learning through prefrontal regulation.", "Psychology of achievement", 0.78);
        
        addDepthKnowledge("success", DepthLevel.EXPERT,
            "Success follows a power law distribution - small differences in compounding factors create vast outcomes. The 'Matthew Effect' describes how initial advantages accumulate. Peak performance states involve flow (autonomous processing, clear goals, immediate feedback) in the limbic-cortical feedback loop. Intrinsic motivation quality, measured through self-determination theory, moderates long-term achievement trajectories.", "Complex systems and performance psychology", 0.70);
        
        // PRODUCTIVITY
        addDepthKnowledge("productivity", DepthLevel.SURFACE,
            "Productivity means getting things done efficiently.", "Basic definition", 0.95);
        addDepthKnowledge("productivity", DepthLevel.SURFACE,
            "Productive people manage their time well.", "Common knowledge", 0.88);
        
        addDepthKnowledge("productivity", DepthLevel.NUANCE,
            "The Pomodoro Technique (25-min work + 5-min break) improves focus.", "Time management research", 0.85);
        addDepthKnowledge("productivity", DepthLevel.NUANCE,
            "Working longer hours doesn't mean getting more done (diminishing returns).", "Productivity studies", 0.82);
        addDepthKnowledge("productivity", DepthLevel.NUANCE,
            "Deep work - focused, uninterrupted work - is increasingly valuable.", "Cal Newport's research", 0.80);
        
        addDepthKnowledge("productivity", DepthLevel.DEEP,
            "Attention is a finite resource that depletes with use. Decision fatigue reduces self-control throughout the day. The brain's default mode network activates during rest, subconsciously processing problems. Batching similar tasks reduces 'cognitive switching costs'.", "Cognitive psychology", 0.78);
        
        addDepthKnowledge("productivity", DepthLevel.EXPERT,
            "Productivity optimization involves identifying leverage points in complex systems. The 80/20 principle (Pareto distribution) suggests 20% of inputs produce 80% of outputs. Meta-productivity involves optimizing the process of optimization. Ultradian rhythms (90-120 minute cycles) align with natural attention fluctuations. Strategic laziness - avoiding unnecessary work - often distinguishes high performers.", "Systems theory and cognitive ergonomics", 0.70);
        
        // COMMUNICATION
        addDepthKnowledge("communication", DepthLevel.SURFACE,
            "Communication is sharing information with others.", "Basic definition", 0.95);
        addDepthKnowledge("communication", DepthLevel.SURFACE,
            "Good communication involves talking and listening.", "Common understanding", 0.90);
        
        addDepthKnowledge("communication", DepthLevel.NUANCE,
            "Nonverbal cues (body language, tone) often matter more than words.", "Mehrabian's research", 0.85);
        addDepthKnowledge("communication", DepthLevel.NUANCE,
            "Active listening - reflecting back what you hear - improves understanding.", "Communication skills", 0.82);
        addDepthKnowledge("communication", DepthLevel.NUANCE,
            "Context shapes meaning - the same words can mean different things.", "Pragmatics research", 0.80);
        
        addDepthKnowledge("communication", DepthLevel.DEEP,
            "Mirror neurons in the premotor cortex activate both when performing and observing actions, underlying empathy and communication. The temporal-parietal junction helps us understand others' perspectives. Miscommunication often stems from Theory of Mind errors - assuming others know what we know.", "Social cognitive neuroscience", 0.78);
        
        addDepthKnowledge("communication", DepthLevel.EXPERT,
            "Communication involves coordinated timing between interlocutors, regulated by the suprachiasmatic nucleus and cerebellar timing circuits. Conversational turn-taking shows near-instantaneous neural synchronization (interbrain coherence). The hierarchical prediction framework suggests communication is fundamentally about predicting and updating mental models of communicative partners. Metacommunication (communicating about communication) regulates relationship dynamics.", "Neuropragmatics and interpersonal coordination", 0.70);
    }
    
    /**
     * Add knowledge at a specific depth level for a concept
     */
    private void addDepthKnowledge(String concept, DepthLevel level, String content, 
                                   String source, double confidence) {
        String lowerConcept = concept.toLowerCase();
        if (conceptNodes.containsKey(lowerConcept)) {
            ConceptNode node = conceptNodes.get(lowerConcept);
            node.knowledgeAtDepths.get(level).add(new DepthKnowledge(content, source, confidence));
        }
    }
    
    /**
     * Public method to add knowledge at a specific depth level
     */
    public void addKnowledgeAtDepth(String concept, DepthLevel level, String content, 
                                    String source, double confidence) {
        addDepthKnowledge(concept, level, content, source, confidence);
    }
    
    // ==================== CONCEPT CONNECTIONS ====================
    
    /**
     * Find related concepts to a topic
     */
    public List<String> findRelatedConcepts(String topic) {
        List<String> related = new ArrayList<>();
        String lowerTopic = topic.toLowerCase();
        
        // Direct match
        if (conceptNodes.containsKey(lowerTopic)) {
            related.addAll(conceptNodes.get(lowerTopic).relatedConcepts);
        }
        
        // Partial match
        for (String key : conceptNodes.keySet()) {
            if (lowerTopic.contains(key) || key.contains(lowerTopic)) {
                related.addAll(conceptNodes.get(key).relatedConcepts);
            }
        }
        
        // Domain-based matching
        for (String domain : domainClusters.keySet()) {
            if (domainClusters.get(domain).contains(lowerTopic)) {
                related.addAll(domainClusters.get(domain));
            }
        }
        
        return related;
    }
    
    /**
     * Get cross-domain associations
     */
    public List<String> getCrossDomainAssociations(String topic) {
        List<String> associations = new ArrayList<>();
        String lowerTopic = topic.toLowerCase();
        
        // Find domains the topic belongs to
        Set<String> topicDomains = new HashSet<>();
        for (String domain : domainClusters.keySet()) {
            if (domainClusters.get(domain).contains(lowerTopic)) {
                topicDomains.add(domain);
            }
        }
        
        // Find concepts from OTHER domains that connect
        for (String domain : domainClusters.keySet()) {
            if (!topicDomains.contains(domain)) {
                for (String concept : domainClusters.get(domain)) {
                    if (conceptNodes.containsKey(concept)) {
                        ConceptNode node = conceptNodes.get(concept);
                        // Check if this concept relates to our topic
                        for (String related : node.relatedConcepts) {
                            if (related.toLowerCase().contains(lowerTopic) || 
                                lowerTopic.contains(related.toLowerCase())) {
                                associations.add("[" + domain.toUpperCase() + "] " + 
                                    capitalize(node.concept) + ": " + node.connections.get(0));
                            }
                        }
                    }
                }
            }
        }
        
        return associations;
    }
    
    /**
     * Generate interesting tangent based on topic
     */
    public String generateTangent(String topic) {
        String lowerTopic = topic.toLowerCase();
        
        // Find matching tangent
        for (Tangent tangent : tangents) {
            if (lowerTopic.contains(tangent.trigger)) {
                return tangent.tangent + " " + tangent.connection;
            }
        }
        
        // Generate based on domain
        for (String domain : domainClusters.keySet()) {
            if (domainClusters.get(domain).contains(lowerTopic)) {
                return generateDomainTangent(domain, topic);
            }
        }
        
        // Random tangent
        Tangent randomTangent = tangents.get(random.nextInt(tangents.size()));
        return randomTangent.tangent + " " + randomTangent.connection;
    }
    
    /**
     * Generate tangent for a domain
     */
    private String generateDomainTangent(String domain, String topic) {
        String[] domainTangents = {
            "technology: " + "Here's something fascinating about " + topic + " - it connects to how our brains process information.",
            "science: " + "Speaking of " + topic + ", did you know that scientific understanding of this has evolved dramatically?",
            "philosophy: " + topic + " makes me wonder - philosophers have debated this for centuries.",
            "psychology: " + "There's interesting research on how " + topic + " affects our mental states.",
            "arts: " + topic + " has inspired countless artists throughout history.",
            "life: " + "When it comes to " + topic + ", experience really is the best teacher."
        };
        
        return domainTangents[new Random().nextInt(domainTangents.length)];
    }
    
    /**
     * Get concept description
     */
    public String getConceptDescription(String concept) {
        String lowerConcept = concept.toLowerCase();
        
        if (conceptNodes.containsKey(lowerConcept)) {
            ConceptNode node = conceptNodes.get(lowerConcept);
            return node.connections.get(0);
        }
        
        return null;
    }
    
    /**
     * Get fun fact about concept
     */
    public String getFunFact(String concept) {
        String lowerConcept = concept.toLowerCase();
        
        if (conceptNodes.containsKey(lowerConcept)) {
            ConceptNode node = conceptNodes.get(lowerConcept);
            if (!node.funFacts.isEmpty()) {
                return node.funFacts.get(random.nextInt(node.funFacts.size()));
            }
        }
        
        return null;
    }
    
    /**
     * Build conversation bridge between topics
     */
    public String buildConversationBridge(String fromTopic, String toTopic) {
        String lowerFrom = fromTopic.toLowerCase();
        String lowerTo = toTopic.toLowerCase();
        
        // Find common domain
        String commonDomain = findCommonDomain(lowerFrom, lowerTo);
        
        if (commonDomain != null) {
            return "Speaking of " + fromTopic + ", it reminds me that " + 
                   "in the realm of " + commonDomain + ", " + toTopic + 
                   " is actually connected in an interesting way.";
        }
        
        // Find related concepts
        Set<String> fromRelated = new HashSet<>(findRelatedConcepts(lowerFrom));
        Set<String> toRelated = new HashSet<>(findRelatedConcepts(lowerTo));
        
        fromRelated.retainAll(toRelated);
        
        if (!fromRelated.isEmpty()) {
            String bridgeConcept = fromRelated.iterator().next();
            return "That connects to " + toTopic + " through the concept of " + 
                   bridgeConcept + ". They're more related than they might seem.";
        }
        
        // Default bridge
        return "Shifting from " + fromTopic + " to " + toTopic + ", " +
               "there's actually an interesting connection worth exploring.";
    }
    
    /**
     * Find common domain between two topics
     */
    private String findCommonDomain(String topic1, String topic2) {
        for (String domain : domainClusters.keySet()) {
            Set<String> concepts = domainClusters.get(domain);
            if (concepts.contains(topic1) && concepts.contains(topic2)) {
                return domain;
            }
        }
        return null;
    }
    
    /**
     * Get all concepts in a domain
     */
    public List<String> getDomainConcepts(String domain) {
        if (domainClusters.containsKey(domain)) {
            return new ArrayList<>(domainClusters.get(domain));
        }
        return new ArrayList<>();
    }
    
    /**
     * Search for concepts matching query
     */
    public List<String> searchConcepts(String query) {
        List<String> matches = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (String key : conceptNodes.keySet()) {
            if (key.contains(lowerQuery) || lowerQuery.contains(key)) {
                matches.add(capitalize(key));
            }
        }
        
        for (String domain : domainClusters.keySet()) {
            if (domain.contains(lowerQuery)) {
                matches.add("Domain: " + capitalize(domain));
            }
        }
        
        return matches;
    }
    
    /**
     * Get concept network visualization
     */
    public String getConceptNetwork(String topic) {
        StringBuilder network = new StringBuilder();
        network.append("Concept Network for: ").append(capitalize(topic)).append("\n\n");
        
        String lowerTopic = topic.toLowerCase();
        
        if (conceptNodes.containsKey(lowerTopic)) {
            ConceptNode node = conceptNodes.get(lowerTopic);
            network.append("Related Concepts:\n");
            for (String related : node.relatedConcepts) {
                network.append("  - ").append(capitalize(related)).append("\n");
            }
            network.append("\nDomains: ").append(String.join(", ", node.domains)).append("\n");
        } else {
            network.append("No direct concept found. Here are related topics:\n");
            List<String> related = findRelatedConcepts(topic);
            for (String r : related.stream().limit(5).toArray(String[]::new)) {
                network.append("  - ").append(capitalize(r)).append("\n");
            }
        }
        
        return network.toString();
    }
    
    /**
     * Capitalize first letter
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    // ==================== KNOWLEDGE DEPTH ACCESSORS ====================
    
    /**
     * Get knowledge at a specific depth level for a concept
     * @param concept The concept to get knowledge for
     * @param level The depth level desired
     * @return A random knowledge item at that depth, or null if not available
     */
    public String getKnowledgeAtDepth(String concept, DepthLevel level) {
        String lowerConcept = concept.toLowerCase();
        if (conceptNodes.containsKey(lowerConcept)) {
            ConceptNode node = conceptNodes.get(lowerConcept);
            List<DepthKnowledge> knowledgeList = node.knowledgeAtDepths.get(level);
            if (!knowledgeList.isEmpty()) {
                DepthKnowledge knowledge = knowledgeList.get(random.nextInt(knowledgeList.size()));
                return knowledge.content;
            }
        }
        return null;
    }
    
    /**
     * Get knowledge at a specific depth level with source attribution
     * @param concept The concept to get knowledge for
     * @param level The depth level desired
     * @return Formatted string with content and source
     */
    public String getKnowledgeAtDepthWithSource(String concept, DepthLevel level) {
        String lowerConcept = concept.toLowerCase();
        if (conceptNodes.containsKey(lowerConcept)) {
            ConceptNode node = conceptNodes.get(lowerConcept);
            List<DepthKnowledge> knowledgeList = node.knowledgeAtDepths.get(level);
            if (!knowledgeList.isEmpty()) {
                DepthKnowledge knowledge = knowledgeList.get(random.nextInt(knowledgeList.size()));
                return knowledge.content + " (Source: " + knowledge.source + ", Confidence: " + 
                       String.format("%.0f%%", knowledge.confidence * 100) + ")";
            }
        }
        return null;
    }
    
    /**
     * Get knowledge at the specified depth OR any lower (simpler) depth
     * Useful for when a user wants a deeper explanation but simpler options can substitute
     * @param concept The concept to get knowledge for
     * @param maxLevel The maximum depth level (will include all levels from SURFACE up to this)
     * @return A random knowledge item at or below the specified level
     */
    public String getKnowledgeAtDepthOrLower(String concept, DepthLevel maxLevel) {
        String lowerConcept = concept.toLowerCase();
        if (conceptNodes.containsKey(lowerConcept)) {
            ConceptNode node = conceptNodes.get(lowerConcept);
            
            // Collect all knowledge from SURFACE up to maxLevel
            List<DepthKnowledge> availableKnowledge = new ArrayList<>();
            boolean include = false;
            for (DepthLevel level : DepthLevel.values()) {
                if (level == maxLevel) {
                    include = true;
                }
                if (include) {
                    availableKnowledge.addAll(node.knowledgeAtDepths.get(level));
                }
            }
            
            if (!availableKnowledge.isEmpty()) {
                DepthKnowledge knowledge = availableKnowledge.get(random.nextInt(availableKnowledge.size()));
                return knowledge.content;
            }
        }
        return null;
    }
    
    /**
     * Get the deepest (most expert) knowledge available for a concept
     * @param concept The concept to get knowledge for
     * @return The most detailed knowledge item available, or null
     */
    public String getDeepestKnowledge(String concept) {
        String lowerConcept = concept.toLowerCase();
        if (conceptNodes.containsKey(lowerConcept)) {
            ConceptNode node = conceptNodes.get(lowerConcept);
            
            // Start from EXPERT and work down
            for (int i = DepthLevel.values().length - 1; i >= 0; i--) {
                DepthLevel level = DepthLevel.values()[i];
                List<DepthKnowledge> knowledgeList = node.knowledgeAtDepths.get(level);
                if (!knowledgeList.isEmpty()) {
                    DepthKnowledge knowledge = knowledgeList.get(random.nextInt(knowledgeList.size()));
                    return knowledge.content;
                }
            }
        }
        return null;
    }
    
    /**
     * Get knowledge summary showing what's available at each depth for a concept
     * @param concept The concept to summarize
     * @return Formatted summary of available knowledge at each depth
     */
    public String getKnowledgeSummary(String concept) {
        String lowerConcept = concept.toLowerCase();
        if (!conceptNodes.containsKey(lowerConcept)) {
            return "Concept not found in knowledge network.";
        }
        
        ConceptNode node = conceptNodes.get(lowerConcept);
        StringBuilder summary = new StringBuilder();
        summary.append("Knowledge Depth Summary for: ").append(capitalize(concept)).append("\n\n");
        
        boolean hasAnyKnowledge = false;
        for (DepthLevel level : DepthLevel.values()) {
            List<DepthKnowledge> knowledgeList = node.knowledgeAtDepths.get(level);
            if (!knowledgeList.isEmpty()) {
                hasAnyKnowledge = true;
                summary.append("[").append(level.name()).append("]: ")
                       .append(knowledgeList.size()).append(" item(s) available\n");
                
                // Show sample from each level
                DepthKnowledge sample = knowledgeList.get(0);
                String preview = sample.content.length() > 80 ? 
                    sample.content.substring(0, 77) + "..." : sample.content;
                summary.append("   Sample: \"").append(preview).append("\"\n\n");
            }
        }
        
        if (!hasAnyKnowledge) {
            summary.append("No depth-level knowledge available for this concept.");
        }
        
        return summary.toString();
    }
    
    /**
     * Get all concepts that have knowledge at a specific depth level
     * @param level The depth level to search for
     * @return List of concepts that have knowledge at this level
     */
    public List<String> getConceptsWithDepthLevel(DepthLevel level) {
        List<String> concepts = new ArrayList<>();
        for (String key : conceptNodes.keySet()) {
            ConceptNode node = conceptNodes.get(key);
            if (!node.knowledgeAtDepths.get(level).isEmpty()) {
                concepts.add(capitalize(key));
            }
        }
        return concepts;
    }
    
    /**
     * Find concepts that have expert-level knowledge
     * @return List of concepts with EXPERT-level knowledge
     */
    public List<String> findConceptsWithExpertKnowledge() {
        return getConceptsWithDepthLevel(DepthLevel.EXPERT);
    }
    
    /**
     * Check if a concept has knowledge at a specific depth level
     * @param concept The concept to check
     * @param level The depth level to check
     * @return true if knowledge exists at this level
     */
    public boolean hasKnowledgeAtDepth(String concept, DepthLevel level) {
        String lowerConcept = concept.toLowerCase();
        if (conceptNodes.containsKey(lowerConcept)) {
            ConceptNode node = conceptNodes.get(lowerConcept);
            return !node.knowledgeAtDepths.get(level).isEmpty();
        }
        return false;
    }
    
    /**
     * Get the highest depth level available for a concept
     * @param concept The concept to check
     * @return The highest DepthLevel available, or null if no knowledge
     */
    public DepthLevel getHighestDepthLevel(String concept) {
        String lowerConcept = concept.toLowerCase();
        if (conceptNodes.containsKey(lowerConcept)) {
            ConceptNode node = conceptNodes.get(lowerConcept);
            for (int i = DepthLevel.values().length - 1; i >= 0; i--) {
                DepthLevel level = DepthLevel.values()[i];
                if (!node.knowledgeAtDepths.get(level).isEmpty()) {
                    return level;
                }
            }
        }
        return null;
    }
    
    /**
     * Get all knowledge for a concept as a formatted depth analysis
     * Useful for comprehensive topic exploration
     * @param concept The concept to analyze
     * @return Multi-level analysis from surface to expert
     */
    public String getDepthAnalysis(String concept) {
        String lowerConcept = concept.toLowerCase();
        if (!conceptNodes.containsKey(lowerConcept)) {
            return "Concept not found.";
        }
        
        ConceptNode node = conceptNodes.get(lowerConcept);
        StringBuilder analysis = new StringBuilder();
        analysis.append("=== Depth Analysis: ").append(capitalize(concept)).append(" ===\n\n");
        
        boolean hasAnyContent = false;
        for (DepthLevel level : DepthLevel.values()) {
            List<DepthKnowledge> knowledgeList = node.knowledgeAtDepths.get(level);
            if (!knowledgeList.isEmpty()) {
                hasAnyContent = true;
                analysis.append("--- ").append(level.name()).append(" ---\n");
                
                for (DepthKnowledge k : knowledgeList) {
                    analysis.append("• ").append(k.content);
                    if (k.confidence < 0.8) {
                        analysis.append(" [Confidence: ").append(String.format("%.0f%%", k.confidence * 100)).append("]");
                    }
                    analysis.append("\n");
                }
                analysis.append("\n");
            }
        }
        
        if (!hasAnyContent) {
            analysis.append("No depth-graded knowledge available for this concept.");
        }
        
        return analysis.toString();
    }
    
    // ==================== STATISTICS ====================
    
    /**
     * Get network statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalConcepts", conceptNodes.size());
        stats.put("totalDomains", domainClusters.size());
        stats.put("totalTangents", tangents.size());
        
        Map<String, Integer> domainCounts = new HashMap<>();
        for (String domain : domainClusters.keySet()) {
            domainCounts.put(domain, domainClusters.get(domain).size());
        }
        stats.put("domainBreakdown", domainCounts);
        
        return stats;
    }
}
