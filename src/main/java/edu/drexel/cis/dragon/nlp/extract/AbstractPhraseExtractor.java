/*    */ package edu.drexel.cis.dragon.nlp.extract;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.ontology.Vocabulary;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*    */ 
/*    */ public abstract class AbstractPhraseExtractor extends AbstractConceptExtractor
/*    */   implements PhraseExtractor
/*    */ {
/*    */   protected Lemmatiser lemmatiser;
/*    */   protected Tagger tagger;
/*    */   protected Vocabulary vocabulary;
/*    */   protected boolean useNoun;
/*    */   protected boolean useAdj;
/*    */   protected boolean useVerb;
/*    */ 
/*    */   public AbstractPhraseExtractor(Vocabulary vocabulary, Tagger tagger, Lemmatiser lemmatiser)
/*    */   {
/* 22 */     this.lemmatiser = lemmatiser;
/* 23 */     this.tagger = tagger;
/* 24 */     this.vocabulary = vocabulary;
/* 25 */     this.useNoun = false;
/* 26 */     this.useAdj = false;
/* 27 */     this.useVerb = false;
/*    */   }
/*    */ 
/*    */   public void setSingleNounOption(boolean option) {
/* 31 */     this.useNoun = option;
/*    */   }
/*    */ 
/*    */   public boolean getSingleNounOption() {
/* 35 */     return this.useNoun;
/*    */   }
/*    */ 
/*    */   public void setSingleVerbOption(boolean option) {
/* 39 */     this.useVerb = option;
/*    */   }
/*    */ 
/*    */   public boolean getSingleVerbOption() {
/* 43 */     return this.useVerb;
/*    */   }
/*    */ 
/*    */   public void setSingleAdjectiveOption(boolean option) {
/* 47 */     this.useAdj = option;
/*    */   }
/*    */ 
/*    */   public boolean getSingleAdjectiveOption() {
/* 51 */     return this.useAdj;
/*    */   }
/*    */ 
/*    */   public boolean supportConceptName() {
/* 55 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean supportConceptEntry() {
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   public Lemmatiser getLemmatiser() {
/* 63 */     return this.lemmatiser;
/*    */   }
/*    */ 
/*    */   public void setLemmatiser(Lemmatiser lemmatiser) {
/* 67 */     this.lemmatiser = lemmatiser;
/*    */   }
/*    */ 
/*    */   public Tagger getPOSTagger() {
/* 71 */     return this.tagger;
/*    */   }
/*    */ 
/*    */   public Vocabulary getVocabulary() {
/* 75 */     return this.vocabulary;
/*    */   }
/*    */ 
/*    */   public void initDocExtraction()
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.AbstractPhraseExtractor
 * JD-Core Version:    0.6.2
 */