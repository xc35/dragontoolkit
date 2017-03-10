/*    */ package edu.drexel.cis.dragon.onlinedb.dm;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ 
/*    */ public class NewsGroupArticle extends BasicArticle
/*    */ {
/*    */   public NewsGroupArticle(File file)
/*    */   {
/* 19 */     parse(file);
/*    */   }
/*    */ 
/*    */   public boolean parse(File file)
/*    */   {
/*    */     try
/*    */     {
/* 29 */       BufferedReader br = FileUtil.getTextReader(file);
/*    */       String line;
/* 30 */       while ((line = br.readLine()) != null)
/*    */       {
/* 31 */         if (line.length() != 0) {
/* 32 */           int start = line.indexOf(':');
/* 33 */           if (start < 0) {
/*    */             break;
/*    */           }
/* 36 */           String header = line.substring(0, start);
/* 37 */           if (header.equalsIgnoreCase("subject"))
/* 38 */             this.title = line.substring(start + 1);
/* 39 */           else if (header.equalsIgnoreCase("summary"))
/* 40 */             this.abt = line.substring(start + 1);
/* 41 */           else if (header.equalsIgnoreCase("keywords")) {
/* 42 */             this.meta = line.substring(start + 1);
/*    */           }
/*    */         }
/*    */       }
/* 46 */       StringBuffer sb = new StringBuffer();
/* 47 */       int end = 0;
/* 48 */       if (line != null) {
/* 49 */         sb.append(line.trim());
/* 50 */         line = br.readLine();
/*    */       }
/* 52 */       while (line != null) {
/* 53 */         line = line.trim();
/* 54 */         if (line.length() > 0) {
/* 55 */           sb.append(' ');
/* 56 */           sb.append(line);
/*    */         }
/*    */         else {
/* 59 */           end = sb.length();
/*    */         }
/* 61 */         line = br.readLine();
/*    */       }
/* 63 */       if (sb.length() - end < 300)
/* 64 */         this.body = sb.substring(0, end);
/*    */       else
/* 66 */         this.body = sb.toString();
/* 67 */       return true;
/*    */     }
/*    */     catch (Exception e) {
/* 70 */       e.printStackTrace();
/* 71 */     }return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.dm.NewsGroupArticle
 * JD-Core Version:    0.6.2
 */