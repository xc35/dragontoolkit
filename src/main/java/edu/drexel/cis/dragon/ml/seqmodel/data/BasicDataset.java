/*    */ package edu.drexel.cis.dragon.ml.seqmodel.data;
/*    */ 
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class BasicDataset
/*    */   implements Dataset
/*    */ {
/*    */   private Vector vector;
/*    */   private int originalLabelNum;
/*    */   private int labelNum;
/*    */   private int markovOrder;
/*    */   private int curPos;
/*    */ 
/*    */   public BasicDataset(int originalLabelNum, int markovOrder)
/*    */   {
/* 20 */     this.vector = new Vector();
/* 21 */     this.originalLabelNum = originalLabelNum;
/* 22 */     this.markovOrder = markovOrder;
/* 23 */     this.labelNum = 1;
/* 24 */     for (int i = 0; i < markovOrder; i++) this.labelNum *= originalLabelNum;
/*    */   }
/*    */ 
/*    */   public Dataset copy()
/*    */   {
/* 30 */     BasicDataset dataset = new BasicDataset(this.originalLabelNum, this.markovOrder);
/* 31 */     startScan();
/* 32 */     while (hasNext()) {
/* 33 */       dataset.add(next().copy());
/*    */     }
/* 35 */     return dataset;
/*    */   }
/*    */ 
/*    */   public int size() {
/* 39 */     return this.vector.size();
/*    */   }
/*    */ 
/*    */   public void startScan() {
/* 43 */     this.curPos = 0;
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 47 */     return this.curPos < this.vector.size();
/*    */   }
/*    */ 
/*    */   public DataSequence next() {
/* 51 */     this.curPos += 1;
/* 52 */     return (DataSequence)this.vector.get(this.curPos - 1);
/*    */   }
/*    */ 
/*    */   public boolean add(DataSequence seq) {
/* 56 */     seq.setParent(this);
/* 57 */     this.vector.add(seq);
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */   public int getLabelNum() {
/* 62 */     return this.labelNum;
/*    */   }
/*    */ 
/*    */   public int getOriginalLabelNum() {
/* 66 */     return this.originalLabelNum;
/*    */   }
/*    */ 
/*    */   public int getMarkovOrder() {
/* 70 */     return this.markovOrder;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.data.BasicDataset
 * JD-Core Version:    0.6.2
 */