package edu.drexel.cis.dragon.ml.seqmodel.feature;

import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
import edu.drexel.cis.dragon.ml.seqmodel.data.Dataset;

public abstract interface FeatureType
{
  public abstract boolean startScanFeaturesAt(DataSequence paramDataSequence, int paramInt1, int paramInt2);

  public abstract boolean hasNext();

  public abstract Feature next();

  public abstract boolean needTraining();

  public abstract boolean train(Dataset paramDataset);

  public abstract boolean saveTrainingResult();

  public abstract boolean readTrainingResult();

  public abstract int getTypeID();

  public abstract void setTypeID(int paramInt);

  public abstract boolean supportSegment();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureType
 * JD-Core Version:    0.6.2
 */