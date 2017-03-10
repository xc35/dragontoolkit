/*    */ package edu.drexel.cis.dragon.ml.seqmodel.data;
/*    */ 
/*    */ public class BasicLabelConverter
/*    */   implements LabelConverter
/*    */ {
/*    */   public int getInternalLabel(int externalLabel)
/*    */   {
/* 17 */     return externalLabel - 1;
/*    */   }
/*    */ 
/*    */   public int getInternalLabel(String externalLabel) {
/*    */     try {
/* 22 */       return Integer.parseInt(externalLabel) - 1;
/*    */     } catch (Exception e) {
/*    */     }
/* 25 */     return -1;
/*    */   }
/*    */ 
/*    */   public int getExternalLabelID(int internalLabel)
/*    */   {
/* 30 */     return internalLabel + 1;
/*    */   }
/*    */ 
/*    */   public String getExternalLabelString(int internalLabel) {
/* 34 */     return Integer.toString(internalLabel + 1);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.data.BasicLabelConverter
 * JD-Core Version:    0.6.2
 */