/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ 
/*     */ public abstract class AbstractIndexer
/*     */   implements Indexer
/*     */ {
/*     */   private PrintWriter log;
/*     */   protected IRSection[] arrSections;
/*     */   protected boolean relationSupported;
/*     */   protected boolean initialized;
/*     */   protected boolean enable_AllSection;
/*     */   private ArrayList sectionList;
/*     */   private ArrayList conceptList;
/*     */   private ArrayList relationList;
/*     */   private ArrayList sectionConceptList;
/*     */   private ArrayList sectionRelationList;
/*     */ 
/*     */   public AbstractIndexer(boolean relationSupported)
/*     */   {
/*  31 */     this.log = null;
/*  32 */     this.relationSupported = relationSupported;
/*  33 */     this.initialized = false;
/*  34 */     this.sectionList = new ArrayList();
/*  35 */     this.conceptList = new ArrayList();
/*  36 */     this.relationList = new ArrayList();
/*  37 */     this.sectionConceptList = new ArrayList();
/*  38 */     this.sectionRelationList = new ArrayList(); } 
/*     */   protected abstract void initDocIndexing();
/*     */ 
/*     */   protected abstract String getRemainingSections(Article paramArticle);
/*     */ 
/*     */   protected abstract boolean extract(String paramString, ArrayList paramArrayList);
/*     */ 
/*     */   protected abstract boolean extract(String paramString, ArrayList paramArrayList1, ArrayList paramArrayList2);
/*     */ 
/*     */   protected abstract boolean setDoc(String paramString);
/*     */ 
/*     */   protected abstract void initSectionWrite(IRSection paramIRSection);
/*     */ 
/*     */   protected abstract void initIndex();
/*     */ 
/*     */   protected abstract void write(int paramInt, ArrayList paramArrayList);
/*     */ 
/*     */   protected abstract void write(int paramInt, ArrayList paramArrayList1, ArrayList paramArrayList2);
/*     */ 
/*  52 */   public void setLog(String logFile) { this.log = FileUtil.getPrintWriter(logFile); }
/*     */ 
/*     */ 
/*     */   protected boolean addSection(IRSection section)
/*     */   {
/*  59 */     if (this.initialized) return false;
/*     */ 
/*  61 */     for (int i = 0; i < this.sectionList.size(); i++) {
/*  62 */       IRSection cur = (IRSection)this.sectionList.get(i);
/*  63 */       if (cur.getSectionID() == section.getSectionID())
/*  64 */         return false;
/*  65 */       if (cur.getSectionName().equalsIgnoreCase(section.getSectionName()))
/*  66 */         return false;
/*     */     }
/*  68 */     this.sectionList.add(section);
/*  69 */     return true;
/*     */   }
/*     */ 
/*     */   public void initialize()
/*     */   {
/*  76 */     if (this.initialized) return;
/*     */ 
/*  78 */     this.arrSections = new IRSection[this.sectionList.size()];
/*  79 */     Collections.sort(this.sectionList);
/*  80 */     for (int i = 0; i < this.sectionList.size(); i++) {
/*  81 */       IRSection cur = (IRSection)this.sectionList.get(i);
/*  82 */       this.arrSections[i] = cur;
/*  83 */       if (cur.getSectionID() == 0)
/*  84 */         this.enable_AllSection = true;
/*  85 */       initSectionWrite(cur);
/*     */     }
/*  87 */     initIndex();
/*  88 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   public boolean index(Article article)
/*     */   {
/*     */     try
/*     */     {
/*  96 */       if (!this.initialized) return false;
/*     */ 
/*  98 */       writeLog(new Date().toString());
/*  99 */       writeLog("Indexing article #" + article.getKey() + ": ");
/*     */ 
/* 101 */       this.conceptList.clear();
/* 102 */       this.relationList.clear();
/* 103 */       initDocIndexing();
/* 104 */       if (!setDoc(article.getKey())) {
/* 105 */         writeLog("indexed\n");
/* 106 */         return false;
/*     */       }
/*     */       int i;
/* 109 */       if (this.enable_AllSection)
/* 110 */         i = 1;
/*     */       else
/* 112 */         i = 0;
/* 113 */       boolean ret = true;
/* 114 */       for (; i < this.arrSections.length; i++) {
/* 115 */         this.sectionConceptList.clear();
/* 116 */         this.sectionRelationList.clear();
/* 117 */         int sectionID = this.arrSections[i].getSectionID();
/* 118 */         if (this.relationSupported) {
/* 119 */           ret = extract(getSection(article, sectionID), this.sectionConceptList, this.sectionRelationList);
/* 120 */           write(sectionID, this.sectionConceptList, this.sectionRelationList);
/* 121 */           if (this.enable_AllSection) {
/* 122 */             this.conceptList.addAll(this.sectionConceptList);
/* 123 */             this.relationList.addAll(this.sectionRelationList);
/*     */           }
/*     */         }
/*     */         else {
/* 127 */           ret = extract(getSection(article, sectionID), this.sectionConceptList);
/* 128 */           write(sectionID, this.sectionConceptList);
/* 129 */           if (this.enable_AllSection) {
/* 130 */             this.conceptList.addAll(this.sectionConceptList);
/*     */           }
/*     */         }
/* 133 */         if (!ret)
/*     */           break;
/*     */       }
/* 136 */       if ((this.enable_AllSection) && (ret)) {
/* 137 */         this.sectionConceptList.clear();
/* 138 */         this.sectionRelationList.clear();
/* 139 */         if (this.relationSupported) {
/* 140 */           ret = extract(getRemainingSections(article), this.sectionConceptList, this.sectionRelationList);
/* 141 */           this.conceptList.addAll(this.sectionConceptList);
/* 142 */           this.relationList.addAll(this.sectionRelationList);
/* 143 */           write(0, this.conceptList, this.relationList);
/*     */         }
/*     */         else {
/* 146 */           ret = extract(getRemainingSections(article), this.sectionConceptList);
/* 147 */           this.conceptList.addAll(this.sectionConceptList);
/* 148 */           write(0, this.conceptList);
/*     */         }
/*     */       }
/* 151 */       if (ret) {
/* 152 */         writeLog("succeeded\r\n");
/* 153 */         return true;
/*     */       }
/*     */ 
/* 156 */       writeLog("failed\r\n");
/* 157 */       return false;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 161 */       e.printStackTrace();
/* 162 */       writeLog("failed\r\n");
/* 163 */     }return false;
/*     */   }
/*     */ 
/*     */   protected String getSection(Article paper, int sectionID)
/*     */   {
/* 168 */     switch (sectionID) {
/*     */     case 1:
/* 170 */       return paper.getTitle();
/*     */     case 2:
/* 172 */       return paper.getAbstract();
/*     */     case 3:
/* 174 */       return paper.getBody();
/*     */     case 4:
/* 176 */       return paper.getMeta();
/*     */     }
/* 178 */     return null;
/*     */   }
/*     */ 
/*     */   protected void writeLog(String content) {
/* 182 */     if (this.log != null) {
/* 183 */       this.log.write(content);
/* 184 */       this.log.flush();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.AbstractIndexer
 * JD-Core Version:    0.6.2
 */