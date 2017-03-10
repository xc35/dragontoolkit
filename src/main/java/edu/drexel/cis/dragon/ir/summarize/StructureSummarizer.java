package edu.drexel.cis.dragon.ir.summarize;

import edu.drexel.cis.dragon.ir.index.IndexReader;
import java.util.ArrayList;

public abstract interface StructureSummarizer
{
  public abstract IndexReader getIndexReader();

  public abstract TopicSummary summarize(ArrayList paramArrayList);

  public abstract TopicSummary summarize(ArrayList paramArrayList, int paramInt);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.summarize.StructureSummarizer
 * JD-Core Version:    0.6.2
 */