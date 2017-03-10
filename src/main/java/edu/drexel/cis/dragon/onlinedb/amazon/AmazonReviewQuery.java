/*     */ package edu.drexel.cis.dragon.onlinedb.amazon;
/*     */ 
/*     */ import edu.drexel.cis.dragon.onlinedb.AbstractQuery;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*     */ import edu.drexel.cis.dragon.util.Conversion;
/*     */ import edu.drexel.cis.dragon.util.HttpUtil;
/*     */ 
/*     */ public class AmazonReviewQuery extends AbstractQuery
/*     */ {
/*     */   protected String webEnv;
/*     */   protected String queryKey;
/*     */   protected String term;
/*     */   protected String reviewUrl;
/*     */   protected Article[] arrArticle;
/*     */   protected HttpUtil http;
/*     */ 
/*     */   public AmazonReviewQuery(String productCode)
/*     */   {
/*  24 */     super(10);
/*  25 */     this.arrArticle = new Article[this.pageWidth];
/*  26 */     this.term = productCode;
/*  27 */     this.reviewUrl = "/gp/product/customer-reviews/";
/*  28 */     this.http = new HttpUtil("www.amazon.com");
/*     */   }
/*     */ 
/*     */   public AmazonReviewQuery() {
/*  32 */     super(10);
/*  33 */     this.arrArticle = new Article[this.pageWidth];
/*  34 */     this.reviewUrl = "/gp/product/customer-reviews/";
/*  35 */     this.http = new HttpUtil("www.amazon.com");
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  40 */     AmazonReviewQuery query = new AmazonReviewQuery("B000AYGDIO");
/*  41 */     query.initQuery();
/*     */   }
/*     */ 
/*     */   public boolean supportArticleKeyRetrieval()
/*     */   {
/*  46 */     return false;
/*     */   }
/*     */ 
/*     */   public void setSearchTerm(String productCode) {
/*  50 */     this.term = productCode;
/*     */   }
/*     */ 
/*     */   public boolean initQuery()
/*     */   {
/*  56 */     this.curPageNo = -1;
/*  57 */     this.curArticle = null;
/*  58 */     this.curPageWidth = 0;
/*     */ 
/*  60 */     String curUrl = this.reviewUrl + this.term;
/*  61 */     String content = this.http.get(curUrl);
/*  62 */     if (content == null) return false;
/*     */ 
/*  65 */     this.articleNum = getReviewNum(content);
/*  66 */     if (this.articleNum == 0)
/*  67 */       this.pageNum = 0;
/*     */     else
/*  69 */       this.pageNum = ((this.articleNum - 1) / this.pageWidth + 1);
/*  70 */     if (this.articleNum > 0) {
/*  71 */       this.curPageWidth = readReviewsFromWebPage(content);
/*     */     }
/*  73 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean moveToPage(int pageNo)
/*     */   {
/*  79 */     if ((pageNo >= this.pageNum) || (this.pageNum == 0)) {
/*  80 */       return false;
/*     */     }
/*  82 */     if (pageNo == this.curPageNo) return true;
/*     */ 
/*  84 */     if ((pageNo == 0) && (this.curPageNo == -1)) {
/*  85 */       this.curPageNo = pageNo;
/*  86 */       this.curArticleNo = 0;
/*  87 */       if (this.curPageWidth <= 0) return false;
/*  88 */       this.curArticle = this.arrArticle[this.curArticleNo];
/*  89 */       return true;
/*     */     }
/*     */ 
/*  92 */     String curUrl = this.reviewUrl + this.term + "?_encoding=UTF8&customer-reviews.sort_by=-SubmissionDate&customer-reviews.start=" + (pageNo * this.pageWidth + 1);
/*  93 */     String content = this.http.get(curUrl);
/*  94 */     if (content == null) {
/*  95 */       return false;
/*     */     }
/*  97 */     this.curPageWidth = readReviewsFromWebPage(content);
/*  98 */     if (this.curPageWidth <= 0) return false;
/*  99 */     this.curPageNo = pageNo;
/* 100 */     this.curArticleNo = 0;
/* 101 */     this.curArticle = this.arrArticle[this.curArticleNo];
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */   public Article getArticleByKey(String PMID) {
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   protected Article getArticle(int articleNo) {
/* 110 */     return this.arrArticle[articleNo];
/*     */   }
/*     */ 
/*     */   private int getReviewNum(String content)
/*     */   {
/*     */     try
/*     */     {
/* 117 */       int start = content.indexOf("customer-reviews.sort_by");
/* 118 */       if (start < 0) return 0;
/* 119 */       start = content.indexOf("</form>", start);
/* 120 */       if (start < 0) return 0;
/* 121 */       int end = start + 7;
/* 122 */       while (!Character.isDigit(content.charAt(end))) end++;
/* 123 */       if (end - start >= 30) return 0;
/*     */ 
/* 125 */       start = content.indexOf("of ", end) + 3;
/* 126 */       end = content.indexOf("\n", start);
/* 127 */       return Integer.parseInt(content.substring(start, end));
/*     */     }
/*     */     catch (Exception e) {
/* 130 */       e.printStackTrace();
/* 131 */     }return 0;
/*     */   }
/*     */ 
/*     */   private int readReviewsFromWebPage(String content)
/*     */   {
/*     */     try
/*     */     {
/* 140 */       int start = content.indexOf("customer-reviews.sort_by");
/* 141 */       if (start < 0) return 0;
/* 142 */       start = content.indexOf("</form>", start);
/* 143 */       if (start < 0) return 0;
/*     */ 
/* 145 */       int count = 0;
/* 146 */       start = content.indexOf("<!-- BOUNDARY -->", start);
/* 147 */       while (start > 0) {
/* 148 */         int end = content.indexOf("<!-- BOUNDARY -->", start + 20);
/* 149 */         if (end > 0) {
/* 150 */           this.arrArticle[count] = readReview(content.substring(start, end));
/* 151 */           count++;
/* 152 */           start = end;
/*     */         }
/*     */         else
/*     */         {
/* 156 */           end = content.indexOf("<hr ", start);
/* 157 */           this.arrArticle[count] = readReview(content.substring(start, end));
/* 158 */           count++;
/* 159 */           start = -1;
/*     */         }
/*     */       }
/* 162 */       return count;
/*     */     }
/*     */     catch (Exception e) {
/* 165 */       e.printStackTrace();
/* 166 */     }return 0;
/*     */   }
/*     */ 
/*     */   private Article readReview(String content)
/*     */   {
/* 174 */     BasicArticle article = new BasicArticle();
/* 175 */     int start = content.indexOf("name=\"") + 7;
/* 176 */     int end = content.indexOf("\"", start);
/* 177 */     article.setKey(content.substring(start, end));
/*     */ 
/* 179 */     end = content.indexOf("width", end);
/* 180 */     start = content.lastIndexOf("/", end);
/* 181 */     article.setCategory(Integer.parseInt(content.substring(start + 7, start + 8)));
/*     */ 
/* 183 */     start = content.indexOf("<b>", end) + 3;
/* 184 */     end = content.indexOf("</b>", start);
/* 185 */     article.setTitle(content.substring(start, end));
/*     */ 
/* 187 */     start = end + 5;
/* 188 */     end = content.indexOf("<br />", start);
/* 189 */     article.setDate(Conversion.engDate(content.substring(start, end).trim()));
/*     */ 
/* 191 */     start = content.indexOf("</b>", end);
/* 192 */     if (start > 0)
/* 193 */       start += 4;
/*     */     else
/* 195 */       start = content.indexOf("</table>", end) + 8;
/* 196 */     end = content.indexOf("<nobr>", start);
/* 197 */     end = content.lastIndexOf("<br /><br />", end);
/* 198 */     article.setAbstract(processReviewContent(content.substring(start, end)));
/*     */ 
/* 200 */     return article;
/*     */   }
/*     */ 
/*     */   private String processReviewContent(String raw)
/*     */   {
/* 209 */     raw = raw.replaceAll("\r", "");
/* 210 */     raw = raw.replaceAll("\n", "");
/* 211 */     raw = raw.replaceAll("<br />", "<br>");
/* 212 */     raw = raw.replaceAll("<p>", "<br>");
/* 213 */     String[] arrPara = raw.split("<br>");
/* 214 */     StringBuffer sb = new StringBuffer();
/* 215 */     for (int i = 0; i < arrPara.length; i++)
/*     */     {
/*     */       String line;
/* 216 */       if ((arrPara[i] != null) && ((line = arrPara[i].trim()).length() != 0))
/*     */       {
/* 217 */         if (sb.length() > 0) sb.append(" ");
/* 218 */         sb.append(line);
/* 219 */         if (".?!".indexOf(line.charAt(line.length() - 1)) < 0)
/* 220 */           sb.append('.'); 
/*     */       }
/*     */     }
/* 222 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.amazon.AmazonReviewQuery
 * JD-Core Version:    0.6.2
 */