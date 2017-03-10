/*    */ package edu.drexel.cis.dragon.pinyin.util;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ public class ChineseFile
/*    */ {
/*    */   public static String readTextFile(String inputFile)
/*    */   {
/* 17 */     return FileUtil.readTextFile(inputFile, "GBK");
/*    */   }
/*    */ 
/*    */   public static BufferedWriter getTextWriter(String file) {
/* 21 */     return FileUtil.getTextWriter(file, "GBK");
/*    */   }
/*    */ 
/*    */   public static BufferedReader getTextReader(String file) {
/* 25 */     return FileUtil.getTextReader(file, "GBK");
/*    */   }
/*    */ 
/*    */   public static PrintWriter getScreen() {
/*    */     try {
/* 30 */       return new PrintWriter(new OutputStreamWriter(System.out, "GBK"), true);
/*    */     }
/*    */     catch (Exception e) {
/* 33 */       e.printStackTrace();
/* 34 */     }return null;
/*    */   }
/*    */ 
/*    */   public static boolean containsUnrecognizedChar(String message)
/*    */   {
/* 42 */     int num = 0;
/* 43 */     int size = Math.min(message.length() - 2, 4096);
/* 44 */     for (int i = 0; i < size; i++) {
/* 45 */       char ch = message.charAt(i);
/*    */ 
/* 47 */       if ((ch == '?') && 
/* 48 */         (message.charAt(i + 1) == '?') && (message.charAt(i + 2) == '?')) {
/* 49 */         num++;
/*    */       }
/*    */     }
/* 52 */     return num >= 5;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.pinyin.util.ChineseFile
 * JD-Core Version:    0.6.2
 */