/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.extract.DualConceptExtractor;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class DualIndexer
/*     */   implements Indexer
/*     */ {
/*     */   private PrintWriter log;
/*     */   protected BasicIndexWriteController firstWriter;
/*     */   protected BasicIndexWriteController secondWriter;
/*     */   protected boolean relationSupported;
/*     */   protected boolean initialized;
/*     */   protected DualConceptExtractor extractor;
/*     */   protected boolean firstUseConcept;
/*     */   protected boolean secondUseConcept;
/*     */ 
/*     */   public DualIndexer(DualConceptExtractor extractor, String firstIndexFolder, String secondIndexFolder)
/*     */   {
/*  26 */     this(extractor, false, firstIndexFolder, false, secondIndexFolder);
/*     */   }
/*     */ 
/*     */   public DualIndexer(DualConceptExtractor extractor, boolean useConcept, String firstIndexFolder, String secondIndexFolder) {
/*  30 */     this(extractor, useConcept, firstIndexFolder, useConcept, secondIndexFolder);
/*     */   }
/*     */ 
/*     */   public DualIndexer(DualConceptExtractor extractor, boolean firstUseConcept, String firstIndexFolder, boolean secondUseConcept, String secondIndexFolder)
/*     */   {
/*  35 */     this.extractor = extractor;
/*  36 */     this.firstUseConcept = firstUseConcept;
/*  37 */     this.relationSupported = false;
/*  38 */     this.firstWriter = new BasicIndexWriteController(firstIndexFolder, this.relationSupported, firstUseConcept);
/*  39 */     this.secondWriter = new BasicIndexWriteController(secondIndexFolder, this.relationSupported, secondUseConcept);
/*  40 */     this.log = null;
/*  41 */     this.initialized = false;
/*     */   }
/*     */ 
/*     */   public void setLog(String logFile) {
/*  45 */     this.log = FileUtil.getPrintWriter(logFile);
/*     */   }
/*     */ 
/*     */   public void initialize() {
/*  49 */     if (this.initialized) return;
/*     */ 
/*  51 */     this.firstWriter.addSection(new IRSection(0));
/*  52 */     this.secondWriter.addSection(new IRSection(0));
/*  53 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   public void close() {
/*  57 */     this.initialized = false;
/*  58 */     this.firstWriter.close();
/*  59 */     this.secondWriter.close();
/*     */   }
/*     */ 
/*     */   public synchronized boolean index(Article article)
/*     */   {
/*     */     try
/*     */     {
/*  66 */       if (!this.initialized) return false;
/*     */ 
/*  68 */       writeLog(new Date().toString());
/*  69 */       writeLog("Indexing article #" + article.getKey() + ": ");
/*     */ 
/*  71 */       this.extractor.initDocExtraction();
/*  72 */       boolean ret = (this.firstWriter.indexed(article.getKey())) || (this.secondWriter.indexed(article.getKey()));
/*  73 */       if (ret) {
/*  74 */         writeLog("indexed\n");
/*  75 */         return false;
/*     */       }
/*     */ 
/*  78 */       ret = this.extractor.extractFromDoc(article);
/*  79 */       if (ret) {
/*  80 */         this.firstWriter.setDoc(article.getKey());
/*  81 */         this.firstWriter.write(0, this.extractor.getFirstConceptList());
/*  82 */         this.secondWriter.setDoc(article.getKey());
/*  83 */         this.secondWriter.write(0, this.extractor.getSecondConceptList());
/*  84 */         writeLog("succeeded\r\n");
/*  85 */         return true;
/*     */       }
/*     */ 
/*  88 */       writeLog("failed\r\n");
/*  89 */       return false;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  93 */       e.printStackTrace();
/*  94 */       writeLog("failed\r\n");
/*  95 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean indexed(String docKey)
/*     */   {
/* 100 */     return this.firstWriter.indexed(docKey);
/*     */   }
/*     */ 
/*     */   protected void writeLog(String content) {
/* 104 */     if (this.log != null) {
/* 105 */       this.log.write(content);
/* 106 */       this.log.flush();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.DualIndexer
 * JD-Core Version:    0.6.2
 */