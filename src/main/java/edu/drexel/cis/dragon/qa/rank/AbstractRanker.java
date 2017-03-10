/*    */ package edu.drexel.cis.dragon.qa.rank;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public abstract class AbstractRanker
/*    */   implements CandidateRanker
/*    */ {
/*    */   protected int top;
/*    */ 
/*    */   public AbstractRanker()
/*    */   {
/*  9 */     this.top = 15;
/*    */   }
/*    */ 
/*    */   protected ArrayList getTopCandidate(ArrayList list)
/*    */   {
/* 15 */     if ((this.top <= 0) || (list.size() < this.top)) {
/* 16 */       return list;
/*    */     }
/* 18 */     ArrayList newList = new ArrayList(this.top);
/* 19 */     for (int i = 0; i < list.size(); i++)
/* 20 */       newList.add(list.get(i));
/* 21 */     return newList;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.rank.AbstractRanker
 * JD-Core Version:    0.6.2
 */