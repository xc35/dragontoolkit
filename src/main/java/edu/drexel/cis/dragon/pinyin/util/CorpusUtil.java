/*    */ package edu.drexel.cis.dragon.pinyin.util;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicCollectionReader;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicCollectionWriter;
/*    */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*    */ import edu.drexel.cis.dragon.onlinedb.CollectionWriter;
/*    */ import edu.drexel.cis.dragon.util.EnvVariable;
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import edu.drexel.cis.dragon.util.HttpContent;
/*    */ import edu.drexel.cis.dragon.util.HttpUtil;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class CorpusUtil
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 24 */     EnvVariable.setCharSet("GBK");
/* 25 */     CollectionReader in = new BasicCollectionReader("material/bk_china.collection", null);
/* 26 */     CollectionWriter out = new BasicCollectionWriter("material/china.collection", "material/china.index", false);
/* 27 */     filter(in, out, "material/china.log");
/*    */ 
/* 29 */     in = new BasicCollectionReader("material/newchina.collection", null);
/* 30 */     out = new BasicCollectionWriter("material/china.collection", "material/china.index", true);
/* 31 */     merge(out, in);
/*    */   }
/*    */ 
/*    */   public static void merge(CollectionWriter out, CollectionReader newCollection)
/*    */   {
/*    */     try
/*    */     {
/*    */       Article article;
/* 41 */       while ((article = newCollection.getNextArticle()) != null)
/*    */       {
/* 42 */         out.add(article);
/* 43 */       }out.close();
/*    */     }
/*    */     catch (Exception e) {
/* 46 */       e.printStackTrace();
/* 47 */       out.close();
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void filter(CollectionReader in, CollectionWriter out, String logFile)
/*    */   {
/* 55 */     PrintWriter log = FileUtil.getPrintWriter(logFile, false, "gbk");
/*    */     Article article;
/* 56 */     while ((article = in.getNextArticle()) != null)
/*    */     {
/* 57 */       if ((article.getBody() == null) || (ChineseFile.containsUnrecognizedChar(article.getBody()))) {
/* 58 */         log.println(article.getKey());
/*    */       }
/*    */       else
/* 61 */         out.add(article);
/*    */     }
/* 63 */     in.close();
/* 64 */     out.close();
/* 65 */     log.close();
/*    */   }
/*    */ 
/*    */   public static void download(CollectionWriter out, String urlListFile)
/*    */   {
/*    */     try
/*    */     {
/* 78 */       int count = 0;
/* 79 */       BufferedReader br = ChineseFile.getTextReader(urlListFile);
/* 80 */       HttpUtil web = new HttpUtil("www.google.com", 80, "gbk");
/* 81 */       web.setAutoRefresh(true);
/* 82 */       HttpContent extractor = new HttpContent();
/*    */       String url;
/* 84 */       while ((url = br.readLine()) != null)
/*    */       {
/* 85 */         ChineseFile.getScreen().println(new Date() + " processing url #" + ++count + " " + url);
/*    */         try
/*    */         {
/* 88 */           int start = url.indexOf("//");
/* 89 */           if (start < 0)
/* 90 */             start = 0;
/*    */           else
/* 92 */             start += 2;
/* 93 */           int end = url.indexOf("/", start);
/* 94 */           String host = url.substring(start, end);
/*    */ 
/* 97 */           String path = url.substring(end);
/*    */ 
/* 100 */           start = host.indexOf(":");
/*    */           int port;
/* 101 */           if (start > 0) {
/* 102 */             port = Integer.parseInt(host.substring(start + 1));
/* 103 */             host = host.substring(0, start).trim();
/*    */           } else {
/* 105 */             port = 80;
/* 106 */           }web.setHost(host, port, "gbk");
/* 107 */           String content = web.get(path);
/* 108 */           if (content != null) {
/* 109 */             Article article = new BasicArticle();
/* 110 */             article.setKey(url);
/* 111 */             article.setBody(extractor.extractText(content));
/* 112 */             out.add(article);
/*    */           }
/*    */         } catch (Exception e) {
/* 115 */           e.printStackTrace();
/*    */         }
/*    */       }
/* 118 */       out.close();
/* 119 */       br.close();
/*    */     }
/*    */     catch (Exception e) {
/* 122 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.pinyin.util.CorpusUtil
 * JD-Core Version:    0.6.2
 */