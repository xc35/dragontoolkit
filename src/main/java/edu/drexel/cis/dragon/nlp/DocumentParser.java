package edu.drexel.cis.dragon.nlp;

import java.util.ArrayList;

public abstract interface DocumentParser
{
  public abstract Document parse(String paramString);

  public abstract Paragraph parseParagraph(String paramString);

  public abstract Sentence parseSentence(String paramString);

  public abstract ArrayList parseTokens(String paramString);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.DocumentParser
 * JD-Core Version:    0.6.2
 */