/*     */ package edu.drexel.cis.dragon.onlinedb.trec;
/*     */ 
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.ArticleParser;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicArticleIndex;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicArticleKey;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import java.io.File;
/*     */ import java.io.RandomAccessFile;
/*     */ 
/*     */ public class TrecCollectionReader
/*     */   implements CollectionReader
/*     */ {
/*     */   private CollectionReader curCollection;
/*     */   private BasicArticleIndex articleIndex;
/*     */   private RandomAccessFile curCacheFile;
/*     */   private ArticleParser parser;
/*     */   private File[] arrFile;
/*     */   private int curCacheFileIndex;
/*     */   private String filename;
/*     */   private String collectionPath;
/*     */   private String indexFile;
/*     */   private String articleRoot;
/*     */   private byte[] buf;
/*     */   private int curFileIndex;
/*     */   private int totalFileIndex;
/*     */ 
/*     */   public TrecCollectionReader(ArticleParser parser)
/*     */   {
/*  30 */     this(parser, "DOC");
/*     */   }
/*     */ 
/*     */   public TrecCollectionReader(ArticleParser parser, String articleRoot) {
/*  34 */     this.parser = parser;
/*  35 */     this.articleRoot = articleRoot;
/*  36 */     this.curCollection = null;
/*  37 */     this.totalFileIndex = 0;
/*  38 */     this.curFileIndex = 0;
/*  39 */     this.arrFile = null;
/*  40 */     this.curCacheFileIndex = -1;
/*  41 */     this.curCacheFile = null;
/*     */   }
/*     */ 
/*     */   private TrecCollectionReader(File folder, ArticleParser parser, String articleRoot) {
/*  45 */     this.articleRoot = articleRoot;
/*  46 */     this.parser = parser;
/*  47 */     this.curCacheFileIndex = -1;
/*  48 */     this.curCacheFile = null;
/*  49 */     loadCollection(folder, null);
/*     */   }
/*     */ 
/*     */   public TrecCollectionReader(String collectionPath, ArticleParser parser) {
/*  53 */     this(new File(collectionPath), parser, "DOC");
/*     */   }
/*     */ 
/*     */   public TrecCollectionReader(String collectionPath, ArticleParser parser, String articleRoot) {
/*  57 */     this(new File(collectionPath), parser, articleRoot);
/*     */   }
/*     */ 
/*     */   public TrecCollectionReader(String collectionPath, String indexFile, ArticleParser parser) {
/*  61 */     this(collectionPath, indexFile, parser, "DOC");
/*     */   }
/*     */ 
/*     */   public TrecCollectionReader(String collectionPath, String indexFile, ArticleParser parser, String articleRoot) {
/*  65 */     this.parser = parser;
/*  66 */     this.articleRoot = articleRoot;
/*  67 */     this.curCacheFileIndex = -1;
/*  68 */     this.curCacheFile = null;
/*  69 */     loadCollection(new File(collectionPath), indexFile);
/*     */   }
/*     */ 
/*     */   public boolean loadCollection(String collectionPath) {
/*  73 */     return loadCollection(collectionPath, null);
/*     */   }
/*     */ 
/*     */   public boolean loadCollection(String collectionPath, String indexFile) {
/*  77 */     return loadCollection(new File(collectionPath), indexFile);
/*     */   }
/*     */ 
/*     */   private boolean loadCollection(File folder, String indexFile) {
/*  81 */     close();
/*  82 */     this.indexFile = indexFile;
/*  83 */     if (indexFile == null)
/*  84 */       this.articleIndex = null;
/*  85 */     else if ((!new File(indexFile).exists()) || (new File(indexFile).length() <= 18L))
/*  86 */       this.articleIndex = new BasicArticleIndex(indexFile, true);
/*     */     else {
/*  88 */       this.articleIndex = new BasicArticleIndex(indexFile, false);
/*     */     }
/*  90 */     if (!folder.isDirectory()) {
/*  91 */       this.arrFile = new File[1];
/*  92 */       this.arrFile[0] = folder;
/*  93 */       this.totalFileIndex = 1;
/*  94 */       this.filename = null;
/*     */     }
/*     */     else {
/*  97 */       this.filename = folder.getName();
/*  98 */       this.arrFile = folder.listFiles();
/*  99 */       this.totalFileIndex = this.arrFile.length;
/*     */     }
/* 101 */     this.collectionPath = folder.getPath();
/* 102 */     this.curFileIndex = 0;
/* 103 */     if (this.arrFile[0].isDirectory())
/* 104 */       this.curCollection = new TrecCollectionReader(this.arrFile[0], this.parser, this.articleRoot);
/*     */     else
/* 106 */       this.curCollection = new TrecFileReader(this.arrFile[0], this.parser, this.articleRoot);
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */   public ArticleParser getArticleParser() {
/* 111 */     return this.parser;
/*     */   }
/*     */ 
/*     */   public void setArticleParser(ArticleParser parser) {
/* 115 */     this.parser = parser;
/* 116 */     if (this.curCollection != null)
/* 117 */       this.curCollection.setArticleParser(parser);
/*     */   }
/*     */ 
/*     */   public Article getArticleByKey(String key)
/*     */   {
/*     */     try
/*     */     {
/* 125 */       if ((this.articleIndex == null) || (this.articleIndex.isWritingMode()))
/* 126 */         return null;
/* 127 */       BasicArticleKey articleKey = this.articleIndex.search(key);
/* 128 */       if (articleKey == null)
/* 129 */         return null;
/* 130 */       if (articleKey.getFileIndex() != this.curCacheFileIndex) {
/* 131 */         if (this.curCacheFile != null)
/* 132 */           this.curCacheFile.close();
/* 133 */         this.curCacheFileIndex = articleKey.getFileIndex();
/* 134 */         String curFilename = this.articleIndex.getFilename(this.curCacheFileIndex);
/* 135 */         if (curFilename != null)
/* 136 */           curFilename = this.collectionPath + "/" + curFilename;
/*     */         else
/* 138 */           curFilename = this.collectionPath;
/* 139 */         this.curCacheFile = new RandomAccessFile(curFilename, "r");
/*     */       }
/* 141 */       this.curCacheFile.seek(articleKey.getOffset());
/* 142 */       if ((this.buf == null) || (this.buf.length < articleKey.getLength()))
/* 143 */         this.buf = new byte[articleKey.getLength() + 10240];
/* 144 */       this.curCacheFile.read(this.buf, 0, articleKey.getLength());
/* 145 */       return this.parser.parse(new String(this.buf, 0, articleKey.getLength()));
/*     */     }
/*     */     catch (Exception e) {
/* 148 */       e.printStackTrace();
/* 149 */     }return null;
/*     */   }
/*     */ 
/*     */   public Article getNextArticle()
/*     */   {
/*     */     try
/*     */     {
/* 158 */       if ((this.curCollection == null) && (this.parser == null)) {
/* 159 */         return null;
/*     */       }
/* 161 */       Article cur = this.curCollection.getNextArticle();
/* 162 */       while ((cur == null) && (this.curFileIndex < this.totalFileIndex - 1)) {
/* 163 */         this.curCollection.close();
/* 164 */         this.curFileIndex += 1;
/* 165 */         if (this.arrFile[this.curFileIndex].isDirectory())
/* 166 */           this.curCollection = new TrecCollectionReader(this.arrFile[this.curFileIndex], this.parser, this.articleRoot);
/*     */         else
/* 168 */           this.curCollection = new TrecFileReader(this.arrFile[this.curFileIndex], this.parser, this.articleRoot);
/* 169 */         cur = this.curCollection.getNextArticle();
/*     */       }
/*     */ 
/* 172 */       if ((cur != null) && (this.articleIndex != null) && (this.articleIndex.isWritingMode()))
/*     */       {
/* 174 */         String curArticleFilename = getArticleFilename();
/* 175 */         if (curArticleFilename.indexOf("/") > 0) {
/* 176 */           curArticleFilename = curArticleFilename.substring(curArticleFilename.indexOf("/") + 1);
/* 177 */           this.articleIndex.add(cur.getKey(), curArticleFilename, getArticleOffset(), getArticleLength());
/*     */         }
/*     */         else {
/* 180 */           this.articleIndex.add(cur.getKey(), getArticleOffset());
/*     */         }
/*     */       }
/* 183 */       return cur;
/*     */     }
/*     */     catch (Exception e) {
/* 186 */       e.printStackTrace();
/* 187 */     }return null;
/*     */   }
/*     */ 
/*     */   public long getArticleOffset()
/*     */   {
/* 192 */     if (this.curCollection == null)
/* 193 */       return -1L;
/* 194 */     if (this.curCollection.getClass().getName().equals("dragon.onlinedb.trec.TrecFileReader")) {
/* 195 */       return ((TrecFileReader)this.curCollection).getArticleOffset();
/*     */     }
/*     */ 
/* 198 */     return ((TrecCollectionReader)this.curCollection).getArticleOffset();
/*     */   }
/*     */ 
/*     */   public int getArticleLength()
/*     */   {
/* 203 */     if (this.curCollection == null)
/* 204 */       return -1;
/* 205 */     if (this.curCollection.getClass().getName().equals("dragon.onlinedb.trec.TrecFileReader")) {
/* 206 */       return ((TrecFileReader)this.curCollection).getArticleLength();
/*     */     }
/*     */ 
/* 209 */     return ((TrecCollectionReader)this.curCollection).getArticleLength();
/*     */   }
/*     */ 
/*     */   public String getArticleFilename()
/*     */   {
/* 216 */     if (this.curCollection == null)
/* 217 */       return null;
/*     */     String name;
/* 219 */     if (this.curCollection.getClass().getName().equals("dragon.onlinedb.trec.TrecFileReader"))
/* 220 */       name = ((TrecFileReader)this.curCollection).getArticleFilename();
/*     */     else
/* 222 */       name = ((TrecCollectionReader)this.curCollection).getArticleFilename();
/* 223 */     if (this.filename == null) {
/* 224 */       return name;
/*     */     }
/* 226 */     return this.filename + "/" + name;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*     */     try {
/* 232 */       if (this.curCollection != null)
/* 233 */         this.curCollection.close();
/* 234 */       if (this.articleIndex != null)
/* 235 */         this.articleIndex.close();
/* 236 */       if (this.curCacheFileIndex >= 0) {
/* 237 */         this.curCacheFileIndex = -1;
/* 238 */         this.curCacheFile.close();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 242 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean supportArticleKeyRetrieval() {
/* 247 */     return (this.articleIndex != null) && (!this.articleIndex.isWritingMode());
/*     */   }
/*     */ 
/*     */   public void restart() {
/* 251 */     loadCollection(this.collectionPath, this.indexFile);
/*     */   }
/*     */ 
/*     */   public int size() {
/* 255 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.trec.TrecCollectionReader
 * JD-Core Version:    0.6.2
 */