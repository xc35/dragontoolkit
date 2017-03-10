/*     */ package edu.drexel.cis.dragon.ml.seqmodel.crf;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.DoubleDenseMatrix;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.feature.Feature;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.feature.FeatureGenerator;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.model.ModelGraph;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public abstract class AbstractCRF
/*     */ {
/*     */   protected EdgeGenerator edgeGen;
/*     */   protected FeatureGenerator featureGenerator;
/*     */   protected ModelGraph model;
/*     */   protected double[] lambda;
/*     */ 
/*     */   public AbstractCRF(ModelGraph model, FeatureGenerator featureGen)
/*     */   {
/*  26 */     this.model = model;
/*  27 */     this.edgeGen = new EdgeGenerator(model.getMarkovOrder(), model.getOriginalLabelNum());
/*  28 */     this.featureGenerator = featureGen;
/*     */   }
/*     */ 
/*     */   public FeatureGenerator getFeatureGenerator() {
/*  32 */     return this.featureGenerator;
/*     */   }
/*     */ 
/*     */   public ModelGraph getModelGraph() {
/*  36 */     return this.model;
/*     */   }
/*     */ 
/*     */   public double[] getModelParameter() {
/*  40 */     return this.lambda;
/*     */   }
/*     */ 
/*     */   public boolean saveModelParameter(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  47 */       PrintWriter out = new PrintWriter(new FileOutputStream(filename));
/*  48 */       out.println(this.lambda.length);
/*  49 */       for (int i = 0; i < this.lambda.length; i++)
/*  50 */         out.println(this.lambda[i]);
/*  51 */       out.close();
/*  52 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/*  55 */       e.printStackTrace();
/*  56 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean readModelParameter(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  66 */       BufferedReader in = new BufferedReader(new FileReader(filename));
/*  67 */       int featureNum = Integer.parseInt(in.readLine());
/*  68 */       this.lambda = new double[featureNum];
/*  69 */       int pos = 0;
/*     */       String line;
/*  70 */       while ((line = in.readLine()) != null)
/*     */       {
 
/*  71 */         this.lambda[(pos++)] = Double.parseDouble(line);
/*     */       }
/*  73 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/*  76 */       e.printStackTrace();
/*  77 */     }return false;
/*     */   }
/*     */ 
/*     */   protected void computeTransMatrix(double[] lambda, DataSequence data, int startPos, int endPos, DoubleDenseMatrix transMatrix, boolean takeExp)
/*     */   {
/*  82 */     this.featureGenerator.startScanFeaturesAt(data, startPos, endPos);
/*  83 */     computeTransMatrix(lambda, transMatrix, takeExp);
/*     */   }
/*     */ 
/*     */   protected void computeTransMatrix(double[] lambda, DoubleDenseMatrix transMatrix, boolean takeExp)
/*     */   {
/*  92 */     int stateNum = transMatrix.rows();
/*  93 */     double[] stateFeatureCost = new double[stateNum];
/*  94 */     transMatrix.assign(0.0D);
/*  95 */     while (this.featureGenerator.hasNext()) {
/*  96 */       Feature feature = this.featureGenerator.next();
/*  97 */       int label = feature.getLabel();
/*  98 */       int index = feature.getIndex();
/*     */ 
/* 100 */       if (feature.getPrevLabel() < 0)
/*     */       {
/* 102 */         stateFeatureCost[label] += lambda[index] * feature.getValue();
/*     */       }
/*     */       else {
/* 105 */         transMatrix.add(feature.getPrevLabel(), label, lambda[index] * feature.getValue());
/*     */       }
/*     */     }
/* 108 */     for (int i = 0; i < stateNum; i++) {
/* 109 */       for (int j = 0; j < stateNum; j++) {
/* 110 */         transMatrix.setDouble(j, i, transMatrix.getDouble(j, i) + stateFeatureCost[i]);
/*     */       }
/*     */     }
/* 113 */     if (takeExp)
/* 114 */       for (int i = 0; i < stateNum; i++)
/* 115 */         for (int j = 0; j < stateNum; j++)
/* 116 */           transMatrix.setDouble(i, j, Math.exp(transMatrix.getDouble(i, j)));
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.crf.AbstractCRF
 * JD-Core Version:    0.6.2
 */