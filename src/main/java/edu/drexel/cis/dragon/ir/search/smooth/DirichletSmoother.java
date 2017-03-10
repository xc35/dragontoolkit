/*    */ package edu.drexel.cis.dragon.ir.search.smooth;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*    */ 
/*    */ public class DirichletSmoother extends AbstractSmoother
/*    */ {
/*    */   private long collectionTermCount;
/*    */   private double dirichletCoeffi;
/*    */   private double collectionProb;
/*    */   private int docTermCount;
/*    */ 
/*    */   public DirichletSmoother(IRCollection collection, double dirichletCoeffi)
/*    */   {
/* 22 */     this.collectionTermCount = collection.getTermCount();
/* 23 */     this.dirichletCoeffi = dirichletCoeffi;
/* 24 */     this.useLog = true;
/* 25 */     this.docFirstOptimal = true;
/* 26 */     this.querytermFirstOptimal = true;
/*    */   }
/*    */ 
/*    */   public boolean setParameters(double[] params) {
/* 30 */     if ((params != null) && (params.length >= 1))
/*    */     {
/* 32 */       this.dirichletCoeffi = params[0];
/* 33 */       return true;
/*    */     }
/*    */ 
/* 36 */     return false;
/*    */   }
/*    */ 
/*    */   public void setQueryTerm(SimpleTermPredicate queryTerm) {
/* 40 */     this.queryWeight = queryTerm.getWeight();
/* 41 */     this.collectionProb = (this.dirichletCoeffi * queryTerm.getFrequency() / this.collectionTermCount);
/*    */   }
/*    */ 
/*    */   public void setDoc(IRDoc doc) {
/* 45 */     this.docTermCount = doc.getTermCount();
/*    */   }
/*    */ 
/*    */   protected double computeSmoothedProb(int termFreq) {
/* 49 */     return this.queryWeight * getProb((termFreq + this.collectionProb) / (this.docTermCount + this.dirichletCoeffi));
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.smooth.DirichletSmoother
 * JD-Core Version:    0.6.2
 */