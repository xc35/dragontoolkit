/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryWriter;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class IntFlatDenseMatrix extends AbstractDenseMatrix
/*     */   implements IntDenseMatrix, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private int[][] arrMatrix;
/*     */ 
/*     */   public IntFlatDenseMatrix(int[][] newMatrix)
/*     */   {
/*  20 */     super(newMatrix.length, newMatrix[0].length, 4);
/*  21 */     this.arrMatrix = newMatrix;
/*     */   }
/*     */ 
/*     */   public IntFlatDenseMatrix(int row, int column) {
/*  25 */     super(row, column, 4);
/*  26 */     this.arrMatrix = new int[row][column];
/*     */   }
/*     */ 
/*     */   public IntFlatDenseMatrix(String filename) {
/*  30 */     super(-1, -1, 4);
/*  31 */     readTextMatrixFile(filename);
/*     */   }
/*     */ 
/*     */   public IntFlatDenseMatrix(String filename, boolean binaryFile) {
/*  35 */     super(-1, -1, 4);
/*  36 */     if (binaryFile)
/*  37 */       readBinMatrixFile(filename);
/*     */     else
/*  39 */       readTextMatrixFile(filename);
/*     */   }
/*     */ 
/*     */   public void assign(int val)
/*     */   {
/*  45 */     for (int i = 0; i < this.rows; i++)
/*  46 */       for (int j = 0; j < this.columns; j++)
/*  47 */         this.arrMatrix[i][j] = val;
/*     */   }
/*     */ 
/*     */   public boolean add(int row, int column, int score) {
/*  51 */     if ((row >= this.rows) || (column >= this.columns)) {
/*  52 */       return false;
/*     */     }
/*  54 */     this.arrMatrix[row][column] += score;
/*  55 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean setInt(int row, int column, int score)
/*     */   {
/*  61 */     if ((row >= this.rows) || (column >= this.columns)) {
/*  62 */       return false;
/*     */     }
/*  64 */     this.arrMatrix[row][column] = score;
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean setInt(int row, int[] scores)
/*     */   {
/*  70 */     if ((row >= this.rows) || (scores.length != this.columns))
/*  71 */       return false;
/*  72 */     System.arraycopy(scores, 0, this.arrMatrix[row], 0, this.columns);
/*  73 */     return true;
/*     */   }
/*     */ 
/*     */   public long getRowSum(int row)
/*     */   {
/*  80 */     long sum = 0L;
/*  81 */     for (int i = 0; i < this.columns; i++) {
/*  82 */       sum += this.arrMatrix[row][i];
/*     */     }
/*  84 */     return sum;
/*     */   }
/*     */ 
/*     */   public long getColumnSum(int column)
/*     */   {
/*  91 */     long sum = 0L;
/*  92 */     for (int i = 0; i < this.rows; i++) {
/*  93 */       sum += this.arrMatrix[i][column];
/*     */     }
/*  95 */     return sum;
/*     */   }
/*     */ 
/*     */   public int getInt(int row, int column) {
/*  99 */     return this.arrMatrix[row][column];
/*     */   }
/*     */ 
/*     */   public double getDouble(int row, int column) {
/* 103 */     return this.arrMatrix[row][column];
/*     */   }
/*     */ 
/*     */   public void saveTo(String filename, boolean binary)
/*     */   {
/* 110 */     if (!binary) {
/* 111 */       saveTo(filename);
/* 112 */       return;
/*     */     }
/* 114 */     FastBinaryWriter fastBinWriter = new FastBinaryWriter(filename);
/*     */     try {
/* 116 */       fastBinWriter.writeInt(this.rows);
/* 117 */       fastBinWriter.writeInt(this.columns);
/* 118 */       fastBinWriter.writeInt(this.rows * this.columns);
/*     */ 
/* 120 */       for (int i = 0; i < this.rows; i++) {
/* 121 */         for (int j = 0; j < this.columns; j++) {
/* 122 */           fastBinWriter.writeInt(this.arrMatrix[i][j]);
/*     */         }
/* 124 */         if (i % 100 == 0)
/* 125 */           fastBinWriter.flush();
/*     */       }
/* 127 */       fastBinWriter.flush();
/* 128 */       fastBinWriter.close();
/*     */     } catch (Exception ex) {
/* 130 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void saveTo(String filename)
/*     */   {
/* 136 */     print(FileUtil.getPrintWriter(filename));
/*     */   }
/*     */ 
/*     */   public void print(PrintWriter out)
/*     */   {
/*     */     try
/*     */     {
/* 143 */       out.write(String.valueOf(this.rows) + "," + String.valueOf(this.columns) + "," + String.valueOf(this.rows * this.columns) + 
/* 144 */         "\n");
/*     */ 
/* 146 */       for (int i = 0; i < this.rows; i++) {
/* 147 */         for (int j = 0; j < this.columns; j++)
/* 148 */           out.write(String.valueOf(this.arrMatrix[i][j]) + ",");
/* 149 */         out.write("\n");
/* 150 */         if (i % 100 == 0)
/* 151 */           out.flush();
/*     */       }
/* 153 */       out.flush();
/* 154 */       out.close();
/*     */     }
/*     */     catch (Exception e) {
/* 157 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readTextMatrixFile(String fileName)
/*     */   {
/* 166 */     BufferedReader br = FileUtil.getTextReader(fileName);
/*     */     try {
/* 168 */       String line = br.readLine();
/* 169 */       String[] arrLine = line.split(",");
/*     */ 
/* 171 */       this.rows = Integer.parseInt(arrLine[0]);
/* 172 */       this.columns = Integer.parseInt(arrLine[1]);
/* 173 */       this.arrMatrix = new int[this.rows][this.columns];
/*     */ 
/* 175 */       int i = 0;
/* 176 */       while ((line = br.readLine()) != null) {
/* 177 */         arrLine = line.split(",");
/* 178 */         for (int j = 0; j < this.arrMatrix[0].length; j++)
/* 179 */           this.arrMatrix[i][j] = Integer.parseInt(arrLine[j]);
/* 180 */         i++;
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 184 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readBinMatrixFile(String fileName)
/*     */   {
/* 191 */     FastBinaryReader fbr = new FastBinaryReader(fileName);
/*     */     try {
/* 193 */       this.rows = fbr.readInt();
/* 194 */       this.columns = fbr.readInt();
/* 195 */       this.arrMatrix = new int[this.rows][this.columns];
/*     */ 
/* 197 */       fbr.readInt();
/* 198 */       for (int i = 0; i < this.arrMatrix.length; i++)
/* 199 */         for (int j = 0; j < this.arrMatrix[0].length; j++)
/* 200 */           this.arrMatrix[i][j] = fbr.readInt();
/* 201 */       fbr.close();
/*     */     }
/*     */     catch (Exception ex) {
/* 204 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 210 */     this.rows = 0;
/* 211 */     this.columns = 0;
/* 212 */     this.arrMatrix = null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.IntFlatDenseMatrix
 * JD-Core Version:    0.6.2
 */