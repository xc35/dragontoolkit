/*    */ package edu.drexel.cis.dragon.qa.system;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Term;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ 
/*    */ public class Candidate extends Term
/*    */ {
/*    */   private SortedArray variants;
/*    */   private int capitalFreq;
/*    */   private int semanticFreq;
/*    */   private int initFreq;
/*    */ 
/*    */   public Candidate(Word start, Word end)
/*    */   {
/* 15 */     super(start, end);
/* 16 */     this.capitalFreq = 0;
/* 17 */     this.semanticFreq = 0;
/* 18 */     this.initFreq = 1;
/* 19 */     this.variants = null;
/*    */   }
/*    */ 
/*    */   public void addSemanticFrequency(int inc) {
/* 23 */     this.semanticFreq += inc;
/*    */   }
/*    */ 
/*    */   public void setSemanticFrequency(int freq) {
/* 27 */     this.semanticFreq = freq;
/*    */   }
/*    */ 
/*    */   public int getSemanticFrequency() {
/* 31 */     return this.semanticFreq;
/*    */   }
/*    */ 
/*    */   public void addInitialFrequency(int inc) {
/* 35 */     this.initFreq += inc;
/*    */   }
/*    */ 
/*    */   public void setInitialFrequency(int freq) {
/* 39 */     this.initFreq = freq;
/*    */   }
/*    */ 
/*    */   public int getInitialFrequency() {
/* 43 */     return this.initFreq;
/*    */   }
/*    */ 
/*    */   public void addCapitalFrequency(int inc) {
/* 47 */     this.capitalFreq += inc;
/*    */   }
/*    */ 
/*    */   public void setCapitalFrequency(int freq) {
/* 51 */     this.capitalFreq = freq;
/*    */   }
/*    */ 
/*    */   public int getCapitalFrequency() {
/* 55 */     return this.capitalFreq;
/*    */   }
/*    */ 
/*    */   public SortedArray getVariants() {
/* 59 */     return this.variants;
/*    */   }
/*    */ 
/*    */   public void addVariant(Candidate variant) {
/* 63 */     if (this.variants == null)
/* 64 */       this.variants = new SortedArray(2, new IndexComparator());
/* 65 */     this.variants.add(variant);
/* 66 */     this.variants.add(this);
/*    */   }
/*    */ 
/*    */   public void merge(Candidate variant)
/*    */   {
/* 73 */     addFrequency(variant.getFrequency());
/* 74 */     setWeight(getWeight() + variant.getWeight());
/* 75 */     addCapitalFrequency(variant.getCapitalFrequency());
/* 76 */     addSemanticFrequency(variant.getSemanticFrequency());
/* 77 */     if (this.variants == null) {
/* 78 */       this.variants = new SortedArray(3, new IndexComparator());
/* 79 */       this.variants.add(this);
/*    */     }
/* 81 */     if (variant.getVariants() != null) {
/* 82 */       SortedArray varList = variant.getVariants();
/* 83 */       for (int i = 0; i < varList.size(); i++)
/* 84 */         this.variants.add(varList.get(i));
/*    */     }
/*    */     else {
/* 87 */       this.variants.add(variant);
/*    */     }
/*    */   }
/*    */ 
/* 91 */   public void copyFrom(Candidate can) { setWeight(can.getWeight());
/* 92 */     setFrequency(can.getFrequency());
/* 93 */     setInitialFrequency(can.getInitialFrequency());
/* 94 */     setCapitalFrequency(can.getCapitalFrequency());
/* 95 */     setSemanticFrequency(can.getSemanticFrequency());
/* 96 */     setIndex(can.getIndex());
/* 97 */     if (can.getVariants() == null) {
/* 98 */       this.variants = null;
/*    */     } else {
/* 100 */       if (this.variants != null)
/* 101 */         this.variants.clear();
/*    */       else
/* 103 */         this.variants = new SortedArray(can.getVariants().size() + 1, new IndexComparator());
/* 104 */       this.variants.addAll(can.getVariants());
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.system.Candidate
 * JD-Core Version:    0.6.2
 */