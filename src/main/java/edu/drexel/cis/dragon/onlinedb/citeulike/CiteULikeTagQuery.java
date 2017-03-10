/*     */ package edu.drexel.cis.dragon.onlinedb.citeulike;
/*     */ 
/*     */ import edu.drexel.cis.dragon.onlinedb.AbstractQuery;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.ArticleParser;
/*     */ import edu.drexel.cis.dragon.util.HttpUtil;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class CiteULikeTagQuery extends AbstractQuery
/*     */ {
/*     */   protected HttpUtil http;
/*     */   protected String term;
/*     */   protected String[] arrPaper;
/*     */   private ArticleParser parser;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  25 */     CiteULikeTagQuery query = new CiteULikeTagQuery("network");
/*  26 */     query.initQuery();
/*     */ 
/*  28 */     int top = 10;
/*  29 */     for (int i = 0; (i < top) && (query.moveToNextArticle()); i++) {
/*  30 */       Article article = query.getArticle();
/*  31 */       System.out.println(query.getArticle().getKey() + " " + article.getTitle());
/*     */     }
/*     */   }
/*     */ 
/*     */   public CiteULikeTagQuery() {
/*  36 */     this(null);
/*     */   }
/*     */ 
/*     */   public CiteULikeTagQuery(String term) {
/*  40 */     super(50);
/*  41 */     this.parser = new CiteULikeArticleParser();
/*  42 */     this.arrPaper = new String[this.pageWidth];
/*  43 */     this.term = term;
/*  44 */     this.http = new HttpUtil("www.citeulike.org");
/*     */   }
/*     */ 
/*     */   public boolean supportArticleKeyRetrieval() {
/*  48 */     return true;
/*     */   }
/*     */ 
/*     */   public void setSearchTerm(String term) {
/*  52 */     this.term = term;
/*     */   }
/*     */ 
/*     */   public boolean initQuery() {
/*  56 */     this.curPageNo = -1;
/*  57 */     this.curArticle = null;
/*  58 */     this.curPageWidth = 0;
/*  59 */     this.pageNum = 1;
/*  60 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean moveToPage(int pageNo)
/*     */   {
/*  67 */     if ((pageNo >= this.pageNum) || (this.pageNum == 0))
/*  68 */       return false;
/*  69 */     if (pageNo == this.curPageNo) return true;
/*     */ 
/*  71 */     String curUrl = "/search/all?f=tag&q=" + this.term;
/*  72 */     if (pageNo > 0)
/*  73 */       curUrl = curUrl + "&page=" + (pageNo + 1);
/*  74 */     String content = this.http.get(curUrl);
/*  75 */     if (content == null)
/*  76 */       return false;
/*  77 */     return processPage(pageNo, content);
/*     */   }
/*     */ 
/*     */   private boolean processPage(int pageNo, String content)
/*     */   {
/*  83 */     int count = 0;
/*  84 */     int start = content.indexOf("class=\"title\"");
/*  85 */     while (start >= 0) {
/*  86 */       start = content.indexOf("article", start);
/*  87 */       if (start < 0)
/*     */         break;
/*  89 */       start += 8;
/*  90 */       int end = content.indexOf('"', start);
/*  91 */       this.arrPaper[count] = content.substring(start, end);
/*  92 */       count++;
/*  93 */       start = content.indexOf("class=\"title\"", end);
/*     */     }
/*  95 */     this.curPageNo = pageNo;
/*  96 */     this.curPageWidth = count;
/*  97 */     this.curArticleNo = 0;
/*  98 */     if (this.curPageWidth == 0)
/*  99 */       return false;
/* 100 */     this.curArticle = getArticleByKey(this.arrPaper[this.curArticleNo]);
/*     */ 
/* 103 */     int end = content.indexOf(">Next<");
/* 104 */     if (end < 0) {
/* 105 */       this.pageNum = (pageNo + 1);
/*     */     } else {
/* 107 */       end = content.lastIndexOf("</a>", end);
/* 108 */       start = content.lastIndexOf('>', end);
/* 109 */       this.pageNum = Integer.parseInt(content.substring(start + 1, end));
/*     */     }
/* 111 */     return true;
/*     */   }
/*     */ 
/*     */   public Article getArticleByKey(String id)
/*     */   {
/*     */     try
/*     */     {
/* 118 */       String curUrl = "/article/" + id;
/* 119 */       String content = this.http.get(curUrl);
/* 120 */       if (content == null)
/* 121 */         return null;
/* 122 */       return this.parser.parse(content);
/*     */     }
/*     */     catch (Exception e) {
/* 125 */       e.printStackTrace();
/* 126 */     }return null;
/*     */   }
/*     */ 
/*     */   protected Article getArticle(int articleNo)
/*     */   {
/* 131 */     return getArticleByKey(this.arrPaper[articleNo]);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.citeulike.CiteULikeTagQuery
 * JD-Core Version:    0.6.2
 */