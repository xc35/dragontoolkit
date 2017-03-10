package edu.drexel.cis.dragon.ir.index.sequence;

public abstract interface SequenceReader
{
  public abstract void initialize();

  public abstract void close();

  public abstract int[] getSequence(int paramInt);

  public abstract int getSequenceLength(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.SequenceReader
 * JD-Core Version:    0.6.2
 */