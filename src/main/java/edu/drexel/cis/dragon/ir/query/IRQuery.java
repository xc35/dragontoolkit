package edu.drexel.cis.dragon.ir.query;

public abstract interface IRQuery
{
  public abstract int getQueryKey();

  public abstract void setQueryKey(int paramInt);

  public abstract boolean isPredicate();

  public abstract boolean isCompoundQuery();

  public abstract boolean isRelSimpleQuery();

  public abstract boolean isRelBoolQuery();

  public abstract IRQuery getChild(int paramInt);

  public abstract int getChildNum();

  public abstract double getSelectivity();

  public abstract Operator getOperator();

  public abstract String toString();

  public abstract boolean parse(String paramString);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.IRQuery
 * JD-Core Version:    0.6.2
 */