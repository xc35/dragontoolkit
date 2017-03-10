/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexSortable;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightSortable;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class IRDoc
/*     */   implements WeightSortable, IndexSortable, Comparable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private int index;
/*     */   private int category;
/*     */   private String key;
/*     */   private int termNum;
/*     */   private int termCount;
/*     */   private int relationNum;
/*     */   private int relationCount;
/*     */   private double weight;
/*     */ 
/*     */   public IRDoc(String key)
/*     */   {
/*  23 */     this.key = key;
/*  24 */     this.index = -1;
/*  25 */     this.category = -1;
/*  26 */     this.termNum = 0;
/*  27 */     this.termCount = 0;
/*  28 */     this.relationNum = 0;
/*  29 */     this.relationCount = 0;
/*     */   }
/*     */ 
/*     */   public IRDoc(int index) {
/*  33 */     this.index = index;
/*  34 */     this.key = null;
/*  35 */     this.termNum = 0;
/*  36 */     this.termCount = 0;
/*  37 */     this.relationNum = 0;
/*  38 */     this.relationCount = 0;
/*     */   }
/*     */ 
/*     */   public IRDoc copy()
/*     */   {
/*  45 */     IRDoc cur = new IRDoc(this.key);
/*  46 */     cur.setIndex(this.index);
/*  47 */     cur.setTermCount(this.termCount);
/*  48 */     cur.setTermNum(this.termNum);
/*  49 */     cur.setRelationCount(this.relationCount);
/*  50 */     cur.setRelationNum(this.relationNum);
/*  51 */     cur.setWeight(this.weight);
/*  52 */     return cur;
/*     */   }
/*     */ 
/*     */   public String getKey() {
/*  56 */     return this.key;
/*     */   }
/*     */ 
/*     */   public void setKey(String key) {
/*  60 */     this.key = key;
/*     */   }
/*     */ 
/*     */   public int getCategory() {
/*  64 */     return this.category;
/*     */   }
/*     */ 
/*     */   public void setCategory(int category) {
/*  68 */     this.category = category;
/*     */   }
/*     */ 
/*     */   public int getTermNum() {
/*  72 */     return this.termNum;
/*     */   }
/*     */ 
/*     */   public void setTermNum(int termNum) {
/*  76 */     this.termNum = termNum;
/*     */   }
/*     */ 
/*     */   public int getTermCount() {
/*  80 */     return this.termCount;
/*     */   }
/*     */ 
/*     */   public void setTermCount(int termCount) {
/*  84 */     this.termCount = termCount;
/*     */   }
/*     */ 
/*     */   public int getRelationNum() {
/*  88 */     return this.relationNum;
/*     */   }
/*     */ 
/*     */   public void setRelationNum(int relationNum) {
/*  92 */     this.relationNum = relationNum;
/*     */   }
/*     */ 
/*     */   public int getRelationCount() {
/*  96 */     return this.relationCount;
/*     */   }
/*     */ 
/*     */   public void setRelationCount(int relationCount) {
/* 100 */     this.relationCount = relationCount;
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/* 104 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void setIndex(int index) {
/* 108 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public void setWeight(double weight)
/*     */   {
/* 113 */     this.weight = weight;
/*     */   }
/*     */ 
/*     */   public double getWeight() {
/* 117 */     return this.weight;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object obj)
/*     */   {
/* 123 */     String objKey = ((IRDoc)obj).getKey();
/* 124 */     return this.key.compareTo(objKey);
/*     */   }
/*     */ 
/*     */   public int compareTo(IRDoc doc) {
/* 128 */     return this.key.compareTo(doc.getKey());
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.IRDoc
 * JD-Core Version:    0.6.2
 */