/*    */ package edu.drexel.cis.dragon.ml.seqmodel.crf;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.DoubleDenseMatrix;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.feature.FeatureGenerator;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.model.ModelGraph;
/*    */ import edu.drexel.cis.dragon.util.MathUtil;
/*    */ 
/*    */ public abstract class AbstractTrainer extends AbstractCRF
/*    */   implements Trainer
/*    */ {
/* 18 */   protected static double xtol = 1.0E-016D;
/*    */   protected boolean doScaling;
/*    */   protected int maxIteration;
/*    */ 
/*    */   public AbstractTrainer(ModelGraph model, FeatureGenerator featureGen)
/*    */   {
/* 23 */     super(model, featureGen);
/* 24 */     this.doScaling = true;
/* 25 */     this.maxIteration = 100;
/*    */   }
/*    */ 
/*    */   public boolean needScaling() {
/* 29 */     return this.doScaling;
/*    */   }
/*    */ 
/*    */   public void setScalingOption(boolean option) {
/* 33 */     this.doScaling = option;
/*    */   }
/*    */ 
/*    */   public int getMaxIteration() {
/* 37 */     return this.maxIteration;
/*    */   }
/*    */ 
/*    */   public void setMaxIteration(int maxIteration) {
/* 41 */     this.maxIteration = maxIteration;
/*    */   }
/*    */ 
/*    */   protected void genStateVector(DoubleDenseMatrix transMatrix, double[] oldStateVector, double[] newStateVector, boolean transpose)
/*    */   {
/* 48 */     for (int j = 0; j < transMatrix.columns(); j++)
/* 49 */       for (int i = this.edgeGen.first(j); i < transMatrix.rows(); i = this.edgeGen.next(j, i)) {
/* 50 */         int r = i;
/* 51 */         int c = j;
/* 52 */         if (transpose) {
/* 53 */           r = j;
/* 54 */           c = i;
/*    */         }
/* 56 */         newStateVector[r] += transMatrix.getDouble(i, j) * oldStateVector[c];
/*    */       }
/*    */   }
/*    */ 
/*    */   protected void genStateVectorLog(DoubleDenseMatrix transMatrix, double[] oldStateVector, double[] newStateVector, boolean transpose)
/*    */   {
/* 65 */     for (int j = 0; j < transMatrix.columns(); j++)
/* 66 */       for (int i = this.edgeGen.first(j); i < transMatrix.rows(); i = this.edgeGen.next(j, i)) {
/* 67 */         int r = i;
/* 68 */         int c = j;
/* 69 */         if (transpose) {
/* 70 */           r = j;
/* 71 */           c = i;
/*    */         }
/* 73 */         newStateVector[r] = MathUtil.logSumExp(newStateVector[r], transMatrix.getDouble(i, j) + oldStateVector[c]);
/*    */       }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.crf.AbstractTrainer
 * JD-Core Version:    0.6.2
 */