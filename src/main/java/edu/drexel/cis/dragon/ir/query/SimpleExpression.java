/*    */ package edu.drexel.cis.dragon.ir.query;
/*    */ 
/*    */ public class SimpleExpression extends AbstractExpression
/*    */ {
/*    */   private String field;
/*    */   private Object testValue;
/*    */ 
/*    */   public SimpleExpression(String field, Operator optr, Object testValue)
/*    */   {
/* 17 */     this.field = field;
/* 18 */     this.optr = optr;
/* 19 */     this.testValue = testValue;
/* 20 */     this.expressionType = 2;
/*    */   }
/*    */ 
/*    */   public SimpleExpression(String[] expression)
/*    */   {
/* 27 */     StringBuffer testValue = new StringBuffer(expression[2]);
/* 28 */     for (int k = 3; k < expression.length; k++) {
/* 29 */       testValue.append(' ');
/* 30 */       testValue.append(expression[k]);
/*    */     }
/*    */ 
/* 33 */     this.field = expression[0];
/* 34 */     this.optr = new Operator(expression[1]);
/* 35 */     this.testValue = testValue.toString();
/* 36 */     this.expressionType = 2;
/*    */   }
/*    */ 
/*    */   public int getChildNum() {
/* 40 */     return 0;
/*    */   }
/*    */ 
/*    */   public Expression getChild(int index) {
/* 44 */     return null;
/*    */   }
/*    */ 
/*    */   public String toSQLExpression()
/*    */   {
/* 50 */     StringBuffer sb = new StringBuffer(this.field);
/* 51 */     sb.append(this.optr.toString());
/* 52 */     sb.append("'");
/* 53 */     sb.append(this.testValue.toString());
/* 54 */     sb.append("'");
/* 55 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 61 */     StringBuffer sb = new StringBuffer(this.field);
/* 62 */     sb.append(this.optr.toString());
/* 63 */     sb.append(this.testValue.toString());
/* 64 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   public String getField() {
/* 68 */     return this.field;
/*    */   }
/*    */ 
/*    */   public Object getTestValue() {
/* 72 */     return this.testValue;
/*    */   }
/*    */ 
/*    */   public Operator getOperator() {
/* 76 */     return this.optr;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.SimpleExpression
 * JD-Core Version:    0.6.2
 */