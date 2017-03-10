/*    */ package edu.drexel.cis.dragon.qa.merge;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Token;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ 
/*    */ public class NameMerger
/*    */   implements CandidateMerger
/*    */ {
/*    */   public ArrayList merge(QuestionQuery query, ArrayList list)
/*    */   {
/* 22 */     SortedArray tokenList = new SortedArray();
/* 23 */     for (int i = 0; i < list.size(); i++) {
/* 24 */       Candidate curTerm = (Candidate)list.get(i);
/*    */       String val;
/* 25 */       if (curTerm.getWordNum() == 1)
/* 26 */         val = curTerm.getStartingWord().getContent();
/*    */       else {
/* 28 */         val = curTerm.getStartingWord().getContent() + " " + curTerm.getEndingWord().getContent();
/*    */       }
/* 30 */       Token curToken = new Token(val);
/* 31 */       curToken.setMemo(curTerm);
/* 32 */       if (!tokenList.add(curToken)) {
/* 33 */         Token oldToken = (Token)tokenList.get(tokenList.insertedPos());
/* 34 */         Candidate oldTerm = (Candidate)oldToken.getMemo();
/* 35 */         if (curTerm.getWordNum() > oldTerm.getWordNum()) {
/* 36 */           curTerm.merge(oldTerm);
/* 37 */           oldToken.setMemo(curTerm);
/*    */         }
/*    */         else {
/* 40 */           oldTerm.merge(curTerm);
/*    */         }
/*    */       }
/*    */     }
/*    */ 
/* 45 */     ArrayList newList = new ArrayList(tokenList.size());
/* 46 */     for (int i = 0; i < tokenList.size(); i++)
/* 47 */       newList.add(((Token)tokenList.get(i)).getMemo());
/* 48 */     Collections.sort(newList, new WeightComparator(true));
/* 49 */     return newList;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.merge.NameMerger
 * JD-Core Version:    0.6.2
 */