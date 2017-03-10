/*     */ package edu.drexel.cis.dragon.nlp;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.compare.SortedElement;
/*     */ 
/*     */ public class Token
/*     */   implements Concept, Comparable, SortedElement
/*     */ {
/*     */   private String value;
/*     */   private int index;
/*     */   private int freq;
/*     */   private double weight;
/*     */   private Object memo;
/*     */ 
/*     */   public Token(String value)
/*     */   {
/*  20 */     this.freq = 1;
/*  21 */     this.value = value;
/*  22 */     this.index = -1;
/*  23 */     this.weight = -1.0D;
/*  24 */     this.memo = null;
/*     */   }
/*     */ 
/*     */   public Token(int index, int frequency) {
/*  28 */     this.freq = frequency;
/*  29 */     this.index = index;
/*  30 */     this.weight = -1.0D;
/*  31 */     this.value = null;
/*  32 */     this.memo = null;
/*     */   }
/*     */ 
/*     */   public Token(String value, int index, int frequency) {
/*  36 */     this.freq = frequency;
/*  37 */     this.value = value;
/*  38 */     this.index = index;
/*  39 */     this.weight = -1.0D;
/*  40 */     this.memo = null;
/*     */   }
/*     */ 
/*     */   public Concept copy()
/*     */   {
/*  46 */     Token cur = new Token(this.value);
/*  47 */     cur.setFrequency(this.freq);
/*  48 */     cur.setIndex(this.index);
/*  49 */     cur.setWeight(this.weight);
/*  50 */     return cur;
/*     */   }
/*     */ 
/*     */   public int getConceptType() {
/*  54 */     return 3;
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  58 */     return this.value;
/*     */   }
/*     */ 
/*     */   public String getEntryID() {
/*  62 */     return this.value;
/*     */   }
/*     */ 
/*     */   public String getSemanticType() {
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isSubConcept() {
/*  70 */     return false;
/*     */   }
/*     */ 
/*     */   public Word getStartingWord() {
/*  74 */     return null;
/*     */   }
/*     */ 
/*     */   public Word getEndingWord() {
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getMemo() {
/*  82 */     return this.memo;
/*     */   }
/*     */ 
/*     */   public void setMemo(Object memo) {
/*  86 */     this.memo = memo;
/*     */   }
/*     */ 
/*     */   public String getValue() {
/*  90 */     return this.value;
/*     */   }
/*     */ 
/*     */   public void setValue(String value) {
/*  94 */     this.value = value;
/*     */   }
/*     */ 
/*     */   public void setFrequency(int freq) {
/*  98 */     this.freq = freq;
/*     */   }
/*     */ 
/*     */   public void addFrequency(int inc) {
/* 102 */     this.freq += inc;
/*     */   }
/*     */ 
/*     */   public int getFrequency() {
/* 106 */     return this.freq;
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/* 110 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void setIndex(int index) {
/* 114 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public void setWeight(double weight)
/*     */   {
/* 119 */     this.weight = weight;
/*     */   }
/*     */ 
/*     */   public double getWeight() {
/* 123 */     return this.weight;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object obj)
/*     */   {
/* 129 */     String objValue = ((Token)obj).getValue();
/* 130 */     return this.value.compareToIgnoreCase(objValue);
/*     */   }
/*     */ 
/*     */   public int compareTo(Token token) {
/* 134 */     return this.value.compareToIgnoreCase(token.getValue());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj) {
/* 138 */     if (this.value == null) {
/* 139 */       return this.index == ((SortedElement)obj).getIndex();
/*     */     }
/*     */ 
/* 142 */     return this.value.equalsIgnoreCase(((Concept)obj).getName());
/*     */   }
/*     */ 
/*     */   public boolean equalTo(Concept concept)
/*     */   {
/* 147 */     if (this.value == null) {
/* 148 */       return this.index == concept.getIndex();
/*     */     }
/*     */ 
/* 151 */     return this.value.equalsIgnoreCase(concept.getName());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 156 */     return this.value;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 160 */     if (this.index >= 0) {
/* 161 */       return this.index;
/*     */     }
/* 163 */     return super.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.Token
 * JD-Core Version:    0.6.2
 */