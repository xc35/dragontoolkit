/*    */ package edu.drexel.cis.dragon.ir.query;
/*    */ 
/*    */ public class BoolRelationPredicate extends AbstractPredicate
/*    */ {
/*    */   public BoolRelationPredicate(String[] predicate)
/*    */   {
/* 14 */     parse(predicate);
/*    */   }
/*    */ 
/*    */   protected void parse(String[] predicate) {
/* 18 */     this.weight = 1.0D;
/* 19 */     this.predicateType = 2;
/* 20 */     this.expressionType = 2;
/* 21 */     this.constraint = new BoolExpression(predicate);
/*    */   }
/*    */ 
/*    */   public String toSQLExpression() {
/* 25 */     return this.constraint.toSQLExpression();
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 29 */     return "R(" + this.constraint.toString() + ")";
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.BoolRelationPredicate
 * JD-Core Version:    0.6.2
 */