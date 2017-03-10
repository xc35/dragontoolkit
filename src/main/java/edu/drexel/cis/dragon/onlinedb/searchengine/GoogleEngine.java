/*     */ package edu.drexel.cis.dragon.onlinedb.searchengine;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.HttpContent;
/*     */ import edu.drexel.cis.dragon.util.HttpUtil;
/*     */ import org.apache.commons.httpclient.util.URIUtil;
/*     */ 
/*     */ public class GoogleEngine extends AbstractSearchEngine
/*     */ {
/*     */   private HttpUtil http;
/*     */ 
/*     */   public GoogleEngine()
/*     */   {
/*  19 */     this(10);
/*     */   }
/*     */   public GoogleEngine(int pageWidth) {
/*  22 */     super(pageWidth);
/*  23 */     this.http = new HttpUtil("www.google.com", "UTF-8");
/*  24 */     this.http.setConnectionTimeout(10000);
/*  25 */     this.http.setSocketTimeout(10000);
/*     */   }
/*     */ 
/*     */   public void setSearchTerm(String term) {
/*  29 */     this.term = term;
/*     */   }
/*     */ 
/*     */   public boolean initQuery() {
/*  33 */     this.curPageNo = -1;
/*  34 */     this.curArticle = null;
/*  35 */     this.curPageWidth = 0;
/*  36 */     this.pageNum = 1;
/*  37 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean moveToPage(int pageNo)
/*     */   {
/*     */     try
/*     */     {
/*  48 */       if ((pageNo >= this.pageNum) || (this.pageNum == 0))
/*  49 */         return false;
/*  50 */       if (pageNo == this.curPageNo) return true;
/*     */ 
/*  52 */       String query = this.term;
/*  53 */       query = query.replaceAll("  ", " ");
/*  54 */       query = query.replaceAll("  ", " ");
/*  55 */       query = query.replace(' ', '+');
/*  56 */       query = URIUtil.encodeAll(query, "UTF-8");
/*  57 */       query = query.replaceAll("%2B", "+");
/*     */ 
/*  59 */       int count = 0;
/*  60 */       while ((pageNo < this.pageNum) && (count == 0))
/*     */       {
/*     */         String url;
/*  61 */         if (pageNo == 0)
/*  62 */           url = "/search?hl=en&newwindow=1&rlz=1T4GZHY_enUS237US237&num=" + this.pageWidth + "&q=" + query + "&btnG=Search";
/*     */         else
/*  64 */           url = "/search?hl=en&newwindow=1&rlz=1T4GZHY_enUS237US237&num=" + this.pageWidth + "&q=" + query + "&start=" + pageNo * this.pageWidth;
/*  65 */         if ((this.site != null) && (this.site.length() > 0))
/*  66 */           url = url + "&sitesearch=" + this.site;
/*  67 */         String content = this.http.get(url, "UTF-8");
/*  68 */         if (content == null)
/*  69 */           return false;
/*  70 */         count = processPage(pageNo, content);
/*  71 */         if (count < 0)
/*  72 */           return false;
/*  73 */         if (count > 0) {
/*  74 */           return true;
/*     */         }
/*  76 */         pageNo++;
/*     */       }
/*  78 */       return true;
/*     */     } catch (Exception e) {
/*     */     }
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   private int processPage(int pageNo, String content)
/*     */   {
/*     */     try
/*     */     {
					System.out.println(content);
/*  91 */       int count = 0;
/*     */       String word;
/*  92 */       if (getSummaryOnlyOption())
/*  93 */         word = ">Similar<";
/*     */       else
/*  95 */         word = ">Cached<";
/*  96 */       int startPos = content.indexOf(word);
/*     */       boolean hasDoc;
/*  97 */       if (startPos >= 0)
/*  98 */         hasDoc = true;
/*     */       else {
/* 100 */         hasDoc = false;
/*     */       }
/* 102 */       while (startPos > 0) {
/* 103 */         int pos = startPos;
/*     */ 
/* 106 */         int endPos = content.lastIndexOf("<cite", startPos - 5);
/* 107 */         startPos = content.lastIndexOf("<div ", endPos);
/* 108 */         String summary = content.substring(startPos, endPos);
/*     */ 
/* 110 */         int end = summary.indexOf("<span ");
/* 111 */         if (end >= 0) {
/* 112 */           end = summary.indexOf("<br>", end + 5);
/* 113 */           summary = summary.substring(end + 4);
/*     */         }
/*     */ 
/* 116 */         summary = this.parser.extractText(summary);
/* 117 */         if ((summary == null) || (summary.length() == 0))
/*     */         {
/* 119 */           startPos = content.indexOf(word, pos + 10);
/*     */         }
/*     */         else
/*     */         {
/* 124 */           end = startPos;
/* 125 */           startPos = content.lastIndexOf("href=", startPos - 5);
/*     */ 
/* 127 */           if (content.substring(startPos, end).indexOf("translate.google") >= 0) {
/* 128 */             startPos = content.lastIndexOf("href=", startPos - 5);
/*     */           }
/* 130 */           endPos = content.indexOf("\"", startPos + 6);
/* 131 */           String url = content.substring(startPos + 6, endPos);
/*     */ 
/* 135 */           startPos = content.indexOf(">", endPos);
/* 136 */           endPos = content.indexOf("</a>", startPos + 1);
/* 137 */           String title = this.parser.extractText(content.substring(startPos + 1, endPos));
/*     */ 
/* 139 */           this.arrUrl[count] = new WebLink(url);
/* 140 */           this.arrUrl[count].setSummary(processSnippet(summary));
/* 141 */           this.arrUrl[count].setTitle(processSnippet(title));
/*     */ 
/* 144 */           count++;
/* 145 */           startPos = content.indexOf(word, pos + 10);
/*     */         }
/*     */       }
/* 148 */       this.curPageNo = pageNo;
/* 149 */       this.curPageWidth = count;
/* 150 */       this.curArticleNo = 0;
/* 151 */       if ((this.curPageWidth == 0) && (!hasDoc))
/* 152 */         return -1;
/* 153 */       if (count > 0) {
/* 154 */         this.curArticle = getArticle(0);
/*     */       }
/*     */ 
/* 157 */       int end = content.indexOf(">Next<");
/* 158 */       if (end >= 0)
/* 159 */         this.pageNum = (pageNo + 2);
/* 160 */       return count;
/*     */     }
/*     */     catch (Exception e) {
/* 163 */       e.printStackTrace();
/* 164 */     }return -1;
/*     */   }
/*     */ 
/*     */   private String processSnippet(String content)
/*     */   {
/* 169 */     if ((content == null) || (content.length() == 0)) {
/* 170 */       return content;
/*     */     }
/* 172 */     content = content.replaceAll("&#39;", "'");
/* 173 */     content = content.replaceAll("&quot;", "\"");
/* 174 */     content = content.replaceAll("&lt;", "<");
/* 175 */     content = content.replaceAll("&gt;", ">");
/* 176 */     return content;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.searchengine.GoogleEngine
 * JD-Core Version:    0.6.2
 */