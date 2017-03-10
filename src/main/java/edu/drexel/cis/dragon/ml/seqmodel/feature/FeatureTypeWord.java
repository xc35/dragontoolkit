/*     */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.data.BasicToken;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.data.Dataset;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class FeatureTypeWord extends AbstractFeatureType
/*     */ {
/*  18 */   public static int RARE_THRESHOLD = 0;
/*     */   private FeatureDictionary dict;
/*     */   private String wordFile;
/*     */   private int stateNum;
/*     */   private int curState;
/*     */   private String token;
/*     */   private int tokenId;
/*     */   private boolean caseSensitive;
/*     */ 
/*     */   public FeatureTypeWord(String wordFile, int stateNum)
/*     */   {
/*  28 */     this(wordFile, stateNum, false);
/*     */   }
/*     */ 
/*     */   public FeatureTypeWord(String wordFile, int stateNum, boolean caseSensitive) {
/*  32 */     super(true);
/*  33 */     this.caseSensitive = caseSensitive;
/*  34 */     this.stateNum = stateNum;
/*  35 */     this.wordFile = wordFile;
/*  36 */     this.dict = new FeatureDictionaryChar(stateNum, 500);
/*  37 */     this.idPrefix = "W_";
/*     */   }
/*     */ 
/*     */   public FeatureTypeWord(FeatureDictionary dict, int stateNum) {
/*  41 */     this(dict, stateNum, false);
/*     */   }
/*     */ 
/*     */   public FeatureTypeWord(FeatureDictionary dict, int stateNum, boolean caseSensitive) {
/*  45 */     super(false);
/*  46 */     this.caseSensitive = caseSensitive;
/*  47 */     this.stateNum = stateNum;
/*  48 */     this.wordFile = null;
/*  49 */     this.dict = dict;
/*  50 */     this.idPrefix = "W_";
/*     */   }
/*     */ 
/*     */   public FeatureDictionary getWordDictionary() {
/*  54 */     return this.dict;
/*     */   }
/*     */ 
/*     */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos)
/*     */   {
/*  59 */     this.curState = -1;
/*  60 */     if (startPos != endPos) {
/*  61 */       System.out.println("The starting position and the ending position should be the same for word features");
/*  62 */       return false;
/*     */     }
/*     */ 
/*  65 */     this.tokenId = data.getToken(endPos).getIndex();
/*  66 */     if (this.tokenId < 0) {
/*  67 */       this.token = data.getToken(endPos).getContent();
/*  68 */       if (!this.caseSensitive)
/*  69 */         this.token = this.token.toLowerCase();
/*  70 */       this.tokenId = this.dict.getIndex(this.token);
/*     */     }
/*  72 */     if (this.tokenId < 0) {
/*  73 */       return false;
/*     */     }
/*  75 */     if (this.dict.getCount(this.tokenId) > RARE_THRESHOLD) {
/*  76 */       getNextState();
/*  77 */       return true;
/*     */     }
/*     */ 
/*  80 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean hasNext() {
/*  84 */     return (this.curState >= 0) && (this.curState < this.stateNum);
/*     */   }
/*     */ 
/*     */   public Feature next()
/*     */   {
/*  91 */     FeatureIdentifier id = new FeatureIdentifier(this.idPrefix + this.token, this.tokenId * this.stateNum + this.curState, this.curState);
/*  92 */     BasicFeature f = new BasicFeature(id, this.curState, 1.0D);
/*  93 */     getNextState();
/*  94 */     return f;
/*     */   }
/*     */ 
public boolean train(Dataset trainData) {
    DataSequence seq;
    BasicToken token;
    int pos, index;

    for (trainData.startScan(); trainData.hasNext(); ) {
        seq = trainData.next();
        for (pos = 0; pos < seq.length(); pos++) {
            if(pos>=0 && pos<seq.length()){
                token = seq.getToken(pos);
                if(caseSensitive)
                    index=dict.addFeature(token.getContent(), seq.getLabel(pos));
                else
                    index=dict.addFeature(token.getContent().toLowerCase(), seq.getLabel(pos));
                token.setIndex(index);
            }
        }
    }
    dict.finalize();
    return true;
}

/*     */   public boolean readTrainingResult() {
/* 120 */     return this.dict.read(this.wordFile);
/*     */   }
/*     */ 
/*     */   public boolean saveTrainingResult() {
/* 124 */     return this.dict.write(this.wordFile);
/*     */   }
/*     */ 
/*     */   private void getNextState() {
/* 128 */     if (needTraining())
/* 129 */       this.curState = this.dict.getNextStateWithFeature(this.tokenId, this.curState);
/*     */     else
/* 131 */       this.curState += 1;
/*     */   }
/*     */ 
/*     */   public boolean supportSegment() {
/* 135 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeWord
 * JD-Core Version:    0.6.2
 */