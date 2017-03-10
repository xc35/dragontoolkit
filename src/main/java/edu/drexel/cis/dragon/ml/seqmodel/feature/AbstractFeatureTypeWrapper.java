/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.Dataset;
/*    */ 
/*    */ public abstract class AbstractFeatureTypeWrapper
/*    */   implements FeatureType
/*    */ {
/*    */   protected FeatureType ftype;
/*    */   private int typeID;
/*    */ 
/*    */   public AbstractFeatureTypeWrapper(FeatureType ftype)
/*    */   {
/* 21 */     this.ftype = ftype;
/* 22 */     this.typeID = -1;
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos) {
/* 26 */     return this.ftype.startScanFeaturesAt(data, startPos, endPos);
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 30 */     return this.ftype.hasNext();
/*    */   }
/*    */ 
/*    */   public Feature next() {
/* 34 */     return this.ftype.next();
/*    */   }
/*    */ 
/*    */   public boolean needTraining() {
/* 38 */     return this.ftype.needTraining();
/*    */   }
/*    */ 
/*    */   public boolean train(Dataset data) {
/* 42 */     return this.ftype.train(data);
/*    */   }
/*    */ 
/*    */   public boolean readTrainingResult() {
/* 46 */     return this.ftype.readTrainingResult();
/*    */   }
/*    */ 
/*    */   public boolean saveTrainingResult() {
/* 50 */     return this.ftype.saveTrainingResult();
/*    */   }
/*    */ 
/*    */   public int getTypeID() {
/* 54 */     return this.typeID;
/*    */   }
/*    */ 
/*    */   public void setTypeID(int typeID) {
/* 58 */     this.typeID = typeID;
/*    */   }
/*    */ 
/*    */   public boolean supportSegment() {
/* 62 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.AbstractFeatureTypeWrapper
 * JD-Core Version:    0.6.2
 */