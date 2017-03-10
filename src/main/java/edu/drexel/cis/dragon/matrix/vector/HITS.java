/*     */ package edu.drexel.cis.dragon.matrix.vector;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class HITS
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final int DEFAULTITERATION = 20;
/*     */   private int numOfIteration;
/*     */   private boolean edgeWeightIsCounted;
/*     */   private boolean authorityPriorIsSet;
/*     */   private boolean hubPriorIsSet;
/*     */   private boolean showMessage;
/*     */   private DoubleVector authorityVector;
/*     */   private DoubleVector hubVector;
/*     */ 
/*     */   public HITS(boolean edgeWeightIsCounted)
/*     */   {
/*  26 */     this(edgeWeightIsCounted, 20);
/*     */   }
/*     */ 
/*     */   public HITS(boolean edgeWeightIsCounted, int numOfIteration) {
/*  30 */     this.edgeWeightIsCounted = edgeWeightIsCounted;
/*  31 */     this.numOfIteration = numOfIteration;
/*  32 */     this.authorityPriorIsSet = false;
/*  33 */     this.hubPriorIsSet = false;
/*  34 */     this.showMessage = true;
/*     */   }
/*     */ 
/*     */   public void computeAuthorityHub(SparseMatrix matrix) {
/*  38 */     computeAuthorityHub(matrix, (SparseMatrix)matrix.transpose());
/*     */   }
/*     */ 
/*     */   public void computeAuthorityHub(SparseMatrix matrix, SparseMatrix tMatrix)
/*     */   {
/*  46 */     if (!this.authorityPriorIsSet) {
/*  47 */       this.authorityVector = new DoubleVector(matrix.columns());
/*  48 */       this.authorityVector.assign(1.0D);
/*     */     }
/*  50 */     if (!this.hubPriorIsSet) {
/*  51 */       this.hubVector = new DoubleVector(matrix.rows());
/*  52 */       this.hubVector.assign(1.0D);
/*     */     }
/*  54 */     double[] scoreList = (double[])null;
/*     */ 
/*  56 */     for (int k = 0; k < this.numOfIteration; k++) {
/*  57 */       if (this.showMessage)
/*  58 */         System.out.println(new Date() + " iteration " + k);
/*  59 */       k++;
/*     */ 
/*  62 */       for (int i = 0; i < tMatrix.rows(); i++) {
/*  63 */         int[] indexList = tMatrix.getNonZeroColumnsInRow(i);
/*  64 */         if (this.edgeWeightIsCounted)
/*  65 */           scoreList = tMatrix.getNonZeroDoubleScoresInRow(i);
/*  66 */         double sum = 0.0D;
/*  67 */         for (int j = 0; j < indexList.length; j++) {
/*  68 */           if (this.edgeWeightIsCounted)
/*  69 */             sum += scoreList[j] * this.hubVector.get(indexList[j]);
/*     */           else
/*  71 */             sum += this.hubVector.get(indexList[j]);
/*     */         }
/*  73 */         this.authorityVector.set(i, sum);
/*     */       }
/*  75 */       this.authorityVector.multiply(1.0D / this.authorityVector.distance());
/*     */ 
/*  78 */       for (int i = 0; i < matrix.rows(); i++) {
/*  79 */         int[] indexList = matrix.getNonZeroColumnsInRow(i);
/*  80 */         if (this.edgeWeightIsCounted)
/*  81 */           scoreList = matrix.getNonZeroDoubleScoresInRow(i);
/*  82 */         double sum = 0.0D;
/*  83 */         for (int j = 0; j < indexList.length; j++) {
/*  84 */           if (this.edgeWeightIsCounted)
/*  85 */             sum += scoreList[j] * this.authorityVector.get(indexList[j]);
/*     */           else
/*  87 */             sum += this.authorityVector.get(indexList[j]);
/*     */         }
/*  89 */         this.hubVector.set(i, sum);
/*     */       }
/*  91 */       this.hubVector.multiply(1.0D / this.hubVector.distance());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAuthorityVectorPrior(DoubleVector authorityVector) {
/*  96 */     this.authorityVector = authorityVector;
/*  97 */     this.authorityPriorIsSet = true;
/*     */   }
/*     */ 
/*     */   public void setHubVectorPrior(DoubleVector hubVector) {
/* 101 */     this.hubVector = hubVector;
/* 102 */     this.hubPriorIsSet = true;
/*     */   }
/*     */ 
/*     */   public DoubleVector getAuthorityVector() {
/* 106 */     return this.authorityVector;
/*     */   }
/*     */ 
/*     */   public DoubleVector getHubVector() {
/* 110 */     return this.hubVector;
/*     */   }
/*     */ 
/*     */   public void setMessageOption(boolean option) {
/* 114 */     this.showMessage = option;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.vector.HITS
 * JD-Core Version:    0.6.2
 */