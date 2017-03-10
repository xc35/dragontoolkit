package edu.drexel.cis.dragon.ir.index.sequence;

public abstract interface SequenceWriter
{
  public abstract void initialize();

  public abstract void close();

  public abstract boolean addSequence(int paramInt, int[] paramArrayOfInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.SequenceWriter
 * JD-Core Version:    0.6.2
 */