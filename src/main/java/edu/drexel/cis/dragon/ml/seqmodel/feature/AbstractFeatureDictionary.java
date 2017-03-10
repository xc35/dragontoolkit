/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ public abstract class AbstractFeatureDictionary
/*    */ {
/*    */   protected int stateNum;
/*    */   protected int[][] cntsArray;
/*    */   protected int[] cntsOverAllState;
/*    */   protected int[] cntsOverAllFeature;
/*    */   protected int allTotal;
/*    */   protected boolean finalized;
/*    */ 
/*    */   public abstract int getIndex(Object paramObject);
/*    */ 
/*    */   public AbstractFeatureDictionary(int stateNum)
/*    */   {
/* 23 */     this.stateNum = stateNum;
/*    */   }
/*    */ 
/*    */   public int getStateNum() {
/* 27 */     return this.stateNum;
/*    */   }
/*    */ 
/*    */   public int getCount(Object feature)
/*    */   {
/* 33 */     int index = getIndex(feature);
/* 34 */     return index >= 0 ? this.cntsOverAllState[index] : 0;
/*    */   }
/*    */ 
/*    */   public int getCount(int featureIndex, int label) {
/* 38 */     return this.cntsArray[featureIndex][label];
/*    */   }
/*    */ 
/*    */   public int getCount(int featureIndex) {
/* 42 */     return this.cntsOverAllState[featureIndex];
/*    */   }
/*    */ 
/*    */   public int getStateCount(int state) {
/* 46 */     return this.cntsOverAllFeature[state];
/*    */   }
/*    */ 
/*    */   public int getTotalCount() {
/* 50 */     return this.allTotal;
/*    */   }
/*    */ 
/*    */   public int getNextStateWithFeature(int index, int prevLabel)
/*    */   {
/*    */     int k;
/* 56 */     if (prevLabel >= 0) {
/* 57 */       k = prevLabel + 1;
/*    */     }
/*    */ 
/* 60 */     for ( k = 0; 
/* 61 */       k < this.cntsArray[index].length; k++) {
/* 62 */       if (this.cntsArray[index][k] > 0) {
/* 63 */         return k;
/*    */       }
/*    */     }
/* 66 */     return -1;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.AbstractFeatureDictionary
 * JD-Core Version:    0.6.2
 */