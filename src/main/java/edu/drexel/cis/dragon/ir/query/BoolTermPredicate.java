/*    */ package edu.drexel.cis.dragon.ir.query;
/*    */ 
/*    */ public class BoolTermPredicate extends AbstractPredicate
/*    */ {
/*    */   public BoolTermPredicate(String[] predicate)
/*    */   {
/* 14 */     parse(predicate);
/*    */   }
/*    */ 
/*    */   protected void parse(String[] predicate) {
/* 18 */     this.weight = 0.3D;
/* 19 */     this.predicateType = 1;
/* 20 */     this.expressionType = 2;
/* 21 */     this.constraint = new BoolExpression(predicate);
/*    */   }
/*    */ 
/*    */   public String toSQLExpression() {
/* 25 */     return this.constraint.toSQLExpression();
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 29 */     return "T(" + this.constraint.toString() + ")";
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.BoolTermPredicate
 * JD-Core Version:    0.6.2
 */