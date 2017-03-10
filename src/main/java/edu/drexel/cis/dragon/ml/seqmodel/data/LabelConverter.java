package edu.drexel.cis.dragon.ml.seqmodel.data;

public abstract interface LabelConverter
{
  public abstract int getInternalLabel(int paramInt);

  public abstract int getInternalLabel(String paramString);

  public abstract int getExternalLabelID(int paramInt);

  public abstract String getExternalLabelString(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.data.LabelConverter
 * JD-Core Version:    0.6.2
 */