/*    */ package edu.drexel.cis.dragon.nlp.ontology;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class BasicTermList extends SortedArray
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public BasicTermList(String cuiFile)
/*    */   {
/* 21 */     loadTermList(cuiFile);
/*    */   }
/*    */ 
/*    */   public BasicTerm termAt(int index) {
/* 25 */     return (BasicTerm)get(index);
/*    */   }
/*    */ 
/*    */   public BasicTerm lookup(String term)
/*    */   {
/* 31 */     int pos = binarySearch(new BasicTerm(0, term, null));
/* 32 */     if (pos < 0) {
/* 33 */       return null;
/*    */     }
/* 35 */     return (BasicTerm)get(pos);
/*    */   }
/*    */ 
/*    */   public BasicTerm lookup(BasicTerm term)
/*    */   {
/* 41 */     int pos = binarySearch(term);
/* 42 */     if (pos < 0) {
/* 43 */       return null;
/*    */     }
/* 45 */     return (BasicTerm)get(pos);
/*    */   }
/*    */ 
/*    */   private boolean loadTermList(String filename)
/*    */   {
/*    */     try
/*    */     {
/* 57 */       if (filename == null)
/* 58 */         return false;
/* 59 */       System.out.println(new Date() + " Loading Term List...");
/* 60 */       BufferedReader br = FileUtil.getTextReader(filename);
/* 61 */       String line = br.readLine();
/* 62 */       int total = Integer.parseInt(line);
/* 63 */       ArrayList list = new ArrayList(total);
/*    */ 
/* 65 */       for (int i = 0; i < total; i++) {
/* 66 */         line = br.readLine();
/* 67 */         String[] arrField = line.split("\t");
/* 68 */         BasicTerm cur = new BasicTerm(i, arrField[1], arrField[2].split("_"));
/* 69 */         list.add(cur);
/*    */       }
/* 71 */       br.close();
/* 72 */       Collections.sort(list);
/* 73 */       addAll(list);
/* 74 */       return true;
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 78 */       e.printStackTrace();
/* 79 */     }return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.BasicTermList
 * JD-Core Version:    0.6.2
 */