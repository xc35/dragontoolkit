package edu.drexel.cis.dragon.ir.summarize;

import edu.drexel.cis.dragon.onlinedb.CollectionReader;

public abstract interface GenericMultiDocSummarizer
{
  public abstract String summarize(CollectionReader paramCollectionReader, int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.summarize.GenericMultiDocSummarizer
 * JD-Core Version:    0.6.2
 */