/*    */ package edu.drexel.cis.dragon.nlp.tool.lemmatiser;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ 
/*    */ public class LemmatiserPOS
/*    */ {
/*    */   private Operation[] operations;
/*    */   private int pos;
/*    */   private SortedArray indexList;
/*    */ 
/*    */   public LemmatiserPOS(int POS, Operation[] operations)
/*    */   {
/* 19 */     this.operations = operations;
/* 20 */     this.pos = POS;
/* 21 */     this.indexList = null;
/*    */   }
/*    */ 
/*    */   public LemmatiserPOS(int POS, Operation[] operations, SortedArray indexList) {
/* 25 */     this.operations = operations;
/* 26 */     this.pos = POS;
/* 27 */     this.indexList = indexList;
/*    */   }
/*    */ 
/*    */   public String lemmatise(String derivation)
/*    */   {
/* 35 */     boolean indexChecked = false;
/* 36 */     for (int i = 0; i < this.operations.length; i++) {
/* 37 */       String base = this.operations[i].execute(derivation);
/* 38 */       if (base != null) {
/* 39 */         indexChecked = true;
/* 40 */         if ((!this.operations[i].getIndexLookupOption()) || (this.indexList == null) || (this.indexList.contains(base)))
/* 41 */           return base;
/*    */       }
/*    */     }
/* 44 */     if (indexChecked) {
/* 45 */       return null;
/*    */     }
/* 47 */     if ((this.indexList != null) && (this.indexList.contains(derivation))) {
/* 48 */       return derivation;
/*    */     }
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */   public int getPOSIndex()
/*    */   {
/* 55 */     return this.pos;
/*    */   }
/*    */ 
/*    */   public SortedArray getIndexList() {
/* 59 */     return this.indexList;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.lemmatiser.LemmatiserPOS
 * JD-Core Version:    0.6.2
 */