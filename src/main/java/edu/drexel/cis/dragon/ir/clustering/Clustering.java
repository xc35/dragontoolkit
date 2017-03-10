package edu.drexel.cis.dragon.ir.clustering;

import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
import edu.drexel.cis.dragon.ir.index.IRDoc;
import edu.drexel.cis.dragon.ir.index.IndexReader;

public abstract interface Clustering
{
  public abstract int getClusterNum();

  public abstract long getRandomSeed();

  public abstract void setRandomSeed(long paramLong);

  public abstract DocClusterSet getClusterSet();

  public abstract DocCluster getCluster(int paramInt);

  public abstract IndexReader getIndexReader();

  public abstract boolean cluster();

  public abstract boolean cluster(IRDoc[] paramArrayOfIRDoc);

  public abstract FeatureFilter getFeatureFilter();

  public abstract void setFeatureFilter(FeatureFilter paramFeatureFilter);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.Clustering
 * JD-Core Version:    0.6.2
 */