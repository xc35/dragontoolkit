/*     */ package edu.drexel.cis.dragon.ir.search;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.Operator;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*     */ import edu.drexel.cis.dragon.ir.search.smooth.Smoother;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public class BoolRankSearcher extends AbstractSearcher
/*     */ {
/*     */   public BoolRankSearcher(IndexReader indexReader, Smoother smoother)
/*     */   {
/*  20 */     super(indexReader, smoother);
/*     */   }
/*     */ 
/*     */   public int search(IRQuery query) {
/*  24 */     if (!query.isRelBoolQuery()) {
/*  25 */       this.hitlist = null;
/*  26 */       return 0;
/*     */     }
/*     */ 
/*  29 */     this.query = query;
/*  30 */     this.hitlist = getHitList(query);
/*  31 */     Collections.sort(this.hitlist, new WeightComparator(true));
/*  32 */     return this.hitlist.size();
/*     */   }
/*     */ 
/*     */   private ArrayList getHitList(IRQuery query)
/*     */   {
/*     */     ArrayList list;
/*  39 */     if (query.isPredicate())
/*     */     {
/*  41 */       if (checkSimpleTermPredicate(query))
/*  42 */         list = getDocList((SimpleTermPredicate)query);
/*     */       else
/*  44 */         list = null;
/*     */     }
/*     */     else
/*     */     {
/*  48 */       list = getHitList(query.getChild(0));
/*  49 */       for (int i = 1; i < query.getChildNum(); i++) {
/*  50 */         if (query.getOperator().toString().equalsIgnoreCase("AND"))
/*  51 */           list = addDocList(list, getHitList(query.getChild(i)), true);
/*     */         else
/*  53 */           list = addDocList(list, getHitList(query.getChild(i)), false);
/*     */       }
/*     */     }
/*  56 */     return list;
/*     */   }
/*     */ 
/*     */   private ArrayList getDocList(SimpleTermPredicate predicate)
/*     */   {
/*     */     try
/*     */     {
/*  66 */       IRDoc[] arrDoc = this.indexReader.getTermDocList(predicate.getIndex());
/*  67 */       ArrayList list = new ArrayList(arrDoc.length);
/*  68 */       int[] arrFreq = this.indexReader.getTermDocFrequencyList(predicate.getIndex());
/*  69 */       this.smoother.setQueryTerm(predicate);
/*  70 */       for (int i = 0; i < arrDoc.length; i++) {
/*  71 */         arrDoc[i].setWeight(this.smoother.getSmoothedProb(arrDoc[i], arrFreq[i]));
/*  72 */         list.add(arrDoc[i]);
/*     */       }
/*  74 */       return list;
/*     */     }
/*     */     catch (Exception e) {
/*  77 */       e.printStackTrace();
/*  78 */     }return null;
/*     */   }
/*     */ 
/*     */   private ArrayList addDocList(ArrayList hisDocList, ArrayList curDocList, boolean interaction)
/*     */   {
/*  87 */     int i = 0;
/*  88 */     int j = 0;
/*  89 */     ArrayList newList = new ArrayList();
/*  90 */     while ((i < hisDocList.size()) && (j < curDocList.size())) {
/*  91 */       IRDoc hisDoc = (IRDoc)hisDocList.get(i);
/*  92 */       IRDoc curDoc = (IRDoc)curDocList.get(j);
/*  93 */       if (hisDoc.getIndex() == curDoc.getIndex())
/*     */       {
/*  95 */         hisDoc.setWeight(hisDoc.getWeight() + curDoc.getWeight());
/*  96 */         newList.add(hisDoc);
/*  97 */         i++;
/*  98 */         j++;
/*     */       }
/* 100 */       else if (hisDoc.getIndex() < curDoc.getIndex())
/*     */       {
/* 102 */         if (!interaction) newList.add(hisDoc);
/* 103 */         i++;
/*     */       }
/*     */       else
/*     */       {
/* 107 */         if (!interaction) newList.add(curDoc);
/* 108 */         j++;
/*     */       }
/*     */     }
/*     */ 
/* 112 */     if ((j < curDocList.size()) && (!interaction))
/*     */     {
/* 114 */       for (int k = j; k < curDocList.size(); k++) newList.add(curDocList.get(k));
/*     */     }
/*     */ 
/* 117 */     if ((i < hisDocList.size()) && (!interaction))
/*     */     {
/* 119 */       for (int k = i; k < hisDocList.size(); k++) newList.add(hisDocList.get(k));
/*     */     }
/*     */ 
/* 122 */     return newList;
/*     */   }
/*     */ 
/*     */   private boolean checkSimpleTermPredicate(IRQuery query)
/*     */   {
/* 129 */     SimpleTermPredicate predicate = (SimpleTermPredicate)query;
/* 130 */     if (predicate.getDocFrequency() <= 0)
/*     */     {
/* 132 */       IRTerm curIRTerm = this.indexReader.getIRTerm(predicate.getKey());
/* 133 */       if (curIRTerm != null) {
/* 134 */         predicate.setDocFrequency(curIRTerm.getDocFrequency());
/* 135 */         predicate.setFrequency(curIRTerm.getFrequency());
/* 136 */         predicate.setIndex(curIRTerm.getIndex());
/*     */       }
/*     */     }
/* 139 */     if (predicate.getDocFrequency() <= 0) {
/* 140 */       return false;
/*     */     }
/* 142 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.BoolRankSearcher
 * JD-Core Version:    0.6.2
 */