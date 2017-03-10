/*    */ package edu.drexel.cis.dragon.nlp.compare;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Term;
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class TermLemmaComparator
/*    */   implements Comparator, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public int compare(Object firstObj, Object secondObj)
/*    */   {
/* 20 */     String first = ((Term)firstObj).toLemmaString();
/* 21 */     String second = ((Term)secondObj).toLemmaString();
/* 22 */     return first.compareTo(second);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj) {
/* 26 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.compare.TermLemmaComparator
 * JD-Core Version:    0.6.2
 */