package edu.drexel.cis.dragon.onlinedb;

public abstract interface ArticleParser
{
  public abstract Article parse(String paramString);

  public abstract String assemble(Article paramArticle);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.ArticleParser
 * JD-Core Version:    0.6.2
 */