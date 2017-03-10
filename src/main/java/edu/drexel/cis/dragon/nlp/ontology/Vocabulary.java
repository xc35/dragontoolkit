package edu.drexel.cis.dragon.nlp.ontology;

import edu.drexel.cis.dragon.nlp.Word;

public abstract interface Vocabulary
{
  public abstract boolean isPhrase(String paramString);

  public abstract boolean isPhrase(Word paramWord1, Word paramWord2);

  public abstract boolean isStartingWord(Word paramWord);

  public abstract Word findPhrase(Word paramWord);

  public abstract int getPhraseNum();

  public abstract String getPhrase(int paramInt);

  public abstract int maxPhraseLength();

  public abstract int minPhraseLength();

  public abstract void setAdjectivePhraseOption(boolean paramBoolean);

  public abstract boolean getAdjectivePhraseOption();

  public abstract void setNPPOption(boolean paramBoolean);

  public abstract boolean getNPPOption();

  public abstract void setCoordinateOption(boolean paramBoolean);

  public abstract boolean getCoordinateOption();

  public abstract void setLemmaOption(boolean paramBoolean);

  public abstract boolean getLemmaOption();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.Vocabulary
 * JD-Core Version:    0.6.2
 */