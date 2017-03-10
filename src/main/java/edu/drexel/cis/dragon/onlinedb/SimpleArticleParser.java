/*    */ package edu.drexel.cis.dragon.onlinedb;
/*    */ 
/*    */ public class SimpleArticleParser
/*    */   implements ArticleParser
/*    */ {
/*    */   public Article parse(String content)
/*    */   {
/* 16 */     Article article = new BasicArticle();
/* 17 */     article.setBody(content);
/* 18 */     return article;
/*    */   }
/*    */ 
/*    */   public String assemble(Article article) {
/* 22 */     return article.getBody();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.SimpleArticleParser
 * JD-Core Version:    0.6.2
 */