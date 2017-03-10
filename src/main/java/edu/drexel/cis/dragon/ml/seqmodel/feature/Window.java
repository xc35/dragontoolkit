/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ public class Window
/*    */ {
/*    */   private int start;
/*    */   private boolean startRelativeToLeft;
/*    */   private int end;
/*    */   private boolean endRelativeToLeft;
/* 17 */   private String winName = null;
/* 18 */   private int maxLength = 2147483647;
/* 19 */   private int minLength = 1;
/*    */ 
/*    */   public Window(int start, boolean startRelativeToLeft, int end, boolean endRelativeToLeft) {
/* 22 */     this(start, startRelativeToLeft, end, endRelativeToLeft, null);
/* 23 */     String startB = startRelativeToLeft ? "L" : "R";
/* 24 */     String endB = endRelativeToLeft ? "L" : "R";
/* 25 */     this.winName = (startB + start + endB + end);
/*    */   }
/*    */ 
/*    */   public Window(int start, boolean startRelativeToLeft, int end, boolean endRelativeToLeft, String winName) {
/* 29 */     this.start = start;
/* 30 */     this.startRelativeToLeft = startRelativeToLeft;
/* 31 */     this.end = end;
/* 32 */     this.endRelativeToLeft = endRelativeToLeft;
/* 33 */     this.winName = winName;
/*    */   }
/*    */ 
/*    */   public Window(int start, boolean startRelativeToLeft, int end, boolean endRelativeToLeft, String winName, int minWinLength, int maxWinLength) {
/* 37 */     this(start, startRelativeToLeft, end, endRelativeToLeft, winName);
/* 38 */     this.maxLength = maxWinLength;
/* 39 */     this.minLength = minWinLength;
/*    */   }
/*    */ 
/*    */   public int leftBoundary(int segStart, int segEnd) {
/* 43 */     if (this.startRelativeToLeft) {
/* 44 */       return boundary(segStart, this.start);
/*    */     }
/* 46 */     return boundary(segEnd, this.start);
/*    */   }
/*    */ 
/*    */   public int rightBoundary(int segStart, int segEnd) {
/* 50 */     if (this.endRelativeToLeft) {
/* 51 */       return boundary(segStart, this.end);
/*    */     }
/* 53 */     return boundary(segEnd, this.end);
/*    */   }
/*    */ 
/*    */   private int boundary(int boundary, int offset) {
/* 57 */     return boundary + offset;
/*    */   }
/*    */ 
/*    */   public int getMinLength() {
/* 61 */     return this.minLength;
/*    */   }
/*    */ 
/*    */   public int getMaxLength() {
/* 65 */     return this.maxLength;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 69 */     return this.winName;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.Window
 * JD-Core Version:    0.6.2
 */