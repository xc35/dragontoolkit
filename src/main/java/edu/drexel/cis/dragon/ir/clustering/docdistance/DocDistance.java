package edu.drexel.cis.dragon.ir.clustering.docdistance;

import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
import edu.drexel.cis.dragon.ir.index.IRDoc;

public abstract interface DocDistance
{
  public abstract double getDistance(IRDoc paramIRDoc1, IRDoc paramIRDoc2);

  public abstract void setFeatureFilter(FeatureFilter paramFeatureFilter);

  public abstract FeatureFilter getFeatureFilter();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.docdistance.DocDistance
 * JD-Core Version:    0.6.2
 */