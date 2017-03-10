package edu.drexel.cis.dragon.nlp;

public abstract interface Concept
{
  public static final int TYPE_TERM = 1;
  public static final int TYPE_PHRASE = 2;
  public static final int TYPE_TOKEN = 3;

  public abstract int getConceptType();

  public abstract Concept copy();

  public abstract String getName();

  public abstract String getEntryID();

  public abstract String getSemanticType();

  public abstract boolean isSubConcept();

  public abstract int getIndex();

  public abstract void setIndex(int paramInt);

  public abstract int getFrequency();

  public abstract void setFrequency(int paramInt);

  public abstract void addFrequency(int paramInt);

  public abstract void setWeight(double paramDouble);

  public abstract double getWeight();

  public abstract Object getMemo();

  public abstract void setMemo(Object paramObject);

  public abstract boolean equalTo(Concept paramConcept);

  public abstract Word getStartingWord();

  public abstract Word getEndingWord();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.Concept
 * JD-Core Version:    0.6.2
 */
