/*     */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.data.Dataset;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.model.ModelGraph;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class BasicFeatureGenerator
/*     */   implements FeatureGenerator
/*     */ {
/*     */   protected ModelGraph model;
/*     */   protected Vector featureVector;
/*     */   protected Iterator featureIter;
/*     */   protected FeatureType currentFeatureType;
/*     */   protected FeatureMap featureMap;
/*     */   protected Feature featureToReturn;
/*     */   protected DataSequence curSeq;
/*     */   protected int curStartPos;
/*     */   protected int curEndPos;
/*     */   protected int totalFeatures;
/*     */   protected boolean featureCollectingMode;
/*     */   protected boolean supportSegment;
/*     */ 
/*     */   public BasicFeatureGenerator(ModelGraph model)
/*     */   {
/*  42 */     this(model, false);
/*     */   }
/*     */ 
/*     */   public BasicFeatureGenerator(ModelGraph model, boolean supportSegment) {
/*  46 */     this.model = model;
/*  47 */     this.totalFeatures = 0;
/*  48 */     this.supportSegment = supportSegment;
/*  49 */     this.featureVector = new Vector();
/*  50 */     this.featureToReturn = null;
/*  51 */     this.featureMap = new FeatureMap();
/*  52 */     this.featureCollectingMode = false;
/*     */   }
/*     */ 
/*     */   public boolean supportSegment() {
/*  56 */     return this.supportSegment;
/*     */   }
/*     */ 
/*     */   public boolean addFeatureType(FeatureType fType) {
/*  60 */     if (this.featureMap.isFrozen())
/*  61 */       return false;
/*  62 */     if ((supportSegment()) && (!fType.supportSegment())) {
/*  63 */       return false;
/*     */     }
/*  65 */     fType.setTypeID(this.featureVector.size());
/*  66 */     this.featureVector.add(fType);
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */   public int getFeatureTypeNum() {
/*  71 */     return this.featureVector.size();
/*     */   }
/*     */ 
/*     */   public FeatureType getFeatureTYpe(int index) {
/*  75 */     return (FeatureType)this.featureVector.get(index);
/*     */   }
/*     */ 
/*     */   protected FeatureType getFeatureType(int i) {
/*  79 */     return (FeatureType)this.featureVector.elementAt(i);
/*     */   }
/*     */ 
/*     */   public boolean train(Dataset trainData)
/*     */   {
/*  87 */     for (int i = 0; i < this.featureVector.size(); i++) {
/*  88 */       FeatureType cur = getFeatureType(i);
/*  89 */       if (cur.needTraining()) {
/*  90 */         if (!cur.train(trainData))
/*  91 */           return false;
/*  92 */         cur.saveTrainingResult();
/*     */       }
/*     */     }
/*  95 */     collectFeatureIdentifiers(trainData);
/*  96 */     this.totalFeatures = this.featureMap.getFeatureNum();
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean loadFeatureData()
/*     */   {
/* 105 */     for (int i = 0; i < this.featureVector.size(); i++) {
/* 106 */       FeatureType cur = getFeatureType(i);
/* 107 */       if (cur.needTraining())
/* 108 */         cur.readTrainingResult();
/*     */     }
/* 110 */     return true;
/*     */   }
/*     */ 
/*     */   protected void collectFeatureIdentifiers(Dataset trainData)
/*     */   {
    DataSequence seq;
    int segStart, segEnd;

    featureCollectingMode = true;
    for (trainData.startScan(); trainData.hasNext(); ) {
        seq = trainData.next();
        segStart=0;
        while(segStart<seq.length()){
            if(supportSegment)
                segEnd=seq.getSegmentEnd(segStart);
            else
                segEnd=segStart;
            for (startScanFeaturesAt(seq, segStart, segEnd); hasNext(); ) {
                next();
            }
            segStart=segEnd+1;
        }
    }
    featureCollectingMode = false;
    featureMap.freezeFeatures();
/*     */   }
/*     */ 
/*     */   protected void advance()
/*     */   {
    FeatureIdentifier id;
    int featureIndex;

    while (true) {
        for (;((currentFeatureType == null) || !currentFeatureType.hasNext()) && featureIter.hasNext();) {
            currentFeatureType = (FeatureType)featureIter.next();
        }
        if (!currentFeatureType.hasNext())
            break;
        while (currentFeatureType.hasNext()) {
            featureToReturn=currentFeatureType.next();
            id=featureToReturn.getID();
            //gurantee feature id is unique as long as the id within the feature type is unique.
            id.setId(id.getId()*getFeatureTypeNum()+currentFeatureType.getTypeID());
            featureIndex=featureMap.getId(id);
            if(featureIndex<0 && featureCollectingMode){
                if(retainFeature(curSeq,featureToReturn))
                    featureIndex=featureMap.add(id);
            }

            if (featureIndex < 0){
                featureToReturn=null;
                continue;
            }
            featureToReturn.setIndex(featureIndex);

            if(!isValidFeature(curSeq,curStartPos,curEndPos,featureToReturn)){
                featureToReturn = null;
                continue;
            }
            return;
        }
    }
    featureToReturn=null;
/*     */   }
/*     */ 
/*     */   protected boolean isValidFeature(DataSequence data, int curStartPos, int curEndPos, Feature featureToReturn) {
/* 174 */     if ((curStartPos > 0) && (curEndPos < data.length() - 1))
/* 175 */       return true;
/* 176 */     if ((curStartPos == 0) && (this.model.isStartState(featureToReturn.getLabel())) && (
/* 177 */       (data.length() > 1) || (this.model.isEndState(featureToReturn.getLabel()))))
/* 178 */       return true;
/* 179 */     if ((curEndPos == data.length() - 1) && (this.model.isEndState(featureToReturn.getLabel())))
/* 180 */       return true;
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean retainFeature(DataSequence seq, Feature f)
/*     */   {
/* 186 */     return (seq.getLabel(this.curEndPos) == f.getLabel()) && (
/* 186 */       (this.curStartPos == 0) || (f.getPrevLabel() < 0) || (seq.getLabel(this.curStartPos - 1) == f.getPrevLabel()));
/*     */   }
/*     */ 
/*     */   protected void initScanFeaturesAt(DataSequence d) {
/* 190 */     this.curSeq = d;
/* 191 */     this.currentFeatureType = null;
/* 192 */     this.featureIter = this.featureVector.iterator();
/* 193 */     advance();
/*     */   }
/*     */ 
/*     */   public void startScanFeaturesAt(DataSequence d, int startPos, int endPos) {
/* 197 */     this.curStartPos = startPos;
/* 198 */     this.curEndPos = endPos;
/* 199 */     for (int i = 0; i < this.featureVector.size(); i++) {
/* 200 */       getFeatureType(i).startScanFeaturesAt(d, startPos, endPos);
/*     */     }
/* 202 */     initScanFeaturesAt(d);
/*     */   }
/*     */ 
/*     */   public boolean hasNext() {
/* 206 */     return this.featureToReturn != null;
/*     */   }
/*     */ 
/*     */   public Feature next()
/*     */   {
/* 212 */     Feature cur = this.featureToReturn.copy();
/* 213 */     advance();
/* 214 */     return cur;
/*     */   }
/*     */ 
/*     */   public int getFeatureNum() {
/* 218 */     return this.totalFeatures;
/*     */   }
/*     */ 
/*     */   public String getFeatureName(int featureIndex) {
/* 222 */     return this.featureMap.getName(featureIndex);
/*     */   }
/*     */ 
/*     */   public boolean readFeatures(String fileName)
/*     */   {
/*     */     try
/*     */     {
/* 230 */       BufferedReader in = new BufferedReader(new FileReader(fileName));
/* 231 */       this.totalFeatures = this.featureMap.read(in);
/* 232 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 235 */       e.printStackTrace();
/* 236 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean saveFeatures(String fileName)
/*     */   {
/*     */     try
/*     */     {
/* 245 */       PrintWriter out = new PrintWriter(new FileOutputStream(fileName));
/* 246 */       this.featureMap.write(out);
/* 247 */       out.close();
/* 248 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 251 */       e.printStackTrace();
/* 252 */     }return false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.BasicFeatureGenerator
 * JD-Core Version:    0.6.2
 */