/*     */ package edu.drexel.cis.dragon.qa.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Document;
/*     */ import edu.drexel.cis.dragon.nlp.Paragraph;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.qa.query.QueryWord;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.score.CandidateScorer;
/*     */ import edu.drexel.cis.dragon.qa.score.FrequencyScorer;
/*     */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public abstract class AbstractCandidateFinder
/*     */   implements CandidateFinder
/*     */ {
/*     */   protected double sentThreshold;
/*     */   protected CandidateScorer scorer;
/*     */ 
/*     */   public AbstractCandidateFinder()
/*     */   {
/*  17 */     this.sentThreshold = 0.5D;
/*  18 */     this.scorer = new FrequencyScorer();
/*     */   }
/*     */ 
/*     */   public void setCandidateScorer(CandidateScorer scorer) {
/*  22 */     this.scorer = scorer;
/*     */   }
/*     */ 
/*     */   public CandidateScorer getCandidateScorer() {
/*  26 */     return this.scorer;
/*     */   }
/*     */ 
/*     */   public void setMinSentenceScore(double threshold) {
/*  30 */     this.sentThreshold = threshold;
/*     */   }
/*     */ 
/*     */   public double getMinSentenceScore() {
/*  34 */     return this.sentThreshold;
/*     */   }
/*     */ 
/*     */   public void initDocument(Document doc, QuestionQuery query)
/*     */   {
/*     */   }
/*     */ 
/*     */   public ArrayList extract(Document doc, QuestionQuery query)
/*     */   {
/*     */     try
/*     */     {
/*  46 */       ArrayList list = new ArrayList();
/*  47 */       if (doc == null)
/*  48 */         return list;
/*  49 */       Paragraph pg = doc.getFirstParagraph();
/*  50 */       while (pg != null) {
/*  51 */         Sentence sent = pg.getFirstSentence();
/*  52 */         while (sent != null)
/*     */         {
/*  54 */           if (sent.getWordNum() > 2) {
/*  55 */             ArrayList curTermList = extract(sent, query);
/*  56 */             if ((curTermList != null) && (curTermList.size() > 0)) {
/*  57 */               list.addAll(curTermList);
/*  58 */               curTermList.clear();
/*     */             }
/*     */           }
/*  61 */           sent = sent.next;
/*     */         }
/*  63 */         pg = pg.next;
/*     */       }
/*  65 */       return list;
/*     */     }
/*     */     catch (Exception e) {
/*  68 */       e.printStackTrace();
/*  69 */     }return null;
/*     */   }
/*     */ 
/*     */   protected void markQueryWord(Word[] arrWord, QueryWord[] arrQWord, QuestionQuery query)
/*     */   {
/*  74 */     markQueryWord(arrWord, arrQWord, query, null);
/*     */   }
/*     */ 
/*     */   protected void markQueryWord(Word[] arrWord, QueryWord[] arrQWord, QuestionQuery query, SimpleDictionary funcDict)
/*     */   {
/*  82 */     for (int i = 0; i < query.size(); i++) {
/*  83 */       QueryWord qWord = query.getQueryWord(i);
/*  84 */       qWord.setSentenceCount(0);
/*  85 */       String lemma = needLemmaComparison(qWord);
/*  86 */       String qcontent = qWord.getContent();
/*  87 */       for (int j = 0; j < arrWord.length; j++) {
/*  88 */         if (arrQWord[j] == null)
/*     */         {
/*  90 */           String content = arrWord[j].getContent();
/*  91 */           if (equalToQueryWord(qcontent, lemma, qWord.getPOSTag(), content)) {
/*  92 */             arrQWord[j] = qWord;
/*  93 */             qWord.addFrequency(1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  98 */     if (funcDict == null)
/*  99 */       return;
/* 100 */     for (int i = 0; i < arrWord.length; i++)
/* 101 */       if ((arrQWord[i] == null) && ((arrWord[i].isPunctuation()) || (funcDict.exist(arrWord[i].getContent())))) {
/* 102 */         arrQWord[i] = new QueryWord(arrWord[i].getContent());
/* 103 */         arrQWord[i].setFunctionFlag(true);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected boolean equalToQueryWord(String qcontent, String qlemma, int qpos, String content)
/*     */   {
/* 109 */     if (qlemma == null) {
/* 110 */       if ((content.equalsIgnoreCase(qcontent)) || ((content.length() == qcontent.length() + 1) && (content.equalsIgnoreCase(qcontent + "s"))) || (
/* 111 */         (qpos == 2) && (content.length() == qcontent.length() + 2) && (content.equalsIgnoreCase(qcontent + "ed")))) {
/* 112 */         return true;
/*     */       }
/* 114 */       return false;
/*     */     }
/*     */ 
/* 118 */     if ((content.equalsIgnoreCase(qcontent)) || (content.equalsIgnoreCase(qlemma))) {
/* 119 */       return true;
/*     */     }
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   private String needLemmaComparison(QueryWord qword)
/*     */   {
/* 128 */     String lemma = qword.getLemma();
/* 129 */     if ((lemma == null) || (lemma.equalsIgnoreCase(qword.getContent())))
/* 130 */       return null;
/* 131 */     if ((qword.getContent().length() == lemma.length() + 1) && (qword.getContent().endsWith("s")))
/* 132 */       return null;
/* 133 */     return lemma;
/*     */   }
/*     */ 
/*     */   protected int getCaseInfo(Word cur) {
/* 137 */     if ((cur.getPOSIndex() >= 1) && (cur.getPOSIndex() <= 4)) {
/* 138 */       if (cur.prev != null) {
/* 139 */         return cur.isInitialCapital() ? 1 : 0;
/*     */       }
/* 141 */       return cur.isInitialCapital() ? -1 : 0;
/*     */     }
/*     */ 
/* 144 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.extract.AbstractCandidateFinder
 * JD-Core Version:    0.6.2
 */