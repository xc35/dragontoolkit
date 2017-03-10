/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.extract.BasicTripleExtractor;
/*    */ import edu.drexel.cis.dragon.nlp.extract.ConceptFilter;
/*    */ import edu.drexel.cis.dragon.nlp.extract.TermExtractor;
/*    */ import edu.drexel.cis.dragon.nlp.extract.TripleExtractor;
/*    */ 
/*    */ public class TripleExtractorConfig extends ConfigUtil
/*    */ {
/*    */   public TripleExtractorConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public TripleExtractorConfig(ConfigureNode root)
/*    */   {
/* 20 */     super(root);
/*    */   }
/*    */ 
/*    */   public TripleExtractorConfig(String configFile) {
/* 24 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public TripleExtractor getTripleExtractor(int tripleExtractorID) {
/* 28 */     return getTripleExtractor(this.root, tripleExtractorID);
/*    */   }
/*    */ 
/*    */   public TripleExtractor getTripleExtractor(ConfigureNode node, int tripleExtractorID) {
/* 32 */     return loadTripleExtractor(node, tripleExtractorID);
/*    */   }
/*    */ 
/*    */   private TripleExtractor loadTripleExtractor(ConfigureNode node, int tripleExtractorID)
/*    */   {
/* 39 */     ConfigureNode tripleExtractorNode = getConfigureNode(node, "tripleextractor", tripleExtractorID);
/* 40 */     if (tripleExtractorNode == null)
/* 41 */       return null;
/* 42 */     String tripleExtractorName = tripleExtractorNode.getNodeName();
/* 43 */     return loadTripleExtractor(tripleExtractorName, tripleExtractorNode);
/*    */   }
/*    */ 
/*    */   protected TripleExtractor loadTripleExtractor(String tripleExtractorName, ConfigureNode tripleExtractorNode) {
/* 47 */     if (tripleExtractorName.equalsIgnoreCase("BasicTripleExtractor")) {
/* 48 */       return loadBasicTripleExtractor(tripleExtractorNode);
/*    */     }
/* 50 */     return (TripleExtractor)loadResource(tripleExtractorNode);
/*    */   }
/*    */ 
/*    */   private TripleExtractor loadBasicTripleExtractor(ConfigureNode curNode)
/*    */   {
/* 62 */     int extractorID = curNode.getInt("conceptextractor", 0);
/* 63 */     int filterID = curNode.getInt("conceptfilter", 0);
/* 64 */     boolean filterOption = curNode.getBoolean("filteroption", true);
/* 65 */     boolean relationCheck = curNode.getBoolean("relationcheck", true);
/* 66 */     boolean coreference = curNode.getBoolean("coreference", false);
/* 67 */     boolean coordinatingCheck = curNode.getBoolean("coordinatingcheck", false);
/* 68 */     boolean semanticCheck = curNode.getBoolean("semanticcheck", true);
/* 69 */     boolean clauseIdentify = curNode.getBoolean("clauseidentify", true);
/*    */ 
/* 71 */     ConceptExtractorConfig extractorConfig = new ConceptExtractorConfig();
/* 72 */     TermExtractor extractor = (TermExtractor)extractorConfig.getConceptExtractor(curNode, extractorID);
/* 73 */     ConceptFilterConfig filterConfig = new ConceptFilterConfig();
/* 74 */     ConceptFilter filter = filterConfig.getConceptFilter(curNode, filterID);
/* 75 */     BasicTripleExtractor tripleExtractor = new BasicTripleExtractor(extractor);
/* 76 */     tripleExtractor.setConceptFilter(filter);
/* 77 */     tripleExtractor.setFilteringOption(filterOption);
/* 78 */     tripleExtractor.setCoordinatingCheckOption(coordinatingCheck);
/* 79 */     tripleExtractor.setRelationCheckOption(relationCheck);
/* 80 */     tripleExtractor.setSemanticCheckOption(semanticCheck);
/* 81 */     tripleExtractor.setCoReferenceOption(coreference);
/* 82 */     tripleExtractor.setClauseIdentifyOption(clauseIdentify);
/* 83 */     return tripleExtractor;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.TripleExtractorConfig
 * JD-Core Version:    0.6.2
 */