/*    */ package edu.drexel.cis.dragon.nlp.compare;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class WeightComparator
/*    */   implements Comparator, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private double direction;
/*    */ 
/*    */   public WeightComparator()
/*    */   {
/* 18 */     this.direction = -1.0D;
/*    */   }
/*    */ 
/*    */   public WeightComparator(boolean reversed) {
/* 22 */     if (reversed)
/* 23 */       this.direction = -1.0D;
/*    */     else
/* 25 */       this.direction = 1.0D;
/*    */   }
/*    */ 
/*    */   public int compare(Object firstObj, Object secondObj)
/*    */   {
/* 30 */     double weight1 = ((WeightSortable)firstObj).getWeight() * this.direction;
/* 31 */     double weight2 = ((WeightSortable)secondObj).getWeight() * this.direction;
/* 32 */     if (weight1 < weight2)
/* 33 */       return -1;
/* 34 */     if (weight1 == weight2) {
/* 35 */       return 0;
/*    */     }
/* 37 */     return 1;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.compare.WeightComparator
 * JD-Core Version:    0.6.2
 */