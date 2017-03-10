/*     */ package edu.drexel.cis.dragon.ir.classification;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.FeatureSelector;
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.NullFeatureSelector;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.kngbase.KnowledgeBase;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleFlatDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ 
/*     */ public class SemanticNBClassifier extends NBClassifier
/*     */ {
/*     */   private IndexReader topicIndexReader;
/*     */   private DoubleSparseMatrix topicTransMatrix;
/*     */   private double transCoefficient;
/*     */   private double bkgCoefficient;
/*     */   private int[] topicMap;
/*     */   private int[] termMap;
/*     */ 
/*     */   public SemanticNBClassifier(String modelFile)
/*     */   {
/*  25 */     super(modelFile);
/*     */   }
/*     */ 
/*     */   public SemanticNBClassifier(IndexReader indexReader, double bkgCoefficient) {
/*  29 */     super(indexReader);
/*  30 */     this.topicIndexReader = null;
/*  31 */     this.topicTransMatrix = null;
/*  32 */     this.transCoefficient = 0.0D;
/*  33 */     this.bkgCoefficient = bkgCoefficient;
/*  34 */     this.featureSelector = new NullFeatureSelector();
/*     */   }
/*     */ 
/*     */   public SemanticNBClassifier(IndexReader indexReader, IndexReader topicIndexReader, DoubleSparseMatrix topicTransMatrix, double transCoefficient, double bkgCoefficient)
/*     */   {
/*  39 */     super(indexReader);
/*  40 */     this.featureSelector = new NullFeatureSelector();
/*  41 */     this.topicIndexReader = topicIndexReader;
/*  42 */     this.topicTransMatrix = topicTransMatrix;
/*  43 */     this.transCoefficient = transCoefficient;
/*  44 */     this.bkgCoefficient = bkgCoefficient;
/*  45 */     this.topicMap = new int[topicIndexReader.getCollection().getTermNum()];
/*  46 */     for (int i = 0; i < this.topicMap.length; i++)
/*  47 */       this.topicMap[i] = i;
/*  48 */     this.termMap = new int[indexReader.getCollection().getTermNum()];
/*  49 */     for (int i = 0; i < this.termMap.length; i++)
/*  50 */       this.termMap[i] = i;
/*     */   }
/*     */ 
/*     */   public SemanticNBClassifier(IndexReader indexReader, IndexReader topicIndexReader, KnowledgeBase kngBase, double transCoefficient, double bkgCoefficient)
/*     */   {
/*  55 */     super(indexReader);
/*  56 */     this.featureSelector = new NullFeatureSelector();
/*  57 */     this.topicIndexReader = topicIndexReader;
/*  58 */     this.topicTransMatrix = kngBase.getKnowledgeMatrix();
/*  59 */     this.transCoefficient = transCoefficient;
/*  60 */     this.bkgCoefficient = bkgCoefficient;
/*     */ 
/*  64 */     this.topicMap = new int[topicIndexReader.getCollection().getTermNum()];
/*  65 */     for (int i = 0; i < this.topicMap.length; i++) {
/*  66 */       this.topicMap[i] = kngBase.getRowKeyList().search(topicIndexReader.getTermKey(i));
/*     */     }
/*     */ 
/*  70 */     this.termMap = new int[kngBase.getColumnKeyList().size()];
/*  71 */     for (int i = 0; i < this.termMap.length; i++) {
/*  72 */       IRTerm curTerm = indexReader.getIRTerm(kngBase.getColumnKeyList().search(i));
/*  73 */       if (curTerm == null)
/*  74 */         this.termMap[i] = -1;
/*     */       else
/*  76 */         this.termMap[i] = curTerm.getIndex();
/*     */     }
/*     */   }
/*     */ 
/*     */   public double getTranslationCoefficient() {
/*  81 */     return this.transCoefficient;
/*     */   }
/*     */ 
/*     */   public void setTranslationCoefficient(double transCoefficient) {
/*  85 */     this.transCoefficient = transCoefficient;
/*     */   }
/*     */ 
/*     */   public double getBackgroundCoefficient() {
/*  89 */     return this.bkgCoefficient;
/*     */   }
/*     */ 
/*     */   public void setBackgroundCoefficient(double bkgCoefficient) {
/*  93 */     this.bkgCoefficient = bkgCoefficient;
/*     */   }
/*     */ 
/*     */   public void train(DocClassSet trainingDocSet)
/*     */   {
/* 104 */     if ((this.indexReader == null) && (this.sparseMatrix == null)) {
/* 105 */       return;
/*     */     }
/* 107 */     this.classPrior = getClassPrior(trainingDocSet);
/* 108 */     this.featureSelector.train(this.indexReader, trainingDocSet);
/* 109 */     this.arrLabel = new String[trainingDocSet.getClassNum()];
/* 110 */     for (int i = 0; i < trainingDocSet.getClassNum(); i++)
/* 111 */       this.arrLabel[i] = trainingDocSet.getDocClass(i).getClassName();
/* 112 */     this.model = new DoubleFlatDenseMatrix(trainingDocSet.getClassNum(), this.featureSelector.getSelectedFeatureNum());
/* 113 */     double[] bkgModel = getBackgroundModel(this.indexReader);
/* 114 */     for (int i = 0; i < trainingDocSet.getClassNum(); i++) {
/* 115 */       int classSum = 0;
/* 116 */       DocClass cur = trainingDocSet.getDocClass(i);
/* 117 */       for (int j = 0; j < cur.getDocNum(); j++) {
/* 118 */         IRDoc curDoc = cur.getDoc(j);
/* 119 */         int[] arrIndex = this.indexReader.getTermIndexList(curDoc.getIndex());
/* 120 */         int[] arrFreq = this.indexReader.getTermFrequencyList(curDoc.getIndex());
/* 121 */         for (int k = 0; k < arrIndex.length; k++) {
/* 122 */           int newTermIndex = this.featureSelector.map(arrIndex[k]);
/* 123 */           if (newTermIndex >= 0) {
/* 124 */             classSum += arrFreq[k];
/* 125 */             this.model.add(i, newTermIndex, arrFreq[k]);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 130 */       if (this.topicTransMatrix != null) {
/* 131 */         double[] transModel = computeTranslationModel(cur);
/* 132 */         double a = (1.0D - this.bkgCoefficient) * (1.0D - this.transCoefficient) / classSum;
/* 133 */         double b = (1.0D - this.transCoefficient) * this.bkgCoefficient;
/* 134 */         for (int k = 0; k < this.model.columns(); k++)
/*     */         {
/* 136 */           this.model.setDouble(i, k, Math.log(transModel[k] * this.transCoefficient + this.model.getDouble(i, k) * a + bkgModel[k] * b));
/*     */         }
/*     */       } else {
/* 139 */         double a = (1.0D - this.bkgCoefficient) / classSum;
/* 140 */         for (int k = 0; k < this.model.columns(); k++)
/*     */         {
/* 142 */           this.model.setDouble(i, k, Math.log(this.model.getDouble(i, k) * a + bkgModel[k] * this.bkgCoefficient));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private double[] computeTranslationModel(DocClass curClass)
/*     */   {
/* 155 */     int topicNum = this.topicIndexReader.getCollection().getTermNum();
/* 156 */     int[] arrCount = new int[topicNum];
/* 157 */     int termNum = this.indexReader.getCollection().getTermNum();
/* 158 */     int docNum = this.topicIndexReader.getCollection().getDocNum();
/* 159 */     for (int i = 0; i < curClass.getDocNum(); i++) {
/* 160 */       IRDoc curDoc = curClass.getDoc(i);
/* 161 */       if (curDoc.getIndex() < docNum)
/*     */       {
/* 163 */         int[] arrIndex = this.topicIndexReader.getTermIndexList(curDoc.getIndex());
/* 164 */         int[] arrFreq = this.topicIndexReader.getTermFrequencyList(curDoc.getIndex());
/* 165 */         if (arrIndex != null)
/*     */         {
/* 167 */           for (int j = 0; j < arrIndex.length; j++)
/* 168 */             arrCount[arrIndex[j]] += arrFreq[j];
/*     */         }
/*     */       }
/*     */     }
/* 172 */     for (int i = 0; i < this.topicMap.length; i++) {
/* 173 */       int topicIndex = this.topicMap[i];
/* 174 */       if (topicIndex < 0)
/* 175 */         arrCount[i] = 0;
/* 176 */       else if (topicIndex >= this.topicTransMatrix.rows())
/* 177 */         arrCount[i] = 0;
/* 178 */       else if (this.topicTransMatrix.getNonZeroNumInRow(topicIndex) <= 0) {
/* 179 */         arrCount[i] = 0;
/*     */       }
/*     */     }
/*     */ 
/* 183 */     double sum = MathUtil.sumArray(arrCount);
/* 184 */     double[] arrModel = new double[termNum];
/* 185 */     for (int i = 0; i < topicNum; i++) {
/* 186 */       if (arrCount[i] > 0)
/*     */       {
/* 188 */         int topicIndex = this.topicMap[i];
/* 189 */         double rate = arrCount[i] / sum;
/* 190 */         int[] arrIndex = this.topicTransMatrix.getNonZeroColumnsInRow(topicIndex);
/* 191 */         double[] arrScore = this.topicTransMatrix.getNonZeroDoubleScoresInRow(topicIndex);
/* 192 */         for (int j = 0; j < arrIndex.length; j++) {
/* 193 */           int termIndex = this.termMap[arrIndex[j]];
/* 194 */           if (termIndex >= 0) {
/* 195 */             arrModel[termIndex] += rate * arrScore[j];
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 200 */     if (arrModel.length == this.featureSelector.getSelectedFeatureNum()) {
/* 201 */       return arrModel;
/*     */     }
/* 203 */     double[] arrSelectedModel = new double[this.featureSelector.getSelectedFeatureNum()];
/* 204 */     sum = 0.0D;
/* 205 */     for (int i = 0; i < arrModel.length; i++) {
/* 206 */       int termIndex = this.featureSelector.map(i);
/* 207 */       if (termIndex >= 0) {
/* 208 */         sum += arrModel[i];
/* 209 */         arrSelectedModel[termIndex] = arrModel[i];
/*     */       }
/*     */     }
/* 212 */     for (int i = 0; i < arrSelectedModel.length; i++)
/* 213 */       arrSelectedModel[i] /= sum;
/* 214 */     return arrSelectedModel;
/*     */   }
/*     */ 
/*     */   private double[] getBackgroundModel(IndexReader reader)
/*     */   {
/* 222 */     int termNum = reader.getCollection().getTermNum();
/* 223 */     int featureNum = this.featureSelector.getSelectedFeatureNum();
/* 224 */     double sum = 0.0D;
/* 225 */     double[] arrModel = new double[featureNum];
/* 226 */     for (int i = 0; i < termNum; i++) {
/* 227 */       int newIndex = this.featureSelector.map(i);
/* 228 */       if (newIndex >= 0) {
/* 229 */         arrModel[newIndex] = reader.getIRTerm(i).getFrequency();
/* 230 */         sum += arrModel[newIndex];
/*     */       }
/*     */     }
/* 233 */     for (int i = 0; i < featureNum; i++)
/* 234 */       arrModel[i] /= sum;
/* 235 */     return arrModel;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.SemanticNBClassifier
 * JD-Core Version:    0.6.2
 */