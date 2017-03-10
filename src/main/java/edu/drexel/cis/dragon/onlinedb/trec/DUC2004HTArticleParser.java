/*    */ package edu.drexel.cis.dragon.onlinedb.trec;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.ArticleParser;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ 
/*    */ public class DUC2004HTArticleParser
/*    */   implements ArticleParser
/*    */ {
/*    */   public String assemble(Article article)
/*    */   {
/* 16 */     return null;
/*    */   }
/*    */ 
/*    */   public Article parse(String content)
/*    */   {
/* 23 */     BasicArticle article = null;
/*    */     try {
/* 25 */       article = new BasicArticle();
/*    */ 
/* 28 */       int start = content.indexOf("docid=");
/* 29 */       if (start < 0)
/* 30 */         return null;
/* 31 */       start += 6;
/* 32 */       int end = content.indexOf(" ", start);
/* 33 */       article.setKey(content.substring(start, end).trim());
/*    */ 
/* 36 */       start = content.indexOf(">", end + 1);
/* 37 */       start++;
/* 38 */       end = content.indexOf("(AFP)", start);
/* 39 */       if (end > 0) {
/* 40 */         start = end + 5;
/* 41 */         end = content.indexOf(" - ", start);
/* 42 */         if (end < start + 5)
/* 43 */           start = end + 3;
/*    */       }
/* 45 */       article.setBody(content.substring(start));
/* 46 */       return article;
/*    */     }
/*    */     catch (Exception e) {
/* 49 */       e.printStackTrace();
/* 50 */       if (article.getKey() != null)
/* 51 */         return article;
/*    */     }
/* 53 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.trec.DUC2004HTArticleParser
 * JD-Core Version:    0.6.2
 */