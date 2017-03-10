/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.SimplePairList;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class OnlineIndexWriteController extends AbstractIndexWriteController
/*     */ {
/*     */   private OnlineIndexWriter indexWriter;
/*     */ 
/*     */   public OnlineIndexWriteController(boolean relationSupported, boolean indexConceptEntry)
/*     */   {
/*  20 */     super(relationSupported, indexConceptEntry);
/*  21 */     this.curDocIndex = -1;
/*  22 */     this.curDocKey = null;
/*     */   }
/*     */ 
/*     */   public void initialize() {
/*  26 */     if (this.initialized)
/*  27 */       return;
/*  28 */     this.docKeyList = new SimpleElementList();
/*  29 */     this.termKeyList = new SimpleElementList();
/*  30 */     if (this.relationSupported) {
/*  31 */       this.relationKeyList = new SimplePairList();
/*     */     }
/*  33 */     this.indexWriter = new OnlineIndexWriter(this.relationSupported);
/*  34 */     this.indexWriter.initialize();
/*  35 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   public void close() {
/*  39 */     this.indexWriter.close();
/*  40 */     this.initialized = false;
/*     */   }
/*     */ 
/*     */   public boolean write(ArrayList conceptList)
/*     */   {
/*     */     try
/*     */     {
/*  47 */       if (this.curDocKey == null) {
/*  48 */         return false;
/*     */       }
/*  50 */       IRDoc curDoc = new IRDoc(new String(this.curDocKey));
/*  51 */       curDoc.setIndex(this.curDocIndex);
/*  52 */       IRTerm[] arrTerms = getIRTermArray(generateIRTermList(conceptList), curDoc);
/*  53 */       return this.indexWriter.write(curDoc, arrTerms);
/*     */     }
/*     */     catch (Exception e) {
/*  56 */       e.printStackTrace();
/*  57 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean write(ArrayList conceptList, ArrayList tripleList)
/*     */   {
/*     */     try
/*     */     {
/*  66 */       if (this.curDocKey == null) {
/*  67 */         return false;
/*     */       }
/*  69 */       IRDoc curDoc = new IRDoc(new String(this.curDocKey));
/*  70 */       curDoc.setIndex(this.curDocIndex);
/*  71 */       IRTerm[] arrTerms = getIRTermArray(generateIRTermList(conceptList), curDoc);
/*  72 */       IRRelation[] arrRelations = getIRRelationArray(generateIRRelationList(tripleList), curDoc);
/*  73 */       return this.indexWriter.write(curDoc, arrTerms, arrRelations);
/*     */     }
/*     */     catch (Exception e) {
/*  76 */       e.printStackTrace();
/*  77 */     }return false;
/*     */   }
/*     */ 
/*     */   public SimpleElementList getDocKeyList()
/*     */   {
/*  82 */     return this.docKeyList;
/*     */   }
/*     */ 
/*     */   public SimpleElementList getTermKeyList() {
/*  86 */     return this.termKeyList;
/*     */   }
/*     */ 
/*     */   public SimplePairList getRelationKeyList() {
/*  90 */     return this.relationKeyList;
/*     */   }
/*     */ 
/*     */   public IRTermIndexList getTermIndexList() {
/*  94 */     return this.indexWriter.getTermIndexList();
/*     */   }
/*     */ 
/*     */   public IRRelationIndexList getRelationIndexList() {
/*  98 */     return this.indexWriter.getRelationIndexList();
/*     */   }
/*     */ 
/*     */   public IRDocIndexList getDocIndexList() {
/* 102 */     return this.indexWriter.getDocIndexList();
/*     */   }
/*     */ 
/*     */   public IntSparseMatrix getDocTermMatrix() {
/* 106 */     return this.indexWriter.getDocTermMatrix();
/*     */   }
/*     */ 
/*     */   public IntSparseMatrix getDocRelationMatrix() {
/* 110 */     return this.indexWriter.getDocRelationMatrix();
/*     */   }
/*     */ 
/*     */   public IRCollection getIRCollection() {
/* 114 */     return this.indexWriter.getIRCollection();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.OnlineIndexWriteController
 * JD-Core Version:    0.6.2
 */