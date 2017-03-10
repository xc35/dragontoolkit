/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.BasicIndexer;
/*     */ import edu.drexel.cis.dragon.ir.index.DualIndexer;
/*     */ import edu.drexel.cis.dragon.ir.index.Indexer;
/*     */ import edu.drexel.cis.dragon.ir.index.OnlineIndexer;
/*     */ import edu.drexel.cis.dragon.ir.index.sentence.BasicSentenceIndexer;
/*     */ import edu.drexel.cis.dragon.ir.index.sentence.DualSentenceIndexer;
/*     */ import edu.drexel.cis.dragon.ir.index.sentence.OnlineSentenceIndexer;
/*     */ import edu.drexel.cis.dragon.ir.index.sequence.BasicSequenceIndexer;
/*     */ import edu.drexel.cis.dragon.ir.index.sequence.OnlineSequenceIndexer;
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.DualConceptExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.TokenExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.TripleExtractor;
/*     */ 
/*     */ public class IndexerConfig extends ConfigUtil
/*     */ {
/*     */   public IndexerConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public IndexerConfig(ConfigureNode root)
/*     */   {
/*  23 */     super(root);
/*     */   }
/*     */ 
/*     */   public IndexerConfig(String configFile) {
/*  27 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public Indexer getIndexer(int indexerID) {
/*  31 */     return getIndexer(this.root, indexerID);
/*     */   }
/*     */ 
/*     */   public Indexer getIndexer(ConfigureNode node, int indexerID) {
/*  35 */     return loadIndexer(node, indexerID);
/*     */   }
/*     */ 
/*     */   private Indexer loadIndexer(ConfigureNode node, int indexerID)
/*     */   {
/*  42 */     ConfigureNode indexerNode = getConfigureNode(node, "indexer", indexerID);
/*  43 */     if (indexerNode == null)
/*  44 */       return null;
/*  45 */     String indexerName = indexerNode.getNodeName();
/*  46 */     return loadIndexer(indexerName, indexerNode);
/*     */   }
/*     */ 
/*     */   protected Indexer loadIndexer(String indexerName, ConfigureNode indexerNode) {
/*  50 */     if (indexerName.equalsIgnoreCase("BasicIndexer"))
/*  51 */       return loadBasicIndexer(indexerNode);
/*  52 */     if (indexerName.equalsIgnoreCase("OnlineIndexer"))
/*  53 */       return loadOnlineIndexer(indexerNode);
/*  54 */     if (indexerName.equalsIgnoreCase("BasicSequenceIndexer"))
/*  55 */       return loadBasicSequenceIndexer(indexerNode);
/*  56 */     if (indexerName.equalsIgnoreCase("OnlineSequenceIndexer"))
/*  57 */       return loadOnlineSequenceIndexer(indexerNode);
/*  58 */     if (indexerName.equalsIgnoreCase("OnlineSentenceIndexer"))
/*  59 */       return loadOnlineSentenceIndexer(indexerNode);
/*  60 */     if (indexerName.equalsIgnoreCase("BasicSentenceIndexer"))
/*  61 */       return loadBasicSentenceIndexer(indexerNode);
/*  62 */     if (indexerName.equalsIgnoreCase("DualSentenceIndexer"))
/*  63 */       return loadDualSentenceIndexer(indexerNode);
/*  64 */     if (indexerName.equalsIgnoreCase("DualIndexer")) {
/*  65 */       return loadDualIndexer(indexerNode);
/*     */     }
/*  67 */     return (Indexer)loadResource(indexerNode);
/*     */   }
/*     */ 
/*     */   private Indexer loadBasicIndexer(ConfigureNode curNode)
/*     */   {
/*  80 */     String charIndexFolder = curNode.getString("characterindexfolder", null);
/*  81 */     String cptIndexFolder = curNode.getString("conceptindexfolder", null);
/*  82 */     int extractorID = curNode.getInt("conceptextractor", -1);
/*     */     BasicIndexer indexer;
/*  83 */     if (extractorID >= 0) {
/*  84 */       ConceptExtractorConfig cptConfig = new ConceptExtractorConfig();
/*  85 */       ConceptExtractor ce = cptConfig.getConceptExtractor(curNode, extractorID);
/*  86 */       indexer = new BasicIndexer(ce, charIndexFolder, cptIndexFolder);
/*     */     }
/*     */     else {
/*  89 */       extractorID = curNode.getInt("tripleextractor", -1);
/*  90 */       if (extractorID < 0)
/*  91 */         return null;
/*  92 */       TripleExtractorConfig tripleConfig = new TripleExtractorConfig();
/*  93 */       TripleExtractor te = tripleConfig.getTripleExtractor(curNode, extractorID);
/*  94 */       indexer = new BasicIndexer(te, charIndexFolder, cptIndexFolder);
/*     */     }
/*  96 */     String logFile = curNode.getString("logfile", null);
/*  97 */     if (logFile != null) {
/*  98 */       indexer.setLog(logFile);
/*     */     }
/* 100 */     boolean indexAll = curNode.getBoolean("indexall", true);
/* 101 */     boolean indexTitle = curNode.getBoolean("indextitle", false);
/* 102 */     boolean indexAbstract = curNode.getBoolean("indexabstract", false);
/* 103 */     boolean indexBody = curNode.getBoolean("indexbody", false);
/* 104 */     boolean indexMeta = curNode.getBoolean("indexmeta", false);
/* 105 */     indexer.setSectionIndexOption(indexAll, indexTitle, indexAbstract, indexBody, indexMeta);
/* 106 */     indexer.initialize();
/* 107 */     return indexer;
/*     */   }
/*     */ 
/*     */   private Indexer loadOnlineIndexer(ConfigureNode curNode)
/*     */   {
/* 121 */     boolean useConcept = curNode.getBoolean("indexconceptentry", false);
/* 122 */     int extractorID = curNode.getInt("conceptextractor", -1);
/*     */     OnlineIndexer indexer;
/* 123 */     if (extractorID >= 0) {
/* 124 */       ConceptExtractorConfig cptConfig = new ConceptExtractorConfig();
/* 125 */       ConceptExtractor ce = cptConfig.getConceptExtractor(curNode, extractorID);
/* 126 */       indexer = new OnlineIndexer(ce, useConcept);
/*     */     }
/*     */     else {
/* 129 */       extractorID = curNode.getInt("tripleextractor", -1);
/* 130 */       if (extractorID < 0)
/* 131 */         return null;
/* 132 */       TripleExtractorConfig tripleConfig = new TripleExtractorConfig();
/* 133 */       TripleExtractor te = tripleConfig.getTripleExtractor(curNode, extractorID);
/* 134 */       indexer = new OnlineIndexer(te, useConcept);
/*     */     }
/*     */ 
/* 137 */     boolean indexTitle = curNode.getBoolean("indextitle", true);
/* 138 */     boolean indexAbstract = curNode.getBoolean("indexabstract", true);
/* 139 */     boolean indexBody = curNode.getBoolean("indexbody", true);
/* 140 */     boolean indexMeta = curNode.getBoolean("indexmeta", true);
/* 141 */     indexer.screenArticleContent(indexTitle, indexAbstract, indexBody, indexMeta);
/* 142 */     indexer.initialize();
/* 143 */     return indexer;
/*     */   }
/*     */ 
/*     */   private Indexer loadDualIndexer(ConfigureNode curNode)
/*     */   {
/* 153 */     String firstIndexFolder = curNode.getString("firstindexfolder");
/* 154 */     String secondIndexFolder = curNode.getString("secondindexfolder");
/* 155 */     int extractorID = curNode.getInt("dualconceptextractor");
/* 156 */     DualConceptExtractor ce = new DualConceptExtractorConfig().getDualConceptExtractor(curNode, extractorID);
/* 157 */     boolean firstUseConcept = curNode.getBoolean("firstindexconceptentry", false);
/* 158 */     boolean secondUseConcept = curNode.getBoolean("secondindexconceptentry", false);
/* 159 */     DualIndexer indexer = new DualIndexer(ce, firstUseConcept, firstIndexFolder, secondUseConcept, secondIndexFolder);
/* 160 */     String logFile = curNode.getString("logfile", null);
/* 161 */     if (logFile != null)
/* 162 */       indexer.setLog(logFile);
/* 163 */     indexer.initialize();
/* 164 */     return indexer;
/*     */   }
/*     */ 
/*     */   private Indexer loadBasicSentenceIndexer(ConfigureNode curNode)
/*     */   {
/* 177 */     String indexFolder = curNode.getString("characterindexfolder", null);
/*     */     boolean useConcept;
/* 178 */     if (indexFolder != null) {
/* 179 */       useConcept = false;
/*     */     } else {
/* 181 */       indexFolder = curNode.getString("conceptindexfolder", null);
/* 182 */       if (indexFolder == null) {
/* 183 */         return null;
/*     */       }
/* 185 */       useConcept = true;
/*     */     }
/*     */ 
/* 188 */     int extractorID = curNode.getInt("conceptextractor", -1);
/*     */     BasicSentenceIndexer indexer;
/* 189 */     if (extractorID >= 0) {
/* 190 */       ConceptExtractorConfig cptConfig = new ConceptExtractorConfig();
/* 191 */       ConceptExtractor ce = cptConfig.getConceptExtractor(curNode, extractorID);
/* 192 */       indexer = new BasicSentenceIndexer(ce, useConcept, indexFolder);
/*     */     }
/*     */     else {
/* 195 */       extractorID = curNode.getInt("tripleextractor", -1);
/* 196 */       if (extractorID < 0)
/* 197 */         return null;
/* 198 */       TripleExtractorConfig tripleConfig = new TripleExtractorConfig();
/* 199 */       TripleExtractor te = tripleConfig.getTripleExtractor(curNode, extractorID);
/* 200 */       indexer = new BasicSentenceIndexer(te, useConcept, indexFolder);
/*     */     }
/* 202 */     String logFile = curNode.getString("logfile", null);
/* 203 */     if (logFile != null) {
/* 204 */       indexer.setLog(logFile);
/*     */     }
/* 206 */     boolean indexTitle = curNode.getBoolean("indextitle", true);
/* 207 */     boolean indexAbstract = curNode.getBoolean("indexabstract", true);
/* 208 */     boolean indexBody = curNode.getBoolean("indexbody", true);
/* 209 */     indexer.screenArticleContent(indexTitle, indexAbstract, indexBody);
/* 210 */     indexer.initialize();
/* 211 */     indexer.setMinSentenceLength(curNode.getInt("minsentencelength", 1));
/* 212 */     return indexer;
/*     */   }
/*     */ 
/*     */   private Indexer loadOnlineSentenceIndexer(ConfigureNode curNode)
/*     */   {
/* 224 */     boolean useConcept = curNode.getBoolean("indexconceptentry", false);
/* 225 */     int extractorID = curNode.getInt("conceptextractor", -1);
/*     */     OnlineSentenceIndexer indexer;
/* 226 */     if (extractorID >= 0) {
/* 227 */       ConceptExtractorConfig cptConfig = new ConceptExtractorConfig();
/* 228 */       ConceptExtractor ce = cptConfig.getConceptExtractor(curNode, extractorID);
/* 229 */       indexer = new OnlineSentenceIndexer(ce, useConcept);
/*     */     }
/*     */     else {
/* 232 */       extractorID = curNode.getInt("tripleextractor", -1);
/* 233 */       if (extractorID < 0)
/* 234 */         return null;
/* 235 */       TripleExtractorConfig tripleConfig = new TripleExtractorConfig();
/* 236 */       TripleExtractor te = tripleConfig.getTripleExtractor(curNode, extractorID);
/* 237 */       indexer = new OnlineSentenceIndexer(te, useConcept);
/*     */     }
/*     */ 
/* 240 */     boolean indexTitle = curNode.getBoolean("indextitle", true);
/* 241 */     boolean indexAbstract = curNode.getBoolean("indexabstract", true);
/* 242 */     boolean indexBody = curNode.getBoolean("indexbody", true);
/* 243 */     indexer.screenArticleContent(indexTitle, indexAbstract, indexBody);
/* 244 */     indexer.initialize();
/* 245 */     indexer.setMinSentenceLength(curNode.getInt("minsentencelength", 1));
/* 246 */     return indexer;
/*     */   }
/*     */ 
/*     */   private Indexer loadDualSentenceIndexer(ConfigureNode curNode)
/*     */   {
/* 256 */     String firstIndexFolder = curNode.getString("firstindexfolder");
/* 257 */     String secondIndexFolder = curNode.getString("secondindexfolder");
/* 258 */     int extractorID = curNode.getInt("dualconceptextractor");
/* 259 */     DualConceptExtractor ce = new DualConceptExtractorConfig().getDualConceptExtractor(curNode, extractorID);
/* 260 */     boolean firstUseConcept = curNode.getBoolean("firstindexconceptentry", false);
/* 261 */     boolean secondUseConcept = curNode.getBoolean("secondindexconceptentry", false);
/* 262 */     DualSentenceIndexer indexer = new DualSentenceIndexer(ce, firstUseConcept, firstIndexFolder, secondUseConcept, secondIndexFolder);
/* 263 */     String logFile = curNode.getString("logfile", null);
/* 264 */     if (logFile != null)
/* 265 */       indexer.setLog(logFile);
/* 266 */     indexer.initialize();
/* 267 */     return indexer;
/*     */   }
/*     */ 
/*     */   private Indexer loadBasicSequenceIndexer(ConfigureNode curNode)
/*     */   {
/* 278 */     int extractorID = curNode.getInt("conceptextractor");
/* 279 */     ConceptExtractorConfig cptConfig = new ConceptExtractorConfig();
/* 280 */     TokenExtractor te = (TokenExtractor)cptConfig.getConceptExtractor(curNode, extractorID);
/* 281 */     String indexFolder = curNode.getString("characterindexfolder");
/* 282 */     BasicSequenceIndexer indexer = new BasicSequenceIndexer(te, indexFolder);
/* 283 */     String logFile = curNode.getString("logfile", null);
/* 284 */     if (logFile != null)
/* 285 */       indexer.setLog(logFile);
/* 286 */     boolean indexTitle = curNode.getBoolean("indextitle", false);
/* 287 */     boolean indexAbstract = curNode.getBoolean("indexabstract", false);
/* 288 */     boolean indexBody = curNode.getBoolean("indexbody", false);
/* 289 */     boolean indexMeta = curNode.getBoolean("indexmeta", false);
/* 290 */     indexer.setSectionIndexOption(indexTitle, indexAbstract, indexBody, indexMeta);
/* 291 */     indexer.initialize();
/* 292 */     return indexer;
/*     */   }
/*     */ 
/*     */   private Indexer loadOnlineSequenceIndexer(ConfigureNode curNode)
/*     */   {
/* 302 */     int extractorID = curNode.getInt("conceptextractor");
/* 303 */     ConceptExtractorConfig cptConfig = new ConceptExtractorConfig();
/* 304 */     TokenExtractor te = (TokenExtractor)cptConfig.getConceptExtractor(curNode, extractorID);
/* 305 */     OnlineSequenceIndexer indexer = new OnlineSequenceIndexer(te);
/* 306 */     boolean indexTitle = curNode.getBoolean("indextitle", false);
/* 307 */     boolean indexAbstract = curNode.getBoolean("indexabstract", false);
/* 308 */     boolean indexBody = curNode.getBoolean("indexbody", false);
/* 309 */     boolean indexMeta = curNode.getBoolean("indexmeta", false);
/* 310 */     indexer.setSectionIndexOption(indexTitle, indexAbstract, indexBody, indexMeta);
/* 311 */     indexer.initialize();
/* 312 */     return indexer;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.IndexerConfig
 * JD-Core Version:    0.6.2
 */