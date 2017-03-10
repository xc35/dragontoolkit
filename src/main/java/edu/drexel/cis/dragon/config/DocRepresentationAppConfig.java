/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ import edu.drexel.cis.dragon.ir.kngbase.DocRepresentation;
/*    */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class DocRepresentationAppConfig
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 26 */     if (args.length != 2) {
/* 27 */       System.out.println("Please input two parameters: configuration xml file and document representation id");
/* 28 */       return;
/*    */     }
/*    */ 
/* 31 */     ConfigureNode root = new BasicConfigureNode(args[0]);
/* 32 */     ConfigUtil util = new ConfigUtil();
/* 33 */     ConfigureNode appNode = util.getConfigureNode(root, "docrepresentationapp", Integer.parseInt(args[1]));
/* 34 */     if (appNode == null)
/* 35 */       return;
/* 36 */     DocRepresentationAppConfig app = new DocRepresentationAppConfig();
/* 37 */     String appName = appNode.getNodeName();
/* 38 */     if (appName.equalsIgnoreCase("ModelDocRepresentationApp"))
/* 39 */       app.genModelMatrix(appNode);
/* 40 */     else if (appName.equalsIgnoreCase("TFIDFDocRepresentationApp"))
/* 41 */       app.genTFIDFMatrix(appNode);
/* 42 */     else if (appName.equalsIgnoreCase("NormTFDocRepresentationApp"))
/* 43 */       app.genNormTFMatrix(appNode);
/*    */     else;
/*    */   }
/*    */ 
/*    */   public void genModelMatrix(ConfigureNode node)
/*    */   {
/* 57 */     int indexReaderID = node.getInt("indexreader");
/* 58 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/* 59 */     DocRepresentation docRepresentation = new DocRepresentation(indexReader);
/* 60 */     int topicIndexReaderID = node.getInt("topicindexreader");
/* 61 */     IndexReader topicIndexReader = new IndexReaderConfig().getIndexReader(node, topicIndexReaderID);
/* 62 */     int transMatrixID = node.getInt("transmatrix");
/* 63 */     DoubleSparseMatrix transMatrix = new SparseMatrixConfig().getDoubleSparseMatrix(node, transMatrixID);
/* 64 */     double bkgCoefficient = node.getDouble("bkgcoefficient");
/* 65 */     double transCoefficient = node.getDouble("transcoefficient");
/* 66 */     double probThreshold = node.getDouble("probthreshold");
/* 67 */     boolean isPhraseSignature = node.getBoolean("phrasesignature", true);
/* 68 */     String matrixKey = node.getString("matrixkey", "doctermtrans");
/* 69 */     String matrixPath = node.getString("matrixpath");
/* 70 */     docRepresentation.genModelMatrix(topicIndexReader, transMatrix, transCoefficient, bkgCoefficient, isPhraseSignature, probThreshold, matrixPath, matrixKey);
/*    */   }
/*    */ 
/*    */   public void genTFIDFMatrix(ConfigureNode node)
/*    */   {
/* 79 */     int indexReaderID = node.getInt("indexreader");
/* 80 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/* 81 */     DocRepresentation docRepresentation = new DocRepresentation(indexReader);
/* 82 */     String matrixPath = node.getString("matrixpath");
/* 83 */     String matrixKey = node.getString("matrixkey", "doctermtfidf");
/* 84 */     docRepresentation.genTFIDFMatrix(matrixPath, matrixKey);
/*    */   }
/*    */ 
/*    */   public void genNormTFMatrix(ConfigureNode node)
/*    */   {
/* 93 */     int indexReaderID = node.getInt("indexreader");
/* 94 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/* 95 */     DocRepresentation docRepresentation = new DocRepresentation(indexReader);
/* 96 */     String matrixKey = node.getString("matrixkey", "doctermnormtf");
/* 97 */     String matrixPath = node.getString("matrixpath");
/* 98 */     docRepresentation.genNormTFMatrix(matrixPath, matrixKey);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.DocRepresentationAppConfig
 * JD-Core Version:    0.6.2
 */