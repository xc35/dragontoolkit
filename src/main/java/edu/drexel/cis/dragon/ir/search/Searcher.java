package edu.drexel.cis.dragon.ir.search;

import edu.drexel.cis.dragon.ir.index.IRDoc;
import edu.drexel.cis.dragon.ir.index.IndexReader;
import edu.drexel.cis.dragon.ir.query.IRQuery;
import edu.drexel.cis.dragon.ir.search.smooth.Smoother;
import java.util.ArrayList;

public abstract interface Searcher
{
  public abstract int search(IRQuery paramIRQuery);

  public abstract IRDoc getIRDoc(int paramInt);

  public abstract ArrayList getRankedDocumentList();

  public abstract int getRetrievedDocNum();

  public abstract IndexReader getIndexReader();

  public abstract Smoother getSmoother();

  public abstract IRQuery getQuery();

  public abstract void setQueryWeightingOption(boolean paramBoolean);

  public abstract boolean getQueryWeightingOption();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.Searcher
 * JD-Core Version:    0.6.2
 */