/*    */ package edu.drexel.cis.dragon.onlinedb.dm;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticleParser;
/*    */ 
/*    */ public class ReutersArticleParser extends BasicArticleParser
/*    */ {
/*    */   public Article parse(String text)
/*    */   {
/* 22 */     int start = text.indexOf("NEWID") + 7;
/* 23 */     int end = text.indexOf(">") - 1;
/* 24 */     String articleKey = text.substring(start, end);
/* 25 */     Article article = new BasicArticle();
/* 26 */     article.setKey(articleKey);
/*    */ 
/* 29 */     String topics = null;
/* 30 */     start = text.indexOf("<TOPICS>") + 8;
/* 31 */     end = text.indexOf("</TOPICS>");
/* 32 */     String topicStr = text.substring(start, end);
/* 33 */     if (topicStr.length() > 1) {
/* 34 */       while (topicStr.indexOf("<D>") >= 0) {
/* 35 */         start = topicStr.indexOf("<D>") + 3;
/* 36 */         end = topicStr.indexOf("</D>");
/* 37 */         if (topics != null)
/* 38 */           topics = topics + ", " + topicStr.substring(start, end).trim();
/*    */         else
/* 40 */           topics = topicStr.substring(start, end).trim();
/* 41 */         topicStr = topicStr.substring(end + 4);
/*    */       }
/* 43 */       article.setMeta(topics);
/*    */     }
/*    */ 
/* 47 */     start = text.indexOf("<TEXT>") + 6;
/* 48 */     end = text.indexOf("</TEXT>");
/* 49 */     text = text.substring(start, end);
/*    */ 
/* 52 */     start = text.indexOf("<TITLE>");
/* 53 */     if (start >= 0) {
/* 54 */       start += 7;
/* 55 */       end = text.indexOf("</TITLE>");
/* 56 */       String title = text.substring(start, end);
/* 57 */       text = text.substring(end + 8);
/* 58 */       article.setTitle(title);
/*    */     }
/*    */ 
/* 62 */     start = text.indexOf("<BODY>");
/*    */     String body;
/* 63 */     if (start >= 0) {
/* 64 */       start += 6;
/* 65 */       end = text.indexOf("</BODY>", start);
/* 66 */       body = text.substring(start, end);
/*    */     }
/*    */     else {
/* 69 */       end = text.indexOf("</AUTHOR>");
/* 70 */       if (end > 0) {
/* 71 */         text = text.substring(end + 9);
/*    */       }
/* 73 */       end = text.indexOf("</DATELINE>");
/* 74 */       if (end > 0)
/* 75 */         text = text.substring(end + 11);
/* 76 */       body = text;
/*    */     }
/* 78 */     article.setBody(body);
/*    */ 
/* 80 */     return article;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.dm.ReutersArticleParser
 * JD-Core Version:    0.6.2
 */