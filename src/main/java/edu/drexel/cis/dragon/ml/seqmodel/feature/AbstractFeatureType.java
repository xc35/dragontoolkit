/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.Dataset;
/*    */ 
/*    */ public abstract class AbstractFeatureType
/*    */   implements FeatureType
/*    */ {
/*    */   protected String idPrefix;
/*    */   private boolean needTraining;
/*    */   private int typeID;
/*    */ 
/*    */   public AbstractFeatureType(boolean needTraining)
/*    */   {
/* 21 */     this.needTraining = needTraining;
/* 22 */     this.typeID = -1;
/*    */   }
/*    */ 
/*    */   public boolean needTraining() {
/* 26 */     return this.needTraining;
/*    */   }
/*    */ 
/*    */   public boolean train(Dataset data) {
/* 30 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean saveTrainingResult() {
/* 34 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean readTrainingResult() {
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   public int getTypeID() {
/* 42 */     return this.typeID;
/*    */   }
/*    */ 
/*    */   public void setTypeID(int typeID) {
/* 46 */     this.typeID = typeID;
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int pos) {
/* 50 */     return startScanFeaturesAt(data, pos, pos);
/*    */   }
/*    */ 
/*    */   public boolean supportSegment() {
/* 54 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.AbstractFeatureType
 * JD-Core Version:    0.6.2
 */