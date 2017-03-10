/*     */ package edu.drexel.cis.dragon.onlinedb.trec;
/*     */ 
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.ArticleParser;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ 
/*     */ public class TrecFileReader
/*     */   implements CollectionReader
/*     */ {
/*     */   private ArticleParser parser;
/*     */   private BufferedReader reader;
/*     */   private StringBuffer sb;
/*     */   private char[] buf;
/*     */   private String filename;
/*     */   private File colFile;
/*     */   private String articleRoot;
/*     */   private long curArticleOffset;
/*     */   private long deletedBytes;
/*     */   private int curArticleLength;
/*     */   private boolean done;
/*     */ 
/*     */   public TrecFileReader(ArticleParser parser)
/*     */   {
/*  28 */     this(null, parser, "DOC");
/*     */   }
/*     */ 
/*     */   public TrecFileReader(ArticleParser parser, String articleRoot) {
/*  32 */     this(null, parser, articleRoot);
/*     */   }
/*     */ 
/*     */   public TrecFileReader(File colFile, ArticleParser parser) {
/*  36 */     this(colFile, parser, "DOC");
/*     */   }
/*     */ 
/*     */   public TrecFileReader(File colFile, ArticleParser parser, String articleRoot) {
/*  40 */     this.parser = parser;
/*  41 */     this.articleRoot = articleRoot;
/*  42 */     this.buf = new char[10240];
/*  43 */     loadCollection(colFile);
/*     */   }
/*     */ 
/*     */   public boolean loadFile(String colFile) {
/*  47 */     return loadCollection(new File(colFile));
/*     */   }
/*     */ 
/*     */   public boolean loadCollection(File colFile) {
/*     */     try {
/*  52 */       this.deletedBytes = 0L;
/*  53 */       this.curArticleOffset = -1L;
/*  54 */       this.curArticleLength = 0;
/*  55 */       this.colFile = colFile;
/*     */ 
/*  57 */       if ((colFile == null) || (!testCollectionFile(colFile))) {
/*  58 */         this.done = true;
/*  59 */         this.reader = null;
/*  60 */         this.sb = null;
/*  61 */         this.filename = null;
/*  62 */         return false;
/*     */       }
/*     */ 
/*  65 */       this.filename = colFile.getName();
/*  66 */       this.reader = FileUtil.getTextReader(colFile);
/*  67 */       this.done = false;
/*  68 */       this.sb = new StringBuffer(10240);
/*  69 */       return true;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  73 */       e.printStackTrace();
/*  74 */       this.reader = null;
/*  75 */       this.done = true;
/*  76 */     }return false;
/*     */   }
/*     */ 
/*     */   public ArticleParser getArticleParser()
/*     */   {
/*  81 */     return this.parser;
/*     */   }
/*     */ 
/*     */   public void setArticleParser(ArticleParser parser) {
/*  85 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */   public Article getArticleByKey(String key) {
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   public Article getNextArticle()
/*     */   {
/*     */     try
/*     */     {
/*  97 */       if ((this.reader == null) || (this.sb == null)) {
/*  98 */         return null;
/*     */       }
/* 100 */       int end = this.sb.indexOf("</" + this.articleRoot + ">");
/* 101 */       while ((end < 0) && (!this.done)) {
/* 102 */         int len = this.reader.read(this.buf);
/* 103 */         if (len < this.buf.length)
/* 104 */           this.done = true;
/* 105 */         int start = this.sb.length();
/* 106 */         this.sb.append(this.buf, 0, len);
/* 107 */         end = this.sb.indexOf("</" + this.articleRoot + ">", start);
/*     */       }
/* 109 */       if (end < 0) return null;
/*     */ 
/* 111 */       end = end + 3 + this.articleRoot.length();
/* 112 */       int start = this.sb.lastIndexOf("<" + this.articleRoot, end);
/* 113 */       if (start < 0) return null;
/*     */ 
/* 115 */       this.curArticleOffset = (this.deletedBytes + start);
/* 116 */       this.curArticleLength = (end - start);
/* 117 */       Article article = this.parser.parse(this.sb.substring(start, end));
/* 118 */       this.sb.delete(0, end);
/* 119 */       this.deletedBytes += end;
/* 120 */       return article;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 124 */       e.printStackTrace();
/* 125 */     }return null;
/*     */   }
/*     */ 
/*     */   public long getArticleOffset()
/*     */   {
/* 130 */     return this.curArticleOffset;
/*     */   }
/*     */ 
/*     */   public int getArticleLength() {
/* 134 */     return this.curArticleLength;
/*     */   }
/*     */ 
/*     */   public String getArticleFilename() {
/* 138 */     return this.filename;
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */     try {
/* 143 */       if (this.reader != null)
/* 144 */         this.reader.close();
/*     */     }
/*     */     catch (Exception e) {
/* 147 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean testCollectionFile(File file)
/*     */   {
/*     */     try
/*     */     {
/* 156 */       if ((!file.exists()) || (file.isDirectory()))
/* 157 */         return false;
/* 158 */       BufferedReader br = FileUtil.getTextReader(file);
/* 159 */       boolean ret = br.readLine().trim().equalsIgnoreCase("<DOC>");
/* 160 */       br.close();
/* 161 */       return ret;
/*     */     } catch (Exception e) {
/*     */     }
/* 164 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean supportArticleKeyRetrieval()
/*     */   {
/* 169 */     return false;
/*     */   }
/*     */ 
/*     */   public void restart() {
/* 173 */     loadCollection(this.colFile);
/*     */   }
/*     */ 
/*     */   public int size() {
/* 177 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.trec.TrecFileReader
 * JD-Core Version:    0.6.2
 */