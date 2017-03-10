/*    */ package edu.drexel.cis.dragon.ir.summarize;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public abstract class AbstractStructureSum
/*    */ {
/*    */   protected IndexReader indexReader;
/*    */ 
/*    */   public abstract TopicSummary summarize(ArrayList paramArrayList, int paramInt);
/*    */ 
/*    */   public AbstractStructureSum(IndexReader indexReader)
/*    */   {
/* 20 */     this.indexReader = indexReader;
/*    */   }
/*    */ 
/*    */   public IndexReader getIndexReader() {
/* 24 */     return this.indexReader;
/*    */   }
/*    */ 
/*    */   public TopicSummary summarize(ArrayList docSet) {
/* 28 */     return summarize(docSet, 2147483647);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.summarize.AbstractStructureSum
 * JD-Core Version:    0.6.2
 */