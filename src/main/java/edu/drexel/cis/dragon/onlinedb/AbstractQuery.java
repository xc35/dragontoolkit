/*     */ package edu.drexel.cis.dragon.onlinedb;
/*     */ 
/*     */ public abstract class AbstractQuery
/*     */   implements ArticleQuery
/*     */ {
/*     */   protected int curPageNo;
/*     */   protected int curArticleNo;
/*     */   protected int pageNum;
/*     */   protected int articleNum;
/*     */   protected int pageWidth;
/*     */   protected int curPageWidth;
/*     */   protected Article curArticle;
/*     */   protected ArticleParser parser;
/*     */ 
/*     */   protected abstract Article getArticle(int paramInt);
/*     */ 
/*     */   public AbstractQuery(int pageWidth)
/*     */   {
/*  22 */     this.pageWidth = pageWidth;
/*  23 */     this.curPageNo = -1;
/*  24 */     this.curArticle = null;
/*  25 */     this.articleNum = -1;
/*     */   }
/*     */ 
/*     */   public int getCurPageNo() {
/*  29 */     return this.curPageNo;
/*     */   }
/*     */ 
/*     */   public boolean moveToNextPage() {
/*  33 */     return moveToPage(this.curPageNo + 1);
/*     */   }
/*     */ 
/*     */   public int getTotalArticleNum() {
/*  37 */     return this.articleNum;
/*     */   }
/*     */ 
/*     */   public int getPageNum() {
/*  41 */     return this.pageNum;
/*     */   }
/*     */ 
/*     */   public int getPageWidth() {
/*  45 */     return this.pageWidth;
/*     */   }
/*     */ 
/*     */   public int getCurPageWidth() {
/*  49 */     return this.curPageWidth;
/*     */   }
/*     */ 
/*     */   public boolean moveToNextArticle() {
				int PageWidth = this.curPageWidth - 1;
				int ArticleNo  = this.curArticleNo; 
				int PageNo= this.curPageNo ;
/*  53 */     if ((ArticleNo >= PageWidth ) || (PageNo <= -1)) 
				{
						if (!moveToNextPage())
/*  55 */         return false;
/*     */     }   else    {
/*  59 */       this.curArticleNo += 1;
/*  60 */       this.curArticle = getArticle(this.curArticleNo);
/*     */     }
/*  62 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean moveToArticle(int paperNo) {
/*  66 */     if ((paperNo < 0) || (paperNo > this.curPageWidth)) {
/*  67 */       return false;
/*     */     }
/*  69 */     this.curArticleNo = paperNo;
/*  70 */     this.curArticle = getArticle(this.curArticleNo);
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */   public Article getArticle() {
/*  75 */     return this.curArticle;
/*     */   }
/*     */ 
/*     */   public String getArticleKey() {
/*  79 */     return this.curArticle.getKey();
/*     */   }
/*     */ 
/*     */   public boolean loadCollection(String collectionPath, String collectionName) {
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */   public ArticleParser getArticleParser() {
/*  87 */     return this.parser;
/*     */   }
/*     */ 
/*     */   public void setArticleParser(ArticleParser parser) {
/*  91 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */   public Article getNextArticle() {
/*  95 */     if (moveToNextArticle()) {
/*  96 */       return this.curArticle;
/*     */     }
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */   public void close() {
/* 102 */     this.curPageNo = -1;
/* 103 */     this.curArticle = null;
/*     */   }
/*     */ 
/*     */   public void restart() {
/* 107 */     close();
/* 108 */     initQuery();
/*     */   }
/*     */ 
/*     */   public int size() {
/* 112 */     return this.articleNum;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.AbstractQuery
 * JD-Core Version:    0.6.2
 */