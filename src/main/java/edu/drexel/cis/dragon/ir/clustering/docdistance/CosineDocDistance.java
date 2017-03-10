/*     */ package edu.drexel.cis.dragon.ir.clustering.docdistance;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.matrix.DenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ 
/*     */ public class CosineDocDistance extends AbstractDocDistance
/*     */ {
/*     */   public CosineDocDistance(SparseMatrix doctermMatrix)
/*     */   {
/*  18 */     super(doctermMatrix);
/*     */   }
/*     */ 
/*     */   public CosineDocDistance(DenseMatrix doctermMatrix) {
/*  22 */     super(doctermMatrix);
/*     */   }
/*     */ 
/*     */   public double getDistance(IRDoc first, IRDoc second)
/*     */   {
/*  29 */     if (this.sparseMatrix != null) {
/*  30 */       double[] firstScoreList = this.sparseMatrix.getNonZeroDoubleScoresInRow(first.getIndex());
/*  31 */       double[] secondScoreList = this.sparseMatrix.getNonZeroDoubleScoresInRow(second.getIndex());
/*  32 */       int[] firstIndexList = this.sparseMatrix.getNonZeroColumnsInRow(first.getIndex());
/*  33 */       int[] secondIndexList = this.sparseMatrix.getNonZeroColumnsInRow(second.getIndex());
/*  34 */       return 1.0D - cosine(firstIndexList, firstScoreList, secondIndexList, secondScoreList);
/*     */     }
/*     */ 
/*  37 */     double[] firstScoreList = this.denseMatrix.getDouble(first.getIndex());
/*  38 */     double[] secondScoreList = this.denseMatrix.getDouble(second.getIndex());
/*  39 */     return 1.0D - cosine(firstScoreList, secondScoreList);
/*     */   }
/*     */ 
/*     */   private double cosine(double[] arrXScore, double[] arrYScore)
/*     */   {
/*  47 */     double xy = 0.0D;
/*  48 */     double x2 = 0.0D;
/*  49 */     double y2 = 0.0D;
/*     */ 
/*  51 */     for (int i = 0; i < arrXScore.length; i++) {
/*  52 */       if ((this.featureFilter == null) || (this.featureFilter.map(i) >= 0)) {
/*  53 */         xy += arrXScore[i] * arrYScore[i];
/*  54 */         x2 += arrXScore[i] * arrXScore[i];
/*  55 */         y2 += arrYScore[i] * arrYScore[i];
/*     */       }
/*     */     }
/*     */ 
/*  59 */     return xy / (Math.sqrt(x2) * Math.sqrt(y2));
/*     */   }
/*     */ 
/*     */   private double cosine(int[] arrXCol, double[] arrXScore, int[] arrYCol, double[] arrYScore)
/*     */   {
/*  66 */     if ((arrXCol == null) || (arrYCol == null))
/*  67 */       return 0.0D;
/*  68 */     int xNum = arrXCol.length;
/*  69 */     int yNum = arrYCol.length;
/*  70 */     if ((xNum == 0) || (yNum == 0))
/*  71 */       return 0.0D;
/*  72 */     int x = 0;
/*  73 */     int y = 0;
/*  74 */     double xy = 0.0D;
/*  75 */     double x2 = 0.0D;
/*  76 */     double y2 = 0.0D;
/*     */     do
/*     */     {
/*  79 */       if (arrXCol[x] < arrYCol[y]) {
/*  80 */         if ((this.featureFilter == null) || (this.featureFilter.map(arrXCol[x]) >= 0))
/*  81 */           x2 += arrXScore[x] * arrXScore[x];
/*  82 */         x++;
/*     */       }
/*  84 */       else if (arrXCol[x] == arrYCol[y]) {
/*  85 */         if ((this.featureFilter == null) || (this.featureFilter.map(arrXCol[x]) >= 0)) {
/*  86 */           xy += arrXScore[x] * arrYScore[y];
/*  87 */           x2 += arrXScore[x] * arrXScore[x];
/*  88 */           y2 += arrYScore[y] * arrYScore[y];
/*     */         }
/*  90 */         x++;
/*  91 */         y++;
/*     */       }
/*     */       else
/*     */       {
/*  95 */         if ((this.featureFilter == null) || (this.featureFilter.map(arrYCol[y]) >= 0))
/*  96 */           y2 += arrYScore[y] * arrYScore[y];
/*  97 */         y++;
/*     */       }
/*  78 */       if (x >= xNum) break;  } while (y < yNum);
/*     */ 
/* 100 */     while (y < yNum)
/*     */     {
/* 102 */       if ((this.featureFilter == null) || (this.featureFilter.map(arrYCol[y]) >= 0))
/* 103 */         y2 += arrYScore[y] * arrYScore[y];
/* 104 */       y++;
/*     */     }
/* 106 */     while (x < xNum)
/*     */     {
/* 108 */       if ((this.featureFilter == null) || (this.featureFilter.map(arrXCol[x]) >= 0))
/* 109 */         x2 += arrXScore[x] * arrXScore[x];
/* 110 */       x++;
/*     */     }
/* 112 */     return xy / (Math.sqrt(x2) * Math.sqrt(y2));
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.docdistance.CosineDocDistance
 * JD-Core Version:    0.6.2
 */