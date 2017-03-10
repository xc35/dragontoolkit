package edu.drexel.cis.dragon.qa.merge;

import edu.drexel.cis.dragon.qa.query.QuestionQuery;
import java.util.ArrayList;

public abstract interface CandidateMerger
{
  public abstract ArrayList merge(QuestionQuery paramQuestionQuery, ArrayList paramArrayList);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.merge.CandidateMerger
 * JD-Core Version:    0.6.2
 */