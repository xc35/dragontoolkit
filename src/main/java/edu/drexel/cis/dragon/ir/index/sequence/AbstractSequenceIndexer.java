/*     */ package edu.drexel.cis.dragon.ir.index.sequence;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.Indexer;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public abstract class AbstractSequenceIndexer
/*     */   implements Indexer
/*     */ {
/*     */   private ConceptExtractor ce;
/*     */   private PrintWriter log;
/*     */   protected AbstractSequenceIndexWriter writer;
/*     */   protected boolean initialized;
/*     */   private boolean useTitle;
/*     */   private boolean useAbstract;
/*     */   private boolean useBody;
/*     */   private boolean useMeta;
/*     */ 
/*     */   public AbstractSequenceIndexer(ConceptExtractor ce)
/*     */   {
/*  28 */     this.ce = ce;
/*  29 */     this.useTitle = true;
/*  30 */     this.useAbstract = true;
/*  31 */     this.useMeta = false;
/*  32 */     this.useBody = true;
/*     */   }
/*     */ 
/*     */   public void setSectionIndexOption(boolean title, boolean abt, boolean body, boolean meta) {
/*  36 */     this.useMeta = meta;
/*  37 */     this.useTitle = title;
/*  38 */     this.useAbstract = abt;
/*  39 */     this.useBody = body;
/*     */   }
/*     */ 
/*     */   public void initialize() {
/*  43 */     if (this.initialized)
/*  44 */       return;
/*  45 */     this.writer.initialize();
/*  46 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  51 */     if (this.writer != null)
/*  52 */       this.writer.close();
/*  53 */     if (this.log != null)
/*  54 */       this.log.close();
/*  55 */     this.initialized = false;
/*     */   }
/*     */ 
/*     */   public boolean index(Article article)
/*     */   {
/*     */     try
/*     */     {
/*  67 */       StringBuffer sb = new StringBuffer();
/*  68 */       if ((this.useTitle) && (article.getTitle() != null)) {
/*  69 */         if (sb.length() > 0)
/*  70 */           sb.append("\n\n");
/*  71 */         sb.append(article.getTitle());
/*     */       }
/*  73 */       if ((this.useAbstract) && (article.getAbstract() != null)) {
/*  74 */         if (sb.length() > 0)
/*  75 */           sb.append("\n\n");
/*  76 */         sb.append(article.getAbstract());
/*     */       }
/*  78 */       if ((this.useBody) && (article.getBody() != null)) {
/*  79 */         if (sb.length() > 0)
/*  80 */           sb.append("\n\n");
/*  81 */         sb.append(article.getBody());
/*     */       }
/*  83 */       if ((this.useMeta) && (article.getMeta() != null)) {
/*  84 */         if (sb.length() > 0)
/*  85 */           sb.append("\n\n");
/*  86 */         sb.append(article.getMeta());
/*     */       }
/*  88 */       if (sb.length() <= 0) {
/*  89 */         writeLog(new Date().toString() + " Indexing article #" + article.getKey() + ": no content\n");
/*  90 */         return false;
/*     */       }
/*     */ 
/*  93 */       IRDoc curDoc = new IRDoc(article.getKey());
/*  94 */       ArrayList list = this.ce.extractFromDoc(sb.toString());
/*  95 */       IRTerm[] arrTerm = new IRTerm[list.size()];
/*  96 */       for (int i = 0; i < list.size(); i++) {
/*  97 */         arrTerm[i] = new IRTerm(((Token)list.get(i)).getName(), -1, 1);
/*     */       }
/*  99 */       boolean result = this.writer.write(curDoc, arrTerm);
/* 100 */       if (result)
/* 101 */         writeLog(new Date().toString() + " Indexing article #" + article.getKey() + ": successful\n");
/*     */       else
/* 103 */         writeLog(new Date().toString() + " Indexing article #" + article.getKey() + ": failed\n");
/* 104 */       return result;
/*     */     }
/*     */     catch (Exception e) {
/* 107 */       e.printStackTrace();
/* 108 */       writeLog(new Date().toString() + " Indexing article #" + article.getKey() + ": failed\n");
/* 109 */     }return false;
/*     */   }
/*     */ 
/*     */   public void setLog(String logFile)
/*     */   {
/* 114 */     this.log = FileUtil.getPrintWriter(logFile);
/*     */   }
/*     */ 
/*     */   public boolean indexed(String docKey) {
/* 118 */     return this.writer.indexed(docKey);
/*     */   }
/*     */ 
/*     */   protected void writeLog(String content) {
/* 122 */     if (this.log != null) {
/* 123 */       this.log.write(content);
/* 124 */       this.log.flush();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.AbstractSequenceIndexer
 * JD-Core Version:    0.6.2
 */