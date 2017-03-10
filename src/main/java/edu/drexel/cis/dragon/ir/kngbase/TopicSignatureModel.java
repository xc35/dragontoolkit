/*     */ package edu.drexel.cis.dragon.ir.kngbase;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRSignature;
/*     */ import edu.drexel.cis.dragon.ir.index.IRSignatureIndexList;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSuperSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.Counter;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class TopicSignatureModel
/*     */ {
/*     */   private IRSignatureIndexList srcIndexList;
/*     */   private IRSignatureIndexList destIndexList;
/*     */   private IntSparseMatrix srcSignatureDocMatrix;
/*     */   private IntSparseMatrix destDocSignatureMatrix;
/*     */   private IntSparseMatrix cooccurMatrix;
/*     */   private boolean useDocFrequency;
/*     */   private boolean useMeanTrim;
/*     */   private boolean useEM;
/*     */   private double probThreshold;
/*     */   private double bkgCoeffi;
/*     */   private int[] buf;
/*     */   private int iterationNum;
/*     */   private int totalDestSignatureNum;
/*     */   private int DOC_THRESH;
/*     */ 
/*     */   public TopicSignatureModel(IRSignatureIndexList srcIndexList, IntSparseMatrix srcSignatureDocMatrix, IntSparseMatrix destDocSignatureMatrix)
/*     */   {
/*  49 */     this.srcIndexList = srcIndexList;
/*  50 */     this.srcSignatureDocMatrix = srcSignatureDocMatrix;
/*  51 */     this.destDocSignatureMatrix = destDocSignatureMatrix;
/*  52 */     this.useDocFrequency = true;
/*  53 */     this.useMeanTrim = true;
/*  54 */     this.probThreshold = 0.001D;
/*  55 */     this.useEM = false;
/*  56 */     this.iterationNum = 15;
/*  57 */     this.bkgCoeffi = 0.5D;
/*  58 */     this.totalDestSignatureNum = destDocSignatureMatrix.columns();
/*     */   }
/*     */ 
/*     */   public TopicSignatureModel(IRSignatureIndexList srcIndexList, IntSparseMatrix cooccurMatrix)
/*     */   {
/*  67 */     this.srcIndexList = srcIndexList;
/*  68 */     this.cooccurMatrix = cooccurMatrix;
/*  69 */     this.useMeanTrim = true;
/*  70 */     this.probThreshold = 0.001D;
/*  71 */     this.useEM = false;
/*  72 */     this.iterationNum = 15;
/*  73 */     this.bkgCoeffi = 0.5D;
/*  74 */     this.totalDestSignatureNum = cooccurMatrix.columns();
/*     */   }
/*     */ 
/*     */   public TopicSignatureModel(IRSignatureIndexList srcIndexList, IRSignatureIndexList destIndexList, IntSparseMatrix cooccurMatrix)
/*     */   {
/*  84 */     this.srcIndexList = srcIndexList;
/*  85 */     this.destIndexList = destIndexList;
/*  86 */     this.cooccurMatrix = cooccurMatrix;
/*  87 */     this.useMeanTrim = true;
/*  88 */     this.probThreshold = 0.001D;
/*  89 */     this.useEM = true;
/*  90 */     this.iterationNum = 15;
/*  91 */     this.bkgCoeffi = 0.5D;
/*  92 */     this.totalDestSignatureNum = cooccurMatrix.columns();
/*     */   }
/*     */ 
/*     */   public TopicSignatureModel(IRSignatureIndexList srcIndexList, IntSparseMatrix srcSignatureDocMatrix, IRSignatureIndexList destIndexList, IntSparseMatrix destDocSignatureMatrix)
/*     */   {
/* 103 */     this.srcIndexList = srcIndexList;
/* 104 */     this.srcSignatureDocMatrix = srcSignatureDocMatrix;
/* 105 */     this.destIndexList = destIndexList;
/* 106 */     this.destDocSignatureMatrix = destDocSignatureMatrix;
/* 107 */     this.useDocFrequency = true;
/* 108 */     this.useMeanTrim = true;
/* 109 */     this.probThreshold = 0.001D;
/* 110 */     this.useEM = true;
/* 111 */     this.iterationNum = 15;
/* 112 */     this.bkgCoeffi = 0.5D;
/* 113 */     this.totalDestSignatureNum = destDocSignatureMatrix.columns();
/*     */   }
/*     */ 
/*     */   public void setUseEM(boolean option) {
/* 117 */     this.useEM = option;
/*     */   }
/*     */ 
/*     */   public boolean getUseEM() {
/* 121 */     return this.useEM;
/*     */   }
/*     */ 
/*     */   public void setEMBackgroundCoefficient(double coeffi) {
/* 125 */     this.bkgCoeffi = coeffi;
/*     */   }
/*     */ 
/*     */   public double getEMBackgroundCoefficient() {
/* 129 */     return this.bkgCoeffi;
/*     */   }
/*     */ 
/*     */   public void setEMIterationNum(int iterationNum) {
/* 133 */     this.iterationNum = iterationNum;
/*     */   }
/*     */ 
/*     */   public int getEMIterationNum() {
/* 137 */     return this.iterationNum;
/*     */   }
/*     */ 
/*     */   public void setUseDocFrequency(boolean option) {
/* 141 */     this.useDocFrequency = option;
/*     */   }
/*     */ 
/*     */   public boolean getUseDocFrequency() {
/* 145 */     return this.useDocFrequency;
/*     */   }
/*     */ 
/*     */   public void setUseMeanTrim(boolean option) {
/* 149 */     this.useMeanTrim = option;
/*     */   }
/*     */ 
/*     */   public boolean getUseMeanTrim() {
/* 153 */     return this.useMeanTrim;
/*     */   }
/*     */ 
/*     */   public void setProbThreshold(double threshold) {
/* 157 */     this.probThreshold = threshold;
/*     */   }
/*     */ 
/*     */   public double getProbThreshold() {
/* 161 */     return this.probThreshold;
/*     */   }
/*     */ 
/*     */   public boolean genMappingMatrix(int minDocFrequency, String matrixPath, String matrixKey)
/*     */   {
/* 174 */     String transIndexFile = matrixPath + "/" + matrixKey + ".index";
/* 175 */     String transMatrixFile = matrixPath + "/" + matrixKey + ".matrix";
/* 176 */     String transTIndexFile = matrixPath + "/" + matrixKey + "t.index";
/* 177 */     String transTMatrixFile = matrixPath + "/" + matrixKey + "t.matrix";
/* 178 */     File file = new File(transMatrixFile);
/* 179 */     if (file.exists()) file.delete();
/* 180 */     file = new File(transIndexFile);
/* 181 */     if (file.exists()) file.delete();
/* 182 */     file = new File(transTMatrixFile);
/* 183 */     if (file.exists()) file.delete();
/* 184 */     file = new File(transTIndexFile);
/* 185 */     if (file.exists()) file.delete();
/*     */ 
/* 187 */     DoubleSuperSparseMatrix outputTransMatrix = new DoubleSuperSparseMatrix(transIndexFile, transMatrixFile, false, false);
/* 188 */     outputTransMatrix.setFlushInterval(2147483647);
/* 189 */     DoubleSuperSparseMatrix outputTransTMatrix = new DoubleSuperSparseMatrix(transTIndexFile, transTMatrixFile, false, false);
/* 190 */     outputTransTMatrix.setFlushInterval(2147483647);
/* 191 */     int cellNum = 0;
/* 192 */     int rowNum = this.srcIndexList.size();
/* 193 */     this.buf = new int[this.totalDestSignatureNum];
/* 194 */     if (this.destDocSignatureMatrix != null) {
/* 195 */       this.DOC_THRESH = computeDocThreshold(this.destDocSignatureMatrix);
/*     */     }
/* 197 */     for (int i = 0; i < rowNum; i++) {
/* 198 */       if (i % 1000 == 0) System.out.println(new Date().toString() + " Processing Row#" + i);
/*     */ 
/* 200 */       if ((this.srcIndexList.getIRSignature(i).getDocFrequency() >= minDocFrequency) && (
/* 201 */         (this.cooccurMatrix == null) || (this.cooccurMatrix.getNonZeroNumInRow(i) >= 5)))
/*     */       {
/* 203 */         ArrayList tokenList = genSignatureMapping(i);
/* 204 */         for (int j = 0; j < tokenList.size(); j++) {
/* 205 */           Token curToken = (Token)tokenList.get(j);
/* 206 */           outputTransMatrix.add(i, curToken.getIndex(), curToken.getWeight());
/* 207 */           outputTransTMatrix.add(curToken.getIndex(), i, curToken.getWeight());
/*     */         }
/* 209 */         cellNum += tokenList.size();
/* 210 */         tokenList.clear();
/* 211 */         if (cellNum >= 5000000) {
/* 212 */           outputTransTMatrix.flush();
/* 213 */           outputTransMatrix.flush();
/* 214 */           cellNum = 0;
/*     */         }
/*     */       }
/*     */     }
/* 217 */     outputTransTMatrix.finalizeData();
/* 218 */     outputTransTMatrix.close();
/* 219 */     outputTransMatrix.finalizeData();
/* 220 */     outputTransMatrix.close();
/* 221 */     return true;
/*     */   }
/*     */ 
/*     */   public ArrayList genSignatureMapping(int srcSignatureIndex)
/*     */   {
/*     */     ArrayList tokenList;
/* 228 */     if (this.srcSignatureDocMatrix != null) {
/* 229 */       int[] arrDoc = this.srcSignatureDocMatrix.getNonZeroColumnsInRow(srcSignatureIndex);
/* 230 */       if (arrDoc.length > this.DOC_THRESH)
/* 231 */         tokenList = computeDistributionByArray(arrDoc);
/*     */       else
/* 233 */         tokenList = computeDistributionByHash(arrDoc);
/*     */     }
/*     */     else {
/* 236 */       tokenList = computeDistributionByCooccurMatrix(srcSignatureIndex);
/*     */     }
/* 238 */     if (this.useEM)
/* 239 */       tokenList = emTopicSignatureModel(tokenList);
/* 240 */     return tokenList;
/*     */   }
/*     */ 
/*     */   private int computeDocThreshold(IntSparseMatrix doctermMatrix) {
/* 244 */     return (int)(doctermMatrix.columns() / computeAvgTermNum(doctermMatrix) / 8.0D);
/*     */   }
/*     */ 
/*     */   private double computeAvgTermNum(IntSparseMatrix doctermMatrix)
/*     */   {
/* 252 */     Random random = new Random();
/* 253 */     int num = Math.min(50, doctermMatrix.rows());
/* 254 */     double sum = 0.0D;
/* 255 */     for (int i = 0; i < num; i++) {
/* 256 */       int index = random.nextInt(doctermMatrix.rows());
/* 257 */       sum += doctermMatrix.getNonZeroNumInRow(index);
/*     */     }
/* 259 */     return sum / num;
/*     */   }
/*     */ 
/*     */   private ArrayList computeDistributionByCooccurMatrix(int signatureIndex)
/*     */   {
/* 269 */     double rowTotal = 0.0D;
/* 270 */     int[] arrIndex = this.cooccurMatrix.getNonZeroColumnsInRow(signatureIndex);
/* 271 */     int[] arrFreq = this.cooccurMatrix.getNonZeroIntScoresInRow(signatureIndex);
/* 272 */     for (int i = 0; i < arrFreq.length; i++)
/* 273 */       rowTotal += arrFreq[i];
/*     */     double mean;
/* 274 */     if (this.useMeanTrim)
/* 275 */       mean = rowTotal / arrFreq.length;
/*     */     else
/* 277 */       mean = 0.5D;
/* 278 */     if (mean < rowTotal * getMinInitProb()) {
/* 279 */       mean = rowTotal * getMinInitProb();
/*     */     }
/* 281 */     rowTotal = 0.0D;
/* 282 */     ArrayList list = new ArrayList();
/* 283 */     for (int i = 0; i < arrFreq.length; i++) {
/* 284 */       if (arrFreq[i] >= mean) {
/* 285 */         list.add(new Token(arrIndex[i], arrFreq[i]));
/* 286 */         rowTotal += arrFreq[i];
/*     */       }
/*     */     }
/* 289 */     for (int i = 0; i < list.size(); i++) {
/* 290 */       Token curToken = (Token)list.get(i);
/* 291 */       curToken.setWeight(curToken.getFrequency() / rowTotal);
/*     */     }
/* 293 */     return list;
/*     */   }
/*     */ 
/*     */   private ArrayList computeDistributionByArray(int[] arrDoc)
/*     */   {
/* 303 */     double rowTotal = 0.0D;
/* 304 */     if (this.buf == null)
/* 305 */       this.buf = new int[this.totalDestSignatureNum];
/* 306 */     MathUtil.initArray(this.buf, 0);
/* 307 */     for (int j = 0; j < arrDoc.length; j++) {
/* 308 */       int[] arrIndex = this.destDocSignatureMatrix.getNonZeroColumnsInRow(arrDoc[j]);
/*     */       int[] arrFreq;
/* 309 */       if (this.useDocFrequency)
/* 310 */         arrFreq = (int[])null;
/*     */       else
/* 312 */         arrFreq = this.destDocSignatureMatrix.getNonZeroIntScoresInRow(arrDoc[j]);
/* 313 */       for (int k = 0; k < arrIndex.length; k++) {
/* 314 */         if (this.useDocFrequency)
/* 315 */           this.buf[arrIndex[k]] += 1;
/*     */         else {
/* 317 */           this.buf[arrIndex[k]] += arrFreq[k];
/*     */         }
/*     */       }
/*     */     }
/* 321 */     int nonZeroNum = 0;
/* 322 */     for (int i = 0; i < this.buf.length; i++)
/* 323 */       if (this.buf[i] > 0) {
/* 324 */         nonZeroNum++;
/* 325 */         rowTotal += this.buf[i];
/*     */       }
/*     */     double mean;
/* 328 */     if (this.useMeanTrim)
/* 329 */       mean = rowTotal / nonZeroNum;
/*     */     else
/* 331 */       mean = 0.5D;
/* 332 */     if (mean < rowTotal * getMinInitProb()) {
/* 333 */       mean = rowTotal * getMinInitProb();
/*     */     }
/* 335 */     rowTotal = 0.0D;
/* 336 */     ArrayList list = new ArrayList();
/* 337 */     for (int i = 0; i < this.buf.length; i++) {
/* 338 */       if (this.buf[i] >= mean) {
/* 339 */         list.add(new Token(i, this.buf[i]));
/* 340 */         rowTotal += this.buf[i];
/*     */       }
/*     */     }
/* 343 */     for (int i = 0; i < list.size(); i++) {
/* 344 */       Token curToken = (Token)list.get(i);
/* 345 */       curToken.setWeight(curToken.getFrequency() / rowTotal);
/*     */     }
/* 347 */     return list;
/*     */   }
/*     */ 
/*     */   private ArrayList computeDistributionByHash(int[] arrDoc)
/*     */   {
/* 356 */     ArrayList tokenList = countTokensByHashMap(arrDoc);
/* 357 */     double rowTotal = 0.0D;
/* 358 */     for (int i = 0; i < tokenList.size(); i++)
/* 359 */       rowTotal += ((Token)tokenList.get(i)).getFrequency();
/*     */     ArrayList list;
/* 361 */     if ((this.useMeanTrim) || (rowTotal * getMinInitProb() > 1.0D))
/*     */     {
/*     */       double mean;
/* 362 */       if (this.useMeanTrim)
/* 363 */         mean = rowTotal / tokenList.size();
/*     */       else
/* 365 */         mean = 0.5D;
/* 366 */       if (mean < rowTotal * getMinInitProb())
/* 367 */         mean = rowTotal * getMinInitProb();
/* 368 */       list = new ArrayList();
/* 369 */       rowTotal = 0.0D;
/* 370 */       for (int i = 0; i < tokenList.size(); i++) {
/* 371 */         Token curToken = (Token)tokenList.get(i);
/* 372 */         if (curToken.getFrequency() >= mean) {
/* 373 */           list.add(curToken);
/* 374 */           rowTotal += curToken.getFrequency();
/*     */         }
/*     */       }
/* 377 */       tokenList.clear();
/*     */     }
/*     */     else {
/* 380 */       list = tokenList;
/*     */     }
/* 382 */     for (int i = 0; i < list.size(); i++) {
/* 383 */       Token curToken = (Token)list.get(i);
/* 384 */       curToken.setWeight(curToken.getFrequency() / rowTotal);
/*     */     }
/* 386 */     return list;
/*     */   }
/*     */ 
/*     */   private ArrayList countTokensByHashMap(int[] arrDoc)
/*     */   {
/* 398 */     HashMap hash = new HashMap();
/* 399 */     for (int j = 0; j < arrDoc.length; j++) {
/* 400 */       int termNum = this.destDocSignatureMatrix.getNonZeroNumInRow(arrDoc[j]);
/* 401 */       if (termNum != 0)
/*     */       {
/* 403 */         int[] arrTerm = this.destDocSignatureMatrix.getNonZeroColumnsInRow(arrDoc[j]);
/*     */         int[] arrFreq;
/* 404 */         if (this.useDocFrequency)
/* 405 */           arrFreq = (int[])null;
/*     */         else
/* 407 */           arrFreq = this.destDocSignatureMatrix.getNonZeroIntScoresInRow(arrDoc[j]);
/* 408 */         for (int i = 0; i < termNum; i++)
/*     */         {
/*     */           Token curToken;
/* 409 */           if (this.useDocFrequency)
/* 410 */             curToken = new Token(arrTerm[i], 1);
/*     */           else
/* 412 */             curToken = new Token(arrTerm[i], arrFreq[i]);
/* 413 */           Counter counter = (Counter)hash.get(curToken);
/* 414 */           if (counter == null) {
/* 415 */             counter = new Counter(curToken.getFrequency());
/* 416 */             hash.put(curToken, counter);
/*     */           }
/*     */           else {
/* 419 */             counter.addCount(curToken.getFrequency());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 423 */     ArrayList list = new ArrayList(hash.size());
/* 424 */     Iterator iterator = hash.keySet().iterator();
/* 425 */     while (iterator.hasNext()) {
/* 426 */       Token curToken = (Token)iterator.next();
/* 427 */       Counter counter = (Counter)hash.get(curToken);
/* 428 */       curToken.setFrequency(counter.getCount());
/* 429 */       list.add(curToken);
/*     */     }
/* 431 */     hash.clear();
/* 432 */     return list;
/*     */   }
/*     */ 
/*     */   private double getMinInitProb()
/*     */   {
/* 441 */     return this.probThreshold;
/*     */   }
/*     */ 
/*     */   private ArrayList emTopicSignatureModel(ArrayList list)
/*     */   {
/* 451 */     int termNum = list.size();
/* 452 */     double[] arrProb = new double[termNum];
/*     */ 
/* 455 */     double[] arrCollectionProb = new double[termNum];
/* 456 */     double weightSum = 0.0D;
/* 457 */     for (int i = 0; i < termNum; i++) {
/* 458 */       Token curToken = (Token)list.get(i);
/* 459 */       if (this.useDocFrequency)
/* 460 */         arrCollectionProb[i] = this.destIndexList.getIRSignature(curToken.getIndex()).getDocFrequency();
/*     */       else
/* 462 */         arrCollectionProb[i] = this.destIndexList.getIRSignature(curToken.getIndex()).getFrequency();
/* 463 */       weightSum += arrCollectionProb[i];
/*     */     }
/* 465 */     for (int i = 0; i < termNum; i++) {
/* 466 */       arrCollectionProb[i] /= weightSum;
/*     */     }
/*     */ 
/* 469 */     for (int i = 0; i < this.iterationNum; i++) {
/* 470 */       weightSum = 0.0D;
/* 471 */       for (int j = 0; j < termNum; j++) {
/* 472 */         Token curToken = (Token)list.get(j);
/* 473 */         arrProb[j] = 
/* 474 */           ((1.0D - this.bkgCoeffi) * curToken.getWeight() / (
/* 474 */           (1.0D - this.bkgCoeffi) * curToken.getWeight() + this.bkgCoeffi * arrCollectionProb[j]) * curToken.getFrequency());
/* 475 */         weightSum += arrProb[j];
/*     */       }
/* 477 */       for (int j = 0; j < termNum; j++) {
/* 478 */         Token curToken = (Token)list.get(j);
/* 479 */         curToken.setWeight(arrProb[j] / weightSum);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 490 */     return list;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.kngbase.TopicSignatureModel
 * JD-Core Version:    0.6.2
 */