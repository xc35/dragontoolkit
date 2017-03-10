package edu.drexel.cis.dragon.matrix;

public abstract interface DenseMatrix extends Matrix
{
  public abstract double[] getDouble(int paramInt);

  public abstract int[] getInt(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.DenseMatrix
 * JD-Core Version:    0.6.2
 */