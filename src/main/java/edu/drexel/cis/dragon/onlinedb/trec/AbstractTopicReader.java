/*    */ package edu.drexel.cis.dragon.onlinedb.trec;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.ArticleParser;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticleParser;
/*    */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public abstract class AbstractTopicReader
/*    */   implements CollectionReader
/*    */ {
/*    */   private ArrayList topics;
/*    */   private int curTopic;
/*    */ 
/*    */   public AbstractTopicReader(String topicFile)
/*    */   {
/* 19 */     this.topics = loadTopics(topicFile);
/* 20 */     this.curTopic = 0;
/*    */   }
/*    */ 
/*    */   protected abstract ArrayList loadTopics(String paramString);
/*    */ 
/*    */   public boolean loadCollection(String topicFile) {
/* 26 */     this.topics = loadTopics(topicFile);
/* 27 */     this.curTopic = 0;
/* 28 */     return true;
/*    */   }
/*    */ 
/*    */   public ArticleParser getArticleParser() {
/* 32 */     return new BasicArticleParser();
/*    */   }
/*    */ 
/*    */   public void setArticleParser(ArticleParser parser)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Article getNextArticle() {
/* 40 */     if ((this.topics == null) || (this.curTopic >= this.topics.size())) {
/* 41 */       return null;
/*    */     }
/* 43 */     this.curTopic += 1;
/* 44 */     return (Article)this.topics.get(this.curTopic - 1);
/*    */   }
/*    */ 
/*    */   public Article getArticleByKey(String key)
/*    */   {
/* 49 */     return null;
/*    */   }
/*    */ 
/*    */   public void close() {
/* 53 */     this.topics = null;
/*    */   }
/*    */ 
/*    */   public int size() {
/* 57 */     return this.topics.size();
/*    */   }
/*    */ 
/*    */   public boolean supportArticleKeyRetrieval() {
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   public void restart() {
/* 65 */     this.curTopic = 0;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.trec.AbstractTopicReader
 * JD-Core Version:    0.6.2
 */