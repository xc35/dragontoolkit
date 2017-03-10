/*     */ package edu.drexel.cis.dragon.onlinedb;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.File;
/*     */ 
/*     */ public class SimpleCollectionReader
/*     */   implements CollectionReader
/*     */ {
/*     */   private File[] arrFile;
/*     */   private int curPos;
/*     */   private ArticleParser parser;
/*     */   private String root;
/*     */ 
/*     */   public SimpleCollectionReader(String folder)
/*     */   {
/*  22 */     this(folder, new SimpleArticleParser());
/*     */   }
/*     */ 
/*     */   public SimpleCollectionReader(String folder, ArticleParser parser)
/*     */   {
/*  28 */     this.root = folder;
/*  29 */     File file = new File(folder);
/*  30 */     if (file.isDirectory())
/*  31 */       this.arrFile = file.listFiles();
/*     */     else
/*  33 */       this.arrFile = null;
/*  34 */     this.curPos = 0;
/*  35 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */   public SimpleCollectionReader(ArticleParser parser) {
/*  39 */     this.root = null;
/*  40 */     this.arrFile = null;
/*  41 */     this.parser = parser;
/*  42 */     this.curPos = 0;
/*     */   }
/*     */ 
/*     */   public boolean loadCollection(String collectionPath)
/*     */   {
/*  48 */     this.root = collectionPath;
/*  49 */     File file = new File(collectionPath);
/*  50 */     if (file.isDirectory()) {
/*  51 */       this.arrFile = file.listFiles();
/*  52 */       if (this.parser == null)
/*  53 */         this.parser = new SimpleArticleParser();
/*     */     }
/*     */     else {
/*  56 */       this.arrFile = null;
/*     */     }
/*  58 */     this.curPos = 0;
/*  59 */     return this.arrFile != null;
/*     */   }
/*     */ 
/*     */   public ArticleParser getArticleParser() {
/*  63 */     return this.parser;
/*     */   }
/*     */ 
/*     */   public void setArticleParser(ArticleParser parser) {
/*  67 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */   public Article getNextArticle()
/*     */   {
/*  73 */     if ((this.parser == null) || (this.arrFile == null))
/*  74 */       return null;
/*  75 */     while (this.curPos < this.arrFile.length) {
/*  76 */       if (this.arrFile[this.curPos].isFile()) {
/*  77 */         Article article = this.parser.parse(FileUtil.readTextFile(this.arrFile[this.curPos]));
/*  78 */         if (article.getKey() == null)
/*  79 */           article.setKey(this.arrFile[this.curPos].getName());
/*  80 */         this.curPos += 1;
/*  81 */         return article;
/*     */       }
/*     */ 
/*  84 */       this.curPos += 1;
/*     */     }
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   public Article getArticleByKey(String key)
/*     */   {
/*  93 */     File file = new File(this.root, key);
/*  94 */     if ((file.exists()) && (file.isFile())) {
/*  95 */       Article article = this.parser.parse(FileUtil.readTextFile(file));
/*  96 */       article.setKey(key);
/*  97 */       return article;
/*     */     }
/*     */ 
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   public void close() {
/* 104 */     this.parser = null;
/*     */   }
/*     */ 
/*     */   public boolean supportArticleKeyRetrieval() {
/* 108 */     return true;
/*     */   }
/*     */ 
/*     */   public void restart() {
/* 112 */     this.curPos = 0;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 116 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.SimpleCollectionReader
 * JD-Core Version:    0.6.2
 */