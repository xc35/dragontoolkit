/*    */ package edu.drexel.cis.dragon.nlp;
/*    */ 
/*    */ public class Document
/*    */ {
/*    */   public static final int TITLE = 1;
/*    */   public static final int ABSTRACT = 2;
/*    */   public static final int INTRODUCTION = 3;
/*    */   public static final int BODY = 4;
/*    */   public static final int CONCLUSION = 5;
/*    */   private Paragraph start;
/*    */   private Paragraph end;
/*    */   private int count;
/*    */   private int index;
/*    */ 
/*    */   public Document()
/*    */   {
/* 24 */     this.start = null;
/* 25 */     this.end = null;
/* 26 */     this.count = 0;
/*    */   }
/*    */ 
/*    */   public boolean addParagraph(Paragraph paragraph) {
/* 30 */     if (paragraph == null) {
/* 31 */       return false;
/*    */     }
/* 33 */     paragraph.setParent(this);
/* 34 */     if (this.end != null)
/* 35 */       this.end.next = paragraph;
/* 36 */     if (this.start == null)
/* 37 */       this.start = paragraph;
/* 38 */     paragraph.prev = this.end;
/* 39 */     paragraph.next = null;
/* 40 */     this.end = paragraph;
/* 41 */     this.count += 1;
/* 42 */     return false;
/*    */   }
/*    */ 
/*    */   public Paragraph getFirstParagraph() {
/* 46 */     return this.start;
/*    */   }
/*    */ 
/*    */   public Paragraph getLastParagraph() {
/* 50 */     return this.end;
/*    */   }
/*    */ 
/*    */   public int getParagraphNum() {
/* 54 */     return this.count;
/*    */   }
/*    */ 
/*    */   public int getIndex() {
/* 58 */     return this.index;
/*    */   }
/*    */ 
/*    */   public void setIndex(int index) {
/* 62 */     this.index = index;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.Document
 * JD-Core Version:    0.6.2
 */