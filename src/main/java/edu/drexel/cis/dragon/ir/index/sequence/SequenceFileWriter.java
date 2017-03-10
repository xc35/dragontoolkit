/*     */ package edu.drexel.cis.dragon.ir.index.sequence;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryWriter;
/*     */ import java.io.File;
/*     */ import java.io.RandomAccessFile;
/*     */ 
/*     */ public class SequenceFileWriter
/*     */   implements SequenceWriter
/*     */ {
/*     */   private static final int BUF_THRESHOLD = 512000;
/*     */   private String seqIndexFile;
/*     */   private String seqMatrixFile;
/*     */   private FastBinaryWriter matrixFile;
/*     */   private FastBinaryWriter indexFile;
/*     */   private int maxRowIndex;
/*     */   private int maxTermIndex;
/*     */   private int termCount;
/*     */ 
/*     */   public SequenceFileWriter(String seqIndexFile, String seqMatrixFile)
/*     */   {
/*  22 */     this.seqIndexFile = seqIndexFile;
/*  23 */     this.seqMatrixFile = seqMatrixFile;
/*  24 */     init();
/*     */   }
/*     */ 
/*     */   public void initialize()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean addSequence(int rowIndex, int[] seq)
/*     */   {
/*     */     try {
/*  34 */       if (rowIndex <= this.maxRowIndex) {
/*  35 */         return false;
/*     */       }
/*  37 */       for (int i = this.maxRowIndex + 1; i < rowIndex; i++) {
/*  38 */         this.indexFile.writeInt(i);
/*  39 */         this.indexFile.writeLong(this.matrixFile.getFilePointer());
/*  40 */         this.indexFile.writeInt(0);
/*  41 */         this.matrixFile.writeInt(i);
/*  42 */         this.matrixFile.writeInt(0);
/*     */       }
/*     */ 
/*  45 */       this.indexFile.writeInt(rowIndex);
/*  46 */       this.indexFile.writeLong(this.matrixFile.getFilePointer());
/*  47 */       this.indexFile.writeInt(seq.length);
/*  48 */       this.matrixFile.writeInt(rowIndex);
/*  49 */       this.matrixFile.writeInt(seq.length);
/*  50 */       for (int i = 0; i < seq.length; i++) {
/*  51 */         if (seq[i] > this.maxTermIndex)
/*  52 */           this.maxTermIndex = seq[i];
/*  53 */         this.matrixFile.writeInt(seq[i]);
/*     */       }
/*  55 */       this.termCount += seq.length;
/*  56 */       if (this.indexFile.bytesInBuffer() > 512000)
/*  57 */         this.indexFile.flush();
/*  58 */       if (this.matrixFile.bytesInBuffer() > 512000)
/*  59 */         this.matrixFile.flush();
/*  60 */       this.maxRowIndex = rowIndex;
/*  61 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/*  64 */       e.printStackTrace();
/*  65 */     }return false;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  70 */     if (this.matrixFile != null) {
/*  71 */       this.matrixFile.close();
/*  72 */       editHeader(this.seqMatrixFile);
/*     */     }
/*  74 */     if (this.indexFile != null) {
/*  75 */       this.indexFile.close();
/*  76 */       editHeader(this.seqIndexFile);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void init()
/*     */   {
/*     */     try
/*     */     {
/*  85 */       File file = new File(this.seqMatrixFile);
/*  86 */       if ((!file.exists()) || (file.length() == 0L)) {
/*  87 */         this.maxRowIndex = -1;
/*  88 */         this.maxTermIndex = -1;
/*  89 */         this.termCount = 0;
/*     */       }
/*     */       else {
/*  92 */         FastBinaryReader fbr = new FastBinaryReader(file);
/*  93 */         this.maxRowIndex = (fbr.readInt() - 1);
/*  94 */         this.maxTermIndex = (fbr.readInt() - 1);
/*  95 */         this.termCount = fbr.readInt();
/*  96 */         fbr.close();
/*     */       }
/*     */ 
/*  99 */       this.matrixFile = new FastBinaryWriter(this.seqMatrixFile, true);
/* 100 */       if (this.matrixFile.getFilePointer() == 0L) {
/* 101 */         this.matrixFile.writeInt(0);
/* 102 */         this.matrixFile.writeInt(0);
/* 103 */         this.matrixFile.writeInt(0);
/*     */       }
/*     */ 
/* 106 */       this.indexFile = new FastBinaryWriter(this.seqIndexFile, true);
/* 107 */       if (this.indexFile.getFilePointer() == 0L) {
/* 108 */         this.indexFile.writeInt(0);
/* 109 */         this.indexFile.writeInt(0);
/* 110 */         this.indexFile.writeInt(0);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 114 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void editHeader(String file)
/*     */   {
/*     */     try
/*     */     {
/* 122 */       RandomAccessFile raf = new RandomAccessFile(file, "rw");
/* 123 */       raf.writeInt(this.maxRowIndex + 1);
/* 124 */       raf.writeInt(this.maxTermIndex + 1);
/* 125 */       raf.writeInt(this.termCount);
/* 126 */       raf.close();
/*     */     }
/*     */     catch (Exception e) {
/* 129 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.SequenceFileWriter
 * JD-Core Version:    0.6.2
 */