/*    */ package edu.drexel.cis.dragon.util;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ 
/*    */ public class FormatUtil
/*    */ {
/*    */   public static DecimalFormat getNumericFormat(int intDigits, int fractionDigits)
/*    */   {
/* 20 */     DecimalFormat fm = new DecimalFormat();
/* 21 */     fm.setMinimumIntegerDigits(intDigits);
/* 22 */     fm.setMaximumFractionDigits(fractionDigits);
/* 23 */     fm.setMinimumFractionDigits(fractionDigits);
/* 24 */     fm.setGroupingUsed(false);
/* 25 */     return fm;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.FormatUtil
 * JD-Core Version:    0.6.2
 */