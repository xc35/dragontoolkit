/*    */ package edu.drexel.cis.dragon.ir.index;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*    */ import edu.drexel.cis.dragon.matrix.IntSuperSparseMatrix;
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import java.io.File;
/*    */ 
/*    */ public class BasicIndexWriter extends AbstractIndexWriter
/*    */ {
/*    */   private FileIndex fileIndex;
/*    */ 
/*    */   public BasicIndexWriter(String directory, boolean relationSupported)
/*    */   {
/* 20 */     super(relationSupported);
/* 21 */     this.fileIndex = new FileIndex(directory, relationSupported);
/*    */   }
/*    */ 
/*    */   public void initialize() {
/* 25 */     if (this.initialized)
/* 26 */       return;
/* 27 */     this.doc_in_cache = 0;
/* 28 */     this.collection = new IRCollection();
/* 29 */     this.collection.load(this.fileIndex.getCollectionFilename());
/*    */ 
/* 31 */     this.docIndexList = new BasicIRDocIndexList(this.fileIndex.getDocIndexListFilename(), true);
/* 32 */     this.termIndexList = new BasicIRTermIndexList(this.fileIndex.getTermIndexListFilename(), true);
/* 33 */     this.doctermMatrix = new IntSuperSparseMatrix(this.fileIndex.getDocTermIndexFilename(), this.fileIndex.getDocTermFilename(), false, false);
/* 34 */     ((IntSuperSparseMatrix)this.doctermMatrix).setFlushInterval(2147483647);
/*    */ 
/* 36 */     if (this.relationSupported) {
/* 37 */       this.relationIndexList = new BasicIRRelationIndexList(this.fileIndex.getRelationIndexListFilename(), true);
/* 38 */       this.docrelationMatrix = new IntSuperSparseMatrix(this.fileIndex.getDocRelationIndexFilename(), this.fileIndex.getDocRelationFilename(), false, false);
/* 39 */       ((IntSuperSparseMatrix)this.docrelationMatrix).setFlushInterval(2147483647);
/*    */     }
/* 41 */     this.initialized = true;
/*    */   }
/*    */ 
/*    */   public void flush() {
/* 45 */     this.doc_in_cache = 0;
/* 46 */     this.collection.setDocNum(this.docIndexList.size());
/* 47 */     this.collection.setTermNum(this.termIndexList.size());
/* 48 */     ((IntSuperSparseMatrix)this.doctermMatrix).flush();
/*    */ 
/* 50 */     if (this.relationSupported) {
/* 51 */       this.collection.setRelationNum(this.relationIndexList.size());
/* 52 */       ((IntSuperSparseMatrix)this.docrelationMatrix).flush();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/* 60 */     flush();
/* 61 */     this.collection.save(this.fileIndex.getCollectionFilename());
/* 62 */     this.docIndexList.close();
/* 63 */     this.termIndexList.close();
/* 64 */     this.doctermMatrix.finalizeData();
/* 65 */     this.doctermMatrix.close();
/*    */ 
/* 67 */     if (this.relationSupported) {
/* 68 */       this.relationIndexList.close();
/* 69 */       this.docrelationMatrix.finalizeData();
/* 70 */       this.docrelationMatrix.close();
/*    */     }
/*    */ 
/* 73 */     TransposeIRMatrix trans = new TransposeIRMatrix();
/* 74 */     if (FileUtil.exist(this.fileIndex.getDocTermFilename()))
/* 75 */       trans.genTermDocMatrix(this.fileIndex.getDirectory());
/* 76 */     if ((this.relationSupported) && 
/* 77 */       (FileUtil.exist(this.fileIndex.getDocRelationFilename())))
/* 78 */       trans.genRelationDocMatrix(this.fileIndex.getDirectory());
/*    */   }
/*    */ 
/*    */   public void clean()
/*    */   {
/* 84 */     File file = new File(this.fileIndex.getDirectory());
/* 85 */     file.delete();
/* 86 */     file.mkdir();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.BasicIndexWriter
 * JD-Core Version:    0.6.2
 */