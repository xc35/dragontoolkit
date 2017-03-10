package edu.drexel.cis.dragon.ir.classification.featureselection;

import edu.drexel.cis.dragon.ir.classification.DocClassSet;
import edu.drexel.cis.dragon.ir.index.IndexReader;
import edu.drexel.cis.dragon.matrix.DenseMatrix;
import edu.drexel.cis.dragon.matrix.SparseMatrix;

public abstract interface FeatureSelector
{
  public abstract void train(SparseMatrix paramSparseMatrix, DocClassSet paramDocClassSet);

  public abstract void train(DenseMatrix paramDenseMatrix, DocClassSet paramDocClassSet);

  public abstract void train(IndexReader paramIndexReader, DocClassSet paramDocClassSet);

  public abstract boolean isSelected(int paramInt);

  public abstract int getSelectedFeatureNum();

  public abstract int map(int paramInt);

  public abstract void setSelectedFeatures(int[] paramArrayOfInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.featureselection.FeatureSelector
 * JD-Core Version:    0.6.2
 */