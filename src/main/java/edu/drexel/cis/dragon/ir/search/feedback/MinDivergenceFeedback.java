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
/*    */ public class MinDivergenceFeedback extends AbstractFeedback
/*    */ {
/*    */   private int expandTermNum;
/*    */   private double bkgCoeffi;
/*    */ 
/*    */   public MinDivergenceFeedback(Searcher searcher, int feedbackDocNum, int expandTermNum, double feedbackCoeffi, double bkgCoeffi)
/*    */   {
/* 25 */     super(searcher, feedbackDocNum, feedbackCoeffi);
/* 26 */     this.expandTermNum = expandTermNum;
/* 27 */     this.bkgCoeffi = bkgCoeffi;
/*    */   }
/*    */ 
/*    */   protected ArrayList estimateNewQueryModel(IRQuery oldQuery)
/*    */   {
/* 41 */     IndexReader indexReader = this.searcher.getIndexReader();
/* 42 */     this.searcher.search(oldQuery);
/* 43 */     int docNum = this.feedbackDocNum < this.searcher.getRetrievedDocNum() ? this.feedbackDocNum : this.searcher.getRetrievedDocNum();
/* 44 */     if (docNum == 0) return null;
/*    */ 
/* 46 */     SortedArray termList = new SortedArray(new IndexComparator());
/* 47 */     for (int i = 0; i < docNum; i++) {
/* 48 */       IRDoc curDoc = this.searcher.getIRDoc(i);
/* 49 */       int[] arrIndex = indexReader.getTermIndexList(curDoc.getIndex());
/* 50 */       int[] arrFreq = indexReader.getTermFrequencyList(curDoc.getIndex());
/* 51 */       for (int j = 0; j < arrIndex.length; j++) {
/* 52 */         Token curToken = new Token(null);
/* 53 */         curToken.setIndex(arrIndex[j]);
/* 54 */         curToken.setWeight(Math.log(arrFreq[j] * 1.0D / curDoc.getTermCount()));
/* 55 */         if (!termList.add(curToken)) {
/* 56 */           Token oldToken = (Token)termList.get(termList.insertedPos());
/* 57 */           oldToken.setWeight(oldToken.getWeight() + curToken.getWeight());
/*    */         }
/*    */       }
/*    */     }
/* 61 */     double collectionTermCount = indexReader.getCollection().getTermCount();
int i;
/* 62 */     for (i = 0; i < termList.size(); i++) {
/* 63 */       Token curToken = (Token)termList.get(i);
/* 64 */       double weight = curToken.getWeight() / docNum - this.bkgCoeffi * indexReader.getIRTerm(curToken.getIndex()).getFrequency() / collectionTermCount;
/* 65 */       curToken.setWeight(Math.exp(weight / (1.0D - this.bkgCoeffi)));
/*    */     }
/*    */ 
/* 69 */     termList.setComparator(new WeightComparator(true));
/* 70 */     int predicateNum = oldQuery.getChildNum() + this.expandTermNum < termList.size() ? oldQuery.getChildNum() + this.expandTermNum : termList.size();
/* 71 */     ArrayList newPredicateList = new ArrayList(predicateNum);
/* 72 */     double weightSum = 0.0D;
/* 73 */     for (i = 0; i < predicateNum; i++) weightSum += ((Token)termList.get(i)).getWeight();
/* 74 */     for (i = 0; i < predicateNum; i++) {
/* 75 */       Token curToken = (Token)termList.get(i);
/* 76 */       SimpleTermPredicate curPredicate = buildSimpleTermPredicate(curToken.getIndex(), curToken.getWeight() / weightSum);
/* 77 */       newPredicateList.add(curPredicate);
/*    */     }
/* 79 */     return newPredicateList;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.feedback.MinDivergenceFeedback
 * JD-Core Version:    0.6.2
 */