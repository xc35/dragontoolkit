/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ 
/*     */ public class IntGiantSparseMatrix extends AbstractGiantSparseMatrix
/*     */   implements IntSparseMatrix
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */   public IntGiantSparseMatrix(String indexFile, String matrixFile)
/*     */   {
/*  17 */     super(indexFile, matrixFile, IntCell.getCellDataLength());
/*     */   }
/*     */ 
/*     */   public IntGiantSparseMatrix(String indexFile, String matrixFile, boolean mergeMode, boolean miniMode) {
/*  21 */     super(indexFile, matrixFile, IntCell.getCellDataLength(), mergeMode, miniMode);
/*     */   }
/*     */ 
/*     */   public SparseMatrix createSparseMatrix()
/*     */   {
/*  26 */     String indexFile = FileUtil.getNewTempFilename("newmatrix", "index");
/*  27 */     String matrixFile = FileUtil.getNewTempFilename("newmatrix", "matrix");
/*  28 */     return new IntGiantSparseMatrix(indexFile, matrixFile, false, false);
/*     */   }
/*     */ 
/*     */   protected AbstractFlatSparseMatrix createFlatSparseMatrix(boolean mergeMode, boolean miniMode) {
/*  32 */     return new IntFlatSparseMatrix(mergeMode, miniMode);
/*     */   }
/*     */ 
/*     */   protected Row createRow(int row, int columns, byte[] data)
/*     */   {
/*  38 */     IntRow cur = new IntRow();
/*  39 */     cur.load(row, columns, data);
/*  40 */     return cur;
/*     */   }
/*     */ 
/*     */   public Cell createCell(int row, int column, byte[] data)
/*     */   {
/*  46 */     IntCell cur = new IntCell(row, column);
/*  47 */     cur.fromByteArray(data);
/*  48 */     return cur;
/*     */   }
/*     */ 
/*     */   public Cell createCell(int row, int column, String data)
/*     */   {
/*  54 */     IntCell cur = new IntCell(row, column);
/*  55 */     cur.fromString(data);
/*  56 */     return cur;
/*     */   }
/*     */ 
/*     */   public boolean add(int row, int column, int score) {
/*  60 */     return add(new IntCell(row, column, score));
/*     */   }
/*     */ 
/*     */   public int getInt(int row, int column) {
/*  64 */     if (row >= this.rows) return 0;
/*  65 */     return ((IntRow)getRow(row)).getInt(column);
/*     */   }
/*     */ 
/*     */   public int getNonZeroIntScoreInRow(int row, int index) {
/*  69 */     if (row >= this.rows) return 0;
/*  70 */     return ((IntRow)getRow(row)).getNonZeroIntScore(index);
/*     */   }
/*     */ 
/*     */   public int[] getNonZeroIntScoresInRow(int row)
/*     */   {
/*  76 */     if (row >= this.rows)
/*  77 */       return null;
/*  78 */     int[] oldArray = ((IntRow)getRow(row)).getNonZeroIntScores();
/*  79 */     int[] newArray = new int[oldArray.length];
/*  80 */     System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
/*  81 */     return newArray;
/*     */   }
/*     */ 
/*     */   public double[] getNonZeroDoubleScoresInRow(int row)
/*     */   {
/*  89 */     if (row >= this.rows)
/*  90 */       return null;
/*  91 */     int[] oldArray = ((IntRow)getRow(row)).getNonZeroIntScores();
/*  92 */     if (oldArray == null) {
/*  93 */       return null;
/*     */     }
/*  95 */     double[] newArray = new double[oldArray.length];
/*  96 */     for (int i = 0; i < oldArray.length; i++)
/*  97 */       newArray[i] = oldArray[i];
/*  98 */     return newArray;
/*     */   }
/*     */ 
/*     */   public long getRowSum(int row)
/*     */   {
/* 106 */     long sum = 0L;
/* 107 */     int[] scores = getNonZeroIntScoresInRow(row);
/* 108 */     for (int count = 0; count < scores.length; count++)
/*     */     {
/* 110 */       sum += scores[count];
/*     */     }
/* 112 */     return sum;
/*     */   }
/*     */ 
/*     */   public long getColumnSum(int column)
/*     */   {
/* 117 */     return ((IntSuperSparseMatrix)transpose()).getRowSum(column);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.IntGiantSparseMatrix
 * JD-Core Version:    0.6.2
 */