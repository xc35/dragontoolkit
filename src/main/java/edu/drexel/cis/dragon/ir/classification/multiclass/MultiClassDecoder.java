package edu.drexel.cis.dragon.ir.classification.multiclass;

public abstract interface MultiClassDecoder
{
  public abstract int decode(CodeMatrix paramCodeMatrix, double[] paramArrayOfDouble);

  public abstract int[] rank();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.multiclass.MultiClassDecoder
 * JD-Core Version:    0.6.2
 */