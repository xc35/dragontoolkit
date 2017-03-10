/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.ByteArrayConvert;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class IntCell extends AbstractCell
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected int row;
/*     */   protected int col;
/*     */   protected int score;
/*     */ 
/*     */   public IntCell(int row, int column, int score)
/*     */   {
/*  19 */     this.row = row;
/*  20 */     this.col = column;
/*  21 */     this.score = score;
/*     */   }
/*     */ 
/*     */   public IntCell(int row, int column) {
/*  25 */     this.row = row;
/*  26 */     this.col = column;
/*  27 */     this.score = 0;
/*     */   }
/*     */ 
/*     */   public static int getCellDataLength() {
/*  31 */     return 4;
/*     */   }
/*     */ 
/*     */   public void merge(Cell cell) {
/*  35 */     if (cell.getResetOption())
/*  36 */       this.score = cell.getIntScore();
/*     */     else
/*  38 */       this.score += cell.getIntScore();
/*     */   }
/*     */ 
/*     */   public byte[] toByteArray() {
/*  42 */     return ByteArrayConvert.toByte(this.score);
/*     */   }
/*     */ 
/*     */   public void fromByteArray(byte[] data) {
/*  46 */     this.score = ByteArrayConvert.toInt(data);
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  50 */     return String.valueOf(this.score);
/*     */   }
/*     */ 
/*     */   public void fromString(String data) {
/*  54 */     this.score = Integer.parseInt(data);
/*     */   }
/*     */ 
/*     */   public Cell transpose() {
/*  58 */     return new IntCell(this.col, this.row, this.score);
/*     */   }
/*     */ 
/*     */   public int getRow() {
/*  62 */     return this.row;
/*     */   }
/*     */ 
/*     */   public int getColumn() {
/*  66 */     return this.col;
/*     */   }
/*     */ 
/*     */   public double getDoubleScore() {
/*  70 */     return this.score;
/*     */   }
/*     */ 
/*     */   public int getIntScore() {
/*  74 */     return this.score;
/*     */   }
/*     */ 
/*     */   public long getLongScore() {
/*  78 */     return this.score;
/*     */   }
/*     */ 
/*     */   public byte getByteScore() {
/*  82 */     return (byte)this.score;
/*     */   }
/*     */ 
/*     */   public void setScore(double score) {
/*  86 */     this.score = ((int)score);
/*     */   }
/*     */ 
/*     */   public void setDoubleScore(double score) {
/*  90 */     this.score = ((int)score);
/*     */   }
/*     */ 
/*     */   public void setIntScore(int score) {
/*  94 */     this.score = score;
/*     */   }
/*     */ 
/*     */   public void setLongScore(long score) {
/*  98 */     this.score = ((int)score);
/*     */   }
/*     */ 
/*     */   public void setByteScore(byte score) {
/* 102 */     this.score = score;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.IntCell
 * JD-Core Version:    0.6.2
 */