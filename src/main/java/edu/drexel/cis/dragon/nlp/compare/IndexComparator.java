/*    */ package edu.drexel.cis.dragon.nlp.compare;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class IndexComparator
/*    */   implements Comparator, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private int direction;
/*    */ 
/*    */   public IndexComparator()
/*    */   {
/* 19 */     this.direction = 1;
/*    */   }
/*    */ 
/*    */   public IndexComparator(boolean reversed) {
/* 23 */     if (reversed)
/* 24 */       this.direction = -1;
/*    */     else
/* 26 */       this.direction = 1;
/*    */   }
/*    */ 
/*    */   public int compare(Object firstObj, Object secondObj)
/*    */   {
/* 31 */     int index1 = ((IndexSortable)firstObj).getIndex() * this.direction;
/* 32 */     int index2 = ((IndexSortable)secondObj).getIndex() * this.direction;
/* 33 */     if (index1 < index2)
/* 34 */       return -1;
/* 35 */     if (index1 > index2) {
/* 36 */       return 1;
/*    */     }
/* 38 */     return 0;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj) {
/* 42 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.compare.IndexComparator
 * JD-Core Version:    0.6.2
 */