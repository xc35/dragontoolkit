/*    */ package edu.drexel.cis.dragon.nlp.compare;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class FrequencyComparator
/*    */   implements Comparator, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private int direction;
/*    */ 
/*    */   public FrequencyComparator()
/*    */   {
/* 19 */     this.direction = -1;
/*    */   }
/*    */ 
/*    */   public FrequencyComparator(boolean reversed) {
/* 23 */     if (reversed)
/* 24 */       this.direction = -1;
/*    */     else
/* 26 */       this.direction = 1;
/*    */   }
/*    */ 
/*    */   public int compare(Object firstObj, Object secondObj)
/*    */   {
/* 31 */     int freq1 = ((FrequencySortable)firstObj).getFrequency() * this.direction;
/* 32 */     int freq2 = ((FrequencySortable)secondObj).getFrequency() * this.direction;
/* 33 */     if (freq1 < freq2)
/* 34 */       return -1;
/* 35 */     if (freq1 > freq2) {
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
 * Qualified Name:     dragon.nlp.compare.FrequencyComparator
 * JD-Core Version:    0.6.2
 */