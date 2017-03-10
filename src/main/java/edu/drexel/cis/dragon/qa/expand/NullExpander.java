/*    */ package edu.drexel.cis.dragon.qa.expand;
/*    */ 
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.system.CandidateBase;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class NullExpander
/*    */   implements CandidateExpander
/*    */ {
/*    */   public ArrayList expand(QuestionQuery query, CandidateBase base, ArrayList list)
/*    */   {
/* 10 */     return list;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.expand.NullExpander
 * JD-Core Version:    0.6.2
 */