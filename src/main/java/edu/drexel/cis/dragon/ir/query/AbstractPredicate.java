/*    */ package edu.drexel.cis.dragon.ir.query;
/*    */ 
/*    */ public abstract class AbstractPredicate extends AbstractIRQuery
/*    */   implements Predicate
/*    */ {
/*    */   protected double weight;
/*    */   protected int predicateType;
/*    */   protected int expressionType;
/*    */   protected Expression constraint;
/*    */ 
/*    */   public AbstractPredicate()
/*    */   {
/* 17 */     this.weight = 1.0D;
/* 18 */     this.predicateType = 0;
/* 19 */     this.expressionType = 0;
/* 20 */     this.constraint = null;
/*    */   }
/*    */ 
/*    */   public boolean parse(String predicate) {
/* 24 */     parse(getTokenList(predicate));
/* 25 */     return true;
/*    */   }
/*    */ 
/*    */   public Operator getOperator() {
/* 29 */     return this.constraint.getOperator();
/*    */   }
/*    */ 
/*    */   public boolean isPredicate() {
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean isCompoundQuery() {
/* 37 */     return false;
/*    */   }
/*    */ 
/*    */   public IRQuery getChild(int index) {
/* 41 */     return null;
/*    */   }
/*    */ 
/*    */   public int getChildNum() {
/* 45 */     return 0;
/*    */   }
/*    */ 
/*    */   public double getSelectivity() {
/* 49 */     return 0.0D;
/*    */   }
/*    */ 
/*    */   public double getWeight() {
/* 53 */     return this.weight;
/*    */   }
/*    */ 
/*    */   public void setWeight(double weight) {
/* 57 */     this.weight = weight;
/*    */   }
/*    */ 
/*    */   public Expression getConstraint() {
/* 61 */     return this.constraint;
/*    */   }
/*    */ 
/*    */   public boolean isSimplePredicate() {
/* 65 */     return this.expressionType == 1;
/*    */   }
/*    */ 
/*    */   public boolean isBoolPredicate() {
/* 69 */     return this.expressionType == 2;
/*    */   }
/*    */ 
/*    */   public boolean isTermPredicate() {
/* 73 */     return this.predicateType == 1;
/*    */   }
/*    */ 
/*    */   public boolean isRelationPredicate() {
/* 77 */     return this.predicateType == 2;
/*    */   }
/*    */ 
/*    */   public boolean isQualifierPredicate() {
/* 81 */     return this.predicateType == 3;
/*    */   }
/*    */ 
/*    */   protected abstract void parse(String[] paramArrayOfString);
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.AbstractPredicate
 * JD-Core Version:    0.6.2
 */