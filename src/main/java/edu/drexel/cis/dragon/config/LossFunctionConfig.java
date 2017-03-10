/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.classification.multiclass.HingeLoss;
/*    */ import edu.drexel.cis.dragon.ir.classification.multiclass.LinearLoss;
/*    */ import edu.drexel.cis.dragon.ir.classification.multiclass.LossFunction;
/*    */ 
/*    */ public class LossFunctionConfig extends ConfigUtil
/*    */ {
/*    */   public LossFunctionConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public LossFunctionConfig(ConfigureNode root)
/*    */   {
/* 19 */     super(root);
/*    */   }
/*    */ 
/*    */   public LossFunctionConfig(String configFile) {
/* 23 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public LossFunction getLossFunction(int lossFuncID) {
/* 27 */     return getLossFunction(this.root, lossFuncID);
/*    */   }
/*    */ 
/*    */   public LossFunction getLossFunction(ConfigureNode node, int lossFuncID) {
/* 31 */     return loadLossFunction(node, lossFuncID);
/*    */   }
/*    */ 
/*    */   private LossFunction loadLossFunction(ConfigureNode node, int lossFuncID)
/*    */   {
/* 38 */     ConfigureNode lossFuncNode = getConfigureNode(node, "LossFunction", lossFuncID);
/* 39 */     if (lossFuncNode == null)
/* 40 */       return null;
/* 41 */     String lossFuncName = lossFuncNode.getNodeName();
/* 42 */     return loadLossFunction(lossFuncName, lossFuncNode);
/*    */   }
/*    */ 
/*    */   protected LossFunction loadLossFunction(String lossFuncName, ConfigureNode lossFuncNode) {
/* 46 */     if (lossFuncName.equalsIgnoreCase("HingeLoss"))
/* 47 */       return new HingeLoss();
/* 48 */     if (lossFuncName.equalsIgnoreCase("LinearLoss")) {
/* 49 */       return new LinearLoss();
/*    */     }
/* 51 */     return (LossFunction)loadResource(lossFuncNode);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.LossFunctionConfig
 * JD-Core Version:    0.6.2
 */