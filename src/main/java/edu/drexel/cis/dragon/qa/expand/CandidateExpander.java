package edu.drexel.cis.dragon.qa.expand;

import edu.drexel.cis.dragon.qa.query.QuestionQuery;
import edu.drexel.cis.dragon.qa.system.CandidateBase;
import java.util.ArrayList;

public abstract interface CandidateExpander
{
  public abstract ArrayList expand(QuestionQuery paramQuestionQuery, CandidateBase paramCandidateBase, ArrayList paramArrayList);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.expand.CandidateExpander
 * JD-Core Version:    0.6.2
 */