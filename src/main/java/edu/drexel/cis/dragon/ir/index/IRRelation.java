/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.compare.FrequencySortable;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexSortable;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class IRRelation
/*     */   implements IRSignature, IndexSortable, FrequencySortable, Comparable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private int first;
/*     */   private int second;
/*     */   private int docFrequency;
/*     */   private int frequency;
/*     */   private int index;
/*     */ 
/*     */   public IRRelation(int firstTermIndex, int secondTermIndex, int frequency)
/*     */   {
/*  23 */     this.first = firstTermIndex;
/*  24 */     this.second = secondTermIndex;
/*  25 */     this.index = -1;
/*  26 */     this.frequency = frequency;
/*  27 */     this.docFrequency = 0;
/*     */   }
/*     */ 
/*     */   public IRRelation(int index, int firstTermIndex, int secondTermIndex, int frequency, int docFrequency) {
/*  31 */     this.first = firstTermIndex;
/*  32 */     this.second = secondTermIndex;
/*  33 */     this.index = index;
/*  34 */     this.frequency = frequency;
/*  35 */     this.docFrequency = docFrequency;
/*     */   }
/*     */ 
/*     */   public IRRelation copy() {
/*  39 */     return new IRRelation(this.index, this.first, this.second, this.frequency, this.docFrequency);
/*     */   }
/*     */ 
/*     */   public int getFirstTerm() {
/*  43 */     return this.first;
/*     */   }
/*     */ 
/*     */   public void setFirstTerm(int first) {
/*  47 */     this.first = first;
/*     */   }
/*     */ 
/*     */   public int getSecondTerm() {
/*  51 */     return this.second;
/*     */   }
/*     */ 
/*     */   public void setSecondTerm(int second) {
/*  55 */     this.second = second;
/*     */   }
/*     */ 
/*     */   public void setFrequency(int freq) {
/*  59 */     this.frequency = freq;
/*     */   }
/*     */ 
/*     */   public void addFrequency(int inc) {
/*  63 */     this.frequency += inc;
/*     */   }
/*     */ 
/*     */   public int getFrequency() {
/*  67 */     return this.frequency;
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/*  71 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void setIndex(int index) {
/*  75 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public int getDocFrequency() {
/*  79 */     return this.docFrequency;
/*     */   }
/*     */ 
/*     */   public void addDocFrequency(int inc) {
/*  83 */     this.docFrequency += inc;
/*     */   }
/*     */ 
/*     */   public void setDocFrequency(int freq) {
/*  87 */     this.docFrequency = freq;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object obj)
/*     */   {
/*  93 */     int indexObj = ((IRRelation)obj).getFirstTerm();
/*  94 */     if (this.first == indexObj) {
/*  95 */       indexObj = ((IRRelation)obj).getSecondTerm();
/*  96 */       if (this.second == indexObj)
/*  97 */         return 0;
/*  98 */       if (this.second > indexObj) {
/*  99 */         return 1;
/*     */       }
/* 101 */       return -1;
/*     */     }
/* 103 */     if (this.first > indexObj) {
/* 104 */       return 1;
/*     */     }
/* 106 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.IRRelation
 * JD-Core Version:    0.6.2
 */