/*    */ package edu.drexel.cis.dragon.ir.index;
/*    */ 
/*    */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class OnlineIndexReader extends AbstractIndexReader
/*    */ {
/*    */   protected OnlineIndexer indexer;
/*    */ 
/*    */   public OnlineIndexReader(OnlineIndexer indexer)
/*    */   {
/* 19 */     this(indexer, null);
/*    */   }
/*    */ 
/*    */   public OnlineIndexReader(OnlineIndexer indexer, CollectionReader collectionReader) {
/* 23 */     super(indexer.isRelationSupported(), collectionReader);
/* 24 */     this.indexer = indexer;
/*    */   }
/*    */ 
/*    */   public void initialize() {
/* 28 */     if ((this.initialized) || (this.collectionReader == null)) {
/* 29 */       return;
/*    */     }
/* 31 */     this.indexer.initialize();
/* 32 */     if (!index(this.indexer, this.collectionReader)) {
/* 33 */       System.out.println("Failed to index articles!");
/* 34 */       return;
/*    */     }
/* 36 */     this.indexer.close();
/* 37 */     this.collection = this.indexer.getIRCollection();
/* 38 */     this.docIndexList = this.indexer.getDocIndexList();
/* 39 */     this.docKeyList = this.indexer.getDocKeyList();
/* 40 */     this.termIndexList = this.indexer.getTermIndexList();
/* 41 */     this.termKeyList = this.indexer.getTermKeyList();
/* 42 */     this.doctermMatrix = this.indexer.getDocTermMatrix();
/* 43 */     this.doctermMatrix.finalizeData();
/* 44 */     this.termdocMatrix = ((IntSparseMatrix)this.doctermMatrix.transpose());
/* 45 */     if (this.relationSupported) {
/* 46 */       this.relationIndexList = this.indexer.getRelationIndexList();
/* 47 */       this.docrelationMatrix = this.indexer.getDocRelationMatrix();
/* 48 */       this.docrelationMatrix.finalizeData();
/* 49 */       this.relationdocMatrix = ((IntSparseMatrix)this.docrelationMatrix.transpose());
/*    */     }
/* 51 */     this.initialized = true;
/*    */   }
/*    */ 
/*    */   public void setCollectionReader(CollectionReader collectionReader) {
/* 55 */     this.initialized = false;
/* 56 */     this.collectionReader = collectionReader;
/*    */   }
/*    */ 
/*    */   protected boolean index(Indexer indexer, CollectionReader collectionReader)
/*    */   {
/*    */     try
/*    */     {
/* 63 */       Article article = collectionReader.getNextArticle();
/* 64 */       while (article != null) {
/* 65 */         if (!indexer.indexed(article.getKey())) {
/* 66 */           indexer.index(article);
/*    */         }
/* 68 */         article = collectionReader.getNextArticle();
/*    */       }
/* 70 */       return true;
/*    */     }
/*    */     catch (Exception e) {
/* 73 */       e.printStackTrace();
/* 74 */     }return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.OnlineIndexReader
 * JD-Core Version:    0.6.2
 */