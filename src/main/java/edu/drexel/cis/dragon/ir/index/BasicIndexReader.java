/*    */ package edu.drexel.cis.dragon.ir.index;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.IntGiantSparseMatrix;
/*    */ import edu.drexel.cis.dragon.matrix.IntSuperSparseMatrix;
/*    */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*    */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*    */ import java.io.File;
/*    */ 
/*    */ public class BasicIndexReader extends AbstractIndexReader
/*    */ {
/*    */   private FileIndex fileIndex;
/*    */ 
/*    */   public BasicIndexReader(String directory, boolean relationSupported)
/*    */   {
/* 21 */     this(directory, relationSupported, null);
/*    */   }
/*    */ 
/*    */   public BasicIndexReader(String directory, boolean relationSupported, CollectionReader collectionReader) {
/* 25 */     super(relationSupported);
/* 26 */     this.relationSupported = relationSupported;
/* 27 */     this.collectionReader = collectionReader;
/* 28 */     this.fileIndex = new FileIndex(directory, relationSupported);
/*    */   }
/*    */ 
/*    */   public void initialize() {
/* 32 */     this.collection = new IRCollection();
/* 33 */     this.collection.load(this.fileIndex.getCollectionFilename());
/* 34 */     this.termIndexList = new BasicIRTermIndexList(this.fileIndex.getTermIndexListFilename(), false);
/* 35 */     this.docIndexList = new BasicIRDocIndexList(this.fileIndex.getDocIndexListFilename(), false);
/* 36 */     this.termdocMatrix = new IntSuperSparseMatrix(this.fileIndex.getTermDocIndexFilename(), this.fileIndex.getTermDocFilename());
/* 37 */     this.doctermMatrix = new IntGiantSparseMatrix(this.fileIndex.getDocTermIndexFilename(), this.fileIndex.getDocTermFilename());
/* 38 */     if (new File(this.fileIndex.getDocKeyListFilename()).exists())
/* 39 */       this.docKeyList = new SimpleElementList(this.fileIndex.getDocKeyListFilename(), false);
/* 40 */     if (new File(this.fileIndex.getTermKeyListFilename()).exists())
/* 41 */       this.termKeyList = new SimpleElementList(this.fileIndex.getTermKeyListFilename(), false);
/* 42 */     if (this.relationSupported) {
/* 43 */       this.relationIndexList = new BasicIRRelationIndexList(this.fileIndex.getRelationIndexListFilename(), false);
/* 44 */       this.relationdocMatrix = new IntGiantSparseMatrix(this.fileIndex.getRelationDocIndexFilename(), this.fileIndex.getRelationDocFilename());
/* 45 */       this.docrelationMatrix = new IntGiantSparseMatrix(this.fileIndex.getDocRelationIndexFilename(), this.fileIndex.getDocRelationFilename());
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.BasicIndexReader
 * JD-Core Version:    0.6.2
 */