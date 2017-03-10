/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.File;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public abstract class AbstractSuperSparseMatrix extends AbstractSparseMatrix
/*     */ {
/*  18 */   private static int DEFAULT_CACHESIZE = 10000;
/*  19 */   private static int DEFAULT_FLUSHINTERVAL = 1000000;
/*     */   protected String matrixFilename;
/*     */   protected String indexFilename;
/*     */   protected int totalCell;
/*     */   protected RandomAccessFile matrix;
/*     */   protected long[] arrRowPosInFile;
/*     */   protected int cacheSize;
/*     */   protected Row[] arrCachedRow;
/*     */   protected float[] arrRowLoadFactor;
/*     */   protected int[] arrRowPosInCache;
/*     */   protected int[] arrRowStart;
/*     */   protected int firstEmpty;
/*     */   protected AbstractFlatSparseMatrix cacheMatrix;
/*     */   protected int flushInterval;
/*     */   protected SparseMatrixFactory matrixFactory;
/*     */ 
/*     */   protected abstract Row createRow(int paramInt1, int paramInt2, byte[] paramArrayOfByte);
/*     */ 
/*     */   protected abstract AbstractFlatSparseMatrix createFlatSparseMatrix(boolean paramBoolean1, boolean paramBoolean2);
/*     */ 
/*     */   public AbstractSuperSparseMatrix(String indexFilename, String matrixFilename, int cellDataLength, boolean mergeMode, boolean miniMode)
/*     */   {
/*  43 */     super(mergeMode, miniMode, cellDataLength);
/*  44 */     this.indexFilename = indexFilename;
/*  45 */     this.matrixFilename = matrixFilename;
/*  46 */     this.matrixFactory = new SparseMatrixFactory(matrixFilename, cellDataLength);
/*  47 */     this.flushInterval = DEFAULT_FLUSHINTERVAL;
/*  48 */     this.isFinalized = false;
/*  49 */     this.cacheMatrix = createFlatSparseMatrix(mergeMode, miniMode);
/*     */ 
/*  51 */     this.matrix = null;
/*  52 */     this.arrRowPosInFile = null;
/*  53 */     this.arrCachedRow = null;
/*  54 */     this.arrRowLoadFactor = null;
/*  55 */     this.arrRowPosInCache = null;
/*  56 */     this.arrRowStart = null;
/*  57 */     this.firstEmpty = 0;
/*  58 */     this.cacheSize = 0;
/*     */   }
/*     */ 
/*     */   public AbstractSuperSparseMatrix(String indexFilename, String matrixFilename, int cellDataLength)
/*     */   {
/*  63 */     super(false, false, cellDataLength);
/*  64 */     this.indexFilename = indexFilename;
/*  65 */     this.matrixFilename = matrixFilename;
/*  66 */     this.matrixFactory = null;
/*  67 */     this.flushInterval = 0;
/*  68 */     this.isFinalized = true;
/*  69 */     this.cacheMatrix = null;
/*     */ 
/*  71 */     this.matrix = null;
/*  72 */     this.arrRowPosInFile = null;
/*  73 */     this.arrCachedRow = null;
/*  74 */     this.arrRowLoadFactor = null;
/*  75 */     this.arrRowPosInCache = null;
/*  76 */     this.arrRowStart = null;
/*  77 */     this.firstEmpty = 0;
/*  78 */     this.cacheSize = 0;
/*     */ 
/*  80 */     initData(DEFAULT_CACHESIZE);
/*     */   }
/*     */ 
/*     */   private void initData(int cacheSize) {
/*  84 */     if ((this.indexFilename != null) && (FileUtil.exist(this.indexFilename)))
/*  85 */       readIndexFile(this.indexFilename);
/*  86 */     else if (FileUtil.exist(this.matrixFilename))
/*  87 */       readIndexFromMatrix(this.matrixFilename);
/*     */     try {
/*  89 */       if (FileUtil.exist(this.matrixFilename)) {
/*  90 */         this.matrix = new RandomAccessFile(this.matrixFilename, "r");
/*  91 */         setCache(cacheSize);
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  96 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setCache(int cacheSize) {
/* 101 */     if (cacheSize <= 0)
/* 102 */       cacheSize = DEFAULT_CACHESIZE;
/* 103 */     if (this.arrCachedRow == null) {
/* 104 */       this.cacheSize = cacheSize;
/* 105 */       this.arrCachedRow = new Row[cacheSize];
/* 106 */       this.firstEmpty = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setFlushInterval(int interval) {
/* 111 */     this.flushInterval = interval;
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */     try {
/* 116 */       this.totalCell = 0;
/* 117 */       if (this.matrix != null)
/* 118 */         this.matrix.close();
/* 119 */       this.arrRowPosInFile = null;
/* 120 */       this.arrRowPosInFile = null;
/* 121 */       this.cacheSize = 0;
/* 122 */       this.arrCachedRow = null;
/* 123 */       this.arrRowLoadFactor = null;
/* 124 */       this.arrRowStart = null;
/* 125 */       this.firstEmpty = 0;
/*     */     }
/*     */     catch (Exception e) {
/* 128 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean add(Cell cell) {
/* 133 */     if (this.isFinalized) {
/* 134 */       return false;
/*     */     }
/* 136 */     this.cacheMatrix.add(cell);
/* 137 */     if (this.cacheMatrix.getNonZeroNum() >= this.flushInterval) {
/* 138 */       flush();
/*     */     }
/* 140 */     return true;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/* 145 */     this.cacheMatrix.finalizeData(true);
/* 146 */     this.matrixFactory.add(this.cacheMatrix);
/* 147 */     this.cacheMatrix.close();
/*     */   }
/*     */ 
/*     */   public boolean finalizeData(boolean sorting)
/*     */   {
/* 153 */     if (this.isFinalized) return false;
/* 154 */     flush();
/* 155 */     this.columns = this.matrixFactory.columns();
/* 156 */     this.rows = this.matrixFactory.rows();
/* 157 */     this.totalCell = this.matrixFactory.getNonZeroNum();
/* 158 */     this.isFinalized = true;
/* 159 */     if (this.indexFilename != null) this.matrixFactory.genIndexFile(this.indexFilename);
/* 160 */     if (!this.matrixFactory.getMatrixFilename().equalsIgnoreCase(this.matrixFilename)) {
/* 161 */       File file = new File(this.matrixFilename);
/* 162 */       file.delete();
/* 163 */       new File(this.matrixFactory.getMatrixFilename()).renameTo(file);
/*     */     }
/* 165 */     return true;
/*     */   }
/*     */ 
/*     */   public String getMatrixFilename() {
/* 169 */     return this.matrixFilename;
/*     */   }
/*     */ 
/*     */   public String getIndexFilename() {
/* 173 */     return this.indexFilename;
/*     */   }
/*     */ 
/*     */   public int getNonZeroNum() {
/* 177 */     return this.totalCell;
/*     */   }
/*     */ 
/*     */   public int getNonZeroNumInRow(int row) {
/* 181 */     if (row >= this.rows) return 0;
/*     */ 
/* 183 */     if (this.arrRowStart == null) {
/* 184 */       initData(this.cacheSize);
/*     */     }
/* 186 */     return this.arrRowStart[(row + 1)] - this.arrRowStart[row];
/*     */   }
/*     */ 
/*     */   public int getNonZeroColumnInRow(int row, int index) {
/* 190 */     return getRow(row).getNonZeroColumn(index);
/*     */   }
/*     */ 
/*     */   public int[] getNonZeroColumnsInRow(int row)
/*     */   {
/* 196 */     if (row >= this.rows) return null;
/*     */ 
/* 198 */     int[] oldArray = getRow(row).getNonZeroColumns();
/* 199 */     int[] newArray = new int[oldArray.length];
/* 200 */     System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
/* 201 */     return newArray;
/*     */   }
/*     */ 
/*     */   public Cell getCell(int row, int col) {
/* 205 */     if (row >= this.rows) return null;
/*     */ 
/* 207 */     return getRow(row).getCell(col);
/*     */   }
/*     */ 
/*     */   public Cell getNonZeroCellInRow(int row, int index) {
/* 211 */     return getRow(row).getNonZeroCell(index);
/*     */   }
/*     */ 
/*     */   protected Row getRow(int index)
/*     */   {
/* 217 */     if (this.arrRowStart == null) {
/* 218 */       initData(this.cacheSize);
/*     */     }
/*     */ 
/* 221 */     if (this.arrRowPosInCache[index] >= 0) {
/* 222 */       return this.arrCachedRow[this.arrRowPosInCache[index]];
/*     */     }
/*     */ 
/* 225 */     return loadRow(index, getRoomInCache());
/*     */   }
/*     */ 
/*     */   private Row loadRow(int index, int posInCache)
/*     */   {
/*     */     try
/*     */     {
/*     */       int tmp5_4 = index;
/*     */       float[] tmp5_1 = this.arrRowLoadFactor; tmp5_1[tmp5_4] = ((float)(tmp5_1[tmp5_4] + 0.1D * Math.log(this.arrRowStart[(index + 1)] - this.arrRowStart[index])));
/* 237 */       Row curRow = this.arrCachedRow[posInCache];
/* 238 */       if (curRow != null) {
/* 239 */         this.arrRowPosInCache[curRow.getRowIndex()] = -1;
/*     */       }
/*     */ 
/* 242 */       this.matrix.seek(this.arrRowPosInFile[index]);
/* 243 */       this.matrix.readInt();
/* 244 */       int len = this.matrix.readInt();
/* 245 */       byte[] data = new byte[len * (getCellDataLength() + 4)];
/* 246 */       this.matrix.read(data);
/*     */ 
/* 248 */       if (curRow != null) {
/* 249 */         curRow.load(index, len, data);
/* 250 */         curRow.setLoadFactor(this.arrRowLoadFactor[index]);
/*     */       }
/*     */       else {
/* 253 */         curRow = createRow(index, len, data);
/* 254 */         curRow.setLoadFactor(this.arrRowLoadFactor[index]);
/* 255 */         this.arrCachedRow[posInCache] = curRow;
/*     */       }
/* 257 */       this.arrRowPosInCache[index] = posInCache;
/* 258 */       return curRow;
/*     */     } catch (Exception e) {
/*     */     }
/* 261 */     return null;
/*     */   }
/*     */ 
/*     */   private int getRoomInCache()
/*     */   {
	 int i;
/* 270 */     if (this.firstEmpty >= 0) {
/* 271 */       int pos = this.firstEmpty;
/* 272 */       this.firstEmpty += 1;
/* 273 */       if (this.firstEmpty >= this.cacheSize) {
/* 274 */         this.firstEmpty = -1;
/*     */       }
/* 276 */       return pos;
/*     */     }
/*     */ 
/* 279 */     ArrayList list = new ArrayList(this.cacheSize);
/* 280 */     for (  i = 0; i < this.cacheSize; i++) {
/* 281 */       list.add(this.arrCachedRow[i]);
/*     */     }
/* 283 */     Collections.sort(list);
/* 284 */     int breakpoint = (int)(this.cacheSize * 0.9D);
/* 285 */     for (i = 0; i < breakpoint; i++) {
/* 286 */       Row row = (Row)list.get(i);
/* 287 */       this.arrRowPosInCache[row.getRowIndex()] = i;
/* 288 */       this.arrCachedRow[i] = row;
/*     */     }
/* 290 */     for (i = breakpoint; i < this.cacheSize; i++) {
/* 291 */       Row row = (Row)list.get(i);
/* 292 */       this.arrRowPosInCache[row.getRowIndex()] = -1;
/* 293 */       this.arrCachedRow[i] = null;
/*     */     }
/* 295 */     int pos = breakpoint;
/* 296 */     this.firstEmpty = (pos + 1);
/* 297 */     return pos;
/*     */   }
/*     */ 
/*     */   private void readIndexFile(String filename)
/*     */   {
/*     */     try
/*     */     {
/* 307 */       FastBinaryReader reader = new FastBinaryReader(filename);
/* 308 */       this.rows = reader.readInt();
/* 309 */       this.columns = reader.readInt();
/* 310 */       this.totalCell = reader.readInt();
/*     */ 
/* 312 */       this.arrRowLoadFactor = new float[this.rows];
/* 313 */       this.arrRowPosInCache = new int[this.rows];
/* 314 */       this.arrRowPosInFile = new long[this.rows + 1];
/* 315 */       this.arrRowStart = new int[this.rows + 1];
/* 316 */       this.arrRowStart[0] = 0;
/* 317 */       for (int i = 0; i < this.rows; i++)
/*     */       {
/* 319 */         int row = reader.readInt();
/* 320 */         long offset = reader.readLong();
/* 321 */         int freq = reader.readInt();
/* 322 */         this.arrRowLoadFactor[row] = ((float)Math.log(freq));
/* 323 */         this.arrRowPosInCache[row] = -1;
/* 324 */         this.arrRowPosInFile[row] = offset;
/* 325 */         this.arrRowStart[(row + 1)] = (this.arrRowStart[row] + freq);
/*     */       }
/* 327 */       this.arrRowPosInFile[this.rows] = (this.arrRowPosInFile[(this.rows - 1)] + (this.arrRowStart[this.rows] - this.arrRowStart[(this.rows - 1)]) * (this.cellDataLength + 4) + 8L);
/* 328 */       reader.close();
/*     */     }
/*     */     catch (Exception e) {
/* 331 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readIndexFromMatrix(String filename)
/*     */   {
/*     */     try
/*     */     {
/* 340 */       RandomAccessFile raf = new RandomAccessFile(filename, "r");
/* 341 */       this.rows = raf.readInt();
/* 342 */       this.columns = raf.readInt();
/* 343 */       this.totalCell = raf.readInt();
/*     */ 
/* 345 */       this.arrRowLoadFactor = new float[this.rows];
/* 346 */       this.arrRowPosInCache = new int[this.rows];
/* 347 */       this.arrRowPosInFile = new long[this.rows + 1];
/* 348 */       this.arrRowStart = new int[this.rows + 1];
/* 349 */       this.arrRowStart[0] = 0;
/* 350 */       for (int i = 0; i < this.rows; i++)
/*     */       {
/* 352 */         long offset = raf.getFilePointer();
/* 353 */         int row = raf.readInt();
/* 354 */         int freq = raf.readInt();
/* 355 */         this.arrRowLoadFactor[row] = ((float)Math.log(freq));
/* 356 */         this.arrRowPosInCache[row] = -1;
/* 357 */         this.arrRowPosInFile[row] = offset;
/* 358 */         this.arrRowStart[(row + 1)] = (this.arrRowStart[row] + freq);
/* 359 */         raf.skipBytes(freq * (this.cellDataLength + 4));
/*     */       }
/* 361 */       this.arrRowPosInFile[this.rows] = raf.getFilePointer();
/* 362 */       raf.close();
/*     */     }
/*     */     catch (Exception e) {
/* 365 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.AbstractSuperSparseMatrix
 * JD-Core Version:    0.6.2
 */