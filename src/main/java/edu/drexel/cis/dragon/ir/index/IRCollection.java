/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryWriter;
/*     */ import java.io.File;
/*     */ 
/*     */ public class IRCollection
/*     */ {
/*     */   private int docNum;
/*     */   private int termNum;
/*     */   private int relationNum;
/*     */   private long termCount;
/*     */   private long relationCount;
/*     */ 
/*     */   public IRCollection()
/*     */   {
/*  21 */     this.docNum = 0;
/*  22 */     this.termNum = 0;
/*  23 */     this.relationNum = 0;
/*  24 */     this.termCount = 0L;
/*  25 */     this.relationCount = 0L;
/*     */   }
/*     */ 
/*     */   public int getDocNum() {
/*  29 */     return this.docNum;
/*     */   }
/*     */ 
/*     */   public void setDocNum(int docNum) {
/*  33 */     this.docNum = docNum;
/*     */   }
/*     */ 
/*     */   public void addDocNum(int inc) {
/*  37 */     this.docNum += inc;
/*     */   }
/*     */ 
/*     */   public int getTermNum() {
/*  41 */     return this.termNum;
/*     */   }
/*     */ 
/*     */   public void setTermNum(int termNum) {
/*  45 */     this.termNum = termNum;
/*     */   }
/*     */ 
/*     */   public long getTermCount() {
/*  49 */     return this.termCount;
/*     */   }
/*     */ 
/*     */   public void setTermCount(long termCount) {
/*  53 */     this.termCount = termCount;
/*     */   }
/*     */ 
/*     */   public void addTermCount(int inc) {
/*  57 */     this.termCount += inc;
/*     */   }
/*     */ 
/*     */   public int getRelationNum() {
/*  61 */     return this.relationNum;
/*     */   }
/*     */ 
/*     */   public void setRelationNum(int relationNum) {
/*  65 */     this.relationNum = relationNum;
/*     */   }
/*     */ 
/*     */   public long getRelationCount() {
/*  69 */     return this.relationCount;
/*     */   }
/*     */ 
/*     */   public void setRelationCount(long relationCount) {
/*  73 */     this.relationCount = relationCount;
/*     */   }
/*     */ 
/*     */   public void addRelationCount(int inc) {
/*  77 */     this.relationCount += inc;
/*     */   }
/*     */ 
/*     */   public void load(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  85 */       File file = new File(filename);
/*  86 */       if (file.exists()) {
/*  87 */         FastBinaryReader fbr = new FastBinaryReader(file);
/*  88 */         setDocNum(fbr.readInt());
/*  89 */         setTermNum(fbr.readInt());
/*  90 */         setTermCount(fbr.readLong());
/*  91 */         setRelationNum(fbr.readInt());
/*  92 */         setRelationCount(fbr.readLong());
/*  93 */         fbr.close();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  97 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void save(String filename)
/*     */   {
/*     */     try
/*     */     {
/* 105 */       FastBinaryWriter fbs = new FastBinaryWriter(filename);
/* 106 */       fbs.writeInt(getDocNum());
/* 107 */       fbs.writeInt(getTermNum());
/* 108 */       fbs.writeLong(getTermCount());
/* 109 */       fbs.writeInt(getRelationNum());
/* 110 */       fbs.writeLong(getRelationCount());
/* 111 */       fbs.close();
/*     */     }
/*     */     catch (Exception e) {
/* 114 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.IRCollection
 * JD-Core Version:    0.6.2
 */