/*     */ package edu.drexel.cis.dragon.ir.clustering;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.clustermodel.ClusterModel;
/*     */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Date;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class BasicKMean extends AbstractClustering
/*     */ {
/*     */   protected ClusterModel distMetric;
/*     */   protected boolean initialized;
/*     */   protected int maxIteration;
/*     */   private boolean initAllObjs;
/*     */ 
/*     */   public BasicKMean(ClusterModel distMetric, int clusterNum)
/*     */   {
/*  23 */     this(null, distMetric, clusterNum, false);
/*     */   }
/*     */ 
/*     */   public BasicKMean(IndexReader indexReader, ClusterModel distMetric, int clusterNum) {
/*  27 */     this(indexReader, distMetric, clusterNum, false);
/*     */   }
/*     */ 
/*     */   public BasicKMean(ClusterModel distMetric, int clusterNum, boolean initAllObjs) {
/*  31 */     this(null, distMetric, clusterNum, initAllObjs);
/*     */   }
/*     */ 
/*     */   public BasicKMean(IndexReader indexReader, ClusterModel distMetric, int clusterNum, boolean initAllObjs) {
/*  35 */     super(indexReader);
/*  36 */     this.clusterNum = clusterNum;
/*  37 */     this.distMetric = distMetric;
/*  38 */     this.initialized = false;
/*  39 */     this.maxIteration = 200;
/*  40 */     this.randomSeed = 0L;
/*  41 */     this.initAllObjs = initAllObjs;
/*     */   }
/*     */ 
/*     */   public BasicKMean(ClusterModel distMetric, DocClusterSet initClusterSet) {
/*  45 */     this(null, distMetric, initClusterSet);
/*     */   }
/*     */ 
/*     */   public BasicKMean(IndexReader indexReader, ClusterModel distMetric, DocClusterSet initClusterSet) {
/*  49 */     super(indexReader);
/*  50 */     this.clusterNum = initClusterSet.getClusterNum();
/*  51 */     this.distMetric = distMetric;
/*  52 */     this.clusterSet = initClusterSet;
/*  53 */     distMetric.setDocClusters(initClusterSet);
/*  54 */     this.initialized = true;
/*  55 */     this.maxIteration = 200;
/*     */   }
/*     */ 
/*     */   public void setUseAllObjectForInitialization(boolean option)
/*     */   {
/*  64 */     this.initAllObjs = option;
/*     */   }
/*     */ 
/*     */   public boolean getUseAllObjectForInitialization() {
/*  68 */     return this.initAllObjs;
/*     */   }
/*     */ 
/*     */   protected boolean initialize(IRDoc[] arrDoc)
/*     */   {
/*  75 */     Random random = new Random();
/*  76 */     if (this.randomSeed > 0L)
/*  77 */       random.setSeed(this.randomSeed);
/*  78 */     this.clusterSet = new DocClusterSet(this.clusterNum);
/*  79 */     for (int i = 0; i < arrDoc.length; i++) {
/*  80 */       arrDoc[i].setCategory(-1);
/*     */     }
/*  82 */     int i = 0;
/*     */ 
/*  84 */     while (i < this.clusterNum) {
/*  85 */       int curDocNo = (int)(random.nextDouble() * arrDoc.length);
/*  86 */       if (arrDoc[curDocNo].getCategory() == -1) {
/*  87 */         this.clusterSet.addDoc(i, arrDoc[curDocNo]);
/*  88 */         i++;
/*     */       }
/*     */     }
/*     */ 
/*  92 */     for (i = 0; (this.initAllObjs) && (i < arrDoc.length); i++)
/*  93 */       if (arrDoc[i].getCategory() == -1)
/*     */       {
/*  95 */         this.clusterSet.addDoc(random.nextInt(this.clusterNum), arrDoc[i]);
/*     */       }
/*  97 */     this.distMetric.setDocClusters(this.clusterSet);
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean cluster(IRDoc[] arrDoc)
/*     */   {
/* 109 */     if ((this.featureFilter != null) && (this.indexReader != null)) {
/* 110 */       this.featureFilter.initialize(this.indexReader, arrDoc);
/* 111 */       this.distMetric.setFeatureFilter(this.featureFilter);
/*     */     }
/* 113 */     Random random = new Random();
/* 114 */     if (this.randomSeed > 0L)
/* 115 */       random.setSeed(this.randomSeed);
/* 116 */     int[] arrCanCluster = new int[this.clusterNum];
/* 117 */     int docNum = arrDoc.length;
/* 118 */     int movingObj = docNum;
/* 119 */     int iteration = 0;
/* 120 */     if ((!this.initialized) && (!initialize(arrDoc))) {
/* 121 */       return false;
/*     */     }
/*     */ 
/* 124 */     while ((movingObj > 0) && (iteration < this.maxIteration)) {
/* 125 */       if (this.showProgress) {
/* 126 */         System.out.print(new Date().toString() + " " + iteration++);
/* 127 */         System.out.print(" ");
/* 128 */         System.out.println(movingObj);
/*     */       }
/*     */ 
/* 131 */       movingObj = 0;
/*     */ 
/* 133 */       for (int i = 0; i < docNum; i++) {
/* 134 */         double minDist = 1.7976931348623157E+308D;
/*     */ 
/* 137 */         int candidateNum = 0;
/* 138 */         for (int j = 0; j < this.clusterNum; j++) {
/* 139 */           double curDist = this.distMetric.getDistance(arrDoc[i], j);
/* 140 */           if (curDist <= minDist - 1.E-005D) {
/* 141 */             minDist = curDist;
/* 142 */             arrCanCluster[0] = j;
/* 143 */             candidateNum = 1;
/*     */           }
/* 145 */           else if (Math.abs(curDist - minDist) < 1.E-005D) {
/* 146 */             if (curDist < minDist)
/* 147 */               minDist = curDist;
/* 148 */             arrCanCluster[candidateNum] = j;
/* 149 */             candidateNum++;
/*     */           }
/*     */         }
/*     */         int curCluster;
/* 152 */         if (candidateNum == 1) {
/* 153 */           curCluster = arrCanCluster[0];
/*     */         } else {
/* 155 */           curCluster = (int)(random.nextDouble() * candidateNum);
/* 156 */           if (curCluster == candidateNum)
/* 157 */             curCluster = arrCanCluster[(curCluster - 1)];
/*     */           else {
/* 159 */             curCluster = arrCanCluster[curCluster];
/*     */           }
/*     */         }
/*     */ 
/* 163 */         int oldCluster = arrDoc[i].getCategory();
/* 164 */         if (curCluster != oldCluster)
/*     */         {
/* 168 */           this.clusterSet.removeDoc(arrDoc[i].getCategory(), arrDoc[i]);
/* 169 */           this.clusterSet.addDoc(curCluster, arrDoc[i]);
/* 170 */           movingObj++;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 175 */       if (movingObj == 0) break;
/* 176 */       this.distMetric.setDocClusters(this.clusterSet);
/*     */     }
/* 178 */     return true;
/*     */   }
/*     */ 
/*     */   public int getMaxIteration() {
/* 182 */     return this.maxIteration;
/*     */   }
/*     */ 
/*     */   public void setMaxIteration(int iteration) {
/* 186 */     this.maxIteration = iteration;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.BasicKMean
 * JD-Core Version:    0.6.2
 */