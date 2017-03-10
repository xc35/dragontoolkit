/*    */ package edu.drexel.cis.dragon.ir.index.sequence;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.BasicIRDocIndexList;
/*    */ import edu.drexel.cis.dragon.ir.index.BasicIRTermIndexList;
/*    */ import edu.drexel.cis.dragon.ir.index.FileIndex;
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDocIndexList;
/*    */ import edu.drexel.cis.dragon.ir.index.IRTermIndexList;
/*    */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.io.File;
/*    */ 
/*    */ public class BasicSequenceIndexWriter extends AbstractSequenceIndexWriter
/*    */ {
/*    */   private FileIndex fileIndex;
/*    */ 
/*    */   public BasicSequenceIndexWriter(String directory)
/*    */   {
/* 21 */     this.fileIndex = new FileIndex(directory, false);
/*    */   }
/*    */ 
/*    */   public void initialize() {
/* 25 */     if (this.initialized)
/* 26 */       return;
/* 27 */     this.collection = new IRCollection();
/* 28 */     this.collection.load(this.fileIndex.getCollectionFilename());
/*    */ 
/* 30 */     this.termCache = new SortedArray(500);
/* 31 */     this.docKeyList = new SimpleElementList(this.fileIndex.getDocKeyListFilename(), true);
/* 32 */     this.termKeyList = new SimpleElementList(this.fileIndex.getTermKeyListFilename(), true);
/* 33 */     this.docIndexList = new BasicIRDocIndexList(this.fileIndex.getDocIndexListFilename(), true);
/* 34 */     this.termIndexList = new BasicIRTermIndexList(this.fileIndex.getTermIndexListFilename(), true);
/* 35 */     this.doctermMatrix = new SequenceFileWriter(this.fileIndex.getDocTermSeqIndexFilename(), this.fileIndex.getDocTermSeqFilename());
/* 36 */     this.initialized = true;
/*    */   }
/*    */ 
/*    */   public void close() {
/* 40 */     flush();
/* 41 */     this.collection.save(this.fileIndex.getCollectionFilename());
/* 42 */     this.docIndexList.close();
/* 43 */     this.termIndexList.close();
/* 44 */     this.doctermMatrix.close();
/* 45 */     this.docKeyList.close();
/* 46 */     this.termKeyList.close();
/* 47 */     this.initialized = false;
/*    */   }
/*    */ 
/*    */   public void clean()
/*    */   {
/* 53 */     File file = new File(this.fileIndex.getDirectory());
/* 54 */     file.delete();
/* 55 */     file.mkdir();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.BasicSequenceIndexWriter
 * JD-Core Version:    0.6.2
 */