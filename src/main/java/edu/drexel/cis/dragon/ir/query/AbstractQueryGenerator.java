/*    */ package edu.drexel.cis.dragon.ir.query;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ 
/*    */ public abstract class AbstractQueryGenerator
/*    */   implements QueryGenerator
/*    */ {
/*    */   public IRQuery generate(String topic)
/*    */   {
/* 19 */     BasicArticle article = new BasicArticle();
/* 20 */     article.setTitle(topic);
/* 21 */     return generate(article);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.AbstractQueryGenerator
 * JD-Core Version:    0.6.2
 */