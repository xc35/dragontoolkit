/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ 
/*     */ public abstract class AbstractSparseMatrix extends AbstractMatrix
/*     */   implements SparseMatrix, Serializable
/*     */ {
/*     */   protected boolean isFinalized;
/*     */   protected boolean mergeMode;
/*     */   protected boolean miniMode;
/*     */ 
/*     */   public AbstractSparseMatrix(boolean mergeMode, boolean miniMode, int cellDataLength)
/*     */   {
/*  19 */     this.mergeMode = mergeMode;
/*  20 */     this.miniMode = miniMode;
/*  21 */     this.cellDataLength = cellDataLength;
/*  22 */     this.transposeMatrix = null;
/*  23 */     this.isFinalized = false;
/*     */   }
/*     */ 
/*     */   public boolean isFinalized() {
/*  27 */     return this.isFinalized;
/*     */   }
/*     */ 
/*     */   public boolean finalizeData() {
/*  31 */     return finalizeData(true);
/*     */   }
/*     */ 
/*     */   public Cell getNonZeroCellInColumn(int column, int index) {
/*  35 */     return ((SparseMatrix)transpose()).getNonZeroCellInRow(column, index);
/*     */   }
/*     */ 
/*     */   public int getNonZeroNumInColumn(int column) {
/*  39 */     return ((SparseMatrix)transpose()).getNonZeroNumInRow(column);
/*     */   }
/*     */ 
/*     */   public int getNonZeroRowInColumn(int column, int index) {
/*  43 */     return ((SparseMatrix)transpose()).getNonZeroColumnInRow(column, index);
/*     */   }
/*     */ 
/*     */   public int[] getNonZeroRowsInColumn(int column) {
/*  47 */     return ((SparseMatrix)transpose()).getNonZeroColumnsInRow(column);
/*     */   }
/*     */ 
/*     */   public double getDouble(int row, int column)
/*     */   {
/*  53 */     Cell curCell = getCell(row, column);
/*  54 */     if (curCell != null) {
/*  55 */       return curCell.getDoubleScore();
/*     */     }
/*  57 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public int getInt(int row, int column)
/*     */   {
/*  63 */     Cell curCell = getCell(row, column);
/*  64 */     if (curCell != null) {
/*  65 */       return curCell.getIntScore();
/*     */     }
/*  67 */     return 0;
/*     */   }
/*     */ 
/*     */   public double getNonZeroDoubleScoreInColumn(int column, int index) {
/*  71 */     return ((SparseMatrix)transpose()).getNonZeroDoubleScoreInRow(column, index);
/*     */   }
/*     */ 
/*     */   public double[] getNonZeroDoubleScoresInColumn(int column) {
/*  75 */     return ((SparseMatrix)transpose()).getNonZeroDoubleScoresInRow(column);
/*     */   }
/*     */ 
/*     */   public int getNonZeroIntScoreInColumn(int column, int index) {
/*  79 */     return ((SparseMatrix)transpose()).getNonZeroIntScoreInRow(column, index);
/*     */   }
/*     */ 
/*     */   public int[] getNonZeroIntScoresInColumn(int column) {
/*  83 */     return ((SparseMatrix)transpose()).getNonZeroIntScoresInRow(column);
/*     */   }
/*     */ 
/*     */   public double getNonZeroDoubleScoreInRow(int row, int index) {
/*  87 */     return getNonZeroCellInRow(row, index).getDoubleScore();
/*     */   }
/*     */ 
/*     */   public int getNonZeroIntScoreInRow(int row, int index) {
/*  91 */     return getNonZeroCellInRow(row, index).getIntScore();
/*     */   }
/*     */ 
/*     */   public double[] getNonZeroDoubleScoresInRow(int row)
/*     */   {
/*  98 */     int num = getNonZeroNumInRow(row);
/*  99 */     double[] arrScore = new double[num];
/*     */ 
/* 101 */     for (int count = 0; count < num; count++)
/*     */     {
/* 103 */       arrScore[count] = getNonZeroCellInRow(row, count).getDoubleScore();
/*     */     }
/* 105 */     return arrScore;
/*     */   }
/*     */ 
/*     */   public int[] getNonZeroIntScoresInRow(int row)
/*     */   {
/* 112 */     int num = getNonZeroNumInRow(row);
/* 113 */     int[] arrScore = new int[num];
/*     */ 
/* 115 */     for (int count = 0; count < num; count++)
/*     */     {
/* 117 */       arrScore[count] = getNonZeroCellInRow(row, count).getIntScore();
/*     */     }
/* 119 */     return arrScore;
/*     */   }
/*     */ 
/*     */   public boolean genCooccurrenceMatrix(IntSparseMatrix outputCooccurMatrix) {
/* 123 */     return genCooccurrenceMatrix(this, 1, outputCooccurMatrix);
/*     */   }
/*     */ 
/*     */   public boolean genCooccurrenceMatrix(SparseMatrix matrixY, IntSparseMatrix outputCooccurMatrix) {
/* 127 */     return genCooccurrenceMatrix(matrixY, 1, outputCooccurMatrix);
/*     */   }
/*     */ 
/*     */   public boolean genCooccurrenceMatrix(SparseMatrix matrixY, int minOccurrence, IntSparseMatrix outputCooccurMatrix)
/*     */   {
/* 138 */     SparseMatrix matrixX = this;
/* 139 */     if (matrixX.columns() != matrixY.columns()) return false;
/* 140 */     boolean equal = matrixX.equals(matrixY);
/*     */ 
/* 142 */     for (int i = 0; i < matrixX.rows(); i++) {
/* 143 */       System.out.println(new Date().toString() + " Processing Row " + i);
/* 144 */       int[] arrColumnX = matrixX.getNonZeroColumnsInRow(i);
/* 145 */       int xNum = matrixX.getNonZeroNumInRow(i);
/* 146 */       for (int j = equal ? i + 1 : 0; j < matrixY.rows(); j++) {
/* 147 */         int coOccurCount = 0;
/* 148 */         int x = 0;
/* 149 */         int y = 0;
/* 150 */         int[] arrColumnY = matrixY.getNonZeroColumnsInRow(j);
/* 151 */         int yNum = matrixY.getNonZeroNumInRow(j);
/*     */ 
/* 153 */         while ((x < xNum) && (y < yNum))
/* 154 */           if (arrColumnX[x] < arrColumnY[y]) {
/* 155 */             x++;
/* 156 */           } else if (arrColumnX[x] == arrColumnY[y]) {
/* 157 */             x++;
/* 158 */             y++;
/* 159 */             coOccurCount++;
/*     */           }
/*     */           else {
/* 162 */             y++;
/*     */           }
/* 164 */         if (coOccurCount >= minOccurrence) {
/* 165 */           outputCooccurMatrix.add(i, j, coOccurCount);
/* 166 */           if (equal) {
/* 167 */             outputCooccurMatrix.add(i, i, xNum);
/* 168 */             outputCooccurMatrix.add(j, i, coOccurCount);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 173 */     if (equal)
/* 174 */       outputCooccurMatrix.finalizeData();
/*     */     else
/* 176 */       outputCooccurMatrix.finalizeData(false);
/* 177 */     return true;
/*     */   }
/*     */ 
/*     */   public int getCooccurrenceCount(int rowA, int rowB)
/*     */   {
/* 185 */     int coOccurCount = 0;
/* 186 */     int x = 0;
/* 187 */     int y = 0;
/* 188 */     int xNum = getNonZeroNumInRow(rowA);
/* 189 */     int yNum = getNonZeroNumInRow(rowB);
/*     */ 
/* 191 */     while ((x < xNum) && (y < yNum)) {
/* 192 */       int xCol = getNonZeroColumnInRow(rowA, x);
/* 193 */       int yCol = getNonZeroColumnInRow(rowB, y);
/* 194 */       if (xCol < yCol) {
/* 195 */         x++;
/* 196 */       } else if (xCol == yCol) {
/* 197 */         x++;
/* 198 */         y++;
/* 199 */         coOccurCount++;
/*     */       }
/*     */       else {
/* 202 */         y++;
/*     */       }
/*     */     }
/* 204 */     return coOccurCount;
/*     */   }
/*     */ 
/*     */   public double cosine(int rowA, int rowB)
/*     */   {
/* 211 */     int x = 0;
/* 212 */     int y = 0;
/* 213 */     double xy = 0.0D;
/* 214 */     double x2 = 0.0D;
/* 215 */     double y2 = 0.0D;
/* 216 */     int xNum = getNonZeroNumInRow(rowA);
/* 217 */     int yNum = getNonZeroNumInRow(rowB);
/* 218 */     if ((xNum == 0) || (yNum == 0))
/* 219 */       return 0.0D;
/* 220 */     double[] arrXScore = getNonZeroDoubleScoresInRow(rowA);
/* 221 */     int[] arrXCol = getNonZeroColumnsInRow(rowA);
/* 222 */     double[] arrYScore = getNonZeroDoubleScoresInRow(rowB);
/* 223 */     int[] arrYCol = getNonZeroColumnsInRow(rowB);
/*     */     do
/*     */     {
/* 226 */       if (arrXCol[x] < arrYCol[y]) {
/* 227 */         x2 += arrXScore[x] * arrXScore[x];
/* 228 */         x++;
/* 229 */       } else if (arrXCol[x] == arrYCol[y]) {
/* 230 */         xy += arrXScore[x] * arrYScore[y];
/* 231 */         x2 += arrXScore[x] * arrXScore[x];
/* 232 */         y2 += arrYScore[y] * arrYScore[y];
/* 233 */         x++;
/* 234 */         y++;
/*     */       }
/*     */       else {
/* 237 */         y2 += arrYScore[y] * arrYScore[y];
/* 238 */         y++;
/*     */       }
/* 225 */       if (x >= xNum) break;  } while (y < yNum);
/*     */ 
/* 242 */     while (y < yNum)
/*     */     {
/* 244 */       y2 += arrYScore[y] * arrYScore[y];
/* 245 */       y++;
/*     */     }
/*     */ 
/* 248 */     while (x < xNum)
/*     */     {
/* 250 */       x2 += arrXScore[x] * arrXScore[x];
/* 251 */       x++;
/*     */     }
/* 253 */     return xy / (Math.sqrt(x2) * Math.sqrt(y2));
/*     */   }
/*     */ 
/*     */   public static boolean genTranslationMatrix(IntSparseMatrix inputCooccurMatrix, DoubleSparseMatrix outputTransMatrix)
/*     */   {
/* 261 */     for (int i = 0; i < inputCooccurMatrix.rows(); i++) {
/* 262 */       int[] arrColumn = inputCooccurMatrix.getNonZeroColumnsInRow(i);
/* 263 */       int[] arrScore = inputCooccurMatrix.getNonZeroIntScoresInRow(i);
/* 264 */       double rowLen = arrColumn.length;
/* 265 */       double rowSum = 0.0D;
/* 266 */       for (int j = 0; j < rowLen; j++) rowSum += arrScore[j];
/* 267 */       for (int j = 0; j < rowLen; j++) outputTransMatrix.add(i, arrColumn[j], arrScore[j] / rowSum);
/*     */     }
/* 269 */     outputTransMatrix.finalizeData(false);
/* 270 */     return true;
/*     */   }
/*     */ 
/*     */   public Matrix transpose()
/*     */   {
/* 277 */     if (!isFinalized()) {
/* 278 */       return null;
/*     */     }
/* 280 */     if (this.transposeMatrix == null)
/*     */     {
/* 282 */       SparseMatrix curMatrix = createSparseMatrix();
/* 283 */       for (int i = 0; i < this.rows; i++)
/*     */       {
/* 285 */         int num = getNonZeroNumInRow(i);
/* 286 */         for (int j = 0; j < num; j++)
/* 287 */           curMatrix.add(getNonZeroCellInRow(i, j).transpose());
/*     */       }
/* 289 */       curMatrix.finalizeData();
/* 290 */       curMatrix.setTranspose(this);
/* 291 */       this.transposeMatrix = curMatrix;
/*     */     }
/* 293 */     return this.transposeMatrix;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.AbstractSparseMatrix
 * JD-Core Version:    0.6.2
 */