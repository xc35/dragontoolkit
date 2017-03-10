/*     */ package edu.drexel.cis.dragon.ir.query;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRSection;
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.TripleExtractor;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public class BasicQueryGenerator extends AbstractQueryGenerator
/*     */ {
/*     */   protected QueryWriter writer;
/*     */   protected IRSection[] arrSections;
/*     */   protected boolean relationSupported;
/*     */   protected boolean initialized;
/*     */   private ArrayList sectionConceptList;
/*     */   private ArrayList sectionRelationList;
/*     */   private TripleExtractor te;
/*     */   private ConceptExtractor ce;
/*     */ 
/*     */   public BasicQueryGenerator(TripleExtractor te, boolean indexConceptEntry)
/*     */   {
/*  27 */     this.te = te;
/*  28 */     this.relationSupported = true;
/*  29 */     this.writer = new QueryWriter(this.relationSupported, indexConceptEntry);
/*  30 */     this.initialized = false;
/*  31 */     this.sectionConceptList = new ArrayList();
/*  32 */     this.sectionRelationList = new ArrayList();
/*     */   }
/*     */ 
/*     */   public BasicQueryGenerator(ConceptExtractor ce, boolean indexConceptEntry) {
/*  36 */     this.ce = ce;
/*  37 */     this.relationSupported = false;
/*  38 */     this.writer = new QueryWriter(this.relationSupported, indexConceptEntry);
/*  39 */     this.initialized = false;
/*  40 */     this.sectionConceptList = new ArrayList();
/*  41 */     this.sectionRelationList = new ArrayList();
/*     */   }
/*     */ 
/*     */   public void initialize(double titleWeight, double abstractWeight, double bodyWeight, double metaWeight, double subtermWeight)
/*     */   {
/*  49 */     ArrayList sectionList = new ArrayList();
/*  50 */     this.writer.setSubTermWeight(subtermWeight);
/*     */ 
/*  52 */     if (titleWeight > 0.0D) {
/*  53 */       IRSection cur = new IRSection(1);
/*  54 */       cur.setWeight(titleWeight);
/*  55 */       sectionList.add(cur);
/*     */     }
/*  57 */     if (abstractWeight > 0.0D) {
/*  58 */       IRSection cur = new IRSection(2);
/*  59 */       cur.setWeight(abstractWeight);
/*  60 */       sectionList.add(cur);
/*     */     }
/*  62 */     if (bodyWeight > 0.0D) {
/*  63 */       IRSection cur = new IRSection(3);
/*  64 */       cur.setWeight(bodyWeight);
/*  65 */       sectionList.add(cur);
/*     */     }
/*  67 */     if (metaWeight > 0.0D) {
/*  68 */       IRSection cur = new IRSection(4);
/*  69 */       cur.setWeight(metaWeight);
/*  70 */       sectionList.add(cur);
/*     */     }
/*     */ 
/*  73 */     this.arrSections = new IRSection[sectionList.size()];
/*  74 */     Collections.sort(sectionList);
/*  75 */     for (int i = 0; i < sectionList.size(); i++) {
/*  76 */       IRSection cur = (IRSection)sectionList.get(i);
/*  77 */       this.arrSections[i] = cur;
/*  78 */       this.writer.addSection(cur.copy());
/*     */     }
/*  80 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   public IRQuery generate(Article article)
/*     */   {
/*     */     try
/*     */     {
/*  87 */       if (!this.initialized) return null;
/*     */ 
/*  89 */       this.writer.initNewQuery();
/*     */ 
/*  91 */       for (int i = 0; i < this.arrSections.length; i++) {
/*  92 */         this.sectionConceptList.clear();
/*  93 */         this.sectionRelationList.clear();
/*  94 */         int sectionID = this.arrSections[i].getSectionID();
/*  95 */         if (this.relationSupported) {
/*  96 */           extract(getSection(article, sectionID), this.sectionConceptList, this.sectionRelationList);
/*  97 */           this.writer.write(sectionID, this.sectionConceptList, this.sectionRelationList);
/*     */         }
/*     */         else {
/* 100 */           extract(getSection(article, sectionID), this.sectionConceptList);
/* 101 */           this.writer.write(sectionID, this.sectionConceptList);
/*     */         }
/*     */       }
/* 104 */       return new RelSimpleQuery(this.writer.getQuery());
/*     */     }
/*     */     catch (Exception e) {
/* 107 */       e.printStackTrace();
/* 108 */     }return null;
/*     */   }
/*     */ 
/*     */   private boolean extract(String content, ArrayList conceptList, ArrayList relationList)
/*     */   {
/* 115 */     if ((content == null) || (content.length() <= 5)) {
/* 116 */       return true;
/*     */     }
/* 118 */     boolean ret = this.te.extractFromDoc(content);
/* 119 */     if (ret) {
/* 120 */       if (this.te.getConceptList() != null) {
/* 121 */         conceptList.addAll(this.te.getConceptList());
/*     */       }
/* 123 */       if (this.te.getTripleList() != null) {
/* 124 */         relationList.addAll(this.te.getTripleList());
/*     */       }
/*     */     }
/* 127 */     return ret;
/*     */   }
/*     */ 
/*     */   private boolean extract(String content, ArrayList conceptList) {
/* 131 */     if ((content == null) || (content.length() <= 2)) {
/* 132 */       return true;
/*     */     }
/* 134 */     if (this.ce.extractFromDoc(content) != null) {
/* 135 */       conceptList.addAll(this.ce.getConceptList());
/* 136 */       return true;
/*     */     }
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   private String getSection(Article paper, int sectionID) {
/* 142 */     switch (sectionID) {
/*     */     case 1:
/* 144 */       return paper.getTitle();
/*     */     case 2:
/* 146 */       return paper.getAbstract();
/*     */     case 3:
/* 148 */       return paper.getBody();
/*     */     case 4:
/* 150 */       return paper.getMeta();
/*     */     }
/* 152 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.BasicQueryGenerator
 * JD-Core Version:    0.6.2
 */