/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.BasicToken;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class FeatureTypeUnknownWord extends AbstractFeatureType
/*    */ {
/*    */   private int curState;
/*    */   private FeatureDictionary dict;
/*    */   private int stateNum;
/*    */   private boolean caseSensitive;
/*    */ 
/*    */   public FeatureTypeUnknownWord(FeatureDictionary d)
/*    */   {
/* 21 */     this(d, false);
/*    */   }
/*    */ 
/*    */   public FeatureTypeUnknownWord(FeatureDictionary d, boolean caseSensitive) {
/* 25 */     super(false);
/* 26 */     this.caseSensitive = caseSensitive;
/* 27 */     this.dict = d;
/* 28 */     this.idPrefix = "UW";
/* 29 */     this.stateNum = d.getStateNum();
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos)
/*    */   {
/* 36 */     this.curState = this.stateNum;
/* 37 */     if (startPos != endPos) {
/* 38 */       System.out.println("The starting position and the ending position should be the same for unknown word features");
/* 39 */       return false;
/*    */     }
/*    */     int count;
/* 42 */     if (data.getToken(endPos).getIndex() >= 0) {
/* 43 */       count = this.dict.getCount(data.getToken(endPos).getIndex());
/*    */     } else {
/* 45 */       String token = data.getToken(endPos).getContent();
/* 46 */       if (!this.caseSensitive)
/* 47 */         token = token.toLowerCase();
/* 48 */       count = this.dict.getCount(token);
/*    */     }
/* 50 */     if (count > FeatureTypeWord.RARE_THRESHOLD + 1) {
/* 51 */       return false;
/*    */     }
/* 53 */     this.curState = 0;
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 59 */     return this.curState < this.stateNum;
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 66 */     FeatureIdentifier id = new FeatureIdentifier(this.idPrefix, this.curState, this.curState);
/* 67 */     Feature f = new BasicFeature(id, this.curState, 1.0D);
/* 68 */     this.curState += 1;
/* 69 */     return f;
/*    */   }
/*    */ 
/*    */   public boolean supportSegment() {
/* 73 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeUnknownWord
 * JD-Core Version:    0.6.2
 */