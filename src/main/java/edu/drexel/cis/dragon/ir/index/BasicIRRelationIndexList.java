/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.util.ByteArrayConvert;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class BasicIRRelationIndexList
/*     */   implements IRRelationIndexList, IRSignatureIndexList
/*     */ {
/*     */   private RandomAccessFile raf;
/*     */   private SortedArray indexList;
/*     */   private int elementLength;
/*     */   private boolean writingMode;
/*     */   private String indexlistFilename;
/*     */   private int relationNum;
/*     */   private int maxOldIndex;
/*     */   private int maxIndex;
/*     */   private int maxCacheSize;
/*     */   private byte[] buf;
/*     */ 
/*     */   public BasicIRRelationIndexList(String filename, boolean writingMode)
/*     */   {
/*     */     try
/*     */     {
/*  29 */       this.elementLength = 20;
/*  30 */       this.buf = new byte[this.elementLength];
/*  31 */       this.writingMode = writingMode;
/*  32 */       this.indexlistFilename = filename;
/*  33 */       this.maxCacheSize = 200000;
/*  34 */       if (writingMode) {
/*  35 */         this.raf = new RandomAccessFile(filename, "rw");
/*  36 */         if (this.raf.length() < 4L) {
/*  37 */           this.raf.writeInt(0);
/*  38 */           this.maxOldIndex = -1;
/*     */         }
/*     */         else {
/*  41 */           this.maxOldIndex = (this.raf.readInt() - 1);
/*  42 */         }this.maxIndex = this.maxOldIndex;
/*  43 */         this.raf.close();
/*  44 */         this.raf = null;
/*  45 */         this.indexList = new SortedArray(new IndexComparator());
/*     */       }
/*     */       else {
/*  48 */         if (FileUtil.exist(filename)) {
/*  49 */           this.raf = new RandomAccessFile(filename, "r");
/*  50 */           if (this.raf.length() > 0L)
/*  51 */             this.relationNum = this.raf.readInt();
/*     */           else
/*  53 */             this.relationNum = 0;
/*     */         }
/*     */         else {
/*  56 */           this.relationNum = 0;
/*  57 */         }this.maxIndex = (this.relationNum - 1);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  61 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setCacheSize(int size) {
/*  66 */     this.maxCacheSize = size;
/*     */   }
/*     */ 
/*     */   public int getCacheSize() {
/*  70 */     return this.maxCacheSize;
/*     */   }
/*     */ 
/*     */   public IRSignature getIRSignature(int index) {
/*  74 */     return get(index);
/*     */   }
/*     */ 
/*     */   public IRRelation get(int index) {
/*     */     try {
/*  79 */       if ((this.writingMode) || (index >= this.relationNum)) {
/*  80 */         return null;
/*     */       }
/*  82 */       this.raf.seek(index * this.elementLength + 4);
/*  83 */       this.raf.read(this.buf);
/*  84 */       return getIRRelationFromByteArray(this.buf);
/*     */     }
/*     */     catch (Exception e) {
/*  87 */       e.printStackTrace();
/*  88 */     }return null;
/*     */   }
/*     */ 
/*     */   public boolean add(IRRelation curRelation)
/*     */   {
/*  95 */     if (!this.writingMode) {
/*  96 */       return false;
/*     */     }
/*  98 */     if (!this.indexList.add(curRelation.copy())) {
/*  99 */       IRRelation oldRelation = (IRRelation)this.indexList.get(this.indexList.insertedPos());
/* 100 */       oldRelation.addFrequency(curRelation.getFrequency());
/* 101 */       oldRelation.setDocFrequency(oldRelation.getDocFrequency() + curRelation.getDocFrequency());
/*     */     }
/*     */     else {
/* 104 */       if (curRelation.getIndex() > this.maxIndex)
/* 105 */         this.maxIndex = curRelation.getIndex();
/* 106 */       if (this.indexList.size() > this.maxCacheSize)
/* 107 */         saveRelationIndexList(this.indexlistFilename, this.indexList);
/*     */     }
/* 109 */     return true;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 113 */     return this.maxIndex + 1;
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */     try {
/* 118 */       if (this.writingMode) {
/* 119 */         saveRelationIndexList(this.indexlistFilename, this.indexList);
/* 120 */         this.indexList.clear();
/*     */       }
/* 123 */       else if (this.raf != null) {
/* 124 */         this.raf.close();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 128 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void saveRelationIndexList(String filename, SortedArray list)
/*     */   {
/*     */     try
/*     */     {
					  int i;
/* 139 */       if ((list == null) || (list.size() == 0))
/* 140 */         return;
/* 141 */       System.out.println(new Date() + " Saving Relation Index List...");
/*     */ 
/* 144 */       FastBinaryReader fbr = new FastBinaryReader(filename);
/* 145 */       fbr.skip(4L);
/* 146 */       int lastIndex = 0;
/* 147 */       for ( i = 0; i < list.size(); i++) {
/* 148 */         IRRelation cur = (IRRelation)list.get(i);
/* 149 */         if (cur.getIndex() > this.maxOldIndex)
/*     */           break;
/* 151 */         fbr.skip((cur.getIndex() - lastIndex) * this.elementLength + 12);
/* 152 */         cur.addFrequency(fbr.readInt());
/* 153 */         cur.setDocFrequency(cur.getDocFrequency() + fbr.readInt());
/* 154 */         lastIndex = cur.getIndex() + 1;
/*     */       }
/* 156 */       int cutoff = i - 1;
/* 157 */       fbr.close();
/*     */ 
/* 160 */       RandomAccessFile rafRelation = new RandomAccessFile(filename, "rw");
/* 161 */       rafRelation.writeInt(this.maxIndex + 1);
/* 162 */       for ( i = 0; i <= cutoff; i++) {
/* 163 */         IRRelation cur = (IRRelation)list.get(i);
/* 164 */         rafRelation.seek(cur.getIndex() * this.elementLength + 4);
/* 165 */         writeToByteArray(cur, this.buf);
/* 166 */         rafRelation.write(this.buf);
/*     */       }
/*     */ 
/* 170 */       lastIndex = this.maxOldIndex;
/* 171 */       rafRelation.seek((this.maxOldIndex + 1) * this.elementLength + 4);
/* 172 */       for ( i = cutoff + 1; i < list.size(); i++) {
/* 173 */         IRRelation cur = (IRRelation)list.get(i);
/* 174 */         for (int j = lastIndex + 1; j < cur.getIndex(); j++) {
/* 175 */           writeToByteArray(j, this.buf);
/* 176 */           rafRelation.write(this.buf);
/*     */         }
/* 178 */         writeToByteArray(cur, this.buf);
/* 179 */         rafRelation.write(this.buf);
/* 180 */         lastIndex = cur.getIndex();
/*     */       }
/* 182 */       rafRelation.close();
/* 183 */       this.maxOldIndex = this.maxIndex;
/* 184 */       list.clear();
/*     */     }
/*     */     catch (Exception ex) {
/* 187 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeToByteArray(IRRelation cur, byte[] array) {
/* 192 */     ByteArrayConvert.toByte(cur.getIndex(), array, 0);
/* 193 */     ByteArrayConvert.toByte(cur.getFirstTerm(), array, 4);
/* 194 */     ByteArrayConvert.toByte(cur.getSecondTerm(), array, 8);
/* 195 */     ByteArrayConvert.toByte(cur.getFrequency(), array, 12);
/* 196 */     ByteArrayConvert.toByte(cur.getDocFrequency(), array, 16);
/*     */   }
/*     */ 
/*     */   private void writeToByteArray(int index, byte[] array) {
/* 200 */     ByteArrayConvert.toByte(index, array, 0);
/* 201 */     ByteArrayConvert.toByte(-1, array, 4);
/* 202 */     ByteArrayConvert.toByte(-1, array, 8);
/* 203 */     ByteArrayConvert.toByte(0, array, 12);
/* 204 */     ByteArrayConvert.toByte(0, array, 16);
/*     */   }
/*     */ 
/*     */   private IRRelation getIRRelationFromByteArray(byte[] array)
/*     */   {
/* 210 */     int relationIndex = ByteArrayConvert.toInt(array, 0);
/* 211 */     int first = ByteArrayConvert.toInt(array, 4);
/* 212 */     int second = ByteArrayConvert.toInt(array, 8);
/* 213 */     int frequency = ByteArrayConvert.toInt(array, 12);
/* 214 */     int docFrequency = ByteArrayConvert.toInt(array, 16);
/* 215 */     return new IRRelation(relationIndex, first, second, frequency, docFrequency);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.BasicIRRelationIndexList
 * JD-Core Version:    0.6.2
 */