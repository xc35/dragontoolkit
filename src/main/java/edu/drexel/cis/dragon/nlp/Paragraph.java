/*    */ package edu.drexel.cis.dragon.nlp;
/*    */ 
/*    */ public class Paragraph
/*    */ {
/*    */   public Paragraph next;
/*    */   public Paragraph prev;
/*    */   private Document parent;
/*    */   private Sentence start;
/*    */   private Sentence end;
/*    */   private int role;
/*    */   private int count;
/*    */   private int index;
/*    */ 
/*    */   public Paragraph()
/*    */   {
/* 22 */     this.next = null;
/* 23 */     this.prev = null;
/* 24 */     this.start = null;
/* 25 */     this.end = null;
/* 26 */     this.parent = null;
/* 27 */     this.role = 0;
/* 28 */     this.count = 0;
/*    */   }
/*    */ 
/*    */   public int getIndex() {
/* 32 */     return this.index;
/*    */   }
/*    */ 
/*    */   public void setIndex(int index) {
/* 36 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public Document getParent() {
/* 40 */     return this.parent;
/*    */   }
/*    */ 
/*    */   public void setParent(Document parent) {
/* 44 */     this.parent = parent;
/*    */   }
/*    */ 
/*    */   public boolean addSentence(Sentence sent)
/*    */   {
/* 49 */     if (sent == null) {
/* 50 */       return false;
/*    */     }
/* 52 */     sent.setParent(this);
/* 53 */     if (this.end != null)
/* 54 */       this.end.next = sent;
/* 55 */     if (this.start == null)
/* 56 */       this.start = sent;
/* 57 */     sent.prev = this.end;
/* 58 */     sent.next = null;
/* 59 */     this.end = sent;
/* 60 */     this.count += 1;
/* 61 */     return true;
/*    */   }
/*    */ 
/*    */   public Sentence getFirstSentence() {
/* 65 */     return this.start;
/*    */   }
/*    */ 
/*    */   public Sentence getLastSentence() {
/* 69 */     return this.end;
/*    */   }
/*    */ 
/*    */   public int getRoleInDocument() {
/* 73 */     return this.role;
/*    */   }
/*    */ 
/*    */   public void setRoleInDocument(int role) {
/* 77 */     this.role = role;
/*    */   }
/*    */ 
/*    */   public int getSentenceNum() {
/* 81 */     return this.count;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.Paragraph
 * JD-Core Version:    0.6.2
 */