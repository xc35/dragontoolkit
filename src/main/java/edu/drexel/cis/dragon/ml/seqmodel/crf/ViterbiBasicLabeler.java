/*    */ package edu.drexel.cis.dragon.ml.seqmodel.crf;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.DoubleDenseMatrix;
/*    */ import edu.drexel.cis.dragon.matrix.DoubleFlatDenseMatrix;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.feature.FeatureGenerator;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.model.ModelGraph;
/*    */ import edu.drexel.cis.dragon.util.MathUtil;
/*    */ 
/*    */ public class ViterbiBasicLabeler extends AbstractCRF
/*    */   implements Labeler
/*    */ {
/*    */   private int[][] winningLabel;
/*    */   private double[] score;
/*    */   private int[] solutionOrder;
/*    */ 
/*    */   public ViterbiBasicLabeler(ModelGraph model, FeatureGenerator featureGenerator)
/*    */   {
/* 25 */     super(model, featureGenerator);
/* 26 */     this.score = new double[model.getStateNum()];
/*    */   }
/*    */ 
/*    */   public boolean label(DataSequence dataSeq) {
/* 30 */     return label(dataSeq, this.lambda);
/*    */   }
/*    */ 
/*    */   public boolean label(DataSequence dataSeq, double[] lambda)
/*    */   {
/* 39 */     int stateNum = this.model.getStateNum();
/* 40 */     int markovOrder = this.model.getMarkovOrder();
/* 41 */     DoubleDenseMatrix transMatrix = new DoubleFlatDenseMatrix(stateNum, stateNum);
/* 42 */     DoubleDenseMatrix scoreMatrix = new DoubleFlatDenseMatrix(stateNum, stateNum);
/* 43 */     double[] arrPrevScore = new double[stateNum];
/* 44 */     this.winningLabel = new int[dataSeq.length()][stateNum];
/*    */ 
/* 46 */     for (int i = markovOrder - 1; i < dataSeq.length(); i++) {
/* 47 */       computeTransMatrix(lambda, dataSeq, i, i, transMatrix, false);
/* 48 */       scoreMatrix.assign(0.0D);
/* 49 */       for (int yi = this.edgeGen.firstLabel(i); yi < stateNum; yi = this.edgeGen.nextLabel(yi, i)) {
/* 50 */         if (i > markovOrder - 1) {
/* 51 */           for (int yp = this.edgeGen.first(yi); yp < stateNum; yp = this.edgeGen.next(yi, yp))
/* 52 */             scoreMatrix.setDouble(yp, yi, arrPrevScore[yp] + transMatrix.getDouble(yp, yi));
/*    */         }
/*    */         else {
/* 55 */           arrPrevScore[yi] = transMatrix.getDouble(0, yi);
/*    */         }
/*    */       }
/*    */ 
/* 59 */       if (i > markovOrder - 1) {
/* 60 */         for (int yi = 0; yi < stateNum; yi++) {
/* 61 */           int prevLabel = 0;
/* 62 */           double maxScore = scoreMatrix.getDouble(prevLabel, yi);
/* 63 */           for (int yp = 1; yp < stateNum; yp++) {
/* 64 */             if (scoreMatrix.getDouble(yp, yi) > maxScore) {
/* 65 */               maxScore = scoreMatrix.getDouble(yp, yi);
/* 66 */               prevLabel = yp;
/*    */             }
/*    */           }
/* 69 */           arrPrevScore[yi] = maxScore;
/* 70 */           this.winningLabel[i][yi] = prevLabel;
/*    */         }
/*    */       }
/*    */     }
/* 74 */     MathUtil.copyArray(arrPrevScore, this.score);
/* 75 */     this.solutionOrder = MathUtil.rankElementInArray(this.score, true);
/* 76 */     getBestSolution(dataSeq, 0);
/* 77 */     return true;
/*    */   }
/*    */ 
/*    */   public double getBestSolution(DataSequence dataSeq, int order)
/*    */   {
/* 83 */     int markovOrder = this.model.getMarkovOrder();
/* 84 */     int prevLabel = this.solutionOrder[order];
/* 85 */     for (int i = dataSeq.length() - 1; i >= markovOrder - 1; i--) {
/* 86 */       dataSeq.setLabel(i, prevLabel);
/* 87 */       prevLabel = this.winningLabel[i][prevLabel];
/*    */     }
/* 89 */     this.model.mapStateToLabel(dataSeq);
/* 90 */     return this.score[this.solutionOrder[order]];
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.crf.ViterbiBasicLabeler
 * JD-Core Version:    0.6.2
 */