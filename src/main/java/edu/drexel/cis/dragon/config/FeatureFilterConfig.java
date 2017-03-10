/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.DocFrequencyFilter;
/*    */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*    */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.NullFeatureFilter;
/*    */ 
/*    */ public class FeatureFilterConfig extends ConfigUtil
/*    */ {
/*    */   public FeatureFilterConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public FeatureFilterConfig(ConfigureNode root)
/*    */   {
/* 20 */     super(root);
/*    */   }
/*    */ 
/*    */   public FeatureFilterConfig(String configFile) {
/* 24 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public FeatureFilter getFeatureFilter(int filterID) {
/* 28 */     return getFeatureFilter(this.root, filterID);
/*    */   }
/*    */ 
/*    */   public FeatureFilter getFeatureFilter(ConfigureNode node, int filterID) {
/* 32 */     return loadFeatureFilter(node, filterID);
/*    */   }
/*    */ 
/*    */   private FeatureFilter loadFeatureFilter(ConfigureNode node, int filterID)
/*    */   {
/* 39 */     ConfigureNode filterNode = getConfigureNode(node, "featurefilter", filterID);
/* 40 */     if (filterNode == null)
/* 41 */       return null;
/* 42 */     String filterName = filterNode.getNodeName();
/* 43 */     return loadFeatureFilter(filterName, filterNode);
/*    */   }
/*    */ 
/*    */   protected FeatureFilter loadFeatureFilter(String filterName, ConfigureNode filterNode) {
/* 47 */     if (filterName.equalsIgnoreCase("NullFeatureFilter"))
/* 48 */       return new NullFeatureFilter();
/* 49 */     if (filterName.equalsIgnoreCase("DocFrequencyFilter")) {
/* 50 */       return loadDocFrequencySelector(filterNode);
/*    */     }
/* 52 */     return (FeatureFilter)loadResource(filterNode);
/*    */   }
/*    */ 
/*    */   private FeatureFilter loadDocFrequencySelector(ConfigureNode node) {
/* 56 */     return new DocFrequencyFilter(node.getInt("mindocfrequency", 5));
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.FeatureFilterConfig
 * JD-Core Version:    0.6.2
 */