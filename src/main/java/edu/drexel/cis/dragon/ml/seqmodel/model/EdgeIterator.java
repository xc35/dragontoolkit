package edu.drexel.cis.dragon.ml.seqmodel.model;

public abstract interface EdgeIterator
{
  public abstract void start();

  public abstract boolean hasNext();

  public abstract Edge next();

  public abstract boolean nextIsOuter();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.model.EdgeIterator
 * JD-Core Version:    0.6.2
 */