/*    */ package edu.drexel.cis.dragon.ir.index.sentence;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*    */ import edu.drexel.cis.dragon.nlp.extract.TripleExtractor;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class BasicSentenceIndexer extends AbstractSentenceIndexer
/*    */ {
/*    */   private BasicSentenceWriteController writer;
/*    */   private ConceptExtractor cptExtractor;
/*    */   private TripleExtractor tripleExtractor;
/*    */ 
/*    */   public BasicSentenceIndexer(ConceptExtractor extractor, boolean useConcept, String indexFolder)
/*    */   {
/* 22 */     super(extractor.getDocumentParser());
/* 23 */     this.cptExtractor = extractor;
/* 24 */     this.writer = new BasicSentenceWriteController(indexFolder, false, useConcept);
/*    */   }
/*    */ 
/*    */   public BasicSentenceIndexer(TripleExtractor extractor, boolean useConcept, String indexFolder) {
/* 28 */     super(extractor.getConceptExtractor().getDocumentParser());
/* 29 */     this.tripleExtractor = extractor;
/* 30 */     this.cptExtractor = extractor.getConceptExtractor();
/* 31 */     this.writer = new BasicSentenceWriteController(indexFolder, true, useConcept);
/*    */   }
/*    */ 
/*    */   public boolean indexedSentence(String sentKey) {
/* 35 */     return this.writer.indexed(sentKey);
/*    */   }
/*    */ 
/*    */   public boolean index(Sentence sent, String sentKey)
/*    */   {
/*    */     try
/*    */     {
/* 42 */       if ((sent == null) || (sent.getFirstWord() == null) || (this.writer.indexed(sentKey))) {
/* 43 */         return false;
/*    */       }
/* 45 */       this.writer.addRawSentence(sent);
/* 46 */       ArrayList cptList = this.cptExtractor.extractFromSentence(sent);
/* 47 */       if (this.writer.isRelationSupported()) {
/* 48 */         ArrayList tripleList = this.tripleExtractor.extractFromSentence(sent);
/* 49 */         this.writer.write(cptList, tripleList);
/*    */       }
/*    */       else {
/* 52 */         this.writer.write(cptList);
/* 53 */       }return true;
/*    */     }
/*    */     catch (Exception e) {
/* 56 */       e.printStackTrace();
/* 57 */     }return false;
/*    */   }
/*    */ 
/*    */   public void initialize()
/*    */   {
/* 62 */     if (this.initialized)
/* 63 */       return;
/* 64 */     this.writer.initialize();
/* 65 */     this.initialized = true;
/*    */   }
/*    */ 
/*    */   public void close() {
/* 69 */     this.writer.close();
/* 70 */     super.close();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sentence.BasicSentenceIndexer
 * JD-Core Version:    0.6.2
 */