/*    */ package edu.drexel.cis.dragon.matrix;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.ByteArrayConvert;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class DoubleCell extends AbstractCell
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected int row;
/*    */   protected int col;
/*    */   protected double score;
/*    */ 
/*    */   public DoubleCell(int row, int column)
/*    */   {
/* 19 */     this.row = row;
/* 20 */     this.col = column;
/* 21 */     this.score = 0.0D;
/*    */   }
/*    */ 
/*    */   public DoubleCell(int row, int column, double score) {
/* 25 */     this.row = row;
/* 26 */     this.col = column;
/* 27 */     this.score = score;
/*    */   }
/*    */ 
/*    */   public static int getCellDataLength() {
/* 31 */     return 8;
/*    */   }
/*    */ 
/*    */   public void merge(Cell cell) {
/* 35 */     if (cell.getResetOption())
/* 36 */       this.score = cell.getDoubleScore();
/*    */     else
/* 38 */       this.score += cell.getDoubleScore();
/*    */   }
/*    */ 
/*    */   public byte[] toByteArray() {
/* 42 */     return ByteArrayConvert.toByte(this.score);
/*    */   }
/*    */ 
/*    */   public void fromByteArray(byte[] data) {
/* 46 */     this.score = ByteArrayConvert.toDouble(data);
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 50 */     return String.valueOf(this.score);
/*    */   }
/*    */ 
/*    */   public void fromString(String data) {
/* 54 */     this.score = Double.parseDouble(data);
/*    */   }
/*    */ 
/*    */   public Cell transpose() {
/* 58 */     return new DoubleCell(this.col, this.row, this.score);
/*    */   }
/*    */ 
/*    */   public int getRow() {
/* 62 */     return this.row;
/*    */   }
/*    */ 
/*    */   public int getColumn() {
/* 66 */     return this.col;
/*    */   }
/*    */ 
/*    */   public double getDoubleScore() {
/* 70 */     return this.score;
/*    */   }
/*    */ 
/*    */   public int getIntScore() {
/* 74 */     return (int)this.score;
/*    */   }
/*    */ 
/*    */   public long getLongScore() {
/* 78 */      return (long)score;
/*    */   }
/*    */ 
/*    */   public byte getByteScore() {
/* 82 */     return (byte)(int)this.score;
/*    */   }
/*    */ 
/*    */   public void setDoubleScore(double score) {
/* 86 */     this.score = score;
/*    */   }
/*    */ 
/*    */   public void setIntScore(int score) {
/* 90 */     this.score = score;
/*    */   }
/*    */ 
/*    */   public void setLongScore(long score) {
/* 94 */     this.score = score;
/*    */   }
/*    */ 
/*    */   public void setByteScore(byte score) {
/* 98 */     this.score = score;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.DoubleCell
 * JD-Core Version:    0.6.2
 */