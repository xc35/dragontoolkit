package edu.drexel.cis.dragon.qa.rank;

import edu.drexel.cis.dragon.qa.query.QuestionQuery;
import edu.drexel.cis.dragon.qa.system.CandidateBase;
import java.util.ArrayList;

public abstract interface CandidateRanker
{
  public abstract ArrayList rank(QuestionQuery paramQuestionQuery, CandidateBase paramCandidateBase, ArrayList paramArrayList);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.rank.CandidateRanker
 * JD-Core Version:    0.6.2
 */