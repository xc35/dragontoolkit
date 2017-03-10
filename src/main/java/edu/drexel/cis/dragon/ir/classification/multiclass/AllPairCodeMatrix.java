/*    */ package edu.drexel.cis.dragon.ir.classification.multiclass;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.IntFlatSparseMatrix;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class AllPairCodeMatrix extends AbstractCodeMatrix
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private IntFlatSparseMatrix matrix;
/*    */ 
/*    */   public AllPairCodeMatrix()
/*    */   {
/* 19 */     this(1);
/*    */   }
/*    */ 
/*    */   public AllPairCodeMatrix(int classNum) {
/* 23 */     super(classNum);
/* 24 */     setClassNum(classNum);
/*    */   }
/*    */ 
/*    */   public void setClassNum(int classNum)
/*    */   {
/* 30 */     this.classNum = classNum;
/* 31 */     this.matrix = new IntFlatSparseMatrix();
/* 32 */     this.classifierNum = 0;
/* 33 */     for (int i = 0; i < classNum - 1; i++) {
/* 34 */       for (int j = i + 1; j < classNum; j++) {
/* 35 */         this.matrix.add(i, this.classifierNum, 1);
/* 36 */         this.matrix.add(j, this.classifierNum, -1);
/* 37 */         this.classifierNum += 1;
/*    */       }
/*    */     }
/* 40 */     this.matrix.finalizeData();
/*    */   }
/*    */ 
/*    */   public int getCode(int classIndex, int classifierIndex) {
/* 44 */     return this.matrix.getInt(classIndex, classifierIndex);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.multiclass.AllPairCodeMatrix
 * JD-Core Version:    0.6.2
 */