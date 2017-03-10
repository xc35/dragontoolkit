/*    */ package edu.drexel.cis.dragon.nlp.tool.lemmatiser;
/*    */ 
/*    */ public class SuffixEDetachOperation
/*    */   implements Operation
/*    */ {
/*    */   private String vowel;
/*    */   private String suffix;
/*    */   private int pos;
/*    */   private boolean indexLookupOption;
/*    */ 
/*    */   SuffixEDetachOperation(int POS, String suffix)
/*    */   {
/* 19 */     this.pos = POS;
/* 20 */     this.suffix = suffix;
/* 21 */     this.vowel = "aieou";
/* 22 */     this.indexLookupOption = true;
/*    */   }
/*    */ 
/*    */   public boolean getIndexLookupOption() {
/* 26 */     return this.indexLookupOption;
/*    */   }
/*    */ 
/*    */   public void setIndexLookupOption(boolean option) {
/* 30 */     this.indexLookupOption = option;
/*    */   }
/*    */ 
/*    */   public String execute(String derivation)
/*    */   {
/* 36 */     if ((derivation.length() > this.suffix.length()) && (derivation.endsWith(this.suffix))) {
/* 37 */       String base = derivation.substring(0, derivation.length() - this.suffix.length());
/* 38 */       if (!endingEPattern(base)) {
/* 39 */         return base;
/*    */       }
/*    */ 
/* 42 */       return base + "e";
/*    */     }
/*    */ 
/* 45 */     return null;
/*    */   }
/*    */ 
/*    */   private boolean endingEPattern(String base)
/*    */   {
/* 51 */     int len = base.length();
/* 52 */     if (len < 3) return false;
/* 53 */     if ((this.vowel.indexOf(base.charAt(len - 1)) < 0) && (this.vowel.indexOf(base.charAt(len - 2)) >= 0) && (this.vowel.indexOf(base.charAt(len - 3)) < 0))
/* 54 */       return true;
/* 55 */     return false;
/*    */   }
/*    */ 
/*    */   public String getSuffix() {
/* 59 */     return this.suffix;
/*    */   }
/*    */ 
/*    */   public int getPOSIndex() {
/* 63 */     return this.pos;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.lemmatiser.SuffixEDetachOperation
 * JD-Core Version:    0.6.2
 */