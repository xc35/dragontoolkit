/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.BasicToken;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class FeatureTypeKnownWord extends AbstractFeatureType
/*    */ {
/*    */   private FeatureDictionary dict;
/*    */   private int stateNum;
/*    */   private int curState;
/*    */   private double wordFreq;
/*    */   private int wordIndex;
/*    */   private boolean caseSensitive;
/*    */ 
/*    */   public FeatureTypeKnownWord(FeatureDictionary wordDict)
/*    */   {
/* 25 */     this(wordDict, false);
/*    */   }
/*    */ 
/*    */   public FeatureTypeKnownWord(FeatureDictionary wordDict, boolean caseSensitive) {
/* 29 */     super(false);
/* 30 */     this.dict = wordDict;
/* 31 */     this.stateNum = this.dict.getStateNum();
/* 32 */     this.idPrefix = "K_";
/*    */   }
/*    */ 
/*    */   private void getNextState() {
/* 36 */     for (this.curState += 1; this.curState < this.stateNum; this.curState += 1)
/* 37 */       if (this.dict.getCount(this.wordIndex, this.curState) == 0)
/* 38 */         return;
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos)
/*    */   {
/* 46 */     this.curState = -1;
/* 47 */     if (startPos != endPos) {
/* 48 */       System.out.println("The starting position and the ending position should be the same for word features");
/* 49 */       return false;
/*    */     }
/*    */ 
/* 52 */     this.wordIndex = data.getToken(endPos).getIndex();
/* 53 */     if (this.wordIndex < 0) {
/* 54 */       String token = data.getToken(endPos).getContent();
/* 55 */       if (!this.caseSensitive)
/* 56 */         token = token.toLowerCase();
/* 57 */       this.wordIndex = this.dict.getIndex(token);
/*    */     }
/* 59 */     if (this.wordIndex < 0) {
/* 60 */       return false;
/*    */     }
/* 62 */     if (this.dict.getCount(this.wordIndex) <= FeatureTypeWord.RARE_THRESHOLD + 1) {
/* 63 */       return false;
/*    */     }
/* 65 */     getNextState();
/* 66 */     this.wordFreq = Math.log(this.dict.getCount(this.wordIndex) / this.dict.getTotalCount());
/* 67 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 71 */     return (this.curState < this.stateNum) && (this.curState >= 0);
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 78 */     FeatureIdentifier id = new FeatureIdentifier(this.idPrefix, this.curState, this.curState);
/* 79 */     BasicFeature f = new BasicFeature(id, this.curState, this.wordFreq);
/* 80 */     getNextState();
/* 81 */     return f;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeKnownWord
 * JD-Core Version:    0.6.2
 */