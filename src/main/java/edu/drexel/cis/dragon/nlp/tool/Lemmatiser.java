package edu.drexel.cis.dragon.nlp.tool;

public abstract interface Lemmatiser
{
  public static final int FIRSTPOS = 1;
  public static final int LASTPOS = 4;

  public abstract String lemmatize(String paramString);

  public abstract String lemmatize(String paramString, int paramInt);

  public abstract String stem(String paramString);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.Lemmatiser
 * JD-Core Version:    0.6.2
 */