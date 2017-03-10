/*    */ package edu.drexel.cis.dragon.ir.clustering.featurefilter;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ 
/*    */ public class NullFeatureFilter extends AbstractFeatureFilter
/*    */ {
/*    */   protected int[] getSelectedFeatures(IndexReader indexReader, IRDoc[] docSet)
/*    */   {
/* 19 */     int[] featureMap = new int[indexReader.getCollection().getTermNum()];
/* 20 */     for (int i = 0; i < featureMap.length; i++)
/* 21 */       featureMap[i] = i;
/* 22 */     return featureMap;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.featurefilter.NullFeatureFilter
 * JD-Core Version:    0.6.2
 */