/*     */ package edu.drexel.cis.dragon.ir.index.sequence;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDocIndexList;
/*     */ import edu.drexel.cis.dragon.ir.index.IRRelation;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTermIndexList;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public abstract class AbstractSequenceIndexReader
/*     */   implements SequenceIndexReader
/*     */ {
/*     */   protected CollectionReader collectionReader;
/*     */   protected SimpleElementList termKeyList;
/*     */   protected SimpleElementList docKeyList;
/*     */   protected IRTermIndexList termIndexList;
/*     */   protected IRDocIndexList docIndexList;
/*     */   protected SequenceReader doctermSeq;
/*     */   protected IRCollection collection;
/*     */   protected boolean initialized;
/*     */ 
/*     */   public void close()
/*     */   {
/*  26 */     this.termIndexList.close();
/*  27 */     this.docIndexList.close();
/*  28 */     this.doctermSeq.close();
/*  29 */     this.initialized = false;
/*     */   }
/*     */ 
/*     */   public boolean isRelationSupported() {
/*  33 */     return false;
/*     */   }
/*     */ 
/*     */   public IRCollection getCollection() {
/*  37 */     return this.collection;
/*     */   }
/*     */ 
/*     */   public IRDoc getDoc(int index) {
/*  41 */     return this.docIndexList.get(index).copy();
/*     */   }
/*     */ 
/*     */   public String getDocKey(int index) {
/*  45 */     return this.docKeyList.search(index);
/*     */   }
/*     */ 
/*     */   public IRDoc getDoc(String key)
/*     */   {
/*  52 */     int index = this.docKeyList.search(key);
/*  53 */     if (index >= 0) {
/*  54 */       IRDoc cur = getDoc(index);
/*  55 */       cur.setKey(key);
/*  56 */       return cur;
/*     */     }
/*     */ 
/*  59 */     return null;
/*     */   }
/*     */ 
/*     */   public Article getOriginalDoc(String key) {
/*  63 */     if (this.collectionReader == null) {
/*  64 */       System.out.println("Collection Reader is not set yet!");
/*  65 */       return null;
/*     */     }
/*     */ 
/*  68 */     return this.collectionReader.getArticleByKey(key);
/*     */   }
/*     */ 
/*     */   public Article getOriginalDoc(int index) {
/*  72 */     return getOriginalDoc(getDocKey(index));
/*     */   }
/*     */ 
/*     */   public IRTerm[] getTermList(int docIndex)
/*     */   {
/*  79 */     int len = this.doctermSeq.getSequenceLength(docIndex);
/*  80 */     if (len == 0) {
/*  81 */       return null;
/*     */     }
/*  83 */     int[] arrIndex = getTermIndexList(docIndex);
/*  84 */     IRTerm[] arrTerm = new IRTerm[len];
/*  85 */     for (int i = 0; i < len; i++) {
/*  86 */       arrTerm[i] = new IRTerm(arrIndex[i], 1);
/*     */     }
/*  88 */     return arrTerm;
/*     */   }
/*     */ 
/*     */   public int[] getTermIndexList(int docIndex)
/*     */   {
/*  93 */     return this.doctermSeq.getSequence(docIndex);
/*     */   }
/*     */ 
/*     */   public int[] getTermFrequencyList(int docIndex)
/*     */   {
/*  99 */     int len = this.doctermSeq.getSequenceLength(docIndex);
/* 100 */     if (len == 0) {
/* 101 */       return null;
/*     */     }
/* 103 */     int[] arrFreq = new int[len];
/* 104 */     for (int i = 0; i < len; i++)
/* 105 */       arrFreq[i] = 1;
/* 106 */     return arrFreq;
/*     */   }
/*     */ 
/*     */   public IRTerm getIRTerm(int index)
/*     */   {
/* 111 */     return this.termIndexList.get(index);
/*     */   }
/*     */ 
/*     */   public String getTermKey(int index) {
/* 115 */     return this.termKeyList.search(index);
/*     */   }
/*     */ 
/*     */   public IRTerm getIRTerm(String key)
/*     */   {
/* 122 */     int pos = this.termKeyList.search(key);
/* 123 */     if (pos >= 0) {
/* 124 */       IRTerm cur = this.termIndexList.get(pos);
/* 125 */       cur.setKey(key);
/* 126 */       return cur;
/*     */     }
/*     */ 
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */   public IRTerm getIRTerm(int termIndex, int docIndex)
/*     */   {
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */   public IRDoc[] getTermDocList(int termIndex) {
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   public int[] getTermDocFrequencyList(int termIndex) {
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */   public int[] getTermDocIndexList(int termIndex) {
/* 146 */     return null;
/*     */   }
/*     */ 
/*     */   public IRRelation[] getRelationList(int docIndex) {
/* 150 */     return null;
/*     */   }
/*     */ 
/*     */   public int[] getRelationFrequencyList(int docIndex) {
/* 154 */     return null;
/*     */   }
/*     */ 
/*     */   public int[] getRelationIndexList(int docIndex) {
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */   public IRRelation getIRRelation(int index) {
/* 162 */     return null;
/*     */   }
/*     */ 
/*     */   public IRRelation getIRRelation(int relationIndex, int docIndex) {
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */   public IRDoc[] getRelationDocList(int relationIndex) {
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */   public int[] getRelationDocFrequencyList(int relationIndex) {
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   public int[] getRelationDocIndexList(int relationIndex) {
/* 178 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.AbstractSequenceIndexReader
 * JD-Core Version:    0.6.2
 */