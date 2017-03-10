/*    */ package edu.drexel.cis.dragon.matrix.factorize;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.DoubleDenseMatrix;
/*    */ import edu.drexel.cis.dragon.matrix.DoubleFlatDenseMatrix;
/*    */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*    */ import java.util.Random;
/*    */ 
/*    */ public abstract class AbstractFactorization
/*    */   implements Factorization
/*    */ {
/*    */   public static final double SMALL_QUANTITY = 1.E-009D;
/*    */ 
/*    */   protected DoubleDenseMatrix genPositiveMatrix(int x, int y)
/*    */   {
/* 23 */     Random rand = new Random(1L);
/* 24 */     DoubleDenseMatrix matrix = new DoubleFlatDenseMatrix(x, y);
/* 25 */     for (int i = 0; i < x; i++) {
/* 26 */       for (int j = 0; j < y; j++) {
/* 27 */         matrix.setDouble(i, j, rand.nextDouble() + 1.E-009D);
/*    */       }
/*    */     }
/* 30 */     return matrix;
/*    */   }
/*    */ 
/*    */   protected void product(DoubleDenseMatrix a, DoubleDenseMatrix b, DoubleDenseMatrix c)
/*    */   {
/* 37 */     for (int row = 0; row < a.rows(); row++)
/* 38 */       for (int col = 0; col < b.columns(); col++) {
/* 39 */         double score = 0.0D;
/* 40 */         for (int i = 0; i < a.columns(); i++) {
/* 41 */           score += a.getDouble(row, i) * b.getDouble(i, col);
/*    */         }
/* 43 */         c.setDouble(row, col, score);
/*    */       }
/*    */   }
/*    */ 
/*    */   protected void product(SparseMatrix a, DoubleDenseMatrix b, DoubleDenseMatrix c)
/*    */   {
/* 54 */     for (int row = 0; row < a.rows(); row++) {
/* 55 */       int[] arrColumn = a.getNonZeroColumnsInRow(row);
/* 56 */       double[] arrScore = a.getNonZeroDoubleScoresInRow(row);
/* 57 */       for (int col = 0; col < b.columns(); col++) {
/* 58 */         double score = 0.0D;
/* 59 */         for (int i = 0; i < arrColumn.length; i++) {
/* 60 */           score += arrScore[i] * b.getDouble(arrColumn[i], col);
/*    */         }
/* 62 */         c.setDouble(row, col, score);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   protected void transpose(DoubleDenseMatrix a, DoubleDenseMatrix at)
/*    */   {
/* 70 */     for (int row = 0; row < a.rows(); row++)
/* 71 */       for (int col = 0; col < a.columns(); col++)
/* 72 */         at.setDouble(col, row, a.getDouble(row, col));
/*    */   }
/*    */ 
/*    */   protected void normalizeColumn(DoubleDenseMatrix a)
/*    */   {
/* 81 */     for (int col = 0; col < a.columns(); col++) {
/* 82 */       double norm = 0.0D;
/* 83 */       for (int row = 0; row < a.rows(); row++) {
/* 84 */         double score = a.getDouble(row, col);
/* 85 */         norm += score * score;
/*    */       }
/* 87 */       norm = Math.sqrt(norm);
/* 88 */       for (int row = 0; row < a.rows(); row++) {
/* 89 */         double score = a.getDouble(row, col);
/* 90 */         a.setDouble(row, col, score / norm);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.factorize.AbstractFactorization
 * JD-Core Version:    0.6.2
 */