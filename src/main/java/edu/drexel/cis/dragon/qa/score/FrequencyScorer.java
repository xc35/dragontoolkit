/*    */ package edu.drexel.cis.dragon.qa.score;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.qa.query.QueryWord;
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*    */ 
/*    */ public class FrequencyScorer
/*    */   implements CandidateScorer
/*    */ {
/*    */   public double initialize(QuestionQuery query, Sentence sent, QueryWord[] arrWord)
/*    */   {
/* 10 */     return 1.0D;
/*    */   }
/*    */ 
/*    */   public double score(Candidate candidate) {
/* 14 */     candidate.setWeight(candidate.getFrequency());
/* 15 */     return candidate.getWeight();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.score.FrequencyScorer
 * JD-Core Version:    0.6.2
 */