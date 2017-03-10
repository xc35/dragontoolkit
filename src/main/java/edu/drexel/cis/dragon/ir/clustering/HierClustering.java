/*     */ package edu.drexel.cis.dragon.ir.clustering;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.docdistance.DocDistance;
/*     */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class HierClustering extends AbstractClustering
/*     */ {
/*     */   public static final int SINGLE_LINKAGE = -1;
/*     */   public static final int AVERAGE_LINKAGE = 0;
/*     */   public static final int COMPLETE_LINKAGE = 1;
/*     */   private DocDistance distMetric;
/*     */   private double[][] ccDist;
/*     */   private int linkage;
/*     */ 
/*     */   public HierClustering(DocDistance distMetric, int clusterNum, int linkage)
/*     */   {
/*  25 */     this(null, distMetric, clusterNum, linkage);
/*     */   }
/*     */ 
/*     */   public HierClustering(IndexReader indexReader, DocDistance distMetric, int clusterNum, int linkage) {
/*  29 */     super(indexReader);
/*  30 */     this.distMetric = distMetric;
/*  31 */     this.clusterNum = clusterNum;
/*  32 */     this.linkage = linkage;
/*     */   }
/*     */ 
/*     */   public boolean cluster(IRDoc[] arrDoc)
/*     */   {
/*  41 */     if ((this.featureFilter != null) && (this.indexReader != null)) {
/*  42 */       this.featureFilter.initialize(this.indexReader, arrDoc);
/*  43 */       this.distMetric.setFeatureFilter(this.featureFilter);
/*     */     }
/*     */ 
/*  46 */     if (this.showProgress)
/*  47 */       System.out.println(new Date() + " Computing the pairwise document similarity...");
/*  48 */     this.clusterSet = new DocClusterSet(arrDoc.length);
/*  49 */     this.ccDist = new double[arrDoc.length][arrDoc.length];
/*  50 */     for (int i = 0; i < arrDoc.length; i++) {
/*  51 */       this.clusterSet.addDoc(i, arrDoc[i]);
/*  52 */       this.ccDist[i][i] = 0.0D;
/*  53 */       for (int j = i + 1; j < arrDoc.length; j++) {
/*  54 */         this.ccDist[i][j] = Math.min(this.distMetric.getDistance(arrDoc[i], arrDoc[j]), this.distMetric.getDistance(arrDoc[j], arrDoc[i]));
/*  55 */         this.ccDist[j][i] = this.ccDist[i][j];
/*     */       }
/*     */     }
/*     */ 
/*  59 */     int k = 0;
/*  60 */     while (k + this.clusterNum < arrDoc.length) {
/*  61 */       if ((this.showProgress) && (k++ % 100 == 0)) {
/*  62 */         System.out.println(new Date() + " " + (arrDoc.length - k));
/*     */       }
/*     */ 
/*  65 */       double min = 1.7976931348623157E+308D;
/*  66 */       int indexI = -1;
/*  67 */       int indexJ = -1;
/*  68 */       for (int i = 0; i < arrDoc.length; i++) {
/*  69 */         if (this.ccDist[i][0] != -1.0D)
/*  70 */           for (int j = 0; j < arrDoc.length; j++)
/*  71 */             if ((i != j) && (this.ccDist[i][j] != -1.0D))
/*     */             {
/*  73 */               if (min > this.ccDist[i][j]) {
/*  74 */                 min = this.ccDist[i][j];
/*  75 */                 indexI = i;
/*  76 */                 indexJ = j;
/*     */               }
/*     */             }
/*     */       }
/*  80 */       mergeCluster(this.clusterSet.getDocCluster(indexI), this.clusterSet.getDocCluster(indexJ));
/*     */     }
/*     */ 
/*  84 */     DocClusterSet newClusterSet = new DocClusterSet(this.clusterNum);
/*  85 */     int j = 0;
/*  86 */     for (int i = 0; i < this.clusterSet.getClusterNum(); i++) {
/*  87 */       if (this.clusterSet.getDocCluster(i).getDocNum() != 0)
/*  88 */         newClusterSet.setDocCluster(this.clusterSet.getDocCluster(i), j++);
/*     */     }
/*  90 */     this.clusterSet = newClusterSet;
/*     */ 
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   private void mergeCluster(DocCluster mergingCluster, DocCluster deletingCluster)
/*     */   {
/*  99 */     int curMerging = mergingCluster.getClusterID();
/* 100 */     int curDeleting = deletingCluster.getClusterID();
/*     */ 
/* 103 */     for (int i = 0; i < this.clusterSet.getClusterNum(); i++) {
/* 104 */       if ((i != curMerging) && (i != curDeleting) && (this.clusterSet.getDocCluster(i).getDocNum() != 0))
/*     */       {
/* 106 */         this.ccDist[curMerging][i] = getDistance(mergingCluster, deletingCluster, i, this.linkage);
/* 107 */         this.ccDist[i][curMerging] = this.ccDist[curMerging][i];
/*     */       }
/*     */     }
/*     */ 
/* 111 */     for (int i = 0; i < this.clusterSet.getClusterNum(); i++) {
/* 112 */       this.ccDist[curDeleting][i] = -1.0D;
/* 113 */       this.ccDist[i][curDeleting] = -1.0D;
/*     */     }
/*     */ 
/* 117 */     for (int i = 0; i < deletingCluster.getDocNum(); i++)
/* 118 */       mergingCluster.addDoc(deletingCluster.getDoc(i));
/* 119 */     deletingCluster.removeAll();
/*     */   }
/*     */ 
/*     */   private double getDistance(DocCluster mergingCluster, DocCluster deletingCluster, int clusterID, int linkage)
/*     */   {
/* 125 */     int curMerging = mergingCluster.getClusterID();
/* 126 */     int curDeleting = deletingCluster.getClusterID();
/* 127 */     if (linkage == -1)
/* 128 */       return Math.min(this.ccDist[curMerging][clusterID], this.ccDist[curDeleting][clusterID]);
/* 129 */     if (linkage == 1) {
/* 130 */       return Math.max(this.ccDist[curMerging][clusterID], this.ccDist[curDeleting][clusterID]);
/*     */     }
/* 132 */     return (this.ccDist[curMerging][clusterID] * mergingCluster.getDocNum() + 
/* 133 */       this.ccDist[curDeleting][clusterID] * deletingCluster.getDocNum()) / (mergingCluster.getDocNum() + deletingCluster.getDocNum());
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.HierClustering
 * JD-Core Version:    0.6.2
 */