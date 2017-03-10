/*    */ package edu.drexel.cis.dragon.ir.kngbase;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*    */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*    */ 
/*    */ public class BasicKnowledgeBase
/*    */   implements KnowledgeBase
/*    */ {
/*    */   private DoubleSparseMatrix transMatrix;
/*    */   private SimpleElementList rowKeyList;
/*    */   private SimpleElementList columnKeyList;
/*    */ 
/*    */   public BasicKnowledgeBase(DoubleSparseMatrix transMatrix, SimpleElementList rowKeyList, SimpleElementList columnKeyList)
/*    */   {
/* 19 */     this.transMatrix = transMatrix;
/* 20 */     this.rowKeyList = rowKeyList;
/* 21 */     this.columnKeyList = columnKeyList;
/*    */   }
/*    */ 
/*    */   public DoubleSparseMatrix getKnowledgeMatrix() {
/* 25 */     return this.transMatrix;
/*    */   }
/*    */ 
/*    */   public SimpleElementList getRowKeyList() {
/* 29 */     return this.rowKeyList;
/*    */   }
/*    */ 
/*    */   public SimpleElementList getColumnKeyList() {
/* 33 */     return this.columnKeyList;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.kngbase.BasicKnowledgeBase
 * JD-Core Version:    0.6.2
 */