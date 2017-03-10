/*    */ package edu.drexel.cis.dragon.ir.index.sequence;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.Indexer;
/*    */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class OnlineSequenceIndexReader extends AbstractSequenceIndexReader
/*    */ {
/*    */   private OnlineSequenceIndexer indexer;
/*    */ 
/*    */   public OnlineSequenceIndexReader(ConceptExtractor ce)
/*    */   {
/* 20 */     this(ce, null);
/*    */   }
/*    */ 
/*    */   public OnlineSequenceIndexReader(ConceptExtractor ce, CollectionReader collectionReader) {
/* 24 */     this(new OnlineSequenceIndexer(ce), collectionReader);
/*    */   }
/*    */ 
/*    */   public OnlineSequenceIndexReader(OnlineSequenceIndexer indexer) {
/* 28 */     this(indexer, null);
/*    */   }
/*    */ 
/*    */   public OnlineSequenceIndexReader(OnlineSequenceIndexer indexer, CollectionReader collectionReader) {
/* 32 */     this.collectionReader = collectionReader;
/* 33 */     this.indexer = indexer;
/*    */   }
/*    */ 
/*    */   public void initialize() {
/* 37 */     if ((this.initialized) || (this.collectionReader == null)) {
/* 38 */       return;
/*    */     }
/* 40 */     this.indexer.initialize();
/* 41 */     if (!index(this.indexer, this.collectionReader)) {
/* 42 */       System.out.println("Failed to index articles!");
/* 43 */       return;
/*    */     }
/* 45 */     this.indexer.close();
/* 46 */     this.collection = this.indexer.getIRCollection();
/* 47 */     this.docIndexList = this.indexer.getDocIndexList();
/* 48 */     this.docKeyList = this.indexer.getDocKeyList();
/* 49 */     this.termIndexList = this.indexer.getTermIndexList();
/* 50 */     this.termKeyList = this.indexer.getTermKeyList();
/* 51 */     this.doctermSeq = this.indexer.getSequenceReader();
/* 52 */     this.initialized = true;
/*    */   }
/*    */ 
/*    */   public void setCollectionReader(CollectionReader collectionReader) {
/* 56 */     this.initialized = false;
/* 57 */     this.collectionReader = collectionReader;
/*    */   }
/*    */ 
/*    */   protected boolean index(Indexer indexer, CollectionReader collectionReader)
/*    */   {
/*    */     try
/*    */     {
/* 64 */       Article article = collectionReader.getNextArticle();
/* 65 */       while (article != null) {
/* 66 */         if (!indexer.indexed(article.getKey())) {
/* 67 */           indexer.index(article);
/*    */         }
/* 69 */         article = collectionReader.getNextArticle();
/*    */       }
/* 71 */       return true;
/*    */     }
/*    */     catch (Exception e) {
/* 74 */       e.printStackTrace();
/* 75 */     }return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.OnlineSequenceIndexReader
 * JD-Core Version:    0.6.2
 */