/*    */ package edu.drexel.cis.dragon.pinyin.util;
/*    */ 
/*    */ public class TimerUtil
/*    */ {
/*    */   public static void oneSecond()
/*    */   {
/*    */     try
/*    */     {
/* 15 */       Thread.sleep(1000L);
/*    */     }
/*    */     catch (InterruptedException e) {
/* 18 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void manySecond(long s) {
/*    */     try {
/* 24 */       Thread.sleep(s * 1000L);
/*    */     }
/*    */     catch (InterruptedException e) {
/* 27 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.pinyin.util.TimerUtil
 * JD-Core Version:    0.6.2
 */