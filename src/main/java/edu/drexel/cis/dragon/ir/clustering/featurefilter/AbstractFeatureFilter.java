/*    */ package edu.drexel.cis.dragon.ir.clustering.featurefilter;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ import edu.drexel.cis.dragon.matrix.DenseMatrix;
/*    */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*    */ import edu.drexel.cis.dragon.util.MathUtil;
/*    */ 
/*    */ public abstract class AbstractFeatureFilter
/*    */   implements FeatureFilter
/*    */ {
/*    */   protected int[] featureMap;
/*    */   protected int selectedFeatureNum;
/*    */ 
/*    */   protected abstract int[] getSelectedFeatures(IndexReader paramIndexReader, IRDoc[] paramArrayOfIRDoc);
/*    */ 
/*    */   protected int[] getSelectedFeatures(SparseMatrix doctermMatrix, IRDoc[] docSet)
/*    */   {
/* 26 */     int[] featureMap = new int[doctermMatrix.columns()];
/* 27 */     for (int i = 0; i < featureMap.length; i++)
/* 28 */       featureMap[i] = i;
/* 29 */     return featureMap;
/*    */   }
/*    */ 
/*    */   protected int[] getSelectedFeatures(DenseMatrix doctermMatrix, IRDoc[] docSet)
/*    */   {
/* 35 */     int[] featureMap = new int[doctermMatrix.columns()];
/* 36 */     for (int i = 0; i < featureMap.length; i++)
/* 37 */       featureMap[i] = i;
/* 38 */     return featureMap;
/*    */   }
/*    */ 
/*    */   public void initialize(IndexReader indexReader, IRDoc[] docSet) {
/* 42 */     setSelectedFeatures(getSelectedFeatures(indexReader, docSet));
/*    */   }
/*    */ 
/*    */   public void initialize(SparseMatrix doctermMatrix, IRDoc[] docSet) {
/* 46 */     setSelectedFeatures(getSelectedFeatures(doctermMatrix, docSet));
/*    */   }
/*    */ 
/*    */   public void initialize(DenseMatrix doctermMatrix, IRDoc[] docSet)
/*    */   {
/* 51 */     setSelectedFeatures(getSelectedFeatures(doctermMatrix, docSet));
/*    */   }
/*    */ 
/*    */   protected void setSelectedFeatures(int[] selectedFeatures)
/*    */   {
/* 57 */     if (selectedFeatures == null)
/* 58 */       return;
/* 59 */     int oldFeatureNum = selectedFeatures[(selectedFeatures.length - 1)] + 1;
/* 60 */     this.featureMap = new int[oldFeatureNum];
/* 61 */     MathUtil.initArray(this.featureMap, -1);
/* 62 */     for (int i = 0; i < selectedFeatures.length; i++)
/* 63 */       this.featureMap[selectedFeatures[i]] = i;
/* 64 */     this.selectedFeatureNum = selectedFeatures.length;
/*    */   }
/*    */ 
/*    */   public boolean isSelected(int originalFeatureIndex) {
/* 68 */     if (originalFeatureIndex >= this.featureMap.length) {
/* 69 */       return false;
/*    */     }
/* 71 */     return this.featureMap[originalFeatureIndex] != -1;
/*    */   }
/*    */ 
/*    */   public int map(int originalFeatureIndex) {
/* 75 */     if (originalFeatureIndex >= this.featureMap.length) {
/* 76 */       return -1;
/*    */     }
/* 78 */     return this.featureMap[originalFeatureIndex];
/*    */   }
/*    */ 
/*    */   public int getSelectedFeatureNum() {
/* 82 */     return this.selectedFeatureNum;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.featurefilter.AbstractFeatureFilter
 * JD-Core Version:    0.6.2
 */