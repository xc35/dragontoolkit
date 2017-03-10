/*    */ package edu.drexel.cis.dragon.onlinedb.trec;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ 
/*    */ public class CoarseSgmArticleParser extends SgmArticleParser
/*    */ {
/*    */   public Article parse(String content)
/*    */   {
/* 21 */     BasicArticle article = null;
/*    */     try {
/* 23 */       article = new BasicArticle();
/*    */ 
/* 26 */       int start = content.indexOf("<DOCNO>") + 7;
/* 27 */       int end = content.indexOf("<", start);
/* 28 */       article.setKey(content.substring(start, end).trim());
/*    */ 
/* 31 */       article.setBody(removeTag(getBodyContent(content, end + 8)));
/* 32 */       return article;
/*    */     }
/*    */     catch (Exception e) {
/* 35 */       e.printStackTrace();
/* 36 */       if (article.getKey() != null)
/* 37 */         return article;
/*    */     }
/* 39 */     return null;
/*    */   }
/*    */ 
/*    */   protected String getBodyContent(String rawText, int start)
/*    */   {
/* 53 */     start = rawText.indexOf("</", start);
/* 54 */     int end = rawText.indexOf(">", start + 1);
/* 55 */     return rawText.substring(end + 1);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.trec.CoarseSgmArticleParser
 * JD-Core Version:    0.6.2
 */