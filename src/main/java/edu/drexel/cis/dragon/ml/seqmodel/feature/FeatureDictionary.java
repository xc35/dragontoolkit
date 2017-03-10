package edu.drexel.cis.dragon.ml.seqmodel.feature;

public abstract interface FeatureDictionary
{
  public abstract int getStateNum();

  public abstract int getIndex(Object paramObject);

  public abstract boolean contain(Object paramObject);

  public abstract int getCount(Object paramObject);

  public abstract int getCount(int paramInt);

  public abstract int getCount(int paramInt1, int paramInt2);

  public abstract int getStateCount(int paramInt);

  public abstract int getTotalCount();

  public abstract int size();

  public abstract int getNextStateWithFeature(int paramInt1, int paramInt2);

  public abstract int addFeature(Object paramObject, int paramInt);

  public abstract void finalize();

  public abstract boolean read(String paramString);

  public abstract boolean write(String paramString);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureDictionary
 * JD-Core Version:    0.6.2
 */