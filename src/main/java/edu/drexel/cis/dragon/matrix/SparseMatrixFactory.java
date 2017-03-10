/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryWriter;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class SparseMatrixFactory
/*     */ {
/*     */   private String matrixFilename;
/*     */   private int rows;
/*     */   private int columns;
/*     */   private int cells;
/*     */   private int cellDataLength;
/*     */ 
/*     */   public SparseMatrixFactory(String matrixFilename, int cellDataLength)
/*     */   {
/*  22 */     this.matrixFilename = matrixFilename;
/*  23 */     this.cellDataLength = cellDataLength;
/*  24 */     readStatInfo(matrixFilename);
/*     */   }
/*     */ 
/*     */   public String getMatrixFilename() {
/*  28 */     return this.matrixFilename;
/*     */   }
/*     */ 
/*     */   public int rows() {
/*  32 */     return this.rows;
/*     */   }
/*     */ 
/*     */   public int columns() {
/*  36 */     return this.columns;
/*     */   }
/*     */ 
/*     */   public int getCellDataLength() {
/*  40 */     return this.cellDataLength;
/*     */   }
/*     */ 
/*     */   public int getNonZeroNum() {
/*  44 */     return this.cells;
/*     */   }
/*     */ 
/*     */   public boolean add(SparseMatrix newMatrix)
/*     */   {
/*     */     try
/*     */     {
/*  62 */       if ((newMatrix == null) || (newMatrix.getNonZeroNum() == 0)) {
/*  63 */         return false;
/*     */       }
/*  65 */       File matrixf = new File(this.matrixFilename);
/*  66 */       if (!matrixf.exists()) {
/*  67 */         return saveSparseMatrix(newMatrix);
/*     */       }
/*     */ 
/*  70 */       if (newMatrix.getBaseRow() >= this.rows) {
/*  71 */         return append(newMatrix);
/*     */       }
/*     */ 
/*  74 */       System.out.println(new Date() + " Adding to old matrix...");
/*  75 */       ArrayList mergedCellList = new ArrayList();
/*  76 */       byte[] cellData = new byte[this.cellDataLength];
/*  77 */       int mergedCellNum = 0;
/*  78 */       String tempMatrixFilename = this.matrixFilename + ".tmp";
/*  79 */       FastBinaryReader fbMatrixOld = new FastBinaryReader(matrixf);
/*  80 */       FastBinaryWriter fbMatrixNew = new FastBinaryWriter(tempMatrixFilename);
/*     */ 
/*  83 */       fbMatrixOld.skip(12L);
/*  84 */       fbMatrixNew.writeInt(this.rows > newMatrix.rows() ? this.rows : newMatrix.rows());
/*  85 */       fbMatrixNew.writeInt(this.columns > newMatrix.columns() ? this.columns : newMatrix.columns());
/*  86 */       fbMatrixNew.writeInt(this.cells + newMatrix.getNonZeroNum());
/*     */ 
/*  89 */       RandomAccessFile rafMatrixOld = new RandomAccessFile(matrixf, "r");
/*  90 */       long priorBaseRowData = getSuperMatrixRowStart(rafMatrixOld, newMatrix.getBaseRow()) - 12L;
/*  91 */       if (priorBaseRowData > 0L)
/*  92 */         fbMatrixNew.write(fbMatrixOld, priorBaseRowData);
/*  93 */       rafMatrixOld.seek(priorBaseRowData + 12L + 8L);
/*     */ 
/*  96 */       int minRows = newMatrix.rows() < this.rows ? newMatrix.rows() : this.rows;
/*  97 */       for (int i = newMatrix.getBaseRow(); i < minRows; i++) {
/*  98 */         fbMatrixOld.readInt();
/*  99 */         int oldNum = fbMatrixOld.readInt();
/* 100 */         int newNum = newMatrix.getNonZeroNumInRow(i);
/*     */         boolean rowAppendMode;
/* 103 */         if (newNum == 0) {
/* 104 */            rowAppendMode = true;
/* 105 */           rafMatrixOld.skipBytes(oldNum * (this.cellDataLength + 4) + 8);
/*     */         }
/* 107 */         else if (oldNum == 0) {
/* 108 */            rowAppendMode = true;
/* 109 */           rafMatrixOld.skipBytes(8);
/*     */         }
/*     */         else {
/* 112 */           rafMatrixOld.skipBytes((oldNum - 1) * (this.cellDataLength + 4));
/* 113 */           int lastColumnInRow = rafMatrixOld.readInt();
/* 114 */           rafMatrixOld.skipBytes(this.cellDataLength + 8);
/* 115 */           if (lastColumnInRow < newMatrix.getNonZeroColumnInRow(i, 0))
/* 116 */             rowAppendMode = true;
/*     */           else {
/* 118 */             rowAppendMode = false;
/*     */           }
/*     */         }
/* 121 */         if (rowAppendMode)
/*     */         {
/* 123 */           fbMatrixNew.writeInt(i);
/* 124 */           fbMatrixNew.writeInt(newNum + oldNum);
/* 125 */           fbMatrixNew.write(fbMatrixOld, oldNum * (newMatrix.getCellDataLength() + 4));
/*     */ 
/* 128 */           for (int j = 0; j < newNum; j++) {
/* 129 */             fbMatrixNew.writeInt(newMatrix.getNonZeroColumnInRow(i, j));
/* 130 */             fbMatrixNew.write(newMatrix.getNonZeroCellInRow(i, j).toByteArray());
/*     */           }
/* 132 */           fbMatrixNew.flush();
/*     */         }
/*     */         else
/*     */         {
/* 136 */           mergedCellList.clear();
/* 137 */           int oldPos = 0;
/* 138 */           int newPos = 0;
/* 139 */           Cell oldCell = null;
/* 140 */           Cell newCell = null;
/* 141 */           while ((oldPos < oldNum) && (newPos < newNum)) {
/* 142 */             if (oldCell == null) {
/* 143 */               int column = fbMatrixOld.readInt();
/* 144 */               fbMatrixOld.read(cellData);
/* 145 */               oldCell = newMatrix.createCell(i, column, cellData);
/*     */             }
/* 147 */             if (newCell == null) {
/* 148 */               newCell = newMatrix.getNonZeroCellInRow(i, newPos);
/*     */             }
/* 150 */             if (oldCell.getColumn() < newCell.getColumn()) {
/* 151 */               mergedCellList.add(oldCell);
/* 152 */               oldPos++;
/* 153 */               oldCell = null;
/*     */             }
/* 155 */             else if (oldCell.getColumn() > newCell.getColumn()) {
/* 156 */               mergedCellList.add(newCell);
/* 157 */               newPos++;
/* 158 */               newCell = null;
/*     */             }
/*     */             else {
/* 161 */               oldCell.merge(newCell);
/* 162 */               mergedCellList.add(oldCell);
/* 163 */               oldPos++;
/* 164 */               newPos++;
/* 165 */               oldCell = null;
/* 166 */               newCell = null;
/* 167 */               mergedCellNum++;
/*     */             }
/*     */           }
/*     */ 
/* 171 */           if (oldCell != null)
/*     */           {
/* 173 */             mergedCellList.add(oldCell);
/* 174 */             oldPos++;
/*     */           }
/*     */ 
/* 177 */           while (oldPos < oldNum) {
/* 178 */             int column = fbMatrixOld.readInt();
/* 179 */             fbMatrixOld.read(cellData);
/* 180 */             oldCell = newMatrix.createCell(i, column, cellData);
/* 181 */             mergedCellList.add(oldCell);
/* 182 */             oldPos++;
/*     */           }
/*     */ 
/* 185 */           while (newPos < newNum) {
/* 186 */             mergedCellList.add(newMatrix.getNonZeroCellInRow(i, newPos));
/* 187 */             newPos++;
/*     */           }
/*     */ 
/* 191 */           int num = mergedCellList.size();
/* 192 */           fbMatrixNew.writeInt(i);
/* 193 */           fbMatrixNew.writeInt(num);
/*     */ 
/* 195 */           for (int j = 0; j < num; j++) {
/* 196 */             newCell = (Cell)mergedCellList.get(j);
/* 197 */             fbMatrixNew.writeInt(newCell.getColumn());
/* 198 */             fbMatrixNew.write(newCell.toByteArray());
/*     */           }
/* 200 */           fbMatrixNew.flush();
/*     */         }
/*     */       }
/* 203 */       mergedCellList.clear();
/* 204 */       rafMatrixOld.close();
/* 205 */       if (minRows < this.rows)
/* 206 */         fbMatrixNew.write(fbMatrixOld, fbMatrixOld.remaining());
/* 207 */       fbMatrixOld.close();
/*     */ 
/* 210 */       for (int i = minRows; i < newMatrix.rows(); i++) {
/* 211 */         int num = newMatrix.getNonZeroNumInRow(i);
/* 212 */         fbMatrixNew.writeInt(i);
/* 213 */         fbMatrixNew.writeInt(num);
/* 214 */         for (int j = 0; j < num; j++) {
/* 215 */           fbMatrixNew.writeInt(newMatrix.getNonZeroColumnInRow(i, j));
/* 216 */           fbMatrixNew.write(newMatrix.getNonZeroCellInRow(i, j).toByteArray());
/*     */         }
/* 218 */         fbMatrixNew.flush();
/*     */       }
/* 220 */       fbMatrixNew.close();
/*     */ 
/* 222 */       this.rows = (this.rows > newMatrix.rows() ? this.rows : newMatrix.rows());
/* 223 */       this.columns = (this.columns > newMatrix.columns() ? this.columns : newMatrix.columns());
/* 224 */       this.cells = (this.cells + newMatrix.getNonZeroNum() - mergedCellNum);
/*     */ 
/* 226 */       if (!matrixf.delete()) {
/* 227 */         this.matrixFilename = tempMatrixFilename;
/*     */       }
/*     */       else {
/* 230 */         File matrixTemp = new File(tempMatrixFilename);
/* 231 */         matrixTemp.renameTo(matrixf);
/*     */       }
/*     */ 
/* 235 */       if (mergedCellNum > 0) {
/* 236 */         rafMatrixOld = new RandomAccessFile(this.matrixFilename, "rw");
/* 237 */         rafMatrixOld.skipBytes(8);
/* 238 */         rafMatrixOld.writeInt(this.cells);
/* 239 */         rafMatrixOld.close();
/*     */       }
/*     */ 
/* 242 */       System.out.println(new Date() + " Finish adding");
/* 243 */       return true;
/*     */     }
/*     */     catch (Exception ex) {
/* 246 */       ex.printStackTrace();
/* 247 */     }return false;
/*     */   }
/*     */ 
/*     */   private boolean append(SparseMatrix newMatrix)
/*     */   {
/*     */     try
/*     */     {
/* 257 */       if ((newMatrix == null) || (newMatrix.getNonZeroNum() == 0)) return false;
/*     */ 
/* 259 */       File matrixf = new File(this.matrixFilename);
/* 260 */       if (!matrixf.exists()) {
/* 261 */         return saveSparseMatrix(newMatrix);
/*     */       }
/* 263 */       System.out.println(new Date() + " Appending to old matrix...");
/*     */ 
/* 265 */       RandomAccessFile rafMatrix = new RandomAccessFile(matrixf, "rw");
/* 266 */       rafMatrix.writeInt(newMatrix.rows());
/* 267 */       rafMatrix.writeInt(newMatrix.columns());
/* 268 */       rafMatrix.writeInt(this.cells + newMatrix.getNonZeroNum());
/* 269 */       rafMatrix.close();
/*     */ 
/* 271 */       FastBinaryWriter fbMatrix = new FastBinaryWriter(this.matrixFilename, true);
/*     */ 
/* 273 */       for (int i = this.rows; i < newMatrix.rows(); i++) {
/* 274 */         int num = newMatrix.getNonZeroNumInRow(i);
/* 275 */         fbMatrix.writeInt(i);
/* 276 */         fbMatrix.writeInt(num);
/* 277 */         for (int j = 0; j < num; j++) {
/* 278 */           fbMatrix.writeInt(newMatrix.getNonZeroColumnInRow(i, j));
/* 279 */           fbMatrix.write(newMatrix.getNonZeroCellInRow(i, j).toByteArray());
/*     */         }
/* 281 */         fbMatrix.flush();
/*     */       }
/*     */ 
/* 284 */       fbMatrix.close();
/* 285 */       this.rows = newMatrix.rows();
/* 286 */       this.columns = newMatrix.columns();
/* 287 */       this.cells += newMatrix.getNonZeroNum();
/*     */ 
/* 289 */       System.out.println(new Date() + " Finish appending");
/* 290 */       return true;
/*     */     }
/*     */     catch (Exception ex) {
/* 293 */       ex.printStackTrace();
/* 294 */     }return false;
/*     */   }
/*     */ 
/*     */   public void genIndexFile(String indexFilename)
/*     */   {
/*     */     try
/*     */     {
/* 306 */       File file = new File(this.matrixFilename);
/* 307 */       if (!file.exists())
/* 308 */         return;
/* 309 */       RandomAccessFile rafMatrix = new RandomAccessFile(file, "r");
/* 310 */       new File(indexFilename).delete();
/* 311 */       FastBinaryWriter fwIndex = new FastBinaryWriter(indexFilename);
/*     */ 
/* 313 */       rafMatrix.skipBytes(12);
/* 314 */       fwIndex.writeInt(this.rows);
/* 315 */       fwIndex.writeInt(this.columns);
/* 316 */       fwIndex.writeInt(this.cells);
/* 317 */       for (int i = 0; i < this.rows; i++) {
/* 318 */         long rowOffset = rafMatrix.getFilePointer();
/* 319 */         int row = rafMatrix.readInt();
/* 320 */         if (row != i) {
/* 321 */           System.out.println("error");
/*     */         }
/* 323 */         int rowLen = rafMatrix.readInt();
/* 324 */         fwIndex.writeInt(row);
/* 325 */         fwIndex.writeLong(rowOffset);
/* 326 */         fwIndex.writeInt(rowLen);
/* 327 */         rafMatrix.skipBytes(rowLen * (this.cellDataLength + 4));
/* 328 */         fwIndex.flush();
/*     */       }
/* 330 */       rafMatrix.close();
/* 331 */       fwIndex.close();
/*     */     }
/*     */     catch (Exception e) {
/* 334 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean saveSparseMatrix(SparseMatrix matrix)
/*     */   {
/* 342 */     System.out.println(new Date() + " Saving Matrix...");
/*     */     try {
/* 344 */       FastBinaryWriter rafMatrix = new FastBinaryWriter(this.matrixFilename);
/* 345 */       this.rows = matrix.rows();
/* 346 */       this.columns = matrix.columns();
/* 347 */       this.cells = matrix.getNonZeroNum();
/* 348 */       rafMatrix.writeInt(this.rows);
/* 349 */       rafMatrix.writeInt(this.columns);
/* 350 */       rafMatrix.writeInt(this.cells);
/*     */ 
/* 352 */       for (int i = 0; i < this.rows; i++) {
/* 353 */         int num = matrix.getNonZeroNumInRow(i);
/* 354 */         rafMatrix.writeInt(i);
/* 355 */         rafMatrix.writeInt(num);
/* 356 */         for (int j = 0; j < num; j++) {
/* 357 */           rafMatrix.writeInt(matrix.getNonZeroColumnInRow(i, j));
/* 358 */           rafMatrix.write(matrix.getNonZeroCellInRow(i, j).toByteArray());
/*     */         }
/* 360 */         rafMatrix.flush();
/*     */       }
/* 362 */       rafMatrix.close();
/* 363 */       System.out.println(new Date() + " Finish saving");
/* 364 */       return true;
/*     */     }
/*     */     catch (Exception ex) {
/* 367 */       ex.printStackTrace();
/* 368 */     }return false;
/*     */   }
/*     */ 
/*     */   private void readStatInfo(String matrixFile)
/*     */   {
/* 376 */     File matrixf = new File(this.matrixFilename);
/* 377 */     if (!matrixf.exists())
/*     */     {
/* 379 */       this.rows = 0;
/* 380 */       this.columns = 0;
/* 381 */       this.cells = 0;
/* 382 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 386 */       FastBinaryReader rafMatrix = new FastBinaryReader(matrixFile);
/* 387 */       this.rows = rafMatrix.readInt();
/* 388 */       this.columns = rafMatrix.readInt();
/* 389 */       this.cells = rafMatrix.readInt();
/*     */     }
/*     */     catch (Exception e) {
/* 392 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private long getSuperMatrixRowStart(RandomAccessFile raf, int row)
/*     */   {
/*     */     try
/*     */     {
/* 400 */       raf.seek(12L);
/* 401 */       while (raf.readInt() < row) {
/* 402 */         int len = raf.readInt();
/* 403 */         raf.skipBytes(len * (this.cellDataLength + 4));
/*     */       }
/* 405 */       return raf.getFilePointer() - 4L;
/*     */     }
/*     */     catch (Exception e) {
/* 408 */       e.printStackTrace();
/* 409 */     }return -1L;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.SparseMatrixFactory
 * JD-Core Version:    0.6.2
 */