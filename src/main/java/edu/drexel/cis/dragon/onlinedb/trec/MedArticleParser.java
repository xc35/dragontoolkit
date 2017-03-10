/*    */ package edu.drexel.cis.dragon.onlinedb.trec;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.ArticleParser;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ 
/*    */ public class MedArticleParser
/*    */   implements ArticleParser
/*    */ {
/*    */   public Article parse(String content)
/*    */   {
/*    */     try
/*    */     {
/* 21 */       BasicArticle article = new BasicArticle();
/*    */ 
/* 24 */       int start = content.indexOf("<DOCNO>") + 7;
/* 25 */       int end = content.indexOf("<", start);
/* 26 */       article.setKey(content.substring(start, end).trim());
/*    */ 
/* 29 */       start = end;
/* 30 */       start = content.indexOf("<ArticleTitle>");
/* 31 */       if (start >= 0) {
/* 32 */         start += 14;
/* 33 */         end = content.indexOf("</ArticleTitle>", start);
/* 34 */         article.setTitle(content.substring(start, end).replace('\n', ' '));
/*    */       }
/*    */ 
/* 38 */       start = end;
/* 39 */       start = content.indexOf("<AbstractText>");
/* 40 */       if (start >= 0) {
/* 41 */         start += 14;
/* 42 */         end = content.indexOf("</AbstractText>", start);
/* 43 */         article.setAbstract(content.substring(start, end));
/*    */       }
/*    */ 
/* 46 */       return article;
/*    */     }
/*    */     catch (Exception e) {
/* 49 */       e.printStackTrace();
/* 50 */     }return null;
/*    */   }
/*    */ 
/*    */   public String assemble(Article article)
/*    */   {
/* 55 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.trec.MedArticleParser
 * JD-Core Version:    0.6.2
 */