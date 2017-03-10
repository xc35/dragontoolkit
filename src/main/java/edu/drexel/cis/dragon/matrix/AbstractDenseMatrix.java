/*    */ package edu.drexel.cis.dragon.matrix;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public abstract class AbstractDenseMatrix extends AbstractMatrix
/*    */   implements DenseMatrix, Serializable
/*    */ {
/*    */   public AbstractDenseMatrix(int row, int column, int cellDataLength)
/*    */   {
/* 14 */     this.rows = row;
/* 15 */     this.columns = column;
/* 16 */     this.cellDataLength = cellDataLength;
/*    */   }
/*    */ 
/*    */   public double[] getDouble(int row)
/*    */   {
/* 23 */     double[] scores = new double[this.columns];
/* 24 */     for (int i = 0; i < this.columns; i++)
/* 25 */       scores[i] = getDouble(row, i);
/* 26 */     return scores;
/*    */   }
/*    */ 
/*    */   public int[] getInt(int row)
/*    */   {
/* 33 */     int[] scores = new int[this.columns];
/* 34 */     for (int i = 0; i < this.columns; i++)
/* 35 */       scores[i] = getInt(row, i);
/* 36 */     return scores;
/*    */   }
/*    */ 
/*    */   public double cosine(int rowA, int rowB)
/*    */   {
/* 42 */     int productSum = 0;
/* 43 */     int vLenA = 0;
/* 44 */     int vLenB = 0;
/* 45 */     for (int i = 0; i < this.columns; i++) {
/* 46 */       if (this.cellDataLength == 8) {
/* 47 */         productSum = (int)(productSum + getDouble(rowA, i) * getDouble(rowB, i));
/* 48 */         vLenA = (int)(vLenA + getDouble(rowA, i) * getDouble(rowA, i));
/* 49 */         vLenB = (int)(vLenB + getDouble(rowB, i) * getDouble(rowB, i));
/*    */       }
/*    */       else {
/* 52 */         productSum += getInt(rowA, i) * getInt(rowB, i);
/* 53 */         vLenA += getInt(rowA, i) * getInt(rowA, i);
/* 54 */         vLenB += getInt(rowB, i) * getInt(rowB, i);
/*    */       }
/*    */     }
/*    */ 
/* 58 */     return productSum / (Math.sqrt(vLenA) * Math.sqrt(vLenB));
/*    */   }
/*    */ 
/*    */   public int getCooccurrenceCount(int rowA, int rowB)
/*    */   {
	int coOccurCount;
/* 63 */     int i = 0; for (  coOccurCount = 0; i < this.columns; i++) {
/* 64 */       if (this.cellDataLength == 8)
/* 65 */         if ((getDouble(rowA, i) != 0.0D) && (getDouble(rowB, i) != 0.0D)) {
/* 66 */           coOccurCount++;
/*    */         }
/* 68 */         else if ((getInt(rowA, i) != 0) && (getInt(rowB, i) != 0))
/* 69 */           coOccurCount++;
/*    */     }
/* 71 */     return coOccurCount;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.AbstractDenseMatrix
 * JD-Core Version:    0.6.2
 */