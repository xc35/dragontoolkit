/*     */ package edu.drexel.cis.dragon.ir.summarize;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.DocCluster;
/*     */ import edu.drexel.cis.dragon.ir.clustering.DocClusterSet;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public abstract class AbstractSentenceSum
/*     */ {
/*     */   protected String buildSummary(IndexReader indexReader, ArrayList sentSet, int summaryLength, DoubleVector weightVector)
/*     */   {
/*  31 */     int curLength = 0;
/*  32 */     TopicSummary summary = new TopicSummary(3);
/*  33 */     ArrayList list = new ArrayList(sentSet.size());
/*  34 */     for (int i = 0; i < sentSet.size(); i++) {
/*  35 */       IRDoc curDoc = (IRDoc)sentSet.get(i);
/*  36 */       curDoc.setWeight(weightVector.get(i));
/*  37 */       list.add(curDoc);
/*     */     }
/*  39 */     Collections.sort(list, new WeightComparator(true));
/*     */       int i;
/*  41 */     for (i = 0; (i < list.size()) && (curLength < summaryLength); i++) {
/*  42 */       IRDoc curDoc = (IRDoc)list.get(i);
/*  43 */       Article article = indexReader.getOriginalDoc(curDoc.getIndex());
/*     */       String curSentence;
/*  44 */       if ((article != null) && ((curSentence = article.getTitle()) != null))
/*     */       {
/*     */          
/*  46 */         if (!summary.contains(new TextUnit(curSentence)))
/*     */         {
/*  48 */           if (curLength < summaryLength) {
/*  49 */             summary.addText(new TextUnit(curSentence, curDoc.getIndex(), curDoc.getWeight()));
/*  50 */             curLength += curSentence.length();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  53 */     summary.sortByWegiht();
/*  54 */     if (summary.size() == 0) {
/*  55 */       return null;
/*     */     }
/*  57 */     StringBuffer autoSum = new StringBuffer(summary.getTextUnit(0).getText());
/*  58 */     for (i = 1; i < summary.size(); i++) {
/*  59 */       autoSum.append("\n");
/*  60 */       autoSum.append(summary.getTextUnit(i).getText());
/*     */     }
/*  62 */     if (autoSum.length() <= summaryLength) {
/*  63 */       return autoSum.toString();
/*     */     }
/*  65 */     return autoSum.substring(0, summaryLength);
/*     */   }
/*     */ 
/*     */   protected String buildSummary(IndexReader indexReader, ArrayList sentSet, int summaryLength, DoubleVector weightVector, DocClusterSet clusters)
/*     */   {int i;
/*  81 */     SortedArray list = new SortedArray(sentSet.size(), new IndexComparator());
/*  82 */     for ( i = 0; i < sentSet.size(); i++) {
/*  83 */       IRDoc curDoc = (IRDoc)sentSet.get(i);
/*  84 */       curDoc.setWeight(weightVector.get(i));
/*  85 */       list.add(curDoc);
/*     */     }
/*     */     
/*  88 */     for (i = 0; i < clusters.getClusterNum(); i++) {
/*  89 */       DocCluster curCluster = clusters.getDocCluster(i);
/*  90 */       for (int j = 0; j < curCluster.getDocNum(); j++) {
/*  91 */         IRDoc curDoc = curCluster.getDoc(j);
/*  92 */         int pos = list.binarySearch(curDoc);
/*  93 */         if (pos >= 0)
/*     */         {
/*  95 */           curDoc = (IRDoc)list.get(pos);
/*  96 */           curDoc.setCategory(i);
/*     */         }
/*     */       }
/*     */     }
/* 100 */     list.setComparator(new WeightComparator(true));
/* 101 */     boolean[] usedDoc = new boolean[list.size()];
/* 102 */     boolean[] usedCluster = new boolean[clusters.getClusterNum()];
/* 103 */     int curLength = 0;
/* 104 */     TopicSummary summary = new TopicSummary(3);
/*     */ 
/* 107 */     for (i = 0; (i < list.size()) && (curLength < summaryLength); i++) {
/* 108 */       IRDoc curDoc = (IRDoc)list.get(i);
/* 109 */       if (usedCluster[curDoc.getCategory()] == false)
/*     */       {
/* 111 */         Article article = indexReader.getOriginalDoc(curDoc.getIndex());
/*     */         String curSentence;
/* 112 */         if ((article != null) && ((curSentence = article.getTitle()) != null))
/*     */         {
 
/* 114 */           if (!summary.contains(new TextUnit(curSentence)))
/*     */           {
/* 116 */             if (curLength < summaryLength) {
/* 117 */               summary.addText(new TextUnit(curSentence, curDoc.getIndex(), curDoc.getWeight()));
/* 118 */               curLength += curSentence.length();
/* 119 */               usedCluster[curDoc.getCategory()] = true;
/* 120 */               usedDoc[i] = true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 125 */     for (i = 0; (i < list.size()) && (curLength < summaryLength); i++)
/* 126 */       if (usedDoc[i] == false)
/*     */       {
/* 128 */         IRDoc curDoc = (IRDoc)list.get(i);
/* 129 */         Article article = indexReader.getOriginalDoc(curDoc.getIndex());
/*     */         String curSentence;
/* 130 */         if ((article != null) && ((curSentence = article.getTitle()) != null))
/*     */         {
/* 132 */           if (!summary.contains(new TextUnit(curSentence)))
/*     */           {
/* 134 */             if (curLength < summaryLength) {
/* 135 */               summary.addText(new TextUnit(curSentence, curDoc.getIndex(), curDoc.getWeight()));
/* 136 */               curLength += curSentence.length();
/* 137 */               usedDoc[i] = true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 141 */     summary.sortByWegiht();
/* 142 */     if (summary.size() == 0) {
/* 143 */       return null;
/*     */     }
/* 145 */     StringBuffer autoSum = new StringBuffer(summary.getTextUnit(0).getText());
/* 146 */     for (i = 1; i < summary.size(); i++) {
/* 147 */       autoSum.append("\n");
/* 148 */       autoSum.append(summary.getTextUnit(i).getText());
/*     */     }
/* 150 */     if (autoSum.length() <= summaryLength) {
/* 151 */       return autoSum.toString();
/*     */     }
/* 153 */     return autoSum.substring(0, summaryLength);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.summarize.AbstractSentenceSum
 * JD-Core Version:    0.6.2
 */