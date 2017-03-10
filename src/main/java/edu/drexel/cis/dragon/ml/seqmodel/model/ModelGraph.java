package edu.drexel.cis.dragon.ml.seqmodel.model;

import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;

public abstract interface ModelGraph
{
  public abstract int getStateNum();

  public abstract int getStartStateNum();

  public abstract int getEndStateNum();

  public abstract boolean isEndState(int paramInt);

  public abstract boolean isStartState(int paramInt);

  public abstract int getStartState(int paramInt);

  public abstract int getEndState(int paramInt);

  public abstract int getEdgeNum();

  public abstract int getLabel(int paramInt);

  public abstract int getLabelNum();

  public abstract int getOriginalLabelNum();

  public abstract int getMarkovOrder();

  public abstract EdgeIterator getEdgeIterator();

  public abstract boolean mapStateToLabel(DataSequence paramDataSequence);

  public abstract boolean mapLabelToState(DataSequence paramDataSequence);

  public abstract boolean mapLabelToState(DataSequence paramDataSequence, int paramInt1, int paramInt2);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.model.ModelGraph
 * JD-Core Version:    0.6.2
 */