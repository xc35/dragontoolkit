package edu.drexel.cis.dragon.ml.seqmodel.data;

public abstract interface Dataset
{
  public abstract void startScan();

  public abstract boolean hasNext();

  public abstract DataSequence next();

  public abstract int getLabelNum();

  public abstract int getOriginalLabelNum();

  public abstract int getMarkovOrder();

  public abstract int size();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.data.Dataset
 * JD-Core Version:    0.6.2
 */