/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.DoubleFlatSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleGiantSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSuperSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntFlatSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntGiantSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntSuperSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ 
/*     */ public class SparseMatrixConfig extends ConfigUtil
/*     */ {
/*     */   public SparseMatrixConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SparseMatrixConfig(ConfigureNode root)
/*     */   {
/*  20 */     super(root);
/*     */   }
/*     */ 
/*     */   public SparseMatrixConfig(String configFile) {
/*  24 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public SparseMatrix getSparseMatrix(int matrixID, String matrixType) {
/*  28 */     return getSparseMatrix(this.root, matrixID, matrixType);
/*     */   }
/*     */ 
/*     */   public SparseMatrix getSparseMatrix(ConfigureNode node, int matrixID, String matrixType) {
/*  32 */     if ((matrixType != null) && (matrixType.toLowerCase().indexOf("sparse") < 0)) {
/*  33 */       return null;
/*     */     }
/*  35 */     if ((matrixType == null) || (matrixType.toLowerCase().startsWith("double"))) {
/*  36 */       return loadDoubleSparseMatrix(node, matrixID);
/*     */     }
/*  38 */     return loadIntSparseMatrix(node, matrixID);
/*     */   }
/*     */ 
/*     */   public IntSparseMatrix getIntSparseMatrix(int matrixID) {
/*  42 */     return getIntSparseMatrix(this.root, matrixID);
/*     */   }
/*     */ 
/*     */   public IntSparseMatrix getIntSparseMatrix(ConfigureNode node, int matrixID) {
/*  46 */     return loadIntSparseMatrix(node, matrixID);
/*     */   }
/*     */ 
/*     */   private IntSparseMatrix loadIntSparseMatrix(ConfigureNode node, int matrixID)
/*     */   {
/*  52 */     ConfigureNode matrixNode = getConfigureNode(node, "intsparsematrix", matrixID);
/*  53 */     if (matrixNode == null)
/*  54 */       return null;
/*  55 */     String matrixName = matrixNode.getNodeName();
/*  56 */     return loadIntSparseMatrix(matrixName, matrixNode);
/*     */   }
/*     */ 
/*     */   protected IntSparseMatrix loadIntSparseMatrix(String matrixName, ConfigureNode matrixNode) {
/*  60 */     if (matrixName.equalsIgnoreCase("IntSuperSparseMatrix"))
/*  61 */       return loadIntSuperSparseMatrix(matrixNode);
/*  62 */     if (matrixName.equalsIgnoreCase("IntGiantSparseMatrix"))
/*  63 */       return loadIntGiantSparseMatrix(matrixNode);
/*  64 */     if (matrixName.equalsIgnoreCase("IntFlatSparseMatrix")) {
/*  65 */       return loadIntFlatSparseMatrix(matrixNode);
/*     */     }
/*  67 */     return (IntSparseMatrix)loadResource(matrixNode);
/*     */   }
/*     */ 
/*     */   private IntSparseMatrix loadIntFlatSparseMatrix(ConfigureNode node)
/*     */   {
/*  74 */     String filename = node.getString("filename", null);
/*  75 */     boolean binaryMode = node.getBoolean("binaryfile", false);
/*  76 */     boolean mergeMode = node.getBoolean("mergemode", false);
/*  77 */     boolean miniMode = node.getBoolean("minimode", false);
/*  78 */     if (filename != null) {
/*  79 */       return new IntFlatSparseMatrix(filename, binaryMode);
/*     */     }
/*  81 */     return new IntFlatSparseMatrix(mergeMode, miniMode);
/*     */   }
/*     */ 
/*     */   private IntSparseMatrix loadIntSuperSparseMatrix(ConfigureNode node)
/*     */   {
/*  88 */     String path = node.getString("matrixpath");
/*  89 */     String key = node.getString("matrixkey");
/*  90 */     String matrixFile = path + "/" + key + ".matrix";
/*  91 */     String indexFile = path + "/" + key + ".index";
/*  92 */     if (node.exist("mergemode")) {
/*  93 */       return new IntSuperSparseMatrix(indexFile, matrixFile, node.getBoolean("mergemode", false), node.getBoolean("minimode", false));
/*     */     }
/*  95 */     return new IntSuperSparseMatrix(indexFile, matrixFile);
/*     */   }
/*     */ 
/*     */   private IntSparseMatrix loadIntGiantSparseMatrix(ConfigureNode node)
/*     */   {
/* 102 */     String path = node.getString("matrixpath");
/* 103 */     String key = node.getString("matrixkey");
/* 104 */     String matrixFile = path + "/" + key + ".matrix";
/* 105 */     String indexFile = path + "/" + key + ".index";
/* 106 */     if (node.exist("mergemode")) {
/* 107 */       return new IntGiantSparseMatrix(indexFile, matrixFile, node.getBoolean("mergemode", false), node.getBoolean("minimode", false));
/*     */     }
/* 109 */     return new IntGiantSparseMatrix(indexFile, matrixFile);
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix getDoubleSparseMatrix(int matrixID) {
/* 113 */     return getDoubleSparseMatrix(this.root, matrixID);
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix getDoubleSparseMatrix(ConfigureNode node, int matrixID) {
/* 117 */     return loadDoubleSparseMatrix(node, matrixID);
/*     */   }
/*     */ 
/*     */   private DoubleSparseMatrix loadDoubleSparseMatrix(ConfigureNode node, int matrixID)
/*     */   {
/* 123 */     ConfigureNode matrixNode = getConfigureNode(node, "doublesparsematrix", matrixID);
/* 124 */     if (matrixNode == null)
/* 125 */       return null;
/* 126 */     String matrixName = matrixNode.getNodeName();
/* 127 */     return loadDoubleSparseMatrix(matrixName, matrixNode);
/*     */   }
/*     */ 
/*     */   protected DoubleSparseMatrix loadDoubleSparseMatrix(String matrixName, ConfigureNode matrixNode) {
/* 131 */     if (matrixName.equalsIgnoreCase("DoubleSuperSparseMatrix"))
/* 132 */       return loadDoubleSuperSparseMatrix(matrixNode);
/* 133 */     if (matrixName.equalsIgnoreCase("DoubleGiantSparseMatrix"))
/* 134 */       return loadDoubleGiantSparseMatrix(matrixNode);
/* 135 */     if (matrixName.equalsIgnoreCase("DoubleFlatSparseMatrix")) {
/* 136 */       return loadDoubleFlatSparseMatrix(matrixNode);
/*     */     }
/* 138 */     return (DoubleSparseMatrix)loadResource(matrixNode);
/*     */   }
/*     */ 
/*     */   private DoubleSparseMatrix loadDoubleFlatSparseMatrix(ConfigureNode node)
/*     */   {
/* 145 */     String filename = node.getString("filename", null);
/* 146 */     boolean binaryMode = node.getBoolean("binaryfile", false);
/* 147 */     boolean mergeMode = node.getBoolean("mergemode", false);
/* 148 */     boolean miniMode = node.getBoolean("minimode", false);
/* 149 */     if (filename != null) {
/* 150 */       return new DoubleFlatSparseMatrix(filename, binaryMode);
/*     */     }
/* 152 */     return new DoubleFlatSparseMatrix(mergeMode, miniMode);
/*     */   }
/*     */ 
/*     */   private DoubleSparseMatrix loadDoubleSuperSparseMatrix(ConfigureNode node)
/*     */   {
/* 159 */     String path = node.getString("matrixpath");
/* 160 */     String key = node.getString("matrixkey");
/* 161 */     String matrixFile = path + "/" + key + ".matrix";
/* 162 */     String indexFile = path + "/" + key + ".index";
/* 163 */     if (node.exist("mergemode")) {
/* 164 */       return new DoubleSuperSparseMatrix(indexFile, matrixFile, node.getBoolean("mergemode", false), node.getBoolean("minimode", false));
/*     */     }
/* 166 */     return new DoubleSuperSparseMatrix(indexFile, matrixFile);
/*     */   }
/*     */ 
/*     */   private DoubleSparseMatrix loadDoubleGiantSparseMatrix(ConfigureNode node)
/*     */   {
/* 173 */     String path = node.getString("matrixpath");
/* 174 */     String key = node.getString("matrixkey");
/* 175 */     String matrixFile = path + "/" + key + ".matrix";
/* 176 */     String indexFile = path + "/" + key + ".index";
/* 177 */     if (node.exist("mergemode")) {
/* 178 */       return new DoubleGiantSparseMatrix(indexFile, matrixFile, node.getBoolean("mergemode", false), node.getBoolean("minimode", false));
/*     */     }
/* 180 */     return new DoubleGiantSparseMatrix(indexFile, matrixFile);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.SparseMatrixConfig
 * JD-Core Version:    0.6.2
 */