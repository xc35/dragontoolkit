/*    */ package edu.drexel.cis.dragon.cptc;
/*    */ 
/*    */ public class ConceptCategory
/*    */   implements Comparable
/*    */ {
/*    */   private String concept;
/*    */   private String[] prediction;
/*    */   private String actual;
/*    */ 
/*    */   public ConceptCategory(String concept)
/*    */   {
/*  9 */     this.concept = concept;
/*    */   }
/*    */ 
/*    */   public String getConcept() {
/* 13 */     return this.concept;
/*    */   }
/*    */ 
/*    */   public String[] getPrediction() {
/* 17 */     return this.prediction;
/*    */   }
/*    */ 
/*    */   public void setPrediction(String[] arrLabel) {
/* 21 */     this.prediction = arrLabel;
/*    */   }
/*    */ 
/*    */   public String getActual() {
/* 25 */     return this.actual;
/*    */   }
/*    */ 
/*    */   public void setActual(String actual) {
/* 29 */     this.actual = actual;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj) {
/* 33 */     return this.concept.compareToIgnoreCase(((ConceptCategory)obj).getConcept());
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.cptc.ConceptCategory
 * JD-Core Version:    0.6.2
 */