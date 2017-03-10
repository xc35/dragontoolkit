package edu.drexel.cis.dragon.nlp.tool.xtract;

import edu.drexel.cis.dragon.nlp.Sentence;

public abstract interface WordPairGenerator
{
  public abstract int generate(Sentence paramSentence);

  public abstract WordPairStat getWordPairs(int paramInt);

  public abstract void setMaxSpan(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.xtract.WordPairGenerator
 * JD-Core Version:    0.6.2
 */