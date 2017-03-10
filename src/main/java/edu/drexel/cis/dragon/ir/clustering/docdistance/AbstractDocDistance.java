/*    */ package edu.drexel.cis.dragon.ir.clustering.docdistance;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*    */ import edu.drexel.cis.dragon.matrix.DenseMatrix;
/*    */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*    */ 
/*    */ public abstract class AbstractDocDistance
/*    */   implements DocDistance
/*    */ {
/*    */   protected FeatureFilter featureFilter;
/*    */   protected SparseMatrix sparseMatrix;
/*    */   protected DenseMatrix denseMatrix;
/*    */ 
/*    */   public AbstractDocDistance(SparseMatrix matrix)
/*    */   {
/* 21 */     this.sparseMatrix = matrix;
/*    */   }
/*    */ 
/*    */   public AbstractDocDistance(DenseMatrix matrix) {
/* 25 */     this.denseMatrix = matrix;
/*    */   }
/*    */ 
/*    */   public void setFeatureFilter(FeatureFilter selector) {
/* 29 */     this.featureFilter = selector;
/*    */   }
/*    */ 
/*    */   public FeatureFilter getFeatureFilter() {
/* 33 */     return this.featureFilter;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.docdistance.AbstractDocDistance
 * JD-Core Version:    0.6.2
 */