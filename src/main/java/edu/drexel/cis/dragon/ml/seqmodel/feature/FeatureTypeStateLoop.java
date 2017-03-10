/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ 
/*    */ public class FeatureTypeStateLoop extends AbstractFeatureTypeWrapper
/*    */ {
/*    */   private Feature nextFeature;
/*    */   int stateNum;
/*    */   int curState;
/* 14 */   boolean optimize = false;
/*    */ 
/*    */   public FeatureTypeStateLoop(FeatureType ftype, int stateNum) {
/* 17 */     super(ftype);
/* 18 */     this.stateNum = stateNum;
/*    */   }
/*    */ 
/*    */   boolean advance() {
/* 22 */     this.curState += 1;
/* 23 */     if (this.curState < this.stateNum)
/* 24 */       return true;
/* 25 */     if (this.ftype.hasNext()) {
/* 26 */       this.nextFeature = this.ftype.next();
/* 27 */       this.curState = 0;
/*    */     }
/* 29 */     return this.curState < this.stateNum;
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos) {
/* 33 */     this.curState = this.stateNum;
/* 34 */     this.ftype.startScanFeaturesAt(data, startPos, endPos);
/* 35 */     return advance();
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 39 */     return this.curState < this.stateNum;
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 46 */     Feature curFeature = this.nextFeature.copy();
/* 47 */     curFeature.setLabel(this.curState);
/* 48 */     FeatureIdentifier id = curFeature.getID();
/* 49 */     id.setState(this.curState);
/* 50 */     id.setId(id.getId() * this.stateNum + this.curState);
/*    */ 
/* 52 */     advance();
/* 53 */     return curFeature;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeStateLoop
 * JD-Core Version:    0.6.2
 */