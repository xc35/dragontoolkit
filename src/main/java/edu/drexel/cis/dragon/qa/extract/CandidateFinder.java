package edu.drexel.cis.dragon.qa.extract;

import edu.drexel.cis.dragon.nlp.Document;
import edu.drexel.cis.dragon.nlp.Sentence;
import edu.drexel.cis.dragon.qa.query.QuestionQuery;
import edu.drexel.cis.dragon.qa.score.CandidateScorer;
import java.util.ArrayList;

public abstract interface CandidateFinder
{
  public abstract void initDocument(Document paramDocument, QuestionQuery paramQuestionQuery);

  public abstract ArrayList extract(Document paramDocument, QuestionQuery paramQuestionQuery);

  public abstract ArrayList extract(Sentence paramSentence, QuestionQuery paramQuestionQuery);

  public abstract void setMinSentenceScore(double paramDouble);

  public abstract double getMinSentenceScore();

  public abstract void setCandidateScorer(CandidateScorer paramCandidateScorer);

  public abstract CandidateScorer getCandidateScorer();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.extract.CandidateFinder
 * JD-Core Version:    0.6.2
 */