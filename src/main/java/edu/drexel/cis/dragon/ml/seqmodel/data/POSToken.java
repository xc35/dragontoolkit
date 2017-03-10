/*    */ package edu.drexel.cis.dragon.ml.seqmodel.data;
/*    */ 
/*    */ public class POSToken extends BasicToken
/*    */ {
/*    */   protected int posTag;
/*    */ 
/*    */   public POSToken(String content)
/*    */   {
/* 16 */     super(content);
/* 17 */     this.posTag = -1;
/*    */   }
/*    */ 
/*    */   public POSToken(String content, int label) {
/* 21 */     super(content, label);
/* 22 */     this.posTag = -1;
/*    */   }
/*    */ 
/*    */   public int getPOSTag() {
/* 26 */     return this.posTag;
/*    */   }
/*    */ 
/*    */   public void setPOSTag(int tag) {
/* 30 */     this.posTag = tag;
/*    */   }
/*    */ 
/*    */   public BasicToken copy()
/*    */   {
/* 36 */     POSToken cur = new POSToken(this.content, this.label);
/* 37 */     cur.setPOSTag(this.posTag);
/* 38 */     cur.setSegmentMarker(this.isSegmentStart);
/* 39 */     cur.setIndex(this.index);
/* 40 */     return cur;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.data.POSToken
 * JD-Core Version:    0.6.2
 */