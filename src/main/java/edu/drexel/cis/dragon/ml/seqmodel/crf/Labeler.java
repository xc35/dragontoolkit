package edu.drexel.cis.dragon.ml.seqmodel.crf;

import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
import edu.drexel.cis.dragon.ml.seqmodel.feature.FeatureGenerator;
import edu.drexel.cis.dragon.ml.seqmodel.model.ModelGraph;

public abstract interface Labeler
{
  public abstract ModelGraph getModelGraph();

  public abstract FeatureGenerator getFeatureGenerator();

  public abstract double[] getModelParameter();

  public abstract boolean readModelParameter(String paramString);

  public abstract boolean label(DataSequence paramDataSequence);

  public abstract boolean label(DataSequence paramDataSequence, double[] paramArrayOfDouble);

  public abstract double getBestSolution(DataSequence paramDataSequence, int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.crf.Labeler
 * JD-Core Version:    0.6.2
 */