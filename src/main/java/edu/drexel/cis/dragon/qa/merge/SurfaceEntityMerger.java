/*    */ package edu.drexel.cis.dragon.qa.merge;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Token;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ 
/*    */ public class SurfaceEntityMerger
/*    */   implements CandidateMerger
/*    */ {
/*    */   private Lemmatiser lemmatiser;
/*    */ 
/*    */   public SurfaceEntityMerger(Lemmatiser lemmatiser)
/*    */   {
/* 18 */     this.lemmatiser = lemmatiser;
/*    */   }
/*    */ 
/*    */   public ArrayList merge(QuestionQuery query, ArrayList list)
/*    */   {
/* 28 */     SortedArray tokenList = new SortedArray();
/* 29 */     for (int i = 0; i < list.size(); i++) {
/* 30 */       Candidate curTerm = (Candidate)list.get(i);
/* 31 */       Token curToken = new Token(getSurfaceForm(curTerm));
/* 32 */       curToken.setMemo(curTerm);
/* 33 */       if (!tokenList.add(curToken)) {
/* 34 */         Token oldToken = (Token)tokenList.get(tokenList.insertedPos());
/* 35 */         Candidate oldTerm = (Candidate)oldToken.getMemo();
/* 36 */         if (curTerm.getWordNum() > oldTerm.getWordNum()) {
/* 37 */           curTerm.merge(oldTerm);
/* 38 */           oldToken.setMemo(curTerm);
/*    */         }
/*    */         else {
/* 41 */           oldTerm.merge(curTerm);
/*    */         }
/*    */       }
/*    */     }
/* 45 */     ArrayList newList = new ArrayList(tokenList.size());
/* 46 */     for (int i = 0; i < tokenList.size(); i++)
/* 47 */       newList.add(((Token)tokenList.get(i)).getMemo());
/* 48 */     Collections.sort(newList, new WeightComparator(true));
/* 49 */     return newList;
/*    */   }
/*    */ 
/*    */   protected String getSurfaceForm(Candidate cand)
/*    */   {
/* 56 */     Word start = cand.getStartingWord();
/* 57 */     Word end = cand.getEndingWord();
/* 58 */     StringBuffer name = new StringBuffer(getString(start));
/* 59 */     while ((start != null) && (!start.equals(end))) {
/* 60 */       start = start.next;
/* 61 */       name.append(getString(start));
/*    */     }
/* 63 */     return name.toString().replaceAll("['-\\.]", "");
/*    */   }
/*    */ 
/*    */   private String getString(Word word)
/*    */   {
/* 68 */     if (this.lemmatiser == null) {
/* 69 */       return word.getContent().toLowerCase();
/*    */     }
/* 71 */     if (word.getLemma() == null)
/* 72 */       word.setLemma(this.lemmatiser.lemmatize(word.getContent(), word.getPOSIndex()));
/* 73 */     return word.getLemma();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.merge.SurfaceEntityMerger
 * JD-Core Version:    0.6.2
 */