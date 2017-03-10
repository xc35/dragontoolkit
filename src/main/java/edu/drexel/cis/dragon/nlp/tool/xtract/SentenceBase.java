/*    */ package edu.drexel.cis.dragon.nlp.tool.xtract;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.IntFlatSparseMatrix;
/*    */ import edu.drexel.cis.dragon.matrix.SparseMatrixFactory;
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ 
/*    */ public class SentenceBase
/*    */ {
/*    */   private SparseMatrixFactory factory;
/*    */   private IntFlatSparseMatrix cacheMatrix;
/*    */   private String indexFile;
/*    */   private int sentenceNum;
/*    */   private int threshold;
/*    */ 
/*    */   public SentenceBase(String indexFile, String matrixFile)
/*    */   {
/* 21 */     this.indexFile = indexFile;
/* 22 */     this.factory = new SparseMatrixFactory(matrixFile, 4);
/* 23 */     this.cacheMatrix = new IntFlatSparseMatrix();
/* 24 */     this.sentenceNum = this.factory.rows();
/* 25 */     this.threshold = 500000;
/*    */   }
/*    */ 
/*    */   public int addSentence(Sentence sent)
/*    */   {
/* 32 */     int sentIndex = this.sentenceNum;
/* 33 */     this.sentenceNum += 1;
/* 34 */     Word cur = sent.getFirstWord();
/* 35 */     while (cur != null) {
/* 36 */       this.cacheMatrix.add(sentIndex, cur.getIndex(), cur.getPOSIndex());
/* 37 */       cur = cur.next;
/*    */     }
/*    */ 
/* 40 */     if (this.cacheMatrix.getNonZeroNum() >= this.threshold)
/*    */     {
/* 42 */       this.cacheMatrix.finalizeData(false);
/* 43 */       this.factory.add(this.cacheMatrix);
/* 44 */       this.cacheMatrix.close();
/*    */     }
/* 46 */     return sentIndex;
/*    */   }
/*    */ 
/*    */   public void close() {
/* 50 */     this.cacheMatrix.finalizeData(false);
/* 51 */     this.factory.add(this.cacheMatrix);
/* 52 */     this.cacheMatrix.close();
/* 53 */     this.factory.genIndexFile(this.indexFile);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.xtract.SentenceBase
 * JD-Core Version:    0.6.2
 */