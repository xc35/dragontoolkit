/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.classification.multiclass.AllPairCodeMatrix;
/*    */ import edu.drexel.cis.dragon.ir.classification.multiclass.CodeMatrix;
/*    */ import edu.drexel.cis.dragon.ir.classification.multiclass.OVACodeMatrix;
/*    */ 
/*    */ public class CodeMatrixConfig extends ConfigUtil
/*    */ {
/*    */   public CodeMatrixConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public CodeMatrixConfig(ConfigureNode root)
/*    */   {
/* 19 */     super(root);
/*    */   }
/*    */ 
/*    */   public CodeMatrixConfig(String configFile) {
/* 23 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public CodeMatrix getCodeMatrix(int codeMatrixID) {
/* 27 */     return getCodeMatrix(this.root, codeMatrixID);
/*    */   }
/*    */ 
/*    */   public CodeMatrix getCodeMatrix(ConfigureNode node, int codeMatrixID) {
/* 31 */     return loadCodeMatrix(node, codeMatrixID);
/*    */   }
/*    */ 
/*    */   private CodeMatrix loadCodeMatrix(ConfigureNode node, int codeMatrixID)
/*    */   {
/* 38 */     ConfigureNode codeMatrixNode = getConfigureNode(node, "CodeMatrix", codeMatrixID);
/* 39 */     if (codeMatrixNode == null)
/* 40 */       return null;
/* 41 */     String codeMatrixName = codeMatrixNode.getNodeName();
/* 42 */     return loadCodeMatrix(codeMatrixName, codeMatrixNode);
/*    */   }
/*    */ 
/*    */   protected CodeMatrix loadCodeMatrix(String codeMatrixName, ConfigureNode codeMatrixNode) {
/* 46 */     if (codeMatrixName.equalsIgnoreCase("OVACodeMatrix"))
/* 47 */       return new OVACodeMatrix(codeMatrixNode.getInt("classnum", 1));
/* 48 */     if (codeMatrixName.equalsIgnoreCase("AllPairCodeMatrix")) {
/* 49 */       return new AllPairCodeMatrix(codeMatrixNode.getInt("classnum", 1));
/*    */     }
/* 51 */     return (CodeMatrix)loadResource(codeMatrixNode);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.CodeMatrixConfig
 * JD-Core Version:    0.6.2
 */