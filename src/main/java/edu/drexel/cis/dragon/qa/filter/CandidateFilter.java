package edu.drexel.cis.dragon.qa.filter;

import edu.drexel.cis.dragon.qa.query.QuestionQuery;
import edu.drexel.cis.dragon.qa.system.Candidate;
import java.util.ArrayList;

public abstract interface CandidateFilter
{
  public abstract boolean keep(QuestionQuery paramQuestionQuery, Candidate paramCandidate);

  public abstract ArrayList filter(QuestionQuery paramQuestionQuery, ArrayList paramArrayList);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.filter.CandidateFilter
 * JD-Core Version:    0.6.2
 */