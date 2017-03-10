/*    */ package edu.drexel.cis.dragon.util;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*    */ 
/*    */ public class PairIndex
/*    */ {
/*    */   private int[] arrRowStart;
/*    */   private int[] arrIndex;
/*    */   private int rows;
/*    */   private int columns;
/*    */ 
/*    */   public PairIndex(SparseMatrix pairMatrix)
/*    */   {
/* 24 */     this.rows = pairMatrix.rows();
/* 25 */     this.columns = pairMatrix.columns();
/* 26 */     this.arrRowStart = new int[this.rows + 1];
/* 27 */     this.arrRowStart[0] = 0;
/* 28 */     this.arrIndex = new int[pairMatrix.getNonZeroNum()];
/*    */ 
/* 30 */     for (int i = 0; i < this.rows; i++) {
/* 31 */       int[] arrColumn = pairMatrix.getNonZeroColumnsInRow(i);
/* 32 */       this.arrRowStart[(i + 1)] = (this.arrRowStart[i] + arrColumn.length);
/* 33 */       for (int j = 0; j < arrColumn.length; j++)
/* 34 */         this.arrIndex[(this.arrRowStart[i] + j)] = arrColumn[j];
/*    */     }
/*    */   }
/*    */ 
/*    */   public int getColumn(int pairIndex) {
/* 39 */     return this.arrIndex[pairIndex];
/*    */   }
/*    */ 
/*    */   public int getRow(int pairIndex)
/*    */   {
/* 45 */     int low = 0;
/* 46 */     int high = this.rows - 1;
/* 47 */     while (low <= high) {
/* 48 */       int middle = (low + high) / 2;
/* 49 */       if (this.arrRowStart[middle] > pairIndex) {
/* 50 */         high = middle - 1; } else {
/* 51 */         if (pairIndex < this.arrRowStart[(middle + 1)]) {
/* 52 */           return middle;
/*    */         }
/* 54 */         low = middle + 1;
/*    */       }
/*    */     }
/* 56 */     return -1;
/*    */   }
/*    */ 
/*    */   public int getPairIndex(int row, int col)
/*    */   {
/* 62 */     if (row >= this.rows)
/* 63 */       return -1;
/* 64 */     int low = this.arrRowStart[row];
/* 65 */     int high = this.arrRowStart[(row + 1)] - 1;
/* 66 */     while (low <= high) {
/* 67 */       int middle = (low + high) / 2;
/* 68 */       if (this.arrIndex[middle] == col)
/* 69 */         return middle;
/* 70 */       if (this.arrIndex[middle] > col)
/* 71 */         high = middle - 1;
/*    */       else
/* 73 */         low = middle + 1;
/*    */     }
/* 75 */     return -1;
/*    */   }
/*    */ 
/*    */   public int getRowLength(int row) {
/* 79 */     return this.arrRowStart[(row + 1)] - this.arrRowStart[row];
/*    */   }
/*    */ 
/*    */   public int getRowStart(int row) {
/* 83 */     return this.arrRowStart[row];
/*    */   }
/*    */ 
/*    */   public int rows() {
/* 87 */     return this.rows;
/*    */   }
/*    */ 
/*    */   public int columns() {
/* 91 */     return this.columns;
/*    */   }
/*    */ 
/*    */   public int pairs() {
/* 95 */     return this.arrRowStart[this.rows];
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.PairIndex
 * JD-Core Version:    0.6.2
 */