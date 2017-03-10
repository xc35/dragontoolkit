package edu.drexel.cis.dragon.ml.seqmodel.feature;

import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
import edu.drexel.cis.dragon.ml.seqmodel.data.Dataset;

public abstract interface FeatureGenerator
{
  public abstract boolean addFeatureType(FeatureType paramFeatureType);

  public abstract int getFeatureTypeNum();

  public abstract FeatureType getFeatureTYpe(int paramInt);

  public abstract boolean train(Dataset paramDataset);

  public abstract boolean loadFeatureData();

  public abstract boolean readFeatures(String paramString);

  public abstract boolean saveFeatures(String paramString);

  public abstract void startScanFeaturesAt(DataSequence paramDataSequence, int paramInt1, int paramInt2);

  public abstract boolean hasNext();

  public abstract Feature next();

  public abstract int getFeatureNum();

  public abstract String getFeatureName(int paramInt);

  public abstract boolean supportSegment();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureGenerator
 * JD-Core Version:    0.6.2
 */