/*    */ package edu.drexel.cis.dragon.nlp.compare;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Concept;
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class ConceptEntryIDComparator
/*    */   implements Comparator, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public int compare(Object firstObj, Object secondObj)
/*    */   {
/* 22 */     Concept first = (Concept)firstObj;
/* 23 */     String cui1 = first.getEntryID();
/* 24 */     Concept second = (Concept)secondObj;
/* 25 */     String cui2 = second.getEntryID();
/* 26 */     if ((cui1 != null) && (cui2 != null))
/* 27 */       return cui1.compareTo(cui2);
/* 28 */     if ((cui1 == null) && (cui2 == null))
/* 29 */       return first.getName().compareToIgnoreCase(second.getName());
/* 30 */     if (cui1 == null) {
/* 31 */       return -1;
/*    */     }
/* 33 */     return 1;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj) {
/* 37 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.compare.ConceptEntryIDComparator
 * JD-Core Version:    0.6.2
 */