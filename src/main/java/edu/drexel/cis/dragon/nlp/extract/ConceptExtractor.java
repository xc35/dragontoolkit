package edu.drexel.cis.dragon.nlp.extract;

import edu.drexel.cis.dragon.nlp.Document;
import edu.drexel.cis.dragon.nlp.DocumentParser;
import edu.drexel.cis.dragon.nlp.Sentence;
import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
import edu.drexel.cis.dragon.util.SortedArray;
import java.io.PrintWriter;
import java.util.ArrayList;

public abstract interface ConceptExtractor
{
  public abstract void initDocExtraction();

  public abstract SortedArray mergeConceptByName(ArrayList paramArrayList);

  public abstract SortedArray mergeConceptByEntryID(ArrayList paramArrayList);

  public abstract ArrayList extractFromDoc(String paramString);

  public abstract ArrayList extractFromDoc(Document paramDocument);

  public abstract ArrayList extractFromSentence(Sentence paramSentence);

  public abstract ArrayList getConceptList();

  public abstract boolean supportConceptName();

  public abstract boolean supportConceptEntry();

  public abstract void setSubConceptOption(boolean paramBoolean);

  public abstract boolean getSubConceptOption();

  public abstract boolean getFilteringOption();

  public abstract void setFilteringOption(boolean paramBoolean);

  public abstract void setConceptFilter(ConceptFilter paramConceptFilter);

  public abstract ConceptFilter getConceptFilter();

  public abstract Lemmatiser getLemmatiser();

  public abstract void setLemmatiser(Lemmatiser paramLemmatiser);

  public abstract void setDocumentParser(DocumentParser paramDocumentParser);

  public abstract DocumentParser getDocumentParser();

  public abstract void print(PrintWriter paramPrintWriter);

  public abstract void print(PrintWriter paramPrintWriter, ArrayList paramArrayList);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.ConceptExtractor
 * JD-Core Version:    0.6.2
 */