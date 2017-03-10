/*    */ package edu.drexel.cis.dragon.nlp.tool.lemmatiser;
/*    */ 
/*    */ public class SuffixDetachOperation
/*    */   implements Operation
/*    */ {
/*    */   private String suffix;
/*    */   private String changeTo;
/*    */   private int pos;
/*    */   private boolean indexLookupOption;
/*    */ 
/*    */   SuffixDetachOperation(int POS, String suffix, String changeTo)
/*    */   {
/* 18 */     this.pos = POS;
/* 19 */     this.suffix = suffix;
/* 20 */     this.changeTo = changeTo;
/* 21 */     this.indexLookupOption = false;
/*    */   }
/*    */ 
/*    */   public String execute(String derivation) {
/* 25 */     if ((derivation.length() > this.suffix.length()) && (derivation.endsWith(this.suffix))) {
/* 26 */       if (this.changeTo == null) {
/* 27 */         return derivation.substring(0, derivation.length() - this.suffix.length());
/*    */       }
/*    */ 
/* 30 */       return derivation.substring(0, derivation.length() - this.suffix.length()) + this.changeTo;
/*    */     }
/*    */ 
/* 33 */     return null;
/*    */   }
/*    */ 
/*    */   public boolean getIndexLookupOption() {
/* 37 */     return this.indexLookupOption;
/*    */   }
/*    */ 
/*    */   public void setIndexLookupOption(boolean option) {
/* 41 */     this.indexLookupOption = option;
/*    */   }
/*    */ 
/*    */   public String getSuffix() {
/* 45 */     return this.suffix;
/*    */   }
/*    */ 
/*    */   public String getChangeTo() {
/* 49 */     return this.changeTo;
/*    */   }
/*    */ 
/*    */   public int getPOSIndex() {
/* 53 */     return this.pos;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.lemmatiser.SuffixDetachOperation
 * JD-Core Version:    0.6.2
 */