/*     */ package edu.drexel.cis.dragon.ir.topicmodel;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class TwoDimensionEM extends AbstractTwoDimensionModel
/*     */ {
/*     */   protected DoubleVector viewBkgModel;
/*     */   protected DoubleVector themeBkgModel;
/*     */   protected double viewBkgCoeffi;
/*     */   protected double themeBkgCoeffi;
/*     */   protected double comThemeCoeffi;
/*     */ 
/*     */   public TwoDimensionEM(IndexReader viewIndexReader, IndexReader topicIndexReader, double viewBkgCoeffi, double themeBkgCoeffi, double comThemeCoeffi)
/*     */   {
/*  23 */     this(viewIndexReader, null, viewBkgCoeffi, topicIndexReader, null, themeBkgCoeffi, comThemeCoeffi);
/*     */   }
/*     */ 
/*     */   public TwoDimensionEM(IndexReader viewIndexReader, DoubleVector viewBkgModel, double viewBkgCoeffi, IndexReader topicIndexReader, DoubleVector themeBkgModel, double themeBkgCoeffi, double comThemeCoeffi)
/*     */   {
/*  28 */     super(viewIndexReader, topicIndexReader);
/*  29 */     if (viewBkgModel == null)
/*  30 */       viewBkgModel = getBkgModel(viewIndexReader);
/*     */     else
/*  32 */       viewBkgModel = viewBkgModel.copy();
/*  33 */     this.viewBkgCoeffi = viewBkgCoeffi;
/*  34 */     this.viewBkgModel.multiply(viewBkgCoeffi);
/*     */ 
/*  36 */     if (themeBkgModel == null)
/*  37 */       themeBkgModel = getBkgModel(topicIndexReader);
/*     */     else
/*  39 */       themeBkgModel = themeBkgModel.copy();
/*  40 */     this.themeBkgCoeffi = themeBkgCoeffi;
/*  41 */     this.themeBkgModel.multiply(themeBkgCoeffi);
/*     */ 
/*  43 */     this.comThemeCoeffi = comThemeCoeffi;
/*     */   }
/*     */ 
/*     */   public boolean estimateModel(int viewNum, int topicNum)
/*     */   {
/*  58 */     this.viewNum = viewNum;
/*  59 */     this.themeNum = topicNum;
/*     */ 
/*  61 */     this.arrViewProb = new double[viewNum][this.viewTermNum];
/*  62 */     double[][] arrTempViewProb = new double[viewNum][this.viewTermNum];
/*  63 */     this.arrDocView = new double[this.docNum][viewNum];
/*  64 */     double[] arrDocViewSum = new double[viewNum];
/*  65 */     double[] arrViewProbSum = new double[viewNum];
/*     */ 
/*  67 */     this.arrThemeProb = new double[viewNum][this.themeNum][this.themeTermNum];
/*  68 */     double[][][] arrTempThemeProb = new double[viewNum][this.themeNum][this.themeTermNum];
/*  69 */     this.arrCommonThemeProb = new double[this.themeNum][this.themeTermNum];
/*  70 */     double[][] arrTempThemeCommonProb = new double[this.themeNum][this.themeTermNum];
/*  71 */     this.arrDocTheme = new double[this.docNum][viewNum][this.themeNum];
/*  72 */     double[][] arrDocThemeSum = new double[viewNum][this.themeNum];
/*     */ 
/*  76 */     initialize(this.docNum, this.viewTermNum, viewNum, this.arrViewProb, this.arrDocView, 
/*  77 */       this.themeTermNum, this.themeNum, this.arrCommonThemeProb, this.arrThemeProb, this.arrDocTheme);
/*     */      int m;
/*  80 */     printStatus("Estimating the coefficients of two-dimensional mixture model...");
/*  81 */     for (int k = 0; k < this.iterations; k++) {
/*  82 */       printStatus("Iteration #" + (k + 1));
/*  83 */       for ( m = 0; m < viewNum; m++)
/*  84 */         for (int i = 0; i < this.viewTermNum; i++)
/*  85 */           arrTempViewProb[m][i] = 0.0D;
/*  86 */       for (int l = 0; l < this.themeNum; l++)
/*  87 */         for (int i = 0; i < this.themeTermNum; i++)
/*  88 */           arrTempThemeCommonProb[l][i] = 0.0D;
/*  89 */       for (m = 0; m < viewNum; m++) {
/*  90 */         for (int l = 0; l < this.themeNum; l++)
/*  91 */           for (int i = 0; i < this.themeTermNum; i++)
/*  92 */             arrTempThemeProb[m][l][i] = 0.0D;
/*     */       }
/*  94 */       for (int i = 0; i < this.docNum; i++) {
/*  95 */         for (m = 0; m < viewNum; m++) arrDocViewSum[m] = 0.0D;
/*  96 */         for (m = 0; m < viewNum; m++) {
/*  97 */           for (int l = 0; l < this.themeNum; l++) {
/*  98 */             arrDocThemeSum[m][l] = 0.0D;
/*     */           }
/*     */         }
/* 101 */         int[] arrIndex = this.topicIndexReader.getTermIndexList(i);
/* 102 */         int[] arrFreq = this.topicIndexReader.getTermFrequencyList(i);
/*     */ 
/* 104 */         for (int j = 0; j < arrIndex.length; j++) {
/* 105 */           int termIndex = arrIndex[j];
/* 106 */           double themeProbSum = 0.0D;
/* 107 */           for (m = 0; m < viewNum; m++) {
/* 108 */             arrViewProbSum[m] = 0.0D;
/* 109 */             for (int l = 0; l < this.themeNum; l++)
/* 110 */               arrViewProbSum[m] += ((1.0D - this.comThemeCoeffi) * this.arrThemeProb[m][l][termIndex] + this.comThemeCoeffi * this.arrCommonThemeProb[l][termIndex]) * this.arrDocTheme[i][m][l];
/* 111 */             arrViewProbSum[m] *= this.arrDocView[i][m];
/* 112 */             themeProbSum += arrViewProbSum[m];
/*     */           }
/*     */           double themeBkgProb;
/* 115 */           if (themeProbSum != 0.0D)
/* 116 */             themeBkgProb = this.themeBkgModel.get(termIndex) / (themeProbSum * (1.0D - this.themeBkgCoeffi) + this.themeBkgModel.get(termIndex));
/*     */           else
/* 118 */             themeBkgProb = 0.0D;
/* 119 */           for (m = 0; m < viewNum; m++)
/*     */           {
/* 121 */             if (themeProbSum != 0.0D) {
/* 122 */               arrDocViewSum[m] += arrFreq[j] * arrViewProbSum[m] / themeProbSum;
/*     */             }
/* 124 */             for (int l = 0; l < this.themeNum; l++)
/*     */             {
/*     */               double themeProb;
/* 125 */               if (themeProbSum != 0.0D)
/* 126 */                 themeProb = ((1.0D - this.comThemeCoeffi) * this.arrThemeProb[m][l][termIndex] + 
/* 127 */                   this.comThemeCoeffi * this.arrCommonThemeProb[l][termIndex]) * 
/* 128 */                   this.arrDocView[i][m] * this.arrDocTheme[i][m][l] / themeProbSum;
/*     */               else
/* 130 */                 themeProb = 0.0D;
/* 131 */               double commonThemeProb = (1.0D - this.comThemeCoeffi) * this.arrThemeProb[m][l][termIndex] + 
/* 132 */                 this.comThemeCoeffi * this.arrCommonThemeProb[l][termIndex];
/* 133 */               if (commonThemeProb > 0.0D)
/* 134 */                 commonThemeProb = this.comThemeCoeffi * this.arrCommonThemeProb[l][termIndex] / commonThemeProb;
/*     */               else
/* 136 */                 commonThemeProb = 0.0D;
/* 137 */               double termProb = arrFreq[j] * themeProb;
/* 138 */               arrDocThemeSum[m][l] += termProb;
/* 139 */               termProb *= (1.0D - themeBkgProb);
/* 140 */               arrTempThemeProb[m][l][termIndex] += termProb * (1.0D - commonThemeProb);
/* 141 */               arrTempThemeCommonProb[l][termIndex] += termProb * commonThemeProb;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 146 */         for (m = 0; m < viewNum; m++) {
/* 147 */           double docWeightSum = 0.0D;
/* 148 */           for (int l = 0; l < this.themeNum; l++)
/* 149 */             docWeightSum += arrDocThemeSum[m][l];
/* 150 */           if (docWeightSum > 0.0D) {
/* 151 */             for (int l = 0; l < this.themeNum; l++)
/* 152 */               this.arrDocTheme[i][m][l] = (arrDocThemeSum[m][l] / docWeightSum);
/*     */           }
/*     */           else {
/* 155 */             for (int l = 0; l < this.themeNum; l++) {
/* 156 */               this.arrDocTheme[i][m][l] = 0.0D;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 161 */         arrIndex = this.viewIndexReader.getTermIndexList(i);
/* 162 */         arrFreq = this.viewIndexReader.getTermFrequencyList(i);
/*     */ 
/* 164 */         for (int j = 0; j < arrIndex.length; j++) {
/* 165 */           int termIndex = arrIndex[j];
/* 166 */           double themeProbSum = 0.0D;
/* 167 */           for (m = 0; m < viewNum; m++) {
/* 168 */             themeProbSum += this.arrViewProb[m][termIndex] * this.arrDocView[m][i];
/*     */           }
/* 170 */           double viewBkgProb = this.viewBkgModel.get(termIndex) / (themeProbSum * (1.0D - this.viewBkgCoeffi) + this.viewBkgModel.get(termIndex));
/*     */ 
/* 172 */           for (m = 0; m < viewNum; m++)
/*     */           {
/*     */             double themeProb;
/* 173 */             if (themeProbSum != 0.0D) {
/* 174 */               themeProb = this.arrViewProb[m][termIndex] * this.arrDocView[i][m] / themeProbSum;
/*     */             }
/*     */             else
/* 177 */               themeProb = 0.0D;
/* 178 */             double termProb = arrFreq[j] * themeProb;
/* 179 */             arrDocViewSum[m] += termProb;
/* 180 */             termProb *= (1.0D - viewBkgProb);
/* 181 */             arrTempViewProb[m][termIndex] += termProb;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 186 */         double docWeightSum = 0.0D;
/* 187 */         for (m = 0; m < viewNum; m++)
/* 188 */           docWeightSum += arrDocViewSum[m];
/* 189 */         if (docWeightSum > 0.0D) {
/* 190 */           for (m = 0; m < viewNum; m++)
/* 191 */             this.arrDocView[i][m] = (arrDocViewSum[m] / docWeightSum);
/*     */         }
/*     */         else {
/* 194 */           for (m = 0; m < viewNum; m++) {
/* 195 */             this.arrDocView[i][m] = 0.0D;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 201 */       for (m = 0; m < viewNum; m++) {
/* 202 */         double termProbSum = 0.0D;
/* 203 */         for (int i = 0; i < this.viewTermNum; i++)
/* 204 */           termProbSum += arrTempViewProb[m][i];
/* 205 */         for (int i = 0; i < this.viewTermNum; i++) {
/* 206 */           if (termProbSum != 0.0D)
/* 207 */             this.arrViewProb[m][i] = (arrTempViewProb[m][i] / termProbSum);
/*     */           else {
/* 209 */             this.arrViewProb[m][i] = 0.0D;
/*     */           }
/*     */         }
/*     */       }
/* 213 */       for (int l = 0; l < this.themeNum; l++) {
/* 214 */         double termProbSum = 0.0D;
/* 215 */         for (int i = 0; i < this.themeTermNum; i++) {
/* 216 */           termProbSum += arrTempThemeCommonProb[l][i];
/*     */         }
/* 218 */         for (int i = 0; i < this.themeTermNum; i++) {
/* 219 */           if (termProbSum != 0.0D)
/* 220 */             this.arrCommonThemeProb[l][i] = (arrTempThemeCommonProb[l][i] / termProbSum);
/*     */           else {
/* 222 */             this.arrCommonThemeProb[1][i] = 0.0D;
/*     */           }
/*     */         }
/*     */       }
/* 226 */       for (m = 0; m < viewNum; m++) {
/* 227 */         for (int l = 0; l < this.themeNum; l++) {
/* 228 */           double termProbSum = 0.0D;
/* 229 */           for (int i = 0; i < this.themeTermNum; i++) {
/* 230 */             termProbSum += arrTempThemeProb[m][l][i];
/*     */           }
/* 232 */           for (int i = 0; i < this.themeTermNum; i++)
/* 233 */             if (termProbSum != 0.0D)
/* 234 */               this.arrThemeProb[m][l][i] = (arrTempThemeProb[m][l][i] / termProbSum);
/*     */             else
/* 236 */               this.arrThemeProb[m][1][i] = 0.0D;
/*     */         }
/*     */       }
/*     */     }
/* 240 */     printStatus("");
/* 241 */     return true;
/*     */   }
/*     */ 
/*     */   protected void initialize(int docNum, int viewTermNum, int viewNum, double[][] arrViewModel, double[][] arrDocView, int themeTermNum, int themeNum, double[][] arrThemeCommonModel, double[][][] arrThemeModel, double[][][] arrDocTheme)
/*     */   {
/*     */     Random random;
/* 250 */     if (this.seed >= 0)
/* 251 */       random = new Random(this.seed);
/*     */     else
/* 253 */       random = new Random();
/* 254 */     double termProb = 1.0D / viewTermNum;
/* 255 */     for (int i = 0; i < viewNum; i++)
/* 256 */       for (int j = 0; j < viewTermNum; j++)
/* 257 */         arrViewModel[i][j] = termProb;
/* 258 */     for (int i = 0; i < docNum; i++) {
/* 259 */       double docProb = 0.0D;
/* 260 */       for (int j = 0; j < viewNum; j++) {
/* 261 */         arrDocView[i][j] = random.nextDouble();
/* 262 */         docProb += arrDocView[i][j];
/*     */       }
/* 264 */       for (int j = 0; j < viewNum; j++) {
/* 265 */         arrDocView[i][j] /= docProb;
/*     */       }
/*     */     }
/* 268 */     termProb = 1.0D / themeTermNum;
/* 269 */     for (int j = 0; j < themeNum; j++)
/* 270 */       for (int k = 0; k < themeTermNum; k++)
/* 271 */         arrThemeCommonModel[j][k] = termProb;
/* 272 */     for (int i = 0; i < viewNum; i++)
/* 273 */       for (int j = 0; j < themeNum; j++)
/* 274 */         for (int k = 0; k < themeTermNum; k++)
/* 275 */           arrThemeModel[i][j][k] = termProb;
/* 276 */     for (int i = 0; i < viewNum; i++)
/* 277 */       for (int k = 0; k < docNum; k++) {
/* 278 */         double docProb = 0.0D;
/* 279 */         for (int j = 0; j < themeNum; j++) {
/* 280 */           arrDocTheme[k][i][j] = random.nextDouble();
/* 281 */           docProb += arrDocTheme[k][i][j];
/*     */         }
/* 283 */         for (int j = 0; j < themeNum; j++)
/* 284 */           if (docProb != 0.0D)
/* 285 */             arrDocTheme[k][i][j] /= docProb;
/*     */           else
/* 287 */             arrDocTheme[k][i][j] = 0.0D;
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.topicmodel.TwoDimensionEM
 * JD-Core Version:    0.6.2
 */