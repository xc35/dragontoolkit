/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.IntFlatSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntGiantSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import edu.drexel.cis.dragon.util.ByteArrayConvert;
/*     */ import edu.drexel.cis.dragon.util.ByteArrayWriter;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryWriter;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class TransposeIRMatrix
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  25 */     TransposeIRMatrix transpose = new TransposeIRMatrix();
/*  26 */     StringBuffer directory = new StringBuffer();
/*  27 */     for (int i = 0; i < args.length; i++) {
/*  28 */       directory.append(args[i]);
/*  29 */       directory.append(' ');
/*     */     }
/*  31 */     transpose.genTermDocMatrix(directory.toString().trim());
/*     */   }
/*     */ 
/*     */   public void genTermDocMatrix(String directory)
/*     */   {
/*     */     try
/*     */     {
/*  44 */       FileIndex indexer = new FileIndex(directory, false);
/*     */ 
/*  46 */       File oldMatrix = new File(indexer.getTermDocFilename() + ".tmp");
/*  47 */       File newMatrix = new File(indexer.getTermDocFilename());
/*  48 */       if ((newMatrix.exists()) && 
/*  49 */         (!newMatrix.renameTo(oldMatrix))) {
/*  50 */         return;
/*     */       }
/*  52 */       File oldIndex = new File(indexer.getTermDocIndexFilename() + ".tmp");
/*  53 */       File newIndex = new File(indexer.getTermDocIndexFilename());
/*  54 */       if ((newIndex.exists()) && 
/*  55 */         (!newIndex.renameTo(oldIndex))) {
/*  56 */         return;
/*     */       }
/*     */ 
/*  60 */       int oldDocNum = 0;
/*  61 */       long[] arrOffset = initTermDocMatrix(indexer);
/*  62 */       if (oldMatrix.exists())
/*  63 */         oldDocNum = mergeOldMatrix(arrOffset, newMatrix, oldMatrix);
/*  64 */       RandomAccessFile raf = new RandomAccessFile(indexer.getTermDocFilename(), "rw");
/*  65 */       IntGiantSparseMatrix doctermMatrix = new IntGiantSparseMatrix(indexer.getDocTermIndexFilename(), indexer.getDocTermFilename());
/*  66 */       genTransposeMatrix(raf, oldDocNum, arrOffset, doctermMatrix);
/*     */ 
/*  68 */       if (oldMatrix.exists())
/*  69 */         oldMatrix.delete();
/*  70 */       if (oldIndex.exists())
/*  71 */         oldIndex.delete();
/*     */     }
/*     */     catch (Exception e) {
/*  74 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void genRelationDocMatrix(String directory)
/*     */   {
/*     */     try
/*     */     {
/*  87 */       FileIndex indexer = new FileIndex(directory, false);
/*     */ 
/*  89 */       File oldMatrix = new File(indexer.getRelationDocFilename() + ".tmp");
/*  90 */       File newMatrix = new File(indexer.getRelationDocFilename());
/*  91 */       if ((newMatrix.exists()) && 
/*  92 */         (!newMatrix.renameTo(oldMatrix))) {
/*  93 */         return;
/*     */       }
/*  95 */       File oldIndex = new File(indexer.getRelationDocIndexFilename() + ".tmp");
/*  96 */       File newIndex = new File(indexer.getRelationDocIndexFilename());
/*  97 */       if ((newIndex.exists()) && 
/*  98 */         (!newIndex.renameTo(oldIndex))) {
/*  99 */         return;
/*     */       }
/*     */ 
/* 102 */       int oldDocNum = 0;
/* 103 */       long[] arrOffset = initRelationDocMatrix(indexer);
/* 104 */       if (oldMatrix.exists()) {
/* 105 */         oldDocNum = mergeOldMatrix(arrOffset, newMatrix, oldMatrix);
/*     */       }
/* 107 */       RandomAccessFile raf = new RandomAccessFile(indexer.getRelationDocFilename(), "rw");
/* 108 */       IntGiantSparseMatrix docrelationMatrix = new IntGiantSparseMatrix(indexer.getDocRelationIndexFilename(), indexer.getDocRelationFilename());
/* 109 */       genTransposeMatrix(raf, oldDocNum, arrOffset, docrelationMatrix);
/*     */ 
/* 111 */       if (oldMatrix.exists())
/* 112 */         oldMatrix.delete();
/* 113 */       if (oldIndex.exists())
/* 114 */         oldIndex.delete();
/*     */     }
/*     */     catch (Exception e) {
/* 117 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private long[] initTermDocMatrix(FileIndex indexer)
/*     */   {
/*     */     try
/*     */     {
/* 130 */       byte[] buf = new byte[8];
/* 131 */       int i = 0;
/* 132 */       ByteArrayConvert.toByte(i, buf, 0);
/* 133 */       ByteArrayConvert.toByte(i, buf, 4);
/*     */ 
/* 135 */       IRCollection collection = new IRCollection();
/* 136 */       IRTermIndexList indexList = new BasicIRTermIndexList(indexer.getTermIndexListFilename(), false);
/* 137 */       collection.load(indexer.getCollectionFilename());
/* 138 */       long[] arrOffset = new long[collection.getTermNum()];
/*     */ 
/* 140 */       System.out.println(new Date().toString() + " initializing term doc matrix...");
/* 141 */       int totalCell = readCellNum(indexer.getDocTermFilename());
/* 142 */       FastBinaryWriter fbwMatrix = new FastBinaryWriter(indexer.getTermDocFilename());
/* 143 */       FastBinaryWriter fbwIndex = new FastBinaryWriter(indexer.getTermDocIndexFilename());
/* 144 */       fbwMatrix.writeInt(collection.getTermNum());
/* 145 */       fbwMatrix.writeInt(collection.getDocNum());
/* 146 */       fbwMatrix.writeInt(totalCell);
/* 147 */       fbwIndex.writeInt(collection.getTermNum());
/* 148 */       fbwIndex.writeInt(collection.getDocNum());
/* 149 */       fbwIndex.writeInt(totalCell);
/* 150 */       for (i = 0; i < indexList.size(); i++) {
/* 151 */         int len = indexList.get(i).getDocFrequency();
/* 152 */         fbwIndex.writeInt(i);
/* 153 */         fbwIndex.writeLong(fbwMatrix.getFilePointer());
/* 154 */         fbwIndex.writeInt(len);
/* 155 */         fbwIndex.flush();
/* 156 */         fbwMatrix.writeInt(i);
/* 157 */         fbwMatrix.writeInt(len);
/* 158 */         arrOffset[i] = fbwMatrix.getFilePointer();
/* 159 */         for (int j = 0; j < len; j++)
/* 160 */           fbwMatrix.write(buf);
/*     */       }
/* 162 */       fbwIndex.close();
/* 163 */       fbwMatrix.close();
/*     */ 
/* 165 */       return arrOffset;
/*     */     }
/*     */     catch (Exception e) {
/* 168 */       e.printStackTrace();
/* 169 */     }return null;
/*     */   }
/*     */ 
/*     */   private long[] initRelationDocMatrix(FileIndex indexer)
/*     */   {
/*     */     try
/*     */     {
/* 183 */       byte[] buf = new byte[8];
/* 184 */       int i = 0;
/* 185 */       ByteArrayConvert.toByte(i, buf, 0);
/* 186 */       ByteArrayConvert.toByte(i, buf, 4);
/*     */ 
/* 188 */       IRCollection collection = new IRCollection();
/* 189 */       IRRelationIndexList indexList = new BasicIRRelationIndexList(indexer.getRelationIndexListFilename(), false);
/* 190 */       collection.load(indexer.getCollectionFilename());
/* 191 */       long[] arrOffset = new long[collection.getRelationNum()];
/*     */ 
/* 193 */       System.out.println(new Date().toString() + " initializing relation doc matrix...");
/* 194 */       int totalCell = readCellNum(indexer.getDocRelationFilename());
/* 195 */       FastBinaryWriter fbwMatrix = new FastBinaryWriter(indexer.getRelationDocFilename());
/* 196 */       FastBinaryWriter fbwIndex = new FastBinaryWriter(indexer.getRelationDocIndexFilename());
/* 197 */       fbwMatrix.writeInt(collection.getRelationNum());
/* 198 */       fbwMatrix.writeInt(collection.getDocNum());
/* 199 */       fbwMatrix.writeInt(totalCell);
/* 200 */       fbwIndex.writeInt(collection.getRelationNum());
/* 201 */       fbwIndex.writeInt(collection.getDocNum());
/* 202 */       fbwIndex.writeInt(totalCell);
/* 203 */       for (i = 0; i < indexList.size(); i++) {
/* 204 */         int len = indexList.get(i).getDocFrequency();
/* 205 */         fbwIndex.writeInt(i);
/* 206 */         fbwIndex.writeLong(fbwMatrix.getFilePointer());
/* 207 */         fbwIndex.writeInt(len);
/* 208 */         fbwIndex.flush();
/* 209 */         fbwMatrix.writeInt(i);
/* 210 */         fbwMatrix.writeInt(len);
/* 211 */         arrOffset[i] = fbwMatrix.getFilePointer();
/* 212 */         for (int j = 0; j < len; j++)
/* 213 */           fbwMatrix.write(buf);
/*     */       }
/* 215 */       fbwIndex.close();
/* 216 */       fbwMatrix.close();
/*     */ 
/* 218 */       return arrOffset;
/*     */     }
/*     */     catch (Exception e) {
/* 221 */       e.printStackTrace();
/* 222 */     }return null;
/*     */   }
/*     */ 
/*     */   private void genTransposeMatrix(RandomAccessFile newMatrix, int startingRow, long[] arrOffset, IntSparseMatrix oldMatrix)
/*     */   {
/*     */     try
/*     */     { int i;
/* 232 */       int doc_cache = 40000;
/* 233 */       int count = 0;
/* 234 */       IntFlatSparseMatrix cacheMatrix = new IntFlatSparseMatrix(false, true);
/*     */ 
/* 236 */       for ( i = startingRow; i < oldMatrix.rows(); i++) {
/* 237 */         count++;
/* 238 */         int[] arrColumn = oldMatrix.getNonZeroColumnsInRow(i);
/* 239 */         int[] arrFreq = oldMatrix.getNonZeroIntScoresInRow(i);
/* 240 */         for (int j = 0; j < arrColumn.length; j++) {
/* 241 */           cacheMatrix.add(arrColumn[j], i, arrFreq[j]);
/*     */         }
/* 243 */         if (count >= doc_cache) {
/* 244 */           System.out.println(new Date().toString() + " processing row #" + i);
/* 245 */           count = 0;
/* 246 */           cacheMatrix.finalizeData();
/* 247 */           add(newMatrix, arrOffset, cacheMatrix);
/* 248 */           cacheMatrix.close();
/*     */         }
/*     */       }
/* 251 */       if (count > 0) {
/* 252 */         System.out.println(new Date().toString() + " processing row #" + i);
/* 253 */         count = 0;
/* 254 */         cacheMatrix.finalizeData();
/* 255 */         add(newMatrix, arrOffset, cacheMatrix);
/* 256 */         cacheMatrix.close();
/*     */       }
/*     */ 
/* 259 */       newMatrix.close();
/* 260 */       oldMatrix.close();
/* 261 */       System.out.println("Done!");
/*     */     }
/*     */     catch (Exception e) {
/* 264 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void add(RandomAccessFile newMatrix, long[] arrOffset, IntFlatSparseMatrix cacheMatrix)
/*     */   {
/*     */     try
/*     */     {
/* 273 */       System.out.println(new Date().toString() + " dumping to disk");
/* 274 */       ByteArrayWriter baw = new ByteArrayWriter();
/* 275 */       int start = cacheMatrix.getBaseRow();
/* 276 */       int end = cacheMatrix.rows();
/* 277 */       for (int i = start; i < end; i++) {
/* 278 */         int len = cacheMatrix.getNonZeroNumInRow(i);
/* 279 */         if (len != 0) {
/* 280 */           newMatrix.seek(arrOffset[i]);
/* 281 */           for (int j = 0; j < len; j++) {
/* 282 */             baw.writeInt(cacheMatrix.getNonZeroColumnInRow(i, j));
/* 283 */             baw.writeInt(cacheMatrix.getNonZeroIntScoreInRow(i, j));
/*     */           }
/* 285 */           newMatrix.write(baw.toByteArray());
/* 286 */           baw.reset();
/* 287 */           arrOffset[i] += 8 * len;
/*     */         }
/*     */       }
/* 289 */       baw.close();
/*     */     }
/*     */     catch (Exception e) {
/* 292 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private int readCellNum(String filename)
/*     */   {
/*     */     try
/*     */     {
/* 301 */       FastBinaryReader reader = new FastBinaryReader(filename);
/* 302 */       reader.skip(8L);
/* 303 */       int cellNum = reader.readInt();
/* 304 */       reader.close();
/* 305 */       return cellNum;
/*     */     }
/*     */     catch (Exception e) {
/* 308 */       e.printStackTrace();
/* 309 */     }return 0;
/*     */   }
/*     */ 
/*     */   private int mergeOldMatrix(long[] arrOffset, File newMatrix, File oldMatrix)
/*     */   {
/*     */     try
/*     */     {
/* 319 */       System.out.println(new Date().toString() + " merging old term doc matrix...");
/* 320 */       FastBinaryReader fbr = new FastBinaryReader(oldMatrix);
/* 321 */       RandomAccessFile raf = new RandomAccessFile(newMatrix, "rw");
/* 322 */       int rows = fbr.readInt();
/* 323 */       int cols = fbr.readInt();
/* 324 */       fbr.skip(4L);
/* 325 */       for (int i = 0; i < rows; i++) {
/* 326 */         fbr.skip(4L);
/* 327 */         int len = fbr.readInt();
/* 328 */         if (len > 0) {
/* 329 */           raf.seek(arrOffset[i]);
/* 330 */           move(fbr, raf, len * 8);
/* 331 */           arrOffset[i] += len * 8;
/*     */         }
/*     */       }
/* 334 */       fbr.close();
/* 335 */       raf.close();
/* 336 */       return cols;
/*     */     }
/*     */     catch (Exception e) {
/* 339 */       e.printStackTrace();
/* 340 */     }return 0;
/*     */   }
/*     */ 
/*     */   private void move(FastBinaryReader src, RandomAccessFile dest, long length)
/*     */   {
/*     */     try
/*     */     {
/* 349 */       byte[] buf = new byte[(int)(10240L < length ? 10240L : length)];
/* 350 */       while (length > 0L) {
/* 351 */         int count = (int)(length > buf.length ? buf.length : length);
/* 352 */         count = src.read(buf, 0, count);
/* 353 */         if (count <= 0) break;
/* 354 */         dest.write(buf, 0, count);
/* 355 */         length -= count;
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 363 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.TransposeIRMatrix
 * JD-Core Version:    0.6.2
 */