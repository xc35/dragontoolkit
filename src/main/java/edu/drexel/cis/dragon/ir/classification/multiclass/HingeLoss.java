/*    */ package edu.drexel.cis.dragon.ir.classification.multiclass;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class HingeLoss
/*    */   implements LossFunction, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public double loss(double val)
/*    */   {
/* 16 */     return Math.max(1.0D - val, 0.0D);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.multiclass.HingeLoss
 * JD-Core Version:    0.6.2
 */