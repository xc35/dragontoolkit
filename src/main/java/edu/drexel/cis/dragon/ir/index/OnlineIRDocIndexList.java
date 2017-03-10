/*    */ package edu.drexel.cis.dragon.ir.index;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class OnlineIRDocIndexList
/*    */   implements IRDocIndexList
/*    */ {
/*    */   private ArrayList indexList;
/*    */   private int lastDocIndex;
/*    */ 
/*    */   public OnlineIRDocIndexList()
/*    */   {
/* 19 */     this.indexList = new ArrayList(200);
/* 20 */     this.lastDocIndex = -1;
/*    */   }
/*    */ 
/*    */   public IRDoc get(int index) {
/* 24 */     return index < this.indexList.size() ? (IRDoc)this.indexList.get(index) : null;
/*    */   }
/*    */ 
/*    */   public boolean add(IRDoc curDoc)
/*    */   {
/* 30 */     if (curDoc.getIndex() <= this.lastDocIndex)
/* 31 */       return false;
/* 32 */     for (int i = this.lastDocIndex + 1; i < curDoc.getIndex(); i++) {
/* 33 */       this.indexList.add(new IRDoc(i));
/*    */     }
/* 35 */     this.indexList.add(curDoc.copy());
/* 36 */     this.lastDocIndex = curDoc.getIndex();
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */   public void close() {
/* 41 */     this.indexList.clear();
/*    */   }
/*    */ 
/*    */   public int size() {
/* 45 */     return this.indexList.size();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.OnlineIRDocIndexList
 * JD-Core Version:    0.6.2
 */