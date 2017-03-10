/*     */ package edu.drexel.cis.dragon.ir.search;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.RelSimpleQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*     */ import edu.drexel.cis.dragon.ir.search.smooth.Smoother;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public class PartialRankSearcher extends AbstractSearcher
/*     */ {
/*     */   public PartialRankSearcher(IndexReader indexReader, Smoother smoother)
/*     */   {
/*  20 */     super(indexReader, smoother);
/*     */   }
/*     */ 
/*     */   public int search(IRQuery query)
/*     */   {
/*  28 */     if (!query.isRelSimpleQuery()) {
/*  29 */       this.hitlist = null;
/*  30 */       return 0;
/*     */     }
/*     */ 
/*  33 */     this.query = query;
/*  34 */     ArrayList queryList = new ArrayList();
/*  35 */     SimpleTermPredicate[] arrPredicate = checkSimpleTermQuery((RelSimpleQuery)query);
/*  36 */     if ((arrPredicate == null) || (arrPredicate.length == 0)) {
/*  37 */       this.hitlist = null;
/*  38 */       return 0;
/*     */     }
/*     */ 
/*  41 */     this.hitlist = addDocList(new ArrayList(), queryList, getHitList(arrPredicate[0]), arrPredicate[0]);
/*  42 */     queryList.add(arrPredicate[0]);
/*     */ 
/*  44 */     for (int i = 1; i < arrPredicate.length; i++) {
/*  45 */       this.hitlist = addDocList(this.hitlist, queryList, getHitList(arrPredicate[i]), arrPredicate[i]);
/*  46 */       queryList.add(arrPredicate[i]);
/*     */     }
/*  48 */     Collections.sort(this.hitlist, new WeightComparator(true));
/*  49 */     return this.hitlist.size();
/*     */   }
/*     */ 
/*     */   private IRDoc[] getHitList(SimpleTermPredicate predicate)
/*     */   {
/*     */     try
/*     */     {
/*  58 */       if (predicate == null) return null;
/*     */ 
/*  60 */       IRDoc[] arrDoc = this.indexReader.getTermDocList(predicate.getIndex());
/*  61 */       int[] arrFreq = this.indexReader.getTermDocFrequencyList(predicate.getIndex());
/*  62 */       this.smoother.setQueryTerm(predicate);
/*     */ 
/*  64 */       for (int i = 0; i < arrDoc.length; i++) {
/*  65 */         arrDoc[i].setWeight(this.smoother.getSmoothedProb(arrDoc[i], arrFreq[i]));
/*     */       }
/*  67 */       return arrDoc;
/*     */     }
/*     */     catch (Exception e) {
/*  70 */       e.printStackTrace();
/*  71 */     }return null;
/*     */   }
/*     */ 
/*     */   private ArrayList addDocList(ArrayList hisDocList, ArrayList hisQueryList, IRDoc[] curDocList, SimpleTermPredicate curPredicate)
/*     */   {
/*  80 */     if (curDocList == null) return hisDocList;
/*     */ 
/*  82 */     int i = 0;
/*  83 */     int j = 0;
/*  84 */     ArrayList newList = new ArrayList();
/*     */ 
/*  86 */     while ((i < hisDocList.size()) && (j < curDocList.length)) {
/*  87 */       IRDoc hisDoc = (IRDoc)hisDocList.get(i);
/*  88 */       if (hisDoc.getIndex() == curDocList[j].getIndex())
/*     */       {
/*  90 */         hisDoc.setWeight(hisDoc.getWeight() + curDocList[j].getWeight());
/*  91 */         newList.add(hisDoc);
/*  92 */         i++;
/*  93 */         j++;
/*     */       }
/*  95 */       else if (hisDoc.getIndex() < curDocList[j].getIndex())
/*     */       {
/*  97 */         hisDoc.setWeight(hisDoc.getWeight() + this.smoother.getSmoothedProb(hisDoc, curPredicate));
/*  98 */         newList.add(hisDoc);
/*  99 */         i++;
/*     */       }
/*     */       else
/*     */       {
/* 103 */         curDocList[j].setWeight(curDocList[j].getWeight() + computeWeight(curDocList[j], hisQueryList));
/* 104 */         newList.add(curDocList[j]);
/* 105 */         j++;
/*     */       }
/*     */     }
/*     */ 
/* 109 */     if (j < curDocList.length)
/*     */     {
/* 111 */       for (int k = j; k < curDocList.length; k++) {
/* 112 */         curDocList[k].setWeight(curDocList[k].getWeight() + computeWeight(curDocList[k], hisQueryList));
/* 113 */         newList.add(curDocList[k]);
/*     */       }
/*     */     }
/*     */ 
/* 117 */     if (i < hisDocList.size())
/*     */     {
/* 119 */       for (int k = i; k < hisDocList.size(); k++) {
/* 120 */         IRDoc hisDoc = (IRDoc)hisDocList.get(k);
/* 121 */         hisDoc.setWeight(hisDoc.getWeight() + this.smoother.getSmoothedProb(hisDoc, curPredicate));
/* 122 */         newList.add(hisDoc);
/*     */       }
/*     */     }
/* 125 */     return newList;
/*     */   }
/*     */ 
/*     */   private double computeWeight(IRDoc doc, ArrayList hisQueryList)
/*     */   {
/* 132 */     double weight = 0.0D;
/* 133 */     this.smoother.setDoc(doc);
/* 134 */     for (int i = 0; i < hisQueryList.size(); i++) {
/* 135 */       weight += this.smoother.getSmoothedProb((SimpleTermPredicate)hisQueryList.get(i));
/*     */     }
/* 137 */     return weight;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.PartialRankSearcher
 * JD-Core Version:    0.6.2
 */