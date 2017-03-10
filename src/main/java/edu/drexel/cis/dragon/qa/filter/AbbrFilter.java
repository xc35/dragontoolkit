/*    */ package edu.drexel.cis.dragon.qa.filter;
/*    */ 
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class AbbrFilter extends AbstractFilter
/*    */ {
/*    */   public boolean keep(QuestionQuery query, Candidate candidate)
/*    */   {
/* 10 */     return true;
/*    */   }
/*    */ 
/*    */   public ArrayList filter(QuestionQuery query, ArrayList list) {
/* 14 */     return list;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.filter.AbbrFilter
 * JD-Core Version:    0.6.2
 */