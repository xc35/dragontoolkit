/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.BasicIRTermIndexList;
/*    */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*    */ import edu.drexel.cis.dragon.ir.index.IRTermIndexList;
/*    */ import edu.drexel.cis.dragon.ir.kngbase.CooccurrenceGenerator;
/*    */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class CooccurrenceAppConfig
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 22 */     if (args.length != 2) {
/* 23 */       System.out.println("Please input two parameters: configuration xml file and indexing applicaiton id");
/* 24 */       return;
/*    */     }
/*    */ 
/* 27 */     ConfigureNode root = new BasicConfigureNode(args[0]);
/* 28 */     ConfigUtil util = new ConfigUtil();
/* 29 */     ConfigureNode appNode = util.getConfigureNode(root, "cooccurrenceapp", Integer.parseInt(args[1]));
/* 30 */     if (appNode == null)
/* 31 */       return;
/* 32 */     CooccurrenceAppConfig cooccurApp = new CooccurrenceAppConfig();
/* 33 */     cooccurApp.generateCooccurrenceMatrix(appNode);
/*    */   }
/*    */ 
/*    */   public void generateCooccurrenceMatrix(ConfigureNode node)
/*    */   {
/* 41 */     int minDocFreq = node.getInt("mindocfrequency", 1);
/* 42 */     int maxDocFreq = node.getInt("maxdocfrequency", 2147483647);
/* 43 */     int cache = node.getInt("cache", 5000000);
/* 44 */     String matrixFolder = node.getString("cooccurrencematrixpath");
/* 45 */     String matrixKey = node.getString("cooccurrencematrixkey");
/* 46 */     int matrixAID = node.getInt("firstmatrix");
/* 47 */     int matrixBID = node.getInt("secondmatrix", matrixAID);
/* 48 */     String indexListFileA = node.getString("firstindexlistfile", null);
/* 49 */     String indexListFileB = node.getString("secondindexlistfile", null);
/* 50 */     IntSparseMatrix matrixA = new SparseMatrixConfig().getIntSparseMatrix(node, matrixAID);
/* 51 */     if (matrixAID == matrixBID) {
/* 52 */       generateCooccurrenceMatrix(matrixA, indexListFileA, matrixFolder, matrixKey, cache, minDocFreq, maxDocFreq);
/*    */     } else {
/* 54 */       IntSparseMatrix matrixB = new SparseMatrixConfig().getIntSparseMatrix(node, matrixBID);
/* 55 */       generateCooccurrenceMatrix(matrixA, indexListFileA, matrixB, indexListFileB, matrixFolder, matrixKey, cache, minDocFreq, maxDocFreq);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void generateCooccurrenceMatrix(IntSparseMatrix doctermMatrixA, String indexListFileA, IntSparseMatrix doctermMatrixB, String indexListFileB, String matrixFolder, String matrixKey, int cache, int minDocFreq, int maxDocFreq)
/*    */   {
/* 63 */     CooccurrenceGenerator generator = new CooccurrenceGenerator();
/* 64 */     generator.setCacheSize(cache);
/* 65 */     generator.setMinDocFrequency(minDocFreq);
/* 66 */     generator.setMaxDocFrequency(maxDocFreq);
/* 67 */     generator.generate(doctermMatrixA, getTermDocFrequencyList(indexListFileA), doctermMatrixB, 
/* 68 */       getTermDocFrequencyList(indexListFileB), matrixFolder, matrixKey);
/*    */   }
/*    */ 
/*    */   public void generateCooccurrenceMatrix(IntSparseMatrix doctermMatrix, String indexListFile, String matrixFolder, String matrixKey, int cache, int minDocFreq, int maxDocFreq)
/*    */   {
/* 75 */     CooccurrenceGenerator generator = new CooccurrenceGenerator();
/* 76 */     generator.setCacheSize(cache);
/* 77 */     generator.setMinDocFrequency(minDocFreq);
/* 78 */     generator.setMaxDocFrequency(maxDocFreq);
/* 79 */     generator.generate(doctermMatrix, getTermDocFrequencyList(indexListFile), matrixFolder, matrixKey);
/*    */   }
/*    */ 
/*    */   public int[] getTermDocFrequencyList(String indexListFile)
/*    */   {
/* 86 */     if (indexListFile == null)
/* 87 */       return null;
/* 88 */     IRTermIndexList list = new BasicIRTermIndexList(indexListFile, false);
/* 89 */     int[] arrDocFreq = new int[list.size()];
/* 90 */     for (int i = 0; i < list.size(); i++)
/* 91 */       arrDocFreq[i] = list.get(i).getDocFrequency();
/* 92 */     return arrDocFreq;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.CooccurrenceAppConfig
 * JD-Core Version:    0.6.2
 */