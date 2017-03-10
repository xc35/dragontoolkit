/*     */ package edu.drexel.cis.dragon.ir.kngbase;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRRelation;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleFlatSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleGiantSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntFlatSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntGiantSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.extract.TokenExtractor;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class HALSpace
/*     */   implements KnowledgeBase
/*     */ {
/*     */   private SimpleElementList termList;
/*     */   private TokenExtractor te;
/*     */   private int windowSize;
/*     */   private IntSparseMatrix cooccurMatrix;
/*     */   private DoubleSparseMatrix halMatrix;
/*     */   private boolean fileBasedMatrix;
/*     */   private SortedArray relationCache;
/*     */   private boolean showProgress;
/*     */   private boolean useExternalTokenIndex;
/*     */ 
/*     */   public HALSpace(TokenExtractor te, int windowSize)
/*     */   {
/*  32 */     this(new SimpleElementList(), te, windowSize);
/*     */   }
/*     */ 
/*     */   public HALSpace(SimpleElementList termList, TokenExtractor te, int windowSize) {
/*  36 */     this.termList = termList;
/*  37 */     this.useExternalTokenIndex = (termList.size() > 0);
/*  38 */     this.te = te;
/*  39 */     te.setFilteringOption(false);
/*  40 */     this.windowSize = windowSize;
/*  41 */     this.halMatrix = new DoubleFlatSparseMatrix();
/*  42 */     this.fileBasedMatrix = false;
/*  43 */     this.cooccurMatrix = new IntFlatSparseMatrix(true, true);
/*  44 */     this.relationCache = new SortedArray();
/*  45 */     this.showProgress = false;
/*     */   }
/*     */ 
/*     */   public HALSpace(SimpleElementList termList, TokenExtractor te, int windowSize, String indexFilename, String matrixFilename) {
/*  49 */     this.termList = termList;
/*  50 */     this.useExternalTokenIndex = (termList.size() > 0);
/*  51 */     this.te = te;
/*  52 */     te.setFilteringOption(false);
/*  53 */     this.windowSize = windowSize;
/*  54 */     this.halMatrix = new DoubleGiantSparseMatrix(indexFilename, matrixFilename, false, false);
/*  55 */     ((DoubleGiantSparseMatrix)this.halMatrix).setFlushInterval(2147483647);
/*  56 */     this.fileBasedMatrix = true;
/*  57 */     this.cooccurMatrix = new IntGiantSparseMatrix(indexFilename + ".tmp", matrixFilename + ".tmp", true, true);
/*  58 */     this.relationCache = new SortedArray();
/*  59 */     this.showProgress = false;
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix getKnowledgeMatrix() {
/*  63 */     return this.halMatrix;
/*     */   }
/*     */ 
/*     */   public SimpleElementList getRowKeyList() {
/*  67 */     return this.termList;
/*     */   }
/*     */ 
/*     */   public SimpleElementList getColumnKeyList() {
/*  71 */     return this.termList;
/*     */   }
/*     */ 
/*     */   public void setShowProgress(boolean option) {
/*  75 */     this.showProgress = option;
/*     */   }
/*     */ 
/*     */   public void add(ArrayList articleList)
/*     */   {
/*  81 */     for (int i = 0; i < articleList.size(); i++)
/*  82 */       addArticle((Article)articleList.get(i));
/*     */   }
/*     */ 
/*     */   public void add(CollectionReader collectionReader)
/*     */   {
/*  89 */     int count = 0;
/*  90 */     Article article = collectionReader.getNextArticle();
/*  91 */     while (article != null)
/*     */     {
/*  93 */       addArticle(article);
/*  94 */       count++;
/*  95 */       if ((this.showProgress) && (count % 10 == 0)) {
/*  96 */         System.out.println(new Date().toString() + " Processed Articles: " + count);
/*     */       }
/*  98 */       article = collectionReader.getNextArticle();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void finalizeData()
/*     */   {
/* 107 */     this.cooccurMatrix.finalizeData();
/* 108 */     int row = this.cooccurMatrix.rows();
/* 109 */     for (int i = 0; i < row; i++) {
/* 110 */       int[] arrCol = this.cooccurMatrix.getNonZeroColumnsInRow(i);
/* 111 */       int[] arrFreq = this.cooccurMatrix.getNonZeroIntScoresInRow(i);
/* 112 */       int len = arrFreq.length;
/* 113 */       double sum = this.cooccurMatrix.getRowSum(i);
/* 114 */       double mean = sum / len;
/* 115 */       sum = 0.0D;
/* 116 */       for (int j = 0; j < len; j++) {
/* 117 */         if (arrFreq[j] >= mean) {
/* 118 */           sum += arrFreq[j];
/*     */         }
/*     */       }
/* 121 */       for (int j = 0; j < len; j++) {
/* 122 */         if (arrFreq[j] >= mean)
/* 123 */           this.halMatrix.add(i, arrCol[j], arrFreq[j] / sum);
/*     */       }
/* 125 */       if ((this.showProgress) && (i % 1000 == 0)) System.out.println("Processed Rows: " + i);
/* 126 */       if ((this.fileBasedMatrix) && (i % 5000 == 0)) ((DoubleGiantSparseMatrix)this.halMatrix).flush();
/*     */     }
/* 128 */     this.halMatrix.finalizeData();
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix getHALMatrix() {
/* 132 */     return this.halMatrix;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 137 */     this.halMatrix.close();
/* 138 */     this.cooccurMatrix.close();
/* 139 */     if (this.fileBasedMatrix) {
/* 140 */       new File(((IntGiantSparseMatrix)this.cooccurMatrix).getIndexFilename()).delete();
/* 141 */       new File(((IntGiantSparseMatrix)this.cooccurMatrix).getMatrixFilename()).delete();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addArticle(Article article)
/*     */   {
/* 153 */     StringBuffer sb = new StringBuffer();
/* 154 */     if (article.getTitle() != null) {
/* 155 */       sb.append(article.getTitle());
/* 156 */       sb.append(' ');
/*     */     }
/* 158 */     if (article.getAbstract() != null) {
/* 159 */       sb.append(article.getAbstract());
/* 160 */       sb.append(' ');
/*     */     }
/* 162 */     if (article.getBody() != null) {
/* 163 */       sb.append(article.getBody());
/* 164 */       sb.append(' ');
/*     */     }
/* 166 */     if (sb.length() <= 20) {
/* 167 */       return;
/*     */     }
/* 169 */     ArrayList tokenList = this.te.extractFromDoc(sb.toString().trim());
/* 170 */     if ((tokenList == null) || (tokenList.size() < this.windowSize)) {
/* 171 */       return;
/*     */     }
/* 173 */     Token[] arrToken = new Token[tokenList.size()];
/* 174 */     SortedArray cache = new SortedArray();
/* 175 */     for (int i = 0; i < tokenList.size(); i++) {
/* 176 */       arrToken[i] = ((Token)tokenList.get(i));
/* 177 */       int pos = cache.binarySearch(arrToken[i]);
/* 178 */       if (pos >= 0) {
/* 179 */         arrToken[i].setIndex(((Token)cache.get(pos)).getIndex());
/*     */       } else {
/* 181 */         arrToken[i].setIndex(tokenSearch(arrToken[i].getValue()));
/* 182 */         if (arrToken[i].getIndex() >= 0)
/* 183 */           cache.add(pos * -1 - 1, arrToken[i]);
/*     */       }
/*     */     }
/* 186 */     cache.clear();
/* 187 */     tokenList.clear();
/*     */ 
/* 189 */     for (int i = 0; i <= arrToken.length - this.windowSize; i++) {
/* 190 */       int first = arrToken[i].getIndex();
/* 191 */       if (first != -1)
/*     */       {
/* 193 */         for (int j = 1; j < this.windowSize; j++) {
/* 194 */           int second = arrToken[(i + j)].getIndex();
/* 195 */           if (second != -1) {
/* 196 */             addRelation(first, second, this.windowSize - j);
/* 197 */             addRelation(second, first, this.windowSize - j);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 202 */     for (int i = 0; i < this.relationCache.size(); i++) {
/* 203 */       IRRelation relation = (IRRelation)this.relationCache.get(i);
/* 204 */       this.cooccurMatrix.add(relation.getFirstTerm(), relation.getSecondTerm(), relation.getFrequency());
/* 205 */       this.cooccurMatrix.add(relation.getSecondTerm(), relation.getFirstTerm(), relation.getFrequency());
/*     */     }
/* 207 */     this.relationCache.clear();
/*     */   }
/*     */ 
/*     */   private int tokenSearch(String token) {
/* 211 */     if (this.useExternalTokenIndex) {
/* 212 */       return this.termList.search(token);
/*     */     }
/* 214 */     return this.termList.add(token);
/*     */   }
/*     */ 
/*     */   private boolean addRelation(int first, int second, int score)
/*     */   {
/* 220 */     IRRelation cur = new IRRelation(first, second, score);
/* 221 */     if (!this.relationCache.add(cur))
/*     */     {
/* 223 */       cur = (IRRelation)this.relationCache.get(this.relationCache.insertedPos());
/* 224 */       cur.addFrequency(score);
/*     */     }
/* 226 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.kngbase.HALSpace
 * JD-Core Version:    0.6.2
 */