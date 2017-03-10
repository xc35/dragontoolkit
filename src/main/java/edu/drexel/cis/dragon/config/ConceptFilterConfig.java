/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.extract.BasicConceptFilter;
/*    */ import edu.drexel.cis.dragon.nlp.extract.ConceptFilter;
/*    */ 
/*    */ public class ConceptFilterConfig extends ConfigUtil
/*    */ {
/*    */   public ConceptFilterConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ConceptFilterConfig(ConfigureNode root)
/*    */   {
/* 20 */     super(root);
/*    */   }
/*    */ 
/*    */   public ConceptFilterConfig(String configFile) {
/* 24 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public ConceptFilter getConceptFilter(int taggerID) {
/* 28 */     return getConceptFilter(this.root, taggerID);
/*    */   }
/*    */ 
/*    */   public ConceptFilter getConceptFilter(ConfigureNode node, int conceptFilterID) {
/* 32 */     return loadConceptFilter(node, conceptFilterID);
/*    */   }
/*    */ 
/*    */   private ConceptFilter loadConceptFilter(ConfigureNode node, int conceptFilterID)
/*    */   {
/* 38 */     ConfigureNode conceptFilterNode = getConfigureNode(node, "conceptFilter", conceptFilterID);
/* 39 */     if (conceptFilterNode == null) {
/* 40 */       return null;
/*    */     }
/* 42 */     String conceptFilterName = conceptFilterNode.getNodeName();
/* 43 */     if (conceptFilterName.equalsIgnoreCase("BasicConceptFilter")) {
/* 44 */       return loadBasicConceptFilter(conceptFilterNode);
/*    */     }
/* 46 */     return (ConceptFilter)loadResource(conceptFilterNode);
/*    */   }
/*    */ 
/*    */   private ConceptFilter loadBasicConceptFilter(ConfigureNode curNode)
/*    */   {
/* 54 */     String stoplistFile = curNode.getString("stoplistfile", null);
/* 55 */     String excludedSTYFile = curNode.getString("excludedstyfile", null);
/* 56 */     String supportedSTYFile = curNode.getString("supportedstyfile", null);
/* 57 */     String excludedSTY = curNode.getString("excludedsty", null);
/* 58 */     String supportedSTY = curNode.getString("supportedsty", null);
/* 59 */     BasicConceptFilter conceptFilter = new BasicConceptFilter(stoplistFile, supportedSTYFile, excludedSTYFile);
/* 60 */     if ((excludedSTY != null) && (excludedSTY.trim().length() > 0))
/* 61 */       conceptFilter.addMultiExcludedSTY(excludedSTY.trim());
/* 62 */     if ((supportedSTY != null) && (supportedSTY.trim().length() > 0))
/* 63 */       conceptFilter.addMultiSupportedSTY(supportedSTY.trim());
/* 64 */     return conceptFilter;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.ConceptFilterConfig
 * JD-Core Version:    0.6.2
 */