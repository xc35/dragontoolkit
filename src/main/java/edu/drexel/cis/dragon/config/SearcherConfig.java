/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.search.BoolRankSearcher;
/*     */ import edu.drexel.cis.dragon.ir.search.FeedbackSearcher;
/*     */ import edu.drexel.cis.dragon.ir.search.FullRankSearcher;
/*     */ import edu.drexel.cis.dragon.ir.search.PartialRankSearcher;
/*     */ import edu.drexel.cis.dragon.ir.search.Searcher;
/*     */ import edu.drexel.cis.dragon.ir.search.feedback.Feedback;
/*     */ import edu.drexel.cis.dragon.ir.search.smooth.Smoother;
/*     */ 
/*     */ public class SearcherConfig extends ConfigUtil
/*     */ {
/*     */   public SearcherConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SearcherConfig(ConfigureNode root)
/*     */   {
/*  23 */     super(root);
/*     */   }
/*     */ 
/*     */   public SearcherConfig(String configFile) {
/*  27 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public Searcher getSearcher(int searcherID) {
/*  31 */     return getSearcher(this.root, searcherID);
/*     */   }
/*     */ 
/*     */   public Searcher getSearcher(ConfigureNode node, int searcherID) {
/*  35 */     return loadSearcher(node, searcherID);
/*     */   }
/*     */ 
/*     */   private Searcher loadSearcher(ConfigureNode node, int searcherID)
/*     */   {
/*  42 */     ConfigureNode searcherNode = getConfigureNode(node, "searcher", searcherID);
/*  43 */     if (searcherNode == null)
/*  44 */       return null;
/*  45 */     String searcherName = searcherNode.getNodeName();
/*  46 */     return loadSearcher(searcherName, searcherNode);
/*     */   }
/*     */ 
/*     */   protected Searcher loadSearcher(String searcherName, ConfigureNode searcherNode) {
/*  50 */     if (searcherName.equalsIgnoreCase("FullRankSearcher"))
/*  51 */       return loadFullRankSearcher(searcherNode);
/*  52 */     if (searcherName.equalsIgnoreCase("PartialRankSearcher"))
/*  53 */       return loadPartialRankSearcher(searcherNode);
/*  54 */     if (searcherName.equalsIgnoreCase("BoolRankSearcher"))
/*  55 */       return loadBoolRankSearcher(searcherNode);
/*  56 */     if (searcherName.equalsIgnoreCase("FeedbackSearcher")) {
/*  57 */       return loadFeedbackSearcher(searcherNode);
/*     */     }
/*  59 */     return (Searcher)loadResource(searcherNode);
/*     */   }
/*     */ 
/*     */   private Searcher loadFullRankSearcher(ConfigureNode node)
/*     */   {
/*  66 */     int indexReaderID = node.getInt("indexreader");
/*  67 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/*  68 */     return new FullRankSearcher(indexReader, getSmoother(node));
/*     */   }
/*     */ 
/*     */   private Searcher loadPartialRankSearcher(ConfigureNode node)
/*     */   {
/*  75 */     int indexReaderID = node.getInt("indexreader");
/*  76 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/*  77 */     return new PartialRankSearcher(indexReader, getSmoother(node));
/*     */   }
/*     */ 
/*     */   private Searcher loadBoolRankSearcher(ConfigureNode node)
/*     */   {
/*  84 */     int indexReaderID = node.getInt("indexreader");
/*  85 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/*  86 */     return new BoolRankSearcher(indexReader, getSmoother(node));
/*     */   }
/*     */ 
/*     */   private Searcher loadFeedbackSearcher(ConfigureNode node)
/*     */   {
/*  95 */     FeedbackConfig config = new FeedbackConfig();
/*  96 */     int feedbackID = node.getInt("feedback");
/*  97 */     Feedback feedback = config.getFeedback(node, feedbackID);
/*  98 */     boolean sameSearcher = node.getBoolean("sameasinitsearcher", false);
/*  99 */     if (sameSearcher) {
/* 100 */       return new FeedbackSearcher(feedback.getSearcher(), feedback);
/*     */     }
/* 102 */     int searcherID = node.getInt("searcher");
/* 103 */     return new FeedbackSearcher(getSearcher(node, searcherID), feedback);
/*     */   }
/*     */ 
/*     */   private Smoother getSmoother(ConfigureNode node)
/*     */   {
/* 111 */     int smootherID = node.getInt("smoother", 0);
/* 112 */     if (smootherID <= 0)
/* 113 */       return null;
/* 114 */     SmootherConfig config = new SmootherConfig();
/* 115 */     return config.getSmoother(node, smootherID);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.SearcherConfig
 * JD-Core Version:    0.6.2
 */