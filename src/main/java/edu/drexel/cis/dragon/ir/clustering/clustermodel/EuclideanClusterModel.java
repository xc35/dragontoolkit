/*     */ package edu.drexel.cis.dragon.ir.clustering.clustermodel;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.DocCluster;
/*     */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.matrix.DenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ 
/*     */ public class EuclideanClusterModel extends AbstractClusterModel
/*     */ {
/*     */   private double[][] arrClusterVector;
/*     */   private double[] arrClusterVectorLen;
/*     */   private SparseMatrix sparseMatrix;
/*     */   private DenseMatrix denseMatrix;
/*     */ 
/*     */   public EuclideanClusterModel(int clusterNum, SparseMatrix docTerm)
/*     */   {
/*  22 */     super(clusterNum);
/*  23 */     this.arrClusterVectorLen = new double[clusterNum];
/*  24 */     this.sparseMatrix = docTerm;
/*     */   }
/*     */ 
/*     */   public EuclideanClusterModel(int clusterNum, DenseMatrix docTerm) {
/*  28 */     super(clusterNum);
/*  29 */     this.arrClusterVectorLen = new double[clusterNum];
/*  30 */     this.denseMatrix = docTerm;
/*     */   }
/*     */ 
/*     */   public void setClusterNum(int clusterNum) {
/*  34 */     this.clusterNum = clusterNum;
/*  35 */     this.arrClusterVectorLen = new double[clusterNum];
/*     */   }
/*     */ 
/*     */   public void setDocCluster(DocCluster cluster)
/*     */   {
/*     */     int featureNum;
/*  42 */     if (this.featureFilter == null)
/*  43 */       featureNum = this.sparseMatrix != null ? this.sparseMatrix.columns() : this.denseMatrix.columns();
/*     */     else
/*  45 */       featureNum = this.featureFilter.getSelectedFeatureNum();
/*     */     int[] indexList;
/*  46 */     if (this.sparseMatrix == null) {
/*  47 */      indexList = new int[featureNum];
/*  48 */       for (int i = 0; i < featureNum; i++)
/*  49 */         indexList[i] = i;
/*     */     }
/*     */     else {
/*  52 */       indexList = (int[])null;
/*     */     }
/*  54 */     if ((this.arrClusterVector == null) || (this.arrClusterVector.length != this.clusterNum) || (this.arrClusterVector[0].length != featureNum))
/*  55 */       this.arrClusterVector = new double[this.clusterNum][featureNum];
/*  56 */     int clusterID = cluster.getClusterID();
/*     */ 
/*  58 */     for (int i = 0; i < cluster.getDocNum(); i++)
/*     */     {
/*     */       double[] freqList;
/*  59 */       if (this.sparseMatrix != null) {
/*  60 */         indexList = this.sparseMatrix.getNonZeroColumnsInRow(cluster.getDoc(i).getIndex());
/*  61 */         freqList = this.sparseMatrix.getNonZeroDoubleScoresInRow(cluster.getDoc(i).getIndex());
/*     */       }
/*     */       else {
/*  64 */         freqList = this.denseMatrix.getDouble(cluster.getDoc(i).getIndex());
/*     */       }
/*     */ 
/*  67 */       for (int j = 0; j < indexList.length; j++) {
/*  68 */         int newIndex = indexList[j];
/*  69 */         if (this.featureFilter != null)
/*  70 */           newIndex = this.featureFilter.map(newIndex);
/*  71 */         if (newIndex >= 0) {
/*  72 */           this.arrClusterVector[clusterID][newIndex] += freqList[j];
/*     */         }
/*     */       }
/*     */     }
/*  76 */     this.arrClusterVectorLen[clusterID] = 0.0D;
/*  77 */     for (int i = 0; i < this.arrClusterVector[clusterID].length; i++)
/*  78 */       if (this.arrClusterVector[clusterID][i] != 0.0D)
/*     */       {
/*  80 */         this.arrClusterVector[clusterID][i] /= cluster.getDocNum();
/*  81 */         this.arrClusterVectorLen[clusterID] += this.arrClusterVector[clusterID][i] * this.arrClusterVector[clusterID][i];
/*     */       }
/*     */   }
/*     */ 
/*     */   public double getDistance(IRDoc doc, int clusterID)
/*     */   {
/*  89 */     double product = this.arrClusterVectorLen[clusterID];
/*     */     double[] freqList;
/*     */     int[] indexList;
/*  90 */     if (this.sparseMatrix != null) {
/*  91 */       indexList = this.sparseMatrix.getNonZeroColumnsInRow(doc.getIndex());
/*  92 */       freqList = this.sparseMatrix.getNonZeroDoubleScoresInRow(doc.getIndex());
/*     */     }
/*     */     else {
/*  95 */       freqList = this.denseMatrix.getDouble(doc.getIndex());
/*  96 */       indexList = (int[])null;
/*     */     }
/*     */ 
/*  99 */     for (int i = 0; i < freqList.length; i++) {
/* 100 */       int newIndex = this.sparseMatrix != null ? indexList[i] : i;
/* 101 */       if (this.featureFilter != null)
/* 102 */         newIndex = this.featureFilter.map(newIndex);
/* 103 */       if (newIndex >= 0) {
/* 104 */         product += freqList[i] * (freqList[i] - 2.0D * this.arrClusterVector[clusterID][newIndex]);
/*     */       }
/*     */     }
/* 107 */     return Math.sqrt(product);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.clustermodel.EuclideanClusterModel
 * JD-Core Version:    0.6.2
 */