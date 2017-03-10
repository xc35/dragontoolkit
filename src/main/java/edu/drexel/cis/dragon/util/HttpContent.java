/*    */ package edu.drexel.cis.dragon.util;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class HttpContent
/*    */ {
/* 13 */   private static String formatTags = "<b> <font> <u> <i> <strong> <small> <big>";
/*    */ 
/*    */   public String extractText(String rawContent)
/*    */   {
/*    */     try
/*    */     {
/* 24 */       String content = rawContent.toLowerCase();
/* 25 */       StringBuffer sb = new StringBuffer();
/* 26 */       int start = 0;
/* 27 */       int lastPos = 0;
/*    */ 
/* 29 */       while (start >= 0)
/*    */       {
/* 31 */         start = content.indexOf('<', start);
/*    */ 
/* 33 */         if (start >= 0) {
/* 34 */           if (start > lastPos) {
/* 35 */             sb.append(rawContent.substring(lastPos, start));
/*    */           }
/* 37 */           int end = content.indexOf(">", start);
/* 38 */           if (end < 0)
/*    */             break;
/* 40 */           String tagLine = content.substring(start + 1, end).trim();
/* 41 */           String tag = getTagName(tagLine);
/* 42 */           if ((tag.equals("style")) || (tag.equals("script")))
/*    */           {
/* 44 */             if ((!tagLine.endsWith("/")) && (!tagLine.startsWith("/"))) {
/* 45 */               end = content.indexOf("</" + tag, end + 1);
/* 46 */               end = content.indexOf(">", end + 1);
/*    */             }
/*    */           }
/*    */ 
/* 50 */           if ((sb.length() > 0) && (sb.charAt(sb.length() - 1) != ' ') && (needSpace(tag))) {
/* 51 */             sb.append(' ');
/*    */           }
/* 53 */           lastPos = end + 1;
/* 54 */           if (end < start)
/*    */           {
/* 56 */             System.out.println("Error occur!!!");
/* 57 */             break;
/*    */           }
/*    */ 
/* 60 */           start = end;
/*    */         }
/*    */       }
/* 63 */       if (lastPos < content.length())
/* 64 */         sb.append(content.substring(lastPos).trim());
/* 65 */       String str = sb.toString();
/* 66 */       str = str.replaceAll("(&nbsp;)+", " ");
/* 67 */       return str.replaceAll(" +", " ").trim();
/*    */     }
/*    */     catch (Exception e) {
/* 70 */       e.printStackTrace();
/* 71 */     }return null;
/*    */   }
/*    */ 
/*    */   private boolean needSpace(String tag)
/*    */   {
/* 76 */     if (formatTags.indexOf("<" + tag.toLowerCase() + ">") >= 0) {
/* 77 */       return false;
/*    */     }
/* 79 */     return true;
/*    */   }
/*    */ 
/*    */   private String getTagName(String fragment) {
/* 83 */     if (fragment.charAt(0) == '/') {
/* 84 */       return fragment.substring(1).trim();
/*    */     }
/* 86 */     int start = fragment.indexOf(' ');
/* 87 */     if (start < 0) {
/* 88 */       return fragment;
/*    */     }
/* 90 */     return fragment.substring(0, start);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.HttpContent
 * JD-Core Version:    0.6.2
 */