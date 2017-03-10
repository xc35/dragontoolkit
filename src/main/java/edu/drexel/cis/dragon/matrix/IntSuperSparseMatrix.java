/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ 
/*     */ public class IntSuperSparseMatrix extends AbstractSuperSparseMatrix
/*     */   implements IntSparseMatrix
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */   public IntSuperSparseMatrix(String matrixFile)
/*     */   {
/*  17 */     super(null, matrixFile, IntCell.getCellDataLength());
/*     */   }
/*     */ 
/*     */   public IntSuperSparseMatrix(String indexFile, String matrixFile) {
/*  21 */     super(indexFile, matrixFile, IntCell.getCellDataLength());
/*     */   }
/*     */ 
/*     */   public IntSuperSparseMatrix(String matrixFile, boolean mergeMode, boolean miniMode) {
/*  25 */     super(null, matrixFile, IntCell.getCellDataLength(), mergeMode, miniMode);
/*     */   }
/*     */ 
/*     */   public IntSuperSparseMatrix(String indexFile, String matrixFile, boolean mergeMode, boolean miniMode) {
/*  29 */     super(indexFile, matrixFile, IntCell.getCellDataLength(), mergeMode, miniMode);
/*     */   }
/*     */ 
/*     */   public SparseMatrix createSparseMatrix()
/*     */   {
/*  34 */     String indexFile = FileUtil.getNewTempFilename("newmatrix", "index");
/*  35 */     String matrixFile = FileUtil.getNewTempFilename("newmatrix", "matrix");
/*  36 */     return new IntSuperSparseMatrix(indexFile, matrixFile, false, false);
/*     */   }
/*     */ 
/*     */   protected AbstractFlatSparseMatrix createFlatSparseMatrix(boolean mergeMode, boolean miniMode) {
/*  40 */     return new IntFlatSparseMatrix(mergeMode, miniMode);
/*     */   }
/*     */ 
/*     */   protected Row createRow(int row, int columns, byte[] data)
/*     */   {
/*  46 */     IntRow cur = new IntRow();
/*  47 */     cur.load(row, columns, data);
/*  48 */     return cur;
/*     */   }
/*     */ 
/*     */   public Cell createCell(int row, int column, byte[] data)
/*     */   {
/*  54 */     IntCell cur = new IntCell(row, column);
/*  55 */     cur.fromByteArray(data);
/*  56 */     return cur;
/*     */   }
/*     */ 
/*     */   public Cell createCell(int row, int column, String data)
/*     */   {
/*  62 */     IntCell cur = new IntCell(row, column);
/*  63 */     cur.fromString(data);
/*  64 */     return cur;
/*     */   }
/*     */ 
/*     */   public boolean add(int row, int column, int score) {
/*  68 */     return add(new IntCell(row, column, score));
/*     */   }
/*     */ 
/*     */   public int getInt(int row, int column) {
/*  72 */     if (row >= this.rows) return 0;
/*  73 */     return ((IntRow)getRow(row)).getInt(column);
/*     */   }
/*     */ 
/*     */   public int getNonZeroIntScoreInRow(int row, int index) {
/*  77 */     if (row >= this.rows) return 0;
/*  78 */     return ((IntRow)getRow(row)).getNonZeroIntScore(index);
/*     */   }
/*     */ 
/*     */   public int[] getNonZeroIntScoresInRow(int row)
/*     */   {
/*  84 */     if (row >= this.rows)
/*  85 */       return null;
/*  86 */     int[] oldArray = ((IntRow)getRow(row)).getNonZeroIntScores();
/*  87 */     int[] newArray = new int[oldArray.length];
/*  88 */     System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
/*  89 */     return newArray;
/*     */   }
/*     */ 
/*     */   public double[] getNonZeroDoubleScoresInRow(int row)
/*     */   {
/*  97 */     if (row >= this.rows)
/*  98 */       return null;
/*  99 */     int[] oldArray = ((IntRow)getRow(row)).getNonZeroIntScores();
/* 100 */     if (oldArray == null) {
/* 101 */       return null;
/*     */     }
/* 103 */     double[] newArray = new double[oldArray.length];
/* 104 */     for (int i = 0; i < oldArray.length; i++)
/* 105 */       newArray[i] = oldArray[i];
/* 106 */     return newArray;
/*     */   }
/*     */ 
/*     */   public long getRowSum(int row)
/*     */   {
/* 113 */     long sum = 0L;
/* 114 */     int[] scores = getNonZeroIntScoresInRow(row);
/* 115 */     for (int count = 0; count < scores.length; count++)
/*     */     {
/* 117 */       sum += scores[count];
/*     */     }
/* 119 */     return sum;
/*     */   }
/*     */ 
/*     */   public long getColumnSum(int column)
/*     */   {
/* 124 */     return ((IntSuperSparseMatrix)transpose()).getRowSum(column);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.IntSuperSparseMatrix
 * JD-Core Version:    0.6.2
 */