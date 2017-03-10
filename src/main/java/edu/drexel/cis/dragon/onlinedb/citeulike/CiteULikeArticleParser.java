/*    */ package edu.drexel.cis.dragon.onlinedb.citeulike;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticleParser;
/*    */ import edu.drexel.cis.dragon.onlinedb.bibtex.BibTeXArticle;
/*    */ 
/*    */ public class CiteULikeArticleParser extends BasicArticleParser
/*    */ {
/*    */   public Article parse(String raw)
/*    */   {
/* 26 */     int start = raw.indexOf("<pre>");
/* 27 */     if (start < 0)
/* 28 */       return null;
/* 29 */     start += 5;
/* 30 */     int end = raw.indexOf("</pre>", start);
/* 31 */     if (end < 0)
/* 32 */       return null;
/* 33 */     Article article = new BibTeXArticle(raw.substring(start, end));
/* 34 */     if (article != null) {
/* 35 */       if (article.getKey() != null) {
/* 36 */         start = article.getKey().indexOf(':');
/* 37 */         if (start >= 0)
/* 38 */           article.setKey(article.getKey().substring(start + 1));
/*    */       }
/* 40 */       if (article.getMeta() != null) {
/* 41 */         article.setMeta(article.getMeta().replace('-', '_'));
/*    */       }
/*    */     }
/*    */ 
/* 45 */     return article;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.citeulike.CiteULikeArticleParser
 * JD-Core Version:    0.6.2
 */