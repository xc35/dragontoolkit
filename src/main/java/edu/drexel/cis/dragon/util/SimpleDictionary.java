/*    */ package edu.drexel.cis.dragon.util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.File;
/*    */ 
/*    */ public class SimpleDictionary
/*    */ {
/*    */   private SortedArray list;
/*    */   private boolean caseSensitive;
/*    */ 
/*    */   public SimpleDictionary()
/*    */   {
/* 19 */     this(false);
/*    */   }
/*    */ 
/*    */   public SimpleDictionary(boolean caseSensitive) {
/* 23 */     this.caseSensitive = caseSensitive;
/* 24 */     this.list = new SortedArray();
/*    */   }
/*    */ 
/*    */   public SimpleDictionary(String dictFile) {
/* 28 */     this(dictFile, false);
/*    */   }
/*    */ 
/*    */   public SimpleDictionary(String dictFile, boolean caseSensitive) {
/* 32 */     this.list = loadList(dictFile, caseSensitive);
/* 33 */     this.caseSensitive = caseSensitive;
/*    */   }
/*    */ 
/*    */   public void add(String word) {
/* 37 */     if (this.caseSensitive)
/* 38 */       this.list.add(word);
/*    */     else
/* 40 */       this.list.add(word.toLowerCase());
/*    */   }
/*    */ 
/*    */   public boolean exist(String word) {
/* 44 */     if (word == null)
/* 45 */       return false;
/* 46 */     if (this.caseSensitive) {
/* 47 */       return this.list.binarySearch(word) >= 0;
/*    */     }
/* 49 */     return this.list.binarySearch(word.toLowerCase()) >= 0;
/*    */   }
/*    */ 
/*    */   public static void merge(String inputFolder, String outputFile, boolean caseSensitive)
/*    */   {
/* 58 */     File folder = new File(inputFolder);
/* 59 */     if (!folder.exists())
/* 60 */       return;
/* 61 */     if (!folder.isDirectory())
/* 62 */       return;
/* 63 */     File[] arrFile = folder.listFiles();
/* 64 */     SortedArray list = new SortedArray();
/* 65 */     for (int i = 0; i < arrFile.length; i++) {
/* 66 */       loadList(inputFolder + "/" + arrFile[i].getName(), list, caseSensitive);
/*    */     }
/*    */     try
/*    */     {
/* 70 */       BufferedWriter bw = FileUtil.getTextWriter(outputFile);
/* 71 */       for (int i = 0; i < list.size(); i++)
/* 72 */         bw.write((String)list.get(i) + "\n");
/* 73 */       list.clear();
/* 74 */       bw.close();
/*    */     }
/*    */     catch (Exception e) {
/* 77 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public static SortedArray loadList(String filename, boolean caseSensitive)
/*    */   {
/* 84 */     SortedArray list = new SortedArray();
/* 85 */     loadList(filename, list, caseSensitive);
/* 86 */     return list;
/*    */   }
/*    */ 
/*    */   public static void loadList(String filename, SortedArray list, boolean caseSensitive)
/*    */   {
/*    */     try
/*    */     {
/* 95 */       if ((filename == null) || (filename.trim().length() == 0)) {
/* 96 */         return;
/*    */       }
/* 98 */       if ((!FileUtil.exist(filename)) && (FileUtil.exist(EnvVariable.getDragonHome() + "/" + filename))) {
/* 99 */         filename = EnvVariable.getDragonHome() + "/" + filename;
/*    */       }
/* 101 */       BufferedReader br = FileUtil.getTextReader(filename);
/* 102 */       String line = br.readLine().trim();
/*    */       try {
/* 104 */         Integer.parseInt(line);
/* 105 */         line = "";
/*    */       }
/*    */       catch (Exception localException1)
/*    */       {
/*    */       }
/*    */ 
/* 111 */       while (line != null) {
/* 112 */         line = line.trim();
/* 113 */         if (line.length() == 0) {
/* 114 */           line = br.readLine();
/*    */         }
/*    */         else {
/* 117 */           int pos = line.indexOf('\t');
/* 118 */           if (pos > 0)
/* 119 */             line = line.substring(0, pos);
/* 120 */           if (!caseSensitive)
/* 121 */             list.add(line.toLowerCase());
/*    */           else
/* 123 */             list.add(line);
/* 124 */           line = br.readLine();
/*    */         }
/*    */       }
/* 126 */       br.close();
/*    */     }
/*    */     catch (Exception e) {
/* 129 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.SimpleDictionary
 * JD-Core Version:    0.6.2
 */