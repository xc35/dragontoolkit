/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class DoubleRow extends AbstractRow
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private int[] columns;
/*     */   private double[] scores;
/*     */ 
/*     */   public DoubleRow()
/*     */   {
/*  19 */     this.length = 0;
/*  20 */     this.row = -1;
/*  21 */     this.columns = null;
/*  22 */     this.scores = null;
/*  23 */     this.loadFactor = 0.0F;
/*     */   }
/*     */ 
/*     */   public DoubleRow(int row, int num, int[] columns, double[] scores) {
/*  27 */     this.length = num;
/*  28 */     this.row = row;
/*  29 */     this.columns = columns;
/*  30 */     this.scores = scores;
/*  31 */     this.loadFactor = 0.0F;
/*     */   }
/*     */ 
/*     */   public void load(int row, int num, byte[] data)
/*     */   {
/*     */     try
/*     */     {
/*  39 */       this.row = row;
/*  40 */       this.length = num;
/*  41 */       this.columns = new int[num];
/*  42 */       this.scores = new double[num];
/*  43 */       DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
/*  44 */       for (int i = 0; i < num; i++) {
/*  45 */         this.columns[i] = dis.readInt();
/*  46 */         this.scores[i] = dis.readDouble();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  50 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int[] getNonZeroColumns() {
/*  55 */     return this.columns;
/*     */   }
/*     */ 
/*     */   public int getNonZeroColumn(int index) {
/*  59 */     return this.columns[index];
/*     */   }
/*     */ 
/*     */   public double[] getNonZeroDoubleScores() {
/*  63 */     return this.scores;
/*     */   }
/*     */ 
/*     */   public double getNonZeroDoubleScore(int index) {
/*  67 */     return this.scores[index];
/*     */   }
/*     */ 
/*     */   public int getNonZeroIntScore(int index) {
/*  71 */     return (int)this.scores[index];
/*     */   }
/*     */ 
/*     */   public void setNonZeroDoubleScore(int index, double score) {
/*  75 */     this.scores[index] = score;
/*     */   }
/*     */ 
/*     */   public double getDouble(int column)
/*     */   {
/*     */     int index;
/*  81 */     if ((index = getColumnIndex(column)) >= 0) {
/*  82 */       return this.scores[index];
/*     */     }
/*  84 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public Cell getNonZeroCell(int index) {
/*  88 */     return new DoubleCell(this.row, this.columns[index], this.scores[index]);
/*     */   }
/*     */ 
/*     */   public Cell getCell(int column)
/*     */   {
/*     */     int index;
/*  94 */     if ((index = getColumnIndex(column)) >= 0) {
/*  95 */       return getNonZeroCell(index);
/*     */     }
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */   private int getColumnIndex(int column)
/*     */   {
/* 104 */     int low = 0;
/* 105 */     int high = this.length - 1;
/* 106 */     while (low <= high)
/*     */     {
/* 108 */       int middle = (low + high) / 2;
/* 109 */       int curColumn = this.columns[middle];
/* 110 */       if (curColumn == column)
/* 111 */         return middle;
/* 112 */       if (curColumn < column)
/* 113 */         low = middle + 1;
/*     */       else
/* 115 */         high = middle - 1;
/*     */     }
/* 117 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.DoubleRow
 * JD-Core Version:    0.6.2
 */