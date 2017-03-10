/*     */ package edu.drexel.cis.dragon.onlinedb.pubmed;
/*     */ 
/*     */ import edu.drexel.cis.dragon.onlinedb.AbstractQuery;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.util.HttpUtil;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class PubMedQuery extends AbstractQuery
/*     */ {
/*     */   protected HttpUtil http;
/*     */   protected String webEnv;
/*     */   protected String queryKey;
/*     */   protected String database;
/*     */   protected String term;
/*     */   protected String eSearchUrl;
/*     */   protected String eFetchUrl;
/*     */   protected String server;
/*     */   protected String startDate;
/*     */   protected String endDate;
/*     */   protected String[] arrPaper;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  27 */     PubMedQuery query = new PubMedQuery(1);
/*  28 */     String term = "hypertension[TIAB] AND diabetes[TIAB]";
/*  29 */     term = "";
/*  30 */     query.setSearchTerm(term);
/*  31 */     query.setDateRange("1995/01/01", "2003/12/31");
/*  32 */     query.initQuery();
/*     */ 
/*  34 */     int top = 100000;
/*  35 */     for (int i = 0; (i < top) && (query.moveToNextArticle()); i++)
/*  36 */       System.out.println(query.getArticle().getKey());
/*     */   }
/*     */ 
/*     */   public PubMedQuery(String term, int pageWidth)
/*     */   {
/*  41 */     super(pageWidth);
/*  42 */     this.arrPaper = new String[pageWidth];
/*  43 */     if (term != null)
/*  44 */       this.term = term.replace(' ', '+');
/*  45 */     this.eSearchUrl = "/entrez/eutils/esearch.fcgi?";
/*  46 */     this.eFetchUrl = "/entrez/eutils/efetch.fcgi?";
/*  47 */     this.server = "eutils.ncbi.nlm.nih.gov";
/*  48 */     this.http = new HttpUtil(this.server);
/*  49 */     this.startDate = null;
/*  50 */     this.endDate = null;
/*     */   }
/*     */ 
/*     */   public PubMedQuery(int pageWidth) {
/*  54 */     this(null, pageWidth);
/*     */   }
/*     */ 
/*     */   public boolean supportArticleKeyRetrieval() {
/*  58 */     return true;
/*     */   }
/*     */ 
/*     */   public void setSearchTerm(String term) {
/*  62 */     this.term = term.replace(' ', '+');
/*     */   }
/*     */ 
/*     */   public void setDateRange(String start, String end) {
/*  66 */     this.startDate = start;
/*  67 */     this.endDate = end;
/*     */   }
/*     */ 
/*     */   public boolean initQuery()
/*     */   {
/*  75 */     this.curPageNo = -1;
/*  76 */     this.curArticle = null;
/*  77 */     this.curPageWidth = 0;
/*     */     String curUrl;
/*  79 */     if ((this.term == null) || (this.term.length() == 0))
/*  80 */       curUrl = this.eSearchUrl + "usehistory=y&db=pubmed&retmax=1";
/*     */     else
/*  82 */       curUrl = this.eSearchUrl + "usehistory=y&db=pubmed&term=" + this.term + "&retmax=1";
/*  83 */     if ((this.startDate != null) && (this.endDate != null))
/*  84 */       curUrl = curUrl + "&mindate=" + this.startDate + "&maxdate=" + this.endDate;
/*  85 */     String content = this.http.get(curUrl);
/*  86 */     if (content == null) return false;
/*     */ 
/*  89 */     int start = content.indexOf("<Count>") + 7;
/*  90 */     int end = content.indexOf("</Count>", start);
/*  91 */     this.articleNum = Integer.parseInt(content.substring(start, end));
/*  92 */     start = content.indexOf("<QueryKey>", start) + 10;
/*  93 */     end = content.indexOf("</QueryKey>", start);
/*  94 */     this.queryKey = content.substring(start, end);
/*  95 */     start = content.indexOf("<WebEnv>", start) + 8;
/*  96 */     end = content.indexOf("</WebEnv>", start);
/*  97 */     this.webEnv = content.substring(start, end);
/*  98 */     if (this.articleNum == 0)
/*  99 */       this.pageNum = 0;
/*     */     else
/* 101 */       this.pageNum = ((this.articleNum - 1) / this.pageWidth + 1);
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean moveToPage(int pageNo)
/*     */   {
/* 110 */     if ((pageNo >= this.pageNum) || (this.pageNum == 0))
/* 111 */       return false;
/* 112 */     if (pageNo == this.curPageNo) return true;
/*     */ 
/* 114 */     String curUrl = this.eFetchUrl + "db=PubMed&WebEnv=" + this.webEnv + 
/* 115 */       "&retmode=text&query_key=" + this.queryKey;
/* 116 */     curUrl = curUrl + "&retmax=" + this.pageWidth + "&retstart=" + 
/* 117 */       pageNo * this.pageWidth;
/* 118 */     String content = this.http.get(curUrl);
/* 119 */     if (content == null) {
/* 120 */       return false;
/*     */     }
/* 122 */     int count = 0;
/* 123 */     int start = content.indexOf("Pubmed-entry");
/* 124 */     while (start >= 0) {
/* 125 */       int end = content.indexOf("Pubmed-entry", start + 12);
/* 126 */       if (end >= 0) {
/* 127 */         this.arrPaper[count] = content.substring(start, end);
/*     */       }
/*     */       else {
/* 130 */         this.arrPaper[count] = content.substring(start);
/*     */       }
/* 132 */       count++;
/* 133 */       start = end;
/*     */     }
/* 135 */     this.curPageNo = pageNo;
/* 136 */     this.curPageWidth = count;
/* 137 */     this.curArticleNo = 0;
/* 138 */     if (this.arrPaper[this.curArticleNo] == null)
/* 139 */       return false;
/* 140 */     this.curArticle = new PubMedArticle(this.arrPaper[this.curArticleNo]);
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */   public Article getArticleByKey(String PMID)
/*     */   {
/* 149 */     String curUrl = this.eFetchUrl + "db=PubMed&retmode=text&id=" + PMID;
/* 150 */     String content = this.http.get(curUrl);
/* 151 */     if (content == null) {
/* 152 */       return null;
/*     */     }
/* 154 */     int start = content.indexOf("Pubmed-entry");
/* 155 */     if (start >= 0) {
/* 156 */       return new PubMedArticle(content);
/*     */     }
/*     */ 
/* 159 */     return null;
/*     */   }
/*     */ 
/*     */   protected Article getArticle(int articleNo) {
/* 163 */     return new PubMedArticle(this.arrPaper[articleNo]);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.pubmed.PubMedQuery
 * JD-Core Version:    0.6.2
 */