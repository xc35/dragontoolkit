package edu.drexel.cis.dragon.matrix;

public abstract interface Row extends Comparable
{
  public abstract void load(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

  public abstract int getRowIndex();

  public abstract int getNonZeroNum();

  public abstract int[] getNonZeroColumns();

  public abstract int getNonZeroColumn(int paramInt);

  public abstract Cell getCell(int paramInt);

  public abstract Cell getNonZeroCell(int paramInt);

  public abstract double getNonZeroDoubleScore(int paramInt);

  public abstract int getNonZeroIntScore(int paramInt);

  public abstract void setLoadFactor(float paramFloat);

  public abstract float getLoadFactor();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.Row
 * JD-Core Version:    0.6.2
 */