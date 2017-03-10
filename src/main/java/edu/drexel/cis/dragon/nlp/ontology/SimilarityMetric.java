package edu.drexel.cis.dragon.nlp.ontology;

import edu.drexel.cis.dragon.nlp.Term;

public abstract interface SimilarityMetric
{
  public abstract double getSimilarity(Term paramTerm1, Term paramTerm2);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.SimilarityMetric
 * JD-Core Version:    0.6.2
 */