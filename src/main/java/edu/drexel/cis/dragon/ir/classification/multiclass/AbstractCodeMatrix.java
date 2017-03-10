/*    */ package edu.drexel.cis.dragon.ir.classification.multiclass;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public abstract class AbstractCodeMatrix
/*    */   implements CodeMatrix, Serializable
/*    */ {
/*    */   protected int classNum;
/*    */   protected int classifierNum;
/*    */ 
/*    */   public AbstractCodeMatrix(int classNum)
/*    */   {
/* 17 */     this.classNum = classNum;
/*    */   }
/*    */ 
/*    */   public int getClassNum() {
/* 21 */     return this.classNum;
/*    */   }
/*    */ 
/*    */   public int getClassifierNum() {
/* 25 */     return this.classifierNum;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.multiclass.AbstractCodeMatrix
 * JD-Core Version:    0.6.2
 */