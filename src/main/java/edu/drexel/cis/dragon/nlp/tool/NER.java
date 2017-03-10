package edu.drexel.cis.dragon.nlp.tool;

import edu.drexel.cis.dragon.nlp.Document;
import edu.drexel.cis.dragon.nlp.Sentence;
import java.util.ArrayList;

public abstract interface NER
{
  public abstract ArrayList extractFromDoc(Document paramDocument);

  public abstract ArrayList extractFromSentence(Sentence paramSentence);

  public abstract ArrayList extractEntities(String paramString);

  public abstract String annotate(String paramString);

  public abstract void setAnnotationTypes(String[] paramArrayOfString);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.NER
 * JD-Core Version:    0.6.2
 */