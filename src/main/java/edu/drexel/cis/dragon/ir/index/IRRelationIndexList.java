package edu.drexel.cis.dragon.ir.index;

public abstract interface IRRelationIndexList
{
  public abstract IRRelation get(int paramInt);

  public abstract boolean add(IRRelation paramIRRelation);

  public abstract int size();

  public abstract void close();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.IRRelationIndexList
 * JD-Core Version:    0.6.2
 */