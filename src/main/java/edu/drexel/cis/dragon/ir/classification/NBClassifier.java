/*     */ package edu.drexel.cis.dragon.ir.classification;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.FeatureSelector;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.DenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleFlatDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.Row;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ 
/*     */ public class NBClassifier extends AbstractClassifier
/*     */ {
/*     */   protected DoubleFlatDenseMatrix model;
/*     */   protected DoubleVector classPrior;
/*     */   protected DoubleVector lastClassProb;
/*     */   private int[] rank;
/*     */ 
/*     */   public NBClassifier(String modelFile)
/*     */   {
/*     */     try
/*     */     {
/*  28 */       ObjectInputStream oin = new ObjectInputStream(new FileInputStream(modelFile));
/*  29 */       this.model = ((DoubleFlatDenseMatrix)oin.readObject());
/*  30 */       this.classPrior = ((DoubleVector)oin.readObject());
/*  31 */       this.classNum = this.classPrior.size();
/*  32 */       this.featureSelector = ((FeatureSelector)oin.readObject());
/*  33 */       this.arrLabel = new String[this.model.rows()];
/*  34 */       for (int i = 0; i < this.arrLabel.length; i++)
/*  35 */         this.arrLabel[i] = ((String)oin.readObject());
/*     */     }
/*     */     catch (Exception e) {
/*  38 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NBClassifier(IndexReader indexReader) {
/*  43 */     super(indexReader);
/*     */   }
/*     */ 
/*     */   public NBClassifier(SparseMatrix doctermMatrix) {
/*  47 */     super(doctermMatrix);
/*     */   }
/*     */ 
/*     */   public NBClassifier(DenseMatrix doctermMatrix) {
/*  51 */     super(doctermMatrix);
/*     */   }
/*     */ 
/*     */   public void train(DocClassSet trainingDocSet)
/*     */   {
/*  61 */     if ((this.indexReader == null) && (this.sparseMatrix == null)) {
/*  62 */       return;
/*     */     }
/*  64 */     this.classNum = trainingDocSet.getClassNum();
/*  65 */     this.classPrior = getClassPrior(trainingDocSet);
/*  66 */     trainFeatureSelector(trainingDocSet);
/*  67 */     this.arrLabel = new String[this.classNum];
/*  68 */     for (int i = 0; i < this.classNum; i++)
/*  69 */       this.arrLabel[i] = trainingDocSet.getDocClass(i).getClassName();
/*  70 */     this.model = new DoubleFlatDenseMatrix(this.classNum, this.featureSelector.getSelectedFeatureNum());
/*  71 */     this.model.assign(1.0D);
/*  72 */     for (int i = 0; i < this.classNum; i++) {
/*  73 */       int classSum = this.featureSelector.getSelectedFeatureNum();
/*  74 */       DocClass cur = trainingDocSet.getDocClass(i);
/*  75 */       for (int j = 0; j < cur.getDocNum(); j++) {
/*  76 */         IRDoc curDoc = cur.getDoc(j);
/*  77 */         Row row = getRow(curDoc.getIndex());
/*  78 */         for (int k = 0; k < row.getNonZeroNum(); k++) {
/*  79 */           int newTermIndex = this.featureSelector.map(row.getNonZeroColumn(k));
/*  80 */           if (newTermIndex >= 0) {
/*  81 */             classSum = (int)(classSum + row.getNonZeroDoubleScore(k));
/*  82 */             this.model.add(i, newTermIndex, row.getNonZeroDoubleScore(k));
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*  87 */       double rate = 1.0D / classSum;
/*  88 */       for (int k = 0; k < this.model.columns(); k++)
/*  89 */         this.model.setDouble(i, k, Math.log(this.model.getDouble(i, k) * rate));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected DoubleVector getClassPrior(DocClassSet docSet)
/*     */   {
/*  97 */     int sum = docSet.getClassNum();
/*  98 */     DoubleVector vector = new DoubleVector(docSet.getClassNum());
/*  99 */     vector.assign(1.0D);
/* 100 */     for (int i = 0; i < docSet.getClassNum(); i++) {
/* 101 */       vector.set(i, docSet.getDocClass(i).getDocNum());
/* 102 */       sum += docSet.getDocClass(i).getDocNum();
/*     */     }
/* 104 */     for (int i = 0; i < docSet.getClassNum(); i++)
/* 105 */       vector.set(i, Math.log(vector.get(i) / sum));
/* 106 */     return vector;
/*     */   }
/*     */ 
/*     */   public int classify(IRDoc doc)
/*     */   {
/* 112 */     int label = classify(getRow(doc.getIndex()));
/* 113 */     doc.setWeight(this.lastClassProb.get(label));
/* 114 */     return label;
/*     */   }
/*     */ 
/*     */   public int classify(Row doc)
/*     */   {
/* 121 */     this.lastClassProb = this.classPrior.copy();
/* 122 */     int classNum = this.model.rows();
/* 123 */     for (int k = 0; k < doc.getNonZeroNum(); k++) {
/* 124 */       int newTermIndex = this.featureSelector.map(doc.getNonZeroColumn(k));
/* 125 */       if (newTermIndex >= 0) {
/* 126 */         for (int j = 0; j < classNum; j++)
/* 127 */           this.lastClassProb.add(j, doc.getNonZeroDoubleScore(k) * this.model.getDouble(j, newTermIndex));
/*     */       }
/*     */     }
/* 130 */     this.rank = this.lastClassProb.rank(true);
/* 131 */     return this.rank[0];
/*     */   }
/*     */ 
/*     */   public int[] rank() {
/* 135 */     return this.rank;
/*     */   }
/*     */ 
/*     */   public void saveModel(String modelFile)
/*     */   {
/*     */     try
/*     */     {
/* 143 */       ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(modelFile));
/* 144 */       out.writeObject(this.model);
/* 145 */       out.writeObject(this.classPrior);
/* 146 */       out.writeObject(this.featureSelector);
/* 147 */       for (int i = 0; i < this.model.rows(); i++)
/* 148 */         out.writeObject(getClassLabel(i));
/* 149 */       out.flush();
/* 150 */       out.close();
/*     */     }
/*     */     catch (Exception e) {
/* 153 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.NBClassifier
 * JD-Core Version:    0.6.2
 */