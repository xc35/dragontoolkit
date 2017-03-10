/*     */ package edu.drexel.cis.dragon.ml.seqmodel.model;
/*     */ 
/*     */ public class CompleteModel extends AbstractModel
/*     */ {
/*     */   private int markovOrder;
/*     */   private int originalLabelNum;
/*     */ 
/*     */   public CompleteModel(int labelNum)
/*     */   {
/*  16 */     this(labelNum, 1);
/*     */   }
/*     */ 
/*     */   public CompleteModel(int labelNum, int markovOrder) {
/*  20 */     super(labelNum, "Complete");
/*  21 */     this.numLabels = computeLabelNum(labelNum, markovOrder);
/*  22 */     this.markovOrder = markovOrder;
/*  23 */     this.originalLabelNum = labelNum;
/*     */   }
/*     */ 
/*     */   public int getOriginalLabelNum() {
/*  27 */     return this.originalLabelNum;
/*     */   }
/*     */ 
/*     */   public int getMarkovOrder() {
/*  31 */     return this.markovOrder;
/*     */   }
/*     */ 
/*     */   public int getLabel(int state) {
/*  35 */     return state;
/*     */   }
/*     */ 
/*     */   public int getEdgeNum() {
/*  39 */     return this.numLabels * this.numLabels;
/*     */   }
/*     */ 
/*     */   public int getStartStateNum() {
/*  43 */     return this.numLabels;
/*     */   }
/*     */ 
/*     */   public int getEndStateNum() {
/*  47 */     return this.numLabels;
/*     */   }
/*     */ 
/*     */   public int getStartState(int i) {
/*  51 */     if (i < getStartStateNum()) {
/*  52 */       return i;
/*     */     }
/*  54 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getEndState(int i) {
/*  58 */     if (i < getEndStateNum()) {
/*  59 */       return i;
/*     */     }
/*  61 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean isEndState(int i) {
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isStartState(int i) {
/*  69 */     return true;
/*     */   }
/*     */ 
/*     */   public EdgeIterator getEdgeIterator() {
/*  73 */     return new SingleEdgeIterator(getLabelNum());
/*     */   }
/*     */ 
/*     */   private int computeLabelNum(int originalLabelNum, int markovOrder)
/*     */   {
/*  79 */     int labelNum = originalLabelNum;
/*  80 */     int i = 1;
/*  81 */     while (i < markovOrder) {
/*  82 */       labelNum *= originalLabelNum;
/*  83 */       i++;
/*     */     }
/*  85 */     return labelNum;
/*     */   }
/*     */   private class SingleEdgeIterator implements EdgeIterator {
/*     */     private int labelNum;
/*     */     private Edge edge;
/*     */     private Edge edgeToReturn;
/*     */ 
/*  94 */     public SingleEdgeIterator(int labelNum) { this.labelNum = labelNum;
/*  95 */       this.edge = new Edge();
/*  96 */       this.edgeToReturn = new Edge();
/*  97 */       start(); }
/*     */ 
/*     */     public void start()
/*     */     {
/* 101 */       this.edge.setStart(0);
/* 102 */       this.edge.setEnd(0);
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 106 */       return this.edge.getStart() < this.labelNum;
/*     */     }
/*     */ 
/*     */     public Edge next() {
/* 110 */       this.edgeToReturn.setStart(this.edge.getStart());
/* 111 */       this.edgeToReturn.setEnd(this.edge.getEnd());
/* 112 */       this.edge.setEnd(this.edge.getEnd() + 1);
/* 113 */       if (this.edge.getEnd() == this.labelNum) {
/* 114 */         this.edge.setEnd(0);
/* 115 */         this.edge.setStart(this.edge.getStart() + 1);
/*     */       }
/* 117 */       return this.edgeToReturn;
/*     */     }
/*     */ 
/*     */     public boolean nextIsOuter() {
/* 121 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.model.CompleteModel
 * JD-Core Version:    0.6.2
 */