package edu.drexel.cis.dragon.qa.query;

public abstract interface QueryGenerator
{
  public abstract QuestionQuery generate(String paramString);

  public abstract void setQueryScorer(QueryScorer paramQueryScorer);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.query.QueryGenerator
 * JD-Core Version:    0.6.2
 */