package edu.drexel.cis.dragon.matrix;

public abstract interface DoubleSparseMatrix extends SparseMatrix
{
  public abstract boolean add(int paramInt1, int paramInt2, double paramDouble);

  public abstract double getRowSum(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.DoubleSparseMatrix
 * JD-Core Version:    0.6.2
 */