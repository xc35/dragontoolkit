/*     */ package edu.drexel.cis.dragon.ir.summarize;
/*     */ 
/*     */ import edu.drexel.cis.dragon.config.ConfigureNode;
/*     */ import edu.drexel.cis.dragon.config.IndexerConfig;
/*     */ import edu.drexel.cis.dragon.ir.clustering.BasicKMean;
/*     */ import edu.drexel.cis.dragon.ir.clustering.clustermodel.ClusterModel;
/*     */ import edu.drexel.cis.dragon.ir.clustering.clustermodel.CosineClusterModel;
/*     */ import edu.drexel.cis.dragon.ir.clustering.docdistance.CosineDocDistance;
/*     */ import edu.drexel.cis.dragon.ir.clustering.docdistance.DocDistance;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.sentence.OnlineSentenceIndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.sentence.OnlineSentenceIndexer;
/*     */ import edu.drexel.cis.dragon.ir.kngbase.DocRepresentation;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ import edu.drexel.cis.dragon.matrix.vector.PowerMethod;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class ClusterLexRankSummarizer extends LexRankSummarizer
/*     */ {
/*     */   protected ClusterModel clusterModel;
/*     */   private int clusterNum;
/*     */ 
/*     */   public static ClusterLexRankSummarizer getClusterLexRankSummarizer(ConfigureNode node)
/*     */   {
/*  38 */     boolean tfidf = node.getBoolean("tfidf", true);
/*  39 */     boolean continuous = node.getBoolean("continuousscore", true);
/*  40 */     double similarityThreshold = node.getDouble("similaritythreshold");
/*  41 */     int clusterNum = node.getInt("clusternum");
/*  42 */     int indexerID = node.getInt("onlinesentenceindexer");
/*  43 */     OnlineSentenceIndexer indexer = (OnlineSentenceIndexer)new IndexerConfig().getIndexer(node, indexerID);
/*  44 */     ClusterLexRankSummarizer summarizer = new ClusterLexRankSummarizer(indexer, tfidf, clusterNum);
/*  45 */     summarizer.setContinuousScoreOpiton(continuous);
/*  46 */     summarizer.setSimilarityThreshold(similarityThreshold);
/*  47 */     return summarizer;
/*     */   }
/*     */ 
/*     */   public ClusterLexRankSummarizer(OnlineSentenceIndexer indexer, int clusterNum) {
/*  51 */     super(indexer);
/*  52 */     this.clusterNum = clusterNum;
/*     */   }
/*     */ 
/*     */   public ClusterLexRankSummarizer(OnlineSentenceIndexer indexer, boolean useTFIDF, int clusterNum) {
/*  56 */     super(indexer, useTFIDF);
/*  57 */     this.clusterNum = clusterNum;
/*     */   }
/*     */ 
/*     */   public String summarize(CollectionReader collectionReader, int maxLength)
/*     */   {
/*  68 */     this.collectionReader = collectionReader;
/*  69 */     this.indexReader = new OnlineSentenceIndexReader(this.indexer, collectionReader);
/*  70 */     this.indexReader.initialize();
/*  71 */     ArrayList sentSet = getSentenceSet(this.indexReader);
/*  72 */     IRDoc[] arrDoc = new IRDoc[sentSet.size()];
/*  73 */     for (int i = 0; i < arrDoc.length; i++)
/*  74 */       arrDoc[i] = ((IRDoc)sentSet.get(i));
/*  75 */     DoubleVector vector = this.powerMethod.getEigenVector(buildWeightMatrix(sentSet));
/*     */ 
/*  77 */     BasicKMean kmean = new BasicKMean(this.indexReader, this.clusterModel, this.clusterNum);
/*  78 */     kmean.setMaxIteration(50);
/*  79 */     kmean.setRandomSeed(100L);
/*  80 */     kmean.setShowProgress(false);
/*  81 */     kmean.cluster();
/*  82 */     String summary = buildSummary(this.indexReader, sentSet, maxLength, vector, kmean.getClusterSet());
/*  83 */     this.indexReader.close();
/*  84 */     this.distanceMetric = null;
/*  85 */     this.clusterModel = null;
/*  86 */     return summary;
/*     */   }
/*     */ 
/*     */   protected double computeSimilarity(IRDoc firstSent, IRDoc secondSent)
/*     */   {
/*  92 */     if (this.distanceMetric == null) {
/*  93 */       if (this.useTFIDF) {
/*  94 */         DocRepresentation docRepresentation = new DocRepresentation(this.indexReader);
/*  95 */         docRepresentation.setMessageOption(false);
/*  96 */         DoubleSparseMatrix matrix = docRepresentation.genTFIDFMatrix();
/*  97 */         this.distanceMetric = new CosineDocDistance(matrix);
/*  98 */         this.clusterModel = new CosineClusterModel(this.clusterNum, matrix);
/*     */       }
/*     */       else {
/* 101 */         this.distanceMetric = new CosineDocDistance(this.indexReader.getDocTermMatrix());
/* 102 */         this.clusterModel = new CosineClusterModel(this.clusterNum, this.indexReader.getDocTermMatrix());
/*     */       }
/*     */     }
/* 105 */     return 1.0D - this.distanceMetric.getDistance(firstSent, secondSent);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.summarize.ClusterLexRankSummarizer
 * JD-Core Version:    0.6.2
 */