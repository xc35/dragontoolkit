/*     */ package edu.drexel.cis.dragon.ir.clustering.clustermodel;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.DocCluster;
/*     */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.matrix.DenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ 
/*     */ public class CosineClusterModel extends AbstractClusterModel
/*     */ {
/*     */   private double[][] arrClusterVector;
/*     */   private double[] arrClusterVectorLen;
/*     */   private SparseMatrix sparseMatrix;
/*     */   private DenseMatrix denseMatrix;
/*     */ 
/*     */   public CosineClusterModel(int clusterNum, SparseMatrix doctermMatrix)
/*     */   {
/*  23 */     super(clusterNum);
/*  24 */     this.sparseMatrix = doctermMatrix;
/*  25 */     this.arrClusterVectorLen = new double[clusterNum];
/*     */   }
/*     */ 
/*     */   public CosineClusterModel(int clusterNum, DenseMatrix doctermMatrix) {
/*  29 */     super(clusterNum);
/*  30 */     this.denseMatrix = doctermMatrix;
/*  31 */     this.arrClusterVectorLen = new double[clusterNum];
/*     */   }
/*     */ 
/*     */   public void setClusterNum(int clusterNum) {
/*  35 */     this.clusterNum = clusterNum;
/*  36 */     this.arrClusterVectorLen = new double[clusterNum];
/*     */   }
/*     */ 
/*     */   public void setDocCluster(DocCluster cluster)
/*     */   {
/*     */     int featureNum;
/*  43 */     if (this.featureFilter == null)
/*  44 */       featureNum = this.sparseMatrix != null ? this.sparseMatrix.columns() : this.denseMatrix.columns();
/*     */     else
/*  46 */       featureNum = this.featureFilter.getSelectedFeatureNum();
/*     */     int[] indexList;
/*  47 */     if (this.sparseMatrix == null) {
/*  48 */      indexList = new int[featureNum];
/*  49 */       for (int i = 0; i < featureNum; i++)
/*  50 */         indexList[i] = i;
/*     */     }
/*     */     else {
/*  53 */       indexList = (int[])null;
/*     */     }
/*  55 */     if ((this.arrClusterVector == null) || (this.arrClusterVector.length != this.clusterNum) || (this.arrClusterVector[0].length != featureNum))
/*  56 */       this.arrClusterVector = new double[this.clusterNum][featureNum];
/*  57 */     int clusterID = cluster.getClusterID();
/*  58 */     double vectorLength = 0.0D;
/*     */ 
/*  60 */     for (int i = 0; i < cluster.getDocNum(); i++)
/*     */     {
/*     */       double[] freqList;
/*  61 */       if (this.sparseMatrix != null) {
/*  62 */         indexList = this.sparseMatrix.getNonZeroColumnsInRow(cluster.getDoc(i).getIndex());
/*  63 */         freqList = this.sparseMatrix.getNonZeroDoubleScoresInRow(cluster.getDoc(i).getIndex());
/*     */       }
/*     */       else {
/*  66 */         freqList = this.denseMatrix.getDouble(cluster.getDoc(i).getIndex());
/*     */       }
/*  68 */       for (int j = 0; j < indexList.length; j++) {
/*  69 */         int newIndex = indexList[j];
/*  70 */         if (this.featureFilter != null)
/*  71 */           newIndex = this.featureFilter.map(newIndex);
/*  72 */         if (newIndex >= 0) {
/*  73 */           this.arrClusterVector[clusterID][newIndex] += freqList[j];
/*     */         }
/*     */       }
/*     */     }
/*  77 */     for (int i = 0; i < this.arrClusterVector[clusterID].length; i++)
/*  78 */       if (this.arrClusterVector[clusterID][i] != 0.0D)
/*     */       {
/*  80 */         this.arrClusterVector[clusterID][i] /= cluster.getDocNum();
/*  81 */         vectorLength += this.arrClusterVector[clusterID][i] * this.arrClusterVector[clusterID][i];
/*     */       }
/*  83 */     this.arrClusterVectorLen[clusterID] = Math.sqrt(vectorLength);
/*     */   }
/*     */ 
/*     */   public double getDistance(IRDoc doc, int clusterID)
/*     */   {
/*  90 */     double product = 0.0D;
/*  91 */     double docLength = 0.0D;
/*     */     double[] freqList;
/*     */     int[] indexList;
/*  92 */     if (this.sparseMatrix != null) {
/*  93 */      indexList = this.sparseMatrix.getNonZeroColumnsInRow(doc.getIndex());
/*  94 */       freqList = this.sparseMatrix.getNonZeroDoubleScoresInRow(doc.getIndex());
/*     */     }
/*     */     else {
/*  97 */       freqList = this.denseMatrix.getDouble(doc.getIndex());
/*  98 */       indexList = (int[])null;
/*     */     }
/*     */ 
/* 101 */     for (int i = 0; i < freqList.length; i++) {
/* 102 */       int newIndex = this.sparseMatrix != null ? indexList[i] : i;
/* 103 */       if (this.featureFilter != null)
/* 104 */         newIndex = this.featureFilter.map(newIndex);
/* 105 */       if (newIndex >= 0) {
/* 106 */         product += this.arrClusterVector[clusterID][newIndex] * freqList[i];
/* 107 */         docLength += freqList[i] * freqList[i];
/*     */       }
/*     */     }
/* 110 */     docLength = Math.sqrt(docLength);
/* 111 */     if ((docLength == 0.0D) || (this.arrClusterVectorLen[clusterID] == 0.0D))
/* 112 */       return 1.0D;
/* 113 */     return 1.0D - product / this.arrClusterVectorLen[clusterID] / docLength;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.clustermodel.CosineClusterModel
 * JD-Core Version:    0.6.2
 */