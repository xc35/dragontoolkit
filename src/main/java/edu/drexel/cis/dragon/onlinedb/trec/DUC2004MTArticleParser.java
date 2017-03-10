/*    */ package edu.drexel.cis.dragon.onlinedb.trec;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.ArticleParser;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ 
/*    */ public class DUC2004MTArticleParser
/*    */   implements ArticleParser
/*    */ {
/*    */   public String assemble(Article article)
/*    */   {
/* 16 */     return null;
/*    */   }
/*    */ 
/*    */   public Article parse(String content)
/*    */   {
/* 25 */     BasicArticle article = null;
/*    */     try {
/* 27 */       article = new BasicArticle();
/*    */ 
/* 30 */       int start = content.indexOf("<DOCNO>") + 7;
/* 31 */       int end = content.indexOf("<", start);
/* 32 */       article.setKey(content.substring(start, end).trim());
/*    */ 
/* 35 */       StringBuffer body = null;
/* 36 */       start = content.indexOf("<s num", end);
/* 37 */       while (start > 0) {
/* 38 */         start = content.indexOf(">", start + 6) + 1;
/* 39 */         end = content.indexOf("</s>", start);
/* 40 */         String sentence = content.substring(start, end);
/* 41 */         start = sentence.indexOf("(AFP) -");
/* 42 */         if (start >= 0)
/* 43 */           sentence = sentence.substring(start + 7);
/* 44 */         if (body == null) {
/* 45 */           body = new StringBuffer(sentence);
/*    */         } else {
/* 47 */           body.append(' ');
/* 48 */           body.append(sentence);
/*    */         }
/* 50 */         start = content.indexOf("<s num", end + 4);
/*    */       }
/* 52 */       article.setBody(body.toString());
/* 53 */       return article;
/*    */     }
/*    */     catch (Exception e) {
/* 56 */       e.printStackTrace();
/* 57 */       if (article.getKey() != null)
/* 58 */         return article;
/*    */     }
/* 60 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.trec.DUC2004MTArticleParser
 * JD-Core Version:    0.6.2
 */