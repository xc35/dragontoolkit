/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class IntRow extends AbstractRow
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected int[] columns;
/*     */   protected int[] scores;
/*     */ 
/*     */   public IntRow()
/*     */   {
/*  20 */     this.length = 0;
/*  21 */     this.row = -1;
/*  22 */     this.columns = null;
/*  23 */     this.scores = null;
/*  24 */     this.loadFactor = 0.0F;
/*     */   }
/*     */ 
/*     */   public IntRow(int row, int num, int[] columns, int[] scores) {
/*  28 */     this.length = num;
/*  29 */     this.row = row;
/*  30 */     this.columns = columns;
/*  31 */     this.scores = scores;
/*  32 */     this.loadFactor = 0.0F;
/*     */   }
/*     */ 
/*     */   public void load(int row, int num, byte[] data)
/*     */   {
/*     */     try
/*     */     {
/*  40 */       this.row = row;
/*  41 */       this.length = num;
/*  42 */       this.columns = new int[num];
/*  43 */       this.scores = new int[num];
/*  44 */       DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
/*  45 */       for (int i = 0; i < num; i++) {
/*  46 */         this.columns[i] = dis.readInt();
/*  47 */         this.scores[i] = dis.readInt();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  51 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int[] getNonZeroColumns() {
/*  56 */     return this.columns;
/*     */   }
/*     */ 
/*     */   public int getNonZeroColumn(int index) {
/*  60 */     return this.columns[index];
/*     */   }
/*     */ 
/*     */   public int[] getNonZeroIntScores() {
/*  64 */     return this.scores;
/*     */   }
/*     */ 
/*     */   public int getNonZeroIntScore(int index) {
/*  68 */     return this.scores[index];
/*     */   }
/*     */ 
/*     */   public void setNonZeroIntScore(int index, int score) {
/*  72 */     this.scores[index] = score;
/*     */   }
/*     */ 
/*     */   public double getNonZeroDoubleScore(int index) {
/*  76 */     return this.scores[index];
/*     */   }
/*     */ 
/*     */   public int getInt(int column)
/*     */   {
/*     */     int index;
/*  82 */     if ((index = getColumnIndex(column)) >= 0) {
/*  83 */       return this.scores[index];
/*     */     }
/*  85 */     return 0;
/*     */   }
/*     */ 
/*     */   public Cell getNonZeroCell(int index) {
/*  89 */     return new IntCell(this.row, this.columns[index], this.scores[index]);
/*     */   }
/*     */ 
/*     */   public Cell getCell(int column)
/*     */   {
/*     */     int index;
/*  95 */     if ((index = getColumnIndex(column)) >= 0) {
/*  96 */       return getNonZeroCell(index);
/*     */     }
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */   private int getColumnIndex(int column)
/*     */   {
/* 105 */     int low = 0;
/* 106 */     int high = this.length - 1;
/* 107 */     while (low <= high)
/*     */     {
/* 109 */       int middle = (low + high) / 2;
/* 110 */       int curColumn = this.columns[middle];
/* 111 */       if (curColumn == column)
/* 112 */         return middle;
/* 113 */       if (curColumn < column)
/* 114 */         low = middle + 1;
/*     */       else
/* 116 */         high = middle - 1;
/*     */     }
/* 118 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.IntRow
 * JD-Core Version:    0.6.2
 */