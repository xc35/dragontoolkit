package edu.drexel.cis.dragon.ml.seqmodel.evaluate;

import edu.drexel.cis.dragon.ml.seqmodel.data.Dataset;

public abstract interface Evaluator
{
  public abstract void evaluate(Dataset paramDataset1, Dataset paramDataset2);

  public abstract int totalLabels();

  public abstract int annotatedLabels();

  public abstract int correctAnnotatedLabels();

  public abstract double precision();

  public abstract double recall();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.evaluate.Evaluator
 * JD-Core Version:    0.6.2
 */