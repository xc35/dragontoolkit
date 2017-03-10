package edu.drexel.cis.dragon.ir.search.expand;

import edu.drexel.cis.dragon.ir.query.IRQuery;

public abstract interface QueryExpansion
{
  public abstract IRQuery expand(IRQuery paramIRQuery);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.expand.QueryExpansion
 * JD-Core Version:    0.6.2
 */