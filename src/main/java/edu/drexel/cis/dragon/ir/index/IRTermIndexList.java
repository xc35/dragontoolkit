package edu.drexel.cis.dragon.ir.index;

public abstract interface IRTermIndexList
{
  public abstract IRTerm get(int paramInt);

  public abstract boolean add(IRTerm paramIRTerm);

  public abstract void close();

  public abstract int size();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.IRTermIndexList
 * JD-Core Version:    0.6.2
 */