/*     */ package edu.drexel.cis.dragon.matrix.vector;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.DenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class PowerMethod
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private double tolerance;
/*     */   private double dampingFactor;
/*     */   private boolean showMessage;
/*     */   private int maxIteration;
/*     */ 
/*     */   public PowerMethod(double tolerance, double dampingFactor)
/*     */   {
/*  25 */     this.tolerance = tolerance;
/*  26 */     this.dampingFactor = dampingFactor;
/*  27 */     this.showMessage = true;
/*  28 */     this.maxIteration = 50;
/*     */   }
/*     */ 
/*     */   public void setMaxIteration(int iteration) {
/*  32 */     this.maxIteration = iteration;
/*     */   }
/*     */ 
/*     */   public int getMaxIteration() {
/*  36 */     return this.maxIteration;
/*     */   }
/*     */ 
/*     */   public DoubleVector getEigenVector(SparseMatrix matrix)
/*     */   {
/*  50 */     DoubleVector oldVector = new DoubleVector(Math.max(matrix.rows(), matrix.columns()));
/*  51 */     DoubleVector newVector = new DoubleVector(oldVector.size());
/*  52 */     oldVector.assign(1.0D / oldVector.size());
/*     */ 
/*  55 */     double[] arrRate = new double[matrix.rows()];
/*  56 */     for (int i = 0; i < matrix.rows(); i++) {
/*  57 */       arrRate[i] = 0.0D;
/*  58 */       int len = matrix.getNonZeroNumInRow(i);
/*     */ 
/*  60 */       for (int j = 0; j < len; j++)
/*  61 */         arrRate[i] += matrix.getNonZeroDoubleScoreInRow(i, j);
/*  62 */       arrRate[i] = ((1.0D - this.dampingFactor) / arrRate[i]);
/*     */     }
/*     */ 
/*  66 */     double constFactor = this.dampingFactor / matrix.rows();
/*     */ 
/*  68 */     int k = 0;
/*  69 */     while (k < this.maxIteration) {
/*  70 */       if (this.showMessage)
/*  71 */         System.out.println(new Date() + " iteration " + k);
/*  72 */       k++;
/*  73 */       newVector.assign(0.0D);
/*  74 */       for (int i = 0; i < matrix.rows(); i++) {
/*  75 */         int len = matrix.getNonZeroNumInRow(i);
/*  76 */         for (int j = 0; j < len; j++) {
/*  77 */           double d = arrRate[i] * matrix.getNonZeroDoubleScoreInRow(i, j) + constFactor;
/*  78 */           int index = matrix.getNonZeroColumnInRow(i, j);
/*  79 */           newVector.set(index, newVector.get(index) + d * oldVector.get(i));
/*     */         }
/*     */       }
/*  82 */       double distance = newVector.distance(oldVector);
/*  83 */       System.out.println(distance);
/*  84 */       if (distance < this.tolerance)
/*     */         break;
/*  86 */       oldVector.assign(newVector);
/*     */     }
/*  88 */     return newVector;
/*     */   }
/*     */ 
/*     */   public DoubleVector getEigenVector(DenseMatrix matrix)
/*     */   {
/*  97 */     if (matrix.rows() != matrix.columns()) {
/*  98 */       return null;
/*     */     }
/* 100 */     DoubleVector oldVector = new DoubleVector(matrix.rows());
/* 101 */     DoubleVector newVector = new DoubleVector(matrix.rows());
/* 102 */     oldVector.assign(1.0D / matrix.rows());
/*     */ 
/* 105 */     double[] arrRate = new double[matrix.rows()];
/* 106 */     for (int i = 0; i < matrix.rows(); i++) {
/* 107 */       arrRate[i] = 0.0D;
/*     */ 
/* 109 */       for (int j = 0; j < matrix.rows(); j++)
/* 110 */         arrRate[i] += matrix.getDouble(i, j);
/* 111 */       arrRate[i] = ((1.0D - this.dampingFactor) / arrRate[i]);
/*     */     }
/*     */ 
/* 115 */     double constFactor = this.dampingFactor / matrix.rows();
/*     */ 
/* 117 */     int k = 0;
/* 118 */     while (k < this.maxIteration) {
/* 119 */       if (this.showMessage)
/* 120 */         System.out.println(new Date() + " iteration " + k);
/* 121 */       k++;
/* 122 */       newVector.assign(0.0D);
/* 123 */       for (int i = 0; i < matrix.rows(); i++) {
/* 124 */         for (int j = 0; j < matrix.rows(); j++) {
/* 125 */           double d = arrRate[i] * matrix.getDouble(i, j) + constFactor;
/* 126 */           newVector.set(j, newVector.get(j) + d * oldVector.get(i));
/*     */         }
/*     */       }
/* 129 */       if (newVector.distance(oldVector) < this.tolerance)
/*     */         break;
/* 131 */       oldVector.assign(newVector);
/*     */     }
/* 133 */     return newVector;
/*     */   }
/*     */ 
/*     */   public void setMessageOption(boolean option) {
/* 137 */     this.showMessage = option;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.vector.PowerMethod
 * JD-Core Version:    0.6.2
 */