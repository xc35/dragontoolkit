/*    */ package edu.drexel.cis.dragon.qa.system;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.compare.WeightSortable;
/*    */ 
/*    */ public class SupportingDoc
/*    */   implements WeightSortable
/*    */ {
/*    */   private String url;
/*    */   private String snippet;
/*    */   private double weight;
/*    */   private int sentIndex;
/*    */ 
/*    */   public SupportingDoc(int sentIndex, double weight)
/*    */   {
/* 12 */     this.weight = weight;
/* 13 */     this.sentIndex = sentIndex;
/*    */   }
/*    */ 
/*    */   public double getWeight() {
/* 17 */     return this.weight;
/*    */   }
/*    */ 
/*    */   public void setWeight(double weight) {
/* 21 */     this.weight = weight;
/*    */   }
/*    */ 
/*    */   public String getURL() {
/* 25 */     return this.url;
/*    */   }
/*    */ 
/*    */   public void setURL(String url) {
/* 29 */     this.url = url;
/*    */   }
/*    */ 
/*    */   public String getSnippet() {
/* 33 */     return this.snippet;
/*    */   }
/*    */ 
/*    */   public void setSnippet(String snippet) {
/* 37 */     this.snippet = snippet;
/*    */   }
/*    */ 
/*    */   public int getSentenceIndex() {
/* 41 */     return this.sentIndex;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.system.SupportingDoc
 * JD-Core Version:    0.6.2
 */