/*     */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*     */ 
/*     */ public class BasicFeature
/*     */   implements Feature
/*     */ {
/*     */   private FeatureIdentifier id;
/*     */   private int index;
/*     */   private int ystart;
/*     */   private int yend;
/*     */   private double val;
/*     */ 
/*     */   public BasicFeature()
/*     */   {
/*  21 */     this.index = -1;
/*  22 */     this.ystart = -1;
/*  23 */     this.yend = -1;
/*  24 */     this.id = null;
/*     */   }
/*     */ 
/*     */   public BasicFeature(String id, int label, double val) {
/*  28 */     this(id, -1, label, val);
/*     */   }
/*     */ 
/*     */   public BasicFeature(FeatureIdentifier id, int label, double val) {
/*  32 */     this(id, -1, label, val);
/*     */   }
/*     */ 
/*     */   public BasicFeature(String id, int prevLabel, int label, double val) {
/*  36 */     this(new FeatureIdentifier(id), prevLabel, label, val);
/*     */   }
/*     */ 
/*     */   public BasicFeature(FeatureIdentifier id, int prevLabel, int label, double val) {
/*  40 */     this.id = id;
/*  41 */     this.yend = label;
/*  42 */     this.val = val;
/*  43 */     this.ystart = prevLabel;
/*  44 */     this.index = -1;
/*     */   }
/*     */ 
/*     */   public BasicFeature(BasicFeature f) {
/*  48 */     copyFrom(f);
/*     */   }
/*     */ 
/*     */   public void copyFrom(BasicFeature f) {
/*  52 */     this.index = f.getIndex();
/*  53 */     this.ystart = f.getPrevLabel();
/*  54 */     this.yend = f.getLabel();
/*  55 */     this.val = f.getValue();
/*  56 */     this.id = f.getID().copy();
/*     */   }
/*     */ 
/*     */   public Feature copy() {
/*  60 */     return new BasicFeature(this);
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/*  64 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void setIndex(int index) {
/*  68 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public int getLabel() {
/*  72 */     return this.yend;
/*     */   }
/*     */ 
/*     */   public void setLabel(int label) {
/*  76 */     this.yend = label;
/*     */   }
/*     */ 
/*     */   public int getPrevLabel() {
/*  80 */     return this.ystart;
/*     */   }
/*     */ 
/*     */   public void setPrevLabel(int prevLabel) {
/*  84 */     this.ystart = prevLabel;
/*     */   }
/*     */ 
/*     */   public double getValue() {
/*  88 */     return this.val;
/*     */   }
/*     */ 
/*     */   public void setValue(double val) {
/*  92 */     this.val = val;
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  96 */     return this.id + " " + this.val;
/*     */   }
/*     */ 
/*     */   public FeatureIdentifier getID() {
/* 100 */     return this.id;
/*     */   }
/*     */ 
/*     */   public void setID(FeatureIdentifier id) {
/* 104 */     this.id = id;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.BasicFeature
 * JD-Core Version:    0.6.2
 */