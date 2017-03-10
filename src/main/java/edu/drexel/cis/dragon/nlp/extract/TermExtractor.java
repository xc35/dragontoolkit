package edu.drexel.cis.dragon.nlp.extract;

import edu.drexel.cis.dragon.nlp.ontology.Ontology;
import edu.drexel.cis.dragon.nlp.tool.Tagger;

public abstract interface TermExtractor extends ConceptExtractor
{
  public abstract Tagger getPOSTagger();

  public abstract Ontology getOntology();

  public abstract void setCoordinatingCheckOption(boolean paramBoolean);

  public abstract boolean getCoordinatingCheckOption();

  public abstract void setAbbreviationOption(boolean paramBoolean);

  public abstract boolean getAbbreviationOption();

  public abstract void setAttributeCheckOption(boolean paramBoolean);

  public abstract boolean getAttributeCheckOption();

  public abstract boolean enableAttributeCheckOption(AttributeChecker paramAttributeChecker);

  public abstract boolean getSemanticCheckOption();

  public abstract void setSemanticCheckOption(boolean paramBoolean);

  public abstract boolean getCoordinatingTermPredictOption();

  public abstract void setCoordinatingTermPredictOption(boolean paramBoolean);

  public abstract boolean getCompoundTermPredictOption();

  public abstract void setCompoundTermPredictOption(boolean paramBoolean);

  public abstract boolean enableCompoundTermPredictOption(String paramString);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.TermExtractor
 * JD-Core Version:    0.6.2
 */