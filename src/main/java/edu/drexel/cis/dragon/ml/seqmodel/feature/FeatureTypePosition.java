/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ 
/*    */ public class FeatureTypePosition extends AbstractFeatureTypeWrapper
/*    */ {
/*    */   private Feature savedFeature;
/*    */   private boolean squareSent;
/*    */   private int segStart;
/*    */   private int segEnd;
/*    */   private int currPos;
/*    */   private transient DataSequence dataSeq;
/*    */ 
/*    */   public FeatureTypePosition(FeatureType ftype)
/*    */   {
/* 20 */     super(ftype);
/*    */   }
/*    */ 
/*    */   private void advance() {
/*    */     while (true) {
/* 25 */       if (this.ftype.hasNext())
/* 26 */         return;
/* 27 */       this.currPos += 1;
/* 28 */       if (this.currPos > this.segEnd)
/* 29 */         return;
/* 30 */       this.ftype.startScanFeaturesAt(this.dataSeq, this.currPos, this.currPos);
/*    */     }
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int pos) {
/* 35 */     return startScanFeaturesAt(data, pos, pos);
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos) {
/* 39 */     this.segStart = startPos;
/* 40 */     this.segEnd = endPos;
/* 41 */     this.currPos = startPos;
/* 42 */     this.squareSent = true;
/* 43 */     this.dataSeq = data;
/* 44 */     this.ftype.startScanFeaturesAt(data, startPos, startPos);
/* 45 */     advance();
/* 46 */     return this.ftype.hasNext();
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 50 */     return (!this.squareSent) || ((this.currPos <= this.segEnd) && (this.ftype.hasNext()));
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 57 */     if (!this.squareSent) {
/* 58 */       this.squareSent = true;
/*    */ 
/* 60 */       this.savedFeature.setValue(this.savedFeature.getValue() * this.savedFeature.getValue());
/* 61 */       FeatureIdentifier id = this.savedFeature.getID();
/* 62 */       id.setName("POS^2" + id.getName());
/* 63 */       id.setId(id.getId() * 2 + 1);
/* 64 */       advance();
/* 65 */       return this.savedFeature;
/*    */     }
/*    */ 
/* 68 */     Feature f = this.ftype.next();
/* 69 */     f.setValue((this.currPos - this.segStart + 1) / (this.segEnd - this.segStart + 1));
/* 70 */     this.savedFeature = f.copy();
/* 71 */     this.squareSent = false;
/* 72 */     FeatureIdentifier id = f.getID();
/* 73 */     id.setName("POS_" + id.getName());
/* 74 */     id.setId(id.getId() * 2);
/* 75 */     return f;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypePosition
 * JD-Core Version:    0.6.2
 */