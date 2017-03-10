/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.classification.multiclass.LossMultiClassDecoder;
/*    */ import edu.drexel.cis.dragon.ir.classification.multiclass.MultiClassDecoder;
/*    */ 
/*    */ public class MultiClassDecoderConfig extends ConfigUtil
/*    */ {
/*    */   public MultiClassDecoderConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public MultiClassDecoderConfig(ConfigureNode root)
/*    */   {
/* 19 */     super(root);
/*    */   }
/*    */ 
/*    */   public MultiClassDecoderConfig(String configFile) {
/* 23 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public MultiClassDecoder getMultiClassDecoder(int decoderID) {
/* 27 */     return getMultiClassDecoder(this.root, decoderID);
/*    */   }
/*    */ 
/*    */   public MultiClassDecoder getMultiClassDecoder(ConfigureNode node, int decoderID) {
/* 31 */     return loadMultiClassDecoder(node, decoderID);
/*    */   }
/*    */ 
/*    */   private MultiClassDecoder loadMultiClassDecoder(ConfigureNode node, int decoderID)
/*    */   {
/* 38 */     ConfigureNode decoderNode = getConfigureNode(node, "MultiClassDecoder", decoderID);
/* 39 */     if (decoderNode == null)
/* 40 */       return null;
/* 41 */     String decoderName = decoderNode.getNodeName();
/* 42 */     return loadMultiClassDecoder(decoderName, decoderNode);
/*    */   }
/*    */ 
/*    */   protected MultiClassDecoder loadMultiClassDecoder(String decoderName, ConfigureNode decoderNode) {
/* 46 */     if (decoderName.equalsIgnoreCase("LossMultiClassDecoder")) {
/* 47 */       return loadLossMultiClassDecoder(decoderNode);
/*    */     }
/* 49 */     return (MultiClassDecoder)loadResource(decoderNode);
/*    */   }
/*    */ 
/*    */   private MultiClassDecoder loadLossMultiClassDecoder(ConfigureNode node)
/*    */   {
/* 55 */     int lossFuncID = node.getInt("lossfunction");
/* 56 */     if (lossFuncID < 1)
/* 57 */       return null;
/* 58 */     return new LossMultiClassDecoder(new LossFunctionConfig().getLossFunction(node, lossFuncID));
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.MultiClassDecoderConfig
 * JD-Core Version:    0.6.2
 */