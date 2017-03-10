/*     */ package edu.drexel.cis.dragon.ir.classification;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.FeatureSelector;
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.NullFeatureSelector;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleFlatDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntRow;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class NigamActiveLearning extends NBClassifier
/*     */ {
/*     */   private IntRow[] externalUnlabeled;
/*     */   private DocClass unlabeledSet;
/*     */   private DocClass unlabeledSetBackup;
/*     */   private int externalDocOffset;
/*     */   private double convergeThreshold;
/*     */   private double unlabeledRate;
/*     */   private int runNum;
/*     */ 
/*     */   public NigamActiveLearning(String modelFile)
/*     */   {
/*  28 */     super(modelFile);
/*     */   }
/*     */ 
/*     */   public NigamActiveLearning(IndexReader indexReader, double unlabeledRate) {
/*  32 */     super(indexReader);
/*  33 */     this.externalDocOffset = indexReader.getCollection().getDocNum();
/*  34 */     this.runNum = 15;
/*  35 */     this.convergeThreshold = 0.0001D;
/*  36 */     this.unlabeledRate = unlabeledRate;
/*  37 */     this.featureSelector = new NullFeatureSelector();
/*     */   }
/*     */ 
/*     */   public void setUnlabeledData(IndexReader newIndexReader, DocClass docSet)
/*     */   {
/*  47 */     int[] termMap = getTermMap(newIndexReader, this.indexReader);
/*  48 */     this.externalUnlabeled = new IntRow[docSet.getDocNum()];
/*  49 */     this.unlabeledSet = new DocClass(0);
/*     */ 
/*  51 */     int docNum = 0;
/*  52 */     for (int i = 0; i < this.externalUnlabeled.length; i++) {
/*  53 */       IRDoc curDoc = docSet.getDoc(i);
/*  54 */       int[] arrIndex = newIndexReader.getTermIndexList(curDoc.getIndex());
/*  55 */       int[] arrFreq = newIndexReader.getTermFrequencyList(curDoc.getIndex());
/*  56 */       if (arrIndex != null)
/*     */       {
/*  58 */         int termNum = 0;
/*  59 */         for (int j = 0; j < arrIndex.length; j++)
/*  60 */           if (termMap[arrIndex[j]] >= 0)
/*  61 */             termNum++;
/*  62 */         if (termNum != 0)
/*     */         {
/*  65 */           int[] arrNewIndex = new int[termNum];
/*  66 */           int[] arrNewFreq = new int[termNum];
/*  67 */           termNum = 0;
/*  68 */           for (int j = 0; j < arrIndex.length; j++) {
/*  69 */             int newIndex = termMap[arrIndex[j]];
/*  70 */             if (newIndex >= 0) {
/*  71 */               arrNewIndex[termNum] = newIndex;
/*  72 */               arrNewFreq[termNum] = arrFreq[j];
/*  73 */               termNum++;
/*     */             }
/*     */           }
/*  76 */           this.externalUnlabeled[docNum] = new IntRow(docNum, termNum, arrNewIndex, arrNewFreq);
/*  77 */           curDoc.setIndex(this.externalDocOffset + docNum);
/*  78 */           curDoc.setKey("external_unlabeled" + curDoc.getKey());
/*  79 */           this.unlabeledSet.addDoc(curDoc);
/*  80 */           docNum++; } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*  85 */   public void setUnlabeledData(DocClass docSet) { this.unlabeledSet = docSet;
/*  86 */     this.externalUnlabeled = null;
/*     */   }
/*     */ 
/*     */   public DocClassSet classify(DocClassSet trainingDocSet, DocClass testingDocs)
/*     */   {
/*  93 */     if ((this.indexReader == null) && (this.sparseMatrix == null)) {
/*  94 */       return null;
/*     */     }
/*  96 */     if (this.unlabeledRate > 0.0D)
/*     */     {
/*  98 */       this.unlabeledSetBackup = this.unlabeledSet;
/*  99 */       this.unlabeledSet = new DocClass(0);
/* 100 */       if (this.unlabeledSetBackup != null) {
/* 101 */         for (int i = 0; i < this.unlabeledSetBackup.getDocNum(); i++) {
/* 102 */           this.unlabeledSet.addDoc(this.unlabeledSetBackup.getDoc(i));
/*     */         }
/*     */       }
/* 105 */       ArrayList list = new ArrayList(testingDocs.getDocNum());
/* 106 */       for (int i = 0; i < testingDocs.getDocNum(); i++) {
/* 107 */         list.add(testingDocs.getDoc(i));
/*     */       }
/* 109 */       Collections.shuffle(list, new Random(10L));
/* 110 */       int num = (int)(this.unlabeledRate * list.size());
/* 111 */       for (int i = 0; i < num; i++) {
/* 112 */         this.unlabeledSet.addDoc((IRDoc)list.get(i));
/*     */       }
/* 114 */       train(trainingDocSet);
/*     */ 
/* 116 */       this.unlabeledSet.removeAll();
/* 117 */       this.unlabeledSet = this.unlabeledSetBackup;
/*     */     }
/*     */     else {
/* 120 */       train(trainingDocSet);
/* 121 */     }return classify(testingDocs);
/*     */   }
/*     */ 
/*     */   public void train(DocClassSet trainingDocSet)
/*     */   {
/* 132 */     if ((this.indexReader == null) && (this.sparseMatrix == null)) {
/* 133 */       return;
/*     */     }
/* 135 */     this.classNum = trainingDocSet.getClassNum();
/* 136 */     this.arrLabel = new String[this.classNum];
/* 137 */     for (int i = 0; i < this.classNum; i++) {
/* 138 */       this.arrLabel[i] = trainingDocSet.getDocClass(i).getClassName();
/*     */     }
/*     */ 
/* 141 */     eStep(trainingDocSet);
/* 142 */     double prevProb = 0.0D;
/* 143 */     int curRun = 0;
/* 144 */     double prob = -1.797693134862316E+38D;
/*     */ 
/* 146 */     while ((Math.abs(prob - prevProb) > this.convergeThreshold) && (curRun < this.runNum)) {
/* 147 */       prevProb = prob;
/* 148 */       prob = 0.0D;
/*     */ 
/* 151 */       DocClassSet classifiedUnlabeledSet = classify(this.unlabeledSet);
/*     */ 
/* 154 */       for (int i = 0; i < trainingDocSet.getClassNum(); i++) {
/* 155 */         DocClass cur = trainingDocSet.getDocClass(i);
/* 156 */         for (int j = 0; j < cur.getDocNum(); j++) {
/* 157 */           IRDoc curDoc = cur.getDoc(j);
/* 158 */           prob += curDoc.getWeight();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 163 */       for (int i = 0; i < trainingDocSet.getClassNum(); i++) {
/* 164 */         DocClass cur = trainingDocSet.getDocClass(i);
/* 165 */         for (int j = 0; j < cur.getDocNum(); j++) {
/* 166 */           IRDoc curDoc = cur.getDoc(j);
/* 167 */           double docProb = this.classPrior.get(i);
/* 168 */           int[] arrIndex = this.indexReader.getTermIndexList(curDoc.getIndex());
/* 169 */           int[] arrFreq = this.indexReader.getTermFrequencyList(curDoc.getIndex());
/* 170 */           for (int k = 0; k < arrIndex.length; k++) {
/* 171 */             int newTermIndex = this.featureSelector.map(arrIndex[k]);
/* 172 */             if (newTermIndex >= 0)
/* 173 */               docProb += arrFreq[k] * this.model.getDouble(i, newTermIndex);
/*     */           }
/* 175 */           prob += docProb;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 180 */       DocClassSet newTrainingSet = new DocClassSet(trainingDocSet.getClassNum());
/* 181 */       for (int i = 0; i < trainingDocSet.getClassNum(); i++) {
/* 182 */         DocClass cur = trainingDocSet.getDocClass(i);
/* 183 */         for (int j = 0; j < cur.getDocNum(); j++)
/* 184 */           newTrainingSet.addDoc(i, cur.getDoc(j));
/*     */       }
/* 186 */       for (int i = 0; i < classifiedUnlabeledSet.getClassNum(); i++) {
/* 187 */         DocClass cur = classifiedUnlabeledSet.getDocClass(i);
/* 188 */         for (int j = 0; j < cur.getDocNum(); j++) {
/* 189 */           newTrainingSet.addDoc(i, cur.getDoc(j));
/*     */         }
/*     */       }
/* 192 */       eStep(newTrainingSet);
/*     */ 
/* 194 */       curRun++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int classify(IRDoc curDoc)
/*     */   {
/*     */     int[] arrFreq;
/*     */     int[] arrIndex;
/* 203 */     if (curDoc.getKey().startsWith("external_unlabeled"))
/*     */     {
/* 205 */       arrIndex = this.externalUnlabeled[(curDoc.getIndex() - this.externalDocOffset)].getNonZeroColumns();
/* 206 */       arrFreq = this.externalUnlabeled[(curDoc.getIndex() - this.externalDocOffset)].getNonZeroIntScores();
/*     */     }
/*     */     else {
/* 209 */       arrIndex = this.indexReader.getTermIndexList(curDoc.getIndex());
/* 210 */       arrFreq = this.indexReader.getTermFrequencyList(curDoc.getIndex());
/*     */     }
/* 212 */     IntRow row = new IntRow(0, arrIndex.length, arrIndex, arrFreq);
/* 213 */     int label = classify(row);
/* 214 */     curDoc.setWeight(this.lastClassProb.get(label));
/* 215 */     return label;
/*     */   }
/*     */ 
/*     */   private void eStep(DocClassSet trainingDocSet)
/*     */   {
/* 231 */     this.classPrior = getClassPrior(trainingDocSet);
/* 232 */     this.featureSelector.train(this.indexReader, trainingDocSet);
/* 233 */     this.model = new DoubleFlatDenseMatrix(trainingDocSet.getClassNum(), this.featureSelector.getSelectedFeatureNum());
/* 234 */     this.model.assign(1.0D);
/* 235 */     for (int i = 0; i < trainingDocSet.getClassNum(); i++) {
/* 236 */       int classSum = this.featureSelector.getSelectedFeatureNum();
/* 237 */       DocClass cur = trainingDocSet.getDocClass(i);
/* 238 */       for (int j = 0; j < cur.getDocNum(); j++) {
/* 239 */         IRDoc curDoc = cur.getDoc(j);
/*     */         int[] arrFreq;
/*     */         int[] arrIndex;
/* 240 */         if (curDoc.getKey().startsWith("external_unlabeled"))
/*     */         {
/* 242 */           arrIndex = this.externalUnlabeled[(curDoc.getIndex() - this.externalDocOffset)].getNonZeroColumns();
/* 243 */           arrFreq = this.externalUnlabeled[(curDoc.getIndex() - this.externalDocOffset)].getNonZeroIntScores();
/*     */         }
/*     */         else {
/* 246 */           arrIndex = this.indexReader.getTermIndexList(curDoc.getIndex());
/* 247 */           arrFreq = this.indexReader.getTermFrequencyList(curDoc.getIndex());
/*     */         }
/* 249 */         for (int k = 0; k < arrIndex.length; k++) {
/* 250 */           int newTermIndex = this.featureSelector.map(arrIndex[k]);
/* 251 */           if (newTermIndex >= 0) {
/* 252 */             classSum += arrFreq[k];
/* 253 */             this.model.add(i, newTermIndex, arrFreq[k]);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 258 */       double rate = 1.0D / classSum;
/* 259 */       for (int k = 0; k < this.model.columns(); k++)
/* 260 */         this.model.setDouble(i, k, Math.log(this.model.getDouble(i, k) * rate));
/*     */     }
/*     */   }
/*     */ 
/*     */   private int[] getTermMap(IndexReader src, IndexReader dest)
/*     */   {
/* 269 */     int[] termMap = new int[src.getCollection().getTermNum()];
/* 270 */     for (int i = 0; i < termMap.length; i++) {
/* 271 */       IRTerm irTerm = dest.getIRTerm(src.getTermKey(i));
/* 272 */       if (irTerm != null)
/* 273 */         termMap[i] = irTerm.getIndex();
/*     */       else
/* 275 */         termMap[i] = -1;
/*     */     }
/* 277 */     return termMap;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.NigamActiveLearning
 * JD-Core Version:    0.6.2
 */