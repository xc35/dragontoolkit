/*    */ package edu.drexel.cis.dragon.nlp;
/*    */ 
/*    */ public class Counter
/*    */ {
/*    */   private int count;
/*    */ 
/*    */   public Counter()
/*    */   {
/* 16 */     this.count = 0;
/*    */   }
/*    */   public Counter(int k) {
/* 19 */     this.count = k;
/*    */   }
/*    */ 
/*    */   public void addCount(int k) {
/* 23 */     this.count += k;
/*    */   }
/*    */ 
/*    */   public void setCount(int k) {
/* 27 */     this.count = k;
/*    */   }
/*    */ 
/*    */   public int getCount() {
/* 31 */     return this.count;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.Counter
 * JD-Core Version:    0.6.2
 */