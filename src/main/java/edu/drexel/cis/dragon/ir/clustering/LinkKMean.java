/*     */ package edu.drexel.cis.dragon.ir.clustering;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.clustermodel.ClusterModel;
/*     */ import edu.drexel.cis.dragon.ir.clustering.clustermodel.MultinomialClusterModel;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleFlatDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class LinkKMean extends AbstractClustering
/*     */ {
/*     */   private Clustering initClustering;
/*     */   private SparseMatrix inLinks;
/*     */   private SparseMatrix outLinks;
/*     */   private ClusterModel distMetric;
/*     */   private int maxIteration;
/*     */   private boolean useWeight;
/*     */ 
/*     */   public LinkKMean(Clustering initClustering, SparseMatrix links)
/*     */   {
/*  32 */     this(initClustering, links, null);
/*     */   }
/*     */ 
/*     */   public LinkKMean(Clustering initClustering, SparseMatrix outLinks, SparseMatrix inLinks)
/*     */   {
/*  44 */     super(initClustering.getIndexReader());
/*  45 */     this.initClustering = initClustering;
/*  46 */     this.inLinks = inLinks;
/*  47 */     this.outLinks = outLinks;
/*  48 */     this.clusterNum = initClustering.getClusterNum();
/*     */ 
/*  50 */     this.distMetric = new MultinomialClusterModel(this.clusterNum, this.indexReader, 0.5D);
/*     */   }
/*     */ 
/*     */   public void setUseWeight(boolean useWeight) {
/*  54 */     this.useWeight = useWeight;
/*     */   }
/*     */ 
/*     */   public boolean getUseWeight() {
/*  58 */     return this.useWeight;
/*     */   }
/*     */ 
/*     */   protected boolean initialize(IRDoc[] arrDoc) {
/*  62 */     this.initClustering.setRandomSeed(this.randomSeed);
/*  63 */     this.initClustering.cluster(arrDoc);
/*  64 */     this.distMetric.setFeatureFilter(this.initClustering.getFeatureFilter());
/*  65 */     this.clusterSet = this.initClustering.getClusterSet();
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean cluster(IRDoc[] arrDoc)
/*     */   {
/*  78 */     DoubleVector probVector = new DoubleVector(this.clusterNum);
/*  79 */     int movingObj = arrDoc.length;
/*  80 */     int iteration = 0;
/*  81 */     int[] arrDocLabel = new int[this.indexReader.getCollection().getDocNum()];
/*  82 */     double[] outNeighbor = new double[this.clusterNum];
/*     */     double[] inNeighbor;
/*  83 */     if (this.inLinks != null)
/*  84 */       inNeighbor = new double[this.clusterNum];
/*     */     else {
/*  86 */       inNeighbor = (double[])null;
/*     */     }
/*  88 */     if (!initialize(arrDoc))
/*  89 */       return false;
/*     */     int i;
while(movingObj>0 && iteration<maxIteration){
/*  93 */       if (this.showProgress) {
/*  94 */         System.out.print(new Date().toString() + " " + iteration++);
/*  95 */         System.out.print(" ");
/*  96 */         System.out.println(movingObj);
/*     */       }
/*     */ 
/*  99 */       this.distMetric.setDocClusters(this.clusterSet);
/* 100 */       MathUtil.initArray(arrDocLabel, -1);
/* 101 */       for (i = 0; i < arrDoc.length; i++)
/* 102 */         arrDocLabel[arrDoc[i].getIndex()] = arrDoc[i].getCategory();
/* 103 */       DoubleDenseMatrix transMatrix = estimateClassTransferProb(arrDoc, arrDocLabel);
/*     */ 
/* 105 */       movingObj = 0;
/*     */ 
/* 108 */       MathUtil.initArray(outNeighbor, 0);
/* 109 */       int[] arrIndex = this.outLinks.getNonZeroColumnsInRow(arrDoc[i].getIndex());
/*     */       double[] arrScore;
/* 110 */       if (this.useWeight)
/* 111 */         arrScore = this.outLinks.getNonZeroDoubleScoresInRow(arrDoc[i].getIndex());
/*     */       else
/* 113 */         arrScore = (double[])null;
/* 114 */       if (arrIndex != null) {
/* 115 */         for (int j = 0; j < arrIndex.length; j++) {
/* 116 */           int label = arrDocLabel[arrIndex[j]];
/* 117 */           if (label >= 0) {
/* 118 */             if (!this.useWeight)
/* 119 */               outNeighbor[label] += 1.0D;
/*     */             else
/* 121 */               outNeighbor[label] += arrScore[j];
/*     */           }
/*     */         }
/*     */       }
/* 125 */       if (this.inLinks != null) {
/* 126 */         MathUtil.initArray(inNeighbor, 0.0D);
/* 127 */         arrIndex = this.inLinks.getNonZeroColumnsInRow(arrDoc[i].getIndex());
/* 128 */         if (this.useWeight)
/* 129 */           arrScore = this.inLinks.getNonZeroDoubleScoresInRow(arrDoc[i].getIndex());
/*     */         else
/* 131 */           arrScore = (double[])null;
/* 132 */         if (arrIndex != null) {
/* 133 */           for (int j = 0; j < arrIndex.length; j++) {
/* 134 */             int label = arrDocLabel[arrIndex[j]];
/* 135 */             if (label >= 0) {
/* 136 */               if (!this.useWeight)
/* 137 */                 inNeighbor[label] += 1.0D;
/*     */               else {
/* 139 */                 inNeighbor[label] += arrScore[j];
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 145 */       for (int j = 0; j < this.clusterNum; j++)
/* 146 */         probVector.set(j, getLogLikelihood(arrDoc[i], j, transMatrix, outNeighbor, inNeighbor));
/* 147 */       int curCluster = probVector.getDimWithMaxValue();
/* 148 */       int oldCluster = arrDoc[i].getCategory();
/* 149 */       if (curCluster != oldCluster)
/*     */       {
/* 151 */         this.clusterSet.removeDoc(oldCluster, arrDoc[i]);
/* 152 */         this.clusterSet.addDoc(curCluster, arrDoc[i]);
/* 153 */         movingObj++;
/*     */       }
/* 107 */       i++;
/*     */     }
/*     */ 
/* 157 */     return true;
/*     */   }
/*     */ 
/*     */   public int getMaxIteration() {
/* 161 */     return this.maxIteration;
/*     */   }
/*     */ 
/*     */   public void setMaxIteration(int iteration) {
/* 165 */     this.maxIteration = iteration;
/*     */   }
/*     */ 
/*     */   protected double getLogLikelihood(IRDoc doc, int clusterID, DoubleDenseMatrix transMatrix, double[] arrOutLinks, double[] arrInLinks)
/*     */   {
/* 172 */     double sum = -this.distMetric.getDistance(doc, clusterID);
/* 173 */     for (int i = 0; i < this.clusterNum; i++)
/* 174 */       sum += arrOutLinks[i] * transMatrix.getDouble(clusterID, i);
/* 175 */     if (arrInLinks != null) {
/* 176 */       for (int i = 0; i < this.clusterNum; i++)
/* 177 */         sum += arrInLinks[i] * transMatrix.getDouble(i, clusterID);
/*     */     }
/* 179 */     return sum;
/*     */   }
/*     */ 
/*     */   protected DoubleDenseMatrix estimateClassTransferProb(IRDoc[] arrDoc, int[] arrDocLabel)
/*     */   {
/* 188 */     DoubleFlatDenseMatrix matrix = new DoubleFlatDenseMatrix(this.clusterNum, this.clusterNum);
/* 189 */     matrix.assign(1.0D);
/* 190 */     for (int i = 0; i < arrDoc.length; i++) {
/* 191 */       int startLabel = arrDocLabel[arrDoc[i].getIndex()];
/* 192 */       int[] arrIndex = this.outLinks.getNonZeroColumnsInRow(arrDoc[i].getIndex());
/* 193 */       if (arrIndex != null)
/*     */       {
/*     */         double[] arrScore;
/* 194 */         if (this.useWeight)
/* 195 */           arrScore = this.outLinks.getNonZeroDoubleScoresInRow(arrDoc[i].getIndex());
/*     */         else
/* 197 */           arrScore = (double[])null;
/* 198 */         for (int j = 0; j < arrIndex.length; j++) {
/* 199 */           int endLabel = arrDocLabel[arrIndex[j]];
/* 200 */           if (endLabel >= 0) {
/* 201 */             if (!this.useWeight)
/* 202 */               matrix.add(startLabel, endLabel, 1.0D);
/*     */             else
/* 204 */               matrix.add(startLabel, endLabel, arrScore[j]);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 209 */     for (int i = 0; i < this.clusterNum; i++) {
/* 210 */       double sum = matrix.getRowSum(i);
/* 211 */       for (int j = 0; j < this.clusterNum; j++) {
/* 212 */         matrix.setDouble(i, j, Math.log(matrix.getDouble(i, j) / sum));
/*     */       }
/*     */     }
/* 215 */     return matrix;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.LinkKMean
 * JD-Core Version:    0.6.2
 */