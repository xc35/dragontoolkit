package edu.drexel.cis.dragon.onlinedb;

public abstract interface CollectionReader
{
  public abstract ArticleParser getArticleParser();

  public abstract void setArticleParser(ArticleParser paramArticleParser);

  public abstract Article getNextArticle();

  public abstract Article getArticleByKey(String paramString);

  public abstract boolean supportArticleKeyRetrieval();

  public abstract void close();

  public abstract void restart();

  public abstract int size();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.CollectionReader
 * JD-Core Version:    0.6.2
 */