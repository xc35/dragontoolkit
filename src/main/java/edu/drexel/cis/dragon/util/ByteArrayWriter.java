/*     */ package edu.drexel.cis.dragon.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class ByteArrayWriter
/*     */   implements DataOutput
/*     */ {
/*     */   private ByteArrayOutputStream baos;
/*     */ 
/*     */   public ByteArrayWriter(int capacity)
/*     */   {
/*  18 */     this.baos = new ByteArrayOutputStream(capacity);
/*     */   }
/*     */ 
/*     */   public ByteArrayWriter() {
/*  22 */     this.baos = new ByteArrayOutputStream();
/*     */   }
/*     */ 
/*     */   public void write(int value) throws IOException {
/*  26 */     this.baos.write(value);
/*     */   }
/*     */ 
/*     */   public void writeByte(int value) throws IOException {
/*  30 */     this.baos.write(value);
/*     */   }
/*     */ 
/*     */   public void writeBytes(String value) throws IOException {
/*  34 */     this.baos.write(value.getBytes());
/*     */   }
/*     */ 
/*     */   public void writeBoolean(boolean value) throws IOException {
/*  38 */     if (value)
/*  39 */       this.baos.write(1);
/*     */     else
/*  41 */       this.baos.write(0);
/*     */   }
/*     */ 
/*     */   public void writeChar(int value) throws IOException {
/*  45 */     this.baos.write(ByteArrayConvert.toByte((char)value));
/*     */   }
/*     */ 
/*     */   public void writeChars(String value)
/*     */     throws IOException
/*     */   {
/*  51 */     int len = value.length();
/*  52 */     for (int i = 0; i < len; i++)
/*  53 */       this.baos.write(ByteArrayConvert.toByte(value.charAt(i)));
/*     */   }
/*     */ 
/*     */   public void writeShort(int value) throws IOException {
/*  57 */     this.baos.write(ByteArrayConvert.toByte((short)value));
/*     */   }
/*     */ 
/*     */   public void writeInt(int value) throws IOException {
/*  61 */     this.baos.write(ByteArrayConvert.toByte(value));
/*     */   }
/*     */ 
/*     */   public void writeLong(long value) throws IOException {
/*  65 */     this.baos.write(ByteArrayConvert.toByte(value));
/*     */   }
/*     */ 
/*     */   public void writeFloat(float value) throws IOException {
/*  69 */     this.baos.write(ByteArrayConvert.toByte(value));
/*     */   }
/*     */ 
/*     */   public void writeDouble(double value) throws IOException {
/*  73 */     this.baos.write(ByteArrayConvert.toByte(value));
/*     */   }
/*     */ 
/*     */   public void write(byte[] value) throws IOException {
/*  77 */     this.baos.write(value);
/*     */   }
/*     */ 
/*     */   public void write(byte[] value, int off, int len) throws IOException {
/*  81 */     this.baos.write(value, off, len);
/*     */   }
/*     */ 
/*     */   public void writeUTF(String value) throws IOException {
/*  85 */     System.out.println("not implemented yet!");
/*     */   }
/*     */ 
/*     */   public void reset() {
/*  89 */     this.baos.reset();
/*     */   }
/*     */ 
/*     */   public int size() {
/*  93 */     return this.baos.size();
/*     */   }
/*     */ 
/*     */   public byte[] toByteArray() {
/*  97 */     return this.baos.toByteArray();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 101 */     this.baos.close();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.ByteArrayWriter
 * JD-Core Version:    0.6.2
 */