/*    */ package edu.drexel.cis.dragon.nlp.compare;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Concept;
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class ConceptNameComparator
/*    */   implements Comparator, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public int compare(Object firstObj, Object secondObj)
/*    */   {
/* 20 */     String first = ((Concept)firstObj).getName();
/* 21 */     String second = ((Concept)secondObj).getName();
/* 22 */     return first.compareToIgnoreCase(second);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj) {
/* 26 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.compare.ConceptNameComparator
 * JD-Core Version:    0.6.2
 */