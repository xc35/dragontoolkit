package edu.drexel.cis.dragon.ir.search.feedback;

import edu.drexel.cis.dragon.ir.query.IRQuery;
import edu.drexel.cis.dragon.ir.search.Searcher;

public abstract interface Feedback
{
  public abstract IRQuery updateQueryModel(IRQuery paramIRQuery);

  public abstract int getFeedbackDocNum();

  public abstract void setFeedbackDocNum(int paramInt);

  public abstract Searcher getSearcher();

  public abstract void setSearcher(Searcher paramSearcher);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.feedback.Feedback
 * JD-Core Version:    0.6.2
 */