/*     */ package edu.drexel.cis.dragon.ir.clustering.docdistance;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ 
/*     */ public class KLDivDocDistance extends AbstractDocDistance
/*     */ {
/*     */   private double bkgCoefficient;
/*     */   private double normThreshold;
/*     */   private double[] arrBkgModel;
/*     */   private boolean norm;
/*     */ 
/*     */   public KLDivDocDistance(DoubleSparseMatrix docModelMatrix)
/*     */   {
/*  21 */     this(docModelMatrix, 0.0D);
/*     */   }
/*     */ 
/*     */   public KLDivDocDistance(DoubleSparseMatrix doctermMatrix, double normThreshold) {
/*  25 */     super(doctermMatrix);
/*     */ 
/*  28 */     this.bkgCoefficient = 0.1D;
/*  29 */     int termNum = this.sparseMatrix.columns();
/*  30 */     this.arrBkgModel = new double[termNum];
/*  31 */     for (int i = 0; i < termNum; i++)
/*  32 */       this.arrBkgModel[i] = (this.bkgCoefficient / termNum);
/*  33 */     if (normThreshold > 0.0D) {
/*  34 */       this.norm = true;
/*  35 */       this.normThreshold = normThreshold;
/*     */     }
/*     */     else {
/*  38 */       this.norm = false;
/*  39 */       this.normThreshold = 0.0D;
/*     */     }
/*     */   }
/*     */ 
/*     */   public KLDivDocDistance(IndexReader indexReader, DoubleSparseMatrix docModelMatrix) {
/*  44 */     this(indexReader, docModelMatrix, 0.0D);
/*     */   }
/*     */ 
/*     */   public KLDivDocDistance(IndexReader indexReader, DoubleSparseMatrix doctermMatrix, double normThreshold)
/*     */   {
/*  49 */     super(doctermMatrix);
/*     */ 
/*  54 */     this.bkgCoefficient = 0.1D;
/*  55 */     int termNum = indexReader.getCollection().getTermNum();
/*  56 */     this.arrBkgModel = new double[termNum];
/*  57 */     double totalTermCount = indexReader.getCollection().getTermCount();
/*  58 */     for (int i = 0; i < termNum; i++)
/*  59 */       this.arrBkgModel[i] = (indexReader.getIRTerm(i).getFrequency() / totalTermCount * this.bkgCoefficient);
/*  60 */     if (normThreshold > 0.0D) {
/*  61 */       this.norm = true;
/*  62 */       this.normThreshold = normThreshold;
/*     */     }
/*     */     else {
/*  65 */       this.norm = false;
/*  66 */       this.normThreshold = 0.0D;
/*     */     }
/*     */   }
/*     */ 
/*     */   public double getDistance(IRDoc first, IRDoc second)
/*     */   {
/*  76 */     int[] arrFirstIndex = this.sparseMatrix.getNonZeroColumnsInRow(first.getIndex());
/*  77 */     double[] arrFirstScore = this.sparseMatrix.getNonZeroDoubleScoresInRow(first.getIndex());
/*  78 */     int firstCount = this.sparseMatrix.getNonZeroNumInRow(first.getIndex());
/*  79 */     int[] arrSecondIndex = this.sparseMatrix.getNonZeroColumnsInRow(second.getIndex());
/*  80 */     double[] arrSecondScore = this.sparseMatrix.getNonZeroDoubleScoresInRow(second.getIndex());
/*  81 */     int secondCount = this.sparseMatrix.getNonZeroNumInRow(second.getIndex());
/*  82 */     double distance = 0.0D;
/*  83 */     int i = 0;
/*  84 */     int j = 0;
/*     */ 
/*  86 */     while (i < firstCount) {
/*  87 */       while ((j < secondCount) && (arrSecondIndex[j] < arrFirstIndex[i])) j++;
/*     */       double secondProb;
/*  88 */       if ((j >= secondCount) || (arrSecondIndex[j] != arrFirstIndex[i]))
/*  89 */         secondProb = this.arrBkgModel[arrFirstIndex[i]];
/*     */       else
/*  91 */         secondProb = (1.0D - this.bkgCoefficient) * arrSecondScore[j] + this.arrBkgModel[arrFirstIndex[i]];
/*  92 */       double firstProb = (1.0D - this.bkgCoefficient) * arrFirstScore[i] + this.arrBkgModel[arrFirstIndex[i]];
/*  93 */       distance += firstProb * Math.log(firstProb / secondProb);
/*  94 */       i++;
/*     */     }
/*     */ 
/*  97 */     if (this.norm) {
/*  98 */       if (distance >= this.normThreshold) {
/*  99 */         return 1.0D;
/*     */       }
/* 101 */       return distance / this.normThreshold;
/*     */     }
/*     */ 
/* 104 */     return distance;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.docdistance.KLDivDocDistance
 * JD-Core Version:    0.6.2
 */