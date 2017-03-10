package edu.drexel.cis.dragon.ir.topicmodel;

public abstract interface TwoDimensionModel
{
  public abstract boolean estimateModel(int paramInt1, int paramInt2);

  public abstract int getViewNum();

  public abstract int getTopicNum();

  public abstract double[] getView(int paramInt);

  public abstract double[] getCommonTopic(int paramInt);

  public abstract double[] getViewTopic(int paramInt1, int paramInt2);

  public abstract int getDocNum();

  public abstract double[] getDocViews(int paramInt);

  public abstract double[] getDocTopics(int paramInt1, int paramInt2);

  public abstract int getViewTermNum();

  public abstract int getTopicTermNum();

  public abstract String getViewTermName(int paramInt);

  public abstract String getTopicTermName(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.topicmodel.TwoDimensionModel
 * JD-Core Version:    0.6.2
 */