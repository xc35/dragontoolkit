/*    */ package edu.drexel.cis.dragon.nlp;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexSortable;
/*    */ 
/*    */ public class SimpleElement
/*    */   implements IndexSortable, Comparable
/*    */ {
/*    */   private int index;
/*    */   private String key;
/*    */ 
/*    */   public SimpleElement(String key, int index)
/*    */   {
/* 18 */     this.key = key;
/* 19 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public String getKey() {
/* 23 */     return this.key;
/*    */   }
/*    */ 
/*    */   public void setKey(String key) {
/* 27 */     this.key = key;
/*    */   }
/*    */ 
/*    */   public int getIndex() {
/* 31 */     return this.index;
/*    */   }
/*    */ 
/*    */   public void setIndex(int index) {
/* 35 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj)
/*    */   {
/* 41 */     String objKey = ((SimpleElement)obj).getKey();
/* 42 */     return this.key.compareToIgnoreCase(objKey);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.SimpleElement
 * JD-Core Version:    0.6.2
 */