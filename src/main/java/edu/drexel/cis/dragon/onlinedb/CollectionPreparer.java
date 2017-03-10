package edu.drexel.cis.dragon.onlinedb;

public abstract interface CollectionPreparer
{
  public abstract boolean addListedArticles(String paramString);

  public abstract boolean addArticles(ArticleQuery paramArticleQuery);

  public abstract boolean addArticles(String paramString);

  public abstract boolean addArticles(String[] paramArrayOfString);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.CollectionPreparer
 * JD-Core Version:    0.6.2
 */