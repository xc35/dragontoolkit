/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.BasicToken;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class FeatureTypeWordScore extends AbstractFeatureType
/*    */ {
/*    */   private FeatureDictionary dict;
/*    */   private int curState;
/*    */   private int tokenId;
/*    */   private int stateNum;
/*    */   private boolean caseSensitive;
/*    */ 
/*    */   public FeatureTypeWordScore(FeatureDictionary d)
/*    */   {
/* 21 */     this(d, false);
/*    */   }
/*    */ 
/*    */   public FeatureTypeWordScore(FeatureDictionary d, boolean caseSensitive) {
/* 25 */     super(false);
/* 26 */     this.caseSensitive = caseSensitive;
/* 27 */     this.dict = d;
/* 28 */     this.stateNum = d.getStateNum();
/* 29 */     this.idPrefix = "WS_";
/*    */   }
/*    */ 
/*    */   private void getNextLabel() {
/* 33 */     this.curState = this.dict.getNextStateWithFeature(this.tokenId, this.curState);
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos)
/*    */   {
/* 39 */     this.curState = -1;
/* 40 */     if (startPos != endPos) {
/* 41 */       System.out.println("The starting position and the ending position should be the same for word score features");
/* 42 */       return false;
/*    */     }
/*    */ 
/* 45 */     this.tokenId = data.getToken(endPos).getIndex();
/* 46 */     if (this.tokenId < 0) {
/* 47 */       String token = data.getToken(endPos).getContent();
/* 48 */       if (!this.caseSensitive)
/* 49 */         token = token.toLowerCase();
/* 50 */       this.tokenId = this.dict.getIndex(token);
/*    */     }
/* 52 */     if (this.tokenId < 0) {
/* 53 */       return false;
/*    */     }
/* 55 */     if (this.dict.getCount(this.tokenId) > FeatureTypeWord.RARE_THRESHOLD) {
/* 56 */       getNextLabel();
/* 57 */       return true;
/*    */     }
/*    */ 
/* 60 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 64 */     return (this.curState < this.stateNum) && (this.curState >= 0);
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 72 */     FeatureIdentifier id = new FeatureIdentifier(this.idPrefix, this.curState, this.curState);
/* 73 */     double val = Math.log(this.dict.getCount(this.tokenId, this.curState) / this.dict.getStateCount(this.curState));
/* 74 */     BasicFeature f = new BasicFeature(id, this.curState, val);
/* 75 */     getNextLabel();
/* 76 */     return f;
/*    */   }
/*    */ 
/*    */   public boolean supportSegment() {
/* 80 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeWordScore
 * JD-Core Version:    0.6.2
 */