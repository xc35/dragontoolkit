/*     */ package edu.drexel.cis.dragon.matrix.vector;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class IntVector
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private int[] vector;
/*     */ 
/*     */   public IntVector(int size)
/*     */   {
/*  18 */     this.vector = new int[size];
/*     */   }
/*     */ 
/*     */   public IntVector(int[] vector) {
/*  22 */     this.vector = vector;
/*     */   }
/*     */ 
/*     */   public IntVector copy()
/*     */   {
/*  28 */     int[] newVector = new int[this.vector.length];
/*  29 */     System.arraycopy(this.vector, 0, newVector, 0, this.vector.length);
/*  30 */     return new IntVector(newVector);
/*     */   }
/*     */ 
/*     */   public void assign(int initValue) {
/*  34 */     for (int i = 0; i < this.vector.length; i++)
/*  35 */       this.vector[i] = initValue;
/*     */   }
/*     */ 
/*     */   public void assign(IntVector newVector) {
/*  39 */     for (int i = 0; i < this.vector.length; i++)
/*  40 */       this.vector[i] = newVector.get(i);
/*     */   }
/*     */ 
/*     */   public void multiply(int rate) {
/*  44 */     for (int i = 0; i < this.vector.length; i++)
/*  45 */       this.vector[i] *= rate;
/*     */   }
/*     */ 
/*     */   public void add(IntVector newVector) {
/*  49 */     if (this.vector.length != newVector.size())
/*  50 */       return;
/*  51 */     for (int i = 0; i < this.vector.length; i++)
/*  52 */       this.vector[i] += newVector.get(i);
/*     */   }
/*     */ 
/*     */   public void add(int index, int inc) {
/*  56 */     this.vector[index] += inc;
/*     */   }
/*     */ 
/*     */   public int get(int index) {
/*  60 */     return this.vector[index];
/*     */   }
/*     */ 
/*     */   public void set(int index, int value) {
/*  64 */     this.vector[index] = value;
/*     */   }
/*     */ 
/*     */   public int size() {
/*  68 */     return this.vector.length;
/*     */   }
/*     */ 
/*     */   public double distance()
/*     */   {
/*  75 */     int sum = 0;
/*  76 */     for (int i = 0; i < this.vector.length; i++) {
/*  77 */       sum += this.vector[i] * this.vector[i];
/*     */     }
/*  79 */     return Math.sqrt(sum);
/*     */   }
/*     */ 
/*     */   public double distance(IntVector origin)
/*     */   {
/*  86 */     if (this.vector.length != origin.size()) {
/*  87 */       return -1.0D;
/*     */     }
/*  89 */     int sum = 0;
/*  90 */     for (int i = 0; i < this.vector.length; i++) {
/*  91 */       int a = this.vector[i] - origin.get(i);
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
/*     */   public int getMaxValue() {
/* 106 */     return MathUtil.max(this.vector);
/*     */   }
/*     */ 
/*     */   public int getMinValue() {
/* 110 */     return MathUtil.min(this.vector);
/*     */   }
/*     */ 
/*     */   public double getAvgValue() {
/* 114 */     return MathUtil.average(this.vector);
/*     */   }
/*     */ 
/*     */   public int getSummation() {
/* 118 */     return (int)MathUtil.sumArray(this.vector);
/*     */   }
/*     */ 
/*     */   public int dotProduct(IntVector newVector)
/*     */   {
/* 125 */     int product = 0;
/* 126 */     for (int i = 0; i < this.vector.length; i++)
/* 127 */       product += this.vector[i] * newVector.get(i);
/* 128 */     return product;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.vector.IntVector
 * JD-Core Version:    0.6.2
 */