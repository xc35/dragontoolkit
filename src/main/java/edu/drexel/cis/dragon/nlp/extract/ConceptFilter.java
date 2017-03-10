package edu.drexel.cis.dragon.nlp.extract;

import edu.drexel.cis.dragon.nlp.Concept;

public abstract interface ConceptFilter
{
  public abstract boolean keep(Concept paramConcept);

  public abstract boolean keep(String paramString);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.ConceptFilter
 * JD-Core Version:    0.6.2
 */