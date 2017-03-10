package edu.drexel.cis.dragon.ml.seqmodel.data;

public abstract interface DataWriter
{
  public abstract boolean write(Dataset paramDataset);

  public abstract boolean write(DataSequence paramDataSequence);

  public abstract void close();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.data.DataWriter
 * JD-Core Version:    0.6.2
 */