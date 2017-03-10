package edu.drexel.cis.dragon.onlinedb;

public abstract interface ArticleQuery extends CollectionReader
{
  public abstract boolean initQuery();

  public abstract void setSearchTerm(String paramString);

  public abstract boolean moveToNextPage();

  public abstract boolean moveToPage(int paramInt);

  public abstract int getPageNum();

  public abstract int getPageWidth();

  public abstract int getCurPageWidth();

  public abstract int getCurPageNo();

  public abstract boolean moveToNextArticle();

  public abstract boolean moveToArticle(int paramInt);

  public abstract String getArticleKey();

  public abstract Article getArticle();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.ArticleQuery
 * JD-Core Version:    0.6.2
 */