/*    */ package edu.drexel.cis.dragon.qa.util;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.io.BufferedReader;
/*    */ 
/*    */ public class UnitUtil
/*    */ {
/*    */   private SortedArray list;
/*    */ 
/*    */   public UnitUtil(String unitFile)
/*    */   {
/* 10 */     this.list = load(unitFile);
/*    */   }
/*    */ 
/*    */   public Unit search(String unit)
/*    */   {
/* 16 */     int pos = this.list.binarySearch(new Unit(unit, null, null));
/* 17 */     if (pos >= 0) {
/* 18 */       return (Unit)this.list.get(pos);
/*    */     }
/* 20 */     return null;
/*    */   }
/*    */ 
/*    */   private SortedArray load(String filename)
/*    */   {
/*    */     try
/*    */     {
/* 29 */       SortedArray list = new SortedArray();
/* 30 */       BufferedReader br = FileUtil.getTextReader(filename);
/*    */       String line;
/* 31 */       while ((line = br.readLine()) != null)
/*    */       {
/* 32 */         if (line.trim().length() != 0)
/*    */         {
/* 34 */           String[] arrField = line.split("\t");
/* 35 */           if (arrField.length >= 3)
/* 36 */             list.add(new Unit(arrField[0], arrField[1], arrField[2]));
/*    */           else
/* 38 */             list.add(new Unit(arrField[0], arrField[1], null)); 
/*    */         }
/*    */       }
/* 40 */       return list;
/*    */     }
/*    */     catch (Exception e) {
/* 43 */       e.printStackTrace();
/* 44 */     }return null;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.util.UnitUtil
 * JD-Core Version:    0.6.2
 */