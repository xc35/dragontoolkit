/*     */ package edu.drexel.cis.dragon.onlinedb.bibtex;
/*     */ 
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import java.util.Date;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class BibTeXArticle
/*     */   implements Article
/*     */ {
/*     */   private String key;
/*     */   private TreeMap map;
/*     */   private int category;
/*     */   private int length;
/*     */   private Date date;
/*     */ 
/*     */   public BibTeXArticle(String text)
/*     */   {
/*  27 */     int start = text.indexOf('{');
/*  28 */     int end = text.indexOf(',', start);
/*  29 */     this.key = text.substring(start + 1, end).trim();
/*  30 */     this.map = new TreeMap();
/*     */ 
/*  32 */     int next = text.indexOf("= {");
/*  33 */     while (next >= 0) {
/*  34 */       start = text.lastIndexOf('\t', next);
/*  35 */       String field = text.substring(start + 1, next).trim();
/*  36 */       end = text.indexOf("},", next);
/*  37 */       if (end < 0)
/*  38 */         end = text.indexOf("}", next);
/*  39 */       String val = text.substring(next + 3, end);
/*  40 */       set(field, val);
/*  41 */       next = text.indexOf("= {", end);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void set(String field, String val) {
/*  46 */     this.map.put(field, val);
/*     */   }
/*     */ 
/*     */   public String get(String field) {
/*  50 */     return (String)this.map.get(field);
/*     */   }
/*     */ 
/*     */   public int getCategory() {
/*  54 */     return this.category;
/*     */   }
/*     */ 
/*     */   public void setCategory(int category) {
/*  58 */     this.category = category;
/*     */   }
/*     */ 
/*     */   public String getTitle() {
/*  62 */     return get("title");
/*     */   }
/*     */ 
/*     */   public void setTitle(String title) {
/*  66 */     set("title", title);
/*     */   }
/*     */ 
/*     */   public String getMeta() {
/*  70 */     return get("keywords");
/*     */   }
/*     */ 
/*     */   public void setMeta(String meta) {
/*  74 */     set("keywords", meta);
/*     */   }
/*     */ 
/*     */   public String getKey() {
/*  78 */     return this.key;
/*     */   }
/*     */ 
/*     */   public void setKey(String key) {
/*  82 */     this.key = key;
/*     */   }
/*     */ 
/*     */   public String getAbstract() {
/*  86 */     return get("abstract");
/*     */   }
/*     */ 
/*     */   public void setAbstract(String abt) {
/*  90 */     set("abstract", abt);
/*     */   }
/*     */ 
/*     */   public String getBody() {
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */   public void setBody(String body) {
/*     */   }
/*     */ 
/*     */   public Date getDate() {
/* 101 */     return this.date;
/*     */   }
/*     */ 
/*     */   public void setDate(Date date) {
/* 105 */     this.date = date;
/*     */   }
/*     */ 
/*     */   public int getLength() {
/* 109 */     return this.length;
/*     */   }
/*     */ 
/*     */   public void setLength(int length) {
/* 113 */     this.length = length;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object obj) {
/* 117 */     return this.key.compareTo(((Article)obj).getKey());
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.bibtex.BibTeXArticle
 * JD-Core Version:    0.6.2
 */