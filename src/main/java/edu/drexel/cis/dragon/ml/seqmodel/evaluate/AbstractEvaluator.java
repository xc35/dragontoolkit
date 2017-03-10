/*    */ package edu.drexel.cis.dragon.ml.seqmodel.evaluate;
/*    */ 
/*    */ public abstract class AbstractEvaluator
/*    */   implements Evaluator
/*    */ {
/*    */   protected int totalLabels;
/*    */   protected int annotatedLabels;
/*    */   protected int correctAnnotatedLabels;
/*    */ 
/*    */   public int totalLabels()
/*    */   {
/* 21 */     return this.totalLabels;
/*    */   }
/*    */ 
/*    */   public int annotatedLabels() {
/* 25 */     return this.annotatedLabels;
/*    */   }
/*    */ 
/*    */   public int correctAnnotatedLabels() {
/* 29 */     return this.correctAnnotatedLabels;
/*    */   }
/*    */ 
/*    */   public double precision() {
/* 33 */     return this.correctAnnotatedLabels / this.annotatedLabels;
/*    */   }
/*    */   public double recall() {
/* 36 */     return this.correctAnnotatedLabels / this.totalLabels;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.evaluate.AbstractEvaluator
 * JD-Core Version:    0.6.2
 */