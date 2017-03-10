/*    */ package edu.drexel.cis.dragon.ir.clustering;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ 
/*    */ public abstract class AbstractClustering
/*    */   implements Clustering
/*    */ {
/*    */   protected IndexReader indexReader;
/*    */   protected FeatureFilter featureFilter;
/*    */   protected int clusterNum;
/*    */   protected DocClusterSet clusterSet;
/*    */   protected boolean showProgress;
/*    */   protected long randomSeed;
/*    */ 
/*    */   public AbstractClustering(IndexReader indexReader)
/*    */   {
/* 24 */     this.indexReader = indexReader;
/* 25 */     this.showProgress = true;
/*    */   }
/*    */ 
/*    */   public int getClusterNum() {
/* 29 */     return this.clusterNum;
/*    */   }
/*    */ 
/*    */   public long getRandomSeed() {
/* 33 */     return this.randomSeed;
/*    */   }
/*    */ 
/*    */   public void setRandomSeed(long seed) {
/* 37 */     this.randomSeed = seed;
/*    */   }
/*    */ 
/*    */   public DocClusterSet getClusterSet() {
/* 41 */     return this.clusterSet;
/*    */   }
/*    */ 
/*    */   public DocCluster getCluster(int index) {
/* 45 */     return this.clusterSet.getDocCluster(index);
/*    */   }
/*    */ 
/*    */   public IndexReader getIndexReader() {
/* 49 */     return this.indexReader;
/*    */   }
/*    */ 
/*    */   public boolean cluster()
/*    */   {
/* 56 */     IRDoc[] arrDoc = new IRDoc[this.indexReader.getCollection().getDocNum()];
/* 57 */     for (int i = 0; i < arrDoc.length; i++)
/* 58 */       arrDoc[i] = this.indexReader.getDoc(i);
/* 59 */     return cluster(arrDoc);
/*    */   }
/*    */ 
/*    */   public FeatureFilter getFeatureFilter() {
/* 63 */     return this.featureFilter;
/*    */   }
/*    */ 
/*    */   public void setFeatureFilter(FeatureFilter selector)
/*    */   {
/* 68 */     this.featureFilter = selector;
/*    */   }
/*    */ 
/*    */   public void setShowProgress(boolean option) {
/* 72 */     this.showProgress = option;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.AbstractClustering
 * JD-Core Version:    0.6.2
 */