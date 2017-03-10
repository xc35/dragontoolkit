/*    */ package edu.drexel.cis.dragon.ir.index.sentence;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.BasicIndexReader;
/*    */ import edu.drexel.cis.dragon.ir.index.FileIndex;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicCollectionReader;
/*    */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*    */ import java.io.File;
/*    */ 
/*    */ public class BasicSentenceIndexReader extends BasicIndexReader
/*    */ {
/*    */   private FileIndex fileIndex;
/*    */ 
/*    */   public BasicSentenceIndexReader(String directory, boolean relationSupported)
/*    */   {
/* 19 */     super(directory, relationSupported);
/* 20 */     this.fileIndex = new FileIndex(directory, relationSupported);
/*    */   }
/*    */ 
/*    */   public void initialize()
/*    */   {
/* 25 */     super.initialize();
/* 26 */     if ((new File(this.fileIndex.getRawSentenceCollectionFilename()).exists()) && (new File(this.fileIndex.getRawSentenceIndexFilename()).exists()))
/* 27 */       this.collectionReader = new BasicCollectionReader(this.fileIndex.getDirectory(), FileIndex.getSentenceCollectionName());
/*    */   }
/*    */ 
/*    */   public void close() {
/* 31 */     this.collectionReader.close();
/* 32 */     super.close();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sentence.BasicSentenceIndexReader
 * JD-Core Version:    0.6.2
 */