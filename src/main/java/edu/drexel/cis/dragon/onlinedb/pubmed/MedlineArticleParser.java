/*    */ package edu.drexel.cis.dragon.onlinedb.pubmed;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticleParser;
/*    */ 
/*    */ public class MedlineArticleParser extends BasicArticleParser
/*    */ {
/*    */   public Article parse(String content)
/*    */   {
/*    */     try
/*    */     {
/* 20 */       BasicArticle article = new BasicArticle();
/* 21 */       article.setKey(processText(getSection(content, "PMID", 0)));
/* 22 */       String title = getSection(content, "TI", 0);
/* 23 */       if (title != null) {
/* 24 */         title = processText(title).trim();
/* 25 */         if (title.charAt(0) == '[')
/* 26 */           title = title.substring(1, title.length() - 1);
/*    */       }
/* 28 */       article.setTitle(title);
/* 29 */       article.setAbstract(getSection(content, "AB", 0));
/* 30 */       return article;
/*    */     }
/*    */     catch (Exception e) {
/* 33 */       e.printStackTrace();
/* 34 */     }return null;
/*    */   }
/*    */ 
/*    */   protected String getSection(String content, String name, int start)
/*    */   {
/* 41 */     while (name.length() < 4)
/* 42 */       name = name + " ";
/* 43 */     name = name + "-";
/* 44 */     start = content.indexOf(name, start);
/* 45 */     if (start < 0)
/* 46 */       return null;
/* 47 */     start += 6;
/*    */ 
/* 49 */     int end = content.indexOf("- ", start);
/* 50 */     while (end >= 0) {
/* 51 */       if ((end - 5 > 0) && (content.charAt(end - 5) == '\n')) {
/* 52 */         return content.substring(start, end - 5);
/*    */       }
/* 54 */       end = content.indexOf("- ", end + 2);
/*    */     }
/* 56 */     return content.substring(start);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.pubmed.MedlineArticleParser
 * JD-Core Version:    0.6.2
 */