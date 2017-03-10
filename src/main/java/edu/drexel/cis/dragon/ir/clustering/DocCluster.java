/*    */ package edu.drexel.cis.dragon.ir.clustering;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class DocCluster
/*    */ {
/*    */   private int clusterID;
/*    */   private String clusterName;
/*    */   private SortedArray list;
/*    */ 
/*    */   public DocCluster(int clusterID)
/*    */   {
/* 23 */     this.clusterID = clusterID;
/* 24 */     this.clusterName = String.valueOf(clusterID);
/* 25 */     this.list = new SortedArray(new IndexComparator());
/*    */   }
/*    */ 
/*    */   public boolean addDoc(IRDoc doc) {
/* 29 */     doc.setCategory(this.clusterID);
/* 30 */     return this.list.add(doc);
/*    */   }
/*    */ 
/*    */   public boolean removeDoc(IRDoc doc)
/*    */   {
/* 36 */     int pos = this.list.binarySearch(doc);
/* 37 */     if (pos < 0) {
/* 38 */       return false;
/*    */     }
/* 40 */     this.list.remove(pos);
/* 41 */     doc.setCategory(-1);
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */   public void removeAll() {
/* 46 */     this.list.clear();
/*    */   }
/*    */ 
/*    */   public int getDocNum() {
/* 50 */     return this.list.size();
/*    */   }
/*    */ 
/*    */   public IRDoc getDoc(int index) {
/* 54 */     return (IRDoc)this.list.get(index);
/*    */   }
/*    */ 
/*    */   public boolean containDoc(IRDoc doc) {
/* 58 */     return this.list.contains(doc);
/*    */   }
/*    */ 
/*    */   public ArrayList getDocSet() {
/* 62 */     return this.list;
/*    */   }
/*    */ 
/*    */   public int getClusterID() {
/* 66 */     return this.clusterID;
/*    */   }
/*    */ 
/*    */   public void setClusterID(int clusterID) {
/* 70 */     this.clusterID = clusterID;
/*    */   }
/*    */ 
/*    */   public String getClusterName() {
/* 74 */     return this.clusterName;
/*    */   }
/*    */ 
/*    */   public void setClusterName(String clusterName) {
/* 78 */     this.clusterName = clusterName;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.DocCluster
 * JD-Core Version:    0.6.2
 */