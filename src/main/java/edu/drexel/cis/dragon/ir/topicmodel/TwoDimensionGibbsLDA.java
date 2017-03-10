/*     */ package edu.drexel.cis.dragon.ir.topicmodel;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import java.util.Date;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class TwoDimensionGibbsLDA extends AbstractTwoDimensionModel
/*     */ {
/*     */   private double alpha;
/*     */   private double beta;
/*     */   private double wBeta;
/*     */   private double gamma0;
/*     */   private double gamma1;
/*     */   private double delta;
/*     */   private double wDelta;
/*     */   private double epsilon;
/*     */   private double wEpsilon;
/*     */   private double rho;
/*     */   private int tokenNumO;
/*     */   private int tokenNumP;
/*     */ 
/*     */   public TwoDimensionGibbsLDA(IndexReader viewIndexReader, IndexReader topicIndexReader, double alpha, double beta, double gamma0, double gamma1, double delta, double epsilon, double rho)
/*     */   {
/*  26 */     super(viewIndexReader, topicIndexReader);
/*  27 */     this.seed = -1;
/*  28 */     this.iterations = 1000;
/*  29 */     this.alpha = alpha;
/*  30 */     this.beta = beta;
/*  31 */     this.gamma0 = gamma0;
/*  32 */     this.delta = delta;
/*  33 */     this.epsilon = epsilon;
/*  34 */     this.rho = rho;
/*  35 */     this.tokenNumO = ((int)viewIndexReader.getCollection().getTermCount());
/*  36 */     this.tokenNumP = ((int)viewIndexReader.getCollection().getTermCount());
/*  37 */     this.wBeta = (beta * (this.viewTermNum + this.themeTermNum));
/*  38 */     this.wDelta = (delta * this.themeTermNum);
/*  39 */     this.wEpsilon = (epsilon * this.themeTermNum);
/*     */   }
/*     */ 
/*     */   public boolean estimateModel(int viewNum, int topicNum)
/*     */   {
/*  50 */     this.viewNum = viewNum;
/*  51 */     this.themeNum = topicNum;
/*  52 */     int[] arrTermO = new int[this.tokenNumO];
/*  53 */     int[] arrDocO = new int[this.tokenNumO];
/*  54 */     int[] arrTermP = new int[this.tokenNumP];
/*  55 */     int[] arrDocP = new int[this.tokenNumP];
/*  56 */     int[] arrZO = new int[this.tokenNumO];
/*  57 */     int[] arrZP = new int[this.tokenNumP];
/*  58 */     int[] arrY = new int[this.tokenNumP];
/*  59 */     int[] arrX = new int[this.tokenNumP];
/*     */ 
/*  61 */     int[][] arrTVCountO = new int[this.viewTermNum][viewNum];
/*  62 */     int[][] arrTVCountP = new int[this.themeTermNum][viewNum];
/*  63 */     int[][] arrDVCount = new int[this.docNum][viewNum];
/*  64 */     int[][] arrTTCount = new int[this.themeTermNum][this.themeNum];
/*  65 */     int[][][] arrTTViewCount = new int[this.themeTermNum][viewNum][this.themeNum];
/*  66 */     int[][][] arrTTComCount = new int[this.themeTermNum][viewNum][this.themeNum];
/*  67 */     int[][][] arrDTCount = new int[this.docNum][viewNum][this.themeNum];
/*     */ 
/*  70 */     this.arrViewProb = new double[viewNum][this.viewTermNum];
/*  71 */     this.arrDocView = new double[this.docNum][viewNum];
/*  72 */     this.arrThemeProb = new double[viewNum][this.themeNum][this.themeTermNum];
/*  73 */     this.arrDocTheme = new double[this.docNum][viewNum][this.themeNum];
/*  74 */     this.arrCommonThemeProb = new double[this.themeNum][this.themeTermNum];
/*     */ 
/*  77 */     readSequence(this.viewIndexReader, arrTermO, arrDocO);
/*  78 */     readSequence(this.topicIndexReader, arrTermP, arrDocP);
/*     */ 
/*  81 */     run(this.seed, arrTermO, arrDocO, arrZO, arrTVCountO, arrTermP, arrDocP, arrZP, arrTVCountP, arrDVCount, 
/*  82 */       arrY, arrX, arrTTViewCount, arrTTComCount, arrTTCount, arrDTCount);
/*     */      int i,j;
/*  85 */     for ( i = 0; i < viewNum; i++) {
/*  86 */       double sum = this.beta * this.viewTermNum;
/*  87 */       for ( j = 0; j < this.viewTermNum; j++)
/*  88 */         sum += arrTVCountO[j][i];
/*  89 */       for (j = 0; j < this.viewTermNum; j++) {
/*  90 */         this.arrViewProb[i][j] = ((arrTVCountO[j][i] + this.beta) / sum);
/*     */       }
/*     */     }
/*     */ 
/*  94 */     for (i = 0; i < this.docNum; i++) {
/*  95 */       double sum = viewNum * this.alpha;
/*  96 */       for ( j = 0; j < viewNum; j++)
/*  97 */         sum += arrDVCount[i][j];
/*  98 */       for (j = 0; j < viewNum; j++) {
/*  99 */         this.arrDocView[i][j] = ((arrDVCount[i][j] + this.alpha) / sum);
/*     */       }
/*     */     }
/*     */ 
/* 103 */     for (i = 0; i < this.themeNum; i++) {
/* 104 */       double sum = this.delta * this.themeTermNum;
/* 105 */       for ( j = 0; j < this.themeTermNum; j++)
/* 106 */         sum += arrTTCount[j][i];
/* 107 */       for (j = 0; j < this.themeTermNum; j++) {
/* 108 */         this.arrCommonThemeProb[i][j] = ((arrTTCount[j][i] + this.delta) / sum);
/*     */       }
/*     */     }
/*     */ 
/* 112 */     for (int k = 0; k < viewNum; k++) {
/* 113 */       for (i = 0; i < this.themeNum; i++) {
/* 114 */         double sum = this.epsilon * this.themeTermNum;
/* 115 */         for ( j = 0; j < this.themeTermNum; j++)
/* 116 */           sum += arrTTViewCount[j][k][i];
/* 117 */         for (j = 0; j < this.themeTermNum; j++) {
/* 118 */           this.arrThemeProb[k][i][j] = ((arrTTViewCount[j][k][i] + this.epsilon) / sum);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 123 */     for (int k = 0; k < this.docNum; k++) {
/* 124 */       for (i = 0; i < viewNum; i++) {
/* 125 */         double sum = this.rho * this.themeNum;
/* 126 */         for ( j = 0; j < this.themeNum; j++)
/* 127 */           sum += arrDTCount[k][i][j];
/* 128 */         for (j = 0; j < this.themeNum; j++) {
/* 129 */           this.arrDocTheme[k][i][j] = ((arrDTCount[k][i][j] + this.rho) / sum);
/*     */         }
/*     */       }
/*     */     }
/* 133 */     return true;
/*     */   }
/*     */ 
/*     */   private void run(int seed, int[] arrTermO, int[] arrDocO, int[] arrZO, int[][] arrTVCountO, int[] arrTermP, int[] arrDocP, int[] arrZP, int[][] arrTVCountP, int[][] arrDVCount, int[] arrY, int[] arrX, int[][][] arrTTViewCount, int[][][] arrTTComCount, int[][] arrTTCount, int[][][] arrDTCount)
/*     */   {
	               int i,j;
/* 147 */     int[] arrViewCount = new int[this.viewNum];
/* 148 */     int[] arrTopicCount = new int[this.themeNum];
/* 149 */     int[][] arrViewTopicCount = new int[this.viewNum][this.themeNum];
/* 150 */     int[] arrOrderO = new int[this.tokenNumO];
/* 151 */     int[] arrOrderP = new int[this.tokenNumP];
/* 152 */     Random random = new Random();
/* 153 */     if (seed >= 0) {
/* 154 */       random.setSeed(seed);
/*     */     }
/* 156 */     printStatus(new Date().toString() + " Starting random initialization...");
/* 157 */     for ( i = 0; i < this.tokenNumO; i++) {
/* 158 */       int view = random.nextInt(this.viewNum);
/* 159 */       arrZO[i] = view;
/* 160 */       int termIndex = arrTermO[i];
/* 161 */       int docIndex = arrDocO[i];
/* 162 */       arrTVCountO[termIndex][view] += 1;
/* 163 */       arrDVCount[docIndex][view] += 1;
/* 164 */       arrViewCount[view] += 1;
/*     */     }
/* 166 */     for (i = 0; i < this.tokenNumP; i++) {
/* 167 */       int view = random.nextInt(this.viewNum);
/* 168 */       arrZP[i] = view;
/* 169 */       int termIndex = arrTermP[i];
/* 170 */       int docIndex = arrDocP[i];
/* 171 */       arrTVCountP[termIndex][view] += 1;
/* 172 */       arrDVCount[docIndex][view] += 1;
/* 173 */       arrViewCount[view] += 1;
/*     */     }
/*     */ 
/* 176 */     for (i = 0; i < this.tokenNumP; i++) {
/* 177 */       int topic = random.nextInt(this.themeNum);
/* 178 */       int view = arrZP[i];
/* 179 */       arrY[i] = topic;
/* 180 */       int termIndex = arrTermP[i];
/* 181 */       int docIndex = arrDocP[i];
/* 182 */       arrTTViewCount[termIndex][view][topic] += 1;
/* 183 */       arrDTCount[docIndex][view][topic] += 1;
/* 184 */       arrViewTopicCount[view][topic] += 1;
/* 185 */       arrX[i] = 1;
/*     */     }
/*     */ 
/* 189 */     printStatus(new Date().toString() + " Determining random update sequence...");
/* 190 */     for (i = 0; i < this.tokenNumO; i++) arrOrderO[i] = i;
/* 191 */     for (i = 0; i < this.tokenNumO - 1; i++)
/*     */     {
/* 193 */       int k = i + random.nextInt(this.tokenNumO - i);
/*     */ 
/* 195 */        j = arrOrderO[k];
/* 196 */       arrOrderO[k] = arrOrderO[i];
/* 197 */       arrOrderO[i] = j;
/*     */     }
/*     */ 
/* 200 */     for (i = 0; i < this.tokenNumP; i++) arrOrderP[i] = i;
/* 201 */     for (i = 0; i < this.tokenNumP - 1; i++)
/*     */     {
/* 203 */       int k = i + random.nextInt(this.tokenNumP - i);
/*     */ 
/* 205 */        j = arrOrderP[k];
/* 206 */       arrOrderP[k] = arrOrderP[i];
/* 207 */       arrOrderP[i] = j;
/*     */     }
/*     */ 
/* 210 */     for (int iter = 0; iter < this.iterations; iter++) {
/* 211 */       printStatus(new Date().toString() + " Iteration #" + (iter + 1));
/*     */ 
/* 214 */       for (int k = 0; k < this.tokenNumO; k++) {
/* 215 */         i = arrOrderO[k];
/* 216 */         int termIndex = arrTermO[i];
/* 217 */         int docIndex = arrDocO[i];
/* 218 */         int view = arrZO[i];
/*     */ 
/* 221 */         arrViewCount[view] -= 1;
/* 222 */         arrTVCountO[termIndex][view] -= 1;
/* 223 */         arrDVCount[docIndex][view] -= 1;
/*     */ 
/* 226 */         view = sampleView(random, arrTVCountO[termIndex], arrDVCount[docIndex], arrViewCount);
/*     */ 
/* 229 */         arrZO[i] = view;
/* 230 */         arrTVCountO[termIndex][view] += 1;
/* 231 */         arrDVCount[docIndex][view] += 1;
/* 232 */         arrViewCount[view] += 1;
/*     */       }
/*     */ 
/* 236 */       for (int k = 0; k < this.tokenNumP; k++) {
/* 237 */         i = arrOrderP[k];
/* 238 */         int termIndex = arrTermP[i];
/* 239 */         int docIndex = arrDocP[i];
/* 240 */         int topic = arrY[i];
/* 241 */         int view = arrZP[i];
/* 242 */         int status = arrX[i];
/*     */ 
/* 246 */         arrViewCount[view] -= 1;
/* 247 */         arrTVCountP[termIndex][view] -= 1;
/* 248 */         arrDVCount[docIndex][view] -= 1;
/* 249 */         if (status == 0) {
/* 250 */           arrTTComCount[termIndex][view][topic] -= 1;
/*     */         }
/*     */         else {
/* 253 */           arrTTViewCount[termIndex][view][topic] -= 1;
/* 254 */           arrViewTopicCount[view][topic] -= 1;
/*     */         }
/* 256 */         arrDTCount[docIndex][view][topic] -= 1;
/*     */ 
/* 259 */         view = sampleView(random, arrTVCountP[termIndex], arrDVCount[docIndex], arrViewCount);
/*     */ 
/* 262 */         arrZP[i] = view;
/* 263 */         arrTVCountP[termIndex][view] += 1;
/* 264 */         arrDVCount[docIndex][view] += 1;
/* 265 */         arrViewCount[view] += 1;
/* 266 */         if (status == 0) {
/* 267 */           arrTTComCount[termIndex][view][topic] += 1;
/*     */         } else {
/* 269 */           arrTTViewCount[termIndex][view][topic] += 1;
/* 270 */           arrViewTopicCount[view][topic] += 1;
/*     */         }
/* 272 */         arrDTCount[docIndex][view][topic] += 1;
/*     */ 
/* 276 */         if (status == 0)
/*     */         {
/* 279 */           arrTopicCount[topic] -= 1;
/* 280 */           arrTTCount[termIndex][topic] -= 1;
/* 281 */           arrTTComCount[termIndex][view][topic] -= 1;
/* 282 */           arrDTCount[docIndex][view][topic] -= 1;
/*     */ 
/* 285 */           topic = sampleCommonTopic(random, arrTTCount[termIndex], arrDTCount[docIndex][view], arrTopicCount);
/*     */ 
/* 288 */           arrY[i] = topic;
/* 289 */           arrTTComCount[termIndex][view][topic] += 1;
/* 290 */           arrTTCount[termIndex][topic] += 1;
/* 291 */           arrDTCount[docIndex][view][topic] += 1;
/* 292 */           arrTopicCount[topic] += 1;
/*     */         }
/*     */         else
/*     */         {
/* 297 */           arrViewTopicCount[view][topic] -= 1;
/* 298 */           arrTTViewCount[termIndex][view][topic] -= 1;
/* 299 */           arrDTCount[docIndex][view][topic] -= 1;
/*     */ 
/* 302 */           topic = sampleViewTopic(random, arrTTViewCount[termIndex][view], arrDTCount[docIndex][view], arrViewTopicCount[view]);
/*     */ 
/* 305 */           arrY[i] = topic;
/* 306 */           arrTTViewCount[termIndex][view][topic] += 1;
/* 307 */           arrDTCount[docIndex][view][topic] += 1;
/* 308 */           arrViewTopicCount[view][topic] += 1;
/*     */         }
/*     */ 
/* 313 */         if (status == 0) {
/* 314 */           arrTopicCount[topic] -= 1;
/* 315 */           arrTTCount[termIndex][topic] -= 1;
/* 316 */           arrTTComCount[termIndex][view][topic] -= 1;
/* 317 */           arrDTCount[docIndex][view][topic] -= 1;
/*     */         }
/*     */         else {
/* 320 */           arrViewTopicCount[view][topic] -= 1;
/* 321 */           arrTTViewCount[termIndex][view][topic] -= 1;
/* 322 */           arrDTCount[docIndex][view][topic] -= 1;
/*     */         }
/*     */ 
/* 326 */         status = sampleStatus(random, arrTTViewCount[termIndex][view][topic], arrViewTopicCount[view][topic], 
/* 327 */           arrTTComCount[termIndex][view][topic], arrTTCount[termIndex][topic], arrTopicCount[topic]);
/*     */ 
/* 330 */         arrX[i] = status;
/* 331 */         if (status == 0) {
/* 332 */           arrTTComCount[termIndex][view][topic] += 1;
/* 333 */           arrTTCount[termIndex][topic] += 1;
/* 334 */           arrDTCount[docIndex][view][topic] += 1;
/* 335 */           arrTopicCount[topic] += 1;
/*     */         }
/*     */         else {
/* 338 */           arrTTViewCount[termIndex][view][topic] += 1;
/* 339 */           arrDTCount[docIndex][view][topic] += 1;
/* 340 */           arrViewTopicCount[view][topic] += 1;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int sampleView(Random random, int[] arrTVCount, int[] arrDVCount, int[] arrViewCount)
/*     */   {
/* 351 */     double totalProb = 0.0D;
/* 352 */     double[] arrProb = new double[this.viewNum];
/* 353 */     for (int j = 0; j < this.viewNum; j++) {
/* 354 */       arrProb[j] = ((arrTVCount[j] + this.beta) / (arrViewCount[j] + this.wBeta) * (arrDVCount[j] + this.alpha));
/* 355 */       totalProb += arrProb[j];
/*     */     }
/* 357 */     double r = totalProb * random.nextDouble();
/* 358 */     double max = arrProb[0];
/* 359 */     int view = 0;
/* 360 */     while (r > max) {
/* 361 */       view++;
/* 362 */       max += arrProb[view];
/*     */     }
/* 364 */     return view;
/*     */   }
/*     */ 
/*     */   private int sampleCommonTopic(Random random, int[] arrTTCount, int[] arrDTCount, int[] arrTopicCount)
/*     */   {
/* 372 */     double totalProb = 0.0D;
/* 373 */     double[] arrProb = new double[this.themeNum];
/* 374 */     for (int j = 0; j < this.themeNum; j++) {
/* 375 */       arrProb[j] = ((arrTTCount[j] + this.delta) / (arrTopicCount[j] + this.wDelta) * (arrDTCount[j] + this.rho));
/* 376 */       totalProb += arrProb[j];
/*     */     }
/* 378 */     double r = totalProb * random.nextDouble();
/* 379 */     double max = arrProb[0];
/* 380 */     int topic = 0;
/* 381 */     while (r > max) {
/* 382 */       topic++;
/* 383 */       max += arrProb[topic];
/*     */     }
/* 385 */     return topic;
/*     */   }
/*     */ 
/*     */   private int sampleViewTopic(Random random, int[] arrTTViewCount, int[] arrDTCount, int[] arrViewTopicCount)
/*     */   {
/* 393 */     double totalProb = 0.0D;
/* 394 */     double[] arrProb = new double[this.themeNum];
/* 395 */     for (int j = 0; j < this.themeNum; j++) {
/* 396 */       arrProb[j] = ((arrTTViewCount[j] + this.epsilon) / (arrViewTopicCount[j] + this.wEpsilon) * (arrDTCount[j] + this.rho));
/* 397 */       totalProb += arrProb[j];
/*     */     }
/* 399 */     double r = totalProb * random.nextDouble();
/* 400 */     double max = arrProb[0];
/* 401 */     int topic = 0;
/* 402 */     while (r > max) {
/* 403 */       topic++;
/* 404 */       max += arrProb[topic];
/*     */     }
/* 406 */     return topic;
/*     */   }
/*     */ 
/*     */   private int sampleStatus(Random random, int ttViewCount, int sumTTViewCount, int ttCommonCount, int ttCount, int sumTTCount)
/*     */   {
/* 412 */     double x0 = (this.gamma0 + ttCommonCount) * (ttCount + this.delta) / (sumTTCount + this.wDelta);
/* 413 */     double x1 = (this.gamma1 + ttViewCount) * (ttViewCount + this.epsilon) / (sumTTViewCount + this.wEpsilon);
/* 414 */     x0 /= (x0 + x1);
/* 415 */     double prob = random.nextDouble();
/* 416 */     if (prob <= x0) {
/* 417 */       return 0;
/*     */     }
/* 419 */     return 1;
/*     */   }
/*     */ 
/*     */   private void readSequence(IndexReader indexReader, int[] arrTerm, int[] arrDoc)
/*     */   {
/* 426 */     int docNum = indexReader.getCollection().getDocNum();
/* 427 */     int count = 0;
/* 428 */     for (int i = 0; i < docNum; i++) {
/* 429 */       int[] arrIndex = indexReader.getTermIndexList(i);
/* 430 */       int[] arrFreq = indexReader.getTermFrequencyList(i);
/* 431 */       if ((arrIndex != null) && (arrIndex.length != 0))
/* 432 */         for (int j = 0; j < arrIndex.length; j++) {
/* 433 */           for (int k = 0; k < arrFreq[j]; k++) {
/* 434 */             arrTerm[(count + k)] = arrIndex[j];
/* 435 */             arrDoc[(count + k)] = i;
/*     */           }
/* 437 */           count += arrFreq[j];
/*     */         }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.topicmodel.TwoDimensionGibbsLDA
 * JD-Core Version:    0.6.2
 */