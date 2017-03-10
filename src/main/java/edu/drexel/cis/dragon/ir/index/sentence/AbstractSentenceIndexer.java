/*     */ package edu.drexel.cis.dragon.ir.index.sentence;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.Indexer;
/*     */ import edu.drexel.cis.dragon.nlp.Document;
/*     */ import edu.drexel.cis.dragon.nlp.DocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.Paragraph;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Date;
/*     */ 
/*     */ public abstract class AbstractSentenceIndexer
/*     */   implements Indexer
/*     */ {
/*     */   protected boolean initialized;
/*     */   protected DocumentParser parser;
/*     */   private PrintWriter log;
/*     */   private SortedArray docs;
/*     */   private int minSentenceLength;
/*     */   private boolean useTitle;
/*     */   private boolean useAbstract;
/*     */   private boolean useBody;
/*     */ 
/*     */   public AbstractSentenceIndexer(DocumentParser parser)
/*     */   {
/*  28 */     this.parser = parser;
/*  29 */     this.docs = new SortedArray();
/*  30 */     this.initialized = false;
/*  31 */     this.useTitle = true;
/*  32 */     this.useAbstract = true;
/*  33 */     this.useBody = true;
/*  34 */     this.initialized = false;
/*  35 */     this.minSentenceLength = 3;
/*     */   }
/*     */ 
/*     */   public abstract boolean index(Sentence paramSentence, String paramString);
/*     */ 
/*     */   public void setMinSentenceLength(int minLength) {
/*  41 */     if (minLength >= 1)
/*  42 */       this.minSentenceLength = minLength;
/*     */   }
/*     */ 
/*     */   public int getMinSentenceLength() {
/*  46 */     return this.minSentenceLength;
/*     */   }
/*     */ 
/*     */   public boolean screenArticleContent(boolean useTitle, boolean useAbstract, boolean useBody) {
/*  50 */     if (this.initialized)
/*  51 */       return false;
/*  52 */     this.useTitle = useTitle;
/*  53 */     this.useAbstract = useAbstract;
/*  54 */     this.useBody = useBody;
/*  55 */     return true;
/*     */   }
/*     */ 
/*     */   public void setLog(String logFile) {
/*  59 */     this.log = FileUtil.getPrintWriter(logFile);
/*     */   }
/*     */ 
/*     */   public boolean indexed(String docKey) {
/*  63 */     return this.docs.contains(docKey);
/*     */   }
/*     */ 
/*     */   public synchronized boolean index(Article article)
/*     */   {
/*     */     try
/*     */     {
/*  74 */       if (!this.initialized) {
/*  75 */         System.out.println("Please initialize the indexer before indexing!");
/*  76 */         return false;
/*     */       }
/*  78 */       if ((article.getKey() == null) || (this.docs.contains(article.getKey())))
/*  79 */         return false;
/*  80 */       Document doc = getDocument(article);
/*  81 */       if (doc == null) {
/*  82 */         return false;
/*     */       }
/*  84 */       int index = -1;
/*  85 */       this.docs.add(article.getKey());
/*  86 */       Paragraph para = doc.getFirstParagraph();
/*  87 */       while (para != null) {
/*  88 */         Sentence sent = para.getFirstSentence();
/*  89 */         while (sent != null)
/*     */         {
/*  91 */           if (sent.getWordNum() >= this.minSentenceLength) {
/*  92 */             index++;
/*  93 */             String sentKey = getSentenceKey(article.getKey(), index);
/*  94 */             if (index(sent, sentKey))
/*  95 */               writeLog(new Date().toString() + " " + sentKey + " successful");
/*     */             else
/*  97 */               writeLog(new Date().toString() + " " + sentKey + " failed");
/*     */           }
/*  99 */           sent = sent.next;
/*     */         }
/* 101 */         para = para.next;
/*     */       }
/* 103 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 106 */       e.printStackTrace();
/* 107 */     }return false;
/*     */   }
/*     */ 
/*     */   protected Document getDocument(Article article)
/*     */   {
/* 114 */     if ((article.getKey() == null) || (article.getKey().trim().length() == 0)) {
/* 115 */       return null;
/*     */     }
/* 117 */     Document doc = new Document();
/* 118 */     if (this.useTitle)
/* 119 */       doc.addParagraph(this.parser.parseParagraph(article.getTitle()));
/* 120 */     if (this.useAbstract)
/* 121 */       doc.addParagraph(this.parser.parseParagraph(article.getAbstract()));
/* 122 */     if ((this.useBody) && (article.getBody() != null))
/* 123 */       doc.addParagraph(this.parser.parseParagraph(article.getBody()));
/* 124 */     if (doc.getFirstParagraph() != null) {
/* 125 */       return doc;
/*     */     }
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   protected String getSentenceKey(String docKey, int sentIndex) {
/* 131 */     return docKey + "_" + sentIndex;
/*     */   }
/*     */ 
/*     */   protected void writeLog(String content) {
/* 135 */     if (this.log != null) {
/* 136 */       this.log.write(content);
/* 137 */       this.log.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() {
/* 142 */     if (this.log != null)
/* 143 */       this.log.close();
/* 144 */     this.initialized = false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sentence.AbstractSentenceIndexer
 * JD-Core Version:    0.6.2
 */