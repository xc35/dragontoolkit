package edu.drexel.cis.dragon.matrix;

public abstract interface Matrix
{
  public abstract int rows();

  public abstract int columns();

  public abstract int getBaseRow();

  public abstract int getBaseColumn();

  public abstract int getCellDataLength();

  public abstract int getInt(int paramInt1, int paramInt2);

  public abstract double getDouble(int paramInt1, int paramInt2);

  public abstract double cosine(int paramInt1, int paramInt2);

  public abstract int getCooccurrenceCount(int paramInt1, int paramInt2);

  public abstract Matrix transpose();

  public abstract Matrix getTranspose();

  public abstract void setTranspose(Matrix paramMatrix);

  public abstract void close();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.Matrix
 * JD-Core Version:    0.6.2
 */