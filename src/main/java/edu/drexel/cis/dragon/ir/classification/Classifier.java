package edu.drexel.cis.dragon.ir.classification;

import edu.drexel.cis.dragon.ir.classification.featureselection.FeatureSelector;
import edu.drexel.cis.dragon.ir.index.IRDoc;
import edu.drexel.cis.dragon.ir.index.IndexReader;
import edu.drexel.cis.dragon.matrix.Row;

public abstract interface Classifier
{
  public abstract IndexReader getIndexReader();

  public abstract FeatureSelector getFeatureSelector();

  public abstract void setFeatureSelector(FeatureSelector paramFeatureSelector);

  public abstract void train(DocClassSet paramDocClassSet);

  public abstract void train(DocClassSet paramDocClassSet1, DocClassSet paramDocClassSet2);

  public abstract void train(String paramString1, String paramString2);

  public abstract DocClassSet classify(DocClass paramDocClass);

  public abstract DocClassSet classify(DocClassSet paramDocClassSet, DocClass paramDocClass);

  public abstract DocClassSet classify(DocClassSet paramDocClassSet1, DocClassSet paramDocClassSet2, DocClass paramDocClass);

  public abstract int classify(IRDoc paramIRDoc);

  public abstract int classify(Row paramRow);

  public abstract String getClassLabel(int paramInt);

  public abstract int[] rank();

  public abstract void saveModel(String paramString);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.Classifier
 * JD-Core Version:    0.6.2
 */