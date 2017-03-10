/*     */ package edu.drexel.cis.dragon.ml.seqmodel.crf;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.DoubleDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleFlatDenseMatrix;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.feature.FeatureGenerator;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.model.ModelGraph;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ 
/*     */ public class ViterbiSegmentLabeler extends AbstractCRF
/*     */   implements Labeler
/*     */ {
/*     */   private int maxLen;
/*     */   private int[][] winningLabel;
/*     */   private int[][] winningPos;
/*     */   private double[] score;
/*     */   private int[] solutionOrder;
/*     */ 
/*     */   public ViterbiSegmentLabeler(ModelGraph model, FeatureGenerator featureGenerator, int maxSegmentLength)
/*     */   {
/*  27 */     super(model, featureGenerator);
/*  28 */     this.maxLen = maxSegmentLength;
/*  29 */     this.score = new double[model.getStateNum()];
/*     */   }
/*     */ 
/*     */   public boolean label(DataSequence dataSeq) {
/*  33 */     return label(dataSeq, this.lambda);
/*     */   }
/*     */ 
/*     */   public boolean label(DataSequence dataSeq, double[] lambda)
/*     */   {
/*  42 */     int stateNum = this.model.getStateNum();
/*  43 */     DoubleDenseMatrix transMatrix = new DoubleFlatDenseMatrix(stateNum, stateNum);
/*  44 */     DoubleDenseMatrix[] scoreMatrix = new DoubleFlatDenseMatrix[this.maxLen];
/*  45 */     for (int i = 0; i < this.maxLen; i++)
/*  46 */       scoreMatrix[i] = new DoubleFlatDenseMatrix(stateNum, stateNum);
/*  47 */     double[][] partialScore = new double[dataSeq.length()][stateNum];
/*  48 */     this.winningLabel = new int[dataSeq.length()][stateNum];
/*  49 */     this.winningPos = new int[dataSeq.length()][stateNum];
/*     */      int i;
/*  51 */     for (i = 0; i < dataSeq.length(); i++) {
/*  52 */       if (i > 0) {
/*  53 */         int ell = 0;
/*     */         do { computeTransMatrix(lambda, dataSeq, i - ell, i, transMatrix, false);
/*  55 */           scoreMatrix[ell].assign(0.0D);
/*  56 */           for (int yi = 0; yi < stateNum; yi++)
/*  57 */             for (int yp = 0; yp < stateNum; yp++)
/*  58 */               scoreMatrix[ell].setDouble(yp, yi, partialScore[(i - ell - 1)][yp] + transMatrix.getDouble(yp, yi));
/*  53 */           ell++; if (ell >= this.maxLen) break;  } while (i - ell > 0);
/*     */       }
/*     */       else
/*     */       {
/*  63 */         for (int yi = 0; yi < stateNum; yi++) {
/*  64 */           partialScore[i][yi] = transMatrix.getDouble(0, yi);
/*  65 */           this.winningPos[i][yi] = 0;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  71 */       if (i > 0) {
/*  72 */         for (int yi = 0; yi < stateNum; yi++) {
/*  73 */           int prevLabel = -1;
/*  74 */           int startPos = -1;
/*  75 */           double maxScore = -1.0D;
/*  76 */           for (int ell = 0; (ell < this.maxLen) && (i - ell >= 0); ell++) {
/*  77 */             for (int yp = 0; yp < stateNum; yp++) {
/*  78 */               if (scoreMatrix[ell].getDouble(yp, yi) > maxScore) {
/*  79 */                 maxScore = scoreMatrix[ell].getDouble(yp, yi);
/*  80 */                 prevLabel = yp;
/*  81 */                 startPos = i - ell;
/*     */               }
/*     */             }
/*     */           }
/*  85 */           partialScore[i][yi] = maxScore;
/*  86 */           this.winningLabel[i][yi] = prevLabel;
/*  87 */           this.winningPos[i][yi] = startPos;
/*     */         }
/*     */       }
/*     */     }
/*  91 */     MathUtil.copyArray(partialScore[(dataSeq.length() - 1)], this.score);
/*  92 */     this.solutionOrder = MathUtil.rankElementInArray(this.score, true);
/*  93 */     getBestSolution(dataSeq, 0);
/*  94 */     return true;
/*     */   }
/*     */ 
/*     */   public double getBestSolution(DataSequence dataSeq, int order)
/*     */   {
/* 101 */     int endPos = dataSeq.length() - 1;
/* 102 */     int prevLabel = this.solutionOrder[order];
/* 103 */     int startPos = this.winningPos[endPos][prevLabel];
/* 104 */     while (startPos >= 0) {
/* 105 */       dataSeq.setSegment(startPos, endPos, prevLabel);
/* 106 */       prevLabel = this.winningLabel[endPos][prevLabel];
/* 107 */       endPos = startPos - 1;
/* 108 */       if (endPos < 0) break;
/* 109 */       startPos = this.winningPos[endPos][prevLabel];
/*     */     }
/*     */ 
/* 113 */     this.model.mapStateToLabel(dataSeq);
/* 114 */     return this.score[this.solutionOrder[order]];
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.crf.ViterbiSegmentLabeler
 * JD-Core Version:    0.6.2
 */