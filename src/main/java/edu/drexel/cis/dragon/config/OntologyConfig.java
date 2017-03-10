/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.ontology.BasicOntology;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.Ontology;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.umls.UmlsAmbiguityOntology;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.umls.UmlsFileBackedOntology;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ 
/*     */ public class OntologyConfig extends ConfigUtil
/*     */ {
/*     */   public OntologyConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public OntologyConfig(ConfigureNode root)
/*     */   {
/*  22 */     super(root);
/*     */   }
/*     */ 
/*     */   public OntologyConfig(String configFile) {
/*  26 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public Ontology getOntology(int taggerID) {
/*  30 */     return getOntology(this.root, taggerID);
/*     */   }
/*     */ 
/*     */   public Ontology getOntology(ConfigureNode node, int ontologyID) {
/*  34 */     return loadOntology(node, ontologyID);
/*     */   }
/*     */ 
/*     */   private Ontology loadOntology(ConfigureNode node, int ontologyID)
/*     */   {
/*  40 */     ConfigureNode ontologyNode = getConfigureNode(node, "ontology", ontologyID);
/*  41 */     if (ontologyNode == null)
/*  42 */       return null;
/*  43 */     String ontologyName = ontologyNode.getNodeName();
/*  44 */     return loadOntology(ontologyName, node, ontologyNode);
/*     */   }
/*     */ 
/*     */   protected Ontology loadOntology(String ontologyName, ConfigureNode node, ConfigureNode ontologyNode) {
/*  48 */     if (ontologyName.equalsIgnoreCase("BasicOntology"))
/*  49 */       return loadBasicOntology(ontologyNode);
/*  50 */     if (ontologyName.equalsIgnoreCase("UmlsExactOntology"))
/*  51 */       return loadUmlsExactOntology(ontologyNode);
/*  52 */     if (ontologyName.equalsIgnoreCase("UmlsAmbiguityOntology")) {
/*  53 */       return loadUmlsAmbiguityOntology(ontologyNode);
/*     */     }
/*  55 */     return (Ontology)loadResource(ontologyNode);
/*     */   }
/*     */ 
/*     */   private Ontology loadBasicOntology(ConfigureNode curNode)
/*     */   {
/*  66 */     String termFile = curNode.getString("termfile");
/*  67 */     int lemmatiserID = curNode.getInt("lemmatiser", 0);
/*  68 */     boolean lemmaOption = curNode.getBoolean("lemmaoption", false);
/*  69 */     boolean nppOption = curNode.getBoolean("nppoption", false);
/*  70 */     boolean coordinateOption = curNode.getBoolean("coordinateoption", false);
/*  71 */     boolean adjtermOption = curNode.getBoolean("adjtermoption", false);
/*  72 */     boolean senseOption = curNode.getBoolean("sensedisambiguation", false);
/*  73 */     String nonBoundaryPunc = curNode.getString("nonboundarypunctuation", "");
/*     */ 
/*  75 */     LemmatiserConfig lemmaConfig = new LemmatiserConfig();
/*  76 */     Lemmatiser lemmatiser = lemmaConfig.getLemmatiser(curNode, lemmatiserID);
/*  77 */     BasicOntology ontology = new BasicOntology(termFile, lemmatiser);
/*  78 */     ontology.setLemmaOption(lemmaOption);
/*  79 */     ontology.setAdjectiveTermOption(adjtermOption);
/*  80 */     ontology.setCoordinateOption(coordinateOption);
/*  81 */     ontology.setNPPOption(nppOption);
/*  82 */     ontology.setSenseDisambiguationOption(senseOption);
/*  83 */     ontology.setNonBoundaryPunctuation(nonBoundaryPunc);
/*  84 */     return ontology;
/*     */   }
/*     */ 
/*     */   private Ontology loadUmlsExactOntology(ConfigureNode vobNode)
/*     */   {
/*  95 */     String directory = vobNode.getString("directory", null);
/*  96 */     int lemmatiserID = vobNode.getInt("lemmatiser", 0);
/*  97 */     boolean lemmaOption = vobNode.getBoolean("lemmaoption", false);
/*  98 */     boolean nppOption = vobNode.getBoolean("nppoption", false);
/*  99 */     boolean coordinateOption = vobNode.getBoolean("coordinateoption", false);
/* 100 */     boolean adjtermOption = vobNode.getBoolean("adjtermoption", false);
/* 101 */     boolean senseOption = vobNode.getBoolean("sensedisambiguation", false);
/* 102 */     String nonBoundaryPunc = vobNode.getString("nonboundarypunctuation", "");
/*     */ 
/* 104 */     LemmatiserConfig lemmaConfig = new LemmatiserConfig();
/* 105 */     Lemmatiser lemmatiser = lemmaConfig.getLemmatiser(vobNode, lemmatiserID);
/*     */     UmlsFileBackedOntology ontology;
/* 106 */     if (directory == null)
/* 107 */       ontology = new UmlsFileBackedOntology(lemmatiser);
/*     */     else
/* 109 */       ontology = new UmlsFileBackedOntology(directory, lemmatiser);
/* 110 */     ontology.setLemmaOption(lemmaOption);
/* 111 */     ontology.setAdjectiveTermOption(adjtermOption);
/* 112 */     ontology.setCoordinateOption(coordinateOption);
/* 113 */     ontology.setNPPOption(nppOption);
/* 114 */     ontology.setSenseDisambiguationOption(senseOption);
/* 115 */     ontology.setNonBoundaryPunctuation(nonBoundaryPunc);
/* 116 */     return ontology;
/*     */   }
/*     */ 
/*     */   private Ontology loadUmlsAmbiguityOntology(ConfigureNode vobNode)
/*     */   {
/* 129 */     String directory = vobNode.getString("directory", null);
/* 130 */     int lemmatiserID = vobNode.getInt("lemmatiser", 0);
/* 131 */     boolean lemmaOption = vobNode.getBoolean("lemmaoption", false);
/* 132 */     boolean nppOption = vobNode.getBoolean("nppoption", false);
/* 133 */     boolean coordinateOption = vobNode.getBoolean("coordinateoption", false);
/* 134 */     boolean adjtermOption = vobNode.getBoolean("adjtermoption", false);
/* 135 */     boolean senseOption = vobNode.getBoolean("sensedisambiguation", false);
/* 136 */     String nonBoundaryPunc = vobNode.getString("nonboundarypunctuation", "");
/* 137 */     double minScore = vobNode.getDouble("minscore", 0.95D);
/* 138 */     double minSelectivity = vobNode.getDouble("minselectivity", 0.0D);
/* 139 */     int maxSkippedWord = vobNode.getInt("maxskippedword", 1);
/*     */ 
/* 141 */     LemmatiserConfig lemmaConfig = new LemmatiserConfig();
/* 142 */     Lemmatiser lemmatiser = lemmaConfig.getLemmatiser(vobNode, lemmatiserID);
/*     */     UmlsAmbiguityOntology ontology;
/* 143 */     if (directory == null)
/* 144 */       ontology = new UmlsAmbiguityOntology(lemmatiser);
/*     */     else
/* 146 */       ontology = new UmlsAmbiguityOntology(directory, lemmatiser);
/* 147 */     ontology.setLemmaOption(lemmaOption);
/* 148 */     ontology.setAdjectiveTermOption(adjtermOption);
/* 149 */     ontology.setCoordinateOption(coordinateOption);
/* 150 */     ontology.setNPPOption(nppOption);
/* 151 */     ontology.setSenseDisambiguationOption(senseOption);
/* 152 */     ontology.setNonBoundaryPunctuation(nonBoundaryPunc);
/* 153 */     ontology.setMinScore(minScore);
/* 154 */     ontology.setMinSelectivity(minSelectivity);
/* 155 */     ontology.setMaxSkippedWords(maxSkippedWord);
/* 156 */     return ontology;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.OntologyConfig
 * JD-Core Version:    0.6.2
 */