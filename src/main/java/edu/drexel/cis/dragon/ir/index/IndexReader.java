package edu.drexel.cis.dragon.ir.index;

import edu.drexel.cis.dragon.onlinedb.Article;

public abstract interface IndexReader
{
  public abstract void initialize();

  public abstract void close();

  public abstract boolean isRelationSupported();

  public abstract IRCollection getCollection();

  public abstract IRDoc getDoc(int paramInt);

  public abstract IRDoc getDoc(String paramString);

  public abstract String getDocKey(int paramInt);

  public abstract Article getOriginalDoc(String paramString);

  public abstract Article getOriginalDoc(int paramInt);

  public abstract IRTerm[] getTermList(int paramInt);

  public abstract int[] getTermIndexList(int paramInt);

  public abstract int[] getTermFrequencyList(int paramInt);

  public abstract IRTerm getIRTerm(String paramString);

  public abstract IRTerm getIRTerm(int paramInt);

  public abstract IRTerm getIRTerm(int paramInt1, int paramInt2);

  public abstract String getTermKey(int paramInt);

  public abstract IRDoc[] getTermDocList(int paramInt);

  public abstract int[] getTermDocFrequencyList(int paramInt);

  public abstract int[] getTermDocIndexList(int paramInt);

  public abstract IRRelation[] getRelationList(int paramInt);

  public abstract int[] getRelationFrequencyList(int paramInt);

  public abstract int[] getRelationIndexList(int paramInt);

  public abstract IRRelation getIRRelation(int paramInt);

  public abstract IRRelation getIRRelation(int paramInt1, int paramInt2);

  public abstract IRDoc[] getRelationDocList(int paramInt);

  public abstract int[] getRelationDocFrequencyList(int paramInt);

  public abstract int[] getRelationDocIndexList(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.IndexReader
 * JD-Core Version:    0.6.2
 */