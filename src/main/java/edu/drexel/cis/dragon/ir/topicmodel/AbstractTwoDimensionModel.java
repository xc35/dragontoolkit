/*    */ package edu.drexel.cis.dragon.ir.topicmodel;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ 
/*    */ public abstract class AbstractTwoDimensionModel extends AbstractModel
/*    */   implements TwoDimensionModel
/*    */ {
/*    */   protected IndexReader viewIndexReader;
/*    */   protected IndexReader topicIndexReader;
/*    */   protected int docNum;
/*    */   protected int viewNum;
/*    */   protected int themeNum;
/*    */   protected int viewTermNum;
/*    */   protected int themeTermNum;
/*    */   protected double[][] arrViewProb;
/*    */   protected double[][] arrDocView;
/*    */   protected double[][][] arrThemeProb;
/*    */   protected double[][][] arrDocTheme;
/*    */   protected double[][] arrCommonThemeProb;
/*    */ 
/*    */   public AbstractTwoDimensionModel(IndexReader viewIndexReader, IndexReader topicIndexReader)
/*    */   {
/* 21 */     this.viewIndexReader = viewIndexReader;
/* 22 */     this.topicIndexReader = topicIndexReader;
/* 23 */     this.docNum = Math.min(viewIndexReader.getCollection().getDocNum(), topicIndexReader.getCollection().getDocNum());
/* 24 */     this.viewTermNum = viewIndexReader.getCollection().getTermNum();
/* 25 */     this.themeTermNum = topicIndexReader.getCollection().getTermNum();
/*    */   }
/*    */ 
/*    */   public int getViewNum() {
/* 29 */     return this.viewNum;
/*    */   }
/*    */ 
/*    */   public int getTopicNum() {
/* 33 */     return this.themeNum;
/*    */   }
/*    */ 
/*    */   public double[] getView(int viewIndex) {
/* 37 */     return this.arrViewProb[viewIndex];
/*    */   }
/*    */ 
/*    */   public double[] getCommonTopic(int topicIndex) {
/* 41 */     return this.arrCommonThemeProb[topicIndex];
/*    */   }
/*    */ 
/*    */   public double[] getViewTopic(int viewIndex, int topicIndex) {
/* 45 */     return this.arrThemeProb[viewIndex][topicIndex];
/*    */   }
/*    */ 
/*    */   public int getDocNum() {
/* 49 */     return this.docNum;
/*    */   }
/*    */ 
/*    */   public double[] getDocViews(int docIndex) {
/* 53 */     return this.arrDocView[docIndex];
/*    */   }
/*    */ 
/*    */   public double[] getDocTopics(int docIndex, int viewIndex) {
/* 57 */     return this.arrDocTheme[docIndex][viewIndex];
/*    */   }
/*    */ 
/*    */   public int getViewTermNum() {
/* 61 */     return this.viewTermNum;
/*    */   }
/*    */ 
/*    */   public int getTopicTermNum() {
/* 65 */     return this.themeTermNum;
/*    */   }
/*    */ 
/*    */   public String getViewTermName(int termIndex) {
/* 69 */     return this.viewIndexReader.getTermKey(termIndex);
/*    */   }
/*    */ 
/*    */   public String getTopicTermName(int termIndex) {
/* 73 */     return this.topicIndexReader.getTermKey(termIndex);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.topicmodel.AbstractTwoDimensionModel
 * JD-Core Version:    0.6.2
 */