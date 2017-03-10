/*    */ package edu.drexel.cis.dragon.ir.summarize;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexSortable;
/*    */ import edu.drexel.cis.dragon.nlp.compare.WeightSortable;
/*    */ 
/*    */ public class TextUnit
/*    */   implements WeightSortable, IndexSortable, Comparable
/*    */ {
/*    */   public static final int UNIT_TERM = 1;
/*    */   public static final int UNIT_RELATION = 2;
/*    */   public static final int UNIT_SENTENCE = 3;
/*    */   public static final int UNIT_PARAGRAPH = 4;
/*    */   public static final int UNIT_ARTICLE = 5;
/*    */   private String text;
/*    */   private double weight;
/*    */   private int index;
/*    */ 
/*    */   public TextUnit(String text)
/*    */   {
/* 25 */     this(text, -1, 0.0D);
/*    */   }
/*    */ 
/*    */   public TextUnit(String text, double weight) {
/* 29 */     this(text, -1, weight);
/*    */   }
/*    */ 
/*    */   public TextUnit(String text, int index, double weight) {
/* 33 */     this.text = text;
/* 34 */     this.index = index;
/* 35 */     this.weight = weight;
/*    */   }
/*    */ 
/*    */   public double getWeight() {
/* 39 */     return this.weight;
/*    */   }
/*    */ 
/*    */   public void setWeight(double weight) {
/* 43 */     this.weight = weight;
/*    */   }
/*    */ 
/*    */   public int getIndex() {
/* 47 */     return this.index;
/*    */   }
/*    */ 
/*    */   public void setIndex(int index) {
/* 51 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public String getText() {
/* 55 */     return this.text;
/*    */   }
/*    */ 
/*    */   public void setText(String text) {
/* 59 */     this.text = text;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj) {
/* 63 */     return this.text.compareToIgnoreCase(((TextUnit)obj).getText());
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.summarize.TextUnit
 * JD-Core Version:    0.6.2
 */