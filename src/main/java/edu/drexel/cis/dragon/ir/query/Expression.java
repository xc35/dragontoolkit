package edu.drexel.cis.dragon.ir.query;

public abstract interface Expression
{
  public static final int EXPRESSION_BOOL = 1;
  public static final int EXPRESSION_SIMPLE = 2;

  public abstract String toString();

  public abstract String toSQLExpression();

  public abstract Operator getOperator();

  public abstract int getChildNum();

  public abstract Expression getChild(int paramInt);

  public abstract boolean isSimpleExpression();

  public abstract boolean isBoolExpression();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.Expression
 * JD-Core Version:    0.6.2
 */