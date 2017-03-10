/*    */ package edu.drexel.cis.dragon.ir.search;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*    */ import edu.drexel.cis.dragon.ir.search.expand.QueryExpansion;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class QueryExpansionSearcher extends AbstractSearcher
/*    */ {
/*    */   private Searcher searcher;
/*    */   private QueryExpansion qe;
/*    */ 
/*    */   public QueryExpansionSearcher(Searcher searcher, QueryExpansion qe)
/*    */   {
/* 21 */     super(searcher.getIndexReader(), searcher.getSmoother());
/* 22 */     this.searcher = searcher;
/* 23 */     this.qe = qe;
/*    */   }
/*    */ 
/*    */   public int search(IRQuery query) {
/* 27 */     this.query = this.qe.expand(query);
/* 28 */     this.searcher.search(this.query);
/* 29 */     this.hitlist = this.searcher.getRankedDocumentList();
/* 30 */     return this.hitlist.size();
/*    */   }
/*    */ 
/*    */   public QueryExpansion getQueryExpansion() {
/* 34 */     return this.qe;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.QueryExpansionSearcher
 * JD-Core Version:    0.6.2
 */