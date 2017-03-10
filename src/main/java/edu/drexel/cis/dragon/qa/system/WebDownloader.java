/*     */ package edu.drexel.cis.dragon.qa.system;
/*     */ 
/*     */ import edu.drexel.cis.dragon.onlinedb.ArrayCollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.ArticleQuery;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicCollectionWriter;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.searchengine.AbstractSearchEngine;
/*     */ import edu.drexel.cis.dragon.qa.query.QueryGenerator;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class WebDownloader
/*     */ {
/*     */   private QueryGenerator generator;
/*     */   private ArticleQuery searcher;
/*     */   private int maxDocNum;
/*     */   private int minDocNum;
/*     */   private int delay;
/*     */ 
/*     */   public WebDownloader(QueryGenerator generator, ArticleQuery searcher, int maxDocNum, int minDocNum)
/*     */   {
/*  21 */     this.generator = generator;
/*  22 */     this.searcher = searcher;
/*  23 */     this.maxDocNum = maxDocNum;
/*  24 */     this.minDocNum = minDocNum;
/*  25 */     this.delay = 30;
/*     */   }
/*     */ 
/*     */   public void setDelay(int seconds) {
/*  29 */     this.delay = seconds;
/*     */   }
/*     */ 
/*     */   public int getDelay() {
/*  33 */     return this.delay;
/*     */   }
/*     */   public void setMaxDocumentNum(int max) {
/*  36 */     this.maxDocNum = max;
/*     */   }
/*     */ 
/*     */   public int getMaxDocumentNum() {
/*  40 */     return this.maxDocNum;
/*     */   }
/*     */ 
/*     */   public void setMinDocumentNum(int min) {
/*  44 */     this.minDocNum = min;
/*     */   }
/*     */ 
/*     */   public int getMinDocumentNum() {
/*  48 */     return this.minDocNum;
/*     */   }
/*     */ 
/*     */   public void download(String collectionFolder, String questionFile, boolean groupQuery)
/*     */   {
/*     */     try
/*     */     {
/*  59 */       new File(collectionFolder).mkdirs();
/*  60 */       BufferedReader br = FileUtil.getTextReader(questionFile);
/*     */       String line;
/*  61 */       while ((line = br.readLine()) != null)
/*     */       {
/*  62 */         String qno = line.substring(0, line.indexOf('\t'));
/*  63 */         String question = line.substring(line.indexOf('\t') + 1);
/*  64 */         QuestionQuery query = this.generator.generate(question);
/*     */         String webQuery;
/*  65 */         if (groupQuery)
/*  66 */           webQuery = query.getGroupQuery();
/*     */         else
/*  68 */           webQuery = query.getWordQuery();
/*  69 */         System.out.println("Processing Question #" + qno + " " + webQuery);
/*  70 */         ArrayCollectionReader reader = webSearch(webQuery);
/*  71 */         if (groupQuery) {
/*  72 */           int i = 1;
/*  73 */           while ((i < query.getQueryNum()) && (reader.size() < this.minDocNum)) {
/*  74 */             webQuery = query.getQuery(i++);
/*  75 */             System.out.println("Processing Question #" + qno + " " + webQuery);
/*  76 */             reader = webSearch(reader, webQuery);
/*     */           }
/*     */         }
/*  79 */         saveCollection(collectionFolder, qno, reader);
/*  80 */         AbstractSearchEngine.sleepManySeconds(this.delay);
/*     */       }
/*  82 */       br.close();
/*     */     }
/*     */     catch (Exception e) {
/*  85 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public ArrayCollectionReader webSearch(String query) {
/*  90 */     return webSearch(null, query);
/*     */   }
/*     */ 
/*     */   public ArrayCollectionReader webSearch(ArrayCollectionReader reader, String query)
/*     */   {
/*  96 */     if (reader == null)
/*  97 */       reader = new ArrayCollectionReader();
/*  98 */     this.searcher.setSearchTerm(query);
/*  99 */     this.searcher.initQuery();
/*     */     Article curArticle;
/* 101 */     while ((reader.size() < this.maxDocNum) && ((curArticle = this.searcher.getNextArticle()) != null))
/*     */     {
/* 102 */       if (curArticle.getBody() != null)
/*     */       {
/* 104 */         curArticle.setKey(curArticle.getKey().toLowerCase());
/* 105 */         reader.addArticle(curArticle);
/*     */       }
/*     */     }
/* 107 */     return reader;
/*     */   }
/*     */ 
/*     */   public void saveCollection(String path, String collectionKey, CollectionReader reader)
/*     */   {
/* 114 */     BasicCollectionWriter writer = new BasicCollectionWriter(path + "/" + collectionKey + ".collection", path + "/" + collectionKey + ".index", false);
/*     */     Article article;
/* 115 */     while ((article = reader.getNextArticle()) != null)
/*     */     {
/* 116 */       writer.add(article);
/* 117 */     }writer.close();
/* 118 */     reader.restart();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.system.WebDownloader
 * JD-Core Version:    0.6.2
 */