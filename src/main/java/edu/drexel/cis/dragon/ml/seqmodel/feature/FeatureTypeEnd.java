/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.model.ModelGraph;
/*    */ 
/*    */ public class FeatureTypeEnd extends AbstractFeatureType
/*    */ {
/*    */   private ModelGraph model;
/*    */   private int curState;
/*    */   private int index;
/*    */ 
/*    */   public FeatureTypeEnd(ModelGraph model)
/*    */   {
/* 23 */     super(false);
/* 24 */     this.idPrefix = "END_";
/* 25 */     this.model = model;
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos) {
/* 29 */     if (endPos < data.length() - 1) {
/* 30 */       this.curState = -1;
/* 31 */       return false;
/*    */     }
/*    */ 
/* 34 */     this.index = 0;
/* 35 */     this.curState = this.model.getEndState(this.index);
/* 36 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 41 */     return this.curState >= 0;
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 47 */     FeatureIdentifier id = new FeatureIdentifier(this.idPrefix, this.curState, this.curState);
/* 48 */     BasicFeature f = new BasicFeature(id, this.curState, 1.0D);
/* 49 */     this.index += 1;
/* 50 */     this.curState = this.model.getEndState(this.index);
/* 51 */     return f;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeEnd
 * JD-Core Version:    0.6.2
 */