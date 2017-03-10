/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.DocumentParser;
/*    */ import edu.drexel.cis.dragon.nlp.extract.EngDocumentParser;
/*    */ 
/*    */ public class DocumentParserConfig extends ConfigUtil
/*    */ {
/*    */   public DocumentParserConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public DocumentParserConfig(ConfigureNode root)
/*    */   {
/* 21 */     super(root);
/*    */   }
/*    */ 
/*    */   public DocumentParserConfig(String configFile) {
/* 25 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public DocumentParser getDocumentParser(int parserID) {
/* 29 */     return getDocumentParser(this.root, parserID);
/*    */   }
/*    */ 
/*    */   public DocumentParser getDocumentParser(ConfigureNode node, int parserID) {
/* 33 */     return loadDocumentParser(node, parserID);
/*    */   }
/*    */ 
/*    */   private DocumentParser loadDocumentParser(ConfigureNode node, int parserID)
/*    */   {
/* 40 */     ConfigureNode parserNode = getConfigureNode(node, "documentparser", parserID);
/* 41 */     if (parserNode == null)
/* 42 */       return null;
/* 43 */     String parserName = parserNode.getNodeName();
/* 44 */     return loadDocumentParser(parserName, parserNode);
/*    */   }
/*    */ 
/*    */   protected DocumentParser loadDocumentParser(String parserName, ConfigureNode parserNode) {
/* 48 */     if (parserName.equalsIgnoreCase("EngDocumentParser")) {
/* 49 */       return loadEngDocumentParser(parserNode);
/*    */     }
/* 51 */     return (DocumentParser)loadResource(parserNode);
/*    */   }
/*    */ 
/*    */   private DocumentParser loadEngDocumentParser(ConfigureNode node) {
/* 55 */     return new EngDocumentParser(getWordDelimitor(node.getString("notworddelimitor", "")));
/*    */   }
/*    */ 
/*    */   private String getWordDelimitor(String notWordDelimitor)
/*    */   {
/* 63 */     StringBuffer sb = new StringBuffer();
/* 64 */     String delimitors = " \r\n\t_-.;,?/\"'`:(){}!+[]><=%$#*@&^~|\\";
/* 65 */     if ((notWordDelimitor == null) && (notWordDelimitor.length() == 0))
/* 66 */       return delimitors;
/* 67 */     for (int i = 0; i < delimitors.length(); i++) {
/* 68 */       if (notWordDelimitor.indexOf(delimitors.charAt(i)) < 0)
/* 69 */         sb.append(delimitors.charAt(i));
/*    */     }
/* 71 */     return sb.toString();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.DocumentParserConfig
 * JD-Core Version:    0.6.2
 */