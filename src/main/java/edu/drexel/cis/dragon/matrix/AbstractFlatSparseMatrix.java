/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public abstract class AbstractFlatSparseMatrix extends AbstractSparseMatrix
/*     */   implements Serializable
/*     */ {
/*     */   protected ArrayList list;
/*     */   private int[] arrRowStartPos;
/*     */   private int lastAccessedRow;
/*     */   private int lastRowStart;
/*     */   private int lastRowEnd;
/*     */   private CoordinateComparator coordinateComparator;
/*     */ 
/*     */   public AbstractFlatSparseMatrix(boolean mergeMode, boolean miniMode, int cellDataLength)
/*     */   {
/*  24 */     super(mergeMode, miniMode, cellDataLength);
/*  25 */     this.coordinateComparator = new CoordinateComparator();
/*  26 */     this.list = new ArrayList();
/*     */   }
/*     */ 
/*     */   protected int getRowStart(int row) {
/*  30 */     if (row < this.rowBase) return 0;
/*     */ 
/*  32 */     if (!this.miniMode) {
/*  33 */       return this.arrRowStartPos[(row - this.rowBase)];
/*     */     }
/*     */ 
/*  36 */     if (row != this.lastAccessedRow)
/*  37 */       setRowRange(row);
/*  38 */     return this.lastRowStart;
/*     */   }
/*     */ 
/*     */   protected int getRowEnd(int row)
/*     */   {
/*  43 */     if (row < this.rowBase) return 0;
/*     */ 
/*  45 */     if (!this.miniMode) {
/*  46 */       return this.arrRowStartPos[(row + 1 - this.rowBase)];
/*     */     }
/*     */ 
/*  49 */     if (row != this.lastAccessedRow)
/*  50 */       setRowRange(row);
/*  51 */     return this.lastRowEnd;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  56 */     this.isFinalized = false;
/*  57 */     if (this.list != null)
/*  58 */       this.list.clear();
/*  59 */     this.transposeMatrix = null;
/*  60 */     this.arrRowStartPos = null;
/*  61 */     this.rows = 0;
/*  62 */     this.rowBase = 0;
/*  63 */     this.columns = 0;
/*     */   }
/*     */ 
/*     */   public int getNonZeroNum() {
/*  67 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   public Cell getNonZeroCell(int index) {
/*  71 */     return (Cell)this.list.get(index);
/*     */   }
/*     */ 
/*     */   public Cell getCell(int row, int column)
/*     */   {
/*  78 */     if ((row < this.rowBase) || (row >= this.rows)) return null;
/*     */ 
/*  80 */     int start = getRowStart(row);
/*  81 */     int end = getRowEnd(row) - 1;
/*  82 */     Cell curCell = new IntCell(row, column, 0);
/*  83 */     int pos = SortedArray.binarySearch(this.list, curCell, start, end, this.coordinateComparator);
/*  84 */     if (pos < 0) {
/*  85 */       return null;
/*     */     }
/*  87 */     return (Cell)this.list.get(pos);
/*     */   }
/*     */ 
/*     */   public Cell getNonZeroCellInRow(int row, int index) {
/*  91 */     return (Cell)this.list.get(getRowStart(row) + index);
/*     */   }
/*     */ 
/*     */   public int getNonZeroNumInRow(int row) {
/*  95 */     if (row >= this.rows) return 0;
/*  96 */     return getRowEnd(row) - getRowStart(row);
/*     */   }
/*     */ 
/*     */   public boolean add(Cell cell) {
/* 100 */     if (this.isFinalized) return false;
/*     */ 
/* 102 */     if (this.mergeMode) {
/* 103 */       int pos = SortedArray.binarySearch(this.list, cell, 0, this.list.size() - 1, this.coordinateComparator);
/* 104 */       if (pos >= 0)
/* 105 */         ((Cell)this.list.get(pos)).merge(cell);
/*     */       else
/* 107 */         this.list.add(-1 * pos - 1, cell);
/*     */     }
/*     */     else {
/* 110 */       this.list.add(cell);
/*     */     }
/* 112 */     if (cell.getColumn() >= this.columns)
/* 113 */       this.columns = (cell.getColumn() + 1);
/* 114 */     return true;
/*     */   }
/*     */ 
/*     */   public int getNonZeroColumnInRow(int row, int index) {
/* 118 */     return getNonZeroCellInRow(row, index).getColumn();
/*     */   }
/*     */ 
/*     */   public int[] getNonZeroColumnsInRow(int row)
/*     */   {
/* 125 */     int num = getNonZeroNumInRow(row);
/* 126 */     int[] arrColumn = new int[num];
/*     */ 
/* 128 */     for (int count = 0; count < num; count++)
/*     */     {
/* 130 */       arrColumn[count] = getNonZeroCellInRow(row, count).getColumn();
/*     */     }
/* 132 */     return arrColumn;
/*     */   }
/*     */ 
/*     */   public void setNonZeroDoubleScoreInRow(int row, int index, double score) {
/* 136 */     getNonZeroCellInRow(row, index).setDoubleScore(score);
/*     */   }
/*     */ 
/*     */   public void setNonZeroIntScoreInRow(int row, int index, int score) {
/* 140 */     getNonZeroCellInRow(row, index).setIntScore(score);
/*     */   }
/*     */ 
/*     */   public void setDouble(int row, int column, double score)
/*     */   {
/* 146 */     Cell curCell = getCell(row, column);
/* 147 */     if (curCell != null)
/* 148 */       curCell.setDoubleScore(score);
/*     */   }
/*     */ 
/*     */   public void setInt(int row, int column, int score)
/*     */   {
/* 154 */     Cell curCell = getCell(row, column);
/* 155 */     if (curCell != null)
/* 156 */       curCell.setDoubleScore(score);
/*     */   }
/*     */ 
/*     */   public void flush() {
/*     */   }
/*     */ 
/*     */   public boolean finalizeData(boolean sorting) {
/* 163 */     if (this.isFinalized) {
/* 164 */       return false;
/*     */     }
/* 166 */     if (this.mergeMode)
/* 167 */       sorting = false;
/* 168 */     if (this.list.size() > 0) {
/* 169 */       if (sorting) Collections.sort(this.list, new CoordinateComparator());
/* 170 */       this.rowBase = ((Cell)this.list.get(0)).getRow();
/* 171 */       this.rows = (((Cell)this.list.get(this.list.size() - 1)).getRow() + 1);
/*     */     }
/*     */     else {
/* 174 */       this.rowBase = 0;
/* 175 */       this.rows = 0;
/*     */     }
/* 177 */     this.isFinalized = true;
/* 178 */     if (!this.miniMode)
/* 179 */       setRowStart();
/*     */     else
/* 181 */       setRowRange(0);
/* 182 */     return true;
/*     */   }
/*     */ 
/*     */   private void setRowStart()
/*     */   {
/* 189 */     int total = this.rows - this.rowBase;
/* 190 */     this.arrRowStartPos = new int[total + 1];
/* 191 */     int high = this.list.size() - 1;
/* 192 */     this.arrRowStartPos[0] = 0;
/* 193 */     this.arrRowStartPos[total] = this.list.size();
/*     */ 
/* 195 */     for (int i = 1; i < total; i++)
/*     */     {
/* 197 */       Cell curCell = new IntCell(i + this.rowBase, 0);
/* 198 */       int pos = SortedArray.binarySearch(this.list, curCell, this.arrRowStartPos[(i - 1)], high);
/* 199 */       if (pos < 0) pos = -1 * pos - 1;
/* 200 */       this.arrRowStartPos[i] = pos;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void saveTo(String filename, boolean binary)
/*     */   {
/* 207 */     if (!binary) {
/* 208 */       saveTo(filename);
/*     */     }
/*     */     else {
/* 211 */       SparseMatrixFactory factory = new SparseMatrixFactory(filename, getCellDataLength());
/* 212 */       factory.add(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void saveTo(String filename)
/*     */   {
/* 218 */     print(FileUtil.getPrintWriter(filename));
/*     */   }
/*     */ 
/*     */   public void print(PrintWriter out)
/*     */   {
/*     */     try
/*     */     {
/* 227 */       out.write(String.valueOf(this.rows) + "," + String.valueOf(this.columns) + "," + String.valueOf(this.list.size()) + "\n");
/*     */ 
/* 229 */       for (int i = 0; i < this.rows; i++)
/*     */       {
/* 231 */         int num = getNonZeroNumInRow(i);
/* 232 */         for (int j = 0; j < num; j++)
/*     */         {
/* 234 */           Cell cur = getNonZeroCellInRow(i, j);
/* 235 */           out.write("(" + i + "," + cur.getColumn() + "," + cur.toString() + ")\t");
/*     */         }
/* 237 */         out.write("\n");
/*     */       }
/* 239 */       out.close();
/*     */     }
/*     */     catch (Exception e) {
/* 242 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean readBinaryMatrixFile(String filename)
/*     */   {
/* 252 */     this.isFinalized = false;
/* 253 */     byte[] data = new byte[getCellDataLength()];
/*     */     try
/*     */     {
/* 257 */       FastBinaryReader rafMatrix = new FastBinaryReader(filename);
/* 258 */       int rows = rafMatrix.readInt();
/* 259 */       this.columns = rafMatrix.readInt();
/* 260 */       int cells = rafMatrix.readInt();
/* 261 */       this.list = new ArrayList(cells);
/*     */ 
/* 264 */       for (int i = 0; i < rows; i++)
/*     */       {
/* 266 */         int row = i;
/* 267 */         rafMatrix.readInt();
/* 268 */         int num = rafMatrix.readInt();
/* 269 */         for (int j = 0; j < num; j++)
/*     */         {
/* 271 */           int column = rafMatrix.readInt();
/* 272 */           rafMatrix.read(data);
/* 273 */           add(createCell(row, column, data));
/*     */         }
/*     */       }
/* 276 */       rafMatrix.close();
/* 277 */       finalizeData(false);
/* 278 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 281 */       e.printStackTrace();
/* 282 */     }return false;
/*     */   }
/*     */ 
/*     */   protected boolean readTextMatrixFile(String filename)
/*     */   {
/* 291 */     this.isFinalized = false;
/*     */     try
/*     */     {
/* 295 */       BufferedReader br = FileUtil.getTextReader(filename);
/* 296 */       String fragment = br.readLine();
/* 297 */       int i = 0;
/* 298 */       int j = fragment.indexOf(',', i);
/* 299 */       this.rows = Integer.parseInt(fragment.substring(i, j));
/* 300 */       i = j + 1;
/* 301 */       j = fragment.indexOf(',', i);
/* 302 */       this.columns = Integer.parseInt(fragment.substring(i, j));
/* 303 */       int cells = Integer.parseInt(fragment.substring(j + 1));
/* 304 */       this.list = new ArrayList(cells);
/*     */ 
/* 307 */       int lineNo = -1;
/* 308 */       String line = br.readLine();
/* 309 */       while (line != null) {
/* 310 */         lineNo++;
/* 311 */         processLine(lineNo, line);
/* 312 */         line = br.readLine();
/*     */       }
/* 314 */       br.close();
/* 315 */       finalizeData(false);
/* 316 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 319 */       e.printStackTrace();
/* 320 */     }return false;
/*     */   }
/*     */ 
/*     */   private void processLine(int lineNo, String line)
/*     */   {
/* 329 */     int startPos = 0;
/* 330 */     int endPos = line.indexOf('\t', startPos);
/* 331 */     while (endPos > 0) {
/* 332 */       String fragment = line.substring(startPos, endPos);
/* 333 */       startPos = endPos + 1;
/* 334 */       endPos = line.indexOf('\t', startPos);
/*     */ 
/* 336 */       int i = fragment.indexOf('(') + 1;
/* 337 */       int j = fragment.indexOf(',', i);
/* 338 */       int row = Integer.parseInt(fragment.substring(i, j));
/* 339 */       i = j + 1;
/* 340 */       j = fragment.indexOf(',', i);
/* 341 */       int col = Integer.parseInt(fragment.substring(i, j));
/* 342 */       i = j + 1;
/* 343 */       j = fragment.indexOf(')', i);
/* 344 */       Cell cur = createCell(row, col, fragment.substring(i, j));
/* 345 */       add(cur);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setRowRange(int row) {
/* 350 */     this.lastAccessedRow = row;
/* 351 */     this.lastRowStart = SortedArray.binarySearch(this.list, new IntCell(row, 0), 0, this.list.size() - 1, this.coordinateComparator);
/* 352 */     if (this.lastRowStart < 0) this.lastRowStart = (this.lastRowStart * -1 - 1);
/* 353 */     this.lastRowEnd = SortedArray.binarySearch(this.list, new IntCell(row + 1, 0), this.lastRowStart, this.list.size() - 1, this.coordinateComparator);
/* 354 */     if (this.lastRowEnd < 0) this.lastRowEnd = (this.lastRowEnd * -1 - 1);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.AbstractFlatSparseMatrix
 * JD-Core Version:    0.6.2
 */