/*    */ package edu.drexel.cis.dragon.util;
/*    */ 
/*    */ public class TextUtil
/*    */ {
/*    */   private String text;
/*    */ 
/*    */   public TextUtil(String text)
/*    */   {
/* 16 */     this.text = text;
/*    */   }
/*    */ 
/*    */   public int countOccurrence(String str) {
/* 20 */     return countOccurrence(str, 0);
/*    */   }
/*    */ 
/*    */   public int countOccurrence(String str, int start)
/*    */   {
/* 26 */     int count = 0;
/* 27 */     start = this.text.indexOf(str, start);
/* 28 */     while (start >= 0) {
/* 29 */       count++;
/* 30 */       start = this.text.indexOf(str, start + str.length());
/*    */     }
/* 32 */     return count;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.TextUtil
 * JD-Core Version:    0.6.2
 */