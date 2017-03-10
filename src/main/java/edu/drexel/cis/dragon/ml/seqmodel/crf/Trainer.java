package edu.drexel.cis.dragon.ml.seqmodel.crf;

import edu.drexel.cis.dragon.ml.seqmodel.data.Dataset;
import edu.drexel.cis.dragon.ml.seqmodel.feature.FeatureGenerator;
import edu.drexel.cis.dragon.ml.seqmodel.model.ModelGraph;

public abstract interface Trainer
{
  public abstract ModelGraph getModelGraph();

  public abstract FeatureGenerator getFeatureGenerator();

  public abstract double[] getModelParameter();

  public abstract boolean saveModelParameter(String paramString);

  public abstract boolean train(Dataset paramDataset);

  public abstract boolean needScaling();

  public abstract void setScalingOption(boolean paramBoolean);

  public abstract int getMaxIteration();

  public abstract void setMaxIteration(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.crf.Trainer
 * JD-Core Version:    0.6.2
 */