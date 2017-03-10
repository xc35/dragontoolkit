/*    */ package edu.drexel.cis.dragon.ir.clustering.featurefilter;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ import edu.drexel.cis.dragon.util.MathUtil;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class DocFrequencyFilter extends AbstractFeatureFilter
/*    */ {
/*    */   private int minDocFrequency;
/*    */ 
/*    */   public DocFrequencyFilter(int minDocFrequency)
/*    */   {
/* 20 */     this.minDocFrequency = minDocFrequency;
/*    */   }
/*    */ 
/*    */   protected int[] getSelectedFeatures(IndexReader indexReader, IRDoc[] docSet)
/*    */   {
/* 29 */     int termNum = indexReader.getCollection().getTermNum();
/*    */     int[] arrDocFreq;
/* 30 */     if ((docSet != null) && (docSet.length < indexReader.getCollection().getDocNum() * 0.67D))
/* 31 */       arrDocFreq = computeTermCount(indexReader, docSet);
/*    */     else
/* 33 */       arrDocFreq = (int[])null;
/* 34 */     ArrayList list = new ArrayList(termNum);
/* 35 */     for (int i = 0; i < termNum; i++)
/* 36 */       if ((arrDocFreq == null) || (arrDocFreq[i] != 0))
/*    */       {
/* 38 */         IRTerm curTerm = indexReader.getIRTerm(i);
/* 39 */         if (curTerm.getDocFrequency() >= this.minDocFrequency)
/* 40 */           list.add(curTerm);
/*    */       }
/* 42 */     int[] featureMap = new int[list.size()];
/* 43 */     for (int i = 0; i < featureMap.length; i++)
/* 44 */       featureMap[i] = ((IRTerm)list.get(i)).getIndex();
/* 45 */     return featureMap;
/*    */   }
/*    */ 
/*    */   private int[] computeTermCount(IndexReader indexReader, IRDoc[] arrDoc)
/*    */   {
/* 52 */     int[] buf = new int[indexReader.getCollection().getTermNum()];
/* 53 */     MathUtil.initArray(buf, 0);
/* 54 */     for (int j = 0; j < arrDoc.length; j++) {
/* 55 */       int[] arrIndex = indexReader.getTermIndexList(arrDoc[j].getIndex());
/* 56 */       for (int k = 0; k < arrIndex.length; k++)
/* 57 */         buf[arrIndex[k]] += 1;
/*    */     }
/* 59 */     return buf;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.featurefilter.DocFrequencyFilter
 * JD-Core Version:    0.6.2
 */