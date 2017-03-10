/*    */ package edu.drexel.cis.dragon.nlp.extract;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ 
/*    */ public class CoReference
/*    */ {
/*    */   public int execute(Sentence sent)
/*    */   {
/* 24 */     int count = 0;
/* 25 */     Word cur = sent.getFirstWord();
/* 26 */     while (cur != null)
/*    */     {
/* 28 */       boolean flag = false;
/* 29 */       if ((cur.getPOSIndex() == 6) || (cur.getContent().equalsIgnoreCase("such"))) {
/* 30 */         String word = cur.getContent();
/* 31 */         if (word.equalsIgnoreCase("it"))
/* 32 */           flag = solveIt(cur, sent);
/* 33 */         else if (word.equalsIgnoreCase("such"))
/* 34 */           flag = solveIt(cur, sent);
/*    */       }
/* 36 */       if (flag) count++;
/* 37 */       cur = cur.next;
/*    */     }
/* 39 */     return count;
/*    */   }
/*    */ 
/*    */   private boolean solveIt(Word target, Sentence sent)
/*    */   {
/* 44 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.CoReference
 * JD-Core Version:    0.6.2
 */