/*    */ package edu.drexel.cis.dragon.onlinedb.isi;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.ArticleParser;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ 
/*    */ public class ISIArticleParser
/*    */   implements ArticleParser
/*    */ {
/*    */   public Article parse(String content)
/*    */   {
/* 20 */     String[] fields = (String[])null;
/* 21 */     Article article = new BasicArticle();
/* 22 */     if (content == null) return null;
/* 23 */     fields = content.split("\t");
/* 24 */     article.setAbstract(fields[10]);
/* 25 */     article.setMeta(fields[1]);
/* 26 */     article.setTitle(fields[3]);
/* 27 */     article.setKey(fields[(fields.length - 1)]);
/*    */ 
/* 29 */     return article;
/*    */   }
/*    */ 
/*    */   public String assemble(Article article) {
/* 33 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.isi.ISIArticleParser
 * JD-Core Version:    0.6.2
 */