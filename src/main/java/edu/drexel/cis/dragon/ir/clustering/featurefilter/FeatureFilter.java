package edu.drexel.cis.dragon.ir.clustering.featurefilter;

import edu.drexel.cis.dragon.ir.index.IRDoc;
import edu.drexel.cis.dragon.ir.index.IndexReader;
import edu.drexel.cis.dragon.matrix.DenseMatrix;
import edu.drexel.cis.dragon.matrix.SparseMatrix;

public abstract interface FeatureFilter
{
  public abstract void initialize(IndexReader paramIndexReader, IRDoc[] paramArrayOfIRDoc);

  public abstract void initialize(SparseMatrix paramSparseMatrix, IRDoc[] paramArrayOfIRDoc);

  public abstract void initialize(DenseMatrix paramDenseMatrix, IRDoc[] paramArrayOfIRDoc);

  public abstract boolean isSelected(int paramInt);

  public abstract int getSelectedFeatureNum();

  public abstract int map(int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.featurefilter.FeatureFilter
 * JD-Core Version:    0.6.2
 */