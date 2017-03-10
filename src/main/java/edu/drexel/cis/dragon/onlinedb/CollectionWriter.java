package edu.drexel.cis.dragon.onlinedb;

public abstract interface CollectionWriter
{
  public abstract boolean add(Article paramArticle);

  public abstract ArticleParser getArticleParser();

  public abstract void setArticleParser(ArticleParser paramArticleParser);

  public abstract void close();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.CollectionWriter
 * JD-Core Version:    0.6.2
 */