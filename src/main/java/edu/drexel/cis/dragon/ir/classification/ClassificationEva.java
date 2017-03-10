/*     */ package edu.drexel.cis.dragon.ir.classification;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.util.FormatUtil;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.DecimalFormat;
/*     */ 
/*     */ public class ClassificationEva
/*     */ {
/*     */   private double[] arrPrecision;
/*     */   private double[] arrRecall;
/*     */   private double[] arrF1;
/*     */   private double[] arrPn;
/*     */   private double precision;
/*     */   private double recall;
/*     */   private double f1;
/*     */   private double mrr;
/*     */ 
/*     */   public boolean evaluate(int classNum, int[] human, int[][] machine)
/*     */   {
/*  29 */     if (human.length != machine.length) {
/*  30 */       return false;
/*     */     }
/*  32 */     this.mrr = 0.0D;
/*  33 */     int count = 0;
/*  34 */     this.arrPn = new double[classNum];
/*  35 */     DocClassSet humanSet = new DocClassSet(classNum);
/*  36 */     DocClassSet machineSet = new DocClassSet(classNum);
/*     */ 		int j;
/*  38 */     for (int i = 0; i < human.length; i++) {
/*  39 */       humanSet.addDoc(human[i], new IRDoc(i));
/*  40 */       int[] rank = machine[i];
/*  41 */       if (rank != null)
/*     */       {
/*  44 */         machineSet.addDoc(rank[0], new IRDoc(i));
/*  45 */         count++;
/*  46 */         for ( j = 0; (j < classNum) && (rank[j] != human[i]); j++);
/*  47 */         this.mrr += 1.0D / (j + 1);
/*  48 */         for (; j < classNum; j++)
/*  49 */           this.arrPn[j] += 1.0D; 
/*     */       }
/*     */     }
/*  51 */     this.mrr /= count;
/*  52 */     for (int i = 0; i < classNum; i++) {
/*  53 */       this.arrPn[i] /= count;
/*     */     }
/*  55 */     return evaluate(humanSet, machineSet);
/*     */   }
/*     */ 
/*     */   public boolean evaluate(DocClassSet human, DocClassSet machine)
/*     */   {
/*  63 */     if (human.getClassNum() != machine.getClassNum()) return false;
/*     */ 
/*  65 */     int humanTotal = 0;
/*  66 */     int machineTotal = 0;
/*  67 */     int totalCount = 0;
/*  68 */     this.arrPrecision = new double[human.getClassNum()];
/*  69 */     this.arrRecall = new double[human.getClassNum()];
/*  70 */     this.arrF1 = new double[human.getClassNum()];
/*     */ 
/*  72 */     for (int i = 0; i < human.getClassNum(); i++) {
/*  73 */       int[] arrResult = matchDocs(human.getDocClass(i), machine.getDocClass(i));
/*  74 */       if (arrResult[1] == 0)
/*  75 */         this.arrPrecision[i] = 0.0D;
/*     */       else
/*  77 */         this.arrPrecision[i] = (arrResult[0] / arrResult[1]);
/*  78 */       if (arrResult[2] == 0)
/*  79 */         this.arrRecall[i] = 0.0D;
/*     */       else
/*  81 */         this.arrRecall[i] = (arrResult[0] / arrResult[2]);
/*  82 */       if ((this.arrPrecision[i] == 0.0D) || (this.arrRecall[i] == 0.0D))
/*  83 */         this.arrF1[i] = 0.0D;
/*     */       else
/*  85 */         this.arrF1[i] = (2.0D * this.arrPrecision[i] * this.arrRecall[i] / (this.arrPrecision[i] + this.arrRecall[i]));
/*  86 */       humanTotal += arrResult[2];
/*  87 */       machineTotal += arrResult[1];
/*  88 */       totalCount += arrResult[0];
/*     */     }
/*  90 */     this.precision = (totalCount / machineTotal);
/*  91 */     this.recall = (totalCount / humanTotal);
/*  92 */     if ((this.precision == 0.0D) || (this.recall == 0.0D))
/*  93 */       this.f1 = 0.0D;
/*     */     else {
/*  95 */       this.f1 = (2.0D * this.precision * this.recall / (this.precision + this.recall));
/*     */     }
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */   public double getPrecision(int classID) {
/* 101 */     return this.arrPrecision[classID];
/*     */   }
/*     */ 
/*     */   public double getRecall(int classID) {
/* 105 */     return this.arrRecall[classID];
/*     */   }
/*     */ 
/*     */   public double getFscore(int classID) {
/* 109 */     return this.arrF1[classID];
/*     */   }
/*     */ 
/*     */   public double getMicroPrecision() {
/* 113 */     return this.precision;
/*     */   }
/*     */ 
/*     */   public double getMicroRecall() {
/* 117 */     return this.recall;
/*     */   }
/*     */ 
/*     */   public double getMicroFScore() {
/* 121 */     return this.f1;
/*     */   }
/*     */ 
/*     */   public double getMacroPrecision() {
/* 125 */     return MathUtil.average(this.arrPrecision);
/*     */   }
/*     */ 
/*     */   public double getMacroRecall() {
/* 129 */     return MathUtil.average(this.arrRecall);
/*     */   }
/*     */ 
/*     */   public double getMacroFScore() {
/* 133 */     return MathUtil.average(this.arrF1);
/*     */   }
/*     */ 
/*     */   public double getMRR() {
/* 137 */     return this.mrr;
/*     */   }
/*     */ 
/*     */   public double getPrecisionN(int top) {
/* 141 */     if ((this.arrPn == null) || (top >= this.arrPn.length)) {
/* 142 */       return 0.0D;
/*     */     }
/* 144 */     return this.arrPn[top];
/*     */   }
/*     */ 
/*     */   public void print(PrintWriter out)
/*     */   {
/*     */     try
/*     */     {
/* 152 */       DecimalFormat df1 = FormatUtil.getNumericFormat(2, 0);
/* 153 */       DecimalFormat df2 = FormatUtil.getNumericFormat(1, 2);
/*     */ 
/* 155 */       for (int i = 0; i < this.arrPrecision.length; i++) {
/* 156 */         out.write("Class #" + df1.format(i) + ": ");
/* 157 */         out.write(df2.format(this.arrPrecision[i] * 100.0D) + "%/" + df2.format(this.arrRecall[i] * 100.0D) + "%\n");
/*     */       }
/* 159 */       out.write("Overall: ");
/* 160 */       out.write(df2.format(this.precision * 100.0D) + "%/" + df2.format(this.recall * 100.0D) + "%\n");
/* 161 */       out.flush();
/*     */     }
/*     */     catch (Exception e) {
/* 164 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private int[] matchDocs(DocClass human, DocClass machine)
/*     */   {
/* 173 */     int humanNum = human.getDocNum();
/* 174 */     int machineNum = machine.getDocNum();
/* 175 */     int i = 0;
/* 176 */     int j = 0;
/* 177 */     int correct = 0;
/* 178 */     while ((i < humanNum) && (j < machineNum)) {
/* 179 */       int humanIndex = human.getDoc(i).getIndex();
/* 180 */       int machineIndex = machine.getDoc(j).getIndex();
/* 181 */       if (humanIndex == machineIndex) {
/* 182 */         correct++;
/* 183 */         i++;
/* 184 */         j++;
/*     */       }
/* 186 */       else if (humanIndex < machineIndex) {
/* 187 */         i++;
/*     */       }
/*     */       else {
/* 190 */         j++;
/*     */       }
/*     */     }
/* 193 */     int[] arrResult = new int[3];
/* 194 */     arrResult[0] = correct;
/* 195 */     arrResult[1] = machineNum;
/* 196 */     arrResult[2] = humanNum;
/* 197 */     return arrResult;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.ClassificationEva
 * JD-Core Version:    0.6.2
 */