/*     */ package edu.drexel.cis.dragon.onlinedb;
/*     */ 
/*     */ import java.util.Date;
/*     */ 
/*     */ public class BasicArticle
/*     */   implements Article
/*     */ {
/*     */   protected String rawText;
/*     */   protected String key;
/*     */   protected String title;
/*     */   protected String meta;
/*     */   protected String abt;
/*     */   protected String body;
/*     */   protected Date date;
/*     */   protected int category;
/*     */   protected int length;
/*     */ 
/*     */   public BasicArticle(String rawText)
/*     */   {
/*  21 */     this.rawText = rawText;
/*  22 */     this.title = null;
/*  23 */     this.abt = null;
/*  24 */     this.key = null;
/*  25 */     this.body = null;
/*  26 */     this.date = null;
/*  27 */     this.category = -1;
/*  28 */     this.length = -1;
/*     */   }
/*     */ 
/*     */   public BasicArticle() {
/*  32 */     this.title = null;
/*  33 */     this.abt = null;
/*  34 */     this.key = null;
/*  35 */     this.body = null;
/*  36 */     this.date = null;
/*  37 */     this.category = -1;
/*  38 */     this.rawText = null;
/*  39 */     this.length = -1;
/*     */   }
/*     */ 
/*     */   public String getRawText() {
/*  43 */     return this.rawText;
/*     */   }
/*     */ 
/*     */   public int getCategory() {
/*  47 */     return this.category;
/*     */   }
/*     */ 
/*     */   public void setCategory(int category) {
/*  51 */     this.category = category;
/*     */   }
/*     */ 
/*     */   public String getTitle() {
/*  55 */     return this.title;
/*     */   }
/*     */ 
/*     */   public void setTitle(String title) {
/*  59 */     this.title = title;
/*     */   }
/*     */ 
/*     */   public String getMeta() {
/*  63 */     return this.meta;
/*     */   }
/*     */ 
/*     */   public void setMeta(String meta) {
/*  67 */     this.meta = meta;
/*     */   }
/*     */ 
/*     */   public String getKey() {
/*  71 */     return this.key;
/*     */   }
/*     */ 
/*     */   public void setKey(String key) {
/*  75 */     this.key = key;
/*     */   }
/*     */ 
/*     */   public String getAbstract() {
/*  79 */     return this.abt;
/*     */   }
/*     */ 
/*     */   public void setAbstract(String abt) {
/*  83 */     this.abt = abt;
/*     */   }
/*     */ 
/*     */   public String getBody() {
/*  87 */     return this.body;
/*     */   }
/*     */ 
/*     */   public void setBody(String body) {
/*  91 */     this.body = body;
/*     */   }
/*     */ 
/*     */   public Date getDate() {
/*  95 */     return this.date;
/*     */   }
/*     */ 
/*     */   public void setDate(Date date) {
/*  99 */     this.date = date;
/*     */   }
/*     */ 
/*     */   public int getLength() {
/* 103 */     return this.length;
/*     */   }
/*     */ 
/*     */   public void setLength(int length) {
/* 107 */     this.length = length;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object obj) {
/* 111 */     return this.key.compareTo(((Article)obj).getKey());
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.BasicArticle
 * JD-Core Version:    0.6.2
 */