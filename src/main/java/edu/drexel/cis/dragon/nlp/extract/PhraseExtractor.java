package edu.drexel.cis.dragon.nlp.extract;

import edu.drexel.cis.dragon.nlp.ontology.Vocabulary;
import edu.drexel.cis.dragon.nlp.tool.Tagger;

public abstract interface PhraseExtractor extends ConceptExtractor
{
  public abstract Tagger getPOSTagger();

  public abstract Vocabulary getVocabulary();

  public abstract void setSingleNounOption(boolean paramBoolean);

  public abstract boolean getSingleNounOption();

  public abstract void setSingleVerbOption(boolean paramBoolean);

  public abstract boolean getSingleVerbOption();

  public abstract void setSingleAdjectiveOption(boolean paramBoolean);

  public abstract boolean getSingleAdjectiveOption();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.PhraseExtractor
 * JD-Core Version:    0.6.2
 */