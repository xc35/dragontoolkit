/*     */ package edu.drexel.cis.dragon.ml.seqmodel.data;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class BasicDataSequence
/*     */   implements DataSequence
/*     */ {
/*     */   private Vector vector;
/*     */   private Dataset parent;
/*     */ 
/*     */   public BasicDataSequence()
/*     */   {
/*  19 */     this(50);
/*     */   }
/*     */ 
/*     */   public BasicDataSequence(int length) {
/*  23 */     this.parent = null;
/*  24 */     this.vector = new Vector(length, 50);
/*     */   }
/*     */ 
/*     */   public DataSequence copy()
/*     */   {
/*  31 */     int len = length();
/*  32 */     BasicDataSequence seq = new BasicDataSequence(length());
/*  33 */     for (int i = 0; i < len; i++)
/*  34 */       seq.add(getToken(i).copy());
/*  35 */     return seq;
/*     */   }
/*     */ 
/*     */   public Dataset getParent() {
/*  39 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void setParent(Dataset parent) {
/*  43 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   public int length() {
/*  47 */     return this.vector.size();
/*     */   }
/*     */ 
/*     */   public int getLabel(int pos)
/*     */   {
/*  54 */     int markovOrder = this.parent.getMarkovOrder();
/*  55 */     if (markovOrder <= 1) {
/*  56 */       BasicToken token = (BasicToken)this.vector.get(pos);
/*  57 */       return token.getLabel();
/*     */     }
/*     */ 
/*  60 */     if (pos >= markovOrder - 1) {
/*  61 */       int label = 0;
/*  62 */       for (int i = pos + 1 - markovOrder; i <= pos; i++) {
/*  63 */         BasicToken token = (BasicToken)this.vector.get(i);
/*  64 */         label = label * this.parent.getOriginalLabelNum() + token.getLabel();
/*     */       }
/*  66 */       return label;
/*     */     }
/*     */ 
/*  69 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getOriginalLabel(int pos)
/*     */   {
/*  74 */     return ((BasicToken)this.vector.get(pos)).getLabel();
/*     */   }
/*     */ 
/*     */   public BasicToken getToken(int pos) {
/*  78 */     return (BasicToken)this.vector.get(pos);
/*     */   }
/*     */ 
/*     */   public void setLabel(int pos, int label)
/*     */   {
/*  85 */     BasicToken token = (BasicToken)this.vector.get(pos);
/*  86 */     int markovOrder = this.parent.getMarkovOrder();
/*  87 */     if (markovOrder > 1) {
/*  88 */       token.setLabel(label % this.parent.getOriginalLabelNum());
/*     */ 
/*  90 */       if (pos == markovOrder - 1) {
/*  91 */         label /= this.parent.getOriginalLabelNum();
/*  92 */         while (pos > 0) {
/*  93 */           pos--;
/*  94 */           label /= this.parent.getOriginalLabelNum();
/*  95 */           token = (BasicToken)this.vector.get(pos);
/*  96 */           token.setLabel(label % this.parent.getOriginalLabelNum());
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 101 */       token.setLabel(label);
/*     */     }
/*     */   }
/*     */ 
/* 105 */   public void add(BasicToken token) { this.vector.add(token); }
/*     */ 
/*     */ 
/*     */   public int getSegmentEnd(int segmentStart)
/*     */   {
/* 111 */     int len = length();
/* 112 */     int curPos = segmentStart + 1;
/* 113 */     while (curPos < len) {
/* 114 */       if (getToken(curPos).isSegmentStart()) {
/*     */         break;
/*     */       }
/* 117 */       curPos++;
/*     */     }
/* 119 */     return curPos - 1;
/*     */   }
/*     */ 
/*     */   public void setSegment(int segmentStart, int segmentEnd, int label)
/*     */   {
/* 126 */     if (this.parent.getMarkovOrder() > 1) {
/* 127 */       System.out.println("Only first-order markov allowed for segment sequencing!");
/* 128 */       return;
/*     */     }
/*     */ 
/* 131 */     BasicToken curToken = getToken(segmentStart);
/* 132 */     curToken.setSegmentMarker(true);
/* 133 */     curToken.setLabel(label);
/* 134 */     for (int i = segmentStart + 1; i <= segmentEnd; i++) {
/* 135 */       curToken = getToken(i);
/* 136 */       curToken.setSegmentMarker(false);
/* 137 */       curToken.setLabel(label);
/*     */     }
/* 139 */     if (segmentEnd < length() - 1)
/* 140 */       getToken(segmentEnd + 1).setSegmentMarker(true);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.data.BasicDataSequence
 * JD-Core Version:    0.6.2
 */