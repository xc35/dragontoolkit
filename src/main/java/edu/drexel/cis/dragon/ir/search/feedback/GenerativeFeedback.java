/*    */ package edu.drexel.cis.dragon.ir.search.feedback;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*    */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*    */ import edu.drexel.cis.dragon.ir.search.Searcher;
/*    */ import edu.drexel.cis.dragon.nlp.Token;
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*    */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class GenerativeFeedback extends AbstractFeedback
/*    */ {
/*    */   private int expandTermNum;
/*    */   private double bkgCoeffi;
/*    */ 
/*    */   public GenerativeFeedback(Searcher searcher, int feedbackDocNum, int expandTermNum, double feedbackCoeffi, double bkgCoeffi)
/*    */   {
/* 25 */     super(searcher, feedbackDocNum, feedbackCoeffi);
/* 26 */     this.expandTermNum = expandTermNum;
/* 27 */     this.bkgCoeffi = bkgCoeffi;
/*    */   }
/*    */ 
/*    */   protected ArrayList estimateNewQueryModel(IRQuery oldQuery)
/*    */   {
/* 42 */     IndexReader indexReader = this.searcher.getIndexReader();
/* 43 */     this.searcher.search(oldQuery);
/* 44 */     int docNum = this.feedbackDocNum < this.searcher.getRetrievedDocNum() ? this.feedbackDocNum : this.searcher.getRetrievedDocNum();
/* 45 */     if (docNum == 0) return null;
/*    */ 
/* 48 */     SortedArray termList = new SortedArray(new IndexComparator());
/* 49 */     for (int i = 0; i < docNum; i++) {
/* 50 */       IRDoc curDoc = this.searcher.getIRDoc(i);
/* 51 */       int[] arrIndex = indexReader.getTermIndexList(curDoc.getIndex());
/* 52 */       int[] arrFreq = indexReader.getTermFrequencyList(curDoc.getIndex());
/* 53 */       for (int j = 0; j < arrIndex.length; j++) {
/* 54 */         Token curToken = new Token(null);
/* 55 */         curToken.setIndex(arrIndex[j]);
/* 56 */         curToken.setFrequency(arrFreq[j]);
/* 57 */         if (!termList.add(curToken)) {
/* 58 */           ((Token)termList.get(termList.insertedPos())).addFrequency(curToken.getFrequency());
/*    */         }
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 64 */     int iterationNum = 15;
/* 65 */     double[] arrProb = new double[termList.size()];
/* 66 */     double[] arrCollectionProb = new double[termList.size()];
/* 67 */     double collectionTermCount = indexReader.getCollection().getTermCount();
int i;
/* 68 */     for (i = 0; i < termList.size(); i++) {
/* 69 */       Token curToken = (Token)termList.get(i);
/* 70 */       curToken.setWeight(1.0D / termList.size());
/* 71 */       arrCollectionProb[i] = (this.bkgCoeffi * indexReader.getIRTerm(curToken.getIndex()).getFrequency() / collectionTermCount);
/*    */     }
/*    */ 
/* 75 */     for (i = 0; i < iterationNum; i++) {
/* 76 */       double weightSum = 0.0D;
/* 77 */       for (int j = 0; j < termList.size(); j++) {
/* 78 */         Token curToken = (Token)termList.get(j);
/* 79 */         arrProb[j] = ((1.0D - this.bkgCoeffi) * curToken.getWeight() / ((1.0D - this.bkgCoeffi) * curToken.getWeight() + arrCollectionProb[j]) * curToken.getFrequency());
/* 80 */         weightSum += arrProb[j];
/*    */       }
/* 82 */       for (int j = 0; j < termList.size(); j++) {
/* 83 */         ((Token)termList.get(j)).setWeight(arrProb[j] / weightSum);
/*    */       }
/*    */     }
/*    */ 
/* 87 */     termList.setComparator(new WeightComparator(true));
/* 88 */     int predicateNum = oldQuery.getChildNum() + this.expandTermNum < termList.size() ? oldQuery.getChildNum() + this.expandTermNum : termList.size();
/* 89 */     ArrayList newPredicateList = new ArrayList(predicateNum);
/* 90 */     double weightSum = 0.0D;
/* 91 */     for (i = 0; i < predicateNum; i++) weightSum += ((Token)termList.get(i)).getWeight();
/* 92 */     for (i = 0; i < predicateNum; i++) {
/* 93 */       Token curToken = (Token)termList.get(i);
/* 94 */       SimpleTermPredicate curPredicate = buildSimpleTermPredicate(curToken.getIndex(), curToken.getWeight() / weightSum);
/* 95 */       newPredicateList.add(curPredicate);
/*    */     }
/* 97 */     return newPredicateList;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.feedback.GenerativeFeedback
 * JD-Core Version:    0.6.2
 */