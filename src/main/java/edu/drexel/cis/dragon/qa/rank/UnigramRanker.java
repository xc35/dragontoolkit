/*     */ package edu.drexel.cis.dragon.qa.rank;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import edu.drexel.cis.dragon.qa.query.QueryWord;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*     */ import edu.drexel.cis.dragon.qa.system.CandidateBase;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public class UnigramRanker extends AbstractRanker
/*     */ {
/*     */   private int top;
/*     */   private int minFreq;
/*     */   private double bkgCoefficient;
/*     */ 
/*     */   public UnigramRanker()
/*     */   {
/*  21 */     this.bkgCoefficient = 0.5D;
/*     */   }
/*     */ 
/*     */   public ArrayList rank(QuestionQuery query, CandidateBase base, ArrayList list)
/*     */   {
	/*     */      int i;
/*  34 */     int numWord = 0;
/*  35 */     for ( i = 0; i < query.size(); i++)
/*  36 */       if (query.getQueryWord(i).getWeight() > 0.0D)
/*  37 */         numWord++;
/*  38 */     SortedArray queryWords = new SortedArray();
/*  39 */     double[] arrQueryModel = new double[numWord];
/*  40 */     int count = 0;
/*  41 */     for ( i = 0; i < query.size(); i++) {
/*  42 */       QueryWord qword = query.getQueryWord(i);
/*  43 */       if (qword.getWeight() != 0.0D)
/*     */       {
/*  45 */         arrQueryModel[count] = qword.getWeight();
/*  46 */         Token token = new Token(qword.getContent());
/*  47 */         token.setIndex(count);
/*  48 */         queryWords.add(token);
/*  49 */         if (qword.getContent().equalsIgnoreCase(qword.getLemma())) {
/*  50 */           if ((qword.getPOSTag() == 1) || (qword.getPOSTag() == 2)) {
/*  51 */             token = new Token(qword.getContent() + "s");
/*  52 */             token.setIndex(count);
/*  53 */             queryWords.add(token);
/*     */           }
/*     */         }
/*     */         else {
/*  57 */           token = new Token(qword.getLemma());
/*  58 */           token.setIndex(count);
/*  59 */           queryWords.add(token);
/*     */         }
/*  61 */         count++;
/*     */       }
/*     */     }
/*  64 */     double[] arrCollectionModel = getCollectionModel(base, queryWords, numWord);
/*     */ 
/*  66 */     int numCan = Math.min(this.top, list.size());
/*  67 */     ArrayList newList = new ArrayList(list.size());
/*  68 */     for ( i = 0; i < numCan; i++) {
/*  69 */       resetQueryWords(queryWords);
/*  70 */       Candidate curCan = (Candidate)list.get(i);
/*  71 */       if (curCan.getFrequency() < this.minFreq)
/*     */         break;
/*  73 */       double weight = Math.log(curCan.getWeight());
/*  74 */       double[] arrCandidateModel = getCandidateModel(base, getSentence(curCan, base), queryWords, numWord);
/*  75 */       for (int j = 0; j < numWord; j++) {
/*  76 */         double prob = arrCollectionModel[j] * this.bkgCoefficient + (1.0D - this.bkgCoefficient) * arrCandidateModel[j];
/*  77 */         if (prob > 0.0D) {
/*  78 */           weight += Math.log(prob) * arrQueryModel[j];
/*     */         }
/*     */       }
/*  81 */       curCan.setWeight(weight);
/*  82 */       newList.add(curCan);
/*     */     }
/*  84 */     Collections.sort(newList, new WeightComparator(true));

/*  86 */     for (; i < list.size(); i++) {
/*  87 */       newList.add(list.get(i));
/*     */     }
/*  89 */     return newList;
/*     */   }
/*     */ 
/*     */   private int[] getSentence(Candidate can, CandidateBase base)
/*     */   {
/*  97 */     int[] arrSent = new int[base.getSentenceNum()];
/*  98 */     int[] arrIndex = base.getCandidateSentences(can.getIndex());
/*  99 */     for (int j = 0; j < arrIndex.length; j++) {
/* 100 */       arrSent[arrIndex[j]] = 1;
/*     */     }
/* 102 */     if (can.getVariants() == null)
/* 103 */       return arrSent;
/* 104 */     ArrayList variants = can.getVariants();
/* 105 */     for (int  i = 0; i < variants.size(); i++) {
/* 106 */       arrIndex = base.getCandidateSentences(((Candidate)variants.get(i)).getIndex());
/* 107 */       for (int j = 0; j < arrIndex.length; j++)
/* 108 */         arrSent[arrIndex[j]] = 1;
/*     */     }
/* 110 */     return arrSent;
/*     */   }
/*     */ 
/*     */   private void resetQueryWords(SortedArray queryWords)
/*     */   {
/* 117 */     for (int i = 0; i < queryWords.size(); i++) {
/* 118 */       Token token = (Token)queryWords.get(i);
/* 119 */       token.setFrequency(0);
/* 120 */       token.setWeight(0.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected double[] getCollectionModel(CandidateBase base, SortedArray queryWords, int numWord)
/*     */   {
/* 127 */     int[] arrSent = new int[base.getSentenceNum()];
/* 128 */     MathUtil.initArray(arrSent, 1);
/* 129 */     return getCandidateModel(base, arrSent, queryWords, numWord);
/*     */   }
/*     */ 
/*     */   protected double[] getCandidateModel(CandidateBase base, int[] arrSent, SortedArray queryWords, int numWord)
/*     */   {
/* 138 */     int total = 0;
/*     */ 
/* 140 */     for (int i = 0; i < arrSent.length; i++) {
/* 141 */       if (arrSent[i] != 0)
/*     */       {
/* 143 */         Word cur = base.getSentence(i).getFirstWord();
/* 144 */         while (cur != null) {
/* 145 */           if (!cur.isPunctuation()) {
/* 146 */             total++;
/* 147 */             int pos = queryWords.binarySearch(new Token(cur.getContent()));
/* 148 */             if (pos >= 0)
/* 149 */               ((Token)queryWords.get(pos)).addFrequency(1);
/*     */           }
/* 151 */           cur = cur.next;
/*     */         }
/*     */       }
/*     */     }
/* 155 */     double[] arrModel = new double[numWord];
/* 156 */     for (int i = 0; i < queryWords.size(); i++) {
/* 157 */       Token curToken = (Token)queryWords.get(i);
/* 158 */       arrModel[curToken.getIndex()] += curToken.getFrequency();
/*     */     }
/* 160 */     for (int i = 0; i < numWord; i++)
/* 161 */       arrModel[i] /= total;
/* 162 */     return arrModel;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.rank.UnigramRanker
 * JD-Core Version:    0.6.2
 */