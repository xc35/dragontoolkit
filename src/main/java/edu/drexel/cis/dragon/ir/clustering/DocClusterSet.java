/*    */ package edu.drexel.cis.dragon.ir.clustering;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ 
/*    */ public class DocClusterSet
/*    */ {
/*    */   private int clusterNum;
/*    */   private DocCluster[] arrCluster;
/*    */ 
/*    */   public DocClusterSet(int clusterNum)
/*    */   {
/* 20 */     this.clusterNum = clusterNum;
/* 21 */     this.arrCluster = new DocCluster[clusterNum];
/* 22 */     for (int i = 0; i < clusterNum; i++)
/* 23 */       this.arrCluster[i] = new DocCluster(i);
/*    */   }
/*    */ 
/*    */   public DocCluster getDocCluster(int clusterID) {
/* 27 */     return this.arrCluster[clusterID];
/*    */   }
/*    */ 
/*    */   public void setDocCluster(DocCluster docCluster, int clusterID) {
/* 31 */     docCluster.setClusterID(clusterID);
/* 32 */     this.arrCluster[clusterID] = docCluster;
/*    */   }
/*    */ 
/*    */   public int getClusterNum() {
/* 36 */     return this.clusterNum;
/*    */   }
/*    */ 
/*    */   public boolean addDoc(int clusterID, IRDoc curDoc) {
/* 40 */     if ((clusterID >= this.clusterNum) || (clusterID < 0)) {
/* 41 */       return false;
/*    */     }
/* 43 */     return this.arrCluster[clusterID].addDoc(curDoc);
/*    */   }
/*    */ 
/*    */   public boolean removeDoc(int clusterID, IRDoc curDoc) {
/* 47 */     if ((clusterID >= this.clusterNum) || (clusterID < 0)) {
/* 48 */       return false;
/*    */     }
/* 50 */     return this.arrCluster[clusterID].removeDoc(curDoc);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.DocClusterSet
 * JD-Core Version:    0.6.2
 */