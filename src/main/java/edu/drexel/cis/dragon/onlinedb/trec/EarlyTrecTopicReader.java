/*    */ package edu.drexel.cis.dragon.onlinedb.trec;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class EarlyTrecTopicReader extends AbstractTopicReader
/*    */ {
/*    */   public EarlyTrecTopicReader(String topicFile)
/*    */   {
/* 18 */     super(topicFile);
/*    */   }
/*    */ 
/*    */   protected ArrayList loadTopics(String topicFile)
/*    */   {
/*    */     try
/*    */     {
/* 27 */       String content = FileUtil.readTextFile(topicFile);
/* 28 */       ArrayList list = new ArrayList();
/* 29 */       int start = 0;
/* 30 */       int end = content.indexOf("</top>");
/* 31 */       while (end > 0) {
/* 32 */         end += 6;
/* 33 */         list.add(parseEarlyTrecTopic(content.substring(start, end)));
/* 34 */         start = end;
/* 35 */         end = content.indexOf("</top>", start);
/*    */       }
/* 37 */       return list;
/*    */     }
/*    */     catch (Exception e) {
/* 40 */       e.printStackTrace();
/* 41 */     }return null;
/*    */   }
/*    */ 
/*    */   private Article parseEarlyTrecTopic(String content)
/*    */   {
/* 50 */     BasicArticle article = new BasicArticle();
/* 51 */     String section = getTopicSection("num", content);
/* 52 */     int start = section.indexOf(":") + 1;
/* 53 */     article.setKey(section.substring(start).trim());
/* 54 */     article.setCategory(Integer.parseInt(article.getKey()));
/*    */ 
/* 56 */     section = getTopicSection("title", content);
/* 57 */     if (section != null) {
/* 58 */       start = section.indexOf(":") + 1;
/* 59 */       article.setTitle(section.substring(start).trim());
/*    */     }
/*    */ 
/* 62 */     section = getTopicSection("desc", content);
/* 63 */     if (section != null) {
/* 64 */       start = section.indexOf(":") + 1;
/* 65 */       article.setAbstract(section.substring(start).trim());
/*    */     }
/*    */ 
/* 68 */     section = getTopicSection("narr", content);
/* 69 */     if (section != null) {
/* 70 */       start = section.indexOf(":") + 1;
/* 71 */       article.setBody(section.substring(start).trim());
/*    */     }
/*    */ 
/* 74 */     section = getTopicSection("con", content);
/* 75 */     if (section != null) {
/* 76 */       start = section.indexOf(":") + 1;
/* 77 */       article.setMeta(section.substring(start).trim());
/*    */     }
/*    */ 
/* 80 */     return article;
/*    */   }
/*    */ 
/*    */   private String getTopicSection(String sectionName, String topic)
/*    */   {
/* 86 */     int start = topic.indexOf("<" + sectionName + ">");
/* 87 */     if (start < 0) return null;
/*    */ 
/* 89 */     start += sectionName.length() + 2;
/* 90 */     int end = topic.indexOf("\n<", start);
/* 91 */     topic = topic.substring(start, end);
/* 92 */     return topic.replace('\n', ' ').trim();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.trec.EarlyTrecTopicReader
 * JD-Core Version:    0.6.2
 */