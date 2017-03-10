/*    */ package edu.drexel.cis.dragon.qa.filter;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*    */ 
/*    */ public class BasicWebFilter extends AbstractFilter
/*    */ {
/*    */   public boolean keep(QuestionQuery query, Candidate term)
/*    */   {
/*  8 */     if (query.getSubAnswerType() == 1) {
/*  9 */       return isEmail(term);
/*    */     }
/* 11 */     return isUrl(term);
/*    */   }
/*    */ 
/*    */   protected boolean isUrl(Candidate term)
/*    */   {
/* 19 */     int wordNum = term.getWordNum();
/* 20 */     Word cur = term.getStartingWord();
/* 21 */     int i = 0;
/* 22 */     boolean found = false;
/* 23 */     while (i < wordNum) {
/* 24 */       if (cur.getContent().equals("@"))
/* 25 */         return false;
/* 26 */       if (cur.getContent().equals("."))
/* 27 */         found = true;
/* 28 */       i++;
/* 29 */       cur = cur.next;
/*    */     }
/* 31 */     return found;
/*    */   }
/*    */ 
/*    */   protected boolean isEmail(Candidate term)
/*    */   {
/* 38 */     int wordNum = term.getWordNum();
/* 39 */     Word cur = term.getStartingWord();
/* 40 */     int i = 0;
/* 41 */     while ((i < wordNum) && (!cur.getContent().equals("@"))) {
/* 42 */       i++;
/* 43 */       cur = cur.next;
/*    */     }
/* 45 */     return i < wordNum;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.filter.BasicWebFilter
 * JD-Core Version:    0.6.2
 */