/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ 
/*     */ public class DoubleSuperSparseMatrix extends AbstractSuperSparseMatrix
/*     */   implements DoubleSparseMatrix
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */   public DoubleSuperSparseMatrix(String matrixFile)
/*     */   {
/*  17 */     super(null, matrixFile, DoubleCell.getCellDataLength());
/*     */   }
/*     */ 
/*     */   public DoubleSuperSparseMatrix(String indexFile, String matrixFile) {
/*  21 */     super(indexFile, matrixFile, DoubleCell.getCellDataLength());
/*     */   }
/*     */ 
/*     */   public DoubleSuperSparseMatrix(String matrixFile, boolean mergeMode, boolean miniMode) {
/*  25 */     super(null, matrixFile, DoubleCell.getCellDataLength(), mergeMode, miniMode);
/*     */   }
/*     */ 
/*     */   public DoubleSuperSparseMatrix(String indexFile, String matrixFile, boolean mergeMode, boolean miniMode) {
/*  29 */     super(indexFile, matrixFile, DoubleCell.getCellDataLength(), mergeMode, miniMode);
/*     */   }
/*     */ 
/*     */   public SparseMatrix createSparseMatrix()
/*     */   {
/*  34 */     String indexFile = FileUtil.getNewTempFilename("newmatrix", "index");
/*  35 */     String matrixFile = FileUtil.getNewTempFilename("newmatrix", "matrix");
/*  36 */     return new DoubleSuperSparseMatrix(indexFile, matrixFile, false, false);
/*     */   }
/*     */ 
/*     */   protected AbstractFlatSparseMatrix createFlatSparseMatrix(boolean mergeMode, boolean miniMode) {
/*  40 */     return new DoubleFlatSparseMatrix(mergeMode, miniMode);
/*     */   }
/*     */ 
/*     */   protected Row createRow(int row, int columns, byte[] data)
/*     */   {
/*  46 */     DoubleRow cur = new DoubleRow();
/*  47 */     cur.load(row, columns, data);
/*  48 */     return cur;
/*     */   }
/*     */ 
/*     */   public Cell createCell(int row, int column, byte[] data)
/*     */   {
/*  54 */     DoubleCell cur = new DoubleCell(row, column);
/*  55 */     cur.fromByteArray(data);
/*  56 */     return cur;
/*     */   }
/*     */ 
/*     */   public Cell createCell(int row, int column, String data)
/*     */   {
/*  62 */     DoubleCell cur = new DoubleCell(row, column);
/*  63 */     cur.fromString(data);
/*  64 */     return cur;
/*     */   }
/*     */ 
/*     */   public boolean add(int row, int column, double score) {
/*  68 */     return add(new DoubleCell(row, column, score));
/*     */   }
/*     */ 
/*     */   public double getDouble(int row, int column) {
/*  72 */     if (row >= this.rows) return 0.0D;
/*  73 */     return ((DoubleRow)getRow(row)).getDouble(column);
/*     */   }
/*     */ 
/*     */   public double getNonZeroDoubleScoreInRow(int row, int index) {
/*  77 */     if (row >= this.rows) return 0.0D;
/*  78 */     return ((DoubleRow)getRow(row)).getNonZeroDoubleScore(index);
/*     */   }
/*     */ 
/*     */   public double[] getNonZeroDoubleScoresInRow(int row)
/*     */   {
/*  84 */     if (row >= this.rows)
/*  85 */       return null;
/*  86 */     double[] oldArray = ((DoubleRow)getRow(row)).getNonZeroDoubleScores();
/*  87 */     double[] newArray = new double[oldArray.length];
/*  88 */     System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
/*  89 */     return newArray;
/*     */   }
/*     */ 
/*     */   public double getRowSum(int row)
/*     */   {
/*  96 */     double sum = 0.0D;
/*  97 */     double[] scores = getNonZeroDoubleScoresInRow(row);
/*  98 */     for (int count = 0; count < scores.length; count++)
/*     */     {
/* 100 */       sum += scores[count];
/*     */     }
/* 102 */     return sum;
/*     */   }
/*     */ 
/*     */   public double getColumnSum(int column)
/*     */   {
/* 107 */     return ((DoubleSuperSparseMatrix)transpose()).getRowSum(column);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.DoubleSuperSparseMatrix
 * JD-Core Version:    0.6.2
 */