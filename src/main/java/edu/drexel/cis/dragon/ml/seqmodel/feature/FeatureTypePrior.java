/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ 
/*    */ public class FeatureTypePrior extends AbstractFeatureType
/*    */ {
/*    */   private int stateNum;
/*    */   private int curState;
/*    */ 
/*    */   public FeatureTypePrior(int stateNum)
/*    */   {
/* 19 */     super(false);
/* 20 */     this.stateNum = stateNum;
/* 21 */     this.idPrefix = "Bias_";
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos) {
/* 25 */     this.curState = (this.stateNum - 1);
/* 26 */     return hasNext();
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 30 */     return this.curState >= 0;
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 37 */     FeatureIdentifier id = new FeatureIdentifier(this.idPrefix + this.curState, this.curState, this.curState);
/* 38 */     Feature f = new BasicFeature(id, this.curState, 1.0D);
/* 39 */     this.curState -= 1;
/* 40 */     return f;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypePrior
 * JD-Core Version:    0.6.2
 */