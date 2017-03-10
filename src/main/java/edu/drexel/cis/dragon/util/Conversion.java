/*    */ package edu.drexel.cis.dragon.util;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class Conversion
/*    */ {
/* 16 */   private static String[] arrEngNum = { "a", "an", "one", "billion", "eight", "eighteen", "eighty", "eleven", "five", "fifteen", "fifty", "four", "fourteen", "forty", 
/* 17 */     "hundred", "million", "nine", "nineteen", "ninty", "seven", "seventeen", "seventy", "six", "sixteen", "sixty", "ten", 
/* 18 */     "thirteen", "thirty", "thousand", "three", "twelve", "twenty", "two", "zero" };
/*    */ 
/* 19 */   private static int[] arrInt = { 1, 1, 1, 1000000000, 8, 18, 80, 11, 5, 15, 50, 4, 14, 40, 100, 1000000, 9, 19, 90, 7, 17, 70, 6, 16, 60, 10, 13, 30, 1000, 3, 12, 20, 2 };
/*    */ 
/*    */   public static Date engDate(String str)
/*    */   {
/*    */     try
/*    */     {
/* 27 */       return new SimpleDateFormat().parse(str);
/*    */     }
/*    */     catch (Exception e) {
/*    */     }
/* 31 */     return null;
/*    */   }
/*    */ 
/*    */   public static int engInt(String str)
/*    */   {
/* 39 */     int low = 0;
/* 40 */     int high = arrEngNum.length - 1;
/* 41 */     while (low < high)
/*    */     {
/* 43 */       int middle = (high + low) / 2;
/*    */       int cmp;
/* 44 */       if ((cmp = arrEngNum[middle].compareToIgnoreCase(str)) == 0) return arrInt[middle];
/* 45 */       if (cmp > 0) high = middle - 1; else {
/* 46 */         low = middle + 1;
/*    */       }
/*    */     }
/*    */     try
/*    */     {
/* 51 */       return Integer.parseInt(str);
/*    */     }
/*    */     catch (Exception e) {
/*    */     }
/* 55 */     return -1;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.Conversion
 * JD-Core Version:    0.6.2
 */