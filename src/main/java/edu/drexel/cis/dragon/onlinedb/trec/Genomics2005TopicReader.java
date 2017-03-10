/*    */ package edu.drexel.cis.dragon.onlinedb.trec;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import java.io.BufferedReader;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class Genomics2005TopicReader extends AbstractTopicReader
/*    */ {
/*    */   public Genomics2005TopicReader(String topicFile)
/*    */   {
/* 19 */     super(topicFile);
/*    */   }
/*    */ 
/*    */   protected ArrayList loadTopics(String topicFile)
/*    */   {
/*    */     try
/*    */     {
/* 30 */       BufferedReader br = FileUtil.getTextReader(topicFile);
/* 31 */       int total = Integer.parseInt(br.readLine());
/* 32 */       ArrayList query = new ArrayList(total);
/* 33 */       for (int i = 0; i < total; i++) {
/* 34 */         String line = br.readLine();
/* 35 */         String[] arrField = line.split("\t");
/* 36 */         BasicArticle cur = new BasicArticle();
/* 37 */         cur.setKey(arrField[0]);
/* 38 */         cur.setCategory(Integer.parseInt(arrField[0]));
/* 39 */         cur.setTitle(arrField[1]);
/* 40 */         query.add(cur);
/*    */       }
/* 42 */       return query;
/*    */     }
/*    */     catch (Exception e) {
/* 45 */       e.printStackTrace();
/* 46 */     }return null;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.trec.Genomics2005TopicReader
 * JD-Core Version:    0.6.2
 */