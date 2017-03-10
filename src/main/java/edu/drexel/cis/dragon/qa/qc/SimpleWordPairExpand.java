/*    */ package edu.drexel.cis.dragon.qa.qc;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.tool.xtract.EngWordPairExpand;
/*    */ 
/*    */ public class SimpleWordPairExpand extends EngWordPairExpand
/*    */ {
/*    */   public SimpleWordPairExpand(int maxSpan, String indexFolder, double threshold)
/*    */   {
/* 18 */     super(maxSpan, indexFolder, threshold);
/*    */   }
/*    */ 
/*    */   protected boolean checkValidation(String word, int posIndex)
/*    */   {
/* 23 */     return true;
/*    */   }
/*    */ 
/*    */   protected boolean checkEndingWordValidation(String word, int posIndex) {
/* 27 */     return true;
/*    */   }
/*    */ 
/*    */   protected boolean checkStartingWordValidation(String word, int posIndex) {
/* 31 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.qc.SimpleWordPairExpand
 * JD-Core Version:    0.6.2
 */