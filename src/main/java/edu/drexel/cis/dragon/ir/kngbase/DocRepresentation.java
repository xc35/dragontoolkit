/*     */ package edu.drexel.cis.dragon.ir.kngbase;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleFlatSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSuperSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class DocRepresentation
/*     */ {
/*     */   private IndexReader indexReader;
/*     */   private int[] termMap;
/*     */   private boolean showMessage;
/*     */ 
/*     */   public DocRepresentation(IndexReader indexReader)
/*     */   {
/*  25 */     this.indexReader = indexReader;
/*  26 */     this.showMessage = true;
/*     */   }
/*     */ 
/*     */   public DocRepresentation(IndexReader indexReader, int[] termMap) {
/*  30 */     this.indexReader = indexReader;
/*  31 */     this.termMap = termMap;
/*  32 */     this.showMessage = true;
/*     */   }
/*     */ 
/*     */   public void setMessageOption(boolean showMessage) {
/*  36 */     this.showMessage = showMessage;
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix genModelMatrix(IndexReader signatureIndexReader, DoubleSparseMatrix transMatrix, double transCoefficient, double bkgCoefficient, boolean isPhraseSignature, double probThreshold, String matrixPath, String matrixKey)
/*     */   {
/*  41 */     return genModelMatrix(signatureIndexReader, null, transMatrix, transCoefficient, bkgCoefficient, isPhraseSignature, probThreshold, matrixPath, matrixKey);
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix genModelMatrix(IndexReader signatureIndexReader, int[] signatureMap, DoubleSparseMatrix transMatrix, double transCoefficient, double bkgCoefficient, boolean isPhraseSignature, double probThreshold, String matrixPath, String matrixKey)
/*     */   {
/*  50 */     String indexDir = matrixPath;
/*  51 */     File file = new File(indexDir + "/" + matrixKey + ".index");
/*  52 */     if (file.exists()) file.delete();
/*  53 */     file = new File(indexDir + "/" + matrixKey + ".matrix");
/*  54 */     if (file.exists()) file.delete();
/*  55 */     DoubleSuperSparseMatrix matrix = new DoubleSuperSparseMatrix(indexDir + "/" + matrixKey + ".index", indexDir + "/" + matrixKey + ".matrix", false, false);
/*  56 */     return genModelMatrix(signatureIndexReader, signatureMap, transMatrix, transCoefficient, bkgCoefficient, isPhraseSignature, probThreshold, matrix);
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix genModelMatrix(IndexReader signatureIndexReader, DoubleSparseMatrix transMatrix, double transCoefficient, double bkgCoefficient, boolean isPhraseSignature, double probThreshold)
/*     */   {
/*  61 */     return genModelMatrix(signatureIndexReader, null, transMatrix, transCoefficient, bkgCoefficient, isPhraseSignature, probThreshold);
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix genModelMatrix(IndexReader signatureIndexReader, int[] signatureMap, DoubleSparseMatrix transMatrix, double transCoefficient, double bkgCoefficient, boolean isPhraseSignature, double probThreshold)
/*     */   {
/*  68 */     DoubleFlatSparseMatrix matrix = new DoubleFlatSparseMatrix(false, false);
/*  69 */     return genModelMatrix(signatureIndexReader, signatureMap, transMatrix, transCoefficient, bkgCoefficient, isPhraseSignature, probThreshold, matrix);
/*     */   }
/*     */ 
/*     */   private DoubleSparseMatrix genModelMatrix(IndexReader signatureIndexReader, int[] signatureMap, DoubleSparseMatrix transMatrix, double transCoefficient, double bkgCoefficient, boolean isPhraseSignature, double probThreshold, DoubleSparseMatrix matrix)
/*     */   {
/*  82 */     int docNum = this.indexReader.getCollection().getDocNum();
/*     */     int termNum;
/*  83 */     if (this.termMap == null)
/*  84 */       termNum = this.indexReader.getCollection().getTermNum();
/*     */     else
/*  86 */       termNum = Math.max(transMatrix.columns(), MathUtil.max(this.termMap) + 1);
/*  87 */     double[] arrBkgModel = new double[this.indexReader.getCollection().getTermNum()];
/*  88 */     double[] arrTransModel = new double[termNum];
/*  89 */     double weightSum = this.indexReader.getCollection().getTermCount();
/*  90 */     for (int i = 0; i < arrBkgModel.length; i++) {
/*  91 */       arrBkgModel[i] = (this.indexReader.getIRTerm(i).getFrequency() / weightSum * (1.0D - transCoefficient) * bkgCoefficient);
/*     */     }
/*     */ 
/*  94 */     ArrayList termList = new ArrayList();
/*  95 */     int[] indexList = (int[])null;
/*  96 */     int[] freqList = (int[])null;
/*  97 */     for (int i = 0; i < docNum; i++) {
/*  98 */       if ((i > 0) && (i % 2000 == 0)) {
/*  99 */         matrix.flush();
/* 100 */         if (this.showMessage)
/* 101 */           System.out.println(new Date() + " processing doc #" + i);
/*     */       }
/* 103 */       if (this.indexReader.getDoc(i).getTermNum() > 0)
/*     */       {
/*     */         int signatureNum;
/* 107 */         if (i >= signatureIndexReader.getCollection().getDocNum()) {
/* 108 */           signatureNum = 0;
/*     */         } else {
/* 110 */           for (int j = 0; j < termNum; j++)
/* 111 */             arrTransModel[j] = 0.0D;
/* 112 */           if (isPhraseSignature) {
/* 113 */              signatureNum = signatureIndexReader.getDoc(i).getTermNum();
/* 114 */             indexList = signatureIndexReader.getTermIndexList(i);
/* 115 */             freqList = signatureIndexReader.getTermFrequencyList(i);
/*     */           } else {
/* 117 */             signatureNum = signatureIndexReader.getDoc(i).getRelationNum();
/* 118 */             indexList = signatureIndexReader.getRelationIndexList(i);
/* 119 */             freqList = signatureIndexReader.getRelationFrequencyList(i);
/*     */           }
/*     */         }
/*     */ 
/* 123 */         int usedSignature = 0;
/* 124 */         weightSum = 0.0D;
/* 125 */         for (int j = 0; j < signatureNum; j++)
/*     */         {
/*     */           int curSignatureIndex;
/* 126 */           if (signatureMap == null)
/* 127 */             curSignatureIndex = indexList[j];
/*     */           else
/* 129 */             curSignatureIndex = signatureMap[indexList[j]];
/* 130 */           if (curSignatureIndex >= transMatrix.rows()) break;
/* 131 */           int[] cols = transMatrix.getNonZeroColumnsInRow(curSignatureIndex);
/* 132 */           double[] scores = transMatrix.getNonZeroDoubleScoresInRow(curSignatureIndex);
/* 133 */           if (cols.length > 0)
/* 134 */             usedSignature++;
/* 135 */           double rate = freqList[j];
/* 136 */           weightSum += freqList[j];
/* 137 */           for (int k = 0; k < cols.length; k++) {
/* 138 */             arrTransModel[cols[k]] += scores[k] * rate;
/*     */           }
/*     */         }
/* 141 */         if (usedSignature > 0) {
/* 142 */           double rate = transCoefficient / usedSignature / weightSum;
/* 143 */           for (int j = 0; j < termNum; j++) {
/* 144 */             if (arrTransModel[j] > 0.0D) {
/* 145 */               arrTransModel[j] *= rate;
/*     */             }
/*     */           }
/*     */         }
/* 149 */         indexList = this.indexReader.getTermIndexList(i);
/* 150 */         freqList = this.indexReader.getTermFrequencyList(i);
/* 151 */         weightSum = this.indexReader.getDoc(i).getTermCount();
/* 152 */         double rate = (1.0D - transCoefficient) * (1.0D - bkgCoefficient) / weightSum;
/* 153 */         for (int j = 0; j < indexList.length; j++) {
/* 154 */           arrTransModel[map(indexList[j])] += freqList[j] * rate;
/*     */         }
/*     */ 
/* 157 */         for (int j = 0; j < arrBkgModel.length; j++) {
/* 158 */           arrTransModel[map(j)] += arrBkgModel[j];
/*     */         }
/* 160 */         if (usedSignature == 0)
/*     */         {
/* 162 */           rate = 1.0D / (1.0D - transCoefficient);
/* 163 */           for (int j = 0; j < termNum; j++) {
/* 164 */             arrTransModel[j] *= rate;
/*     */           }
/*     */         }
/* 167 */         termList.clear();
/* 168 */         weightSum = 0.0D;
/* 169 */         for (int j = 0; j < termNum; j++)
/*     */         {
/* 171 */           if (arrTransModel[j] >= probThreshold) {
/* 172 */             Token curToken = new Token(null);
/* 173 */             curToken.setIndex(j);
/* 174 */             curToken.setWeight(arrTransModel[j]);
/* 175 */             termList.add(curToken);
/* 176 */             weightSum += arrTransModel[j];
/*     */           }
/*     */         }
/* 179 */         for (int j = 0; j < termList.size(); j++) {
/* 180 */           Token curToken = (Token)termList.get(j);
/* 181 */           matrix.add(i, curToken.getIndex(), curToken.getWeight() / weightSum);
/*     */         }
/*     */       }
/*     */     }
/* 184 */     matrix.finalizeData();
/* 185 */     return matrix;
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix genTFIDFMatrix(String matrixPath, String matrixKey)
/*     */   {
/* 193 */     String indexFolder = matrixPath;
/* 194 */     File file = new File(indexFolder + "/" + matrixKey + ".index");
/* 195 */     if (file.exists()) file.delete();
/* 196 */     file = new File(indexFolder + "/" + matrixKey + ".matrix");
/* 197 */     if (file.exists()) file.delete();
/* 198 */     DoubleSuperSparseMatrix tfidfMatrix = new DoubleSuperSparseMatrix(indexFolder + "/" + matrixKey + ".index", indexFolder + "/" + matrixKey + ".matrix", false, false);
/* 199 */     return genTFIDFMatrix(tfidfMatrix);
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix genTFIDFMatrix()
/*     */   {
/* 205 */     DoubleFlatSparseMatrix matrix = new DoubleFlatSparseMatrix(false, false);
/* 206 */     return genTFIDFMatrix(matrix);
/*     */   }
/*     */ 
/*     */   private DoubleSparseMatrix genTFIDFMatrix(DoubleSparseMatrix matrix)
/*     */   {
/* 215 */     double[] arrIDF = new double[this.indexReader.getCollection().getTermNum()];
/* 216 */     double sum = this.indexReader.getCollection().getDocNum();
/* 217 */     for (int i = 0; i < arrIDF.length; i++) {
/* 218 */       arrIDF[i] = Math.log(sum / this.indexReader.getIRTerm(i).getDocFrequency());
/*     */     }
/* 220 */     int docNum = this.indexReader.getCollection().getDocNum();
/* 221 */     for (int i = 0; i < docNum; i++) {
/* 222 */       if ((i > 0) && (i % 2000 == 0)) {
/* 223 */         matrix.flush();
/* 224 */         if (this.showMessage)
/* 225 */           System.out.println(new Date() + " processing doc #" + i);
/*     */       }
/* 227 */       int[] termIndexList = this.indexReader.getTermIndexList(i);
/* 228 */       int[] termFreqList = this.indexReader.getTermFrequencyList(i);
/* 229 */       for (int j = 0; j < termIndexList.length; j++) {
/* 230 */         matrix.add(i, map(termIndexList[j]), termFreqList[j] * arrIDF[termIndexList[j]]);
/*     */       }
/*     */     }
/* 233 */     matrix.finalizeData();
/* 234 */     return matrix;
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix genNormTFMatrix(String matrixPath, String matrixKey)
/*     */   {
/* 242 */     String indexFolder = matrixPath;
/* 243 */     File file = new File(indexFolder + "/" + matrixKey + ".index");
/* 244 */     if (file.exists()) file.delete();
/* 245 */     file = new File(indexFolder + "/" + matrixKey + ".matrix");
/* 246 */     if (file.exists()) file.delete();
/* 247 */     DoubleSuperSparseMatrix matrix = new DoubleSuperSparseMatrix(indexFolder + "/" + matrixKey + ".index", indexFolder + "/" + matrixKey + ".matrix", false, false);
/* 248 */     return genNormTFMatrix(matrix);
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix genNormTFMatrix()
/*     */   {
/* 254 */     DoubleFlatSparseMatrix matrix = new DoubleFlatSparseMatrix(false, false);
/* 255 */     return genNormTFMatrix(matrix);
/*     */   }
/*     */ 
/*     */   private DoubleSparseMatrix genNormTFMatrix(DoubleSparseMatrix matrix)
/*     */   {
/* 263 */     double docNum = this.indexReader.getCollection().getDocNum();
/* 264 */     for (int i = 0; i < docNum; i++) {
/* 265 */       if ((i > 0) && (i % 2000 == 0)) {
/* 266 */         matrix.flush();
/* 267 */         if (this.showMessage)
/* 268 */           System.out.println(new Date() + " processing doc #" + i);
/*     */       }
/* 270 */       int[] termIndexList = this.indexReader.getTermIndexList(i);
/* 271 */       int[] termFreqList = this.indexReader.getTermFrequencyList(i);
/* 272 */       double sum = 0.0D;
/* 273 */       for (int j = 0; j < termIndexList.length; j++) {
/* 274 */         sum += termFreqList[j] * termFreqList[j];
/*     */       }
/* 276 */       sum = Math.sqrt(sum);
/* 277 */       for (int j = 0; j < termIndexList.length; j++) {
/* 278 */         matrix.add(i, map(termIndexList[j]), termFreqList[j] / sum);
/*     */       }
/*     */     }
/* 281 */     matrix.finalizeData();
/* 282 */     return matrix;
/*     */   }
/*     */ 
/*     */   private int map(int oldTermIndex) {
/* 286 */     if (this.termMap == null) {
/* 287 */       return oldTermIndex;
/*     */     }
/* 289 */     return this.termMap[oldTermIndex];
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.kngbase.DocRepresentation
 * JD-Core Version:    0.6.2
 */