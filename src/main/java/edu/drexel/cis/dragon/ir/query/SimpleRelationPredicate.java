/*    */ package edu.drexel.cis.dragon.ir.query;
/*    */ 
/*    */ public class SimpleRelationPredicate extends AbstractPredicate
/*    */ {
/*    */   private SimpleTermPredicate first;
/*    */   private SimpleTermPredicate second;
/*    */ 
/*    */   public SimpleRelationPredicate(String[] predicate)
/*    */   {
/* 16 */     parse(predicate);
/*    */   }
/*    */ 
/*    */   protected void parse(String[] predicate)
/*    */   {
/* 22 */     this.predicateType = 2;
/* 23 */     this.expressionType = 1;
/* 24 */     this.constraint = new BoolExpression(predicate);
/*    */ 
/* 26 */     SimpleExpression simple = (SimpleExpression)this.constraint.getChild(0).getChild(0);
/* 27 */     this.first = new SimpleTermPredicate(new SimpleExpression("TERM", simple.getOperator(), simple.getTestValue()));
/*    */ 
/* 29 */     simple = (SimpleExpression)this.constraint.getChild(1).getChild(0);
/* 30 */     this.second = new SimpleTermPredicate(new SimpleExpression("TERM", simple.getOperator(), simple.getTestValue()));
/*    */   }
/*    */ 
/*    */   public String toSQLExpression() {
/* 34 */     return this.constraint.toSQLExpression();
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 38 */     if (this.weight != 1.0D) {
/* 39 */       return "R(" + String.valueOf(this.weight) + ", " + this.constraint.toString() + ")";
/*    */     }
/* 41 */     return "R(" + this.constraint.toString() + ")";
/*    */   }
/*    */ 
/*    */   public SimpleTermPredicate getFirstTermPredicate()
/*    */   {
/* 46 */     return this.first;
/*    */   }
/*    */ 
/*    */   public SimpleTermPredicate getSecondTermPredicate() {
/* 50 */     return this.second;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.SimpleRelationPredicate
 * JD-Core Version:    0.6.2
 */