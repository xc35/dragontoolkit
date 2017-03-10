/*    */ package edu.drexel.cis.dragon.qa.filter;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public class BasicEntityFilter extends AbstractFilter
/*    */ {
/*    */   private StopWordEntityFilter stopFilter;
/*    */   private Pattern numPattern;
/*    */ 
/*    */   public BasicEntityFilter(String stopwordFile)
/*    */   {
/* 14 */     this.stopFilter = new StopWordEntityFilter(stopwordFile);
/* 15 */     this.numPattern = Pattern.compile("(^([0-9]+)(\\W)?)+");
/*    */   }
/*    */ 
/*    */   public boolean keep(QuestionQuery query, Candidate term)
/*    */   {
/* 21 */     String first = term.getStartingWord().getContent();
/* 22 */     if ((term.getWordNum() == 1) && 
/* 23 */       (this.numPattern.matcher(first).find())) {
/* 24 */       return false;
/*    */     }
/* 26 */     if ((first.startsWith("AP")) || (first.startsWith("WSJ")) || (first.toLowerCase().startsWith("question")))
/* 27 */       return false;
/* 28 */     if (!isEntity(term))
/* 29 */       return false;
/* 30 */     return this.stopFilter.keep(query, term);
/*    */   }
/*    */ 
/*    */   private boolean isEntity(Candidate term)
/*    */   {
/* 37 */     Word cur = term.getStartingWord();
/* 38 */     if (!cur.isNumber())
/* 39 */       return true;
/* 40 */     cur = cur.next;
/* 41 */     int count = 1;
/* 42 */     while ((count < term.getWordNum()) && (cur.isInitialCapital())) {
/* 43 */       cur = cur.next;
/* 44 */       count++;
/*    */     }
/* 46 */     return count >= term.getWordNum();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.filter.BasicEntityFilter
 * JD-Core Version:    0.6.2
 */