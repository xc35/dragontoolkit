/*    */ package edu.drexel.cis.dragon.qa.rank;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*    */ import edu.drexel.cis.dragon.qa.system.CandidateBase;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ 
/*    */ public class TFIDFRanker extends AbstractRanker
/*    */ {
/*    */   public ArrayList rank(QuestionQuery query, CandidateBase base, ArrayList list)
/*    */   {
/* 19 */     IDFBuilder idfBuilder = new IDFBuilder();
/* 20 */     for (int i = 0; i < base.getDocumentNum(); i++) {
/* 21 */       idfBuilder.add(base.getDocument(i));
/*    */     }
/* 23 */     list = getTopCandidate(list);
/* 24 */     for (int i = 0; i < list.size(); i++) {
/* 25 */       double sum = 0.0D;
/* 26 */       Candidate curCand = (Candidate)list.get(i);
/* 27 */       ArrayList varList = curCand.getVariants();
/* 28 */       if (varList == null)
/* 29 */         sum = computeTFIDF(idfBuilder, curCand);
/*    */       else {
/* 31 */         for (int j = 0; j < varList.size(); j++) {
/* 32 */           Candidate var = (Candidate)varList.get(j);
/* 33 */           sum += computeTFIDF(idfBuilder, var);
/*    */         }
/*    */       }
/* 36 */       curCand.setWeight(sum);
/*    */     }
/* 38 */     Collections.sort(list, new WeightComparator(true));
/* 39 */     return list;
/*    */   }
/*    */ 
/*    */   private double computeTFIDF(IDFBuilder idfBuilder, Candidate curCand)
/*    */   {
/* 47 */     int len = curCand.getWordNum();
/* 48 */     Word cur = curCand.getStartingWord();
/* 49 */     int step = 0;
/* 50 */     double sum = 0.0D;
/* 51 */     int count = 0;
/* 52 */     while (step < len) {
/* 53 */       if (!cur.isPunctuation()) {
/* 54 */         sum += idfBuilder.getIDF(cur.getContent());
/* 55 */         step++;
/* 56 */         count++;
/*    */       }
/* 58 */       cur = cur.next;
/*    */     }
/* 60 */     return curCand.getInitialFrequency() * sum / count;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.rank.TFIDFRanker
 * JD-Core Version:    0.6.2
 */