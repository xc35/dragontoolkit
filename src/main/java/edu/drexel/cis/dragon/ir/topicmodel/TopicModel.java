package edu.drexel.cis.dragon.ir.topicmodel;

public abstract interface TopicModel
{
  public abstract boolean estimateModel(int paramInt);

  public abstract int getTopicNum();

  public abstract double[] getTopic(int paramInt);

  public abstract int getDocNum();

  public abstract double[] getDocTopics(int paramInt);

  public abstract int getTermNum();

  public abstract String getTermName(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.topicmodel.TopicModel
 * JD-Core Version:    0.6.2
 */