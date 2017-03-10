/*     */ package edu.drexel.cis.dragon.matrix;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryWriter;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class DoubleFlatDenseMatrix extends AbstractDenseMatrix
/*     */   implements DoubleDenseMatrix, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private double[][] arrMatrix;
/*     */ 
/*     */   public DoubleFlatDenseMatrix(double[][] newMatrix)
/*     */   {
/*  20 */     super(newMatrix.length, newMatrix[0].length, 8);
/*  21 */     this.arrMatrix = newMatrix;
/*     */   }
/*     */ 
/*     */   public DoubleFlatDenseMatrix(int row, int column) {
/*  25 */     super(row, column, 8);
/*  26 */     this.arrMatrix = new double[row][column];
/*     */   }
/*     */ 
/*     */   public DoubleFlatDenseMatrix(String filename) {
/*  30 */     super(-1, -1, 8);
/*  31 */     readTextMatrixFile(filename);
/*     */   }
/*     */ 
/*     */   public DoubleFlatDenseMatrix(String filename, boolean binaryFile) {
/*  35 */     super(-1, -1, 8);
/*  36 */     if (binaryFile)
/*  37 */       readBinMatrixFile(filename);
/*     */     else
/*  39 */       readTextMatrixFile(filename);
/*     */   }
/*     */ 
/*     */   public void assign(double val)
/*     */   {
/*  45 */     for (int i = 0; i < this.rows; i++)
/*  46 */       for (int j = 0; j < this.columns; j++)
/*  47 */         this.arrMatrix[i][j] = val;
/*     */   }
/*     */ 
/*     */   public boolean add(int row, int column, double score) {
/*  51 */     if ((row >= this.rows) || (column >= this.columns)) {
/*  52 */       return false;
/*     */     }
/*  54 */     this.arrMatrix[row][column] += score;
/*  55 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean setDouble(int row, int column, double score)
/*     */   {
/*  60 */     if ((row >= this.rows) || (column >= this.columns)) {
/*  61 */       return false;
/*     */     }
/*  63 */     this.arrMatrix[row][column] = score;
/*  64 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean setDouble(int row, double[] scores)
/*     */   {
/*  69 */     if ((row >= this.rows) || (scores.length != this.columns))
/*  70 */       return false;
/*  71 */     System.arraycopy(scores, 0, this.arrMatrix[row], 0, this.columns);
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */   public double getRowSum(int row)
/*     */   {
/*  79 */     double sum = 0.0D;
/*  80 */     for (int i = 0; i < this.columns; i++) {
/*  81 */       sum += this.arrMatrix[row][i];
/*     */     }
/*  83 */     return sum;
/*     */   }
/*     */ 
/*     */   public double getColumnSum(int column)
/*     */   {
/*  90 */     double sum = 0.0D;
/*  91 */     for (int i = 0; i < this.rows; i++) {
/*  92 */       sum += this.arrMatrix[i][column];
/*     */     }
/*  94 */     return sum;
/*     */   }
/*     */ 
/*     */   public int getInt(int row, int column) {
/*  98 */     return (int)this.arrMatrix[row][column];
/*     */   }
/*     */ 
/*     */   public double getDouble(int row, int column) {
/* 102 */     return this.arrMatrix[row][column];
/*     */   }
/*     */ 
/*     */   public void saveTo(String filename, boolean binary)
/*     */   {
/* 109 */     if (!binary) {
/* 110 */       saveTo(filename);
/* 111 */       return;
/*     */     }
/* 113 */     FastBinaryWriter fastBinWriter = new FastBinaryWriter(filename);
/*     */     try {
/* 115 */       fastBinWriter.writeInt(this.rows);
/* 116 */       fastBinWriter.writeInt(this.columns);
/* 117 */       fastBinWriter.writeInt(this.rows * this.columns);
/*     */ 
/* 119 */       for (int i = 0; i < this.rows; i++) {
/* 120 */         for (int j = 0; j < this.columns; j++) {
/* 121 */           fastBinWriter.writeDouble(this.arrMatrix[i][j]);
/*     */         }
/* 123 */         if (i % 100 == 0)
/* 124 */           fastBinWriter.flush();
/*     */       }
/* 126 */       fastBinWriter.flush();
/* 127 */       fastBinWriter.close();
/*     */     } catch (Exception ex) {
/* 129 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void saveTo(String filename)
/*     */   {
/* 135 */     print(FileUtil.getPrintWriter(filename));
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
/* 173 */       this.arrMatrix = new double[this.rows][this.columns];
/*     */ 
/* 175 */       int i = 0;
/* 176 */       while ((line = br.readLine()) != null) {
/* 177 */         arrLine = line.split(",");
/* 178 */         for (int j = 0; j < this.arrMatrix[0].length; j++)
/* 179 */           this.arrMatrix[i][j] = Double.parseDouble(arrLine[j]);
/* 180 */         i++;
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 185 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readBinMatrixFile(String fileName)
/*     */   {
/* 193 */     FastBinaryReader fbr = new FastBinaryReader(fileName);
/*     */     try {
/* 195 */       this.rows = fbr.readInt();
/* 196 */       this.columns = fbr.readInt();
/* 197 */       this.arrMatrix = new double[this.rows][this.columns];
/*     */ 
/* 199 */       fbr.readInt();
/* 200 */       for (int i = 0; i < this.rows; i++)
/* 201 */         for (int j = 0; j < this.columns; j++)
/* 202 */           this.arrMatrix[i][j] = fbr.readDouble();
/* 203 */       fbr.close();
/*     */     }
/*     */     catch (Exception ex) {
/* 206 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() {
/* 211 */     this.rows = 0;
/* 212 */     this.columns = 0;
/* 213 */     this.arrMatrix = null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.matrix.DoubleFlatDenseMatrix
 * JD-Core Version:    0.6.2
 */