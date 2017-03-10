/*    */ package edu.drexel.cis.dragon.ir.query;
/*    */ 
/*    */ public class Operator
/*    */ {
/*    */   private static final String optrs = " AND OR = LIKE ";
/*    */   String optr;
/*    */ 
/*    */   public Operator(String optr)
/*    */   {
/* 17 */     if (isOperator(optr))
/* 18 */       this.optr = optr.toUpperCase();
/*    */     else
/* 20 */       optr = null;
/*    */   }
/*    */ 
/*    */   public boolean test(Object firstValue, Object secondValue) {
/* 24 */     return false;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 28 */     return this.optr;
/*    */   }
/*    */ 
/*    */   public boolean isOperator(String optr) {
/* 32 */     return " AND OR = LIKE ".indexOf(" " + optr.toUpperCase() + " ") >= 0;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.Operator
 * JD-Core Version:    0.6.2
 */