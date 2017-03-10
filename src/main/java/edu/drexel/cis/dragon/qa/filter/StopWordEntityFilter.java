/*    */ package edu.drexel.cis.dragon.qa.filter;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*    */ import edu.drexel.cis.dragon.util.EnvVariable;
/*    */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*    */ 
/*    */ public class StopWordEntityFilter extends AbstractFilter
/*    */ {
/*    */   private SimpleDictionary stopDict;
/*    */ 
/*    */   public StopWordEntityFilter(String stopwordFile)
/*    */   {
/* 11 */     if (stopwordFile == null)
/* 12 */       stopwordFile = EnvVariable.getDragonHome() + "/nlpdata/qa/entity.stopword";
/* 13 */     this.stopDict = new SimpleDictionary(stopwordFile);
/*    */   }
/*    */ 
/*    */   public boolean keep(QuestionQuery query, Candidate term)
/*    */   {
/* 19 */     if ((term.getWordNum() == 1) && (term.getStartingWord().getContent().length() == 1))
/* 20 */       return false;
/* 21 */     boolean ret = this.stopDict.exist(term.toString());
/* 22 */     if ((!ret) && (term.getStartingWord().getLemma() != null))
/* 23 */       ret = this.stopDict.exist(term.toLemmaString());
/* 24 */     if ((!ret) && (term.getWordNum() == 1) && (term.getEndingWord().getContent().endsWith("."))) {
/* 25 */       String word = term.getEndingWord().getContent();
/* 26 */       ret = this.stopDict.exist(word.substring(0, word.length() - 1));
/*    */     }
/* 28 */     return !ret;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.filter.StopWordEntityFilter
 * JD-Core Version:    0.6.2
 */