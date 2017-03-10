/*    */ package edu.drexel.cis.dragon.ir.index;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class OnlineIRRelationIndexList
/*    */   implements IRRelationIndexList, IRSignatureIndexList
/*    */ {
/*    */   private ArrayList indexList;
/*    */ 
/*    */   public OnlineIRRelationIndexList()
/*    */   {
/* 18 */     this.indexList = new ArrayList(5000);
/*    */   }
/*    */ 
/*    */   public IRSignature getIRSignature(int index) {
/* 22 */     return get(index);
/*    */   }
/*    */ 
/*    */   public IRRelation get(int index) {
/* 26 */     return index < this.indexList.size() ? (IRRelation)this.indexList.get(index) : null;
/*    */   }
/*    */ 
/*    */   public boolean add(IRRelation curRelation)
/*    */   {
/* 32 */     if (curRelation.getIndex() < this.indexList.size()) {
/* 33 */       IRRelation oldRelation = (IRRelation)this.indexList.get(curRelation.getIndex());
/* 34 */       oldRelation.addFrequency(curRelation.getFrequency());
/* 35 */       oldRelation.setDocFrequency(oldRelation.getDocFrequency() + curRelation.getDocFrequency());
/*    */     }
/*    */     else {
/* 38 */       for (int i = this.indexList.size(); i < curRelation.getIndex(); i++) {
/* 39 */         this.indexList.add(new IRRelation(i, 0, 0, -1, -1));
/*    */       }
/* 41 */       curRelation = curRelation.copy();
/* 42 */       this.indexList.add(curRelation);
/*    */     }
/* 44 */     return true;
/*    */   }
/*    */ 
/*    */   public int size() {
/* 48 */     return this.indexList.size();
/*    */   }
/*    */ 
/*    */   public void close() {
/* 52 */     this.indexList.clear();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.OnlineIRRelationIndexList
 * JD-Core Version:    0.6.2
 */