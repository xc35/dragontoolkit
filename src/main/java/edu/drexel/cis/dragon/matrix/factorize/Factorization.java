package edu.drexel.cis.dragon.matrix.factorize;

import edu.drexel.cis.dragon.matrix.DoubleDenseMatrix;
import edu.drexel.cis.dragon.matrix.SparseMatrix;

public abstract interface Factorization
{
  public abstract void factorize(SparseMatrix paramSparseMatrix, int paramInt);

  public abstract DoubleDenseMatrix getLeftMatrix();

  public abstract DoubleDenseMatrix getRightMatrix();

  public abstract DoubleDenseMatrix getMiddleMatrix();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.factorize.Factorization
 * JD-Core Version:    0.6.2
 */