/*     */ package edu.drexel.cis.dragon.ir.topicmodel;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ import java.util.Date;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class SimpleMixtureModel extends AbstractTopicModel
/*     */ {
/*     */   protected DoubleVector bkgModel;
/*     */   protected double bkgCoefficient;
/*     */ 
/*     */   public SimpleMixtureModel(IndexReader indexReader, double bkgCoefficient)
/*     */   {
/*  21 */     this(indexReader, null, bkgCoefficient);
/*     */   }
/*     */ 
/*     */   public SimpleMixtureModel(IndexReader indexReader, DoubleVector bkgModel, double bkgCoefficient) {
/*  25 */     super(indexReader);
/*  26 */     if (bkgModel == null)
/*  27 */       bkgModel = getBkgModel(indexReader);
/*     */     else
/*  29 */       bkgModel = bkgModel.copy();
/*  30 */     this.bkgModel = bkgModel;
/*  31 */     this.bkgCoefficient = bkgCoefficient;
/*  32 */     this.bkgModel.multiply(bkgCoefficient);
/*     */   }
/*     */ 
/*     */   public boolean estimateModel(int topicNum) {
/*  36 */     return estimateModel(null, topicNum);
/*     */   }
/*     */ 
/*     */   public boolean estimateModel(int[] arrDoc, int topicNum)
/*     */   {
/*  48 */     this.themeNum = topicNum;
/*  49 */     this.termNum = this.indexReader.getCollection().getTermNum();
/*  50 */     if (arrDoc == null)
/*  51 */       this.docNum = this.indexReader.getCollection().getDocNum();
/*     */     else {
/*  53 */       this.docNum = arrDoc.length;
/*     */     }
/*  55 */     this.arrThemeTerm = new double[this.themeNum][this.termNum];
/*  56 */     double[][] arrTempProb = new double[this.themeNum][this.termNum];
/*  57 */     this.arrDocTheme = new double[this.docNum][this.themeNum];
/*  58 */     double[] arrDocWeightSum = new double[this.themeNum];
/*     */ 
/*  61 */     initialize(this.termNum, this.themeNum, this.docNum, this.arrThemeTerm, this.arrDocTheme);
/*     */ 
/*  64 */     printStatus("Estimating the coefficients of simple mixture model...");
/*  65 */     for (int k = 0; k < this.iterations; k++) {
/*  66 */       printStatus(new Date().toString() + " Iteration #" + (k + 1));
/*  67 */       for (int i = 0; i < this.themeNum; i++) {
/*  68 */         for (int j = 0; j < this.termNum; j++)
/*  69 */           arrTempProb[i][j] = 0.0D;
/*     */       }
/*  71 */       for (int i = 0; i < this.docNum; i++)
/*     */       {
/*     */         int[] arrFreq;
/*     */         int[] arrIndex;
/*  72 */         if (arrDoc == null) {
/*  73 */          arrIndex = this.indexReader.getTermIndexList(i);
/*  74 */           arrFreq = this.indexReader.getTermFrequencyList(i);
/*     */         }
/*     */         else {
/*  77 */           arrIndex = this.indexReader.getTermIndexList(arrDoc[i]);
/*  78 */           arrFreq = this.indexReader.getTermFrequencyList(arrDoc[i]);
/*     */         }
/*  80 */         for (int m = 0; m < this.themeNum; m++) arrDocWeightSum[m] = 0.0D;
/*     */ 
/*  82 */         for (int j = 0; j < arrIndex.length; j++) {
/*  83 */           int termIndex = arrIndex[j];
/*  84 */           double themeProbSum = 0.0D;
/*  85 */           for (int m = 0; m < this.themeNum; m++) {
/*  86 */             themeProbSum += this.arrThemeTerm[m][termIndex] * this.arrDocTheme[i][m];
/*     */           }
/*  88 */           double bkgProb = this.bkgModel.get(termIndex) / (themeProbSum * (1.0D - this.bkgCoefficient) + this.bkgModel.get(termIndex));
/*     */ 
/*  90 */           for (int m = 0; m < this.themeNum; m++)
/*     */           {
/*     */             double themeProb;
/*  91 */             if (themeProbSum != 0.0D)
/*     */             {
/*  93 */               themeProb = this.arrThemeTerm[m][termIndex] * this.arrDocTheme[i][m] / themeProbSum;
/*     */             }
/*  95 */             else themeProb = 0.0D;
/*  96 */             double termProb = arrFreq[j] * themeProb;
/*  97 */             arrDocWeightSum[m] += termProb;
/*  98 */             arrTempProb[m][termIndex] += termProb * (1.0D - bkgProb);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 103 */         double docWeightSum = 0.0D;
/* 104 */         for (int m = 0; m < this.themeNum; m++)
/* 105 */           docWeightSum += arrDocWeightSum[m];
/* 106 */         if (docWeightSum > 0.0D) {
/* 107 */           for (int m = 0; m < this.themeNum; m++)
/* 108 */             this.arrDocTheme[i][m] = (arrDocWeightSum[m] / docWeightSum);
/*     */         }
/*     */         else {
/* 111 */           for (int m = 0; m < this.themeNum; m++) {
/* 112 */             this.arrDocTheme[i][m] = 0.0D;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 117 */       for (int i = 0; i < this.themeNum; i++) {
/* 118 */         double termProbSum = 0.0D;
/* 119 */         for (int j = 0; j < this.termNum; j++)
/* 120 */           termProbSum += arrTempProb[i][j];
/* 121 */         for (int j = 0; j < this.termNum; j++)
/* 122 */           this.arrThemeTerm[i][j] = (arrTempProb[i][j] / termProbSum);
/*     */       }
/*     */     }
/* 125 */     printStatus("");
/* 126 */     return true;
/*     */   }
/*     */ 
/*     */   protected void initialize(int termNum, int themeNum, int docNum, double[][] arrModel, double[][] arrDocMembership)
/*     */   {
/* 134 */     double termProb = 1.0D / termNum;
/* 135 */     for (int i = 0; i < themeNum; i++) {
/* 136 */       for (int j = 0; j < termNum; j++)
/* 137 */         arrModel[i][j] = termProb;
/*     */     }
/* 139 */     Random random = new Random(this.seed);
/* 140 */     for (int i = 0; i < docNum; i++) {
/* 141 */       double docProb = 0.0D;
/* 142 */       for (int j = 0; j < themeNum; j++) {
/* 143 */         arrDocMembership[i][j] = random.nextDouble();
/* 144 */         docProb += arrDocMembership[i][j];
/*     */       }
/* 146 */       for (int j = 0; j < themeNum; j++)
/* 147 */         arrDocMembership[i][j] /= docProb;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.topicmodel.SimpleMixtureModel
 * JD-Core Version:    0.6.2
 */