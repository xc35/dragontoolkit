/*    */ package edu.drexel.cis.dragon.ir.index.sequence;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.BasicIRDocIndexList;
/*    */ import edu.drexel.cis.dragon.ir.index.BasicIRTermIndexList;
/*    */ import edu.drexel.cis.dragon.ir.index.FileIndex;
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*    */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*    */ 
/*    */ public class BasicSequenceIndexReader extends AbstractSequenceIndexReader
/*    */ {
/*    */   private FileIndex fileIndex;
/*    */ 
/*    */   public BasicSequenceIndexReader(String directory)
/*    */   {
/* 20 */     this(directory, null);
/*    */   }
/*    */ 
/*    */   public BasicSequenceIndexReader(String directory, CollectionReader collectionReader) {
/* 24 */     this.collectionReader = collectionReader;
/* 25 */     this.fileIndex = new FileIndex(directory, false);
/*    */   }
/*    */ 
/*    */   public void initialize() {
/* 29 */     if (this.initialized)
/* 30 */       return;
/* 31 */     this.collection = new IRCollection();
/* 32 */     this.collection.load(this.fileIndex.getCollectionFilename());
/* 33 */     this.termIndexList = new BasicIRTermIndexList(this.fileIndex.getTermIndexListFilename(), false);
/* 34 */     this.docIndexList = new BasicIRDocIndexList(this.fileIndex.getDocIndexListFilename(), false);
/* 35 */     this.termKeyList = new SimpleElementList(this.fileIndex.getTermKeyListFilename(), false);
/* 36 */     this.docKeyList = new SimpleElementList(this.fileIndex.getDocKeyListFilename(), false);
/* 37 */     this.doctermSeq = new SequenceFileReader(this.fileIndex.getDocTermSeqIndexFilename(), this.fileIndex.getDocTermSeqFilename());
/* 38 */     this.initialized = true;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.BasicSequenceIndexReader
 * JD-Core Version:    0.6.2
 */