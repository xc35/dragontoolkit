package edu.drexel.cis.dragon.ir.index;

public abstract interface IRSignature
{
  public abstract int getIndex();

  public abstract void setIndex(int paramInt);

  public abstract int getDocFrequency();

  public abstract void setDocFrequency(int paramInt);

  public abstract int getFrequency();

  public abstract void setFrequency(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.IRSignature
 * JD-Core Version:    0.6.2
 */