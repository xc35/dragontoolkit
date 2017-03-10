/*    */ package edu.drexel.cis.dragon.ir.search.smooth;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*    */ 
/*    */ public class OkapiSmoother extends AbstractSmoother
/*    */ {
/*    */   private int docNum;
/*    */   private double curTermIDF;
/*    */   private double curDocLengthRatio;
/*    */   private double avgDocLength;
/*    */   private double bm25k1;
/*    */   private double bm25b;
/*    */   private double param1;
/*    */   private double param2;
/*    */ 
/*    */   public OkapiSmoother(IRCollection collection)
/*    */   {
/* 22 */     this.docNum = collection.getDocNum();
/* 23 */     this.avgDocLength = (collection.getTermCount() * 1.0D / this.docNum);
/* 24 */     this.bm25b = 0.75D;
/* 25 */     this.bm25k1 = 2.0D;
/* 26 */     this.useLog = false;
/* 27 */     this.docFirstOptimal = true;
/* 28 */     this.querytermFirstOptimal = true;
/* 29 */     this.param1 = (this.bm25k1 * (1.0D - this.bm25b));
/* 30 */     this.param2 = (this.bm25k1 * this.bm25b);
/*    */   }
/*    */ 
/*    */   public OkapiSmoother(IRCollection collection, double bm25k1, double bm25b) {
/* 34 */     this.docNum = collection.getDocNum();
/* 35 */     this.avgDocLength = (collection.getTermCount() * 1.0D / this.docNum);
/* 36 */     this.bm25b = bm25b;
/* 37 */     this.bm25k1 = bm25k1;
/* 38 */     this.useLog = false;
/* 39 */     this.docFirstOptimal = true;
/* 40 */     this.querytermFirstOptimal = true;
/* 41 */     this.param1 = (bm25k1 * (1.0D - bm25b));
/* 42 */     this.param2 = (bm25k1 * bm25b);
/*    */   }
/*    */ 
/*    */   public boolean setParameters(double[] params) {
/* 46 */     if ((params != null) && (params.length >= 2))
/*    */     {
/* 48 */       this.bm25k1 = params[0];
/* 49 */       this.bm25b = params[1];
/* 50 */       this.param1 = (this.bm25k1 * (1.0D - this.bm25b));
/* 51 */       this.param2 = (this.bm25k1 * this.bm25b);
/* 52 */       return true;
/*    */     }
/*    */ 
/* 55 */     return false;
/*    */   }
/*    */ 
/*    */   public void setQueryTerm(SimpleTermPredicate queryTerm) {
/* 59 */     this.queryWeight = queryTerm.getWeight();
/* 60 */     this.curTermIDF = Math.log((this.docNum - queryTerm.getDocFrequency() + 0.5D) / (queryTerm.getDocFrequency() + 0.5D));
/*    */   }
/*    */ 
/*    */   public void setDoc(IRDoc doc) {
/* 64 */     this.curDocLengthRatio = (this.param2 * doc.getTermCount() / this.avgDocLength);
/*    */   }
/*    */ 
/*    */   protected double computeSmoothedProb(int termFrequency)
/*    */   {
/* 70 */     return this.queryWeight * termFrequency * this.curTermIDF / (this.param1 + this.curDocLengthRatio + termFrequency);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.smooth.OkapiSmoother
 * JD-Core Version:    0.6.2
 */