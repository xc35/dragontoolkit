/*     */ package edu.drexel.cis.dragon.nlp.tool.xtract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexSortable;
/*     */ 
/*     */ public class WordPairStat
/*     */   implements IndexSortable, Comparable
/*     */ {
/*     */   private int maxSpan;
/*     */   private int index;
/*     */   private int first;
/*     */   private int second;
/*     */   private int[] arrCount;
/*     */   private int totalCount;
/*     */ 
/*     */   public WordPairStat(int first, int second, int maxSpan)
/*     */   {
/*  21 */     this.index = -1;
/*  22 */     this.maxSpan = maxSpan;
/*  23 */     this.first = first;
/*  24 */     this.second = second;
/*  25 */     this.arrCount = new int[2 * maxSpan];
/*  26 */     for (int i = 0; i < 2 * maxSpan; i++) this.arrCount[i] = 0;
/*  27 */     this.totalCount = 0;
/*     */   }
/*     */ 
/*     */   public WordPairStat(int index, int first, int second, int maxSpan) {
/*  31 */     this.index = index;
/*  32 */     this.first = first;
/*  33 */     this.second = second;
/*  34 */     this.maxSpan = maxSpan;
/*  35 */     this.arrCount = new int[2 * maxSpan];
/*  36 */     for (int i = 0; i < 2 * maxSpan; i++) this.arrCount[i] = 0;
/*  37 */     this.totalCount = 0;
/*     */   }
/*     */ 
/*     */   public WordPairStat copy()
/*     */   {
/*  44 */     WordPairStat stat = new WordPairStat(this.index, this.first, this.second, this.maxSpan);
/*  45 */     for (int i = 0; i < this.maxSpan; i++) stat.addFrequency(i - this.maxSpan, this.arrCount[i]);
/*  46 */     for (int i = 1; i <= this.maxSpan; i++) stat.addFrequency(i, this.arrCount[(i + this.maxSpan - 1)]);
/*  47 */     return stat;
/*     */   }
/*     */ 
/*     */   public void addFrequency(int span, int inc) {
/*  51 */     if (span < 0)
/*  52 */       this.arrCount[(span + this.maxSpan)] += inc;
/*     */     else
/*  54 */       this.arrCount[(span + this.maxSpan - 1)] += inc;
/*  55 */     this.totalCount += inc;
/*     */   }
/*     */ 
/*     */   public int getFrequency(int span) {
/*  59 */     if (span > 0) {
/*  60 */       return this.arrCount[(span + this.maxSpan - 1)];
/*     */     }
/*  62 */     return this.arrCount[(span + this.maxSpan)];
/*     */   }
/*     */ 
/*     */   public int getTotalFrequency() {
/*  66 */     return this.totalCount;
/*     */   }
/*     */ 
/*     */   public void setIndex(int index) {
/*  70 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/*  74 */     return this.index;
/*     */   }
/*     */ 
/*     */   public int getFirstWord() {
/*  78 */     return this.first;
/*     */   }
/*     */ 
/*     */   public void setFirstWord(int word) {
/*  82 */     this.first = word;
/*     */   }
/*     */ 
/*     */   public int getSecondWord() {
/*  86 */     return this.second;
/*     */   }
/*     */ 
/*     */   public void setSecondWord(int word) {
/*  90 */     this.second = word;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object obj)
/*     */   {
/*  96 */     int indexObj = ((WordPairStat)obj).getFirstWord();
/*  97 */     if (this.first == indexObj) {
/*  98 */       indexObj = ((WordPairStat)obj).getSecondWord();
/*  99 */       if (this.second == indexObj)
/* 100 */         return 0;
/* 101 */       if (this.second > indexObj) {
/* 102 */         return 1;
/*     */       }
/* 104 */       return -1;
/*     */     }
/* 106 */     if (this.first > indexObj) {
/* 107 */       return 1;
/*     */     }
/* 109 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.xtract.WordPairStat
 * JD-Core Version:    0.6.2
 */