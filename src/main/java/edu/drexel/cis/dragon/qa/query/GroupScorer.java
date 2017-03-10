/*    */ package edu.drexel.cis.dragon.qa.query;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ 
/*    */ public class GroupScorer
/*    */   implements QueryScorer
/*    */ {
/*    */   public void score(QuestionQuery query)
/*    */   {
/* 15 */     double totalScore = 0.0D;
					int j;
/* 16 */     for (int i = 0; i < query.size(); i++) {
/* 17 */       QueryWord cur = query.getQueryWord(i);
/* 18 */       int groupID = cur.getGroupID();
/* 19 */       int count = 0;
/*    */ 
/* 21 */       for ( j = i; j < query.size(); j++) {
/* 22 */         cur = query.getQueryWord(j);
/* 23 */         if (cur.getGroupID() != groupID)
/*    */           break;
/* 25 */         if (!cur.isFunctionalWord()) {
/* 26 */           count++;
/*    */         }
/*    */       }
/* 29 */       if (count > 0) {
/* 30 */         for (int k = i; k < j; k++) {
/* 31 */           cur = query.getQueryWord(k);
/* 32 */           if (cur.isFunctionalWord()) {
/* 33 */             cur.setWeight(0.0D);
/*    */           } else {
/* 35 */             cur.setWeight(1.0D / count);
/* 36 */             if (cur.isYear())
/* 37 */               cur.setWeight(1.0D);
/*    */           }
/* 39 */           totalScore += cur.getWeight();
/*    */         }
/*    */       }
/* 42 */       i = j - 1;
/*    */     }
/*    */ 
/* 46 */     for (int i = 0; i < query.size(); i++)
/* 47 */       query.getQueryWord(i).setWeight(query.getQueryWord(i).getWeight() / totalScore);
/*    */   }
/*    */ 
/*    */   public static int divideSentence(Sentence sent)
/*    */   {
/* 54 */     int groupNum = 0;
/* 55 */     Word cur = sent.getFirstWord();
/* 56 */     while (cur != null) {
/* 57 */       if (cur.getContent().equals("\"")) {
/* 58 */         if (cur.prev != null)
/* 59 */           groupNum++;
/* 60 */         while ((cur != null) && (!cur.equals("\""))) {
/* 61 */           cur.setClauseID(groupNum);
/* 62 */           cur = cur.next;
/*    */         }
/* 64 */         if (cur == null) break;
/* 65 */         cur.setClauseID(groupNum);
/* 66 */         if (cur.next != null) {
/* 67 */           groupNum++;
/*    */         }
/*    */ 
/*    */       }
/* 72 */       else if ((" how what when which whose whom where that then who while but whereas although because since though ' ".indexOf(" " + cur.getContent().toLowerCase() + " ") >= 0) || 
/* 73 */         (cur.getPOSIndex() == 5)) {
/* 74 */         if (cur.prev != null)
/* 75 */           groupNum++;
/* 76 */         cur.setClauseID(groupNum);
/*    */       }
/* 78 */       else if (cur.getPOSIndex() == 2) {
/* 79 */         if (cur.prev != null)
/* 80 */           groupNum++;
/* 81 */         cur.setClauseID(groupNum);
/* 82 */         if (cur.next != null)
/* 83 */           groupNum++;
/*    */       }
/*    */       else {
/* 86 */         cur.setClauseID(groupNum);
/* 87 */       }cur = cur.next;
/*    */     }
/* 89 */     return groupNum + 1;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.query.GroupScorer
 * JD-Core Version:    0.6.2
 */