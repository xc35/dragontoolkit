/*     */ package edu.drexel.cis.dragon.ir.kngbase;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSuperSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntGiantSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import edu.drexel.cis.dragon.util.PairIndex;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class TranslationModel
/*     */ {
/*     */   protected IntSparseMatrix source;
/*     */   protected IntSparseMatrix target;
/*     */   protected PairIndex pairIndex;
/*     */   protected int iterations;
/*     */ 
/*     */   public TranslationModel(IntSparseMatrix source, IntSparseMatrix target)
/*     */   {
/*  35 */     this(source, target, null);
/*     */   }
/*     */ 
/*     */   public TranslationModel(IntSparseMatrix source, IntSparseMatrix target, IntSparseMatrix pairMatrix)
/*     */   {
/*  47 */     this.iterations = 10;
/*  48 */     this.source = source;
/*  49 */     this.target = target;
/*  50 */     this.pairIndex = new PairIndex(pairMatrix);
/*     */   }
/*     */ 
/*     */   public void translate(String matrixPath, String matrixKey)
/*     */   {
/*  58 */     if (this.pairIndex == null)
/*  59 */       this.pairIndex = new PairIndex(genCooccurMatrix(this.source, this.target));
/*  60 */     double[] probBuf = new double[1000];
/*  61 */     int[] indexBuf = new int[1000];
/*  62 */     double[] trans = new double[this.pairIndex.pairs()];
/*  63 */     double[] transTmp = new double[this.pairIndex.pairs()];
/*     */ 
/*  66 */     initialize(trans);
/*     */ 
/*  69 */     for (int k = 0; k < this.iterations; k++) {
/*  70 */       int count = 0;
/*  71 */       for (int i = 0; i < this.source.rows(); i++) {
/*  72 */         if (count++ % 1000 == 0) {
/*  73 */           System.out.println(new Date().toString() + " training: iteration " + (k + 1) + " pair #" + count);
/*     */         }
/*  75 */         if ((this.source.getNonZeroNumInRow(i) != 0) && (this.target.getNonZeroNumInRow(i) != 0))
/*     */         {
/*  78 */           int[] arrSourceIndex = this.source.getNonZeroColumnsInRow(i);
/*  79 */           int[] arrSourceFreq = this.source.getNonZeroIntScoresInRow(i);
/*  80 */           int[] arrTargetIndex = this.target.getNonZeroColumnsInRow(i);
/*  81 */           int[] arrTargetFreq = this.target.getNonZeroIntScoresInRow(i);
/*  82 */           int len = arrSourceIndex.length;
/*  83 */           if (probBuf.length < len) {
/*  84 */             probBuf = new double[len];
/*  85 */             indexBuf = new int[len];
/*     */           }
/*     */ 
/*  88 */           for (int j = 0; j < arrTargetIndex.length; j++)
/*     */           {
/*  90 */             double sum = 0.0D;
/*  91 */             for (int l = 0; l < len; l++) {
/*  92 */               indexBuf[l] = this.pairIndex.getPairIndex(arrSourceIndex[l], arrTargetIndex[j]);
/*  93 */               probBuf[l] = trans[indexBuf[l]];
/*  94 */               sum += probBuf[l] * arrSourceFreq[l];
/*     */             }
/*     */ 
/*  98 */             for (int l = 0; l < len; l++) {
/*  99 */               transTmp[indexBuf[l]] += probBuf[l] / sum * arrTargetFreq[j] * arrSourceFreq[l];
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 104 */       for (int i = 0; i < this.pairIndex.rows(); i++) {
/* 105 */         int start = this.pairIndex.getRowStart(i);
/* 106 */         int len = this.pairIndex.getRowLength(i);
/* 107 */         double sum = 0.0D;
/* 108 */         for (int j = 0; j < len; j++)
/* 109 */           sum += transTmp[(start + j)];
/* 110 */         for (int j = 0; j < len; j++) {
/* 111 */           transTmp[(start + j)] /= sum;
/* 112 */           transTmp[(start + j)] = 0.0D;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 118 */     genMatrix(trans, matrixPath, matrixKey);
/*     */   }
/*     */ 
/*     */   protected IntSparseMatrix genCooccurMatrix(IntSparseMatrix source, IntSparseMatrix target)
/*     */   {
/* 125 */     System.out.println(new Date().toString() + " generating source-target cooccurrence matrix...");
/* 126 */     String matrixFolder = System.getProperty("user.dir");
/* 127 */     String matrixKey = "tmp_src_target_cooccur";
/* 128 */     CooccurrenceGenerator generator = new CooccurrenceGenerator();
/* 129 */     generator.generate(source, target, matrixFolder, matrixKey);
/* 130 */     System.out.println(new Date().toString() + "source-target cooccurrence matrix generated!");
/* 131 */     return new IntGiantSparseMatrix(matrixFolder + "/" + matrixKey + ".index", matrixFolder + "/" + matrixKey + ".matrix");
/*     */   }
/*     */ 
/*     */   protected void initialize(double[] trans)
/*     */   {
/* 138 */     for (int i = 0; i < this.pairIndex.rows(); i++) {
/* 139 */       int start = this.pairIndex.getRowStart(i);
/* 140 */       int len = this.pairIndex.getRowLength(i);
/* 141 */       double score = 1.0D / len;
/* 142 */       for (int j = 0; j < len; j++)
/* 143 */         trans[(start + j)] = score;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void genMatrix(double[] trans, String matrixPath, String matrixKey)
/*     */   {
/* 153 */     System.out.println(new Date().toString() + " saving translation probabilities to disk...");
/*     */ 
/* 155 */     String transIndexFile = matrixPath + "/" + matrixKey + ".index";
/* 156 */     String transMatrixFile = matrixPath + "/" + matrixKey + ".matrix";
/* 157 */     String transTIndexFile = matrixPath + "/" + matrixKey + "t.index";
/* 158 */     String transTMatrixFile = matrixPath + "/" + matrixKey + "t.matrix";
/* 159 */     File file = new File(transMatrixFile);
/* 160 */     if (file.exists()) file.delete();
/* 161 */     file = new File(transIndexFile);
/* 162 */     if (file.exists()) file.delete();
/* 163 */     file = new File(transTMatrixFile);
/* 164 */     if (file.exists()) file.delete();
/* 165 */     file = new File(transTIndexFile);
/* 166 */     if (file.exists()) file.delete();
/*     */ 
/* 168 */     DoubleSuperSparseMatrix outputTransMatrix = new DoubleSuperSparseMatrix(transIndexFile, transMatrixFile, false, false);
/* 169 */     outputTransMatrix.setFlushInterval(2147483647);
/* 170 */     DoubleSuperSparseMatrix outputTransTMatrix = new DoubleSuperSparseMatrix(transTIndexFile, transTMatrixFile, false, false);
/* 171 */     outputTransTMatrix.setFlushInterval(2147483647);
/* 172 */     int cellNum = 0;
/*     */ 
/* 174 */     for (int i = 0; i < this.pairIndex.rows(); i++) {
/* 175 */       int len = this.pairIndex.getRowLength(i);
/* 176 */       int start = this.pairIndex.getRowStart(i);
/* 177 */       for (int j = 0; j < len; j++) {
/* 178 */         outputTransMatrix.add(i, this.pairIndex.getColumn(start + j), trans[(start + j)]);
/* 179 */         outputTransTMatrix.add(this.pairIndex.getColumn(start + j), i, trans[(start + j)]);
/*     */       }
/* 181 */       cellNum += len;
/* 182 */       if (cellNum >= 5000000) {
/* 183 */         outputTransTMatrix.flush();
/* 184 */         outputTransMatrix.flush();
/* 185 */         cellNum = 0;
/*     */       }
/*     */     }
/*     */ 
/* 189 */     outputTransTMatrix.finalizeData();
/* 190 */     outputTransTMatrix.close();
/* 191 */     outputTransMatrix.finalizeData();
/* 192 */     outputTransMatrix.close();
/*     */ 
/* 194 */     System.out.println(new Date().toString() + " done!");
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.kngbase.TranslationModel
 * JD-Core Version:    0.6.2
 */