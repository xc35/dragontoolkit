package edu.drexel.cis.dragon.matrix;

public abstract interface SparseMatrix extends Matrix
{
  public abstract int getNonZeroNum();

  public abstract int getNonZeroNumInRow(int paramInt);

  public abstract int getNonZeroNumInColumn(int paramInt);

  public abstract int getNonZeroColumnInRow(int paramInt1, int paramInt2);

  public abstract int getNonZeroRowInColumn(int paramInt1, int paramInt2);

  public abstract int[] getNonZeroColumnsInRow(int paramInt);

  public abstract int[] getNonZeroRowsInColumn(int paramInt);

  public abstract Cell getNonZeroCellInRow(int paramInt1, int paramInt2);

  public abstract Cell getNonZeroCellInColumn(int paramInt1, int paramInt2);

  public abstract double getNonZeroDoubleScoreInRow(int paramInt1, int paramInt2);

  public abstract double getNonZeroDoubleScoreInColumn(int paramInt1, int paramInt2);

  public abstract double[] getNonZeroDoubleScoresInRow(int paramInt);

  public abstract double[] getNonZeroDoubleScoresInColumn(int paramInt);

  public abstract int getNonZeroIntScoreInRow(int paramInt1, int paramInt2);

  public abstract int getNonZeroIntScoreInColumn(int paramInt1, int paramInt2);

  public abstract int[] getNonZeroIntScoresInRow(int paramInt);

  public abstract int[] getNonZeroIntScoresInColumn(int paramInt);

  public abstract boolean genCooccurrenceMatrix(IntSparseMatrix paramIntSparseMatrix);

  public abstract boolean genCooccurrenceMatrix(SparseMatrix paramSparseMatrix, IntSparseMatrix paramIntSparseMatrix);

  public abstract boolean genCooccurrenceMatrix(SparseMatrix paramSparseMatrix, int paramInt, IntSparseMatrix paramIntSparseMatrix);

  public abstract Cell getCell(int paramInt1, int paramInt2);

  public abstract boolean add(Cell paramCell);

  public abstract void flush();

  public abstract boolean finalizeData();

  public abstract boolean finalizeData(boolean paramBoolean);

  public abstract boolean isFinalized();

  public abstract SparseMatrix createSparseMatrix();

  public abstract Cell createCell(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

  public abstract Cell createCell(int paramInt1, int paramInt2, String paramString);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.SparseMatrix
 * JD-Core Version:    0.6.2
 */