/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.BasicKMean;
/*     */ import edu.drexel.cis.dragon.ir.clustering.BisectKMean;
/*     */ import edu.drexel.cis.dragon.ir.clustering.Clustering;
/*     */ import edu.drexel.cis.dragon.ir.clustering.HierClustering;
/*     */ import edu.drexel.cis.dragon.ir.clustering.LinkKMean;
/*     */ import edu.drexel.cis.dragon.ir.clustering.clustermodel.ClusterModel;
/*     */ import edu.drexel.cis.dragon.ir.clustering.docdistance.DocDistance;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ 
/*     */ public class ClusteringConfig extends ConfigUtil
/*     */ {
/*     */   public ClusteringConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ClusteringConfig(ConfigureNode root)
/*     */   {
/*  24 */     super(root);
/*     */   }
/*     */ 
/*     */   public ClusteringConfig(String configFile) {
/*  28 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public Clustering getClustering(int clusteringID) {
/*  32 */     return getClustering(this.root, clusteringID);
/*     */   }
/*     */ 
/*     */   public Clustering getClustering(ConfigureNode node, int clusteringID) {
/*  36 */     return loadClustering(node, clusteringID);
/*     */   }
/*     */ 
/*     */   private Clustering loadClustering(ConfigureNode node, int clusteringID)
/*     */   {
/*  43 */     ConfigureNode clusteringNode = getConfigureNode(node, "clustering", clusteringID);
/*  44 */     if (clusteringNode == null)
/*  45 */       return null;
/*  46 */     String clusteringName = clusteringNode.getNodeName();
/*  47 */     return loadClustering(clusteringName, clusteringNode);
/*     */   }
/*     */ 
/*     */   protected Clustering loadClustering(String clusteringName, ConfigureNode clusteringNode) {
/*  51 */     if (clusteringName.equalsIgnoreCase("HierClustering"))
/*  52 */       return loadHierClustering(clusteringNode);
/*  53 */     if (clusteringName.equalsIgnoreCase("BasicKMean"))
/*  54 */       return loadBasicKMean(clusteringNode);
/*  55 */     if (clusteringName.equalsIgnoreCase("BisectKMean"))
/*  56 */       return loadBisectKMean(clusteringNode);
/*  57 */     if (clusteringName.equalsIgnoreCase("LinkKMean")) {
/*  58 */       return loadLinkKMean(clusteringNode);
/*     */     }
/*  60 */     return (Clustering)loadResource(clusteringNode);
/*     */   }
/*     */ 
/*     */   private Clustering loadHierClustering(ConfigureNode node)
/*     */   {
/*  70 */     int clusterNum = node.getInt("clusternum");
/*  71 */     int filterID = node.getInt("featurefilter", -1);
/*  72 */     int indexReaderID = node.getInt("indexreader");
/*  73 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/*  74 */     int docDistanceID = node.getInt("docdistance");
/*  75 */     DocDistance docDistance = new DocDistanceConfig().getDocDistance(node, docDistanceID);
/*  76 */     String linkageMode = node.getString("linkage", "complete");
/*     */     int linkage;
/*  77 */     if (linkageMode.equalsIgnoreCase("complete")) {
/*  78 */       linkage = 1;
/*     */     }
/*     */     else
/*     */     {
/*  79 */       if (linkageMode.equalsIgnoreCase("average")) {
/*  80 */         linkage = 0;
/*     */       }
/*     */       else
/*     */       {
/*  81 */         if (linkageMode.equalsIgnoreCase("single"))
/*  82 */           linkage = -1;
/*     */         else
/*  84 */           return null;
/*     */       }
/*     */     }
/*  85 */     HierClustering clustering = new HierClustering(indexReader, docDistance, clusterNum, linkage);
/*  86 */     if (filterID > 0)
/*  87 */       clustering.setFeatureFilter(new FeatureFilterConfig().getFeatureFilter(node, filterID));
/*  88 */     return clustering;
/*     */   }
/*     */ 
/*     */   private Clustering loadBasicKMean(ConfigureNode node)
/*     */   {
/*  99 */     int clusterNum = node.getInt("clusternum");
/* 100 */     int indexReaderID = node.getInt("indexreader");
/* 101 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/* 102 */     int clusterModelID = node.getInt("clustermodel");
/* 103 */     ClusterModel clusterModel = new ClusterModelConfig().getClusterModel(node, clusterModelID);
/* 104 */     int maxIteration = node.getInt("maxiteration", 50);
/* 105 */     long randomSeed = node.getInt("randomseed", -1);
/* 106 */     int filterID = node.getInt("featurefilter", -1);
/* 107 */     boolean initAllObjects = node.getBoolean("initallobjects", false);
/* 108 */     BasicKMean kmean = new BasicKMean(indexReader, clusterModel, clusterNum, initAllObjects);
/* 109 */     kmean.setMaxIteration(maxIteration);
/* 110 */     kmean.setRandomSeed(randomSeed);
/* 111 */     if (filterID > 0)
/* 112 */       kmean.setFeatureFilter(new FeatureFilterConfig().getFeatureFilter(node, filterID));
/* 113 */     return kmean;
/*     */   }
/*     */ 
/*     */   private Clustering loadBisectKMean(ConfigureNode node)
/*     */   {
/* 125 */     int clusterNum = node.getInt("clusternum");
/* 126 */     int indexReaderID = node.getInt("indexreader");
/* 127 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/* 128 */     int clusterModelID = node.getInt("clustermodel");
/* 129 */     ClusterModel clusterModel = new ClusterModelConfig().getClusterModel(node, clusterModelID);
/* 130 */     boolean refine = node.getBoolean("refine", true);
/* 131 */     int maxIteration = node.getInt("maxiteration", 50);
/* 132 */     long randomSeed = node.getInt("randomseed", -1);
/* 133 */     int filterID = node.getInt("featurefilter", -1);
/* 134 */     boolean initAllObjects = node.getBoolean("initallobjects", false);
/* 135 */     BisectKMean kmean = new BisectKMean(indexReader, clusterModel, clusterNum, initAllObjects);
/* 136 */     kmean.setMaxIteration(maxIteration);
/* 137 */     kmean.setRandomSeed(randomSeed);
/* 138 */     kmean.setRefine(refine);
/* 139 */     if (filterID > 0)
/* 140 */       kmean.setFeatureFilter(new FeatureFilterConfig().getFeatureFilter(node, filterID));
/* 141 */     return kmean;
/*     */   }
/*     */ 
/*     */   private Clustering loadLinkKMean(ConfigureNode node)
/*     */   {
/* 151 */     int clusteringID = node.getInt("initclustering");
/* 152 */     Clustering initClustering = new ClusteringConfig().getClustering(node, clusteringID);
/* 153 */     int outLinkID = node.getInt("outlinkmatrix");
/* 154 */     String paraType = node.getParameterType("outlinkmatrix");
/*     */     SparseMatrix outLinks;
/* 155 */     if ((paraType == null) || (paraType.equalsIgnoreCase("intsparematrix")))
/* 156 */       outLinks = new SparseMatrixConfig().getIntSparseMatrix(node, outLinkID);
/*     */     else
/* 158 */       outLinks = new SparseMatrixConfig().getDoubleSparseMatrix(node, outLinkID);
/* 159 */     int inLinkID = node.getInt("inlinkmatrix");
/*     */     SparseMatrix inLinks;
/* 160 */     if ((inLinkID <= 0) || (inLinkID == outLinkID)) {
/* 161 */       inLinks = null;
/*     */     } else {
/* 163 */       paraType = node.getParameterType("inlinkmatrix");
/* 164 */       if ((paraType == null) || (paraType.equalsIgnoreCase("intsparematrix")))
/* 165 */         inLinks = new SparseMatrixConfig().getIntSparseMatrix(node, inLinkID);
/*     */       else
/* 167 */         inLinks = new SparseMatrixConfig().getDoubleSparseMatrix(node, inLinkID);
/*     */     }
/* 169 */     LinkKMean kmean = new LinkKMean(initClustering, outLinks, inLinks);
/* 170 */     kmean.setMaxIteration(node.getInt("maxiteration", 10));
/* 171 */     kmean.setRandomSeed(node.getInt("randomseed", -1));
/* 172 */     kmean.setUseWeight(node.getBoolean("useweight", false));
/* 173 */     return kmean;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.ClusteringConfig
 * JD-Core Version:    0.6.2
 */