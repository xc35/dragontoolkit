/*    */ package edu.drexel.cis.dragon.qa.rank;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.system.CandidateBase;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ 
/*    */ public class ScorerRanker extends AbstractRanker
/*    */ {
/*    */   public ArrayList rank(QuestionQuery query, CandidateBase base, ArrayList list)
/*    */   {
/* 11 */     list = getTopCandidate(list);
/* 12 */     Collections.sort(list, new WeightComparator(true));
/* 13 */     return list;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.rank.ScorerRanker
 * JD-Core Version:    0.6.2
 */