/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.search.Searcher;
/*     */ import edu.drexel.cis.dragon.ir.search.feedback.Feedback;
/*     */ import edu.drexel.cis.dragon.ir.search.feedback.GenerativeFeedback;
/*     */ import edu.drexel.cis.dragon.ir.search.feedback.InformationFlowFeedback;
/*     */ import edu.drexel.cis.dragon.ir.search.feedback.PhraseTransFeedback;
/*     */ import edu.drexel.cis.dragon.ir.search.feedback.RelationTransFeedback;
/*     */ import edu.drexel.cis.dragon.ir.search.feedback.RocchioFeedback;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.extract.TokenExtractor;
/*     */ 
/*     */ public class FeedbackConfig extends ConfigUtil
/*     */ {
/*     */   public FeedbackConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public FeedbackConfig(ConfigureNode root)
/*     */   {
/*  24 */     super(root);
/*     */   }
/*     */ 
/*     */   public FeedbackConfig(String configFile) {
/*  28 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public Feedback getFeedback(int feedbackID) {
/*  32 */     return getFeedback(this.root, feedbackID);
/*     */   }
/*     */ 
/*     */   public Feedback getFeedback(ConfigureNode node, int feedbackID) {
/*  36 */     return loadFeedback(node, feedbackID);
/*     */   }
/*     */ 
/*     */   private Feedback loadFeedback(ConfigureNode node, int feedbackID)
/*     */   {
/*  43 */     ConfigureNode feedbackNode = getConfigureNode(node, "feedback", feedbackID);
/*  44 */     if (feedbackNode == null)
/*  45 */       return null;
/*  46 */     String feedbackName = feedbackNode.getNodeName();
/*  47 */     return loadFeedback(feedbackName, feedbackNode);
/*     */   }
/*     */ 
/*     */   protected Feedback loadFeedback(String feedbackName, ConfigureNode feedbackNode) {
/*  51 */     if (feedbackName.equalsIgnoreCase("GenerativeFeedback"))
/*  52 */       return loadGenerativeFeedback(feedbackNode);
/*  53 */     if (feedbackName.equalsIgnoreCase("MinDivergence"))
/*  54 */       return loadMinDivergenceFeedback(feedbackNode);
/*  55 */     if (feedbackName.equalsIgnoreCase("RocchioFeedback"))
/*  56 */       return loadRocchioFeedback(feedbackNode);
/*  57 */     if (feedbackName.equalsIgnoreCase("InformationFlowFeedback"))
/*  58 */       return loadInformationFlowFeedback(feedbackNode);
/*  59 */     if (feedbackName.equalsIgnoreCase("RelationTransFeedback"))
/*  60 */       return loadRelationTransFeedback(feedbackNode);
/*  61 */     if (feedbackName.equalsIgnoreCase("PhraseTransFeedback")) {
/*  62 */       return loadPhraseTransFeedback(feedbackNode);
/*     */     }
/*  64 */     return (Feedback)loadResource(feedbackNode);
/*     */   }
/*     */ 
/*     */   private Feedback loadGenerativeFeedback(ConfigureNode node)
/*     */   {
/*  71 */     int feedbackDocNum = node.getInt("feedbackdocnum", 10);
/*  72 */     int expandTermNum = node.getInt("expandtermnum", 10);
/*  73 */     double feedbackCoefficient = node.getDouble("feedbackcoefficient", 0.6D);
/*  74 */     double bkgCoefficient = node.getDouble("bkgcoefficient", 0.5D);
/*  75 */     return new GenerativeFeedback(getSearcher(node), feedbackDocNum, expandTermNum, feedbackCoefficient, bkgCoefficient);
/*     */   }
/*     */ 
/*     */   private Feedback loadMinDivergenceFeedback(ConfigureNode node)
/*     */   {
/*  82 */     int feedbackDocNum = node.getInt("feedbackdocnum", 10);
/*  83 */     int expandTermNum = node.getInt("expandtermnum", 10);
/*  84 */     double feedbackCoefficient = node.getDouble("feedbackcoefficient", 0.6D);
/*  85 */     double bkgCoefficient = node.getDouble("bkgcoefficient", 0.5D);
/*  86 */     return new GenerativeFeedback(getSearcher(node), feedbackDocNum, expandTermNum, feedbackCoefficient, bkgCoefficient);
/*     */   }
/*     */ 
/*     */   private Feedback loadRocchioFeedback(ConfigureNode node)
/*     */   {
/*  94 */     int feedbackDocNum = node.getInt("feedbackdocnum", 10);
/*  95 */     int expandTermNum = node.getInt("expandtermnum", 10);
/*  96 */     double feedbackCoefficient = node.getDouble("feedbackcoefficient", 0.6D);
/*  97 */     boolean useBM25 = node.getBoolean("usebm25", false);
/*  98 */     if (useBM25) {
/*  99 */       double bm25k1 = node.getDouble("bm25k1", 2.0D);
/* 100 */       double bm25b = node.getDouble("bm25b", 0.75D);
/* 101 */       return new RocchioFeedback(getSearcher(node), feedbackDocNum, expandTermNum, feedbackCoefficient, bm25k1, bm25b);
/*     */     }
/*     */ 
/* 104 */     return new RocchioFeedback(getSearcher(node), feedbackDocNum, expandTermNum, feedbackCoefficient);
/*     */   }
/*     */ 
/*     */   private Feedback loadInformationFlowFeedback(ConfigureNode node)
/*     */   {
/* 114 */     int teID = node.getInt("tokenextractor", 0);
/* 115 */     if (teID <= 0)
/* 116 */       return null;
/* 117 */     ConceptExtractorConfig ceConfig = new ConceptExtractorConfig();
/* 118 */     TokenExtractor te = (TokenExtractor)ceConfig.getConceptExtractor(node, teID);
/* 119 */     int feedbackDocNum = node.getInt("feedbackdocnum", 10);
/* 120 */     int expandTermNum = node.getInt("expandtermnum", 10);
/* 121 */     double feedbackCoefficient = node.getDouble("feedbackcoefficient", 0.6D);
/* 122 */     InformationFlowFeedback feedback = new InformationFlowFeedback(te, getSearcher(node), feedbackDocNum, expandTermNum, feedbackCoefficient);
/* 123 */     feedback.setHALWindowSize(node.getInt("windowsize", 8));
/* 124 */     feedback.setInfrequentTermThreshold(node.getInt("minfrequency", 25));
/* 125 */     feedback.setDominantVectorWeight(node.getDouble("dominance1", 0.5D));
/* 126 */     feedback.setDominantVectorThreshold(node.getDouble("threshold1", 0.0D));
/* 127 */     feedback.setSubordinateVectorWeight(node.getDouble("dominance1", 0.3D));
/* 128 */     feedback.setSubordinateVectorThreshold(node.getDouble("threshold1", 0.0D));
/* 129 */     feedback.setMultiplier(node.getDouble("multiplier", 2.0D));
/* 130 */     return feedback;
/*     */   }
/*     */ 
/*     */   private Feedback loadRelationTransFeedback(ConfigureNode node)
/*     */   {
/* 141 */     int feedbackDocNum = node.getInt("feedbackdocnum", 10);
/* 142 */     int expandTermNum = node.getInt("expandtermnum", 10);
/* 143 */     double feedbackCoefficient = node.getDouble("feedbackcoefficient", 0.6D);
/* 144 */     double bkgCoefficient = node.getDouble("bkgcoefficient", 0.5D);
/* 145 */     boolean selfTranslation = node.getBoolean("selftranslation", true);
/* 146 */     boolean generativeModel = node.getBoolean("generativemodel", false);
/* 147 */     Searcher searcher = getSearcher(node);
/*     */ 
/* 149 */     if (selfTranslation) {
/* 150 */       if (generativeModel) {
/* 151 */         return new RelationTransFeedback(searcher, feedbackDocNum, expandTermNum, feedbackCoefficient, bkgCoefficient);
/*     */       }
/* 153 */       return new RelationTransFeedback(searcher, feedbackDocNum, expandTermNum, feedbackCoefficient);
/*     */     }
/*     */ 
/* 156 */     int transMatrixID = node.getInt("transmatrix");
/* 157 */     DoubleSparseMatrix transMatrix = new SparseMatrixConfig().getDoubleSparseMatrix(node, transMatrixID);
/* 158 */     if (generativeModel) {
/* 159 */       return new RelationTransFeedback(searcher, feedbackDocNum, expandTermNum, feedbackCoefficient, bkgCoefficient, transMatrix);
/*     */     }
/* 161 */     return new RelationTransFeedback(searcher, feedbackDocNum, expandTermNum, feedbackCoefficient, transMatrix);
/*     */   }
/*     */ 
/*     */   private Feedback loadPhraseTransFeedback(ConfigureNode node)
/*     */   {
/* 174 */     int feedbackDocNum = node.getInt("feedbackdocnum", 10);
/* 175 */     int expandTermNum = node.getInt("expandtermnum", 10);
/* 176 */     double feedbackCoefficient = node.getDouble("feedbackcoefficient", 0.6D);
/* 177 */     double bkgCoefficient = node.getDouble("bkgcoefficient", 0.5D);
/* 178 */     boolean selfTranslation = node.getBoolean("selftranslation", true);
/* 179 */     int phraseIndexReaderID = node.getInt("phraseindexreader");
/* 180 */     IndexReader phraseIndexReader = new IndexReaderConfig().getIndexReader(node, phraseIndexReaderID);
/* 181 */     Searcher searcher = getSearcher(node);
/* 182 */     if (selfTranslation) {
/* 183 */       return new PhraseTransFeedback(searcher, feedbackDocNum, expandTermNum, feedbackCoefficient, phraseIndexReader, bkgCoefficient);
/*     */     }
/* 185 */     int transMatrixID = node.getInt("transmatrix");
/* 186 */     DoubleSparseMatrix transMatrix = new SparseMatrixConfig().getDoubleSparseMatrix(node, transMatrixID);
/* 187 */     return new PhraseTransFeedback(searcher, feedbackDocNum, expandTermNum, feedbackCoefficient, phraseIndexReader, bkgCoefficient, transMatrix);
/*     */   }
/*     */ 
/*     */   private Searcher getSearcher(ConfigureNode node)
/*     */   {
/* 195 */     SearcherConfig config = new SearcherConfig();
/* 196 */     int searcherID = node.getInt("searcher");
/* 197 */     return config.getSearcher(node, searcherID);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.FeedbackConfig
 * JD-Core Version:    0.6.2
 */