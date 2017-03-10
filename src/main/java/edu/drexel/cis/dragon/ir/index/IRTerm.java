/*    */ package edu.drexel.cis.dragon.ir.index;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.compare.FrequencySortable;
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexSortable;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class IRTerm
/*    */   implements IRSignature, IndexSortable, FrequencySortable, Comparable, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String key;
/*    */   private int docFrequency;
/*    */   private int frequency;
/*    */   private int index;
/*    */ 
/*    */   public IRTerm(String key)
/*    */   {
/* 22 */     this.key = key;
/* 23 */     this.docFrequency = 0;
/* 24 */     this.frequency = 0;
/* 25 */     this.index = -1;
/*    */   }
/*    */ 
/*    */   public IRTerm(int index, int frequency) {
/* 29 */     this.key = null;
/* 30 */     this.docFrequency = 0;
/* 31 */     this.frequency = frequency;
/* 32 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public IRTerm(String key, int index, int frequency) {
/* 36 */     this.key = key;
/* 37 */     this.docFrequency = 0;
/* 38 */     this.frequency = frequency;
/* 39 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public IRTerm(int index, int frequency, int docFrequency) {
/* 43 */     this.key = null;
/* 44 */     this.docFrequency = docFrequency;
/* 45 */     this.frequency = frequency;
/* 46 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public IRTerm copy()
/*    */   {
/* 52 */     IRTerm cur = new IRTerm(this.index, this.frequency, this.docFrequency);
/*    */ 
/* 54 */     cur.setKey(this.key);
/* 55 */     return cur;
/*    */   }
/*    */ 
/*    */   public String getKey() {
/* 59 */     return this.key;
/*    */   }
/*    */ 
/*    */   public void setKey(String key) {
/* 63 */     this.key = key;
/*    */   }
/*    */ 
/*    */   public void setFrequency(int freq) {
/* 67 */     this.frequency = freq;
/*    */   }
/*    */ 
/*    */   public void addFrequency(int inc) {
/* 71 */     this.frequency += inc;
/*    */   }
/*    */ 
/*    */   public int getFrequency() {
/* 75 */     return this.frequency;
/*    */   }
/*    */ 
/*    */   public int getIndex() {
/* 79 */     return this.index;
/*    */   }
/*    */ 
/*    */   public void setIndex(int index) {
/* 83 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public int getDocFrequency() {
/* 87 */     return this.docFrequency;
/*    */   }
/*    */ 
/*    */   public void addDocFrequency(int inc) {
/* 91 */     this.docFrequency += inc;
/*    */   }
/*    */ 
/*    */   public void setDocFrequency(int freq) {
/* 95 */     this.docFrequency = freq;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj) {
/* 99 */     return this.key.compareToIgnoreCase(((IRTerm)obj).getKey());
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.IRTerm
 * JD-Core Version:    0.6.2
 */