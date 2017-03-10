/*     */ package edu.drexel.cis.dragon.ir.search.feedback;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.kngbase.HALSpace;
/*     */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*     */ import edu.drexel.cis.dragon.ir.search.Searcher;
/*     */ import edu.drexel.cis.dragon.ir.search.expand.InformationFlowQE;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElement;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.extract.TokenExtractor;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class InformationFlowFeedback extends AbstractFeedback
/*     */ {
/*     */   private InformationFlowQE qe;
/*     */   private TokenExtractor te;
/*     */   private SimpleElementList vocabulary;
/*     */   private int windowSize;
/*     */   private int minFrequency;
/*     */ 
/*     */   public InformationFlowFeedback(TokenExtractor te, Searcher searcher, int feedbackDocNum, int expandTermNum, double feedbackCoeffi)
/*     */   {
/*  29 */     super(searcher, feedbackDocNum, feedbackCoeffi);
/*  30 */     this.te = te;
/*  31 */     this.qe = new InformationFlowQE(searcher.getIndexReader(), expandTermNum, feedbackCoeffi);
/*  32 */     this.windowSize = 8;
/*  33 */     this.minFrequency = 25;
/*     */   }
/*     */ 
/*     */   protected ArrayList estimateNewQueryModel(IRQuery oldQuery) {
/*  37 */     return null;
/*     */   }
/*     */ 
/*     */   public void setHALWindowSize(int size) {
/*  41 */     this.windowSize = size;
/*     */   }
/*     */ 
/*     */   public int getHALWindowSize() {
/*  45 */     return this.windowSize;
/*     */   }
/*     */ 
/*     */   public void setInfrequentTermThreshold(int threshold) {
/*  49 */     this.minFrequency = threshold;
/*     */   }
/*     */ 
/*     */   public int getInfrequentTermThreshold() {
/*  53 */     return this.minFrequency;
/*     */   }
/*     */ 
/*     */   public void setMultiplier(double multiplier) {
/*  57 */     this.qe.setMultiplier(multiplier);
/*     */   }
/*     */ 
/*     */   public double getMultiplier() {
/*  61 */     return this.qe.getMultiplier();
/*     */   }
/*     */ 
/*     */   public void setDominantVectorWeight(double weight) {
/*  65 */     this.qe.setDominantVectorWeight(weight);
/*     */   }
/*     */ 
/*     */   public double getDominantVectorWeight() {
/*  69 */     return this.qe.getDominantVectorWeight();
/*     */   }
/*     */ 
/*     */   public void setSubordinateVectorWeight(double weight) {
/*  73 */     this.qe.setSubordinateVectorWeight(weight);
/*     */   }
/*     */ 
/*     */   public double getSubordinateVectorWeight() {
/*  77 */     return this.qe.getSubordinateVectorWeight();
/*     */   }
/*     */ 
/*     */   public void setDominantVectorThreshold(double threshold) {
/*  81 */     this.qe.setDominantVectorThreshold(threshold);
/*     */   }
/*     */ 
/*     */   public double getDominantVectorThreshold() {
/*  85 */     return this.qe.getDominantVectorThreshold();
/*     */   }
/*     */ 
/*     */   public void setSubordinateVectorThreshold(double threshold) {
/*  89 */     this.qe.setSubordinateVectorThreshold(threshold);
/*     */   }
/*     */ 
/*     */   public double getSubordinateVectorThreshold() {
/*  93 */     return this.qe.getSubordinateVectorThreshold();
/*     */   }
/*     */ 
/*     */   public IRQuery updateQueryModel(IRQuery oldQuery)
/*     */   {
/* 103 */     this.searcher.search(oldQuery);
/* 104 */     int docNum = this.feedbackDocNum < this.searcher.getRetrievedDocNum() ? this.feedbackDocNum : this.searcher.getRetrievedDocNum();
/* 105 */     if (docNum == 0) return oldQuery;
/*     */ 
/* 107 */     ArrayList articleList = new ArrayList(docNum);
/* 108 */     IndexReader reader = this.searcher.getIndexReader();
/* 109 */     for (int i = 0; i < docNum; i++) {
/* 110 */       String docKey = reader.getDocKey(this.searcher.getIRDoc(i).getIndex());
/* 111 */       articleList.add(reader.getOriginalDoc(docKey));
/*     */     }
/*     */ 
/* 114 */     if (this.vocabulary == null)
/* 115 */       this.vocabulary = prepVocabulary(this.searcher.getIndexReader(), this.minFrequency);
/* 116 */     HALSpace hal = new HALSpace(this.vocabulary, this.te, this.windowSize);
/* 117 */     hal.setShowProgress(false);
/* 118 */     hal.add(articleList);
/* 119 */     hal.finalizeData();
/* 120 */     this.qe.setHALSpace(hal);
/* 121 */     return this.qe.expand(oldQuery);
/*     */   }
/*     */ 
/*     */   private SimpleElementList prepVocabulary(IndexReader reader, int freqThreshold)
/*     */   {
/* 130 */     SimpleElementList newList = new SimpleElementList();
/* 131 */     int termNum = reader.getCollection().getTermNum();
/* 132 */     for (int i = 0; i < termNum; i++) {
/* 133 */       IRTerm curTerm = reader.getIRTerm(i);
/* 134 */       if (curTerm.getFrequency() >= freqThreshold) {
/* 135 */         int index = curTerm.getIndex();
/* 136 */         String key = reader.getTermKey(index);
/* 137 */         newList.add(new SimpleElement(key, index));
/*     */       }
/*     */     }
/* 140 */     return newList;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.feedback.InformationFlowFeedback
 * JD-Core Version:    0.6.2
 */