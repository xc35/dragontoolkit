/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.topicmodel.AspectModel;
/*     */ import edu.drexel.cis.dragon.ir.topicmodel.GibbsLDA;
/*     */ import edu.drexel.cis.dragon.ir.topicmodel.SimpleMixtureModel;
/*     */ import edu.drexel.cis.dragon.ir.topicmodel.TopicModel;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ 
/*     */ public class TopicModelConfig extends ConfigUtil
/*     */ {
/*     */   public TopicModelConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public TopicModelConfig(ConfigureNode root)
/*     */   {
/*  22 */     super(root);
/*     */   }
/*     */ 
/*     */   public TopicModelConfig(String configFile) {
/*  26 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public TopicModel getTopicModel(int modelID) {
/*  30 */     return getTopicModel(this.root, modelID);
/*     */   }
/*     */ 
/*     */   public TopicModel getTopicModel(ConfigureNode node, int modelID) {
/*  34 */     return loadTopicModel(node, modelID);
/*     */   }
/*     */ 
/*     */   private TopicModel loadTopicModel(ConfigureNode node, int modelID)
/*     */   {
/*  41 */     ConfigureNode modelNode = getConfigureNode(node, "topicmodel", modelID);
/*  42 */     if (modelNode == null)
/*  43 */       return null;
/*  44 */     String modelName = modelNode.getNodeName();
/*  45 */     return loadTopicModel(modelName, modelNode);
/*     */   }
/*     */ 
/*     */   protected TopicModel loadTopicModel(String modelName, ConfigureNode modelNode) {
/*  49 */     if (modelName.equalsIgnoreCase("GibbsLDA"))
/*  50 */       return loadGibbsLDA(modelNode);
/*  51 */     if (modelName.equalsIgnoreCase("AspectModel"))
/*  52 */       return loadAspectModel(modelNode);
/*  53 */     if (modelName.equalsIgnoreCase("SimpleMixtureModel")) {
/*  54 */       return loadSimpleMixtureModel(modelNode);
/*     */     }
/*  56 */     return (TopicModel)loadResource(modelNode);
/*     */   }
/*     */ 
/*     */   private TopicModel loadGibbsLDA(ConfigureNode node)
/*     */   {
/*  65 */     double alpha = node.getDouble("alpha");
/*  66 */     double beta = node.getDouble("beta");
/*  67 */     int indexReaderID = node.getInt("indexreader");
/*  68 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/*  69 */     GibbsLDA model = new GibbsLDA(indexReader, alpha, beta);
/*  70 */     model.setIterationNum(node.getInt("iterations", 100));
/*  71 */     model.setRandomSeed(node.getInt("randomseed", -1));
/*  72 */     return model;
/*     */   }
/*     */ 
/*     */   private TopicModel loadAspectModel(ConfigureNode node)
/*     */   {
/*  80 */     int indexReaderID = node.getInt("indexreader");
/*  81 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/*  82 */     AspectModel model = new AspectModel(indexReader);
/*  83 */     model.setIterationNum(node.getInt("iterations", 100));
/*  84 */     model.setRandomSeed(node.getInt("randomseed", -1));
/*  85 */     return model;
/*     */   }
/*     */ 
/*     */   private TopicModel loadSimpleMixtureModel(ConfigureNode node)
/*     */   {
/*  95 */     double bkgCoefficient = node.getDouble("bkgCoefficient");
/*  96 */     int indexReaderID = node.getInt("indexreader");
/*  97 */     IndexReader indexReader = new IndexReaderConfig().getIndexReader(node, indexReaderID);
/*  98 */     int bkgIndexReaderID = node.getInt("bkgindexreader", indexReaderID);
/*     */     SimpleMixtureModel model;
/*  99 */     if (bkgIndexReaderID == indexReaderID) {
/* 100 */       model = new SimpleMixtureModel(indexReader, bkgCoefficient);
/*     */     } else {
/* 102 */       IndexReader bkgIndexReader = new IndexReaderConfig().getIndexReader(node, bkgIndexReaderID);
/* 103 */       DoubleVector bkgModel = getBackgroundModel(indexReader, bkgIndexReader);
/* 104 */       bkgIndexReader.close();
/* 105 */       model = new SimpleMixtureModel(indexReader, bkgModel, bkgCoefficient);
/*     */     }
/* 107 */     model.setIterationNum(node.getInt("iterations", 100));
/* 108 */     model.setRandomSeed(node.getInt("randomseed", -1));
/* 109 */     return model;
/*     */   }
/*     */ 
/*     */   private DoubleVector getBackgroundModel(IndexReader indexReader, IndexReader bkgIndexReader)
/*     */   {
/* 119 */     DoubleVector bkgModel = new DoubleVector(indexReader.getCollection().getTermNum());
/* 120 */     boolean needSmooth = false;
/*     */ 
/* 122 */     double sum = 0.0D;
/* 123 */     for (int i = 0; i < bkgModel.size(); i++) {
/* 124 */       IRTerm curTerm = bkgIndexReader.getIRTerm(indexReader.getTermKey(i));
/*     */       int freq;
/* 125 */       if (curTerm != null) {
/* 126 */         freq = curTerm.getFrequency();
/* 127 */         if (freq == 0)
/* 128 */           needSmooth = true;
/*     */       }
/*     */       else {
/* 131 */         freq = 0;
/* 132 */         needSmooth = true;
/*     */       }
/* 134 */       sum += freq;
/* 135 */       bkgModel.set(i, freq);
/*     */     }
/*     */ 
/* 138 */     if (needSmooth) {
/* 139 */       for (int i = 0; i < bkgModel.size(); i++)
/* 140 */         bkgModel.add(i, 1.0D);
/* 141 */       sum += bkgModel.size();
/*     */     }
/* 143 */     bkgModel.multiply(1.0D / sum);
/* 144 */     return bkgModel;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.TopicModelConfig
 * JD-Core Version:    0.6.2
 */