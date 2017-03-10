/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryWriter;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class BasicIRDocIndexList
/*     */   implements IRDocIndexList
/*     */ {
/*  17 */   private static int doc_cache_size = 5000;
/*     */   private FastBinaryWriter fbw;
/*     */   private ArrayList indexList;
/*     */   private boolean writingMode;
/*     */   private String indexlistFilename;
/*     */   private int docNum;
/*     */   private int lastDocIndex;
/*     */   private int doc_in_cache;
/*     */ 
/*     */   public BasicIRDocIndexList(String filename, boolean writingMode)
/*     */   {
/*     */     try
/*     */     {
/*  28 */       this.writingMode = writingMode;
/*  29 */       this.indexlistFilename = filename;
/*  30 */       if (writingMode) {
/*  31 */         this.doc_in_cache = 0;
/*  32 */         this.fbw = new FastBinaryWriter(filename, true);
/*  33 */         if (this.fbw.getFilePointer() < 4L)
/*  34 */           this.fbw.writeInt(0);
/*  35 */         this.lastDocIndex = (((int)this.fbw.getFilePointer() - 4) / 20 - 1);
/*     */       }
/*     */       else {
/*  38 */         this.indexList = loadDocIndexList(filename);
/*  39 */         this.docNum = this.indexList.size();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  43 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public IRDoc get(int index) {
/*  48 */     return index < this.docNum ? (IRDoc)this.indexList.get(index) : null;
/*     */   }
/*     */ 
/*     */   public boolean add(IRDoc curDoc)
/*     */   {
/*     */     try
/*     */     {
/*  55 */       if ((!this.writingMode) || (curDoc.getIndex() <= this.lastDocIndex))
/*  56 */         return false;
/*  57 */       for (int i = this.lastDocIndex + 1; i < curDoc.getIndex(); i++) {
/*  58 */         this.doc_in_cache += 1;
/*  59 */         this.fbw.writeInt(i);
/*  60 */         this.fbw.writeInt(0);
/*  61 */         this.fbw.writeInt(0);
/*  62 */         this.fbw.writeInt(0);
/*  63 */         this.fbw.writeInt(0);
/*     */       }
/*     */ 
/*  66 */       this.fbw.writeInt(curDoc.getIndex());
/*  67 */       this.fbw.writeInt(curDoc.getTermNum());
/*  68 */       this.fbw.writeInt(curDoc.getTermCount());
/*  69 */       this.fbw.writeInt(curDoc.getRelationNum());
/*  70 */       this.fbw.writeInt(curDoc.getRelationCount());
/*  71 */       this.doc_in_cache += 1;
/*  72 */       if (this.doc_in_cache >= doc_cache_size)
/*  73 */         flush();
/*  74 */       this.lastDocIndex = curDoc.getIndex();
/*  75 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/*  78 */       e.printStackTrace();
/*  79 */     }return false;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*     */     try
/*     */     {
/*  87 */       if (this.writingMode) {
/*  88 */         this.fbw.close();
/*  89 */         RandomAccessFile raf = new RandomAccessFile(this.indexlistFilename, "rw");
/*  90 */         raf.writeInt(this.lastDocIndex + 1);
/*  91 */         raf.close();
/*     */       }
/*     */       else {
/*  94 */         this.indexList.clear();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  98 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size() {
/* 103 */     if (this.writingMode) {
/* 104 */       return this.lastDocIndex + 1;
/*     */     }
/* 106 */     return this.docNum;
/*     */   }
/*     */ 
/*     */   private void flush() {
/* 110 */     if ((this.writingMode) && (this.fbw != null)) {
/* 111 */       this.fbw.flush();
/* 112 */       this.doc_in_cache = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   private ArrayList loadDocIndexList(String filename)
/*     */   {
/*     */     try
/*     */     {
/* 124 */       File file = new File(filename);
/* 125 */       if (!file.exists()) {
/* 126 */         return new ArrayList();
/*     */       }
/* 128 */       System.out.println(new Date() + " Loading Document List...");
/* 129 */       FastBinaryReader br = new FastBinaryReader(filename);
/* 130 */       int total = br.readInt();
/* 131 */       ArrayList list = new ArrayList(total);
/*     */ 
/* 133 */       for (int i = 0; i < total; i++) {
/* 134 */         IRDoc cur = new IRDoc(br.readInt());
/* 135 */         cur.setTermNum(br.readInt());
/* 136 */         cur.setTermCount(br.readInt());
/* 137 */         cur.setRelationNum(br.readInt());
/* 138 */         cur.setRelationCount(br.readInt());
/* 139 */         list.add(cur);
/*     */       }
/* 141 */       br.close();
/* 142 */       br.close();
/* 143 */       return list;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 147 */       e.printStackTrace();
/* 148 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.BasicIRDocIndexList
 * JD-Core Version:    0.6.2
 */