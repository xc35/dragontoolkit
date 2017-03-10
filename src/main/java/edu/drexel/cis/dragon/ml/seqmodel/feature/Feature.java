package edu.drexel.cis.dragon.ml.seqmodel.feature;

public abstract interface Feature
{
  public abstract FeatureIdentifier getID();

  public abstract void setID(FeatureIdentifier paramFeatureIdentifier);

  public abstract Feature copy();

  public abstract int getIndex();

  public abstract void setIndex(int paramInt);

  public abstract int getLabel();

  public abstract void setLabel(int paramInt);

  public abstract int getPrevLabel();

  public abstract void setPrevLabel(int paramInt);

  public abstract double getValue();

  public abstract void setValue(double paramDouble);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.Feature
 * JD-Core Version:    0.6.2
 */