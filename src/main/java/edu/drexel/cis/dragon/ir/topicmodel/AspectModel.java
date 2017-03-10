/*     */ package edu.drexel.cis.dragon.ir.topicmodel;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import java.util.Date;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class AspectModel extends AbstractTopicModel
/*     */ {
/*     */   public AspectModel(IndexReader indexReader)
/*     */   {
/*  18 */     super(indexReader);
/*     */   }
/*     */ 
/*     */   public boolean estimateModel(int topicNum) {
/*  22 */     return estimateModel(null, topicNum);
/*     */   }
/*     */ 
/*     */   public boolean estimateModel(int[] arrDoc, int topicNum)
/*     */   {
/*  34 */     this.themeNum = topicNum;
/*  35 */     this.termNum = this.indexReader.getCollection().getTermNum();
/*  36 */     if (arrDoc == null)
/*  37 */       this.docNum = this.indexReader.getCollection().getDocNum();
/*     */     else {
/*  39 */       this.docNum = arrDoc.length;
/*     */     }
/*  41 */     this.arrThemeTerm = new double[this.themeNum][this.termNum];
/*  42 */     double[][] arrTempProb = new double[this.themeNum][this.termNum];
/*  43 */     this.arrDocTheme = new double[this.docNum][this.themeNum];
/*  44 */     double[] arrDocWeightSum = new double[this.themeNum];
/*     */ 
/*  47 */     initialize(this.termNum, this.themeNum, this.docNum, this.arrThemeTerm, this.arrDocTheme);
/*     */ 
/*  50 */     printStatus("Estimating the coefficients of simple mixture model...");
/*  51 */     for (int k = 0; k < this.iterations; k++) {
/*  52 */       printStatus(new Date().toString() + " Iteration #" + (k + 1));
/*  53 */       for (int i = 0; i < this.themeNum; i++) {
/*  54 */         for (int j = 0; j < this.termNum; j++)
/*  55 */           arrTempProb[i][j] = 0.0D;
/*     */       }
/*  57 */       for (int i = 0; i < this.docNum; i++)
/*     */       {int m;
/*     */         int[] arrFreq;
/*     */         int[] arrIndex;
/*  58 */         if (arrDoc == null) {
/*  59 */          arrIndex = this.indexReader.getTermIndexList(i);
/*  60 */           arrFreq = this.indexReader.getTermFrequencyList(i);
/*     */         }
/*     */         else {
/*  63 */           arrIndex = this.indexReader.getTermIndexList(arrDoc[i]);
/*  64 */           arrFreq = this.indexReader.getTermFrequencyList(arrDoc[i]);
/*     */         }
/*  66 */         for ( m = 0; m < this.themeNum; m++) arrDocWeightSum[m] = 0.0D;
/*     */ 
/*  68 */         for (int j = 0; j < arrIndex.length; j++) {
/*  69 */           int termIndex = arrIndex[j];
/*  70 */           double themeProbSum = 0.0D;
/*  71 */           for (m = 0; m < this.themeNum; m++) {
/*  72 */             themeProbSum += this.arrThemeTerm[m][termIndex] * this.arrDocTheme[i][m];
/*     */           }
/*     */ 
/*  75 */           for (m = 0; m < this.themeNum; m++)
/*     */           {
/*     */             double themeProb;
/*  76 */             if (themeProbSum != 0.0D)
/*     */             {
/*  78 */               themeProb = this.arrThemeTerm[m][termIndex] * this.arrDocTheme[i][m] / themeProbSum;
/*     */             }
/*  80 */             else themeProb = 0.0D;
/*  81 */             double termProb = arrFreq[j] * themeProb;
/*  82 */             arrDocWeightSum[m] += termProb;
/*  83 */             arrTempProb[m][termIndex] += termProb;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*  88 */         double docWeightSum = 0.0D;
/*  89 */         for (m = 0; m < this.themeNum; m++)
/*  90 */           docWeightSum += arrDocWeightSum[m];
/*  91 */         if (docWeightSum > 0.0D) {
/*  92 */           for (m = 0; m < this.themeNum; m++)
/*  93 */             this.arrDocTheme[i][m] = (arrDocWeightSum[m] / docWeightSum);
/*     */         }
/*     */         else {
/*  96 */           for (m = 0; m < this.themeNum; m++) {
/*  97 */             this.arrDocTheme[i][m] = 0.0D;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 102 */       for (int i = 0; i < this.themeNum; i++) {
/* 103 */         double termProbSum = 0.0D;
/* 104 */         for (int j = 0; j < this.termNum; j++)
/* 105 */           termProbSum += arrTempProb[i][j];
/* 106 */         for (int j = 0; j < this.termNum; j++)
/* 107 */           this.arrThemeTerm[i][j] = (arrTempProb[i][j] / termProbSum);
/*     */       }
/*     */     }
/* 110 */     printStatus("");
/* 111 */     return true;
/*     */   }
/*     */ 
/*     */   protected void initialize(int termNum, int themeNum, int docNum, double[][] arrModel, double[][] arrDocMembership)
/*     */   {
/* 119 */     double termProb = 1.0D / termNum;
/* 120 */     for (int i = 0; i < themeNum; i++) {
/* 121 */       for (int j = 0; j < termNum; j++)
/* 122 */         arrModel[i][j] = termProb;
/*     */     }
/* 124 */     Random random = new Random(this.seed);
/* 125 */     for (int i = 0; i < docNum; i++) {
/* 126 */       double docProb = 0.0D;
/* 127 */       for (int j = 0; j < themeNum; j++) {
/* 128 */         arrDocMembership[i][j] = random.nextDouble();
/* 129 */         docProb += arrDocMembership[i][j];
/*     */       }
/* 131 */       for (int j = 0; j < themeNum; j++)
/* 132 */         arrDocMembership[i][j] /= docProb;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.topicmodel.AspectModel
 * JD-Core Version:    0.6.2
 */