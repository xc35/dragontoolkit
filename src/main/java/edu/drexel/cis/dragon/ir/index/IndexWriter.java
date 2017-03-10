package edu.drexel.cis.dragon.ir.index;

public abstract interface IndexWriter
{
  public abstract void initialize();

  public abstract void close();

  public abstract void clean();

  public abstract boolean write(IRDoc paramIRDoc, IRTerm[] paramArrayOfIRTerm, IRRelation[] paramArrayOfIRRelation);

  public abstract boolean write(IRDoc paramIRDoc, IRTerm[] paramArrayOfIRTerm);

  public abstract int size();

  public abstract void flush();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.IndexWriter
 * JD-Core Version:    0.6.2
 */