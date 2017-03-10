/*     */ package edu.drexel.cis.dragon.ir.classification.featureselection;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.classification.DocClass;
/*     */ import edu.drexel.cis.dragon.ir.classification.DocClassSet;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class InfoGainFeatureSelector extends AbstractFeatureSelector
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private double topPercentage;
/*     */ 
/*     */   public InfoGainFeatureSelector(double topPercentage)
/*     */   {
/*  29 */     this.topPercentage = topPercentage;
/*     */   }
/*     */ 
/*     */   protected int[] getSelectedFeatures(SparseMatrix doctermMatrix, DocClassSet trainingSet) {
/*  33 */     System.out.println("InfoGainSelector does not accept SparseMatrix as input. Please use IndexReader as input instead.");
/*  34 */     return null;
/*     */   }
/*     */ 
/*     */   protected int[] getSelectedFeatures(IndexReader indexReader, DocClassSet trainingSet)
/*     */   {
/*  42 */     SortedArray list = computeTermIG(indexReader, trainingSet);
/*  43 */     int termNum = (int)(this.topPercentage * indexReader.getCollection().getTermNum());
/*  44 */     termNum = Math.min(list.size(), termNum);
/*  45 */     SortedArray selectedList = new SortedArray(termNum, new IndexComparator());
/*  46 */     for (int i = 0; i < termNum; i++) {
/*  47 */       selectedList.add(list.get(i));
/*     */     }
/*  49 */     int[] featureMap = new int[selectedList.size()];
/*  50 */     for (int i = 0; i < featureMap.length; i++)
/*  51 */       featureMap[i] = ((Token)selectedList.get(i)).getIndex();
/*  52 */     return featureMap;
/*     */   }
/*     */ 
/*     */   private SortedArray computeTermIG(IndexReader indexReader, DocClassSet trainingSet)
/*     */   {
/*  64 */     int trainingDocNum = 0;
/*  65 */     for (int i = 0; i < trainingSet.getClassNum(); i++) {
/*  66 */       trainingDocNum += trainingSet.getDocClass(i).getDocNum();
/*     */     }
/*  68 */     DoubleVector classPrior = getClassPrior(trainingSet);
/*  69 */     double classEntropy = calEntropy(classPrior);
/*     */ 
/*  71 */     DoubleVector classVector = classPrior.copy();
/*  72 */     classVector.multiply(trainingDocNum);
/*     */ 
/*  74 */     int[] arrDoc = new int[indexReader.getCollection().getDocNum()];
/*  75 */     MathUtil.initArray(arrDoc, -1);
/*  76 */     for (int i = 0; i < trainingSet.getClassNum(); i++) {
/*  77 */       DocClass docClass = trainingSet.getDocClass(i);
/*  78 */       for (int j = 0; j < docClass.getDocNum(); j++) {
/*  79 */         arrDoc[docClass.getDoc(j).getIndex()] = i;
/*     */       }
/*     */     }
/*  82 */     int termNum = indexReader.getCollection().getTermNum();
/*  83 */     SortedArray list = new SortedArray(termNum, new IndexComparator());
/*  84 */     DoubleVector termVector = new DoubleVector(termNum);
/*  85 */     DoubleVector classDistrWiTerm = new DoubleVector(classPrior.size());
/*  86 */     DoubleVector classDistrWoTerm = new DoubleVector(classPrior.size());
/*  87 */     for (int i = 0; i < termNum; i++) {
/*  88 */       int[] arrDocIndex = indexReader.getTermDocIndexList(i);
/*  89 */       if ((arrDocIndex != null) && (arrDocIndex.length != 0))
/*     */       {
/*  91 */         classDistrWiTerm.assign(0.0D);
/*  92 */         classDistrWoTerm.assign(classVector);
/*  93 */         int docCount = 0;
/*  94 */         for (int j = 0; j < arrDocIndex.length; j++) {
/*  95 */           int docLabel = arrDoc[arrDocIndex[j]];
/*  96 */           if (docLabel >= 0)
/*     */           {
/*  98 */             classDistrWiTerm.add(docLabel, 1.0D);
/*  99 */             classDistrWoTerm.add(docLabel, -1.0D);
/* 100 */             docCount++;
/*     */           }
/*     */         }
/* 103 */         if (docCount != 0)
/*     */         {
/* 106 */           classDistrWiTerm.multiply(1.0D / docCount);
/* 107 */           classDistrWoTerm.multiply(1.0D / (trainingDocNum - docCount));
/* 108 */           termVector.set(i, classEntropy - calEntropy(classDistrWiTerm) - calEntropy(classDistrWoTerm));
/*     */         }
/*     */       }
/*     */     }
/* 111 */     for (int i = 0; i < termVector.size(); i++) {
/* 112 */       Token curTerm = new Token(i, 0);
/* 113 */       if (termVector.get(i) > 0.0D)
/*     */       {
/* 115 */         curTerm.setWeight(termVector.get(i));
/* 116 */         list.add(curTerm);
/*     */       }
/*     */     }
/* 118 */     list.setComparator(new WeightComparator(true));
/* 119 */     return list;
/*     */   }
/*     */ 
/*     */   private double calEntropy(DoubleVector probVector)
/*     */   {
/* 126 */     double sum = 0.0D;
/* 127 */     for (int i = 0; i < probVector.size(); i++) {
/* 128 */       if (probVector.get(i) == 0.0D)
/* 129 */         sum -= 4.9E-324D * Math.log(4.9E-324D);
/*     */       else
/* 131 */         sum -= probVector.get(i) * Math.log(probVector.get(i));
/*     */     }
/* 133 */     return sum;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.featureselection.InfoGainFeatureSelector
 * JD-Core Version:    0.6.2
 */