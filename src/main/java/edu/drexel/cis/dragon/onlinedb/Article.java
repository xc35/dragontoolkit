package edu.drexel.cis.dragon.onlinedb;

import java.util.Date;

public abstract interface Article extends Comparable
{
  public abstract int getCategory();

  public abstract void setCategory(int paramInt);

  public abstract String getTitle();

  public abstract void setTitle(String paramString);

  public abstract String getMeta();

  public abstract void setMeta(String paramString);

  public abstract String getKey();

  public abstract void setKey(String paramString);

  public abstract String getAbstract();

  public abstract void setAbstract(String paramString);

  public abstract String getBody();

  public abstract void setBody(String paramString);

  public abstract Date getDate();

  public abstract void setDate(Date paramDate);

  public abstract int getLength();

  public abstract void setLength(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.Article
 * JD-Core Version:    0.6.2
 */