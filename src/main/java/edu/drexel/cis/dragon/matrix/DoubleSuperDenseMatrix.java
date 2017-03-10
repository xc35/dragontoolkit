/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import java.io.PrintStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class DoubleSuperDenseMatrix extends AbstractDenseMatrix
/*     */   implements DoubleDenseMatrix, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private RandomAccessFile raf;
/*     */   private double[] cache;
/*     */   private int curRow;
/*     */   private boolean readOnly;
/*     */   private boolean modified;
/*     */ 
/*     */   public DoubleSuperDenseMatrix(String filename)
/*     */   {
/*  24 */     this(filename, 0, true);
/*     */   }
/*     */ 
/*     */   public DoubleSuperDenseMatrix(String filename, int columns, boolean readOnly) {
/*  28 */     super(0, columns, 8);
/*     */     try
/*     */     {
/*  31 */       this.curRow = -1;
/*  32 */       this.cache = null;
/*  33 */       this.readOnly = readOnly;
/*  34 */       if (readOnly)
/*  35 */         this.raf = new RandomAccessFile(filename, "r");
/*     */       else
/*  37 */         this.raf = new RandomAccessFile(filename, "rw");
/*  38 */       if (this.raf.length() <= 12L) {
/*  39 */         this.rows = 0;
/*  40 */         this.raf.writeDouble(this.rows);
/*  41 */         this.raf.writeDouble(columns);
/*  42 */         this.raf.writeDouble(this.rows * columns);
/*     */       }
/*     */       else {
/*  45 */         this.rows = this.raf.readInt();
/*  46 */         this.columns = this.raf.readInt();
/*     */       }
/*  48 */       if (this.columns > 0)
/*  49 */         this.cache = new double[this.columns];
/*     */     }
/*     */     catch (Exception e) {
/*  52 */       this.raf = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public double getDouble(int row, int column) {
/*  57 */     if (row >= this.rows)
/*  58 */       return 0.0D;
/*  59 */     if (this.curRow != row)
/*  60 */       readCacheRow(row);
/*  61 */     return this.cache[column];
/*     */   }
/*     */ 
/*     */   public int getInt(int row, int column) {
/*  65 */     return (int)getDouble(row, column);
/*     */   }
/*     */ 
/*     */   public void assign(double val) {
/*  69 */     System.out.println("This method is not supported.");
/*     */   }
/*     */ 
/*     */   public boolean add(int row, int column, double score) {
/*  73 */     if ((this.readOnly) || (row > this.rows))
/*  74 */       return false;
/*  75 */     if (row == this.rows) {
/*  76 */       writeCacheRow();
/*  77 */       this.rows += 1;
/*  78 */       this.curRow = row;
/*  79 */       MathUtil.initArray(this.cache, 0.0D);
/*     */     }
/*  81 */     else if (row != this.curRow) {
/*  82 */       readCacheRow(row);
/*  83 */     }this.cache[column] = score;
/*  84 */     this.modified = true;
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean setDouble(int row, int column, double score) {
/*  89 */     if ((this.readOnly) || (row > this.rows))
/*  90 */       return false;
/*  91 */     if (row == this.rows) {
/*  92 */       writeCacheRow();
/*  93 */       this.rows += 1;
/*  94 */       this.curRow = row;
/*  95 */       MathUtil.initArray(this.cache, 0.0D);
/*     */     }
/*  97 */     else if (row != this.curRow) {
/*  98 */       readCacheRow(row);
/*  99 */     }this.cache[column] = score;
/* 100 */     this.modified = true;
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean setDouble(int row, double[] scores) {
/* 105 */     if ((this.readOnly) || (row > this.rows) || (scores.length != this.columns))
/* 106 */       return false;
/* 107 */     writeCacheRow();
/* 108 */     if (row == this.rows)
/* 109 */       this.rows += 1;
/* 110 */     this.curRow = row;
/* 111 */     System.arraycopy(scores, 0, this.cache, 0, this.columns);
/* 112 */     this.modified = true;
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   public double getRowSum(int row)
/*     */   {
/* 121 */     if (row >= this.rows)
/* 122 */       return 0.0D;
/* 123 */     if (this.curRow != row)
/* 124 */       readCacheRow(row);
/* 125 */     return MathUtil.sumArray(this.cache);
/*     */   }
/*     */ 
/*     */   public double getColumnSum(int column)
/*     */   {
/* 133 */     System.out.println("This method is not supported.");
/* 134 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */     try {
/* 139 */       writeCacheRow();
/* 140 */       this.raf.seek(0L);
/* 141 */       this.raf.writeInt(this.rows);
/* 142 */       this.raf.writeInt(this.columns);
/* 143 */       this.raf.writeInt(this.rows * this.columns);
/* 144 */       this.raf.setLength(12 + this.rows * this.columns * this.cellDataLength);
/* 145 */       this.raf.close();
/*     */     }
/*     */     catch (Exception e) {
/* 148 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeCacheRow()
/*     */   {
/*     */     try
/*     */     {
/* 156 */       if ((this.curRow < 0) || (!this.modified))
/* 157 */         return;
/* 158 */       this.raf.seek(12 + this.curRow * this.columns * this.cellDataLength);
/* 159 */       for (int i = 0; i < this.columns; i++)
/* 160 */         this.raf.writeDouble(this.cache[i]);
/* 161 */       this.modified = false;
/*     */     }
/*     */     catch (Exception e) {
/* 164 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readCacheRow(int row)
/*     */   {
/*     */     try
/*     */     {
/* 172 */       writeCacheRow();
/* 173 */       this.curRow = row;
/* 174 */       this.raf.seek(12 + this.curRow * this.columns * this.cellDataLength);
/* 175 */       for (int i = 0; i < this.columns; i++)
/* 176 */         this.cache[i] = this.raf.readDouble();
/*     */     }
/*     */     catch (Exception e) {
/* 179 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.DoubleSuperDenseMatrix
 * JD-Core Version:    0.6.2
 */