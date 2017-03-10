/*    */ package edu.drexel.cis.dragon.onlinedb;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class ArrayCollectionReader
/*    */   implements CollectionReader
/*    */ {
/*    */   private SortedArray sortlist;
/*    */   private ArrayList list;
/*    */   private int curPos;
/*    */ 
/*    */   public ArrayCollectionReader()
/*    */   {
/* 21 */     this.list = new ArrayList();
/* 22 */     this.sortlist = new SortedArray();
/* 23 */     this.curPos = 0;
/*    */   }
/*    */ 
/*    */   public ArticleParser getArticleParser() {
/* 27 */     return null;
/*    */   }
/*    */ 
/*    */   public void setArticleParser(ArticleParser parser) {
/*    */   }
/*    */ 
/*    */   public int size() {
/* 34 */     return this.list.size();
/*    */   }
/*    */ 
/*    */   public void close() {
/* 38 */     this.list.clear();
/* 39 */     this.sortlist.clear();
/* 40 */     this.curPos = 0;
/*    */   }
/*    */ 
/*    */   public Article getNextArticle() {
/* 44 */     if (this.curPos < this.list.size()) {
/* 45 */       return (Article)this.list.get(this.curPos++);
/*    */     }
/* 47 */     return null;
/*    */   }
/*    */ 
/*    */   public Article getArticleByKey(String key)
/*    */   {
/* 53 */     BasicArticle article = new BasicArticle();
/* 54 */     article.setKey(key);
/* 55 */     if (this.sortlist.binarySearch(article) < 0) {
/* 56 */       return null;
/*    */     }
/* 58 */     return (Article)this.sortlist.get(this.sortlist.insertedPos());
/*    */   }
/*    */ 
/*    */   public boolean addArticle(Article article) {
/* 62 */     if (!this.sortlist.add(article)) {
/* 63 */       return false;
/*    */     }
/* 65 */     this.list.add(article);
/* 66 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean supportArticleKeyRetrieval()
/*    */   {
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */   public void restart() {
/* 75 */     this.curPos = 0;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.ArrayCollectionReader
 * JD-Core Version:    0.6.2
 */