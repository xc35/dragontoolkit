/*    */ package edu.drexel.cis.dragon.nlp.tool.lemmatiser;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ 
/*    */ public class VerbOperation
/*    */   implements Operation
/*    */ {
/*    */   private SortedArray verbIndexList;
/*    */ 
/*    */   public VerbOperation(SortedArray verbIndexList)
/*    */   {
/* 18 */     this.verbIndexList = verbIndexList;
/*    */   }
/*    */ 
/*    */   public boolean getIndexLookupOption() {
/* 22 */     return false;
/*    */   }
/*    */ 
/*    */   public String execute(String derivation) {
/* 26 */     if (this.verbIndexList == null) return null;
/*    */ 
/* 28 */     if (derivation.endsWith("ed")) {
/* 29 */       derivation = derivation.substring(0, derivation.length() - 2);
/* 30 */       if (this.verbIndexList.binarySearch(derivation) > 0) {
/* 31 */         return derivation;
/*    */       }
/* 33 */       return null;
/*    */     }
/* 35 */     if (derivation.endsWith("ing")) {
/* 36 */       derivation = derivation.substring(0, derivation.length() - 3);
/* 37 */       if (this.verbIndexList.binarySearch(derivation) > 0) {
/* 38 */         return derivation;
/*    */       }
/* 40 */       return null;
/*    */     }
/* 42 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.lemmatiser.VerbOperation
 * JD-Core Version:    0.6.2
 */