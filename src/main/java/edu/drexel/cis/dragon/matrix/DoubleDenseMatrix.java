package edu.drexel.cis.dragon.matrix;

public abstract interface DoubleDenseMatrix extends DenseMatrix
{
  public abstract void assign(double paramDouble);

  public abstract boolean add(int paramInt1, int paramInt2, double paramDouble);

  public abstract boolean setDouble(int paramInt1, int paramInt2, double paramDouble);

  public abstract boolean setDouble(int paramInt, double[] paramArrayOfDouble);

  public abstract double getRowSum(int paramInt);

  public abstract double getColumnSum(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.DoubleDenseMatrix
 * JD-Core Version:    0.6.2
 */