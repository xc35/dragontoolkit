/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ 
/*    */ public class FeatureTypeWindow extends AbstractFeatureTypeWrapper
/*    */ {
/*    */   protected int currentWindow;
/*    */   protected int startPos;
/*    */   protected int endPos;
/*    */   protected transient DataSequence dataSeq;
/*    */   protected Window[] windows;
/*    */   private int dataLen;
/*    */ 
/*    */   public FeatureTypeWindow(Window[] windows, FeatureType ftype)
/*    */   {
/* 23 */     super(ftype);
/* 24 */     this.windows = windows;
/*    */   }
/*    */ 
/*    */   protected boolean advance(boolean firstCall)
/*    */   {
/* 30 */     while ((firstCall) || (!this.ftype.hasNext())) {
/* 31 */       this.currentWindow -= 1;
/* 32 */       if (this.currentWindow < 0) {
/* 33 */         return false;
/*    */       }
/* 35 */       if ((this.windows[this.currentWindow].getMaxLength() >= this.endPos + 1 - this.startPos) && 
/* 36 */         (this.windows[this.currentWindow].getMinLength() <= this.endPos + 1 - this.startPos)) {
/* 37 */         int rightB = this.windows[this.currentWindow].rightBoundary(this.startPos, this.endPos);
/* 38 */         int leftB = this.windows[this.currentWindow].leftBoundary(this.startPos, this.endPos);
/*    */ 
/* 40 */         if ((leftB < this.dataLen) && (rightB >= 0) && (leftB <= rightB)) {
/* 41 */           this.ftype.startScanFeaturesAt(this.dataSeq, Math.max(leftB, 0), Math.min(rightB, this.dataLen - 1));
/* 42 */           firstCall = false;
/*    */         }
/*    */       }
/*    */     }
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos) {
/* 49 */     this.currentWindow = this.windows.length;
/* 50 */     this.dataSeq = data;
/* 51 */     this.dataLen = this.dataSeq.length();
/* 52 */     this.startPos = startPos;
/* 53 */     this.endPos = endPos;
/* 54 */     return advance(true);
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 58 */     return (this.ftype.hasNext()) && (this.currentWindow >= 0);
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 65 */     Feature f = this.ftype.next();
/* 66 */     FeatureIdentifier id = f.getID();
/* 67 */     id.setName(id.getName() + ".W." + this.windows[this.currentWindow]);
/* 68 */     id.setId(id.getId() * this.windows.length + this.currentWindow);
/* 69 */     advance(false);
/* 70 */     return f;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeWindow
 * JD-Core Version:    0.6.2
 */