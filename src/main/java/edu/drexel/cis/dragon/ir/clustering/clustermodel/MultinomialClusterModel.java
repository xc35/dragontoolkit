/*     */ package edu.drexel.cis.dragon.ir.clustering.clustermodel;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.DocCluster;
/*     */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.kngbase.KnowledgeBase;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ 
/*     */ public class MultinomialClusterModel extends AbstractClusterModel
/*     */ {
/*     */   private static final int SMOOTH_LAPLACIAN = 0;
/*     */   private static final int SMOOTH_BKG = 1;
/*     */   private static final int SMOOTH_TRANS = 2;
/*     */   private IndexReader indexReader;
/*     */   private IndexReader topicIndexReader;
/*     */   private DoubleSparseMatrix topicTransMatrix;
/*     */   private double[][] arrClusterModel;
/*     */   private double[] arrBkgModel;
/*     */   private double bkgCoefficient;
/*     */   private double transCoefficient;
/*     */   private int[] topicMap;
/*     */   private int[] termMap;
/*     */   private int featureNum;
/*     */   private int smoothingMethod;
/*     */ 
/*     */   public MultinomialClusterModel(int clusterNum, IndexReader indexReader)
/*     */   {
/*  36 */     super(clusterNum);
/*  37 */     this.indexReader = indexReader;
/*  38 */     this.smoothingMethod = 0;
/*  39 */     this.featureNum = indexReader.getCollection().getTermNum();
/*     */   }
/*     */ 
/*     */   public MultinomialClusterModel(int clusterNum, IndexReader indexReader, double bkgCoefficient) {
/*  43 */     super(clusterNum);
/*  44 */     this.indexReader = indexReader;
/*  45 */     this.bkgCoefficient = bkgCoefficient;
/*  46 */     this.smoothingMethod = 1;
/*  47 */     this.featureNum = indexReader.getCollection().getTermNum();
/*     */   }
/*     */ 
/*     */   public MultinomialClusterModel(int clusterNum, IndexReader indexReader, IndexReader topicIndexReader, DoubleSparseMatrix topicTransMatrix, double transCoefficient, double bkgCoefficient)
/*     */   {
/*  52 */     super(clusterNum);
/*  53 */     this.indexReader = indexReader;
/*  54 */     this.topicIndexReader = topicIndexReader;
/*  55 */     this.topicTransMatrix = topicTransMatrix;
/*  56 */     this.transCoefficient = transCoefficient;
/*  57 */     this.bkgCoefficient = bkgCoefficient;
/*  58 */     this.smoothingMethod = 2;
/*  59 */     this.featureNum = indexReader.getCollection().getTermNum();
/*  60 */     this.topicMap = new int[topicIndexReader.getCollection().getTermNum()];
/*  61 */     for (int i = 0; i < this.topicMap.length; i++)
/*  62 */       this.topicMap[i] = i;
/*  63 */     this.termMap = new int[indexReader.getCollection().getTermNum()];
/*  64 */     for (int i = 0; i < this.termMap.length; i++)
/*  65 */       this.termMap[i] = i;
/*     */   }
/*     */ 
/*     */   public MultinomialClusterModel(int clusterNum, IndexReader indexReader, IndexReader topicIndexReader, KnowledgeBase kngBase, double transCoefficient, double bkgCoefficient)
/*     */   {
/*  70 */     super(clusterNum);
/*  71 */     this.indexReader = indexReader;
/*  72 */     this.topicIndexReader = topicIndexReader;
/*  73 */     this.topicTransMatrix = kngBase.getKnowledgeMatrix();
/*  74 */     this.transCoefficient = transCoefficient;
/*  75 */     this.bkgCoefficient = bkgCoefficient;
/*  76 */     this.smoothingMethod = 2;
/*  77 */     this.featureNum = indexReader.getCollection().getTermNum();
/*     */ 
/*  81 */     this.topicMap = new int[topicIndexReader.getCollection().getTermNum()];
/*  82 */     for (int i = 0; i < this.topicMap.length; i++) {
/*  83 */       this.topicMap[i] = kngBase.getRowKeyList().search(topicIndexReader.getTermKey(i));
/*     */     }
/*     */ 
/*  87 */     this.termMap = new int[kngBase.getColumnKeyList().size()];
/*  88 */     for (int i = 0; i < this.termMap.length; i++) {
/*  89 */       IRTerm curTerm = indexReader.getIRTerm(kngBase.getColumnKeyList().search(i));
/*  90 */       if (curTerm == null)
/*  91 */         this.termMap[i] = -1;
/*     */       else
/*  93 */         this.termMap[i] = curTerm.getIndex();
/*     */     }
/*     */   }
/*     */ 
/*     */   public double getTranslationCoefficient() {
/*  98 */     return this.transCoefficient;
/*     */   }
/*     */ 
/*     */   public void setTranslationCoefficient(double transCoefficient) {
/* 102 */     this.transCoefficient = transCoefficient;
/*     */   }
/*     */ 
/*     */   public double getBackgroundCoefficient() {
/* 106 */     return this.bkgCoefficient;
/*     */   }
/*     */ 
/*     */   public void setBackgroundCoefficient(double bkgCoefficient) {
/* 110 */     this.bkgCoefficient = bkgCoefficient;
/*     */   }
/*     */ 
/*     */   public void setFeatureFilter(FeatureFilter featureFilter) {
/* 114 */     this.featureFilter = featureFilter;
/* 115 */     if (featureFilter != null)
/* 116 */       this.featureNum = featureFilter.getSelectedFeatureNum();
/*     */     else
/* 118 */       this.featureNum = this.indexReader.getCollection().getTermNum();
/*     */   }
/*     */ 
/*     */   public double getDistance(IRDoc doc, int clusterID)
/*     */   {
/* 126 */     int[] arrIndex = this.indexReader.getTermIndexList(doc.getIndex());
/* 127 */     int[] arrFreq = this.indexReader.getTermFrequencyList(doc.getIndex());
/*     */     int len;
/* 128 */     if (arrIndex == null)
/* 129 */       len = 0;
/*     */     else
/* 131 */       len = arrIndex.length;
/* 132 */     double sum = 0.0D;
/* 133 */     for (int i = 0; i < len; i++)
/*     */     {
/*     */       int newIndex;
/* 134 */       if (this.featureFilter == null)
/* 135 */         newIndex = arrIndex[i];
/*     */       else
/* 137 */         newIndex = this.featureFilter.map(arrIndex[i]);
/* 138 */       if (newIndex >= 0)
/* 139 */         sum += arrFreq[i] * this.arrClusterModel[clusterID][newIndex];
/*     */     }
/* 141 */     return -sum;
/*     */   }
/*     */ 
/*     */   public void setClusterNum(int clusterNum) {
/* 145 */     this.clusterNum = clusterNum;
/*     */   }
/*     */ 
/*     */   public void setDocCluster(DocCluster cluster)
/*     */   {
/* 154 */     if ((this.arrClusterModel == null) || (this.arrClusterModel.length != this.clusterNum) || (this.arrClusterModel[0].length != this.featureNum))
/* 155 */       this.arrClusterModel = new double[this.clusterNum][this.featureNum];
/* 156 */     int[] arrCount = new int[this.featureNum];
/* 157 */     for (int i = 0; i < cluster.getDocNum(); i++) {
/* 158 */       IRDoc curDoc = cluster.getDoc(i);
/* 159 */       int[] arrIndex = this.indexReader.getTermIndexList(curDoc.getIndex());
/* 160 */       int[] arrFreq = this.indexReader.getTermFrequencyList(curDoc.getIndex());
/*     */       int len;
/* 161 */       if (arrIndex == null)
/* 162 */         len = 0;
/*     */       else
/* 164 */         len = arrIndex.length;
/* 165 */       for (int j = 0; j < len; j++)
/*     */       {
/*     */         int newIndex;
/* 166 */         if (this.featureFilter == null)
/* 167 */           newIndex = arrIndex[j];
/*     */         else
/* 169 */           newIndex = this.featureFilter.map(arrIndex[j]);
/* 170 */         if (newIndex >= 0) {
/* 171 */           arrCount[newIndex] += arrFreq[j];
/*     */         }
/*     */       }
/*     */     }
/* 175 */     if (this.smoothingMethod == 0)
/* 176 */       laplacianSmoothing(arrCount, cluster.getClusterID());
/* 177 */     else if (this.smoothingMethod == 1)
/* 178 */       backgroundSmoothing(arrCount, cluster.getClusterID());
/*     */     else
/* 180 */       translationSmoothing(arrCount, computeTranslationModel(cluster), cluster.getClusterID());
/*     */   }
/*     */ 
/*     */   private void translationSmoothing(int[] arrCount, double[] arrTransModel, int clusterID)
/*     */   {
/* 189 */     if ((this.arrBkgModel == null) || (this.arrBkgModel.length != this.featureNum))
/* 190 */       this.arrBkgModel = getBackgroundModel(this.indexReader);
/* 191 */     double sum = getSummation(arrCount);
/* 192 */     double a = this.transCoefficient;
/* 193 */     double b = (1.0D - this.bkgCoefficient) * (1.0D - this.transCoefficient) / sum;
/* 194 */     double c = this.bkgCoefficient * (1.0D - this.transCoefficient);
/*     */ 
/* 196 */     for (int i = 0; i < this.featureNum; i++)
/* 197 */       this.arrClusterModel[clusterID][i] = Math.log(arrTransModel[i] * a + arrCount[i] * b + this.arrBkgModel[i] * c);
/*     */   }
/*     */ 
/*     */   private double[] computeTranslationModel(DocCluster cluster)
/*     */   {
/* 208 */     int topicNum = this.topicIndexReader.getCollection().getTermNum();
/* 209 */     int[] arrCount = new int[topicNum];
/* 210 */     int termNum = this.indexReader.getCollection().getTermNum();
/* 211 */     int docNum = this.topicIndexReader.getCollection().getDocNum();
/* 212 */     for (int i = 0; i < cluster.getDocNum(); i++) {
/* 213 */       IRDoc curDoc = cluster.getDoc(i);
/* 214 */       if (curDoc.getIndex() < docNum)
/*     */       {
/* 216 */         int[] arrIndex = this.topicIndexReader.getTermIndexList(curDoc.getIndex());
/* 217 */         int[] arrFreq = this.topicIndexReader.getTermFrequencyList(curDoc.getIndex());
/* 218 */         if (arrIndex != null)
/*     */         {
/* 220 */           for (int j = 0; j < arrIndex.length; j++)
/* 221 */             arrCount[arrIndex[j]] += arrFreq[j];
/*     */         }
/*     */       }
/*     */     }
/* 225 */     for (int i = 0; i < this.topicMap.length; i++) {
/* 226 */       int topicIndex = this.topicMap[i];
/* 227 */       if (topicIndex < 0)
/* 228 */         arrCount[i] = 0;
/* 229 */       else if (topicIndex >= this.topicTransMatrix.rows())
/* 230 */         arrCount[i] = 0;
/* 231 */       else if (this.topicTransMatrix.getNonZeroNumInRow(topicIndex) <= 0) {
/* 232 */         arrCount[i] = 0;
/*     */       }
/*     */     }
/*     */ 
/* 236 */     double sum = MathUtil.sumArray(arrCount);
/* 237 */     double[] arrModel = new double[termNum];
/* 238 */     for (int i = 0; i < topicNum; i++) {
/* 239 */       if (arrCount[i] > 0)
/*     */       {
/* 241 */         int topicIndex = this.topicMap[i];
/* 242 */         double rate = arrCount[i] / sum;
/* 243 */         int[] arrIndex = this.topicTransMatrix.getNonZeroColumnsInRow(topicIndex);
/* 244 */         double[] arrScore = this.topicTransMatrix.getNonZeroDoubleScoresInRow(topicIndex);
/* 245 */         for (int j = 0; j < arrIndex.length; j++) {
/* 246 */           int termIndex = this.termMap[arrIndex[j]];
/* 247 */           if (termIndex >= 0) {
/* 248 */             arrModel[termIndex] += rate * arrScore[j];
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 253 */     if (arrModel.length == this.featureFilter.getSelectedFeatureNum()) {
/* 254 */       return arrModel;
/*     */     }
/* 256 */     double[] arrSelectedModel = new double[this.featureFilter.getSelectedFeatureNum()];
/* 257 */     sum = 0.0D;
/* 258 */     for (int i = 0; i < arrModel.length; i++) {
/* 259 */       int termIndex = this.featureFilter.map(i);
/* 260 */       if (termIndex >= 0) {
/* 261 */         sum += arrModel[i];
/* 262 */         arrSelectedModel[termIndex] = arrModel[i];
/*     */       }
/*     */     }
/* 265 */     for (int i = 0; i < arrSelectedModel.length; i++)
/* 266 */       arrSelectedModel[i] /= sum;
/* 267 */     return arrSelectedModel;
/*     */   }
/*     */ 
/*     */   private void backgroundSmoothing(int[] arrCount, int clusterID)
/*     */   {
/* 274 */     if ((this.arrBkgModel == null) || (this.arrBkgModel.length != this.featureNum))
/* 275 */       this.arrBkgModel = getBackgroundModel(this.indexReader);
/* 276 */     double sum = getSummation(arrCount);
/* 277 */     for (int i = 0; i < this.featureNum; i++)
/* 278 */       this.arrClusterModel[clusterID][i] = Math.log(arrCount[i] / sum * (1.0D - this.bkgCoefficient) + this.bkgCoefficient * this.arrBkgModel[i]);
/*     */   }
/*     */ 
/*     */   private void laplacianSmoothing(int[] arrCount, int clusterID)
/*     */   {
/* 285 */     double sum = getSummation(arrCount) + this.featureNum;
/* 286 */     for (int i = 0; i < this.featureNum; i++)
/* 287 */       this.arrClusterModel[clusterID][i] = Math.log((arrCount[i] + 1.0D) / sum);
/*     */   }
/*     */ 
/*     */   private double getSummation(int[] arrCount)
/*     */   {
/* 294 */     long sum = 0L;
/* 295 */     for (int i = 0; i < arrCount.length; i++)
/* 296 */       sum += arrCount[i];
/* 297 */     return sum;
/*     */   }
/*     */ 
/*     */   private double[] getBackgroundModel(IndexReader reader)
/*     */   {
/* 305 */     int termNum = reader.getCollection().getTermNum();
/* 306 */     double sum = 0.0D;
/* 307 */     double[] arrModel = new double[this.featureNum];
/* 308 */     for (int i = 0; i < termNum; i++)
/*     */     {
/*     */       int newIndex;
/* 309 */       if (this.featureFilter == null)
/* 310 */         newIndex = i;
/*     */       else
/* 312 */         newIndex = this.featureFilter.map(i);
/* 313 */       if (newIndex >= 0) {
/* 314 */         arrModel[newIndex] = reader.getIRTerm(i).getFrequency();
/* 315 */         sum += arrModel[newIndex];
/*     */       }
/*     */     }
/* 318 */     for (int i = 0; i < this.featureNum; i++)
/* 319 */       arrModel[i] /= sum;
/* 320 */     return arrModel;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.clustermodel.MultinomialClusterModel
 * JD-Core Version:    0.6.2
 */