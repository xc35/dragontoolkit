package edu.drexel.cis.dragon.nlp.extract;

import edu.drexel.cis.dragon.nlp.Document;
import edu.drexel.cis.dragon.nlp.Sentence;
import edu.drexel.cis.dragon.util.SortedArray;
import java.io.PrintWriter;
import java.util.ArrayList;

public abstract interface TripleExtractor
{
  public abstract void initDocExtraction();

  public abstract boolean extractFromDoc(String paramString);

  public abstract boolean extractFromDoc(Document paramDocument);

  public abstract ArrayList extractFromSentence(Sentence paramSentence);

  public abstract ArrayList getConceptList();

  public abstract ArrayList getTripleList();

  public abstract ConceptExtractor getConceptExtractor();

  public abstract SortedArray mergeTriples(SortedArray paramSortedArray, ArrayList paramArrayList);

  public abstract boolean getFilteringOption();

  public abstract void setFilteringOption(boolean paramBoolean);

  public abstract void setConceptFilter(ConceptFilter paramConceptFilter);

  public abstract ConceptFilter getConceptFilter();

  public abstract void setCoordinatingCheckOption(boolean paramBoolean);

  public abstract boolean getCoordinatingCheckOption();

  public abstract void setCoReferenceOption(boolean paramBoolean);

  public abstract boolean getCoReferenceOption();

  public abstract boolean getSemanticCheckOption();

  public abstract void setSemanticCheckOption(boolean paramBoolean);

  public abstract boolean getRelationCheckOption();

  public abstract void setRelationCheckOption(boolean paramBoolean);

  public abstract boolean getClauseIdentifyOption();

  public abstract void setClauseIdentifyOption(boolean paramBoolean);

  public abstract void print(PrintWriter paramPrintWriter);

  public abstract void print(PrintWriter paramPrintWriter, ArrayList paramArrayList1, ArrayList paramArrayList2);
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.TripleExtractor
 * JD-Core Version:    0.6.2
 */