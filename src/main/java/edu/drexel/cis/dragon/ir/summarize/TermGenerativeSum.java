/*    */ package edu.drexel.cis.dragon.ir.summarize;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ import edu.drexel.cis.dragon.nlp.Token;
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class TermGenerativeSum extends AbstractStructureSum
/*    */   implements StructureSummarizer
/*    */ {
/*    */   private double bkgCoeffi;
/*    */ 
/*    */   public TermGenerativeSum(IndexReader indexReader, double bkgCoefficient)
/*    */   {
/* 21 */     super(indexReader);
/* 22 */     this.bkgCoeffi = bkgCoefficient;
/*    */   }
/*    */ 
/*    */   public TopicSummary summarize(ArrayList docSet, int maxLength)
/*    */   {
/* 37 */     int docNum = docSet.size();
/* 38 */     SortedArray termList = new SortedArray(new IndexComparator());
/* 39 */     for (int i = 0; i < docNum; i++) {
/* 40 */       IRDoc curDoc = (IRDoc)docSet.get(i);
/* 41 */       int[] arrIndex = this.indexReader.getTermIndexList(curDoc.getIndex());
/* 42 */       int[] arrFreq = this.indexReader.getTermFrequencyList(curDoc.getIndex());
/* 43 */       for (int j = 0; j < arrIndex.length; j++) {
/* 44 */         Token curToken = new Token(null);
/* 45 */         curToken.setIndex(arrIndex[j]);
/* 46 */         curToken.setFrequency(arrFreq[j]);
/* 47 */         if (!termList.add(curToken)) {
/* 48 */           ((Token)termList.get(termList.insertedPos())).addFrequency(curToken.getFrequency());
/*    */         }
/*    */       }
/*    */ 
/*    */     }
/*    */      int i;
/* 54 */     int iterationNum = 15;
/* 55 */     double[] arrProb = new double[termList.size()];
/* 56 */     double[] arrCollectionProb = new double[termList.size()];
/* 57 */     double collectionTermCount = this.indexReader.getCollection().getTermCount();
/* 58 */     for (i = 0; i < termList.size(); i++) {
/* 59 */       Token curToken = (Token)termList.get(i);
/* 60 */       curToken.setWeight(1.0D / termList.size());
/* 61 */       arrCollectionProb[i] = (this.bkgCoeffi * this.indexReader.getIRTerm(curToken.getIndex()).getFrequency() / collectionTermCount);
/*    */     }
/*    */ 
/* 65 */     for (i = 0; i < iterationNum; i++) {
/* 66 */       double weightSum = 0.0D;
/* 67 */       for (int j = 0; j < termList.size(); j++) {
/* 68 */         Token curToken = (Token)termList.get(j);
/* 69 */         arrProb[j] = ((1.0D - this.bkgCoeffi) * curToken.getWeight() / ((1.0D - this.bkgCoeffi) * curToken.getWeight() + arrCollectionProb[j]) * curToken.getFrequency());
/* 70 */         weightSum += arrProb[j];
/*    */       }
/* 72 */       for (int j = 0; j < termList.size(); j++) {
/* 73 */         ((Token)termList.get(j)).setWeight(arrProb[j] / weightSum);
/*    */       }
/*    */     }
/* 76 */     TopicSummary summary = new TopicSummary(1);
/* 77 */     int termNum = Math.min(termList.size(), maxLength);
/* 78 */     for (i = 0; i < termNum; i++) {
/* 79 */       Token curToken = (Token)termList.get(i);
/* 80 */       TextUnit curTerm = new TextUnit(this.indexReader.getTermKey(curToken.getIndex()), curToken.getIndex(), curToken.getWeight());
/* 81 */       summary.addText(curTerm);
/*    */     }
/* 83 */     summary.sortByWegiht();
/* 84 */     return summary;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.summarize.TermGenerativeSum
 * JD-Core Version:    0.6.2
 */