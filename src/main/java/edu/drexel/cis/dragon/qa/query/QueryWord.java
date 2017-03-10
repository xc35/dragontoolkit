/*     */ package edu.drexel.cis.dragon.qa.query;
/*     */ 
/*     */ import edu.drexel.cis.dragon.qa.merge.AbbrEntityMerger;
/*     */ 
/*     */ public class QueryWord
/*     */   implements Comparable
/*     */ {
/*     */   private String content;
/*     */   private String lemma;
/*     */   private String stem;
/*     */   private String abbr;
/*     */   private int pos;
/*     */   private double weight;
/*     */   private int index;
/*     */   private int groupID;
/*     */   private int freq;
/*     */   private boolean functional;
/*     */   private boolean isHeadNoun;
/*     */   private boolean isYear;
/*     */   private boolean isVerbBe;
/*     */ 
/*     */   public QueryWord(String word)
/*     */   {
/*  15 */     this.content = word;
/*  16 */     if (AbbrEntityMerger.isAbbr(word)) {
/*  17 */       this.abbr = AbbrEntityMerger.getAbbr(word);
/*  18 */       if (this.abbr.equals("USA"))
/*  19 */         this.abbr = "US";
/*     */     }
/*     */     else {
/*  22 */       this.abbr = null;
/*  23 */     }this.isHeadNoun = false;
/*  24 */     this.isYear = isYear(word);
/*  25 */     this.index = -1;
/*  26 */     this.weight = 0.0D;
/*     */   }
/*     */ 
/*     */   public void setSentenceCount(int freq) {
/*  30 */     this.freq = freq;
/*     */   }
/*     */ 
/*     */   public int getFrequency() {
/*  34 */     return this.freq;
/*     */   }
/*     */ 
/*     */   public void addFrequency(int inc) {
/*  38 */     this.freq += inc;
/*     */   }
/*     */ 
/*     */   public boolean isVerbBe() {
/*  42 */     return this.isVerbBe;
/*     */   }
/*     */ 
/*     */   public void setVerbBeFlag(boolean flag) {
/*  46 */     this.isVerbBe = flag;
/*     */   }
/*     */ 
/*     */   public boolean isYear() {
/*  50 */     return this.isYear;
/*     */   }
/*     */ 
/*     */   public void setHeadNounFlag(boolean flag) {
/*  54 */     this.isHeadNoun = flag;
/*     */   }
/*     */ 
/*     */   public boolean isHeadNoun() {
/*  58 */     return this.isHeadNoun;
/*     */   }
/*     */ 
/*     */   public void setGroupID(int groupID) {
/*  62 */     this.groupID = groupID;
/*     */   }
/*     */ 
/*     */   public int getGroupID() {
/*  66 */     return this.groupID;
/*     */   }
/*     */ 
/*     */   public String getContent() {
/*  70 */     return this.content;
/*     */   }
/*     */ 
/*     */   public String getLemma() {
/*  74 */     return this.lemma;
/*     */   }
/*     */ 
/*     */   public void setLemma(String lemma) {
/*  78 */     this.lemma = lemma;
/*     */   }
/*     */ 
/*     */   public String getStem() {
/*  82 */     return this.stem;
/*     */   }
/*     */ 
/*     */   public void setStem(String stem) {
/*  86 */     this.stem = stem;
/*     */   }
/*     */ 
/*     */   public int getPOSTag() {
/*  90 */     return this.pos;
/*     */   }
/*     */ 
/*     */   public void setPOSTag(int tag) {
/*  94 */     this.pos = tag;
/*     */   }
/*     */ 
/*     */   public boolean isFunctionalWord() {
/*  98 */     return this.functional;
/*     */   }
/*     */ 
/*     */   public void setFunctionFlag(boolean flag) {
/* 102 */     this.functional = flag;
/*     */   }
/*     */ 
/*     */   public boolean isInitialCapital() {
/* 106 */     return Character.isUpperCase(this.content.charAt(0));
/*     */   }
/*     */ 
/*     */   public double getWeight() {
/* 110 */     return this.weight;
/*     */   }
/*     */ 
/*     */   public void setWeight(double weight) {
/* 114 */     this.weight = weight;
/*     */   }
/*     */ 
/*     */   public void setIndex(int index) {
/* 118 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/* 122 */     return this.index;
/*     */   }
/*     */ 
/*     */   public String getAbbr() {
/* 126 */     return this.abbr;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 130 */     return this.content;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object obj) {
/* 134 */     return this.content.compareToIgnoreCase(((QueryWord)obj).getContent());
/*     */   }
/*     */ 
/*     */   private boolean isYear(String word) {
/* 138 */     if (word.length() != 4) {
/* 139 */       return false;
/*     */     }
/* 141 */     return (Character.isDigit(word.charAt(0))) && (Character.isDigit(word.charAt(1))) && (Character.isDigit(word.charAt(2))) && 
/* 141 */       (Character.isDigit(word.charAt(3)));
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.query.QueryWord
 * JD-Core Version:    0.6.2
 */