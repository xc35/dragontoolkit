/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.SimplePairList;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class BasicIndexWriteController extends AbstractIndexWriteController
/*     */ {
/*     */   private static final int MAX_SEC = 10;
/*     */   private FileIndex fileIndex;
/*     */   private IRSection[] arrSections;
/*     */ 
/*     */   public BasicIndexWriteController(String directory, boolean relationSupported, boolean indexConceptEntry)
/*     */   {
/*  21 */     super(relationSupported, indexConceptEntry);
/*  22 */     this.fileIndex = new FileIndex(directory, relationSupported);
/*  23 */     this.arrSections = new IRSection[10];
/*     */   }
/*     */ 
/*     */   public void initialize() {
/*  27 */     if (this.initialized)
/*  28 */       return;
/*  29 */     this.processedDoc = 0;
/*  30 */     this.docKeyList = new SimpleElementList(this.fileIndex.getDocKeyListFilename(), true);
/*  31 */     this.termKeyList = new SimpleElementList(this.fileIndex.getTermKeyListFilename(), true);
/*  32 */     if (this.relationSupported) {
/*  33 */       this.relationKeyList = new SimplePairList(this.fileIndex.getRelationKeyListFilename(), true);
/*     */     }
/*  35 */     for (int i = 0; i < this.arrSections.length; i++)
/*  36 */       if ((this.arrSections[i] != null) && (this.arrSections[i].getIndexWriter() != null))
/*  37 */         this.arrSections[i].getIndexWriter().initialize();
/*  38 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   public boolean addSection(IRSection section)
/*     */   {
/*  44 */     if (section.getSectionID() > 10) return false;
/*     */ 
/*  46 */     IndexWriter cur = new BasicIndexWriter(this.fileIndex.getDirectory() + "/" + section.getSectionName(), this.relationSupported);
/*  47 */     cur.initialize();
/*  48 */     section.setIndexWriter(cur);
/*  49 */     this.arrSections[section.getSectionID()] = section;
/*  50 */     return true;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/*  56 */     for (int i = 0; i < 10; i++)
/*  57 */       if (this.arrSections[i] != null)
/*  58 */         this.arrSections[i].getIndexWriter().flush();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  65 */     this.docKeyList.close();
/*  66 */     this.termKeyList.close();
/*  67 */     if (this.relationSupported) {
/*  68 */       this.relationKeyList.close();
/*     */     }
/*  70 */     for (int i = 0; i < 10; i++)
/*  71 */       if (this.arrSections[i] != null)
/*  72 */         this.arrSections[i].getIndexWriter().close();
/*     */   }
/*     */ 
/*     */   public boolean write(int section, ArrayList conceptList)
/*     */   {
/*     */     try
/*     */     {
/*  80 */       if ((this.arrSections[section] == null) || (this.curDocKey == null)) {
/*  81 */         return false;
/*     */       }
/*  83 */       IRDoc curDoc = new IRDoc(this.curDocKey);
/*  84 */       curDoc.setIndex(this.curDocIndex);
/*  85 */       IRTerm[] arrTerms = getIRTermArray(generateIRTermList(conceptList), curDoc);
/*  86 */       return this.arrSections[section].getIndexWriter().write(curDoc, arrTerms);
/*     */     }
/*     */     catch (Exception e) {
/*  89 */       e.printStackTrace();
/*  90 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean write(int section, ArrayList conceptList, ArrayList tripleList)
/*     */   {
/*     */     try
/*     */     {
/*  99 */       if ((this.arrSections[section] == null) || (this.curDocKey == null)) {
/* 100 */         return false;
/*     */       }
/* 102 */       IRDoc curDoc = new IRDoc(this.curDocKey);
/* 103 */       curDoc.setIndex(this.curDocIndex);
/* 104 */       IRTerm[] arrTerms = getIRTermArray(generateIRTermList(conceptList), curDoc);
/* 105 */       IRRelation[] arrRelations = getIRRelationArray(generateIRRelationList(tripleList), curDoc);
/* 106 */       return this.arrSections[section].getIndexWriter().write(curDoc, arrTerms, arrRelations);
/*     */     }
/*     */     catch (Exception e) {
/* 109 */       e.printStackTrace();
/* 110 */     }return false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.BasicIndexWriteController
 * JD-Core Version:    0.6.2
 */