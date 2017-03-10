/*     */ package edu.drexel.cis.dragon.ir.classification.featureselection;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.classification.DocClass;
/*     */ import edu.drexel.cis.dragon.ir.classification.DocClassSet;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.IntDenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class MutualInfoFeatureSelector extends AbstractFeatureSelector
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private double topPercentage;
/*     */   private boolean avgMode;
/*     */ 
/*     */   public MutualInfoFeatureSelector(double topPercentage, boolean avgMode)
/*     */   {
/*  29 */     this.topPercentage = topPercentage;
/*  30 */     this.avgMode = avgMode;
/*     */   }
/*     */ 
/*     */   protected int[] getSelectedFeatures(IndexReader indexReader, DocClassSet trainingSet)
/*     */   {
/*  39 */     DoubleVector classPrior = getClassPrior(trainingSet);
/*  40 */     int docNum = 0;
/*  41 */     for (int i = 0; i < trainingSet.getClassNum(); i++)
/*  42 */       docNum += trainingSet.getDocClass(i).getDocNum();
/*  43 */     SortedArray list = computeTermMI(getTermDistribution(indexReader, trainingSet), classPrior, docNum);
/*  44 */     int termNum = (int)(this.topPercentage * indexReader.getCollection().getTermNum());
/*  45 */     termNum = Math.min(list.size(), termNum);
/*  46 */     SortedArray selectedList = new SortedArray(termNum, new IndexComparator());
/*  47 */     for (int i = 0; i < termNum; i++) {
/*  48 */       selectedList.add(list.get(i));
/*     */     }
/*  50 */     int[] featureMap = new int[selectedList.size()];
/*  51 */     for (int i = 0; i < featureMap.length; i++)
/*  52 */       featureMap[i] = ((Token)selectedList.get(i)).getIndex();
/*  53 */     return featureMap;
/*     */   }
/*     */ 
/*     */   protected int[] getSelectedFeatures(SparseMatrix doctermMatrix, DocClassSet trainingSet)
/*     */   {
/*  62 */     DoubleVector classPrior = getClassPrior(trainingSet);
/*  63 */     int docNum = 0;
/*  64 */     for (int i = 0; i < trainingSet.getClassNum(); i++)
/*  65 */       docNum += trainingSet.getDocClass(i).getDocNum();
/*  66 */     SortedArray list = computeTermMI(getTermDistribution(doctermMatrix, trainingSet), classPrior, docNum);
/*  67 */     int termNum = (int)(this.topPercentage * doctermMatrix.columns());
/*  68 */     termNum = Math.min(list.size(), termNum);
/*  69 */     SortedArray selectedList = new SortedArray(termNum, new IndexComparator());
/*  70 */     for (int i = 0; i < termNum; i++) {
/*  71 */       selectedList.add(list.get(i));
/*     */     }
/*  73 */     int[] featureMap = new int[selectedList.size()];
/*  74 */     for (int i = 0; i < featureMap.length; i++)
/*  75 */       featureMap[i] = ((Token)selectedList.get(i)).getIndex();
/*  76 */     return featureMap;
/*     */   }
/*     */ 
/*     */   private SortedArray computeTermMI(IntDenseMatrix termDistri, DoubleVector classPrior, int docNum)
/*     */   {
/*  86 */     DoubleVector classVector = classPrior.copy();
/*  87 */     classVector.multiply(docNum);
/*     */ 
/*  89 */     DoubleVector termVector = new DoubleVector(termDistri.columns());
/*  90 */     for (int i = 0; i < termDistri.columns(); i++) {
/*  91 */       termVector.set(i, termDistri.getColumnSum(i));
/*     */     }
/*  93 */     double total = docNum;
/*  94 */     DoubleVector chiVector = new DoubleVector(classVector.size());
/*  95 */     SortedArray list = new SortedArray(termVector.size(), new IndexComparator());
/*  96 */     for (int i = 0; i < termVector.size(); i++)
/*  97 */       if (termVector.get(i) > 0.0D)
/*     */       {
/*  99 */         for (int j = 0; j < classVector.size(); j++) {
/* 100 */           chiVector.set(j, calMutualInformation(termDistri.getInt(j, i), classVector.get(j), termVector.get(i), total));
/*     */         }
/* 102 */         Token curTerm = new Token(i, 0);
/* 103 */         if (this.avgMode)
/* 104 */           curTerm.setWeight(chiVector.dotProduct(classPrior));
/*     */         else
/* 106 */           curTerm.setWeight(chiVector.getMaxValue());
/* 107 */         list.add(curTerm);
/*     */       }
/* 109 */     list.setComparator(new WeightComparator(true));
/* 110 */     return list;
/*     */   }
/*     */ 
/*     */   private double calMutualInformation(double t1t2occur, double t1sum, double t2sum, double total) {
/* 114 */     if ((t1t2occur == 0.0D) || (t1sum == 0.0D) || (t2sum == 0.0D))
/* 115 */       return 0.0D;
/* 116 */     return Math.log(t1t2occur * total / (t1sum * t2sum));
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.featureselection.MutualInfoFeatureSelector
 * JD-Core Version:    0.6.2
 */