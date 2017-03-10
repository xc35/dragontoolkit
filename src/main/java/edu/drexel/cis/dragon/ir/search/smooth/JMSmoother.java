/*    */ package edu.drexel.cis.dragon.ir.search.smooth;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*    */ 
/*    */ public class JMSmoother extends AbstractSmoother
/*    */   implements Smoother
/*    */ {
/*    */   private double bkgCoefficient;
/*    */   private int docTermCount;
/*    */   private long collectionTermCount;
/*    */   private double collectionProb;
/*    */ 
/*    */   public JMSmoother(IRCollection collection, double bkgCoefficient)
/*    */   {
/* 21 */     this.collectionTermCount = collection.getTermCount();
/* 22 */     this.bkgCoefficient = bkgCoefficient;
/* 23 */     this.useLog = true;
/* 24 */     this.docFirstOptimal = true;
/* 25 */     this.querytermFirstOptimal = true;
/*    */   }
/*    */ 
/*    */   public boolean setParameters(double[] params) {
/* 29 */     if ((params != null) && (params.length >= 1))
/*    */     {
/* 31 */       this.bkgCoefficient = params[0];
/* 32 */       return true;
/*    */     }
/*    */ 
/* 35 */     return false;
/*    */   }
/*    */ 
/*    */   public void setQueryTerm(SimpleTermPredicate queryTerm) {
/* 39 */     this.queryWeight = queryTerm.getWeight();
/* 40 */     this.collectionProb = (this.bkgCoefficient * queryTerm.getFrequency() / this.collectionTermCount);
/*    */   }
/*    */ 
/*    */   public void setDoc(IRDoc doc) {
/* 44 */     this.docTermCount = doc.getTermCount();
/* 45 */     if (this.docTermCount <= 0) this.docTermCount = 1; 
/*    */   }
/*    */ 
/*    */   protected double computeSmoothedProb(int termFreq)
/*    */   {
/* 49 */     return this.queryWeight * getProb(termFreq * (1.0D - this.bkgCoefficient) / this.docTermCount + this.collectionProb);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.smooth.JMSmoother
 * JD-Core Version:    0.6.2
 */