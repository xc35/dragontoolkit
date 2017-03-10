/*    */ package edu.drexel.cis.dragon.qa.query;
/*    */ 
/*    */ public class WordScorer
/*    */   implements QueryScorer
/*    */ {
/*    */   public void score(QuestionQuery query)
/*    */   {
/* 13 */     double totalScore = 0.0D;
/* 14 */     for (int i = 0; i < query.size(); i++) {
/* 15 */       QueryWord curQWord = query.getQueryWord(i);
/* 16 */       if (!curQWord.isFunctionalWord()) {
/* 17 */         curQWord.setWeight(1.0D);
/*    */       }
/*    */       else {
/* 20 */         curQWord.setWeight(0.0D);
/*    */       }
/* 22 */       totalScore += curQWord.getWeight();
/*    */     }
/*    */ 
/* 25 */     for (int i = 0; i < query.size(); i++)
/* 26 */       query.getQueryWord(i).setWeight(query.getQueryWord(i).getWeight() / totalScore);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.query.WordScorer
 * JD-Core Version:    0.6.2
 */