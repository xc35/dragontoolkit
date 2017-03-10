/*     */ package edu.drexel.cis.dragon.nlp.tool.xtract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.IntSuperSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.Document;
/*     */ import edu.drexel.cis.dragon.nlp.DocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.Paragraph;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.SimplePairList;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.extract.EngDocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class EngWordPairIndexer
/*     */   implements WordPairIndexer
/*     */ {
/*     */   protected int maxSpan;
/*     */   protected Tagger tagger;
/*     */   protected Lemmatiser lemmatiser;
/*     */   protected WordPairGenerator pairGenerator;
/*     */   protected SentenceBase sentenceBase;
/*     */   protected IntSuperSparseMatrix[] arrPairSentLeftMatrix;
/*     */   protected IntSuperSparseMatrix[] arrPairSentRightMatrix;
/*     */   protected SimpleElementList docKeyList;
/*     */   protected SimpleElementList wordKeyList;
/*     */   protected SimplePairList pairKeyList;
/*     */   protected WordPairStatList wordpairStatList;
/*     */   protected DocumentParser parser;
/*     */   protected int flushInterval;
/*     */   protected int indexedNum;
/*     */ 
/*     */   public EngWordPairIndexer(String folder, int maxSpan, Tagger tagger, Lemmatiser lemmatiser)
/*     */   {
/*  33 */     this(folder, maxSpan, tagger, lemmatiser, new EngWordPairGenerator(maxSpan));
/*     */   }
/*     */ 
/*     */   public EngWordPairIndexer(String folder, int maxSpan, Tagger tagger, Lemmatiser lemmatiser, WordPairGenerator pairGenerator) {
/*  37 */     this.maxSpan = maxSpan;
/*  38 */     this.tagger = tagger;
/*  39 */     this.lemmatiser = lemmatiser;
/*  40 */     this.pairGenerator = pairGenerator;
/*  41 */     this.flushInterval = 10000;
/*     */ 
/*  43 */     new File(folder).mkdirs();
/*  44 */     this.parser = new EngDocumentParser();
/*  45 */     this.sentenceBase = new SentenceBase(folder + "/sentencebase.index", folder + "/sentencebase.matrix");
/*  46 */     this.docKeyList = new SimpleElementList(folder + "/dockey.list", true);
/*  47 */     this.wordKeyList = new SimpleElementList(folder + "/wordkey.list", true);
/*  48 */     this.pairKeyList = new SimplePairList(folder + "/pairkey.list", true);
/*  49 */     this.wordpairStatList = new WordPairStatList(folder + "/pairstat.list", maxSpan, true);
/*  50 */     this.arrPairSentRightMatrix = new IntSuperSparseMatrix[maxSpan];
/*  51 */     for (int i = 1; i <= maxSpan; i++) {
/*  52 */       this.arrPairSentRightMatrix[(i - 1)] = new IntSuperSparseMatrix(folder + "/pairsentr" + i + ".index", 
/*  53 */         folder + "/pairsentr" + i + ".matrix", false, false);
/*  54 */       this.arrPairSentRightMatrix[(i - 1)].setFlushInterval(2147483647);
/*     */     }
/*  56 */     this.arrPairSentLeftMatrix = new IntSuperSparseMatrix[maxSpan];
/*  57 */     for (int i = 1; i <= maxSpan; i++) {
/*  58 */       this.arrPairSentLeftMatrix[(i - 1)] = new IntSuperSparseMatrix(folder + "/pairsentl" + i + ".index", 
/*  59 */         folder + "/pairsentl" + i + ".matrix", false, false);
/*  60 */       this.arrPairSentLeftMatrix[(i - 1)].setFlushInterval(2147483647);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DocumentParser getDocumentParser() {
/*  65 */     return this.parser;
/*     */   }
/*     */ 
/*     */   public void setDocumentParser(DocumentParser parser) {
/*  69 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  75 */     this.sentenceBase.close();
/*  76 */     this.docKeyList.close();
/*  77 */     this.wordKeyList.close();
/*  78 */     this.wordpairStatList.close();
/*  79 */     this.pairKeyList.close();
/*  80 */     for (int i = 0; i < this.maxSpan; i++) {
/*  81 */       this.arrPairSentRightMatrix[i].finalizeData();
/*  82 */       this.arrPairSentRightMatrix[i].close();
/*     */     }
/*  84 */     for (int i = 0; i < this.maxSpan; i++) {
/*  85 */       this.arrPairSentLeftMatrix[i].finalizeData();
/*  86 */       this.arrPairSentLeftMatrix[i].close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/*  93 */     for (int i = 0; i < this.maxSpan; i++)
/*  94 */       this.arrPairSentRightMatrix[i].flush();
/*  95 */     for (int i = 0; i < this.maxSpan; i++)
/*  96 */       this.arrPairSentLeftMatrix[i].flush();
/*     */   }
/*     */ 
/*     */   public void index(CollectionReader collectionReader)
/*     */   {
/*     */     try
/*     */     {
/* 103 */       this.indexedNum = 0;
/* 104 */       Article curArticle = collectionReader.getNextArticle();
/* 105 */       while (curArticle != null) {
/* 106 */         if ((this.indexedNum > 0) && (this.indexedNum % this.flushInterval == 0))
/* 107 */           flush();
/* 108 */         indexArticle(curArticle);
/* 109 */         this.indexedNum += 1;
/* 110 */         curArticle = collectionReader.getNextArticle();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 114 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean indexArticle(Article curArticle)
/*     */   {
/*     */     try
/*     */     {
/* 124 */       if (this.docKeyList.contains(curArticle.getKey())) {
/* 125 */         return true;
/*     */       }
/* 127 */       System.out.println(new Date().toString() + " " + curArticle.getKey());
/*     */ 
/* 129 */       this.docKeyList.add(curArticle.getKey());
/* 130 */       Document curDoc = new Document();
/* 131 */       curDoc.addParagraph(this.parser.parseParagraph(curArticle.getTitle()));
/* 132 */       curDoc.addParagraph(this.parser.parseParagraph(curArticle.getAbstract()));
/* 133 */       curDoc.addParagraph(this.parser.parseParagraph(curArticle.getBody()));
/* 134 */       Paragraph curParagraph = curDoc.getFirstParagraph();
/* 135 */       while (curParagraph != null) {
/* 136 */         Sentence curSent = curParagraph.getFirstSentence();
/* 137 */         while (curSent != null) {
/* 138 */           indexSentence(curSent);
/* 139 */           curSent = curSent.next;
/*     */         }
/* 141 */         curParagraph = curParagraph.next;
/*     */       }
/* 143 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 146 */       e.printStackTrace();
/* 147 */     }return false;
/*     */   }
/*     */ 
/*     */   private boolean indexSentence(Sentence sent)
/*     */   {
/*     */     try
/*     */     {
/* 156 */       if (sent.getWordNum() < 2) {
/* 157 */         return true;
/*     */       }
/*     */ 
/* 160 */       preprocessSentence(sent);
/*     */ 
/* 164 */       int num = this.pairGenerator.generate(sent);
/*     */       int sentIndex;
/* 166 */       if (num > 0)
/* 167 */         sentIndex = this.sentenceBase.addSentence(sent);
/*     */       else
/* 169 */         return true;
 
/* 170 */       for (int i = 0; i < num; i++) {
/* 171 */         WordPairStat curPair = this.pairGenerator.getWordPairs(i);
/* 172 */         curPair.setIndex(this.pairKeyList.add(curPair.getFirstWord(), curPair.getSecondWord()));
/* 173 */         this.wordpairStatList.add(curPair);
/* 174 */         for (int j = 1; j <= this.maxSpan; j++)
/*     */         {
/* 176 */           if (curPair.getFrequency(j) > 0)
/* 177 */             this.arrPairSentRightMatrix[(j - 1)].add(curPair.getIndex(), sentIndex, curPair.getFrequency(j));
/*     */         }
/* 179 */         for (int j = 1; j <= this.maxSpan; j++)
/*     */         {
/* 181 */           if (curPair.getFrequency(-j) > 0)
/* 182 */             this.arrPairSentLeftMatrix[(j - 1)].add(curPair.getIndex(), sentIndex, curPair.getFrequency(-j));
/*     */         }
/*     */       }
/* 185 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 188 */       e.printStackTrace();
/* 189 */     }return false;
/*     */   }
/*     */ 
/*     */   protected void preprocessSentence(Sentence sent)
/*     */   {
/* 201 */     if (this.tagger != null) this.tagger.tag(sent);
/*     */ 
/* 204 */     Word cur = sent.getFirstWord();
/* 205 */     while (cur != null) {
/* 206 */       if (cur.getPOSIndex() == 1) {
/* 207 */         if (this.lemmatiser != null)
/* 208 */           cur.setLemma(this.lemmatiser.lemmatize(cur.getContent(), 1));
/*     */         else
/* 210 */           cur.setLemma(cur.getContent().toLowerCase());
/*     */       }
/* 212 */       else cur.setLemma(cur.getContent().toLowerCase());
/* 213 */       cur.setIndex(this.wordKeyList.add(cur.getLemma()));
/* 214 */       cur = cur.next;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.xtract.EngWordPairIndexer
 * JD-Core Version:    0.6.2
 */