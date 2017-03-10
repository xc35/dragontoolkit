/*     */ package edu.drexel.cis.dragon.ir.search.feedback;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*     */ import edu.drexel.cis.dragon.ir.search.Searcher;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class RocchioFeedback extends AbstractFeedback
/*     */ {
/*     */   private int expandTermNum;
/*     */   private boolean useBM25;
/*     */   private double param1;
/*     */   private double param2;
/*     */   private double avgDocLength;
/*     */ 
/*     */   public RocchioFeedback(Searcher searcher, int feedbackDocNum, int expandTermNum, double relevantDocCoeffi)
/*     */   {
/*  28 */     super(searcher, feedbackDocNum, relevantDocCoeffi);
/*  29 */     this.expandTermNum = expandTermNum;
/*  30 */     this.useBM25 = false;
/*     */   }
/*     */ 
/*     */   public RocchioFeedback(Searcher searcher, int feedbackDocNum, int expandTermNum, double relevantDocCoeffi, double bm25k1, double bm25b) {
/*  34 */     super(searcher, feedbackDocNum, relevantDocCoeffi);
/*  35 */     this.expandTermNum = expandTermNum;
/*     */ 
/*  37 */     this.useBM25 = true;
/*  38 */     IRCollection collection = searcher.getIndexReader().getCollection();
/*  39 */     this.avgDocLength = (collection.getTermCount() * 1.0D / collection.getDocNum());
/*  40 */     this.param1 = (bm25k1 * (1.0D - bm25b));
/*  41 */     this.param2 = (bm25k1 * bm25b);
/*     */   }
/*     */ 
/*     */   protected ArrayList estimateNewQueryModel(IRQuery oldQuery)
/*     */   {
/*  55 */     IndexReader indexReader = this.searcher.getIndexReader();
/*  56 */     this.searcher.search(oldQuery);
/*  57 */     int releDocNum = this.feedbackDocNum < this.searcher.getRetrievedDocNum() ? this.feedbackDocNum : this.searcher.getRetrievedDocNum();
/*  58 */     if (releDocNum == 0) return null;
/*     */ 
/*  60 */     int totalDocNum = indexReader.getCollection().getDocNum();
/*     */ 
/*  63 */     SortedArray termList = new SortedArray(new IndexComparator());
/*  64 */     for (int i = 0; i < releDocNum; i++) {
/*  65 */       IRDoc curDoc = this.searcher.getIRDoc(i);
/*  66 */       int[] arrIndex = indexReader.getTermIndexList(curDoc.getIndex());
/*  67 */       int[] arrFreq = indexReader.getTermFrequencyList(curDoc.getIndex());
/*  68 */       for (int j = 0; j < arrIndex.length; j++) {
/*  69 */         Token curToken = new Token(null);
/*  70 */         curToken.setIndex(arrIndex[j]);
/*  71 */         double weight = getTermWeight(curDoc, arrFreq[j]);
/*  72 */         curToken.setWeight(weight);
/*  73 */         if (!termList.add(curToken)) {
/*  74 */           curToken = (Token)termList.get(termList.insertedPos());
/*  75 */           curToken.setWeight(curToken.getWeight() + weight);
/*     */         }
/*     */       }
/*     */     }
/*  79 */     for (int i = 0; i < termList.size(); i++) {
/*  80 */       Token curToken = (Token)termList.get(i);
/*  81 */       curToken.setWeight(curToken.getWeight() * getIDF(totalDocNum, indexReader.getIRTerm(curToken.getIndex()).getDocFrequency()));
/*     */     }
/*     */ 
/*  85 */     termList.setComparator(new WeightComparator(true));
/*  86 */     int predicateNum = oldQuery.getChildNum() + this.expandTermNum < termList.size() ? oldQuery.getChildNum() + this.expandTermNum : termList.size();
/*  87 */     ArrayList newPredicateList = new ArrayList(predicateNum);
/*  88 */     double weightSum = 0.0D;
/*  89 */     for (int i = 0; i < predicateNum; i++) {
/*  90 */       Token curToken = (Token)termList.get(i);
/*  91 */       if (curToken.getWeight() <= 0.0D)
/*  92 */         predicateNum = i;
/*     */       else
/*  94 */         weightSum += ((Token)termList.get(i)).getWeight();
/*     */     }
/*  96 */     for (int i = 0; i < predicateNum; i++) {
/*  97 */       Token curToken = (Token)termList.get(i);
/*  98 */       SimpleTermPredicate curPredicate = buildSimpleTermPredicate(curToken.getIndex(), curToken.getWeight() / weightSum);
/*  99 */       newPredicateList.add(curPredicate);
/*     */     }
/* 101 */     return newPredicateList;
/*     */   }
/*     */ 
/*     */   private double getTermWeight(IRDoc curDoc, int termFreq) {
/* 105 */     if (this.useBM25) {
/* 106 */       return termFreq / (this.param1 + this.param2 * curDoc.getTermCount() / this.avgDocLength + termFreq);
/*     */     }
/* 108 */     return termFreq;
/*     */   }
/*     */ 
/*     */   private double getIDF(int collectionDocNum, int termDocFrequency) {
/* 112 */     return Math.log((collectionDocNum + 1.0D) / (termDocFrequency + 0.5D));
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.feedback.RocchioFeedback
 * JD-Core Version:    0.6.2
 */