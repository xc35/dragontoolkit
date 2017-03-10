package edu.drexel.cis.dragon.ir.query;

import edu.drexel.cis.dragon.onlinedb.Article;

public abstract interface QueryGenerator
{
  public abstract IRQuery generate(Article paramArticle);

  public abstract IRQuery generate(String paramString);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.QueryGenerator
 * JD-Core Version:    0.6.2
 */