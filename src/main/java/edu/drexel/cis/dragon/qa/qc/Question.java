/*    */ package edu.drexel.cis.dragon.qa.qc;
/*    */ 
/*    */ public class Question
/*    */ {
/*    */   private String key;
/*    */   private String qword;
/*    */   private String category;
/*    */   private boolean training;
/*    */ 
/*    */   public Question(String key)
/*    */   {
/* 10 */     this.key = key;
/* 11 */     this.training = true;
/*    */   }
/*    */ 
/*    */   public String getQuestionKey() {
/* 15 */     return this.key;
/*    */   }
/*    */ 
/*    */   public void setQuestionWord(String qword) {
/* 19 */     this.qword = qword;
/*    */   }
/*    */ 
/*    */   public String getQuestionWord() {
/* 23 */     return this.qword;
/*    */   }
/*    */ 
/*    */   public void setCategory(String category) {
/* 27 */     this.category = category;
/*    */   }
/*    */ 
/*    */   public String getCategory() {
/* 31 */     return this.category;
/*    */   }
/*    */ 
/*    */   public void setTrainingOption(boolean training) {
/* 35 */     this.training = training;
/*    */   }
/*    */ 
/*    */   public boolean getTrainingOption() {
/* 39 */     return this.training;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.qc.Question
 * JD-Core Version:    0.6.2
 */