/*    */ package edu.drexel.cis.dragon.ir.classification.featureselection;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.classification.DocClassSet;
/*    */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ import edu.drexel.cis.dragon.matrix.IntDenseMatrix;
/*    */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class DocFrequencySelector extends AbstractFeatureSelector
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private int minDocFrequency;
/*    */ 
/*    */   public DocFrequencySelector(int minDocFrequency)
/*    */   {
/* 25 */     this.minDocFrequency = minDocFrequency;
/*    */   }
/*    */ 
/*    */   protected int[] getSelectedFeatures(SparseMatrix doctermMatrix, DocClassSet trainingSet)
/*    */   {
/* 33 */     int[] featureMap = getTermDocFrequency(doctermMatrix, trainingSet);
/* 34 */     int termNum = featureMap.length;
/* 35 */     ArrayList list = new ArrayList(termNum);
/* 36 */     for (int i = 0; i < termNum; i++) {
/* 37 */       if (featureMap[i] >= this.minDocFrequency) {
/* 38 */         list.add(new Integer(i));
/*    */       }
/*    */     }
/* 41 */     featureMap = new int[list.size()];
/* 42 */     for (int i = 0; i < featureMap.length; i++)
/* 43 */       featureMap[i] = ((Integer)list.get(i)).intValue();
/* 44 */     return featureMap;
/*    */   }
/*    */ 
/*    */   protected int[] getSelectedFeatures(IndexReader indexReader, DocClassSet trainingSet)
/*    */   {
/* 54 */     IntDenseMatrix termDistri = getTermDistribution(indexReader, trainingSet);
/* 55 */     int termNum = termDistri.columns();
/* 56 */     ArrayList list = new ArrayList(termNum);
/* 57 */     for (int i = 0; i < termNum; i++)
/* 58 */       if (termDistri.getColumnSum(i) > 0L)
/*    */       {
/* 60 */         IRTerm curTerm = indexReader.getIRTerm(i);
/* 61 */         if (curTerm.getDocFrequency() >= this.minDocFrequency)
/* 62 */           list.add(curTerm);
/*    */       }
/* 64 */     int[] featureMap = new int[list.size()];
/* 65 */     for (int i = 0; i < featureMap.length; i++)
/* 66 */       featureMap[i] = ((IRTerm)list.get(i)).getIndex();
/* 67 */     return featureMap;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.featureselection.DocFrequencySelector
 * JD-Core Version:    0.6.2
 */