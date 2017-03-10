/*     */ package edu.drexel.cis.dragon.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ 
/*     */ public class FastFileInputStream extends InputStream
/*     */ {
/*     */   private byte[] buf;
/*     */   private int buf_length;
/*     */   private int buf_pos;
/*     */   private long available;
/*     */   private FileInputStream fis;
/*     */   private RandomAccessFile raf;
/*     */   private int inputType;
/*     */   private boolean class_open_file;
/*     */ 
/*     */   public FastFileInputStream(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  27 */       File f = new File(filename);
/*  28 */       this.available = f.length();
/*  29 */       this.fis = new FileInputStream(f);
/*  30 */       this.raf = null;
/*  31 */       this.buf_length = 512;
/*  32 */       this.buf = new byte[this.buf_length];
/*  33 */       this.buf_pos = this.buf_length;
/*  34 */       this.inputType = 1;
/*  35 */       this.class_open_file = true;
/*     */     }
/*     */     catch (Exception e) {
/*  38 */       this.fis = null;
/*  39 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public FastFileInputStream(File f) {
/*     */     try {
/*  45 */       this.available = f.length();
/*  46 */       this.fis = new FileInputStream(f);
/*  47 */       this.raf = null;
/*  48 */       this.buf_length = 512;
/*  49 */       this.buf = new byte[this.buf_length];
/*  50 */       this.buf_pos = this.buf_length;
/*  51 */       this.inputType = 1;
/*  52 */       this.class_open_file = true;
/*     */     }
/*     */     catch (Exception e) {
/*  55 */       this.fis = null;
/*  56 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public FastFileInputStream(RandomAccessFile raf, long length) {
/*  61 */     this.buf_length = 512;
/*  62 */     this.buf = new byte[this.buf_length];
/*  63 */     this.buf_pos = this.buf_length;
/*  64 */     this.raf = raf;
/*  65 */     this.fis = null;
/*  66 */     this.available = length;
/*  67 */     this.inputType = 2;
/*  68 */     this.class_open_file = false;
/*     */   }
/*     */ 
/*     */   public int read() throws IOException {
/*  72 */     refillBuffer();
/*  73 */     if (this.available <= 0L) {
/*  74 */       return -1;
/*     */     }
/*     */ 
/*  77 */     this.buf_pos += 1;
/*  78 */     this.available -= 1L;
/*  79 */     return this.buf[(this.buf_pos - 1)] & 0xFF;
/*     */   }
/*     */ 
/*     */   public int read(byte[] buffer) throws IOException {
/*  83 */     return read(buffer, 0, buffer.length);
/*     */   }
/*     */ 
/*     */   public int read(byte[] buffer, int off, int len)
/*     */     throws IOException
/*     */   {
/*  89 */     refillBuffer();
/*  90 */     if (this.available <= 0L) {
/*  91 */       return -1;
/*     */     }
/*     */ 
/*  94 */     int count = this.buf_length - this.buf_pos;
/*  95 */     if (this.available < len) len = (int)this.available;
/*     */ 
/*  97 */     if (len <= count) {
/*  98 */       System.arraycopy(this.buf, this.buf_pos, buffer, off, len);
/*  99 */       this.buf_pos += len;
/* 100 */       this.available -= len;
/* 101 */       return len;
/*     */     }
/*     */ 
/* 104 */     System.arraycopy(this.buf, this.buf_pos, buffer, off, count);
/* 105 */     this.buf_pos = this.buf_length;
/* 106 */     int left = internalRead(buffer, off + count, len - count);
/* 107 */     if (left <= 0) {
/* 108 */       this.available = 0L;
/* 109 */       return count;
/*     */     }
/*     */ 
/* 112 */     this.available -= count + left;
/* 113 */     return count + left;
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */   public void mark(int readLimit)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*     */   }
/*     */ 
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/* 134 */     if (this.available < n) n = this.available;
/* 135 */     int count = this.buf_length - this.buf_pos;
/* 136 */     if (n <= count) {
/* 137 */       this.buf_pos = ((int)(this.buf_pos + n));
/* 138 */       this.available -= n;
/* 139 */       return n;
/*     */     }
/*     */ 
/* 142 */     this.buf_pos = this.buf_length;
/* 143 */     long left = internalSkip(n - count);
/* 144 */     this.available = (this.available - count - left);
/* 145 */     return count + left;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */   {
/* 150 */     return (int)this.available;
/*     */   }
/*     */ 
/*     */   public long remaining() {
/* 154 */     return this.available;
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 158 */     if (this.class_open_file) {
/* 159 */       if (this.fis != null) this.fis.close();
/* 160 */       if (this.raf != null) this.raf.close(); 
/*     */     }
/*     */   }
/*     */ 
/*     */   private void refillBuffer()
/*     */   {
/* 165 */     if ((this.available <= 0L) || (this.buf_pos < this.buf_length)) {
/* 166 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 170 */       this.buf_length = internalRead(this.buf, 0, this.buf.length);
/* 171 */       this.buf_pos = 0;
/*     */     }
/*     */     catch (Exception e) {
/* 174 */       this.buf_length = 0;
/* 175 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private int internalRead(byte[] buffer, int off, int len) throws IOException {
/* 180 */     if (this.inputType == 1) {
/* 181 */       return this.fis.read(buffer, off, len);
/*     */     }
/* 183 */     return this.raf.read(buffer, off, len);
/*     */   }
/*     */ 
/*     */   private long internalSkip(long n) throws IOException {
/* 187 */     if (this.inputType == 1) {
/* 188 */       return this.fis.skip(n);
/*     */     }
/* 190 */     return this.raf.skipBytes((int)n);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.FastFileInputStream
 * JD-Core Version:    0.6.2
 */