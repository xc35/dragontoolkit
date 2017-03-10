/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ public class IRSection
/*     */   implements Comparable
/*     */ {
/*     */   public static final int SEC_ALL = 0;
/*     */   public static final int SEC_TITLE = 1;
/*     */   public static final int SEC_ABSTRACT = 2;
/*     */   public static final int SEC_BODY = 3;
/*     */   public static final int SEC_META = 4;
/*     */   public static final int DEF_SEC_NUM = 4;
/*     */   private int id;
/*     */   private String name;
/*     */   private IndexWriter indexWriter;
/*     */   private IndexReader indexReader;
/*     */   private boolean enabled;
/*     */   private double weight;
/*     */ 
/*     */   public IRSection(int section)
/*     */   {
/*  28 */     this.id = section;
/*  29 */     switch (section) {
/*     */     case 1:
/*  31 */       this.name = "title";
/*  32 */       break;
/*     */     case 2:
/*  34 */       this.name = "absract";
/*  35 */       break;
/*     */     case 3:
/*  37 */       this.name = "body";
/*  38 */       break;
/*     */     case 4:
/*  40 */       this.name = "meta";
/*  41 */       break;
/*     */     default:
/*  43 */       this.name = "all";
/*     */     }
/*  45 */     this.enabled = true;
/*  46 */     this.weight = 1.0D;
/*     */   }
/*     */ 
/*     */   public IRSection(int section, String name) {
/*  50 */     this.id = section;
/*  51 */     this.name = name;
/*  52 */     this.enabled = true;
/*     */   }
/*     */ 
/*     */   public IRSection copy()
/*     */   {
/*  58 */     IRSection cur = new IRSection(this.id, this.name);
/*  59 */     if (enabled())
/*  60 */       cur.enable();
/*     */     else
/*  62 */       cur.disable();
/*  63 */     cur.setIndexReader(getIndexReader());
/*  64 */     cur.setIndexWriter(getIndexWriter());
/*  65 */     cur.setWeight(getWeight());
/*  66 */     return cur;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object obj)
/*     */   {
/*  72 */     int objID = ((IRSection)obj).getSectionID();
/*  73 */     if (this.id == objID)
/*  74 */       return 0;
/*  75 */     if (this.id > objID) {
/*  76 */       return 1;
/*     */     }
/*  78 */     return -1;
/*     */   }
/*     */ 
/*     */   public String getSectionName() {
/*  82 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int getSectionID() {
/*  86 */     return this.id;
/*     */   }
/*     */ 
/*     */   public void setWeight(double weight) {
/*  90 */     this.weight = weight;
/*     */   }
/*     */ 
/*     */   public double getWeight() {
/*  94 */     return this.weight;
/*     */   }
/*     */ 
/*     */   public IndexWriter getIndexWriter() {
/*  98 */     return this.indexWriter;
/*     */   }
/*     */ 
/*     */   public void setIndexWriter(IndexWriter writer) {
/* 102 */     this.indexWriter = writer;
/*     */   }
/*     */ 
/*     */   public IndexReader getIndexReader() {
/* 106 */     return this.indexReader;
/*     */   }
/*     */ 
/*     */   public void setIndexReader(IndexReader reader) {
/* 110 */     this.indexReader = reader;
/*     */   }
/*     */ 
/*     */   public boolean enabled() {
/* 114 */     return this.enabled;
/*     */   }
/*     */ 
/*     */   public void enable() {
/* 118 */     this.enabled = true;
/*     */   }
/*     */ 
/*     */   public void disable() {
/* 122 */     this.enabled = false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.IRSection
 * JD-Core Version:    0.6.2
 */