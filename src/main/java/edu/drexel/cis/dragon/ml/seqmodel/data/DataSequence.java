package edu.drexel.cis.dragon.ml.seqmodel.data;

public abstract interface DataSequence
{
  public abstract Dataset getParent();

  public abstract void setParent(Dataset paramDataset);

  public abstract DataSequence copy();

  public abstract int length();

  public abstract int getLabel(int paramInt);

  public abstract int getOriginalLabel(int paramInt);

  public abstract BasicToken getToken(int paramInt);

  public abstract void setLabel(int paramInt1, int paramInt2);

  public abstract int getSegmentEnd(int paramInt);

  public abstract void setSegment(int paramInt1, int paramInt2, int paramInt3);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.data.DataSequence
 * JD-Core Version:    0.6.2
 */