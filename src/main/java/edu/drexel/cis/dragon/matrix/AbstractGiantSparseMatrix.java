/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.ByteArrayConvert;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.File;
/*     */ import java.io.RandomAccessFile;
/*     */ 
/*     */ public abstract class AbstractGiantSparseMatrix extends AbstractSparseMatrix
/*     */ {
/*  18 */   private static int DEFAULT_FLUSHINTERVAL = 1000000;
/*     */   protected String indexFilename;
/*     */   protected String matrixFilename;
/*     */   protected int totalCell;
/*     */   protected RandomAccessFile rafIndex;
/*     */   protected RandomAccessFile rafMatrix;
/*     */   private int lastAccessIndex;
/*     */   private int lastAccessRowLen;
/*     */   private long lastAccessRowStart;
/*     */   private Row lastAccessRow;
/*     */   private byte[] buf;
/*     */   protected AbstractFlatSparseMatrix cacheMatrix;
/*     */   protected int flushInterval;
/*     */   protected SparseMatrixFactory matrixFactory;
/*     */ 
/*     */   protected abstract Row createRow(int paramInt1, int paramInt2, byte[] paramArrayOfByte);
/*     */ 
/*     */   protected abstract AbstractFlatSparseMatrix createFlatSparseMatrix(boolean paramBoolean1, boolean paramBoolean2);
/*     */ 
/*     */   public AbstractGiantSparseMatrix(String indexFilename, String matrixFilename, int cellDataLength)
/*     */   {
/*  38 */     super(false, false, cellDataLength);
/*  39 */     this.indexFilename = indexFilename;
/*  40 */     this.matrixFilename = matrixFilename;
/*  41 */     this.matrixFactory = null;
/*  42 */     this.flushInterval = 0;
/*  43 */     this.isFinalized = true;
/*  44 */     this.cacheMatrix = null;
/*  45 */     initData();
/*     */   }
/*     */ 
/*     */   public AbstractGiantSparseMatrix(String indexFilename, String matrixFilename, int cellDataLength, boolean mergeMode, boolean miniMode) {
/*  49 */     super(mergeMode, miniMode, cellDataLength);
/*  50 */     this.indexFilename = indexFilename;
/*  51 */     this.matrixFilename = matrixFilename;
/*  52 */     this.matrixFactory = new SparseMatrixFactory(matrixFilename, cellDataLength);
/*  53 */     this.flushInterval = DEFAULT_FLUSHINTERVAL;
/*  54 */     this.isFinalized = false;
/*  55 */     this.cacheMatrix = createFlatSparseMatrix(mergeMode, miniMode);
/*     */   }
/*     */ 
/*     */   private void initData() {
/*     */     try {
/*  60 */       this.buf = new byte[12];
/*  61 */       if ((FileUtil.exist(this.indexFilename)) && (FileUtil.exist(this.matrixFilename))) {
/*  62 */         this.rafIndex = new RandomAccessFile(this.indexFilename, "r");
/*  63 */         this.rafMatrix = new RandomAccessFile(this.matrixFilename, "r");
/*  64 */         this.lastAccessIndex = -1;
/*  65 */         this.rows = this.rafIndex.readInt();
/*  66 */         this.columns = this.rafIndex.readInt();
/*  67 */         this.totalCell = this.rafIndex.readInt();
/*     */       }
/*     */       else {
/*  70 */         this.rafIndex = null;
/*  71 */         this.rafMatrix = null;
/*  72 */         this.lastAccessIndex = -1;
/*  73 */         this.rows = 0;
/*  74 */         this.columns = 0;
/*  75 */         this.totalCell = 0;
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  79 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setFlushInterval(int interval) {
/*  84 */     this.flushInterval = interval;
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */     try {
/*  89 */       if (this.isFinalized) {
/*  90 */         if (this.rafIndex != null)
/*  91 */           this.rafIndex.close();
/*  92 */         if (this.rafMatrix != null)
/*  93 */           this.rafMatrix.close();
/*  94 */         this.lastAccessIndex = -1;
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  98 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean add(Cell cell) {
/* 103 */     if (this.isFinalized) {
/* 104 */       return false;
/*     */     }
/* 106 */     this.cacheMatrix.add(cell);
/* 107 */     if (this.cacheMatrix.getNonZeroNum() >= this.flushInterval) {
/* 108 */       flush();
/*     */     }
/* 110 */     return true;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/* 115 */     if (this.isFinalized) return;
/*     */ 
/* 117 */     this.cacheMatrix.finalizeData(true);
/* 118 */     this.matrixFactory.add(this.cacheMatrix);
/* 119 */     this.cacheMatrix.close();
/*     */   }
/*     */ 
/*     */   public boolean finalizeData(boolean sorting)
/*     */   {
/* 125 */     if (this.isFinalized) return false;
/* 126 */     flush();
/* 127 */     this.rows = this.matrixFactory.rows();
/* 128 */     this.columns = this.matrixFactory.columns();
/* 129 */     this.totalCell = this.matrixFactory.getNonZeroNum();
/* 130 */     this.isFinalized = true;
/* 131 */     if (this.indexFilename != null) this.matrixFactory.genIndexFile(this.indexFilename);
/* 132 */     if (!this.matrixFactory.getMatrixFilename().equalsIgnoreCase(this.matrixFilename)) {
/* 133 */       File file = new File(this.matrixFilename);
/* 134 */       file.delete();
/* 135 */       new File(this.matrixFactory.getMatrixFilename()).renameTo(file);
/*     */     }
/* 137 */     initData();
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   public String getMatrixFilename() {
/* 142 */     return this.matrixFilename;
/*     */   }
/*     */ 
/*     */   public String getIndexFilename() {
/* 146 */     return this.indexFilename;
/*     */   }
/*     */ 
/*     */   public int getNonZeroNum() {
/* 150 */     return this.totalCell;
/*     */   }
/*     */ 
/*     */   public int getNonZeroNumInRow(int row) {
/* 154 */     if (row >= this.rows) {
/* 155 */       return 0;
/*     */     }
/* 157 */     return getRowLen(row);
/*     */   }
/*     */ 
/*     */   public int getNonZeroColumnInRow(int row, int index) {
/* 161 */     return getRow(row).getNonZeroColumn(index);
/*     */   }
/*     */ 
/*     */   public int[] getNonZeroColumnsInRow(int row)
/*     */   {
/* 167 */     if (row >= this.rows) return null;
/*     */ 
/* 169 */     int[] oldArray = getRow(row).getNonZeroColumns();
/* 170 */     int[] newArray = new int[oldArray.length];
/* 171 */     System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
/* 172 */     return newArray;
/*     */   }
/*     */ 
/*     */   public Cell getCell(int row, int col) {
/* 176 */     if (row >= this.rows) return null;
/*     */ 
/* 178 */     return getRow(row).getCell(col);
/*     */   }
/*     */ 
/*     */   public Cell getNonZeroCellInRow(int row, int index) {
/* 182 */     return getRow(row).getNonZeroCell(index);
/*     */   }
/*     */ 
/*     */   private int getRowLen(int index) {
/*     */     try {
/* 187 */       if (this.lastAccessIndex == index) {
/* 188 */         return this.lastAccessRowLen;
/*     */       }
/* 190 */       this.rafIndex.seek(index * 16 + 12 + 4);
/* 191 */       this.rafIndex.read(this.buf);
/* 192 */       this.lastAccessRowStart = ByteArrayConvert.toLong(this.buf, 0);
/* 193 */       this.lastAccessRowLen = ByteArrayConvert.toInt(this.buf, 8);
/* 194 */       this.lastAccessRow = null;
/* 195 */       this.lastAccessIndex = index;
/* 196 */       return this.lastAccessRowLen;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 200 */       e.printStackTrace();
/* 201 */     }return -1;
/*     */   }
/*     */ 
/*     */   protected Row getRow(int index)
/*     */   {
/* 207 */     if (this.lastAccessIndex == index) {
/* 208 */       if (this.lastAccessRow != null) {
/* 209 */         return this.lastAccessRow;
/*     */       }
/* 211 */       return loadRow(index, this.lastAccessRowLen);
/*     */     }
/*     */ 
/* 214 */     return loadRow(index, -1);
/*     */   }
/*     */ 
/*     */   private Row loadRow(int index, int rowLen)
/*     */   {
/*     */     try
/*     */     {
/* 224 */       if (rowLen < 0) rowLen = getRowLen(index);
/*     */       byte[] data;
/* 225 */       if (rowLen == 0) {
/* 226 */         data = new byte[0];
/*     */       } else {
/* 228 */         this.rafMatrix.seek(this.lastAccessRowStart + 8L);
/* 229 */         data = new byte[rowLen * (getCellDataLength() + 4)];
/* 230 */         this.rafMatrix.read(data);
/*     */       }
/* 232 */       Row curRow = createRow(index, rowLen, data);
/* 233 */       this.lastAccessRow = curRow;
/* 234 */       return curRow;
/*     */     } catch (Exception e) {
/*     */     }
/* 237 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.AbstractGiantSparseMatrix
 * JD-Core Version:    0.6.2
 */