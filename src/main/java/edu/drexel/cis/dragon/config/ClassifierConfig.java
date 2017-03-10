/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.classification.Classifier;
/*     */ import edu.drexel.cis.dragon.ir.classification.DocClass;
/*     */ import edu.drexel.cis.dragon.ir.classification.LibSVMClassifier;
/*     */ import edu.drexel.cis.dragon.ir.classification.NBClassifier;
/*     */ import edu.drexel.cis.dragon.ir.classification.NigamActiveLearning;
/*     */ import edu.drexel.cis.dragon.ir.classification.SVMLightClassifier;
/*     */ import edu.drexel.cis.dragon.ir.classification.SemanticNBClassifier;
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.FeatureSelector;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.kngbase.KnowledgeBase;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class ClassifierConfig extends ConfigUtil
/*     */ {
/*     */   public ClassifierConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ClassifierConfig(ConfigureNode root)
/*     */   {
/*  25 */     super(root);
/*     */   }
/*     */ 
/*     */   public ClassifierConfig(String configFile) {
/*  29 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public Classifier getClassifier(int classifierID) {
/*  33 */     return getClassifier(this.root, classifierID);
/*     */   }
/*     */ 
/*     */   public Classifier getClassifier(ConfigureNode node, int classifierID) {
/*  37 */     return loadClassifier(node, classifierID);
/*     */   }
/*     */ 
/*     */   private Classifier loadClassifier(ConfigureNode node, int classifierID)
/*     */   {
/*  44 */     ConfigureNode classifierNode = getConfigureNode(node, "classifier", classifierID);
/*  45 */     if (classifierNode == null)
/*  46 */       return null;
/*  47 */     String classifierName = classifierNode.getNodeName();
/*  48 */     return loadClassifier(classifierName, classifierNode);
/*     */   }
/*     */ 
/*     */   protected Classifier loadClassifier(String classifierName, ConfigureNode classifierNode) {
/*  52 */     if (classifierName.equalsIgnoreCase("NBClassifier"))
/*  53 */       return loadNBClassifier(classifierNode);
/*  54 */     if (classifierName.equalsIgnoreCase("SVMLightClassifier"))
/*  55 */       return loadSVMLightClassifier(classifierNode);
/*  56 */     if (classifierName.equalsIgnoreCase("LibSVMClassifier"))
/*  57 */       return loadLibSVMClassifier(classifierNode);
/*  58 */     if (classifierName.equalsIgnoreCase("NigamActiveLearning"))
/*  59 */       return loadNigamActiveLearning(classifierNode);
/*  60 */     if (classifierName.equalsIgnoreCase("SemanticNBClassifier")) {
/*  61 */       return loadSemanticNBClassifier(classifierNode);
/*     */     }
/*  63 */     return (Classifier)loadResource(classifierNode);
/*     */   }
/*     */ 
/*     */   private Classifier loadNBClassifier(ConfigureNode node)
/*     */   {
/*  73 */     int indexReaderID = node.getInt("indexreader");
/*     */     NBClassifier classifier;
/*  75 */     if (indexReaderID > 0) {
/*  76 */       classifier = new NBClassifier(new IndexReaderConfig().getIndexReader(node, indexReaderID));
/*     */     } else {
/*  78 */       int matrixID = node.getInt("doctermmatrix");
/*  79 */       String matrixType = node.getParameterType("doctermmatrix", "doublesparsematrix");
/*     */       SparseMatrix sparseMatrix;
/*  80 */       if ((sparseMatrix = new SparseMatrixConfig().getSparseMatrix(node, matrixID, matrixType)) != null)
/*  81 */         classifier = new NBClassifier(sparseMatrix);
/*     */       else {
/*  83 */         classifier = new NBClassifier(new DenseMatrixConfig().getDenseMatrix(node, matrixID, matrixType));
/*     */       }
/*     */     }
/*  86 */     int selectorID = node.getInt("featureselector");
/*  87 */     FeatureSelector featureSelector = new FeatureSelectorConfig().getFeatureSelector(node, selectorID);
/*  88 */     if (featureSelector != null)
/*  89 */       classifier.setFeatureSelector(featureSelector);
/*  90 */     return classifier;
/*     */   }
/*     */ 
/*     */   private Classifier loadSVMLightClassifier(ConfigureNode node)
/*     */   {
/* 100 */     int indexReaderID = node.getInt("indexreader");
/*     */     SVMLightClassifier classifier;
/* 102 */     if (indexReaderID > 0) {
/* 103 */       classifier = new SVMLightClassifier(new IndexReaderConfig().getIndexReader(node, indexReaderID));
/*     */     } else {
/* 105 */       int matrixID = node.getInt("doctermmatrix");
/* 106 */       String matrixType = node.getParameterType("doctermmatrix", "doublesparsematrix");
/*     */       SparseMatrix sparseMatrix;
/* 107 */       if ((sparseMatrix = new SparseMatrixConfig().getSparseMatrix(node, matrixID, matrixType)) != null)
/* 108 */         classifier = new SVMLightClassifier(sparseMatrix);
/*     */       else {
/* 110 */         classifier = new SVMLightClassifier(new DenseMatrixConfig().getDenseMatrix(node, matrixID, matrixType));
/*     */       }
/*     */     }
/* 113 */     int selectorID = node.getInt("featureselector");
/* 114 */     FeatureSelector featureSelector = new FeatureSelectorConfig().getFeatureSelector(node, selectorID);
/* 115 */     if (featureSelector != null)
/* 116 */       classifier.setFeatureSelector(featureSelector);
/* 117 */     int matrixID = node.getInt("codematrix");
/* 118 */     if (matrixID > 0)
/* 119 */       classifier.setCodeMatrix(new CodeMatrixConfig().getCodeMatrix(node, matrixID));
/* 120 */     int decoderID = node.getInt("multiclassdecoder");
/* 121 */     if (decoderID > 0)
/* 122 */       classifier.setMultiClassDecoder(new MultiClassDecoderConfig().getMultiClassDecoder(node, decoderID));
/* 123 */     classifier.setScalingOption(node.getBoolean("scaling", false));
/* 124 */     return classifier;
/*     */   }
/*     */ 
/*     */   private Classifier loadLibSVMClassifier(ConfigureNode node)
/*     */   {
/* 134 */     int indexReaderID = node.getInt("indexreader");
/*     */     LibSVMClassifier classifier;
/* 136 */     if (indexReaderID > 0) {
/* 137 */       classifier = new LibSVMClassifier(new IndexReaderConfig().getIndexReader(node, indexReaderID));
/*     */     } else {
/* 139 */       int matrixID = node.getInt("doctermmatrix");
/* 140 */       String matrixType = node.getParameterType("doctermmatrix", "doublesparsematrix");
/*     */       SparseMatrix sparseMatrix;
/* 141 */       if ((sparseMatrix = new SparseMatrixConfig().getSparseMatrix(node, matrixID, matrixType)) != null)
/* 142 */         classifier = new LibSVMClassifier(sparseMatrix);
/*     */       else {
/* 144 */         classifier = new LibSVMClassifier(new DenseMatrixConfig().getDenseMatrix(node, matrixID, matrixType));
/*     */       }
/*     */     }
/* 147 */     int selectorID = node.getInt("featureselector");
/* 148 */     FeatureSelector featureSelector = new FeatureSelectorConfig().getFeatureSelector(node, selectorID);
/* 149 */     if (featureSelector != null)
/* 150 */       classifier.setFeatureSelector(featureSelector);
/* 151 */     int decoderID = node.getInt("multiclassdecoder");
/* 152 */     if (decoderID > 0)
/* 153 */       classifier.setMultiClassDecoder(new MultiClassDecoderConfig().getMultiClassDecoder(node, decoderID));
/* 154 */     classifier.setScalingOption(node.getBoolean("scaling", false));
/* 155 */     classifier.setScalingOption(node.getBoolean("propestimate", true));
/* 156 */     return classifier;
/*     */   }
/*     */ 
/*     */   private Classifier loadNigamActiveLearning(ConfigureNode node)
/*     */   {
/* 166 */     int selectorID = node.getInt("featureselector");
/* 167 */     FeatureSelector featureSelector = new FeatureSelectorConfig().getFeatureSelector(node, selectorID);
/* 168 */     int indexReaderID = node.getInt("indexreader");
/* 169 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/* 170 */     double unlabeledRate = node.getDouble("unlabeledrate", 0.0D);
/* 171 */     NigamActiveLearning classifier = new NigamActiveLearning(indexReader, unlabeledRate);
/* 172 */     if (featureSelector != null) {
/* 173 */       classifier.setFeatureSelector(featureSelector);
/*     */     }
/* 175 */     indexReaderID = node.getInt("unlabeledindexreader");
/* 176 */     int unlabeledDocNum = node.getInt("unlabeleddocnum");
/* 177 */     if ((indexReaderID > 0) && (unlabeledDocNum > 0)) {
/* 178 */       indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/* 179 */       classifier.setUnlabeledData(indexReader, prepareUnlabeledDocSet(indexReader, 10, unlabeledDocNum));
/*     */     }
/* 181 */     return classifier;
/*     */   }
/*     */ 
/*     */   private DocClass prepareUnlabeledDocSet(IndexReader reader, int randomSeed, int num)
/*     */   {
/* 189 */     if (reader == null) {
/* 190 */       return null;
/*     */     }
/* 192 */     int docNum = reader.getCollection().getDocNum();
/* 193 */     ArrayList list = new ArrayList(docNum);
/* 194 */     for (int i = 0; i < docNum; i++)
/* 195 */       list.add(new Integer(i));
/* 196 */     Collections.shuffle(list, new Random(randomSeed));
/* 197 */     DocClass docSet = new DocClass(0);
/* 198 */     for (int i = 0; i < num; i++)
/* 199 */       docSet.addDoc(reader.getDoc(((Integer)list.get(i)).intValue()));
/* 200 */     return docSet;
/*     */   }
/*     */ 
/*     */   private Classifier loadSemanticNBClassifier(ConfigureNode node)
/*     */   {
/* 211 */     double bkgCoefficient = node.getDouble("bkgcoefficient");
/* 212 */     int indexReaderID = node.getInt("indexreader");
/* 213 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/*     */ 
/* 215 */     int matrixID = node.getInt("transmatrix");
/* 216 */     int kngID = node.getInt("knowledgebase");
/*     */     SemanticNBClassifier classifier;
/* 217 */     if (matrixID > 0) {
/* 218 */       double transCoefficient = node.getDouble("transcoefficient");
/* 219 */       DoubleSparseMatrix transMatrix = new SparseMatrixConfig().getDoubleSparseMatrix(node, matrixID);
/* 220 */       int topicIndexReaderID = node.getInt("topicindexreader", indexReaderID);
/*     */       IndexReader topicIndexReader;
/* 221 */       if (topicIndexReaderID == indexReaderID)
/* 222 */         topicIndexReader = indexReader;
/*     */       else
/* 224 */         topicIndexReader = new IndexReaderConfig().getIndexReader(node, topicIndexReaderID);
/* 225 */       classifier = new SemanticNBClassifier(indexReader, topicIndexReader, transMatrix, transCoefficient, bkgCoefficient);
/*     */     }
/*     */     else
/*     */     {
/* 227 */       if (kngID > 0) {
/* 228 */         double transCoefficient = node.getDouble("transcoefficient");
/* 229 */         KnowledgeBase kngBase = new KnowledgeBaseConfig().getKnowledgeBase(node, kngID);
/* 230 */         int topicIndexReaderID = node.getInt("topicindexreader", indexReaderID);
/*     */         IndexReader topicIndexReader;
/* 231 */         if (topicIndexReaderID == indexReaderID)
/* 232 */           topicIndexReader = indexReader;
/*     */         else
/* 234 */           topicIndexReader = new IndexReaderConfig().getIndexReader(node, topicIndexReaderID);
/* 235 */         classifier = new SemanticNBClassifier(indexReader, topicIndexReader, kngBase, transCoefficient, bkgCoefficient);
/*     */       }
/*     */       else {
/* 238 */         classifier = new SemanticNBClassifier(indexReader, bkgCoefficient); } 
/* 239 */     }int selectorID = node.getInt("featureselector");
/* 240 */     if (selectorID > 0)
/* 241 */       classifier.setFeatureSelector(new FeatureSelectorConfig().getFeatureSelector(node, selectorID));
/* 242 */     return classifier;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.ClassifierConfig
 * JD-Core Version:    0.6.2
 */