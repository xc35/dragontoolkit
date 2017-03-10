/*     */ package edu.drexel.cis.dragon.ir.search.expand;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.kngbase.HALSpace;
/*     */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.RelSimpleQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleRow;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public class InformationFlowQE extends AbstractQE
/*     */   implements QueryExpansion
/*     */ {
/*     */   private DoubleSparseMatrix halMatrix;
/*     */   private SimpleElementList halTermList;
/*     */   private IndexReader indexReader;
/*     */   private int expandTerm;
/*     */   private double expandCoeffi;
/*     */   private double dominance1;
/*     */   private double dominance2;
/*     */   private double multiplier;
/*     */   private double threshold1;
/*     */   private double threshold2;
/*     */ 
/*     */   public InformationFlowQE(IndexReader indexReader, int expandTermNum, double expandCoeffi)
/*     */   {
/*  33 */     super(indexReader);
/*  34 */     this.indexReader = indexReader;
/*  35 */     this.expandTerm = expandTermNum;
/*  36 */     this.expandCoeffi = expandCoeffi;
/*  37 */     this.dominance1 = 0.5D;
/*  38 */     this.dominance2 = 0.3D;
/*  39 */     this.multiplier = 2.0D;
/*  40 */     this.threshold1 = 0.0D;
/*  41 */     this.threshold2 = 0.0D;
/*     */   }
/*     */ 
/*     */   public InformationFlowQE(HALSpace halSpace, IndexReader indexReader, int expandTermNum, double expandCoeffi) {
/*  45 */     super(indexReader);
/*  46 */     this.halMatrix = halSpace.getKnowledgeMatrix();
/*  47 */     this.halTermList = halSpace.getRowKeyList();
/*  48 */     this.indexReader = indexReader;
/*  49 */     this.expandTerm = expandTermNum;
/*  50 */     this.expandCoeffi = expandCoeffi;
/*  51 */     this.dominance1 = 0.5D;
/*  52 */     this.dominance2 = 0.3D;
/*  53 */     this.multiplier = 2.0D;
/*  54 */     this.threshold1 = 0.0D;
/*  55 */     this.threshold2 = 0.0D;
/*     */   }
/*     */ 
/*     */   public void setHALSpace(HALSpace halSpace) {
/*  59 */     this.halMatrix = halSpace.getKnowledgeMatrix();
/*  60 */     this.halTermList = halSpace.getRowKeyList();
/*     */   }
/*     */ 
/*     */   public void setMultiplier(double multiplier) {
/*  64 */     if (multiplier < 1.0D)
/*  65 */       return;
/*  66 */     this.multiplier = multiplier;
/*     */   }
/*     */ 
/*     */   public double getMultiplier() {
/*  70 */     return this.multiplier;
/*     */   }
/*     */ 
/*     */   public void setDominantVectorWeight(double weight) {
/*  74 */     if ((weight < 0.0D) || (weight > 1.0D))
/*  75 */       return;
/*  76 */     this.dominance1 = weight;
/*     */   }
/*     */ 
/*     */   public double getDominantVectorWeight() {
/*  80 */     return this.dominance1;
/*     */   }
/*     */ 
/*     */   public void setSubordinateVectorWeight(double weight) {
/*  84 */     if ((weight < 0.0D) || (weight > 1.0D))
/*  85 */       return;
/*  86 */     this.dominance2 = weight;
/*     */   }
/*     */ 
/*     */   public double getSubordinateVectorWeight() {
/*  90 */     return this.dominance2;
/*     */   }
/*     */ 
/*     */   public void setDominantVectorThreshold(double threshold) {
/*  94 */     if (threshold < 0.0D) return;
/*  95 */     this.threshold1 = threshold;
/*     */   }
/*     */ 
/*     */   public double getDominantVectorThreshold() {
/*  99 */     return this.threshold1;
/*     */   }
/*     */ 
/*     */   public void setSubordinateVectorThreshold(double threshold) {
/* 103 */     if (threshold < 0.0D) return;
/* 104 */     this.threshold2 = threshold;
/*     */   }
/*     */ 
/*     */   public double getSubordinateVectorThreshold() {
/* 108 */     return this.threshold2;
/*     */   }
/*     */ 
/*     */   public IRQuery expand(IRQuery initQuery)
/*     */   {
/* 118 */     if (!initQuery.isRelSimpleQuery()) return null;
/* 119 */     SimpleTermPredicate[] arrPredicate = checkSimpleTermQuery((RelSimpleQuery)initQuery);
/* 120 */     if ((arrPredicate == null) || (arrPredicate.length == 0) || (this.halMatrix == null)) {
/* 121 */       return initQuery;
/*     */     }
/* 123 */     ArrayList oldList = new ArrayList();
/* 124 */     int docNum = this.indexReader.getCollection().getDocNum();
/* 125 */     for (int i = 0; i < arrPredicate.length; i++) {
/* 126 */       int index = arrPredicate[i].getIndex();
/*     */ 
/* 128 */       int halIndex = this.halTermList.search(arrPredicate[i].getKey());
/* 129 */       if ((halIndex >= 0) && (this.halMatrix.getNonZeroNumInRow(halIndex) > 0)) {
/* 130 */         Token token = new Token(arrPredicate[i].getKey());
/* 131 */         token.setIndex(halIndex);
/* 132 */         token.setWeight(arrPredicate[i].getWeight() * Math.log(docNum * 1.0D / this.indexReader.getIRTerm(index).getDocFrequency()));
/* 133 */         oldList.add(token);
/*     */       }
/*     */     }
/* 136 */     if (oldList.size() == 0) return initQuery;
/*     */ 
/* 138 */     ArrayList newList = expand(oldList, this.expandTerm);
/* 139 */     SimpleTermPredicate[] arrExpand = new SimpleTermPredicate[newList.size()];
/* 140 */     for (int i = 0; i < arrExpand.length; i++) {
/* 141 */       Token token = (Token)newList.get(i);
/* 142 */       arrExpand[i] = buildSimpleTermPredicate(token.getIndex(), token.getWeight());
/*     */     }
/*     */ 
/* 145 */     return buildQuery(arrPredicate, arrExpand, this.expandCoeffi);
/*     */   }
/*     */ 
/*     */   private ArrayList expand(ArrayList queryList, int top)
/*     */   {
/* 158 */     ArrayList candidates = new ArrayList(this.halMatrix.rows());
/* 159 */     ArrayList topList = new ArrayList(top);
/*     */ 
/* 162 */     Collections.sort(queryList, new WeightComparator(true));
/* 163 */     Token token = (Token)queryList.get(0);
/* 164 */     int rowIndex = token.getIndex();
/*     */ 
/* 166 */     DoubleRow first = getVector(rowIndex);
/*     */ 
/* 168 */     for (int i = 1; i < queryList.size(); i++) {
/* 169 */       rowIndex = token.getIndex();
/* 170 */       DoubleRow second = getVector(rowIndex);
/* 171 */       first = combineVector(first, second);
/*     */     }
/*     */ 
/* 175 */     first = filterVector(first, getMeanScoreInVector(first));
/* 176 */     double sum = getSumScoreInVector(first);
/*     */ 
/* 178 */     for (int i = 0; i < this.halMatrix.rows(); i++) {
/* 179 */       if (this.halMatrix.getNonZeroNumInRow(i) != 0)
/*     */       {
/* 181 */         DoubleRow second = getVector(i);
/* 182 */         token = new Token(null);
/* 183 */         token.setIndex(i);
/* 184 */         token.setWeight(intersect(first, second) / sum);
/* 185 */         candidates.add(token);
/*     */       }
/*     */     }
/* 188 */     Collections.sort(candidates, new WeightComparator(true));
/* 189 */     for (int i = 0; i < top; i++) {
/* 190 */       token = (Token)candidates.get(i);
/*     */ 
/* 192 */       IRTerm term = this.indexReader.getIRTerm(this.halTermList.search(token.getIndex()));
/* 193 */       if (term != null) {
/* 194 */         token.setIndex(term.getIndex());
/* 195 */         topList.add(token);
/*     */       }
/*     */     }
/* 198 */     candidates.clear();
/* 199 */     return topList;
/*     */   }
/*     */ 
/*     */   private DoubleRow getVector(int index) {
/* 203 */     return new DoubleRow(index, this.halMatrix.getNonZeroNumInRow(index), this.halMatrix.getNonZeroColumnsInRow(index), this.halMatrix.getNonZeroDoubleScoresInRow(index));
/*     */   }
/*     */ 
/*     */   private double intersect(DoubleRow first, DoubleRow second)
/*     */   {
/* 211 */     int i = 0;
/* 212 */     int j = 0;
/* 213 */     double sum = 0.0D;
/* 214 */     int len1 = first.getNonZeroNum();
/* 215 */     int len2 = second.getNonZeroNum();
/* 216 */     while ((i < len1) && (j < len2)) {
/* 217 */       int x = first.getNonZeroColumn(i);
/* 218 */       int y = second.getNonZeroColumn(j);
/* 219 */       if (x == y) {
/* 220 */         sum += first.getNonZeroDoubleScore(i);
/* 221 */         i++;
/* 222 */         j++;
/*     */       }
/* 224 */       else if (x < y) {
/* 225 */         i++;
/*     */       }
/*     */       else {
/* 228 */         j++;
/*     */       }
/*     */     }
/* 231 */     return sum;
/*     */   }
/*     */ 
/*     */   private DoubleRow combineVector(DoubleRow first, DoubleRow second)
/*     */   {
/* 237 */     first = reweightVector(first, this.dominance1);
/* 238 */     second = reweightVector(second, this.dominance2);
/* 239 */     strengthenCommonProperty(first, this.threshold1, second, this.threshold2, this.multiplier);
/* 240 */     DoubleRow vector = addVector(first, second);
/* 241 */     return normalizeVector(vector);
/*     */   }
/*     */ 
/*     */   private DoubleRow filterVector(DoubleRow vector, double threshold)
/*     */   {
/* 252 */     int len = vector.getNonZeroNum();
/* 253 */     ArrayList tokenList = new ArrayList();
/* 254 */     for (int i = 0; i < len; i++) {
/* 255 */       double score = vector.getNonZeroDoubleScore(i);
/* 256 */       if (score >= threshold) {
/* 257 */         Token curToken = new Token(null);
/* 258 */         curToken.setIndex(vector.getNonZeroColumn(i));
/* 259 */         curToken.setWeight(score);
/* 260 */         tokenList.add(curToken);
/*     */       }
/*     */     }
/*     */ 
/* 264 */     int[] arrCol = new int[tokenList.size()];
/* 265 */     double[] arrScore = new double[tokenList.size()];
/* 266 */     for (int i = 0; i < tokenList.size(); i++) {
/* 267 */       Token curToken = (Token)tokenList.get(i);
/* 268 */       arrCol[i] = curToken.getIndex();
/* 269 */       arrScore[i] = curToken.getWeight();
/*     */     }
/* 271 */     return new DoubleRow(vector.getRowIndex(), tokenList.size(), arrCol, arrScore);
/*     */   }
/*     */ 
/*     */   private double getMaxScoreInVector(DoubleRow vector)
/*     */   {
/* 278 */     double max = 4.9E-324D;
/* 279 */     int len = vector.getNonZeroNum();
/* 280 */     for (int i = 0; i < len; i++)
/* 281 */       if (max < vector.getNonZeroDoubleScore(i)) max = vector.getNonZeroDoubleScore(i);
/* 282 */     return max;
/*     */   }
/*     */ 
/*     */   private double getMeanScoreInVector(DoubleRow vector)
/*     */   {
/* 289 */     double mean = 0.0D;
/* 290 */     int len = vector.getNonZeroNum();
/* 291 */     for (int i = 0; i < len; i++)
/* 292 */       mean += vector.getNonZeroDoubleScore(i);
/* 293 */     return mean / len;
/*     */   }
/*     */ 
/*     */   private double getSumScoreInVector(DoubleRow vector)
/*     */   {
/* 300 */     double sum = 0.0D;
/* 301 */     int len = vector.getNonZeroNum();
/* 302 */     for (int i = 0; i < len; i++)
/* 303 */       sum += vector.getNonZeroDoubleScore(i);
/* 304 */     return sum;
/*     */   }
/*     */ 
/*     */   private DoubleRow reweightVector(DoubleRow vector, double scale)
/*     */   {
/* 311 */     int len = vector.getNonZeroNum();
/* 312 */     double max = getMaxScoreInVector(vector);
/* 313 */     for (int i = 0; i < len; i++)
/* 314 */       vector.setNonZeroDoubleScore(i, scale + vector.getNonZeroDoubleScore(i) * scale / max);
/* 315 */     return vector;
/*     */   }
/*     */ 
/*     */   private DoubleRow normalizeVector(DoubleRow vector)
/*     */   {
/* 321 */     double sum = getSumScoreInVector(vector);
/* 322 */     return reweightVector(vector, 1.0D / sum);
/*     */   }
/*     */ 
/*     */   private void strengthenCommonProperty(DoubleRow first, double thresholdFirst, DoubleRow second, double thresholdSecond, double multiplier)
/*     */   {
/* 330 */     int i = 0;
/* 331 */     int j = 0;
/* 332 */     int len1 = first.getNonZeroNum();
/* 333 */     int len2 = second.getNonZeroNum();
/* 334 */     while ((i < len1) && (j < len2)) {
/* 335 */       double a = first.getNonZeroDoubleScore(i);
/* 336 */       if (a < thresholdFirst) {
/* 337 */         i++;
/*     */       }
/*     */       else {
/* 340 */         double b = second.getNonZeroDoubleScore(j);
/* 341 */         if (b < thresholdSecond) {
/* 342 */           j++;
/*     */         }
/*     */         else
/*     */         {
/* 346 */           int x = first.getNonZeroColumn(i);
/* 347 */           int y = second.getNonZeroColumn(j);
/* 348 */           if (x == y) {
/* 349 */             first.setNonZeroDoubleScore(i, a * multiplier);
/* 350 */             second.setNonZeroDoubleScore(j, b * multiplier);
/* 351 */             i++;
/* 352 */             j++;
/*     */           }
/* 354 */           else if (x < y) {
/* 355 */             i++;
/*     */           }
/*     */           else {
/* 358 */             j++;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private DoubleRow addVector(DoubleRow first, DoubleRow second)
/*     */   {
/* 371 */     int i = 0;
/* 372 */     int j = 0;
/* 373 */     int len1 = first.getNonZeroNum();
/* 374 */     int len2 = second.getNonZeroNum();
/* 375 */     ArrayList tokenList = new ArrayList();
/* 376 */     while ((i < len1) && (j < len2)) {
/* 377 */       int x = first.getNonZeroColumn(i);
/* 378 */       int y = second.getNonZeroColumn(j);
/*     */       Token curToken;
/* 379 */       if (x == y) {
/* 380 */         curToken = new Token(null);
/* 381 */         curToken.setIndex(x);
/* 382 */         curToken.setWeight(first.getNonZeroDoubleScore(i) + second.getNonZeroDoubleScore(j));
/* 383 */         i++;
/* 384 */         j++;
/*     */       }
/* 386 */       else if (x < y) {
/* 387 */         curToken = new Token(null);
/* 388 */         curToken.setIndex(x);
/* 389 */         curToken.setWeight(first.getNonZeroDoubleScore(i));
/* 390 */         i++;
/*     */       }
/*     */       else {
/* 393 */         curToken = new Token(null);
/* 394 */         curToken.setIndex(y);
/* 395 */         curToken.setWeight(second.getNonZeroDoubleScore(j));
/* 396 */         j++;
/*     */       }
/* 398 */       tokenList.add(curToken);
/*     */     }
/*     */ 
/* 401 */     for (int k = i; k < len1; k++) {
/* 402 */       Token curToken = new Token(null);
/* 403 */       curToken.setIndex(first.getNonZeroColumn(k));
/* 404 */       curToken.setWeight(first.getNonZeroDoubleScore(k));
/* 405 */       k++;
/*     */     }
/* 407 */     for (int k = j; k < len2; k++) {
/* 408 */       Token curToken = new Token(null);
/* 409 */       curToken.setIndex(second.getNonZeroColumn(k));
/* 410 */       curToken.setWeight(second.getNonZeroDoubleScore(k));
/* 411 */       k++;
/*     */     }
/*     */ 
/* 414 */     int[] arrCol = new int[tokenList.size()];
/* 415 */     double[] arrScore = new double[tokenList.size()];
/* 416 */     for (i = 0; i < tokenList.size(); i++) {
/* 417 */       Token curToken = (Token)tokenList.get(i);
/* 418 */       arrCol[i] = curToken.getIndex();
/* 419 */       arrScore[i] = curToken.getWeight();
/*     */     }
/* 421 */     return new DoubleRow(-1, tokenList.size(), arrCol, arrScore);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.expand.InformationFlowQE
 * JD-Core Version:    0.6.2
 */