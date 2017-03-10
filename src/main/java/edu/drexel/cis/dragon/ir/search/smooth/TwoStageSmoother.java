/*    */ package edu.drexel.cis.dragon.ir.search.smooth;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*    */ 
/*    */ public class TwoStageSmoother extends AbstractSmoother
/*    */   implements Smoother
/*    */ {
/*    */   private double bkgCoefficient;
/*    */   private double dirichletCoefficient;
/*    */   private int docTermCount;
/*    */   private long collectionTermCount;
/*    */   private double docCollectionProb;
/*    */   private double queryBackgroundProb;
/*    */ 
/*    */   public TwoStageSmoother(IRCollection collection, double bkgCoefficient, double dirichletCoefficient)
/*    */   {
/* 21 */     this.collectionTermCount = collection.getTermCount();
/* 22 */     this.bkgCoefficient = bkgCoefficient;
/* 23 */     this.dirichletCoefficient = dirichletCoefficient;
/* 24 */     this.useLog = true;
/* 25 */     this.docFirstOptimal = true;
/* 26 */     this.querytermFirstOptimal = true;
/*    */   }
/*    */ 
/*    */   public boolean setParameters(double[] params) {
/* 30 */     if ((params != null) && (params.length >= 2))
/*    */     {
/* 32 */       this.bkgCoefficient = params[0];
/* 33 */       this.dirichletCoefficient = params[1];
/* 34 */       return true;
/*    */     }
/*    */ 
/* 37 */     return false;
/*    */   }
/*    */ 
/*    */   public void setQueryTerm(SimpleTermPredicate queryTerm) {
/* 41 */     this.queryWeight = queryTerm.getWeight();
/* 42 */     this.docCollectionProb = (this.dirichletCoefficient * queryTerm.getFrequency() / this.collectionTermCount);
/* 43 */     this.queryBackgroundProb = (this.bkgCoefficient * queryTerm.getFrequency() / this.collectionTermCount);
/*    */   }
/*    */ 
/*    */   public void setDoc(IRDoc doc) {
/* 47 */     this.docTermCount = doc.getTermCount();
/*    */   }
/*    */ 
/*    */   protected double computeSmoothedProb(int termFreq) {
/* 51 */     return this.queryWeight * getProb((1.0D - this.bkgCoefficient) * (termFreq + this.docCollectionProb) / (this.docTermCount + this.dirichletCoefficient) + this.queryBackgroundProb);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.smooth.TwoStageSmoother
 * JD-Core Version:    0.6.2
 */