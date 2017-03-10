/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ 
/*    */ public class FeatureTypeSegmentLength extends AbstractFeatureType
/*    */ {
/*    */   protected int segLen;
/*    */   protected int maxLen;
/*    */ 
/*    */   public FeatureTypeSegmentLength()
/*    */   {
/* 15 */     super(false);
/* 16 */     this.maxLen = 2147483647;
/*    */   }
/*    */ 
/*    */   public FeatureTypeSegmentLength(int maxSegmentLength) {
/* 20 */     super(false);
/* 21 */     this.maxLen = maxSegmentLength;
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int pos) {
/* 25 */     return startScanFeaturesAt(data, pos, pos);
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos) {
/* 29 */     this.segLen = Math.min(endPos + 1 - startPos, this.maxLen);
/* 30 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 34 */     return this.segLen > 0;
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 43 */     int curState = -1;
/* 44 */     String name = "Length" + (this.segLen == this.maxLen ? ">=" : "=") + this.segLen;
/* 45 */     FeatureIdentifier id = new FeatureIdentifier(name, this.segLen, curState);
/* 46 */     Feature f = new BasicFeature(id, curState, 1.0D);
/* 47 */     this.segLen = 0;
/* 48 */     return f;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeSegmentLength
 * JD-Core Version:    0.6.2
 */