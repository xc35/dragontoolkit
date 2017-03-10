package edu.drexel.cis.dragon.nlp.tool;

import edu.drexel.cis.dragon.nlp.Sentence;

public abstract interface Tagger
{
  public static final int POS_NOUN = 1;
  public static final int POS_VERB = 2;
  public static final int POS_ADJECTIVE = 3;
  public static final int POS_ADVERB = 4;
  public static final int POS_IN = 5;
  public static final int POS_PRONOUN = 6;
  public static final int POS_DT = 7;
  public static final int POS_CC = 8;
  public static final int POS_NUM = 9;

  public abstract void tag(Sentence paramSentence);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.Tagger
 * JD-Core Version:    0.6.2
 */