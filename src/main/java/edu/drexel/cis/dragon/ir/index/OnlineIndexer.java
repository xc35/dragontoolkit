/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.SimplePairList;
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.TripleExtractor;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class OnlineIndexer extends AbstractIndexer
/*     */ {
/*     */   private TripleExtractor te;
/*     */   private ConceptExtractor ce;
/*     */   private OnlineIndexWriteController writer;
/*     */   private int minArticleSize;
/*     */   private boolean useTitle;
/*     */   private boolean useAbstract;
/*     */   private boolean useBody;
/*     */   private boolean useMeta;
/*     */ 
/*     */   public OnlineIndexer(TripleExtractor te, boolean useConcept)
/*     */   {
/*  26 */     super(true);
/*  27 */     this.te = te;
/*  28 */     this.minArticleSize = 3;
/*  29 */     this.useTitle = true;
/*  30 */     this.useAbstract = true;
/*  31 */     this.useBody = true;
/*  32 */     this.useMeta = true;
/*  33 */     addSection(new IRSection(0));
/*  34 */     this.writer = new OnlineIndexWriteController(this.relationSupported, useConcept);
/*     */   }
/*     */ 
/*     */   public OnlineIndexer(ConceptExtractor ce, boolean useConcept) {
/*  38 */     super(false);
/*  39 */     this.ce = ce;
/*  40 */     this.minArticleSize = 3;
/*  41 */     this.useTitle = true;
/*  42 */     this.useAbstract = true;
/*  43 */     this.useBody = true;
/*  44 */     this.useMeta = true;
/*  45 */     addSection(new IRSection(0));
/*  46 */     this.writer = new OnlineIndexWriteController(this.relationSupported, useConcept);
/*     */   }
/*     */ 
/*     */   public void close() {
/*  50 */     this.initialized = false;
/*  51 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public boolean isRelationSupported() {
/*  55 */     return this.relationSupported;
/*     */   }
/*     */ 
/*     */   public boolean indexed(String docKey) {
/*  59 */     return this.writer.indexed(docKey);
/*     */   }
/*     */ 
/*     */   public void setMinArticleSize(int minSize) {
/*  63 */     this.minArticleSize = minSize;
/*     */   }
/*     */ 
/*     */   public boolean screenArticleContent(boolean useTitle, boolean useAbstract, boolean useBody, boolean useMeta) {
/*  67 */     if (this.initialized)
/*  68 */       return false;
/*  69 */     this.useTitle = useTitle;
/*  70 */     this.useAbstract = useAbstract;
/*  71 */     this.useBody = useBody;
/*  72 */     this.useMeta = useMeta;
/*  73 */     return true;
/*     */   }
/*     */ 
/*     */   public IRTermIndexList getTermIndexList() {
/*  77 */     return this.writer.getTermIndexList();
/*     */   }
/*     */ 
/*     */   public IRRelationIndexList getRelationIndexList() {
/*  81 */     return this.writer.getRelationIndexList();
/*     */   }
/*     */ 
/*     */   public IRDocIndexList getDocIndexList() {
/*  85 */     return this.writer.getDocIndexList();
/*     */   }
/*     */ 
/*     */   public SimpleElementList getDocKeyList() {
/*  89 */     return this.writer.getDocKeyList();
/*     */   }
/*     */ 
/*     */   public SimpleElementList getTermKeyList() {
/*  93 */     return this.writer.getTermKeyList();
/*     */   }
/*     */ 
/*     */   public SimplePairList getRelationKeyList() {
/*  97 */     return this.writer.getRelationKeyList();
/*     */   }
/*     */ 
/*     */   public IntSparseMatrix getDocTermMatrix() {
/* 101 */     return this.writer.getDocTermMatrix();
/*     */   }
/*     */ 
/*     */   public IntSparseMatrix getDocRelationMatrix() {
/* 105 */     return this.writer.getDocRelationMatrix();
/*     */   }
/*     */ 
/*     */   public IRCollection getIRCollection() {
/* 109 */     return this.writer.getIRCollection();
/*     */   }
/*     */ 
/*     */   protected void initDocIndexing() {
/* 113 */     if (this.te != null)
/* 114 */       this.te.initDocExtraction();
/* 115 */     if (this.ce != null)
/* 116 */       this.ce.initDocExtraction();
/*     */   }
/*     */ 
/*     */   protected boolean extract(String content, ArrayList conceptList, ArrayList relationList)
/*     */   {
/*     */     try
/*     */     {
/* 123 */       if ((content == null) || (content.length() < this.minArticleSize)) {
/* 124 */         return true;
/*     */       }
/* 126 */       boolean ret = this.te.extractFromDoc(content);
/* 127 */       if (ret) {
/* 128 */         if (this.te.getConceptList() != null) {
/* 129 */           conceptList.addAll(this.te.getConceptList());
/*     */         }
/* 131 */         if (this.te.getTripleList() != null) {
/* 132 */           relationList.addAll(this.te.getTripleList());
/*     */         }
/*     */       }
/* 135 */       return ret;
/*     */     }
/*     */     catch (Exception e) {
/* 138 */       e.printStackTrace();
/* 139 */     }return false;
/*     */   }
/*     */ 
/*     */   protected boolean extract(String content, ArrayList conceptList)
/*     */   {
/*     */     try {
/* 145 */       if ((content == null) || (content.length() < this.minArticleSize)) {
/* 146 */         return true;
/*     */       }
/* 148 */       if (this.ce.extractFromDoc(content) != null) {
/* 149 */         conceptList.addAll(this.ce.getConceptList());
/* 150 */         return true;
/*     */       }
/*     */ 
/* 153 */       return false;
/*     */     }
/*     */     catch (Exception e) {
/* 156 */       e.printStackTrace();
/* 157 */     }return false;
/*     */   }
/*     */ 
/*     */   protected String getRemainingSections(Article paper)
/*     */   {
/* 164 */     StringBuffer sb = new StringBuffer();
/*     */     String section;
/* 165 */     if ((this.useTitle) && ((section = paper.getTitle()) != null) && (section.length() >= this.minArticleSize)) {
/* 166 */       if (sb.length() > 0)
/* 167 */         sb.append("\n\n");
/* 168 */       sb.append(section);
/*     */     }
/* 170 */     if ((this.useAbstract) && ((section = paper.getAbstract()) != null) && (section.length() >= this.minArticleSize)) {
/* 171 */       if (sb.length() > 0)
/* 172 */         sb.append("\n\n");
/* 173 */       sb.append(section);
/*     */     }
/* 175 */     if ((this.useBody) && ((section = paper.getBody()) != null) && (section.length() >= this.minArticleSize)) {
/* 176 */       if (sb.length() > 0)
/* 177 */         sb.append("\n\n");
/* 178 */       sb.append(section);
/*     */     }
/* 180 */     if ((this.useMeta) && ((section = paper.getMeta()) != null) && (section.length() >= this.minArticleSize)) {
/* 181 */       if (sb.length() > 0)
/* 182 */         sb.append("\n\n");
/* 183 */       sb.append(section);
/*     */     }
/* 185 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   protected void write(int sectionID, ArrayList conceptList) {
/* 189 */     this.writer.write(conceptList);
/*     */   }
/*     */ 
/*     */   protected void write(int sectionID, ArrayList conceptList, ArrayList relationList) {
/* 193 */     this.writer.write(conceptList, relationList);
/*     */   }
/*     */ 
/*     */   protected void initSectionWrite(IRSection section)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void initIndex() {
/* 201 */     this.writer.initialize();
/*     */   }
/*     */ 
/*     */   protected boolean setDoc(String docKey) {
/* 205 */     return this.writer.setDoc(docKey);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.OnlineIndexer
 * JD-Core Version:    0.6.2
 */