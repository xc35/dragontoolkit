/*    */ package edu.drexel.cis.dragon.qa.util;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.io.BufferedReader;
/*    */ 
/*    */ public class VerbUtil
/*    */ {
/*    */   private SortedArray excList;
/*    */   private SimpleDictionary verbDict;
/*    */ 
/*    */   public VerbUtil(String verbFile, String exceptionFile)
/*    */   {
/* 14 */     this.excList = loadExceptionFile(exceptionFile);
/* 15 */     this.verbDict = new SimpleDictionary(verbFile);
/*    */   }
/*    */ 
/*    */   public boolean exist(String verb) {
/* 19 */     return this.verbDict.exist(verb);
/*    */   }
/*    */ 
/*    */   public String getSimplePast(String base)
/*    */   {
/* 25 */     int pos = this.excList.binarySearch(new VerbVariant(base, null, null));
/* 26 */     if (pos < 0) {
/* 27 */       if (base.endsWith("e")) {
/* 28 */         return base + "d";
/*    */       }
/* 30 */       return base + "ed";
/*    */     }
/*    */ 
/* 33 */     return ((VerbVariant)this.excList.get(pos)).getSimplePast();
/*    */   }
/*    */ 
/*    */   public String getPastParticle(String base)
/*    */   {
/* 39 */     int pos = this.excList.binarySearch(new VerbVariant(base, null, null));
/* 40 */     if (pos < 0) {
/* 41 */       if (base.endsWith("e")) {
/* 42 */         return base + "d";
/*    */       }
/* 44 */       return base + "ed";
/*    */     }
/*    */ 
/* 47 */     return ((VerbVariant)this.excList.get(pos)).getPastParticle();
/*    */   }
/*    */ 
/*    */   public String getThirdPerson(String base)
/*    */   {
/* 54 */     int len = base.length();
/* 55 */     char ch = base.charAt(len - 1);
/* 56 */     ch = Character.toLowerCase(ch);
/* 57 */     if (ch == 'y')
/* 58 */       return base.substring(0, len - 1) + "ies";
/* 59 */     if ((ch == 's') || (ch == 'z') || (ch == 'x'))
/* 60 */       return base + "es";
/* 61 */     if ((ch == 'h') && (
/* 62 */       (base.charAt(len - 2) == 's') || (base.charAt(len - 2) == 'c'))) {
/* 63 */       return base + "es";
/*    */     }
/* 65 */     return base + "s";
/*    */   }
/*    */ 
/*    */   private SortedArray loadExceptionFile(String filename)
/*    */   {
/*    */     try
/*    */     {
/* 75 */       SortedArray list = new SortedArray();
/* 76 */       BufferedReader br = FileUtil.getTextReader(filename);
/*    */       String line;
/* 77 */       while ((line = br.readLine()) != null)
/*    */       {
/* 78 */         if (line.trim().length() != 0)
/*    */         {
/* 80 */           String[] arrField = line.split("\t");
/*    */           int pos;
/* 81 */           if ((pos = arrField[1].indexOf("/")) > 0)
/* 82 */             arrField[1] = arrField[1].substring(0, pos);
/* 83 */           if ((pos = arrField[2].indexOf("/")) > 0)
/* 84 */             arrField[2] = arrField[2].substring(0, pos);
/* 85 */           list.add(new VerbVariant(arrField[0].toLowerCase(), arrField[1].toLowerCase(), arrField[2].toLowerCase()));
/*    */         }
/*    */       }
/* 87 */       return list;
/*    */     }
/*    */     catch (Exception e) {
/* 90 */       e.printStackTrace();
/* 91 */     }return null;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.util.VerbUtil
 * JD-Core Version:    0.6.2
 */