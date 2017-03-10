/*    */ package edu.drexel.cis.dragon.nlp.extract;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*    */ 
/*    */ public abstract class AbstractTokenExtractor extends AbstractConceptExtractor
/*    */   implements TokenExtractor
/*    */ {
/*    */   protected Lemmatiser lemmatiser;
/*    */ 
/*    */   public AbstractTokenExtractor(Lemmatiser lemmatiser)
/*    */   {
/* 18 */     this.lemmatiser = lemmatiser;
/* 19 */     this.cf = null;
/* 20 */     this.conceptFilter_enabled = false;
/*    */   }
/*    */ 
/*    */   public boolean supportConceptName() {
/* 24 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean supportConceptEntry() {
/* 28 */     return false;
/*    */   }
/*    */ 
/*    */   public Lemmatiser getLemmatiser() {
/* 32 */     return this.lemmatiser;
/*    */   }
/*    */ 
/*    */   public void setLemmatiser(Lemmatiser lemmatiser) {
/* 36 */     this.lemmatiser = lemmatiser;
/*    */   }
/*    */ 
/*    */   public void initDocExtraction()
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.AbstractTokenExtractor
 * JD-Core Version:    0.6.2
 */