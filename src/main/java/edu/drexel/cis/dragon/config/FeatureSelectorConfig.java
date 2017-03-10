/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.classification.featureselection.ChiFeatureSelector;
/*    */ import edu.drexel.cis.dragon.ir.classification.featureselection.DocFrequencySelector;
/*    */ import edu.drexel.cis.dragon.ir.classification.featureselection.FeatureSelector;
/*    */ import edu.drexel.cis.dragon.ir.classification.featureselection.InfoGainFeatureSelector;
/*    */ import edu.drexel.cis.dragon.ir.classification.featureselection.MutualInfoFeatureSelector;
/*    */ import edu.drexel.cis.dragon.ir.classification.featureselection.NullFeatureSelector;
/*    */ 
/*    */ public class FeatureSelectorConfig extends ConfigUtil
/*    */ {
/*    */   public FeatureSelectorConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public FeatureSelectorConfig(ConfigureNode root)
/*    */   {
/* 20 */     super(root);
/*    */   }
/*    */ 
/*    */   public FeatureSelectorConfig(String configFile) {
/* 24 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public FeatureSelector getFeatureSelector(int selectorID) {
/* 28 */     return getFeatureSelector(this.root, selectorID);
/*    */   }
/*    */ 
/*    */   public FeatureSelector getFeatureSelector(ConfigureNode node, int selectorID) {
/* 32 */     return loadFeatureSelector(node, selectorID);
/*    */   }
/*    */ 
/*    */   private FeatureSelector loadFeatureSelector(ConfigureNode node, int selectorID)
/*    */   {
/* 39 */     ConfigureNode selectorNode = getConfigureNode(node, "featureselector", selectorID);
/* 40 */     if (selectorNode == null)
/* 41 */       return null;
/* 42 */     String selectorName = selectorNode.getNodeName();
/* 43 */     return loadFeatureSelector(selectorName, selectorNode);
/*    */   }
/*    */ 
/*    */   protected FeatureSelector loadFeatureSelector(String selectorName, ConfigureNode selectorNode) {
/* 47 */     if (selectorName.equalsIgnoreCase("NullFeatureSelector"))
/* 48 */       return new NullFeatureSelector();
/* 49 */     if (selectorName.equalsIgnoreCase("ChiFeatureSelector"))
/* 50 */       return loadChiFeatureSelector(selectorNode);
/* 51 */     if (selectorName.equalsIgnoreCase("DocFrequencySelector"))
/* 52 */       return loadDocFrequencySelector(selectorNode);
/* 53 */     if (selectorName.equalsIgnoreCase("MutualInfoSelector"))
/* 54 */       return loadMutualInfoFeatureSelector(selectorNode);
/* 55 */     if (selectorName.equalsIgnoreCase("InfoGainFeatureSelector")) {
/* 56 */       return loadInfoGainFeatureSelector(selectorNode);
/*    */     }
/* 58 */     return (FeatureSelector)loadResource(selectorNode);
/*    */   }
/*    */ 
/*    */   private FeatureSelector loadChiFeatureSelector(ConfigureNode node) {
/* 62 */     return new ChiFeatureSelector(node.getDouble("toppercentage", 0.1D), node.getBoolean("avgmode", true));
/*    */   }
/*    */ 
/*    */   private FeatureSelector loadMutualInfoFeatureSelector(ConfigureNode node) {
/* 66 */     return new MutualInfoFeatureSelector(node.getDouble("toppercentage", 0.1D), node.getBoolean("avgmode", true));
/*    */   }
/*    */ 
/*    */   private FeatureSelector loadInfoGainFeatureSelector(ConfigureNode node) {
/* 70 */     return new InfoGainFeatureSelector(node.getDouble("toppercentage", 0.1D));
/*    */   }
/*    */ 
/*    */   private FeatureSelector loadDocFrequencySelector(ConfigureNode node) {
/* 74 */     return new DocFrequencySelector(node.getInt("mindocfrequency", 5));
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.FeatureSelectorConfig
 * JD-Core Version:    0.6.2
 */