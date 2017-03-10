/*    */ package edu.drexel.cis.dragon.nlp.ontology;
/*    */ 
/*    */ public class BasicTerm
/*    */   implements Comparable
/*    */ {
/*    */   private String term;
/*    */   private String[] arrCUI;
/*    */   private int index;
/*    */ 
/*    */   public BasicTerm(int index, String term, String[] cuis)
/*    */   {
/* 18 */     this.term = term;
/* 19 */     this.arrCUI = cuis;
/* 20 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj)
/*    */   {
/* 26 */     String objValue = ((BasicTerm)obj).getTerm();
/* 27 */     return this.term.compareToIgnoreCase(objValue);
/*    */   }
/*    */ 
/*    */   public int getIndex() {
/* 31 */     return this.index;
/*    */   }
/*    */ 
/*    */   public String getTerm() {
/* 35 */     return this.term;
/*    */   }
/*    */ 
/*    */   public int getCUINum() {
/* 39 */     if (this.arrCUI == null) {
/* 40 */       return 0;
/*    */     }
/* 42 */     return this.arrCUI.length;
/*    */   }
/*    */ 
/*    */   public String getCUI(int index) {
/* 46 */     return this.arrCUI[index];
/*    */   }
/*    */ 
/*    */   public String[] getAllCUI() {
/* 50 */     return this.arrCUI;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 54 */     return this.term;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.BasicTerm
 * JD-Core Version:    0.6.2
 */