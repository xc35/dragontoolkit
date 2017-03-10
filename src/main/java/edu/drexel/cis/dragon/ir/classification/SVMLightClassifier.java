/*     */ package edu.drexel.cis.dragon.ir.classification;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.FeatureSelector;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.CodeMatrix;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.HingeLoss;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.LossMultiClassDecoder;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.MultiClassDecoder;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.OVACodeMatrix;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.DenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.Row;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.ArrayList;
/*     */ import jnisvmlight.KernelParam;
/*     */ import jnisvmlight.LabeledFeatureVector;
/*     */ import jnisvmlight.LearnParam;
/*     */ import jnisvmlight.SVMLightInterface;
/*     */ import jnisvmlight.SVMLightModel;
/*     */ import jnisvmlight.TrainingParameters;
/*     */ 
/*     */ public class SVMLightClassifier extends AbstractClassifier
/*     */ {
/*     */   private SVMLightModel[] arrModel;
/*     */   private LearnParam learnParam;
/*     */   private KernelParam kernelParam;
/*     */   private CodeMatrix codeMatrix;
/*     */   private MultiClassDecoder classDecoder;
/*     */   private double[] arrConfidence;
/*     */   private boolean scale;
/*     */ 
/*     */   public SVMLightClassifier(String modelFile)
/*     */   {
/*     */     try
/*     */     {
/*  34 */       ObjectInputStream oin = new ObjectInputStream(new FileInputStream(modelFile));
/*  35 */       this.arrModel = new SVMLightModel[oin.readInt()];
/*  36 */       for (int i = 0; i < this.arrModel.length; i++)
/*  37 */         this.arrModel[i] = ((SVMLightModel)oin.readObject());
/*  38 */       this.codeMatrix = ((CodeMatrix)oin.readObject());
/*  39 */       this.classDecoder = ((MultiClassDecoder)oin.readObject());
/*  40 */       this.classNum = oin.readInt();
/*  41 */       this.scale = oin.readBoolean();
/*  42 */       this.featureSelector = ((FeatureSelector)oin.readObject());
/*  43 */       this.arrLabel = new String[this.classNum];
/*  44 */       for (int i = 0; i < this.arrLabel.length; i++)
/*  45 */         this.arrLabel[i] = ((String)oin.readObject());
/*     */     }
/*     */     catch (Exception e) {
/*  48 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public SVMLightClassifier(IndexReader indexReader) {
/*  53 */     super(indexReader);
/*  54 */     this.learnParam = new LearnParam();
/*  55 */     this.kernelParam = new KernelParam();
/*  56 */     this.classDecoder = new LossMultiClassDecoder(new HingeLoss());
/*  57 */     this.codeMatrix = new OVACodeMatrix(1);
/*  58 */     this.classNum = 0;
/*  59 */     this.scale = false;
/*     */   }
/*     */ 
/*     */   public SVMLightClassifier(SparseMatrix doctermMatrix) {
/*  63 */     super(doctermMatrix);
/*  64 */     this.learnParam = new LearnParam();
/*  65 */     this.kernelParam = new KernelParam();
/*  66 */     this.classDecoder = new LossMultiClassDecoder(new HingeLoss());
/*  67 */     this.codeMatrix = new OVACodeMatrix(1);
/*  68 */     this.classNum = 0;
/*  69 */     this.scale = false;
/*     */   }
/*     */ 
/*     */   public SVMLightClassifier(DenseMatrix doctermMatrix) {
/*  73 */     super(doctermMatrix);
/*  74 */     this.learnParam = new LearnParam();
/*  75 */     this.kernelParam = new KernelParam();
/*  76 */     this.classDecoder = new LossMultiClassDecoder(new HingeLoss());
/*  77 */     this.codeMatrix = new OVACodeMatrix(1);
/*  78 */     this.classNum = 0;
/*  79 */     this.scale = false;
/*     */   }
/*     */ 
/*     */   public void setUseLinearKernel() {
/*  83 */     this.kernelParam.kernel_type = 0L;
/*     */   }
/*     */ 
/*     */   public void setUseRBFKernel() {
/*  87 */     this.kernelParam.kernel_type = 2L;
/*     */   }
/*     */ 
/*     */   public void setUsePolynomialKernel() {
/*  91 */     this.kernelParam.kernel_type = 1L;
/*     */   }
/*     */ 
/*     */   public void setUserSigmoidKernel() {
/*  95 */     this.kernelParam.kernel_type = 3L;
/*     */   }
/*     */ 
/*     */   public void setScalingOption(boolean option)
/*     */   {
/* 103 */     this.scale = option;
/*     */   }
/*     */ 
/*     */   public void setCodeMatrix(CodeMatrix matrix)
/*     */   {
/* 111 */     this.codeMatrix = matrix;
/*     */   }
/*     */ 
/*     */   public void setMultiClassDecoder(MultiClassDecoder decoder)
/*     */   {
/* 119 */     this.classDecoder = decoder;
/*     */   }
/*     */ 
/*     */   public int[] rank() {
/* 123 */     return this.classDecoder.rank();
/*     */   }
/*     */ 
/*     */   public void train(DocClassSet trainingDocSet)
/*     */   {
/* 133 */     if ((this.indexReader == null) && (this.sparseMatrix == null) && (this.denseMatrix == null)) {
/* 134 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 138 */       trainFeatureSelector(trainingDocSet);
/* 139 */       this.arrLabel = new String[trainingDocSet.getClassNum()];
/* 140 */       for (int i = 0; i < trainingDocSet.getClassNum(); i++)
/* 141 */         this.arrLabel[i] = trainingDocSet.getDocClass(i).getClassName();
/* 142 */       this.classNum = trainingDocSet.getClassNum();
/* 143 */       this.codeMatrix.setClassNum(this.classNum);
/* 144 */       ArrayList[] arrClass = new ArrayList[this.classNum];
/* 145 */       TrainingParameters param = new TrainingParameters(this.learnParam, this.kernelParam);
/* 146 */       SVMLightInterface svm = new SVMLightInterface();
/* 147 */       this.arrModel = new SVMLightModel[this.codeMatrix.getClassifierNum()];
/* 148 */       for (int i = 0; i < this.classNum; i++) {
/* 149 */         arrClass[i] = loadData(trainingDocSet.getDocClass(i));
/*     */       }
/* 151 */       for (int i = 0; i < this.codeMatrix.getClassifierNum(); i++) {
/* 152 */         LabeledFeatureVector[] arrDoc = loadData(arrClass, this.codeMatrix, i);
/*     */         int posNum;
/* 153 */         int negNum = posNum = 0;
/* 154 */         for (int j = 0; j < arrDoc.length; j++) {
/* 155 */           if (arrDoc[j].getLabel() > 0.0D)
/* 156 */             posNum++;
/*     */           else {
/* 158 */             negNum++;
/*     */           }
/*     */         }
/* 161 */         param.getLearningParameters().svm_costratio = 1.0D;
/* 162 */         this.arrModel[i] = svm.trainModel(arrDoc, param);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 166 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int classify(Row doc)
/*     */   {
/* 174 */     if (this.arrModel == null)
/* 175 */       return -1;
/* 176 */     LabeledFeatureVector example = loadData(doc);
/* 177 */     if (example == null)
/* 178 */       return -1;
/* 179 */     if ((this.arrConfidence == null) || (this.arrConfidence.length != this.codeMatrix.getClassifierNum()))
/* 180 */       this.arrConfidence = new double[this.codeMatrix.getClassifierNum()];
/* 181 */     for (int j = 0; j < this.codeMatrix.getClassifierNum(); j++)
/* 182 */       this.arrConfidence[j] = this.arrModel[j].classify(example);
/* 183 */     return this.classDecoder.decode(this.codeMatrix, this.arrConfidence);
/*     */   }
/*     */ 
/*     */   public double[] getBinaryClassifierConfidence() {
/* 187 */     return this.arrConfidence;
/*     */   }
/*     */ 
/*     */   public void saveModel(String modelFile)
/*     */   {
/*     */     try
/*     */     {
/* 195 */       if (this.arrModel == null)
/* 196 */         return;
/* 197 */       ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(modelFile));
/* 198 */       out.writeInt(this.arrModel.length);
/* 199 */       for (int i = 0; i < this.arrModel.length; i++) {
/* 200 */         this.arrModel[i].removeTrainingData();
/* 201 */         out.writeObject(this.arrModel[i]);
/*     */       }
/* 203 */       out.writeObject(this.codeMatrix);
/* 204 */       out.writeObject(this.classDecoder);
/* 205 */       out.writeInt(this.classNum);
/* 206 */       out.writeBoolean(this.scale);
/* 207 */       out.writeObject(this.featureSelector);
/* 208 */       for (int i = 0; i < this.classNum; i++)
/* 209 */         out.writeObject(getClassLabel(i));
/* 210 */       out.flush();
/* 211 */       out.close();
/*     */     }
/*     */     catch (Exception e) {
/* 214 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private LabeledFeatureVector[] loadData(ArrayList[] arrClass, CodeMatrix matrix, int classifierIndex)
/*     */   {
/* 223 */     ArrayList list = new ArrayList();
/* 224 */     for (int i = 0; i < this.classNum; i++) {
/* 225 */       int label = this.codeMatrix.getCode(i, classifierIndex);
/* 226 */       if (label != 0)
/*     */       {
/* 229 */         for (int j = 0; j < arrClass[i].size(); j++) {
/* 230 */           LabeledFeatureVector curDoc = (LabeledFeatureVector)arrClass[i].get(j);
/* 231 */           curDoc.setLabel(label);
/* 232 */           list.add(curDoc);
/*     */         }
/*     */       }
/*     */     }
/* 236 */     LabeledFeatureVector[] all = new LabeledFeatureVector[list.size()];
/* 237 */     for (int j = 0; j < list.size(); j++) {
/* 238 */       all[j] = ((LabeledFeatureVector)list.get(j));
/*     */     }
/* 240 */     list.clear();
/* 241 */     return all;
/*     */   }
/*     */ 
/*     */   private ArrayList loadData(DocClass docs)
/*     */   {
/* 249 */     ArrayList list = new ArrayList(docs.getDocNum());
/* 250 */     for (int i = 0; i < docs.getDocNum(); i++) {
/* 251 */       LabeledFeatureVector curDoc = loadData(getRow(docs.getDoc(i).getIndex()));
/* 252 */       if (curDoc != null) {
/* 253 */         list.add(curDoc);
/*     */       }
/*     */     }
/* 256 */     return list;
/*     */   }
/*     */ 
/*     */   protected LabeledFeatureVector loadData(Row doc)
/*     */   {
/* 265 */     if (doc == null) {
/* 266 */       return null;
/*     */     }
/* 268 */     int num = 0;
/* 269 */     for (int j = 0; j < doc.getNonZeroNum(); j++) {
/* 270 */       if (this.featureSelector.map(doc.getNonZeroColumn(j)) >= 0) {
/* 271 */         num++;
/*     */       }
/*     */     }
/* 274 */     if (num == 0) {
/* 275 */       return null;
/*     */     }
/*     */ 
/* 278 */     int[] ids = new int[num];
/* 279 */     double[] values = new double[num];
/* 280 */     num = 0;
/* 281 */     for (int j = 0; j < doc.getNonZeroNum(); j++) {
/* 282 */       int newIndex = this.featureSelector.map(doc.getNonZeroColumn(j));
/* 283 */       if (newIndex >= 0) {
/* 284 */         ids[num] = (newIndex + 1);
/* 285 */         values[num] = doc.getNonZeroDoubleScore(j);
/* 286 */         num++;
/*     */       }
/*     */     }
/*     */ 
/* 290 */     if (this.scale) {
/* 291 */       double sum = 0.0D;
/* 292 */       for (int j = 0; j < num; j++) {
/* 293 */         sum += values[j] * values[j];
/* 294 */         sum = Math.sqrt(sum);
/* 295 */         for (j = 0; j < num; j++) {
/* 296 */           values[j] /= sum;
/*     */         }
/*     */       }
/*     */     }
/* 300 */     return new LabeledFeatureVector(1.0D, ids, values);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.SVMLightClassifier
 * JD-Core Version:    0.6.2
 */