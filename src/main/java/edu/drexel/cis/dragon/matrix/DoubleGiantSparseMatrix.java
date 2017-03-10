/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ 
/*     */ public class DoubleGiantSparseMatrix extends AbstractGiantSparseMatrix
/*     */   implements DoubleSparseMatrix
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */   public DoubleGiantSparseMatrix(String indexFile, String matrixFile)
/*     */   {
/*  17 */     super(indexFile, matrixFile, DoubleCell.getCellDataLength());
/*     */   }
/*     */ 
/*     */   public DoubleGiantSparseMatrix(String indexFile, String matrixFile, boolean mergeMode, boolean miniMode) {
/*  21 */     super(indexFile, matrixFile, DoubleCell.getCellDataLength(), mergeMode, miniMode);
/*     */   }
/*     */ 
/*     */   public SparseMatrix createSparseMatrix()
/*     */   {
/*  27 */     String indexFile = FileUtil.getNewTempFilename("newmatrix", "index");
/*  28 */     String matrixFile = FileUtil.getNewTempFilename("newmatrix", "matrix");
/*  29 */     return new DoubleGiantSparseMatrix(indexFile, matrixFile, false, false);
/*     */   }
/*     */ 
/*     */   protected AbstractFlatSparseMatrix createFlatSparseMatrix(boolean mergeMode, boolean miniMode) {
/*  33 */     return new DoubleFlatSparseMatrix(mergeMode, miniMode);
/*     */   }
/*     */ 
/*     */   protected Row createRow(int row, int columns, byte[] data)
/*     */   {
/*  39 */     DoubleRow cur = new DoubleRow();
/*  40 */     cur.load(row, columns, data);
/*  41 */     return cur;
/*     */   }
/*     */ 
/*     */   public Cell createCell(int row, int column, byte[] data)
/*     */   {
/*  47 */     DoubleCell cur = new DoubleCell(row, column);
/*  48 */     cur.fromByteArray(data);
/*  49 */     return cur;
/*     */   }
/*     */ 
/*     */   public Cell createCell(int row, int column, String data)
/*     */   {
/*  55 */     DoubleCell cur = new DoubleCell(row, column);
/*  56 */     cur.fromString(data);
/*  57 */     return cur;
/*     */   }
/*     */ 
/*     */   public boolean add(int row, int column, double score) {
/*  61 */     return add(new DoubleCell(row, column, score));
/*     */   }
/*     */ 
/*     */   public double getDouble(int row, int column) {
/*  65 */     if (row >= this.rows) return 0.0D;
/*  66 */     return ((DoubleRow)getRow(row)).getDouble(column);
/*     */   }
/*     */ 
/*     */   public double getNonZeroDoubleScoreInRow(int row, int index) {
/*  70 */     if (row >= this.rows) return 0.0D;
/*  71 */     return ((DoubleRow)getRow(row)).getNonZeroDoubleScore(index);
/*     */   }
/*     */ 
/*     */   public double[] getNonZeroDoubleScoresInRow(int row)
/*     */   {
/*  77 */     if (row >= this.rows)
/*  78 */       return null;
/*  79 */     double[] oldArray = ((DoubleRow)getRow(row)).getNonZeroDoubleScores();
/*  80 */     double[] newArray = new double[oldArray.length];
/*  81 */     System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
/*  82 */     return newArray;
/*     */   }
/*     */ 
/*     */   public double getRowSum(int row)
/*     */   {
/*  89 */     double sum = 0.0D;
/*  90 */     double[] scores = getNonZeroDoubleScoresInRow(row);
/*  91 */     for (int count = 0; count < scores.length; count++)
/*     */     {
/*  93 */       sum += scores[count];
/*     */     }
/*  95 */     return sum;
/*     */   }
/*     */ 
/*     */   public double getColumnSum(int column)
/*     */   {
/* 100 */     return ((DoubleSuperSparseMatrix)transpose()).getRowSum(column);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.DoubleGiantSparseMatrix
 * JD-Core Version:    0.6.2
 */