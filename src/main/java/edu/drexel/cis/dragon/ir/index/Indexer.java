package edu.drexel.cis.dragon.ir.index;

import edu.drexel.cis.dragon.onlinedb.Article;

public abstract interface Indexer
{
  public abstract void initialize();

  public abstract void close();

  public abstract boolean index(Article paramArticle);

  public abstract boolean indexed(String paramString);

  public abstract void setLog(String paramString);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.Indexer
 * JD-Core Version:    0.6.2
 */