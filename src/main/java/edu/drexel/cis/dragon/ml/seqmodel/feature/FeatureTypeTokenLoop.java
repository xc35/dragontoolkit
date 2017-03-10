/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ 
/*    */ public class FeatureTypeTokenLoop extends AbstractFeatureTypeWrapper
/*    */ {
/*    */   private int currPos;
/*    */   private int segEnd;
/*    */   private transient DataSequence dataSeq;
/*    */ 
/*    */   public FeatureTypeTokenLoop(FeatureType s)
/*    */   {
/* 16 */     super(s);
/*    */   }
/*    */ 
/*    */   private void advance() {
/*    */     while (true) {
/* 21 */       if (this.ftype.hasNext())
/* 22 */         return;
/* 23 */       this.currPos += 1;
/* 24 */       if (this.currPos > this.segEnd)
/* 25 */         return;
/* 26 */       this.ftype.startScanFeaturesAt(this.dataSeq, this.currPos, this.currPos);
/*    */     }
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int pos) {
/* 31 */     return startScanFeaturesAt(data, pos, pos);
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos) {
/* 35 */     this.currPos = startPos;
/* 36 */     this.segEnd = endPos;
/* 37 */     this.dataSeq = data;
/* 38 */     this.ftype.startScanFeaturesAt(data, startPos, startPos);
/* 39 */     advance();
/* 40 */     return this.ftype.hasNext();
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 44 */     return (this.currPos <= this.segEnd) && (this.ftype.hasNext());
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 50 */     Feature f = this.ftype.next();
/* 51 */     advance();
/* 52 */     return f;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeTokenLoop
 * JD-Core Version:    0.6.2
 */