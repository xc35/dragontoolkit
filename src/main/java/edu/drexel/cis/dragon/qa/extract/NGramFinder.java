/*    */ package edu.drexel.cis.dragon.qa.extract;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.qa.query.QueryWord;
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.score.CandidateScorer;
/*    */ import edu.drexel.cis.dragon.qa.score.SimilarityScorer;
/*    */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*    */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class NGramFinder extends AbstractCandidateFinder
/*    */ {
/*    */   private SimpleDictionary funcDict;
/*    */   private int grams;
/*    */ 
/*    */   public NGramFinder(SimpleDictionary funcDict)
/*    */   {
/* 18 */     this.funcDict = funcDict;
/* 19 */     this.grams = 4;
/* 20 */     this.scorer = new SimilarityScorer();
/*    */   }
/*    */ 
/*    */   public ArrayList extract(Sentence sent, QuestionQuery query)
/*    */   {
/* 30 */     ArrayList list = null;
/* 31 */     if (sent.getWordNum() > 255) {
/* 32 */       return list;
/*    */     }
/* 34 */     QueryWord[] arrQWord = new QueryWord[sent.getWordNum()];
/* 35 */     Word[] arrWord = new Word[sent.getWordNum()];
/* 36 */     Word cur = sent.getFirstWord();
/* 37 */     int i = 0;
/* 38 */     while (cur != null) {
/* 39 */       arrWord[i] = cur;
/* 40 */       cur = cur.next;
/* 41 */       i++;
/*    */     }
/*    */ 
/* 45 */     markQueryWord(arrWord, arrQWord, query, this.funcDict);
/* 46 */     if (this.scorer.initialize(query, sent, arrQWord) < this.sentThreshold) {
/* 47 */       return null;
/*    */     }
/*    */ 
/* 50 */     int[] arrCase = new int[arrWord.length];
/* 51 */     for (i = 0; i < arrCase.length; i++) {
/* 52 */       arrCase[i] = getCaseInfo(arrWord[i]);
/*    */     }
/* 54 */     list = new ArrayList();
/* 55 */     for (i = 0; i < arrWord.length; i++)
/* 56 */       if ((arrQWord[i] == null) || (!arrQWord[i].isFunctionalWord()))
/*    */       {
/* 58 */         int caseInfo = arrCase[i];
/* 59 */         for (int j = 0; (j < this.grams) && (j < arrWord.length - i); j++) {
/* 60 */           int curCase = arrCase[(i + j)];
/* 61 */           if (curCase >= 0) {
/* 62 */             if (caseInfo == -1)
/* 63 */               caseInfo = curCase;
/* 64 */             else if (caseInfo != curCase) {
/*    */                 break;
/*    */               }
/*    */           }
/* 68 */           if ((arrQWord[(i + j)] == null) || ((!arrQWord[(i + j)].isFunctionalWord()) && (arrQWord[i] == null)))
/*    */           {
/* 70 */             if ((i + j + 1 >= arrWord.length) || (!arrWord[(i + j + 1)].getContent().equalsIgnoreCase("of")))
/*    */             {
/* 72 */               Candidate curCand = new Candidate(arrWord[i], arrWord[(i + j)]);
/* 73 */               this.scorer.score(curCand);
/* 74 */               list.add(curCand);
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/* 77 */     return list;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.extract.NGramFinder
 * JD-Core Version:    0.6.2
 */