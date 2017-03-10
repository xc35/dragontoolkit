/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.POSToken;
/*    */ 
/*    */ public class FeatureTypePOSPattern extends AbstractFeatureType
/*    */ {
/*    */   private int patternID;
/*    */   private int tagNum;
/*    */   private int maxSegmentLength;
/*    */ 
/*    */   public FeatureTypePOSPattern(int stateNum, int tagNum)
/*    */   {
/* 18 */     this(stateNum, tagNum, 1);
/*    */   }
/*    */ 
/*    */   public FeatureTypePOSPattern(int stateNum, int tagNum, int maxSegmentLength) {
/* 22 */     super(false);
/* 23 */     this.idPrefix = "PC_";
/* 24 */     this.maxSegmentLength = maxSegmentLength;
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos) {
/* 28 */     if (endPos - startPos + 1 > this.maxSegmentLength) {
/* 29 */       this.patternID = -1;
/* 30 */       return false;
/*    */     }
/*    */ 
/* 33 */     this.patternID = getMultiTag(data, startPos, endPos);
/* 34 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 39 */     return this.patternID >= 0;
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 46 */     int curState = -1;
/* 47 */     FeatureIdentifier id = new FeatureIdentifier(this.idPrefix + String.valueOf(this.patternID), this.patternID, curState);
/* 48 */     BasicFeature f = new BasicFeature(id, curState, 1.0D);
/* 49 */     this.patternID = -1;
/* 50 */     return f;
/*    */   }
/*    */ 
/*    */   private int getMultiTag(DataSequence data, int startPos, int endPos)
/*    */   {
/* 56 */     int curMultiTag = ((POSToken)data.getToken(startPos)).getPOSTag();
/* 57 */     for (int i = startPos + 1; i <= endPos; i++) {
/* 58 */       int curTag = ((POSToken)data.getToken(i)).getPOSTag();
/* 59 */       if (curTag < 0) {
/* 60 */         return -1;
/*    */       }
/* 62 */       curMultiTag = curMultiTag * this.tagNum + curTag;
/*    */     }
/* 64 */     return curMultiTag;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypePOSPattern
 * JD-Core Version:    0.6.2
 */