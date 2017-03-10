/*     */ package edu.drexel.cis.dragon.ir.search;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.RelSimpleQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*     */ import edu.drexel.cis.dragon.ir.search.smooth.Smoother;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public class FullRankSearcher extends AbstractSearcher
/*     */ {
/*     */   private boolean docFirst;
/*     */ 
/*     */   public FullRankSearcher(IndexReader indexReader, Smoother smoother)
/*     */   {
/*  22 */     super(indexReader, smoother);
/*  23 */     smoother.setLogLikelihoodOption(true);
/*  24 */     if (smoother.isQueryTermFirstOptimal())
/*  25 */       this.docFirst = false;
/*     */     else
/*  27 */       this.docFirst = true;
/*     */   }
/*     */ 
/*     */   public FullRankSearcher(IndexReader indexReader, Smoother smoother, boolean docFirst) {
/*  31 */     super(indexReader, smoother);
/*  32 */     smoother.setLogLikelihoodOption(true);
/*  33 */     this.docFirst = docFirst;
/*     */   }
/*     */ 
/*     */   public int search(IRQuery query) {
/*  37 */     this.query = query;
/*  38 */     if (this.docFirst) {
/*  39 */       return breadthFirstSearch(query);
/*     */     }
/*  41 */     return depthFirstSearch(query);
/*     */   }
/*     */ 
/*     */   public int breadthFirstSearch(IRQuery query)
/*     */   {
/*  51 */     if (!query.isRelSimpleQuery()) {
/*  52 */       this.hitlist = null;
/*  53 */       return 0;
/*     */     }
/*     */ 
/*  56 */     int docNum = this.indexReader.getCollection().getDocNum();
/*  57 */     this.hitlist = new ArrayList(docNum);
/*  58 */     SimpleTermPredicate[] arrPredicate = checkSimpleTermQuery((RelSimpleQuery)query);
/*  59 */     if ((arrPredicate == null) || (arrPredicate.length == 0)) {
/*  60 */       this.hitlist = null;
/*  61 */       return 0;
/*     */     }
/*     */ 
/*  64 */     for (int i = 0; i < docNum; i++) {
/*  65 */       IRDoc curDoc = this.indexReader.getDoc(i);
/*  66 */       this.smoother.setDoc(curDoc);
/*  67 */       double weight = 0.0D;
/*  68 */       for (int j = 0; j < arrPredicate.length; j++) {
/*  69 */         IRTerm curTerm = this.indexReader.getIRTerm(arrPredicate[j].getIndex(), i);
/*  70 */         if (curTerm != null)
/*  71 */           weight += this.smoother.getSmoothedProb(arrPredicate[j], curTerm.getFrequency());
/*     */         else
/*  73 */           weight += this.smoother.getSmoothedProb(arrPredicate[j]);
/*     */       }
/*  75 */       curDoc.setWeight(weight);
/*  76 */       this.hitlist.add(curDoc);
/*     */     }
/*  78 */     Collections.sort(this.hitlist, new WeightComparator(true));
/*  79 */     return this.hitlist.size();
/*     */   }
/*     */ 
/*     */   public int depthFirstSearch(IRQuery query)
/*     */   {
/*  88 */     if (!query.isRelSimpleQuery()) {
/*  89 */       this.hitlist = null;
/*  90 */       return 0;
/*     */     }
/*     */ 
/*  93 */     int docNum = this.indexReader.getCollection().getDocNum();
/*  94 */     this.hitlist = new ArrayList(docNum);
/*  95 */     SimpleTermPredicate[] arrPredicate = checkSimpleTermQuery((RelSimpleQuery)query);
/*  96 */     if ((arrPredicate == null) || (arrPredicate.length == 0)) {
/*  97 */       this.hitlist = null;
/*  98 */       return 0;
/*     */     }
/*     */ 
/* 101 */     IRDoc[] arrDoc = new IRDoc[docNum];
/* 102 */     for (int i = 0; i < docNum; i++) {
/* 103 */       arrDoc[i] = this.indexReader.getDoc(i);
/* 104 */       arrDoc[i].setWeight(0.0D);
/*     */     }
/*     */ 
/* 107 */     for (int i = 0; i < arrPredicate.length; i++) {
/* 108 */       this.smoother.setQueryTerm(arrPredicate[i]);
/*     */ 
/* 110 */       int[] arrIndex = this.indexReader.getTermDocIndexList(arrPredicate[i].getIndex());
/* 111 */       int[] arrFreq = this.indexReader.getTermDocFrequencyList(arrPredicate[i].getIndex());
/* 112 */       int k = 0;
/* 113 */       for (int j = 0; j < arrIndex.length; j++) {
/* 114 */         while (k < arrIndex[j]) {
/* 115 */           arrDoc[k].setWeight(arrDoc[k].getWeight() + this.smoother.getSmoothedProb(arrDoc[k]));
/* 116 */           k++;
/*     */         }
/* 118 */         arrDoc[k].setWeight(arrDoc[k].getWeight() + this.smoother.getSmoothedProb(arrDoc[k], arrFreq[j]));
/* 119 */         k++;
/*     */       }
/* 121 */       while (k < docNum) {
/* 122 */         arrDoc[k].setWeight(arrDoc[k].getWeight() + this.smoother.getSmoothedProb(arrDoc[k]));
/* 123 */         k++;
/*     */       }
/*     */     }
/*     */ 
/* 127 */     for (int i = 0; i < docNum; i++) this.hitlist.add(arrDoc[i]);
/* 128 */     Collections.sort(this.hitlist, new WeightComparator(true));
/* 129 */     return this.hitlist.size();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.FullRankSearcher
 * JD-Core Version:    0.6.2
 */