/*     */ package edu.drexel.cis.dragon.ir.summarize;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.docdistance.DocDistance;
/*     */ import edu.drexel.cis.dragon.ir.clustering.docdistance.KLDivDocDistance;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.sentence.OnlineSentenceIndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.sentence.OnlineSentenceIndexer;
/*     */ import edu.drexel.cis.dragon.ir.kngbase.DocRepresentation;
/*     */ import edu.drexel.cis.dragon.ir.kngbase.KnowledgeBase;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ 
/*     */ public class SemanticRankSummarizer extends LexRankSummarizer
/*     */ {
/*     */   private OnlineSentenceIndexer phraseIndexer;
/*     */   private KnowledgeBase kngBase;
/*     */   private double transCoefficient;
/*     */   private double bkgCoefficient;
/*     */   private double maxKLDivDistance;
/*     */ 
/*     */   public SemanticRankSummarizer(OnlineSentenceIndexer tokenIndexer, KnowledgeBase kngBase)
/*     */   {
/*  27 */     this(tokenIndexer, null, kngBase);
/*     */   }
/*     */ 
/*     */   public SemanticRankSummarizer(OnlineSentenceIndexer tokenIndexer, OnlineSentenceIndexer phraseIndexer, KnowledgeBase kngBase) {
/*  31 */     super(tokenIndexer);
/*  32 */     this.phraseIndexer = phraseIndexer;
/*  33 */     if (phraseIndexer != null)
/*  34 */       phraseIndexer.setMinSentenceLength(tokenIndexer.getMinSentenceLength());
/*  35 */     this.kngBase = kngBase;
/*  36 */     this.transCoefficient = 0.3D;
/*  37 */     this.bkgCoefficient = 0.4D;
/*  38 */     this.maxKLDivDistance = 10.0D;
/*     */   }
/*     */ 
/*     */   public void setTranslationCoefficient(double coefficient) {
/*  42 */     this.transCoefficient = coefficient;
/*     */   }
/*     */ 
/*     */   public void setBackgroundCoefficient(double coefficient) {
/*  46 */     this.bkgCoefficient = coefficient;
/*     */   }
/*     */ 
/*     */   public void setMaxKLDivDistance(double distance) {
/*  50 */     this.maxKLDivDistance = distance;
/*     */   }
/*     */ 
/*     */   private DoubleSparseMatrix computeTransDocTermMatrix()
/*     */   {
/*     */     DoubleSparseMatrix transMatrix;
/*  59 */     if (this.phraseIndexer != null) {
/*  60 */       this.collectionReader.restart();
/*  61 */       OnlineSentenceIndexReader phraseIndexReader = new OnlineSentenceIndexReader(this.phraseIndexer, this.collectionReader);
/*  62 */       phraseIndexReader.initialize();
/*  63 */       int[] phraseMap = getTermMap(phraseIndexReader, this.kngBase.getRowKeyList());
/*  64 */       int[] termMap = getTermMap(this.indexReader, this.kngBase.getColumnKeyList());
/*  65 */       DocRepresentation docRep = new DocRepresentation(this.indexReader, termMap);
/*  66 */         transMatrix = docRep.genModelMatrix(phraseIndexReader, phraseMap, this.kngBase.getKnowledgeMatrix(), 
/*  67 */         this.transCoefficient, this.bkgCoefficient, true, 0.001D);
/*  68 */       phraseIndexReader.close();
/*     */     }
/*     */     else {
/*  71 */       int[] termMap = getTermMap(this.indexReader, this.kngBase.getColumnKeyList());
/*  72 */       DocRepresentation docRep = new DocRepresentation(this.indexReader, termMap);
/*  73 */       transMatrix = docRep.genModelMatrix(this.indexReader, termMap, this.kngBase.getKnowledgeMatrix(), 
/*  74 */         this.transCoefficient, this.bkgCoefficient, true, 0.001D);
/*     */     }
/*  76 */     return transMatrix;
/*     */   }
/*     */ 
/*     */   private int[] getTermMap(IndexReader reader, SimpleElementList newTermList)
/*     */   {
/*  84 */     int[] termMap = new int[reader.getCollection().getTermNum()];
/*  85 */     int newTermNum = newTermList.size();
/*  86 */     for (int i = 0; i < termMap.length; i++) {
/*  87 */       String curKey = reader.getTermKey(i);
/*  88 */       int newIndex = newTermList.search(curKey);
/*  89 */       if (newIndex >= 0)
/*  90 */         termMap[i] = newIndex;
/*     */       else
/*  92 */         termMap[i] = (newTermNum++);
/*     */     }
/*  94 */     return termMap;
/*     */   }
/*     */ 
/*     */   protected double computeSimilarity(IRDoc firstSent, IRDoc secondSent) {
/*  98 */     if (this.distanceMetric == null) {
/*  99 */       this.distanceMetric = new KLDivDocDistance(computeTransDocTermMatrix(), this.maxKLDivDistance);
/*     */     }
/* 101 */     return Math.max(1.0D - this.distanceMetric.getDistance(firstSent, secondSent), 1.0D - this.distanceMetric.getDistance(secondSent, firstSent));
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.summarize.SemanticRankSummarizer
 * JD-Core Version:    0.6.2
 */