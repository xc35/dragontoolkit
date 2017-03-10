/*    */ package edu.drexel.cis.dragon.matrix;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class CoordinateComparator
/*    */   implements Comparator, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public int compare(Object first, Object second)
/*    */   {
/* 21 */     Cell firstCell = (Cell)first;
/* 22 */     Cell secondCell = (Cell)second;
/*    */ 
/* 24 */     if (firstCell.getRow() > secondCell.getRow())
/* 25 */       return 1;
/* 26 */     if (firstCell.getRow() == secondCell.getRow()) {
/* 27 */       if (firstCell.getColumn() > secondCell.getColumn())
/* 28 */         return 1;
/* 29 */       if (firstCell.getColumn() == secondCell.getColumn()) {
/* 30 */         return 0;
/*    */       }
/* 32 */       return -1;
/*    */     }
/*    */ 
/* 35 */     return -1;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.CoordinateComparator
 * JD-Core Version:    0.6.2
 */