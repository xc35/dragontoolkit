/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*    */ import edu.drexel.cis.dragon.ir.query.QueryGenerator;
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class QueryAppConfig
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 24 */     if (args.length != 2) {
/* 25 */       System.out.println("Please input two parameters: configuration xml file and indexing applicaiton id");
/* 26 */       return;
/*    */     }
/*    */ 
/* 29 */     ConfigureNode root = new BasicConfigureNode(args[0]);
/* 30 */     ConfigUtil util = new ConfigUtil();
/* 31 */     ConfigureNode indexAppNode = util.getConfigureNode(root, "queryapp", Integer.parseInt(args[1]));
/* 32 */     if (indexAppNode == null)
/* 33 */       return;
/* 34 */     QueryAppConfig queryApp = new QueryAppConfig();
/* 35 */     queryApp.generateQuery(indexAppNode);
/*    */   }
/*    */ 
/*    */   public void generateQuery(ConfigureNode indexAppNode)
/*    */   {
/* 44 */     int collectionID = indexAppNode.getInt("topicreader");
/* 45 */     int queryGeneratorID = indexAppNode.getInt("querygenerator");
/* 46 */     CollectionReader topicReader = new CollectionReaderConfig().getCollectionReader(indexAppNode, collectionID);
/* 47 */     QueryGenerator queryGenerator = new QueryGeneratorConfig().getQueryGenerator(indexAppNode, queryGeneratorID);
/* 48 */     String queryFile = indexAppNode.getString("queryfile");
/* 49 */     generateQuery(queryGenerator, topicReader, queryFile);
/*    */   }
/*    */ 
/*    */   public void generateQuery(QueryGenerator queryGenerator, CollectionReader topicReader, String queryFile)
/*    */   {
/*    */     try
/*    */     {
/* 60 */       ArrayList topics = new ArrayList();
/*    */       Article article;
/* 61 */       while ((article = topicReader.getNextArticle()) != null)
/*    */       {
/* 62 */         topics.add(article);
/* 63 */       }PrintWriter out = FileUtil.getPrintWriter(queryFile);
/* 64 */       out.write(topics.size() + "\n");
/*    */ 
/* 66 */       for (int i = 0; i < topics.size(); i++) {
/* 67 */         article = (Article)topics.get(i);
/* 68 */         String curQuery = queryGenerator.generate(article).toString();
/* 69 */         out.write(article.getCategory() + "\t" + curQuery + "\n");
/* 70 */         out.flush();
/*    */       }
/* 72 */       out.close();
/*    */     }
/*    */     catch (Exception e) {
/* 75 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.QueryAppConfig
 * JD-Core Version:    0.6.2
 */