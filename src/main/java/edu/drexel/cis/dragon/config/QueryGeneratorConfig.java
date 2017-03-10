/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.kngbase.KnowledgeBase;
/*     */ import edu.drexel.cis.dragon.ir.query.BasicQueryGenerator;
/*     */ import edu.drexel.cis.dragon.ir.query.PhraseQEGenerator;
/*     */ import edu.drexel.cis.dragon.ir.query.QueryGenerator;
/*     */ import edu.drexel.cis.dragon.nlp.extract.PhraseExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.TokenExtractor;
/*     */ 
/*     */ public class QueryGeneratorConfig extends ConfigUtil
/*     */ {
/*     */   public QueryGeneratorConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public QueryGeneratorConfig(ConfigureNode root)
/*     */   {
/*  22 */     super(root);
/*     */   }
/*     */ 
/*     */   public QueryGeneratorConfig(String configFile) {
/*  26 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public QueryGenerator getQueryGenerator(int queryGeneratorID) {
/*  30 */     return getQueryGenerator(this.root, queryGeneratorID);
/*     */   }
/*     */ 
/*     */   public QueryGenerator getQueryGenerator(ConfigureNode node, int queryGeneratorID) {
/*  34 */     return loadQueryGenerator(node, queryGeneratorID);
/*     */   }
/*     */ 
/*     */   private QueryGenerator loadQueryGenerator(ConfigureNode node, int queryGeneratorID)
/*     */   {
/*  41 */     ConfigureNode queryGeneratorNode = getConfigureNode(node, "queryGenerator", queryGeneratorID);
/*  42 */     if (queryGeneratorNode == null)
/*  43 */       return null;
/*  44 */     String queryGeneratorName = queryGeneratorNode.getNodeName();
/*  45 */     return loadQueryGenerator(queryGeneratorName, queryGeneratorNode);
/*     */   }
/*     */ 
/*     */   protected QueryGenerator loadQueryGenerator(String queryGeneratorName, ConfigureNode queryGeneratorNode) {
/*  49 */     if (queryGeneratorName.equalsIgnoreCase("BasicQueryGenerator"))
/*  50 */       return loadBasicQueryGenerator(queryGeneratorNode);
/*  51 */     if (queryGeneratorName.equalsIgnoreCase("PhraseQEGenerator")) {
/*  52 */       return loadPhraseQEGenerator(queryGeneratorNode);
/*     */     }
/*  54 */     return (QueryGenerator)loadResource(queryGeneratorNode);
/*     */   }
/*     */ 
/*     */   private QueryGenerator loadBasicQueryGenerator(ConfigureNode node)
/*     */   {
/*  63 */     BasicQueryGenerator generator = null;
/*  64 */     boolean useConcept = node.getBoolean("useconcept", false);
/*  65 */     int tripleExtractorID = node.getInt("tripleextractor", 0);
/*  66 */     if (tripleExtractorID <= 0) {
/*  67 */       int conceptExtractorID = node.getInt("conceptextractor");
/*  68 */       generator = new BasicQueryGenerator(new ConceptExtractorConfig().getConceptExtractor(node, conceptExtractorID), useConcept);
/*     */     }
/*     */     else {
/*  71 */       generator = new BasicQueryGenerator(new TripleExtractorConfig().getTripleExtractor(node, tripleExtractorID), useConcept);
/*     */     }
/*  73 */     double title = node.getDouble("titleweight", 0.0D);
/*  74 */     double body = node.getDouble("bodyweight", 0.0D);
/*  75 */     double abt = node.getDouble("abstractweight", 0.0D);
/*  76 */     double meta = node.getDouble("metaweight", 0.0D);
/*  77 */     double subterm = node.getDouble("subterm", 1.0D);
/*  78 */     generator.initialize(title, abt, body, meta, subterm);
/*  79 */     return generator;
/*     */   }
/*     */ 
/*     */   private QueryGenerator loadPhraseQEGenerator(ConfigureNode node)
/*     */   {
/*  91 */     boolean useTitle = node.getBoolean("usetitle", true);
/*  92 */     boolean useAbstract = node.getBoolean("useabstract", false);
/*  93 */     boolean useBody = node.getBoolean("usebody", false);
/*  94 */     boolean useMeta = node.getBoolean("usemeta", false);
/*  95 */     int expandTermNum = node.getInt("expandtermnum", 10);
/*  96 */     double transCoefficient = node.getDouble("transcoefficient", 0.5D);
/*  97 */     int tokenExtractorID = node.getInt("tokenextractor");
/*  98 */     int phraseExtractorID = node.getInt("phraseextractor");
/*  99 */     int kngBaseID = node.getInt("knowledgebase");
/* 100 */     KnowledgeBase kngBase = new KnowledgeBaseConfig().getKnowledgeBase(node, kngBaseID);
/* 101 */     TokenExtractor tokenExtractor = (TokenExtractor)new ConceptExtractorConfig().getConceptExtractor(node, tokenExtractorID);
/*     */     PhraseQEGenerator generator;
/* 102 */     if (phraseExtractorID > 0) {
/* 103 */       PhraseExtractor phraseExtractor = (PhraseExtractor)new ConceptExtractorConfig().getConceptExtractor(node, phraseExtractorID);
/* 104 */       generator = new PhraseQEGenerator(kngBase, phraseExtractor, tokenExtractor, transCoefficient, expandTermNum);
/*     */     }
/*     */     else {
/* 107 */       generator = new PhraseQEGenerator(kngBase, tokenExtractor, transCoefficient, expandTermNum);
/* 108 */     }generator.initialize(useTitle, useAbstract, useBody, useMeta);
/* 109 */     return generator;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.QueryGeneratorConfig
 * JD-Core Version:    0.6.2
 */