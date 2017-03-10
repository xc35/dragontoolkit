/*     */ package edu.drexel.cis.dragon.ir.topicmodel;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.sequence.SequenceIndexReader;
/*     */ import java.util.Date;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class GibbsLDA extends AbstractTopicModel
/*     */ {
/*     */   private int indexType;
/*     */   private int tokenNum;
/*     */   private double alpha;
/*     */   private double beta;
/*     */   private double wBeta;
/*     */   private double tAlpha;
/*     */ 
/*     */   public GibbsLDA(SequenceIndexReader indexReader, double alpha, double beta)
/*     */   {
/*  22 */     this(indexReader, alpha, beta, 1);
/*     */   }
/*     */ 
/*     */   public GibbsLDA(IndexReader indexReader, double alpha, double beta) {
/*  26 */     this(indexReader, alpha, beta, 0);
/*     */   }
/*     */ 
/*     */   private GibbsLDA(IndexReader indexReader, double alpha, double beta, int indexType) {
/*  30 */     super(indexReader);
/*     */ 
/*  32 */     this.alpha = alpha;
/*  33 */     this.beta = beta;
/*  34 */     this.iterations = 500;
/*  35 */     this.termNum = indexReader.getCollection().getTermNum();
/*  36 */     this.tokenNum = ((int)indexReader.getCollection().getTermCount());
/*  37 */     this.docNum = indexReader.getCollection().getDocNum();
/*  38 */     this.wBeta = (this.termNum * beta);
/*  39 */     this.indexType = indexType;
/*     */   }
/*     */ 
/*     */   public boolean estimateModel(int topicNum)
/*     */   {
/*  48 */     this.themeNum = topicNum;
/*  49 */     this.tAlpha = (this.themeNum * this.alpha);
/*  50 */     int[][] arrTTCount = new int[this.termNum][this.themeNum];
/*  51 */     int[][] arrDTCount = new int[this.docNum][this.themeNum];
/*  52 */     int[] arrTerm = new int[this.tokenNum];
/*  53 */     int[] arrDoc = new int[this.tokenNum];
/*  54 */     int[] arrTermLabel = new int[this.tokenNum];
/*     */ 
/*  57 */     printStatus(new Date().toString() + " Reading sequence...");
/*  58 */     if (this.indexType == 0)
/*  59 */       readSequence(this.indexReader, arrTerm, arrDoc);
/*     */     else {
/*  61 */       readSequence((SequenceIndexReader)this.indexReader, arrTerm, arrDoc);
/*     */     }
/*     */ 
/*  64 */     run(this.seed, arrTerm, arrDoc, arrTermLabel, arrTTCount, arrDTCount);
/*     */ 
/*  67 */     this.arrThemeTerm = new double[this.themeNum][this.termNum];
/*  68 */     for (int i = 0; i < this.themeNum; i++) {
/*  69 */       double topicSum = this.wBeta;
/*  70 */       for (int j = 0; j < this.termNum; j++)
/*  71 */         topicSum += arrTTCount[j][i];
/*  72 */       for (int j = 0; j < this.termNum; j++) {
/*  73 */         this.arrThemeTerm[i][j] = ((arrTTCount[j][i] + this.beta) / topicSum);
/*     */       }
/*     */     }
/*     */ 
/*  77 */     this.arrDocTheme = new double[this.docNum][this.themeNum];
/*  78 */     for (int i = 0; i < this.docNum; i++) {
/*  79 */       double docSum = this.tAlpha;
/*  80 */       for (int j = 0; j < this.themeNum; j++)
/*  81 */         docSum += arrDTCount[i][j];
/*  82 */       for (int j = 0; j < this.themeNum; j++)
/*  83 */         this.arrDocTheme[i][j] = ((arrDTCount[i][j] + this.alpha) / docSum);
/*     */     }
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   private void run(int seed, int[] arrTerm, int[] arrDoc, int[] arrTermLabel, int[][] arrTTCount, int[][] arrDTCount)
/*     */   {
/*  95 */     int[] arrTopicCount = new int[this.themeNum];
/*  96 */     int[] arrOrder = new int[this.tokenNum];
/*  97 */     Random random = new Random();
/*  98 */     if (seed >= 0) {
/*  99 */       random.setSeed(seed);
/*     */     }
/* 101 */     printStatus(new Date().toString() + " Starting random initialization...");
/* 102 */     for (int i = 0; i < this.tokenNum; i++) {
/* 103 */       int topic = random.nextInt(this.themeNum);
/* 104 */       arrTermLabel[i] = topic;
/* 105 */       int termIndex = arrTerm[i];
/* 106 */       int docIndex = arrDoc[i];
/* 107 */       arrTTCount[termIndex][topic] += 1;
/* 108 */       arrDTCount[docIndex][topic] += 1;
/* 109 */       arrTopicCount[topic] += 1;
/*     */     }
/*     */ 
/* 113 */     printStatus(new Date().toString() + " Determining random update sequence...");
/* 114 */     for (int i = 0; i < this.tokenNum; i++) arrOrder[i] = i;
/* 115 */     for (int i = 0; i < this.tokenNum - 1; i++)
/*     */     {
/* 117 */       int k = i + random.nextInt(this.tokenNum - i);
/*     */ 
/* 119 */       int j = arrOrder[k];
/* 120 */       arrOrder[k] = arrOrder[i];
/* 121 */       arrOrder[i] = j;
/*     */     }
/*     */ 
/* 124 */     for (int iter = 0; iter < this.iterations; iter++) {
/* 125 */       printStatus(new Date().toString() + " Iteration #" + (iter + 1));
/* 126 */       for (int k = 0; k < this.tokenNum; k++) {
/* 127 */         int i = arrOrder[k];
/* 128 */         int termIndex = arrTerm[i];
/* 129 */         int docIndex = arrDoc[i];
/* 130 */         int topic = arrTermLabel[i];
/*     */ 
/* 133 */         arrTopicCount[topic] -= 1;
/* 134 */         arrTTCount[termIndex][topic] -= 1;
/* 135 */         arrDTCount[docIndex][topic] -= 1;
/*     */ 
/* 138 */         topic = sampleTopic(random, arrTTCount[termIndex], arrDTCount[docIndex], arrTopicCount);
/*     */ 
/* 141 */         arrTermLabel[i] = topic;
/* 142 */         arrTTCount[termIndex][topic] += 1;
/* 143 */         arrDTCount[docIndex][topic] += 1;
/* 144 */         arrTopicCount[topic] += 1;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int sampleTopic(Random random, int[] arrTTCount, int[] arrDTCount, int[] arrTopicCount)
/*     */   {
/* 154 */     double totalProb = 0.0D;
/* 155 */     double[] arrProb = new double[this.themeNum];
/* 156 */     for (int j = 0; j < this.themeNum; j++) {
/* 157 */       arrProb[j] = ((arrTTCount[j] + this.beta) / (arrTopicCount[j] + this.wBeta) * (arrDTCount[j] + this.alpha));
/* 158 */       totalProb += arrProb[j];
/*     */     }
/* 160 */     double r = totalProb * random.nextDouble();
/* 161 */     double max = arrProb[0];
/* 162 */     int topic = 0;
/* 163 */     while (r > max) {
/* 164 */       topic++;
/* 165 */       max += arrProb[topic];
/*     */     }
/* 167 */     return topic;
/*     */   }
/*     */ 
/*     */   private void readSequence(SequenceIndexReader indexReader, int[] arrTerm, int[] arrDoc)
/*     */   {
/* 174 */     int docNum = indexReader.getCollection().getDocNum();
/* 175 */     int count = 0;
/* 176 */     for (int i = 0; i < docNum; i++) {
/* 177 */       int[] arrSeq = indexReader.getTermIndexList(i);
/* 178 */       if ((arrSeq != null) && (arrSeq.length != 0)) {
/* 179 */         for (int j = 0; j < arrSeq.length; j++) {
/* 180 */           arrTerm[(count + j)] = arrSeq[j];
/* 181 */           arrDoc[(count + j)] = i;
/*     */         }
/* 183 */         count += arrSeq.length;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readSequence(IndexReader indexReader, int[] arrTerm, int[] arrDoc)
/*     */   {
/* 191 */     int docNum = indexReader.getCollection().getDocNum();
/* 192 */     int count = 0;
/* 193 */     for (int i = 0; i < docNum; i++) {
/* 194 */       int[] arrIndex = indexReader.getTermIndexList(i);
/* 195 */       int[] arrFreq = indexReader.getTermFrequencyList(i);
/* 196 */       if ((arrIndex != null) && (arrIndex.length != 0))
/* 197 */         for (int j = 0; j < arrIndex.length; j++) {
/* 198 */           for (int k = 0; k < arrFreq[j]; k++) {
/* 199 */             arrTerm[(count + k)] = arrIndex[j];
/* 200 */             arrDoc[(count + k)] = i;
/*     */           }
/* 202 */           count += arrFreq[j];
/*     */         }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.topicmodel.GibbsLDA
 * JD-Core Version:    0.6.2
 */