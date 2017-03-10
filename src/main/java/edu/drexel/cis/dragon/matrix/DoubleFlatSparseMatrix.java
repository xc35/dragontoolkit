/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class DoubleFlatSparseMatrix extends AbstractFlatSparseMatrix
/*     */   implements DoubleSparseMatrix, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */   public DoubleFlatSparseMatrix()
/*     */   {
/*  17 */     super(false, false, DoubleCell.getCellDataLength());
/*     */   }
/*     */ 
/*     */   public DoubleFlatSparseMatrix(boolean mergeMode, boolean miniMode) {
/*  21 */     super(mergeMode, miniMode, DoubleCell.getCellDataLength());
/*     */   }
/*     */ 
/*     */   public DoubleFlatSparseMatrix(String filename) {
/*  25 */     super(false, false, DoubleCell.getCellDataLength());
/*  26 */     readTextMatrixFile(filename);
/*     */   }
/*     */ 
/*     */   public DoubleFlatSparseMatrix(String filename, boolean binaryFile) {
/*  30 */     super(false, false, DoubleCell.getCellDataLength());
/*  31 */     if (binaryFile)
/*  32 */       readBinaryMatrixFile(filename);
/*     */     else
/*  34 */       readTextMatrixFile(filename);
/*     */   }
/*     */ 
/*     */   public SparseMatrix createSparseMatrix() {
/*  38 */     return new DoubleFlatSparseMatrix();
/*     */   }
/*     */ 
/*     */   public Cell createCell(int row, int column, byte[] data)
/*     */   {
/*  44 */     DoubleCell cur = new DoubleCell(row, column);
/*  45 */     cur.fromByteArray(data);
/*  46 */     return cur;
/*     */   }
/*     */ 
/*     */   public Cell createCell(int row, int column, String data)
/*     */   {
/*  52 */     DoubleCell cur = new DoubleCell(row, column);
/*  53 */     cur.fromString(data);
/*  54 */     return cur;
/*     */   }
/*     */ 
/*     */   public boolean add(int row, int column, double value) {
/*  58 */     return add(new DoubleCell(row, column, value));
/*     */   }
/*     */ 
/*     */   public double get(int row, int column) {
/*  62 */     return getDouble(row, column);
/*     */   }
/*     */ 
/*     */   public void set(int row, int column, double score) {
/*  66 */     setDouble(row, column, score);
/*     */   }
/*     */ 
/*     */   public double getQuick(int row, int column) {
/*  70 */     return getDouble(row, column);
/*     */   }
/*     */ 
/*     */   public void setQuick(int row, int column, double score) {
/*  74 */     setDouble(row, column, score);
/*     */   }
/*     */ 
/*     */   public double getRowSum(int row)
/*     */   {
/*  81 */     double sum = 0.0D;
/*  82 */     int num = getNonZeroNumInRow(row);
/*  83 */     for (int count = 0; count < num; count++)
/*     */     {
/*  85 */       sum += getNonZeroDoubleScoreInRow(row, count);
/*     */     }
/*  87 */     return sum;
/*     */   }
/*     */ 
/*     */   public double getColumnSum(int column)
/*     */   {
/*  92 */     return ((DoubleFlatSparseMatrix)transpose()).getRowSum(column);
/*     */   }
/*     */ 
/*     */   public void normalizeColumns()
/*     */   {
/* 102 */     DoubleFlatSparseMatrix matrix = (DoubleFlatSparseMatrix)transpose();
/* 103 */     double[] arrNorm = new double[this.columns];
/* 104 */     for (int i = 0; i < matrix.rows(); i++)
/*     */     {
/* 106 */       arrNorm[i] = 0.0D;
/* 107 */       int num = matrix.getNonZeroNumInRow(i);
/* 108 */       for (int j = 0; j < num; j++)
/*     */       {
/* 110 */         double score = matrix.getNonZeroDoubleScoreInRow(i, j);
/* 111 */         arrNorm[i] += score * score;
/*     */       }
/* 113 */       arrNorm[i] = Math.sqrt(arrNorm[i]);
/*     */ 
/* 115 */       for (int j = 0; j < num; j++)
/*     */       {
/* 117 */         double score = matrix.getNonZeroDoubleScoreInRow(i, j);
/* 118 */         matrix.setNonZeroDoubleScoreInRow(i, j, score);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 123 */     for (int i = 0; i < this.list.size(); i++)
/*     */     {
/* 125 */       DoubleCell cur = (DoubleCell)getNonZeroCell(i);
/* 126 */       cur.setDoubleScore(cur.getDoubleScore() / arrNorm[cur.getColumn()]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void normalizeRows() {
/* 131 */     ((DoubleFlatSparseMatrix)transpose()).normalizeColumns();
/*     */   }
/*     */ 
/*     */   public DoubleDenseMatrix product(DoubleFlatSparseMatrix matrixY)
/*     */   {
/* 141 */     DoubleFlatSparseMatrix matrixX = this;
/* 142 */     if (matrixX.columns() != matrixY.rows()) return null;
/*     */ 
/* 144 */     DoubleDenseMatrix output = new DoubleFlatDenseMatrix(this.rows, matrixY.columns());
/* 145 */     for (int i = 0; i < matrixX.rows(); i++) {
/* 146 */       for (int j = 0; j < matrixY.columns(); j++) {
/* 147 */         double score = 0.0D;
/* 148 */         int x = 0;
/* 149 */         int y = 0;
/* 150 */         int xNum = matrixX.getNonZeroNumInRow(i);
/* 151 */         int yNum = matrixY.getNonZeroNumInColumn(j);
/*     */ 
/* 153 */         while ((x < xNum) && (y < yNum)) {
/* 154 */           int xCol = matrixX.getNonZeroColumnInRow(i, x);
/* 155 */           int yCol = matrixY.getNonZeroRowInColumn(j, y);
/* 156 */           if (xCol < yCol) {
/* 157 */             x++;
/* 158 */           } else if (xCol == yCol) {
/* 159 */             x++;
/* 160 */             y++;
/* 161 */             score += matrixX.getNonZeroDoubleScoreInRow(i, x) * matrixY.getNonZeroDoubleScoreInColumn(j, y);
/*     */           }
/*     */           else {
/* 164 */             y++;
/*     */           }
/*     */         }
/* 166 */         output.setDouble(i, j, score);
/*     */       }
/*     */     }
/* 169 */     return output;
/*     */   }
/*     */ 
/*     */   public DoubleDenseMatrix product(DoubleDenseMatrix b)
/*     */   {
/* 179 */     DoubleDenseMatrix output = new DoubleFlatDenseMatrix(this.rows, b.columns());
/* 180 */     for (int row = 0; row < rows(); row++) {
/* 181 */       int[] arrColumn = getNonZeroColumnsInRow(row);
/* 182 */       double[] arrScore = getNonZeroDoubleScoresInRow(row);
/* 183 */       for (int col = 0; col < b.columns(); col++) {
/* 184 */         double score = 0.0D;
/* 185 */         for (int i = 0; i < arrColumn.length; i++)
/* 186 */           score += arrScore[i] * b.getDouble(arrColumn[i], col);
/* 187 */         output.setDouble(row, col, score);
/*     */       }
/*     */     }
/* 190 */     return output;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.DoubleFlatSparseMatrix
 * JD-Core Version:    0.6.2
 */