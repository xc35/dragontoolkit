/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.kngbase.TranslationModel;
/*    */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class TranslationAppConfig
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 24 */     if (args.length != 2) {
/* 25 */       System.out.println("Please input two parameters: configuration xml file and translation applicaiton id");
/* 26 */       return;
/*    */     }
/*    */ 
/* 29 */     ConfigureNode root = new BasicConfigureNode(args[0]);
/* 30 */     ConfigUtil util = new ConfigUtil();
/* 31 */     ConfigureNode transAppNode = util.getConfigureNode(root, "translationapp", Integer.parseInt(args[1]));
/* 32 */     if (transAppNode == null)
/* 33 */       return;
/* 34 */     TranslationAppConfig transApp = new TranslationAppConfig();
/* 35 */     transApp.translate(transAppNode);
/*    */   }
/*    */ 
/*    */   public void translate(ConfigureNode transNode)
/*    */   {
/* 43 */     String matrixPath = transNode.getString("matrixpath");
/* 44 */     String matrixKey = transNode.getString("matrixkey");
/* 45 */     int matrixID = transNode.getInt("cooccurrencematrix");
/*    */     IntSparseMatrix cooccurMatrix;
/* 46 */     if (matrixID > 0)
/* 47 */       cooccurMatrix = new SparseMatrixConfig().getIntSparseMatrix(transNode, matrixID);
/*    */     else
/* 49 */       cooccurMatrix = null;
/* 50 */     matrixID = transNode.getInt("srcmatrix");
/* 51 */     IntSparseMatrix srcMatrix = new SparseMatrixConfig().getIntSparseMatrix(transNode, matrixID);
/* 52 */     matrixID = transNode.getInt("destmatrix");
/* 53 */     IntSparseMatrix destMatrix = new SparseMatrixConfig().getIntSparseMatrix(transNode, matrixID);
/*    */ 
/* 55 */     translate(srcMatrix, destMatrix, cooccurMatrix, matrixPath, matrixKey);
/*    */   }
/*    */ 
/*    */   public void translate(IntSparseMatrix srcMatrix, IntSparseMatrix destMatrix, IntSparseMatrix cooccurMatrix, String matrixPath, String matrixKey)
/*    */   {
/* 62 */     TranslationModel model = new TranslationModel(srcMatrix, destMatrix, cooccurMatrix);
/* 63 */     model.translate(matrixPath, matrixKey);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.TranslationAppConfig
 * JD-Core Version:    0.6.2
 */