package edu.drexel.cis.dragon.nlp.extract;

import edu.drexel.cis.dragon.nlp.Document;
import edu.drexel.cis.dragon.nlp.DocumentParser;
import edu.drexel.cis.dragon.nlp.Sentence;
import edu.drexel.cis.dragon.onlinedb.Article;
import java.util.ArrayList;

public abstract interface DualConceptExtractor
{
  public abstract boolean extractFromDoc(String paramString);

  public abstract boolean extractFromDoc(Document paramDocument);

  public abstract boolean extractFromDoc(Article paramArticle);

  public abstract boolean extractFromSentence(Sentence paramSentence);

  public abstract ArrayList getFirstConceptList();

  public abstract ArrayList getSecondConceptList();

  public abstract boolean supportConceptName();

  public abstract boolean supportConceptEntry();

  public abstract void initDocExtraction();

  public abstract void setDocumentParser(DocumentParser paramDocumentParser);

  public abstract DocumentParser getDocumentParser();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.DualConceptExtractor
 * JD-Core Version:    0.6.2
 */