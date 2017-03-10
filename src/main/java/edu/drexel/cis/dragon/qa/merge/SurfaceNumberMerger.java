/*    */ package edu.drexel.cis.dragon.qa.merge;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Token;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*    */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ 
/*    */ public class SurfaceNumberMerger
/*    */   implements CandidateMerger
/*    */ {
/*    */   private SimpleDictionary catDict;
/*    */ 
/*    */   public SurfaceNumberMerger()
/*    */   {
/* 18 */     this.catDict = new SimpleDictionary(true);
/* 19 */     this.catDict.add(".");
/* 20 */     this.catDict.add("-");
/* 21 */     this.catDict.add(":");
/* 22 */     this.catDict.add("/");
/* 23 */     this.catDict.add(",");
/* 24 */     this.catDict.add(")");
/* 25 */     this.catDict.add("per");
/* 26 */     this.catDict.add("of");
/*    */   }
/*    */ 
/*    */   public ArrayList merge(QuestionQuery query, ArrayList list)
/*    */   {
/* 36 */     SortedArray tokenList = new SortedArray();
/* 37 */     for (int i = 0; i < list.size(); i++) {
/* 38 */       Candidate curTerm = (Candidate)list.get(i);
/* 39 */       Token curToken = new Token(getSurfaceForm(curTerm));
/* 40 */       curToken.setMemo(curTerm);
/* 41 */       if (!tokenList.add(curToken)) {
/* 42 */         Token oldToken = (Token)tokenList.get(tokenList.insertedPos());
/* 43 */         Candidate oldTerm = (Candidate)oldToken.getMemo();
/* 44 */         oldTerm.merge(curTerm);
/*    */       }
/*    */     }
/*    */ 
/* 48 */     ArrayList newList = new ArrayList(tokenList.size());
/* 49 */     for (int i = 0; i < tokenList.size(); i++)
/* 50 */       newList.add(((Token)tokenList.get(i)).getMemo());
/* 51 */     Collections.sort(newList, new WeightComparator(true));
/* 52 */     return newList;
/*    */   }
/*    */ 
/*    */   protected String getSurfaceForm(Candidate cand)
/*    */   {
/* 59 */     Word start = cand.getStartingWord();
/* 60 */     Word end = cand.getEndingWord();
/* 61 */     StringBuffer name = new StringBuffer(start.getContent());
/* 62 */     while ((start != null) && (!start.equals(end))) {
/* 63 */       start = start.next;
/* 64 */       if (!this.catDict.exist(start.getContent()))
/* 65 */         name.append(start.getContent());
/*    */     }
/* 67 */     return name.toString();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.merge.SurfaceNumberMerger
 * JD-Core Version:    0.6.2
 */