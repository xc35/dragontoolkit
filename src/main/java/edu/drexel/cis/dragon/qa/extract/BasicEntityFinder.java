/*     */ package edu.drexel.cis.dragon.qa.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import edu.drexel.cis.dragon.qa.query.QueryWord;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.score.CandidateScorer;
/*     */ import edu.drexel.cis.dragon.qa.score.SimilarityScorer;
/*     */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*     */ import edu.drexel.cis.dragon.qa.util.QuestionSentence;
/*     */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class BasicEntityFinder extends AbstractCandidateFinder
/*     */ {
/*     */   private Tagger tagger;
/*     */   private SimpleDictionary funcDict;
/*     */   private SimpleDictionary allowedFuncDict;
/*     */ 
/*     */   public BasicEntityFinder(Tagger tagger, SimpleDictionary funcDict, SimpleDictionary allowedFuncWord)
/*     */   {
/*  27 */     this.tagger = tagger;
/*  28 */     this.allowedFuncDict = allowedFuncWord;
/*  29 */     this.funcDict = funcDict;
/*  30 */     this.scorer = new SimilarityScorer();
/*     */   }
/*     */ 
/*     */   public ArrayList extract(Sentence sent, QuestionQuery query)
/*     */   {
/*  40 */     ArrayList list = null;
/*  41 */     if (sent.getWordNum() > 255) {
/*  42 */       return list;
/*     */     }
/*  44 */     QueryWord[] arrQWord = new QueryWord[sent.getWordNum()];
/*  45 */     Word[] arrWord = new Word[sent.getWordNum()];
/*  46 */     Word cur = sent.getFirstWord();
/*  47 */     int i = 0;
/*  48 */     while (cur != null) {
/*  49 */       arrWord[i] = cur;
/*  50 */       cur = cur.next;
/*  51 */       i++;
/*     */     }
/*     */ 
/*  55 */     markQueryWord(arrWord, arrQWord, query, this.funcDict);
/*  56 */     if (this.scorer.initialize(query, sent, arrQWord) < this.sentThreshold) {
/*  57 */       return null;
/*     */     }
/*  59 */     list = new ArrayList();
/*     */ 
/*  62 */     this.tagger.tag(sent);
/*  63 */     for (i = 0; i < arrQWord.length; i++) {
/*  64 */       if ((arrQWord[i] != null) && (arrQWord[i].getIndex() < 0)) {
/*  65 */         arrQWord[i].setPOSTag(arrWord[i].getPOSIndex());
/*  66 */         if ((arrWord[i].getPOSIndex() == 2) && (QuestionSentence.isVerbBe(arrWord[i].getContent()))) {
/*  67 */           arrQWord[i].setVerbBeFlag(true);
/*     */         }
/*     */       }
/*     */     }
/*  71 */     int[] arrCase = new int[arrWord.length];
/*  72 */     for (i = 0; i < arrCase.length; i++) {
/*  73 */       arrCase[i] = getCaseInfo(arrWord[i]);
/*     */     }
/*     */ 
/*  76 */     i = 0;
/*  77 */     while (i < arrWord.length)
/*  78 */       if (!isStartingWord(arrWord[i])) {
/*  79 */         i++;
/*     */       }
/*     */       else
/*     */       {
/*  83 */         int lastEndPos = -1;
/*  84 */         if (isEndingWord(arrWord[i]))
/*  85 */           lastEndPos = i;
/*  86 */         int caseInfo = arrCase[i];
/*     */ 
/*  88 */         int j = i + 1;
/*  89 */         while (j < arrWord.length) {
/*  90 */           int curCase = arrCase[j];
/*  91 */           if (curCase >= 0) {
/*  92 */             if (caseInfo == -1) {
/*  93 */               caseInfo = curCase;
/*  94 */             } else if (caseInfo != curCase) {
/*  95 */               j--;
/*  96 */               break;
/*     */             }
/*     */           }
/*     */ 
/* 100 */           if (isBoundaryWord(arrWord[j]))
/*     */             break;
/* 102 */           if (isEndingWord(arrWord[j]))
/* 103 */             lastEndPos = j;
/* 104 */           j++;
/*     */         }
/*     */ 
/* 107 */         if ((lastEndPos < 0) || ((i == lastEndPos) && (arrWord[i].getPOSIndex() != 1))) {
/* 108 */           i = j + 1;
/*     */         }
/* 114 */         else if ((arrQWord[i] != null) && (arrQWord[lastEndPos] != null)) {
/* 115 */           i = j + 1;
/*     */         }
/* 119 */         else if ((lastEndPos + 1 < arrWord.length) && (arrWord[(lastEndPos + 1)].getContent().equals("of"))) {
/* 120 */           i = j + 1;
/*     */         }
/*     */         else {
/* 123 */           if (lastEndPos - i >= 2)
/*     */           {
/* 125 */             if ((arrQWord[i] != null) && (arrQWord[(i + 1)] != null) && (!arrQWord[(i + 1)].isFunctionalWord())) {
/* 126 */               i += 2;
/* 127 */               continue;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 132 */           Candidate curCand = new Candidate(arrWord[i], arrWord[lastEndPos]);
/* 133 */           if (i == lastEndPos)
/* 134 */             curCand.setCapitalFrequency(arrWord[i].isInitialCapital() ? 1 : 0);
/*     */           else
/* 136 */             curCand.setCapitalFrequency(caseInfo > 0 ? 1 : 0);
/* 137 */           this.scorer.score(curCand);
/*     */ 
/* 140 */           if (list == null)
/* 141 */             list = new ArrayList();
/* 142 */           list.add(curCand);
/* 143 */           i = j + 1;
/*     */         }
/*     */       }
/* 145 */     return list;
/*     */   }
/*     */ 
/*     */   protected boolean isStartingWord(Word cur)
/*     */   {
/* 151 */     int pos = cur.getPOSIndex();
/*     */ 
/* 153 */     return ((cur.isInitialCapital()) && (pos >= 1) && (pos <= 4)) || 
/* 153 */       (pos == 1) || (pos == 3) || (pos == 9);
/*     */   }
/*     */ 
/*     */   protected boolean isEndingWord(Word cur)
/*     */   {
/* 159 */     int pos = cur.getPOSIndex();
/* 160 */     return ((cur.isInitialCapital()) && (pos >= 1) && (pos <= 4)) || (pos == 1) || (pos == 9);
/*     */   }
/*     */ 
/*     */   protected boolean isBoundaryWord(Word curWord)
/*     */   {
/* 166 */     int posIndex = curWord.getPOSIndex();
/* 167 */     if (curWord.isInitialCapital()) {
/* 168 */       if ((posIndex != 5) || (this.allowedFuncDict.exist(curWord.getContent())))
/* 169 */         return false;
/*     */     } else {
/* 171 */       if ((posIndex == 1) || (posIndex == 3) || (posIndex == 9))
/* 172 */         return false;
/* 173 */       if (((curWord.prev.getPOSIndex() == 5) || (curWord.prev.isInitialCapital())) && (this.allowedFuncDict.exist(curWord.getContent())))
/* 174 */         return false; 
/*     */     }
/* 175 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.extract.BasicEntityFinder
 * JD-Core Version:    0.6.2
 */