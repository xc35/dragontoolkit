/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.DenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleFlatDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSuperDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntFlatDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntSuperDenseMatrix;
/*     */ 
/*     */ public class DenseMatrixConfig extends ConfigUtil
/*     */ {
/*     */   public DenseMatrixConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DenseMatrixConfig(ConfigureNode root)
/*     */   {
/*  20 */     super(root);
/*     */   }
/*     */ 
/*     */   public DenseMatrixConfig(String configFile) {
/*  24 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public DenseMatrix getDenseMatrix(int matrixID, String matrixType) {
/*  28 */     return getDenseMatrix(this.root, matrixID, matrixType);
/*     */   }
/*     */ 
/*     */   public DenseMatrix getDenseMatrix(ConfigureNode node, int matrixID, String matrixType) {
/*  32 */     if ((matrixType != null) && (matrixType.toLowerCase().indexOf("dense") < 0)) {
/*  33 */       return null;
/*     */     }
/*  35 */     if ((matrixType == null) || (matrixType.toLowerCase().startsWith("double"))) {
/*  36 */       return loadDoubleDenseMatrix(node, matrixID);
/*     */     }
/*  38 */     return loadIntDenseMatrix(node, matrixID);
/*     */   }
/*     */ 
/*     */   public IntDenseMatrix getIntDenseMatrix(int matrixID) {
/*  42 */     return getIntDenseMatrix(this.root, matrixID);
/*     */   }
/*     */ 
/*     */   public IntDenseMatrix getIntDenseMatrix(ConfigureNode node, int matrixID) {
/*  46 */     return loadIntDenseMatrix(node, matrixID);
/*     */   }
/*     */ 
/*     */   private IntDenseMatrix loadIntDenseMatrix(ConfigureNode node, int matrixID)
/*     */   {
/*  52 */     ConfigureNode matrixNode = getConfigureNode(node, "IntDenseMatrix", matrixID);
/*  53 */     if (matrixNode == null)
/*  54 */       return null;
/*  55 */     String matrixName = matrixNode.getNodeName();
/*  56 */     return loadIntDenseMatrix(matrixName, matrixNode);
/*     */   }
/*     */ 
/*     */   protected IntDenseMatrix loadIntDenseMatrix(String matrixName, ConfigureNode matrixNode) {
/*  60 */     if (matrixName.equalsIgnoreCase("IntFlatDenseMatrix"))
/*  61 */       return loadIntFlatDenseMatrix(matrixNode);
/*  62 */     if (matrixName.equalsIgnoreCase("IntSuperDenseMatrix")) {
/*  63 */       return loadIntSuperDenseMatrix(matrixNode);
/*     */     }
/*  65 */     return (IntDenseMatrix)loadResource(matrixNode);
/*     */   }
/*     */ 
/*     */   private IntDenseMatrix loadIntFlatDenseMatrix(ConfigureNode node)
/*     */   {
/*  72 */     String filename = node.getString("filename", null);
/*  73 */     boolean binaryMode = node.getBoolean("binaryfile", false);
/*  74 */     return new IntFlatDenseMatrix(filename, binaryMode);
/*     */   }
/*     */ 
/*     */   private IntDenseMatrix loadIntSuperDenseMatrix(ConfigureNode node)
/*     */   {
/*  82 */     String filename = node.getString("filename", null);
/*  83 */     boolean readOnly = node.getBoolean("readonly", false);
/*  84 */     int columns = node.getInt("columns", 0);
/*  85 */     return new IntSuperDenseMatrix(filename, columns, readOnly);
/*     */   }
/*     */ 
/*     */   public DoubleDenseMatrix getDoubleDenseMatrix(int matrixID) {
/*  89 */     return getDoubleDenseMatrix(this.root, matrixID);
/*     */   }
/*     */ 
/*     */   public DoubleDenseMatrix getDoubleDenseMatrix(ConfigureNode node, int matrixID) {
/*  93 */     return loadDoubleDenseMatrix(node, matrixID);
/*     */   }
/*     */ 
/*     */   private DoubleDenseMatrix loadDoubleDenseMatrix(ConfigureNode node, int matrixID)
/*     */   {
/*  99 */     ConfigureNode matrixNode = getConfigureNode(node, "DoubleDenseMatrix", matrixID);
/* 100 */     if (matrixNode == null)
/* 101 */       return null;
/* 102 */     String matrixName = matrixNode.getNodeName();
/* 103 */     return loadDoubleDenseMatrix(matrixName, matrixNode);
/*     */   }
/*     */ 
/*     */   protected DoubleDenseMatrix loadDoubleDenseMatrix(String matrixName, ConfigureNode matrixNode) {
/* 107 */     if (matrixName.equalsIgnoreCase("DoubleFlatDenseMatrix"))
/* 108 */       return loadDoubleFlatDenseMatrix(matrixNode);
/* 109 */     if (matrixName.equalsIgnoreCase("DoubleSuperDenseMatrix")) {
/* 110 */       return loadDoubleSuperDenseMatrix(matrixNode);
/*     */     }
/* 112 */     return (DoubleDenseMatrix)loadResource(matrixNode);
/*     */   }
/*     */ 
/*     */   private DoubleDenseMatrix loadDoubleFlatDenseMatrix(ConfigureNode node)
/*     */   {
/* 119 */     String filename = node.getString("filename", null);
/* 120 */     boolean binaryMode = node.getBoolean("binaryfile", false);
/* 121 */     return new DoubleFlatDenseMatrix(filename, binaryMode);
/*     */   }
/*     */ 
/*     */   private DoubleDenseMatrix loadDoubleSuperDenseMatrix(ConfigureNode node)
/*     */   {
/* 129 */     String filename = node.getString("filename", null);
/* 130 */     boolean readOnly = node.getBoolean("readonly", false);
/* 131 */     int columns = node.getInt("columns", 0);
/* 132 */     return new DoubleSuperDenseMatrix(filename, columns, readOnly);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.DenseMatrixConfig
 * JD-Core Version:    0.6.2
 */