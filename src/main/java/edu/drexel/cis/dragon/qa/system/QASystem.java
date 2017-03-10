package edu.drexel.cis.dragon.qa.system;

import edu.drexel.cis.dragon.onlinedb.CollectionReader;
import edu.drexel.cis.dragon.qa.query.QuestionQuery;
import java.util.ArrayList;

public abstract interface QASystem
{
  public abstract ArrayList answer(String paramString);

  public abstract ArrayList answer(CollectionReader paramCollectionReader, QuestionQuery paramQuestionQuery);

  public abstract QuestionQuery generateQuery(String paramString);

  public abstract QuestionQuery getLastQuestionQuery();

  public abstract void setTopDocumentNum(int paramInt);

  public abstract ArrayList getSupportingDoc(Candidate paramCandidate);

  public abstract CandidateBase getCandidateBase();

  public abstract void close();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.system.QASystem
 * JD-Core Version:    0.6.2
 */