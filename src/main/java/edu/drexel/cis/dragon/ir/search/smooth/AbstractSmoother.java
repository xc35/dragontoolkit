/*    */ package edu.drexel.cis.dragon.ir.search.smooth;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*    */ 
/*    */ public abstract class AbstractSmoother
/*    */   implements Smoother
/*    */ {
/*    */   protected boolean useLog;
/*    */   protected double queryWeight;
/*    */   protected boolean docFirstOptimal;
/*    */   protected boolean querytermFirstOptimal;
/*    */ 
/*    */   protected abstract double computeSmoothedProb(int paramInt);
/*    */ 
/*    */   public boolean isDocFirstOptimal()
/*    */   {
/* 22 */     return this.docFirstOptimal;
/*    */   }
/*    */   public boolean isQueryTermFirstOptimal() {
/* 25 */     return this.querytermFirstOptimal;
/*    */   }
/*    */ 
/*    */   public void setLogLikelihoodOption(boolean option) {
/* 29 */     this.useLog = option;
/*    */   }
/*    */ 
/*    */   public boolean getLogLikelihoodOption() {
/* 33 */     return this.useLog;
/*    */   }
/*    */ 
/*    */   public double getSmoothedProb(int termFreq) {
/* 37 */     return computeSmoothedProb(termFreq);
/*    */   }
/*    */ 
/*    */   public double getSmoothedProb(IRDoc doc, int termFreq) {
/* 41 */     setDoc(doc);
/* 42 */     return computeSmoothedProb(termFreq);
/*    */   }
/*    */ 
/*    */   public double getSmoothedProb(IRDoc doc) {
/* 46 */     setDoc(doc);
/* 47 */     return computeSmoothedProb(0);
/*    */   }
/*    */ 
/*    */   public double getSmoothedProb(SimpleTermPredicate queryTerm, int termFreq) {
/* 51 */     setQueryTerm(queryTerm);
/* 52 */     return computeSmoothedProb(termFreq);
/*    */   }
/*    */ 
/*    */   public double getSmoothedProb(SimpleTermPredicate queryTerm) {
/* 56 */     setQueryTerm(queryTerm);
/* 57 */     return computeSmoothedProb(0);
/*    */   }
/*    */ 
/*    */   public double getSmoothedProb(IRDoc doc, SimpleTermPredicate queryTerm) {
/* 61 */     setDoc(doc);
/* 62 */     setQueryTerm(queryTerm);
/* 63 */     return computeSmoothedProb(0);
/*    */   }
/*    */ 
/*    */   public double getSmoothedProb(IRDoc doc, SimpleTermPredicate queryTerm, int termFreq) {
/* 67 */     setDoc(doc);
/* 68 */     setQueryTerm(queryTerm);
/* 69 */     return computeSmoothedProb(termFreq);
/*    */   }
/*    */ 
/*    */   protected double getProb(double originalProb) {
/* 73 */     if (this.useLog) {
/* 74 */       return Math.log(originalProb);
/*    */     }
/*    */ 
/* 77 */     return originalProb;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.smooth.AbstractSmoother
 * JD-Core Version:    0.6.2
 */