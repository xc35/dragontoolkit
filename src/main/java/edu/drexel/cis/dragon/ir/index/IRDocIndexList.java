package edu.drexel.cis.dragon.ir.index;

public abstract interface IRDocIndexList
{
  public abstract IRDoc get(int paramInt);

  public abstract boolean add(IRDoc paramIRDoc);

  public abstract void close();

  public abstract int size();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.IRDocIndexList
 * JD-Core Version:    0.6.2
 */