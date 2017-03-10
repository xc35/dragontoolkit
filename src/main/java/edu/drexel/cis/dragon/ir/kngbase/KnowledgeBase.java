package edu.drexel.cis.dragon.ir.kngbase;

import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
import edu.drexel.cis.dragon.nlp.SimpleElementList;

public abstract interface KnowledgeBase
{
  public abstract DoubleSparseMatrix getKnowledgeMatrix();

  public abstract SimpleElementList getRowKeyList();

  public abstract SimpleElementList getColumnKeyList();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.kngbase.KnowledgeBase
 * JD-Core Version:    0.6.2
 */