/*    */ package edu.drexel.cis.dragon.ir.summarize;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.index.IRRelation;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ import edu.drexel.cis.dragon.nlp.Token;
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class RelationGenerativeSum extends AbstractStructureSum
/*    */   implements StructureSummarizer
/*    */ {
/*    */   private double bkgCoeffi;
/*    */ 
/*    */   public RelationGenerativeSum(IndexReader indexReader, double bkgCoefficient)
/*    */   {
/* 22 */     super(indexReader);
/* 23 */     this.bkgCoeffi = bkgCoefficient;
/*    */   }
/*    */ 
/*    */   public TopicSummary summarize(ArrayList docSet, int maxLength)
/*    */   {
/* 40 */     int docNum = docSet.size();
/* 41 */     SortedArray relationList = new SortedArray(new IndexComparator());
/* 42 */     for (int i = 0; i < docNum; i++) {
/* 43 */       IRDoc curDoc = (IRDoc)docSet.get(i);
/* 44 */       int[] arrIndex = this.indexReader.getRelationIndexList(curDoc.getIndex());
/* 45 */       int[] arrFreq = this.indexReader.getRelationFrequencyList(curDoc.getIndex());
/* 46 */       for (int j = 0; j < arrIndex.length; j++) {
/* 47 */         Token curToken = new Token(null);
/* 48 */         curToken.setIndex(arrIndex[j]);
/* 49 */         curToken.setFrequency(arrFreq[j]);
/* 50 */         if (!relationList.add(curToken)) {
/* 51 */           ((Token)relationList.get(relationList.insertedPos())).addFrequency(curToken.getFrequency());
/*    */         }
/*    */       }
/*    */ 
/*    */     }
/*    */       int i;
/* 57 */     int iterationNum = 15;
/* 58 */     double[] arrProb = new double[relationList.size()];
/* 59 */     double[] arrCollectionProb = new double[relationList.size()];
/* 60 */     double collectionRelationCount = this.indexReader.getCollection().getTermCount();
/* 61 */     for (i = 0; i < relationList.size(); i++) {
/* 62 */       Token curToken = (Token)relationList.get(i);
/* 63 */       curToken.setWeight(1.0D / relationList.size());
/* 64 */       arrCollectionProb[i] = (this.bkgCoeffi * this.indexReader.getIRRelation(curToken.getIndex()).getFrequency() / collectionRelationCount);
/*    */     }
/*    */ 
/* 68 */     for (i = 0; i < iterationNum; i++) {
/* 69 */       double weightSum = 0.0D;
/* 70 */       for (int j = 0; j < relationList.size(); j++) {
/* 71 */         Token curToken = (Token)relationList.get(j);
/* 72 */         arrProb[j] = ((1.0D - this.bkgCoeffi) * curToken.getWeight() / ((1.0D - this.bkgCoeffi) * curToken.getWeight() + arrCollectionProb[j]) * curToken.getFrequency());
/* 73 */         weightSum += arrProb[j];
/*    */       }
/* 75 */       for (int j = 0; j < relationList.size(); j++) {
/* 76 */         ((Token)relationList.get(j)).setWeight(arrProb[j] / weightSum);
/*    */       }
/*    */     }
/* 79 */     TopicSummary summary = new TopicSummary(2);
/* 80 */     int relationNum = Math.min(relationList.size(), maxLength);
/* 81 */     for (i = 0; i < relationNum; i++) {
/* 82 */       Token curToken = (Token)relationList.get(i);
/* 83 */       IRRelation curRelation = this.indexReader.getIRRelation(curToken.getIndex());
/* 84 */       String curText = this.indexReader.getTermKey(curRelation.getFirstTerm()) + "<->" + this.indexReader.getTermKey(curRelation.getSecondTerm());
/* 85 */       TextUnit curTextUnit = new TextUnit(curText, curToken.getIndex(), curToken.getWeight());
/* 86 */       summary.addText(curTextUnit);
/*    */     }
/* 88 */     summary.sortByWegiht();
/* 89 */     return summary;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.summarize.RelationGenerativeSum
 * JD-Core Version:    0.6.2
 */