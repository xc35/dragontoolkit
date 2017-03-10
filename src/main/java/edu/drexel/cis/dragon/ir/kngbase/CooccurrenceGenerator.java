/*     */ package edu.drexel.cis.dragon.ir.kngbase;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntSuperSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrixFactory;
/*     */ import edu.drexel.cis.dragon.nlp.Counter;
/*     */ import edu.drexel.cis.dragon.nlp.SimplePair;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class CooccurrenceGenerator
/*     */ {
/*     */   private HashMap hashMap;
/*     */   private int cacheSize;
/*     */   private int minDocFreq;
/*     */   private int maxDocFreq;
/*     */ 
/*     */   public CooccurrenceGenerator()
/*     */   {
/*  22 */     this.cacheSize = 3000000;
/*  23 */     this.hashMap = new HashMap((int)(this.cacheSize / 0.75D + 10.0D));
/*  24 */     this.minDocFreq = 1;
/*  25 */     this.maxDocFreq = 2147483647;
/*     */   }
/*     */ 
/*     */   public void setMinDocFrequency(int minDocFreq) {
/*  29 */     this.minDocFreq = minDocFreq;
/*     */   }
/*     */ 
/*     */   public int getMinDocFrequency() {
/*  33 */     return this.minDocFreq;
/*     */   }
/*     */ 
/*     */   public void setMaxDocFrequency(int maxDocFreq) {
/*  37 */     this.maxDocFreq = maxDocFreq;
/*     */   }
/*     */ 
/*     */   public int getMaxDocFrequency() {
/*  41 */     return this.maxDocFreq;
/*     */   }
/*     */ 
/*     */   public void setCacheSize(int size) {
/*  45 */     this.cacheSize = size;
/*  46 */     this.hashMap.clear();
/*  47 */     this.hashMap = new HashMap((int)(this.cacheSize / 0.75D + 10.0D));
/*     */   }
/*     */ 
/*     */   public int getCacheSize() {
/*  51 */     return this.cacheSize;
/*     */   }
/*     */ 
/*     */   public boolean generate(IntSparseMatrix doctermMatrixA, IntSparseMatrix doctermMatrixB, String matrixFolder, String matrixKey) {
/*  55 */     return generate(doctermMatrixA, null, doctermMatrixB, null, matrixFolder, matrixKey);
/*     */   }
/*     */ 
/*     */   public boolean generate(IntSparseMatrix doctermMatrixA, int[] termDocFreqA, IntSparseMatrix doctermMatrixB, int[] termDocFreqB, String matrixFolder, String matrixKey)
/*     */   {
/*     */     try
/*     */     {
/*  70 */       boolean[] usedListA = checkTermDocFrequency(doctermMatrixA.columns(), termDocFreqA);
/*  71 */       boolean[] usedListB = checkTermDocFrequency(doctermMatrixB.columns(), termDocFreqB);
/*     */ 
/*  73 */       String matrixFile = matrixFolder + "/" + matrixKey + ".matrix";
/*  74 */       String indexFile = matrixFolder + "/" + matrixKey + ".index";
/*  75 */       File file = new File(matrixFile);
/*  76 */       if (file.exists()) file.delete();
/*  77 */       file = new File(indexFile);
/*  78 */       if (file.exists()) file.delete();
/*     */ 
/*  80 */       IntSuperSparseMatrix cooccurMatrix = new IntSuperSparseMatrix(indexFile, matrixFile, false, false);
/*  81 */       SparseMatrixFactory smf = null;
/*  82 */       String tmpMatrixFile = matrixFolder + "/" + matrixKey + "tmp.matrix";
/*  83 */       String tmpIndexFile = matrixFolder + "/" + matrixKey + "tmp.index";
/*  84 */       int docNum = Math.min(doctermMatrixA.rows(), doctermMatrixB.rows());
/*  85 */       boolean isFirst = true;
/*  86 */       SimplePair.setHashCapacity(doctermMatrixA.columns());
/*  87 */       for (int i = 0; i < docNum; i++) {
/*  88 */         if (i % 1000 == 0)
/*  89 */           System.out.println(new Date() + " " + i);
/*  90 */         if (this.hashMap.size() >= this.cacheSize) {
/*  91 */           if (isFirst) {
/*  92 */             convertPairsToMatrix(this.hashMap, cooccurMatrix, false);
/*  93 */             isFirst = false;
/*  94 */             smf = new SparseMatrixFactory(matrixFolder + "/" + matrixKey + ".matrix", 4);
/*     */           } else {
/*  96 */             cooccurMatrix = new IntSuperSparseMatrix(tmpIndexFile, tmpMatrixFile, false, false);
/*  97 */             convertPairsToMatrix(this.hashMap, cooccurMatrix, false);
/*  98 */             smf.add(cooccurMatrix);
/*  99 */             cooccurMatrix.close();
/* 100 */             new File(tmpIndexFile).delete();
/* 101 */             new File(tmpMatrixFile).delete();
/*     */           }
/*     */         }
/*     */ 
/* 105 */         int[] indexListA = doctermMatrixA.getNonZeroColumnsInRow(i);
/* 106 */         int[] indexListB = doctermMatrixB.getNonZeroColumnsInRow(i);
/* 107 */         for (int j = 0; j < indexListA.length; j++) {
/* 108 */           if (usedListA[indexListA[j]] != false) {
/* 109 */             for (int k = 0; k < indexListB.length; k++)
/* 110 */               if (usedListB[indexListB[k]] != false) {
/* 111 */                 SimplePair coOccurPair = new SimplePair(-1, indexListA[j], indexListB[k]);
/* 112 */                 Counter counter = (Counter)this.hashMap.get(coOccurPair);
/* 113 */                 if (counter != null)
/* 114 */                   counter.addCount(1);
/*     */                 else
/* 116 */                   this.hashMap.put(coOccurPair, new Counter(1));
/*     */               }
/*     */           }
/*     */         }
/*     */       }
/* 121 */       if (this.hashMap.size() > 0) {
/* 122 */         if (isFirst) {
/* 123 */           convertPairsToMatrix(this.hashMap, cooccurMatrix, false);
/*     */         }
/*     */         else {
/* 126 */           cooccurMatrix = new IntSuperSparseMatrix(tmpIndexFile, tmpMatrixFile, false, false);
/* 127 */           convertPairsToMatrix(this.hashMap, cooccurMatrix, false);
/* 128 */           smf.add(cooccurMatrix);
/* 129 */           cooccurMatrix.close();
/* 130 */           new File(tmpIndexFile).delete();
/* 131 */           new File(tmpMatrixFile).delete();
/* 132 */           smf.genIndexFile(matrixFolder + "/" + matrixKey + ".index");
/*     */         }
/*     */       }
/* 135 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 138 */       e.printStackTrace();
/* 139 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean generate(IntSparseMatrix doctermMatrix, String matrixFolder, String matrixKey)
/*     */   {
/* 144 */     return generate(doctermMatrix, new int[0], matrixFolder, matrixKey);
/*     */   }
/*     */ 
/*     */   public boolean generate(IntSparseMatrix doctermMatrix, int[] termDocFreq, String matrixFolder, String matrixKey)
/*     */   {
/*     */     try
/*     */     {
/* 158 */       boolean[] usedList = checkTermDocFrequency(doctermMatrix.columns(), termDocFreq);
/*     */ 
/* 160 */       String matrixFile = matrixFolder + "/" + matrixKey + ".matrix";
/* 161 */       String indexFile = matrixFolder + "/" + matrixKey + ".index";
/* 162 */       File file = new File(matrixFile);
/* 163 */       if (file.exists()) file.delete();
/* 164 */       file = new File(indexFile);
/* 165 */       if (file.exists()) file.delete();
/*     */ 
/* 167 */       IntSuperSparseMatrix cooccurMatrix = new IntSuperSparseMatrix(indexFile, matrixFile, false, false);
/* 168 */       SparseMatrixFactory smf = null;
/* 169 */       String tmpMatrixFile = matrixFolder + "/" + matrixKey + "tmp.matrix";
/* 170 */       String tmpIndexFile = matrixFolder + "/" + matrixKey + "tmp.index";
/* 171 */       boolean isFirst = true;
/* 172 */       SimplePair.setHashCapacity(doctermMatrix.rows());
/* 173 */       for (int i = 0; i < doctermMatrix.rows(); i++) {
/* 174 */         if (i % 1000 == 0)
/* 175 */           System.out.println(new Date() + " " + i);
/* 176 */         if (this.hashMap.size() >= this.cacheSize) {
/* 177 */           if (isFirst) {
/* 178 */             convertPairsToMatrix(this.hashMap, cooccurMatrix, true);
/* 179 */             isFirst = false;
/* 180 */             smf = new SparseMatrixFactory(matrixFolder + "/" + matrixKey + ".matrix", 4);
/*     */           } else {
/* 182 */             cooccurMatrix = new IntSuperSparseMatrix(tmpIndexFile, tmpMatrixFile, false, false);
/* 183 */             convertPairsToMatrix(this.hashMap, cooccurMatrix, true);
/* 184 */             smf.add(cooccurMatrix);
/* 185 */             cooccurMatrix.close();
/* 186 */             new File(tmpIndexFile).delete();
/* 187 */             new File(tmpMatrixFile).delete();
/*     */           }
/*     */         }
/*     */ 
/* 191 */         int[] indexList = doctermMatrix.getNonZeroColumnsInRow(i);
/* 192 */         for (int j = 0; j < indexList.length; j++) {
/* 193 */           if (usedList[indexList[j]] != false)
/*     */           {
/* 195 */             for (int k = j + 1; k < indexList.length; k++)
/* 196 */               if (usedList[indexList[k]] != false)
/*     */               {
/* 198 */                 SimplePair coOccurPair = new SimplePair(-1, indexList[j], indexList[k]);
/* 199 */                 Counter counter = (Counter)this.hashMap.get(coOccurPair);
/* 200 */                 if (counter != null)
/* 201 */                   counter.addCount(1);
/*     */                 else
/* 203 */                   this.hashMap.put(coOccurPair, new Counter(1));
/*     */               }
/*     */           }
/*     */         }
/*     */       }
/* 208 */       if (this.hashMap.size() > 0) {
/* 209 */         if (isFirst) {
/* 210 */           convertPairsToMatrix(this.hashMap, cooccurMatrix, true);
/*     */         }
/*     */         else {
/* 213 */           cooccurMatrix = new IntSuperSparseMatrix(tmpIndexFile, tmpMatrixFile, false, false);
/* 214 */           convertPairsToMatrix(this.hashMap, cooccurMatrix, true);
/* 215 */           smf.add(cooccurMatrix);
/* 216 */           cooccurMatrix.close();
/* 217 */           new File(tmpIndexFile).delete();
/* 218 */           new File(tmpMatrixFile).delete();
/* 219 */           smf.genIndexFile(matrixFolder + "/" + matrixKey + ".index");
/*     */         }
/*     */       }
/* 222 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 225 */       e.printStackTrace();
/* 226 */     }return false;
/*     */   }
/*     */ 
/*     */   private void convertPairsToMatrix(HashMap hashMap, IntSuperSparseMatrix matrix, boolean symmetric)
/*     */   {
/* 236 */     if (symmetric)
/* 237 */       matrix.setFlushInterval(2 * hashMap.size() + 1);
/*     */     else
/* 239 */       matrix.setFlushInterval(hashMap.size() + 1);
/* 240 */     Set keySet = hashMap.keySet();
/* 241 */     Iterator iterator = keySet.iterator();
/* 242 */     while (iterator.hasNext()) {
/* 243 */       SimplePair coOccurPair = (SimplePair)iterator.next();
/* 244 */       Counter counter = (Counter)hashMap.get(coOccurPair);
/* 245 */       matrix.add(coOccurPair.getFirstElement(), coOccurPair.getSecondElement(), counter.getCount());
/* 246 */       if ((symmetric) && (coOccurPair.getFirstElement() != coOccurPair.getSecondElement()))
/* 247 */         matrix.add(coOccurPair.getSecondElement(), coOccurPair.getFirstElement(), counter.getCount());
/*     */     }
/* 249 */     matrix.finalizeData();
/* 250 */     hashMap.clear();
/*     */   }
/*     */ 
/*     */   private boolean[] checkTermDocFrequency(int termNum, int[] arrTermDocFreq)
/*     */   {
/* 257 */     boolean[] usedList = new boolean[termNum];
/* 258 */     for (int i = 0; i < termNum; i++)
/* 259 */       usedList[i] = true;
/* 260 */     if ((arrTermDocFreq == null) || (arrTermDocFreq.length == 0) || (this.minDocFreq <= 1))
/* 261 */       return usedList;
/* 262 */     for (int i = 0; i < termNum; i++)
/* 263 */       if ((arrTermDocFreq[i] < this.minDocFreq) || (arrTermDocFreq[i] > this.maxDocFreq))
/* 264 */         usedList[i] = false;
/* 265 */     return usedList;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.kngbase.CooccurrenceGenerator
 * JD-Core Version:    0.6.2
 */