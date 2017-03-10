/*    */ package edu.drexel.cis.dragon.ir.index;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class OnlineIRTermIndexList
/*    */   implements IRTermIndexList, IRSignatureIndexList
/*    */ {
/*    */   private ArrayList indexList;
/*    */ 
/*    */   public OnlineIRTermIndexList()
/*    */   {
/* 17 */     this.indexList = new ArrayList(2000);
/*    */   }
/*    */ 
/*    */   public IRSignature getIRSignature(int index) {
/* 21 */     return get(index);
/*    */   }
/*    */ 
/*    */   public IRTerm get(int index) {
/* 25 */     return index < this.indexList.size() ? (IRTerm)this.indexList.get(index) : null;
/*    */   }
/*    */ 
/*    */   public boolean add(IRTerm curTerm)
/*    */   {
/* 31 */     if (curTerm.getIndex() < this.indexList.size()) {
/* 32 */       IRTerm oldTerm = (IRTerm)this.indexList.get(curTerm.getIndex());
/* 33 */       oldTerm.addFrequency(curTerm.getFrequency());
/* 34 */       oldTerm.setDocFrequency(oldTerm.getDocFrequency() + curTerm.getDocFrequency());
/*    */     }
/*    */     else
/*    */     {
/* 38 */       for (int i = this.indexList.size(); i < curTerm.getIndex(); i++) {
/* 39 */         this.indexList.add(new IRTerm(i, 0, 0));
/*    */       }
/* 41 */       curTerm = curTerm.copy();
/* 42 */       this.indexList.add(curTerm);
/*    */     }
/* 44 */     return true;
/*    */   }
/*    */ 
/*    */   public void close() {
/* 48 */     this.indexList.clear();
/*    */   }
/*    */ 
/*    */   public int size() {
/* 52 */     return this.indexList.size();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.OnlineIRTermIndexList
 * JD-Core Version:    0.6.2
 */