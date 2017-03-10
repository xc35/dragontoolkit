/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.Indexer;
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*    */ import java.io.PrintStream;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class IndexAppConfig
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
/* 31 */     ConfigureNode indexAppNode = util.getConfigureNode(root, "indexapp", Integer.parseInt(args[1]));
/* 32 */     if (indexAppNode == null)
/* 33 */       return;
/* 34 */     IndexAppConfig indexApp = new IndexAppConfig();
/* 35 */     indexApp.indexCollection(indexAppNode);
/*    */   }
/*    */ 
/*    */   public void indexCollection(ConfigureNode indexAppNode)
/*    */   {
/* 48 */     CollectionReaderConfig collectionConfig = new CollectionReaderConfig();
/* 49 */     IndexerConfig indexerConfig = new IndexerConfig();
/* 50 */     boolean useMeta = indexAppNode.getBoolean("usemeta", false);
/* 51 */     int indexerID = indexAppNode.getInt("indexer", -1);
/* 52 */     if (indexerID < 0)
/* 53 */       return;
/* 54 */     Indexer indexer = indexerConfig.getIndexer(indexAppNode, indexerID);
/* 55 */     if (indexer == null)
/* 56 */       return;
/* 57 */     String collectionIDs = indexAppNode.getString("collectionreader", null);
/* 58 */     if (collectionIDs == null)
/* 59 */       return;
/* 60 */     String[] arrCollection = collectionIDs.split(";");
/* 61 */     CollectionReader[] arrCollectionReader = new CollectionReader[arrCollection.length];
/* 62 */     for (int i = 0; i < arrCollection.length; i++) {
/* 63 */       arrCollectionReader[i] = collectionConfig.getCollectionReader(indexAppNode, Integer.parseInt(arrCollection[i]));
/*    */     }
/* 65 */     indexCollection(indexer, arrCollectionReader, useMeta);
/*    */   }
/*    */ 
/*    */   public void indexCollection(Indexer indexer, CollectionReader[] arrCollectionReader, boolean useMeta)
/*    */   {
/*    */     try
/*    */     {
/* 73 */       for (int i = 0; i < arrCollectionReader.length; i++) {
/* 74 */         Article article = arrCollectionReader[i].getNextArticle();
/* 75 */         while (article != null) {
/* 76 */           if (!indexer.indexed(article.getKey())) {
/* 77 */             System.out.print(new Date().toString() + " Indexing article #" + article.getKey() + ": ");
/* 78 */             if (!useMeta)
/* 79 */               article.setMeta(null);
/* 80 */             if (!indexer.index(article)) {
/* 81 */               System.out.println("failed");
/*    */             }
/*    */             else {
/* 84 */               System.out.println("successful");
/*    */             }
/*    */           }
/* 87 */           article = arrCollectionReader[i].getNextArticle();
/*    */         }
/* 89 */         arrCollectionReader[i].close();
/*    */       }
/* 91 */       indexer.close();
/*    */     }
/*    */     catch (Exception e) {
/* 94 */       e.printStackTrace();
/* 95 */       indexer.close();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.IndexAppConfig
 * JD-Core Version:    0.6.2
 */