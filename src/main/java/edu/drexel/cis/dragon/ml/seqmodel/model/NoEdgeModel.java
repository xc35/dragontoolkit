/*    */ package edu.drexel.cis.dragon.ml.seqmodel.model;
/*    */ 
/*    */ public class NoEdgeModel extends AbstractModel
/*    */ {
/*    */   private EmptyEdgeIter emptyIter;
/*    */ 
/*    */   public NoEdgeModel(int nlabels)
/*    */   {
/* 16 */     super(nlabels, "NoEdge");
/* 17 */     this.emptyIter = new EmptyEdgeIter(null);
/*    */   }
/*    */ 
/*    */   public int getEdgeNum() {
/* 21 */     return 0;
/*    */   }
/*    */ 
/*    */   public int getLabel(int state) {
/* 25 */     return state;
/*    */   }
/*    */ 
/*    */   public int getStartStateNum() {
/* 29 */     return this.numLabels;
/*    */   }
/*    */ 
/*    */   public int getEndStateNum() {
/* 33 */     return this.numLabels;
/*    */   }
/*    */ 
/*    */   public int getStartState(int i) {
/* 37 */     if (i < getStartStateNum()) {
/* 38 */       return i;
/*    */     }
/* 40 */     return -1;
/*    */   }
/*    */ 
/*    */   public int getEndState(int i) {
/* 44 */     if (i < getEndStateNum()) {
/* 45 */       return i;
/*    */     }
/* 47 */     return -1;
/*    */   }
/*    */ 
/*    */   public boolean isEndState(int i) {
/* 51 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean isStartState(int i) {
/* 55 */     return true;
/*    */   }
/*    */ 
/*    */   public EdgeIterator getEdgeIterator() {
/* 59 */     return this.emptyIter;
/*    */   }
/*    */   private class EmptyEdgeIter implements EdgeIterator {
/*    */     private EmptyEdgeIter() {
/*    */     }
/*    */     public void start() {
/*    */     }
/* 66 */     public boolean hasNext() { return false; }
/*    */ 
/*    */     public Edge next()
/*    */     {
/* 70 */       return null;
/*    */     }
/*    */ 
/*    */     public boolean nextIsOuter() {
/* 74 */       return false;
/*    */     }
/*    */ 
/*    */     EmptyEdgeIter(EmptyEdgeIter arg2)
/*    */     {
/* 62 */       this();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.model.NoEdgeModel
 * JD-Core Version:    0.6.2
 */