package edu.drexel.cis.dragon.matrix;

public abstract interface Cell extends Comparable
{
  public abstract int getRow();

  public abstract int getColumn();

  public abstract Cell transpose();

  public abstract byte[] toByteArray();

  public abstract void fromByteArray(byte[] paramArrayOfByte);

  public abstract String toString();

  public abstract void fromString(String paramString);

  public abstract void setDoubleScore(double paramDouble);

  public abstract double getDoubleScore();

  public abstract void setLongScore(long paramLong);

  public abstract long getLongScore();

  public abstract void setIntScore(int paramInt);

  public abstract int getIntScore();

  public abstract void setByteScore(byte paramByte);

  public abstract byte getByteScore();

  public abstract void merge(Cell paramCell);

  public abstract boolean getResetOption();

  public abstract void setResetOption(boolean paramBoolean);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.Cell
 * JD-Core Version:    0.6.2
 */