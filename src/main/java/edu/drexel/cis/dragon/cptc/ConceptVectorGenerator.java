/*     */ package edu.drexel.cis.dragon.cptc;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleRow;
/*     */ import edu.drexel.cis.dragon.matrix.IntRow;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ 
/*     */ public class ConceptVectorGenerator
/*     */ {
/*     */   private IndexReader indexReader;
/*     */   private FeatureFilter filter;
/*     */   private SimpleElementList wordKeyList;
/*     */   private double[] idf;
/*     */   private double[] buf;
/*     */   private boolean useRanking;
/*     */   private int top;
/*     */ 
/*     */   public ConceptVectorGenerator(SimpleElementList wordKeyList, String idfFile)
/*     */   {
/*  18 */     this.idf = loadWordIDF(idfFile);
/*  19 */     this.buf = new double[wordKeyList.size()];
/*  20 */     this.wordKeyList = wordKeyList;
/*  21 */     this.top = 2147483647;
/*     */   }
/*     */ 
/*     */   public ConceptVectorGenerator(IndexReader indexReader, FeatureFilter filter)
/*     */   {
/*  27 */     this.filter = filter;
/*  28 */     this.indexReader = indexReader;
/*  29 */     this.buf = new double[filter.getSelectedFeatureNum()];
/*  30 */     this.idf = new double[filter.getSelectedFeatureNum()];
/*  31 */     int termNum = indexReader.getCollection().getTermNum();
/*  32 */     int docNum = indexReader.getCollection().getDocNum();
/*  33 */     for (int i = 0; i < termNum; i++) {
/*  34 */       int newIndex = filter.map(i);
/*  35 */       if (newIndex >= 0) {
/*  36 */         this.idf[newIndex] = Math.log(docNum / indexReader.getIRTerm(i).getDocFrequency());
/*     */       }
/*     */     }
/*  39 */     this.top = 2147483647;
/*     */   }
/*     */ 
/*     */   public void setUseRanking(boolean option) {
/*  43 */     this.useRanking = option;
/*     */   }
/*     */ 
/*     */   public boolean getUseRanking() {
/*  47 */     return this.useRanking;
/*     */   }
/*     */ 
/*     */   public void setTop(int top) {
/*  51 */     this.top = top;
/*     */   }
/*     */ 
/*     */   public int getTop() {
/*  55 */     return this.top;
/*     */   }
/*     */ 
/*     */   public DoubleRow generateVector(ConceptDesc concept)
/*     */   {
/*  64 */     if (this.indexReader == null) {
/*  65 */       return null;
/*     */     }
/*  67 */     MathUtil.initArray(this.buf, 0.0D);
/*  68 */     int docNum = concept.getArticleNum();
/*  69 */     for (int j = 0; j < docNum; j++)
/*  70 */       if (concept.getArticleRank(j) <= this.top)
/*     */       {
/*  72 */         IRDoc curDoc = this.indexReader.getDoc(concept.getArticleKey(j));
/*  73 */         if (curDoc == null) {
/*  74 */           curDoc = this.indexReader.getDoc(concept.getArticleKey(j) + "_" + concept.getConcept());
/*     */         }
/*  76 */         if ((curDoc != null) && (curDoc.getTermNum() != 0))
/*     */         {
/*  78 */           double sum = 0.0D;
/*  79 */           int termNum = curDoc.getTermNum();
/*  80 */           int[] arrTerm = this.indexReader.getTermIndexList(curDoc.getIndex());
/*  81 */           int[] arrFreq = this.indexReader.getTermFrequencyList(curDoc.getIndex());
/*  82 */           for (int i = 0; i < termNum; i++) {
/*  83 */             int newIndex = this.filter.map(arrTerm[i]);
/*  84 */             if (newIndex >= 0)
/*  85 */               sum += Math.pow(arrFreq[i] * this.idf[newIndex], 2.0D);
/*     */           }
/*  87 */           sum = 1.0D / Math.sqrt(sum);
/*  88 */           if (this.useRanking)
/*  89 */             sum = sum * (this.top + 1 - concept.getArticleRank(j)) / this.top;
/*  90 */           for (int i = 0; i < termNum; i++) {
/*  91 */             int newIndex = this.filter.map(arrTerm[i]);
/*  92 */             if (newIndex >= 0)
/*  93 */               this.buf[newIndex] += arrFreq[i] * this.idf[newIndex] * sum;
/*     */           }
/*     */         }
/*     */       }
/*  97 */     int count = 0;
/*  98 */     double sum = 0.0D;
/*  99 */     for (int i = 0; i < this.buf.length; i++) {
/* 100 */       if (this.buf[i] > 0.0D) {
/* 101 */         count++;
/* 102 */         sum += this.buf[i] * this.buf[i];
/*     */       }
/*     */     }
/* 105 */     sum = Math.sqrt(sum);
/* 106 */     int[] arrTerm = new int[count];
/* 107 */     double[] arrWeight = new double[count];
/* 108 */     count = 0;
/* 109 */     for (int i = 0; i < this.buf.length; i++) {
/* 110 */       if (this.buf[i] > 0.0D) {
/* 111 */         arrTerm[count] = i;
/* 112 */         arrWeight[count] = (this.buf[i] / sum);
/* 113 */         count++;
/*     */       }
/*     */     }
/* 116 */     return new DoubleRow(0, count, arrTerm, arrWeight);
/*     */   }
/*     */ 
/*     */   public DoubleRow generateVector(IntRow[] docs)
/*     */   {
/* 124 */     if (this.wordKeyList == null) {
/* 125 */       return null;
/*     */     }
/* 127 */     MathUtil.initArray(this.buf, 0.0D);
/* 128 */     int docNum = docs.length;
/* 129 */     for (int j = 0; j < docNum; j++) {
/* 130 */       IntRow curDoc = docs[j];
/* 131 */       if (curDoc.getRowIndex() <= this.top)
/*     */       {
/* 133 */         if ((curDoc != null) && (curDoc.getNonZeroNum() != 0))
/*     */         {
/* 135 */           double sum = 0.0D;
/* 136 */           int termNum = curDoc.getNonZeroNum();
/* 137 */           for (int i = 0; i < termNum; i++) {
/* 138 */             int index = curDoc.getNonZeroColumn(i);
/* 139 */             sum += Math.pow(curDoc.getNonZeroIntScore(i) * this.idf[index], 2.0D);
/*     */           }
/* 141 */           sum = 1.0D / Math.sqrt(sum);
/* 142 */           if (this.useRanking)
/* 143 */             sum = sum * (this.top + 1 - curDoc.getRowIndex()) / this.top;
/* 144 */           for (int i = 0; i < termNum; i++) {
/* 145 */             int index = curDoc.getNonZeroColumn(i);
/* 146 */             this.buf[index] += curDoc.getNonZeroIntScore(i) * this.idf[index] * sum;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 150 */     int count = 0;
/* 151 */     double sum = 0.0D;
/* 152 */     for (int i = 0; i < this.buf.length; i++) {
/* 153 */       if (this.buf[i] > 0.0D) {
/* 154 */         count++;
/* 155 */         sum += this.buf[i] * this.buf[i];
/*     */       }
/*     */     }
/* 158 */     sum = Math.sqrt(sum);
/* 159 */     int[] arrTerm = new int[count];
/* 160 */     double[] arrWeight = new double[count];
/* 161 */     count = 0;
/* 162 */     for (int i = 0; i < this.buf.length; i++) {
/* 163 */       if (this.buf[i] > 0.0D) {
/* 164 */         arrTerm[count] = i;
/* 165 */         arrWeight[count] = (this.buf[i] / sum);
/* 166 */         count++;
/*     */       }
/*     */     }
/* 169 */     return new DoubleRow(0, count, arrTerm, arrWeight);
/*     */   }
/*     */ 
/*     */   private double[] loadWordIDF(String file)
/*     */   {
/*     */     try
/*     */     {
/* 178 */       FastBinaryReader fbr = new FastBinaryReader(file);
/* 179 */       double[] idf = new double[fbr.readInt()];
/* 180 */       for (int i = 0; i < idf.length; i++)
/* 181 */         idf[i] = fbr.readDouble();
/* 182 */       fbr.close();
/* 183 */       return idf;
/*     */     }
/*     */     catch (Exception e) {
/* 186 */       e.printStackTrace();
/* 187 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.cptc.ConceptVectorGenerator
 * JD-Core Version:    0.6.2
 */