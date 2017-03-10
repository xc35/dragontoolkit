/*     */ package edu.drexel.cis.dragon.ir.classification.featureselection;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.classification.DocClass;
/*     */ import edu.drexel.cis.dragon.ir.classification.DocClassSet;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.DenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntFlatDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class AbstractFeatureSelector
/*     */   implements FeatureSelector, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected int[] featureMap;
/*     */   protected int selectedFeatureNum;
/*     */ 
/*     */   protected abstract int[] getSelectedFeatures(IndexReader paramIndexReader, DocClassSet paramDocClassSet);
/*     */ 
/*     */   protected abstract int[] getSelectedFeatures(SparseMatrix paramSparseMatrix, DocClassSet paramDocClassSet);
/*     */ 
/*     */   protected int[] getSelectedFeatures(DenseMatrix doctermMatrix, DocClassSet trainingSet)
/*     */   {
/*  29 */     int[] featureMap = new int[doctermMatrix.columns()];
/*  30 */     for (int i = 0; i < featureMap.length; i++)
/*  31 */       featureMap[i] = i;
/*  32 */     return featureMap;
/*     */   }
/*     */ 
/*     */   public void train(IndexReader indexReader, DocClassSet trainingSet) {
/*  36 */     setSelectedFeatures(getSelectedFeatures(indexReader, trainingSet));
/*     */   }
/*     */ 
/*     */   public void train(SparseMatrix doctermMatrix, DocClassSet trainingSet) {
/*  40 */     setSelectedFeatures(getSelectedFeatures(doctermMatrix, trainingSet));
/*     */   }
/*     */ 
/*     */   public void train(DenseMatrix doctermMatrix, DocClassSet trainingSet) {
/*  44 */     setSelectedFeatures(getSelectedFeatures(doctermMatrix, trainingSet));
/*     */   }
/*     */ 
/*     */   public void setSelectedFeatures(int[] selectedFeatures)
/*     */   {
/*  50 */     if (selectedFeatures == null)
/*  51 */       return;
/*  52 */     int oldFeatureNum = selectedFeatures[(selectedFeatures.length - 1)] + 1;
/*  53 */     this.featureMap = new int[oldFeatureNum];
/*  54 */     MathUtil.initArray(this.featureMap, -1);
/*  55 */     for (int i = 0; i < selectedFeatures.length; i++)
/*  56 */       this.featureMap[selectedFeatures[i]] = i;
/*  57 */     this.selectedFeatureNum = selectedFeatures.length;
/*     */   }
/*     */ 
/*     */   public boolean isSelected(int originalFeatureIndex) {
/*  61 */     if (originalFeatureIndex >= this.featureMap.length) {
/*  62 */       return false;
/*     */     }
/*  64 */     return this.featureMap[originalFeatureIndex] != -1;
/*     */   }
/*     */ 
/*     */   public int map(int originalFeatureIndex) {
/*  68 */     if (originalFeatureIndex >= this.featureMap.length) {
/*  69 */       return -1;
/*     */     }
/*  71 */     return this.featureMap[originalFeatureIndex];
/*     */   }
/*     */ 
/*     */   public int getSelectedFeatureNum() {
/*  75 */     return this.selectedFeatureNum;
/*     */   }
/*     */ 
/*     */   protected DoubleVector getClassPrior(DocClassSet docSet)
/*     */   {
/*  82 */     int sum = docSet.getClassNum();
/*  83 */     DoubleVector vector = new DoubleVector(docSet.getClassNum());
/*  84 */     vector.assign(0.0D);
/*  85 */     for (int i = 0; i < docSet.getClassNum(); i++) {
/*  86 */       vector.set(i, docSet.getDocClass(i).getDocNum());
/*  87 */       sum += docSet.getDocClass(i).getDocNum();
/*     */     }
/*  89 */     for (int i = 0; i < docSet.getClassNum(); i++)
/*  90 */       vector.set(i, vector.get(i) / sum);
/*  91 */     return vector;
/*     */   }
/*     */ 
/*     */   protected int[] getTermDocFrequency(SparseMatrix matrix, DocClassSet trainingSet)
/*     */   {
/* 100 */     int[] arrStat = new int[matrix.columns()];
/* 101 */     for (int i = 0; i < trainingSet.getClassNum(); i++) {
/* 102 */       DocClass curClass = trainingSet.getDocClass(i);
/* 103 */       for (int j = 0; j < curClass.getDocNum(); j++) {
/* 104 */         IRDoc curDoc = curClass.getDoc(j);
/* 105 */         int[] arrIndex = matrix.getNonZeroColumnsInRow(curDoc.getIndex());
/* 106 */         if ((arrIndex != null) && (arrIndex.length != 0)) {
/* 107 */           for (int k = 0; k < arrIndex.length; k++)
/* 108 */             arrStat[arrIndex[k]] += 1;
/*     */         }
/*     */       }
/*     */     }
/* 112 */     return arrStat;
/*     */   }
/*     */ 
/*     */   protected IntDenseMatrix getTermDistribution(IndexReader indexReader, DocClassSet trainingSet)
/*     */   {
/* 122 */     IntFlatDenseMatrix matrix = new IntFlatDenseMatrix(trainingSet.getClassNum(), indexReader.getCollection().getTermNum());
/* 123 */     matrix.assign(0);
/* 124 */     for (int i = 0; i < trainingSet.getClassNum(); i++) {
/* 125 */       DocClass curClass = trainingSet.getDocClass(i);
/* 126 */       for (int j = 0; j < curClass.getDocNum(); j++) {
/* 127 */         IRDoc curDoc = curClass.getDoc(j);
/* 128 */         int[] arrIndex = indexReader.getTermIndexList(curDoc.getIndex());
/* 129 */         if ((arrIndex != null) && (arrIndex.length != 0)) {
/* 130 */           for (int k = 0; k < arrIndex.length; k++)
/* 131 */             matrix.add(i, arrIndex[k], 1);
/*     */         }
/*     */       }
/*     */     }
/* 135 */     return matrix;
/*     */   }
/*     */ 
/*     */   protected IntDenseMatrix getTermDistribution(SparseMatrix doctermMatrix, DocClassSet trainingSet)
/*     */   {
/* 145 */     IntFlatDenseMatrix matrix = new IntFlatDenseMatrix(trainingSet.getClassNum(), doctermMatrix.columns());
/* 146 */     matrix.assign(0);
/* 147 */     for (int i = 0; i < trainingSet.getClassNum(); i++) {
/* 148 */       DocClass curClass = trainingSet.getDocClass(i);
/* 149 */       for (int j = 0; j < curClass.getDocNum(); j++) {
/* 150 */         IRDoc curDoc = curClass.getDoc(j);
/* 151 */         int[] arrIndex = doctermMatrix.getNonZeroColumnsInRow(curDoc.getIndex());
/* 152 */         if ((arrIndex != null) && (arrIndex.length != 0)) {
/* 153 */           for (int k = 0; k < arrIndex.length; k++)
/* 154 */             matrix.add(i, arrIndex[k], 1);
/*     */         }
/*     */       }
/*     */     }
/* 158 */     return matrix;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.featureselection.AbstractFeatureSelector
 * JD-Core Version:    0.6.2
 */