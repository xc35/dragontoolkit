/*    */ package edu.drexel.cis.dragon.qa.util;
/*    */ 
/*    */ public class Unit
/*    */   implements Comparable
/*    */ {
/*    */   private String unit;
/*    */   private String baseName;
/*    */   private String category;
/*    */ 
/*    */   public Unit(String unit, String baseName, String category)
/*    */   {
/*  7 */     this.unit = unit;
/*  8 */     this.baseName = baseName;
/*  9 */     this.category = category;
/*    */   }
/*    */ 
/*    */   public String getUnitName() {
/* 13 */     return this.unit;
/*    */   }
/*    */ 
/*    */   public String getBaseName() {
/* 17 */     return this.baseName;
/*    */   }
/*    */ 
/*    */   public String getCategory() {
/* 21 */     return this.category;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object unitObj) {
/* 25 */     return this.unit.compareToIgnoreCase(((Unit)unitObj).getUnitName());
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.util.Unit
 * JD-Core Version:    0.6.2
 */