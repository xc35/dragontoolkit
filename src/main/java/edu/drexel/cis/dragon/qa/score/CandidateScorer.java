package edu.drexel.cis.dragon.qa.score;

import edu.drexel.cis.dragon.nlp.Sentence;
import edu.drexel.cis.dragon.qa.query.QueryWord;
import edu.drexel.cis.dragon.qa.query.QuestionQuery;
import edu.drexel.cis.dragon.qa.system.Candidate;

public abstract interface CandidateScorer
{
  public abstract double initialize(QuestionQuery paramQuestionQuery, Sentence paramSentence, QueryWord[] paramArrayOfQueryWord);

  public abstract double score(Candidate paramCandidate);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.score.CandidateScorer
 * JD-Core Version:    0.6.2
 */