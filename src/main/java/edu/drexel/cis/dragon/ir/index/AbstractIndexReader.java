/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.IntCell;
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public abstract class AbstractIndexReader
/*     */   implements IndexReader
/*     */ {
/*     */   protected CollectionReader collectionReader;
/*     */   protected SimpleElementList termKeyList;
/*     */   protected IRTermIndexList termIndexList;
/*     */   protected IRRelationIndexList relationIndexList;
/*     */   protected SimpleElementList docKeyList;
/*     */   protected IRDocIndexList docIndexList;
/*     */   protected IntSparseMatrix termdocMatrix;
/*     */   protected IntSparseMatrix doctermMatrix;
/*     */   protected IntSparseMatrix relationdocMatrix;
/*     */   protected IntSparseMatrix docrelationMatrix;
/*     */   protected boolean relationSupported;
/*     */   protected boolean initialized;
/*     */   protected IRCollection collection;
/*     */ 
/*     */   public AbstractIndexReader(boolean relationSupported)
/*     */   {
/*  33 */     this(relationSupported, null);
/*     */   }
/*     */ 
/*     */   public AbstractIndexReader(boolean relationSupported, CollectionReader collectionReader) {
/*  37 */     this.relationSupported = relationSupported;
/*  38 */     this.collectionReader = collectionReader;
/*     */   }
/*     */ 
/*     */   public IntSparseMatrix getDocTermMatrix() {
/*  42 */     return this.doctermMatrix;
/*     */   }
/*     */ 
/*     */   public IntSparseMatrix getTermDocMatrix() {
/*  46 */     return this.termdocMatrix;
/*     */   }
/*     */ 
/*     */   public IntSparseMatrix getDocRelationMatrix() {
/*  50 */     return this.docrelationMatrix;
/*     */   }
/*     */ 
/*     */   public IntSparseMatrix getRelaitonDocMatrix() {
/*  54 */     return this.relationdocMatrix;
/*     */   }
/*     */ 
/*     */   public void close() {
/*  58 */     this.termIndexList.close();
/*  59 */     this.docIndexList.close();
/*  60 */     this.termdocMatrix.close();
/*  61 */     this.doctermMatrix.close();
/*  62 */     if (this.relationSupported) {
/*  63 */       this.relationdocMatrix.close();
/*  64 */       this.docrelationMatrix.close();
/*  65 */       this.relationIndexList.close();
/*     */     }
/*  67 */     this.initialized = false;
/*     */   }
/*     */ 
/*     */   public boolean isRelationSupported() {
/*  71 */     return this.relationSupported;
/*     */   }
/*     */ 
/*     */   public void setIRDocKeyList(SimpleElementList keyList) {
/*  75 */     this.docKeyList = keyList;
/*     */   }
/*     */ 
/*     */   public void setIRTermKeyList(SimpleElementList keyList) {
/*  79 */     this.termKeyList = keyList;
/*     */   }
/*     */ 
/*     */   public IRTerm getIRTerm(int index) {
/*  83 */     return this.termIndexList.get(index);
/*     */   }
/*     */ 
/*     */   public String getTermKey(int index) {
/*  87 */     return this.termKeyList.search(index);
/*     */   }
/*     */ 
/*     */   public IRTerm getIRTerm(String key)
/*     */   {
/*  94 */     int pos = this.termKeyList.search(key);
/*  95 */     if (pos >= 0) {
/*  96 */       IRTerm cur = this.termIndexList.get(pos);
/*  97 */       cur.setKey(key);
/*  98 */       return cur;
/*     */     }
/*     */ 
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   public IRTerm getIRTerm(int termIndex, int docIndex)
/*     */   {
/* 108 */     IntCell cell = (IntCell)this.termdocMatrix.getCell(termIndex, docIndex);
/* 109 */     if (cell == null) {
/* 110 */       return null;
/*     */     }
/* 112 */     return new IRTerm(termIndex, cell.getIntScore());
/*     */   }
/*     */ 
/*     */   public IRTerm[] getTermList(int docIndex)
/*     */   {
/* 120 */     int len = this.doctermMatrix.getNonZeroNumInRow(docIndex);
/* 121 */     IRTerm[] arrTerm = new IRTerm[len];
/* 122 */     for (int i = 0; i < len; i++) {
/* 123 */       IntCell curCell = (IntCell)this.doctermMatrix.getNonZeroCellInRow(docIndex, i);
/* 124 */       arrTerm[i] = new IRTerm(curCell.getColumn(), curCell.getIntScore());
/*     */     }
/* 126 */     return arrTerm;
/*     */   }
/*     */ 
/*     */   public int[] getTermFrequencyList(int docIndex) {
/* 130 */     return this.doctermMatrix.getNonZeroIntScoresInRow(docIndex);
/*     */   }
/*     */ 
/*     */   public int[] getTermIndexList(int docIndex) {
/* 134 */     return this.doctermMatrix.getNonZeroColumnsInRow(docIndex);
/*     */   }
/*     */ 
/*     */   public IRRelation getIRRelation(int index) {
/* 138 */     return this.relationIndexList.get(index);
/*     */   }
/*     */ 
/*     */   public IRRelation getIRRelation(int relationIndex, int docIndex)
/*     */   {
/* 144 */     IntCell cell = (IntCell)this.docrelationMatrix.getCell(docIndex, relationIndex);
/* 145 */     return new IRRelation(relationIndex, -1, -1, cell.getIntScore(), 0);
/*     */   }
/*     */ 
/*     */   public int[] getRelationIndexList(int docIndex) {
/* 149 */     return this.docrelationMatrix.getNonZeroColumnsInRow(docIndex);
/*     */   }
/*     */ 
/*     */   public int[] getRelationFrequencyList(int docIndex) {
/* 153 */     return this.docrelationMatrix.getNonZeroIntScoresInRow(docIndex);
/*     */   }
/*     */ 
/*     */   public IRRelation[] getRelationList(int docIndex)
/*     */   {
/* 161 */     int len = this.docrelationMatrix.getNonZeroNumInRow(docIndex);
/* 162 */     IRRelation[] arrRelation = new IRRelation[len];
/* 163 */     for (int i = 0; i < len; i++) {
/* 164 */       IntCell cell = (IntCell)this.docrelationMatrix.getNonZeroCellInRow(docIndex, i);
/* 165 */       arrRelation[i] = new IRRelation(cell.getColumn(), -1, -1, cell.getIntScore(), 0);
/*     */     }
/* 167 */     return arrRelation;
/*     */   }
/*     */ 
/*     */   public IRCollection getCollection() {
/* 171 */     return this.collection;
/*     */   }
/*     */ 
/*     */   public IRDoc getDoc(int index) {
/* 175 */     return this.docIndexList.get(index).copy();
/*     */   }
/*     */ 
/*     */   public String getDocKey(int index) {
/* 179 */     return this.docKeyList.search(index);
/*     */   }
/*     */ 
/*     */   public IRDoc getDoc(String key)
/*     */   {
/* 186 */     int index = this.docKeyList.search(key);
/* 187 */     if (index >= 0) {
/* 188 */       IRDoc cur = getDoc(index);
/* 189 */       cur.setKey(key);
/* 190 */       return cur;
/*     */     }
/*     */ 
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */   public Article getOriginalDoc(String key) {
/* 197 */     if (this.collectionReader == null) {
/* 198 */       System.out.println("Collection Reader is not set yet!");
/* 199 */       return null;
/*     */     }
/*     */ 
/* 202 */     return this.collectionReader.getArticleByKey(key);
/*     */   }
/*     */ 
/*     */   public Article getOriginalDoc(int index)
/*     */   {
/* 207 */     return getOriginalDoc(getDocKey(index));
/*     */   }
/*     */ 
/*     */   public int[] getTermDocIndexList(int termIndex) {
/* 211 */     return this.termdocMatrix.getNonZeroColumnsInRow(termIndex);
/*     */   }
/*     */ 
/*     */   public IRDoc[] getTermDocList(int termIndex)
/*     */   {
/* 219 */     int[] arrDocIndex = getTermDocIndexList(termIndex);
/* 220 */     IRDoc[] arrDoc = new IRDoc[arrDocIndex.length];
/* 221 */     for (int i = 0; i < arrDocIndex.length; i++) {
/* 222 */       arrDoc[i] = getDoc(arrDocIndex[i]);
/*     */     }
/* 224 */     return arrDoc;
/*     */   }
/*     */ 
/*     */   public int[] getTermDocFrequencyList(int termIndex) {
/* 228 */     return this.termdocMatrix.getNonZeroIntScoresInRow(termIndex);
/*     */   }
/*     */ 
/*     */   public int[] getRelationDocFrequencyList(int relationIndex) {
/* 232 */     return this.relationdocMatrix.getNonZeroIntScoresInRow(relationIndex);
/*     */   }
/*     */ 
/*     */   public IRDoc[] getRelationDocList(int relationIndex)
/*     */   {
/* 240 */     int[] arrDocIndex = getRelationDocIndexList(relationIndex);
/* 241 */     IRDoc[] arrDoc = new IRDoc[arrDocIndex.length];
/* 242 */     for (int i = 0; i < arrDocIndex.length; i++) {
/* 243 */       arrDoc[i] = getDoc(arrDocIndex[i]);
/*     */     }
/* 245 */     return arrDoc;
/*     */   }
/*     */ 
/*     */   public int[] getRelationDocIndexList(int relationIndex)
/*     */   {
/* 250 */     return this.relationdocMatrix.getNonZeroColumnsInRow(relationIndex);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.AbstractIndexReader
 * JD-Core Version:    0.6.2
 */