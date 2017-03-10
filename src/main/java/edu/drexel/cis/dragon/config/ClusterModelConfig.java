/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.clustermodel.ClusterModel;
/*     */ import edu.drexel.cis.dragon.ir.clustering.clustermodel.CosineClusterModel;
/*     */ import edu.drexel.cis.dragon.ir.clustering.clustermodel.EuclideanClusterModel;
/*     */ import edu.drexel.cis.dragon.ir.clustering.clustermodel.MultinomialClusterModel;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.kngbase.KnowledgeBase;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ 
/*     */ public class ClusterModelConfig extends ConfigUtil
/*     */ {
/*     */   public ClusterModelConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ClusterModelConfig(ConfigureNode root)
/*     */   {
/*  23 */     super(root);
/*     */   }
/*     */ 
/*     */   public ClusterModelConfig(String configFile) {
/*  27 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public ClusterModel getClusterModel(int clusterModelID) {
/*  31 */     return getClusterModel(this.root, clusterModelID);
/*     */   }
/*     */ 
/*     */   public ClusterModel getClusterModel(ConfigureNode node, int clusterModelID) {
/*  35 */     return loadClusterModel(node, clusterModelID);
/*     */   }
/*     */ 
/*     */   private ClusterModel loadClusterModel(ConfigureNode node, int clusterModelID)
/*     */   {
/*  42 */     ConfigureNode clusterModelNode = getConfigureNode(node, "clustermodel", clusterModelID);
/*  43 */     if (clusterModelNode == null)
/*  44 */       return null;
/*  45 */     String clusterModelName = clusterModelNode.getNodeName();
/*  46 */     return loadClusterModel(clusterModelName, clusterModelNode);
/*     */   }
/*     */ 
/*     */   protected ClusterModel loadClusterModel(String clusterModelName, ConfigureNode clusterModelNode) {
/*  50 */     if (clusterModelName.equalsIgnoreCase("CosineClusterModel"))
/*  51 */       return loadCosineClusterModel(clusterModelNode);
/*  52 */     if (clusterModelName.equalsIgnoreCase("EuclideanClusterModel"))
/*  53 */       return loadEuclideanClusterModel(clusterModelNode);
/*  54 */     if (clusterModelName.equalsIgnoreCase("MultinomialClusterModel")) {
/*  55 */       return loadMultinomialClusterModel(clusterModelNode);
/*     */     }
/*  57 */     return (ClusterModel)loadResource(clusterModelNode);
/*     */   }
/*     */ 
/*     */   private ClusterModel loadCosineClusterModel(ConfigureNode node)
/*     */   {
/*  65 */     int clusterNum = node.getInt("clusternum");
/*  66 */     int matrixID = node.getInt("doctermmatrix");
/*     */     String matrixType;
/*  67 */     if (matrixID > 0) {
/*  68 */       matrixType = node.getParameterType("doctermmatrix", "doublesparsematrix");
/*     */     }
/*     */     else
/*     */     {
/*  69 */       if ((matrixID = node.getInt("doublematrix")) > 0) {
/*  70 */         matrixType = "doublesparsematrix";
/*     */       }
/*     */       else
/*     */       {
/*  71 */         if ((matrixID = node.getInt("intmatrix")) > 0)
/*  72 */           matrixType = "intsparsematrix";
/*     */         else
/*  74 */           return null;
/*     */       }
/*     */     }
/*     */     SparseMatrix sparseMatrix;
/*  76 */     if ((sparseMatrix = new SparseMatrixConfig().getSparseMatrix(node, matrixID, matrixType)) != null) {
/*  77 */       return new CosineClusterModel(clusterNum, sparseMatrix);
/*     */     }
/*  79 */     return new CosineClusterModel(clusterNum, new DenseMatrixConfig().getDenseMatrix(node, matrixID, matrixType));
/*     */   }
/*     */ 
/*     */   private ClusterModel loadEuclideanClusterModel(ConfigureNode node)
/*     */   {
/*  87 */     int clusterNum = node.getInt("clusternum");
/*  88 */     int matrixID = node.getInt("doctermmatrix");
/*     */     String matrixType;
/*  89 */     if (matrixID > 0) {
/*  90 */       matrixType = node.getParameterType("doctermmatrix", "doublesparsematrix");
/*     */     }
/*     */     else
/*     */     {
/*  91 */       if ((matrixID = node.getInt("doublematrix")) > 0) {
/*  92 */         matrixType = "doublesparsematrix";
/*     */       }
/*     */       else
/*     */       {
/*  93 */         if ((matrixID = node.getInt("intmatrix")) > 0)
/*  94 */           matrixType = "intsparsematrix";
/*     */         else
/*  96 */           return null;
/*     */       }
/*     */     }
/*     */     SparseMatrix sparseMatrix;
/*  98 */     if ((sparseMatrix = new SparseMatrixConfig().getSparseMatrix(node, matrixID, matrixType)) != null) {
/*  99 */       return new EuclideanClusterModel(clusterNum, sparseMatrix);
/*     */     }
/* 101 */     return new EuclideanClusterModel(clusterNum, new DenseMatrixConfig().getDenseMatrix(node, matrixID, matrixType));
/*     */   }
/*     */ 
/*     */   private ClusterModel loadMultinomialClusterModel(ConfigureNode node)
/*     */   {
/* 111 */     int indexReaderID = node.getInt("indexreader");
/* 112 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/* 113 */     int clusterNum = node.getInt("clusternum");
/* 114 */     double bkgCoefficient = node.getDouble("bkgcoefficient", -1.0D);
/* 115 */     if (bkgCoefficient <= 0.0D) {
/* 116 */       return new MultinomialClusterModel(clusterNum, indexReader);
/*     */     }
/* 118 */     int matrixID = node.getInt("transmatrix");
/* 119 */     int kngID = node.getInt("knowledgebase");
/* 120 */     int topicIndexReaderID = node.getInt("topicindexreader", indexReaderID);
/* 121 */     if ((matrixID <= 0) && (kngID <= 0))
/* 122 */       return new MultinomialClusterModel(clusterNum, indexReader, bkgCoefficient);
/*     */     IndexReader topicIndexReader;
/* 123 */     if (topicIndexReaderID == indexReaderID)
/* 124 */       topicIndexReader = indexReader;
/*     */     else
/* 126 */       topicIndexReader = new IndexReaderConfig().getIndexReader(node, topicIndexReaderID);
/* 127 */     double transCoefficient = node.getDouble("transcoefficient");
/* 128 */     if (matrixID > 0) {
/* 129 */       DoubleSparseMatrix topicTransMatrix = new SparseMatrixConfig().getDoubleSparseMatrix(node, matrixID);
/* 130 */       return new MultinomialClusterModel(clusterNum, indexReader, topicIndexReader, topicTransMatrix, 
/* 131 */         transCoefficient, bkgCoefficient);
/*     */     }
/*     */ 
/* 134 */     KnowledgeBase kngBase = new KnowledgeBaseConfig().getKnowledgeBase(node, kngID);
/* 135 */     return new MultinomialClusterModel(clusterNum, indexReader, topicIndexReader, kngBase, 
/* 136 */       transCoefficient, bkgCoefficient);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.ClusterModelConfig
 * JD-Core Version:    0.6.2
 */