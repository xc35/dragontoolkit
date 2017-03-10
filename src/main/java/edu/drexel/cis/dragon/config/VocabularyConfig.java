/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.ontology.BasicVocabulary;
/*    */ import edu.drexel.cis.dragon.nlp.ontology.Vocabulary;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*    */ 
/*    */ public class VocabularyConfig extends ConfigUtil
/*    */ {
/*    */   public VocabularyConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public VocabularyConfig(ConfigureNode root)
/*    */   {
/* 20 */     super(root);
/*    */   }
/*    */ 
/*    */   public VocabularyConfig(String configFile) {
/* 24 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public Vocabulary getVocabulary(int vocabularyID) {
/* 28 */     return getVocabulary(this.root, vocabularyID);
/*    */   }
/*    */ 
/*    */   public Vocabulary getVocabulary(ConfigureNode node, int vocabularyID) {
/* 32 */     return loadVocabulary(node, vocabularyID);
/*    */   }
/*    */ 
/*    */   private Vocabulary loadVocabulary(ConfigureNode node, int vocabularyID)
/*    */   {
/* 38 */     ConfigureNode vocabularyNode = getConfigureNode(node, "vocabulary", vocabularyID);
/* 39 */     if (vocabularyNode == null)
/* 40 */       return null;
/* 41 */     String vocabularyName = vocabularyNode.getNodeName();
/* 42 */     return loadVocabulary(vocabularyName, vocabularyNode);
/*    */   }
/*    */ 
/*    */   protected Vocabulary loadVocabulary(String vocabularyName, ConfigureNode vocabularyNode) {
/* 46 */     if (vocabularyName.equalsIgnoreCase("BasicVocabulary")) {
/* 47 */       return loadBasicVocabulary(vocabularyNode);
/*    */     }
/* 49 */     return (Vocabulary)loadResource(vocabularyNode);
/*    */   }
/*    */ 
/*    */   private Vocabulary loadBasicVocabulary(ConfigureNode curNode)
/*    */   {
/* 60 */     String vobFile = curNode.getString("vobfile");
/* 61 */     int lemmatiserID = curNode.getInt("lemmatiser", 0);
/* 62 */     boolean lemmaOption = curNode.getBoolean("lemmaoption", false);
/* 63 */     boolean nppOption = curNode.getBoolean("nppoption", false);
/* 64 */     boolean coordinateOption = curNode.getBoolean("coordinateoption", false);
/* 65 */     boolean adjtermOption = curNode.getBoolean("adjtermoption", false);
/* 66 */     String nonBoundaryPunc = curNode.getString("nonboundarypunctuation", "");
/*    */ 
/* 68 */     LemmatiserConfig lemmaConfig = new LemmatiserConfig();
/* 69 */     Lemmatiser lemmatiser = lemmaConfig.getLemmatiser(curNode, lemmatiserID);
/* 70 */     BasicVocabulary vocabulary = new BasicVocabulary(vobFile, lemmatiser);
/* 71 */     vocabulary.setLemmaOption(lemmaOption);
/* 72 */     vocabulary.setAdjectivePhraseOption(adjtermOption);
/* 73 */     vocabulary.setCoordinateOption(coordinateOption);
/* 74 */     vocabulary.setNPPOption(nppOption);
/* 75 */     vocabulary.setNonBoundaryPunctuation(nonBoundaryPunc);
/* 76 */     return vocabulary;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.VocabularyConfig
 * JD-Core Version:    0.6.2
 */