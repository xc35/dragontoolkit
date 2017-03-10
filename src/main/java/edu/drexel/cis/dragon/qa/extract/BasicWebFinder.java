/*     */ package edu.drexel.cis.dragon.qa.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.qa.query.QueryWord;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.score.CandidateScorer;
/*     */ import edu.drexel.cis.dragon.qa.score.SimilarityScorer;
/*     */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*     */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class BasicWebFinder extends AbstractCandidateFinder
/*     */ {
/*     */   private SimpleDictionary funcDict;
/*     */ 
/*     */   public BasicWebFinder(SimpleDictionary funcDict)
/*     */   {
/*  17 */     this.funcDict = funcDict;
/*  18 */     this.scorer = new SimilarityScorer(true);
/*     */   }
/*     */ 
/*     */   public ArrayList extract(Sentence sent, QuestionQuery query)
/*     */   {
/*  28 */     ArrayList list = null;
/*  29 */     if (sent.getWordNum() > 255) {
/*  30 */       return list;
/*     */     }
/*  32 */     QueryWord[] arrQWord = new QueryWord[sent.getWordNum()];
/*  33 */     Word[] arrWord = new Word[sent.getWordNum()];
/*  34 */     Word cur = sent.getFirstWord();
/*  35 */     int i = 0;
/*  36 */     while (cur != null) {
/*  37 */       arrWord[i] = cur;
/*  38 */       cur = cur.next;
/*  39 */       i++;
/*     */     }
/*     */ 
/*  43 */     markQueryWord(arrWord, arrQWord, query, this.funcDict);
/*  44 */     if (this.scorer.initialize(query, sent, arrQWord) < this.sentThreshold) {
/*  45 */       return null;
/*     */     }
/*  47 */     list = new ArrayList();
/*     */ 
/*  50 */     i = 0;
/*  51 */     while (i < arrWord.length)
/*  52 */       if (!isStartingWord(arrWord[i])) {
/*  53 */         i++;
/*     */       }
/*     */       else
/*     */       {
/*  58 */         int lastEndPos = -1;
/*  59 */         if (isEndingWord(arrWord[i])) {
/*  60 */           lastEndPos = i;
/*     */         }
/*  62 */         int j = i + 1;
/*  63 */         while (j < arrWord.length)
/*     */         {
/*  65 */           if (isBoundaryWord(arrWord[j]))
/*     */             break;
/*  67 */           if (isEndingWord(arrWord[j]))
/*  68 */             lastEndPos = j;
/*  69 */           j++;
/*     */         }
/*     */ 
/*  73 */         if ((lastEndPos < 0) || (lastEndPos - i < 2)) {
/*  74 */           i = j + 1;
/*     */         }
/*     */         else
/*     */         {
/*  79 */           Candidate curCand = new Candidate(arrWord[i], arrWord[lastEndPos]);
/*  80 */           this.scorer.score(curCand);
/*     */ 
/*  83 */           if (list == null)
/*  84 */             list = new ArrayList();
/*  85 */           list.add(curCand);
/*  86 */           i = j + 1;
/*     */         }
/*     */       }
/*  88 */     return list;
/*     */   }
/*     */ 
/*     */   protected boolean isStartingWord(Word cur) {
/*  92 */     return !cur.isPunctuation();
/*     */   }
/*     */ 
/*     */   protected boolean isEndingWord(Word cur) {
/*  96 */     return !cur.isPunctuation();
/*     */   }
/*     */ 
/*     */   protected boolean isBoundaryWord(Word curWord) {
/* 100 */     if (curWord.isPunctuation()) {
/* 101 */       if (".-@/~_".indexOf(curWord.getContent()) >= 0)
/* 102 */         return false;
/* 103 */       return true;
/*     */     }
/*     */ 
/* 106 */     if (curWord.prev.isPunctuation()) {
/* 107 */       return false;
/*     */     }
/* 109 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.extract.BasicWebFinder
 * JD-Core Version:    0.6.2
 */