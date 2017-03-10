package edu.drexel.cis.dragon.nlp.tool.xtract;

import edu.drexel.cis.dragon.nlp.DocumentParser;
import edu.drexel.cis.dragon.onlinedb.Article;
import edu.drexel.cis.dragon.onlinedb.CollectionReader;

public abstract interface WordPairIndexer
{
  public abstract DocumentParser getDocumentParser();

  public abstract void setDocumentParser(DocumentParser paramDocumentParser);

  public abstract void index(CollectionReader paramCollectionReader);

  public abstract boolean indexArticle(Article paramArticle);

  public abstract void close();

  public abstract void flush();
}

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.xtract.WordPairIndexer
 * JD-Core Version:    0.6.2
 */