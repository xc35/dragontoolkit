/*     */ package edu.drexel.cis.dragon.ir.search.evaluate;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.FormatUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ public class TrecEva
/*     */ {
/*     */   private String evaResultFolder;
/*     */   private double[] evaResult;
/*     */ 
/*     */   public TrecEva(String evaResultFolder)
/*     */   {
/*  25 */     this.evaResultFolder = evaResultFolder;
/*     */   }
/*     */ 
/*     */   public double[] evaluateQuery(IRQuery query, ArrayList hitlist, ArrayList relevantList) {
/*  29 */     return evaluateQuery(query, hitlist, relevantList, null);
/*     */   }
/*     */ 
/*     */   public double[] evaluateQuery(IRQuery query, ArrayList hitlist, ArrayList relevantList, IndexReader indexReader)
/*     */   {
/*     */     try
/*     */     {
/*  44 */       double top100Precision = 0.0D;
/*  45 */       double top100Recall = 0.0D;
/*  46 */       double top10Precision = 0.0D;
/*  47 */       double top10Recall = 0.0D;
/*  48 */       double avgPrecision = 0.0D;
/*  49 */       double overallPrecision = 0.0D;
/*  50 */       double overallRecall = 0.0D;
/*  51 */       double[] result = new double[10];
/*     */ 
/*  53 */       if ((relevantList == null) || (relevantList.size() == 0) || (hitlist == null) || (hitlist.size() == 0)) return result;
/*     */ 
/*  55 */       DecimalFormat df = FormatUtil.getNumericFormat(2, 2);
/*  56 */       DecimalFormat dfWeight = FormatUtil.getNumericFormat(2, 4);
/*  57 */       int topicID = query.getQueryKey();
/*  58 */       PrintWriter out1 = FileUtil.getPrintWriter(this.evaResultFolder + "/topic_" + topicID + ".eva");
/*  59 */       int base = relevantList.size();
/*  60 */       int retrieved = hitlist.size();
/*  61 */       if (retrieved > 1000) retrieved = 1000;
/*  62 */       int relevant = 0;
/*  63 */       Comparator indexComparator = new IndexComparator();
/*  64 */       Collections.sort(relevantList, indexComparator);
/*     */ 
/*  66 */       out1.write(query.toString() + "\n");
/*  67 */       out1.flush();
/*  68 */       for (int i = 0; i < retrieved; i++) {
/*  69 */         IRDoc curDoc = (IRDoc)hitlist.get(i);
/*  70 */         if (SortedArray.binarySearch(relevantList, curDoc, indexComparator) >= 0) {
/*  71 */           relevant++;
/*  72 */           avgPrecision += 100.0D * relevant / (i + 1);
/*     */         }
/*  74 */         if (i == 9) {
/*  75 */           top10Precision = 100.0D * relevant / (i + 1);
/*  76 */           top10Recall = 100.0D * relevant / base;
/*     */         }
/*  78 */         if (i == 99) {
/*  79 */           top100Precision = 100.0D * relevant / (i + 1);
/*  80 */           top100Recall = 100.0D * relevant / base;
/*     */         }
/*  82 */         if (indexReader == null)
/*  83 */           out1.write("Top " + (i + 1) + " #" + curDoc.getIndex() + "(" + relevant + "): ");
/*     */         else
/*  85 */           out1.write("Top " + (i + 1) + " #" + indexReader.getDocKey(curDoc.getIndex()) + "(#" + curDoc.getIndex() + ", " + relevant + "): ");
/*  86 */         out1.write(dfWeight.format(curDoc.getWeight()) + " " + df.format(100.0D * relevant / (i + 1)) + 
/*  87 */           "%/" + df.format(100.0D * relevant / base) + "%\r\n");
/*  88 */         out1.flush();
/*     */       }
/*  90 */       overallPrecision = 100.0D * relevant / retrieved;
/*  91 */       overallRecall = 100.0D * relevant / base;
/*  92 */       if (retrieved < 10) {
/*  93 */         top10Precision = overallPrecision;
/*  94 */         top10Recall = overallRecall;
/*     */       }
/*  96 */       if (retrieved < 100) {
/*  97 */         top100Precision = overallPrecision;
/*  98 */         top100Recall = overallRecall;
/*     */       }
/* 100 */       avgPrecision /= base;
/* 101 */       out1.write("Top 10 Precison/Recall:" + df.format(top10Precision) + "%/" + df.format(top10Recall) + "%\r\n");
/* 102 */       out1.write("Top 100 Precison/Recall:" + df.format(top100Precision) + "%/" + df.format(top100Recall) + 
/* 103 */         "%\r\n");
/* 104 */       out1.write("Overall Precison/Recall:" + df.format(overallPrecision) + "%/" + df.format(overallRecall) + 
/* 105 */         "%\r\n");
/* 106 */       out1.write("Average Precison:" + df.format(avgPrecision) + "%\r\n");
/* 107 */       out1.close();
/*     */ 
/* 109 */       result[0] = topicID;
/* 110 */       result[1] = retrieved;
/* 111 */       result[2] = relevant;
/* 112 */       result[3] = top10Precision;
/* 113 */       result[4] = top10Recall;
/* 114 */       result[5] = top100Precision;
/* 115 */       result[6] = top100Recall;
/* 116 */       result[7] = overallPrecision;
/* 117 */       result[8] = overallRecall;
/* 118 */       result[9] = avgPrecision;
/* 119 */       this.evaResult = new double[10];
/* 120 */       System.arraycopy(result, 0, this.evaResult, 0, 10);
/* 121 */       return result;
/*     */     }
/*     */     catch (Exception e) {
/* 124 */       e.printStackTrace();
/* 125 */     }return null;
/*     */   }
/*     */ 
/*     */   public int getTopicID()
/*     */   {
/* 130 */     return (int)this.evaResult[0];
/*     */   }
/*     */ 
/*     */   public int getRetrievedDocNum() {
/* 134 */     return (int)this.evaResult[1];
/*     */   }
/*     */ 
/*     */   public int getRelevantDocNum() {
/* 138 */     return (int)this.evaResult[2];
/*     */   }
/*     */ 
/*     */   public double getTop10Precision() {
/* 142 */     return this.evaResult[3];
/*     */   }
/*     */ 
/*     */   public double getTop10Recall() {
/* 146 */     return this.evaResult[4];
/*     */   }
/*     */ 
/*     */   public double getTop100Precision() {
/* 150 */     return this.evaResult[5];
/*     */   }
/*     */ 
/*     */   public double getTop100Recall() {
/* 154 */     return this.evaResult[6];
/*     */   }
/*     */ 
/*     */   public double getOverallPrecision() {
/* 158 */     return this.evaResult[7];
/*     */   }
/*     */ 
/*     */   public double getOverallRecall() {
/* 162 */     return this.evaResult[8];
/*     */   }
/*     */ 
/*     */   public double getAveragePrecision() {
/* 166 */     return this.evaResult[9];
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.evaluate.TrecEva
 * JD-Core Version:    0.6.2
 */