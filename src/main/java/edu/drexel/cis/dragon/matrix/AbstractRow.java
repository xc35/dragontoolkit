/*    */ package edu.drexel.cis.dragon.matrix;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public abstract class AbstractRow
/*    */   implements Row, Serializable
/*    */ {
/*    */   protected int row;
/*    */   protected int length;
/*    */   protected float loadFactor;
/*    */ 
/*    */   public int compareTo(Object obj)
/*    */   {
/* 19 */     double objLoadFactor = ((Row)obj).getLoadFactor();
/* 20 */     if (this.loadFactor > objLoadFactor)
/* 21 */       return -1;
/* 22 */     if (this.loadFactor < objLoadFactor) {
/* 23 */       return 1;
/*    */     }
/* 25 */     return 0;
/*    */   }
/*    */ 
/*    */   public int getRowIndex() {
/* 29 */     return this.row;
/*    */   }
/*    */ 
/*    */   public int getNonZeroNum() {
/* 33 */     return this.length;
/*    */   }
/*    */ 
/*    */   public void setLoadFactor(float factor) {
/* 37 */     this.loadFactor = factor;
/*    */   }
/*    */ 
/*    */   public float getLoadFactor() {
/* 41 */     return this.loadFactor;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.AbstractRow
 * JD-Core Version:    0.6.2
 */