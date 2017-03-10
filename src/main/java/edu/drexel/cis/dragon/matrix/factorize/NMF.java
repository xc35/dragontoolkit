/*    */ package edu.drexel.cis.dragon.matrix.factorize;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.DoubleDenseMatrix;
/*    */ import edu.drexel.cis.dragon.matrix.DoubleFlatDenseMatrix;
/*    */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*    */ 
/*    */ public class NMF extends AbstractFactorization
/*    */ {
/*    */   private SparseMatrix xt;
/*    */   private DoubleDenseMatrix u;
/*    */   private DoubleDenseMatrix m;
/*    */   private DoubleDenseMatrix v;
/*    */   private int iterations;
/*    */ 
/*    */   public NMF(int iterations)
/*    */   {
/* 20 */     this.iterations = iterations;
/*    */   }
/*    */ 
/*    */   public void factorize(SparseMatrix x, int dimension)
/*    */   {
/* 28 */     this.xt = ((SparseMatrix)x.transpose());
/* 29 */     this.u = genPositiveMatrix(x.rows(), dimension);
/* 30 */     this.v = genPositiveMatrix(x.columns(), dimension);
/* 31 */     DoubleDenseMatrix xv = new DoubleFlatDenseMatrix(x.rows(), dimension);
/* 32 */     DoubleDenseMatrix vt = new DoubleFlatDenseMatrix(dimension, x.columns());
/* 33 */     DoubleDenseMatrix vtv = new DoubleFlatDenseMatrix(dimension, dimension);
/* 34 */     DoubleDenseMatrix utu = vtv;
/* 35 */     DoubleDenseMatrix uvtv = new DoubleFlatDenseMatrix(x.rows(), dimension);
/* 36 */     DoubleDenseMatrix xtu = new DoubleFlatDenseMatrix(x.columns(), dimension);
/* 37 */     DoubleDenseMatrix ut = new DoubleFlatDenseMatrix(dimension, x.rows());
/* 38 */     DoubleDenseMatrix vutu = new DoubleFlatDenseMatrix(x.columns(), dimension);
/*    */ 
/* 40 */     for (int k = 0; k < this.iterations; k++) {
/* 41 */       product(x, this.v, xv);
/* 42 */       transpose(this.v, vt);
/* 43 */       product(vt, this.v, vtv);
/* 44 */       product(this.u, vtv, uvtv);
/*    */ 
/* 46 */       product(this.xt, this.u, xtu);
/* 47 */       transpose(this.u, ut);
/* 48 */       product(ut, this.u, utu);
/* 49 */       product(this.v, utu, vutu);
/*    */ 
/* 51 */       for (int i = 0; i < this.v.rows(); i++) {
/* 52 */         for (int j = 0; j < this.v.columns(); j++) {
/* 53 */           double score = vutu.getDouble(i, j) + 1.E-009D;
/* 54 */           score = this.v.getDouble(i, j) * xtu.getDouble(i, j) / score;
/* 55 */           this.v.setDouble(i, j, score);
/*    */         }
/*    */       }
/*    */ 
/* 59 */       for (int i = 0; i < this.u.rows(); i++) {
/* 60 */         for (int j = 0; j < this.u.columns(); j++) {
/* 61 */           double score = uvtv.getDouble(i, j) + 1.E-009D;
/* 62 */           score = this.u.getDouble(i, j) * xv.getDouble(i, j) / score;
/* 63 */           this.u.setDouble(i, j, score);
/*    */         }
/*    */       }
/* 66 */       normalizeColumn(this.u);
/*    */     }
/*    */   }
/*    */ 
/*    */   public DoubleDenseMatrix getLeftMatrix() {
/* 71 */     return this.u;
/*    */   }
/*    */ 
/*    */   public DoubleDenseMatrix getRightMatrix() {
/* 75 */     return this.v;
/*    */   }
/*    */ 
/*    */   public DoubleDenseMatrix getMiddleMatrix() {
/* 79 */     return this.m;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.factorize.NMF
 * JD-Core Version:    0.6.2
 */