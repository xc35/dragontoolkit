/*    */ package edu.drexel.cis.dragon.qa.filter;
/*    */ 
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public abstract class AbstractFilter
/*    */   implements CandidateFilter
/*    */ {
/*    */   public ArrayList filter(QuestionQuery query, ArrayList list)
/*    */   {
/* 13 */     ArrayList finalList = new ArrayList();
/* 14 */     for (int i = 0; i < list.size(); i++) {
/* 15 */       Candidate curTerm = (Candidate)list.get(i);
/* 16 */       if (keep(query, curTerm))
/* 17 */         finalList.add(curTerm);
/*    */     }
/* 19 */     return finalList;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.filter.AbstractFilter
 * JD-Core Version:    0.6.2
 */