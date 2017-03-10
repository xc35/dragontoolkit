/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.BasicIRTermIndexList;
/*     */ import edu.drexel.cis.dragon.ir.index.IRSignatureIndexList;
/*     */ import edu.drexel.cis.dragon.ir.kngbase.TopicSignatureModel;
/*     */ import edu.drexel.cis.dragon.matrix.IntGiantSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntSuperSparseMatrix;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class TopicSignatureMappingAppConfig
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  25 */     if (args.length != 2) {
/*  26 */       System.out.println("Please input two parameters: configuration xml file and translation applicaiton id");
/*  27 */       return;
/*     */     }
/*     */ 
/*  30 */     ConfigureNode root = new BasicConfigureNode(args[0]);
/*  31 */     ConfigUtil util = new ConfigUtil();
/*  32 */     ConfigureNode transAppNode = util.getConfigureNode(root, "translationapp", Integer.parseInt(args[1]));
/*  33 */     if (transAppNode == null)
/*  34 */       return;
/*  35 */     TopicSignatureMappingAppConfig transApp = new TopicSignatureMappingAppConfig();
/*  36 */     transApp.mapping(transAppNode);
/*     */   }
/*     */ 
/*     */   public void mapping(ConfigureNode transNode)
/*     */   {
/*  47 */     String srcIndexSection = transNode.getString("srcindexsection");
/*  48 */     if (srcIndexSection == null)
/*  49 */       srcIndexSection = transNode.getString("indexsection", "all");
/*  50 */     String destIndexSection = transNode.getString("destindexsection");
/*  51 */     if (destIndexSection == null)
/*  52 */       destIndexSection = transNode.getString("indexsection", "all");
/*  53 */     String srcIndexFolder = transNode.getString("srcindexfolder");
/*  54 */     String destIndexFolder = transNode.getString("destindexfolder", srcIndexFolder);
/*  55 */     srcIndexFolder = srcIndexFolder + "/" + srcIndexSection;
/*  56 */     destIndexFolder = destIndexFolder + "/" + destIndexSection;
/*  57 */     String transMatrixKey = transNode.getString("translationmatrixkey");
/*  58 */     boolean relation = transNode.getBoolean("relation", false);
/*  59 */     boolean useEM = transNode.getBoolean("useem", true);
/*  60 */     boolean useDocFreq = transNode.getBoolean("usedocfrequency", true);
/*  61 */     boolean useMeanTrim = transNode.getBoolean("usemeantrim", false);
/*  62 */     int minFrequency = transNode.getInt("minfrequency", 2);
/*  63 */     double emBkgCoefficient = transNode.getDouble("embkgcoefficient", 0.5D);
/*  64 */     double probThreshold = transNode.getDouble("probthreshold", 0.001D);
/*  65 */     int matrixID = transNode.getInt("cooccurrencematrix");
/*     */     IntSparseMatrix cooccurMatrix;
/*  66 */     if (matrixID > 0)
/*  67 */       cooccurMatrix = new SparseMatrixConfig().getIntSparseMatrix(transNode, matrixID);
/*     */     else
/*  69 */       cooccurMatrix = null;
/*  70 */     mapping(cooccurMatrix, srcIndexFolder, relation, minFrequency, destIndexFolder, transMatrixKey, useEM, useDocFreq, 
/*  71 */       useMeanTrim, probThreshold, emBkgCoefficient);
/*     */   }
/*     */ 
/*     */   public void mapping(IntSparseMatrix cooccurMatrix, String srcIndexFolder, boolean relation, int minFrequency, String destIndexFolder, String matrixKey, boolean useEM, boolean useDocFreq, boolean useMeanTrim, double probThreshold, double emBkgCoefficient)
/*     */   {
/*     */     IRSignatureIndexList srcIndexList;
/*  80 */     if (relation)
/*  81 */       srcIndexList = new BasicIRTermIndexList(srcIndexFolder + "/relationindex.list", false);
/*     */     else
/*  83 */       srcIndexList = new BasicIRTermIndexList(srcIndexFolder + "/termindex.list", false);
/*     */     IRSignatureIndexList destIndexList;
/*  84 */     if (useEM)
/*  85 */       destIndexList = new BasicIRTermIndexList(destIndexFolder + "/termindex.list", false);
/*     */     else
/*  87 */       destIndexList = null;
/*     */     TopicSignatureModel model;
/*  88 */     if (cooccurMatrix == null)
/*     */     {
/*     */       IntSparseMatrix srcMatrix;
/*  89 */       if (relation)
/*  90 */         srcMatrix = new IntGiantSparseMatrix(srcIndexFolder + "/relationdoc.index", srcIndexFolder + "/relationdoc.matrix");
/*     */       else
/*  92 */         srcMatrix = new IntGiantSparseMatrix(srcIndexFolder + "/termdoc.index", srcIndexFolder + "/termdoc.matrix");
/*  93 */       IntSparseMatrix destMatrix = new IntSuperSparseMatrix(destIndexFolder + "/docterm.index", destIndexFolder + "/docterm.matrix");
/*  94 */       model = new TopicSignatureModel(srcIndexList, srcMatrix, destIndexList, destMatrix);
/*     */     }
/*     */     else {
/*  97 */       model = new TopicSignatureModel(srcIndexList, destIndexList, cooccurMatrix);
/*  98 */     }model.setUseDocFrequency(useDocFreq);
/*  99 */     model.setUseEM(useEM);
/* 100 */     model.setUseMeanTrim(useMeanTrim);
/* 101 */     model.setProbThreshold(probThreshold);
/* 102 */     model.setEMBackgroundCoefficient(emBkgCoefficient);
/* 103 */     model.genMappingMatrix(minFrequency, destIndexFolder, matrixKey);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.TopicSignatureMappingAppConfig
 * JD-Core Version:    0.6.2
 */