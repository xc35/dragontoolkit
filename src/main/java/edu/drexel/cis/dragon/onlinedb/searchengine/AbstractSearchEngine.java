/*     */ package edu.drexel.cis.dragon.onlinedb.searchengine;
/*     */ 
/*     */ import edu.drexel.cis.dragon.onlinedb.AbstractQuery;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*     */ import edu.drexel.cis.dragon.util.HttpContent;
/*     */ import edu.drexel.cis.dragon.util.HttpUtil;
/*     */ 
/*     */ public abstract class AbstractSearchEngine extends AbstractQuery
/*     */ {
/*     */   private HttpUtil webServer;
/*     */   protected WebLink[] arrUrl;
/*     */   protected String term;
/*     */   protected HttpContent parser;
/*     */   protected String site;
/*     */   protected String defaultEncoding;
/*     */   private boolean summaryOnly;
/*     */   private boolean removeTag;
/*     */ 
/*     */   public AbstractSearchEngine(int pageWidth)
/*     */   {
/*  26 */     super(pageWidth);
/*  27 */     this.arrUrl = new WebLink[this.pageWidth];
/*  28 */     this.removeTag = true;
/*  29 */     this.parser = new HttpContent();
/*  30 */     this.webServer = new HttpUtil("www.google.com");
/*  31 */     this.webServer.setConnectionTimeout(10000);
/*  32 */     this.webServer.setSocketTimeout(10000);
/*  33 */     this.site = null;
/*  34 */     this.defaultEncoding = null;
/*  35 */     this.summaryOnly = false;
/*     */   }
/*     */ 
/*     */   public void setSiteRestriction(String site) {
/*  39 */     if (site != null)
/*  40 */       site = site.trim();
/*  41 */     this.site = site;
/*     */   }
/*     */ 
/*     */   public String getSiteRestriction() {
/*  45 */     return this.site;
/*     */   }
/*     */ 
/*     */   public void setDefaultEncoding(String encoding) {
/*  49 */     this.defaultEncoding = encoding;
/*     */   }
/*     */ 
/*     */   public String getDefaultEncoding() {
/*  53 */     return this.defaultEncoding;
/*     */   }
/*     */ 
/*     */   public void setAutoRefresh(boolean enable) {
/*  57 */     this.webServer.setAutoRefresh(enable);
/*     */   }
/*     */ 
/*     */   public boolean getAutoRefresh() {
/*  61 */     return this.webServer.getAutoRefresh();
/*     */   }
/*     */ 
/*     */   public void setSummaryOnlyOption(boolean option) {
/*  65 */     this.summaryOnly = option;
/*     */   }
/*     */ 
/*     */   public boolean getSummaryOnlyOption() {
/*  69 */     return this.summaryOnly;
/*     */   }
/*     */ 
/*     */   public void setRemoveTagOption(boolean option) {
/*  73 */     this.removeTag = option;
/*     */   }
/*     */ 
/*     */   public boolean getRemoveTagOption() {
/*  77 */     return this.removeTag;
/*     */   }
/*     */ 
/*     */   public void setSearchTerm(String term) {
/*  81 */     this.term = term;
/*     */   }
/*     */ 
/*     */   public boolean supportArticleKeyRetrieval() {
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   public Article getArticleByKey(String id)
/*     */   {
/*  91 */     WebLink link = new WebLink(id);
/*  92 */     return getArticle(link, true);
/*     */   }
/*     */ 
/*     */   protected Article getArticle(int articleNo) {
/*  96 */     return getArticle(this.arrUrl[articleNo], false);
/*     */   }
/*     */ 
/*     */   protected Article getArticle(WebLink link, boolean useKey)
/*     */   {
/* 104 */     Article article = new BasicArticle();
/* 105 */     article.setKey(link.toString());
/* 106 */     article.setTitle(link.getTitle());
/* 107 */     if ((this.summaryOnly) && (!useKey)) {
/* 108 */       article.setBody(link.getSummary());
/* 109 */       return article;
/*     */     }
/*     */     try
/*     */     {
/* 113 */       if ((!this.webServer.getHost().equalsIgnoreCase(link.getHost())) || (this.webServer.getPort() != link.getPort()))
/* 114 */         this.webServer.setHost(link.getHost(), link.getPort(), this.defaultEncoding);
/* 115 */       String content = this.webServer.get(link.getPath());
/* 116 */       if (content == null)
/* 117 */         return article;
/* 118 */       int start = content.indexOf("<html");
/* 119 */       if (start < 0)
/* 120 */         start = content.indexOf("<HTML");
/* 121 */       if (start < 0)
/* 122 */         return article;
/* 123 */       if (this.removeTag)
/*     */       {
/* 125 */         start = content.indexOf("<title>", start);
/* 126 */         if (start < 0)
/* 127 */           start = content.indexOf("<TITLE>");
/* 128 */         if (start >= 0) {
/* 129 */           start += 7;
/* 130 */           int end = content.indexOf("<", start);
/* 131 */           article.setTitle(content.substring(start, end));
/*     */         }
/*     */         else {
/* 134 */           start = 0;
/*     */         }
/*     */ 
/* 137 */         start = content.indexOf("<body ", start);
/* 138 */         if (start < 0)
/* 139 */           start = content.indexOf("<BODY ");
/* 140 */         if (start > 0)
/* 141 */           content = content.substring(start);
/* 142 */         article.setBody(this.parser.extractText(content));
/*     */       }
/*     */       else {
/* 145 */         article.setBody(content);
/* 146 */       }return article;
/*     */     } catch (Exception e) {
/*     */     }
/* 149 */     return article;
/*     */   }
/*     */ 
/*     */   public static void sleepOneSecond()
/*     */   {
/*     */     try {
/* 155 */       Thread.sleep(1000L);
/*     */     }
/*     */     catch (InterruptedException e) {
/* 158 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void sleepManySeconds(long s) {
/*     */     try {
/* 164 */       Thread.sleep(s * 1000L);
/*     */     }
/*     */     catch (InterruptedException e) {
/* 167 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.searchengine.AbstractSearchEngine
 * JD-Core Version:    0.6.2
 */