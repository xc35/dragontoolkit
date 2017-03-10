/*    */ package edu.drexel.cis.dragon.ir.index.sentence;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.extract.DualConceptExtractor;
/*    */ 
/*    */ public class DualSentenceIndexer extends AbstractSentenceIndexer
/*    */ {
/*    */   private BasicSentenceWriteController firstWriter;
/*    */   private BasicSentenceWriteController secondWriter;
/*    */   private DualConceptExtractor cptExtractor;
/*    */ 
/*    */   public DualSentenceIndexer(DualConceptExtractor extractor, String firstIndexFolder, String secondIndexFolder)
/*    */   {
/* 20 */     this(extractor, false, firstIndexFolder, false, secondIndexFolder);
/*    */   }
/*    */ 
/*    */   public DualSentenceIndexer(DualConceptExtractor extractor, boolean useConcept, String firstIndexFolder, String secondIndexFolder) {
/* 24 */     this(extractor, useConcept, firstIndexFolder, useConcept, secondIndexFolder);
/*    */   }
/*    */ 
/*    */   public DualSentenceIndexer(DualConceptExtractor extractor, boolean firstUseConcept, String firstIndexFolder, boolean secondUseConcept, String secondIndexFolder)
/*    */   {
/* 29 */     super(extractor.getDocumentParser());
/* 30 */     this.cptExtractor = extractor;
/* 31 */     this.firstWriter = new BasicSentenceWriteController(firstIndexFolder, false, firstUseConcept);
/* 32 */     this.secondWriter = new BasicSentenceWriteController(secondIndexFolder, false, secondUseConcept);
/*    */   }
/*    */ 
/*    */   public boolean indexedSentence(String sentKey) {
/* 36 */     return this.firstWriter.indexed(sentKey);
/*    */   }
/*    */ 
/*    */   public boolean index(Sentence sent, String sentKey) {
/*    */     try {
/* 41 */       if (this.firstWriter.indexed(sentKey)) {
/* 42 */         return false;
/*    */       }
/* 44 */       this.firstWriter.addRawSentence(sent);
/* 45 */       this.cptExtractor.initDocExtraction();
/* 46 */       this.cptExtractor.extractFromSentence(sent);
/* 47 */       this.firstWriter.write(this.cptExtractor.getFirstConceptList());
/* 48 */       this.secondWriter.write(this.cptExtractor.getSecondConceptList());
/* 49 */       return true;
/*    */     }
/*    */     catch (Exception e) {
/* 52 */       e.printStackTrace();
/* 53 */     }return false;
/*    */   }
/*    */ 
/*    */   public void initialize()
/*    */   {
/* 58 */     if (this.initialized) {
/* 59 */       return;
/*    */     }
/* 61 */     this.firstWriter.initialize();
/* 62 */     this.secondWriter.initialize();
/* 63 */     this.initialized = true;
/*    */   }
/*    */ 
/*    */   public void close() {
/* 67 */     this.firstWriter.close();
/* 68 */     this.secondWriter.close();
/* 69 */     super.close();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sentence.DualSentenceIndexer
 * JD-Core Version:    0.6.2
 */