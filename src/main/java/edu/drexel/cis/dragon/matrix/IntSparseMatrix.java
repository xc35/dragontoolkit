package edu.drexel.cis.dragon.matrix;

public abstract interface IntSparseMatrix extends SparseMatrix
{
  public abstract boolean add(int paramInt1, int paramInt2, int paramInt3);

  public abstract long getRowSum(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.IntSparseMatrix
 * JD-Core Version:    0.6.2
 */