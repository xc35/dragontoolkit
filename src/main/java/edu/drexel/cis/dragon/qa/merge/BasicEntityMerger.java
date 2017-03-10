/*     */ package edu.drexel.cis.dragon.qa.merge;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.qa.query.QueryWord;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*     */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class BasicEntityMerger
/*     */   implements CandidateMerger
/*     */ {
/*     */   private Lemmatiser lemmatiser;
/*     */   private AbbrEntityMerger abbrMerger;
/*     */   private SurfaceEntityMerger surfaceMerger;
/*     */   private NameMerger nameMerger;
/*     */   private SimpleDictionary funcDict;
/*     */   private int top;
/*     */   private int canNum;
/*     */ 
/*     */   public BasicEntityMerger(SimpleDictionary allowedFuncWord)
/*     */   {
/*  20 */     this(null, allowedFuncWord);
/*     */   }
/*     */ 
/*     */   public BasicEntityMerger(Lemmatiser lemmatiser, SimpleDictionary allowedFuncWord) {
/*  24 */     this.top = 25;
/*  25 */     this.canNum = 60;
/*  26 */     this.lemmatiser = lemmatiser;
/*  27 */     this.funcDict = allowedFuncWord;
/*  28 */     this.abbrMerger = new AbbrEntityMerger(allowedFuncWord);
/*  29 */     this.surfaceMerger = new SurfaceEntityMerger(lemmatiser);
/*  30 */     this.nameMerger = new NameMerger();
/*     */   }
/*     */ 
/*     */   public ArrayList merge(QuestionQuery query, ArrayList candidates)
/*     */   {
/*  41 */     SimpleDictionary queryDict = getQueryWordList(query);
/*  42 */     String[] arrName = new String[Math.min(this.canNum, candidates.size())];
/*  43 */     Candidate[] arrTerm = new Candidate[arrName.length];
/*  44 */     Candidate[] arrNewTerm = new Candidate[arrName.length];
/*  45 */     boolean[] arrRemoved = new boolean[Math.min(this.top, candidates.size())];
/*  46 */     ArrayList topCandidates = new ArrayList();
/*  47 */     for (int i = 0; i < arrTerm.length; i++) {
/*  48 */       arrTerm[i] = ((Candidate)candidates.get(i));
/*  49 */       arrNewTerm[i] = trim(arrTerm[i], queryDict);
/*  50 */       if (arrNewTerm[i] != null) {
/*  51 */         if (this.lemmatiser == null)
/*  52 */           arrName[i] = (" " + arrNewTerm[i].toString().toLowerCase() + " ");
/*     */         else {
/*  54 */           arrName[i] = (" " + arrNewTerm[i].toLemmaString() + " ");
/*     */         }
/*     */       }
/*     */     }
/*  58 */     for (int i = 0; i < arrRemoved.length; i++)
/*  59 */       if ((arrRemoved[i] == false) && (arrNewTerm[i] != null))
/*     */       {
/*  61 */         for (int j = i + 1; j < arrTerm.length; j++) {
/*  62 */           if (arrNewTerm[j] != null)
/*     */           {
/*  64 */             if ((arrName[i].indexOf(arrName[j]) >= 0) || (arrName[j].indexOf(arrName[i]) >= 0))
/*  65 */               if ((arrTerm[i].getWordNum() > arrNewTerm[i].getWordNum()) && 
/*  66 */                 (arrTerm[j].getWordNum() == arrNewTerm[j].getWordNum()) && 
/*  67 */                 (arrNewTerm[j].getWordNum() == arrNewTerm[i].getWordNum())) {
/*  68 */                 arrTerm[j].merge(arrTerm[i]);
/*  69 */                 arrTerm[i] = arrTerm[j];
/*  70 */                 arrNewTerm[i] = arrNewTerm[j];
/*  71 */                 arrName[i] = arrName[j];
/*  72 */                 arrNewTerm[j] = null;
/*  73 */                 arrName[j] = null;
/*  74 */                 arrTerm[j] = null;
/*     */               }
/*     */               else {
/*  77 */                 arrTerm[i].merge(arrTerm[j]);
/*  78 */                 if (j < this.top)
/*  79 */                   arrRemoved[j] = true;
/*     */               }
/*     */           }
/*     */         }
/*  83 */         topCandidates.add(arrTerm[i]);
/*     */       }
/*  85 */     return this.nameMerger.merge(query, this.abbrMerger.merge(query, this.surfaceMerger.merge(query, topCandidates)));
/*     */   }
/*     */ 
/*     */   protected SimpleDictionary getQueryWordList(QuestionQuery query)
/*     */   {
/*  93 */     SimpleDictionary dict = new SimpleDictionary();
/*  94 */     for (int i = 0; i < query.size(); i++) {
/*  95 */       QueryWord word = query.getQueryWord(i);
/*  96 */       if (!word.isFunctionalWord())
/*  97 */         dict.add(getString(word));
/*     */     }
/*  99 */     return dict;
/*     */   }
/*     */ 
/*     */   private Candidate trim(Candidate term, SimpleDictionary queryDict)
/*     */   {
/*     */     try
/*     */     {
/* 107 */       Word start = term.getStartingWord();
/* 108 */       Word end = term.getEndingWord();
/*     */ 
/* 110 */       while (start != null) {
/* 111 */         if (this.funcDict.exist(start.getContent())) {
/* 112 */           start = start.prev;
/* 113 */           break;
/*     */         }
/* 115 */         if (!queryDict.exist(getString(start))) break;
/* 116 */         start = start.next;
/*     */       }
/*     */ 
/* 120 */       if ((start == null) || (start.getPosInSentence() > end.getPosInSentence())) {
/* 121 */         return null;
/*     */       }
/*     */ 
/* 124 */       while (end != null) {
/* 125 */         if (this.funcDict.exist(end.getContent())) {
/* 126 */           end = end.next;
/* 127 */           break;
/*     */         }
/* 129 */         if (!queryDict.exist(getString(end))) break;
/* 130 */         end = end.prev;
/*     */       }
/*     */ 
/* 134 */       if (end == null) {
/* 135 */         return null;
/*     */       }
/* 137 */       Candidate newTerm = new Candidate(start, end);
/* 138 */       newTerm.copyFrom(term);
/*     */ 
/* 140 */       if (this.lemmatiser != null)
/*     */       {
/* 142 */         start = start.next;
/* 143 */         while ((start != null) && (start.getPosInSentence() < end.getPosInSentence())) {
/* 144 */           getString(start);
/* 145 */           start = start.next;
/*     */         }
/*     */       }
/* 148 */       return newTerm;
/*     */     } catch (Exception e) {
/*     */     }
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */   protected String getString(Word word)
/*     */   {
/* 156 */     if (this.lemmatiser == null) {
/* 157 */       return word.getContent().toLowerCase();
/*     */     }
/* 159 */     if (word.getLemma() == null)
/* 160 */       word.setLemma(this.lemmatiser.lemmatize(word.getContent(), word.getPOSIndex()));
/* 161 */     return word.getLemma();
/*     */   }
/*     */ 
/*     */   protected String getString(QueryWord word)
/*     */   {
/* 166 */     if (this.lemmatiser == null) {
/* 167 */       return word.getContent();
/*     */     }
/* 169 */     if (word.getLemma() == null)
/* 170 */       word.setLemma(this.lemmatiser.lemmatize(word.getContent(), word.getPOSTag()));
/* 171 */     return word.getLemma();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.merge.BasicEntityMerger
 * JD-Core Version:    0.6.2
 */