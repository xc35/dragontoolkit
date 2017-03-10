/*     */ package edu.drexel.cis.dragon.ir.clustering;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.clustermodel.ClusterModel;
/*     */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ 
/*     */ public class BisectKMean extends AbstractClustering
/*     */ {
/*     */   protected ClusterModel distMetric;
/*     */   protected boolean refine;
/*     */   protected int maxIteration;
/*     */   protected DocClusterSet clusterSet;
/*     */   private boolean initAllObjs;
/*     */ 
/*     */   public BisectKMean(ClusterModel distMetric, int clusterNum)
/*     */   {
/*  23 */     this(null, distMetric, clusterNum, false);
/*     */   }
/*     */ 
/*     */   public BisectKMean(IndexReader indexReader, ClusterModel distMetric, int clusterNum) {
/*  27 */     this(indexReader, distMetric, clusterNum, false);
/*     */   }
/*     */ 
/*     */   public BisectKMean(ClusterModel distMetric, int clusterNum, boolean initAllObjs) {
/*  31 */     this(null, distMetric, clusterNum, initAllObjs);
/*     */   }
/*     */ 
/*     */   public BisectKMean(IndexReader indexReader, ClusterModel distMetric, int clusterNum, boolean initAllObjs) {
/*  35 */     super(indexReader);
/*  36 */     this.clusterNum = clusterNum;
/*  37 */     this.distMetric = distMetric;
/*  38 */     this.refine = false;
/*  39 */     this.maxIteration = 200;
/*  40 */     this.randomSeed = 0L;
/*  41 */     this.initAllObjs = initAllObjs;
/*     */   }
/*     */ 
/*     */   public void setUseAllObjectForInitialization(boolean option)
/*     */   {
/*  50 */     this.initAllObjs = option;
/*     */   }
/*     */ 
/*     */   public boolean getUseAllObjectForInitialization() {
/*  54 */     return this.initAllObjs;
/*     */   }
/*     */ 
/*     */   public boolean cluster(IRDoc[] arrDoc) {
/*  58 */     if ((this.featureFilter != null) && (this.indexReader != null)) {
/*  59 */       this.featureFilter.initialize(this.indexReader, arrDoc);
/*  60 */       this.distMetric.setFeatureFilter(this.featureFilter);
/*     */     }
/*  62 */     return cluster(arrDoc, 2);
/*     */   }
/*     */ 
/*     */   public void setRefine(boolean refine) {
/*  66 */     this.refine = refine;
/*     */   }
/*     */ 
/*     */   public int getMaxIteration() {
/*  70 */     return this.maxIteration;
/*     */   }
/*     */ 
/*     */   public void setMaxIteration(int iteration) {
/*  74 */     this.maxIteration = iteration;
/*     */   }
/*     */ 
/*     */   private boolean cluster(IRDoc[] arrDoc, int secNum)
/*     */   {
/*  82 */     if (this.clusterNum < secNum) return false;
/*     */ 
/*  84 */     this.distMetric.setClusterNum(secNum);
/*  85 */     BasicKMean bkm = new BasicKMean(this.indexReader, this.distMetric, secNum, this.initAllObjs);
/*  86 */     bkm.setFeatureFilter(this.featureFilter);
/*  87 */     bkm.setMaxIteration(this.maxIteration);
/*  88 */     bkm.setRandomSeed(this.randomSeed);
/*  89 */     bkm.setShowProgress(this.showProgress);
/*  90 */     int id = 0;
/*  91 */     DocClusterSet mainDcs = new DocClusterSet(this.clusterNum);
/*     */ 
/*  93 */     bkm.cluster(arrDoc);
/*  94 */     DocClusterSet dcs = bkm.getClusterSet();
/*  95 */     for (int i = 0; i < secNum; i++) {
/*  96 */       mainDcs.setDocCluster(dcs.getDocCluster(i), id++);
/*     */     }
/*     */ 	  int i;
/*  99 */     while ( id < this.clusterNum)
/*     */     {
/* 100 */       int max = -2147483648;
/* 101 */       int index = -1;
/* 102 */       for (i = 0; i < id; i++) {
/* 103 */         DocCluster dc = mainDcs.getDocCluster(i);
/* 104 */         if (max < dc.getDocNum()) {
/* 105 */           max = dc.getDocNum();
/* 106 */           index = i;
/*     */         }
/*     */       }
/* 109 */       DocCluster dc = mainDcs.getDocCluster(index);
/*     */ 
/* 111 */       arrDoc = new IRDoc[dc.getDocNum()];
/* 112 */       for (i = 0; i < dc.getDocNum(); i++) {
/* 113 */         arrDoc[i] = dc.getDoc(i);
/* 114 */         arrDoc[i].setCategory(-1);
/*     */       }
/* 116 */       this.distMetric.setClusterNum(secNum);
/* 117 */       bkm = new BasicKMean(this.indexReader, this.distMetric, secNum, this.initAllObjs);
/* 118 */       bkm.setMaxIteration(this.maxIteration);
/* 119 */       bkm.setRandomSeed(this.randomSeed);
/* 120 */       bkm.setShowProgress(this.showProgress);
/* 121 */       bkm.cluster(arrDoc);
/*       */ 
/* 123 */       mainDcs.setDocCluster(bkm.getClusterSet().getDocCluster(0), index);
for(i=1;i<bkm.getClusterNum();i++){
    mainDcs.setDocCluster(bkm.getClusterSet().getDocCluster(i),id++);
}
/*     */     }
/*     */ 
/* 129 */     if (this.refine) {
/* 130 */       this.distMetric.setClusterNum(this.clusterNum);
/* 131 */       bkm = new BasicKMean(this.indexReader, this.distMetric, mainDcs);
/* 132 */       bkm.setMaxIteration(this.maxIteration);
/* 133 */       bkm.setRandomSeed(this.randomSeed);
/* 134 */       bkm.setShowProgress(this.showProgress);
/* 135 */       arrDoc = new IRDoc[this.indexReader.getCollection().getDocNum()];
/* 136 */       bkm.cluster(arrDoc);
/*     */     }
/* 138 */     this.clusterSet = mainDcs;
/*     */ 
/* 140 */     return true;
/*     */   }
/*     */ 
/*     */   public DocClusterSet getClusterSet() {
/* 144 */     return this.clusterSet;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.BisectKMean
 * JD-Core Version:    0.6.2
 */