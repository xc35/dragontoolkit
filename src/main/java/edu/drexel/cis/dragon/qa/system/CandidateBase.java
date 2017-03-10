/*     */ package edu.drexel.cis.dragon.qa.system;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.DoubleFlatSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntFlatSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.Document;
/*     */ import edu.drexel.cis.dragon.nlp.DocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.Paragraph;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class CandidateBase
/*     */ {
/*     */   private DoubleFlatSparseMatrix cansentMatrix;
/*     */   private IntFlatSparseMatrix querysentMatrix;
/*     */   private ArrayList docList;
/*     */   private ArrayList sentList;
/*     */   private ArrayList docKeyList;
/*     */   private int totalCount;
/*     */   private int[] arrQueryCount;
/*     */   private int sentFiltered;
/*     */ 
/*     */   public CandidateBase(QuestionQuery query, DocumentParser parser, CollectionReader reader)
/*     */   {
/*  18 */     this(query, parser, reader, 2147483647);
/*     */   }
/*     */ 
/*     */   public CandidateBase(QuestionQuery query, DocumentParser parser, CollectionReader reader, int top)
/*     */   {
/*  30 */     this.sentFiltered = 0;
/*  31 */     this.totalCount = 0;
/*  32 */     this.docList = new ArrayList();
/*  33 */     this.docKeyList = new ArrayList();
/*  34 */     this.arrQueryCount = new int[query.size()];
/*     */     Article article;
/*  36 */     while (((article = reader.getNextArticle()) != null) && (this.docList.size() < top))
/*     */     {
/*  37 */       StringBuffer buf = new StringBuffer();
/*  38 */       if (article.getTitle() != null) {
/*  39 */         buf.append(article.getTitle());
/*  40 */         buf.append("\n\n");
/*     */       }
/*  42 */       if (article.getAbstract() != null) {
/*  43 */         buf.append(article.getAbstract());
/*  44 */         buf.append("\n\n");
/*     */       }
/*  46 */       if (article.getBody() != null) {
/*  47 */         buf.append(article.getBody());
/*     */       }
/*  49 */       String content = buf.toString();
/*  50 */       content = content.replaceAll("\\.\\.\\.", "\n\n");
/*  51 */       content = content.replaceAll("�", " - ");
/*  52 */       Document doc = parser.parse(content);
/*  53 */       if (doc != null) {
/*  54 */         this.docList.add(doc);
/*  55 */         this.docKeyList.add(article.getKey());
/*     */       }
/*     */     }
/*     */ 
/*  59 */     this.sentList = new ArrayList();
/*  60 */     int sentNum = 0;
/*  61 */     for (int i = 0; i < this.docList.size(); i++) {
/*  62 */       Document doc = getDocument(i);
/*  63 */       doc.setIndex(i);
/*  64 */       Paragraph para = doc.getFirstParagraph();
/*  65 */       while (para != null) {
/*  66 */         mergeShortSentence(para);
/*  67 */         Sentence sent = para.getFirstSentence();
/*  68 */         while (sent != null) {
/*  69 */           if (sent.getWordNum() > 2) {
/*  70 */             sent.setIndex(sentNum);
/*  71 */             sentNum++;
/*  72 */             this.sentList.add(sent);
/*     */           }
/*  74 */           sent = sent.next;
/*     */         }
/*  76 */         para = para.next;
/*     */       }
/*     */     }
/*     */ 
/*  80 */     this.cansentMatrix = new DoubleFlatSparseMatrix();
/*  81 */     this.querysentMatrix = new IntFlatSparseMatrix();
/*     */   }
/*     */ 
/*     */   public int getCollectionCount() {
/*  85 */     return this.totalCount;
/*     */   }
/*     */ 
/*     */   public void addCollectionCount(int inc) {
/*  89 */     this.totalCount += inc;
/*     */   }
/*     */ 
/*     */   public int getSentenceFiltered() {
/*  93 */     return this.sentFiltered;
/*     */   }
/*     */ 
/*     */   public void addSentenceFiltered(int inc) {
/*  97 */     this.sentFiltered += inc;
/*     */   }
/*     */ 
/*     */   public int getQueryWordCount(int queryWordIndex) {
/* 101 */     return this.arrQueryCount[queryWordIndex];
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 105 */     this.totalCount = 0;
/* 106 */     this.sentFiltered = 0;
/* 107 */     MathUtil.initArray(this.arrQueryCount, 0);
/* 108 */     if (this.cansentMatrix != null)
/* 109 */       this.cansentMatrix.close();
/* 110 */     this.cansentMatrix = new DoubleFlatSparseMatrix();
/* 111 */     if (this.querysentMatrix != null)
/* 112 */       this.querysentMatrix.close();
/* 113 */     this.querysentMatrix = new IntFlatSparseMatrix();
/*     */   }
/*     */ 
/*     */   public void finalize() {
/* 117 */     if (this.cansentMatrix != null)
/* 118 */       this.cansentMatrix.finalizeData();
/* 119 */     if (this.querysentMatrix != null)
/* 120 */       this.querysentMatrix.finalizeData();
/*     */   }
/*     */ 
/*     */   public void close() {
/* 124 */     if (this.sentList != null)
/* 125 */       this.sentList.clear();
/* 126 */     if (this.docList != null)
/* 127 */       this.docList.clear();
/* 128 */     if (this.docKeyList != null)
/* 129 */       this.docKeyList.clear();
/* 130 */     if (this.cansentMatrix != null)
/* 131 */       this.cansentMatrix.close();
/* 132 */     if (this.querysentMatrix != null)
/* 133 */       this.querysentMatrix.close();
/*     */   }
/*     */ 
/*     */   public boolean addQueryWord(int wordIndex, int sentIndex, int freq) {
/* 137 */     this.arrQueryCount[wordIndex] += freq;
/* 138 */     return this.querysentMatrix.add(wordIndex, sentIndex, freq);
/*     */   }
/*     */ 
/*     */   public int[] getQuerySentences(int queryIndex) {
/* 142 */     return this.querysentMatrix.getNonZeroColumnsInRow(queryIndex);
/*     */   }
/*     */ 
/*     */   public int[] getQueryCounts(int queryIndex) {
/* 146 */     return this.querysentMatrix.getNonZeroIntScoresInRow(queryIndex);
/*     */   }
/*     */ 
/*     */   public boolean add(int candIndex, int sentIndex, double weight) {
/* 150 */     return this.cansentMatrix.add(candIndex, sentIndex, weight);
/*     */   }
/*     */ 
/*     */   public int[] getCandidateSentences(int candIndex) {
/* 154 */     return this.cansentMatrix.getNonZeroColumnsInRow(candIndex);
/*     */   }
/*     */ 
/*     */   public double[] getCandidateScores(int candIndex) {
/* 158 */     return this.cansentMatrix.getNonZeroDoubleScoresInRow(candIndex);
/*     */   }
/*     */ 
/*     */   public int getDocumentNum() {
/* 162 */     return this.docList.size();
/*     */   }
/*     */ 
/*     */   public Document getDocument(int index) {
/* 166 */     return (Document)this.docList.get(index);
/*     */   }
/*     */ 
/*     */   public String getDocumentURL(int index) {
/* 170 */     return (String)this.docKeyList.get(index);
/*     */   }
/*     */ 
/*     */   public int getSentenceNum() {
/* 174 */     return this.sentList.size();
/*     */   }
/*     */ 
/*     */   public Sentence getSentence(int index) {
/* 178 */     return (Sentence)this.sentList.get(index);
/*     */   }
/*     */ 
/*     */   protected void mergeShortSentence(Paragraph para)
/*     */   {
/* 186 */     boolean needMerge = false;
/* 187 */     Sentence prev = null;
/* 188 */     Sentence cur = para.getFirstSentence();
/* 189 */     while (cur != null)
/* 190 */       if (cur.getWordNum() == 0) {
/* 191 */         cur = cur.next;
/*     */       }
/*     */       else
/*     */       {
/* 195 */         if (needMerge) {
/* 196 */           Word curWord = new Word(";");
/* 197 */           curWord.setType(4);
/* 198 */           prev.getLastWord().next = curWord;
/* 199 */           curWord.prev = prev.getLastWord();
/* 200 */           curWord.next = cur.getFirstWord();
/* 201 */           cur.getFirstWord().prev = curWord;
/* 202 */           prev.setPunctuation(cur.getPunctuation());
/* 203 */           prev.resetBoundary(prev.getFirstWord(), cur.getLastWord());
/* 204 */           prev.next = cur.next;
/*     */         }
/*     */         else {
/* 207 */           prev = cur;
/*     */         }
/*     */ 
/* 210 */         needMerge = false;
/* 211 */         if (((cur.getPunctuation() == '.') || (cur.getPunctuation() == ';')) && (cur.getWordNum() < 6))
/* 212 */           needMerge = true;
/* 213 */         cur = cur.next;
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.system.CandidateBase
 * JD-Core Version:    0.6.2
 */