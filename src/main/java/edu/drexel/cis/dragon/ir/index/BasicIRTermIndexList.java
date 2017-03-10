/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.ByteArrayConvert;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryWriter;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class BasicIRTermIndexList
/*     */   implements IRTermIndexList, IRSignatureIndexList
/*     */ {
/*     */   private RandomAccessFile raf;
/*     */   private ArrayList indexList;
/*     */   private int elementLength;
/*     */   private boolean writingMode;
/*     */   private String indexlistFilename;
/*     */   private int termNum;
/*     */   private byte[] buf;
/*     */ 
/*     */   public BasicIRTermIndexList(String filename, boolean writingMode)
/*     */   {
/*     */     try
/*     */     {
/*  26 */       this.elementLength = 12;
/*  27 */       this.writingMode = writingMode;
/*  28 */       this.indexlistFilename = filename;
/*  29 */       if (writingMode) {
/*  30 */         this.raf = null;
/*  31 */         this.indexList = loadTermIndexList(filename);
/*     */       }
/*     */       else {
/*  34 */         this.raf = new RandomAccessFile(filename, "r");
/*  35 */         this.buf = new byte[this.elementLength];
/*  36 */         if (this.raf.length() > 0L)
/*  37 */           this.termNum = this.raf.readInt();
/*     */         else
/*  39 */           this.termNum = 0;
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  43 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public IRSignature getIRSignature(int index) {
/*  48 */     return get(index);
/*     */   }
/*     */ 
/*     */   public IRTerm get(int index) {
/*     */     try {
/*  53 */       if ((this.writingMode) || (index >= this.termNum)) return null;
/*     */ 
/*  55 */       this.raf.seek(index * this.elementLength + 4);
/*  56 */       this.raf.read(this.buf);
/*  57 */       return getIRTermFromByteArray(this.buf);
/*     */     }
/*     */     catch (Exception e) {
/*  60 */       e.printStackTrace();
/*  61 */     }return null;
/*     */   }
/*     */ 
/*     */   public boolean add(IRTerm curTerm)
/*     */   {
/*  68 */     if (!this.writingMode) return false;
/*     */ 
/*  70 */     if (curTerm.getIndex() < this.indexList.size()) {
/*  71 */       IRTerm oldTerm = (IRTerm)this.indexList.get(curTerm.getIndex());
/*  72 */       oldTerm.addFrequency(curTerm.getFrequency());
/*  73 */       oldTerm.setDocFrequency(oldTerm.getDocFrequency() + curTerm.getDocFrequency());
/*     */     }
/*     */     else
/*     */     {
/*  77 */       for (int i = this.indexList.size(); i < curTerm.getIndex(); i++) {
/*  78 */         this.indexList.add(new IRTerm(i, 0, 0));
/*     */       }
/*  80 */       curTerm = curTerm.copy();
/*  81 */       curTerm.setKey(null);
/*  82 */       this.indexList.add(curTerm);
/*     */     }
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */     try {
/*  89 */       if (this.writingMode) {
/*  90 */         saveTermIndexList(this.indexlistFilename, this.indexList);
/*  91 */         this.indexList.clear();
/*     */       }
/*  94 */       else if (this.raf != null) {
/*  95 */         this.raf.close();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  99 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size() {
/* 104 */     if (this.writingMode) {
/* 105 */       return this.indexList.size();
/*     */     }
/* 107 */     return this.termNum;
/*     */   }
/*     */ 
/*     */   private ArrayList loadTermIndexList(String filename)
/*     */   {
/*     */     try
/*     */     {
/* 118 */       File file = new File(filename);
/* 119 */       if (!file.exists()) {
/* 120 */         return new ArrayList();
/*     */       }
/* 122 */       System.out.println(new Date() + " Loading Term List...");
/* 123 */       FastBinaryReader fbr = new FastBinaryReader(filename);
/* 124 */       int total = fbr.readInt();
/* 125 */       ArrayList list = new ArrayList(total);
/*     */ 
/* 127 */       for (int i = 0; i < total; i++) {
/* 128 */         IRTerm cur = new IRTerm(fbr.readInt(), fbr.readInt(), fbr.readInt());
/* 129 */         list.add(cur);
/*     */       }
/* 131 */       fbr.close();
/* 132 */       return list;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 136 */       e.printStackTrace();
/* 137 */     }return null;
/*     */   }
/*     */ 
/*     */   private void saveTermIndexList(String filename, ArrayList list)
/*     */   {
/*     */     try
/*     */     {
/* 147 */       if (list == null) return;
/* 148 */       System.out.println(new Date() + " Saving Term Index List...");
/* 149 */       FastBinaryWriter fbw = new FastBinaryWriter(filename);
/* 150 */       fbw.writeInt(list.size());
/* 151 */       for (int i = 0; i < list.size(); i++) {
/* 152 */         IRTerm cur = (IRTerm)list.get(i);
/* 153 */         fbw.writeInt(cur.getIndex());
/* 154 */         fbw.writeInt(cur.getFrequency());
/* 155 */         fbw.writeInt(cur.getDocFrequency());
/* 156 */         if (i % 100000 == 0)
/* 157 */           fbw.flush();
/*     */       }
/* 159 */       fbw.close();
/*     */     }
/*     */     catch (Exception ex) {
/* 162 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private IRTerm getIRTermFromByteArray(byte[] array) {
/* 167 */     return new IRTerm(ByteArrayConvert.toInt(array, 0), ByteArrayConvert.toInt(array, 4), ByteArrayConvert.toInt(array, 8));
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.BasicIRTermIndexList
 * JD-Core Version:    0.6.2
 */