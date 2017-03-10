/*     */ package edu.drexel.cis.dragon.onlinedb.amazon;
/*     */ 
/*     */ import edu.drexel.cis.dragon.onlinedb.AbstractQuery;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.HttpUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class AmazonCatalogQuery extends AbstractQuery
/*     */ {
/*     */   protected String queryKey;
/*     */   protected String term;
/*     */   protected String searchUrl;
/*     */   protected String browseUrl;
/*     */   protected Article[] arrArticle;
/*     */   protected HttpUtil http;
/*     */ 
/*     */   public AmazonCatalogQuery(String productCatalog)
/*     */   {
/*  24 */     super(24);
/*  25 */     this.arrArticle = new Article[this.pageWidth];
/*  26 */     this.term = productCatalog;
/*  27 */     this.searchUrl = "/gp/search";
/*  28 */     this.browseUrl = "/gp/browse.html";
/*  29 */     this.http = new HttpUtil("www.amazon.com");
/*     */   }
/*     */ 
/*     */   public AmazonCatalogQuery() {
/*  33 */     super(24);
/*  34 */     this.arrArticle = new Article[this.pageWidth];
/*  35 */     this.searchUrl = "/gp/search";
/*  36 */     this.browseUrl = "/gp/browse.html";
/*  37 */     this.http = new HttpUtil("www.amazon.com");
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  43 */     getProductList("565108", "indexreview/laptop.query", "indexreview/laptop.desc");
/*     */   }
/*     */ 
/*     */   public static void getProductList(String catalogNode, String codeListFile, String descriptionListFile)
/*     */   {
/*     */     try
/*     */     {
/*  54 */       AmazonCatalogQuery query = new AmazonCatalogQuery(catalogNode);
/*  55 */       if ((!query.initQuery()) || (query.size() <= 0)) {
/*  56 */         return;
/*     */       }
/*  58 */       int count = 0;
/*  59 */       SortedArray codeList = new SortedArray(query.size());
/*  60 */       BufferedWriter bwDesc = FileUtil.getTextWriter(descriptionListFile);
/*  61 */       while (query.moveToNextArticle()) {
/*  62 */         Article article = query.getArticle();
/*  63 */         if (article.getKey() != null) {
/*  64 */           System.out.println(count + " " + article.getKey());
/*  65 */           bwDesc.write(article.getKey() + "\t" + article.getTitle() + "\n");
/*  66 */           bwDesc.flush();
/*  67 */           codeList.add(article.getKey());
/*  68 */           count++;
/*     */         }
/*     */       }
/*  71 */       bwDesc.close();
/*     */ 
/*  73 */       BufferedWriter bwCode = FileUtil.getTextWriter(codeListFile);
/*  74 */       bwCode.write(codeList.size() + "\n");
/*  75 */       for (int i = 0; i < codeList.size(); i++) {
/*  76 */         bwCode.write((String)codeList.get(i) + "\n");
/*  77 */         bwCode.flush();
/*     */       }
/*  79 */       bwCode.flush();
/*     */     }
/*     */     catch (Exception e) {
/*  82 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean supportArticleKeyRetrieval() {
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */   public void setSearchTerm(String productCatalog) {
/*  91 */     this.term = productCatalog;
/*     */   }
/*     */ 
/*     */   public boolean initQuery()
/*     */   {
/*  97 */     this.curPageNo = -1;
/*  98 */     this.curArticle = null;
/*  99 */     this.curPageWidth = 0;
/*     */ 
/* 101 */     String curUrl = this.browseUrl + "?_encoding=UTF8&node=" + this.term;
/* 102 */     String content = this.http.get(curUrl);
/* 103 */     if (content == null) return false;
/*     */ 
/* 106 */     this.articleNum = getProductNum(content);
/* 107 */     if (this.articleNum == 0)
/* 108 */       this.pageNum = 0;
/*     */     else
/* 110 */       this.pageNum = ((this.articleNum - 1) / this.pageWidth + 1);
/* 111 */     if (this.articleNum > 0) {
/* 112 */       this.curPageWidth = readProductFromWebPage(content);
/*     */     }
/* 114 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean moveToPage(int pageNo)
/*     */   {
/* 120 */     if ((pageNo >= this.pageNum) || (this.pageNum == 0)) {
/* 121 */       return false;
/*     */     }
/* 123 */     if ((pageNo == 0) && (this.curPageNo == -1)) {
/* 124 */       this.curPageNo = pageNo;
/* 125 */       this.curArticleNo = 0;
/* 126 */       if (this.curPageWidth <= 0) return false;
/* 127 */       this.curArticle = this.arrArticle[this.curArticleNo];
/* 128 */       return true;
/*     */     }
/*     */ 
/* 131 */     String curUrl = this.searchUrl + "?_encoding=UTF8&rh=" + this.queryKey + "&page=" + (pageNo + 1);
/* 132 */     String content = this.http.get(curUrl);
/* 133 */     if (content == null) {
/* 134 */       return false;
/*     */     }
/* 136 */     this.curPageWidth = readProductFromWebPage(content);
/* 137 */     if (this.curPageWidth <= 0) return false;
/* 138 */     this.curPageNo = pageNo;
/* 139 */     this.curArticleNo = 0;
/* 140 */     this.curArticle = this.arrArticle[this.curArticleNo];
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */   public Article getArticleByKey(String PMID) {
/* 145 */     return null;
/*     */   }
/*     */ 
/*     */   protected Article getArticle(int articleNo) {
/* 149 */     return this.arrArticle[articleNo];
/*     */   }
/*     */ 
/*     */   private int getProductNum(String content)
/*     */   {
/*     */     try
/*     */     {
/* 157 */       int start = content.indexOf("\"ladderCount\"");
/* 158 */       if (start < 0) return 0;
/* 159 */       start = content.indexOf("of ", start) + 3;
/* 160 */       int end = content.indexOf(" result", start);
/* 161 */       int num = Integer.parseInt(content.substring(start, end));
/*     */ 
/* 164 */       start = content.indexOf("name=\"rh\"", end) + 9;
/* 165 */       start = content.indexOf("\"", start) + 1;
/* 166 */       end = content.indexOf("\"", start);
/* 167 */       this.queryKey = content.substring(start, end);
/* 168 */       return num;
/*     */     }
/*     */     catch (Exception e) {
/* 171 */       e.printStackTrace();
/* 172 */     }return 0;
/*     */   }
/*     */ 
/*     */   private int readProductFromWebPage(String content)
/*     */   {
/*     */     try
/*     */     {
/* 181 */       int start = content.indexOf("<div id=\"Results\">");
/* 182 */       if (start < 0) return 0;
/* 183 */       int end = content.indexOf("</div>", start);
/* 184 */       if (start < 0) return 0;
/* 185 */       content = content.substring(start, end);
/*     */ 
/* 187 */       int count = 0;
/* 188 */       start = content.indexOf("id=\"Td:");
/* 189 */       while (start > 0) {
/* 190 */         end = content.indexOf("id=\"Td:", start + 10);
/* 191 */         if (end > 0) {
/* 192 */           this.arrArticle[count] = readProduct(content.substring(start, end));
/* 193 */           count++;
/* 194 */           start = end;
/*     */         }
/*     */         else {
/* 197 */           this.arrArticle[count] = readProduct(content.substring(start));
/* 198 */           count++;
/* 199 */           start = end;
/*     */         }
/* 201 */         if (this.arrArticle[(count - 1)] == null) {
/* 202 */           return 0;
/*     */         }
/*     */       }
/* 205 */       return count;
/*     */     }
/*     */     catch (Exception e) {
/* 208 */       e.printStackTrace();
/* 209 */     }return 0;
/*     */   }
/*     */ 
/*     */   private Article readProduct(String content)
/*     */   {
/* 218 */     BasicArticle article = new BasicArticle();
/* 219 */     int start = content.indexOf("<a ") + 3;
/* 220 */     start = content.indexOf("<a ", start);
/* 221 */     start = content.indexOf("product/", start);
/* 222 */     if (start < 0)
/*     */     {
/* 224 */       return article;
/*     */     }
/*     */ 
/* 227 */     start += 8;
/* 228 */     int end = content.indexOf("/", start);
/* 229 */     String key = content.substring(start, end).trim();
/* 230 */     if (key.length() > 10) {
/* 231 */       return null;
/*     */     }
/*     */ 
/* 234 */     article.setKey(content.substring(start, end));
/*     */ 
/* 236 */     start = content.indexOf(">", end) + 1;
/* 237 */     end = content.indexOf("<", start);
/* 238 */     article.setTitle(content.substring(start, end));
/*     */ 
/* 240 */     return article;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.amazon.AmazonCatalogQuery
 * JD-Core Version:    0.6.2
 */