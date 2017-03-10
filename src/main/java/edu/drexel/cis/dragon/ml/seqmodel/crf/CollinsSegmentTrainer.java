/*    */ package edu.drexel.cis.dragon.ml.seqmodel.crf;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.feature.FeatureGenerator;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.model.ModelGraph;
/*    */ 
/*    */ public class CollinsSegmentTrainer extends CollinsBasicTrainer
/*    */ {
/*    */   private int maxSegmentLength;
/*    */ 
/*    */   public CollinsSegmentTrainer(ModelGraph model, FeatureGenerator featureGenerator, int maxSegmentLength)
/*    */   {
/* 20 */     super(model, featureGenerator);
/* 21 */     this.maxSegmentLength = maxSegmentLength;
/*    */   }
/*    */ 
/*    */   protected Labeler getLabeler() {
/* 25 */     return new ViterbiSegmentLabeler(this.model, this.featureGenerator, this.maxSegmentLength);
/*    */   }
/*    */ 
/*    */   protected int getSegmentEnd(DataSequence dataSeq, int start) {
/* 29 */     return dataSeq.getSegmentEnd(start);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.crf.CollinsSegmentTrainer
 * JD-Core Version:    0.6.2
 */