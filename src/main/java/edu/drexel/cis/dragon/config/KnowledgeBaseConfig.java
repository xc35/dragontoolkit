/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.kngbase.BasicKnowledgeBase;
/*    */ import edu.drexel.cis.dragon.ir.kngbase.HALSpace;
/*    */ import edu.drexel.cis.dragon.ir.kngbase.KnowledgeBase;
/*    */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*    */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*    */ import edu.drexel.cis.dragon.nlp.extract.TokenExtractor;
/*    */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*    */ 
/*    */ public class KnowledgeBaseConfig extends ConfigUtil
/*    */ {
/*    */   public KnowledgeBaseConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public KnowledgeBaseConfig(ConfigureNode root)
/*    */   {
/* 24 */     super(root);
/*    */   }
/*    */ 
/*    */   public KnowledgeBaseConfig(String configFile) {
/* 28 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public KnowledgeBase getKnowledgeBase(int kngBaseID) {
/* 32 */     return getKnowledgeBase(this.root, kngBaseID);
/*    */   }
/*    */ 
/*    */   public KnowledgeBase getKnowledgeBase(ConfigureNode node, int kngBaseID) {
/* 36 */     return loadKnowledgeBase(node, kngBaseID);
/*    */   }
/*    */ 
/*    */   private KnowledgeBase loadKnowledgeBase(ConfigureNode node, int kngBaseID)
/*    */   {
/* 43 */     ConfigureNode kngBaseNode = getConfigureNode(node, "knowledgebase", kngBaseID);
/* 44 */     if (kngBaseNode == null)
/* 45 */       return null;
/* 46 */     String kngBaseName = kngBaseNode.getNodeName();
/* 47 */     return loadKnowledgeBase(kngBaseName, kngBaseNode);
/*    */   }
/*    */ 
/*    */   protected KnowledgeBase loadKnowledgeBase(String kngBaseName, ConfigureNode kngBaseNode) {
/* 51 */     if (kngBaseName.equalsIgnoreCase("BasicKnowledgeBase"))
/* 52 */       return loadBasicKnowledgeBase(kngBaseNode);
/* 53 */     if (kngBaseName.equalsIgnoreCase("HALSpace")) {
/* 54 */       return loadHALSpace(kngBaseNode);
/*    */     }
/* 56 */     return (KnowledgeBase)loadResource(kngBaseNode);
/*    */   }
/*    */ 
/*    */   private KnowledgeBase loadBasicKnowledgeBase(ConfigureNode node)
/*    */   {
/* 64 */     String rowKeyListFile = node.getString("rowkeyfile");
/* 65 */     String columnKeyListFile = node.getString("columnkeyfile");
/* 66 */     int kngMatrixID = node.getInt("knowledgematrix");
/* 67 */     DoubleSparseMatrix kngMatrix = new SparseMatrixConfig().getDoubleSparseMatrix(node, kngMatrixID);
/* 68 */     return new BasicKnowledgeBase(kngMatrix, new SimpleElementList(rowKeyListFile, false), new SimpleElementList(columnKeyListFile, false));
/*    */   }
/*    */ 
/*    */   private KnowledgeBase loadHALSpace(ConfigureNode node)
/*    */   {
/* 78 */     String matrixFile = node.getString("matrixfile");
/* 79 */     String indexFile = node.getString("indexfile");
/* 80 */     String termListFile = node.getString("termkeyfile");
/* 81 */     int windowSize = node.getInt("windowsize");
/* 82 */     int collectionID = node.getInt("collectionreader");
/* 83 */     CollectionReader reader = new CollectionReaderConfig().getCollectionReader(node, collectionID);
/* 84 */     int tokenExtractorID = node.getInt("tokenextractor");
/* 85 */     TokenExtractor extractor = (TokenExtractor)new ConceptExtractorConfig().getConceptExtractor(node, tokenExtractorID);
/*    */     HALSpace hal;
/* 86 */     if ((matrixFile != null) && (indexFile != null)) {
/* 87 */       hal = new HALSpace(new SimpleElementList(termListFile, false), extractor, windowSize, indexFile, matrixFile);
/*    */     }
/*    */     else
/*    */     {
/* 88 */       if (termListFile != null)
/* 89 */         hal = new HALSpace(new SimpleElementList(termListFile, false), extractor, windowSize);
/*    */       else
/* 91 */         hal = new HALSpace(extractor, windowSize); 
/*    */     }
/* 92 */     if (reader != null) {
/* 93 */       hal.add(reader);
/* 94 */       hal.finalizeData();
/*    */     }
/* 96 */     return hal;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.KnowledgeBaseConfig
 * JD-Core Version:    0.6.2
 */