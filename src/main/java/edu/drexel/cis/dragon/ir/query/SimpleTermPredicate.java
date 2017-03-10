/*    */ package edu.drexel.cis.dragon.ir.query;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.compare.SortedElement;
/*    */ 
/*    */ public class SimpleTermPredicate extends AbstractPredicate
/*    */   implements SortedElement
/*    */ {
/*    */   private int termFrequency;
/*    */   private int index;
/*    */   private int docFrequency;
/*    */ 
/*    */   public SimpleTermPredicate(String[] predicate)
/*    */   {
/* 19 */     parse(predicate);
/*    */   }
/*    */ 
/*    */   public SimpleTermPredicate(SimpleExpression constraint) {
/* 23 */     this.predicateType = 1;
/* 24 */     this.expressionType = 1;
/* 25 */     this.constraint = constraint;
/*    */   }
/*    */ 
/*    */   protected void parse(String[] predicate) {
/* 29 */     this.predicateType = 1;
/* 30 */     this.expressionType = 1;
/* 31 */     this.constraint = new SimpleExpression(predicate);
/*    */   }
/*    */ 
/*    */   public SimpleTermPredicate copy()
/*    */   {
/* 37 */     SimpleTermPredicate cur = new SimpleTermPredicate((SimpleExpression)this.constraint);
/* 38 */     cur.setWeight(this.weight);
/* 39 */     cur.setIndex(this.index);
/* 40 */     cur.setDocFrequency(this.docFrequency);
/* 41 */     cur.setFrequency(this.termFrequency);
/* 42 */     return cur;
/*    */   }
/*    */ 
/*    */   public String getKey() {
/* 46 */     return getTestValue();
/*    */   }
/*    */ 
/*    */   public String getField() {
/* 50 */     return ((SimpleExpression)this.constraint).getField();
/*    */   }
/*    */ 
/*    */   public String getTestValue() {
/* 54 */     return (String)((SimpleExpression)this.constraint).getTestValue();
/*    */   }
/*    */ 
/*    */   public String toSQLExpression() {
/* 58 */     return this.constraint.toSQLExpression();
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 62 */     if (this.weight != 1.0D) {
/* 63 */       return "T(" + String.valueOf(this.weight) + ", " + this.constraint.toString() + ")";
/*    */     }
/* 65 */     return "T(" + this.constraint.toString() + ")";
/*    */   }
/*    */ 
/*    */   public int getDocFrequency() {
/* 69 */     return this.docFrequency;
/*    */   }
/*    */ 
/*    */   public void setDocFrequency(int freq) {
/* 73 */     this.docFrequency = freq;
/*    */   }
/*    */ 
/*    */   public int getFrequency() {
/* 77 */     return this.termFrequency;
/*    */   }
/*    */ 
/*    */   public void setFrequency(int freq) {
/* 81 */     this.termFrequency = freq;
/*    */   }
/*    */ 
/*    */   public int getIndex() {
/* 85 */     return this.index;
/*    */   }
/*    */ 
/*    */   public void setIndex(int index) {
/* 89 */     this.index = index;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.SimpleTermPredicate
 * JD-Core Version:    0.6.2
 */