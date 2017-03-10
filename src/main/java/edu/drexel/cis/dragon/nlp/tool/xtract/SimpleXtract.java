/*     */ package edu.drexel.cis.dragon.nlp.tool.xtract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.extract.EngDocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class SimpleXtract
/*     */ {
/*     */   private int maxSpan;
/*     */   private String indexFolder;
/*     */ 
/*     */   public SimpleXtract(int maxSpan, String workingDir)
/*     */   {
/*  26 */     this.maxSpan = maxSpan;
/*  27 */     this.indexFolder = workingDir;
/*     */   }
/*     */ 
/*     */   public void index(CollectionReader cr, Tagger tagger, Lemmatiser lemmatiser) {
/*  31 */     index(cr, tagger, lemmatiser);
/*     */   }
/*     */ 
/*     */   public void index(CollectionReader cr, Tagger tagger, Lemmatiser lemmatiser, String wordDelimitor)
/*     */   {
/*  37 */     CollectionReader[] arrReader = new CollectionReader[1];
/*  38 */     arrReader[0] = cr;
/*  39 */     index(arrReader, tagger, lemmatiser, wordDelimitor);
/*     */   }
/*     */ 
/*     */   public void index(CollectionReader[] cr, Tagger tagger, Lemmatiser lemmatiser) {
/*  43 */     index(cr, tagger, lemmatiser, null);
/*     */   }
/*     */ 
/*     */   public void index(CollectionReader[] cr, Tagger tagger, Lemmatiser lemmatiser, String wordDelimitor)
/*     */   {
/*  50 */     WordPairIndexer indexer = new EngWordPairIndexer(this.indexFolder, this.maxSpan, tagger, lemmatiser);
/*  51 */     if ((wordDelimitor != null) && (wordDelimitor.length() > 0))
/*  52 */       indexer.setDocumentParser(new EngDocumentParser(wordDelimitor));
/*  53 */     for (int i = 0; i < cr.length; i++)
/*  54 */       indexer.index(cr[i]);
/*  55 */     indexer.close();
/*     */   }
/*     */ 
/*     */   public void index(CollectionReader cr, WordPairIndexer indexer)
/*     */   {
/*  61 */     CollectionReader[] arrReader = new CollectionReader[1];
/*  62 */     arrReader[0] = cr;
/*  63 */     index(arrReader, indexer);
/*     */   }
/*     */ 
/*     */   public void index(CollectionReader[] cr, WordPairIndexer indexer)
/*     */   {
/*  69 */     for (int i = 0; i < cr.length; i++)
/*  70 */       indexer.index(cr[i]);
/*  71 */     indexer.close();
/*     */   }
/*     */ 
/*     */   public void extract(double minStrength, double minSpread, double minZScore, double minExpandRatio, String outputFile)
/*     */   {
/*  77 */     WordPairExpand expander = new EngWordPairExpand(this.maxSpan, this.indexFolder, minExpandRatio);
/*  78 */     extract(expander, minStrength, minSpread, minZScore, outputFile);
/*     */   }
/*     */ 
/*     */   public void extract(WordPairExpand expander, double minStrength, double minSpread, double minZScore, String outputFile)
/*     */   {
/*  87 */     SortedArray phraseList = new SortedArray();
/*  88 */     PrintWriter screen = FileUtil.getScreen();
/*  89 */     WordPairStat[] arrStat = filter(minStrength, minSpread, minZScore);
/*  90 */     for (int i = 0; i < arrStat.length; i++) {
/*  91 */       for (int j = 1; j < this.maxSpan; j++) {
/*  92 */         if (arrStat[i].getFrequency(j) > 0)
/*  93 */           addPhrase(expander.expand(arrStat[i], j), phraseList, screen);
/*  94 */         if (arrStat[i].getFrequency(-j) > 0) {
/*  95 */           addPhrase(expander.expand(arrStat[i], -j), phraseList, screen);
/*     */         }
/*     */       }
/*     */     }
/*  99 */     printPhrase(phraseList, outputFile);
/*     */   }
/*     */ 
/*     */   private void addPhrase(ArrayList inputList, SortedArray phraseList, PrintWriter screen)
/*     */   {
/* 106 */     if (inputList == null)
/* 107 */       return;
/* 108 */     for (int i = 0; i < inputList.size(); i++) {
/* 109 */       Token token = (Token)inputList.get(i);
/* 110 */       if (!phraseList.add(token)) {
/* 111 */         Token old = (Token)phraseList.get(phraseList.insertedPos());
/* 112 */         old.setFrequency(Math.max(old.getFrequency(), token.getFrequency()));
/*     */       }
/* 114 */       screen.println(token.getValue() + "  " + token.getFrequency());
/*     */     }
/*     */   }
/*     */ 
/*     */   private WordPairStat[] filter(double minStrength, double minSpread, double minZScore)
/*     */   {
/* 122 */     WordPairFilter pairFilter = new WordPairFilter(this.indexFolder, this.maxSpan, minStrength, minSpread, minZScore);
/* 123 */     WordPairStat[] arrStat = pairFilter.execute();
/* 124 */     return arrStat;
/*     */   }
/*     */ 
/*     */   private void printPhrase(SortedArray list, String filename)
/*     */   {
/*     */     try
/*     */     {
/* 133 */       BufferedWriter bw = FileUtil.getTextWriter(filename);
/* 134 */       bw.write(list.size() + "\n");
/* 135 */       for (int i = 0; i < list.size(); i++) {
/* 136 */         Token token = (Token)list.get(i);
/* 137 */         bw.write(token.getValue());
/* 138 */         bw.write(9 + token.getFrequency());
/* 139 */         bw.write(10);
/* 140 */         bw.flush();
/*     */       }
/* 142 */       bw.close();
/*     */     }
/*     */     catch (Exception e) {
/* 145 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void printWordPair(WordPairStat[] arrStat, String filename)
/*     */   {
/*     */     try
/*     */     {
/* 156 */       SimpleElementList wordkeyList = new SimpleElementList(this.indexFolder + "/wordkey.list", false);
/* 157 */       SortedArray pairList = new SortedArray();
/* 158 */       for (int i = 0; i < arrStat.length; i++) {
/* 159 */         String pair = wordkeyList.search(arrStat[i].getFirstWord()) + " " + wordkeyList.search(arrStat[i].getSecondWord());
/* 160 */         pairList.add(pair.toLowerCase());
/*     */       }
/* 162 */       printPhrase(pairList, filename);
/*     */     }
/*     */     catch (Exception e) {
/* 165 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.xtract.SimpleXtract
 * JD-Core Version:    0.6.2
 */