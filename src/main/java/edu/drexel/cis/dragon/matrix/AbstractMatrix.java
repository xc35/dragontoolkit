/*    */ package edu.drexel.cis.dragon.matrix;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public abstract class AbstractMatrix
/*    */   implements Matrix, Serializable
/*    */ {
/*    */   protected Matrix transposeMatrix;
/*    */   protected int rows;
/*    */   protected int columns;
/*    */   protected int rowBase;
/*    */   protected int columnBase;
/*    */   protected int cellDataLength;
/*    */ 
/*    */   public int rows()
/*    */   {
/* 19 */     return this.rows;
/*    */   }
/*    */ 
/*    */   public int columns() {
/* 23 */     return this.columns;
/*    */   }
/*    */ 
/*    */   public void setTranspose(Matrix matrix) {
/* 27 */     this.transposeMatrix = matrix;
/*    */   }
/*    */ 
/*    */   public Matrix getTranspose() {
/* 31 */     return this.transposeMatrix;
/*    */   }
/*    */ 
/*    */   public Matrix transpose() {
/* 35 */     return this.transposeMatrix;
/*    */   }
/*    */ 
/*    */   public int getCellDataLength() {
/* 39 */     return this.cellDataLength;
/*    */   }
/*    */ 
/*    */   public int getBaseRow() {
/* 43 */     return this.rowBase;
/*    */   }
/*    */ 
/*    */   public int getBaseColumn() {
/* 47 */     return this.columnBase;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.AbstractMatrix
 * JD-Core Version:    0.6.2
 */