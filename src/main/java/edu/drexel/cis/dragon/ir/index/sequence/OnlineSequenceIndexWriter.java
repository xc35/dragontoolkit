/*    */ package edu.drexel.cis.dragon.ir.index.sequence;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDocIndexList;
/*    */ import edu.drexel.cis.dragon.ir.index.IRTermIndexList;
/*    */ import edu.drexel.cis.dragon.ir.index.OnlineIRDocIndexList;
/*    */ import edu.drexel.cis.dragon.ir.index.OnlineIRTermIndexList;
/*    */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ 
/*    */ public class OnlineSequenceIndexWriter extends AbstractSequenceIndexWriter
/*    */ {
/*    */   public void initialize()
/*    */   {
/* 21 */     if (this.initialized)
/* 22 */       return;
/* 23 */     this.collection = new IRCollection();
/* 24 */     this.termCache = new SortedArray(500);
/* 25 */     this.docKeyList = new SimpleElementList();
/* 26 */     this.termKeyList = new SimpleElementList();
/* 27 */     this.docIndexList = new OnlineIRDocIndexList();
/* 28 */     this.termIndexList = new OnlineIRTermIndexList();
/* 29 */     this.doctermMatrix = new OnlineSequenceBase();
/* 30 */     this.initialized = true;
/*    */   }
/*    */ 
/*    */   public void close() {
/* 34 */     flush();
/* 35 */     this.initialized = false;
/*    */   }
/*    */ 
/*    */   public void clean() {
/* 39 */     this.termIndexList.close();
/* 40 */     this.docIndexList.close();
/* 41 */     this.doctermMatrix.close();
/* 42 */     this.docKeyList.close();
/* 43 */     this.termKeyList.close();
/*    */   }
/*    */ 
/*    */   public IRTermIndexList getTermIndexList() {
/* 47 */     return this.termIndexList;
/*    */   }
/*    */ 
/*    */   public IRDocIndexList getDocIndexList() {
/* 51 */     return this.docIndexList;
/*    */   }
/*    */ 
/*    */   public SimpleElementList getDocKeyList() {
/* 55 */     return this.docKeyList;
/*    */   }
/*    */ 
/*    */   public SimpleElementList getTermKeyList() {
/* 59 */     return this.termKeyList;
/*    */   }
/*    */ 
/*    */   public IRCollection getIRCollection() {
/* 63 */     return this.collection;
/*    */   }
/*    */ 
/*    */   public SequenceReader getSequenceReader() {
/* 67 */     return (SequenceReader)this.doctermMatrix;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.OnlineSequenceIndexWriter
 * JD-Core Version:    0.6.2
 */