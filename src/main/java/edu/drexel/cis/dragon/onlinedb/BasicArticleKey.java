/*    */ package edu.drexel.cis.dragon.onlinedb;
/*    */ 
/*    */ public class BasicArticleKey
/*    */   implements Comparable
/*    */ {
/*    */   private String key;
/*    */   private long offset;
/*    */   private int length;
/*    */   private int fileIndex;
/*    */ 
/*    */   public BasicArticleKey(String key)
/*    */   {
/* 18 */     this.key = key;
/* 19 */     this.offset = -1L;
/* 20 */     this.fileIndex = -1;
/* 21 */     this.length = 0;
/*    */   }
/*    */ 
/*    */   public String getKey() {
/* 25 */     return this.key;
/*    */   }
/*    */ 
/*    */   public long getOffset() {
/* 29 */     return this.offset;
/*    */   }
/*    */ 
/*    */   public int getLength() {
/* 33 */     return this.length;
/*    */   }
/*    */ 
/*    */   public void setLength(int length) {
/* 37 */     this.length = length;
/*    */   }
/*    */ 
/*    */   public void setOffset(long offset) {
/* 41 */     this.offset = offset;
/*    */   }
/*    */ 
/*    */   public int getFileIndex() {
/* 45 */     return this.fileIndex;
/*    */   }
/*    */ 
/*    */   public void setFileIndex(int index) {
/* 49 */     this.fileIndex = index;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj)
/*    */   {
/* 55 */     String objKey = ((BasicArticleKey)obj).getKey();
/* 56 */     return this.key.compareTo(objKey);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.BasicArticleKey
 * JD-Core Version:    0.6.2
 */