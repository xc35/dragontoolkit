/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.TripleExtractor;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class BasicIndexer extends AbstractIndexer
/*     */ {
/*     */   private BasicIndexWriteController charWriter;
/*     */   private BasicIndexWriteController cptWriter;
/*     */   private TripleExtractor te;
/*     */   private ConceptExtractor ce;
/*     */   private int minArticleSize;
/*     */   private boolean[] remainingSections;
/*     */ 
/*     */   public BasicIndexer(TripleExtractor te, boolean useConcept, String indexFolder)
/*     */   {
/*  24 */     super(true);
/*  25 */     this.te = te;
/*  26 */     this.remainingSections = new boolean[4];
/*  27 */     this.minArticleSize = 5;
/*  28 */     if (useConcept)
/*  29 */       this.cptWriter = new BasicIndexWriteController(indexFolder, this.relationSupported, true);
/*     */     else
/*  31 */       this.charWriter = new BasicIndexWriteController(indexFolder, this.relationSupported, false);
/*     */   }
/*     */ 
/*     */   public BasicIndexer(TripleExtractor te, String charIndexFolder, String cptIndexFolder) {
/*  35 */     super(true);
/*  36 */     this.te = te;
/*  37 */     this.remainingSections = new boolean[4];
/*  38 */     this.minArticleSize = 5;
/*  39 */     if (cptIndexFolder != null)
/*  40 */       this.cptWriter = new BasicIndexWriteController(cptIndexFolder, this.relationSupported, true);
/*  41 */     if (charIndexFolder != null)
/*  42 */       this.charWriter = new BasicIndexWriteController(charIndexFolder, this.relationSupported, false);
/*     */   }
/*     */ 
/*     */   public BasicIndexer(ConceptExtractor ce, boolean useConcept, String indexFolder) {
/*  46 */     super(false);
/*  47 */     this.ce = ce;
/*  48 */     this.remainingSections = new boolean[4];
/*  49 */     if (useConcept)
/*  50 */       this.cptWriter = new BasicIndexWriteController(indexFolder, this.relationSupported, true);
/*     */     else
/*  52 */       this.charWriter = new BasicIndexWriteController(indexFolder, this.relationSupported, false);
/*     */   }
/*     */ 
/*     */   public BasicIndexer(ConceptExtractor ce, String charIndexFolder, String cptIndexFolder) {
/*  56 */     super(false);
/*  57 */     this.ce = ce;
/*  58 */     this.remainingSections = new boolean[4];
/*  59 */     if (cptIndexFolder != null)
/*  60 */       this.cptWriter = new BasicIndexWriteController(cptIndexFolder, this.relationSupported, true);
/*  61 */     if (charIndexFolder != null)
/*  62 */       this.charWriter = new BasicIndexWriteController(charIndexFolder, this.relationSupported, false);
/*     */   }
/*     */ 
/*     */   public void close() {
/*  66 */     this.initialized = false;
/*  67 */     if (this.charWriter != null) {
/*  68 */       this.charWriter.close();
/*  69 */       this.charWriter = null;
/*     */     }
/*  71 */     if (this.cptWriter != null) {
/*  72 */       this.cptWriter.close();
/*  73 */       this.cptWriter = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean indexed(String docKey) {
/*  78 */     if (this.charWriter != null) {
/*  79 */       return this.charWriter.indexed(docKey);
/*     */     }
/*  81 */     if (this.cptWriter != null) {
/*  82 */       return this.cptWriter.indexed(docKey);
/*     */     }
/*     */ 
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   public void setMinArticleSize(int minSize)
/*     */   {
/*  90 */     this.minArticleSize = minSize;
/*     */   }
/*     */ 
/*     */   public void setSectionIndexOption(boolean all, boolean title, boolean abt, boolean body, boolean meta) {
/*  94 */     if (all) {
/*  95 */       addSection(new IRSection(0));
/*     */     }
/*  97 */     this.remainingSections[0] = (title ? false : true);
/*  98 */     if (title) {
/*  99 */       addSection(new IRSection(1));
/*     */     }
/* 101 */     this.remainingSections[1] = (abt ? false : true);
/* 102 */     if (abt) {
/* 103 */       addSection(new IRSection(2));
/*     */     }
/* 105 */     this.remainingSections[2] = (body ? false : true);
/* 106 */     if (body) {
/* 107 */       addSection(new IRSection(3));
/*     */     }
/* 109 */     this.remainingSections[3] = (meta ? false : true);
/* 110 */     if (meta)
/* 111 */       addSection(new IRSection(4));
/*     */   }
/*     */ 
/*     */   protected void initDocIndexing() {
/* 115 */     if (this.te != null)
/* 116 */       this.te.initDocExtraction();
/* 117 */     if (this.ce != null)
/* 118 */       this.ce.initDocExtraction();
/*     */   }
/*     */ 
/*     */   protected boolean extract(String content, ArrayList conceptList, ArrayList relationList)
/*     */   {
/*     */     try
/*     */     {
/* 125 */       if ((content == null) || (content.length() < this.minArticleSize)) {
/* 126 */         return true;
/*     */       }
/* 128 */       boolean ret = this.te.extractFromDoc(content);
/* 129 */       if (ret) {
/* 130 */         if (this.te.getConceptList() != null) {
/* 131 */           conceptList.addAll(this.te.getConceptList());
/*     */         }
/* 133 */         if (this.te.getTripleList() != null) {
/* 134 */           relationList.addAll(this.te.getTripleList());
/*     */         }
/*     */       }
/* 137 */       return ret;
/*     */     }
/*     */     catch (Exception e) {
/* 140 */       e.printStackTrace();
/* 141 */     }return false;
/*     */   }
/*     */ 
/*     */   protected boolean extract(String content, ArrayList conceptList)
/*     */   {
/*     */     try {
/* 147 */       if ((content == null) || (content.length() < this.minArticleSize)) {
/* 148 */         return true;
/*     */       }
/* 150 */       if (this.ce.extractFromDoc(content) != null) {
/* 151 */         conceptList.addAll(this.ce.getConceptList());
/* 152 */         return true;
/*     */       }
/*     */ 
/* 155 */       return false;
/*     */     }
/*     */     catch (Exception e) {
/* 158 */       e.printStackTrace();
/* 159 */     }return false;
/*     */   }
/*     */ 
/*     */   protected String getSection(Article paper, int sectionID)
/*     */   {
/* 164 */     switch (sectionID) {
/*     */     case 1:
/* 166 */       return paper.getTitle();
/*     */     case 2:
/* 168 */       return paper.getAbstract();
/*     */     case 3:
/* 170 */       return paper.getBody();
/*     */     case 4:
/* 172 */       return paper.getMeta();
/*     */     }
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   protected String getRemainingSections(Article paper)
/*     */   {
/* 182 */     StringBuffer sb = new StringBuffer();
/* 183 */     for (int i = 0; i < this.remainingSections.length; i++)
/*     */     {
/*     */       String section;
/* 184 */       if ((this.remainingSections[i] != false) && ((section = getSection(paper, i + 1)) != null) && (section.length() >= this.minArticleSize)) {
/* 185 */         if (sb.length() > 0) {
/* 186 */           sb.append("\n\n");
/*     */         }
/* 188 */         sb.append(section);
/*     */       }
/*     */     }
/* 191 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   protected void write(int sectionID, ArrayList conceptList) {
/* 195 */     if (this.charWriter != null)
/* 196 */       this.charWriter.write(sectionID, conceptList);
/* 197 */     if (this.cptWriter != null)
/* 198 */       this.cptWriter.write(sectionID, conceptList);
/*     */   }
/*     */ 
/*     */   protected void write(int sectionID, ArrayList conceptList, ArrayList relationList) {
/* 202 */     if (this.charWriter != null)
/* 203 */       this.charWriter.write(sectionID, conceptList, relationList);
/* 204 */     if (this.cptWriter != null)
/* 205 */       this.cptWriter.write(sectionID, conceptList, relationList);
/*     */   }
/*     */ 
/*     */   protected void initIndex() {
/* 209 */     if (this.charWriter != null)
/* 210 */       this.charWriter.initialize();
/* 211 */     if (this.cptWriter != null)
/* 212 */       this.cptWriter.initialize();
/*     */   }
/*     */ 
/*     */   protected void initSectionWrite(IRSection section) {
/* 216 */     if (this.charWriter != null)
/* 217 */       this.charWriter.addSection(section.copy());
/* 218 */     if (this.cptWriter != null)
/* 219 */       this.cptWriter.addSection(section.copy());
/*     */   }
/*     */ 
/*     */   protected boolean setDoc(String docKey)
/*     */   {
/* 225 */     boolean ret = true;
/* 226 */     if (this.charWriter != null)
/* 227 */       ret = this.charWriter.setDoc(docKey);
/* 228 */     if ((ret) && (this.cptWriter != null))
/* 229 */       ret = this.cptWriter.setDoc(docKey);
/* 230 */     return ret;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.BasicIndexer
 * JD-Core Version:    0.6.2
 */