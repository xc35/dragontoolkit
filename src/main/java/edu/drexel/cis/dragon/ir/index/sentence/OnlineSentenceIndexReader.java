/*    */ package edu.drexel.cis.dragon.ir.index.sentence;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.AbstractIndexReader;
/*    */ import edu.drexel.cis.dragon.ir.index.Indexer;
/*    */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class OnlineSentenceIndexReader extends AbstractIndexReader
/*    */ {
/*    */   protected OnlineSentenceIndexer indexer;
/*    */   private OnlineSentenceBase sentBase;
/*    */ 
/*    */   public OnlineSentenceIndexReader(OnlineSentenceIndexer indexer)
/*    */   {
/* 20 */     this(indexer, null);
/*    */   }
/*    */ 
/*    */   public OnlineSentenceIndexReader(OnlineSentenceIndexer indexer, CollectionReader collectionReader) {
/* 24 */     super(indexer.isRelationSupported(), collectionReader);
/* 25 */     this.indexer = indexer;
/*    */   }
/*    */ 
/*    */   public void initialize() {
/* 29 */     if ((this.initialized) || (this.collectionReader == null))
/* 30 */       return;
/* 31 */     this.indexer.initialize();
/* 32 */     if (!index(this.indexer, this.collectionReader)) {
/* 33 */       System.out.println("Failed to index articles!");
/* 34 */       return;
/*    */     }
/* 36 */     this.indexer.close();
/* 37 */     this.collection = this.indexer.getIRCollection();
/* 38 */     this.sentBase = this.indexer.getSentenceBase();
/* 39 */     this.docIndexList = this.indexer.getDocIndexList();
/* 40 */     this.docKeyList = this.indexer.getDocKeyList();
/* 41 */     this.termIndexList = this.indexer.getTermIndexList();
/* 42 */     this.termKeyList = this.indexer.getTermKeyList();
/* 43 */     this.doctermMatrix = this.indexer.getDocTermMatrix();
/* 44 */     this.doctermMatrix.finalizeData();
/* 45 */     this.termdocMatrix = ((IntSparseMatrix)this.doctermMatrix.transpose());
/* 46 */     if (this.relationSupported) {
/* 47 */       this.relationIndexList = this.indexer.getRelationIndexList();
/* 48 */       this.docrelationMatrix = this.indexer.getDocRelationMatrix();
/* 49 */       this.docrelationMatrix.finalizeData();
/* 50 */       this.relationdocMatrix = ((IntSparseMatrix)this.docrelationMatrix.transpose());
/*    */     }
/* 52 */     this.initialized = true;
/*    */   }
/*    */ 
/*    */   public void setCollectionReader(CollectionReader collectionReader) {
/* 56 */     this.initialized = false;
/* 57 */     this.collectionReader = collectionReader;
/*    */   }
/*    */ 
/*    */   public void close() {
/* 61 */     this.indexer.close();
/* 62 */     super.close();
/*    */   }
/*    */ 
/*    */   public Article getOriginalDoc(String key)
/*    */   {
/* 68 */     BasicArticle article = new BasicArticle();
/* 69 */     article.setTitle(this.sentBase.get(key));
/* 70 */     if (article.getTitle() == null) {
/* 71 */       return null;
/*    */     }
/* 73 */     article.setKey(key);
/* 74 */     return article;
/*    */   }
/*    */ 
/*    */   protected boolean index(Indexer indexer, CollectionReader collectionReader)
/*    */   {
/*    */     try
/*    */     {
/* 81 */       Article article = collectionReader.getNextArticle();
/* 82 */       while (article != null) {
/* 83 */         if (!indexer.indexed(article.getKey()))
/* 84 */           indexer.index(article);
/* 85 */         article = collectionReader.getNextArticle();
/*    */       }
/* 87 */       return true;
/*    */     }
/*    */     catch (Exception e) {
/* 90 */       e.printStackTrace();
/* 91 */     }return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sentence.OnlineSentenceIndexReader
 * JD-Core Version:    0.6.2
 */