/*    */ package edu.drexel.cis.dragon.ir.index.sentence;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.OnlineIndexWriteController;
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ 
/*    */ public class OnlineSentenceWriteController extends OnlineIndexWriteController
/*    */ {
/*    */   private OnlineSentenceBase sentBase;
/*    */ 
/*    */   public OnlineSentenceWriteController(boolean relationSupported, boolean indexConceptEntry)
/*    */   {
/* 18 */     super(relationSupported, indexConceptEntry);
/* 19 */     this.sentBase = new OnlineSentenceBase();
/*    */   }
/*    */ 
/*    */   public boolean addRawSentence(Sentence sent) {
/* 23 */     if (this.curDocKey == null)
/* 24 */       return false;
/* 25 */     return this.sentBase.add(sent.toString(), this.curDocKey);
/*    */   }
/*    */ 
/*    */   public OnlineSentenceBase getSentenceBase() {
/* 29 */     return this.sentBase;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sentence.OnlineSentenceWriteController
 * JD-Core Version:    0.6.2
 */