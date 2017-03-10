package edu.drexel.cis.dragon.ir.classification.multiclass;

public abstract interface CodeMatrix
{
  public abstract int getClassNum();

  public abstract void setClassNum(int paramInt);

  public abstract int getClassifierNum();

  public abstract int getCode(int paramInt1, int paramInt2);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.multiclass.CodeMatrix
 * JD-Core Version:    0.6.2
 */