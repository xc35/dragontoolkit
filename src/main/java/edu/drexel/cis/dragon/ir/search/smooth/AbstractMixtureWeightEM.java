/*     */ package edu.drexel.cis.dragon.ir.search.smooth;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.query.Predicate;
/*     */ import edu.drexel.cis.dragon.ir.query.RelSimpleQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public abstract class AbstractMixtureWeightEM
/*     */ {
/*     */   protected IndexReader indexReader;
/*     */   protected int iterationNum;
/*     */   protected int componentNum;
/*     */   private PrintWriter statusOut;
/*     */   private boolean docFirst;
/*     */ 
/*     */   public AbstractMixtureWeightEM(IndexReader indexReader, int componentNum, int iterationNum, boolean docFirst)
/*     */   {
/*  25 */     this.indexReader = indexReader;
/*  26 */     this.iterationNum = iterationNum;
/*  27 */     this.componentNum = componentNum;
/*  28 */     this.docFirst = docFirst; } 
/*     */   protected abstract void setInitialParameters(double[] paramArrayOfDouble, IRDoc[] paramArrayOfIRDoc);
/*     */ 
/*     */   protected abstract void init(RelSimpleQuery paramRelSimpleQuery);
/*     */ 
/*     */   protected abstract void setDoc(IRDoc paramIRDoc);
/*     */ 
/*     */   protected abstract void setQueryTerm(SimpleTermPredicate paramSimpleTermPredicate);
/*     */ 
/*     */   protected abstract void getComponentValue(SimpleTermPredicate paramSimpleTermPredicate, int paramInt, double[] paramArrayOfDouble);
/*     */ 
/*     */   protected abstract void getComponentValue(IRDoc paramIRDoc, int paramInt, double[] paramArrayOfDouble);
/*     */ 
/*  39 */   public void setStatusOut(PrintWriter out) { this.statusOut = out; }
/*     */ 
/*     */   public double[] estimateModelCoefficient(RelSimpleQuery query)
/*     */   {
/*  43 */     if (this.docFirst) {
/*  44 */       return breadthFirstEstimate(query);
/*     */     }
/*  46 */     return depthFirstEstimate(query);
/*     */   }
/*     */ 
/*     */   private double[] breadthFirstEstimate(RelSimpleQuery query)
/*     */   {
/*  60 */     SimpleTermPredicate[] arrPredicate = checkSimpleTermQuery(query);
/*  61 */     init(query);
/*     */ 
/*  63 */     double[] arrPreParam = new double[this.componentNum];
/*  64 */     double[] arrParam = new double[this.componentNum];
/*  65 */     double[] arrParamDocSum = new double[this.componentNum];
/*  66 */     double[] arrComp = new double[this.componentNum];
/*     */ 
/*  68 */     int termNum = arrPredicate.length;
/*  69 */     int docNum = getDocNum();
/*  70 */     double[] arrDocWeight = new double[docNum];
/*  71 */     IRDoc[] arrDoc = new IRDoc[docNum];
/*     */ 
/*  73 */     setInitialParameters(arrPreParam, arrDoc);
/*     */ 
/*  76 */     printStatus("Estimating the coefficients of the mixed model...");
/*  77 */     for (int k = 0; k < this.iterationNum; k++) {
/*  78 */       printStatus("Iteration #" + (k + 1));
/*  79 */       double allDocSum = 0.0D;
/*  80 */       for (int m = 0; m < this.componentNum; m++) arrParam[m] = 0.0D;
/*     */ 			int m;
/*  82 */       for (int i = 0; i < docNum; i++) {
/*  83 */         double docSum = arrDoc[i].getWeight();
/*  84 */         for ( m = 0; m < this.componentNum; m++) arrParamDocSum[m] = 0.0D;
/*  85 */         setDoc(arrDoc[i]);
/*     */ 
/*  87 */         for (int j = 0; j < termNum; j++) {
/*  88 */           IRTerm docTerm = this.indexReader.getIRTerm(arrPredicate[j].getIndex(), i);
/*  89 */           getComponentValue(arrPredicate[j], docTerm.getFrequency(), arrComp);
/*  90 */           double termProb = 0.0D;
/*  91 */           for (m = 0; m < this.componentNum; m++) {
/*  92 */             arrPreParam[m] *= arrComp[m];
/*  93 */             termProb += arrComp[m];
/*     */           }
/*  95 */           docSum *= termProb;
/*  96 */           for (m = 0; m < this.componentNum; m++) arrParamDocSum[m] += arrComp[m] / termProb;
/*     */         }
/*  98 */         for (m = 0; m < this.componentNum; m++) arrParam[m] += arrDoc[i].getWeight() * arrParamDocSum[m];
/*  99 */         arrDocWeight[i] = docSum;
/* 100 */         allDocSum += arrDocWeight[i];
/*     */       }
/* 102 */       for (m = 0; m < this.componentNum; m++)
/*     */       {
/* 104 */         arrParam[m] /= termNum;
/* 105 */         printStatus("Coefficient #" + (m + 1) + " " + arrPreParam[m]);
/*     */       }
/* 107 */       for (m = 0; m < docNum; m++)
/* 108 */         arrDoc[m].setWeight(arrDocWeight[m] / allDocSum);
/*     */     }
/* 110 */     printStatus("");
/* 111 */     return arrPreParam;
/*     */   }
/*     */ 
/*     */   private double[] depthFirstEstimate(RelSimpleQuery query)
/*     */   {
/* 124 */     SimpleTermPredicate[] arrPredicate = checkSimpleTermQuery(query);
/* 125 */     init(query);
/*     */ 
/* 127 */     double[] arrPreParam = new double[this.componentNum];
/* 128 */     double[] arrParam = new double[this.componentNum];
/* 129 */     double[] arrComp = new double[this.componentNum];
/*     */ 
/* 131 */     int termNum = arrPredicate.length;
/* 132 */     int docNum = getDocNum();
/* 133 */     double[] arrDocWeight = new double[docNum];
/* 134 */     IRDoc[] arrDoc = new IRDoc[docNum];
/*     */ 
/* 136 */     setInitialParameters(arrPreParam, arrDoc);
/*     */ 
/* 138 */     printStatus("Estimating the coefficients of the mixed model...");
/* 139 */     for (int count = 0; count < this.iterationNum; count++) {
/* 140 */       printStatus("Iteration #" + (count + 1));
/* 141 */       for (int i = 0; i < docNum; i++) arrDocWeight[i] = arrDoc[i].getWeight();
/* 142 */       for (int m = 0; m < this.componentNum; m++) arrParam[m] = 0.0D;
/*     */ 		int m, i;
/* 144 */       for (i = 0; i < arrPredicate.length; i++) {
/* 145 */         setQueryTerm(arrPredicate[i]);
/* 146 */         int[] arrIndex = this.indexReader.getTermDocIndexList(arrPredicate[i].getIndex());
/* 147 */         int[] arrFreq = this.indexReader.getTermDocFrequencyList(arrPredicate[i].getIndex());
/* 148 */         int k = 0;
/* 149 */         for (int j = 0; j < arrIndex.length; j++) {
/* 150 */           while (k < arrIndex[j]) {
/* 151 */             getComponentValue(arrDoc[k], 0, arrComp);
/* 152 */             double termProb = 0.0D;
/* 153 */             for (m = 0; m < this.componentNum; m++) {
/* 154 */               arrPreParam[m] *= arrComp[m];
/* 155 */               termProb += arrComp[m];
/*     */             }
/* 157 */             arrDocWeight[k] *= termProb;
/* 158 */             for (m = 0; m < this.componentNum; m++) arrParam[m] += arrDoc[k].getWeight() * arrComp[m] / termProb;
/* 159 */             k++;
/*     */           }
/*     */ 
/* 162 */           getComponentValue(arrDoc[k], arrFreq[j], arrComp);
/* 163 */           double termProb = 0.0D;
/* 164 */           for (m = 0; m < this.componentNum; m++) {
/* 165 */             arrPreParam[m] *= arrComp[m];
/* 166 */             termProb += arrComp[m];
/*     */           }
/* 168 */           arrDocWeight[k] *= termProb;
/* 169 */           for (m = 0; m < this.componentNum; m++) arrParam[m] += arrDoc[k].getWeight() * arrComp[m] / termProb;
/* 170 */           k++;
/*     */         }
/* 172 */         while (k < docNum) {
/* 173 */           getComponentValue(arrDoc[k], 0, arrComp);
/* 174 */           double termProb = 0.0D;
/* 175 */           for (m = 0; m < this.componentNum; m++) {
/* 176 */             arrPreParam[m] *= arrComp[m];
/* 177 */             termProb += arrComp[m];
/*     */           }
/* 179 */           arrDocWeight[k] *= termProb;
/* 180 */           for (m = 0; m < this.componentNum; m++) arrParam[m] += arrDoc[k].getWeight() * arrComp[m] / termProb;
/* 181 */           k++;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 186 */       for (m = 0; m < this.componentNum; m++)
/*     */       {
/* 188 */         arrParam[m] /= termNum;
/* 189 */         printStatus("Coefficient #" + (m + 1) + " " + arrPreParam[m]);
/*     */       }
/* 191 */       double allSum = 0.0D;
/* 192 */       for (i = 0; i < docNum; i++) allSum += arrDocWeight[i];
/* 193 */       for (i = 0; i < docNum; i++) arrDoc[i].setWeight(arrDocWeight[i] / allSum);
/*     */     }
/*     */ 
/* 196 */     printStatus("");
/* 197 */     return arrPreParam;
/*     */   }
/*     */ 
/*     */   protected int getDocNum()
/*     */   {
/* 202 */     return this.indexReader.getCollection().getDocNum();
/*     */   }
/*     */ 
/*     */   protected IRDoc getDoc(int seq) {
/* 206 */     return this.indexReader.getDoc(seq);
/*     */   }
/*     */ 
/*     */   private void printStatus(String line) {
/*     */     try {
/* 211 */       System.out.println(line);
/* 212 */       if (this.statusOut != null) {
/* 213 */         this.statusOut.write(line + "\n");
/* 214 */         this.statusOut.flush();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 218 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private SimpleTermPredicate[] checkSimpleTermQuery(RelSimpleQuery query)
/*     */   {
/* 228 */     ArrayList list = new ArrayList();
/* 229 */     for (int i = 0; i < query.getChildNum(); i++) {
/* 230 */       if (((Predicate)query.getChild(i)).isTermPredicate()) {
/* 231 */         SimpleTermPredicate predicate = (SimpleTermPredicate)query.getChild(i);
/* 232 */         if (predicate.getDocFrequency() == 0) {
/* 233 */           IRTerm curIRTerm = this.indexReader.getIRTerm(predicate.getKey());
/* 234 */           if (curIRTerm != null) {
/* 235 */             predicate.setDocFrequency(curIRTerm.getDocFrequency());
/* 236 */             predicate.setFrequency(curIRTerm.getFrequency());
/* 237 */             predicate.setIndex(curIRTerm.getIndex());
/*     */           }
/*     */         }
/* 240 */         if (predicate.getDocFrequency() > 0) {
/* 241 */           list.add(predicate);
/*     */         }
/*     */       }
/*     */     }
/* 245 */     SimpleTermPredicate[] arrPredicate = new SimpleTermPredicate[list.size()];
/* 246 */     for (int i = 0; i < list.size(); i++) {
/* 247 */       arrPredicate[i] = ((SimpleTermPredicate)list.get(i)).copy();
/*     */     }
/* 249 */     return arrPredicate;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.smooth.AbstractMixtureWeightEM
 * JD-Core Version:    0.6.2
 */