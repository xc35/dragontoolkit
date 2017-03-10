/*    */ package edu.drexel.cis.dragon.matrix;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public abstract class AbstractCell
/*    */   implements Cell, Serializable
/*    */ {
/*    */   private boolean resetOption;
/*    */ 
/*    */   public boolean getResetOption()
/*    */   {
/* 16 */     return this.resetOption;
/*    */   }
/*    */ 
/*    */   public void setResetOption(boolean option) {
/* 20 */     this.resetOption = option;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj)
/*    */   {
/* 26 */     Cell cellObj = (Cell)obj;
/* 27 */     if (getRow() > cellObj.getRow())
/* 28 */       return 1;
/* 29 */     if (getRow() == cellObj.getRow()) {
/* 30 */       if (getColumn() > cellObj.getColumn())
/* 31 */         return 1;
/* 32 */       if (getColumn() == cellObj.getColumn()) {
/* 33 */         return 0;
/*    */       }
/* 35 */       return -1;
/*    */     }
/*    */ 
/* 38 */     return -1;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.AbstractCell
 * JD-Core Version:    0.6.2
 */