/*    */ package edu.drexel.cis.dragon.ir.index;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.IntFlatSparseMatrix;
/*    */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*    */ 
/*    */ public class OnlineIndexWriter extends AbstractIndexWriter
/*    */ {
/*    */   public OnlineIndexWriter(boolean relationSupported)
/*    */   {
/* 16 */     super(relationSupported);
/*    */   }
/*    */ 
/*    */   public void initialize() {
/* 20 */     if (this.initialized)
/* 21 */       return;
/* 22 */     this.collection = new IRCollection();
/* 23 */     this.docIndexList = new OnlineIRDocIndexList();
/* 24 */     this.termIndexList = new OnlineIRTermIndexList();
/* 25 */     this.doctermMatrix = new IntFlatSparseMatrix(false, false);
/*    */ 
/* 27 */     if (this.relationSupported) {
/* 28 */       this.relationIndexList = new OnlineIRRelationIndexList();
/* 29 */       this.docrelationMatrix = new IntFlatSparseMatrix(false, false);
/*    */     }
/* 31 */     this.initialized = true;
/*    */   }
/*    */ 
/*    */   public void close() {
/* 35 */     flush();
/* 36 */     this.doctermMatrix.finalizeData();
/* 37 */     if (this.relationSupported)
/* 38 */       this.docrelationMatrix.finalizeData();
/* 39 */     this.initialized = false;
/*    */   }
/*    */ 
/*    */   public void clean() {
/* 43 */     this.termIndexList.close();
/* 44 */     this.docIndexList.close();
/* 45 */     this.doctermMatrix.close();
/* 46 */     if (this.relationSupported) {
/* 47 */       this.relationIndexList.close();
/* 48 */       this.docrelationMatrix.close();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void flush() {
/* 53 */     this.collection.setDocNum(this.docIndexList.size());
/* 54 */     this.collection.setTermNum(this.termIndexList.size());
/*    */ 
/* 56 */     if (this.relationSupported)
/* 57 */       this.collection.setRelationNum(this.relationIndexList.size());
/*    */   }
/*    */ 
/*    */   public IRTermIndexList getTermIndexList()
/*    */   {
/* 62 */     return this.termIndexList;
/*    */   }
/*    */ 
/*    */   public IRRelationIndexList getRelationIndexList() {
/* 66 */     return this.relationIndexList;
/*    */   }
/*    */ 
/*    */   public IRDocIndexList getDocIndexList() {
/* 70 */     return this.docIndexList;
/*    */   }
/*    */ 
/*    */   public IntSparseMatrix getDocTermMatrix() {
/* 74 */     return this.doctermMatrix;
/*    */   }
/*    */ 
/*    */   public IntSparseMatrix getDocRelationMatrix() {
/* 78 */     return this.docrelationMatrix;
/*    */   }
/*    */ 
/*    */   public IRCollection getIRCollection() {
/* 82 */     return this.collection;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.OnlineIndexWriter
 * JD-Core Version:    0.6.2
 */