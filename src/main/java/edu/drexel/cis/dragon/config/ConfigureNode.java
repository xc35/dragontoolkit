package edu.drexel.cis.dragon.config;

public abstract interface ConfigureNode
{
  public abstract ConfigureNode getParentNode();

  public abstract ConfigureNode getFirstChild();

  public abstract ConfigureNode getNextSibling();

  public abstract String getNodeName();

  public abstract int getNodeID();

  public abstract String getNodeType();

  public abstract Class getNodeClass();

  public abstract String getString(String paramString);

  public abstract String getString(String paramString1, String paramString2);

  public abstract int getInt(String paramString);

  public abstract int getInt(String paramString, int paramInt);

  public abstract boolean getBoolean(String paramString);

  public abstract boolean getBoolean(String paramString, boolean paramBoolean);

  public abstract double getDouble(String paramString);

  public abstract double getDouble(String paramString, double paramDouble);

  public abstract String getParameterType(String paramString);

  public abstract String getParameterType(String paramString1, String paramString2);

  public abstract boolean exist(String paramString);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.ConfigureNode
 * JD-Core Version:    0.6.2
 */