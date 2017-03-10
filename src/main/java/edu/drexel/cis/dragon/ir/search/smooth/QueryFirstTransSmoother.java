/*     */ package edu.drexel.cis.dragon.ir.search.smooth;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*     */ import edu.drexel.cis.dragon.matrix.Cell;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ 
/*     */ public class QueryFirstTransSmoother extends AbstractSmoother
/*     */ {
/*     */   private IndexReader srcIndexReader;
/*     */   private IndexReader destIndexReader;
/*     */   private DoubleSparseMatrix transMatrix;
/*     */   private Smoother basicSmoother;
/*     */   private boolean relationTrans;
/*     */   private double transCoefficient;
/*     */   private int docNum;
/*     */   private int[] arrDocCount;
/*     */   private int curDocIndex;
/*     */   private double[] arrTrans;
/*     */ 
/*     */   public QueryFirstTransSmoother(IndexReader reader, DoubleSparseMatrix transposedTransMatrix, boolean relationTrans, double transCoefficient, Smoother basicSmoother)
/*     */   {
/*  27 */     this.srcIndexReader = reader;
/*  28 */     this.destIndexReader = reader;
/*  29 */     this.transMatrix = transposedTransMatrix;
/*  30 */     this.relationTrans = relationTrans;
/*  31 */     this.useLog = true;
/*  32 */     this.docFirstOptimal = false;
/*  33 */     this.querytermFirstOptimal = true;
/*  34 */     this.basicSmoother = basicSmoother;
/*  35 */     basicSmoother.setLogLikelihoodOption(false);
/*  36 */     this.transCoefficient = transCoefficient;
/*     */ 
/*  38 */     this.docNum = this.destIndexReader.getCollection().getDocNum();
/*  39 */     this.arrTrans = new double[this.docNum];
/*  40 */     this.arrDocCount = new int[this.docNum];
/*  41 */     for (int i = 0; i < this.docNum; i++) {
/*  42 */       if (relationTrans)
/*  43 */         this.arrDocCount[i] = this.srcIndexReader.getDoc(i).getRelationCount();
/*     */       else
/*  45 */         this.arrDocCount[i] = this.srcIndexReader.getDoc(i).getTermCount();
/*  46 */       if (this.arrDocCount[i] <= 0) this.arrDocCount[i] = 1; 
/*     */     }
/*     */   }
/*     */ 
/*     */   public QueryFirstTransSmoother(IndexReader srcIndexReader, IndexReader destIndexReader, DoubleSparseMatrix transposedTransMatrix, double transCoefficient, Smoother basicSmoother)
/*     */   {
/*  51 */     if (srcIndexReader.getCollection().getDocNum() != destIndexReader.getCollection().getDocNum()) {
/*  52 */       return;
/*     */     }
/*  54 */     this.srcIndexReader = srcIndexReader;
/*  55 */     this.destIndexReader = destIndexReader;
/*  56 */     this.transMatrix = transposedTransMatrix;
/*  57 */     this.relationTrans = false;
/*  58 */     this.useLog = true;
/*  59 */     this.docFirstOptimal = false;
/*  60 */     this.querytermFirstOptimal = true;
/*  61 */     this.basicSmoother = basicSmoother;
/*  62 */     basicSmoother.setLogLikelihoodOption(false);
/*  63 */     this.transCoefficient = transCoefficient;
/*     */ 
/*  65 */     this.docNum = destIndexReader.getCollection().getDocNum();
/*  66 */     this.arrTrans = new double[this.docNum];
/*  67 */     this.arrDocCount = new int[this.docNum];
/*  68 */     for (int i = 0; i < this.docNum; i++) {
/*  69 */       if (this.relationTrans)
/*  70 */         this.arrDocCount[i] = srcIndexReader.getDoc(i).getRelationCount();
/*     */       else
/*  72 */         this.arrDocCount[i] = srcIndexReader.getDoc(i).getTermCount();
/*  73 */       if (this.arrDocCount[i] <= 0) this.arrDocCount[i] = 1; 
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setTranslationMatrix(DoubleSparseMatrix transposedTransMatrix)
/*     */   {
/*  78 */     this.transMatrix = transposedTransMatrix;
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix getTranslationMatrix() {
/*  82 */     return this.transMatrix;
/*     */   }
/*     */ 
/*     */   public boolean setParameters(double[] params) {
/*  86 */     if ((params != null) && (params.length >= 1))
/*     */     {
/*  88 */       this.transCoefficient = params[0];
/*  89 */       return true;
/*     */     }
/*     */ 
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */   public Smoother getBasicSmoother() {
/*  96 */     return this.basicSmoother;
/*     */   }
/*     */ 
/*     */   public double getTranslationCoefficient() {
/* 100 */     return this.transCoefficient;
/*     */   }
/*     */ 
/*     */   public void setQueryTerm(SimpleTermPredicate queryTerm)
/*     */   {
/* 109 */     this.queryWeight = queryTerm.getWeight();
/* 110 */     this.basicSmoother.setQueryTerm(queryTerm);
/* 111 */     for (int j = 0; j < this.docNum; j++) this.arrTrans[j] = 0.0D;
/*     */ 
/* 113 */     int num = this.transMatrix.getNonZeroNumInRow(queryTerm.getIndex());
/* 114 */     for (int j = 0; j < num; j++) {
/* 115 */       Cell transCell = this.transMatrix.getNonZeroCellInRow(queryTerm.getIndex(), j);
/*     */       int[] arrFreq;
/*     */       int[] arrIndex;
/* 116 */       if (this.relationTrans) {
/* 117 */         arrIndex = this.srcIndexReader.getRelationDocIndexList(transCell.getColumn());
/* 118 */         arrFreq = this.srcIndexReader.getRelationDocFrequencyList(transCell.getColumn());
/*     */       }
/*     */       else {
/* 121 */         arrIndex = this.srcIndexReader.getTermDocIndexList(transCell.getColumn());
/* 122 */         arrFreq = this.srcIndexReader.getTermDocFrequencyList(transCell.getColumn());
/*     */       }
/* 124 */       for (int k = 0; k < arrIndex.length; k++)
/* 125 */         this.arrTrans[arrIndex[k]] += transCell.getDoubleScore() * arrFreq[k] / this.arrDocCount[arrIndex[k]];
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDoc(IRDoc doc)
/*     */   {
/* 131 */     this.curDocIndex = doc.getIndex();
/* 132 */     this.basicSmoother.setDoc(doc);
/*     */   }
/*     */ 
/*     */   public double getTranslationProb(int docIndex) {
/* 136 */     return this.arrTrans[docIndex];
/*     */   }
/*     */ 
/*     */   protected double computeSmoothedProb(int termFrequency) {
/* 140 */     return this.queryWeight * getProb((1.0D - this.transCoefficient) * this.basicSmoother.getSmoothedProb(termFrequency) / this.queryWeight + this.transCoefficient * this.arrTrans[this.curDocIndex]);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.smooth.QueryFirstTransSmoother
 * JD-Core Version:    0.6.2
 */