/*    */ package edu.drexel.cis.dragon.ir.summarize;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*    */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ 
/*    */ public class TopicSummary
/*    */ {
/*    */   private SortedArray textList;
/*    */   private int textGranularity;
/*    */ 
/*    */   public TopicSummary(int textGranularity)
/*    */   {
/* 20 */     this.textList = new SortedArray();
/* 21 */     this.textGranularity = textGranularity;
/*    */   }
/*    */ 
/*    */   public int getTextGranularity() {
/* 25 */     return this.textGranularity;
/*    */   }
/*    */ 
/*    */   public boolean addText(TextUnit text) {
/* 29 */     return this.textList.add(text);
/*    */   }
/*    */ 
/*    */   public boolean contains(TextUnit text) {
/* 33 */     return this.textList.contains(text);
/*    */   }
/*    */ 
/*    */   public int size() {
/* 37 */     return this.textList.size();
/*    */   }
/*    */ 
/*    */   public TextUnit getTextUnit(int index) {
/* 41 */     return (TextUnit)this.textList.get(index);
/*    */   }
/*    */ 
/*    */   public void sortByWegiht() {
/* 45 */     this.textList.setComparator(new WeightComparator(true));
/*    */   }
/*    */ 
/*    */   public void sortByIndex() {
/* 49 */     this.textList.setComparator(new IndexComparator());
/*    */   }
/*    */ 
/*    */   public void sortByText() {
/* 53 */     this.textList.setComparator(null);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.summarize.TopicSummary
 * JD-Core Version:    0.6.2
 */