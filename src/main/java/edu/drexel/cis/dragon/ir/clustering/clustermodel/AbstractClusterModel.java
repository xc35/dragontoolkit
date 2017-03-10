/*    */ package edu.drexel.cis.dragon.ir.clustering.clustermodel;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.clustering.DocCluster;
/*    */ import edu.drexel.cis.dragon.ir.clustering.DocClusterSet;
/*    */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ 
/*    */ public abstract class AbstractClusterModel
/*    */   implements ClusterModel
/*    */ {
/*    */   protected FeatureFilter featureFilter;
/*    */   protected int clusterNum;
/*    */ 
/*    */   public AbstractClusterModel(int clusterNum)
/*    */   {
/* 21 */     this.clusterNum = clusterNum;
/*    */   }
/*    */ 
/*    */   public void setDocClusters(DocClusterSet clusterSet)
/*    */   {
/* 27 */     for (int i = 0; i < clusterSet.getClusterNum(); i++)
/* 28 */       setDocCluster(clusterSet.getDocCluster(i));
/*    */   }
/*    */ 
/*    */   public double getDistance(IRDoc doc, DocCluster cluster) {
/* 32 */     setDocCluster(cluster);
/* 33 */     return getDistance(doc, cluster.getClusterID());
/*    */   }
/*    */ 
/*    */   public int getClusterNum() {
/* 37 */     return this.clusterNum;
/*    */   }
/*    */ 
/*    */   public void setFeatureFilter(FeatureFilter selector) {
/* 41 */     this.featureFilter = selector;
/*    */   }
/*    */ 
/*    */   public FeatureFilter getFeatureFilter() {
/* 45 */     return this.featureFilter;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.clustermodel.AbstractClusterModel
 * JD-Core Version:    0.6.2
 */