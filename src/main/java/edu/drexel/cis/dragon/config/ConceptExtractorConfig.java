/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.DocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.Phrase;
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.extract.BasicPhraseExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.BasicTermExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.BasicTokenExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptFilter;
/*     */ import edu.drexel.cis.dragon.nlp.extract.EngDocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.Ontology;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.Vocabulary;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ 
/*     */ public class ConceptExtractorConfig extends ConfigUtil
/*     */ {
/*     */   public ConceptExtractorConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ConceptExtractorConfig(ConfigureNode root)
/*     */   {
/*  24 */     super(root);
/*     */   }
/*     */ 
/*     */   public ConceptExtractorConfig(String configFile) {
/*  28 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public ConceptExtractor getConceptExtractor(int extractorID) {
/*  32 */     return loadConceptExtractor(this.root, extractorID);
/*     */   }
/*     */ 
/*     */   public ConceptExtractor getConceptExtractor(ConfigureNode node, int extractorID) {
/*  36 */     return loadConceptExtractor(node, extractorID);
/*     */   }
/*     */ 
/*     */   private ConceptExtractor loadConceptExtractor(ConfigureNode node, int extractorID)
/*     */   {
/*  43 */     ConfigureNode extractorNode = getConfigureNode(node, "conceptextractor", extractorID);
/*  44 */     if (extractorNode == null)
/*  45 */       return null;
/*  46 */     String extractorName = extractorNode.getNodeName();
/*  47 */     return loadConceptExtractor(extractorName, extractorNode);
/*     */   }
/*     */ 
/*     */   protected ConceptExtractor loadConceptExtractor(String extractorName, ConfigureNode extractorNode) {
/*  51 */     if (extractorName.equalsIgnoreCase("BasicTokenExtractor"))
/*  52 */       return loadBasicTokenExtractor(extractorNode);
/*  53 */     if (extractorName.equalsIgnoreCase("BasicPhraseExtractor"))
/*  54 */       return loadBasicPhraseExtractor(extractorNode);
/*  55 */     if (extractorName.equalsIgnoreCase("BasicTermExtractor")) {
/*  56 */       return loadBasicTermExtractor(extractorNode);
/*     */     }
/*  58 */     return (ConceptExtractor)loadResource(extractorNode);
/*     */   }
/*     */ 
/*     */   private ConceptExtractor loadBasicTokenExtractor(ConfigureNode curNode)
/*     */   {
/*  72 */     int parserID = curNode.getInt("documentparser", 0);
/*  73 */     int lemmatiserID = curNode.getInt("lemmatiser", 0);
/*  74 */     int filterID = curNode.getInt("conceptfilter", 0);
/*  75 */     boolean filterOption = curNode.getBoolean("filteroption", true);
/*  76 */     boolean subtermOption = curNode.getBoolean("subconceptoption", false);
/*  77 */     String wordDelimitor = getWordDelimitor(curNode.getString("notworddelimitor", ""));
/*     */     DocumentParser parser;
/*  79 */     if (parserID > 0)
/*  80 */       parser = new DocumentParserConfig().getDocumentParser(curNode, parserID);
/*     */     else
/*  82 */       parser = new EngDocumentParser(wordDelimitor);
/*  83 */     LemmatiserConfig lemmaConfig = new LemmatiserConfig();
/*  84 */     Lemmatiser lemmatiser = lemmaConfig.getLemmatiser(curNode, lemmatiserID);
/*  85 */     ConceptFilterConfig filterConfig = new ConceptFilterConfig();
/*  86 */     ConceptFilter filter = filterConfig.getConceptFilter(curNode, filterID);
/*  87 */     BasicTokenExtractor extractor = new BasicTokenExtractor(lemmatiser);
/*  88 */     extractor.setConceptFilter(filter);
/*  89 */     extractor.setSubConceptOption(subtermOption);
/*  90 */     extractor.setFilteringOption(filterOption);
/*  91 */     extractor.setDocumentParser(parser);
/*  92 */     return extractor;
/*     */   }
/*     */ 
/*     */   private ConceptExtractor loadBasicPhraseExtractor(ConfigureNode curNode)
/*     */   {
/* 110 */     int parserID = curNode.getInt("documentparser", 0);
/* 111 */     int lemmatiserID = curNode.getInt("lemmatiser", 0);
/* 112 */     int filterID = curNode.getInt("conceptfilter", 0);
/* 113 */     int vobID = curNode.getInt("vocabulary", 0);
/* 114 */     int taggerID = curNode.getInt("tagger", 0);
/* 115 */     boolean filterOption = curNode.getBoolean("filteroption", true);
/* 116 */     boolean subtermOption = curNode.getBoolean("subconceptoption", false);
/* 117 */     boolean overlappedPhrase = curNode.getBoolean("overlappedphrase", false);
/* 118 */     String wordDelimitor = getWordDelimitor(curNode.getString("notworddelimitor", ""));
/* 119 */     String nameMode = curNode.getString("conceptnamemode", "asis");
/* 120 */     boolean singleNoun = curNode.getBoolean("singlenounoption", true);
/* 121 */     boolean singleAdj = curNode.getBoolean("singleadjoption", true);
/* 122 */     boolean singleVerb = curNode.getBoolean("singleverboption", false);
/*     */     DocumentParser parser;
/* 124 */     if (parserID > 0)
/* 125 */       parser = new DocumentParserConfig().getDocumentParser(curNode, parserID);
/*     */     else
/* 127 */       parser = new EngDocumentParser(wordDelimitor);
/* 128 */     LemmatiserConfig lemmaConfig = new LemmatiserConfig();
/* 129 */     Lemmatiser lemmatiser = lemmaConfig.getLemmatiser(curNode, lemmatiserID);
/* 130 */     VocabularyConfig vobConfig = new VocabularyConfig();
/* 131 */     Vocabulary vob = vobConfig.getVocabulary(curNode, vobID);
/* 132 */     TaggerConfig taggerConfig = new TaggerConfig();
/* 133 */     Tagger tagger = taggerConfig.getTagger(curNode, taggerID);
/* 134 */     ConceptFilterConfig filterConfig = new ConceptFilterConfig();
/* 135 */     ConceptFilter filter = filterConfig.getConceptFilter(curNode, filterID);
/* 136 */     BasicPhraseExtractor extractor = new BasicPhraseExtractor(vob, lemmatiser, tagger, overlappedPhrase);
/* 137 */     extractor.setDocumentParser(parser);
/* 138 */     if (nameMode.equalsIgnoreCase("lemma"))
/* 139 */       Phrase.setNameMode(1);
/*     */     else
/* 141 */       Phrase.setNameMode(0);
/* 142 */     extractor.setConceptFilter(filter);
/* 143 */     extractor.setSubConceptOption(subtermOption);
/* 144 */     extractor.setFilteringOption(filterOption);
/* 145 */     extractor.setSingleAdjectiveOption(singleAdj);
/* 146 */     extractor.setSingleNounOption(singleNoun);
/* 147 */     extractor.setSingleVerbOption(singleVerb);
/* 148 */     return extractor;
/*     */   }
/*     */ 
/*     */   private ConceptExtractor loadBasicTermExtractor(ConfigureNode curNode)
/*     */   {
/* 167 */     int parserID = curNode.getInt("documentparser", 0);
/* 168 */     int lemmatiserID = curNode.getInt("lemmatiser", 0);
/* 169 */     int filterID = curNode.getInt("conceptfilter", 0);
/* 170 */     int ontologyID = curNode.getInt("ontology", 0);
/* 171 */     int taggerID = curNode.getInt("tagger", 0);
/* 172 */     boolean filterOption = curNode.getBoolean("filteroption", true);
/* 173 */     boolean subtermOption = curNode.getBoolean("subconceptoption", false);
/* 174 */     String wordDelimitor = getWordDelimitor(curNode.getString("notworddelimitor", ""));
/* 175 */     String nameMode = curNode.getString("conceptnamemode", "asis");
/* 176 */     boolean abbr = curNode.getBoolean("abbreviation", true);
/* 177 */     boolean semanticCheck = curNode.getBoolean("semanticcheck", true);
/* 178 */     boolean attributeCheck = curNode.getBoolean("attributecheck", false);
/* 179 */     boolean coordinatingCheck = curNode.getBoolean("coordinatingcheck", false);
/* 180 */     boolean compoundTermPrediction = curNode.getBoolean("compoundtermprediction", false);
/* 181 */     boolean coordinatingTermPrediction = curNode.getBoolean("coordinatingtermprediction", false);
/*     */     DocumentParser parser;
/* 183 */     if (parserID > 0)
/* 184 */       parser = new DocumentParserConfig().getDocumentParser(curNode, parserID);
/*     */     else
/* 186 */       parser = new EngDocumentParser(wordDelimitor);
/* 187 */     LemmatiserConfig lemmaConfig = new LemmatiserConfig();
/* 188 */     Lemmatiser lemmatiser = lemmaConfig.getLemmatiser(curNode, lemmatiserID);
/* 189 */     OntologyConfig ontologyConfig = new OntologyConfig();
/* 190 */     Ontology ontology = ontologyConfig.getOntology(curNode, ontologyID);
/* 191 */     TaggerConfig taggerConfig = new TaggerConfig();
/* 192 */     Tagger tagger = taggerConfig.getTagger(curNode, taggerID);
/* 193 */     ConceptFilterConfig filterConfig = new ConceptFilterConfig();
/* 194 */     ConceptFilter filter = filterConfig.getConceptFilter(curNode, filterID);
/* 195 */     BasicTermExtractor extractor = new BasicTermExtractor(ontology, lemmatiser, tagger);
/* 196 */     extractor.setDocumentParser(parser);
/* 197 */     if (nameMode.equalsIgnoreCase("norm"))
/* 198 */       Term.setNameMode(2);
/* 199 */     else if (nameMode.equalsIgnoreCase("lemma"))
/* 200 */       Term.setNameMode(1);
/*     */     else
/* 202 */       Term.setNameMode(0);
/* 203 */     extractor.setConceptFilter(filter);
/* 204 */     extractor.setSubConceptOption(subtermOption);
/* 205 */     extractor.setFilteringOption(filterOption);
/* 206 */     extractor.setAbbreviationOption(abbr);
/* 207 */     extractor.setSemanticCheckOption(semanticCheck);
/* 208 */     extractor.setAttributeCheckOption(attributeCheck);
/* 209 */     extractor.setCoordinatingCheckOption(coordinatingCheck);
/* 210 */     extractor.setCompoundTermPredictOption(compoundTermPrediction);
/* 211 */     extractor.setCoordinatingTermPredictOption(coordinatingTermPrediction);
/* 212 */     return extractor;
/*     */   }
/*     */ 
/*     */   private String getWordDelimitor(String notWordDelimitor)
/*     */   {
/* 220 */     StringBuffer sb = new StringBuffer();
/* 221 */     String delimitors = " \r\n\t_-.;,?/\"'`:(){}!+[]><=%$#*@&^~|\\";
/* 222 */     if ((notWordDelimitor == null) && (notWordDelimitor.length() == 0))
/* 223 */       return delimitors;
/* 224 */     for (int i = 0; i < delimitors.length(); i++) {
/* 225 */       if (notWordDelimitor.indexOf(delimitors.charAt(i)) < 0)
/* 226 */         sb.append(delimitors.charAt(i));
/*     */     }
/* 228 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.ConceptExtractorConfig
 * JD-Core Version:    0.6.2
 */