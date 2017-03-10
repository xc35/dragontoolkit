package edu.drexel.cis.dragon.nlp.ontology;

public abstract interface SemanticNet
{
  public abstract Ontology getOntology();

  public abstract String getSemanticTypeDesc(String paramString);

  public abstract String getRelationDesc(String paramString);

  public abstract String getHierarchy(String paramString);

  public abstract String[] getRelations(String[] paramArrayOfString1, String[] paramArrayOfString2);

  public abstract String[] getRelations(String paramString1, String paramString2);

  public abstract boolean isSemanticRelated(String[] paramArrayOfString1, String[] paramArrayOfString2);

  public abstract boolean isSemanticRelated(String paramString1, String paramString2);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.SemanticNet
 * JD-Core Version:    0.6.2
 */