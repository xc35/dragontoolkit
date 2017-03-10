/*    */ package edu.drexel.cis.dragon.ir.clustering.docdistance;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.matrix.DenseMatrix;
/*    */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*    */ 
/*    */ public class EuclideanDocDistance extends AbstractDocDistance
/*    */ {
/*    */   public EuclideanDocDistance(SparseMatrix doctermMatrix)
/*    */   {
/* 17 */     super(doctermMatrix);
/*    */   }
/*    */ 
/*    */   public EuclideanDocDistance(DenseMatrix doctermMatrix) {
/* 21 */     super(doctermMatrix);
/*    */   }
/*    */ 
/*    */   public double getDistance(IRDoc first, IRDoc second) {
/* 25 */     if (this.sparseMatrix != null) {
/* 26 */       return getSparseDistance(first, second);
/*    */     }
/* 28 */     return getDenseDistance(first, second);
/*    */   }
/*    */ 
/*    */   private double getDenseDistance(IRDoc first, IRDoc second)
/*    */   {
/* 35 */     double[] firstScoreList = this.denseMatrix.getDouble(first.getIndex());
/* 36 */     double[] secondScoreList = this.denseMatrix.getDouble(second.getIndex());
/* 37 */     double sum = 0.0D;
/* 38 */     for (int i = 0; i < firstScoreList.length; i++) {
/* 39 */       if ((this.featureFilter == null) || (this.featureFilter.map(i) >= 0))
/* 40 */         sum += (firstScoreList[i] - secondScoreList[i]) * (firstScoreList[i] - secondScoreList[i]);
/*    */     }
/* 42 */     return Math.sqrt(sum);
/*    */   }
/*    */ 
/*    */   private double getSparseDistance(IRDoc first, IRDoc second)
/*    */   {
/* 50 */     double[] firstScoreList = this.sparseMatrix.getNonZeroDoubleScoresInRow(first.getIndex());
/* 51 */     double[] secondScoreList = this.sparseMatrix.getNonZeroDoubleScoresInRow(second.getIndex());
/* 52 */     int[] firstIndexList = this.sparseMatrix.getNonZeroColumnsInRow(first.getIndex());
/* 53 */     int[] secondIndexList = this.sparseMatrix.getNonZeroColumnsInRow(second.getIndex());
/* 54 */     int firstNum = firstScoreList.length;
/* 55 */     int secondNum = secondScoreList.length;
/*    */ 
/* 57 */     int x = 0;
/* 58 */     int y = 0;
/* 59 */     double sum = 0.0D;
/*    */     do
/*    */     {
/* 62 */       if (firstIndexList[x] < secondIndexList[y]) {
/* 63 */         if ((this.featureFilter == null) || (this.featureFilter.map(firstIndexList[x]) >= 0))
/* 64 */           sum += firstScoreList[x] * firstScoreList[x];
/* 65 */         x++;
/*    */       }
/* 67 */       else if (firstIndexList[x] == secondIndexList[y]) {
/* 68 */         if ((this.featureFilter == null) || (this.featureFilter.map(firstIndexList[x]) >= 0))
/* 69 */           sum += (firstScoreList[x] - secondScoreList[y]) * (firstScoreList[x] - secondScoreList[y]);
/* 70 */         x++;
/* 71 */         y++;
/*    */       }
/*    */       else {
/* 74 */         if ((this.featureFilter == null) || (this.featureFilter.map(secondIndexList[y]) >= 0))
/* 75 */           sum += secondScoreList[y] * secondScoreList[y];
/* 76 */         y++;
/*    */       }
/* 61 */       if (x >= firstNum) break;  } while (y < secondNum);
/*    */ 
/* 79 */     while (x < firstNum) {
/* 80 */       if ((this.featureFilter == null) || (this.featureFilter.map(firstIndexList[x]) >= 0))
/* 81 */         sum += firstScoreList[x] * firstScoreList[x];
/* 82 */       x++;
/*    */     }
/* 84 */     while (y < secondNum) {
/* 85 */       if ((this.featureFilter == null) || (this.featureFilter.map(secondIndexList[y]) >= 0))
/* 86 */         sum += secondScoreList[y] * secondScoreList[y];
/* 87 */       y++;
/*    */     }
/* 89 */     return Math.sqrt(sum);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.docdistance.EuclideanDocDistance
 * JD-Core Version:    0.6.2
 */