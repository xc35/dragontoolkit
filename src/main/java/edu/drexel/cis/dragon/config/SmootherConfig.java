/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.BasicIndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.search.smooth.DirichletSmoother;
/*     */ import edu.drexel.cis.dragon.ir.search.smooth.DocFirstTransSmoother;
/*     */ import edu.drexel.cis.dragon.ir.search.smooth.JMSmoother;
/*     */ import edu.drexel.cis.dragon.ir.search.smooth.OkapiSmoother;
/*     */ import edu.drexel.cis.dragon.ir.search.smooth.PivotedNormSmoother;
/*     */ import edu.drexel.cis.dragon.ir.search.smooth.QueryFirstTransSmoother;
/*     */ import edu.drexel.cis.dragon.ir.search.smooth.Smoother;
/*     */ import edu.drexel.cis.dragon.ir.search.smooth.TFIDFSmoother;
/*     */ import edu.drexel.cis.dragon.ir.search.smooth.TwoStageSmoother;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ 
/*     */ public class SmootherConfig extends ConfigUtil
/*     */ {
/*     */   public SmootherConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SmootherConfig(ConfigureNode root)
/*     */   {
/*  22 */     super(root);
/*     */   }
/*     */ 
/*     */   public SmootherConfig(String configFile) {
/*  26 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public Smoother getSmoother(int smootherID) {
/*  30 */     return getSmoother(this.root, smootherID);
/*     */   }
/*     */ 
/*     */   public Smoother getSmoother(ConfigureNode node, int smootherID) {
/*  34 */     return loadSmoother(node, smootherID);
/*     */   }
/*     */ 
/*     */   private Smoother loadSmoother(ConfigureNode node, int smootherID)
/*     */   {
/*  41 */     ConfigureNode smootherNode = getConfigureNode(node, "smoother", smootherID);
/*  42 */     if (smootherNode == null)
/*  43 */       return null;
/*  44 */     String smootherName = smootherNode.getNodeName();
/*  45 */     return loadSmoother(smootherName, smootherNode);
/*     */   }
/*     */ 
/*     */   protected Smoother loadSmoother(String smootherName, ConfigureNode smootherNode) {
/*  49 */     if (smootherName.equalsIgnoreCase("JMSmoother"))
/*  50 */       return loadJMSmoother(smootherNode);
/*  51 */     if (smootherName.equalsIgnoreCase("DirichletSmoother"))
/*  52 */       return loadDirichletSmoother(smootherNode);
/*  53 */     if (smootherName.equalsIgnoreCase("AbsoluteDiscountSmoother"))
/*  54 */       return loadAbsoluteDiscountSmoother(smootherNode);
/*  55 */     if (smootherName.equalsIgnoreCase("TwoStageSmoother"))
/*  56 */       return loadTwoStageSmoother(smootherNode);
/*  57 */     if (smootherName.equalsIgnoreCase("OkapiSmoother"))
/*  58 */       return loadOkapiSmoother(smootherNode);
/*  59 */     if (smootherName.equalsIgnoreCase("TFIDFSmoother"))
/*  60 */       return loadTFIDFSmoother(smootherNode);
/*  61 */     if (smootherName.equalsIgnoreCase("PivotedNormSmoother"))
/*  62 */       return loadPivotedNormSmoother(smootherNode);
/*  63 */     if (smootherName.equalsIgnoreCase("QueryFirstTransSmoother"))
/*  64 */       return loadQueryFirstTransSmoother(smootherNode);
/*  65 */     if (smootherName.equalsIgnoreCase("DocFirstTransSmoother")) {
/*  66 */       return loadDocFirstTransSmoother(smootherNode);
/*     */     }
/*  68 */     return (Smoother)loadResource(smootherNode);
/*     */   }
/*     */ 
/*     */   private Smoother loadJMSmoother(ConfigureNode node)
/*     */   {
/*  76 */     int collectionStatID = node.getInt("collectionstat");
/*  77 */     IRCollection collection = new IndexReaderConfig().getIRCollectionStat(node, collectionStatID);
/*  78 */     double bkgCoefficient = node.getDouble("bkgcoefficient", 0.5D);
/*  79 */     return new JMSmoother(collection, bkgCoefficient);
/*     */   }
/*     */ 
/*     */   private Smoother loadDirichletSmoother(ConfigureNode node)
/*     */   {
/*  87 */     int collectionStatID = node.getInt("collectionstat");
/*  88 */     IRCollection collection = new IndexReaderConfig().getIRCollectionStat(node, collectionStatID);
/*  89 */     double dirichletCoefficient = node.getDouble("dirichletcoefficient", 1000.0D);
/*  90 */     return new DirichletSmoother(collection, dirichletCoefficient);
/*     */   }
/*     */ 
/*     */   private Smoother loadAbsoluteDiscountSmoother(ConfigureNode node)
/*     */   {
/*  98 */     int collectionStatID = node.getInt("collectionstat");
/*  99 */     IRCollection collection = new IndexReaderConfig().getIRCollectionStat(node, collectionStatID);
/* 100 */     double absoluteDiscount = node.getDouble("absolutediscount", 0.6D);
/* 101 */     return new DirichletSmoother(collection, absoluteDiscount);
/*     */   }
/*     */ 
/*     */   private Smoother loadTwoStageSmoother(ConfigureNode node)
/*     */   {
/* 110 */     int collectionStatID = node.getInt("collectionstat");
/* 111 */     IRCollection collection = new IndexReaderConfig().getIRCollectionStat(node, collectionStatID);
/* 112 */     double dirichletCoefficient = node.getDouble("dirichletcoefficient", 1000.0D);
/* 113 */     double bkgCoefficient = node.getDouble("bkgcoefficient", 0.5D);
/* 114 */     return new TwoStageSmoother(collection, bkgCoefficient, dirichletCoefficient);
/*     */   }
/*     */ 
/*     */   private Smoother loadPivotedNormSmoother(ConfigureNode node)
/*     */   {
/* 122 */     int collectionStatID = node.getInt("collectionstat");
/* 123 */     IRCollection collection = new IndexReaderConfig().getIRCollectionStat(node, collectionStatID);
/* 124 */     double s = node.getDouble("s", 0.2D);
/* 125 */     return new PivotedNormSmoother(collection, s);
/*     */   }
/*     */ 
/*     */   private Smoother loadOkapiSmoother(ConfigureNode node)
/*     */   {
/* 133 */     int collectionStatID = node.getInt("collectionstat");
/* 134 */     IRCollection collection = new IndexReaderConfig().getIRCollectionStat(node, collectionStatID);
/* 135 */     double bm25k1 = node.getDouble("bm25k1", 2.0D);
/* 136 */     double bm25b = node.getDouble("bm25b", 0.75D);
/* 137 */     return new OkapiSmoother(collection, bm25k1, bm25b);
/*     */   }
/*     */ 
/*     */   private Smoother loadTFIDFSmoother(ConfigureNode node)
/*     */   {
/* 146 */     int collectionStatID = node.getInt("collectionstat");
/* 147 */     IRCollection collection = new IndexReaderConfig().getIRCollectionStat(node, collectionStatID);
/* 148 */     boolean useBM25 = node.getBoolean("usebm25", false);
/* 149 */     if (useBM25) {
/* 150 */       double bm25k1 = node.getDouble("bm25k1", 2.0D);
/* 151 */       double bm25b = node.getDouble("bm25b", 0.75D);
/* 152 */       return new TFIDFSmoother(collection, bm25k1, bm25b);
/*     */     }
/*     */ 
/* 155 */     return new TFIDFSmoother(collection);
/*     */   }
/*     */ 
/*     */   private Smoother loadQueryFirstTransSmoother(ConfigureNode node)
/*     */   {
/* 168 */     IndexReaderConfig config = new IndexReaderConfig();
/* 169 */     int srcIndexReaderID = node.getInt("srcindexreader");
/* 170 */     int destIndexReaderID = node.getInt("destindexreader");
/* 171 */     BasicIndexReader srcIndexReader = (BasicIndexReader)config.getIndexReader(node, srcIndexReaderID);
/*     */     BasicIndexReader destIndexReader;
/* 172 */     if (destIndexReaderID == srcIndexReaderID)
/* 173 */       destIndexReader = srcIndexReader;
/*     */     else
/* 175 */       destIndexReader = (BasicIndexReader)config.getIndexReader(node, destIndexReaderID);
/* 176 */     double transCoefficient = node.getDouble("transcoefficient");
/* 177 */     int matrixID = node.getInt("transposedtransmatrix");
/* 178 */     int smootherID = node.getInt("basicsmoother");
/* 179 */     Smoother basicSmoother = getSmoother(node, smootherID);
/* 180 */     DoubleSparseMatrix transMatrix = new SparseMatrixConfig().getDoubleSparseMatrix(node, matrixID);
/* 181 */     if (srcIndexReaderID == destIndexReaderID) {
/* 182 */       boolean relationTrans = node.getBoolean("relationtrans", true);
/* 183 */       return new QueryFirstTransSmoother(srcIndexReader, transMatrix, relationTrans, transCoefficient, basicSmoother);
/*     */     }
/*     */ 
/* 186 */     return new QueryFirstTransSmoother(srcIndexReader, destIndexReader, transMatrix, transCoefficient, basicSmoother);
/*     */   }
/*     */ 
/*     */   private Smoother loadDocFirstTransSmoother(ConfigureNode node)
/*     */   {
/* 199 */     IndexReaderConfig config = new IndexReaderConfig();
/* 200 */     int srcIndexReaderID = node.getInt("srcindexreader");
/* 201 */     int destIndexReaderID = node.getInt("destindexreader");
/* 202 */     IndexReader srcIndexReader = config.getIndexReader(node, srcIndexReaderID);
/*     */     IndexReader destIndexReader;
/* 203 */     if (destIndexReaderID == srcIndexReaderID)
/* 204 */       destIndexReader = srcIndexReader;
/*     */     else
/* 206 */       destIndexReader = config.getIndexReader(node, destIndexReaderID);
/* 207 */     double transCoefficient = node.getDouble("transcoefficient");
/* 208 */     int matrixID = node.getInt("transmatrix");
/* 209 */     int smootherID = node.getInt("basicsmoother");
/* 210 */     Smoother basicSmoother = getSmoother(node, smootherID);
/* 211 */     DoubleSparseMatrix transMatrix = new SparseMatrixConfig().getDoubleSparseMatrix(node, matrixID);
/* 212 */     if (srcIndexReaderID == destIndexReaderID) {
/* 213 */       boolean relationTrans = node.getBoolean("relationtrans", true);
/* 214 */       return new DocFirstTransSmoother(srcIndexReader, transMatrix, relationTrans, transCoefficient, basicSmoother);
/*     */     }
/*     */ 
/* 217 */     return new QueryFirstTransSmoother(srcIndexReader, destIndexReader, transMatrix, transCoefficient, basicSmoother);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.SmootherConfig
 * JD-Core Version:    0.6.2
 */