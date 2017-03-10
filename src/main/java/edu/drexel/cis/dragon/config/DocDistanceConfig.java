/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.docdistance.CosineDocDistance;
/*     */ import edu.drexel.cis.dragon.ir.clustering.docdistance.DocDistance;
/*     */ import edu.drexel.cis.dragon.ir.clustering.docdistance.EuclideanDocDistance;
/*     */ import edu.drexel.cis.dragon.ir.clustering.docdistance.KLDivDocDistance;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ 
/*     */ public class DocDistanceConfig extends ConfigUtil
/*     */ {
/*     */   public DocDistanceConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DocDistanceConfig(ConfigureNode root)
/*     */   {
/*  22 */     super(root);
/*     */   }
/*     */ 
/*     */   public DocDistanceConfig(String configFile) {
/*  26 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public DocDistance getDocDistance(int docDistanceID) {
/*  30 */     return getDocDistance(this.root, docDistanceID);
/*     */   }
/*     */ 
/*     */   public DocDistance getDocDistance(ConfigureNode node, int docDistanceID) {
/*  34 */     return loadDocDistance(node, docDistanceID);
/*     */   }
/*     */ 
/*     */   private DocDistance loadDocDistance(ConfigureNode node, int docDistanceID)
/*     */   {
/*  41 */     ConfigureNode docDistanceNode = getConfigureNode(node, "docdistance", docDistanceID);
/*  42 */     if (docDistanceNode == null)
/*  43 */       return null;
/*  44 */     String docDistanceName = docDistanceNode.getNodeName();
/*  45 */     return loadDocDistance(docDistanceName, docDistanceNode);
/*     */   }
/*     */ 
/*     */   protected DocDistance loadDocDistance(String docDistanceName, ConfigureNode docDistanceNode) {
/*  49 */     if (docDistanceName.equalsIgnoreCase("CosineDocDistance"))
/*  50 */       return loadCosineDocDistance(docDistanceNode);
/*  51 */     if (docDistanceName.equalsIgnoreCase("EuclideanDocDistance"))
/*  52 */       return loadEuclideanDocDistance(docDistanceNode);
/*  53 */     if (docDistanceName.equalsIgnoreCase("KLDivDocDistance")) {
/*  54 */       return loadKLDivDocDistance(docDistanceNode);
/*     */     }
/*  56 */     return (DocDistance)loadResource(docDistanceNode);
/*     */   }
/*     */ 
/*     */   private DocDistance loadCosineDocDistance(ConfigureNode node)
/*     */   {
/*  64 */     int matrixID = node.getInt("doctermmatrix");
/*     */     String matrixType;
/*  65 */     if (matrixID > 0) {
/*  66 */       matrixType = node.getParameterType("doctermmatrix", "doublesparsematrix");
/*     */     }
/*     */     else
/*     */     {
/*  67 */       if ((matrixID = node.getInt("doublematrix")) > 0) {
/*  68 */         matrixType = "doublesparsematrix";
/*     */       }
/*     */       else
/*     */       {
/*  69 */         if ((matrixID = node.getInt("intmatrix")) > 0)
/*  70 */           matrixType = "intsparsematrix";
/*     */         else
/*  72 */           return null;
/*     */       }
/*     */     }
/*     */     SparseMatrix sparseMatrix;
/*  74 */     if ((sparseMatrix = new SparseMatrixConfig().getSparseMatrix(node, matrixID, matrixType)) != null) {
/*  75 */       return new CosineDocDistance(sparseMatrix);
/*     */     }
/*  77 */     return new CosineDocDistance(new DenseMatrixConfig().getDenseMatrix(node, matrixID, matrixType));
/*     */   }
/*     */ 
/*     */   private DocDistance loadEuclideanDocDistance(ConfigureNode node)
/*     */   {
/*  85 */     int matrixID = node.getInt("doctermmatrix");
/*     */     String matrixType;
/*  86 */     if (matrixID > 0) {
/*  87 */       matrixType = node.getParameterType("doctermmatrix", "doublesparsematrix");
/*     */     }
/*     */     else
/*     */     {
/*  88 */       if ((matrixID = node.getInt("doublematrix")) > 0) {
/*  89 */         matrixType = "doublesparsematrix";
/*     */       }
/*     */       else
/*     */       {
/*  90 */         if ((matrixID = node.getInt("intmatrix")) > 0)
/*  91 */           matrixType = "intsparsematrix";
/*     */         else
/*  93 */           return null;
/*     */       }
/*     */     }
/*     */     SparseMatrix sparseMatrix;
/*  95 */     if ((sparseMatrix = new SparseMatrixConfig().getSparseMatrix(node, matrixID, matrixType)) != null) {
/*  96 */       return new EuclideanDocDistance(sparseMatrix);
/*     */     }
/*  98 */     return new EuclideanDocDistance(new DenseMatrixConfig().getDenseMatrix(node, matrixID, matrixType));
/*     */   }
/*     */ 
/*     */   private DocDistance loadKLDivDocDistance(ConfigureNode node)
/*     */   {
/* 107 */     int indexReaderID = node.getInt("indexreader");
/* 108 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/* 109 */     int matrixID = node.getInt("doublematrix");
/* 110 */     DoubleSparseMatrix matrix = new SparseMatrixConfig().getDoubleSparseMatrix(node, matrixID);
/* 111 */     double normThreshold = node.getDouble("normthreshold", 0.0D);
/* 112 */     if (indexReader != null) {
/* 113 */       return new KLDivDocDistance(indexReader, matrix, normThreshold);
/*     */     }
/* 115 */     return new KLDivDocDistance(indexReader, matrix);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.DocDistanceConfig
 * JD-Core Version:    0.6.2
 */