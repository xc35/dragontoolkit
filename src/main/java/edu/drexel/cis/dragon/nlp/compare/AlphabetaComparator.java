/*    */ package edu.drexel.cis.dragon.nlp.compare;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class AlphabetaComparator
/*    */   implements Comparator, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private int direction;
/*    */ 
/*    */   public AlphabetaComparator()
/*    */   {
/* 18 */     this.direction = 1;
/*    */   }
/*    */ 
/*    */   public AlphabetaComparator(boolean reversed) {
/* 22 */     if (reversed)
/* 23 */       this.direction = -1;
/*    */     else
/* 25 */       this.direction = 1;
/*    */   }
/*    */ 
/*    */   public int compare(Object firstObj, Object secondObj)
/*    */   {
/* 32 */     String first = firstObj.toString();
/* 33 */     String second = secondObj.toString();
/* 34 */     return first.compareTo(second) * this.direction;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj) {
/* 38 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.compare.AlphabetaComparator
 * JD-Core Version:    0.6.2
 */