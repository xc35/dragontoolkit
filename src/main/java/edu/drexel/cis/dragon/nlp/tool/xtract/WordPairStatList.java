/*     */ package edu.drexel.cis.dragon.nlp.tool.xtract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.util.ByteArrayConvert;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class WordPairStatList
/*     */ {
/*     */   private RandomAccessFile raf;
/*     */   private SortedArray indexList;
/*     */   private int maxSpan;
/*     */   private int elementLength;
/*     */   private boolean writingMode;
/*     */   private String indexlistFilename;
/*     */   private int pairNum;
/*     */   private int maxOldIndex;
/*     */   private int maxIndex;
/*     */   private int maxCacheSize;
/*     */   private byte[] buf;
/*     */ 
/*     */   public WordPairStatList(String filename, int maxSpan, boolean writingMode)
/*     */   {
/*     */     try
/*     */     {
/*  29 */       this.maxSpan = maxSpan;
/*  30 */       this.elementLength = (4 * (2 * maxSpan + 3));
/*  31 */       this.buf = new byte[this.elementLength];
/*  32 */       this.writingMode = writingMode;
/*  33 */       this.indexlistFilename = filename;
/*  34 */       this.maxCacheSize = 200000;
/*  35 */       if (writingMode) {
/*  36 */         this.raf = new RandomAccessFile(filename, "rw");
/*  37 */         if (this.raf.length() < 4L) {
/*  38 */           this.raf.writeInt(0);
/*  39 */           this.maxOldIndex = -1;
/*     */         }
/*     */         else {
/*  42 */           this.maxOldIndex = (this.raf.readInt() - 1);
/*  43 */         }this.maxIndex = this.maxOldIndex;
/*  44 */         this.raf.close();
/*  45 */         this.raf = null;
/*  46 */         this.indexList = new SortedArray(new IndexComparator());
/*     */       }
/*     */       else {
/*  49 */         this.raf = new RandomAccessFile(filename, "r");
/*  50 */         if (this.raf.length() > 0L)
/*  51 */           this.pairNum = this.raf.readInt();
/*     */         else
/*  53 */           this.pairNum = 0;
/*  54 */         this.maxIndex = (this.pairNum - 1);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  58 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setCacheSize(int size) {
/*  63 */     this.maxCacheSize = size;
/*     */   }
/*     */ 
/*     */   public int getCacheSize() {
/*  67 */     return this.maxCacheSize;
/*     */   }
/*     */ 
/*     */   public WordPairStat get(int index) {
/*     */     try {
/*  72 */       if ((this.writingMode) || (index >= this.pairNum)) {
/*  73 */         return null;
/*     */       }
/*  75 */       this.raf.seek(index * this.elementLength + 4);
/*  76 */       this.raf.read(this.buf);
/*  77 */       return getWordPairStatFromByteArray(this.buf);
/*     */     }
/*     */     catch (Exception e) {
/*  80 */       e.printStackTrace();
/*  81 */     }return null;
/*     */   }
/*     */ 
/*     */   public boolean add(WordPairStat stat)
/*     */   {
/*  89 */     if (!this.writingMode) {
/*  90 */       return false;
/*     */     }
/*  92 */     if (!this.indexList.add(stat)) {
/*  93 */       WordPairStat oldStat = (WordPairStat)this.indexList.get(this.indexList.insertedPos());
/*  94 */       for (int i = 1; i <= this.maxSpan; i++)
/*  95 */         oldStat.addFrequency(i, stat.getFrequency(i));
/*  96 */       for (int i = 1; i <= this.maxSpan; i++)
/*  97 */         oldStat.addFrequency(-i, stat.getFrequency(-i));
/*     */     }
/*     */     else {
/* 100 */       if (stat.getIndex() > this.maxIndex)
/* 101 */         this.maxIndex = stat.getIndex();
/* 102 */       if (this.indexList.size() > this.maxCacheSize)
/* 103 */         saveIndexList(this.indexlistFilename, this.indexList);
/*     */     }
/* 105 */     return true;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 109 */     return this.maxIndex + 1;
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */     try {
/* 114 */       if (this.writingMode) {
/* 115 */         saveIndexList(this.indexlistFilename, this.indexList);
/* 116 */         this.indexList.clear();
/*     */       }
/*     */       else {
/* 119 */         this.raf.close();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 123 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void saveIndexList(String filename, SortedArray list)
/*     */   {
/*     */     try
/*     */     {
/* 134 */       if ((list == null) || (list.size() == 0))
/* 135 */         return;
/* 136 */       System.out.println(new Date() + " Saving Word Pair List...");
/*     */           int i;
/* 139 */       FastBinaryReader fbr = new FastBinaryReader(filename);
/* 140 */       fbr.skip(4L);
/* 141 */       int lastIndex = 0;
/* 142 */       for ( i = 0; i < list.size(); i++) {
/* 143 */         WordPairStat cur = (WordPairStat)list.get(i);
/* 144 */         if (cur.getIndex() > this.maxOldIndex)
/*     */           break;
/* 146 */         fbr.skip((cur.getIndex() - lastIndex) * this.elementLength + 12);
/* 147 */         for (int j = 0; j < this.maxSpan; j++)
/* 148 */           cur.addFrequency(j - this.maxSpan, fbr.readInt());
/* 149 */         for (int j = 1; j <= this.maxSpan; j++)
/* 150 */           cur.addFrequency(j, fbr.readInt());
/* 151 */         lastIndex = cur.getIndex() + 1;
/*     */       }
/* 153 */       int cutoff = i - 1;
/* 154 */       fbr.close();
/*     */ 
/* 157 */       RandomAccessFile rafPair = new RandomAccessFile(filename, "rw");
/* 158 */       rafPair.writeInt(this.maxIndex + 1);
/* 159 */       for ( i = 0; i <= cutoff; i++) {
/* 160 */         WordPairStat cur = (WordPairStat)list.get(i);
/* 161 */         rafPair.seek(cur.getIndex() * this.elementLength + 4);
/* 162 */         writeToByteArray(cur, this.buf);
/* 163 */         rafPair.write(this.buf);
/*     */       }
/*     */ 
/* 167 */       lastIndex = this.maxOldIndex;
/* 168 */       rafPair.seek((this.maxOldIndex + 1) * this.elementLength + 4);
/* 169 */       for (i = cutoff + 1; i < list.size(); i++) {
/* 170 */         WordPairStat cur = (WordPairStat)list.get(i);
/* 171 */         for (int j = lastIndex + 1; j < cur.getIndex(); j++) {
/* 172 */           writeToByteArray(j, this.buf);
/* 173 */           rafPair.write(this.buf);
/*     */         }
/* 175 */         writeToByteArray(cur, this.buf);
/* 176 */         rafPair.write(this.buf);
/* 177 */         lastIndex = cur.getIndex();
/*     */       }
/* 179 */       rafPair.close();
/* 180 */       this.maxOldIndex = this.maxIndex;
/* 181 */       list.clear();
/*     */     }
/*     */     catch (Exception ex) {
/* 184 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeToByteArray(WordPairStat cur, byte[] array)
/*     */   {
/* 191 */     ByteArrayConvert.toByte(cur.getIndex(), array, 0);
/* 192 */     ByteArrayConvert.toByte(cur.getFirstWord(), array, 4);
/* 193 */     ByteArrayConvert.toByte(cur.getSecondWord(), array, 8);
/* 194 */     for (int i = 0; i < this.maxSpan; i++)
/* 195 */       ByteArrayConvert.toByte(cur.getFrequency(i - this.maxSpan), array, i * 4 + 12);
/* 196 */     for (int i = 0; i < this.maxSpan; i++)
/* 197 */       ByteArrayConvert.toByte(cur.getFrequency(i + 1), array, (i + this.maxSpan) * 4 + 12);
/*     */   }
/*     */ 
/*     */   private void writeToByteArray(int index, byte[] array)
/*     */   {
/* 203 */     ByteArrayConvert.toByte(index, array, 0);
/* 204 */     ByteArrayConvert.toByte(-1, array, 4);
/* 205 */     ByteArrayConvert.toByte(-1, array, 8);
/* 206 */     for (int i = 0; i < 2 * this.maxSpan; i++)
/* 207 */       ByteArrayConvert.toByte(0, array, i * 4 + 12);
/*     */   }
/*     */ 
/*     */   private WordPairStat getWordPairStatFromByteArray(byte[] array)
/*     */   {
/* 214 */     WordPairStat cur = new WordPairStat(ByteArrayConvert.toInt(array, 0), ByteArrayConvert.toInt(array, 4), ByteArrayConvert.toInt(array, 8), this.maxSpan);
/* 215 */     for (int i = 0; i < this.maxSpan; i++)
/* 216 */       cur.addFrequency(i - this.maxSpan, ByteArrayConvert.toInt(array, 4 * i + 12));
/* 217 */     for (int i = 0; i < this.maxSpan; i++)
/* 218 */       cur.addFrequency(i + 1, ByteArrayConvert.toInt(array, 4 * (i + this.maxSpan) + 12));
/* 219 */     return cur;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.xtract.WordPairStatList
 * JD-Core Version:    0.6.2
 */