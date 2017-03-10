/*     */ package edu.drexel.cis.dragon.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class FileUtil
/*     */ {
/*     */   public static BufferedReader getTextReader(String filename)
/*     */   {
/*  19 */     return getTextReader(new File(filename), null);
/*     */   }
/*     */ 
/*     */   public static BufferedReader getTextReader(File file) {
/*  23 */     return getTextReader(file, null);
/*     */   }
/*     */ 
/*     */   public static BufferedReader getTextReader(String filename, String charSet) {
/*  27 */     return getTextReader(new File(filename), charSet);
/*     */   }
/*     */ 
/*     */   public static BufferedReader getTextReader(File file, String charSet)
/*     */   {
/*     */     try {
/*  33 */       if (charSet == null)
/*  34 */         charSet = EnvVariable.getCharSet();
/*  35 */       if (charSet == null) {
/*  36 */         return new BufferedReader(new FileReader(file));
/*     */       }
/*  38 */       return new BufferedReader(new InputStreamReader(new FileInputStream(file), charSet));
/*     */     }
/*     */     catch (Exception e) {
/*     */     }
/*  42 */     return null;
/*     */   }
/*     */ 
/*     */   public static BufferedWriter getTextWriter(String filename)
/*     */   {
/*  47 */     return getTextWriter(filename, false, null);
/*     */   }
/*     */ 
/*     */   public static BufferedWriter getTextWriter(String filename, String charSet) {
/*  51 */     return getTextWriter(filename, false, charSet);
/*     */   }
/*     */ 
/*     */   public static BufferedWriter getTextWriter(String filename, boolean append) {
/*  55 */     return getTextWriter(filename, append, null);
/*     */   }
/*     */ 
/*     */   public static BufferedWriter getTextWriter(String filename, boolean append, String charSet)
/*     */   {
/*     */     try {
/*  61 */       if (charSet == null)
/*  62 */         charSet = EnvVariable.getCharSet();
/*  63 */       if (charSet == null) {
/*  64 */         return new BufferedWriter(new FileWriter(filename, append));
/*     */       }
/*  66 */       return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, append), charSet));
/*     */     }
/*     */     catch (Exception e) {
/*     */     }
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */   public static boolean saveTextFile(String filename, String content)
/*     */   {
/*  75 */     return saveTextFile(filename, content, null);
/*     */   }
/*     */ 
/*     */   public static boolean saveTextFile(String filename, String content, String charSet)
/*     */   {
/*     */     try
/*     */     {
/*  82 */       BufferedWriter out = getTextWriter(filename, charSet);
/*  83 */       out.write(content);
/*  84 */       out.close();
/*  85 */       return true;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  89 */       e.printStackTrace();
/*  90 */     }return false;
/*     */   }
/*     */ 
/*     */   public static String readTextFile(String filename)
/*     */   {
/*  95 */     return readTextFile(new File(filename), null);
/*     */   }
/*     */ 
/*     */   public static String readTextFile(File file)
/*     */   {
/* 100 */     return readTextFile(file, null);
/*     */   }
/*     */ 
/*     */   public static String readTextFile(String filename, String charSet) {
/* 104 */     return readTextFile(new File(filename), charSet);
/*     */   }
/*     */ 
/*     */   public static String readTextFile(File file, String charSet)
/*     */   {
/* 111 */     int len = 0; int count = 128000;
/*     */     try
/*     */     {
/* 115 */       BufferedReader fr = getTextReader(file, charSet);
/* 116 */       char[] buf = new char[count];
/* 117 */       StringBuffer str = new StringBuffer();
/*     */ 
/* 119 */       len = fr.read(buf);
/*     */       while (true)
/*     */       {
/* 122 */         str.append(buf, 0, len);
/* 123 */         if (len < count) {
/*     */           break;
/*     */         }
/* 126 */         len = fr.read(buf);
/*     */       }
/* 128 */       return str.toString();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/* 133 */     return "";
/*     */   }
/*     */ 
/*     */   public static PrintWriter getScreen()
/*     */   {
/*     */     try
/*     */     {
/* 141 */       String charSet = EnvVariable.getCharSet();
/* 142 */       if (charSet != null) {
/* 143 */         return new PrintWriter(new OutputStreamWriter(System.out, charSet), true);
/*     */       }
/* 145 */       return new PrintWriter(new OutputStreamWriter(System.out), true);
/*     */     }
/*     */     catch (Exception e) {
/* 148 */       e.printStackTrace();
/* 149 */     }return null;
/*     */   }
/*     */ 
/*     */   public static PrintWriter getPrintWriter(String filename)
/*     */   {
/* 154 */     return getPrintWriter(filename, false, null);
/*     */   }
/*     */ 
/*     */   public static PrintWriter getPrintWriter(String filename, boolean append) {
/* 158 */     return getPrintWriter(filename, append, null);
/*     */   }
/*     */ 
/*     */   public static PrintWriter getPrintWriter(String filename, boolean append, String charSet)
/*     */   {
/*     */     try
/*     */     {
/* 165 */       if (charSet == null)
/* 166 */         charSet = EnvVariable.getCharSet();
/* 167 */       if (charSet != null) {
/* 168 */         return new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(filename), append), charSet));
/*     */       }
/* 170 */       return new PrintWriter(new FileOutputStream(new File(filename), append));
/*     */     }
/*     */     catch (Exception e) {
/*     */     }
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getNewTempFilename(String folder, String prefix, String suffix)
/*     */   {
/* 182 */     int i = 1;
/*     */     while (true) {
/* 184 */       File file = new File(folder + prefix + String.valueOf(i) + "." + suffix);
/* 185 */       if (!file.exists())
/*     */         break;
/* 187 */       i++;
/*     */     }
/*     */     File file;
/* 189 */     return folder + prefix + String.valueOf(i) + "." + suffix;
/*     */   }
/*     */ 
/*     */   public static String getNewTempFilename(String prefix, String suffix)
/*     */   {
/* 196 */     int i = 1;
/*     */     while (true) {
/* 198 */       File file = new File(prefix + String.valueOf(i) + "." + suffix);
/* 199 */       if (!file.exists())
/*     */         break;
/* 201 */       i++;
/*     */     }
/*     */     File file;
/* 203 */     return prefix + String.valueOf(i) + "." + suffix;
/*     */   }
/*     */ 
/*     */   public static boolean exist(String filename)
/*     */   {
/* 209 */     File file = new File(filename);
/* 210 */     return file.exists();
/*     */   }
/*     */ 
/*     */   public static long getSize(String filename)
/*     */   {
/* 216 */     File file = new File(filename);
/* 217 */     if (file.exists()) {
/* 218 */       return file.length();
/*     */     }
/* 220 */     return 0L;
/*     */   }
/*     */ 
/*     */   public static void changeTextFileEncoding(String file, String oldEncoding, String newEncoding) {
/* 224 */     changeTextFileEncoding(new File(file), oldEncoding, newEncoding);
/*     */   }
/*     */ 
/*     */   public static void changeTextFileEncoding(File file, String oldEncoding, String newEncoding)
/*     */   {
/*     */     try
/*     */     {
/* 233 */       if (file.isFile()) {
/* 234 */         if (!file.exists())
/* 235 */           return;
/* 236 */         saveTextFile(file.getAbsolutePath(), readTextFile(file, oldEncoding), newEncoding);
/*     */       }
/*     */       else {
/* 239 */         File[] subs = file.listFiles();
/* 240 */         for (int i = 0; i < subs.length; i++)
/* 241 */           changeTextFileEncoding(subs[i], oldEncoding, newEncoding);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 245 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.FileUtil
 * JD-Core Version:    0.6.2
 */