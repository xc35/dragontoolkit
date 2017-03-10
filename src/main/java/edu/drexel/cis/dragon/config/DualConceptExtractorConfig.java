/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.extract.DualConceptExtractor;
/*    */ 
/*    */ public class DualConceptExtractorConfig extends ConfigUtil
/*    */ {
/*    */   public DualConceptExtractorConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public DualConceptExtractorConfig(ConfigureNode root)
/*    */   {
/* 20 */     super(root);
/*    */   }
/*    */ 
/*    */   public DualConceptExtractorConfig(String configFile) {
/* 24 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public DualConceptExtractor getDualConceptExtractor(int extractorID) {
/* 28 */     return loadDualConceptExtractor(this.root, extractorID);
/*    */   }
/*    */ 
/*    */   public DualConceptExtractor getDualConceptExtractor(ConfigureNode node, int extractorID) {
/* 32 */     return loadDualConceptExtractor(node, extractorID);
/*    */   }
/*    */ 
/*    */   private DualConceptExtractor loadDualConceptExtractor(ConfigureNode node, int extractorID)
/*    */   {
/* 39 */     ConfigureNode extractorNode = getConfigureNode(node, "dualconceptextractor", extractorID);
/* 40 */     if (extractorNode == null)
/* 41 */       return null;
/* 42 */     String extractorName = extractorNode.getNodeName();
/* 43 */     return loadDualConceptExtractor(extractorName, extractorNode);
/*    */   }
/*    */ 
/*    */   protected DualConceptExtractor loadDualConceptExtractor(String extractorName, ConfigureNode extractorNode) {
/* 47 */     return (DualConceptExtractor)loadResource(extractorNode);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.DualConceptExtractorConfig
 * JD-Core Version:    0.6.2
 */