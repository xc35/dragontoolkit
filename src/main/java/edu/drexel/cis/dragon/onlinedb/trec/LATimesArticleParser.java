/*    */ package edu.drexel.cis.dragon.onlinedb.trec;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.Conversion;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class LATimesArticleParser extends SgmArticleParser
/*    */ {
/*    */   protected Date extractDate(String rawText)
/*    */   {
/*    */     try
/*    */     {
/* 21 */       String content = processParagraph(getTagContent(rawText, "DATE", false));
/* 22 */       int start = content.indexOf(",") + 1;
/* 23 */       start = content.indexOf(",", start);
/* 24 */       if (start < 0)
/* 25 */         return null;
/* 26 */       content = content.substring(0, start);
/* 27 */       return Conversion.engDate(content);
/*    */     }
/*    */     catch (Exception e) {
/* 30 */       e.printStackTrace();
/* 31 */     }return null;
/*    */   }
/*    */ 
/*    */   protected String extractMeta(String rawText)
/*    */   {
/*    */     try
/*    */     {
/* 40 */       String content = processParagraph(getTagContent(rawText, "SECTION", false));
/* 41 */       int start = content.lastIndexOf(";");
/* 42 */       String last = content.substring(start + 1).trim();
/* 43 */       if (!last.endsWith("Desk"))
/* 44 */         return null;
/* 45 */       return last.substring(0, last.length() - 4).trim();
/*    */     }
/*    */     catch (Exception e) {
/* 48 */       e.printStackTrace();
/* 49 */     }return null;
/*    */   }
/*    */ 
/*    */   protected int extractLength(String rawText)
/*    */   {
/*    */     try
/*    */     {
/* 58 */       String content = processParagraph(getTagContent(rawText, "LENGTH", false));
/* 59 */       if (content == null)
/* 60 */         return -1;
/* 61 */       int start = content.indexOf(" ");
/* 62 */       content = content.substring(0, start);
/* 63 */       return Integer.parseInt(content);
/*    */     }
/*    */     catch (Exception e) {
/* 66 */       e.printStackTrace();
/* 67 */     }return -1;
/*    */   }
/*    */ 
/*    */   private String processParagraph(String paragraph)
/*    */   {
/* 75 */     if (paragraph == null)
/* 76 */       return null;
/* 77 */     int start = paragraph.indexOf("<P>");
/* 78 */     if (start < 0)
/* 79 */       start = 0;
/*    */     else
/* 81 */       start += 3;
/* 82 */     int end = paragraph.indexOf("</P>", start);
/*    */     String content;
/* 83 */     if (end > 0)
/* 84 */       content = paragraph.substring(start, end);
/*    */     else
/* 86 */       content = paragraph.substring(start);
/* 87 */      content = content.replace('\r', ' ');
/* 88 */     content = content.replace('\n', ' ');
/* 89 */     return content.trim();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.trec.LATimesArticleParser
 * JD-Core Version:    0.6.2
 */