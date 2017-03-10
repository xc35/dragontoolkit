/*     */ package edu.drexel.cis.dragon.ml.seqmodel.crf;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.data.Dataset;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.feature.Feature;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.feature.FeatureGenerator;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.model.ModelGraph;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class CollinsBasicTrainer extends AbstractTrainer
/*     */ {
/*     */   protected int topSolutions;
/*     */   protected double beta;
/*     */   protected boolean useUpdated;
/*     */ 
/*     */   public CollinsBasicTrainer(ModelGraph model, FeatureGenerator featureGenerator)
/*     */   {
/*  25 */     super(model, featureGenerator);
/*  26 */     this.topSolutions = Math.min(3, model.getStateNum());
/*  27 */     this.beta = 0.05D;
/*  28 */     this.useUpdated = false;
/*     */   }
/*     */ 
/*     */   public boolean train(Dataset dataset)
/*     */   {
/*  40 */     dataset.startScan();
/*  41 */     while (dataset.hasNext()) {
/*  42 */       this.model.mapLabelToState(dataset.next());
/*     */     }
/*     */ 
/*  45 */     if (!this.featureGenerator.train(dataset))
/*  46 */       return false;
/*  47 */     int featureNum = this.featureGenerator.getFeatureNum();
/*  48 */     this.lambda = new double[featureNum];
/*  49 */     double[] lambdaAvg = new double[featureNum];
/*  50 */     double[] lambdaSum = new double[featureNum];
/*  51 */     MathUtil.initArray(this.lambda, 0.0D);
/*  52 */     MathUtil.initArray(lambdaAvg, 0.0D);
/*  53 */     MathUtil.initArray(this.lambda, 0.0D);
/*  54 */     Labeler labeler = getLabeler();
/*  55 */     DataSequence[] solutions = new DataSequence[this.topSolutions];
/*  56 */     int[] autoStartPos = new int[this.topSolutions];
/*  57 */     int trainingCount = 0;
/*     */ 
/*  59 */     for (int t = 0; t < this.maxIteration; t++) {
/*  60 */       int numErrs = 0;
/*  61 */       dataset.startScan();
/*  62 */       while (dataset.hasNext()) {
/*  63 */         if (trainingCount > 0) {
/*  64 */           MathUtil.copyArray(lambdaSum, lambdaAvg);
/*  65 */           MathUtil.multiArray(lambdaAvg, 1.0D / trainingCount);
/*     */         }
/*  67 */         MathUtil.initArray(autoStartPos, 0);
/*  68 */         DataSequence manualSeq = dataset.next();
/*  69 */         DataSequence autoSeq = manualSeq.copy();
/*  70 */         labeler.label(autoSeq, this.useUpdated ? lambdaAvg : this.lambda);
/*  71 */         double correctScore = getSequenceScore(manualSeq, this.useUpdated ? lambdaAvg : this.lambda);
/*  72 */         int solutionNum = 0;
/*  73 */         for (int k = 0; k < this.topSolutions; k++) {
/*  74 */           autoSeq = manualSeq.copy();
/*  75 */           double curScore = labeler.getBestSolution(autoSeq, k);
/*  76 */           if (curScore < correctScore * (1.0D - this.beta)) {
/*     */             break;
/*     */           }
/*  79 */           this.model.mapLabelToState(autoSeq);
/*  80 */           if (!isCorrect(manualSeq, autoSeq)) {
/*  81 */             solutions[solutionNum] = autoSeq;
/*  82 */             solutionNum++;
/*     */           }
/*     */         }
/*     */ 
/*  86 */         if (solutionNum > 0) {
/*  87 */           int startPos = this.model.getMarkovOrder() - 1;
/*  88 */           while (startPos < manualSeq.length()) {
/*  89 */             int endPos = getSegmentEnd(manualSeq, startPos);
/*  90 */             boolean different = false;
/*  91 */             for (int s = 0; s < solutionNum; s++) {
/*  92 */               if ((autoStartPos[s] != startPos) || (getSegmentEnd(solutions[s], autoStartPos[s]) != endPos) || 
/*  93 */                 (manualSeq.getLabel(endPos) != solutions[s].getLabel(endPos))) {
/*  94 */                 different = true;
/*  95 */                 break;
/*     */               }
/*     */             }
/*  98 */             if (different) {
/*  99 */               numErrs++;
/* 100 */               updateWeights(manualSeq, startPos, endPos, 1.0D, this.lambda);
/* 101 */               for (int s = 0; s < solutionNum; s++)
/*     */               {
/* 103 */                 while (autoStartPos[s] <= endPos) {
/* 104 */                   int autoEndPos = getSegmentEnd(solutions[s], autoStartPos[s]);
/* 105 */                   updateWeights(solutions[s], autoStartPos[s], autoEndPos, -1.0D / solutionNum, this.lambda);
/* 106 */                   autoStartPos[s] = (autoEndPos + 1);
/*     */                 }
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 112 */             for (int s = 0; s < solutionNum; s++)
/*     */             {
/* 114 */               while (autoStartPos[s] <= endPos) {
/* 115 */                 int autoEndPos = getSegmentEnd(solutions[s], autoStartPos[s]);
/* 116 */                 autoStartPos[s] = (autoEndPos + 1);
/*     */               }
/*     */             }
/*     */ 
/* 120 */             startPos = endPos + 1;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 125 */         MathUtil.incArray(lambdaSum, this.lambda);
/* 126 */         trainingCount++;
/*     */       }
/*     */ 
/* 129 */       System.out.println("Iteration " + t + " numErrs " + numErrs);
/* 130 */       if (numErrs == 0)
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/* 135 */     MathUtil.multiArray(lambdaSum, 1.0D / trainingCount);
/* 136 */     MathUtil.copyArray(lambdaSum, this.lambda);
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean isCorrect(DataSequence manual, DataSequence auto)
/*     */   {
/* 142 */     for (int i = 0; i < manual.length(); i++) {
/* 143 */       if (manual.getLabel(i) != auto.getLabel(i))
/* 144 */         return false;
/*     */     }
/* 146 */     return true;
/*     */   }
/*     */ 
/*     */   protected void updateWeights(DataSequence dataSeq, int startPos, int endPos, double wt, double[] grad)
/*     */   {
/* 153 */     this.featureGenerator.startScanFeaturesAt(dataSeq, startPos, endPos);
/* 154 */     while (this.featureGenerator.hasNext()) {
/* 155 */       Feature feature = this.featureGenerator.next();
/* 156 */       int f = feature.getIndex();
/* 157 */       int yp = feature.getLabel();
/* 158 */       int yprev = feature.getPrevLabel();
/*     */ 
/* 160 */       if ((dataSeq.getLabel(endPos) == yp) && ((yprev < 0) || (yprev == dataSeq.getLabel(startPos - 1))))
/* 161 */         grad[f] += wt * feature.getValue();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected double getSequenceScore(DataSequence dataSeq, double[] grad)
/*     */   {
/* 172 */     int startPos = this.model.getMarkovOrder() - 1;
/* 173 */     double score = 0.0D;
/* 174 */     while (startPos < dataSeq.length()) {
/* 175 */       int endPos = getSegmentEnd(dataSeq, startPos);
/* 176 */       this.featureGenerator.startScanFeaturesAt(dataSeq, startPos, endPos);
/* 177 */       while (this.featureGenerator.hasNext()) {
/* 178 */         Feature feature = this.featureGenerator.next();
/* 179 */         int f = feature.getIndex();
/* 180 */         int yp = feature.getLabel();
/* 181 */         int yprev = feature.getPrevLabel();
/* 182 */         if ((dataSeq.getLabel(endPos) == yp) && ((yprev < 0) || (yprev == dataSeq.getLabel(startPos - 1)))) {
/* 183 */           score += grad[f] * feature.getValue();
/*     */         }
/*     */       }
/* 186 */       startPos = endPos + 1;
/*     */     }
/* 188 */     return score;
/*     */   }
/*     */ 
/*     */   protected Labeler getLabeler() {
/* 192 */     return new ViterbiBasicLabeler(this.model, this.featureGenerator);
/*     */   }
/*     */ 
/*     */   protected int getSegmentEnd(DataSequence dataSeq, int start) {
/* 196 */     return start;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.crf.CollinsBasicTrainer
 * JD-Core Version:    0.6.2
 */