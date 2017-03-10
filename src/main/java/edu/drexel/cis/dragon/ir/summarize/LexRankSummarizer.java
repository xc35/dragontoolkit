/*     */ package edu.drexel.cis.dragon.ir.summarize;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.docdistance.CosineDocDistance;
/*     */ import edu.drexel.cis.dragon.ir.clustering.docdistance.DocDistance;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.sentence.OnlineSentenceIndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.sentence.OnlineSentenceIndexer;
/*     */ import edu.drexel.cis.dragon.ir.kngbase.DocRepresentation;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleFlatDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ import edu.drexel.cis.dragon.matrix.vector.PowerMethod;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class LexRankSummarizer extends AbstractSentenceSum
/*     */   implements GenericMultiDocSummarizer
/*     */ {
/*     */   protected OnlineSentenceIndexReader indexReader;
/*     */   protected OnlineSentenceIndexer indexer;
/*     */   protected CollectionReader collectionReader;
/*     */   protected DocDistance distanceMetric;
/*     */   protected PowerMethod powerMethod;
/*     */   protected double threshold;
/*     */   protected boolean useContinuousValue;
/*     */   protected boolean useTFIDF;
/*     */ 
/*     */   public LexRankSummarizer(OnlineSentenceIndexer indexer)
/*     */   {
/*  31 */     this(indexer, true);
/*     */   }
/*     */ 
/*     */   public LexRankSummarizer(OnlineSentenceIndexer indexer, boolean useTFIDF) {
/*  35 */     this.indexer = indexer;
/*  36 */     this.threshold = 0.1D;
/*  37 */     this.useContinuousValue = true;
/*  38 */     this.useTFIDF = useTFIDF;
/*  39 */     this.powerMethod = new PowerMethod(0.0001D, 0.15D);
/*  40 */     this.powerMethod.setMessageOption(false);
/*  41 */     this.powerMethod.setMaxIteration(50);
/*     */   }
/*     */ 
/*     */   public void setSimilarityThreshold(double threshold) {
/*  45 */     this.threshold = threshold;
/*     */   }
/*     */ 
/*     */   public void setContinuousScoreOpiton(boolean option) {
/*  49 */     this.useContinuousValue = option;
/*     */   }
/*     */ 
/*     */   public String summarize(CollectionReader collectionReader, int maxLength)
/*     */   {
/*  57 */     this.collectionReader = collectionReader;
/*  58 */     this.indexReader = new OnlineSentenceIndexReader(this.indexer, collectionReader);
/*  59 */     this.indexReader.initialize();
/*  60 */     ArrayList sentSet = getSentenceSet(this.indexReader);
/*  61 */     DoubleVector vector = this.powerMethod.getEigenVector(buildWeightMatrix(sentSet));
/*  62 */     String summary = buildSummary(this.indexReader, sentSet, maxLength, vector);
/*  63 */     this.indexReader.close();
/*  64 */     this.distanceMetric = null;
/*  65 */     return summary;
/*     */   }
/*     */ 
/*     */   protected DoubleDenseMatrix buildWeightMatrix(ArrayList docSet)
/*     */   {
/*  74 */     DoubleFlatDenseMatrix matrix = new DoubleFlatDenseMatrix(docSet.size(), docSet.size());
/*  75 */     for (int i = 0; i < docSet.size(); i++) {
/*  76 */       matrix.setDouble(i, i, 1.0D);
/*  77 */       IRDoc first = (IRDoc)docSet.get(i);
/*  78 */       for (int j = i + 1; j < docSet.size(); j++) {
/*  79 */         IRDoc second = (IRDoc)docSet.get(j);
/*  80 */         double similarity = computeSimilarity(first, second);
/*  81 */         if (!this.useContinuousValue) {
/*  82 */           if (similarity <= this.threshold)
/*  83 */             similarity = 0.0D;
/*     */           else
/*  85 */             similarity = 1.0D;
/*     */         }
/*  87 */         matrix.setDouble(i, j, similarity);
/*  88 */         matrix.setDouble(j, i, similarity);
/*     */       }
/*     */     }
/*  91 */     return matrix;
/*     */   }
/*     */ 
/*     */   protected double computeSimilarity(IRDoc firstSent, IRDoc secondSent)
/*     */   {
/* 103 */     if (this.distanceMetric == null)
/* 104 */       if (this.useTFIDF) {
/* 105 */         DocRepresentation docRepresentation = new DocRepresentation(this.indexReader);
/* 106 */         docRepresentation.setMessageOption(false);
/* 107 */         this.distanceMetric = new CosineDocDistance(docRepresentation.genTFIDFMatrix());
/*     */       }
/*     */       else {
/* 110 */         this.distanceMetric = new CosineDocDistance(this.indexReader.getDocTermMatrix());
/*     */       }
/* 112 */     return 1.0D - this.distanceMetric.getDistance(firstSent, secondSent);
/*     */   }
/*     */ 
/*     */   protected ArrayList getSentenceSet(IndexReader indexReader)
/*     */   {
/* 119 */     int docNum = indexReader.getCollection().getDocNum();
/* 120 */     ArrayList list = new ArrayList(docNum);
/* 121 */     for (int i = 0; i < docNum; i++)
/* 122 */       list.add(indexReader.getDoc(i));
/* 123 */     return list;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.summarize.LexRankSummarizer
 * JD-Core Version:    0.6.2
 */