package edu.drexel.cis.dragon.nlp.tool;

public abstract interface WordNetUtil
{
  public static final int POS_NOUN = 1;
  public static final int POS_VERB = 2;
  public static final int POS_ADJECTIVE = 3;
  public static final int POS_ADVERB = 4;
  public static final int FIRSTPOS = 1;
  public static final int LASTPOS = 4;

  public abstract String lemmatize(String paramString);

  public abstract String lemmatize(String paramString, int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.WordNetUtil
 * JD-Core Version:    0.6.2
 */