/*    */ package edu.drexel.cis.dragon.ir.search;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*    */ import edu.drexel.cis.dragon.ir.search.feedback.Feedback;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class FeedbackSearcher extends AbstractSearcher
/*    */ {
/*    */   private Searcher searcher;
/*    */   private Feedback feedback;
/*    */ 
/*    */   public FeedbackSearcher(Searcher searcher, Feedback feedback)
/*    */   {
/* 21 */     super(searcher.getIndexReader(), searcher.getSmoother());
/* 22 */     this.searcher = searcher;
/* 23 */     this.feedback = feedback;
/*    */   }
/*    */ 
/*    */   public int search(IRQuery query) {
/* 27 */     this.query = this.feedback.updateQueryModel(query);
/* 28 */     this.searcher.search(this.query);
/* 29 */     this.hitlist = this.searcher.getRankedDocumentList();
/* 30 */     return this.hitlist.size();
/*    */   }
/*    */ 
/*    */   public Feedback getFeedback() {
/* 34 */     return this.feedback;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.FeedbackSearcher
 * JD-Core Version:    0.6.2
 */