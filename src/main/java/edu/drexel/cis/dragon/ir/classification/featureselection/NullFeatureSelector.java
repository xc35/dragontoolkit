/*    */ package edu.drexel.cis.dragon.ir.classification.featureselection;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.classification.DocClassSet;
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class NullFeatureSelector extends AbstractFeatureSelector
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   protected int[] getSelectedFeatures(IndexReader indexReader, DocClassSet trainingSet)
/*    */   {
/* 21 */     int[] featureMap = new int[indexReader.getCollection().getTermNum()];
/* 22 */     for (int i = 0; i < featureMap.length; i++)
/* 23 */       featureMap[i] = i;
/* 24 */     return featureMap;
/*    */   }
/*    */ 
/*    */   protected int[] getSelectedFeatures(SparseMatrix doctermMatrix, DocClassSet trainingSet)
/*    */   {
/* 30 */     int[] featureMap = new int[doctermMatrix.columns()];
/* 31 */     for (int i = 0; i < featureMap.length; i++)
/* 32 */       featureMap[i] = i;
/* 33 */     return featureMap;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.featureselection.NullFeatureSelector
 * JD-Core Version:    0.6.2
 */