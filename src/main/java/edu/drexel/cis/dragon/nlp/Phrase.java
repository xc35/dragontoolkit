/*     */ package edu.drexel.cis.dragon.nlp;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.compare.SortedElement;
/*     */ 
/*     */ public class Phrase
/*     */   implements Concept, Comparable, SortedElement
/*     */ {
/*     */   public static final int NAME_ASIS = 0;
/*     */   public static final int NAME_LEMMA = 1;
/*  17 */   private static int nameMode = 0;
/*     */   private Word starting;
/*     */   private Word ending;
/*     */   private Object memo;
/*     */   private int freq;
/*     */   private double weight;
/*     */   private int len;
/*     */   private int index;
/*     */   private boolean subterm;
/*     */ 
/*     */   public Phrase(Word starting, Word ending)
/*     */   {
/*  28 */     this.starting = starting;
/*  29 */     this.ending = ending;
/*  30 */     this.weight = 0.0D;
/*  31 */     this.freq = 1;
/*  32 */     this.index = 0;
/*  33 */     this.len = 1;
/*  34 */     this.memo = null;
/*  35 */     this.subterm = false;
/*  36 */     Word next = starting;
/*  37 */     while (!next.equals(ending)) {
/*  38 */       this.len += 1;
/*  39 */       next = next.next;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Phrase(Word word) {
/*  44 */     this.starting = word;
/*  45 */     this.ending = word;
/*  46 */     this.freq = 1;
/*  47 */     this.index = 0;
/*  48 */     this.memo = null;
/*  49 */     this.weight = 0.0D;
/*  50 */     this.len = 1;
/*  51 */     this.subterm = false;
/*     */   }
/*     */ 
/*     */   public Concept copy()
/*     */   {
/*  57 */     Phrase newTerm = new Phrase(getStartingWord(), getEndingWord());
/*  58 */     newTerm.setFrequency(getFrequency());
/*  59 */     newTerm.setSubConcept(isSubConcept());
/*  60 */     newTerm.setIndex(getIndex());
/*  61 */     newTerm.setWeight(getWeight());
/*  62 */     return newTerm;
/*     */   }
/*     */ 
/*     */   public int getConceptType() {
/*  66 */     return 2;
/*     */   }
/*     */ 
/*     */   public static void setNameMode(int mode) {
/*  70 */     if ((mode < 0) || (mode > 1))
/*  71 */       return;
/*  72 */     nameMode = mode;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object obj)
/*     */   {
/*  78 */     String objValue = ((Phrase)obj).toString();
/*  79 */     return toString().compareToIgnoreCase(objValue);
/*     */   }
/*     */ 
/*     */   public int compareTo(Phrase term) {
/*  83 */     return toString().compareToIgnoreCase(term.toString());
/*     */   }
/*     */ 
/*     */   public boolean isSubConcept() {
/*  87 */     return this.subterm;
/*     */   }
/*     */ 
/*     */   public void setSubConcept(boolean subterm) {
/*  91 */     this.subterm = subterm;
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  95 */     if (nameMode == 0) {
/*  96 */       return toString();
/*     */     }
/*  98 */     return toLemmaString();
/*     */   }
/*     */ 
/*     */   public String getEntryID() {
/* 102 */     return getName();
/*     */   }
/*     */ 
/*     */   public String getSemanticType() {
/* 106 */     return null;
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
/*     */   public void setWeight(double weight) {
/* 118 */     this.weight = weight;
/*     */   }
/*     */ 
/*     */   public double getWeight() {
/* 122 */     return this.weight;
/*     */   }
/*     */ 
/*     */   public Word getStartingWord() {
/* 126 */     return this.starting;
/*     */   }
/*     */ 
/*     */   public Word getEndingWord() {
/* 130 */     return this.ending;
/*     */   }
/*     */ 
/*     */   public Object getMemo() {
/* 134 */     return this.memo;
/*     */   }
/*     */ 
/*     */   public void setMemo(Object memo) {
/* 138 */     this.memo = memo;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 146 */     StringBuffer term = new StringBuffer(this.starting.getContent());
/* 147 */     Word next = this.starting;
/* 148 */     boolean lastPunctuation = false;
/* 149 */     while (!next.equals(this.ending)) {
/* 150 */       next = next.next;
/* 151 */       if (next.isPunctuation())
/*     */       {
/* 153 */         term.append(next.getContent());
/* 154 */         lastPunctuation = true;
/*     */       }
/*     */       else
/*     */       {
/* 158 */         if (lastPunctuation) {
/* 159 */           term.append(next.getContent());
/*     */         }
/*     */         else {
/* 162 */           term.append(' ');
/* 163 */           term.append(next.getContent());
/*     */         }
/* 165 */         lastPunctuation = false;
/*     */       }
/*     */     }
/* 168 */     return term.toString();
/*     */   }
/*     */ 
/*     */   public String toLemmaString()
/*     */   {
/* 175 */     Word next = this.starting;
/* 176 */     String term = next.getLemma();
/* 177 */     while (!next.equals(this.ending)) {
/* 178 */       next = next.next;
/* 179 */       term = term + " " + next.getLemma();
/*     */     }
/* 181 */     return term;
/*     */   }
/*     */ 
/*     */   public int getWordNum() {
/* 185 */     return this.len;
/*     */   }
/*     */ 
/*     */   public void addFrequency(int inc) {
/* 189 */     this.freq += inc;
/*     */   }
/*     */ 
/*     */   public int getFrequency() {
/* 193 */     return this.freq;
/*     */   }
/*     */ 
/*     */   public void setFrequency(int freq) {
/* 197 */     this.freq = freq;
/*     */   }
/*     */ 
/*     */   public boolean equalTo(Concept concept) {
/* 201 */     return getName().equalsIgnoreCase(concept.getName());
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.Phrase
 * JD-Core Version:    0.6.2
 */