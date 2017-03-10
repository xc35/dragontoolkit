/*     */ package edu.drexel.cis.dragon.onlinedb;
/*     */ 
/*     */ import edu.drexel.cis.dragon.onlinedb.amazon.AmazonReviewQuery;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class BasicCollectionPreparer
/*     */   implements CollectionPreparer
/*     */ {
/*     */   private CollectionWriter collectionWriter;
/*     */   private ArticleQuery query;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  25 */     CollectionWriter writer = new BasicCollectionWriter("indexreview/dc/dc.collection", "indexreview/dc/dc.index", false);
/*  26 */     ArticleQuery query = new AmazonReviewQuery();
/*  27 */     BasicCollectionPreparer preparer = new BasicCollectionPreparer(writer, query);
/*  28 */     preparer.addArticles("indexreview/dc/dc.query");
/*  29 */     preparer.close();
/*     */   }
/*     */ 
/*     */   public BasicCollectionPreparer(CollectionWriter writer, ArticleQuery query) {
/*  33 */     this.collectionWriter = writer;
/*  34 */     this.query = query;
/*     */   }
/*     */ 
/*     */   public boolean addListedArticles(String articleKeyFile)
/*     */   {
/*     */     try
/*     */     {
/*  43 */       if (!this.query.supportArticleKeyRetrieval()) return false;
/*     */ 
/*  45 */       BufferedReader br = FileUtil.getTextReader(articleKeyFile);
/*     */       String line;
/*  46 */       while ((line = br.readLine()) != null)
/*     */       {
/*  47 */         System.out.println(new Date().toString() + " Processing article #" + line);
/*  48 */         Article article = this.query.getArticleByKey(line);
/*  49 */         if (article != null) {
/*  50 */           this.collectionWriter.add(article);
/*     */         }
/*     */       }
/*  53 */       br.close();
/*  54 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/*  57 */       e.printStackTrace();
/*  58 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean addArticles(ArticleQuery query)
/*     */   {
/*     */     try
/*     */     {
/*  66 */       while (query.moveToNextArticle())
/*     */       {
/*  68 */         Article article = query.getArticle();
/*  69 */         if (article != null) {
/*  70 */           System.out.println("Processing article #" + article.getKey());
/*  71 */           this.collectionWriter.add(article);
/*     */         }
/*     */       }
/*  74 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/*  77 */       e.printStackTrace();
/*  78 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean addArticles(String queryFile)
/*     */   {
/*  86 */     String[] arrQuery = loadQuery(queryFile);
/*  87 */     if (arrQuery == null)
/*  88 */       return false;
/*  89 */     return addArticles(arrQuery);
/*     */   }
/*     */ 
/*     */   public boolean addArticles(String[] queries)
/*     */   {
/*     */     try
/*     */     {
/*  96 */       for (int i = 0; i < queries.length; i++) {
/*  97 */         System.out.print(new Date().toString() + " Initializing Query: " + queries[i]);
/*  98 */         this.query.setSearchTerm(queries[i]);
/*  99 */         if (!this.query.initQuery()) {
/* 100 */           System.out.println("  0");
/* 101 */           return false;
/*     */         }
/* 103 */         System.out.println("  " + this.query.size());
/* 104 */         addArticles(this.query);
/*     */       }
/* 106 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 109 */       e.printStackTrace();
/* 110 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean addArticles(ArticleQuery query, int interval)
/*     */   {
/*     */     try
/*     */     {
/* 119 */       int i = -1;
/* 120 */       while (query.moveToNextArticle())
/*     */       {
/* 122 */         i++;
/* 123 */         if (i % interval == 0) {
/* 124 */           Article article = query.getArticle();
/* 125 */           if (article != null) {
/* 126 */             System.out.println("Processing article #" + article.getKey());
/* 127 */             this.collectionWriter.add(article);
/*     */           }
/*     */         }
/*     */       }
/* 131 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 134 */       e.printStackTrace();
/* 135 */     }return false;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 140 */     this.collectionWriter.close();
/*     */   }
/*     */ 
/*     */   private String[] loadQuery(String queryFile)
/*     */   {
/*     */     try
/*     */     {
/* 150 */       BufferedReader br = FileUtil.getTextReader(queryFile);
/* 151 */       int queryNum = Integer.parseInt(br.readLine());
/* 152 */       String[] queries = new String[queryNum];
/* 153 */       for (int i = 0; i < queryNum; i++)
/* 154 */         queries[i] = br.readLine();
/* 155 */       return queries;
/*     */     }
/*     */     catch (Exception ex) {
/* 158 */       ex.printStackTrace();
/* 159 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.BasicCollectionPreparer
 * JD-Core Version:    0.6.2
 */