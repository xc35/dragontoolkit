/*    */ package edu.drexel.cis.dragon.ir.query;
/*    */ 
/*    */ public abstract class AbstractExpression
/*    */   implements Expression
/*    */ {
/*    */   protected Operator optr;
/*    */   protected int expressionType;
/*    */ 
/*    */   public AbstractExpression()
/*    */   {
/* 17 */     this.optr = null;
/*    */   }
/*    */ 
/*    */   public Operator getOperator() {
/* 21 */     return this.optr;
/*    */   }
/*    */ 
/*    */   public boolean isBoolExpression() {
/* 25 */     return this.expressionType == 1;
/*    */   }
/*    */ 
/*    */   public boolean isSimpleExpression() {
/* 29 */     return this.expressionType == 2;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.AbstractExpression
 * JD-Core Version:    0.6.2
 */