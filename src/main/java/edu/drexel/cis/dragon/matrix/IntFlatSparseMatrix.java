/*    */ package edu.drexel.cis.dragon.matrix;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class IntFlatSparseMatrix extends AbstractFlatSparseMatrix
/*    */   implements IntSparseMatrix, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public IntFlatSparseMatrix()
/*    */   {
/* 16 */     super(false, false, IntCell.getCellDataLength());
/*    */   }
/*    */ 
/*    */   public IntFlatSparseMatrix(boolean mergeMode, boolean miniMode) {
/* 20 */     super(mergeMode, miniMode, IntCell.getCellDataLength());
/*    */   }
/*    */ 
/*    */   public IntFlatSparseMatrix(String filename) {
/* 24 */     super(false, false, IntCell.getCellDataLength());
/* 25 */     readTextMatrixFile(filename);
/*    */   }
/*    */ 
/*    */   public IntFlatSparseMatrix(String filename, boolean binaryFile) {
/* 29 */     super(false, false, IntCell.getCellDataLength());
/* 30 */     if (binaryFile)
/* 31 */       readBinaryMatrixFile(filename);
/*    */     else
/* 33 */       readTextMatrixFile(filename);
/*    */   }
/*    */ 
/*    */   public SparseMatrix createSparseMatrix() {
/* 37 */     return new IntFlatSparseMatrix();
/*    */   }
/*    */ 
/*    */   public Cell createCell(int row, int column, byte[] data)
/*    */   {
/* 43 */     IntCell cur = new IntCell(row, column);
/* 44 */     cur.fromByteArray(data);
/* 45 */     return cur;
/*    */   }
/*    */ 
/*    */   public Cell createCell(int row, int column, String data)
/*    */   {
/* 51 */     IntCell cur = new IntCell(row, column);
/* 52 */     cur.fromString(data);
/* 53 */     return cur;
/*    */   }
/*    */ 
/*    */   public boolean add(int row, int column, int score) {
/* 57 */     return add(new IntCell(row, column, score));
/*    */   }
/*    */ 
/*    */   public long getRowSum(int row)
/*    */   {
/* 64 */     long sum = 0L;
/* 65 */     int num = getNonZeroNumInRow(row);
/* 66 */     for (int count = 0; count < num; count++)
/*    */     {
/* 68 */       sum += getNonZeroIntScoreInRow(row, count);
/*    */     }
/* 70 */     return sum;
/*    */   }
/*    */ 
/*    */   public long getColumnSum(int column)
/*    */   {
/* 75 */     return ((IntFlatSparseMatrix)transpose()).getRowSum(column);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.IntFlatSparseMatrix
 * JD-Core Version:    0.6.2
 */