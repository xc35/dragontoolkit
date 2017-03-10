/*    */ package edu.drexel.cis.dragon.ir.search.smooth;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*    */ 
/*    */ public class TFIDFSmoother extends AbstractSmoother
/*    */ {
/*    */   private int docNum;
/*    */   private double curTermIDF;
/*    */   private double curDocLengthRatio;
/*    */   private double avgDocLength;
/*    */   private double bm25k1;
/*    */   private double bm25b;
/*    */   private double param1;
/*    */   private double param2;
/*    */   private boolean useBM25;
/*    */ 
/*    */   public TFIDFSmoother(IRCollection collection)
/*    */   {
/* 23 */     this.docNum = collection.getDocNum();
/* 24 */     this.useLog = false;
/* 25 */     this.docFirstOptimal = true;
/* 26 */     this.querytermFirstOptimal = true;
/* 27 */     this.useBM25 = false;
/*    */   }
/*    */ 
/*    */   public TFIDFSmoother(IRCollection collection, double bm25k1, double bm25b) {
/* 31 */     this.docNum = collection.getDocNum();
/* 32 */     this.avgDocLength = (collection.getTermCount() * 1.0D / this.docNum);
/* 33 */     this.bm25b = bm25b;
/* 34 */     this.bm25k1 = bm25k1;
/* 35 */     this.useBM25 = true;
/* 36 */     this.useLog = false;
/* 37 */     this.docFirstOptimal = true;
/* 38 */     this.querytermFirstOptimal = true;
/* 39 */     this.param1 = (bm25k1 * (1.0D - bm25b));
/* 40 */     this.param2 = (bm25k1 * bm25b);
/*    */   }
/*    */ 
/*    */   public boolean setParameters(double[] params) {
/* 44 */     if ((params != null) && (params.length >= 2))
/*    */     {
/* 46 */       this.bm25k1 = params[0];
/* 47 */       this.bm25b = params[1];
/* 48 */       this.param1 = (this.bm25k1 * (1.0D - this.bm25b));
/* 49 */       this.param2 = (this.bm25k1 * this.bm25b);
/* 50 */       return true;
/*    */     }
/*    */ 
/* 53 */     return false;
/*    */   }
/*    */ 
/*    */   public void setQueryTerm(SimpleTermPredicate queryTerm) {
/* 57 */     this.queryWeight = queryTerm.getWeight();
/* 58 */     this.curTermIDF = Math.log((1 + this.docNum) / (0.5D + queryTerm.getDocFrequency()));
/*    */   }
/*    */ 
/*    */   public void setDoc(IRDoc doc) {
/* 62 */     if (this.useBM25)
/* 63 */       this.curDocLengthRatio = (this.param2 * doc.getTermCount() / this.avgDocLength);
/*    */   }
/*    */ 
/*    */   protected double computeSmoothedProb(int termFrequency) {
/* 67 */     if (this.useBM25) {
/* 68 */       return this.queryWeight * termFrequency * this.curTermIDF / (this.param1 + this.curDocLengthRatio + termFrequency);
/*    */     }
/* 70 */     return this.queryWeight * termFrequency * this.curTermIDF;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.smooth.TFIDFSmoother
 * JD-Core Version:    0.6.2
 */