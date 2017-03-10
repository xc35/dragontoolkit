/*     */ package edu.drexel.cis.dragon.qa.system;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Document;
/*     */ import edu.drexel.cis.dragon.nlp.DocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.Paragraph;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.compare.ConceptNameComparator;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.qa.expand.CandidateExpander;
/*     */ import edu.drexel.cis.dragon.qa.extract.CandidateFinder;
/*     */ import edu.drexel.cis.dragon.qa.filter.CandidateFilter;
/*     */ import edu.drexel.cis.dragon.qa.merge.CandidateMerger;
/*     */ import edu.drexel.cis.dragon.qa.query.QueryGenerator;
/*     */ import edu.drexel.cis.dragon.qa.query.QueryWord;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.rank.CandidateRanker;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public abstract class AbstractQASystem
/*     */   implements QASystem
/*     */ {
/*     */   protected QuestionQuery query;
/*     */   protected CandidateBase base;
/*     */   protected QueryGenerator generator;
/*     */   protected int top;
/*     */ 
/*     */   public AbstractQASystem()
/*     */   {
/*  25 */     this.top = 50;
/*     */   }
/*     */ 
/*     */   public QuestionQuery getLastQuestionQuery() {
/*  29 */     return this.query;
/*     */   }
/*     */ 
/*     */   public QuestionQuery generateQuery(String question) {
/*  33 */     return this.generator.generate(question);
/*     */   }
/*     */ 
/*     */   public CandidateBase getCandidateBase() {
/*  37 */     return this.base;
/*     */   }
/*     */ 
/*     */   public ArrayList getSupportingDoc(Candidate candidate)
/*     */   {
/*  48 */     ArrayList candidates = candidate.getVariants();
/*  49 */     if (candidates == null) {
/*  50 */       candidates = new ArrayList(1);
/*  51 */       candidates.add(candidate);
/*     */     }
/*     */ 
/*  54 */     ArrayList list = new ArrayList();
/*  55 */     for (int k = 0; k < candidates.size(); k++) {
/*  56 */       candidate = (Candidate)candidates.get(k);
/*  57 */       double[] arrScore = this.base.getCandidateScores(candidate.getIndex());
/*  58 */       int[] arrIndex = this.base.getCandidateSentences(candidate.getIndex());
/*  59 */       for (int i = 0; i < arrScore.length; i++) {
/*  60 */         SupportingDoc curDoc = new SupportingDoc(arrIndex[i], arrScore[i]);
/*  61 */         list.add(curDoc);
/*     */       }
/*     */     }
/*     */ 
/*  65 */     Collections.sort(list, new WeightComparator(true));
/*  66 */     while (list.size() > 5)
/*  67 */       list.remove(list.size() - 1);
/*  68 */     for (int i = 0; i < list.size(); i++) {
/*  69 */       SupportingDoc curDoc = (SupportingDoc)list.get(i);
/*  70 */       Sentence sent = this.base.getSentence(curDoc.getSentenceIndex());
/*  71 */       curDoc.setSnippet(sent.toString());
/*  72 */       curDoc.setURL(this.base.getDocumentURL(sent.getParent().getParent().getIndex()));
/*     */     }
/*  74 */     return list;
/*     */   }
/*     */ 
/*     */   protected ArrayList findCandidate(QuestionQuery query, CandidateFinder finder, CandidateMerger merger, CandidateFilter filter, CandidateRanker ranker, CandidateExpander expander, DocumentParser parser, CollectionReader reader)
/*     */   {
/*  82 */     if (this.base != null)
/*  83 */       this.base.close();
/*  84 */     this.base = new CandidateBase(query, parser, reader, this.top);
/*  85 */     ArrayList list = retrieveCandidates(this.base, finder, query);
/*  86 */     if ((list.size() < 20) && (finder.getMinSentenceScore() > 0.25D)) {
/*  87 */       list.clear();
/*  88 */       double threshold = finder.getMinSentenceScore();
/*  89 */       finder.setMinSentenceScore(0.25D);
/*  90 */       list = retrieveCandidates(this.base, finder, query);
/*  91 */       finder.setMinSentenceScore(threshold);
/*     */     }
/*  93 */     Collections.sort(list, new WeightComparator(true));
/*     */ 
/*  96 */     if (merger == null)
/*  97 */       return list;
/*  98 */     list = merger.merge(query, list);
/*     */ 
/* 101 */     if (filter == null) {
/* 102 */       return list;
/*     */     }
/* 104 */     list = filter.filter(query, list);
/*     */ 
/* 107 */     if (ranker == null) {
/* 108 */       return list;
/*     */     }
/* 110 */     list = ranker.rank(query, this.base, list);
/* 111 */     return expander.expand(query, this.base, list);
/*     */   }
/*     */ 
/*     */   protected ArrayList retrieveCandidates(CandidateBase base, CandidateFinder finder, QuestionQuery query)
/*     */   {
/* 123 */     SortedArray list = new SortedArray(new ConceptNameComparator());
/* 124 */     base.reset();
/*     */ 
/* 126 */     for (int i = 0; i < base.getDocumentNum(); i++) {
/* 127 */       finder.initDocument(base.getDocument(i), query);
/* 128 */       Paragraph para = base.getDocument(i).getFirstParagraph();
/* 129 */       while (para != null) {
/* 130 */         Sentence sent = para.getFirstSentence();
/* 131 */         while (sent != null)
/*     */         {
/*     */           ArrayList curList;
/* 132 */           if (sent.getIndex() < 0) {
/* 133 */             curList = null;
/*     */           }
/*     */           else {
/* 136 */             curList = finder.extract(sent, query);
/*     */ 
/* 139 */             base.addCollectionCount(sent.getWordNum());
/* 140 */             for (int k = 0; k < query.size(); k++) {
/* 141 */               QueryWord qword = query.getQueryWord(k);
/* 142 */               if (qword.getFrequency() > 0)
/* 143 */                 base.addQueryWord(qword.getIndex(), sent.getIndex(), qword.getFrequency());
/*     */             }
/*     */           }
/* 146 */           if ((curList == null) || (curList.size() == 0)) {
/* 147 */             if (curList == null)
/* 148 */               base.addSentenceFiltered(1);
/* 149 */             sent = sent.next;
/*     */           }
/*     */           else
/*     */           {
/* 153 */             curList = merge(curList);
/* 154 */             for (int k = 0; k < curList.size(); k++) {
/* 155 */               Candidate curTerm = (Candidate)curList.get(k);
/* 156 */               curTerm.setIndex(list.size());
/* 157 */               if (!list.add(curTerm)) {
/* 158 */                 Candidate oldTerm = (Candidate)list.get(list.insertedPos());
/* 159 */                 oldTerm.addFrequency(curTerm.getFrequency());
/* 160 */                 oldTerm.addInitialFrequency(curTerm.getInitialFrequency());
/* 161 */                 oldTerm.setWeight(oldTerm.getWeight() + curTerm.getWeight());
/* 162 */                 oldTerm.addSemanticFrequency(curTerm.getSemanticFrequency());
/* 163 */                 oldTerm.addCapitalFrequency(curTerm.getCapitalFrequency());
/* 164 */                 curTerm.setIndex(oldTerm.getIndex());
/*     */               }
/*     */ 
/* 167 */               base.add(curTerm.getIndex(), sent.getIndex(), curTerm.getWeight() / curTerm.getFrequency());
/*     */             }
/* 169 */             sent = sent.next;
/* 170 */             curList.clear();
/*     */           }
/*     */         }
/* 172 */         para = para.next;
/*     */       }
/*     */     }
/* 175 */     base.finalize();
/* 176 */     return list;
/*     */   }
/*     */ 
/*     */   private ArrayList merge(ArrayList list)
/*     */   {
/* 184 */     SortedArray newList = new SortedArray(list.size(), new ConceptNameComparator());
/* 185 */     for (int i = 0; i < list.size(); i++) {
/* 186 */       Candidate curTerm = (Candidate)list.get(i);
/* 187 */       if (!newList.add(curTerm)) {
/* 188 */         Candidate oldTerm = (Candidate)newList.get(newList.insertedPos());
/* 189 */         oldTerm.addFrequency(curTerm.getFrequency());
/* 190 */         oldTerm.addInitialFrequency(curTerm.getInitialFrequency());
/* 191 */         oldTerm.setWeight(oldTerm.getWeight() + curTerm.getWeight());
/* 192 */         oldTerm.addSemanticFrequency(curTerm.getSemanticFrequency());
/* 193 */         oldTerm.addCapitalFrequency(curTerm.getCapitalFrequency());
/*     */       }
/*     */     }
/* 196 */     return newList;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.system.AbstractQASystem
 * JD-Core Version:    0.6.2
 */