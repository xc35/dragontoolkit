/*    */ package edu.drexel.cis.dragon.ir.index.sequence;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDocIndexList;
/*    */ import edu.drexel.cis.dragon.ir.index.IRTermIndexList;
/*    */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*    */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*    */ 
/*    */ public class OnlineSequenceIndexer extends AbstractSequenceIndexer
/*    */ {
/*    */   public OnlineSequenceIndexer(ConceptExtractor ce)
/*    */   {
/* 17 */     super(ce);
/* 18 */     this.writer = new OnlineSequenceIndexWriter();
/*    */   }
/*    */ 
/*    */   public IRTermIndexList getTermIndexList() {
/* 22 */     return ((OnlineSequenceIndexWriter)this.writer).getTermIndexList();
/*    */   }
/*    */ 
/*    */   public IRDocIndexList getDocIndexList() {
/* 26 */     return ((OnlineSequenceIndexWriter)this.writer).getDocIndexList();
/*    */   }
/*    */ 
/*    */   public SimpleElementList getDocKeyList() {
/* 30 */     return ((OnlineSequenceIndexWriter)this.writer).getDocKeyList();
/*    */   }
/*    */ 
/*    */   public SimpleElementList getTermKeyList() {
/* 34 */     return ((OnlineSequenceIndexWriter)this.writer).getTermKeyList();
/*    */   }
/*    */ 
/*    */   public SequenceReader getSequenceReader() {
/* 38 */     return ((OnlineSequenceIndexWriter)this.writer).getSequenceReader();
/*    */   }
/*    */ 
/*    */   public IRCollection getIRCollection() {
/* 42 */     return ((OnlineSequenceIndexWriter)this.writer).getIRCollection();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.OnlineSequenceIndexer
 * JD-Core Version:    0.6.2
 */