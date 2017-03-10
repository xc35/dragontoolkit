/*     */ package edu.drexel.cis.dragon.ir.index.sentence;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.AbstractIndexWriteController;
/*     */ import edu.drexel.cis.dragon.ir.index.BasicIndexWriter;
/*     */ import edu.drexel.cis.dragon.ir.index.FileIndex;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRRelation;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexWriter;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.SimplePairList;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicCollectionWriter;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class BasicSentenceWriteController extends AbstractIndexWriteController
/*     */ {
/*     */   private IndexWriter indexWriter;
/*     */   private BasicCollectionWriter rawBW;
/*     */   private FileIndex fileIndex;
/*     */ 
/*     */   public BasicSentenceWriteController(String directory, boolean relationSupported, boolean indexConceptEntry)
/*     */   {
/*  23 */     super(relationSupported, indexConceptEntry);
/*  24 */     this.fileIndex = new FileIndex(directory, relationSupported);
/*     */   }
/*     */ 
/*     */   public void initialize() {
/*  28 */     if (this.initialized) {
/*  29 */       return;
/*     */     }
/*  31 */     this.docKeyList = new SimpleElementList(this.fileIndex.getDocKeyListFilename(), true);
/*  32 */     this.termKeyList = new SimpleElementList(this.fileIndex.getTermKeyListFilename(), true);
/*  33 */     if (this.relationSupported)
/*  34 */       this.relationKeyList = new SimplePairList(this.fileIndex.getRelationKeyListFilename(), true);
/*  35 */     this.indexWriter = new BasicIndexWriter(this.fileIndex.getDirectory(), this.relationSupported);
/*  36 */     this.indexWriter.initialize();
/*  37 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   public void flush() {
/*  41 */     this.indexWriter.flush();
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */     try {
/*  46 */       this.docKeyList.close();
/*  47 */       this.termKeyList.close();
/*  48 */       if (this.relationSupported)
/*  49 */         this.relationKeyList.close();
/*  50 */       this.indexWriter.close();
/*  51 */       this.initialized = false;
/*     */     }
/*     */     catch (Exception e) {
/*  54 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean addRawSentence(Sentence sent)
/*     */   {
/*     */     try
/*     */     {
/*  62 */       BasicArticle article = new BasicArticle();
/*  63 */       article.setKey(this.curDocKey);
/*  64 */       article.setTitle(sent.toString());
/*  65 */       this.rawBW.add(article);
/*  66 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/*  69 */       e.printStackTrace();
/*  70 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean write(ArrayList conceptList)
/*     */   {
/*     */     try
/*     */     {
/*  78 */       if (this.curDocKey == null) {
/*  79 */         return false;
/*     */       }
/*  81 */       IRDoc curDoc = new IRDoc(this.curDocKey);
/*  82 */       curDoc.setIndex(this.curDocIndex);
/*  83 */       IRTerm[] arrTerms = getIRTermArray(generateIRTermList(conceptList), curDoc);
/*  84 */       return this.indexWriter.write(curDoc, arrTerms);
/*     */     }
/*     */     catch (Exception e) {
/*  87 */       e.printStackTrace();
/*  88 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean write(ArrayList conceptList, ArrayList tripleList)
/*     */   {
/*     */     try
/*     */     {
/*  97 */       if (this.curDocKey == null) {
/*  98 */         return false;
/*     */       }
/* 100 */       IRDoc curDoc = new IRDoc(this.curDocKey);
/* 101 */       curDoc.setIndex(this.curDocIndex);
/* 102 */       IRTerm[] arrTerms = getIRTermArray(generateIRTermList(conceptList), curDoc);
/* 103 */       IRRelation[] arrRelations = getIRRelationArray(generateIRRelationList(tripleList), curDoc);
/* 104 */       return this.indexWriter.write(curDoc, arrTerms, arrRelations);
/*     */     }
/*     */     catch (Exception e) {
/* 107 */       e.printStackTrace();
/* 108 */     }return false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sentence.BasicSentenceWriteController
 * JD-Core Version:    0.6.2
 */