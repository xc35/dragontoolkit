/*     */ package edu.drexel.cis.dragon.ir.search.smooth;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRRelation;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ 
/*     */ public class DocFirstTransSmoother extends AbstractSmoother
/*     */ {
/*     */   private IndexReader srcIndexReader;
/*     */   private IndexReader destIndexReader;
/*     */   private DoubleSparseMatrix transMatrix;
/*     */   private Smoother basicSmoother;
/*     */   private double transCoefficient;
/*     */   private boolean relationTrans;
/*     */   private int curQueryTermIndex;
/*     */   private int docSignatureCount;
/*     */   private int[] arrIndex;
/*     */   private int[] arrFreq;
/*     */ 
/*     */   public DocFirstTransSmoother(IndexReader indexReader, DoubleSparseMatrix transMatrix, boolean relationTrans, double transCoefficient, Smoother basicSmoother)
/*     */   {
/*  27 */     this.srcIndexReader = indexReader;
/*  28 */     this.destIndexReader = indexReader;
/*  29 */     this.transMatrix = transMatrix;
/*  30 */     this.basicSmoother = basicSmoother;
/*  31 */     basicSmoother.setLogLikelihoodOption(false);
/*  32 */     this.relationTrans = relationTrans;
/*  33 */     this.useLog = true;
/*  34 */     this.docFirstOptimal = true;
/*  35 */     this.querytermFirstOptimal = false;
/*  36 */     this.transCoefficient = transCoefficient;
/*     */   }
/*     */ 
/*     */   public DocFirstTransSmoother(IndexReader srcIndexReader, IndexReader destIndexReader, DoubleSparseMatrix transMatrix, double transCoefficient, Smoother basicSmoother) {
/*  40 */     this.srcIndexReader = srcIndexReader;
/*  41 */     this.destIndexReader = destIndexReader;
/*  42 */     this.transMatrix = transMatrix;
/*  43 */     this.basicSmoother = basicSmoother;
/*  44 */     basicSmoother.setLogLikelihoodOption(false);
/*  45 */     this.relationTrans = false;
/*  46 */     this.useLog = true;
/*  47 */     this.docFirstOptimal = true;
/*  48 */     this.querytermFirstOptimal = false;
/*  49 */     this.transCoefficient = transCoefficient;
/*     */   }
/*     */ 
/*     */   public boolean setParameters(double[] params) {
/*  53 */     if ((params != null) && (params.length >= 1))
/*     */     {
/*  55 */       this.transCoefficient = params[0];
/*  56 */       return true;
/*     */     }
/*     */ 
/*  59 */     return false;
/*     */   }
/*     */ 
/*     */   public void setTranslationMatrix(DoubleSparseMatrix transMatrix) {
/*  63 */     this.transMatrix = transMatrix;
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix getTranslationMatrix() {
/*  67 */     return this.transMatrix;
/*     */   }
/*     */ 
/*     */   public Smoother getBasicSmoother() {
/*  71 */     return this.basicSmoother;
/*     */   }
/*     */ 
/*     */   public double getTranslationCoefficient() {
/*  75 */     return this.transCoefficient;
/*     */   }
/*     */ 
/*     */   public void setQueryTerm(SimpleTermPredicate queryTerm) {
/*  79 */     this.queryWeight = queryTerm.getWeight();
/*  80 */     this.curQueryTermIndex = queryTerm.getIndex();
/*  81 */     this.basicSmoother.setQueryTerm(queryTerm);
/*     */   }
/*     */ 
/*     */   public void setDoc(IRDoc curDoc)
/*     */   {
/*  87 */     this.basicSmoother.setDoc(curDoc);
/*     */     IRDoc srcDoc;
/*  88 */     if (this.srcIndexReader.equals(this.destIndexReader))
/*  89 */       srcDoc = curDoc;
/*     */     else
/*  91 */       srcDoc = this.srcIndexReader.getDoc(curDoc.getIndex());
/*  92 */     if (this.relationTrans) {
/*  93 */       this.arrIndex = this.srcIndexReader.getRelationIndexList(curDoc.getIndex());
/*  94 */       this.arrFreq = this.srcIndexReader.getRelationFrequencyList(curDoc.getIndex());
/*  95 */       this.docSignatureCount = srcDoc.getRelationCount();
/*  96 */       if (this.docSignatureCount <= 0) this.docSignatureCount = 1; 
/*     */     }
/*     */     else
/*     */     {
/*  99 */       this.arrIndex = this.srcIndexReader.getTermIndexList(curDoc.getIndex());
/* 100 */       this.arrFreq = this.srcIndexReader.getTermFrequencyList(curDoc.getIndex());
/* 101 */       this.docSignatureCount = srcDoc.getTermCount();
/* 102 */       if (this.docSignatureCount <= 0) this.docSignatureCount = 1; 
/*     */     }
/*     */   }
/*     */ 
/*     */   public double getTranslationProb(int termIndex)
/*     */   {
/* 107 */     if (this.transMatrix == null) {
/* 108 */       return getSelfTransProb(termIndex);
/*     */     }
/* 110 */     return getFullTransProb(termIndex);
/*     */   }
/*     */ 
/*     */   protected double computeSmoothedProb(int termFrequency)
/*     */   {
/*     */     double prob;
/* 116 */     if (this.transMatrix == null)
/* 117 */       prob = getSelfTransProb(this.curQueryTermIndex);
/*     */     else
/* 119 */       prob = getFullTransProb(this.curQueryTermIndex);
/* 120 */      prob = this.transCoefficient * prob + (1.0D - this.transCoefficient) * this.basicSmoother.getSmoothedProb(termFrequency) / this.queryWeight;
/* 121 */     return this.queryWeight * getProb(prob);
/*     */   }
/*     */ 
/*     */   private double getFullTransProb(int queryTermIndex)
/*     */   {
/* 128 */     double prob = 0.0D;
/* 129 */     for (int i = 0; (this.arrIndex != null) && (i < this.arrIndex.length); i++) {
/* 130 */       prob += this.transMatrix.getDouble(this.arrIndex[i], queryTermIndex) * this.arrFreq[i] / this.docSignatureCount;
/*     */     }
/* 132 */     return prob;
/*     */   }
/*     */ 
/*     */   private double getSelfTransProb(int queryTermIndex)
/*     */   {
/* 139 */     int count = 0;
/* 140 */     for (int i = 0; (this.arrIndex != null) && (i < this.arrIndex.length); i++) {
/* 141 */       IRRelation curRelation = this.srcIndexReader.getIRRelation(this.arrIndex[i]);
/* 142 */       if ((curRelation.getFirstTerm() == queryTermIndex) || (curRelation.getSecondTerm() == queryTermIndex))
/* 143 */         count += this.arrFreq[i];
/*     */     }
/* 145 */     return 0.5D * count / this.docSignatureCount;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.smooth.DocFirstTransSmoother
 * JD-Core Version:    0.6.2
 */