/*     */ package edu.drexel.cis.dragon.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ 
/*     */ public class FastBinaryWriter extends DataOutputStream
/*     */ {
/*     */   private static final int BUF_SIZE = 1048576;
/*     */   private ByteArrayOutputStream baos;
/*     */   private DataOutputStream dos;
/*     */   private long offset;
/*     */ 
/*     */   public FastBinaryWriter(String outputFile)
/*     */   {
/*  21 */     super(new ByteArrayOutputStream());
/*  22 */     this.baos = ((ByteArrayOutputStream)this.out);
/*     */     try {
/*  24 */       this.dos = new DataOutputStream(new FileOutputStream(new File(outputFile)));
/*     */ 
/*  26 */       this.offset = 0L;
/*     */     }
/*     */     catch (Exception e) {
/*  29 */       this.dos = null;
/*  30 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public FastBinaryWriter(String outputFile, boolean append) {
/*  35 */     super(new ByteArrayOutputStream());
/*  36 */     this.baos = ((ByteArrayOutputStream)this.out);
/*     */ 
/*  38 */     File file = new File(outputFile);
/*     */     try {
/*  40 */       this.dos = new DataOutputStream(new FileOutputStream(file, append));
/*  41 */       this.offset = file.length();
/*     */     }
/*     */     catch (Exception e) {
/*  44 */       this.dos = null;
/*  45 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public long getFilePointer() {
/*  50 */     return this.offset + size();
/*     */   }
/*     */ 
/*     */   public int bytesInBuffer() {
/*  54 */     return this.baos.size();
/*     */   }
/*     */ 
/*     */   public void flush() {
/*     */     try {
/*  59 */       this.baos.writeTo(this.dos);
/*  60 */       this.baos.reset();
/*  61 */       this.dos.flush();
/*  62 */       super.flush();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*     */     try {
/*  72 */       flush();
/*  73 */       this.baos.close();
/*  74 */       this.dos.close();
/*  75 */       super.close();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(byte[] buf, int off, int len)
/*     */   {
/*     */     try {
/*  85 */       super.write(buf, off, len);
/*  86 */       if (this.baos.size() >= 1048576) flush(); 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  89 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(InputStream src, long length)
/*     */   {
/*     */     try
/*     */     {
/*  98 */       if (length <= 0L) return;
/*     */ 
/* 100 */       byte[] buf = new byte[(int)(10240L < length ? 10240L : length)];
/* 101 */       while (length > 0L) {
/* 102 */         int count = (int)(length > buf.length ? buf.length : length);
/* 103 */         count = src.read(buf, 0, count);
/* 104 */         if (count <= 0) break;
/* 105 */         write(buf, 0, count);
/* 106 */         length -= count;
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 114 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(RandomAccessFile src, long length)
/*     */   {
/*     */     try
/*     */     {
/* 123 */       byte[] buf = new byte[(int)(10240L < length ? 10240L : length)];
/* 124 */       while (length > 0L) {
/* 125 */         int count = (int)(length > buf.length ? buf.length : length);
/* 126 */         count = src.read(buf, 0, count);
/* 127 */         if (count <= 0) break;
/* 128 */         write(buf, 0, count);
/* 129 */         length -= count;
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 137 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.FastBinaryWriter
 * JD-Core Version:    0.6.2
 */