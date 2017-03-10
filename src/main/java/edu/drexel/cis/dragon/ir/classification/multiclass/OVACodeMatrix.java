/*    */ package edu.drexel.cis.dragon.ir.classification.multiclass;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class OVACodeMatrix extends AbstractCodeMatrix
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public OVACodeMatrix()
/*    */   {
/* 17 */     this(1);
/*    */   }
/*    */ 
/*    */   public OVACodeMatrix(int classNum) {
/* 21 */     super(classNum);
/* 22 */     setClassNum(classNum);
/*    */   }
/*    */ 
/*    */   public int getCode(int classIndex, int classifierIndex) {
/* 26 */     if (classIndex == classifierIndex) {
/* 27 */       return 1;
/*    */     }
/* 29 */     return -1;
/*    */   }
/*    */ 
/*    */   public void setClassNum(int classNum) {
/* 33 */     this.classifierNum = classNum;
/* 34 */     this.classNum = classNum;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.multiclass.OVACodeMatrix
 * JD-Core Version:    0.6.2
 */