/*     */ package edu.drexel.cis.dragon.ir.index.sentence;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDocIndexList;
/*     */ import edu.drexel.cis.dragon.ir.index.IRRelationIndexList;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTermIndexList;
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.SimplePairList;
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.TripleExtractor;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class OnlineSentenceIndexer extends AbstractSentenceIndexer
/*     */ {
/*     */   private OnlineSentenceWriteController writer;
/*     */   private ConceptExtractor cptExtractor;
/*     */   private TripleExtractor tripleExtractor;
/*     */ 
/*     */   public OnlineSentenceIndexer(ConceptExtractor extractor, boolean useConcept)
/*     */   {
/*  24 */     super(extractor.getDocumentParser());
/*  25 */     this.cptExtractor = extractor;
/*  26 */     this.writer = new OnlineSentenceWriteController(false, useConcept);
/*     */   }
/*     */ 
/*     */   public OnlineSentenceIndexer(TripleExtractor extractor, boolean useConcept) {
/*  30 */     super(extractor.getConceptExtractor().getDocumentParser());
/*  31 */     this.tripleExtractor = extractor;
/*  32 */     this.cptExtractor = extractor.getConceptExtractor();
/*  33 */     this.writer = new OnlineSentenceWriteController(true, useConcept);
/*     */   }
/*     */ 
/*     */   public boolean indexedSentence(String sentKey) {
/*  37 */     return this.writer.indexed(sentKey);
/*     */   }
/*     */ 
/*     */   public boolean isRelationSupported() {
/*  41 */     return this.writer.isRelationSupported();
/*     */   }
/*     */ 
/*     */   public boolean index(Sentence sent, String sentKey)
/*     */   {
/*     */     try
/*     */     {
/*  48 */       if ((sent == null) || (sent.getFirstWord() == null) || (!this.writer.setDoc(sentKey))) {
/*  49 */         return false;
/*     */       }
/*  51 */       this.writer.addRawSentence(sent);
/*  52 */       ArrayList cptList = this.cptExtractor.extractFromSentence(sent);
/*  53 */       if (this.writer.isRelationSupported()) {
/*  54 */         ArrayList tripleList = this.tripleExtractor.extractFromSentence(sent);
/*  55 */         this.writer.write(cptList, tripleList);
/*     */       }
/*     */       else {
/*  58 */         this.writer.write(cptList);
/*  59 */       }return true;
/*     */     }
/*     */     catch (Exception e) {
/*  62 */       e.printStackTrace();
/*  63 */     }return false;
/*     */   }
/*     */ 
/*     */   public void initialize()
/*     */   {
/*  68 */     if (this.initialized)
/*  69 */       return;
/*  70 */     this.writer.initialize();
/*  71 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   public void close() {
/*  75 */     this.initialized = false;
/*  76 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public OnlineSentenceBase getSentenceBase() {
/*  80 */     return this.writer.getSentenceBase();
/*     */   }
/*     */ 
/*     */   public IRTermIndexList getTermIndexList() {
/*  84 */     return this.writer.getTermIndexList();
/*     */   }
/*     */ 
/*     */   public IRRelationIndexList getRelationIndexList() {
/*  88 */     return this.writer.getRelationIndexList();
/*     */   }
/*     */ 
/*     */   public IRDocIndexList getDocIndexList() {
/*  92 */     return this.writer.getDocIndexList();
/*     */   }
/*     */ 
/*     */   public SimpleElementList getDocKeyList() {
/*  96 */     return this.writer.getDocKeyList();
/*     */   }
/*     */ 
/*     */   public SimpleElementList getTermKeyList() {
/* 100 */     return this.writer.getTermKeyList();
/*     */   }
/*     */ 
/*     */   public SimplePairList getRelationKeyList() {
/* 104 */     return this.writer.getRelationKeyList();
/*     */   }
/*     */ 
/*     */   public IntSparseMatrix getDocTermMatrix() {
/* 108 */     return this.writer.getDocTermMatrix();
/*     */   }
/*     */ 
/*     */   public IntSparseMatrix getDocRelationMatrix() {
/* 112 */     return this.writer.getDocRelationMatrix();
/*     */   }
/*     */ 
/*     */   public IRCollection getIRCollection() {
/* 116 */     return this.writer.getIRCollection();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sentence.OnlineSentenceIndexer
 * JD-Core Version:    0.6.2
 */