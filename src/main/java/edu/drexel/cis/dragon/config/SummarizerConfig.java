/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.sentence.OnlineSentenceIndexer;
/*    */ import edu.drexel.cis.dragon.ir.kngbase.KnowledgeBase;
/*    */ import edu.drexel.cis.dragon.ir.summarize.GenericMultiDocSummarizer;
/*    */ import edu.drexel.cis.dragon.ir.summarize.LexRankSummarizer;
/*    */ import edu.drexel.cis.dragon.ir.summarize.SemanticRankSummarizer;
/*    */ 
/*    */ public class SummarizerConfig extends ConfigUtil
/*    */ {
/*    */   public SummarizerConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public SummarizerConfig(ConfigureNode root)
/*    */   {
/* 22 */     super(root);
/*    */   }
/*    */ 
/*    */   public SummarizerConfig(String configFile) {
/* 26 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public GenericMultiDocSummarizer getGenericMultiDocSummarizer(int summarizerID) {
/* 30 */     return getGenericMultiDocSummarizer(this.root, summarizerID);
/*    */   }
/*    */ 
/*    */   public GenericMultiDocSummarizer getGenericMultiDocSummarizer(ConfigureNode node, int summarizerID) {
/* 34 */     return loadGenericMultiDocSummarizer(node, summarizerID);
/*    */   }
/*    */ 
/*    */   private GenericMultiDocSummarizer loadGenericMultiDocSummarizer(ConfigureNode node, int summarizerID)
/*    */   {
/* 41 */     ConfigureNode summarizerNode = getConfigureNode(node, "genericmultidocsummarizer", summarizerID);
/* 42 */     if (summarizerNode == null)
/* 43 */       return null;
/* 44 */     String summarizerName = summarizerNode.getNodeName();
/* 45 */     return loadGenericMultiDocSummarizer(summarizerName, summarizerNode);
/*    */   }
/*    */ 
/*    */   protected GenericMultiDocSummarizer loadGenericMultiDocSummarizer(String summarizerName, ConfigureNode summarizerNode) {
/* 49 */     if (summarizerName.equalsIgnoreCase("LexRankSummarizer"))
/* 50 */       return loadLexRankSummarizer(summarizerNode);
/* 51 */     if (summarizerName.equalsIgnoreCase("SemanticRankSummarizer")) {
/* 52 */       return loadSemanticRankSummarizer(summarizerNode);
/*    */     }
/* 54 */     return (GenericMultiDocSummarizer)loadResource(summarizerNode);
/*    */   }
/*    */ 
/*    */   private GenericMultiDocSummarizer loadLexRankSummarizer(ConfigureNode node)
/*    */   {
/* 64 */     boolean tfidf = node.getBoolean("tfidf", true);
/* 65 */     boolean continuous = node.getBoolean("continuousscore", true);
/* 66 */     double similarityThreshold = node.getDouble("similaritythreshold");
/* 67 */     int indexerID = node.getInt("onlinesentenceindexer");
/* 68 */     OnlineSentenceIndexer indexer = (OnlineSentenceIndexer)new IndexerConfig().getIndexer(node, indexerID);
/* 69 */     LexRankSummarizer summarizer = new LexRankSummarizer(indexer, tfidf);
/* 70 */     summarizer.setContinuousScoreOpiton(continuous);
/* 71 */     summarizer.setSimilarityThreshold(similarityThreshold);
/* 72 */     return summarizer;
/*    */   }
/*    */ 
/*    */   private GenericMultiDocSummarizer loadSemanticRankSummarizer(ConfigureNode node)
/*    */   {
/* 82 */     double transCoefficient = node.getDouble("transcoefficient", 0.3D);
/* 83 */     double bkgCoefficient = node.getDouble("bkgcoefficient", 0.5D);
/* 84 */     int indexerID = node.getInt("tokenindexer");
/* 85 */     OnlineSentenceIndexer tokenIndexer = (OnlineSentenceIndexer)new IndexerConfig().getIndexer(node, indexerID);
/* 86 */     int kngID = node.getInt("knowledgebase");
/* 87 */     KnowledgeBase kngBase = new KnowledgeBaseConfig().getKnowledgeBase(node, kngID);
/* 88 */     indexerID = node.getInt("phraseindexer");
/*    */     SemanticRankSummarizer summarizer;
/* 89 */     if (indexerID > 0) {
/* 90 */       OnlineSentenceIndexer phraseIndexer = (OnlineSentenceIndexer)new IndexerConfig().getIndexer(node, indexerID);
/* 91 */       summarizer = new SemanticRankSummarizer(tokenIndexer, phraseIndexer, kngBase);
/*    */     }
/*    */     else {
/* 94 */       summarizer = new SemanticRankSummarizer(tokenIndexer, kngBase);
/* 95 */     }summarizer.setBackgroundCoefficient(bkgCoefficient);
/* 96 */     summarizer.setTranslationCoefficient(transCoefficient);
/* 97 */     summarizer.setMaxKLDivDistance(node.getDouble("maxkldivdistance", 10.0D));
/* 98 */     return summarizer;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.SummarizerConfig
 * JD-Core Version:    0.6.2
 */