/*     */ package edu.drexel.cis.dragon.nlp;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.compare.SortedElement;
/*     */ 
/*     */ public class Triple
/*     */   implements Comparable, SortedElement
/*     */ {
/*     */   private Concept first;
/*     */   private Concept second;
/*     */   private String rel;
/*     */   private String[] can_rel;
/*     */   private int freq;
/*     */   private int index;
/*     */   private double weight;
/*     */   private Object memo;
/*     */ 
/*     */   public Triple(Concept first, Concept second)
/*     */   {
/*  23 */     this.first = first;
/*  24 */     this.second = second;
/*  25 */     this.rel = null;
/*  26 */     this.can_rel = null;
/*  27 */     this.freq = 1;
/*  28 */     this.weight = 0.0D;
/*  29 */     this.memo = null;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object obj)
/*     */   {
/*  35 */     int objIndex = ((Triple)obj).getFirstConcept().getIndex();
/*  36 */     if (this.first.getIndex() == objIndex) {
/*  37 */       objIndex = ((Triple)obj).getSecondConcept().getIndex();
/*  38 */       if (this.second.getIndex() == objIndex)
/*  39 */         return 0;
/*  40 */       if (this.second.getIndex() > objIndex) {
/*  41 */         return 1;
/*     */       }
/*  43 */       return -1;
/*     */     }
/*  45 */     if (this.first.getIndex() > objIndex) {
/*  46 */       return 1;
/*     */     }
/*  48 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/*  52 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void setIndex(int index) {
/*  56 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public Object getMemo() {
/*  60 */     return this.memo;
/*     */   }
/*     */ 
/*     */   public void setMemo(Object memo) {
/*  64 */     this.memo = memo;
/*     */   }
/*     */ 
/*     */   public Concept getFirstConcept()
/*     */   {
/*  69 */     return this.first;
/*     */   }
/*     */ 
/*     */   public Concept getSecondConcept() {
/*  73 */     return this.second;
/*     */   }
/*     */ 
/*     */   public String getTUI() {
/*  77 */     return this.rel;
/*     */   }
/*     */ 
/*     */   public void setTUI(String rel) {
/*  81 */     this.rel = rel;
/*     */   }
/*     */ 
/*     */   public String[] getCandidateTUI() {
/*  85 */     return this.can_rel;
/*     */   }
/*     */ 
/*     */   public String getCandidateTUI(int index) {
/*  89 */     return this.can_rel[index];
/*     */   }
/*     */ 
/*     */   public int getCandidateTUINum() {
/*  93 */     if (this.can_rel == null) {
/*  94 */       return 0;
/*     */     }
/*     */ 
/*  97 */     return this.can_rel.length;
/*     */   }
/*     */ 
/*     */   public void setCandidateTUI(String[] can_rel)
/*     */   {
/* 102 */     this.can_rel = can_rel;
/*     */   }
/*     */ 
/*     */   public double getWeight() {
/* 106 */     return this.weight;
/*     */   }
/*     */ 
/*     */   public void setWeight(double weight) {
/* 110 */     this.weight = weight;
/*     */   }
/*     */ 
/*     */   public int getFrequency() {
/* 114 */     return this.freq;
/*     */   }
/*     */ 
/*     */   public void setFrequency(int freq) {
/* 118 */     this.freq = freq;
/*     */   }
/*     */ 
/*     */   public void addFrequency(int inc) {
/* 122 */     this.freq += inc;
/*     */   }
/*     */ 
/*     */   public boolean equalTo(Triple triple) {
/* 126 */     if (((this.first.equalTo(triple.getFirstConcept())) && (this.second.equalTo(triple.getSecondConcept()))) || (
/* 127 */       (this.first.equalTo(triple.getSecondConcept())) && (this.second.equalTo(triple.getFirstConcept())))) {
/* 128 */       return true;
/*     */     }
/* 130 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.Triple
 * JD-Core Version:    0.6.2
 */