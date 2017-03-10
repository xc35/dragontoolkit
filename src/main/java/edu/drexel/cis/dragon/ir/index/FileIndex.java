/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import java.io.File;
/*     */ 
/*     */ public class FileIndex
/*     */ {
/*     */   protected String directory;
/*     */   protected boolean relationSupported;
/*     */ 
/*     */   public FileIndex(String directory, boolean relationSupported)
/*     */   {
/*  21 */     if ((directory.endsWith("\\")) || (directory.endsWith("/")))
/*  22 */       this.directory = directory.substring(0, directory.length() - 1);
/*     */     else
/*  24 */       this.directory = directory;
/*  25 */     this.relationSupported = relationSupported;
/*  26 */     File file = new File(directory);
/*  27 */     if (!file.exists()) file.mkdirs(); 
/*     */   }
/*     */ 
/*     */   public String getDirectory()
/*     */   {
/*  31 */     return this.directory;
/*     */   }
/*     */ 
/*     */   public boolean isRelationSupported() {
/*  35 */     return this.relationSupported;
/*     */   }
/*     */ 
/*     */   public String getCollectionFilename() {
/*  39 */     return this.directory + "/collection.stat";
/*     */   }
/*     */ 
/*     */   public String getTermDocFilename() {
/*  43 */     return this.directory + "/termdoc.matrix";
/*     */   }
/*     */ 
/*     */   public String getTermDocIndexFilename() {
/*  47 */     return this.directory + "/termdoc.index";
/*     */   }
/*     */ 
/*     */   public String getRelationDocFilename() {
/*  51 */     return this.directory + "/relationdoc.matrix";
/*     */   }
/*     */ 
/*     */   public String getRelationDocIndexFilename() {
/*  55 */     return this.directory + "/relationdoc.index";
/*     */   }
/*     */ 
/*     */   public String getDocRelationFilename() {
/*  59 */     return this.directory + "/docrelation.matrix";
/*     */   }
/*     */ 
/*     */   public String getDocRelationIndexFilename() {
/*  63 */     return this.directory + "/docrelation.index";
/*     */   }
/*     */ 
/*     */   public String getDocTermFilename() {
/*  67 */     return this.directory + "/docterm.matrix";
/*     */   }
/*     */ 
/*     */   public String getDocTermIndexFilename() {
/*  71 */     return this.directory + "/docterm.index";
/*     */   }
/*     */ 
/*     */   public String getDocTermSeqFilename() {
/*  75 */     return this.directory + "/doctermseq.matrix";
/*     */   }
/*     */ 
/*     */   public String getDocTermSeqIndexFilename() {
/*  79 */     return this.directory + "/doctermseq.index";
/*     */   }
/*     */ 
/*     */   public String getTermIndexListFilename() {
/*  83 */     return this.directory + "/termindex.list";
/*     */   }
/*     */ 
/*     */   public String getTermKeyListFilename() {
/*  87 */     return this.directory + "/termkey.list";
/*     */   }
/*     */ 
/*     */   public String getRelationIndexListFilename() {
/*  91 */     return this.directory + "/relationindex.list";
/*     */   }
/*     */ 
/*     */   public String getRelationKeyListFilename() {
/*  95 */     return this.directory + "/relationkey.list";
/*     */   }
/*     */ 
/*     */   public String getDocIndexListFilename() {
/*  99 */     return this.directory + "/docindex.list";
/*     */   }
/*     */ 
/*     */   public String getDocKeyListFilename() {
/* 103 */     return this.directory + "/dockey.list";
/*     */   }
/*     */ 
/*     */   public String getRawSentenceCollectionFilename() {
/* 107 */     return this.directory + "/rawsentence.collection";
/*     */   }
/*     */ 
/*     */   public String getRawSentenceIndexFilename() {
/* 111 */     return this.directory + "/rawsentence.index";
/*     */   }
/*     */ 
/*     */   public static String getSentenceCollectionName() {
/* 115 */     return "rawsentence";
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.FileIndex
 * JD-Core Version:    0.6.2
 */