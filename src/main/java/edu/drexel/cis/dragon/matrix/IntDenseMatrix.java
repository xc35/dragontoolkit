package edu.drexel.cis.dragon.matrix;

public abstract interface IntDenseMatrix extends DenseMatrix
{
  public abstract void assign(int paramInt);

  public abstract boolean add(int paramInt1, int paramInt2, int paramInt3);

  public abstract boolean setInt(int paramInt1, int paramInt2, int paramInt3);

  public abstract boolean setInt(int paramInt, int[] paramArrayOfInt);

  public abstract long getRowSum(int paramInt);

  public abstract long getColumnSum(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.IntDenseMatrix
 * JD-Core Version:    0.6.2
 */