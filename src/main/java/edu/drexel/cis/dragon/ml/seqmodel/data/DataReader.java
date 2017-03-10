package edu.drexel.cis.dragon.ml.seqmodel.data;

public abstract interface DataReader
{
  public abstract Dataset read();

  public abstract DataSequence readRow();

  public abstract void close();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.data.DataReader
 * JD-Core Version:    0.6.2
 */