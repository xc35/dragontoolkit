/*    */ package edu.drexel.cis.dragon.ml.seqmodel.data;
/*    */ 
/*    */ public class BasicToken
/*    */ {
/*    */   protected String content;
/*    */   protected int label;
/*    */   protected int index;
/*    */   protected boolean isSegmentStart;
/*    */ 
/*    */   public BasicToken(String content)
/*    */   {
/* 18 */     this(content, -1);
/*    */   }
/*    */ 
/*    */   public BasicToken(String content, int label) {
/* 22 */     this.content = content;
/* 23 */     this.label = label;
/* 24 */     this.isSegmentStart = true;
/* 25 */     this.index = -1;
/*    */   }
/*    */ 
/*    */   public BasicToken copy()
/*    */   {
/* 31 */     BasicToken cur = new BasicToken(this.content, this.label);
/* 32 */     cur.setSegmentMarker(this.isSegmentStart);
/* 33 */     cur.setIndex(this.index);
/* 34 */     return cur;
/*    */   }
/*    */ 
/*    */   public String getContent() {
/* 38 */     return this.content;
/*    */   }
/*    */ 
/*    */   public void setContent(String content) {
/* 42 */     this.content = content;
/*    */   }
/*    */ 
/*    */   public int getLabel() {
/* 46 */     return this.label;
/*    */   }
/*    */ 
/*    */   public void setLabel(int label) {
/* 50 */     this.label = label;
/*    */   }
/*    */ 
/*    */   public int getIndex() {
/* 54 */     return this.index;
/*    */   }
/*    */ 
/*    */   public void setIndex(int index) {
/* 58 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public boolean isSegmentStart() {
/* 62 */     return this.isSegmentStart;
/*    */   }
/*    */ 
/*    */   public void setSegmentMarker(boolean isSegmentStart) {
/* 66 */     this.isSegmentStart = isSegmentStart;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.data.BasicToken
 * JD-Core Version:    0.6.2
 */