/*    */ package edu.drexel.cis.dragon.ir.classification.multiclass;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*    */ import java.io.PrintStream;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class LossMultiClassDecoder
/*    */   implements MultiClassDecoder, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private LossFunction lossFunc;
/*    */   private DoubleVector lossVector;
/*    */   private int[] rankings;
/*    */ 
/*    */   public LossMultiClassDecoder(LossFunction lossFunc)
/*    */   {
/* 24 */     this.lossFunc = lossFunc;
/*    */   }
/*    */ 
/*    */   public int decode(CodeMatrix matrix, double[] binClassifierResults)
/*    */   {
/* 31 */     if (binClassifierResults.length != matrix.getClassifierNum()) {
/* 32 */       System.out.println("The input data are not valid. Number of binary classifiers is not consistent.");
/* 33 */       return -1;
/*    */     }
/*    */ 
/* 36 */     this.lossVector = new DoubleVector(matrix.getClassNum());
/* 37 */     for (int i = 0; i < matrix.getClassNum(); i++) {
/* 38 */       for (int j = 0; j < matrix.getClassifierNum(); j++)
/* 39 */         this.lossVector.add(i, this.lossFunc.loss(matrix.getCode(i, j) * binClassifierResults[j]));
/*    */     }
/* 41 */     this.rankings = this.lossVector.rank(false);
/* 42 */     return this.rankings[0];
/*    */   }
/*    */ 
/*    */   public int[] rank() {
/* 46 */     return this.rankings;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.multiclass.LossMultiClassDecoder
 * JD-Core Version:    0.6.2
 */