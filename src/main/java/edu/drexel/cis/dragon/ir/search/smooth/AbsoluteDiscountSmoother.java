/*    */ package edu.drexel.cis.dragon.ir.search.smooth;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*    */ 
/*    */ public class AbsoluteDiscountSmoother extends AbstractSmoother
/*    */ {
/*    */   private long collectionTermCount;
/*    */   private double absoluteDiscount;
/*    */   private double collectionProb;
/*    */   private double delta;
/*    */   private int docTermCount;
/*    */ 
/*    */   public AbsoluteDiscountSmoother(IRCollection collection, double absoluteDiscount)
/*    */   {
/* 22 */     this.collectionTermCount = collection.getTermCount();
/* 23 */     this.absoluteDiscount = absoluteDiscount;
/* 24 */     this.useLog = true;
/* 25 */     this.docFirstOptimal = true;
/* 26 */     this.querytermFirstOptimal = true;
/*    */   }
/*    */ 
/*    */   public boolean setParameters(double[] params) {
/* 30 */     if ((params != null) && (params.length >= 1))
/*    */     {
/* 32 */       this.absoluteDiscount = params[0];
/* 33 */       return true;
/*    */     }
/*    */ 
/* 36 */     return false;
/*    */   }
/*    */ 
/*    */   public void setQueryTerm(SimpleTermPredicate queryTerm) {
/* 40 */     this.queryWeight = queryTerm.getWeight();
/* 41 */     this.collectionProb = (queryTerm.getFrequency() / this.collectionTermCount);
/*    */   }
/*    */ 
/*    */   public void setDoc(IRDoc doc) {
/* 45 */     this.docTermCount = doc.getTermCount();
/* 46 */     if (this.docTermCount <= 0) this.docTermCount = 1;
/* 47 */     this.delta = (this.absoluteDiscount * doc.getTermNum() / this.docTermCount);
/*    */   }
/*    */ 
/*    */   protected double computeSmoothedProb(int termFreq) {
/* 51 */     if (termFreq > this.absoluteDiscount) {
/* 52 */       return this.queryWeight * getProb((termFreq - this.absoluteDiscount) / this.docTermCount + this.delta * this.collectionProb);
/*    */     }
/* 54 */     return this.queryWeight * getProb(this.delta * this.collectionProb);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.smooth.AbsoluteDiscountSmoother
 * JD-Core Version:    0.6.2
 */