package edu.drexel.cis.dragon.ir.search.smooth;

import edu.drexel.cis.dragon.ir.index.IRDoc;
import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;

public abstract interface Smoother
{
  public abstract double getSmoothedProb(IRDoc paramIRDoc, SimpleTermPredicate paramSimpleTermPredicate, int paramInt);

  public abstract double getSmoothedProb(IRDoc paramIRDoc, SimpleTermPredicate paramSimpleTermPredicate);

  public abstract double getSmoothedProb(IRDoc paramIRDoc, int paramInt);

  public abstract double getSmoothedProb(SimpleTermPredicate paramSimpleTermPredicate, int paramInt);

  public abstract double getSmoothedProb(int paramInt);

  public abstract double getSmoothedProb(SimpleTermPredicate paramSimpleTermPredicate);

  public abstract double getSmoothedProb(IRDoc paramIRDoc);

  public abstract boolean setParameters(double[] paramArrayOfDouble);

  public abstract void setQueryTerm(SimpleTermPredicate paramSimpleTermPredicate);

  public abstract void setDoc(IRDoc paramIRDoc);

  public abstract boolean isDocFirstOptimal();

  public abstract boolean isQueryTermFirstOptimal();

  public abstract void setLogLikelihoodOption(boolean paramBoolean);

  public abstract boolean getLogLikelihoodOption();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.smooth.Smoother
 * JD-Core Version:    0.6.2
 */