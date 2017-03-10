package edu.drexel.cis.dragon.ir.index;

public abstract interface IRSignatureIndexList
{
  public abstract int size();

  public abstract IRSignature getIRSignature(int paramInt);

  public abstract void close();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.IRSignatureIndexList
 * JD-Core Version:    0.6.2
 */