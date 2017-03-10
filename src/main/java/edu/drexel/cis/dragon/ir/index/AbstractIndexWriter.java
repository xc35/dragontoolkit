/*    */ package edu.drexel.cis.dragon.ir.index;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public abstract class AbstractIndexWriter
/*    */   implements IndexWriter
/*    */ {
/* 15 */   protected static int doc_cache_size = 5000;
/*    */   protected IRTermIndexList termIndexList;
/*    */   protected IRRelationIndexList relationIndexList;
/*    */   protected IRDocIndexList docIndexList;
/*    */   protected IntSparseMatrix doctermMatrix;
/*    */   protected IntSparseMatrix docrelationMatrix;
/*    */   protected IRCollection collection;
/*    */   protected int doc_in_cache;
/*    */   protected boolean relationSupported;
/*    */   protected boolean initialized;
/*    */ 
/*    */   public AbstractIndexWriter(boolean relationSupported)
/*    */   {
/* 26 */     this.relationSupported = relationSupported;
/* 27 */     this.doc_in_cache = 0;
/*    */   }
/*    */ 
/*    */   public int size() {
/* 31 */     return this.docIndexList.size();
/*    */   }
/*    */ 
/*    */   public synchronized boolean write(IRDoc curDoc, IRTerm[] arrTerms, IRRelation[] arrRelations)
/*    */   {
/*    */     try
/*    */     {
/* 38 */       if (!this.relationSupported) return false;
/*    */ 
/* 40 */       if (!this.docIndexList.add(curDoc)) {
/* 41 */         System.out.println("#" + curDoc.getKey() + " is alrady indexed");
/* 42 */         return false;
/*    */       }
/*    */ 
/* 45 */       if (this.doc_in_cache > doc_cache_size) {
/* 46 */         flush();
/*    */       }
/* 48 */       this.doc_in_cache += 1;
/*    */ 
/* 50 */       for (int i = 0; i < arrTerms.length; i++) {
/* 51 */         arrTerms[i].setDocFrequency(1);
/* 52 */         this.doctermMatrix.add(curDoc.getIndex(), arrTerms[i].getIndex(), arrTerms[i].getFrequency());
/* 53 */         this.termIndexList.add(arrTerms[i]);
/*    */       }
/* 55 */       this.collection.addTermCount(curDoc.getTermCount());
/*    */ 
/* 57 */       for (int i = 0; i < arrRelations.length; i++) {
/* 58 */         arrRelations[i].setDocFrequency(1);
/* 59 */         this.docrelationMatrix.add(curDoc.getIndex(), arrRelations[i].getIndex(), arrRelations[i].getFrequency());
/* 60 */         this.relationIndexList.add(arrRelations[i]);
/*    */       }
/* 62 */       this.collection.addRelationCount(curDoc.getRelationCount());
/*    */ 
/* 64 */       return true;
/*    */     }
/*    */     catch (Exception e) {
/* 67 */       e.printStackTrace();
/* 68 */     }return false;
/*    */   }
/*    */ 
/*    */   public synchronized boolean write(IRDoc curDoc, IRTerm[] arrTerms)
/*    */   {
/*    */     try
/*    */     {
/* 76 */       if (!this.docIndexList.add(curDoc)) {
/* 77 */         System.out.println("#" + curDoc.getKey() + " is already indexed");
/* 78 */         return false;
/*    */       }
/*    */ 
/* 81 */       if (this.doc_in_cache > doc_cache_size) {
/* 82 */         flush();
/*    */       }
/* 84 */       this.doc_in_cache += 1;
/*    */ 
/* 86 */       for (int i = 0; i < arrTerms.length; i++) {
/* 87 */         arrTerms[i].setDocFrequency(1);
/* 88 */         this.doctermMatrix.add(curDoc.getIndex(), arrTerms[i].getIndex(), arrTerms[i].getFrequency());
/* 89 */         this.termIndexList.add(arrTerms[i]);
/*    */       }
/* 91 */       this.collection.addTermCount(curDoc.getTermCount());
/* 92 */       return true;
/*    */     }
/*    */     catch (Exception e) {
/* 95 */       e.printStackTrace();
/* 96 */     }return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.AbstractIndexWriter
 * JD-Core Version:    0.6.2
 */