package edu.drexel.cis.dragon.ir.clustering.clustermodel;

import edu.drexel.cis.dragon.ir.clustering.DocCluster;
import edu.drexel.cis.dragon.ir.clustering.DocClusterSet;
import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
import edu.drexel.cis.dragon.ir.index.IRDoc;

public abstract interface ClusterModel
{
  public abstract double getDistance(IRDoc paramIRDoc, DocCluster paramDocCluster);

  public abstract double getDistance(IRDoc paramIRDoc, int paramInt);

  public abstract void setDocCluster(DocCluster paramDocCluster);

  public abstract void setDocClusters(DocClusterSet paramDocClusterSet);

  public abstract int getClusterNum();

  public abstract void setClusterNum(int paramInt);

  public abstract void setFeatureFilter(FeatureFilter paramFeatureFilter);

  public abstract FeatureFilter getFeatureFilter();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.clustermodel.ClusterModel
 * JD-Core Version:    0.6.2
 */