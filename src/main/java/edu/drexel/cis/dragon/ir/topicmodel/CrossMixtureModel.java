/*     */ package edu.drexel.cis.dragon.ir.topicmodel;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class CrossMixtureModel extends AbstractModel
/*     */ {
/*     */   protected IntSparseMatrix[] arrTopicReader;
/*     */   protected double[] bkgModel;
/*     */   protected double bkgCoefficient;
/*     */   protected double comCoefficient;
/*     */   protected int themeNum;
/*     */   protected int collectionNum;
/*     */   protected int maxTermNum;
/*     */   protected int maxDocNum;
/*     */   private double[][][] arrDocWeight;
/*     */   private double[][][] arrProb;
/*     */   private double[][] arrCommonProb;
/*     */ 
/*     */   public CrossMixtureModel(IntSparseMatrix[] arrTopicMatrix, int themeNum, double[] bkgModel, double bkgCoefficient, double comCoefficient)
/*     */   {
/*  26 */     this.arrTopicReader = arrTopicMatrix;
/*  27 */     this.themeNum = themeNum;
/*  28 */     this.collectionNum = this.arrTopicReader.length;
/*  29 */     this.bkgModel = new double[bkgModel.length];
/*  30 */     this.comCoefficient = comCoefficient;
/*  31 */     for (int i = 0; i < bkgModel.length; i++)
/*  32 */       this.bkgModel[i] = (bkgModel[i] * bkgCoefficient);
/*  33 */     this.bkgCoefficient = bkgCoefficient;
/*  34 */     this.maxTermNum = this.arrTopicReader[0].columns();
/*  35 */     this.maxDocNum = this.arrTopicReader[0].rows();
				   int i;
/*  36 */     for (i = 1; i < this.arrTopicReader.length; i++) {
/*  37 */       if (this.arrTopicReader[i].columns() > this.maxTermNum)
/*  38 */         this.maxTermNum = this.arrTopicReader[i].columns();
/*  39 */       if (this.arrTopicReader[i].rows() > this.maxDocNum)
/*  40 */         this.maxDocNum = this.arrTopicReader[i].rows();
/*     */     }
/*     */   }
/*     */ 
/*     */   public double[][][] getModels() {
/*  45 */     return this.arrProb;
/*     */   }
/*     */ 
/*     */   public double[][] getCommonModels() {
/*  49 */     return this.arrCommonProb;
/*     */   }
/*     */ 
/*     */   public double[][][] getDocMemberships() {
/*  53 */     return this.arrDocWeight;
/*     */   }
/*     */ 
/*     */   public boolean estimateModel()
/*     */   {
/*  67 */     this.arrProb = new double[this.collectionNum][this.themeNum][this.maxTermNum];
/*  68 */     this.arrCommonProb = new double[this.themeNum][this.maxTermNum];
/*  69 */     this.arrDocWeight = new double[this.collectionNum][this.themeNum][this.maxDocNum];
/*  70 */     double[][][] arrTempProb = new double[this.collectionNum][this.themeNum][this.maxTermNum];
/*  71 */     double[][] arrTempCommonProb = new double[this.themeNum][this.maxTermNum];
/*  72 */     double[] arrDocWeightSum = new double[this.themeNum];
/*     */ 
/*  75 */     initialize(this.maxTermNum, this.collectionNum, this.themeNum, this.maxDocNum, this.arrCommonProb, this.arrProb, this.arrDocWeight);
/*     */       int i;
/*  78 */     printStatus("Estimating the coefficients of simple mixture model...");
/*  79 */     for (int k = 0; k < this.iterations; k++) {
/*  80 */       printStatus("Iteration #" + (k + 1));
/*  81 */       for ( i = 0; i < this.themeNum; i++)
/*  82 */         for (int j = 0; j < this.maxTermNum; j++)
/*  83 */           arrTempCommonProb[i][j] = 0.0D;
/*  84 */       for (int n = 0; n < this.collectionNum; n++) {
/*  85 */         for (i = 0; i < this.themeNum; i++)
/*  86 */           for (int j = 0; j < this.maxTermNum; j++)
/*  87 */             arrTempProb[n][i][j] = 0.0D;
/*     */       }
/*  89 */       for (int n = 0; n < this.collectionNum; n++) {
/*  90 */         int docNum = this.arrTopicReader[n].rows();
/*     */ 
/*  92 */         for (i = 0; i < docNum; i++) {
/*  93 */           int[] arrIndex = this.arrTopicReader[n].getNonZeroColumnsInRow(i);
/*  94 */           int[] arrFreq = this.arrTopicReader[n].getNonZeroIntScoresInRow(i);
/*  95 */           for (int m = 0; m < this.themeNum; m++) {
/*  96 */             arrDocWeightSum[m] = 0.0D;
/*     */           }
/*  98 */           for (int j = 0; j < arrIndex.length; j++) {
/*  99 */             int termIndex = arrIndex[j];
/* 100 */             double themeProbSum = 0.0D;
/* 101 */             for (int m = 0; m < this.themeNum; m++) {
/* 102 */               themeProbSum += (this.comCoefficient * this.arrCommonProb[m][termIndex] + (1.0D - this.comCoefficient) * this.arrProb[n][m][termIndex]) * this.arrDocWeight[n][m][i];
/*     */             }
/* 104 */             double bkgProb = this.bkgModel[termIndex] / (themeProbSum * (1.0D - this.bkgCoefficient) + this.bkgModel[termIndex]);
/*     */ 
/* 106 */             for (int m = 0; m < this.themeNum; m++)
/*     */             {
/*     */               double themeProb;
/* 107 */               if (themeProbSum == 0.0D)
/* 108 */                 themeProb = 0.0D;
/*     */               else
/* 110 */                 themeProb = (this.comCoefficient * this.arrCommonProb[m][termIndex] + (1.0D - this.comCoefficient) * this.arrProb[n][m][termIndex]) * this.arrDocWeight[n][m][i] / themeProbSum;
/* 111 */               double comThemeProb = this.comCoefficient * this.arrCommonProb[m][termIndex] + (1.0D - this.comCoefficient) * this.arrProb[n][m][termIndex];
/* 112 */               if (comThemeProb > 0.0D)
/* 113 */                 comThemeProb = this.comCoefficient * this.arrCommonProb[m][termIndex] / comThemeProb;
/*     */               else
/* 115 */                 comThemeProb = 0.0D;
/* 116 */               double termProb = arrFreq[j] * themeProb;
/* 117 */               arrDocWeightSum[m] += termProb;
/* 118 */               termProb *= (1.0D - bkgProb);
/* 119 */               arrTempProb[n][m][termIndex] += termProb * (1.0D - comThemeProb);
/* 120 */               arrTempCommonProb[m][termIndex] += termProb * comThemeProb;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 125 */           double docWeightSum = 0.0D;
/* 126 */           for (int m = 0; m < this.themeNum; m++) {
/* 127 */             docWeightSum += arrDocWeightSum[m];
/*     */           }
/* 129 */           if (docWeightSum > 0.0D) {
/* 130 */             for (int m = 0; m < this.themeNum; m++)
/* 131 */               this.arrDocWeight[n][m][i] = (arrDocWeightSum[m] / docWeightSum);
/*     */           }
/*     */           else {
/* 134 */             for (int m = 0; m < this.themeNum; m++) {
/* 135 */               this.arrDocWeight[n][m][i] = 0.0D;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 141 */       for (i = 0; i < this.themeNum; i++)
/*     */       {
/* 143 */         double termProbSum = 0.0D;
/* 144 */         for (int j = 0; j < this.maxTermNum; j++)
/* 145 */           termProbSum += arrTempCommonProb[i][j];
/* 146 */         for (int j = 0; j < this.maxTermNum; j++) {
/* 147 */           this.arrCommonProb[i][j] = (arrTempCommonProb[i][j] / termProbSum);
/*     */         }
/*     */ 
/* 150 */         for (int n = 0; n < this.collectionNum; n++) {
/* 151 */           termProbSum = 0.0D;
/* 152 */           for (int j = 0; j < this.maxTermNum; j++)
/* 153 */             termProbSum += arrTempProb[n][i][j];
/* 154 */           for (int j = 0; j < this.maxTermNum; j++)
/* 155 */             this.arrProb[n][i][j] = (arrTempProb[n][i][j] / termProbSum);
/*     */         }
/*     */       }
/*     */     }
/* 159 */     printStatus("");
/* 160 */     return true;
/*     */   }
/*     */ 
/*     */   protected void initialize(int maxTermNum, int collectionNum, int themeNum, int maxDocNum, double[][] arrCommonModel, double[][][] arrModel, double[][][] arrDocMembership)
/*     */   {  int n, i;
/* 169 */     double termProb = 1.0D / maxTermNum;
/* 170 */     for ( i = 0; i < themeNum; i++) {
/* 171 */       for (int j = 0; j < maxTermNum; j++)
/* 172 */         arrCommonModel[i][j] = termProb;
/*     */     }
/* 174 */     for ( n = 0; n < collectionNum; n++)
/* 175 */       for (i = 0; i < themeNum; i++)
/* 176 */         for (int j = 0; j < maxTermNum; j++)
/* 177 */           arrModel[n][i][j] = termProb;
/*     */     Random random;
/* 179 */     if (this.seed >= 0)
/* 180 */       random = new Random(this.seed);
/*     */     else
/* 182 */       random = new Random();
/* 183 */     for (n = 0; n < collectionNum; n++)
/* 184 */       for (int j = 0; j < maxDocNum; j++) {
/* 185 */         double docProb = 0.0D;
/* 186 */         for (i = 0; i < themeNum; i++) {
/* 187 */           arrDocMembership[n][i][j] = random.nextDouble();
/* 188 */           docProb += arrDocMembership[n][i][j];
/*     */         }
/* 190 */         for (i = 0; i < themeNum; i++)
/* 191 */           arrDocMembership[n][i][j] /= docProb;
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.topicmodel.CrossMixtureModel
 * JD-Core Version:    0.6.2
 */