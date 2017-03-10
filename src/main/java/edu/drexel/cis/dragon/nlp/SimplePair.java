/*    */ package edu.drexel.cis.dragon.nlp;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexSortable;
/*    */ 
/*    */ public class SimplePair
/*    */   implements IndexSortable, Comparable
/*    */ {
/* 14 */   private static long hashCapacity = 16385L;
/*    */   private int index;
/*    */   private int first;
/*    */   private int second;
/*    */ 
/*    */   public SimplePair(int index, int firstElement, int secondElement)
/*    */   {
/* 20 */     this.index = index;
/* 21 */     this.first = firstElement;
/* 22 */     this.second = secondElement;
/*    */   }
/*    */ 
/*    */   public int getIndex() {
/* 26 */     return this.index;
/*    */   }
/*    */ 
/*    */   public void setIndex(int index) {
/* 30 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public int getFirstElement() {
/* 34 */     return this.first;
/*    */   }
/*    */ 
/*    */   public int getSecondElement() {
/* 38 */     return this.second;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj)
/*    */   {
/* 44 */     int indexObj = ((SimplePair)obj).getFirstElement();
/* 45 */     if (this.first == indexObj) {
/* 46 */       indexObj = ((SimplePair)obj).getSecondElement();
/* 47 */       if (this.second == indexObj)
/* 48 */         return 0;
/* 49 */       if (this.second > indexObj) {
/* 50 */         return 1;
/*    */       }
/* 52 */       return -1;
/*    */     }
/* 54 */     if (this.first > indexObj) {
/* 55 */       return 1;
/*    */     }
/* 57 */     return -1;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 62 */     int indexObj = ((SimplePair)obj).getFirstElement();
/* 63 */     if (this.first == indexObj) {
/* 64 */       indexObj = ((SimplePair)obj).getSecondElement();
/* 65 */       if (this.second == indexObj) {
/* 66 */         return true;
/*    */       }
/* 68 */       return false;
/*    */     }
/* 70 */     return false;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 76 */     long code = this.first * hashCapacity + this.second;
/* 77 */     return (int)code;
/*    */   }
/*    */ 
/*    */   public static void setHashCapacity(int capacity) {
/* 81 */     hashCapacity = capacity;
/*    */   }
/*    */ 
/*    */   public static int getHashCapacity() {
/* 85 */     return (int)hashCapacity;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.SimplePair
 * JD-Core Version:    0.6.2
 */