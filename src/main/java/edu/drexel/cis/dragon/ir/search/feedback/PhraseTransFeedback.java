/*     */ package edu.drexel.cis.dragon.ir.search.feedback;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*     */ import edu.drexel.cis.dragon.ir.search.Searcher;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class PhraseTransFeedback extends AbstractFeedback
/*     */ {
/*     */   private IndexReader phraseIndexer;
/*     */   private DoubleSparseMatrix transMatrix;
/*     */   private int expandTermNum;
/*     */   private double bkgCoeffi;
/*     */   private boolean selfTranslation;
/*     */ 
/*     */   public PhraseTransFeedback(Searcher searcher, int feedbackDocNum, int expandTermNum, double feedbackCoeffi, IndexReader phraseIndexer, double bkgCoeffi)
/*     */   {
/*  29 */     super(searcher, feedbackDocNum, feedbackCoeffi);
/*  30 */     this.expandTermNum = expandTermNum;
/*  31 */     this.bkgCoeffi = bkgCoeffi;
/*  32 */     this.transMatrix = null;
/*  33 */     this.phraseIndexer = phraseIndexer;
/*  34 */     this.selfTranslation = true;
/*     */   }
/*     */ 
/*     */   public PhraseTransFeedback(Searcher searcher, int feedbackDocNum, int expandTermNum, double feedbackCoeffi, IndexReader phraseIndexer, double bkgCoeffi, DoubleSparseMatrix transMatrix) {
/*  38 */     super(searcher, feedbackDocNum, feedbackCoeffi);
/*  39 */     this.expandTermNum = expandTermNum;
/*  40 */     this.bkgCoeffi = bkgCoeffi;
/*  41 */     this.transMatrix = transMatrix;
/*  42 */     this.phraseIndexer = phraseIndexer;
/*  43 */     this.selfTranslation = false;
/*     */   }
/*     */ 
/*     */   protected ArrayList estimateNewQueryModel(IRQuery oldQuery)
/*     */   {
/*  53 */     this.searcher.search(oldQuery);
/*  54 */     int docNum = this.feedbackDocNum < this.searcher.getRetrievedDocNum() ? this.feedbackDocNum : this.searcher.getRetrievedDocNum();
/*  55 */     if (docNum == 0) return null;
/*     */ 
/*  57 */     ArrayList phraseList = generativeModel(docNum);
/*  58 */     ArrayList termList = translate(phraseList);
/*     */ 
/*  61 */     int predicateNum = oldQuery.getChildNum() + this.expandTermNum < termList.size() ? oldQuery.getChildNum() + this.expandTermNum : termList.size();
/*  62 */     ArrayList newPredicateList = new ArrayList(predicateNum);
/*  63 */     double weightSum = 0.0D;
/*  64 */     for (int i = 0; i < predicateNum; i++) weightSum += ((Token)termList.get(i)).getWeight();
/*  65 */     for (int i = 0; i < predicateNum; i++) {
/*  66 */       Token curToken = (Token)termList.get(i);
/*  67 */       SimpleTermPredicate curPredicate = buildSimpleTermPredicate(curToken.getIndex(), curToken.getWeight() / weightSum);
/*  68 */       newPredicateList.add(curPredicate);
/*     */     }
/*  70 */     return newPredicateList;
/*     */   }
/*     */ 
/*     */   private ArrayList generativeModel(int docNum)
/*     */   {
/*  83 */     SortedArray phraseList = new SortedArray(new IndexComparator());
/*  84 */     for (int i = 0; i < docNum; i++) {
/*  85 */       IRDoc curDoc = this.searcher.getIRDoc(i);
/*  86 */       int[] arrIndex = this.phraseIndexer.getTermIndexList(curDoc.getIndex());
/*  87 */       int[] arrFreq = this.phraseIndexer.getTermFrequencyList(curDoc.getIndex());
/*  88 */       for (int j = 0; j < arrIndex.length; j++) {
/*  89 */         Token curToken = new Token(null);
/*  90 */         curToken.setIndex(arrIndex[j]);
/*  91 */         curToken.setFrequency(arrFreq[j]);
/*  92 */         if (!phraseList.add(curToken)) {
/*  93 */           ((Token)phraseList.get(phraseList.insertedPos())).addFrequency(curToken.getFrequency());
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  99 */     int iterationNum = 15;
/* 100 */     double[] arrProb = new double[phraseList.size()];
/* 101 */     double[] arrCollectionProb = new double[phraseList.size()];
/* 102 */     double collectionTermCount = this.phraseIndexer.getCollection().getTermCount();
/* 103 */     for (int i = 0; i < phraseList.size(); i++) {
/* 104 */       Token curToken = (Token)phraseList.get(i);
/* 105 */       curToken.setWeight(1.0D / phraseList.size());
/* 106 */       arrCollectionProb[i] = (this.bkgCoeffi * this.phraseIndexer.getIRTerm(curToken.getIndex()).getFrequency() / collectionTermCount);
/*     */     }
/*     */ 
/* 110 */     for (int i = 0; i < iterationNum; i++) {
/* 111 */       double weightSum = 0.0D;
/* 112 */       for (int j = 0; j < phraseList.size(); j++) {
/* 113 */         Token curToken = (Token)phraseList.get(j);
/* 114 */         arrProb[j] = ((1.0D - this.bkgCoeffi) * curToken.getWeight() / ((1.0D - this.bkgCoeffi) * curToken.getWeight() + arrCollectionProb[j]) * curToken.getFrequency());
/* 115 */         weightSum += arrProb[j];
/*     */       }
/* 117 */       for (int j = 0; j < phraseList.size(); j++)
/* 118 */         ((Token)phraseList.get(j)).setWeight(arrProb[j] / weightSum);
/*     */     }
/* 120 */     phraseList.setComparator(new WeightComparator(true));
/* 121 */     return phraseList;
/*     */   }
/*     */ 
/*     */   private SortedArray translate(ArrayList phraseList)
/*     */   {
/* 136 */     IndexReader reader = this.searcher.getIndexReader();
/* 137 */     double[] arrWeight = new double[reader.getCollection().getTermNum()];
/* 138 */     for (int i = 0; i < arrWeight.length; i++) arrWeight[i] = 0.0D;
/*     */     int phraseNum;
/* 140 */     if (this.selfTranslation)
/* 141 */       phraseNum = phraseList.size();
/*     */     else {
/* 143 */       phraseNum = 100 < phraseList.size() ? 100 : phraseList.size();
/*     */     }
/* 145 */     for (int i = 0; i < phraseNum; i++) {
/* 146 */       Token curToken = (Token)phraseList.get(i);
/* 147 */       if (this.selfTranslation) {
/* 148 */         String curPhrase = this.phraseIndexer.getTermKey(curToken.getIndex());
/* 149 */         String[] arrWord = curPhrase.split(" ");
/* 150 */         for (int j = 0; j < arrWord.length; j++) {
/* 151 */           IRTerm curTerm = reader.getIRTerm(arrWord[j]);
/* 152 */           if (curTerm != null)
/* 153 */             arrWeight[curTerm.getIndex()] += curToken.getWeight();
/*     */         }
/*     */       }
/*     */       else {
/* 157 */         int[] arrIndex = this.transMatrix.getNonZeroColumnsInRow(curToken.getIndex());
/* 158 */         double[] arrTransProb = this.transMatrix.getNonZeroDoubleScoresInRow(curToken.getIndex());
/* 159 */         if (arrIndex != null) {
/* 160 */           for (int j = 0; j < arrIndex.length; j++) {
/* 161 */             arrWeight[arrIndex[j]] += curToken.getWeight() * arrTransProb[j];
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 166 */     SortedArray termList = new SortedArray(new WeightComparator(true));
/* 167 */     for (int i = 0; i < arrWeight.length; i++) {
/* 168 */       if (arrWeight[i] > 0.0D) {
/* 169 */         Token curToken = new Token(null);
/* 170 */         curToken.setWeight(arrWeight[i]);
/* 171 */         curToken.setIndex(i);
/* 172 */         termList.add(curToken);
/*     */       }
/*     */     }
/* 175 */     return termList;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.feedback.PhraseTransFeedback
 * JD-Core Version:    0.6.2
 */