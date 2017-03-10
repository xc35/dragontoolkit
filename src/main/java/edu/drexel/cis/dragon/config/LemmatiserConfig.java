/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*    */ import edu.drexel.cis.dragon.nlp.tool.PorterStemmer;
/*    */ import edu.drexel.cis.dragon.nlp.tool.WordNetDidion;
/*    */ import edu.drexel.cis.dragon.nlp.tool.lemmatiser.EngLemmatiser;
/*    */ 
/*    */ public class LemmatiserConfig extends ConfigUtil
/*    */ {
/*    */   public LemmatiserConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public LemmatiserConfig(ConfigureNode root)
/*    */   {
/* 21 */     super(root);
/*    */   }
/*    */ 
/*    */   public LemmatiserConfig(String configFile) {
/* 25 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public Lemmatiser getLemmatiser(int lemmatiserID) {
/* 29 */     return getLemmatiser(this.root, lemmatiserID);
/*    */   }
/*    */ 
/*    */   public Lemmatiser getLemmatiser(ConfigureNode node, int lemmatiserID) {
/* 33 */     return loadLemmatiser(node, lemmatiserID);
/*    */   }
/*    */ 
/*    */   private Lemmatiser loadLemmatiser(ConfigureNode node, int lemmatiserID)
/*    */   {
/* 40 */     ConfigureNode lemmatiserNode = getConfigureNode(node, "lemmatiser", lemmatiserID);
/* 41 */     if (lemmatiserNode == null)
/* 42 */       return null;
/* 43 */     String lemmatiserName = lemmatiserNode.getNodeName();
/* 44 */     return loadLemmatiser(lemmatiserName, lemmatiserNode);
/*    */   }
/*    */ 
/*    */   protected Lemmatiser loadLemmatiser(String lemmatiserName, ConfigureNode lemmatiserNode) {
/* 48 */     if (lemmatiserName.equalsIgnoreCase("PorterStemmer"))
/* 49 */       return new PorterStemmer();
/* 50 */     if (lemmatiserName.equalsIgnoreCase("WordNetDidion"))
/* 51 */       return loadWordNetDidion(lemmatiserNode);
/* 52 */     if (lemmatiserName.equalsIgnoreCase("EngLemmatiser")) {
/* 53 */       return loadEngLemmatiser(lemmatiserNode);
/*    */     }
/* 55 */     return (Lemmatiser)loadResource(lemmatiserNode);
/*    */   }
/*    */ 
/*    */   private Lemmatiser loadEngLemmatiser(ConfigureNode node)
/*    */   {
/* 63 */     boolean indexLookupOption = false;
/* 64 */     boolean disableVerbAdjective = true;
/* 65 */     String directory = node.getString("directory", null);
/* 66 */     indexLookupOption = node.getBoolean("indexlookupoption", indexLookupOption);
/* 67 */     disableVerbAdjective = node.getBoolean("disableverbadjective", disableVerbAdjective);
/* 68 */     if (directory == null) {
/* 69 */       return new EngLemmatiser(indexLookupOption, disableVerbAdjective);
/*    */     }
/* 71 */     return new EngLemmatiser(directory, indexLookupOption, disableVerbAdjective);
/*    */   }
/*    */ 
/*    */   private Lemmatiser loadWordNetDidion(ConfigureNode node)
/*    */   {
/* 77 */     String directory = node.getString("directory", null);
/* 78 */     if (directory == null) {
/* 79 */       return new WordNetDidion();
/*    */     }
/* 81 */     return new WordNetDidion(directory);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.LemmatiserConfig
 * JD-Core Version:    0.6.2
 */