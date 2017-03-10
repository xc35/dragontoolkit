/*    */ package edu.drexel.cis.dragon.ml.seqmodel.crf;
/*    */ 
/*    */ public class EdgeGenerator
/*    */ {
/*    */   private int offset;
/*    */   private int numOrigY;
/*    */   private int markovOrder;
/*    */ 
/*    */   public EdgeGenerator(int originalLabelNum)
/*    */   {
/* 24 */     this(1, originalLabelNum);
/*    */   }
/*    */ 
/*    */   public EdgeGenerator(int markovOrder, int originalLabelNum) {
/* 28 */     this.offset = 1;
/* 29 */     for (int i = 0; i < markovOrder - 1; i++)
/* 30 */       this.offset *= originalLabelNum;
/* 31 */     this.numOrigY = originalLabelNum;
/* 32 */     this.markovOrder = markovOrder;
/*    */   }
/*    */ 
/*    */   public int first(int destY)
/*    */   {
/* 37 */     return destY / this.numOrigY;
/*    */   }
/*    */ 
/*    */   public int next(int destY, int currentSrcY)
/*    */   {
/* 42 */     return currentSrcY + this.offset;
/*    */   }
/*    */ 
/*    */   public int firstLabel(int pos)
/*    */   {
/* 47 */     return 0;
/*    */   }
/*    */ 
/*    */   public int nextLabel(int currentLabel, int pos)
/*    */   {
/* 52 */     if ((pos >= this.markovOrder - 1) || (currentLabel < this.numOrigY - 1))
/* 53 */       return currentLabel + 1;
/* 54 */     if (currentLabel >= Math.pow(this.numOrigY, pos + 1)) {
/* 55 */       return this.numOrigY * this.offset;
/*    */     }
/* 57 */     return currentLabel + 1;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.crf.EdgeGenerator
 * JD-Core Version:    0.6.2
 */