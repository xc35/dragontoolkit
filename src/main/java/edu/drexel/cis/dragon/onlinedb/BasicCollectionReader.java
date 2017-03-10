/*     */ package edu.drexel.cis.dragon.onlinedb;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.RandomAccessFile;
/*     */ 
/*     */ public class BasicCollectionReader
/*     */   implements CollectionReader
/*     */ {
/*     */   protected ArticleParser parser;
/*     */   protected BufferedReader br;
/*     */   protected BasicArticleIndex indexList;
/*     */   protected RandomAccessFile raf;
/*     */   protected String collectionFile;
/*     */   protected String indexFile;
/*     */ 
/*     */   public BasicCollectionReader()
/*     */   {
/*  23 */     this.parser = new BasicArticleParser();
/*  24 */     this.br = null;
/*     */   }
/*     */ 
/*     */   public BasicCollectionReader(String collectionFile) {
/*  28 */     this(collectionFile, null);
/*     */   }
/*     */ 
/*     */   public BasicCollectionReader(String collectionFile, String indexFile) {
/*  32 */     this.parser = new BasicArticleParser();
/*  33 */     loadCollection(collectionFile, indexFile);
/*     */   }
/*     */ 
/*     */   public BasicCollectionReader(String collectionFile, String indexFile, ArticleParser parser) {
/*  37 */     this.parser = parser;
/*  38 */     loadCollection(collectionFile, indexFile);
/*     */   }
/*     */ 
/*     */   public boolean loadCollection(String collectionFile, String indexFile) {
/*     */     try {
/*  43 */       close();
/*  44 */       this.br = FileUtil.getTextReader(collectionFile);
/*  45 */       if ((indexFile != null) && (new File(indexFile).exists()))
/*  46 */         this.indexList = new BasicArticleIndex(indexFile, false);
/*     */       else
/*  48 */         indexFile = null;
/*  49 */       this.raf = new RandomAccessFile(collectionFile, "r");
/*  50 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/*  53 */       e.printStackTrace();
/*  54 */     }return false;
/*     */   }
/*     */ 
/*     */   public ArticleParser getArticleParser()
/*     */   {
/*  59 */     return this.parser;
/*     */   }
/*     */ 
/*     */   public void setArticleParser(ArticleParser parser) {
/*  63 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */   public Article getArticleByKey(String key)
/*     */   {
/*     */     try
/*     */     {
/*  71 */       BasicArticleKey curKey = this.indexList.search(key);
/*  72 */       if (curKey == null) {
/*  73 */         return null;
/*     */       }
/*  75 */       this.raf.seek(curKey.getOffset());
/*  76 */       String line = this.raf.readLine();
/*  77 */       return this.parser.parse(line);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  81 */       e.printStackTrace();
/*  82 */     }return null;
/*     */   }
/*     */ 
/*     */   public Article getNextArticle()
/*     */   {
/*     */     try
/*     */     {
/*  90 */       if ((this.parser == null) || (this.br == null))
/*  91 */         return null;
/*  92 */       String line = this.br.readLine();
/*  93 */       if ((line == null) || (line.trim().length() == 0)) {
/*  94 */         return null;
/*     */       }
/*  96 */       return this.parser.parse(line);
/*     */     }
/*     */     catch (Exception e) {
/*  99 */       e.printStackTrace();
/* 100 */     }return null;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*     */     try {
/* 106 */       if (this.indexList != null)
/* 107 */         this.indexList.close();
/* 108 */       if (this.br != null)
/* 109 */         this.br.close();
/* 110 */       if (this.raf != null)
/* 111 */         this.raf.close();
/*     */     }
/*     */     catch (Exception e) {
/* 114 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean supportArticleKeyRetrieval() {
/* 119 */     return this.indexList != null;
/*     */   }
/*     */ 
/*     */   public void restart() {
/* 123 */     loadCollection(this.collectionFile, this.indexFile);
/*     */   }
/*     */ 
/*     */   public int size() {
/* 127 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.BasicCollectionReader
 * JD-Core Version:    0.6.2
 */