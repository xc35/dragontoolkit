/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.model.ModelGraph;
/*    */ 
/*    */ public class FeatureTypeStart extends AbstractFeatureType
/*    */ {
/*    */   private ModelGraph model;
/*    */   private int curState;
/*    */   private int index;
/*    */ 
/*    */   public FeatureTypeStart(ModelGraph model)
/*    */   {
/* 21 */     super(false);
/* 22 */     this.idPrefix = "S_";
/* 23 */     this.model = model;
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int pos) {
/* 27 */     return startScanFeaturesAt(data, pos, pos);
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos) {
/* 31 */     if (startPos > 0) {
/* 32 */       this.curState = -1;
/* 33 */       return false;
/*    */     }
/*    */ 
/* 36 */     this.index = 0;
/* 37 */     this.curState = this.model.getStartState(this.index);
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 43 */     return this.curState >= 0;
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 49 */     FeatureIdentifier id = new FeatureIdentifier(this.idPrefix, this.curState, this.curState);
/* 50 */     BasicFeature f = new BasicFeature(id, this.curState, 1.0D);
/* 51 */     this.index += 1;
/* 52 */     this.curState = this.model.getStartState(this.index);
/* 53 */     return f;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeStart
 * JD-Core Version:    0.6.2
 */