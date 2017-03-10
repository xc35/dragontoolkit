package edu.drexel.cis.dragon.nlp.ontology;

import edu.drexel.cis.dragon.nlp.Term;
import edu.drexel.cis.dragon.nlp.Word;
import java.util.ArrayList;

public abstract interface Ontology
{
  public abstract SemanticNet getSemanticNet();

  public abstract SimilarityMetric getSimilarityMetric();

  public abstract String[] getSemanticType(String[] paramArrayOfString);

  public abstract String[] getSemanticType(String paramString);

  public abstract String[] getCUI(String paramString);

  public abstract String[] getCUI(Word paramWord1, Word paramWord2);

  public abstract boolean isTerm(String paramString);

  public abstract boolean isTerm(Word paramWord1, Word paramWord2);

  public abstract boolean isStartingWord(Word paramWord);

  public abstract Term findTerm(Word paramWord);

  public abstract Term findTerm(Word paramWord1, Word paramWord2);

  public abstract ArrayList findAllTerms(Word paramWord);

  public abstract ArrayList findAllTerms(Word paramWord1, Word paramWord2);

  public abstract void setSenseDisambiguationOption(boolean paramBoolean);

  public abstract boolean getSenseDisambiguationOption();

  public abstract void setAdjectiveTermOption(boolean paramBoolean);

  public abstract boolean getAdjectiveTermOption();

  public abstract void setNPPOption(boolean paramBoolean);

  public abstract boolean getNPPOption();

  public abstract void setCoordinateOption(boolean paramBoolean);

  public abstract boolean getCoordinateOption();

  public abstract void setLemmaOption(boolean paramBoolean);

  public abstract boolean getLemmaOption();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.Ontology
 * JD-Core Version:    0.6.2
 */