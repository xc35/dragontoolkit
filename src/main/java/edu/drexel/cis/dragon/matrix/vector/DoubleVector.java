/*     */ package edu.drexel.cis.dragon.matrix.vector;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class DoubleVector
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private double[] vector;
/*     */ 
/*     */   public DoubleVector(int size)
/*     */   {
/*  18 */     this.vector = new double[size];
/*     */   }
/*     */ 
/*     */   public DoubleVector(double[] vector) {
/*  22 */     this.vector = vector;
/*     */   }
/*     */ 
/*     */   public DoubleVector copy()
/*     */   {
/*  28 */     double[] newVector = new double[this.vector.length];
/*  29 */     System.arraycopy(this.vector, 0, newVector, 0, this.vector.length);
/*  30 */     return new DoubleVector(newVector);
/*     */   }
/*     */ 
/*     */   public void assign(double initValue) {
/*  34 */     for (int i = 0; i < this.vector.length; i++)
/*  35 */       this.vector[i] = initValue;
/*     */   }
/*     */ 
/*     */   public void assign(DoubleVector newVector) {
/*  39 */     for (int i = 0; i < this.vector.length; i++)
/*  40 */       this.vector[i] = newVector.get(i);
/*     */   }
/*     */ 
/*     */   public void multiply(double rate) {
/*  44 */     for (int i = 0; i < this.vector.length; i++)
/*  45 */       this.vector[i] *= rate;
/*     */   }
/*     */ 
/*     */   public void add(DoubleVector newVector) {
/*  49 */     if (this.vector.length != newVector.size())
/*  50 */       return;
/*  51 */     for (int i = 0; i < this.vector.length; i++)
/*  52 */       this.vector[i] += newVector.get(i);
/*     */   }
/*     */ 
/*     */   public void add(int index, double inc) {
/*  56 */     this.vector[index] += inc;
/*     */   }
/*     */ 
/*     */   public double get(int index) {
/*  60 */     return this.vector[index];
/*     */   }
/*     */ 
/*     */   public void set(int index, double value) {
/*  64 */     this.vector[index] = value;
/*     */   }
/*     */ 
/*     */   public int size() {
/*  68 */     return this.vector.length;
/*     */   }
/*     */ 
/*     */   public double distance()
/*     */   {
/*  75 */     double sum = 0.0D;
/*  76 */     for (int i = 0; i < this.vector.length; i++) {
/*  77 */       sum += this.vector[i] * this.vector[i];
/*     */     }
/*  79 */     return Math.sqrt(sum);
/*     */   }
/*     */ 
/*     */   public double distance(DoubleVector origin)
/*     */   {
/*  86 */     if (this.vector.length != origin.size()) {
/*  87 */       return -1.0D;
/*     */     }
/*  89 */     double sum = 0.0D;
/*  90 */     for (int i = 0; i < this.vector.length; i++) {
/*  91 */       double a = this.vector[i] - origin.get(i);
/*  92 */       sum += a * a;
/*     */     }
/*  94 */     return Math.sqrt(sum);
/*     */   }
/*     */ 
/*     */   public int[] rank(boolean desc) {
/*  98 */     return MathUtil.rankElementInArray(this.vector, desc);
/*     */   }
/*     */ 
/*     */   public int getDimWithMaxValue() {
/* 102 */     return MathUtil.maxElementInArray(this.vector);
/*     */   }
/*     */ 
/*     */   public double getMaxValue() {
/* 106 */     return MathUtil.max(this.vector);
/*     */   }
/*     */ 
/*     */   public double getMinValue() {
/* 110 */     return MathUtil.min(this.vector);
/*     */   }
/*     */ 
/*     */   public double getAvgValue() {
/* 114 */     return MathUtil.average(this.vector);
/*     */   }
/*     */ 
/*     */   public double getSummation() {
/* 118 */     return MathUtil.sumArray(this.vector);
/*     */   }
/*     */ 
/*     */   public double dotProduct(DoubleVector newVector)
/*     */   {
/* 125 */     double product = 0.0D;
/* 126 */     for (int i = 0; i < this.vector.length; i++)
/* 127 */       product += this.vector[i] * newVector.get(i);
/* 128 */     return product;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.vector.DoubleVector
 * JD-Core Version:    0.6.2
 */