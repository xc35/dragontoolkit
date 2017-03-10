/*    */ package edu.drexel.cis.dragon.ir.topicmodel;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ 
/*    */ public abstract class AbstractTopicModel extends AbstractModel
/*    */   implements TopicModel
/*    */ {
/*    */   protected int themeNum;
/*    */   protected int termNum;
/*    */   protected int docNum;
/*    */   protected double[][] arrThemeTerm;
/*    */   protected double[][] arrDocTheme;
/*    */   protected IndexReader indexReader;
/*    */ 
/*    */   public AbstractTopicModel(IndexReader indexReader)
/*    */   {
/* 20 */     this.indexReader = indexReader;
/*    */   }
/*    */ 
/*    */   public int getTopicNum() {
/* 24 */     return this.themeNum;
/*    */   }
/*    */ 
/*    */   public double[] getTopic(int topicIndex) {
/* 28 */     return this.arrThemeTerm[topicIndex];
/*    */   }
/*    */ 
/*    */   public int getDocNum() {
/* 32 */     return this.docNum;
/*    */   }
/*    */ 
/*    */   public double[] getDocTopics(int docIndex) {
/* 36 */     return this.arrDocTheme[docIndex];
/*    */   }
/*    */ 
/*    */   public int getTermNum() {
/* 40 */     return this.termNum;
/*    */   }
/*    */ 
/*    */   public String getTermName(int termIndex) {
/* 44 */     return this.indexReader.getTermKey(termIndex);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.topicmodel.AbstractTopicModel
 * JD-Core Version:    0.6.2
 */