/*    */ package edu.drexel.cis.dragon.ir.classification.multiclass;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class LinearLoss
/*    */   implements LossFunction, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public double loss(double val)
/*    */   {
/* 16 */     return -val;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.multiclass.LinearLoss
 * JD-Core Version:    0.6.2
 */