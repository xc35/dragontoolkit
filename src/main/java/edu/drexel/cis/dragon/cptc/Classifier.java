/*     */ package edu.drexel.cis.dragon.cptc;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.classification.SVMLightClassifier;
/*     */ import edu.drexel.cis.dragon.matrix.IntRow;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.nlp.extract.BasicConceptFilter;
/*     */ import edu.drexel.cis.dragon.nlp.extract.BasicTokenExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.tool.lemmatiser.EngLemmatiser;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.searchengine.GoogleEngine;
/*     */ import edu.drexel.cis.dragon.util.EnvVariable;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class Classifier
/*     */ {
/*     */   private SVMLightClassifier svm;
/*     */   private GoogleEngine google;
/*     */   private ConceptExtractor extractor;
/*     */   private ConceptVectorGenerator generator;
/*     */   private SimpleElementList wordKeyList;
/*     */   private SortedArray mergedWordList;
/*     */   private ConceptHistory history;
/*     */   private ClassifierDesc io;
/*     */   private int top;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  30 */     Classifier classifier = new Classifier(new ClassifierDesc("cptc/entity_snippet"));
/*  31 */     classifier.setSummeryOnly(true);
/*  32 */     classifier.setRankingOption(true);
/*  33 */     classifier.setTopDocumnentNum(30);
/*  34 */     String[] arrLabel = classifier.classify("Stamford, CT");
/*  35 */     for (int i = 0; i < arrLabel.length; i++)
/*  36 */       System.out.println(arrLabel[i]);
/*  37 */     classifier.close();
/*     */   }
/*     */ 
/*     */   public Classifier(ClassifierDesc io) {
/*  41 */     this.io = io;
/*  42 */     this.history = new ConceptHistory(io.getHistoryFilename());
/*  43 */     this.svm = new SVMLightClassifier(io.getModelFilename());
/*  44 */     this.google = new GoogleEngine();
/*  45 */     this.google.setSummaryOnlyOption(true);
/*  46 */     this.extractor = new BasicTokenExtractor(new EngLemmatiser(true, true));
/*  47 */     this.extractor.setConceptFilter(new BasicConceptFilter(EnvVariable.getDragonHome() + "/nlpdata/exp/rijsbergen.stopword"));
/*  48 */     this.extractor.setFilteringOption(true);
/*  49 */     this.wordKeyList = io.getWordList(false);
/*  50 */     this.generator = new ConceptVectorGenerator(this.wordKeyList, io.getWordIDFFilename());
/*  51 */     this.generator.setTop(10);
/*  52 */     this.generator.setUseRanking(true);
/*  53 */     this.mergedWordList = new SortedArray();
/*  54 */     this.top = 10;
/*     */   }
/*     */ 
/*     */   public Classifier(ClassifierDesc io, ConceptExtractor extractor) {
/*  58 */     this.io = io;
/*  59 */     this.extractor = extractor;
/*  60 */     this.history = new ConceptHistory(io.getHistoryFilename());
/*  61 */     this.svm = new SVMLightClassifier(io.getModelFilename());
/*  62 */     this.google = new GoogleEngine();
/*  63 */     this.google.setSummaryOnlyOption(true);
/*  64 */     this.wordKeyList = io.getWordList(false);
/*  65 */     this.generator = new ConceptVectorGenerator(this.wordKeyList, io.getWordIDFFilename());
/*  66 */     this.generator.setTop(10);
/*  67 */     this.generator.setUseRanking(true);
/*  68 */     this.mergedWordList = new SortedArray();
/*  69 */     this.top = 10;
/*     */   }
/*     */ 
/*     */   public ClassifierDesc getClassifierDescription() {
/*  73 */     return this.io;
/*     */   }
/*     */ 
/*     */   public void setTopDocumnentNum(int top) {
/*  77 */     this.generator.setTop(top);
/*  78 */     this.top = top;
/*     */   }
/*     */ 
/*     */   public int getTopDocumentNum() {
/*  82 */     return this.top;
/*     */   }
/*     */ 
/*     */   public void setSiteRestriction(String site) {
/*  86 */     this.google.setSiteRestriction(site);
/*     */   }
/*     */ 
/*     */   public String getSiteRestriction() {
/*  90 */     return this.google.getSiteRestriction();
/*     */   }
/*     */ 
/*     */   public void setSummeryOnly(boolean option) {
/*  94 */     this.google.setSummaryOnlyOption(option);
/*     */   }
/*     */ 
/*     */   public boolean getSummaryOnly() {
/*  98 */     return this.google.getSummaryOnlyOption();
/*     */   }
/*     */ 
/*     */   public void setRankingOption(boolean option) {
/* 102 */     this.generator.setUseRanking(option);
/*     */   }
/*     */ 
/*     */   public boolean getRankingOption() {
/* 106 */     return this.generator.getUseRanking();
/*     */   }
/*     */ 
/*     */   public String[] classify(String concept)
/*     */   {
/* 117 */     ConceptCategory cptCategory = this.history.search(concept);
/* 118 */     if (cptCategory != null) {
/* 119 */       return cptCategory.getPrediction();
/*     */     }
/*     */ 
/* 123 */     this.google.setSearchTerm(concept);
/* 124 */     this.google.initQuery();
/* 125 */     int count = 0;
/* 126 */     IntRow[] arrRow = new IntRow[this.top];
/*     */     Article curArticle;
/* 127 */     while ((count < this.top) && ((curArticle = this.google.getNextArticle()) != null))
/*     */     {
/* 128 */       if (curArticle.getBody() != null)
/*     */       {
/* 131 */         System.out.println(new Date().toString() + " " + curArticle.getKey());
/* 132 */         curArticle.setKey(curArticle.getKey().toLowerCase());
/* 133 */         arrRow[count] = processArticle(curArticle, count + 1);
/* 134 */         count++;
/*     */       }
/*     */     }
/* 136 */     this.svm.classify(this.generator.generateVector(arrRow));
/* 137 */     int[] rank = this.svm.rank();
/* 138 */     String[] arrLabel = new String[rank.length];
/* 139 */     cptCategory = new ConceptCategory(concept);
/* 140 */     for (int i = 0; i < rank.length; i++) {
/* 141 */       arrLabel[i] = this.svm.getClassLabel(rank[i]);
/*     */     }
/* 143 */     cptCategory.setPrediction(arrLabel);
/* 144 */     this.history.add(cptCategory);
/*     */ 
/* 146 */     return arrLabel;
/*     */   }
/*     */ 
/*     */   public void close() {
/* 150 */     this.history.close();
/*     */   }
/*     */ 
/*     */   private IntRow processArticle(Article article, int rank)
/*     */   {
/* 160 */     if ((article == null) || (article.getBody() == null)) {
/* 161 */       return null;
/*     */     }
/*     */ 
/* 164 */     String content = article.getBody();
/* 165 */     if (article.getTitle() != null)
/* 166 */       content = article.getTitle() + " " + content;
/* 167 */     ArrayList list = this.extractor.extractFromDoc(content);
/* 168 */     this.mergedWordList.clear();
/* 169 */     for (int i = 0; i < list.size(); i++) {
/* 170 */       Token curToken = (Token)list.get(i);
/* 171 */       if (!this.mergedWordList.add(curToken)) {
/* 172 */         ((Token)this.mergedWordList.get(this.mergedWordList.insertedPos())).addFrequency(curToken.getFrequency());
/*     */       }
/*     */     }
/*     */ 
/* 176 */     SortedArray wordList = new SortedArray(new IndexComparator());
/* 177 */     for (int i = 0; i < this.mergedWordList.size(); i++) {
/* 178 */       Token curToken = (Token)this.mergedWordList.get(i);
/* 179 */       int index = this.wordKeyList.search(curToken.getName());
/* 180 */       if (index >= 0) {
/* 181 */         curToken.setIndex(index);
/* 182 */         wordList.add(curToken);
/*     */       }
/*     */     }
/*     */ 
/* 186 */     int[] arrIndex = new int[wordList.size()];
/* 187 */     int[] arrFreq = new int[wordList.size()];
/* 188 */     for (int i = 0; i < wordList.size(); i++) {
/* 189 */       Token curToken = (Token)wordList.get(i);
/* 190 */       arrIndex[i] = curToken.getIndex();
/* 191 */       arrFreq[i] = curToken.getFrequency();
/*     */     }
/* 193 */     list.clear();
/* 194 */     return new IntRow(rank, arrIndex.length, arrIndex, arrFreq);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.cptc.Classifier
 * JD-Core Version:    0.6.2
 */