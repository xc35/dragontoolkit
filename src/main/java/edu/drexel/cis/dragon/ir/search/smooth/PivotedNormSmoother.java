/*    */ package edu.drexel.cis.dragon.ir.search.smooth;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*    */ 
/*    */ public class PivotedNormSmoother extends AbstractSmoother
/*    */ {
/*    */   private int docTermCount;
/*    */   private int docNum;
/*    */   private double s;
/*    */   private double avgDocLength;
/*    */   private double idf;
/*    */ 
/*    */   public PivotedNormSmoother(IRCollection collection)
/*    */   {
/* 22 */     this.useLog = false;
/* 23 */     this.s = 0.2D;
/* 24 */     this.docNum = collection.getDocNum();
/* 25 */     this.avgDocLength = (collection.getTermCount() * 1.0D / this.docNum);
/* 26 */     this.docFirstOptimal = true;
/* 27 */     this.querytermFirstOptimal = true;
/*    */   }
/*    */ 
/*    */   public PivotedNormSmoother(IRCollection collection, double s) {
/* 31 */     this.useLog = false;
/* 32 */     this.s = s;
/* 33 */     this.docNum = collection.getDocNum();
/* 34 */     this.avgDocLength = (collection.getTermCount() * 1.0D / this.docNum);
/* 35 */     this.docFirstOptimal = true;
/* 36 */     this.querytermFirstOptimal = true;
/*    */   }
/*    */ 
/*    */   public boolean setParameters(double[] params) {
/* 40 */     this.s = params[0];
/* 41 */     return true;
/*    */   }
/*    */ 
/*    */   public void setQueryTerm(SimpleTermPredicate queryTerm) {
/* 45 */     this.queryWeight = queryTerm.getWeight();
/* 46 */     this.idf = Math.log((this.docNum + 1.0D) / queryTerm.getDocFrequency());
/*    */   }
/*    */ 
/*    */   public void setDoc(IRDoc doc) {
/* 50 */     this.docTermCount = doc.getTermCount();
/*    */   }
/*    */ 
/*    */   protected double computeSmoothedProb(int termFrequency) {
/* 54 */     return this.queryWeight * (1.0D + Math.log(1.0D + Math.log(termFrequency))) * this.idf / (1.0D - this.s + this.s * this.docTermCount / this.avgDocLength);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.smooth.PivotedNormSmoother
 * JD-Core Version:    0.6.2
 */