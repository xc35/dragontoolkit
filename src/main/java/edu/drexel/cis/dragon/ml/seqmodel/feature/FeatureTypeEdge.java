/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.model.Edge;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.model.EdgeIterator;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.model.ModelGraph;
/*    */ 
/*    */ public class FeatureTypeEdge extends AbstractFeatureType
/*    */ {
/*    */   private ModelGraph model;
/*    */   private EdgeIterator edgeIter;
/*    */   private int curEdgeIndex;
/*    */ 
/*    */   public FeatureTypeEdge(ModelGraph model)
/*    */   {
/* 23 */     super(false);
/* 24 */     this.model = model;
/* 25 */     this.edgeIter = null;
/*    */   }
/*    */ 
/*    */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos) {
/* 29 */     if (startPos <= 0) {
/* 30 */       this.curEdgeIndex = this.model.getEdgeNum();
/* 31 */       return false;
/*    */     }
/*    */ 
/* 34 */     this.curEdgeIndex = 0;
/* 35 */     this.edgeIter = this.model.getEdgeIterator();
/* 36 */     if (this.edgeIter != null)
/* 37 */       this.edgeIter.start();
/* 38 */     return hasNext();
/*    */   }
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 43 */     return (this.edgeIter != null) && (this.curEdgeIndex < this.model.getEdgeNum());
/*    */   }
/*    */ 
/*    */   public Feature next()
/*    */   {
/* 53 */     boolean edgeIsOuter = this.edgeIter.nextIsOuter();
/* 54 */     Edge e = this.edgeIter.next();
/* 55 */     String name = "E." + this.model.getLabel(e.getStart());
/*    */     FeatureIdentifier id;
/* 56 */     if (edgeIsOuter)
/* 57 */       id = new FeatureIdentifier(name, this.model.getLabel(e.getStart()) * this.model.getLabelNum() + this.model.getLabel(e.getEnd()) + this.model.getEdgeNum(), this.model.getLabel(e.getEnd()));
/*    */     else
/* 59 */       id = new FeatureIdentifier(name, this.curEdgeIndex, e.getEnd());
/* 60 */     Feature f = new BasicFeature(id, e.getStart(), e.getEnd(), 1.0D);
/* 61 */     this.curEdgeIndex += 1;
/* 62 */     return f;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeEdge
 * JD-Core Version:    0.6.2
 */