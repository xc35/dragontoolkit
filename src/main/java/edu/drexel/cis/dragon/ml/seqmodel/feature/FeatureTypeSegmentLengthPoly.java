/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ 
/*    */ public class FeatureTypeSegmentLengthPoly extends AbstractFeatureType
/*    */ {
/*    */   private double lenSq;
/*    */   private short callNo;
/*    */   private int maxSegLen;
/*    */ 
/*    */   public FeatureTypeSegmentLengthPoly(int maxSegmentLength)
/*    */   {
/* 16 */     super(false);
/* 17 */     this.maxSegLen = maxSegmentLength;
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int pos) {
/* 21 */     return startScanFeaturesAt(data, pos, pos);
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos) {
/* 25 */     this.lenSq = ((endPos + 1 - startPos) / this.maxSegLen);
/* 26 */     this.callNo = 0;
/* 27 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 31 */     return this.callNo < 2;
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 40 */     int curState = -1;
/* 41 */     String name = this.callNo == 0 ? "LENGTH^1" : "LENGTH^2";
/*    */     Feature f;
/* 42 */     if (this.callNo == 0) {
/* 43 */       FeatureIdentifier id = new FeatureIdentifier(name, 0, curState);
/* 44 */       f = new BasicFeature(id, curState, this.lenSq);
/*    */     } else {
/* 46 */       FeatureIdentifier id = new FeatureIdentifier(name, 1, curState);
/* 47 */       f = new BasicFeature(id, curState, this.lenSq * this.lenSq);
/*    */     }
/* 49 */     this.callNo = ((short)(this.callNo + 1));
/* 50 */     return f;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeSegmentLengthPoly
 * JD-Core Version:    0.6.2
 */