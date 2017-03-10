package edu.drexel.cis.dragon.ir.query;

public abstract interface Predicate extends IRQuery
{
  public static final int PREDICATE_TERM = 1;
  public static final int PREDICATE_RELATION = 2;
  public static final int PREDICATE_QUALIFIER = 3;
  public static final int PREDICATE_SIMPLE = 1;
  public static final int PREDICATE_BOOL = 2;

  public abstract double getSelectivity();

  public abstract double getWeight();

  public abstract void setWeight(double paramDouble);

  public abstract boolean isSimplePredicate();

  public abstract boolean isBoolPredicate();

  public abstract boolean isTermPredicate();

  public abstract boolean isRelationPredicate();

  public abstract boolean isQualifierPredicate();

  public abstract Expression getConstraint();

  public abstract String toSQLExpression();

  public abstract String toString();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.Predicate
 * JD-Core Version:    0.6.2
 */